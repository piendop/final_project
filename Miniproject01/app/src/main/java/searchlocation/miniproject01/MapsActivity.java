package searchlocation.miniproject01;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import searchlocation.miniproject01.Utilis.BottomNavigationViewHelper;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;

    /**************GET USER LOCATION VARIABLES********************************/
    LocationManager locationManager;
    LocationListener locationListener;

    /***********************GET TEXT SEARCH VARIABLES******************************************/
    EditText inputSearch ;
    String textSearch="";
    /********************SHARE PREFERENCE********************/
    SharedPreferences sharedPreferences;
    /********PLACES: NAMES, ADDRESSES AND LOCATIONS************/
    ArrayList<String> addresses = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<LatLng> locations = new ArrayList<>();








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
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setupBottomNavigationView();
        sharedPreferences = this.getSharedPreferences("searchlocation.miniproject01",Context.MODE_PRIVATE);
        //get text search of user
        inputSearch=findViewById(R.id.input_search);
        getTextSearch();
    }


    private void getTextSearch() {

        //close virtual keyboard when click on map
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        linearLayout.setOnClickListener(this);//go to on click method
        //or when user enter notify end inputting text
        inputSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i== KeyEvent.KEYCODE_ENTER && keyEvent.getAction()==KeyEvent.ACTION_DOWN){
                    getPlacesFromTextSearch();
                }
                return false;
            }
        });

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

        //ask for permission
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{//already have a permission request location
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }
    }

    @Override
    public void onClick(View view) {
        //click to somewhere to close virtual keyboard
        if(view.getId()==R.id.linearLayout){
            //close virtual keyboard
            InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if(inputMethodManager!=null && getCurrentFocus()!=null)
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
            getPlacesFromTextSearch();
        }
    }

    private void getPlacesFromTextSearch() {
        //get text search
        //use google api
        String apiJson = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=";
        //get text search, split string and append api
        textSearch = inputSearch.getText().toString();
        String texts[]=textSearch.split(" ");

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
        apiJson+="&key="+"AIzaSyDRT8C-h9zBQRYut6OODsvbZ2kOCumQ4x0";
        Log.i("Web api",apiJson);
        //now get web content in json
        DownloadWebContent webContent =new DownloadWebContent(this);
        webContent.execute(apiJson);
        getAddresses();
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

    public void getAddresses(){

        try {
            addresses =(ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.
                    getString("addresses",ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("Addresses",addresses.toString());
    }
}
