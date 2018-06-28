package searchlocation.miniproject01;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import searchlocation.miniproject01.Utilis.BottomNavigationViewHelper;

public class OnGoingActivity extends AppCompatActivity {

	private static final String TAG = "OnGoingActivity";

	private static final int ERROR_DIALOG_REQUEST=9001;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(isServiceOK()){
			init();
		}
		setupBottomNavigationView();

	}

	private void init() {

	}

	//Check if the services is OK or NOT
	public boolean isServiceOK(){
		Log.d(TAG,"isServicesOK: checking google services version");
		int availabe = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(OnGoingActivity.this);
		if(availabe == ConnectionResult.SUCCESS){
			//OK
			Log.d(TAG,"isServicesOK: Google Play Services is working");
			return true;
		} else if (GoogleApiAvailability.getInstance().isUserResolvableError(availabe)){
			//an error occured
			Log.d(TAG,"isServicesOK: an error occured");
			Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(OnGoingActivity.this,availabe,ERROR_DIALOG_REQUEST);
			dialog.show();
		} else {
			Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
		}
		return false;
	}

	public void setupBottomNavigationView(){
		BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavigation);
		BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
		BottomNavigationViewHelper.enableBottomNavigation(OnGoingActivity.this,bottomNavigationViewEx);

	}
}
