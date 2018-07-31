package searchlocation.miniproject01.UI.Login;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.OnGoing.OnGoingActivity;
import searchlocation.miniproject01.UI.Utilis.IntroCustomLayout;

public class IntroActivity extends AppIntro {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_intro);
		//setUpSlides();
		addSlide(IntroCustomLayout.newInstance(R.layout.intro_slide1));
		addSlide(IntroCustomLayout.newInstance(R.layout.intro_slide2));
		addSlide(IntroCustomLayout.newInstance(R.layout.intro_slide3));
	}

	private void setUpSlides() {
		SliderPage sliderPage = new SliderPage();
		sliderPage.setTitle("Discover Interesting Plans");
		sliderPage.setTitle(getString(R.string.slide1_title));
		sliderPage.setDescription(getString(R.string.slide1_description));
		sliderPage.setImageDrawable(R.drawable.character_discover);
		addSlide(AppIntroFragment.newInstance(sliderPage));

		SliderPage sliderPage2 = new SliderPage();
		sliderPage2.setTitle("Discover Interesting Plans");
		sliderPage2.setTitle(getString(R.string.slide2_title));
		sliderPage2.setDescription(getString(R.string.slide2_description));
		sliderPage2.setImageDrawable(R.drawable.character_write);
		addSlide(AppIntroFragment.newInstance(sliderPage2));

		SliderPage sliderPage3 = new SliderPage();
		sliderPage3.setTitle("Discover Interesting Plans");
		sliderPage3.setTitle(getString(R.string.slide3_title));
		sliderPage3.setDescription(getString(R.string.slide3_description));
		sliderPage3.setImageDrawable(R.drawable.character_live);
		addSlide(AppIntroFragment.newInstance(sliderPage3));
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
		Intent loadOnGoing = new Intent(IntroActivity.this, OnGoingActivity.class);
		startActivity(loadOnGoing);
	}
}
