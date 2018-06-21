package searchlocation.miniproject01.Utilis;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import searchlocation.miniproject01.Plans;
import searchlocation.miniproject01.R;

public class Adapter extends ArrayAdapter<Plans> {

	private static LayoutInflater inflater = null;
	private Context mContext;
	private String[][] data;
	private List<Plans> listOfPlans = new ArrayList<>();

	public Adapter(@Nonnull Context context, ArrayList<Plans> list) {
		super(context, 0 , list);
		mContext = context;
		listOfPlans = list;
	}
	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		View listItem = convertView;
		if(listItem == null)
			listItem = LayoutInflater.from(mContext).inflate(R.layout.layout_custom_card_1,parent,false);

		Plans currentMovie = listOfPlans.get(position);

		ImageView image = (ImageView)listItem.findViewById(R.id.card_img);
		image.setImageResource(currentMovie.getImgId());

		TextView title = (TextView) listItem.findViewById(R.id.card_title);
		title.setText(currentMovie.getTitle());

		TextView tag = (TextView) listItem.findViewById(R.id.card_tags);
		tag.setText(currentMovie.getTags());

		TextView desc = (TextView) listItem.findViewById(R.id.card_desc);
		desc.setText(currentMovie.getDesc());

		return listItem;
	}
}
