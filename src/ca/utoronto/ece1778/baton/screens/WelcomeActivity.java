package ca.utoronto.ece1778.baton.screens;

import java.util.HashMap;
import java.util.Hashtable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import ca.utoronto.ece1778.baton.TEACHER.R;
import ca.utoronto.ece1778.baton.util.AlertDialogManager;
import ca.utoronto.ece1778.baton.util.Constants;

import com.google.android.gcm.GCMRegistrar;

/**
 * Welcome page of Baton Student Check app enviornment: network connection,
 * application configuration items Get the device registered on GCM
 * 
 * @author Yi Zhao
 * 
 */

public class WelcomeActivity extends Activity {
	static final String TAG = "WelcomeActivity";
	// Alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();

	AlertDialog mDialog;

	ConnectivityManager manager;

	AsyncGCMRegTask mTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 开始画面去掉标题栏
		setContentView(R.layout.welcome);
		// Check if GCM configuration is set
		if (Constants.SERVER_URL == null || Constants.SENDER_ID == null
				|| Constants.SERVER_URL.length() == 0
				|| Constants.SENDER_ID.length() == 0) {
			// GCM sernder id / server url is missing
			alert.showAlertDialog(WelcomeActivity.this, "Configuration Error!",
					"Please set your Server URL and GCM Sender ID", false);
			this.finish();
			return;
		}

		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);
	}

	@Override
	protected void onResume() {
		Log.i("WelcomActivity", "onResume Called");
		super.onResume();
		// check network
		boolean isNetworkOk = checkNetworkState();
		if (isNetworkOk) {
			State gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null ? manager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() : null;
			State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null ? manager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() : null;
			if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
				Log.i("WelcomActivity", "wifi connected");
				mTask = new AsyncGCMRegTask(WelcomeActivity.this);
				mTask.execute();


			} else if (gprs == State.CONNECTED || gprs == State.CONNECTING) {
				Log.i("WelcomActivity", "no wifi but data network connected");
				// need user to confirm whether to continue with data flow
				confirmWithGPRS();
			}
		} else {
			// network is unavailable let user to set for network
			setNetwork();
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i(TAG, "onRestoreInstanceState called");
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState!=null && savedInstanceState.get("savedState") != null) {
			HashMap<String, Object> savedState = (HashMap<String, Object>) savedInstanceState.get("savedState");
			Log.i(TAG, "Hashtable retained");
			Object objectTask = savedState.get("mTask");
			if (objectTask != null) {
				Log.i(TAG, "mTask be retained");
				mTask = (AsyncGCMRegTask) objectTask;
				mTask.setActivity(this);
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.i(TAG, "onSaveInstanceState called");
		HashMap<String, Object> returnObject = new HashMap<String, Object>();
		if (mTask != null && !mTask.isCompleted) {
			Log.i(TAG, "mTask is not finished while tilted, saved with mProgress");
			mTask.setActivity(null);
			returnObject.put("mTask", mTask);
		}
		outState.putSerializable("savedState", returnObject);
	}

	private boolean checkNetworkState() {
		boolean flag = false;
		manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		// 去进行判断网络是否连接
		if (manager.getActiveNetworkInfo() != null) {
			flag = manager.getActiveNetworkInfo().isAvailable();
			Log.i("WelcomActivity",
					"manager.getActiveNetworkInfo().isAvailable() = " + flag);
		}
		return flag;
	}

	private void setNetwork() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("Network Info");
		builder.setMessage("Network is unavailable on your phone.");
		builder.setPositiveButton("Network Setting", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = null;
				/**
				 * 判断手机系统的版本,如果API大于10 就是3.0+ 因为3.0以上的版本的设置和3.0以下的设置不一样，调用的方法不同
				 */
				if (android.os.Build.VERSION.SDK_INT > 10) {
					intent = new Intent(
							android.provider.Settings.ACTION_SETTINGS);
				} else {
					intent = new Intent();
					ComponentName component = new ComponentName(
							"com.android.settings",
							"com.android.settings.Settings");// WirelessSettings
					intent.setComponent(component);
					intent.setAction("android.intent.action.VIEW");
				}
				startActivity(intent);
			}
		});

		builder.setNegativeButton("Exit Baton", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				WelcomeActivity.this.finish();
			}
		});
		mDialog = builder.create();
		mDialog.show();
	}

	private void confirmWithGPRS() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("Network Info");
		builder.setMessage("You are currently using data package, continue?");
		builder.setPositiveButton("Continue", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i("WelcomActivity", "continue to use data package");
				mTask = new AsyncGCMRegTask(WelcomeActivity.this);
				mTask.execute();
			}
		});

		builder.setNegativeButton("Exit Baton", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				WelcomeActivity.this.finish();
			}
		});
		mDialog = builder.create();
		mDialog.show();
	}

	public void onGCMRegCompleted() {
		// go to the join page
		Intent intent = new Intent(this, JoinActivity.class);
		WelcomeActivity.this.finish();
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		Log.i("WelcomActivity", "onDestroy Called");
		if (mTask != null) {
			mTask.cancel(true);
		}
		if (GCMRegistrar.isRegistered(this))
			GCMRegistrar.onDestroy(this);
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		Log.i("WelcomActivity", "onPause Called");
		if (mDialog != null)
			mDialog.dismiss();
		super.onPause();
	}
	
	class AsyncGCMRegTask extends AsyncTask<Void, Void, Void> {
		WelcomeActivity activity;
		boolean isCompleted = false;
		String regId = "";

		private AsyncGCMRegTask(WelcomeActivity act) {
			this.activity = act;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(Void... unused) {
			String regId = GCMRegistrar.getRegistrationId(activity);
			// Check if regid already presents
			if (regId == null || regId.equals("")) {
				// TODO: what if GCM register fail?
				GCMRegistrar.register(activity, Constants.SENDER_ID);
				regId = GCMRegistrar.getRegistrationId(activity);
			}
			Log.i(TAG,"AsyncGCMRegTask get regId: " + regId);
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... unused) {
		}

		@Override
		protected void onPostExecute(Void unused) {
			/** notify the UI thread that the process is completed */
			isCompleted = true;
			notifyGCMRegCompleted();
		}

		private void setActivity(WelcomeActivity act) {
			this.activity = act;
			if (isCompleted) {
				notifyGCMRegCompleted();
			}
		}

		private void notifyGCMRegCompleted() {
			if (null != activity) {
				activity.onGCMRegCompleted();
			}
		}

	}

}
