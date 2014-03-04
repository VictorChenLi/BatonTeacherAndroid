package ca.utoronto.ece1778.baton.screens;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ca.utoronto.ece1778.baton.gcm.client.main.R;
import ca.utoronto.ece1778.baton.util.Constants;

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
		Log.i("TalkTagFragment", "onCreatView called");
		// TODO: replace the layout with your final one
		View rootView = inflater.inflate(R.layout.fragment_talk_tab, container,
				false);
		TextView txIntent = (TextView)rootView.findViewById(R.id.tv_intent);
		TextView txName = (TextView)rootView.findViewById(R.id.tv_name);
		TextView txTime = (TextView)rootView.findViewById(R.id.tv_waitTime);
		
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), Constants.TYPEFACE_ACTION_MAN_BOLD);
		txIntent.setTypeface(tf);
		txName.setTypeface(tf);
		txTime.setTypeface(tf);
		/*
		 * TextView dummyTextView = (TextView) rootView
		 * .findViewById(R.id.section_label);
		 * dummyTextView.setText(Integer.toString(getArguments().getInt(
		 * ARG_SECTION_NUMBER)));
		 */
		//
		// lblMessage = (TextView) rootView.findViewById(R.id.lblMessage);
		// Log.i("TalkTagFragment",String.valueOf(lblMessage==null));
		return rootView;
	}

}
