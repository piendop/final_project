package searchlocation.miniproject01;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import searchlocation.miniproject01.Utilis.BottomNavigationViewHelper;

public class Test extends FragmentActivity implements OnMapReadyCallback {

	private GoogleMap mMap;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);//create a mapFragment from map
        mapFragment.getMapAsync(this);
		setupBottomNavigationView();
	}

	public void setupBottomNavigationView(){
		BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavigation);
		BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
		BottomNavigationViewHelper.enableBottomNavigation(Test.this,bottomNavigationViewEx);
		Menu menu = bottomNavigationViewEx.getMenu();
	}


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //get image from satellite
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Add a marker in Sydney and move the camera
        LatLng fuji = new LatLng(35.3605547,138.7190229);//latitude longitude
        //set an icon for marker
        mMap.addMarker(new MarkerOptions().position(fuji).title("Marker in Mt.Fuji").
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));//add a marker and text
        //to zoom in location range 1-20 1: zoom out 20: zoom in
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fuji,15f));
    }
}
