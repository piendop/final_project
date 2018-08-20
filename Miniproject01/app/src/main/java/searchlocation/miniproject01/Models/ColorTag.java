package searchlocation.miniproject01.Models;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import searchlocation.miniproject01.R;
public class ColorTag {
	public static int getColorTag(Context context, int instanceNum) {
		switch (instanceNum) {
			case 1:
				return ContextCompat.getColor(context, R.color.tag_yellow);
			case 2:
				return ContextCompat.getColor(context, R.color.tag_red);
			case 3:
				return ContextCompat.getColor(context, R.color.tag_blue);
			case 4:
				return ContextCompat.getColor(context, R.color.tag_green);
			case 5:
				return ContextCompat.getColor(context, R.color.tag_purple);
			case 6:
				return ContextCompat.getColor(context, R.color.tag_pink);
			case 7:
				return ContextCompat.getColor(context, R.color.tag_black);
			default:
				return Color.WHITE;
		}
	}
}
