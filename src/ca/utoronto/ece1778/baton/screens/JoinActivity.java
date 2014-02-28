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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import ca.utoronto.ece1778.baton.gcm.client.main.R;
import ca.utoronto.ece1778.baton.models.StudentProfile;
import ca.utoronto.ece1778.baton.syncserver.BatonServerCommunicator;
import ca.utoronto.ece1778.baton.util.AlertDialogManager;

/**
 * Join page
 * @author Yi Zhao
 * 
 */
public class JoinActivity extends Activity implements OnClickListener {
	// alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();

	// UI elements
	EditText txtEmail;
	EditText txtClassroom;
	EditText txtPassword;
	Button btnRegister;
	Button btnJoin;

	ProgressDialog mProgress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		txtEmail = (EditText) findViewById(R.id.login_txtEmail);
		txtClassroom = (EditText) findViewById(R.id.login_txtClassroomName);
		txtPassword = (EditText) findViewById(R.id.login_txtPassword);
		btnJoin = (Button) findViewById(R.id.login_btnJoin);
		btnRegister = (Button) findViewById(R.id.login_btnRegister);

		btnJoin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		
		mProgress = new ProgressDialog(this);

		/*in case the previous context is RegisterActivity*/
		Intent intent = this.getIntent();
		String email = intent.getStringExtra(StudentProfile.POST_EMAIL);
		String pwd = intent.getStringExtra(StudentProfile.POST_PASSWORD);
		if (email != null && !email.equals("")) {
			txtEmail.setText(email);
		}
		if (pwd != null && !pwd.equals("")) {
			txtPassword.setText(pwd);
		}
		/*end- in case the previous context is RegisterActivity*/
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btnJoin:
			String email = txtEmail.getText().toString();
			String classroom = txtClassroom.getText().toString();
			String password = txtPassword.getText().toString();
			// Check if user filled the form
			if (email.trim().length() > 0 && password.trim().length() > 0
					&& classroom.trim().length() > 0) {
				new AsyncJoinTask().execute(new String[] {email,classroom,password});
			} else {
				alert.showAlertDialog(JoinActivity.this, "Login Error!",
						"Please fill the form", false);
			}
			break;
		case R.id.login_btnRegister:
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
			break;
		}
		;
	}
	
	public void goToMainScreen(){
		Intent i = new Intent(this, MainScreenActivity.class);
		startActivity(i);
		finish();
	}

	/*
	 * AsyncTask<Type1, Type2, Type3> 1.The type of information that is needed
	 * to process the task (e.g., URLs to download) 2. The type of information
	 * that is passed within the task to indicate progress 3. The type of
	 * information that is passed when the task is completed to the post-task
	 * code
	 */
	class AsyncJoinTask extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			mProgress.setMessage("Joining classroom...");
			mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgress.setCancelable(false);
			mProgress.setProgress(0);
			mProgress.show();
		}

		@Override
		protected String doInBackground(String... token) {
			String result = BatonServerCommunicator.login(
					getApplicationContext(), token);
			//String result = BatonServerCommunicator.REPLY_MESSAGE_LOGIN_SUCCESS;
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
			if (result
					.equals(BatonServerCommunicator.REPLY_MESSAGE_LOGIN_SUCCESS)) {
				goToMainScreen();
			}
		}
	}
}
