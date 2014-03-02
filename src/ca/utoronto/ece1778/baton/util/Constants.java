package ca.utoronto.ece1778.baton.util;

public final class Constants {
	/**Baton sync server url */
//	public static final String SERVER_URL = "http://54.213.105.123:8080/BatonSyncServer";
	public static final String SERVER_URL = "http://138.51.49.230:8080/BatonSyncServer";
	/**Baton Google project id*/
	public static final String SENDER_ID = "553157495789";
	// For universal usage of time format
	public static String DATE_FORMAT_LONG = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	/**
	 * name of intent filter, used when the GcmIntentService broadcast ticket
	 * message to MainScreenActivity
	 */
	public static final String DISPLAY_TICKET_ACTION = "/ca.utoronto.ece1778.baton.DISPLAY_TICKET";

	//TODO: use these constants in message exchange between server and clients
	public static final String GCM_DATA = "data" ;
	public static final String GCM_DATA_TICKET_TYPE = "ticket_type";
	/**include student name, order, ......*/
	public static final String GCM_DATA_TICKET_CONTENT = "ticket_content";
	public static final String TICKET_TYPE_TALK = "talk";
	public static final String TICKET_TYPE_WORK = "work";

	public final static String TALK_TICKET_TIMESTAMP = "talk_ticket_timestamp";
	public final static String TALK_TICKET_INTENT = "talk_ticket_intent";
	/**participate intent*/
	public final static String TALK_INTENT_BUILD = "talk_build";
	public final static String TALK_INTENT_CHALLENGE = "talk_challenge";
	public final static String TALK_INTENT_QUESTION = "talk_question";
	public final static String TALK_INTENT_NEW_IDEA = "talk_new_idea";

	public final static String SQLLITE_STUDENT_DATABASE_NAME = "baton_student";
	public final static String SQLLITE_TABLE_USER_PROFILE = "GCM_USER_PROFILE";
	public final static String USER_COLUMN_KEY = "id";

}
