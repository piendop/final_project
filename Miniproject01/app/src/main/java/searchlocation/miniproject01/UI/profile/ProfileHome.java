package searchlocation.miniproject01.UI.profile;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Discover.DiscoverActivity;
import searchlocation.miniproject01.UI.Editor.LocationEditorFragment;
import searchlocation.miniproject01.UI.Fragments.DraftsFragment;
import searchlocation.miniproject01.UI.Fragments.ProfileFragment;
import searchlocation.miniproject01.UI.Fragments.PublishedFragment;
import searchlocation.miniproject01.UI.Utilis.BottomNavigationViewHelper;
import searchlocation.miniproject01.UI.Utilis.MyFragmentPagerAdapter;

public class ProfileHome extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_home);
		setupBottomNavigationView();

		// Get the ViewPager and set it's PagerAdapter so that it can display items
		ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
		setupViewPager(viewPager);

		// Give the TabLayout the ViewPager
		TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
		tabLayout.setupWithViewPager(viewPager);


	}
	private void setupViewPager(ViewPager viewPager) {
		MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
		adapter.addFragment(new PublishedFragment(), "Published");
		adapter.addFragment(new DraftsFragment(), "Drafts");
		adapter.addFragment(new ProfileFragment(), "Profile");
		viewPager.setAdapter(adapter);
	}
	public void setupBottomNavigationView() {
		AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottomNavigation);
		BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigation);
		BottomNavigationViewHelper.enableBottomNavigation(ProfileHome.this,bottomNavigation);
		bottomNavigation.disableItemAtPosition(4);
	}

}
