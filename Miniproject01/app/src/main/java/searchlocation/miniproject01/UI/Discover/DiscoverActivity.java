package searchlocation.miniproject01.UI.Discover;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import searchlocation.miniproject01.Models.Plan;
import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Reader.Article_Base;
import searchlocation.miniproject01.UI.Utilis.BottomNavigationViewHelper;
import searchlocation.miniproject01.UI.Utilis.PlanAdapter;

public class DiscoverActivity extends AppCompatActivity implements PlanAdapter.OnBottomReachedListener, PlanAdapter.PlanAdapterOnClickHandler {


    private RecyclerView listOfPlans;
    private PlanAdapter mAdapter;
    ArrayList<Plan> planList = new ArrayList<>();
    private static int NUM_LIST_ITEMS = 10;
    private ProgressBar mLoadingIndicator;
    private TextView noPlanTextView;
    private TextView noConnectionTextView;
    private SwipeRefreshLayout refreshLayout;
    private AHBottomNavigation bottomNavigation;
    private AutoCompleteTextView searchTextView;
    private String textSearch;
    private TextView noSearchTextView;
    private LinearLayout linearLayout;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId){
            case R.id.action_refresh:
                init();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
        searchTextView = findViewById(R.id.input_search);
        linearLayout = findViewById(R.id.relLayout1);
        noConnectionTextView = findViewById(R.id.tv_no_connection);
        noSearchTextView = findViewById(R.id.tv_no_search);
        refreshLayout = findViewById(R.id.swipe_refresh);
        listOfPlans = findViewById(R.id.list_plans);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        noPlanTextView = findViewById(R.id.tv_no_plan);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPlan();
            }
        });
        init();
        setupBottomNavigationView();
        getTextSearch();
    }

    private void getTextSearch() {
        searchTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i== KeyEvent.KEYCODE_ENTER && keyEvent.getAction()==KeyEvent.ACTION_DOWN){
                    InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if(inputMethodManager!=null && getCurrentFocus()!=null)
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                    new LoadingSearchPlanInitially().execute();
                }
                return false;
            }
        });
        searchTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if(inputMethodManager!=null && getCurrentFocus()!=null)
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                    new LoadingSearchPlanInitially().execute();
                }
                return false;
            }
        });
    }


    private class LoadingSearchPlanInitially extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
            listOfPlans.setVisibility(View.INVISIBLE);
            searchTextView.setVisibility(View.INVISIBLE);
            noSearchTextView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            textSearch = searchTextView.getText().toString();
            planList.clear();
            Log.i("Text search ",textSearch);
            if (textSearch != null){
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Plan");
                String pattern = "^.*" + textSearch + ".*$";
                query.whereMatches("title", textSearch, "i");
                query.orderByAscending("createdAt");
                query.setLimit(10);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(final List<ParseObject> objects, ParseException e) {
                        if (e == null && objects.size() > 0) {
                            for (final ParseObject object : objects) {
                                final Plan plan = new Plan();
                                //load image to view
                                ParseFile image = (ParseFile) object.get("image");
                                if (image != null) {
                                    image.getDataInBackground(new GetDataCallback() {
                                        @Override
                                        public void done(byte[] data, ParseException e) {
                                            if (e == null && data != null) {
                                                Log.i("Get image", "successful");
                                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                plan.setImage(bitmap);
                                                plan.setTitle(object.getString("title"));
                                                plan.setDesc(object.getString("description"));
                                                plan.setTags(object.getString("hashtag"));
                                                plan.setObjectId(object.getObjectId());
                                                //finally,add to planlist
                                                planList.add(plan);
                                                SharedPreferences preferences = DiscoverActivity.this.getSharedPreferences("SharedPref", 0);
                                                Date date = new Date(preferences.getLong("createdAt", 0));
                                                if (object.getCreatedAt().after(date)) {
                                                    preferences.edit().putLong("createdAt", object.getCreatedAt().getTime()).apply();
                                                }
                                                if (planList.size() == objects.size()) {
                                                    NUM_LIST_ITEMS = objects.size();
                                                    mAdapter.setmNumberItems(NUM_LIST_ITEMS);
                                                    mAdapter.setListOfPlans(planList);
                                                    mAdapter.notifyDataSetChanged();
                                                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                                                    refreshLayout.setVisibility(View.VISIBLE);
                                                    listOfPlans.setVisibility(View.VISIBLE);
                                                }
                                            } else {
                                                Log.i("Get image", "failed");
                                            }
                                        }
                                    });
                                }
                            }
                        } else {
                            Log.i("Could", "not load object");
                            mLoadingIndicator.setVisibility(View.INVISIBLE);
                            noSearchTextView.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            searchTextView.setVisibility(View.VISIBLE);
        }
    }
    private void refreshPlan() {
        noConnectionTextView.setVisibility(View.INVISIBLE);
        init();
        refreshLayout.setRefreshing(false);
    }

    private void init() {
        if(!isNetworkConnected()){
            Log.i("Connection ","failed");
            noConnectionTextView.setVisibility(View.VISIBLE);
            listOfPlans.setVisibility(View.INVISIBLE);
        }else {
            if(mAdapter == null) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                listOfPlans.setLayoutManager(layoutManager);
                //listOfPlans.setHasFixedSize(true);
                mAdapter = new PlanAdapter(NUM_LIST_ITEMS, planList, this, this);
                listOfPlans.setAdapter(mAdapter);

                //load shared plan initially
                new LoadingSharePlanInitially().execute();
            }else{
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                listOfPlans.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setupBottomNavigationView() {
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottomNavigation);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigation);
        BottomNavigationViewHelper.enableBottomNavigation(DiscoverActivity.this,bottomNavigation);
    }

    @Override
    public void onBottomReached(int position) {
        Log.i("Bottom reached: ", Integer.toString(position));
        new LoadPlan().execute(position);
    }

    @Override
    public void onClick(Plan itemPlan) {
        Log.i("Item click", itemPlan.toString());
        Intent intent = new Intent(this, Article_Base.class);
        intent.putExtra("objectId", itemPlan.getObjectId());
        startActivity(intent);
    }

    private class LoadingSharePlanInitially extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParseQuery<ParseObject> query = new ParseQuery<>("Plan");
            String username = ParseUser.getCurrentUser().getObjectId();
            if (username != null) {
                Log.i("username ", username);
                query.whereEqualTo("userId", username);
                query.orderByAscending("createdAt");
                query.setLimit(10);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(final List<ParseObject> objects, ParseException e) {
                        if (e == null && objects.size() > 0) {
                            for (final ParseObject object : objects) {
                                final Plan plan = new Plan();
                                //load image to view
                                ParseFile image = (ParseFile) object.get("image");
                                if (image != null) {
                                    image.getDataInBackground(new GetDataCallback() {
                                        @Override
                                        public void done(byte[] data, ParseException e) {
                                            if (e == null && data != null) {
                                                Log.i("Get image", "successful");
                                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                plan.setImage(bitmap);
                                                plan.setTitle(object.getString("title"));
                                                plan.setDesc(object.getString("description"));
                                                plan.setTags(object.getString("hashtag"));
                                                plan.setObjectId(object.getObjectId());
                                                //finally,add to planlist
                                                planList.add(plan);
                                                SharedPreferences preferences = DiscoverActivity.this.getSharedPreferences("SharedPref", 0);
                                                Date date = new Date(preferences.getLong("createdAt", 0));
                                                if (object.getCreatedAt().after(date)) {
                                                    preferences.edit().putLong("createdAt", object.getCreatedAt().getTime()).apply();
                                                }
                                                if (planList.size() == objects.size()) {
                                                    NUM_LIST_ITEMS = objects.size();
                                                    mAdapter.setmNumberItems(NUM_LIST_ITEMS);
                                                    mAdapter.setListOfPlans(planList);
                                                    mAdapter.notifyDataSetChanged();
                                                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                                                    refreshLayout.setVisibility(View.VISIBLE);
                                                    listOfPlans.setVisibility(View.VISIBLE);
                                                }
                                            } else {
                                                Log.i("Get image", "failed");
                                            }
                                        }
                                    });
                                }
                            }
                        } else {
                            Log.i("Could", "not load object");
                            mLoadingIndicator.setVisibility(View.INVISIBLE);
                            noPlanTextView.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
            return null;
        }

    }

    private class LoadPlan extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(final Integer... pos) {
            final SharedPreferences preferences = DiscoverActivity.this.getSharedPreferences("SharedPref", 0);
            Date date = new Date(preferences.getLong("createdAt", 0));
            if (date.getTime() != 0) {
                ParseQuery<ParseObject> query = new ParseQuery<>("Plan");
                final SharedPreferences sharedPreferences = DiscoverActivity.this.getSharedPreferences("SharedPref", MODE_PRIVATE);
                String username = sharedPreferences.getString("USERNAME", null);
                query.whereEqualTo("userId", username);
                query.whereGreaterThan("createdAt", date);
                query.orderByAscending("createdAt");
                query.setLimit(10);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(final List<ParseObject> objects, ParseException e) {
                        if (e == null && objects.size() > 0) {
                            int position = pos[0];
                            for (final ParseObject object : objects) {
                                ++position;
                                final Plan plan = new Plan();
                                //load image to view
                                ParseFile image = (ParseFile) object.get("image");
                                if (image != null) {
                                    final int pos = position;
                                    image.getDataInBackground(new GetDataCallback() {
                                        @Override
                                        public void done(byte[] data, ParseException e) {
                                            if (e == null && data != null) {
                                                Log.i("Get image", "successful");
                                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                plan.setImage(bitmap);
                                                plan.setTitle(object.getString("title"));
                                                plan.setDesc(object.getString("description"));
                                                plan.setTags(object.getString("hashtag"));
                                                plan.setObjectId(object.getObjectId());
                                                preferences.edit().putLong("createdAt", object.getCreatedAt().getTime()).apply();
                                                //load more plan
                                                NUM_LIST_ITEMS++;
                                                mAdapter.setmNumberItems(NUM_LIST_ITEMS);
                                                mAdapter.addPlan(plan);
                                                mAdapter.notifyItemInserted(pos);
                                            } else {
                                                Log.i("Get image", "failed");
                                            }
                                        }
                                    });
                                }
                            }
                        } else {
                            Log.i("Object", "cannot load more");
                        }
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo()!=null;
    }
}