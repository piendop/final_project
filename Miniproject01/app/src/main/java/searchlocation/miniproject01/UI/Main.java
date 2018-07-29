package searchlocation.miniproject01.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Login.LoginActivity;

public class Main extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent login = new Intent(Main.this, LoginActivity.class);
				startActivity(login);
				finish();
			}
		},3000);
	}
}
