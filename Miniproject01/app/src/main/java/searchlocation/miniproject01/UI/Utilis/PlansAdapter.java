package searchlocation.miniproject01.UI.Utilis;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;



import searchlocation.miniproject01.Models.Plan;
import searchlocation.miniproject01.R;
/*
public class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.MyViewHolder> {

	private List<Plan> planList;
	private LayoutInflater layoutInflater;
	private PostsAdapterListener listener;

	public class MyViewHolder extends RecyclerView.ViewHolder {

		private final ActivityDiscoverBinding binding;

		public MyViewHolder(final ActivityDiscoverBinding itemBinding) {
			super(itemBinding.getRoot());
			this.binding = itemBinding;
		}
	}

	public PlansAdapter(List<Plan> planList, PostsAdapterListener listener) {
		this.planList = planList;
		this.listener = listener;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (layoutInflater == null) {
			layoutInflater = LayoutInflater.from(parent.getContext());
		}
		ActivityDiscoverBinding binding =
				DataBindingUtil.inflate(layoutInflater, R.layout.layout_custom_card_1, parent, false);
		return new MyViewHolder(binding);
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, final int position) {
		holder.binding.setPlans(planList.get(position));
		holder.binding.
		holder.binding.headingImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onPostClicked(planList.get(position));
				}
			}
		});
	}

	@Override
	public int getItemCount() {
		return planList.size();
	}

	public interface PostsAdapterListener {
		void onPostClicked(Plan post);
	}
}

public class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.My> {

public class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.MyViewHolder>{

	private List<Plan> planList;
	private LayoutInflater layoutInflater;
	private PlansAdapterListener listener;

	public class MyViewHolder extends RecyclerView.Adapter {
		private final card binding;

		public MyViewHolder(final card itemBinding){
			super(itemBinding.getRoot());
			this.binding = itemBinding;
		}
	}
	@Override
	public int getItemCount() {
		return planList.size();
	}
}*/
