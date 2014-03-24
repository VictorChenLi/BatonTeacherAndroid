package ca.utoronto.ece1778.baton.database;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ca.utoronto.ece1778.baton.util.CommonUtilities;

import com.baton.publiclib.model.ticketmanage.TalkTicketForDisplay;
import com.baton.publiclib.model.ticketmanage.Ticket;

public class DBAccessImpl implements DBAccess {
	
	private static DBAccessImpl dbAccessImpl=null;
	
	public static final String INSERT_TICKET="insert into ticket (uid,ticketType,ticketContent,timeStamp,lid,ticket_status) " +
			"values (?,?,?,?,?,?)";
	
	public static final String UPDATE_TICKET="update ticket set uid=?,ticketType=?,ticketContent=?,timeStamp=?,lid=?,ticket_status=? where tid=?";
	
	public static final String SELECT_TICKET_BY_ID="Select * from ticket where uid=? ";
	
	public static final String SELECT_TICKETS = "Select * from ticket";
	
	public static final String SELECT_SPEC_TICKET = "Select * from ticket where uid=?, ticket_status=?";
	
	public static final String DETACT_DB = "Select * from ticket";
	
	public static final String DB_NAME="demoP4.db";
	public static final int VERSION=1;
	public Context udcontext;
	
	SQLiteHelper m_Helper;
	SQLiteDatabase wdb;
	SQLiteDatabase rdb;
	
	private DBAccessImpl(Context context) {
    	udcontext=context;
    	m_Helper=new SQLiteHelper(context,DB_NAME,null,VERSION);
    	try
    	{
            wdb=m_Helper.getWritableDatabase();
            rdb=m_Helper.getReadableDatabase();
    	}
    	catch(Exception ee)
    	{
    		ee.printStackTrace();
    	}
    }
	
	public static synchronized DBAccessImpl getInstance(Context context)
	{
		if(dbAccessImpl==null)
			dbAccessImpl= new DBAccessImpl(context);
		return dbAccessImpl;
	}
	
	public Boolean DetactDatabase()
	{
		String[] bindArgs=null;
		Cursor cursor = rdb.rawQuery(DETACT_DB,bindArgs);
		return cursor.moveToNext();
	}
	
	public void ResetDatabase()
	{
		m_Helper.resetDatabase(wdb);
	}
	
	/********************************** Task Operation********************************/
	
    private List<Ticket> fillList(Cursor cursor)
    {
    	List<Ticket> list=new ArrayList<Ticket>();
        while (cursor.moveToNext())
        	 list.add(new Ticket(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getString(6)));
        return list;
    }

	@Override
	public void InsertTicket(Ticket ticket) {
		Object[] bindArgs = ticket.getUserData().toArray(new Object[6]);
    	wdb.execSQL(DBAccessImpl.INSERT_TICKET,bindArgs);
	}
	
	

	@Override
	public List<Ticket> QueryTicketList(int uid) {
		Cursor cursor=null;
		String[] bindArgs=null;
		String sqlStr=SELECT_TICKETS;
		if(uid!=0)
		{
			bindArgs = new String[]{String.valueOf(uid)};
			sqlStr=SELECT_TICKET_BY_ID;
		}
		cursor = rdb.rawQuery(sqlStr,bindArgs);
		List<Ticket> list = fillList(cursor);
		return list;
	}

	@Override
	public void UpdateTicket(Ticket ticket) {
		List<String> args  = ticket.getUserData();
		args.add(String.valueOf(ticket.getTid()));
		Object[] bindArgs = args.toArray(new Object[7]);
    	wdb.execSQL(DBAccessImpl.UPDATE_TICKET,bindArgs);
	}

	@Override
	public void ReadDisplayTickets() {
		
		
	}

	@Override
	public Ticket QueryCurTicket(int uid) {
		Cursor cursor=null;
		String[] bindArgs= new String[]{String.valueOf(uid), Ticket.TICKETSTATUS_RAISING};
		cursor = rdb.rawQuery(DBAccessImpl.SELECT_SPEC_TICKET,bindArgs);
		List<Ticket> list = fillList(cursor);
		return list.size()==0?null:list.get(0);
	}

	@Override
	public void ResponseTicket(int uid) {
		Ticket ticket = this.QueryCurTicket(uid);
		ticket.setTicket_status(Ticket.TICKETSTATUS_RESPOND);
		this.UpdateTicket(ticket);
	}

	@Override
	public void ResetAllTicket(List<Integer> uidList ) {
		List<Ticket> ticketList = this.QueryTicketList(0);
		for(Ticket ticket : ticketList)
		{
			ticket.setTicket_status(Ticket.TICKETSTATUS_DISCARD);
			this.UpdateTicket(ticket);
		}
	}
	
	
}
