package searchlocation.miniproject01.UI.Assistant;

import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Fragments.Option1Fragment;

public class Assistant extends AppCompatActivity {

	public int optionNumber=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assistant);
		OptionMenuFragment optionMenuFragment = new OptionMenuFragment();
		FragmentManager manager = getSupportFragmentManager();
		manager.beginTransaction()
				.add(R.id.chatbox_holder,optionMenuFragment)
				.commit();
//		while (optionNumber==0){
//			Thread.sleep(1000);
//		}

		HandleOption(1);
	}

	private void HandleOption(int optionNumber) {
		switch (optionNumber){
			case 1:
				Option1Fragment optionMenuFragment = new Option1Fragment();
				FragmentManager manager = getSupportFragmentManager();
				manager.beginTransaction()
						.add(R.id.chatbox_holder,optionMenuFragment)
						.commit();
				break;
			case 2:
			case 3:

		}
	}
}

