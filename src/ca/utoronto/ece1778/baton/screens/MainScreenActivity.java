package ca.utoronto.ece1778.baton.screens;

import java.util.Date;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
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
import android.widget.TextView;
import android.widget.Toast;
import ca.utoronto.ece1778.baton.gcm.client.main.R;
import ca.utoronto.ece1778.baton.models.StudentProfile;
import ca.utoronto.ece1778.baton.models.Ticket;
import ca.utoronto.ece1778.baton.util.CommonUtilities;
import ca.utoronto.ece1778.baton.util.Constants;
import ca.utoronto.ece1778.baton.util.WakeLocker;

import com.google.android.gcm.GCMRegistrar;

/**
 * 
 * @author Yi Zhao
 * 
 */
public class MainScreenActivity extends FragmentActivity implements
		ActionBar.TabListener {

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

	private final BroadcastReceiver mHandleMessageReceiver = new TicketBroadcastReceiver();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				Constants.DISPLAY_TICKET_ACTION));

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
			case 2:
				fragment = new ProfileTagFragment();
				break;
			}
			/*
			 * Bundle args = new Bundle();
			 * args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position +
			 * 1); fragment.setArguments(args);
			 */
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
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	class TicketBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("MainScreenActivity", "onReceive Called");
			String ticketType = intent
					.getStringExtra(Ticket.TICKETTYPE_WEB_STR);
			String ticketContent = intent
					.getStringExtra(Ticket.TICKETCONTENT_WEB_STR);
			String timeStamp = intent.getStringExtra(Ticket.TIMESTAMP_WEB_STR);
			String nickName = intent.getStringExtra(StudentProfile.POST_NICK_NAME);
			int uid = intent.getIntExtra(Ticket.UID_WEB_STR, 0);
			Log.i("MainScreenActivity", ticketType + ticketContent + timeStamp);

			// TODO: update talk tab and work tab according to the ticket type
			// and its content here
			if (ticketType.equals(Constants.TICKET_TYPE_TALK)) {
				// Waking up mobile if it is sleeping
				WakeLocker.acquire(getApplicationContext());

				/**
				 * Take appropriate action on this message depending upon your
				 * app requirement For now i am just displaying it on the screen
				 * */
				String newMessage = nickName +":"+ ticketType + ": " + ticketContent;
				Log.i("MainScreenActivity", newMessage);
				Toast.makeText(context, newMessage, Toast.LENGTH_LONG).show();
				TextView tv_intent = (TextView) findViewById(R.id.tv_intent);
				TextView tv_waitTime = (TextView) findViewById(R.id.tv_waitTime);
				TextView tv_name = (TextView) findViewById(R.id.tv_name);
				tv_intent.setText(ticketContent);
				tv_name.setText(nickName);
				
				
//				TextView m = (TextView) findViewById(R.id.lblMessage);
//				m.append(newMessage);
				// Releasing wake lock
				WakeLocker.release();
			}

		}

	}

	protected class WidgeUpdataTask extends AsyncTask<String, Integer, String> {

		Context uiContext;
		
		public WidgeUpdataTask(Context context)
		{
			uiContext = context;
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			String strTime=arg0[0];
			long startTime = CommonUtilities.getDataTime(strTime);
			long curTime = System.currentTimeMillis();
			while(curTime-startTime<3*30*1000)
			{
				if(curTime-startTime<30*1000)
				{
					setProgress(0x00CCFF);
				}
				else
					if (curTime-startTime<2*30*1000)
					{
						setProgress(0xFFCC00);
					}
					else
						if(curTime-startTime<3*30*1000)
						{
							setProgress(0xFF0000);
						}
				curTime = System.currentTimeMillis();
			}
			return null;
		}


		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			TextView tv_waitTime = (TextView) ((Activity)uiContext).findViewById(R.id.tv_waitTime);
			tv_waitTime.setBackgroundColor(values[0]);
		}
		
	}
	
	public void onPause() {
		super.onPause();
		Log.i("MainScreenActivity", "onPause called");
	}

	@Override
	public void onDestroy() {
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

}
