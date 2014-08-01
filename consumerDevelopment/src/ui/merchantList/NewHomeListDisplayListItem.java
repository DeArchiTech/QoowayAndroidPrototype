package ui.merchantList;

public class NewHomeListDisplayListItem{

	public int Id;
	public float Distance;
	public String ThumbNail;
	public String[] Info;
	public String Created;
	public String StoreID;

	public NewHomeListDisplayListItem(int id, String picture, String[] info,
			float distance, String created, String StoreID) {
		Id = id;
		ThumbNail = picture;
		Info = info;
		Distance = distance;
		Created = created;
		this.StoreID = StoreID;
	}	
	/*
	  implements Comparable<NewHomeListDisplayListItem> 
	@Override
	public int compareTo(NewHomeListDisplayListItem item2) {
		// TODO Auto-generated method stub
		return this.Info[0].compareTo(item2.Info[0]);
	}

	public void swapID(NewHomeListDisplayListItem another) {

		int temp = this.Id;
		this.Id = another.Id;
		another.Id = temp;
	}
	
	*/
}
