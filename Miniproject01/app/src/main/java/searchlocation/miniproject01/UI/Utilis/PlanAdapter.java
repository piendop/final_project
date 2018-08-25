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

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {

	private ArrayList<Plan> listOfPlans;
	private int mNumberItems=0;
	final private OnBottomReachedListener onBottomReachedListener;
	final private PlanAdapterOnClickHandler mClickHander;

	public PlanAdapter(int numberOfItems, ArrayList<Plan> list, OnBottomReachedListener onBottomReachedListener, PlanAdapterOnClickHandler clickHandler){

        if(!list.isEmpty()){
            listOfPlans = list;
        }else{
            listOfPlans = new ArrayList<>();
        }
	    mNumberItems=numberOfItems;
        this.onBottomReachedListener = onBottomReachedListener;
        this.mClickHander = clickHandler;
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

    public interface PlanAdapterOnClickHandler{
        void onClick(Plan itemPlan);
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
	    Context context = parent.getContext();
	    int layoutForItem = R.layout.layout_custom_card_1;
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
	        onBottomReachedListener.onBottomReached(position);
        }
    }


    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    class PlanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

	    ImageView image;
	    TextView title;
	    TextView hashtag;
	    TextView desc;

        public PlanViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.headingImage);
            title = itemView.findViewById(R.id.card_title);
            hashtag = itemView.findViewById(R.id.card_tags);
            desc = itemView.findViewById(R.id.card_desc);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex){
            if(listIndex<listOfPlans.size()) {
                Plan plan = listOfPlans.get(listIndex);

                if(plan.getImage()!=null){
                    image.setImageBitmap(plan.getImage());
                }else{
                    image.setImageResource(R.drawable.image2);
                }

                if(plan.getTitle()!=null &&!plan.getTitle().isEmpty()){
                    title.setText(plan.getTitle());
                }else{
                    title.setText("No title");
                }
                if (plan.getTags() == null || plan.getTags().isEmpty()) {
                    hashtag.setText("No hashtag");
                } else {
                    hashtag.setText(plan.getTags());
                }
                if(plan.getDesc()!=null &&!plan.getDesc().isEmpty()){
                    desc.setText(plan.getDesc());
                }else{
                    desc.setText("No description");
                }

            }
        }

        @Override
        public void onClick(View v) {

            int index = getAdapterPosition();
            Plan plan = listOfPlans.get(index);
            mClickHander.onClick(plan);
        }
    }
}
