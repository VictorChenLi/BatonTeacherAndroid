/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.utoronto.ece1778.baton.gcm.client.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import ca.utoronto.ece1778.baton.TEACHER.R;
import ca.utoronto.ece1778.baton.database.DBAccess;
import ca.utoronto.ece1778.baton.database.DBAccessImpl;
import ca.utoronto.ece1778.baton.util.CommonUtilities;
import ca.utoronto.ece1778.baton.util.Constants;

import com.baton.publiclib.model.classmanage.ClassLesson;
import com.baton.publiclib.model.ticketmanage.TalkTicketForDisplay;
import com.baton.publiclib.model.ticketmanage.Ticket;
import com.baton.publiclib.model.usermanage.UserProfile;
import com.google.android.gcm.GCMBaseIntentService;
//import ca.utoronto.ece1778.baton.models.StudentProfile;
//import ca.utoronto.ece1778.baton.models.Ticket;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";
	private DBAccess dbaccess = null;
	
	public GcmIntentService() {
		super(Constants.SENDER_ID);
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		dbaccess = DBAccessImpl.getInstance(getApplicationContext());
		/*
		 * CommonUtilities.displayMessage(context,
		 * "Your device registred with GCM");
		 */
		// Log.d("NAME", MainActivity.name);
		// BatonServerCommunicator.registerDevice(context, MainActivity.name,
		// MainActivity.email, registrationId);
	}

	/**
	 * Method called on device unregistred
	 * */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		CommonUtilities.displayMessage(context,
				getString(R.string.gcm_unregistered));
		// BatonServerCommunicator.unregister(context, registrationId);
	}

	/**
	 * Method called on Receiving a new message A JSON object whose fields
	 * represents the key-value pairs of the message's payload data. If present,
	 * the payload data it will be included in the Intent as application data,
	 * with the key being the extra's name. For instance, "data":{"score":"3x1"}
	 * would result in an intent extra named score whose value is the string
	 * 3x1. There is no limit on the number of key/value pairs, though there is
	 * a limit on the total size of the message (4kb).
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "onMessage called");
		dbaccess = DBAccessImpl.getInstance(getApplicationContext());
		String ticketType = intent.getStringExtra(Ticket.TICKETTYPE_WEB_STR);
		String ticketContent = intent
				.getStringExtra(Ticket.TICKETCONTENT_WEB_STR);
		String timeStamp = intent.getStringExtra(Ticket.TIMESTAMP_WEB_STR);
		int uid = Integer.valueOf(intent.getStringExtra(Ticket.UID_WEB_STR));
		String loginId = intent.getStringExtra(UserProfile.LOGINID_WEB_STR);
		Intent out = null;
		if (ticketType.equals(Ticket.TICKET_TYPE_TALK)) {
			out = new Intent(Constants.DISPLAY_TALK_TICKET_ACTION);
		} else if (ticketType.equals(Ticket.TICKET_TYPE_WORK)) {
			out = new Intent(Constants.DISPLAY_WORK_TICKET_ACTION);
		} else {//should not be reach in normal case
			out = new Intent();
		}
		out.putExtra(Ticket.TICKETTYPE_WEB_STR, ticketType);
		out.putExtra(Ticket.TICKETCONTENT_WEB_STR, ticketContent);
		out.putExtra(Ticket.TIMESTAMP_WEB_STR, timeStamp);
		out.putExtra(Ticket.UID_WEB_STR, uid);
		out.putExtra(UserProfile.LOGINID_WEB_STR, loginId);
		// save into the database before send broadcast to mainscreen activity
		//get the "raising" ticket from the student with this uid
		Ticket curTicket = dbaccess.QueryRaisingTicket(uid);
		//get the ticket icon displayed with the uid
		TalkTicketForDisplay displayTicket = CommonUtilities.getTicketForDisplay(this.getApplication(), String.valueOf(uid));
		if(curTicket!=null)
		{ //an existing 'raising' ticket in database (and also a ticket displayed)
			//update the content if it is changed
			if(!curTicket.getTicketContent().equals(ticketContent))
			{
				curTicket.setTicketContent(ticketContent);
				displayTicket.setParticipate_intent(ticketContent);
			}
			dbaccess.UpdateTicket(curTicket);
		}
		else
		{ //no 'raising' ticket in database under the uid, insert a new ticket in database
			int lid= Integer.valueOf(CommonUtilities.getGlobalStringVar(this.getApplication(), ClassLesson.LESSONID_WEB_STR));
			curTicket=new Ticket(uid, ticketType, ticketContent, timeStamp, lid, Ticket.TICKETSTATUS_RAISING);
			// insert the ticket in database
			dbaccess.InsertTicket(curTicket);
			if(null==displayTicket){
				//if the student has never had a ticket which was displayed, then it cannot be found in TalkTicketForDisplay list. Create one 
				displayTicket = new TalkTicketForDisplay(timeStamp, loginId,uid, lid, ticketContent,Ticket.TICKETSTATUS_RAISING, 1, 0);
				CommonUtilities.putTicketForDisplay(this.getApplication(), String.valueOf(uid), displayTicket);
			}
		  else
			{ 
			  //it means the ticket in displayList is not raising status
			  //if the student has one ticket in displayList, then +1 the participate time
				displayTicket.setTicket_status(Ticket.TICKETSTATUS_RAISING);
				displayTicket.setParticipate_intent(ticketContent);
				displayTicket.setParticipate_times(dbaccess.QueryParticipateTime(uid, lid)+1);
				displayTicket.setStartTimeStamp(timeStamp);
				CommonUtilities.putTicketForDisplay(this.getApplication(), String.valueOf(uid), displayTicket);
			}
		}
		context.sendBroadcast(out);
	}

	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		// CommonUtilities.displayMessage(context, message);
		// notifies user
		// generateNotification(context, message);
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
		CommonUtilities.displayMessage(context,
				getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		CommonUtilities.displayMessage(context,
				getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	/*
	 * private static void generateNotification(Context context, String message)
	 * { int icon = R.drawable.ic_launcher; long when =
	 * System.currentTimeMillis(); NotificationManager notificationManager =
	 * (NotificationManager) context
	 * .getSystemService(Context.NOTIFICATION_SERVICE); Notification
	 * notification = new Notification(icon, message, when);
	 * 
	 * String title = context.getString(R.string.app_name);
	 * 
	 * Intent notificationIntent = new Intent(context, MainActivity.class); //
	 * set intent so it does not start a new activity
	 * notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
	 * Intent.FLAG_ACTIVITY_SINGLE_TOP); PendingIntent intent =
	 * PendingIntent.getActivity(context, 0, notificationIntent, 0);
	 * notification.setLatestEventInfo(context, title, message, intent);
	 * notification.flags |= Notification.FLAG_AUTO_CANCEL;
	 * 
	 * // Play default notification sound notification.defaults |=
	 * Notification.DEFAULT_SOUND;
	 * 
	 * // Vibrate if vibrate is enabled notification.defaults |=
	 * Notification.DEFAULT_VIBRATE; notificationManager.notify(0,
	 * notification);
	 * 
	 * }
	 */

}
