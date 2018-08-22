package searchlocation.miniproject01.UI.Editor;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.github.irshulx.Editor;
import com.github.irshulx.models.EditorTextStyle;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.OnGoing.OnGoingActivity;
import searchlocation.miniproject01.UI.Utilis.BottomNavigationViewHelper;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener {

	private ImageView headingImage;
	private EditText titleEditText;
	private EditText descEditText;
	private Button importImageButton;
	private LinearLayout linearLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);
		headingImage = findViewById(R.id.edit_headingImage);
		titleEditText = findViewById(R.id.edit_title);
		descEditText = findViewById(R.id.edit_description);
		importImageButton = findViewById(R.id.btn_import);
		linearLayout = findViewById(R.id.plan_info);

		importImageButton.setOnClickListener(this);

	}


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.btn_import){
            if(Build.VERSION.SDK_INT<23){
                getPhoto();
            }else {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    getPhoto();
                }
            }
        }
    }

    private void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == RESULT_OK && data!=null){

            //uri link image with app
            Uri selectedImage  = data.getData();

            try {

                final Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                Log.i("Photo","Received");
                /****UPLOAD PHOTO TO PARSE SERVER***/
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
                headingImage.setImageBitmap(rotatedBitmap);


                rotatedBitmap.compress(Bitmap.CompressFormat.PNG,100,stream);

                byte[] byteArray = stream.toByteArray();

                ParseFile file = new ParseFile("image.png",byteArray);

                final ParseObject object = new ParseObject("Plan");

                object.put("image",file);

                object.put("userId", ParseUser.getCurrentUser().getUsername());

                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            Log.i("Image ","uploaded");
                            //headingImage.setImageBitmap(bitmap);
                            SharedPreferences sharedPreferences = EditorActivity.this.getSharedPreferences("SharedPref",MODE_PRIVATE);
                            sharedPreferences.edit().putString("newPlan",object.getObjectId()).apply();
                        }else{
                            Toast.makeText(getApplicationContext(),"Image could not be uploaded - please try again later",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
