package searchlocation.miniproject01.UI.Utilis;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Login.LoginActivity;
import searchlocation.miniproject01.UI.OnGoing.OnGoingActivity;

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
		}


		public static void enableBottomNavigation(final Context context, final AHBottomNavigation AHBottomNavigation) {

		}
}





