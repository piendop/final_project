package searchlocation.miniproject01.Search;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import searchlocation.miniproject01.R;
import searchlocation.miniproject01.Utilis.BottomNavigationViewHelper;
import searchlocation.miniproject01.Utilis.WebThread;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,WebThread.Callback,
        GoogleApiClient.OnConnectionFailedListener{

    GoogleMap mMap;

    /**************GET USER LOCATION VARIABLES********************************/
    LocationManager locationManager;
    LocationListener locationListener;

    /***********************GET TEXT SEARCH VARIABLES******************************************/
    AutoCompleteTextView inputSearch ;
    String textSearch="";

    /********PLACES: NAMES, ADDRESSES AND LOCATIONS************/
    static public ArrayList<String> addresses = new ArrayList<>();
    static public ArrayList<String> names = new ArrayList<>();
    static public ArrayList<LatLng> locations = new ArrayList<>();
    static public Bitmap icon;
    /********PLACE AUTOCOMPLETE ADAPTER***********/
    PlaceAutocompleteAdapter placeAutocompleteAdapter;
    GoogleApiClient mGoogleApiClient;
    //public LatLngBounds LAT_LNG_BOUNDS;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

    private WebThread webThread;


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    public LatLngBounds toBounds(LatLng center, double radiusInMeters) {
        double distanceFromCenterToCorner = radiusInMeters * Math.sqrt(2.0);
        LatLng southwestCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225.0);
        LatLng northeastCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45.0);
        return new LatLngBounds(southwestCorner, northeastCorner);
    }



        @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if(requestCode==1&& grantResults.length>0){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                    ==PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        0,0,locationListener);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setupBottomNavigationView();
        webThread = new WebThread(new Handler(),this);
        webThread.start();
        webThread.handleRequest();
        inputSearch = findViewById(R.id.input_search);

        /****autocomplete init***/
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        getTextSearch();
    }
    public void setupBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavigation);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableBottomNavigation(MapsActivity.this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //add marker to your current location

                LatLng userLocation =  new LatLng(location.getLatitude(),location.getLongitude());
                //set an icon for marker
                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your location").
                        icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));//add a marker and text
                //to zoom in location range 1-20 1: zoom out 20: zoom in
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,16f));


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if(Build.VERSION.SDK_INT<23){
            Location location = getUserLocation();
            LatLng userLocation =  new LatLng(location.getLatitude(),location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(userLocation).title("Your location").
                    icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));//add a marker and text
            //to zoom in location range 1-20 1: zoom out 20: zoom in
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,16f));
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
        //suggested place text
        Location userLocation = getUserLocation();
        if(userLocation!=null) {
            LatLngBounds latLngBounds = toBounds(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()),
                    10000);
            placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                    latLngBounds, null);
        }else{
            placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                    LAT_LNG_BOUNDS, null);
        }
        inputSearch.setAdapter(placeAutocompleteAdapter);
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


    /******SEARCHING PLACES********/

    public void getTextSearch() {
        inputSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i== KeyEvent.KEYCODE_ENTER && keyEvent.getAction()==KeyEvent.ACTION_DOWN){
                    InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if(inputMethodManager!=null && getCurrentFocus()!=null)
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                    getPlacesFromTextSearch();
                }
                return false;
            }
        });
        inputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if(inputMethodManager!=null && getCurrentFocus()!=null)
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                    getPlacesFromTextSearch();
                }
                return false;
            }
        });
    }

    public void getPlacesFromTextSearch() {

        RelativeLayout relativeLayout = findViewById(R.id.relLayout1);

        relativeLayout.setVisibility(View.INVISIBLE);

        //get text search
        //use google api
        String apiJson = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=";
        //get text search, split string and append api
        textSearch = inputSearch.getText().toString();
        String texts[]=textSearch.split(" |, ");

        //add the first word
        apiJson+=texts[0];

        for(int i=1;i<texts.length;++i){
            apiJson+="+"+texts[i];
        }
        //append user location
        Location lastKnownUserLocation = getUserLocation();
        if(lastKnownUserLocation!=null){
            //append api with location
            apiJson+="&location="+Double.toString(lastKnownUserLocation.getLatitude())+"," +
                    Double.toString(lastKnownUserLocation.getLongitude());
        }
        //finally, append key api
        apiJson+="&key="+"AIzaSyCGaXrFNr9aX6jeI-G2JnP4iE7Z5XKEw-U";
        Log.i("Web api",apiJson);
        DownloadWebContent webContent = new DownloadWebContent();
        try {
            String url = webContent.execute(apiJson).get();
            webThread.queueTask(url,0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showOnMap() {
        Long startTime = System.nanoTime();
        //markers to add marker
        ArrayList<Marker> markers = new ArrayList<>();
        //remove update to not center user location
        locationManager.removeUpdates(locationListener);
        //clear user marker
        mMap.clear();

        for (int i=0;i<locations.size();++i){
            markers.add(mMap.addMarker(new MarkerOptions().
                    position(locations.get(i)).
                    title(names.get(i)).
                    icon(BitmapDescriptorFactory.fromBitmap(icon))));
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for(Marker marker:markers){
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds=builder.build();

        int padding = 200;//offset from edges of the map in pixel
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,padding);

        mMap.animateCamera(cameraUpdate);
        Long endTime=System.nanoTime();
        Long totalTime = (endTime-startTime)/1000000;
        Log.i("Time show",Long.toString(totalTime));

    }

    @Override
    protected void onDestroy() {
        locationManager.removeUpdates(locationListener);
        webThread.quit();
        super.onDestroy();
    }
    @Override
    public void onWebContentDownloaded(){
        showOnMap();
        MapsActivity.addresses.clear();
        MapsActivity.locations.clear();
        MapsActivity.names.clear();
        MapsActivity.icon.recycle();
    }

    //class to get web content
    static public class DownloadWebContent extends AsyncTask<String,Void,String> {

        JSONObject object;
        JSONArray array;


        @Override
        protected String doInBackground(String... urls) {
            try {
                long start = System.nanoTime();
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    long end =System.nanoTime();
                    Log.i("Load web",Long.toString((end-start)/1000000000));
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Failed successfully";
            }
        }
    }

}
