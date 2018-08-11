package searchlocation.miniproject01.UI.Fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Login.IntroActivity;
import searchlocation.miniproject01.UI.Login.LoginActivity;
import searchlocation.miniproject01.UI.OnGoing.OnGoingActivity;

public class RememberMeFragment extends DialogFragment {
	//ThingsAdapter adapter;
	FragmentActivity listener;

	public Button notNow,remember;
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	//	currentServiceId = ServiceHelper.getSelectedServiceId(activity);
		View view = inflater.inflate(R.layout.fragment_rememberme, container, false);
		notNow = view.findViewById(R.id.notnow_button);
		remember = view.findViewById(R.id.remember_button);

		//Change isRemember to false
		notNow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loadOnGoingActivity();
				//getDialog().dismiss();
			}
		});

		//Change isRemember to true
		remember.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				writeAccountToSharedPrenferences();
				loadOnGoingActivity();
				//	getDialog().dismiss();
			}
		});
		return view;
	}

	private void writeAccountToSharedPrenferences() {
		SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SharedPref",0);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean("IS_REMEMBER",true);
		editor.commit();
	}

	private void loadOnGoingActivity() {
		Intent intent = new Intent(getActivity(), IntroActivity.class);
		startActivity(intent);
	}


	// This method is called when the fragment is no longer connected to the Activity
	// Any references saved in onAttach should be nulled out here to prevent memory leaks.
	@Override
	public void onDetach() {
		super.onDetach();
		this.listener = null;
	}

}
