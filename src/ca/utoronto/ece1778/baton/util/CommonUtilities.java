package ca.utoronto.ece1778.baton.util;

import android.content.Context;
import android.content.Intent;

public class CommonUtilities {


	/**
	 * Tag used on log messages.
	 */
	static final String TAG = "CommonUtilities";

	/**
	 * Notifies UI to display a message.
	 * <p>
	 * This method is defined in the common helper because it's used both by the
	 * UI and the background service.
	 * 
	 * @param context
	 *            application's context.
	 * @param message
	 *            message to be displayed.
	 * 
	 */
	public static void displayMessage(Context context, String message) {
		/*Intent intent = new Intent(Constants.DISPLAY_MESSAGE_ACTION);
		intent.putExtra(Constants.GCM_DATA_TICKET_TYPE, message);
		context.sendBroadcast(intent);*/
	}
	
	
	/**
	 * MD5
	 * 
	 * @param str
	 * @return MD5 of str
	 */
	public static String getMD5Str(String str) {
		//TODO password MD5
		return str;
	}

}
