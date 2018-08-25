package searchlocation.miniproject01.UI.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.Date;
import java.util.List;

import searchlocation.miniproject01.Models.Plan;
import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Discover.DiscoverActivity;
import searchlocation.miniproject01.UI.Reader.Article_Base;
import searchlocation.miniproject01.UI.Utilis.PlanAdapter;
import searchlocation.miniproject01.UI.Utilis.PlanPublishedAdapter;
import searchlocation.miniproject01.UI.profile.ProfileAdapter;

public class PublishedFragment extends Fragment implements PlanPublishedAdapter.PlanPublishedAdapterOnClickHandler {

	private static int NUM_LIST_ITEMS = 0;
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
				query.orderByAscending("createdProfile");
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
		SharedPreferences preferences = getActivity().getSharedPreferences("SharedPref", 0);
		preferences.edit().putLong("createdProfile",0).apply();
		list.setVisibility(View.INVISIBLE);
		Log.i("Start", "susessfull");
		username= ParseUser.getCurrentUser().getObjectId();
		LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
		list.setLayoutManager(layoutManager);
		//if(mAdapter == null) {
			//listOfPlans.setHasFixedSize(true);
			mAdapter = new PlanPublishedAdapter(3,planList,this);
			list.setAdapter(mAdapter);
			//load shared plan initially
			//loadNewPlan();
		//}else{
			/*mAdapter = new PlanPublishedAdapter(3,planList,this);
			//list.setAdapter(mAdapter);
			//load shared plan initially*/
			new LoadingSharePlanInitially().execute();
			//Log.i("mAdapter ","is null");
			//list.setVisibility(View.VISIBLE);
		//}
	}

    @Override
    public void onPause() {
        super.onPause();
        final SharedPreferences preferences = getActivity().getSharedPreferences("SharedPref", 0);
        preferences.edit().putLong("createdProfile",0).apply();
    }

    private void loadNewPlan() {
        //mLoadingIndicator.setVisibility(View.VISIBLE);

        final SharedPreferences preferences = getActivity().getSharedPreferences("SharedPref", 0);
        Date date = new Date(preferences.getLong("createdProfile", 0));
        username= ParseUser.getCurrentUser().getObjectId();
        if (username!=null ) {
            Log.i("username ",username);
            Log.i("Date ",date.toString());
            ParseQuery<ParseObject> query = new ParseQuery<>("Plan");
            query.orderByAscending("createdProfile");
            query.whereEqualTo("userId", username);
            query.whereGreaterThan("createdProfile",date);
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
                                            getNewPlan(bitmap, plan, object);
                                        } else {
                                            Log.i("Get image", "failed");
                                        }
                                    }
                                });
                            }else{
                                Log.i("Get image sample", "successful");
                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image2);
                                getNewPlan(bitmap,plan,object);
                            }

                            Date date = new Date(preferences.getLong("createdProfile", 0));

                            if (object.getCreatedAt().after(date)) {
                                Log.i("Date ",date.toString());
                                preferences.edit().putLong("createdProfile", object.getCreatedAt().getTime()).apply();
                            }
                        }
                    } else {
                        Log.i("Object", "cannot load more");
                        list.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    private void getNewPlan(Bitmap bitmap, Plan plan, ParseObject object) {
        plan.setImage(bitmap);
        plan.setTitle(object.getString("title"));
        plan.setDesc(object.getString("description"));
        plan.setTags(object.getString("hashtag"));
        plan.setObjectId(object.getObjectId());
        //preferences.edit().putLong("createdProfile", object.getCreatedAt().getTime()).apply();
        //load more plan
        NUM_LIST_ITEMS++;
        mAdapter.setmNumberItems(NUM_LIST_ITEMS);
        planList.add(plan);
        mAdapter.notifyItemInserted(NUM_LIST_ITEMS);
        list.setVisibility(View.VISIBLE);
    }
	@Override
	public void onClick(Plan itemPlan) {
		Log.i("Item click", itemPlan.toString());
		Intent intent = new Intent(getContext(), Article_Base.class);
		intent.putExtra("objectId", itemPlan.getObjectId());
		startActivity(intent);
	}

}
