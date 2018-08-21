package searchlocation.miniproject01.UI.Reader;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import searchlocation.miniproject01.Models.Place;
import searchlocation.miniproject01.Models.Place;
import searchlocation.miniproject01.Models.Plan;
import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Discover.DiscoverActivity;
import searchlocation.miniproject01.UI.OnGoing.OnGoingActivity;
import searchlocation.miniproject01.UI.Utilis.BottomNavigationReader;
import searchlocation.miniproject01.UI.Utilis.BottomNavigationViewHelper;
import searchlocation.miniproject01.UI.Utilis.PlaceItemAdapter;

public class Article_Base extends AppCompatActivity implements PlaceItemAdapter.PlaceAdapterOnClickHandler, PlaceItemAdapter.OnBottomReachedListener, AHBottomNavigation.OnTabSelectedListener {

    private ImageView imagePlace;
    private TextView title;
    private TextView description;
    private LinearLayout planInfo;
    private TextView noConnectionTextView;
    private static int NUM_LIST_ITEMS=10;
    private PlaceItemAdapter mAdapter;
    ArrayList<Place> placeList = new ArrayList<>();
    ProgressBar mLoadingIndicator;
    String objectId;
    private RecyclerView placeRecyclerView;
    private AHBottomNavigation bottomNavigation;



    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo()!=null;
    }

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

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_base);
		setupBottomNavigationReader();
		noConnectionTextView = findViewById(R.id.tv_no_connection);
        imagePlace = findViewById(R.id.headingImage);
        title = findViewById(R.id.tv_title);
        description = findViewById(R.id.tv_description);
        planInfo = findViewById(R.id.plan_info);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        objectId = getIntent().getStringExtra("objectId");
        placeRecyclerView = findViewById(R.id.list_places);
        init();
        bottomNavigation.setOnTabSelectedListener(this);
    }

    private void init() {
        if(!isNetworkConnected()){
            Log.i("Connection ","failed");
            planInfo.setVisibility(View.VISIBLE);
            noConnectionTextView.setVisibility(View.VISIBLE);
        }else {
            Log.i("Id ", objectId);

            //set image and text for place
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Plan");
            query.getInBackground(objectId, new GetCallback<ParseObject>() {
                @Override
                public void done(final ParseObject object, ParseException e) {
                    if (e == null) {
                        ParseFile file = object.getParseFile("image");
                        if (file != null) {
                            file.getDataInBackground(new GetDataCallback() {
                            @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null) {
                                        Log.i("Get image", "successful");
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        imagePlace.setImageBitmap(bitmap);
                                        title.setText(object.getString("title"));
                                        description.setText(object.getString("description"));
                                        planInfo.setVisibility(View.VISIBLE);
                                        noConnectionTextView.setVisibility(View.INVISIBLE);
                                    }else {
                                        Log.i("Get image", "failed");
                                    }
                                }
                            });
                        }
                    }
                    else {
                        Log.i("Could", "not load object");
                    }
                }
            });

            if(mAdapter==null){
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                placeRecyclerView.setLayoutManager(layoutManager);
                //placeRecyclerView.setHasFixedSize(true);
                mAdapter = new PlaceItemAdapter(NUM_LIST_ITEMS,placeList,this,this);
                placeRecyclerView.setAdapter(mAdapter);
                new LoadingSharePlaceInitially().execute();
            }else{
                placeRecyclerView.setVisibility(View.VISIBLE);
                mLoadingIndicator.setVisibility(View.INVISIBLE);
            }

        }
    }

    public void setupBottomNavigationReader(){
		bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottomNavigationReader);
		BottomNavigationReader.setupBottomNavigationView(bottomNavigation);
		BottomNavigationReader.enableBottomNavigation(Article_Base.this,bottomNavigation);
	}

    @Override
    public void onBottomReached(int position) {
        Log.i("Bottom reached ", Integer.toString(position));
        new LoadPlace().execute(position);

    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {
        switch (position) {
            case 0:

                break;
            case 1:

                break;
            case 2:

                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to add this plan as your current plan?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent onGoing = new Intent(Article_Base.this, OnGoingActivity.class); //ACTIVITY_NUMBER 1
                                onGoing.putExtra("currentPlan",objectId);
                                startActivity(onGoing);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                break;
        }
        return false;
    }


    private class LoadingSharePlaceInitially extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ParseQuery<ParseObject> query = new ParseQuery<>("LocationReview");

            if ( objectId!= null) {
                query.whereEqualTo("planId", objectId);
                query.orderByAscending("createdAt");
                query.setLimit(10);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(final List<ParseObject> objects, ParseException e) {
                        if (e == null && objects.size() > 0) {
                            for (final ParseObject object : objects) {
                                final Place place = new Place();
                                ParseGeoPoint geoPoint = object.getParseGeoPoint("location");
                                place.setLatitude(geoPoint.getLatitude());
                                place.setLongitude(geoPoint.getLongitude());
                                place.setID(object.getObjectId());
                                place.setName(object.getString("placeName"));
                                place.setReview(object.getString("review"));
                                place.setAddress(object.getString("address"));
                                placeList.add(place);
                                SharedPreferences preferences = Article_Base.this.getSharedPreferences("SharedPref", 0);
                                Date date = new Date(preferences.getLong("createdPlace", 0));
                                if (object.getCreatedAt().after(date)) {
                                    preferences.edit().putLong("createdPlace", object.getCreatedAt().getTime()).apply();
                                }
                                if(objects.size()==placeList.size()){
                                    NUM_LIST_ITEMS=objects.size();
                                    mAdapter.setmNumberItems(NUM_LIST_ITEMS);
                                    mAdapter.setListOfPlaces(placeList);
                                    mAdapter.notifyDataSetChanged();
                                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                                    placeRecyclerView.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            Log.i("Could", "not load object");
                            mLoadingIndicator.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
            return null;
        }

    }

    private class LoadPlace extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(final Integer... pos) {
            final SharedPreferences preferences = Article_Base.this.getSharedPreferences("SharedPref", 0);
            Date date = new Date(preferences.getLong("createdPlace", 0));
            if (date.getTime() != 0) {
                ParseQuery<ParseObject> query = new ParseQuery<>("Plan");
                final SharedPreferences sharedPreferences = Article_Base.this.getSharedPreferences("SharedPref", MODE_PRIVATE);
                query.whereEqualTo("planId", objectId);
                query.whereGreaterThan("createdAt", date);
                query.setLimit(10);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(final List<ParseObject> objects, ParseException e) {
                        if (e == null && objects.size() > 0) {
                            int position = pos[0];
                            for (final ParseObject object : objects) {
                                ++position;
                                final Place place = new Place();
                                ParseGeoPoint geoPoint = new ParseGeoPoint(object.getParseGeoPoint("location"));
                                place.setLatitude(geoPoint.getLatitude());
                                place.setLongitude(geoPoint.getLongitude());
                                place.setName(object.getString("placeName"));
                                place.setID(object.getObjectId());
                                place.setReview(object.getString("review"));
                                preferences.edit().putLong("createdPlace",object.getCreatedAt().getTime()).apply();
                                ++NUM_LIST_ITEMS;
                                mAdapter.setmNumberItems(NUM_LIST_ITEMS);
                                mAdapter.addPlace(place);
                                mAdapter.notifyItemInserted(position);
                            }
                        } else {
                            Log.i("Object", "cannot load more");
                        }
                    }
                });
            }
            return null;
        }
    }
	@Override
	public void onClick(String itemName) {
		Log.i("Item Name",itemName);
	}
}
