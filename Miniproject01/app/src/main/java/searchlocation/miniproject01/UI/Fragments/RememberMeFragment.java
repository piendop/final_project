package searchlocation.miniproject01.UI.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.BaseFragment;
import searchlocation.miniproject01.UI.Login.IntroActivity;
import searchlocation.miniproject01.UI.Login.LoginActivity;

public class RememberMeFragment extends BaseFragment {
	//ThingsAdapter adapter;
	FragmentActivity listener;
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	//	currentServiceId = ServiceHelper.getSelectedServiceId(activity);
		return inflater.inflate(R.layout.fragment_rememberme, container, false);


	}

	@Override
	protected void initViews(View rootView, Bundle savedInstanceState) {
		super.initViews(rootView, savedInstanceState);

		Button notNow =  rootView.findViewById(R.id.notnow_button);
		Button remember =  rootView.findViewById(R.id.remember_button);

		notNow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
	}
	// This method is called when the fragment is no longer connected to the Activity
	// Any references saved in onAttach should be nulled out here to prevent memory leaks.
	@Override
	public void onDetach() {
		super.onDetach();
		this.listener = null;
	}
	private void loadIntroActivity() {
		Intent loadIntro = new Intent(RememberMeFragment,IntroActivity.class);
		startActivity(loadIntro);
	}
}
