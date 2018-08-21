package searchlocation.miniproject01.UI.OnGoing;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;

import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Utilis.BottomNavigationViewHelper;

public class OnGoingEmptyActivity extends AppCompatActivity {

    AHBottomNavigation bottomNavigation;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupBottomNavigationView();
		bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if(position==0){
                    String planId = getIntent().getStringExtra("currentPlan");
                    if(planId==null){
                        Intent intent = new Intent(OnGoingEmptyActivity.this,OnGoingEmptyActivity.class);
                        startActivity(intent);
                    }
                }
                return false;
            }
        });
	}

	/*This method I use to setup the bottom navigation with helper from Utils folder*/
	private void setupBottomNavigationView(){
		bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottomNavigation);
		BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigation);
		//BottomNavigationViewHelper.enableBottomNavigation(OnGoingEmptyActivity.this,BottomNavigation);
	}

}
