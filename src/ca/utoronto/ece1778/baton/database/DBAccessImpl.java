package ca.utoronto.ece1778.baton.database;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.baton.publiclib.model.ticketmanage.Ticket;

public class DBAccessImpl implements DBAccess {
	
	private static DBAccessImpl dbAccessImpl=null;
	
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
	
	/********************************** Task Operation********************************/
	
    private List<Ticket> fillList(Cursor cursor)
    {
    	List<Ticket> list=new ArrayList<Ticket>();
        while (cursor.moveToNext())
        	 list.add(new Ticket(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),cursor.getString(3), cursor.getString(4), cursor.getString(5)));
        return list;
    }

	@Override
	public void InsertTicket(Ticket ticket) {
		Object[] bindArgs = ticket.getUserData().toArray(new Object[6]);
    	wdb.execSQL(DBAccess.INSERT_TICKET,bindArgs);
	}

	@Override
	public List<Ticket> QueryTicketList(int uid) {
		String[] bindArgs = new String[]{String.valueOf(uid)};
		Cursor cursor = rdb.rawQuery(DBAccess.SELECT_TICKET,bindArgs);
		List<Ticket> list = fillList(cursor);
		return list;
	}

	@Override
	public void updateTicket(Ticket ticket) {
		List<String> args  = ticket.getUserData();
		args.add(String.valueOf(ticket.getTid()));
		Object[] bindArgs = args.toArray(new Object[7]);
    	wdb.execSQL(DBAccess.UPDATE_TICKET,bindArgs);
	}
}
