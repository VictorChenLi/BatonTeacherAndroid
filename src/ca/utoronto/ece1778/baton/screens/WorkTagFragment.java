package ca.utoronto.ece1778.baton.screens;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ca.utoronto.ece1778.baton.gcm.client.main.R;


/**
 * 
 * @author Yi Zhao
 *
 */
public class WorkTagFragment extends Fragment {
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.i("WorkTagFragment","onActivityCreated called");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("WorkTagFragment","onCreateView called");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.i("WorkTagFragment","onDestroyView called");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.i("WorkTagFragment","onDetach called");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.i("WorkTagFragment","onPause called");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i("WorkTagFragment","onResume called");
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.i("WorkTagFragment","onStart called");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.i("WorkTagFragment","onStop called");
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Log.i("WorkTagFragment","onViewCreated called");
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.i("WorkTagFragment","onAttach called");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("WorkTagFragment","onCreate called");
	}

	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "1";

	public WorkTagFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("WorkTagFragment","onCreateView called");
		View rootView = inflater.inflate(R.layout.fragment_work_tab,
				container, false);
		return rootView;
	}
}

