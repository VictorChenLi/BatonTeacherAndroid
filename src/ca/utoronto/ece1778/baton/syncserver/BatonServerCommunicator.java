package ca.utoronto.ece1778.baton.syncserver;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import android.content.Context;
import android.util.Log;
import ca.utoronto.ece1778.baton.gcm.client.main.R;
import ca.utoronto.ece1778.baton.models.StudentProfile;
import ca.utoronto.ece1778.baton.util.CommonUtilities;
import ca.utoronto.ece1778.baton.util.Constants;

import com.google.android.gcm.GCMRegistrar;

/**
 * 
 * @author Yi Zhao
 *
 */
public class BatonServerCommunicator {
	private static final int MAX_ATTEMPTS = 5;
	private static final int BACKOFF_MILLI_SECONDS = 2000;
	private static final Random random = new Random();
	private static final String TAG = "BatonServerCommunicator";
	
	/** string for communication with sync server as a parameter name */
	private static final String POST_CLASSROOM = "classroom";
	
	public static final String REPLY_MESSAGE_REGISTER_SUCCESS = "register_success";
	public static final String REPLY_MESSAGE_REGISTER_FAIL = "register_fail";
	public static final String REPLY_MESSAGE_LOGIN_SUCCESS = "login_success";
	public static final String REPLY_MESSAGE_LOGIN_FAIL = "login_fail";
	

	/**
	 * Register user on the server.
	 */
	public static String register(final Context context,final StudentProfile user) {
		Log.i(TAG, "registering user:");
		Log.i(TAG, user.toString());
		
		String serverUrl = Constants.SERVER_URL + "/register?";
		Map<String, String> params = new HashMap<String, String>();
		params.put(StudentProfile.POST_GCM_ID, user.getGcm_id());
		params.put(StudentProfile.POST_EMAIL, user.getEmail());
		params.put(StudentProfile.POST_FIRST_NAME, user.getFirstName());
		params.put(StudentProfile.POST_LAST_NAME, user.getLastName());
		params.put(StudentProfile.POST_LOGIN_ID,user.getLoginID());
		params.put(StudentProfile.POST_PASSWORD, user.getPassword());

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {
				/*CommonUtilities.displayMessage(context, context.getString(
						R.string.server_registering, i, MAX_ATTEMPTS));*/
				post(serverUrl, params);
				GCMRegistrar.setRegisteredOnServer(context, true);
				String message = context.getString(R.string.server_registered);
				CommonUtilities.displayMessage(context, message);
				Log.i(TAG, "register success");
				return REPLY_MESSAGE_REGISTER_SUCCESS;
			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return REPLY_MESSAGE_REGISTER_FAIL;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}
		String message = context.getString(R.string.server_register_error,
				MAX_ATTEMPTS);
		CommonUtilities.displayMessage(context, message);
		return REPLY_MESSAGE_REGISTER_FAIL;
	}
	
	/**
	 * Login user on the server.
	 */
	public static String login(final Context context,final String[] token) {
		Log.i(TAG, "Login user:");
		Log.i(TAG, Arrays.toString(token));
		
		String serverUrl = Constants.SERVER_URL + "/login?";
		Map<String, String> params = new HashMap<String, String>();
		params.put(StudentProfile.POST_EMAIL, token[0]);
		params.put(POST_CLASSROOM, token[1]);
		params.put(StudentProfile.POST_PASSWORD, token[2]);
		params.put(StudentProfile.POST_GCM_ID, GCMRegistrar.getRegistrationId(context));

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			//TODO: need to deal with different codes and give user appropriate feedback
			Log.d(TAG, "Attempt #" + i + " to register");
			try {
				/*CommonUtilities.displayMessage(context, context.getString(
						R.string.server_registering, i, MAX_ATTEMPTS));*/
				post(serverUrl, params);
				//String message = context.getString(R.string.server_registered);
				//CommonUtilities.displayMessage(context, message);
				Log.i(TAG, "login success");
				return REPLY_MESSAGE_LOGIN_SUCCESS;
			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return REPLY_MESSAGE_LOGIN_FAIL;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}
		String message = context.getString(R.string.server_register_error,
				MAX_ATTEMPTS);
		CommonUtilities.displayMessage(context, message);
		return REPLY_MESSAGE_LOGIN_FAIL;
	}

	/**
	 * Unregister this account/device pair within the server.
	 */
	public static void unregister(final Context context, final StudentProfile user) {
		Log.i(TAG, "unregistering user");
		String serverUrl = Constants.SERVER_URL + "/unregister?";
		Map<String, String> params = new HashMap<String, String>();
		params.put(StudentProfile.POST_GCM_ID, user.getGcm_id());
		params.put(StudentProfile.POST_EMAIL, user.getEmail());
		try {
			post(serverUrl, params);
			GCMRegistrar.setRegisteredOnServer(context, false);
			String message = context.getString(R.string.server_unregistered);
			CommonUtilities.displayMessage(context, message);
		} catch (IOException e) {
			// At this point the device is unregistered from GCM, but still
			// registered in the server.
			// We could try to unregister again, but it is not necessary:
			// if the server tries to send a message to the device, it will get
			// a "NotRegistered" error message and should unregister the device.
			String message = context.getString(
					R.string.server_unregister_error, e.getMessage());
			CommonUtilities.displayMessage(context, message);
		}
	}

	/**
	 * Issue a POST request to the server.
	 * 
	 * @param endpoint
	 *            POST address.
	 * @param params
	 *            request parameters.
	 * 
	 * @throws IOException
	 *             propagated from POST.
	 */
	private static void post(String endpoint, Map<String, String> params)
			throws IOException {

		URL url;
		try {
			url = new URL(endpoint);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("invalid url: " + endpoint);
		}
		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		// constructs the POST body using the parameters
		while (iterator.hasNext()) {
			Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=')
					.append(param.getValue());
			if (iterator.hasNext()) {
				bodyBuilder.append('&');
			}
		}
		String body = bodyBuilder.toString();
		Log.v(TAG, "Posting '" + body + "' to " + url);
		byte[] bytes = body.getBytes();
		HttpURLConnection conn = null;
		try {
			Log.e("URL", "> " + url);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			// post the request
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.close();
			// handle the response
			int status = conn.getResponseCode();
			if (status != 200) {
				throw new IOException("Post failed with error code " + status);
			}
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}
}
