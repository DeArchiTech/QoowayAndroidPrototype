
package data;

import java.io.InputStream;
import java.io.Serializable;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;



import com.qooway.qoowaylibrary.R;

import data.EnumData.ListType;
import framework.Entry;
import framework.PageFragment;
import framework.QoowayActivity;
import framework.DataObject.CheckIn;
import framework.DataObject.Customer;
import framework.DataObject.CustomerTransaction;
import framework.DataObject.Favorite;
import framework.DataObject.Merchant;
import framework.DataObject.MerchantReview;
import framework.DataObject.NotificationItem;
import framework.DataObject.PostObjectWrapper;
import framework.DataObject.Reply;
import framework.DataObject.Review;
import framework.DataObject.SearchCategoryObject;
import framework.DataObject.Subcategory;
import framework.DataObject.Token;
import framework.DataObject.Transaction;
import framework.DataObject.Voucher;
import framework.DataObject.VoucherGrouped;
import framework.DataObject.VoucherList;
import framework.DataObject.WebCuisine;
import framework.DataObject.WebHeardSiteWay;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;


@SuppressWarnings("serial")
public class DataStorageManager implements Serializable{
	
	public List<Merchant> SearchList; // Ryan
	public List<Entry> MerchantList;
	public List<Merchant> SelectedMerchantList;
	public List<Merchant> HomeMerchantList;
	public List<Subcategory> Subcategories;
	public List<WebCuisine> Webcuisines ;
	public List<VoucherList> RedeemableVoucherList;
	//USE FOR MY VOUCHER ACTIVITTY
	public List<Voucher> RedeemedVoucher ;
	//US FOR REDEEM TAB
	public List<VoucherGrouped> RedeemableVouchersSpecificMerchant;

	//US FOR SPECCIFIC VOUCHERS WITHIN REDEEM POINTS TAB
	/*DEPRECATED I BELIEVE
	
	public List<Voucher> RedeemableVouchers ;
	public List<Voucher> RedeemableVoucher;
	public List<VoucherList> result;
	public List<VoucherGrouped> selectedCustomerVoucherListGrouped;
	public List<Voucher> selectedCustomerVoucherList;
	public Voucher selectedVoucher = null;
	public List<Voucher> UsedVouchers ;*/
	//DEPRECATED I BELIEVE
//	
	public List<Review> SelectedMerchantReviews;
	public List<Reply> SelectedMerchantReplies;
	public List<Review> SelectedCustomerReviews;
	public List<CustomerTransaction> customerTransaction;   // Ryan
	public List<Favorite> Favorites; // ryan
	public List<String> Cuisine;
	public List<String> Neighbourhood;
	public List<String> RestaurantType;
	public List<String> Option;
    public List<String> AveragePrice;
	public List<String> SortBy;
	public List<Merchant> SearchBarMerchantList;
	public Map<String,Boolean> cuisine_map;
	public Map<String,Boolean> neighbourhood_map;
	public Map<String,Boolean> restaurant_type_map;
	public Map<String,Boolean> option_map;
	public Map<String,Boolean> average_price_map;
	public Map<String,Boolean> sort_by_map;
	 
	public Map<String,Boolean> apparel_map;
	public Map<String,Boolean> electronics_map;
	public Map<String,Boolean> entertainment_map;
    public Map<String,Boolean> food_map;
    public Map<String,Boolean> health_map;
	public Map<String,Boolean> home_map;
	public Map<String,Boolean> service_map;

	
	
	public List<String> ApparelList; // Retail list - > Ryan
	public List<String> FoodList;
	public List<String> HomeList;
	public List<String> ElectronicsList;
	public List<String> HealthList;
	public List<String> EntertainmentList;
	public List<String> ServicesList;
	public List<WebHeardSiteWay> WebHeardSiteWay;
	
	public Merchant merchant; // Ryan
	public Boolean loggedIn = false;
	public Boolean fragment_Advanced = true;
	public Boolean fragment_Cuisine =false;
	public Boolean fragment_Category =false;
	public Token deviceToken;
	public Customer currentUser = new Customer();
	public TypeOfUser userType;
	public String userName="david@qooway.com";
	public String password="123456";
	public Merchant selectedMerchant;
	public String loginToken ="";
	public List<CheckIn> checkInList;
	public Location currentLocation;
	public QoowayActivity currentActivity;
	private static DataStorageManager singletonInstance;
	public int lastCode = 0;
	public String lastMessage = "";
	private int AsyncTaskCounter = 0;
	public List<Transaction> TransactionList;
	public List<Review> merchantReview;
	public List<Reply> merchantReply;
	public List<MerchantReview> merchantReviewLib;
	
	public Customer selectedNotificationCustomer;
	public Customer CustomerInfoStorage;
	public Customer CustomerPointMultiplier =null;
	public String serverVersionNumber;
	public Transaction checkInTransaction=null;
	public PostObjectWrapper voucherAndTransaction;
	

	public Customer tempCustomerHolderForInfo;

