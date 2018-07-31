package searchlocation.miniproject01.UI.Utilis;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class IntroCustomLayout extends Fragment  {
	private static final String ARG_LAYOUT_RES_ID = "layoutResId";
	private int layoutResId;

	public static android.support.v4.app.Fragment newInstance(int layoutResId) {
		IntroCustomLayout sampleSlide = new IntroCustomLayout();

		Bundle args = new Bundle();
		args.putInt(ARG_LAYOUT_RES_ID, layoutResId);
		sampleSlide.setArguments(args);

		return sampleSlide;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID)) {
			layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
													 @Nullable Bundle savedInstanceState) {
		return inflater.inflate(layoutResId, container, false);
	}
}
