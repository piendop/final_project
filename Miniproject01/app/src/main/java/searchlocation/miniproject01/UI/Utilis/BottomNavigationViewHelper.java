package searchlocation.miniproject01.UI.Utilis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Discover.DiscoverActivity;
import searchlocation.miniproject01.UI.Editor.EditorActivity;
import searchlocation.miniproject01.UI.Favorite.FavoriteActivity;
import searchlocation.miniproject01.UI.OnGoing.OnGoingActivity;
import searchlocation.miniproject01.UI.OnGoing.OnGoingEmptyActivity;
import searchlocation.miniproject01.UI.Reader.Article_Base;
import searchlocation.miniproject01.UI.profile.ProfileHome;

public class BottomNavigationViewHelper {
		public static void setupBottomNavigationView(AHBottomNavigation AHBottomNavigation) {
			//Create Item
			AHBottomNavigationItem ongoing = new AHBottomNavigationItem("ongoing",R.drawable.ic_ongoing);
			AHBottomNavigationItem discover = new AHBottomNavigationItem("discover",R.drawable.ic_discover);
			AHBottomNavigationItem editor = new AHBottomNavigationItem("editor",R.drawable.ic_write);
			AHBottomNavigationItem library = new AHBottomNavigationItem("library",R.drawable.ic_library);
			AHBottomNavigationItem profile = new AHBottomNavigationItem("profile",R.drawable.ic_personal);

			// Hide title of item
			AHBottomNavigation.setTitleState(com.aurelhubert.ahbottomnavigation.AHBottomNavigation.TitleState.ALWAYS_HIDE);
			AHBottomNavigation.setAccentColor(Color.parseColor("#E6E6E6"));
			AHBottomNavigation.setInactiveColor(Color.parseColor("#E6E6E6"));
			AHBottomNavigation.setItemDisableColor(Color.parseColor("#4A90E2"));
			AHBottomNavigation.setCurrentItem(2);
			// Add items
			AHBottomNavigation.addItem(ongoing);
			AHBottomNavigation.addItem(discover);
			AHBottomNavigation.addItem(editor);
			AHBottomNavigation.addItem(library);
			AHBottomNavigation.addItem(profile);

//			AHBottomNavigation.setCurrentItem(0);
		}

		public static void enableBottomNavigation(final Context context, final AHBottomNavigation AHBottomNavigation) {
			AHBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
				@Override
				public boolean onTabSelected(int position, boolean wasSelected) {
					switch (position) {
						case 0:
							SharedPreferences pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
							if (pref.getBoolean("IS_ONGOING", false)){
								Intent onGoing = new Intent(context, OnGoingActivity.class); //ACTIVITY_NUMBER 1
								onGoing.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
								context.startActivity(onGoing);
							} else {
								Intent onGoingEmpty = new Intent(context, OnGoingActivity.class); //ACTIVITY_NUMBER 1
								onGoingEmpty.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
								context.startActivity(onGoingEmpty);
							}
							break;
						case 1:
							Intent discover = new Intent(context, DiscoverActivity.class);
							discover.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
							context.startActivity(discover);
							break;
						case 2:
							Intent editor = new Intent(context, EditorActivity.class);
							context.startActivity(editor);
							break;
						case 3:
							Intent bookmark = new Intent(context, FavoriteActivity.class);
							context.startActivity(bookmark);
							break;
						case 4:
							Intent profile = new Intent(context, ProfileHome.class);
							profile.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
							context.startActivity(profile);
							break;
					}
					return false;
				}
			});
		}
}





