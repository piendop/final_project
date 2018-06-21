package searchlocation.miniproject01;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

import searchlocation.miniproject01.Utilis.Adapter;
import searchlocation.miniproject01.Utilis.BottomNavigationViewHelper;

public class SharePlanActivity extends AppCompatActivity {
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
		setContentView(R.layout.activity_shareplans);
		listOfPlans = (ListView) findViewById(R.id.list_plans);
		setupBottomNavigationView();

		ArrayList<Plans> plansList = new ArrayList<>();
		plansList.add(new Plans(data[0][0], data[0][1], "Mì Quảng, bún mắm nêm, gỏi mít, ốc hút, là những món đặc trưng nhất định phải thử khi đến Đà Nẵng.",imgId[0]));
		plansList.add(new Plans(data[1][0],  data[1][1], data[1][2],imgId[1]));
		plansList.add(new Plans(data[2][0],  data[2][1], data[2][2],imgId[2]));

		mAdapter = new Adapter(this,plansList);
		listOfPlans.setAdapter(mAdapter);

	}

	public void setupBottomNavigationView(){
		BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavigation);
		BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
		BottomNavigationViewHelper.enableBottomNavigation(SharePlanActivity.this,bottomNavigationViewEx);

	}

}
