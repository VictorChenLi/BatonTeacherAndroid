package ca.utoronto.ece1778.baton.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;
import ca.utoronto.ece1778.baton.TEACHER.R;
import ca.utoronto.ece1778.baton.database.DBAccess;
import ca.utoronto.ece1778.baton.database.DBAccessImpl;
import ca.utoronto.ece1778.baton.syncserver.BatonServerCommunicator;
import ca.utoronto.ece1778.baton.util.CommonUtilities;
import ca.utoronto.ece1778.baton.util.Constants;
import ca.utoronto.ece1778.baton.util.GlobalApplication;
import ca.utoronto.ece1778.baton.util.WakeLocker;

import com.baton.publiclib.model.ticketmanage.TalkTicketForDisplay;
import com.baton.publiclib.model.ticketmanage.Ticket;
import com.baton.publiclib.model.usermanage.UserProfile;
import com.google.android.gcm.GCMRegistrar;
//import ca.utoronto.ece1778.baton.models.StudentProfile;
//import ca.utoronto.ece1778.baton.models.Ticket;

/**
 * 
 * @author Yi Zhao
 * 
 */
// TODO 为方便测试，暂时在AndroidManifest.xml中将Launcher
// Activity设置为了MainScreenActivity，以后需要修改回去
public class MainScreenActivity extends FragmentActivity implements
		ActionBar.TabListener {
	static final String TAG = "MainActivity";// name for Log
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	/** UI item on talk tab */
	List<TalkTicketForDisplay> t_ticket4Display;
	GridView talkGridView;
	ArrayList<Item> talkGridArray = new ArrayList<Item>();
	GridViewAdapter talkGridAdapter;

	// ///////////TODO: 实验用//////////////
	String test_StartTime1="2014-03-12T09:18:00.736";
	String test_StartTime2="2014-03-12T09:18:10.736";
	int afterTime = 40;
	long testST = CommonUtilities.getDataTime(test_StartTime1);
	TalkTicketForDisplay test_t3 = null;
	// ////////////////////////////

	/**
	 * talk ticket receiver: receive notification of talk ticket update from
	 * back-end service, reload the ticket for display array
	 */
	// TODO: or maybe don't need a receiver? since the global array will change,
	// anyway..
	private final BroadcastReceiver mHandleMessageReceiver_talk = new TalkTicketBroadcastReceiver();

	private TalkWidgeUpdataTask mTalkUpdateTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate Called");
		setContentView(R.layout.activity_main_screen);
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		registerReceiver(mHandleMessageReceiver_talk, new IntentFilter(
				Constants.DISPLAY_TALK_TICKET_ACTION));

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		// load talk tickets for display
//		t_ticket4Display = CommonUtilities.getTicketForDisplayList(this);

		// //////////////just for testing without real dynamic ticket load and
		// update
//		TalkTicketForDisplay t1 = new TalkTicketForDisplay(test_StartTime1, "Victor",
//				Ticket.TALK_INTENT_NEWIDEA_WEB_STR, "3");
//		TalkTicketForDisplay t2 = new TalkTicketForDisplay(test_StartTime2, "Fiona",
//				Ticket.TALK_INTENT_NEWIDEA_WEB_STR, "3");
//		CommonUtilities.addGlobalTalkVar(this, t1);
//		CommonUtilities.addGlobalTalkVar(this, t2);
		// ///////////////////////////////////////////////////

//		Log.i(TAG, "initial t_ticket4Display size:" + t_ticket4Display.size());

		// start a thread to refresh the UI every 1 second
		t_ticket4Display = CommonUtilities.getTicketForDisplayList(this);
		mTalkUpdateTask = new TalkWidgeUpdataTask();
		mTalkUpdateTask.execute();
	}
	
	public void syncDatabase()
    {
    	DBAccess dbaccess = DBAccessImpl.getInstance(getApplicationContext());
        if(dbaccess.DetactDatabase())
        {
    		BatonServerCommunicator.uploadTicketData(this);
    		dbaccess.ResetDatabase();
        }
    }

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO: Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.menu_profile:
			return true;
		case R.id.menu_log_out:
			return true;
		case R.id.menu_about:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			switch (position) {
			case 0:
				fragment = new TalkTagFragment();
				break;
			case 1:
				fragment = new WorkTagFragment();
				break;

			}
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			}
			return null;
		}
	}

	class TalkTicketBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			WakeLocker.acquire(getApplicationContext());
			// TODO:重新获取t_ticket4Display
			t_ticket4Display = CommonUtilities.getTicketForDisplayList(MainScreenActivity.this);
			Log.i("MainScreenActivity", "onReceive Called");
			WakeLocker.release();
		}

	}

	protected class TalkWidgeUpdataTask extends AsyncTask<Void, Long, String> {

		@Override
		protected String doInBackground(Void... unused) {
			WakeLocker.acquire(getApplicationContext());
			talkGridView = (GridView) mViewPager.findViewById(R.id.talk_tab_grid);
			while (talkGridView == null) {// wait until gridView is created
				talkGridView = (GridView) mViewPager.findViewById(R.id.talk_tab_grid);
				try {
					Thread.sleep(1 * 1000); // sleep for one second
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			while (true) {
				long curTime = System.currentTimeMillis();
				publishProgress(new Long[] { curTime });
				try {
					Thread.sleep(1 * 1000); // sleep for one second
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		protected void onProgressUpdate(Long... curTime) {
			super.onProgressUpdate(curTime);
			// ///TODO 待注释 试验:过一段时间之后t_ticket4Display发生变化，UI是否能正常更新 --goes well
			/*if (test_t3 == null && (curTime[0] - testST) > afterTime * 1000) {
				test_t3 = new TalkTicketForDisplay(test_StartTime1, "Zack",
						Ticket.TALK_INTENT_NEWIDEA_WEB_STR, "3");
				CommonUtilities.addGlobalTalkVar(MainScreenActivity.this, test_t3);
			}*/
			// end///////////////////
			
			talkGridArray.clear();
			List<TalkTicketForDisplay> presentList = new ArrayList<TalkTicketForDisplay>(t_ticket4Display);
			talkGridAdapter = new GridViewAdapter(MainScreenActivity.this, R.layout.talk_student_item, presentList);
			talkGridView.setAdapter(talkGridAdapter);

		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public void onPause() {
		Log.i("MainScreenActivity", "onPause called");
		super.onPause();

	}

	@Override
	public void onDestroy() {
		//TODO 处理黑屏、返回时的线程中断，待优化
		Log.i("MainScreenActivity", "onDestroy called");
		if (mTalkUpdateTask != null && !mTalkUpdateTask.isCancelled()) {
			mTalkUpdateTask.cancel(true);
			Log.i(TAG, "mTalkUpdateTask isCancelled:" + mTalkUpdateTask.isCancelled());
		}
		try {
			unregisterReceiver(mHandleMessageReceiver_talk);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		syncDatabase();
		super.onDestroy();
	}

}
