package searchlocation.miniproject01.UI.Reader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
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
import java.util.Date;
import java.util.List;

import searchlocation.miniproject01.Models.Plan;
import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Discover.DiscoverActivity;
import searchlocation.miniproject01.UI.Reader.Article_Base;
import searchlocation.miniproject01.UI.Reader.RelatedArticleAdapter;
import searchlocation.miniproject01.UI.Utilis.PlanAdapter;
import searchlocation.miniproject01.UI.Utilis.PlanPublishedAdapter;

public class RelatedArticleFragment extends Fragment {

	private static int NUM_LIST_ITEMS = 1;
	private String username;
	private RelatedArticleAdapter mAdapter;
	RecyclerView list;
	ArrayList<Plan> planList = new ArrayList<>();
	public RelatedArticleFragment(){

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_related_article, container, false);
		list = (RecyclerView) view.findViewById(R.id.list_plans);
//		SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
//		RecyclerView listOfPlans = (RecyclerView) view.findViewById(R.id.list_plans);
//		ProgressBar mLoadingIndicator = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);
		return view;
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
			mAdapter = new RelatedArticleAdapter(1,planList);
			list.setAdapter(mAdapter);
			//load shared plan initially
			new LoadingSharePlanInitially().execute();
		}else{
			mAdapter = new RelatedArticleAdapter(1,planList);
			list.setAdapter(mAdapter);
			//load shared plan initially
			new LoadingSharePlanInitially().execute();
			Log.i("mAdapter ","is null");
			list.setVisibility(View.VISIBLE);
		}
		HandleOnclick();
	}
	private class LoadingSharePlanInitially extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... voids) {
			ParseQuery<ParseObject> query = new ParseQuery<>("Plan");
			if (username != null) {
				Log.i("username ", username);
				query.whereNotEqualTo("userId", username);
				query.orderByAscending("createdAt");
				query.setLimit(3);
				query.findInBackground(new FindCallback<ParseObject>() {
					@Override
					public void done(final List<ParseObject> objects, ParseException e) {
						if (e == null && objects.size() > 0) {

							for (final ParseObject object : objects) {
								final Plan plan = new Plan();
								//load image to view
								ParseFile image = (ParseFile) object.get("image");
								if (image != null) {
									image.getDataInBackground(new GetDataCallback() {
										@Override
										public void done(byte[] data, ParseException e) {
											if (e == null && data != null) {
												Log.i("Get image", "successful");
												Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
												plan.setImage(bitmap);
												plan.setTitle(object.getString("title"));
												plan.setDesc(object.getString("description"));
												plan.setTags(object.getString("hashtag"));
												plan.setObjectId(object.getObjectId());
												//finally,add to planlist
												planList.add(plan);
												SharedPreferences preferences = getActivity().getSharedPreferences("SharedPref", 0);
												Date date = new Date(preferences.getLong("createdAt", 0));
												if (object.getCreatedAt().after(date)) {
													preferences.edit().putLong("createdAt", object.getCreatedAt().getTime()).apply();
												}
												if (planList.size() == objects.size()) {
													NUM_LIST_ITEMS = objects.size();
													mAdapter.setmNumberItems(NUM_LIST_ITEMS);
													mAdapter.setListOfPlans(planList);
													mAdapter.notifyDataSetChanged();
													list.setVisibility(View.VISIBLE);
													Log.i("Load", "finished");
												}
											} else {
												Log.i("Get image", "failed");
											}
										}
									});
								}
							}

						} else {
							Log.i("Could", "not load object");
						}
					}
				});
			}
			return null;
		}

		@Override
		protected void onCancelled(Void aVoid) {
			super.onCancelled(aVoid);
			Log.i("Post","Execute");
		}

	}

	private void HandleOnclick() {
		for (int i = 0 ; i<= planList.size();i++){
			final int index = i;
			View viewItem = list.getLayoutManager().findViewByPosition(index);
			Log.i("ViewItemID","");
//			viewItem.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					Log.i("Item click", "");
////					Intent intent = new Intent(getActivity(), RelatedArticleFragment.class);
////					intent.putExtra("objectId", planList.get(index).getObjectId());
////					startActivity(intent);
//				}
//			});
		}
	}
}