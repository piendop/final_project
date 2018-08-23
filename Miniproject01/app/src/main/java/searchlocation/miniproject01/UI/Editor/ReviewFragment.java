package searchlocation.miniproject01.UI.Editor;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import searchlocation.miniproject01.Models.Place;
import searchlocation.miniproject01.R;

public class ReviewFragment extends Fragment {

    private int numberOfItems;
    ArrayList<Place> listOfPlaces;
    private int index;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    public void setListOfPlaces(ArrayList<Place> listOfPlaces) {
        this.listOfPlaces = listOfPlaces;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ArrayList<Place> getListOfPlaces() {
        return listOfPlaces;
    }

    public int getIndex() {
        return index;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public interface OnEditTextListener{
        void onEditTextWatcher(int position);
    }

    public ReviewFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_review_list,container,false);
        ListView listView = rootView.findViewById(R.id.lv_review);
        /*ReviewAdapter mAdapter = new ReviewAdapter(getContext(),listOfPlaces);
        listView.setAdapter(mAdapter);*/
        return rootView;
    }
}
