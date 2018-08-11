package searchlocation.miniproject01.UI.Search;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;

import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Utilis.BottomNavigationViewHelper;

public class MapActivity extends AppCompatActivity {

	private static final String TAG = "MapActivity";
	private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
	private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
	private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
	private static final float DEFAULT_ZOOM = 15f;

	//widgets
	private EditText mSearchText = (EditText) findViewById(R.id.input_search);

	//Var
	private boolean mLocationPermissionsGranted;
	private GoogleMap mMap;
	private FusedLocationProviderClient mFusedLocationProviderClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		setupBottomNavigationView();

	}

	public void setupBottomNavigationView(){
		AHBottomNavigation bottomNavigationViewEx = (AHBottomNavigation) findViewById(R.id.bottomNavigation);
		BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
		//BottomNavigationViewHelper.enableBottomNavigation(MapActivity.this,bottomNavigationViewEx);
		//Menu menu = bottomNavigationViewEx.getMenu();
	}
}