	public NotificationItem selectedNotificationItem = null;   // used for check in, selected check mark
	public Merchant currentMerchant;
	public Token loginCredentials;
	public String deviceTokenString;
	public boolean checkInActivity = false;
	public String selectedNotificationCustomerPointCard;
	public String serverURL = "";
	public boolean slidingAction = false;
	private PageFragment currentPageFragment;
	private Map< String ,String>  netWorkResult;
	private EnumData.ListType SerializationListType;
	public Context currentContext;
	public EnumData.AppVersion appVersion =EnumData.AppVersion.Release;
	private Boolean MerchantsMaxed=false;
	public Boolean isMerchantFavorited=false;
	public Boolean changingFragment=false;
	public Boolean justSignedUp=false;
	public SearchCategoryObject searchCategoryObject;
	
	protected DataStorageManager (){
		
	}
	
    public static DataStorageManager getSingletonInstance() {

        if (null == singletonInstance) {

            singletonInstance = new DataStorageManager();
        }
        return singletonInstance;

    }
	public void setloggedIn(Boolean bool)
	{
		loggedIn=bool;
	}
	
	public void setUserType(TypeOfUser userType)
	{
		this.userType= userType;
	}
	
	public String getUserID()
	{
		String result = "";
		if(currentUser!=null)
		{
			result = currentUser.CustomerID;
		}
		return result;
	}
	
	public String getMerchantID() {
		String result = "";
		if(currentMerchant!=null)
		{
			result = Integer.toString(currentMerchant.StoreID);
		}
		return result;
	}	
	
	public String getDisplayName()
	{
		String result = "";
		if(currentUser.FirstName!= "" &&  currentUser.LasName!= "")
		{
			result = currentUser.FirstName + " " + currentUser.LasName.charAt(0) + ".";
		}
		else
		{
			result = currentUser.Email;
		}
		return result;
	}

	public enum TypeOfUser
	{
		MERCHANT, CUSTOMER
	}
	//REMOVE  
	public void setCurentPageFragment(PageFragment Fragment)
	{
		this.currentPageFragment = Fragment;
	}
	
	public PageFragment getCurrentPageFragment()
	{
		return this.currentPageFragment ;
	}

	
	public void addNetWorkResult(String key,String result)
	{
		if(this.netWorkResult==null)
		{
			this.netWorkResult = new HashMap<String,String>();
		}
		this.netWorkResult.put(key,result);
	}
	
	public Map<String,String> getNetWorkResult()
	{
		return this.netWorkResult;
	}
	
	public void resetNetWorkResult()
	{
		this.netWorkResult= null;
	}
	
	public void setSerializationListType(EnumData.ListType Key)
	{
		this.SerializationListType=Key;
	}
	
	public ListType getSerializationListType()
	{
		return this.SerializationListType;
	}
	
	public void resetSerializationListType()
	{
		this.SerializationListType=EnumData.ListType.Deprecated;
	}
	
	public void incrementAsyncTask()
	{
		AsyncTaskCounter++;
		/*

				
		if(this.currentActivity!=null) {
			if(currentActivity.getClass().getSimpleName().equals("CheckInActivity")) {
				return;
			}
			if(slidingAction) {
				return;
			}
			//this.currentActivity.showProgressDialogLoad(500);
		}
	*/	
	}
	
	public void decrementAsyncTask()
	{
		AsyncTaskCounter--;
		/*

		*/
	}
	
	public int getAsyncTaskCount()
	{
		return this.AsyncTaskCounter ;
	}
	

	public int getApiKeyStore()
	{
		int returnValue = 1;
		switch(this.appVersion)
		{
		case Demo:
			returnValue=R.raw.mykeystore;
			break;
		case Development:
			returnValue=R.raw.mykeystore;
			break;
		case Release:
			returnValue=R.raw.mykeystore_live;
			break;
		default:
			returnValue=R.raw.mykeystore;
			break;
		}
		return returnValue;
	}

	public String getApiUrl()
	{
		String returnValue = "";
		switch(this.appVersion)
		{
		case Demo:
			returnValue = currentContext.getString(R.string.demoUrl);
			break;
		case Development:
			returnValue = currentContext.getString(R.string.developmentUrl);
			break;
		case Release:
			returnValue = currentContext.getString(R.string.releaseUrl);
			break;
		default:
			returnValue= currentContext.getString(R.string.demoUrl);
			break;
		}
		return returnValue;
	}
	
	public String getApiHostNameVerfier()
	{
		String returnValue = "";
		switch(this.appVersion)
		{
		case Demo:
			returnValue = currentContext.getString(R.string.demoHostname);
			break;
		case Development:
			returnValue = currentContext.getString(R.string.developmentHostname);
			break;
		case Release:
			returnValue = currentContext.getString(R.string.releaseHostname);
			break;
		default:
			returnValue= currentContext.getString(R.string.demoHostname);
			break;
		}
		return returnValue;
	}
	
	public String getKeyStorePassword()
	{
		String returnValue = "";
		switch(this.appVersion)
		{
		case Demo:
			returnValue = currentContext.getString(R.string.demoKeystorePassword);
			break;
		case Development:
			returnValue = currentContext.getString(R.string.developmentKeystorePassword);
			break;
		case Release:
			returnValue = currentContext.getString(R.string.releaseKeystorePassword);
			break;
		default:
			returnValue= currentContext.getString(R.string.demoKeystorePassword);
			break;
		}
		return returnValue;
	}
	
	public void setMaxedMerchants(boolean bool)
	{
		MerchantsMaxed=bool;
	}
	
	public boolean getMaxedMerchants()
	{
		return MerchantsMaxed;
	}
	

}
