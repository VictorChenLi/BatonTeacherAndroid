package ca.utoronto.ece1778.baton.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Ticket {
	public static final String TID_WEB_STR = "tid";
	public static final String UID_WEB_STR = "uid";
	public static final String TICKETTYPE_WEB_STR = "ticketType";
	public static final String TICKETCONTENT_WEB_STR = "ticketContent";
	public static final String TIMESTAMP_WEB_STR = "timeStamp";
	
	private int tid;
	private int uid;
	private String ticketType;
	private String ticketContent;
	private String timeStamp;
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getTicketType() {
		return ticketType;
	}
	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}
	public String getTicketContent() {
		return ticketContent;
	}
	public void setTicketContent(String ticketContent) {
		this.ticketContent = ticketContent;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public Ticket(int tid, int uid, String ticketType, String ticketContent,
			String timeStamp) {
		super();
		this.tid = tid;
		this.uid = uid;
		this.ticketType = ticketType;
		this.ticketContent = ticketContent;
		this.timeStamp = timeStamp;
	}
	
	public Ticket(int uid, String ticketType, String ticketContent,
			String timeStamp) {
		super();
		this.uid = uid;
		this.ticketType = ticketType;
		this.ticketContent = ticketContent;
		this.timeStamp = timeStamp;
	}
	
	public Ticket(Map<String, Object> data)
	{
		super();
		this.tid = Integer.valueOf(data.get("tid").toString());
		this.uid = Integer.valueOf(data.get("uid").toString());
		this.ticketType = data.get("ticketType").toString();
		this.ticketContent = data.get("ticketContent").toString();
		this.timeStamp = data.get("timeStamp").toString();
	}
	
	public List<String> getUserData()
	{
		List<String> userData = new ArrayList<String>();
		userData.add(String.valueOf(this.uid));
		userData.add(this.ticketType);
		userData.add(this.ticketContent);
		userData.add(this.timeStamp);
		return userData;
	}
	
}
