package ui.voucher;

public class VoucherDisplayListItem   {

	public int Id;
	public String ThumbNail;
	public String Name;
	public String Description;
	public String MerchantID;   // Ryan added Merchant ID
	

	public VoucherDisplayListItem (int id, String ThumbNail,String Name, String Description, int MerchantID) {
		this.Id = id;
		this.ThumbNail = ThumbNail;
		this.Name = Name;
		this.Description =Description;
		this.MerchantID = "" + MerchantID;
	}
}