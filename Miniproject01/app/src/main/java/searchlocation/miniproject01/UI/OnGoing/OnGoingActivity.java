package searchlocation.miniproject01.UI.OnGoing;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import searchlocation.miniproject01.Models.Place;
import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Login.LoginActivity;
import searchlocation.miniproject01.UI.Utilis.BottomNavigationViewHelper;

public class OnGoingActivity extends AppCompatActivity implements OnGoingAdapter.OnGoingAdapterOnClickHandler, OnGoingAdapter.OnBottomReachedListener{

    private AHBottomNavigation bottomNavigation;
    private static int NUM_LIST_ITEMS=10;
    private OnGoingAdapter mAdapter;
    private ArrayList<Place> placeList = new ArrayList<>();
    private String planId;
    private RecyclerView placeRecyclerView;

    @Override
    public void onBackPressed() {
        /*alert whether user want to log out*/
        /***Set up an alert***/
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure?")
                .setMessage("Do you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

	private static final String TAG = "OnGoingActivity";

	private static final int ERROR_DIALOG_REQUEST=9001;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ongoing);
		setupBottomNavigationView();
		placeRecyclerView = findViewById(R.id.rv_places);

		planId = getIntent().getStringExtra("currentPlan");
		if(planId!=null && !planId.isEmpty()){
		    Log.i("Current plan ",planId);
            if(mAdapter==null){
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                placeRecyclerView.setLayoutManager(layoutManager);
                //placeRecyclerView.setHasFixedSize(true);
                mAdapter = new OnGoingAdapter(placeList,NUM_LIST_ITEMS,this,this);
                placeRecyclerView.setAdapter(mAdapter);
                new LoadingSharePlaceInitially().execute();
            }
        }
	}
	public void setupBottomNavigationView(){
		bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottomNavigation);
		BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigation);
		BottomNavigationViewHelper.enableBottomNavigation(OnGoingActivity.this,bottomNavigation);
	}

    @Override
    public void onBottomReached(int position) {

    }

    @Override
    public void onClick(Place itemPlace) {

    }

    private class LoadingSharePlaceInitially extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ParseQuery<ParseObject> query = new ParseQuery<>("LocationReview");

            if ( planId!= null) {
                query.whereEqualTo("planId", planId);
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
                                SharedPreferences preferences = OnGoingActivity.this.getSharedPreferences("SharedPref", 0);
                                Date date = new Date(preferences.getLong("createdPlace", 0));
                                if (object.getCreatedAt().after(date)) {
                                    preferences.edit().putLong("createdPlace", object.getCreatedAt().getTime()).apply();
                                }
                                if(objects.size()==placeList.size()){
                                    NUM_LIST_ITEMS=objects.size();
                                    mAdapter.setNumberItems(NUM_LIST_ITEMS);
                                    mAdapter.setListOfPlaces(placeList);
                                    mAdapter.notifyDataSetChanged();
                                    Log.i("Item ", "successful");
                                    //mLoadingIndicator.setVisibility(View.INVISIBLE);
                                    placeRecyclerView.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            Log.i("Could", "not load object");
                            //mLoadingIndicator.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
            return null;
        }

    }
}
