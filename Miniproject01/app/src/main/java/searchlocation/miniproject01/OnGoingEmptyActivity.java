package searchlocation.miniproject01;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import searchlocation.miniproject01.Utilis.BottomNavigationViewHelper;

public class OnGoingEmptyActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ongoingempty);
		setupBottomNavigationView();

	}

	/*This method I use to setup the bottom navigation with helper from Utils folder*/
	private void setupBottomNavigationView(){
		BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavigation);
		BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
	}
}
