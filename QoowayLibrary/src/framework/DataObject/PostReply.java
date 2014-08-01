package framework.DataObject;

import java.util.Date;

public class PostReply {
	public int ReplyID;
	public int ReviewID;
	public String StoreID;
	public String CustomerID;
	public String SourceID;
	public String RepliedName;
	public String Remark;
	public String CustomerIP;
	public Date DateCreated;
	public String NickName;
	public int Status;
	public int ReviewLevel;
	public int AgreeNum;
	public int DisagreeNum;
	

	/*
	Reference from API call
	"ReplyID": 4,
	"ReviewID": 1,
	"StoreID": "393",
	"CustomerID": "8081112471260922",
	"SourceID": "sample string 4",
	"RepliedName": "test",
	"Topic": "test",
	"Remark": "good",
	"CustomerIP": "sample string 8",
	"DateCreated": "2014-01-13T10:19:24.667",
	"NickName": "John",
	"Status": 1,
	"ReviewLevel": 1,
	"AgreeNum": 1,
	"DisagreeNum": 1
	*/
}
