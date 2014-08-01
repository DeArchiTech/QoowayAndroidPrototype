package data;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;




import framework.PageFragment;
import framework.QoowayActivity;
import framework.DataObject.Customer;
import framework.DataObject.Merchant;
import framework.DataObject.Reply;
import framework.DataObject.Token;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class WebApiManagerPageFragment {

	private QoowayActivity activity;
	private String httpServerAddress;
	private String httpsServerAddress;
	public Boolean json = true;
	private static WebApiManagerPageFragment singletonInstance;
	DataStorageManager dataStorageManager = DataStorageManager
			.getSingletonInstance();
	Toast toast;

	public static WebApiManagerPageFragment getSingletonInstance(QoowayActivity msa) {
			singletonInstance = new WebApiManagerPageFragment();
			singletonInstance.activity = msa;
			singletonInstance.httpServerAddress = DataStorageManager
					.getSingletonInstance().getSingletonInstance().getApiUrl();
			singletonInstance.httpsServerAddress = DataStorageManager
					.getSingletonInstance().getSingletonInstance().getApiUrl();
		return singletonInstance;
	}

	public static WebApiManagerPageFragment getSingletonInstance() {
		if (null == singletonInstance) {
			WebApiManagerPageFragment.getSingletonInstance(null);
		}
		return singletonInstance;
	}

	public Boolean getCustomerLogin(String username, String password)
			throws InterruptedException, ExecutionException,
			UnsupportedEncodingException {

		dataStorageManager.userName = username;
		dataStorageManager.password = password;

		String base = "api/login/customer?username=";
		String toAppend = username + "&" + "password" + "=" + password;
		return this.webApiHttpsGet(base + toAppend, EnumData.request_mode.LogIN);
	}

	public Boolean getCustomerTokenLogin(String username, String password,
			String regID) throws InterruptedException, ExecutionException,
			UnsupportedEncodingException {

		dataStorageManager.userName = username;
		dataStorageManager.password = password;
		String base = "api/Login/LoginCustomer?username=";
		String toAppend = username + "&" + "password" + "=" + password;
		String toAppend2 = "&Token=" + regID + "&OS=Android&App=Consumer";
		return this.webApiHttpsGet(base + toAppend + toAppend2,
				EnumData.request_mode.LogIN);
	}

	public InputStream getLogo(String MerchantID) throws InterruptedException,
			ExecutionException, UnsupportedEncodingException {
		String base = "api/Picture/GetLogo/";
		String toAppend = MerchantID;
		InputStream result = this.webApiHttpsGetImage(base + toAppend,
				EnumData.request_mode.Query);

		return result;

	}

	public Boolean getCustomerInfoByToken(String loginToken)
			throws InterruptedException, ExecutionException,
			UnsupportedEncodingException {
		String base = "api/Login/GetCustomerWithLoginToken?LogInToken=";
		String toAppend = loginToken;
		return this.webApiHttpsGet(base + toAppend, EnumData.request_mode.Query);
	}

	public Boolean getMerchantInfoByToken(String token)
			throws InterruptedException, ExecutionException,
			UnsupportedEncodingException {
		String base = "api/Login/GetMerchantWithLoginToken?LogInToken=";
		String toAppend = token;
		return this.webApiHttpsGet(base + toAppend, EnumData.request_mode.LogIN);
	}

	public Boolean getVersionName() throws InterruptedException,
			ExecutionException, UnsupportedEncodingException {
		String base = "api/AppData/AppVersion?OS=Android&App=Consumer";
		return this.webApiHttpsGet(base, EnumData.request_mode.Query);
	}

	/*
	 * public String webApiHttpsGetVersion(String url, EnumData.request_mode rm) throws
	 * InterruptedException, ExecutionException { String[] urlToSend = {
	 * "https://" + "online.profitek.com/mobileapi_share" + "/" + url };
	 * 
	 * ConnectivityManager connMgr = (ConnectivityManager) activity
	 * .getSystemService(Context.CONNECTIVITY_SERVICE);
	 * 
	 * NetworkInfo networkInfo = connMgr.getActiveNetworkInfo(); if (networkInfo
	 * != null && networkInfo.isConnected()) { HttpRequestTask task = null; if
	 * (json) { task = new HttpRequestTask(activity, rm); } else { task = new
	 * HttpRequestTask(activity, true); } // activity.showProgressDialog();
	 * return task.execute(urlToSend).get(); } else { return null; } }
	 */
	public Boolean getVersionNumber() throws InterruptedException,
			ExecutionException {
		return this.webApiHttpsGet(
				"api/AppData/AppVersion?OS=Android&App=Merchant",
				EnumData.request_mode.Query);
	}

	public Boolean getCustomerSignOut(String loginToken)
			throws InterruptedException, ExecutionException,
			UnsupportedEncodingException {
		String base = "api/Login/LogoutCustomer?LoginToken=";
		String toAppend = loginToken;
		return this
				.webApiHttpsGet(base + toAppend, EnumData.request_mode.Query);
/*PROACTIVE
		String text = "";

		int duration = Toast.LENGTH_SHORT;
		if (result == null) {
			text = "Logout failed";
		} else if (result.equals("true")) {
			text = "You have logged out";
		} else {
			text = "Logout failed";
		}
		toast = Toast.makeText(this.activity, text, duration);
		toast.show();

		return result;*/
	}

	public Boolean getMerchantTokenLogin(String username, String password,
			String regID) throws InterruptedException, ExecutionException,
			UnsupportedEncodingException {

		dataStorageManager.userName = username;
		dataStorageManager.password = password;
		String base = "api/Login/LoginMerchant?username=";
		String toAppend = username + "&" + "password" + "=" + password;
		String toAppend2 = "&Token=" + regID + "&OS=Android&App=Merchant";
		return this.webApiHttpsGet(base + toAppend + toAppend2,
				EnumData.request_mode.LogIN);
	}

	public Boolean getMerchantLoginNew(String username, String password,
			String token) throws InterruptedException, ExecutionException,
			UnsupportedEncodingException {
		String base = "https://online.profitek.com/appdevelopment/api/Login/LoginMerchant?username=";
		String toAppend = username + "&password=" + password + "&Token="
				+ token + "&OS=" + "Android" + "&App=" + "Merchant";
		return this.webApiHttpsGet2(base + toAppend, EnumData.request_mode.LogIN);
	}

	public Boolean getMerchantSignOut(String loginToken)
			throws InterruptedException, ExecutionException,
			UnsupportedEncodingException {
		String base = "api/Login/LogoutMerchant?LoginToken=";
		String toAppend = loginToken;
		return  this
				.webApiHttpsGet(base + toAppend, EnumData.request_mode.Query);
/*PROACTIVE
		String text = "";

		int duration = Toast.LENGTH_SHORT;
		if (result.equals("true")) {
			text = "You have logged out";
		} else {
			text = "Logout failed";
		}
		toast = Toast.makeText(this.activity, text, duration);
		toast.show();

		return result;*/
	}

	public Boolean getMerchantLogin(String username, String password)
			throws InterruptedException, ExecutionException,
			UnsupportedEncodingException {

		DataStorageManager dataStorageManager = DataStorageManager
				.getSingletonInstance();
		dataStorageManager.userName = username;
		dataStorageManager.password = password;
		String base = "api/login/merchant?username=";
		String toAppend = username + "&" + "password" + "=" + password;
		return this.webApiHttpsGet(base + toAppend, EnumData.request_mode.LogIN);
	}

	public Boolean getMerchantTransactions(String ID)
			throws InterruptedException, ExecutionException {
		return this.webApiHttpGet(httpServerAddress + '/'
				+ "api/transaction/Merchant?MerchantID=" + ID,
				EnumData.request_mode.Query);

	}

	public Boolean getMerchantCheckIns(String MerchantID)
			throws InterruptedException, ExecutionException {
		return this.webApiHttpsGet("api/CheckIn/recentCheckIn?MerchantID="
				+ MerchantID, EnumData.request_mode.Query);
	}

	public Boolean getReplies(int MerchantID) throws InterruptedException,
			ExecutionException {
		return this.webApiHttpsGet("api/Review/getWebReviewReply?MerchantID="
				+ MerchantID, EnumData.request_mode.Query);
	}

	public Boolean getReply(int ReviewID) throws InterruptedException,
			ExecutionException {
		return this.webApiHttpsGet("api/Review?ReviewID=" + ReviewID,
				EnumData.request_mode.Query);
	}

	public Boolean getCustomerLogout(String ID, String token)
			throws InterruptedException, ExecutionException,
			UnsupportedEncodingException {
		return  this.webApiHttpsGet("api/Token/delete/" + ID
				+ "?Token=" + token + "&OS=Android&App=Customer",
				EnumData.request_mode.Query);

	
	}

	public Boolean getCustomerTransactions(String ID)
			throws InterruptedException, ExecutionException {
		return this.webApiHttpGet(httpServerAddress + '/'
				+ "api/transaction/Customer?CustomerID=" + ID,
				EnumData.request_mode.Query);

	}

	public Boolean getMerchantInfo(String ID) // Ryan Addition , to get Merchant
												// Name with Merchant ID
			throws InterruptedException, ExecutionException {
		return this.webApiHttpGet(httpServerAddress + '/'
				+ "api/Merchant/GetMerchant/" + ID, EnumData.request_mode.Query);
	}

	public Boolean getCustomer(String customerID) throws InterruptedException,
			ExecutionException, UnsupportedEncodingException {

		String base = "api/Customer?customerID=";
		String toAppend = customerID;
		return this.webApiHttpsGet(base + toAppend, EnumData.request_mode.LogIN);
	}

	public Boolean getCustomerInfo(String ID) throws InterruptedException,
			ExecutionException {
		return this.webApiHttpGet(httpServerAddress + '/'
				+ "api/Customer/GetCustomer?customerID=" + ID,
				EnumData.request_mode.Query);
	}

	public Boolean getApiMerchant() throws InterruptedException,
			ExecutionException {
		return this.webApiHttpGet(httpServerAddress + '/' + "api/merchant",
				EnumData.request_mode.Query);
	}

	public Boolean getApiMerchant(int start, int count)
			throws InterruptedException, ExecutionException {

		String base = "api/Merchant/GetMerchant?startIndex=";
		String toAppend = start + "&count=" + count;
		return this.webApiHttpGet(httpServerAddress + '/' + base + toAppend,
				EnumData.request_mode.Query);
	}

	public Boolean getApiMerchantSort(int start, int count, double latitude,
			double longitude) throws InterruptedException, ExecutionException {
		String base = "api/Merchant/GetMerchantSortLocation?startIndex=";
		String toAppend = start + "&count=" + count + "&latitude=" + latitude
				+ "&longitude=" + longitude;
		return this.webApiHttpGet(httpServerAddress + '/' + base + toAppend,
				EnumData.request_mode.Query);
	}

	public Boolean getApiWebCuisine() throws InterruptedException,
			ExecutionException {
		return this.webApiHttpsGet("api/WebCuisine", EnumData.request_mode.Query);
	}

	public Boolean getApiSubcategory() throws InterruptedException,
			ExecutionException {
		return this.webApiHttpsGet("api/Subcategory", EnumData.request_mode.Query);
	}

	public Boolean getApiMerchant(String Category) throws InterruptedException,
			ExecutionException, UnsupportedEncodingException {
		String toAppend = URLEncoder.encode(Category, "UTF-8");
		return this.webApiHttpsGet("api/merchant?info=" + toAppend,
				EnumData.request_mode.Query);
	}

	public Boolean getVoidTransactions(String ID) throws InterruptedException,
			ExecutionException {
		return this.webApiHttpGet(httpServerAddress + '/'
				+ "api/Transaction/Voidable?MerchantID=" + ID,
				EnumData.request_mode.Query);

	}

	public Boolean getCheckInConfirm(String CustomerID, int MerchantID,
			int Confirm, String CheckInID, int OrderID)
			throws InterruptedException, ExecutionException {
		return this
				.webApiHttpGet(
						"online.profitek.com/appdevelopment/api/CheckIn/checkInConfirmation?CustomerID="
								+ CustomerID
								+ "&MerchantID="
								+ MerchantID
								+ "&confirm="
								+ Confirm
								+ "&CheckInID="
								+ CheckInID + "&OrderID=" + OrderID,
						EnumData.request_mode.Query);
	}

	public Boolean getCheckInDecline(String CustomerID, int MerchantID,
			int Confirm, String CheckInID) throws InterruptedException,
			ExecutionException {
		return this.webApiHttpsGet(
				"api/CheckIn/checkInConfirmation?CustomerID=" + CustomerID
						+ "&MerchantID=" + MerchantID + "&confirm=" + Confirm
						+ "&CheckInID=" + CheckInID + "&OrderID=" + "1",
				EnumData.request_mode.Query);
	}

	public Boolean getPointCalculation(String pointCardCode, String MerchantID,
			String soldAmount) throws InterruptedException, ExecutionException,
			UnsupportedEncodingException {

		return this.webApiHttpGet("online.profitek.com/appdevelopment" + '/'
				+ "api/Transaction/Calcualtion?pointCardCode=" + pointCardCode
				+ "&MerchantID=" + MerchantID + "&soldAmount=" + soldAmount,
				EnumData.request_mode.Query);

	}

	public Boolean postApiRegister(String json) throws InterruptedException,
			ExecutionException {
		Toast toast = null;
		return this.webApiHttpPost("api/Register", json,
				EnumData.request_mode.Query);
		/*BE PROACTIVE
		String text = "";
		int duration = Toast.LENGTH_SHORT;
		// Changed result from Created to "OK" , database returns OK

		if (result == null) {
			text = "Server Error. Application Not Sent";
		} else if (result.equals("OK")) {
			text = "Application Sent";
		} else {
			text = "Application Not Sent";
		}
		toast = Toast.makeText(this.activity, text, duration);
		toast.show();
		return result;*/
	}

	public Boolean getVoucher(int MerchantID) throws InterruptedException,
			ExecutionException {
		return this.webApiHttpGet(httpServerAddress + '/'
				+ "api/voucher/Merchant?MerchantID=", EnumData.request_mode.Query);
	}

	public Boolean getVoucherAll() throws InterruptedException,
			ExecutionException {
		return this.webApiHttpGet(httpServerAddress + '/'
				+ "api/Voucher/GetVoucher", EnumData.request_mode.Query);
	}

	public Boolean getVoucherList() throws InterruptedException,
			ExecutionException {
		return this.webApiHttpGet(httpServerAddress + '/'
				+ "api/Voucher/GetVoucherList", EnumData.request_mode.Query);
	}

	public Boolean getVoucherRedeemed(String CustomerID)
			throws InterruptedException, ExecutionException {
		return this.webApiHttpsGet("api/Voucher/Redeemed?CustomerID="
				+ CustomerID + "&Grouped=0", EnumData.request_mode.Query);
	}

	public Boolean getVoucherRedeemed(String customerPointCard, int MerchantID)
			throws InterruptedException, ExecutionException {
		return this.webApiHttpsGet("api/voucher/redeemed?customerPointCard="
				+ customerPointCard + "&MerchantID=" + MerchantID,
				EnumData.request_mode.Query);
	}

	public Boolean getVoucherRedeemedWithID(String customerID, int MerchantID)
			throws InterruptedException, ExecutionException {
		return this.webApiHttpsGet("api/Voucher?customerID=" + customerID
				+ "&MerchantID=" + MerchantID, EnumData.request_mode.Query);
	}

	public Boolean getVoucherUsed(String CustomerID)
			throws InterruptedException, ExecutionException {
		return this.webApiHttpsGet("api/voucher/used?CustomerID=" + CustomerID,
				EnumData.request_mode.Query);
	}

	public Boolean getReview(String CustomerID) throws InterruptedException,
			ExecutionException {
		return this.webApiHttpsGet(
				"api/Review/getWebReviewByCustomerID?CustomerID=" + CustomerID,
				EnumData.request_mode.Query);
	}

	public Boolean getReview(int MerchantID) throws InterruptedException,
			ExecutionException {
		return this.webApiHttpsGet(
				"api/Review/getWebReviewByMerchantID?MerchantID=" + MerchantID,
				EnumData.request_mode.Query);
	}

	public Boolean getBarcode(String CustomerID) throws InterruptedException,
			ExecutionException {
		return this.webApiHttpsGet("api/Picture/GetCustomerBarCodeLogo/"
				+ CustomerID, EnumData.request_mode.Query);

	}

	public Boolean getRetailCategories(String CategoryDesc)
			throws InterruptedException, ExecutionException {
		return this.webApiHttpsGet(
				"api/Categories/getSubcategoryDesc?CategoryDesc="
						+ CategoryDesc, EnumData.request_mode.Query);

	}

	public Boolean getSearchBar(String search) throws InterruptedException,
			ExecutionException {
		return this.webApiHttpsGet("api/Search?query=" + search,
				EnumData.request_mode.Query);

	}

	public Boolean postApiReviewReply(String json) throws InterruptedException,
			ExecutionException {
		Toast toast = null;
		return this.webApiHttpPost("api/Review/PostReviewReply", json,
				EnumData.request_mode.Query);
		/*BE PROACTIVEString text = "";
		int duration = Toast.LENGTH_SHORT;
		// Changed result from Created to "OK" , database returns OK
		if (result == null) {
			text = "Server Error. Please try again later";
		} else if (result.equals("OK")) {
			text = "Reply Added.";
		} else {
			text = "Reply Can Not be Added.";
		}
		toast = Toast.makeText(this.activity, text, duration);
		toast.show();
		return result;*/
	}

	// Ryan addition
	public Boolean postDeleteFavorite(String CustomerID, String StoreID)
			throws InterruptedException, ExecutionException {

		return webApiHttpPostDelete("api/Favourite/DeleteFavorite?StoreID="
				+ StoreID + "&CustomerID=" + CustomerID, null,
				EnumData.request_mode.Query);

	}

	public Boolean postDeleteReply(String ReplyID) throws InterruptedException,
			ExecutionException {

		return this.webApiHttpPostDelete(
				"api/Review/DeleteWebReviewReply?WebReviewReplyID=" + ReplyID,
				null, EnumData.request_mode.Query);

	}

	// Ryan addition
	public Boolean getCustomerFavorites(String CustomerID)
			throws InterruptedException, ExecutionException {

		return this.webApiHttpsGet(
				"api/Merchant/GetMerchantsFavourited?customerID=" + CustomerID,
				EnumData.request_mode.Query);

	}

	public Boolean checkFavorite(String CustomerID, String MerchantID)
			throws InterruptedException, ExecutionException {

		return this
				.webApiHttpPostFavorite(
						"api/Favourite/checkCustomerFavoruite?StoreID="
								+ MerchantID + "&CustomerID=" + CustomerID,
						null, EnumData.request_mode.Query);
		//return result;

	}

	public Boolean postReplyEdit(int replyID, String json)
			throws InterruptedException, ExecutionException {
		Toast toast = null;
		return this.webApiHttpPostEdit(
				"api/Review?ReplyID=" + replyID, json, EnumData.request_mode.Query);
	/*BE PROACTIVE	String text = "";
		int duration = Toast.LENGTH_SHORT;
		// Changed result from Created to "OK" , database returns OK
		if (result == null) {
			text = "Server Error. Please try again later";
		} else if (result.equals("OK")) {
			text = "Reply Edited";
		} else {
			text = "Reply Can Not Be Edited";
		}
		toast = Toast.makeText(this.activity, text, duration);
		toast.show();
		return result;*/
	}

	public Boolean postCustomerFavorite(String CustomerID, String MerchantID)
			throws InterruptedException, ExecutionException {
		return this.webApiHttpsPost("api/Favourite/PostFavorite?StoreID=" + MerchantID
				+ "&CustomerID=" + CustomerID, null, EnumData.request_mode.Query);
	}

	public Boolean getCheckInRequest(String CustomerID, int MerchantID)
			throws InterruptedException, ExecutionException {
		String temp = this.httpsServerAddress;
		// this.httpsServerAddress = "online.profitek.com/MobileAPI_Share";
		return  this.webApiHttpsGet("api/Check"
				+ "In/checkInRequest?CustomerID=" + CustomerID + "&MerchantID="
				+ MerchantID, EnumData.request_mode.Query);
		/*
		 * return this.webApiHttpsGet("api/Check" +
		 * "In/checkInRequest?CustomerID=" + CustomerID + "&MerchantID=" +
		 * MerchantID, EnumData.request_mode.Query);
		 */
	/*BE PROACTIVE	this.httpsServerAddress = temp;
		return result;*/
	}

	public Boolean getCheckInCancel(String CustomerID, int MerchantID)
			throws InterruptedException, ExecutionException {
		String temp = this.httpsServerAddress;
		// this.httpsServerAddress = "online.profitek.com/MobileAPI_Share";
		return this.webApiHttpsGet(
				"api/CheckIn/cancelCheckIn?CustomerID=" + CustomerID
						+ "&MerchantID=" + MerchantID, EnumData.request_mode.Query);
		/*BE PROACTIVE	this.httpsServerAddress = temp;
		return result;*/
	}

	public Boolean getCheckInRequestVoucher(String CustomerID, int MerchantID,
			String Voucher) throws InterruptedException, ExecutionException {
		String temp = this.httpsServerAddress;
		// this.httpsServerAddress = "online.profitek.com/MobileAPI_Share";
		return this.webApiHttpsGet("api/CheckIn?" + "CustomerID="
				+ CustomerID + "&MerchantID=" + MerchantID + "&Voucher="
				+ Voucher, EnumData.request_mode.Query);
		/*BE PROACTIVE	this.httpsServerAddress = temp;
		return result;*/
	}

	/*
	 * public String getCheckInRequest(String CustomerID, int MerchantID ,
	 * String VoucherCode) throws InterruptedException, ExecutionException {
	 * //Temporary solution String temp = this.httpsServerAddress;
	 * this.httpsServerAddress = "online.profitek.com/MobileAPI_Share"; String
	 * result = this.webApiHttpsGet("api/Check" +
	 * "In/checkInRequest?CustomerID=" + CustomerID + "&MerchantID=" +
	 * MerchantID, EnumData.request_mode.Query); /* return
	 * this.webApiHttpsGet("api/Check" + "In/checkInRequest?CustomerID=" +
	 * CustomerID + "&MerchantID=" + MerchantID, EnumData.request_mode.Query);
	 * this.httpsServerAddress = temp; return result; }
	 */
	public Boolean getCheckStatus(String CustomerID, int MerchantID)
			throws InterruptedException, ExecutionException {

		return this.webApiHttpsGet("api/CheckIn/checkStatus?CustomerID="
				+ CustomerID + "&MerchantID=" + MerchantID, EnumData.request_mode.Query);

	}

	public Boolean getCancelCheckIn(String CustomerID, int MerchantID)
			throws InterruptedException, ExecutionException {

		return this.webApiHttpsGet("api/CheckIn/cancelCheckIn?CustomerID="
				+ CustomerID + "&MerchantID=" + MerchantID, EnumData.request_mode.Query);

	}

	public Boolean getCuisine() throws InterruptedException, ExecutionException {

		return this.webApiHttpsGet("api/Categories/getWebCuisineDesc",
				EnumData.request_mode.Query);
	}

	public Boolean getRestaurantType() throws InterruptedException,
			ExecutionException {

		return this.webApiHttpsGet("api/Categories/getWebMealTypeDesc",
				EnumData.request_mode.Query);
	}
	
	public Boolean getSearchCategories() throws InterruptedException, ExecutionException{
		return this.webApiHttpsGet("api/Categories/getSearchCategories", EnumData.request_mode.Query);
	}

	public Boolean getGoogleMapRoute(String loc1lat, String loc1long,
			String loc2lat, String loc2long) throws InterruptedException,
			ExecutionException {
		return this.webApiHttpGet(
				"maps.googleapis.com/maps/api/directions/xml?origin=+"
						+ loc1lat + "," + loc1long + "&destination=" + loc2lat
						+ "," + loc2long
						+ "&sensor=false&units=metric&mode=driving",
				EnumData.request_mode.Query);
	}

	public Boolean getDeviceToken(String databaseID, String regid,
			String Application) throws InterruptedException, ExecutionException {

		return this.webApiHttpsGet("api/Token/get" + '/' + databaseID
				+ "?Token=" + regid + "&OS=Android" + "&App=" + Application,
				EnumData.request_mode.Query);
	}

	public Boolean getCustomerInfoPointCard(String PointCard)
			throws InterruptedException, ExecutionException {
		return webApiHttpGet(httpServerAddress + '/'
				+ "api/Customer?PointCardCode=" + PointCard, EnumData.request_mode.Query);
	}

	public Boolean getVoucherRedeemedGroupedPointCard(String customerPointCard,
			int MerchantID, int Grouped) throws InterruptedException,
			ExecutionException {
		return this.webApiHttpsGet("api/Voucher/Redeemed?customerPointCard="
				+ customerPointCard + "&MerchantID=" + MerchantID + "&Grouped="
				+ Grouped, EnumData.request_mode.Query);

	}

	public Boolean getSpecificCustomerVoidTransaction(String MerchantID,
			String CustomerID) throws InterruptedException, ExecutionException {
		return this.webApiHttpGet(httpServerAddress + '/'
				+ "api/Transaction/Voidable?MerchantID=" + MerchantID
				+ "&CustomerID=" + CustomerID, EnumData.request_mode.Query);

	}

	public Boolean GetCardCodeFromCustomerID(String CustomerID)
			throws InterruptedException, ExecutionException {
		return this.webApiHttpGet(httpServerAddress + '/'
				+ "api/Customer/GetCardCodeFromCustomerID?CustomerID="
				+ CustomerID, EnumData.request_mode.Query);
	}

	public Boolean postApiToken(String token, String dataBaseID, String Application)
			throws InterruptedException, ExecutionException {
		Toast toast = null;
		String base = "api/Token/post";
		String toAppend = dataBaseID + "?" + "Token" + "=" + token + "&" + "OS"
				+ "=" + "Android" + "&" + "App" + "=" + Application;
		return this.webApiHttpsPost(base + '/' + toAppend, null,
				EnumData.request_mode.Query);
	/*BE PROACTIVE	String text = "";
		int duration = Toast.LENGTH_SHORT;
		if (result != null && result.equals("Created")) {
			text = "UserTokenAdded to database";
		} else {
			text = "UserToken cannot be added to database";
		}
		toast = Toast.makeText(this.activity, text, duration);
		toast.show();*/
	}

	public Boolean postApiVouchersTransaction(String json)
			throws InterruptedException, ExecutionException {
		return this.HttpPostTransactionCheckIn(
				"api/Voucher/PostVoucherWithTransaction", json,
				EnumData.request_mode.Query);

	
		//
	}

	public Boolean postApiTransactions(String json) throws InterruptedException,
			ExecutionException {

		return this.HttpPostTransactionCheckIn(
				"api/Transaction/PostTransaction", json, EnumData.request_mode.Query);



	}

	public Boolean postVoidTransaction(String OrderID)
			throws InterruptedException, ExecutionException {

		return this.webApiHttpsPost("api/Transaction/VoidTransaction?OrderID="
				+ OrderID, null, EnumData.request_mode.Query);

	}

	public Boolean postSearchRestaurant(String json)
			throws InterruptedException, ExecutionException {
		return this.webApiHttpPostSearch("api/Search/restaurant",
				json, EnumData.request_mode.Query);


	}

	public Boolean postSearchRetail(String json) throws InterruptedException,
			ExecutionException {
		return  this.webApiHttpPostSearch("api/Search/retail", json,
				EnumData.request_mode.Query);
	}

	public Boolean postApiReview(String json) throws InterruptedException,
			ExecutionException {
		Toast toast = null;
		return this.webApiHttpPost("api/Review/PostReview", json,
				EnumData.request_mode.Query);
	/* BE PROACTIVE	String text = "";

		int duration = Toast.LENGTH_SHORT;
		if (result == null) {
			text = "Server Error. Please try again later";
		} else if (result.equals("OK")) {
			text = "Review Sent For Approval";
		} else {
			text = "Review Can Not Sent";
		}
		toast = Toast.makeText(this.activity, text, duration);
		toast.show();
		return result;*/

	}

	// USED to post a voucher selected by customer
	public Boolean postRedeemVoucher(String CustomerID, String VoucherCode)
			throws InterruptedException, ExecutionException {
		String json = null;
		return this.webApiHttpPost("api/Voucher/ActiveToRedeemed?CustomerID="
				+ CustomerID + "&VoucherCode=" + VoucherCode, json,
				EnumData.request_mode.Query);

	}

	// USED TO get all vouchers from a merchant that a customer can redeem
	public Boolean getAllRedeemableVouchersByMerchant(String MerchantID)
			throws InterruptedException, ExecutionException {
		return  this.webApiHttpsGet(
				"api/Voucher/ActiveMerchant?MerchantID=" + MerchantID
						+ "&Grouped=1", EnumData.request_mode.Query);



	}
	
	public Boolean getWebHeardSiteWays()
			throws InterruptedException, ExecutionException {
		return  this.webApiHttpsGet(
				"api/Register/getWebHeardSiteWays" , EnumData.request_mode.Query);
	}
	
	public Boolean postApiPasswordReset(String json)
			throws InterruptedException, ExecutionException {
		Toast toast = null;
		return this.webApiHttpPost("api/ResetPassword/Post", json,
				EnumData.request_mode.Query);
		/*BE PROACTIVEString text = "";

		int duration = Toast.LENGTH_SHORT;
		if (result.equals("OK")) {
			text = "Email Sent.";
		} else {
			text = "Email Not Sent.";
		}
		toast = Toast.makeText(this.activity, text, duration);
		toast.show();
		return result;*/
	}

	public Boolean webApiHttpGet(String url, EnumData.request_mode rm)
			throws InterruptedException, ExecutionException {
		String urlToSend = "https://" + url;
		ConnectivityManager connMgr = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			HttpRequestTask task = null;
			if (json) {
				task = new HttpRequestTask(activity, rm);
			} else {
				task = new HttpRequestTask(activity, true);
			}
			this.startMyTask(task, urlToSend);
		} else {
		}
		
		return true;
	}

	public Boolean webApiHttpsGet(String url, EnumData.request_mode rm)
			throws InterruptedException, ExecutionException {
		String[] urlToSend = { "https://" + httpsServerAddress + "/" + url };

		ConnectivityManager connMgr = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			HttpRequestTask task = null;
			if (json) {
				task = new HttpRequestTask(activity, rm);
			} else {
				task = new HttpRequestTask(activity, true);
			}
			this.startMyTask(task, urlToSend);
		} else {
		}
		
		return true;
	}

	public Boolean webApiHttpPost(String url, String jsonString, EnumData.request_mode rm)
			throws InterruptedException, ExecutionException {
		String urlToSend = "https://" + httpServerAddress + "/" + url;
		String[] input = { urlToSend, jsonString };
		ConnectivityManager connMgr = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			HttpPostTask task = null;
			if (json) {
				task = new HttpPostTask(activity, rm);
			} else {
				task = new HttpPostTask(activity, true);
			}
			// activity.showProgressDialog();
			this.startMyTask(task, urlToSend);
		} else {
		}
