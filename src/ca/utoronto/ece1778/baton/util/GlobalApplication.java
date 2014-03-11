package ca.utoronto.ece1778.baton.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;

import com.baton.publiclib.model.ticketmanage.TalkTicketForDisplay;
import com.baton.publiclib.model.ticketmanage.WorkTicketForDisplay;

public class GlobalApplication extends Application {
	public static final String GLOBAL_VAR_TALK_TICKET_4_DISPLAY_ARRAY = "talk_ticket4Display"; //key name
	private Map<String, String> mStringData; 
	private List<TalkTicketForDisplay> mTalkTicket4DispArray ; 
	private List<WorkTicketForDisplay> mWorkTicket4DispArray ; 
    
    public List<TalkTicketForDisplay> getmTalkTicket4DispArray() {
		return mTalkTicket4DispArray;
	}
	public List<WorkTicketForDisplay> getmWorkTicket4DispArray() {
		return mWorkTicket4DispArray;
	}
	
    public Map<String, String> getStringData() {  
        return mStringData;  
    } 
  
    public void putStringVar(String key, String value)
    {
    	mStringData.put(key, value);
    }
    
    public String getStringVar(String key)
    {
    	return mStringData.get(key).toString();
    }
    
    public void putTalkArrayVar(TalkTicketForDisplay data){
    	mTalkTicket4DispArray.add(data);
    }
    
    public TalkTicketForDisplay getTalkArrayVar(int position){
    	return mTalkTicket4DispArray.get(position);
    }
    
    public void putWorkArrayVar(WorkTicketForDisplay data){
    	mWorkTicket4DispArray.add(data);
    }
    
    public WorkTicketForDisplay getWorkArrayVar(int position){
    	return mWorkTicket4DispArray.get(position);
    }
    


    
    @Override  
    public void onCreate() {  
        super.onCreate();  
          
        mStringData = new HashMap<String, String>();  
        mTalkTicket4DispArray = new ArrayList<TalkTicketForDisplay>();
        mWorkTicket4DispArray = new ArrayList<WorkTicketForDisplay>();
        
        //synchronized the map  
        mStringData = Collections.synchronizedMap(mStringData);   
        mTalkTicket4DispArray = Collections.synchronizedList(mTalkTicket4DispArray);
        mWorkTicket4DispArray = Collections.synchronizedList(mWorkTicket4DispArray);
          
        // then restore your map  
          
    }  
      
    public void onTerminate() {  
        super.onTerminate();  
          
        //save data of the map  
    }  
}
