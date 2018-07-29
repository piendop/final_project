package searchlocation.miniproject01.UI.Editor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.github.irshulx.Editor;
import com.github.irshulx.models.EditorTextStyle;

import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.OnGoing.OnGoingActivity;
import searchlocation.miniproject01.UI.Utilis.BottomNavigationViewHelper;

public class EditorActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);
		Editor editor = (Editor) findViewById(R.id.editor);
		setUpEditor();
	}
	private void setUpEditor() {
		View h1 = findViewById(R.id.action_h1);
		View h2= findViewById(R.id.action_h2);
		View h3 = findViewById(R.id.action_h3);
	}
}
