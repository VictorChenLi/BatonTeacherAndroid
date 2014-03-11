package ca.utoronto.ece1778.baton.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;

import com.baton.publiclib.model.ticketmanage.TalkTicketForDisplay;

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
	
	public static long getDataTime(String strTime)
	{
		Date parseDate=null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_LONG);
		try {
			parseDate = simpleDateFormat.parse(strTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return parseDate.getTime();
	}
	
	public static Object getGlobalStringVar(Activity context,String key)
	{
		GlobalApplication global = (GlobalApplication) context.getApplication();
		return global.getStringVar(key);
	}
	
	public static void putGlobalStringVar(Activity context,String key, String value)
	{
		GlobalApplication global = (GlobalApplication) context.getApplication();
		global.putStringVar(key, value);
	}
	
	public static List<TalkTicketForDisplay> getGlobalTalkArrayVar(Activity context)
	{
		GlobalApplication global = (GlobalApplication) context.getApplication();
		return global.getmTalkTicket4DispArray();
	}
	
	public static void addGlobalTalkVar(Activity context,TalkTicketForDisplay value)
	{
		GlobalApplication global = (GlobalApplication) context.getApplication();
		global.putTalkArrayVar(value);
	}

}
