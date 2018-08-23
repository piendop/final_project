package searchlocation.miniproject01.UI.Editor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import searchlocation.miniproject01.Models.Place;
import searchlocation.miniproject01.R;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>{
    private ArrayList<Place> listOfPlaces;
    private int mNumberItems;
    private TextWatcher textWatcher;
    public static ArrayList<EditModel> editModelArrayList;

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

    public ReviewAdapter(ArrayList<Place> places, int number,ArrayList<EditModel> editModels){
        listOfPlaces=places;
        mNumberItems=number;
        editModelArrayList=editModels;
    }
    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutForItem = R.layout.fragment_location_editor;
        LayoutInflater inflater =LayoutInflater.from(context);
        View view = inflater.inflate(layoutForItem,parent,false);
        ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view,new ReviewCustomEditTextListener());
        return reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        if(listOfPlaces.size()>0){
            holder.bind(position);
            holder.editTextListener.updatePosition(position);
        }
    }

    @Override
    public int getItemCount() {
        return listOfPlaces.size();
    }



    public class ReviewViewHolder extends RecyclerView.ViewHolder implements TextWatcher{
        protected EditText reviewEditText;
        ReviewCustomEditTextListener editTextListener;

        public ReviewViewHolder(View itemView, ReviewCustomEditTextListener customEditTextListener) {
            super(itemView);
            reviewEditText = itemView.findViewById(R.id.edit_review);
            editTextListener = customEditTextListener;
            reviewEditText.addTextChangedListener(editTextListener);
        }

        public ReviewViewHolder(View itemView){
            super(itemView);
            reviewEditText = itemView.findViewById(R.id.edit_review);
            reviewEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    editModelArrayList.get(getAdapterPosition()).setEditTextValue(reviewEditText.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        void bind(int itemIndex){
            Place place = listOfPlaces.get(itemIndex);
            String review = place.getReview();
            if(review!=null && !review.isEmpty()){
                reviewEditText.setText(place.getReview());
            }else{
                reviewEditText.setText(editModelArrayList.get(itemIndex).getEditTextValue());
            }
        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int index = getAdapterPosition();
            listOfPlaces.get(index).setReview(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private class ReviewCustomEditTextListener implements TextWatcher{

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
    }

    /*private Context mContext;
    private ArrayList<Place> places;

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public Object getItem(int position) {
        return places.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ReviewAdapter(Context context, ArrayList<Place> places){
        mContext=context;
        this.places=places;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EditText editText;
        if(convertView==null){
            editText = new EditText(mContext);
            editText.setTextSize(16);
            editText.setPadding(0,0,0,8);
        }else{
            editText = (EditText)convertView;
        }
        String review = places.get(position).getReview();
        if(review!=null && !review.isEmpty())
            editText.setText(review);
        else editText.setText("");
        return editText;
    }*/
}
