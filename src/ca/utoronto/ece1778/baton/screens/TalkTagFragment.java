package ca.utoronto.ece1778.baton.screens;

import java.util.ArrayList;
import java.util.List;

import com.baton.publiclib.model.ticketmanage.TalkTicketForDisplay;

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
import ca.utoronto.ece1778.baton.util.CommonUtilities;

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

	

	GridView gridView;
	List<TalkTicketForDisplay> gridArray = null;
	GridViewAdapter gridAdapter;
	//private final BroadcastReceiver mHandleMessageReceiver = new TicketBroadcastReceiver();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "onCreatView called");
//		Intent intent = new Intent();
//		intent.putExtra(INTENT_EXTRA_ITEM_STUDENT_NAME, "Fiona");
//		intent.putExtra(INTENT_EXTRA_ITEM_PAR_INTENT, Ticket.TALK_INTENT_NEWIDEA_WEB_STR);
//		intent.putExtra(INTENT_EXTRA_ITEM_PAR_TIMES, "3");
//		intent.putExtra(INTENT_EXTRA_ITEM_WAIT_TIME, "16");
//		gridArray.add(new Item(intent));

		View rootView = inflater.inflate(R.layout.fragment_talk_tab, container,false);
		gridView = (GridView) rootView.findViewById(R.id.talk_tab_grid);
		gridArray = CommonUtilities.getTicketForDisplayList(getActivity());
		gridAdapter = new GridViewAdapter(getActivity(), R.layout.talk_student_item,gridArray);
		gridAdapter.setNotifyOnChange(true);
		gridView.setAdapter(gridAdapter);
		
		
		gridView.setOnItemLongClickListener(this);

		return rootView;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

		Log.i("TalkTagFragment","onItemLongClick called");
	
		return true;
	
	}


}
