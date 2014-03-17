package ca.utoronto.ece1778.baton.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import ca.utoronto.ece1778.baton.TEACHER.R;
import ca.utoronto.ece1778.baton.syncserver.BatonServerCommunicator;

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
	
	public static Bitmap getRoundedCornerBitmap(Context context, int drawable_resource, int pixels) {
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                drawable_resource);
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
	
	public static String getGlobalStringVar(Application application,String key)
	{
		GlobalApplication global = (GlobalApplication) application;
		return global.getStringVar(key);
	}
	
	public static void putGlobalStringVar(Application application,String key, String value)
	{
		GlobalApplication global = (GlobalApplication) application;
		global.putStringVar(key, value);
	}
	
	public static String getGlobalStringVar(Activity context,String key)
	{
		GlobalApplication global = (GlobalApplication) context.getApplication();
		return global.getStringVar(key);
	}
	
	public static void putGlobalStringVar(Activity context,String key, String value)
	{
		GlobalApplication global = (GlobalApplication) context.getApplication();
		global.putStringVar(key, value);
	}
	
	public static TalkTicketForDisplay getTicketForDisplay(Activity context, String key)
	{
		GlobalApplication global = (GlobalApplication) context.getApplication();
		return global.getTicketVar(key);
	}
	
	public static void putTicketForDisplay(Activity context, String key, TalkTicketForDisplay ticket)
	{
		GlobalApplication global = (GlobalApplication) context.getApplication();
		global.putTicketVar(key, ticket);
	}
	
	public static TalkTicketForDisplay getTicketForDisplay(Application application, String key)
	{
		GlobalApplication global = (GlobalApplication) application;
		return global.getTicketVar(key);
	}
	
	public static void putTicketForDisplay(Application application, String key, TalkTicketForDisplay ticket)
	{
		GlobalApplication global = (GlobalApplication) application;
		global.putTicketVar(key, ticket);
	}
	
	public static boolean isContainKey(Activity context, String key)
	{
		GlobalApplication global = (GlobalApplication) context.getApplication();
		return global.isContainKey(key);	
	}
	
	public static List<TalkTicketForDisplay> getTicketForDisplayList(Activity context)
	{
		GlobalApplication global = (GlobalApplication) context.getApplication();
		return global.getTicketForDisplayList();
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
