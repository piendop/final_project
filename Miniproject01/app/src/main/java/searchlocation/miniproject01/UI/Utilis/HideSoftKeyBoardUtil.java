package searchlocation.miniproject01.UI.Utilis;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

public class HideSoftKeyBoardUtil {
	public static void hide(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}
}
