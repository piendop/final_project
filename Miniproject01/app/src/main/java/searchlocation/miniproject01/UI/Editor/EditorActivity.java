package searchlocation.miniproject01.UI.Editor;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
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
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import searchlocation.miniproject01.Models.Plan;
import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Database.AppDatabase;
import searchlocation.miniproject01.UI.OnGoing.OnGoingActivity;
import searchlocation.miniproject01.UI.Search.MapsActivity;
import searchlocation.miniproject01.UI.Utilis.PlanAdapter;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener{


    /*********************************************************************************/
    // Constant for logging
    private static final String TAG = EditorActivity.class.getSimpleName();
	private ImageView headingImage;
	private EditText titleEditText;
	private EditText descEditText;
	private Button importImageButton;
	private ImageView addPlace;
    private SharedPreferences sharedPreferences;
    private String planId;
    private ArrayList<Place> places;
    private RecyclerView reviewRecyclerView;
    private ReviewAdapter reviewAdapter;
    private String reviewId;
    private static int NUM_LIST_ITEMS = 0;
    private boolean isNewPlace;
    private boolean isNewPlan;
    private AppDatabase mDb;

    /*************************************************************/
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
                            deleteNewPlan();

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
                            savePlanAndPlaces(title, desc);

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteNewPlan();

                        }
                    })
                    .show();
        }

    }

    private void deleteDatabase() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getInstance(EditorActivity.this).planDao().nukeTable();
                AppDatabase.getInstance(EditorActivity.this).placeDao().nukeTable();
            }
        });
    }

    private void savePlaces() {
        ReviewViewModel viewModel = ViewModelProviders.of(EditorActivity.this).get(ReviewViewModel.class);
        List<Place> places = viewModel.getPlaces().getValue();
        for(Place place:places){
            final ParseObject object = new ParseObject("LocationReview");
            ParseGeoPoint geoPoint = new ParseGeoPoint(place.getLatitude(),place.getLongitude());
            object.put("location",geoPoint);
            object.put("planId",planId);
            object.put("placeName",place.getName());
            object.put("address",place.getAddress());
            object.put("review",place.getReview());
            object.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        Log.i("Review Location ","Successful");
                        sharedPreferences.edit().putString("reviewId",object.getObjectId()).apply();
                        sharedPreferences.edit().putBoolean("isNewPlace",true).apply();
                        Intent intent = new Intent(EditorActivity.this,OnGoingActivity.class);
                        startActivity(intent);
                    }
                }
            });

        }
    }

    private void savePlanAndPlaces(String title, String desc) {
        planId = sharedPreferences.getString("newPlan", null);
        sharedPreferences.edit().putBoolean("isNewPlan",true).apply();
        PlanViewModel planViewModel = ViewModelProviders.of(EditorActivity.this).get(PlanViewModel.class);
        if(planId!=null){
            Plan plan = planViewModel.getPlan().getValue();
            final String _title = plan.getTitle();
            final String _desc = plan.getDesc();
            byte[] data = plan.getData();
            if(data!=null) {
                savePlanWithImage(_title, _desc, data);
            }else{
                savePlanWithNoImage(_title, _desc);
            }
        }else{
            saveNewPlanWithTitleAndDescOnly(title, desc);
        }
    }

    private void saveNewPlanWithTitleAndDescOnly(String title, String desc) {
        ParseObject object = new ParseObject("Plan");
        object.put("title", title);
        object.put("description", desc);
        object.put("userId", ParseUser.getCurrentUser().getObjectId());
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("Title desc only ", "successful");
                    sharedPreferences.edit().putString("newPlan",null).apply();
                    savePlaces();
                    deleteDatabase();
                }
            }
        });
    }

    private void savePlanWithNoImage(final String _title, final String _desc) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Plan");
        query.getInBackground(planId, new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject object, ParseException e) {
                object.put("title", _title);
                object.put("description", _desc);
                object.put("userId", ParseUser.getCurrentUser().getObjectId());
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("Title desc ", "successful");
                            sharedPreferences.edit().putString("newPlan",null).apply();
                            savePlaces();
                            deleteDatabase();
                        }
                    }
                });
            }
        });
    }

    private void savePlanWithImage(final String _title, final String _desc, byte[] data) {
        final ParseFile file = new ParseFile("image.png",data);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Plan");
        query.getInBackground(planId, new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject object, ParseException e) {
                if(e==null){
                    object.put("image",file);
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null){
                                Log.i("Save image"," successful");
                                object.put("title", _title);
                                object.put("description", _desc);
                                object.put("userId", ParseUser.getCurrentUser().getObjectId());
                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        Log.i("Save title desc ","successful");
                                        savePlaces();
                                        deleteDatabase();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    private void deleteNewPlan() {
        sharedPreferences.edit().putString("newPlan",null).apply();
        sharedPreferences.edit().putBoolean("isNewPlan",true).apply();
        planId = sharedPreferences.getString("newPlan", null);
        if(planId!=null){
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Plan");
            query.getInBackground(planId, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if(e==null){
                        Log.i("Delete ","successful");
                        object.deleteInBackground();
                        savePlaces();
                        deleteDatabase();
                    }
                }
            });
        }else{
            Intent intent = new Intent(getApplicationContext(), OnGoingActivity.class);
            startActivity(intent);
        }
    }

    /******************************************************************************/
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);

		headingImage = findViewById(R.id.edit_headingImage);
		titleEditText = findViewById(R.id.edit_title);
		descEditText = findViewById(R.id.edit_description);
		importImageButton = findViewById(R.id.btn_import);
		addPlace = findViewById(R.id.btn_addplace);
        places = new ArrayList<>();
		importImageButton.setOnClickListener(this);
		reviewRecyclerView = findViewById(R.id.rv_reviews);

		addPlace.setOnClickListener(this);



        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        reviewRecyclerView.setLayoutManager(layoutManager);
        //placeRecyclerView.setHasFixedSize(true);
        reviewAdapter = new ReviewAdapter(this);
        reviewRecyclerView.setAdapter(reviewAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        reviewRecyclerView.addItemDecoration(decoration);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<Place> places = reviewAdapter.getListOfPlaces();
                        mDb.placeDao().deletePlace(places.get(position));
                    }
                });
            }
        }).attachToRecyclerView(reviewRecyclerView);

        mDb = AppDatabase.getInstance(getApplicationContext());


	}

    private void newPlan() {

        final ReviewViewModel viewModel = ViewModelProviders.of(this).get(ReviewViewModel.class);
        final PlanViewModel planViewModel = ViewModelProviders.of(this).get(PlanViewModel.class);
        viewModel.getPlaces().observe(this, new Observer<List<Place>>() {
            @Override
            public void onChanged(@Nullable List<Place> places) {
                viewModel.getPlaces().removeObserver(this);
            }
        });
        planViewModel.getPlan().observe(this, new Observer<Plan>() {
            @Override
            public void onChanged(@Nullable Plan plan) {
                planViewModel.getPlan().removeObserver(this);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(MapsActivity.addresses.size()>0) {
            MapsActivity.addresses.clear();
            MapsActivity.locations.clear();
            MapsActivity.names.clear();
            MapsActivity.icon.recycle();
        }
        sharedPreferences = EditorActivity.this.getSharedPreferences("SharedPref",MODE_PRIVATE);
        reviewId = sharedPreferences.getString("reviewId",null);

        isNewPlan = sharedPreferences.getBoolean("isNewPlan",false);
        planId = sharedPreferences.getString("newPlan",null);
        if(isNewPlan) {
            newPlan();
        }else{
            retrievePlacesAndPlan();
        }
    }

    private void retrievePlacesAndPlan() {
        ReviewViewModel viewModel = ViewModelProviders.of(this).get(ReviewViewModel.class);
        PlanViewModel planViewModel = ViewModelProviders.of(this).get(PlanViewModel.class);
        viewModel.getPlaces().observe(this, new Observer<List<Place>>() {
            @Override
            public void onChanged(@Nullable List<Place> places) {
                Log.d(TAG, "Updating lists of reviews from LiveData in ViewModel");
                reviewAdapter.setListOfPlaces(places);
                reviewAdapter.setNumberItems(places.size());
                reviewAdapter.notifyDataSetChanged();
            }
        });

        planViewModel.getPlan().observe(this, new Observer<Plan>() {
            @Override
            public void onChanged(@Nullable Plan plan) {
                if(plan!=null)
                    Log.i("Plan ",plan.toString());
                Log.d(TAG, "Updating plan from LiveData in ViewModel");
                populateUI(plan);
            }
        });
    }

    private void populateUI(Plan plan) {
        if (plan == null) {
            return;
        }

        String title = plan.getTitle();
        String desc = plan.getDesc();
        byte[] data = plan.getData();
        if(data!=null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            headingImage.setImageBitmap(bitmap);
        }
        if(title!=null && !title.isEmpty())
            titleEditText.setText(plan.getTitle());
        if(desc!=null && !desc.isEmpty())
            descEditText.setText(plan.getDesc());

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
        }else if(id == R.id.btn_addplace){
            isNewPlace = sharedPreferences.getBoolean("isNewPlace",false);
            if(planId==null){

                addEmptyPlan();
                //save new plan to local database
                final Plan plan = new Plan(titleEditText.getText().toString(),"",descEditText.getText().toString(),ParseUser.getCurrentUser().getObjectId(),null);
                insertNewPlan(plan);
            }
            else {

                //update the last item of place

                //update all items

                Log.i("Plan id", planId);
                ReviewViewModel viewModel = ViewModelProviders.of(this).get(ReviewViewModel.class);
                List<Place> places = viewModel.getPlaces().getValue();
                for(final Place place :places) {
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDb.placeDao().updatePlace(place);
                        }
                    });
                }
                Intent intent = new Intent(EditorActivity.this,MapsActivity.class);
                startActivity(intent);
            }
        }
    }

    private void insertNewPlan(final Plan plan) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.planDao().insertPlan(plan);
                sharedPreferences.edit().putBoolean("isNewPlan",true).apply();
                Intent intent = new Intent(EditorActivity.this,MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addEmptyPlan() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final ParseObject object = new ParseObject("Plan");
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null) {
                            sharedPreferences.edit().putString("newPlan", object.getObjectId()).apply();
                            sharedPreferences.edit().putBoolean("isNewPlan", true).apply();
                            object.put("userId",ParseUser.getCurrentUser().getObjectId());
                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e==null){
                                        Log.i("Save empty plan", "successful");
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    /******************************************************************************/

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

                if(planId==null) {
                    addEmptyPlan();

                    //save plan to room
                    final Plan plan = new Plan("Your title","","",ParseUser.getCurrentUser().getObjectId(),byteArray);

                    insertNewPlan(plan);


                }else {//plan has been created

                    //update plan
                    PlanViewModel planViewModel = ViewModelProviders.of(this).get(PlanViewModel.class);
                    final Plan plan = planViewModel.getPlan().getValue();
                    plan.setData(byteArray);
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDb.planDao().updatePlan(plan);
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**************************************************************************/

    @Override
    protected void onDestroy() {
        reviewRecyclerView.setVisibility(View.INVISIBLE);
        super.onDestroy();
    }

    /******************************************************************************/

    private class LoadReview extends AsyncTask<Integer, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            reviewRecyclerView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(final Integer... pos) {
            planId = sharedPreferences.getString("newPlan",null);
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


}
