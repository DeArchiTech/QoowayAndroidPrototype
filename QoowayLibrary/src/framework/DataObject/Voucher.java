package framework.DataObject;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Voucher implements Serializable{

    public String LogoPath;
    public String MerchantName;
    public String VoucherTypeDesc ;
    public int MerchantID ;
    public String CustomerID ;
    public String VoucherCode ;
    
    public Voucher() {
    	
    }
    
    public Voucher(VoucherGrouped vg, String VoucherCode){
    	this.LogoPath = vg.LogoPath;
    	this.MerchantName = vg.MerchantName;
    	this.VoucherTypeDesc = vg.VoucherTypeDesc;
    	this.MerchantID = vg.MerchantID;
    	this.CustomerID = vg.CustomerID;
    	this.VoucherCode = VoucherCode;
    }    
}
