package searchlocation.miniproject01.UI.Discover;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import searchlocation.miniproject01.Models.Plan;
import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Utilis.BottomNavigationViewHelper;
import searchlocation.miniproject01.UI.Utilis.PlanAdapter;

public class DiscoverActivity extends AppCompatActivity implements PlanAdapter.OnBottomReachedListener, PlanAdapter.PlanAdapterOnClickHandler{



	private RecyclerView listOfPlans;
	private PlanAdapter mAdapter;
    ArrayList<Plan> planList = new ArrayList<>();
    private static int NUM_LIST_ITEMS = 10;
    private ProgressBar mLoadingIndicator;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discover);
		listOfPlans = findViewById(R.id.list_plans);
		mLoadingIndicator = findViewById(R.id.pb_loading_indicator);


		SharedPreferences preferences = this.getSharedPreferences("SharedPref",MODE_PRIVATE);
		Boolean isLoaded = preferences.getBoolean("isLoaded",false);
		/*if(!isLoaded){
		    mLoadingIndicator.setVisibility(View.INVISIBLE);
		    listOfPlans.setVisibility(View.VISIBLE);
        //}else{*/
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            listOfPlans.setLayoutManager(layoutManager);
            listOfPlans.setHasFixedSize(true);
            mAdapter = new PlanAdapter(NUM_LIST_ITEMS,planList,  this, this);
            listOfPlans.setAdapter(mAdapter);

            //load shared plan initially
            new LoadingSharePlanInitially().execute();
        //}

	}

	public void setupBottomNavigationView(){
		AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottomNavigation);
		BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigation);
		BottomNavigationViewHelper.enableBottomNavigation(DiscoverActivity.this,bottomNavigation);
	}

    @Override
    public void onBottomReached(int position) {
	    Log.i("Bottom reached: ",Integer.toString(position));
        new LoadPlan().execute(position);
    }

    @Override
    public void onClick(Plan itemPlan) {
        Log.i("Item click",itemPlan.toString());
    }

    private class LoadingSharePlanInitially extends AsyncTask<Void,Void,Void>
	{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
		protected Void doInBackground(Void... voids) {
            ParseQuery<ParseObject> query = new ParseQuery<>("Plan");
            final SharedPreferences sharedPreferences = DiscoverActivity.this.getSharedPreferences("SharedPref",MODE_PRIVATE);
            String username = sharedPreferences.getString("USERNAME",null);
            if(username!=null) {
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
                                                //finally,add to planlist
                                                planList.add(plan);
                                                SharedPreferences preferences = DiscoverActivity.this.getSharedPreferences("SharedPref", 0);
                                                Date date = new Date(preferences.getLong("createdAt",0));
                                                if(object.getCreatedAt().after(date)){
                                                    preferences.edit().putLong("createdAt", object.getCreatedAt().getTime()).apply();
                                                }
                                                if (planList.size() == objects.size()) {
                                                    NUM_LIST_ITEMS = objects.size();
                                                    mAdapter.setmNumberItems(NUM_LIST_ITEMS);
                                                    mAdapter.setListOfPlans(planList);
                                                    mAdapter.notifyDataSetChanged();
                                                    mLoadingIndicator.setVisibility(View.INVISIBLE);
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
                        }
                    }
                });
            }
            return null;
		}

    }

    private class LoadPlan extends AsyncTask<Integer,Void,Void>{

        @Override
        protected Void doInBackground(final Integer... pos) {
            final SharedPreferences preferences = DiscoverActivity.this.getSharedPreferences("SharedPref",0);
            Date date = new Date(preferences.getLong("createdAt",0));
            if(date.getTime()!=0){
                ParseQuery<ParseObject> query = new ParseQuery<>("Plan");
                final SharedPreferences sharedPreferences = DiscoverActivity.this.getSharedPreferences("SharedPref",MODE_PRIVATE);
                String username = sharedPreferences.getString("USERNAME",null);
                query.whereEqualTo("userId", username);
                query.whereGreaterThan("createdAt",date);
                query.orderByAscending("createdAt");
                query.setLimit(10);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(final List<ParseObject> objects, ParseException e) {
                        if(e==null && objects.size()>0){
                            int position = pos[0];
                            for(final ParseObject object:objects){
                                ++position;
                                final Plan plan = new Plan();
                                //load image to view
                                ParseFile image = (ParseFile) object.get("image");
                                if(image!=null){
                                    final int pos = position;
                                    image.getDataInBackground(new GetDataCallback() {
                                        @Override
                                        public void done(byte[] data, ParseException e) {
                                            if(e==null&&data!=null){
                                                Log.i("Get image","successful");
                                                Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                                plan.setImage(bitmap);
                                                plan.setTitle(object.getString("title"));
                                                plan.setDesc(object.getString("description"));
                                                plan.setTags(object.getString("hashtag"));

                                                preferences.edit().putLong("createdAt",object.getCreatedAt().getTime()).apply();
                                                //load more plan
                                                NUM_LIST_ITEMS++;
                                                mAdapter.setmNumberItems(NUM_LIST_ITEMS);
                                                mAdapter.addPlan(plan);
                                                mAdapter.notifyItemInserted(pos);
                                            }else{
                                                Log.i("Get image","failed");
                                            }
                                        }
                                    });
                                }
                            }
                        }else{
                            Log.i("Object","cannot load more");
                        }
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SharedPreferences preferences = DiscoverActivity.this.getSharedPreferences("SharedPref",0);
            preferences.edit().putBoolean("isLoaded",true).apply();
        }
    }
}
