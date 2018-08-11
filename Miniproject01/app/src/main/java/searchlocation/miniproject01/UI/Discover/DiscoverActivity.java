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
import com.parse.ParseUser;
import searchlocation.miniproject01.UI.ParseApplication;

import java.util.ArrayList;
import java.util.List;

import searchlocation.miniproject01.Models.Plan;
import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Login.LoginActivity;
import searchlocation.miniproject01.UI.Utilis.Adapter;
import searchlocation.miniproject01.UI.Utilis.BottomNavigationViewHelper;

public class DiscoverActivity extends AppCompatActivity {



	private ListView listOfPlans;
	private Adapter mAdapter;
    ArrayList<Plan> planList = new ArrayList<>();

	String[][] data = {
			{"Du lịch Phú Quốc: Cẩm nang từ A đến Z","#phuquoc#3ngay2dem#","Phú Quốc là quần đảo xinh đẹp nằm sâu trong vùng vịnh Thái Lan, thuộc tỉnh Kiên Giang. Ở vùng biển phía Nam của tổ quốc, đảo Ngọc Phú Quốc – hòn đảo lớn nhất của Việt Nam…"},
			{"Du lịch Phú Quốc: Cẩm nang từ A đến Z","#phuquoc#3ngay2dem#","Mì Quảng, bún mắm nêm, gỏi mít, ốc hút, là những món đặc trưng nhất định phải thử khi đến Đà Nẵng."},
			{"‘Ăn hết’ món ngon đường phố Đà Nẵng với 200k","#danang#2ngay3dem#duongpho#amthuc","Mì Quảng, bún mắm nêm, gỏi mít, ốc hút, là những món đặc trưng nhất định phải thử khi đến Đà Nẵng."}
	};
	int[] imgId = {R.drawable.img1,R.drawable.img2,R.drawable.img3,R.drawable.img4};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discover);
		//new MultiplyTask().execute();
		//listOfPlans = (ListView) findViewById(R.id.list_plans);
        planList.clear();
        ParseQuery<ParseObject> query = new ParseQuery<>("Plan");
        SharedPreferences sharedPreferences = this.getSharedPreferences("SharedPref",MODE_PRIVATE);
        String username = sharedPreferences.getString("USERNAME",null);
        query.whereEqualTo("userId", username);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects.size()>0){
                    for(ParseObject object:objects){
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
                                    }else{
                                        Log.i("Get image","failed");
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        plan.setTitle(object.getString("title"));
                        plan.setDesc(object.getString("description"));
                        plan.setTags(object.getString("hashtag"));

                        //finally,add to planlist
                        planList.add(plan);
                        //mAdapter.notifyDataSetChanged();*/
                    }

                    /*if(planList.size()>0) {
                        mAdapter = new Adapter(DiscoverActivity.this, planList);
                        listOfPlans.setAdapter(mAdapter);
                    }*/
                }else{
                    e.printStackTrace();
                }
            }
        });



	}

	public void setupBottomNavigationView(){
		AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottomNavigation);
		BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigation);
		BottomNavigationViewHelper.enableBottomNavigation(DiscoverActivity.this,bottomNavigation);
	}
	public class MultiplyTask extends AsyncTask<Void,Void,Void>
	{

		@Override
		protected Void doInBackground(Void... voids) {
		    /*planList.clear();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Plan");
            query.whereEqualTo("parent", ParseUser.getCurrentUser());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(e==null && objects.size()>0){
                        for(ParseObject object:objects){
                            final Plan plan = new Plan();
                            plan.setTitle(object.getString("title"));
                            plan.setDesc(object.getString("description"));
                            plan.setTags(object.getString("hashtag"));

                            //load image to view
                            ParseFile image = (ParseFile) object.get("image");

                            if(image!=null){
                                image.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {
                                        if(e==null&&data!=null){
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                            plan.setImage(bitmap);
                                        }
                                    }
                                });
                            }

                            //finally,add to planlist
                            planList.add(plan);
                        }
                    }
                }
            });*/
			return null;
		}

        @Override
        protected void onPostExecute(Void aVoid) {
		    /*if(planList.size()>0) {
                mAdapter = new Adapter(DiscoverActivity.this, planList);
                listOfPlans.setAdapter(mAdapter);
            }*/
        }
    }
}
