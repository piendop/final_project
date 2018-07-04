package searchlocation.miniproject01.Reader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import searchlocation.miniproject01.R;
import searchlocation.miniproject01.Utilis.BottomNavigationViewHelper;

public class ArticleActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article);
		//setupBottomNavigationView();
	}

	public void setupBottomNavigationView(){
		BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavigation);
		BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
		BottomNavigationViewHelper.enableBottomNavigation(ArticleActivity.this,bottomNavigationViewEx);

	}
}
