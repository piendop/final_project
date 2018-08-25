package searchlocation.miniproject01.UI.Assistant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import searchlocation.miniproject01.R;

public class OptionMenuFragment extends Fragment {

	private TextView tv_option1;
	private TextView tv_option2;
	private TextView tv_option3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_assistant_optionchoose, container, false);
		tv_option1 = view.findViewById(R.id.tv_option1);
		tv_option2 = view.findViewById(R.id.tv_option2);
		tv_option3 = view.findViewById(R.id.tv_option3);
		tv_option1.setText("Tao chưa biết đi đâu cả. Tao muốn đọc review của mọi người");
		tv_option2.setText("Tao biết địa điểm rồi nhưng chưa có kế hoạch cụ thể.");
		tv_option3.setText("Tao mới đi về và giờ tao muốn viết để chia sẻ cho mọi người.");
		handleOnclickListner();
		return view;
	}

	private void handleOnclickListner() {
		tv_option1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("Option 1","Onclick");
				((Assistant) getActivity()).optionNumber =1;
			}
		});

		tv_option2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("Option 2","Onclick");
				((Assistant) getActivity()).optionNumber =2;
			}
		});

		tv_option3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("Option 3","Onclick");
				((Assistant) getActivity()).optionNumber =3;
			}
		});
	}
}
