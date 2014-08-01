package ui.searchList;

public class SearchDisplayListItem {

	public int Id;
	public String ThumbNail;
	public String StoreName;
	public String Address;
	public String promotion;
	public String StoreID;
	

	public SearchDisplayListItem(int id, String picture, String StoreName, String Address, String promotion, int StoreID) {
		Id = id;
		ThumbNail = picture;
		this.StoreName = StoreName;
		this.Address = Address;
		this.promotion = promotion;
		this.StoreID = "" + StoreID;  // need so when you delete favorite
	}

}
