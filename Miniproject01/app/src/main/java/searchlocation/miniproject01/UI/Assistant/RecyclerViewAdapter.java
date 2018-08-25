//package searchlocation.miniproject01.UI.Assistant;
//
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import searchlocation.miniproject01.R;
//
//
//public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//	class ViewHolderUser extends RecyclerView.ViewHolder {
//        TextView tv_user;
//		public ViewHolderUser(View itemView){
//			int layoutForItem = R.layout.layout_custom_chatbox_user;
//			tv_user
//			LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
//	}
//
//	class ViewHolderAssistant extends RecyclerView.ViewHolder {
//		TextView tv_assistant;
//		public ViewHolderAssistant(View itemView){
//			int layoutForItem = R.layout.layout_custom_chatbox_assistant;
//			LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
//		}
//
//		}
//
//		@Override
//		public int getItemViewType(int position) {
//			// Just as an example, return 0 or 2 depending on position
//			// Note that unlike in ListView adapters, types don't have to be contiguous
//			return position % 2 * 2;
//		}
//
//		@Override
//		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//			switch (viewType) {
//				case 0: return new ViewHolderUser(...);
//				case 2: return new ViewHolderAssistant(...);
//             ...
//			}
//		}
//
//		@Override
//		public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
//			switch (holder.getItemViewType()) {
//				case 0:
//					ViewHolderUser viewHolder0 = (ViewHolderUser)holder;
//                ...
//					break;
//
//				case 2:
//					ViewHolderAssistant viewHolder2 = (ViewHolderAssistant)holder;
//                ...
//					break;
//			}
//		}
//	}
