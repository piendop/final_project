package searchlocation.miniproject01.UI.Utilis;

import android.content.Context;
import android.graphics.Color;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import searchlocation.miniproject01.R;

public class BottomNavigationReader {
		public static void setupBottomNavigationView(AHBottomNavigation AHBottomNavigation) {
			//Create Item
			AHBottomNavigationItem close = new AHBottomNavigationItem("close", R.drawable.ic_close);
			AHBottomNavigationItem unmark = new AHBottomNavigationItem("unmark", R.drawable.ic_unmark);
			AHBottomNavigationItem addtoOngoing = new AHBottomNavigationItem("addtoOngoing", R.drawable.ic_addto_ongoing);

			// Hide title of item
			AHBottomNavigation.setTitleState(com.aurelhubert.ahbottomnavigation.AHBottomNavigation.TitleState.ALWAYS_HIDE);
			AHBottomNavigation.setAccentColor(Color.parseColor("#628CF5"));
			AHBottomNavigation.setInactiveColor(Color.parseColor("#E6E6E6"));

			// Add items
			AHBottomNavigation.addItem(close);
			AHBottomNavigation.addItem(unmark);
			AHBottomNavigation.addItem(addtoOngoing);


			AHBottomNavigation.setCurrentItem(0);

		}

		public static void enableBottomNavigation(final Context context, final AHBottomNavigation AHBottomNavigation) {
			AHBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
				@Override
				public boolean onTabSelected(int position, boolean wasSelected) {
//					switch (position) {
//						case 0:
//							Toast.makeText(context, "ongoing Activity", Toast.LENGTH_SHORT).show();
//							Intent onGoing = new Intent(context, OnGoingActivity.class); //ACTIVITY_NUMBER 1
//							context.startActivity(onGoing);
//							break;
//						case 1:
//							Toast.makeText(context, "discover Activity", Toast.LENGTH_SHORT).show();
//							Intent discover = new Intent(context, DiscoverActivity.class);
//							context.startActivity(discover);
//						//	AHBottomNavigation.setCurrentItem(1);
//				//			Intent map = new Intent(context, MapsActivity.class); //ACTIVITY_NUMBER 1
//							//context.startActivity(map);
//							break;
//						case 2:
//							Toast.makeText(context, "editor Activity", Toast.LENGTH_SHORT).show();
//							Intent editor = new Intent(context, EditorActivity.class);
//							context.startActivity(editor);
//							break;
//						case 3:
//							Toast.makeText(context, "library Activity", Toast.LENGTH_SHORT).show();
//							//AHBottomNavigation.setCurrentItem(3);
//							break;
//						case 4:
//							Toast.makeText(context, "profile Activity", Toast.LENGTH_SHORT).show();
//							//AHBottomNavigation.setCurrentItem(4);
//							break;
//					}
					return false;
				}
			});
		}
}





