package searchlocation.miniproject01.UI.Reader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;

import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Utilis.BottomNavigationReader;
import searchlocation.miniproject01.UI.Utilis.BottomNavigationViewHelper;

public class Article_Base extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_base);
		setupBottomNavigationReader();
	}
	public void setupBottomNavigationReader(){
		AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottomNavigationReader);
		BottomNavigationReader.setupBottomNavigationView(bottomNavigation);
		BottomNavigationReader.enableBottomNavigation(Article_Base.this,bottomNavigation);
	}
}
