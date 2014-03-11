package ca.utoronto.ece1778.baton.screens;

import java.util.ArrayList;

import android.content.ClipData.Item;
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

/**
 * 
 * @author Yi Zhao
 * 
 */
public class TalkTagFragment extends Fragment implements OnItemLongClickListener{
	static final String TAG = "TalkTagFragment";
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
	//private final BroadcastReceiver mHandleMessageReceiver = new TicketBroadcastReceiver();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "onCreatView called");
		View rootView = inflater.inflate(R.layout.fragment_talk_tab, container,false);
		/*Intent intent = new Intent();
		intent.putExtra(INTENT_EXTRA_ITEM_STUDENT_NAME, "Fiona");
		intent.putExtra(INTENT_EXTRA_ITEM_PAR_INTENT, Ticket.TALK_INTENT_NEWIDEA_WEB_STR);
		intent.putExtra(INTENT_EXTRA_ITEM_PAR_TIMES, "3");
		intent.putExtra(INTENT_EXTRA_ITEM_WAIT_TIME, "16");
		
		gridArray.add(new Item(intent));
		
		gridView = (GridView) rootView.findViewById(R.id.talk_tab_grid);
		
		gridAdapter = new GridViewAdapter(getActivity(), R.layout.talk_student_item,gridArray);
		gridAdapter.setNotifyOnChange(true);
		gridView.setAdapter(gridAdapter);
		
		
		gridView.setOnItemLongClickListener(this);*/

		return rootView;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

		Log.i("TalkTagFragment","onItemLongClick called");
	
		return true;
	
	}


}
