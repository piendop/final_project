package searchlocation.miniproject01.UI.Utilis;
import android.graphics.Color;
import android.util.Log;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import searchlocation.miniproject01.R;


public class BottomNavigationReader {
		public static void setupBottomNavigationView(AHBottomNavigation AHBottomNavigation) {
			Log.i("Bottom","Create item");
			//Create Item
			AHBottomNavigationItem close = new AHBottomNavigationItem("close", R.drawable.ic_close);
			AHBottomNavigationItem ghost1 = new AHBottomNavigationItem("ghost1", R.drawable.ic_ghost);
			AHBottomNavigationItem ghost2 = new AHBottomNavigationItem("ghost2", R.drawable.ic_ghost);
			AHBottomNavigationItem unmark = new AHBottomNavigationItem("unmark", R.drawable.ic_mark);
			AHBottomNavigationItem addtoOngoing = new AHBottomNavigationItem("addtoOngoing", R.drawable.ic_addto_ongoing);

			// Hide title of item
			AHBottomNavigation.setTitleState(com.aurelhubert.ahbottomnavigation.AHBottomNavigation.TitleState.ALWAYS_HIDE);
			//			AHBottomNavigation.setAccentColor(Color.parseColor("#628CF5"));
//			AHBottomNavigation.setInactiveColor(Color.parseColor("#E6E6E6"));
			close.setColor(Color.parseColor("#628CF5"));
			//Create 2 ghosts button for navigation
			ghost1.setColor(Color.parseColor("#FFFFFF"));
			ghost2.setColor(Color.parseColor("#FFFFFF"));
			unmark.setColor(Color.parseColor("#E6E6E6"));
			addtoOngoing.setColor(Color.parseColor("#628CF5"));
			AHBottomNavigation.disableItemAtPosition(1);
			AHBottomNavigation.disableItemAtPosition(2);
			Log.i("Bottom","add item");
			// Add items
			AHBottomNavigation.addItem(close);
			AHBottomNavigation.addItem(ghost1);
			AHBottomNavigation.addItem(ghost2);
			AHBottomNavigation.addItem(unmark);
			AHBottomNavigation.addItem(addtoOngoing);
		}


//		public static void enableBottomNavigation(final Context context, final AHBottomNavigation AHBottomNavigation) {
//
//		}
}





