package ca.utoronto.ece1778.baton.syncserver;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import ca.utoronto.ece1778.baton.TEACHER.R;
import ca.utoronto.ece1778.baton.database.DBAccess;
import ca.utoronto.ece1778.baton.database.DBAccessImpl;
import ca.utoronto.ece1778.baton.util.CommonUtilities;
import ca.utoronto.ece1778.baton.util.Constants;

import com.baton.publiclib.infrastructure.exception.ErrorCode;
import com.baton.publiclib.infrastructure.exception.ServiceException;
import com.baton.publiclib.model.classmanage.ClassLesson;
import com.baton.publiclib.model.classmanage.VirtualClass;
import com.baton.publiclib.model.ticketmanage.TalkTicketForDisplay;
import com.baton.publiclib.model.ticketmanage.Ticket;
import com.baton.publiclib.model.usermanage.UserProfile;
import com.baton.publiclib.utility.JsonHelper;
import com.google.android.gcm.GCMRegistrar;

//import ca.utoronto.ece1778.baton.models.StudentProfile;

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

	public static final String POST_MESSAGE_FAIL = "post_fail";
	public static final String POST_MESSAGE_SUCCESS = "post_success";
	public static final String REPLY_MESSAGE_REGISTER_SUCCESS = "register_success";
	public static final String REPLY_MESSAGE_REGISTER_FAIL = "register_fail";
	public static final String REPLY_MESSAGE_TICKET_SYNC_SUCCESS = "ticket_sync_success";
	public static final String REPLY_MESSAGE_TICKET_SYNC_FAIL = "ticket_sync_fail";
	public static final String REPLY_MESSAGE_LOGIN_SUCCESS = "login_success";
	public static final String REPLY_MESSAGE_LOGIN_FAIL = "login_fail";

	/**
	 * Register user on the server.
	 * @throws ServiceException 
	 */
	public static String register(final Context context, final UserProfile user) throws ServiceException {
		Log.i(TAG, "registering user:");
		Log.i(TAG, user.toString());

		String serverUrl = Constants.SERVER_URL + "/register?";
		Map<String, String> params = new HashMap<String, String>();
		params.put(UserProfile.GCMID_WEB_STR, user.getGcm_regid());
		params.put(UserProfile.EMAIL_WEB_STR, user.getEmail());
		params.put(UserProfile.FNAME_WEB_STR, user.getF_name());
		params.put(UserProfile.LNAME_WEB_STR, user.getL_name());
		params.put(UserProfile.LOGINID_WEB_STR, user.getLogin_id());
		params.put(UserProfile.PASSWORD_WEB_STR, user.getPassword());
		params.put(UserProfile.USERTYPE_WEB_STR, UserProfile.USERTYPE_TEACHER);
		
		// as for register function there are no return value from server
		post(serverUrl, params);

		GCMRegistrar.setRegisteredOnServer(context, true);
		String message = context.getString(R.string.server_registered);
		Log.i(TAG, "register success");
		return message;
	}

	/**
	 * Login user on the server.
	 * @throws ServiceException 
	 */
	public static String login(final Context context, final String[] token) throws ServiceException {
		Log.i(TAG, "Login user:");
		Log.i(TAG, Arrays.toString(token));

		String serverUrl = Constants.SERVER_URL + "/login?";
		Map<String, String> params = new HashMap<String, String>();
		// params.put(UserProfile.EMAIL_WEB_STR, token[0]);
		params.put(UserProfile.LOGINID_WEB_STR, token[0]);
		params.put(VirtualClass.CLASSROOM_NAME_WEB_STR, token[1]);
		params.put(UserProfile.PASSWORD_WEB_STR, token[2]);
		params.put(UserProfile.GCMID_WEB_STR,
				GCMRegistrar.getRegistrationId(context));
		params.put(UserProfile.TEACHER_LOGINID_WEB_STR, token[0]);

		String retStr = post(serverUrl, params);
		Log.i(TAG, "login success");
		return retStr;
	}

	/**
	 * Unregister this account/device pair within the server.
	 * @throws ServiceException 
	 */
