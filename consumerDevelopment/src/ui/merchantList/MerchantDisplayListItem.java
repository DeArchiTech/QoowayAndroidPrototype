package ui.merchantList;

public class MerchantDisplayListItem implements Comparable<MerchantDisplayListItem> {

	public int Id;
	public float Distance;
	public String ThumbNail;
	public String[] Info;
	public String Created;
	public String StoreID;

	public MerchantDisplayListItem(int id, String picture, String[] info,
			float distance, String created, String StoreID) {
		Id = id;
		ThumbNail = picture;
		Info = info;
		Distance = distance;
		Created = created;
		this.StoreID = StoreID;
	}

	@Override
	public int compareTo(MerchantDisplayListItem item2) {
		// TODO Auto-generated method stub
		return this.Info[0].compareTo(item2.Info[0]);
	}

	public void swapID(MerchantDisplayListItem another) {

		int temp = this.Id;
		this.Id = another.Id;
		another.Id = temp;
	}

}