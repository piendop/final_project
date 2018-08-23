package searchlocation.miniproject01.UI.profile;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import searchlocation.miniproject01.Models.Place;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>{


    int mNumberOfItems;
    ArrayList<Place> places;


    public ProfileAdapter(int number, ArrayList<Place> places){
        this.mNumberOfItems = number;
        this.places =places;
    }

    public void setNumberOfItems(int mNumberOfItems) {
        this.mNumberOfItems = mNumberOfItems;
    }

    public int getNumberOfItems() {
        return mNumberOfItems;
    }

    public void setPlaces(ArrayList<Place> places) {
        this.places = places;
    }

    public ArrayList<Place> getPlaces() {
        return places;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        if(places.size()>0){
            holder.bind(position);
        }
    }

    @Override
    public int getItemCount() {
        return mNumberOfItems;
    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder{

        public ProfileViewHolder(View itemView) {
            super(itemView);
        }

        void bind(int itemIndex){

        }
    }
}
