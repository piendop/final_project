package searchlocation.miniproject01.UI.Utilis;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Discover.DiscoverActivity;
import searchlocation.miniproject01.UI.Editor.EditorActivity;
import searchlocation.miniproject01.UI.OnGoing.OnGoingActivity;
import searchlocation.miniproject01.UI.Reader.Article_Base;

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
			AHBottomNavigation.setAccentColor(Color.parseColor("#628CF5"));
			AHBottomNavigation.setInactiveColor(Color.parseColor("#E6E6E6"));

			// Add items
			AHBottomNavigation.addItem(ongoing);
			AHBottomNavigation.addItem(discover);
			AHBottomNavigation.addItem(editor);
			AHBottomNavigation.addItem(library);
			AHBottomNavigation.addItem(profile);

			AHBottomNavigation.setCurrentItem(0);

		}

		public static void enableBottomNavigation(final Context context, final AHBottomNavigation AHBottomNavigation) {
			AHBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
				@Override
				public boolean onTabSelected(int position, boolean wasSelected) {
					switch (position) {
						case 0:
//							Toast.makeText(context, "ongoing Activity", Toast.LENGTH_SHORT).show();
							Intent onGoing = new Intent(context, OnGoingActivity.class); //ACTIVITY_NUMBER 1
							context.startActivity(onGoing);
							break;
						case 1:
//							Toast.makeText(context, "discover Activity", Toast.LENGTH_SHORT).show();
							Intent discover = new Intent(context, DiscoverActivity.class);
							context.startActivity(discover);
						//	AHBottomNavigation.setCurrentItem(1);
				//			Intent map = new Intent(context, MapsActivity.class); //ACTIVITY_NUMBER 1
							//context.startActivity(map);
							break;
						case 2:
							//Toast.makeText(context, "editor Activity", Toast.LENGTH_SHORT).show();
							Intent editor = new Intent(context, EditorActivity.class);
							context.startActivity(editor);
							break;
						case 3:
							//Toast.makeText(context, "library Activity", Toast.LENGTH_SHORT).show();
							//AHBottomNavigation.setCurrentItem(3);
							break;
						case 4:
							//Toast.makeText(context, "profile Activity", Toast.LENGTH_SHORT).show();
							Intent reader = new Intent(context, Article_Base.class);
							context.startActivity(reader);
							//AHBottomNavigation.setCurrentItem(4);
							break;
					}
					return false;
				}
			});
		}
}





