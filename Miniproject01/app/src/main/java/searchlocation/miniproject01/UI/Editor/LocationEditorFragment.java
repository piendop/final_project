package searchlocation.miniproject01.UI.Editor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import searchlocation.miniproject01.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationEditorFragment extends Fragment {


	public LocationEditorFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_location_editor,container,false);

		EditText review = (EditText) rootView.findViewById(R.id.edit_review);
//		Button addReview = (Button) rootView.findViewById(R.id.btn_add_review);
		return rootView;
	}

}
