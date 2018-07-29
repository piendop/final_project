package searchlocation.miniproject01.UI.Discover;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;

import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Utilis.Adapter;
import searchlocation.miniproject01.UI.Utilis.BottomNavigationViewHelper;

public class DiscoverActivity extends AppCompatActivity {
	private ListView listOfPlans;
	private Adapter mAdapter;

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
		new MultiplyTask().execute();
		//listOfPlans = (ListView) findViewById(R.id.list_plans);

		/*
		ArrayList<Plan> planList = new ArrayList<>();
		planList.add(new Plan(data[0][0], data[0][1], "Mì Quảng, bún mắm nêm, gỏi mít, ốc hút, là những món đặc trưng nhất định phải thử khi đến Đà Nẵng.",imgId[0]));
		planList.add(new Plan(data[1][0],  data[1][1], data[1][2],imgId[1]));
		planList.add(new Plan(data[2][0],  data[2][1], data[2][2],imgId[2]));

		mAdapter = new Adapter(this, planList);
		listOfPlans.setAdapter(mAdapter);*/

	}

	public void setupBottomNavigationView(){
		AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottomNavigation);
		BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigation);
		BottomNavigationViewHelper.enableBottomNavigation(DiscoverActivity.this,bottomNavigation);
	}
	public class MultiplyTask extends AsyncTask<String,String,String>
	{
		//Prepare layout
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Toast.makeText(DiscoverActivity.this, "Load layout while fetching data in background", Toast.LENGTH_SHORT).show();
			setupBottomNavigationView();
		}
		//Bind data to layout
		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			Toast.makeText(DiscoverActivity.this, "Render all posts", Toast.LENGTH_SHORT).show();
		}

		//Fetch data from server here
		@Override
		protected String doInBackground(String... strings) {
			return null;
		}
	}
}
