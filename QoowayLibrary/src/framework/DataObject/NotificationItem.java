package framework.DataObject;

public class NotificationItem {

    public int Id;
    public String CheckInID_;
    public String SenderID_;
    public String checkInMessage_;
    public static String hasCheckedIn = "\nhas checked in.\n"; 
    public Boolean checkPressed =false;
    public Boolean crossPressed =false;
    public String CustomerID;
    public String VoucherCode;
    public String NotificationTime;
    public String CustomerName;
    public String Status;
    public Boolean comeFromIntent;
    
    public NotificationItem(int id , String CheckInID, String SenderID, String userName,String time , 
    		 String customerID, String voucherCode, String checkinDate, String customerName, Boolean comeFromIntent, String Status) 
    {
    	Id = id;
    	CheckInID_ = CheckInID;
    	SenderID_ = SenderID;
    	checkInMessage_ =userName + hasCheckedIn +time;
    	this.CustomerID = customerID;
    	this.VoucherCode = voucherCode;
    	this.NotificationTime = checkinDate;
    	this.CustomerName = customerName;
    	this.comeFromIntent = comeFromIntent;
    	this.Status = Status;
    			
    }
    public void setCheckPressed()
    {
    	checkPressed =true;
    }
    public void setCrossPressed()
    {
    	crossPressed =true;
    }
    

    
}
