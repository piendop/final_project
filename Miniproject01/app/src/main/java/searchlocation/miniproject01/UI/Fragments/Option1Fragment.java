package searchlocation.miniproject01.UI.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Discover.DiscoverActivity;

public class Option1Fragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_option1, container, false);

		TextView tv_option1 = view.findViewById(R.id.tv_user);
		tv_option1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent share = new Intent(v.getContext(),DiscoverActivity.class);
				startActivity(share);
			}
		});
		return view;
	}

}
