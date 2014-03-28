/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.utoronto.ece1778.baton.screens;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import ca.utoronto.ece1778.baton.TEACHER.R;
import ca.utoronto.ece1778.baton.syncserver.BatonServerCommunicator;
import ca.utoronto.ece1778.baton.syncserver.InternetConnectionDetector;
import ca.utoronto.ece1778.baton.util.AlertDialogManager;

import com.baton.publiclib.infrastructure.exception.ServiceException;
import com.baton.publiclib.model.usermanage.UserProfile;
import com.google.android.gcm.GCMRegistrar;
//import ca.utoronto.ece1778.baton.models.StudentProfile;

/**
 * User register
 * 
 * @author Yi Zhao
 * 
 */
public class RegisterActivity extends Activity implements OnClickListener {
	static final String TAG = "RegisterActivity";

	AlertDialogManager alert = new AlertDialogManager();

	ProgressDialog mProgress = null;

	// UI elements
	EditText txtFirstName;
	EditText txtLastName;
	EditText txtEmail;
	EditText txtLoginID;
	EditText txtPassword;
	EditText txtConfirmPwd;
	Button btnRegister;

	String pw = null;
	String con_pw = null;

	String gcm_id;

	UserProfile mTeacherProfile = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Log.i(TAG, "RegisterActivity onCreate called");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);

		txtFirstName = (EditText) findViewById(R.id.register_txtFirstName);
		txtLastName = (EditText) findViewById(R.id.register_txtLastName);
		txtEmail = (EditText) findViewById(R.id.register_txtEmail);
		txtLoginID = (EditText) findViewById(R.id.register_txtLoginID);
		txtPassword = (EditText) findViewById(R.id.register_txtPassword);
		txtConfirmPwd = (EditText) findViewById(R.id.register_txtConfirmPwd);
		gcm_id = GCMRegistrar.getRegistrationId(getApplicationContext());
		Log.i(TAG, "on create gcm_id:" + gcm_id);

		btnRegister = (Button) findViewById(R.id.register_btnRegister);
		btnRegister.setOnClickListener(this);

		mProgress = new ProgressDialog(this);
	}

	@Override
	public void onClick(View v) {
		// Log.i(TAG, "RegisterActivity onClick called");

		String fName = txtFirstName.getText().toString();
		String lName = txtLastName.getText().toString();
		String loginId = txtLoginID.getText().toString();
		String email = txtEmail.getText().toString();
		pw = txtPassword.getText().toString();
		con_pw = txtConfirmPwd.getText().toString();

		mTeacherProfile = new UserProfile(gcm_id, loginId, email, pw, fName,
				lName, UserProfile.USERTYPE_TEACHER);
		
		// Log.i(TAG, "in on Click " + mStudentProfile.toString());

		// Check if user filled the form
		if (!isProfileCompleted(mTeacherProfile)) {
			alert.showAlertDialog(RegisterActivity.this,
					"Uncompleted information", "Please enter your details",
					false);

		} else if (!isPasswordMatch()) { // user doen't filled that data ask him
											// to fill the form
			Toast.makeText(this, "Password entered doesn't match.",
					Toast.LENGTH_LONG).show();
		} else {
			// Log.i(TAG, "ProfileCompleted");
			new AsyncRegisterTask()
					.execute(new UserProfile[] { mTeacherProfile });
		}

	}

	public void goToJoinPage() {
		Intent i = new Intent(this, JoinActivity.class);
		i.putExtra(UserProfile.LOGINID_WEB_STR, mTeacherProfile.getLogin_id());
		// TODO dealing with MD5
		i.putExtra(UserProfile.PASSWORD_WEB_STR, mTeacherProfile.getPassword());
		startActivity(i);
		finish();
	}

	private boolean isProfileCompleted(final UserProfile user) {
		if (user.getEmail().trim().length() > 0
				&& user.getF_name().trim().length() > 0
				&& user.getGcm_regid().trim().length() > 0
				&& user.getL_name().trim().length() > 0
				&& user.getPassword().trim().length() > 0)
			return true;
		Log.i("isProfileCompleted",user.toString());
		return false;
	}
	
	private boolean isPasswordMatch() {
		if (pw.equals(con_pw))
			return true;
		return false;
	}

	/*
	 * AsyncTask<Type1, Type2, Type3> 1.The type of information that is needed
	 * to process the task (e.g., URLs to download) 2. The type of information
	 * that is passed within the task to indicate progress 3. The type of
	 * information that is passed when the task is completed to the post-task
	 * code
	 */
	class AsyncRegisterTask extends AsyncTask<UserProfile, Void, String> {
		@Override
		protected void onPreExecute() {
			// System.out.println("onPreExecute() called");
			mProgress.setMessage("Registering...");
			mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgress.setCancelable(false);
			mProgress.setProgress(0);
			mProgress.show();
		}

		@Override
		protected String doInBackground(UserProfile... users) {
			UserProfile u = users[0];
			
			String result=null;
			try {
				result = BatonServerCommunicator.register(
						getApplicationContext(), u);
			} catch (ServiceException e) {
				e.printStackTrace();
				result=BatonServerCommunicator.REPLY_MESSAGE_REGISTER_SUCCESS;
			}
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... unused) {
		}

		@Override
		protected void onPostExecute(String result) {
			mProgress.dismiss();
			Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT)
					.show();
			if (result.equals(BatonServerCommunicator.REPLY_MESSAGE_REGISTER_SUCCESS)) {
				goToJoinPage();
			}
		}
	}

}
