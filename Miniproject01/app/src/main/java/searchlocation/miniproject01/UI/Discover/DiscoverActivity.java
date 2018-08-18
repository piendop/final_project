package searchlocation.miniproject01.UI.Discover;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import searchlocation.miniproject01.Models.Plan;
import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Utilis.Adapter;
import searchlocation.miniproject01.UI.Utilis.BottomNavigationViewHelper;

public class DiscoverActivity extends AppCompatActivity {



	private ListView listOfPlans;
	private Adapter mAdapter;
    ArrayList<Plan> planList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discover);
		listOfPlans = (ListView) findViewById(R.id.list_plans);
		Plan temp = new Plan();
		planList.add(temp);
        mAdapter = new Adapter(DiscoverActivity.this,planList);
        listOfPlans.setAdapter(mAdapter);
        new MultiplyTask().execute();

	}

	public void setupBottomNavigationView(){
		AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottomNavigation);
		BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigation);
		BottomNavigationViewHelper.enableBottomNavigation(DiscoverActivity.this,bottomNavigation);
	}

    private class MultiplyTask extends AsyncTask<Void,Void,Void>
	{
		@Override
		protected Void doInBackground(Void... voids) {
            planList.clear();
            ParseQuery<ParseObject> query = new ParseQuery<>("Plan");
            final SharedPreferences sharedPreferences = DiscoverActivity.this.getSharedPreferences("SharedPref",MODE_PRIVATE);
            String username = sharedPreferences.getString("USERNAME",null);
            query.whereEqualTo("userId", username);
            query.orderByAscending("createdAt");
            query.setLimit(3);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> objects, ParseException e) {
                    if(e==null && objects.size()>0){
                        for(final ParseObject object:objects){
                            final Plan plan = new Plan();
                            //load image to view
                            ParseFile image = (ParseFile) object.get("image");
                            if(image!=null){
                                image.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {
                                        if(e==null&&data!=null){
                                            Log.i("Get image","successful");
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                            plan.setImage(bitmap);
                                            plan.setTitle(object.getString("title"));
                                            plan.setDesc(object.getString("description"));
                                            plan.setTags(object.getString("hashtag"));
                                            //finally,add to planlist
                                            planList.add(plan);
                                            if(planList.size()==3){
                                                mAdapter.notifyDataSetChanged();
                                            }
                                        }else{
                                            Log.i("Get image","failed");
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }
                    }else{
                        e.printStackTrace();
                    }
                }
            });
            return null;
		}
    }
}
