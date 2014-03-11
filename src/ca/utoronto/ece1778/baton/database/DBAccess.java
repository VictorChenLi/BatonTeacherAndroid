package ca.utoronto.ece1778.baton.database;

import java.util.List;

import com.baton.publiclib.model.ticketmanage.Ticket;

public interface DBAccess {
	
	public static final String INSERT_TICKET="insert into ticket (uid,ticketType,ticketContent,timeStamp,lid,ticket_status)";
	
	public static final String UPDATE_TICKET="update ticket set uid=?,ticketType=?,ticketContent=?,timeStamp=?,lid=?,ticket_status=? where tid=?";
	
	public static final String SELECT_TICKET="Select * from ticket where uid=?";
	
	public void InsertTicket(Ticket ticket);
	
	public List<Ticket> QueryTicketList(int uid);
	
	public void updateTicket(Ticket ticket);
}