return true;
	}

	// Used for Edit
	public Boolean webApiHttpPostEdit(String url, String jsonString,
			EnumData.request_mode rm) throws InterruptedException, ExecutionException {
		String urlToSend = "https://" + httpServerAddress + "/" + url;
		String[] input = { urlToSend, jsonString };
		ConnectivityManager connMgr = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			HttpPostTaskEdit task = null;
			if (json) {
				task = new HttpPostTaskEdit(activity, rm);
			} else {
				task = new HttpPostTaskEdit(activity, true);
			}
			this.startMyTask(task, urlToSend);
		} else {
		}
return true;
	}

	// Used for Advanced Search
	public Boolean  webApiHttpPostSearch(String url, String jsonString,
			EnumData.request_mode rm) throws InterruptedException, ExecutionException {
		String urlToSend = "https://" + httpServerAddress + "/" + url;
		String[] input = { urlToSend, jsonString };
		ConnectivityManager connMgr = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			HttpPostTaskSearch task = null;
			if (json) {
				task = new HttpPostTaskSearch(activity, rm);
			} else {
				task = new HttpPostTaskSearch(activity, true);
			}
			this.startMyTask(task, urlToSend);
		} else {
		}
return true;
	}

	// USED FOR deleting favorites
	public Boolean webApiHttpPostDelete(String url, String jsonString,
			EnumData.request_mode rm) throws InterruptedException, ExecutionException {
		String urlToSend = "https://" + httpServerAddress + "/" + url;
		String[] input = { urlToSend, jsonString };
		ConnectivityManager connMgr = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			HttpPostTaskDelete task = null;
			if (json) {
				task = new HttpPostTaskDelete(activity, rm);
			} else {
				task = new HttpPostTaskDelete(activity, true);
			}
			this.startMyTask(task, urlToSend);
		} else {
		}
