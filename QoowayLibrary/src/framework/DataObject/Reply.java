package framework.DataObject;

public class Reply {
	
	public int ReplyID;
	public int ReviewID;
	public String StoreID;
	public String CustomerID;
	public String SourceID;
	public String RepliedName;
	public String Topic;
	public String Remark;
	public String CustomerIP;
	public String DateCreated;
	public String NickName;
	public int Status;
	public int ReviewLevel;
	public int AgreeNum;
	public int DisagreeNum;
	
	public Reply() {
		
	}
	
	public Reply(int replyID, int reviewID, String storeID, String customerID, String sourceID, String repliedName, 
			String topic, String remark, String customerIP, String dateCreated, String nickName, 
			int status, int reviewLevel,int agreeNum, int disagreeNum) {
		ReplyID = replyID;
		ReviewID = reviewID;
		StoreID = storeID;
		CustomerID = customerID;		
		SourceID = sourceID;
		RepliedName = repliedName;
		Topic = topic;
		Remark = remark;
		CustomerIP = customerIP;
		DateCreated = dateCreated;
		NickName = nickName;
		Status = status;
		ReviewLevel = reviewLevel;
		AgreeNum = agreeNum;
		DisagreeNum = disagreeNum;
	}

}
