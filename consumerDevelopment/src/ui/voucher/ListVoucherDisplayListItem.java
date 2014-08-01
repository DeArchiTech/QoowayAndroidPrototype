package ui.voucher;


public class ListVoucherDisplayListItem {

	public int Id;
	public int VoucherCount;
	public String ThumbNail;
	public String Name;
	public String Description;
	public String MerchantID;   // Ryan added Merchant ID
	

	public ListVoucherDisplayListItem (int id, String ThumbNail,String Name, String Description, int MerchantID , int VoucherCount) {
		this.Id = id;
		this.ThumbNail = ThumbNail;
		this.Name = Name;
		this.Description =Description;
		this.MerchantID = "" + MerchantID;
		this.VoucherCount=VoucherCount;
	}
}