//	public static void unregister(final Context context, final UserProfile user) {
//		Log.i(TAG, "unregistering user");
//		String serverUrl = Constants.SERVER_URL + "/unregister?";
//		Map<String, String> params = new HashMap<String, String>();
//		params.put(UserProfile.GCMID_WEB_STR, user.getGcm_regid());
//		params.put(UserProfile.EMAIL_WEB_STR, user.getEmail());
//		try {
//			post(serverUrl, params);
//			GCMRegistrar.setRegisteredOnServer(context, false);
//			String message = context.getString(R.string.server_unregistered);
//			CommonUtilities.displayMessage(context, message);
//		} catch (IOException e) {
//			// At this point the device is unregistered from GCM, but still
//			// registered in the server.
//			// We could try to unregister again, but it is not necessary:
//			// if the server tries to send a message to the device, it will get
//			// a "NotRegistered" error message and should unregister the device.
//			String message = context.getString(
//					R.string.server_unregister_error, e.getMessage());
//			CommonUtilities.displayMessage(context, message);
//		}
//	}

	/**
	 * syncTicketData will send download the ticket data from the server.
	 * @throws ServiceException 
	 */
	public static List<Ticket> syncTicketData(final Context context,
			final String lessonId) throws ServiceException {
		Log.i(TAG, "Sync Ticket data");
		List<Ticket> ticketList = new ArrayList<Ticket>();
		String serverUrl = Constants.SERVER_URL + "/syncTicketData?";
		Map<String, String> params = new HashMap<String, String>();
		params.put(ClassLesson.LESSONID_WEB_STR, String.valueOf(lessonId));
	
		String retStr = post(serverUrl, params);
		String retObjStr[] = retStr.split("&");
		System.out.println(retObjStr[0]);
		System.out.println(retObjStr[1]);
		ticketList = JsonHelper.deserializeList(retObjStr[0], Ticket.class);
		// put display ticket into memory
		List<TalkTicketForDisplay> displayTicketList = JsonHelper
				.deserializeList(retObjStr[1], TalkTicketForDisplay.class);
		Log.i(TAG,displayTicketList.size() + " tickets got from server");
		for (TalkTicketForDisplay displayTicket : displayTicketList) {
			Log.i(TAG,"ticket startTimeStamp: " + displayTicket.getStartTimeStamp());
			Log.i(TAG,"ticket student: " + displayTicket.getStudent_name());
			/**
			 * modified on 27th March by Fiona, only put raising tickets in memor for display
			 */
			Log.i(TAG, "ticket_status:"+displayTicket.getTicket_status());
			if (Ticket.TICKETSTATUS_RAISING.equals(displayTicket.getTicket_status()))
			    CommonUtilities.putTicketForDisplay((Activity) context,
					String.valueOf(displayTicket.getUid()), displayTicket);
		}

		return ticketList;
	}

	public static void uploadTicketData(final Context context) throws ServiceException {
		Log.i(TAG, "upload Ticket data");
		DBAccess dbaccess = DBAccessImpl.getInstance(context);
		List<Ticket> ticketList = dbaccess.QueryTicketList(0);

		String serverUrl = Constants.SERVER_URL + "/uploadTicketData?";
		Map<String, String> params = new HashMap<String, String>();
		params.put(Ticket.TICKET_LIST_WEB_STR, JsonHelper.serialize(ticketList));
		post(serverUrl, params); 

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
	 * @throws ServiceException 
	 */
	private static String post(String endpoint, Map<String, String> params)
			throws ServiceException {

		URL url;
		String retStr = null;
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
		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// As the server might be down, we will retry it a couple times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {

			Log.d(TAG, "Attempt #" + i);
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
					if(status==400)
					{
						// this is the specific exception
						String strException = readHttpResponseMsg(conn);
						ServiceException se = JsonHelper.deserialize(strException, ServiceException.class);
						throw se;
					}
					else
					{
						// throw the network exception
						throw new ServiceException(ErrorCode.Network_connection_Error_Msg,ErrorCode.LoginId_Not_Exist);
					}
				} else {
					return readHttpResponseMsg(conn);
				}
			} catch (ServiceException se) {
				// we handle the 400 error, which is type of exception we already customized
				// if the errorcode equal to network error, we will try it again
				// otherwise, we will throw this exception to the upper layer to handle
				if(!se.getErrorCode().equals(ErrorCode.Network_connection_Error))
					throw se;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (conn != null) {
					conn.disconnect();
				}
			}
			Log.e(TAG, "Failed on attempt " + i);
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
				throw new ServiceException(ErrorCode.System_Unknow_Error_Msg,ErrorCode.System_Unknow_Error);
			}
			// increase backoff exponentially
			backoff *= 2;
		}
		throw new ServiceException(ErrorCode.Network_connection_Error_Msg,ErrorCode.Network_connection_Error);
	}

	public static class UploadTicketTask extends AsyncTask<Void, Void, Void>
	{
		Context uicontext;

		public UploadTicketTask(Context context)
		{
			uicontext = context;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			DBAccess dbaccess = DBAccessImpl.getInstance(uicontext);
	        if(dbaccess.DetactDatabase())
	        {
	    		try {
					BatonServerCommunicator.uploadTicketData(uicontext);
				} catch (ServiceException e) {
					e.printStackTrace();
					Toast.makeText(uicontext, e.getMessage(), Toast.LENGTH_SHORT).show();
				}
	    		dbaccess.ResetDatabase();
	        }
			return null;
		}
	}
	
	public static String readHttpResponseMsg(HttpURLConnection conn) throws IOException
	{
		InputStream in = new BufferedInputStream(
				conn.getInputStream());
		StringBuffer sb = new StringBuffer("");
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(in));
		String inputLine = "";
		while ((inputLine = reader.readLine()) != null) {
			sb.append(inputLine);
//			sb.append("\n");
		}
		in.close();
		return sb.toString();
	}
}
