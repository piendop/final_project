package searchlocation.miniproject01.Utilis;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import searchlocation.miniproject01.ArticleActivity;
import searchlocation.miniproject01.FavoriteActivity;
import searchlocation.miniproject01.OnGoingActivity;
import searchlocation.miniproject01.R;
import searchlocation.miniproject01.SharePlanActivity;
import searchlocation.miniproject01.Test;

public class BottomNavigationViewHelper {
		public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx) {
			bottomNavigationViewEx.enableAnimation(false);
			bottomNavigationViewEx.enableShiftingMode(false);
			bottomNavigationViewEx.enableItemShiftingMode(false);
			bottomNavigationViewEx.setTextVisibility(false);
		}
		public static void enableBottomNavigation(final Context context, BottomNavigationViewEx view) {
			view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
				@Override
				public boolean onNavigationItemSelected(@NonNull MenuItem item) {
					switch (item.getItemId()) {
						case R.id.ic_maps:
							Intent intentmap = new Intent(context, Test.class); //ACTIVITY_NUMBER 1
							item.setIcon(R.drawable.ic_maps_onclick);
							context.startActivity(intentmap);
							break;
						case R.id.ic_plan:
							Intent intentplan = new Intent(context, SharePlanActivity.class); //ACTIVITY_NUMBER 2
							context.startActivity(intentplan);
							break;
						case R.id.ic_favorite:
							Intent intentfavorite = new Intent(context, ArticleActivity.class); //ACTIVITY_NUMBER 3
							item.setIcon(R.drawable.ic_favorite_onclick);
							context.startActivity(intentfavorite);
							break;
						case R.id.ic_highlight:
							Intent intenthighligt = new Intent(context, OnGoingActivity.class);//ACTIVITY_NUMBER 0
							item.setIcon(R.drawable.ic_highlight_onclick);
							context.startActivity(intenthighligt);
							break;
					}
					return false;
				}
			});
		}
}





