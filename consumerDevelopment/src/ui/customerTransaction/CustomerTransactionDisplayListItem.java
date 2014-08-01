package ui.customerTransaction;

public class CustomerTransactionDisplayListItem {
	
	public int Id;
	public String MerchantName;
	public String OrderTime;
	public String SoldAmount;
	public String Points;
	
	public CustomerTransactionDisplayListItem(int id, String MerchantName, String OrderTime, float SoldAmount, float Points){
		int temp_points = (int)Math.floor(Points);  // round down and convert to integer
		this.Id =id;
		this.MerchantName = MerchantName;
		this.OrderTime = OrderTime;
		this.SoldAmount = ""+"$" + (String.format("%.02f", SoldAmount));
		this.Points =""+ temp_points;
	}

}
