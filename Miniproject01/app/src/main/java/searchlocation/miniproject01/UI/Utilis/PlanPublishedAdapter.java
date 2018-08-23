package searchlocation.miniproject01.UI.Utilis;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import searchlocation.miniproject01.Models.Plan;
import searchlocation.miniproject01.R;

public class PlanPublishedAdapter extends RecyclerView.Adapter<PlanPublishedAdapter.PlanViewHolder> {

	private ArrayList<Plan> listOfPlans;
	private int mNumberItems=0;

	public PlanPublishedAdapter(int numberOfItems, ArrayList<Plan> list){

		if(!list.isEmpty()){
			listOfPlans = list;
		}else{
			listOfPlans = new ArrayList<>();
		}
		mNumberItems=numberOfItems;
	}

	public void addPlan(Plan plan){
		listOfPlans.add(plan);
	}

	public void setListOfPlans(ArrayList<Plan> listOfPlans) {
		this.listOfPlans = listOfPlans;
	}

	public void setmNumberItems(int mNumberItems) {
		this.mNumberItems = mNumberItems;
	}

	public interface OnBottomReachedListener{
		void onBottomReached(int position);
	}

	public interface PlanPublishedAdapterOnClickHandler{
		void onClick(Plan itemPlan);
	}

	@NonNull
	@Override
	public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		int layoutForItem = R.layout.layout_custom_card_published;
		LayoutInflater inflater = LayoutInflater.from(context);
		boolean shouldAttachToParentImmediately = false;

		View view = inflater.inflate(layoutForItem,parent,shouldAttachToParentImmediately);

		return new PlanViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
		if(listOfPlans.size()>0)
			holder.bind(position);
		if(position == listOfPlans.size()-1){
		}
	}


	@Override
	public int getItemCount() {
		return mNumberItems;
	}

	class PlanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		TextView title;
		TextView desc;

		public PlanViewHolder(View itemView) {
			super(itemView);
			title = itemView.findViewById(R.id.cardpublished_title);
			desc = itemView.findViewById(R.id.cardpublished_description);
			itemView.setOnClickListener(this);
		}

		void bind(int listIndex){
			Plan plan = listOfPlans.get(listIndex);
			title.setText(plan.getTitle());
			desc.setText(plan.getDesc());
		}

		@Override
		public void onClick(View v) {
			int index = getAdapterPosition();
			Plan plan = listOfPlans.get(index);
		}
	}
}
