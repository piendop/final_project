package searchlocation.miniproject01.UI.Editor;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import searchlocation.miniproject01.Models.Place;
import searchlocation.miniproject01.Models.Plan;
import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Database.AppDatabase;

import static android.content.Context.MODE_PRIVATE;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>{
    private List<Place> listOfPlaces;
    private int mNumberItems;
    private TextWatcher textWatcher;
    private Context context;
    //public static ArrayList<EditModel> editModelArrayList;
    private static final String TAG = ReviewAdapter.class.getSimpleName();

    public void setListOfPlaces(List<Place> places) {
        Log.i("Place ",places.toString());
        listOfPlaces=places;
    }

    public Place getPlace(int position){
        return listOfPlaces.get(position);
    }
    public void setNumberItems(int mNumberItems) {
        this.mNumberItems = mNumberItems;
    }

    public List<Place> getListOfPlaces() {
        return listOfPlaces;
    }

    public int getNumberItems() {
        return mNumberItems;
    }

    public void addPlace(Place place){
        listOfPlaces.add(place);
    }

    public ReviewAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutForItem = R.layout.layout_custom_reviewitem;
        LayoutInflater inflater =LayoutInflater.from(context);
        View view = inflater.inflate(layoutForItem,parent,false);
        //ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view,new ReviewCustomEditTextListener());
        ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view);
        return reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        if(listOfPlaces.size()>0){
            holder.bind(position);
        }
        /*Place place = listOfPlaces.get(position);
        String review = place.getReview();
        if(review!=null && !review.isEmpty()){
            holder.reviewEditText.setText(place.getReview());
        }else{
            holder.reviewEditText.setText("Write your review...");
        }
        holder.addressTextView.setText(place.getAddress());*/
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }





    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        EditText reviewEditText;
        TextView addressTextView;

        public ReviewViewHolder(View itemView){
            super(itemView);
            reviewEditText = itemView.findViewById(R.id.edit_review);
            addressTextView = itemView.findViewById(R.id.tv_address);
            reviewEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //listOfPlaces.get(getAdapterPosition()).setReview(reviewEditText.getText().toString());
                }

                @Override
                public void afterTextChanged(final Editable s) {
                    listOfPlaces.get(getAdapterPosition()).setReview(s.toString());
                    Log.i("review change ",listOfPlaces.get(getAdapterPosition()).getReview());
                    //AppDatabase.getInstance(context).placeDao().updatePlace(listOfPlaces.get(getAdapterPosition()));
                    SharedPreferences sharedPreferences = context.getSharedPreferences
                            ("SharedPref",MODE_PRIVATE);
                    sharedPreferences.edit().putString("review",s.toString()).apply();
                }
            });
        }
        void bind(int itemIndex){
            Place place = listOfPlaces.get(itemIndex);
            String review = place.getReview();
            if(review!=null && !review.isEmpty()){
                reviewEditText.setText(place.getReview());
            }else{
                reviewEditText.setText("Write your review...");
            }
            if(place.getAddress()!=null && !place.getAddress().isEmpty())
                addressTextView.setText(place.getAddress());
        }
    }
}
