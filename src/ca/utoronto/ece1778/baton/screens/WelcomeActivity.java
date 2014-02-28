package ca.utoronto.ece1778.baton.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import ca.utoronto.ece1778.baton.gcm.client.main.R;
import ca.utoronto.ece1778.baton.syncserver.InternetConnectionDetector;
import ca.utoronto.ece1778.baton.util.AlertDialogManager;
import ca.utoronto.ece1778.baton.util.Constants;

import com.google.android.gcm.GCMRegistrar;

/**
 * Welcome page of Baton Student
 * Check app enviornment: network connection, application configuration items
 * Get the device registered on GCM
 * 
 * @author Yi Zhao
 * 
 */

//TODO: need to change to use AsyncTask and progress bar
public class WelcomeActivity extends Activity {
	// label to display gcm messages
	TextView lblMessage;

	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;

	// Alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();

	// Connection detector
	InternetConnectionDetector cd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  
        //requestWindowFeature(Window.FEATURE_NO_TITLE);//开始画面去掉标题栏  
        setContentView(R.layout.welcome);  
       
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);

		cd = new InternetConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(this, "Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}
		

		// Check if GCM configuration is set
		if (Constants.SERVER_URL == null
				|| Constants.SENDER_ID == null
				|| Constants.SERVER_URL.length() == 0
				|| Constants.SENDER_ID.length() == 0) {
			// GCM sernder id / server url is missing
			alert.showAlertDialog(WelcomeActivity.this, "Configuration Error!",
					"Please set your Server URL and GCM Sender ID", false);
			// stop executing code by return
			return;
		}
		

		// TODO:check if has Google Account???

		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);

		lblMessage = (TextView) findViewById(R.id.lblMessage);

		// Get GCM registration id
		final String regId = GCMRegistrar.getRegistrationId(this);

		// Check if regid already presents
		if (regId.equals("")) {
			// Registration is not present, register now with GCM
			GCMRegistrar.register(this, Constants.SENDER_ID);
		} else {
			Toast.makeText(getApplicationContext(),
					"device ready", Toast.LENGTH_SHORT).show();
			// go to the join page
			Intent intent = new Intent(this, JoinActivity.class);
			startActivity(intent);
			WelcomeActivity.this.finish();
		}
	}

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		GCMRegistrar.onDestroy(this);
		super.onDestroy();
	}

}
