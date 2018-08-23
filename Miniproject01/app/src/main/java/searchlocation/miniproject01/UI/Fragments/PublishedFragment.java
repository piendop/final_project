package searchlocation.miniproject01.UI.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import searchlocation.miniproject01.Models.Plan;
import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Discover.DiscoverActivity;
import searchlocation.miniproject01.UI.Utilis.PlanAdapter;
import searchlocation.miniproject01.UI.Utilis.PlanPublishedAdapter;

public class PublishedFragment extends Fragment {

	private static int NUM_LIST_ITEMS = 10;
	private String username;
	private PlanPublishedAdapter mAdapter;
	RecyclerView list;
	ArrayList<Plan> planList = new ArrayList<>();
	Bundle savedInstanceState;
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
		list = (RecyclerView) view.findViewById(R.id.list_published);
//		SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
//		RecyclerView listOfPlans = (RecyclerView) view.findViewById(R.id.list_plans);
//		ProgressBar mLoadingIndicator = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);
		return view;
	}

	private class LoadingSharePlanInitially extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... voids) {
			Log.i("username ", "try to get username");
			ParseQuery<ParseObject> query = new ParseQuery<>("Plan");
			if (username != null) {
				Log.i("username ", username);
				query.whereEqualTo("userId", username);
				query.orderByAscending("createdAt");
				query.setLimit(10);
				query.findInBackground(new FindCallback<ParseObject>() {
					@Override
					public void done(final List<ParseObject> objects, ParseException e) {
						if (e == null && objects.size() > 0) {
							for (final ParseObject object : objects) {
								final Plan plan = new Plan();
								//load image to view
										plan.setTitle(object.getString("title"));
										plan.setDesc(object.getString("description"));
										plan.setObjectId(object.getObjectId());
										//finally,add to planlist
										planList.add(plan);
										if (planList.size() == objects.size()) {
											NUM_LIST_ITEMS = objects.size();
											mAdapter.setmNumberItems(NUM_LIST_ITEMS);
											mAdapter.setListOfPlans(planList);
											mAdapter.notifyDataSetChanged();
											list.setVisibility(View.VISIBLE);
										}
									}
								}
						}
				});
			}
			return null;
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		Log.i("Start", "susessfull");
		username= ParseUser.getCurrentUser().getObjectId();
		LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
		list.setLayoutManager(layoutManager);
		if(mAdapter == null) {
			//listOfPlans.setHasFixedSize(true);
			mAdapter = new PlanPublishedAdapter(3,planList);
			list.setAdapter(mAdapter);
			//load shared plan initially
			new LoadingSharePlanInitially().execute();
		}else{
			mAdapter = new PlanPublishedAdapter(3,planList);
			list.setAdapter(mAdapter);
			//load shared plan initially
			new LoadingSharePlanInitially().execute();
			Log.i("mAdapter ","is null");
			list.setVisibility(View.VISIBLE);
		}
	}

}
