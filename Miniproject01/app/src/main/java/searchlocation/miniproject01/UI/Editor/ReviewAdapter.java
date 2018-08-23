package searchlocation.miniproject01.UI.Editor;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
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

import searchlocation.miniproject01.Models.Place;
import searchlocation.miniproject01.R;

import static android.content.Context.MODE_PRIVATE;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>{
    private ArrayList<Place> listOfPlaces;
    private int mNumberItems;
    private TextWatcher textWatcher;
    private Context context;
    //public static ArrayList<EditModel> editModelArrayList;

    public void setListOfPlaces(ArrayList<Place> listOfPlaces) {
        this.listOfPlaces = listOfPlaces;
    }

    public void setNumberItems(int mNumberItems) {
        this.mNumberItems = mNumberItems;
    }

    public ArrayList<Place> getListOfPlaces() {
        return listOfPlaces;
    }

    public int getNumberItems() {
        return mNumberItems;
    }

    public void addPlace(Place place){
        listOfPlaces.add(place);
    }
    public ReviewAdapter(ArrayList<Place> places, int number){
        listOfPlaces=places;
        mNumberItems=number;
        //editModelArrayList=editModels;
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
            //holder.editTextListener.updatePosition(position);
        }
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }



    public class ReviewViewHolder extends RecyclerView.ViewHolder{
        protected EditText reviewEditText;
        TextView addressTextView;
        //ReviewCustomEditTextListener editTextListener;

        /*public ReviewViewHolder(View itemView, ReviewCustomEditTextListener customEditTextListener) {
            super(itemView);
            reviewEditText = itemView.findViewById(R.id.edit_review);
            editTextListener = customEditTextListener;
            reviewEditText.addTextChangedListener(editTextListener);
        }*/

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
                public void afterTextChanged(Editable s) {
                    /*listOfPlaces.get(getAdapterPosition()).setReview(s.toString());
                    Log.i("review change ",listOfPlaces.get(getAdapterPosition()).getReview());*/
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
            addressTextView.setText(place.getAddress());
        }


        /*@Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            int index = getAdapterPosition();
            listOfPlaces.get(index).setReview(s.toString());
        }*/
    }

    /*private class ReviewCustomEditTextListener implements TextWatcher{

        private int position;

        public void updatePosition(int position){
            this.position = position;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            listOfPlaces.get(position).setReview(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }*/
}
