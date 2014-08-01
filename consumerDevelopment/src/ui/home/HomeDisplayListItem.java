package ui.home;

public class HomeDisplayListItem {
	public int Id;
	public String StoreName;
	public String Address;
	public String logoPath;
	public String SpecialDeal;
	public int StoreID;
	
	public HomeDisplayListItem(int id, String StoreName, String Address, String logoPath, String SpecialDeal, int StoreID) {
		Id = id;
		this.StoreName = StoreName;
		this.Address = Address;
		this.logoPath = logoPath;
		this.SpecialDeal = SpecialDeal;
		this.StoreID = StoreID;
	}

}