return true;
	}

	// need this because we have a method "check favorites " that is a post
	// method that requires to retrieve some value
	public Boolean webApiHttpPostFavorite(String url, String jsonString,
			EnumData.request_mode rm) throws InterruptedException, ExecutionException {
		String urlToSend = "https://" + httpServerAddress + "/" + url;
		String[] input = { urlToSend, jsonString };
		ConnectivityManager connMgr = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			HttpPostTaskFavorite task = null;
			if (json) {
				task = new HttpPostTaskFavorite(activity, rm);
			} else {
				task = new HttpPostTaskFavorite(activity, true);
			}
			this.startMyTask(task, urlToSend);
		} else {
		}
		return true;
	}

	public Boolean webApiHttpsPost(String url, String jsonString, EnumData.request_mode rm)
			throws InterruptedException, ExecutionException {
		String urlToSend = "https://" + httpsServerAddress + "/" + url;
		String[] input = { urlToSend, jsonString };
		ConnectivityManager connMgr = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			HttpPostTask task = null;
			if (json) {
				task = new HttpPostTask(activity, rm);
			} else {
				task = new HttpPostTask(activity, true);
			}
			// activity.showProgressDialog();
			this.startMyTask(task, input);
		} else {
		}
		
		return true;

	}

	public InputStream webApiHttpsGetImage(String url, EnumData.request_mode rm)
			throws InterruptedException, ExecutionException {
		String[] urlToSend = { "https://" + httpsServerAddress + "/" + url };

		ConnectivityManager connMgr = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			HttpRequestImage task = null;
			if (json) {
				task = new HttpRequestImage(activity, rm);
			} else {
				task = new HttpRequestImage(activity, true);
			}
			// activity.showProgressDialog();
			return task.execute(urlToSend).get();
		} else {
			return null;
		}
	}

	public Boolean webApiHttpsGet2(String url, EnumData.request_mode rm)
			throws InterruptedException, ExecutionException {
		String[] urlToSend = { url };
		ConnectivityManager connMgr = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			HttpRequestTask task = null;
			if (json) {
				task = new HttpRequestTask(activity, rm);
			} else {
				task = new HttpRequestTask(activity, true);
			}
			// activity.showProgressDialog();
			this.startMyTask(task, urlToSend);
		} else {
		}
		
		return true;
	}

	public Boolean HttpPostTransactionCheckIn(String url, String jsonString,
			EnumData.request_mode rm) throws InterruptedException, ExecutionException {
		String urlToSend = "https://" + "online.profitek.com/MobileAPI_Share"
				+ "/" + url;
		String[] input = { urlToSend, jsonString };
		ConnectivityManager connMgr = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			HttpPostTransactionCheckIn task = null;
			if (json) {
				task = new HttpPostTransactionCheckIn(activity, rm);
			} else {
				task = new HttpPostTransactionCheckIn(activity, true);
			}
			// activity.showProgressDialog();
			this.startMyTask(task, input);
		} else {
		}
		return true;
	}

	private void startMyTask(AsyncTask asyncTask, String param) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, param);
		else
			asyncTask.execute(param);
                              
	}
	
	private void startMyTask(AsyncTask asyncTask, String[] param) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, param);
		else
			asyncTask.execute(param);

	}

	public void setJson(boolean value) {
		this.json = value;
	}
	
	private Boolean CorrectString(String[] collection, String result)
	{
		boolean resultBool =true;
		for( int i=0 ; i<collection.length ; i++)
		{
			if(result.equalsIgnoreCase(collection[i]))
				resultBool = false;
			if(result.equalsIgnoreCase("\"No more merchants to load\""))
				dataStorageManager.setMaxedMerchants(true);
		}
		return resultBool;
	}



	public void onTaskComplete(EnumData.ListType listType, String result) {
		Deserialize deserializer = new Deserialize();
		switch (listType) {

		case VoucherList:
			dataStorageManager.RedeemableVoucherList = deserializer.getListOfVoucherList(result);
			break;
		case Customer:
			if(result.equals(true) || result.equals("\"Cannot find user in Database\""))
				dataStorageManager.currentUser = new Customer();
			else
				dataStorageManager.currentUser = deserializer.getCustomer(result);
			break;
		case MerchantInfo:
			dataStorageManager.selectedMerchant= deserializer.getMerchant(result);
			break;
		case SelectedMerchantList:
			dataStorageManager.SelectedMerchantList= deserializer.getMerchantList(result);
			break;
		case SearchBarMerchantList:
			if(result!=null)
					dataStorageManager.SearchBarMerchantList = deserializer.getMerchantList(result);
			break;
		case CustomerReview:
			dataStorageManager.SelectedCustomerReviews = deserializer.getReviewList(result);
			break;
		case DeviceToken:
			dataStorageManager.deviceToken = deserializer.getToken(result);
			break;
		case VersionName:
			dataStorageManager.serverVersionNumber = result;
			break;
		case ReviewReplies:
			dataStorageManager.merchantReply = deserializer.getMerchantReplyList(result);
			break;
		case Favorites:
			dataStorageManager.Favorites = deserializer.getFavouriteList(result);
			break;	
		case Transaction:
			dataStorageManager.customerTransaction = deserializer.getCustomerTransactionList(result);
			break;
	/*	case RedeemableVouchersByMerchant:
			dataStorageManager.RedeemableVouchersSameMerchant = deserializer.getVoucherGroupList(result);
			break;*/
		case ApparelList:
			dataStorageManager.ApparelList = deserializer .getStringList(result);
			break;
		case FoodList:
			dataStorageManager.FoodList = deserializer.getStringList(result);
			break;
		case ElectronicsList:
			dataStorageManager.ElectronicsList = deserializer.getStringList(result);
			break;
		case HealthList:
			dataStorageManager.HealthList = deserializer.getStringList(result);
			break;
		case EntertainmentList:
			dataStorageManager.EntertainmentList = deserializer.getStringList(result);
			break;
		case ServicesList:
			dataStorageManager.ServicesList = deserializer.getStringList(result);
			break;
		case RestaurantType:
			dataStorageManager.RestaurantType = deserializer.getStringList(result);
			break;
		case Cuisine:
			dataStorageManager.Cuisine = deserializer.getStringList(result);
			break;
		case SearchList:
			dataStorageManager.SearchList = deserializer.getMerchantList(result);
			break;
		case RedeemedVoucher:
			dataStorageManager.RedeemedVoucher = deserializer.getVoucherList(result); 
			break;
	/*	case RedeemableVouchersSameMerchant:
			dataStorageManager.RedeemableVouchersSameMerchant = deserializer.getVoucherGroupList(result);
			break;*/
		case Login:
			if(result!=null)
			{
				String tokenItem = "";
				String customerItem = "";
				try {
					JSONObject reader = new JSONObject(result);
					JSONObject tokenObject  = reader.getJSONObject("token");
					JSONObject customerObject  = reader.getJSONObject("customer");
					tokenItem = tokenObject.toString();
					customerItem = customerObject.toString();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				dataStorageManager.loginCredentials= deserializer.getToken(tokenItem);
				dataStorageManager.currentUser = deserializer.getCustomer(customerItem);
			}
			break;
		case SelectedMerchantReviews:
			dataStorageManager.SelectedMerchantReviews = deserializer.getReviewList(result);	
			break;
		case SelectedMerchantReplies:
			if (result == null) {
				dataStorageManager.SelectedMerchantReplies = new ArrayList<Reply>();
			} else if (result.equalsIgnoreCase("\"Wrong Token\"")) {
				dataStorageManager.SelectedMerchantReplies = new ArrayList<Reply>();
			} else {
				dataStorageManager.SelectedMerchantReplies= deserializer
						.getReplyList(result);
			}
			break;
		case AppendSelectedMerchants:
			String[] ErrorStrings = {"\"There are no more merchants to load\"",
					"\"Index Array Out of Range, please use a smaller startIndex or Count\"",
					"\"No more merchants to load\""
					};
			if(result!=null && this.CorrectString(ErrorStrings ,result))
			{
				dataStorageManager.SelectedMerchantList.addAll(deserializer
						.getMerchantList(result));
			}
			break;
		case Deprecated:
			break;
		case CheckFavourite:
	        if(result.equals("true")){
	        	dataStorageManager.isMerchantFavorited = true;
	        }
	        else{
	        	dataStorageManager.isMerchantFavorited = false;
	        }
			break;
		case WebSiteHeardWays:
			dataStorageManager.WebHeardSiteWay  = deserializer.getWebHeardSiteWays(result);
			break;
		case SearchCategory:
			dataStorageManager.searchCategoryObject = deserializer.getSearchCategoryObject(result);
			break;
		default:
			break;
			

		}
		dataStorageManager.resetSerializationListType();
			}

	
	}
