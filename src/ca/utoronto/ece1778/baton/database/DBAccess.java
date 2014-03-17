package ca.utoronto.ece1778.baton.database;

import java.util.List;

import com.baton.publiclib.model.ticketmanage.Ticket;

public interface DBAccess {
	

	
	public void InsertTicket(Ticket ticket);
	
	public List<Ticket> QueryTicketList(int uid);
	
	public void UpdateTicket(Ticket ticket);
	
	public void ReadDisplayTickets();
	
	public Ticket QueryCurTicket();
	
	public Boolean DetactDatabase();
	
	public void ResetDatabase();
}
