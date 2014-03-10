package ca.utoronto.ece1778.baton.screens;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.ClipData.Item;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import ca.utoronto.ece1778.baton.TEACHER.R;
import ca.utoronto.ece1778.baton.screens.MainScreenActivity.TicketBroadcastReceiver;
import ca.utoronto.ece1778.baton.util.Constants;

/**
 * 
 * @author Yi Zhao
 * 
 */
public class TalkTagFragment extends Fragment implements OnItemLongClickListener{
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "0";

	public static final String INTENT_EXTRA_ITEM_STUDENT_NAME = "student_name";
	public static final String INTENT_EXTRA_ITEM_WAIT_TIME = "wait_time";
	public static final String INTENT_EXTRA_ITEM_PAR_TIMES = "participate_time";
	public static final String INTENT_EXTRA_ITEM_PAR_INTENT = "participate_intent";

	GridView gridView;
	ArrayList<Item> gridArray = new ArrayList<Item>();
	GridViewAdapter gridAdapter;
	
	private final BroadcastReceiver mHandleMessageReceiver = new TicketBroadcastReceiver();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("TalkTagFragment", "onCreatView called");
		View rootView = inflater.inflate(R.layout.fragment_talk_tab, container,false);
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				Constants.DISPLAY_TICKET_ACTION));
		Intent intent = new Intent();
		intent.putExtra(INTENT_EXTRA_ITEM_STUDENT_NAME, "Fiona");
		intent.putExtra(INTENT_EXTRA_ITEM_PAR_INTENT, Constants.TALK_INTENT_NEW_IDEA);
		intent.putExtra(INTENT_EXTRA_ITEM_PAR_TIMES, "3");
		intent.putExtra(INTENT_EXTRA_ITEM_WAIT_TIME, "16");
		
		gridArray.add(new Item(intent));
		gridArray.add(new Item(intent));
		
		gridView = (GridView) rootView.findViewById(R.id.talk_tab_grid);
		gridAdapter = new GridViewAdapter(getActivity(), R.layout.talk_student_item,gridArray);
		gridView.setAdapter(gridAdapter);
		
		gridView.setOnItemLongClickListener(this);

		return rootView;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

	/*	Toast.makeText(getActivity(),
				gridArray.get(arg2).getIntent().getStringExtra(INTENT_EXTRA_ITEM_STUDENT_NAME), Toast.LENGTH_SHORT)
				.show();*/
		Log.i("TalkTagFragment","onItemLongClick called");
	
		return true;
	
	}


}
