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
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import searchlocation.miniproject01.Utilis.BottomNavigationViewHelper;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    GoogleMap mMap;

    /**************GET USER LOCATION VARIABLES********************************/
    LocationManager locationManager;
    LocationListener locationListener;

    /***********************GET TEXT SEARCH VARIABLES******************************************/
    EditText inputSearch ;
    String textSearch="";

    /********PLACES: NAMES, ADDRESSES AND LOCATIONS************/
    ArrayList<String> addresses = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<LatLng> locations = new ArrayList<>();
    ArrayList<String> icons = new ArrayList<>();




    /********************************************************************************************/
    //class to get web content
    public class DownloadWebContent extends AsyncTask<String,Void,String> {

        JSONObject object;
        JSONArray array;
        int size=10;


        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);//urls[0] can be empty ==> need catch and try
                //we might not open the connection ==> need catch and try
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();
                String result = "";
                while (data != -1) {
                    result += (char) data;
                    data = reader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return "Failed successfully";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                object=new JSONObject(result);
                String places = object.getString("results");
                array = new JSONArray(places);


                if(array.length()<size) size=array.length();
                //save top 10 places
                for (int i=0;i<size;++i){
                    JSONObject place = array.getJSONObject(i);
                    //save addresses
                    addresses.add(place.getString("formatted_address"));

                    //save locations
                    JSONObject geometry = place.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    locations.add(new LatLng(Double.parseDouble(location.getString("lat")),
                            Double.parseDouble(location.getString("lng"))));

                    //save names
                    names.add(place.getString("name"));

                    //save icons
                    icons.add(place.getString("icon"));

                }
                showOnMap();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void showOnMap() {
            //markers to add marker
            ArrayList<Marker> markers = new ArrayList<>();
            //remove update to not center user location
            locationManager.removeUpdates(locationListener);
            //clear user marker
            mMap.clear();

            for (int i=0;i<locations.size();++i){

                DownloadIcon downloadIcon = new DownloadIcon();
                try {
                    Bitmap icon = downloadIcon.execute(icons.get(i)).get();
                    markers.add(mMap.addMarker(new MarkerOptions().
                            position(locations.get(i)).
                            title(names.get(i)).
                            icon(BitmapDescriptorFactory.fromBitmap(icon))));

                } catch (Exception e) {
                    e.printStackTrace();
                    markers.add(mMap.addMarker(new MarkerOptions().position(locations.get(i)).title(names.get(i))));

                }
            }

            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            for(Marker marker:markers){
                builder.include(marker.getPosition());
            }
            LatLngBounds bounds=builder.build();

            int padding = 200;//offset from edges of the map in pixel
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,padding);

            mMap.animateCamera(cameraUpdate);

        }

    }
/**********************************************************************************************/





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
        inputSearch = findViewById(R.id.input_search);
        getTextSearch();
    }



    public void getTextSearch() {

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

    public void getPlacesFromTextSearch() {

        RelativeLayout relativeLayout = findViewById(R.id.relLayout1);

        relativeLayout.setVisibility(View.INVISIBLE);

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
        DownloadWebContent webContent =new DownloadWebContent();
        webContent.execute(apiJson);
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
}
