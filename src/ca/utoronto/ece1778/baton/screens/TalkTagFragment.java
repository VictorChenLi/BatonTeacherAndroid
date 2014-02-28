package ca.utoronto.ece1778.baton.screens;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import ca.utoronto.ece1778.baton.gcm.client.main.R;
import ca.utoronto.ece1778.baton.util.Constants;
import ca.utoronto.ece1778.baton.util.WakeLocker;

/**
 * 
 * @author Yi Zhao
 * 
 */
public class TalkTagFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "0";

	TextView lblMessage;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("TalkTagFragment","onCreatView called");
		//TODO: replace the layout with your final one
		View rootView = inflater.inflate(R.layout.activity_main, container,
				false);
		/*
		 * TextView dummyTextView = (TextView) rootView
		 * .findViewById(R.id.section_label);
		 * dummyTextView.setText(Integer.toString(getArguments().getInt(
		 * ARG_SECTION_NUMBER)));
		 */
		
		lblMessage = (TextView) rootView.findViewById(R.id.lblMessage);
		Log.i("TalkTagFragment",String.valueOf(lblMessage==null));
		return rootView;
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.i("TalkTagFragment","onAttach called");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("TalkTagFragment","onCreat called");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("TalkTagFragment","onDestry called");
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.i("WorkTagFragment","onActivityCreated called");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.i("TalkTagFragment","onDestroyView called");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.i("TalkTagFragment","onDetach called");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.i("TalkTagFragment","onPause called");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i("TalkTagFragment","onResume called");
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.i("TalkTagFragment","onStart called");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.i("TalkTagFragment","onStop called");
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Log.i("TalkTagFragment","onViewCreated called");
	}

	
	
	

}
