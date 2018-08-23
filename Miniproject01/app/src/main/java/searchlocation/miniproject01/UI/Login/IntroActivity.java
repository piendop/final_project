package searchlocation.miniproject01.UI.Login;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;
import com.parse.Parse;
import com.parse.ParseUser;

import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.OnGoing.OnGoingActivity;
import searchlocation.miniproject01.UI.OnGoing.OnGoingEmptyActivity;
import searchlocation.miniproject01.UI.Utilis.IntroCustomLayout;

public class IntroActivity extends AppIntro {

    @Override
    public void onBackPressed() {
        /*alert whether user want to log out*/
        /***Set up an alert***/
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure?")
                .setMessage("Do you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_intro);
		//setUpSlides();
		addSlide(IntroCustomLayout.newInstance(R.layout.intro_slide1));
		addSlide(IntroCustomLayout.newInstance(R.layout.intro_slide2));
		addSlide(IntroCustomLayout.newInstance(R.layout.intro_slide3));
	}



	@Override
	public void onSkipPressed(Fragment currentFragment) {
		super.onSkipPressed(currentFragment);
		loadOnGoingActivity();
		// Do something when users tap on Skip button.

	}
	@Override
	public void onDonePressed(Fragment currentFragment) {
		super.onDonePressed(currentFragment);
		loadOnGoingActivity();
		// Do something when users tap on Done button.
	}

	@Override
	public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
		super.onSlideChanged(oldFragment, newFragment);
		// Do something when the slide changes.
	}

	private void loadOnGoingActivity() {
		Intent loadOnGoing = new Intent(IntroActivity.this, OnGoingEmptyActivity.class);
		startActivity(loadOnGoing);
	}
}
