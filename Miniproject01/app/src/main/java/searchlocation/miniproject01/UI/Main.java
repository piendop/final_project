package searchlocation.miniproject01.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.parse.ParseUser;

import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Login.LoginActivity;
import searchlocation.miniproject01.UI.OnGoing.OnGoingActivity;

public class Main extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if(isRememberme()){
					loadOnGoingActivity();
				} else {
					loadLogin();
				}
			}
		},1000);
	}
	public void loadLogin(){
		Intent login = new Intent(Main.this, LoginActivity.class);
		startActivity(login);
	}
	public void loadOnGoingActivity() {
		Intent loadOnGoing = new Intent(Main.this, OnGoingActivity.class);
		startActivity(loadOnGoing);
	}
	public boolean isRememberme(){
	    SharedPreferences sharedPreferences = this.getSharedPreferences("SharedPref",MODE_PRIVATE);
	    String username = sharedPreferences.getString("USERNAME",null);
		if(username!=null){
		    return true;
        }
        return false;
	}
}
