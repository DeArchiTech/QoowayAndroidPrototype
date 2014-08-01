package framework.DataObject;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Customer implements Serializable{
	public String CustomerID = "";
	public String Email;
	public String FirstName;
	public String LasName;
	public String PointCardCode;
	public int CardTypeID;
	public int NetPoints;
	public String[] CustomerFavorite;
	public String[] CustomerVoucher;
}
