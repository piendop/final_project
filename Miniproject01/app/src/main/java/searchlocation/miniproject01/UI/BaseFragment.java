package searchlocation.miniproject01.UI;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.squareup.leakcanary.RefWatcher;

import icepick.Icepick;

public abstract class BaseFragment extends Fragment {
	protected final String TAG = getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
//	protected boolean DEBUG = MainActivity.DEBUG;

	protected AppCompatActivity activity;
	//public static final ImageLoader imageLoader = ImageLoader.getInstance();

    /*//////////////////////////////////////////////////////////////////////////
    // Fragment's Lifecycle
    //////////////////////////////////////////////////////////////////////////*/

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		activity = (AppCompatActivity) context;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		activity = null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
	//	if (DEBUG) Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
		super.onCreate(savedInstanceState);
		Icepick.restoreInstanceState(this, savedInstanceState);
		if (savedInstanceState != null) onRestoreInstanceState(savedInstanceState);
	}


	@Override
	public void onViewCreated(View rootView, Bundle savedInstanceState) {
		super.onViewCreated(rootView, savedInstanceState);
//		if (DEBUG) {
//			Log.d(TAG, "onViewCreated() called with: rootView = [" + rootView + "], savedInstanceState = [" + savedInstanceState + "]");
//		}
		initViews(rootView, savedInstanceState);
		initListeners();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Icepick.saveInstanceState(this, outState);
	}

	protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		//This line of code use to detect memory leak in android app
		/*RefWatcher refWatcher = main.getRefWatcher(getActivity());
		if (refWatcher != null) refWatcher.watch(this);*/
	}

    /*//////////////////////////////////////////////////////////////////////////
    // Init
    //////////////////////////////////////////////////////////////////////////*/

	protected void initViews(View rootView, Bundle savedInstanceState) {
	}

	protected void initListeners() {
	}

    /*//////////////////////////////////////////////////////////////////////////
    // Utils
    //////////////////////////////////////////////////////////////////////////*/

	public void setTitle(String title) {
//		if (DEBUG) Log.d(TAG, "setTitle() called with: title = [" + title + "]");
		if (activity != null && activity.getSupportActionBar() != null) {
			activity.getSupportActionBar().setTitle(title);
		}
	}
}
