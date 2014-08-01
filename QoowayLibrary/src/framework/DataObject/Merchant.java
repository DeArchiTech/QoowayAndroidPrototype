package framework.DataObject;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Merchant implements Serializable {
	public int StoreID;
	public String Name;
	public String Address;
	public String PostalCode;
	public String Phone;
	public String City;
	public String Province;
	public String Country;
	public String Longitude;
	public String Latitude;
	public String Region;
	public String LogoPath;
	public String AreaCode;
	public String Website;
	public String MenuLink;
	public String PayOption;
	public String Neighborhood;
	public String DressCode;
	public String Parking;
	public String SpecialOffer;
	public String OpenClosOther1;
	public String OpenClosOther2;
	public String OpenClosOther3;
	public String OpenClosOther4;
	public String OpenClosOther5;
	public String OpenClosOther6;
	public String MerchantType;
	public String OtherInfo;
	public int PriceLegend;
	public int Score;
	public String AdditionalInfo;
	public String CanAcceptsWalkin;
	public String CanCatering;
	public String CanTakeout;
	public String CanDelivery;
	public String Dinningstyle;
	public String MerchantPrograms;
	public String[] WebCuisine;
	public String[] WebMealType;
	public String[] Subcategory;
	public Date CreateTime;
}
