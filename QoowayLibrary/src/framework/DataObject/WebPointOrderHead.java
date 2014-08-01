package framework.DataObject;
import java.text.DecimalFormat;
import java.util.Date;
@SuppressWarnings("unused")
public class WebPointOrderHead {

	public Integer OrderID;
	public Date OrderTime;
	public String PointCardCode;
	public String CustomerID;
	public Integer MerchantID;
	public Float SoldAmount;
	public String Reference;
	
	public String OrderType;
	public String Status;
	public String Remark;
	public String OLOStoreID;
	public String OLOStoreName;
	public Float FinalSoldAmount;
	
	public Integer FinalPoints;
	public Integer Points;
	public Integer NetPoints;
	
	public Float PointRate;
	public Date CreateTime;
	public Float OrderFeeAmount;
	public Integer BillId;


	//
	public WebPointOrderHead(String soldAmount, String pointCardCode , String reference , Integer merchantID,String customerID)   // USED FOR RECORD TRANSACTION
	{
		OrderID = 1;
		MerchantID = merchantID;
		Points = 0;
		FinalPoints=0;
		
		
		PointRate= (float) 1.00;
		NetPoints = 0;
		BillId = 1;
		SoldAmount = Float.parseFloat(soldAmount);
		FinalSoldAmount =Float.parseFloat(soldAmount);
		
		float temp_float = (float)1.00;
		DecimalFormat form = new DecimalFormat("0.00");
		String tempSoldAmount = form.format(temp_float);
		
		OrderFeeAmount =  Float.parseFloat(tempSoldAmount);
		OrderType = "I" ;
		Status = "";
		OrderTime = new Date();
		CreateTime = new Date();
		PointCardCode = pointCardCode;
		this.CustomerID = customerID;
		Reference = reference ;
		Remark = "A" ;
		OLOStoreID = "" ;
		OLOStoreName = "" ;
	}
	public WebPointOrderHead(Date missingTime, String reason, String soldAmount,String pointCardCode, String reference, Integer merchantID, String customerID)   // USED for MISSING TRANSACTION
	{
		this.CustomerID = customerID;
		OrderID = 1;
		MerchantID = merchantID;
		
		Points = 0;
		FinalPoints=0;
		NetPoints = 0;
		
		PointRate= (float) 1.00;
		BillId = 0;
		SoldAmount = Float.parseFloat(soldAmount);
		FinalSoldAmount =Float.parseFloat(soldAmount);
		OrderFeeAmount = (float) 1.00;
		OrderType = "I" ;
		Status = "";
		OrderTime = missingTime;
		CreateTime = new Date();    // when you create the date should be always the most recent time
		PointCardCode = pointCardCode;
		
		
		Reference = reference ;
		Remark = "A" ;
		OLOStoreID = "" ;
		OLOStoreName = "" ;
	}
	
	public void setCustomerID(String customerid){
		this.CustomerID = customerid;
	}
	
}
