package searchlocation.miniproject01.UI.OnGoing;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

public class OnGoingActivity extends FragmentActivity implements OnGoingAdapter.OnGoingAdapterOnClickHandler, OnGoingAdapter.OnBottomReachedListener, OnMapReadyCallback {

    private AHBottomNavigation bottomNavigation;
    private static int NUM_LIST_ITEMS = 10;
    private OnGoingAdapter mAdapter;
    private ArrayList<Place> placeList = new ArrayList<>();
    private String planId;
    private RecyclerView placeRecyclerView;

    /***MAP***/
    GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 1 && grantResults.length > 0) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        0, 0, locationListener);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

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
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private static final String TAG = "OnGoingActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setupBottomNavigationView();
        placeRecyclerView = findViewById(R.id.rv_places);

        planId = getIntent().getStringExtra("currentPlan");
        if (planId != null && !planId.isEmpty()) {
            Log.i("Current plan ", planId);
            if (mAdapter == null) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                placeRecyclerView.setLayoutManager(layoutManager);
                //placeRecyclerView.setHasFixedSize(true);
                mAdapter = new OnGoingAdapter(placeList, NUM_LIST_ITEMS, this, this);
                placeRecyclerView.setAdapter(mAdapter);
                new LoadingSharePlaceInitially().execute();
            }
        }
    }

    public void setupBottomNavigationView() {
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottomNavigation);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigation);
        BottomNavigationViewHelper.enableBottomNavigation(OnGoingActivity.this, bottomNavigation);
    }

    @Override
    public void onBottomReached(int position) {
        new LoadPlace().execute(position);
    }

    @Override
    public void onClick(Place itemPlace) {
        LatLng placeLocation = new LatLng(itemPlace.getLatitude(), itemPlace.getLongitude());
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(placeLocation).title(itemPlace.getName()).
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));//add a marker and text
        //to zoom in location range 1-20 1: zoom out 20: zoom in
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation, 16f));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16f));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }

        }
        else {

            //ask for permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {//already have a permission request location
                Location location = getUserLocation();
                LatLng userLocation =  new LatLng(location.getLatitude(),location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your location").
                        icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));//add a marker and text
                //to zoom in location range 1-20 1: zoom out 20: zoom in
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,16f));
            }
        }
    }

    public Location getUserLocation() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            Location lastKnownUserLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastKnownUserLocation!=null)
                return lastKnownUserLocation;
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        locationManager.removeUpdates(locationListener);
        placeRecyclerView.removeAllViews();
        super.onDestroy();
    }

    private class LoadingSharePlaceInitially extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            placeRecyclerView.setVisibility(View.INVISIBLE);
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
                            placeRecyclerView.setVisibility(View.INVISIBLE);
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
            final SharedPreferences preferences = OnGoingActivity.this.getSharedPreferences("SharedPref", 0);
            Date date = new Date(preferences.getLong("createdPlace", 0));
            if (date.getTime() != 0) {
                ParseQuery<ParseObject> query = new ParseQuery<>("Plan");
                query.whereEqualTo("planId", planId);
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
                                mAdapter.setNumberItems(NUM_LIST_ITEMS);
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
}
