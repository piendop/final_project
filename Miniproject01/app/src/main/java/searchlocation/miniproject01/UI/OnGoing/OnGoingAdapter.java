package searchlocation.miniproject01.UI.OnGoing;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import searchlocation.miniproject01.Models.Place;
import searchlocation.miniproject01.R;

public class OnGoingAdapter extends RecyclerView.Adapter<OnGoingAdapter.LocationViewHolder>{
    private List<Place> listOfPlaces;
    private int mNumberItems;
    final private OnBottomReachedListener onBottomReachedListener;
    final private OnGoingAdapterOnClickHandler onGoingAdapterOnClickHandler;

    public OnGoingAdapter(List<Place> listOfPlaces, int mNumberItems, OnBottomReachedListener onBottomReachedListener, OnGoingAdapterOnClickHandler onGoingAdapterOnClickHandler) {
        this.listOfPlaces = listOfPlaces;
        this.mNumberItems = mNumberItems;
        this.onBottomReachedListener = onBottomReachedListener;
        this.onGoingAdapterOnClickHandler = onGoingAdapterOnClickHandler;
    }


    public void addPlace(Place place){
        listOfPlaces.add(place);
    }

    public void setListOfPlaces(List<Place> listOfPlaces) {
        this.listOfPlaces = listOfPlaces;
    }

    public void setNumberItems(int mNumberItems) {
        this.mNumberItems = mNumberItems;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutForItem = R.layout.layout_custom_card_ongoinplace;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutForItem,parent,false);
        LocationViewHolder locationViewHolder = new LocationViewHolder(view);

        return locationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        if(listOfPlaces.size()>0){
            holder.bind(position);
        }
        if(position==listOfPlaces.size()-1){
            onBottomReachedListener.onBottomReached(position);
        }
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public interface OnBottomReachedListener{
        void onBottomReached(int position);
    }

    public interface OnGoingAdapterOnClickHandler{
        void onClick(Place itemPlace);
    }

    class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        TextView address;

        public LocationViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            address = itemView.findViewById(R.id.tv_address);
            itemView.setOnClickListener(this);
        }

        void bind(int itemIndex){
            Place place = listOfPlaces.get(itemIndex);
            name.setText(place.getName());
            address.setText(place.getAddress());
        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            Place place = listOfPlaces.get(index);
            onGoingAdapterOnClickHandler.onClick(place);
        }
    }
}
