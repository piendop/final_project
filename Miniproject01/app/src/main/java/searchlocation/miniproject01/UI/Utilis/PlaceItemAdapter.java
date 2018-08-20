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
import searchlocation.miniproject01.Models.Plan;
import searchlocation.miniproject01.R;

public class PlaceItemAdapter extends RecyclerView.Adapter<PlaceItemAdapter.PlanViewHolder> {

	private List<Place> listOfPlaces;
	private int mNumberItems;
	final private OnBottomReachedListener onBottomReachedListener;
	final private PlanAdapterOnClickHandler mClickHander;

	public PlaceItemAdapter(int numberOfItems, ArrayList<Place> list, OnBottomReachedListener onBottomReachedListener, PlanAdapterOnClickHandler clickHandler){
		listOfPlaces = list;
		mNumberItems=numberOfItems;
		this.onBottomReachedListener = onBottomReachedListener;
		this.mClickHander = clickHandler;
	}

	public void addPlan(Place place){
		listOfPlaces.add(place);
	}

	public void setListOfPlans(List<Place> listOfPlaces) {
		this.listOfPlaces = listOfPlaces;
	}

	public void setmNumberItems(int mNumberItems) {
		this.mNumberItems = mNumberItems;
	}

	public interface OnBottomReachedListener{
		void onBottomReached(int position);
	}

	public interface PlanAdapterOnClickHandler{
		void onClick(String itemName);
	}

	@NonNull
	@Override
	public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		int layoutForItem = R.layout.layout_custom_card_placeitem;
		LayoutInflater inflater = LayoutInflater.from(context);
		boolean shouldAttachToParentImmediately = false;
		View view = inflater.inflate(layoutForItem,parent,shouldAttachToParentImmediately);
		PlanViewHolder planViewHolder = new PlanViewHolder(view);
		//Cho nay sau nay custom
		int backgroundColor = ColorTag.getColorTag(context,1);
		Button button = planViewHolder.itemView.findViewById(R.id.btn_place);
		button.setBackgroundColor(backgroundColor);
		return planViewHolder;
	}

	@Override
	public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
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

	class PlanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		Button placeButton;
		TextView review;

		public PlanViewHolder(View itemView) {
			super(itemView);
			placeButton = itemView.findViewById(R.id.btn_place);
			review = itemView.findViewById(R.id.tv_review);
			itemView.setOnClickListener(this);
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
