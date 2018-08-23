package searchlocation.miniproject01.UI.Editor;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import searchlocation.miniproject01.Models.Place;
import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.OnGoing.OnGoingActivity;
import searchlocation.miniproject01.UI.Search.MapsActivity;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener{

	private ImageView headingImage;
	private EditText titleEditText;
	private EditText descEditText;
	private Button importImageButton;
	private Button addPlaceButton;
    private SharedPreferences sharedPreferences;
    private String planId;
    private ArrayList<Place> places;
    private RecyclerView reviewRecyclerView;
    private ReviewAdapter reviewAdapter;
    private String reviewId;
    private ArrayList<EditModel> editModelArrayList;
    private static int NUM_LIST_ITEMS = 0;


    @Override
    public void onBackPressed() {
        final String title = titleEditText.getText().toString();
        final String desc = descEditText.getText().toString();
        if (title.isEmpty() || desc.isEmpty()) {
            new AlertDialog.Builder(EditorActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("There are no title or description!")
                    .setMessage("Do you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            sharedPreferences.edit().putString("newPlan",null).apply();
                            Intent intent = new Intent(getApplicationContext(), OnGoingActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Are you sure?")
                    .setMessage("Do you want to save this plan?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            planId = sharedPreferences.getString("newPlan", null);


                            if (planId != null) {
                                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Plan");
                                query.getInBackground(planId, new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(final ParseObject object, ParseException e) {
                                        object.put("title", title);
                                        object.put("description", desc);
                                        object.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    Log.i("Title desc ", "successful");
                                                    sharedPreferences.edit().putString("newPlan",null).apply();
                                                    Intent intent = new Intent(getApplicationContext(), OnGoingActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                    }
                                });
                            } else {
                                final ParseObject object = new ParseObject("Plan");
                                addSampleImage(object, title, desc);

                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sharedPreferences.edit().putString("newPlan",null).apply();
                            Intent intent = new Intent(getApplicationContext(), OnGoingActivity.class);
                            startActivity(intent);
                        }
                    })
                    .show();
        }

    }

    private void addSampleImage(final ParseObject object, final String title, final String desc) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image2);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] array = stream.toByteArray();
        ParseFile file = new ParseFile("image.png",array);
        object.put("image",file);
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Log.i("Image null ","successful");
                    object.put("title", title);
                    object.put("description", desc);
                    object.put("userId", ParseUser.getCurrentUser().getObjectId());
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.i("Title desc only ", "successful");
                                sharedPreferences.edit().putString("newPlan",null).apply();
                                Intent intent = new Intent(getApplicationContext(), OnGoingActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);
        if(MapsActivity.addresses.size()>0) {
            MapsActivity.addresses.clear();
            MapsActivity.locations.clear();
            MapsActivity.names.clear();
            MapsActivity.icon.recycle();
        }
		headingImage = findViewById(R.id.edit_headingImage);
		titleEditText = findViewById(R.id.edit_title);
		descEditText = findViewById(R.id.edit_description);
		importImageButton = findViewById(R.id.btn_import);
		addPlaceButton = findViewById(R.id.bt_add_place);
        sharedPreferences = EditorActivity.this.getSharedPreferences("SharedPref",MODE_PRIVATE);
        //init new plan is null
        //sharedPreferences.edit().putString("newPlan",null).apply();

        planId = sharedPreferences.getString("newPlan",null);
        reviewId = sharedPreferences.getString("reviewId",null);
        places = new ArrayList<>();
		importImageButton.setOnClickListener(this);
		reviewRecyclerView = findViewById(R.id.rv_reviews);

		addPlaceButton.setOnClickListener(this);
		editModelArrayList = new ArrayList<>();


        Boolean isNewPlace = getIntent().getBooleanExtra("isNewPlace",false);

        if(isNewPlace){
            NUM_LIST_ITEMS=0;
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            reviewRecyclerView.setLayoutManager(layoutManager);
            //placeRecyclerView.setHasFixedSize(true);
            reviewAdapter = new ReviewAdapter(places, NUM_LIST_ITEMS);
            reviewRecyclerView.setAdapter(reviewAdapter);
            new LoadReview().execute();
        }
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
        }else if(id == R.id.bt_add_place){
            planId = sharedPreferences.getString("newPlan",null);
            if(planId==null){
                final ParseObject object = new ParseObject("Plan");
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image2);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                byte[] array = stream.toByteArray();
                ParseFile file = new ParseFile("image.png",array);
                object.put("image",file);
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            Log.i("Image null ","successful");
                            object.put("userId",ParseUser.getCurrentUser().getObjectId());
                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e==null){
                                        Log.i("Empty plan ","successful");
                                        Log.i("Plan id ",object.getObjectId());
                                        sharedPreferences.edit().putString("newPlan",object.getObjectId()).apply();
                                        Intent intent = new Intent(EditorActivity.this,MapsActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    }
                });
            }
            else{//save review to parse
                Log.i("Plan id",planId);
                saveReviewToParse();
            }

        }
    }

    private void saveReviewToParse() {
        final String review = sharedPreferences.getString("review",null);
        if(review!=null && reviewId!=null) {
            Log.i("review ",review);
            Log.i("review id ",reviewId);

            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("LocationReview");
            query.setLimit(1);
            query.getInBackground(reviewId, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if(e==null){
                        object.put("review",review);
                        object.put("planId",planId);
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e==null){
                                    Log.i("Save review ","successful");
                                    Intent intent = new Intent(EditorActivity.this,MapsActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                }
            });
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


                final ParseFile file = new ParseFile("image.png", byteArray);

                if(planId==null) {


                    final ParseObject object = new ParseObject("Plan");

                    object.put("image", file);

                    object.put("userId", ParseUser.getCurrentUser().getObjectId());

                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.i("Image ", "uploaded");
                                //headingImage.setImageBitmap(bitmap);
                                sharedPreferences.edit().putString("newPlan", object.getObjectId()).apply();
                            } else {
                                Toast.makeText(getApplicationContext(), "Image could not be uploaded - please try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{//plan has been created

                    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Plan");
                    query.getInBackground(planId, new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            if(e==null){
                                object.put("image",file);

                                object.put("userId", ParseUser.getCurrentUser().getObjectId());

                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            Log.i("Image ", "uploaded");
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Image could not be uploaded - please try again later", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private class LoadReview extends AsyncTask<Integer, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            reviewRecyclerView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(final Integer... pos) {
            if(planId!=null) {
                Log.i("Plan id",planId);
                ParseQuery<ParseObject> query = new ParseQuery<>("LocationReview");
                query.whereEqualTo("planId",planId);
                query.setLimit(10);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e==null && objects.size()>0){
                            for(ParseObject object :objects){
                                final Place place = new Place();
                                place.setName(object.getString("placeName"));
                                place.setID(object.getObjectId());
                                place.setReview(object.getString("review"));
                                ++NUM_LIST_ITEMS;
                                reviewAdapter.setNumberItems(NUM_LIST_ITEMS);
                                reviewAdapter.addPlace(place);
                                reviewAdapter.notifyItemInserted(NUM_LIST_ITEMS);
                                reviewRecyclerView.setVisibility(View.VISIBLE);
                            }
                        }else {
                            Log.i("Object", "cannot load more");
                        }
                    }
                });
            }
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        reviewRecyclerView.setVisibility(View.INVISIBLE);
        super.onDestroy();
    }
}
