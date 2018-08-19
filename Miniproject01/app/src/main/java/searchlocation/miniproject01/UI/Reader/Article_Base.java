package searchlocation.miniproject01.UI.Reader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import searchlocation.miniproject01.Models.Plan;
import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Utilis.BottomNavigationReader;
import searchlocation.miniproject01.UI.Utilis.BottomNavigationViewHelper;

public class Article_Base extends AppCompatActivity {

    private ImageView imagePlan;
    private TextView title;
    private TextView description;
    private LinearLayout planInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_base);
		setupBottomNavigationReader();
		imagePlan = findViewById(R.id.headingImage);
		title = findViewById(R.id.tv_title);
		description = findViewById(R.id.tv_description);
		planInfo = findViewById(R.id.plan_info);
        final String objectId = getIntent().getStringExtra("objectId");
        Log.i("Id ",objectId);

        //set image and text for plan
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Plan");
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject object, ParseException e) {
                if(e==null){
                    ParseFile file = object.getParseFile("image");
                    if(file!=null){
                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if(e==null){
                                    Log.i("Get image", "successful");
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    imagePlan.setImageBitmap(bitmap);
                                    title.setText(object.getString("title"));
                                    description.setText(object.getString("description"));
                                    planInfo.setVisibility(View.VISIBLE);
                                }else{
                                    Log.i("Get image", "failed");
                                }
                            }
                        });
                    }
                }else{
                    Log.i("Could", "not load object");
                }
            }
        });
	}
	public void setupBottomNavigationReader(){
		AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottomNavigationReader);
		BottomNavigationReader.setupBottomNavigationView(bottomNavigation);
		BottomNavigationReader.enableBottomNavigation(Article_Base.this,bottomNavigation);
	}
}
