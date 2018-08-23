package searchlocation.miniproject01.UI.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import searchlocation.miniproject01.R;

public class PublishedFragment extends Fragment {

	public PublishedFragment(){

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_page_published, container, false);

		TextView noConnectionTextView = (TextView) view.findViewById(R.id.cardpublished_title);
//		SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
//		RecyclerView listOfPlans = (RecyclerView) view.findViewById(R.id.list_plans);
//		ProgressBar mLoadingIndicator = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);
		TextView noPlanTextView = (TextView) view.findViewById(R.id.cardpublished_description);
		return view;
	}

}
