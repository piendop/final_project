package searchlocation.miniproject01.UI.Utilis;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import searchlocation.miniproject01.Models.ColorTag;
import searchlocation.miniproject01.Models.Place;
import searchlocation.miniproject01.Models.Place;
import searchlocation.miniproject01.R;

public class PlaceItemAdapter extends RecyclerView.Adapter<PlaceItemAdapter.PlaceViewHolder> {

	private List<Place> listOfPlaces;
	private int mNumberItems;
	final private OnBottomReachedListener onBottomReachedListener;
	final private PlaceAdapterOnClickHandler mClickHander;

	public PlaceItemAdapter(int numberOfItems, ArrayList<Place> list, OnBottomReachedListener onBottomReachedListener, PlaceAdapterOnClickHandler clickHandler){
		listOfPlaces = list;
		mNumberItems=numberOfItems;
		this.onBottomReachedListener = onBottomReachedListener;
		this.mClickHander = clickHandler;
	}

	public void addPlace(Place place){
		listOfPlaces.add(place);
	}

	public void setListOfPlaces(List<Place> listOfPlaces) {
		this.listOfPlaces = listOfPlaces;
	}

	public void setmNumberItems(int mNumberItems) {
		this.mNumberItems = mNumberItems;
	}

	public interface OnBottomReachedListener{
		void onBottomReached(int position);
	}

	public interface PlaceAdapterOnClickHandler{
		void onClick(String itemName);
	}

	@NonNull
	@Override
	public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		int layoutForItem = R.layout.layout_custom_card_placeitem;
		LayoutInflater inflater = LayoutInflater.from(context);
		boolean shouldAttachToParentImmediately = false;
		View view = inflater.inflate(layoutForItem,parent,shouldAttachToParentImmediately);
		PlaceViewHolder placeViewHolder = new PlaceViewHolder(view);
		//Cho nay sau nay custom
		int backgroundColor = ColorTag.getColorTag(context,1);
		Button button = placeViewHolder.itemView.findViewById(R.id.btn_place);
		button.setBackgroundColor(backgroundColor);
		return placeViewHolder;
	}

	@Override
	public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
		if(listOfPlaces.size()>0)
			holder.bind(position);
		if(position == listOfPlaces.size()-1){
			onBottomReachedListener.onBottomReached(position);
		}
	}


	@Override
	public int getItemCount() {
		return mNumberItems;
	}

	class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		Button placeButton;
		TextView review;

		public PlaceViewHolder(View itemView) {
			super(itemView);
			placeButton = itemView.findViewById(R.id.btn_place);
			review = itemView.findViewById(R.id.tv_review);
			placeButton.setOnClickListener(this);
		}

		void bind(int listIndex){
			Place place = listOfPlaces.get(listIndex);
			placeButton.setText(place.getName());
			review.setText(place.getReview());
		}

		@Override
		public void onClick(View v) {
			int index = getAdapterPosition();
			Place place = listOfPlaces.get(index);
			String namePlace = place.getName();
			mClickHander.onClick(namePlace);
		}
	}
}
