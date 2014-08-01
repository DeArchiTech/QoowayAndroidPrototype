package data;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import framework.PageFragment;
import framework.QoowayActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

public class WebApiManager {

	private QoowayActivity activity;
	private String httpServerAddress;
	private String httpsServerAddress;
	public Boolean json = true;
	private static WebApiManager singletonInstance;
	DataStorageManager dataStorageManager = DataStorageManager
			.getSingletonInstance();
	Toast toast;

	public static WebApiManager getSingletonInstance(QoowayActivity msa) {
		if (null == singletonInstance) {
			singletonInstance = new WebApiManager();
			singletonInstance.activity = msa;
			singletonInstance.httpServerAddress = DataStorageManager
					.getSingletonInstance().getSingletonInstance().getApiUrl();
			singletonInstance.httpsServerAddress = DataStorageManager
					.getSingletonInstance().getSingletonInstance().getApiUrl();
		}
		return singletonInstance;
	}

	public static WebApiManager getSingletonInstance() {
		if (null == singletonInstance) {

			singletonInstance = new WebApiManager();
			singletonInstance.httpServerAddress = DataStorageManager
					.getSingletonInstance().getSingletonInstance().getApiUrl();
			singletonInstance.httpsServerAddress = DataStorageManager
					.getSingletonInstance().getSingletonInstance().getApiUrl();
		}
		return singletonInstance;
	}

	public static void setQoowayActivity(QoowayActivity msa)
	{
		WebApiManager.getSingletonInstance().activity=msa;
	}
	public String getCustomerLogin(String username, String password)
			throws InterruptedException, ExecutionException,
			UnsupportedEncodingException {

		dataStorageManager.userName = username;
		dataStorageManager.password = password;

		String base = "api/login/customer?username=";
		String toAppend = username + "&" + "password" + "=" + password;
		return this.webApiHttpsGet(base + toAppend, EnumData.request_mode.LogIN);
	}

	public String getCustomerTokenLogin(String username, String password,
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

	public String getCustomerInfoByToken(String loginToken)
			throws InterruptedException, ExecutionException,
			UnsupportedEncodingException {
		String base = "api/Login/GetCustomerWithLoginToken?LogInToken=";
		String toAppend = loginToken;
		return this.webApiHttpsGet(base + toAppend, EnumData.request_mode.Query);
	}

	public String getMerchantInfoByToken(String token)
			throws InterruptedException, ExecutionException,
			UnsupportedEncodingException {
		String base = "api/Login/GetMerchantWithLoginToken?LogInToken=";
		String toAppend = token;
		return this.webApiHttpsGet(base + toAppend, EnumData.request_mode.LogIN);
	}

	public String getVersionName() throws InterruptedException,
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
	public String getVersionNumber() throws InterruptedException,
			ExecutionException {
		return this.webApiHttpsGet(
				"api/AppData/AppVersion?OS=Android&App=Merchant",
				EnumData.request_mode.Query);
	}

	public String getCustomerSignOut(String loginToken)
			throws InterruptedException, ExecutionException,
			UnsupportedEncodingException {
		String base = "api/Login/LogoutCustomer?LoginToken=";
		String toAppend = loginToken;
		String result = this
				.webApiHttpsGet(base + toAppend, EnumData.request_mode.Query);

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

		return result;
	}

	public String getMerchantTokenLogin(String username, String password,
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

	public String getMerchantLoginNew(String username, String password,
			String token) throws InterruptedException, ExecutionException,
			UnsupportedEncodingException {
		String base = "https://online.profitek.com/appdevelopment/api/Login/LoginMerchant?username=";
		String toAppend = username + "&password=" + password + "&Token="
				+ token + "&OS=" + "Android" + "&App=" + "Merchant";
		return this.webApiHttpsGet2(base + toAppend, EnumData.request_mode.LogIN);
	}

	public String getMerchantSignOut(String loginToken)
			throws InterruptedException, ExecutionException,
			UnsupportedEncodingException {
		String base = "api/Login/LogoutMerchant?LoginToken=";
		String toAppend = loginToken;
		String result = this
				.webApiHttpsGet(base + toAppend, EnumData.request_mode.Query);

		String text = "";

		int duration = Toast.LENGTH_SHORT;
		if (result.equals("true")) {
			text = "You have logged out";
		} else {
			text = "Logout failed";
		}
		toast = Toast.makeText(this.activity, text, duration);
		toast.show();

		return result;
	}

	public String getMerchantLogin(String username, String password)
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

	public String getMerchantTransactions(String ID)
			throws InterruptedException, ExecutionException {
		return this.webApiHttpGet(httpServerAddress + '/'
				+ "api/transaction/Merchant?MerchantID=" + ID,
				EnumData.request_mode.Query);

	}

	public String getMerchantCheckIns(String MerchantID)
			throws InterruptedException, ExecutionException {
		return this.webApiHttpsGet("api/CheckIn/recentCheckIn?MerchantID="
				+ MerchantID, EnumData.request_mode.Query);
	}

	public String getReplies(int MerchantID) throws InterruptedException,
			ExecutionException {
		return this.webApiHttpsGet("api/Review/getWebReviewReply?MerchantID="
				+ MerchantID, EnumData.request_mode.Query);
	}

	public String getReply(int ReviewID) throws InterruptedException,
			ExecutionException {
		return this.webApiHttpsGet("api/Review?ReviewID=" + ReviewID,
				EnumData.request_mode.Query);
	}

	public String getCustomerLogout(String ID, String token)
			throws InterruptedException, ExecutionException,
			UnsupportedEncodingException {
		String result = this.webApiHttpsGet("api/Token/delete/" + ID
				+ "?Token=" + token + "&OS=Android&App=Customer",
				EnumData.request_mode.Query);

		return result;
	}

	public String getCustomerTransactions(String ID)
			throws InterruptedException, ExecutionException {
		return this.webApiHttpGet(httpServerAddress + '/'
				+ "api/transaction/Customer?CustomerID=" + ID,
				EnumData.request_mode.Query);

	}

	public String getMerchantInfo(String ID) // Ryan Addition , to get Merchant
												// Name with Merchant ID
			throws InterruptedException, ExecutionException {
		return this.webApiHttpGet(httpServerAddress + '/'
				+ "api/Merchant/GetMerchant/" + ID, EnumData.request_mode.Query);
	}

	public String getCustomer(String customerID) throws InterruptedException,
			ExecutionException, UnsupportedEncodingException {

		String base = "api/Customer?customerID=";
		String toAppend = customerID;
		return this.webApiHttpsGet(base + toAppend, EnumData.request_mode.LogIN);
	}

	public String getCustomerInfo(String ID) throws InterruptedException,
			ExecutionException {
		return this.webApiHttpGet(httpServerAddress + '/'
				+ "api/Customer/GetCustomer?customerID=" + ID,
				EnumData.request_mode.LogIN);
	}

	public String getApiMerchant() throws InterruptedException,
			ExecutionException {
		return this.webApiHttpGet(httpServerAddress + '/' + "api/merchant",
				EnumData.request_mode.Query);
	}

	public String getApiMerchant(int start, int count)
			throws InterruptedException, ExecutionException {

		String base = "api/Merchant/GetMerchant?startIndex=";
		String toAppend = start + "&count=" + count;
		return this.webApiHttpGet(httpServerAddress + '/' + base + toAppend,
				EnumData.request_mode.Query);
	}

	public String getApiMerchantSort(int start, int count, double latitude,
			double longitude) throws InterruptedException, ExecutionException {
		String base = "api/Merchant/GetMerchantSortLocation?startIndex=";
		String toAppend = start + "&count=" + count + "&latitude=" + latitude
				+ "&longitude=" + longitude;
		return this.webApiHttpGet(httpServerAddress + '/' + base + toAppend,
				EnumData.request_mode.Query);
	}

	public String getApiWebCuisine() throws InterruptedException,
			ExecutionException {
		return this.webApiHttpsGet("api/WebCuisine", EnumData.request_mode.Query);
	}

	public String getApiSubcategory() throws InterruptedException,
			ExecutionException {
		return this.webApiHttpsGet("api/Subcategory", EnumData.request_mode.Query);
	}

	public String getApiMerchant(String Category) throws InterruptedException,
			ExecutionException, UnsupportedEncodingException {
		String toAppend = URLEncoder.encode(Category, "UTF-8");
		return this.webApiHttpsGet("api/merchant?info=" + toAppend,
				EnumData.request_mode.Query);
	}

	public String getVoidTransactions(String ID) throws InterruptedException,
			ExecutionException {
		return this.webApiHttpGet(httpServerAddress + '/'
				+ "api/Transaction/Voidable?MerchantID=" + ID,
				EnumData.request_mode.Query);

	}

	public String getCheckInConfirm(String CustomerID, int MerchantID,
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

	public String getCheckInDecline(String CustomerID, int MerchantID,
			int Confirm, String CheckInID) throws InterruptedException,
			ExecutionException {
		return this.webApiHttpsGet(
				"api/CheckIn/checkInConfirmation?CustomerID=" + CustomerID
						+ "&MerchantID=" + MerchantID + "&confirm=" + Confirm
						+ "&CheckInID=" + CheckInID + "&OrderID=" + "1",
				EnumData.request_mode.Query);
	}

	public String getPointCalculation(String pointCardCode, String MerchantID,
			String soldAmount) throws InterruptedException, ExecutionException,
			UnsupportedEncodingException {

		return this.webApiHttpGet("online.profitek.com/appdevelopment" + '/'
				+ "api/Transaction/Calcualtion?pointCardCode=" + pointCardCode
				+ "&MerchantID=" + MerchantID + "&soldAmount=" + soldAmount,
				EnumData.request_mode.Query);

	}

	public String postApiRegister(String json) throws InterruptedException,
			ExecutionException {
		return this.webApiHttpPost("api/Register", json,
				EnumData.request_mode.Query);
	}

	public String getVoucher(int MerchantID) throws InterruptedException,
			ExecutionException {
		return this.webApiHttpGet(httpServerAddress + '/'
				+ "api/voucher/Merchant?MerchantID=", EnumData.request_mode.Query);
	}

	public String getVoucherAll() throws InterruptedException,
			ExecutionException {
		return this.webApiHttpGet(httpServerAddress + '/'
				+ "api/Voucher/GetVoucher", EnumData.request_mode.Query);
	}

	public String getVoucherList() throws InterruptedException,
			ExecutionException {
		return this.webApiHttpGet(httpServerAddress + '/'
				+ "api/Voucher/GetVoucherList", EnumData.request_mode.Query);
	}

	public String getVoucherRedeemed(String CustomerID)
			throws InterruptedException, ExecutionException {
		return this.webApiHttpsGet("api/Voucher/Redeemed?CustomerID="
				+ CustomerID + "&Grouped=0", EnumData.request_mode.Query);
	}

	public String getVoucherRedeemed(String customerPointCard, int MerchantID)
			throws InterruptedException, ExecutionException {
		return this.webApiHttpsGet("api/voucher/redeemed?customerPointCard="
				+ customerPointCard + "&MerchantID=" + MerchantID,
				EnumData.request_mode.Query);
	}

	public String getVoucherRedeemedWithID(String customerID, int MerchantID , int Grouped) 
			throws InterruptedException, ExecutionException {
		return this.webApiHttpsGet("api/Voucher?customerID=" + customerID
				+ "&MerchantID=" + MerchantID +"&Grouped=" + Grouped, EnumData.request_mode.Query);
	}

	public String getVoucherUsed(String CustomerID)
			throws InterruptedException, ExecutionException {
		return this.webApiHttpsGet("api/voucher/used?CustomerID=" + CustomerID,
				EnumData.request_mode.Query);
	}

	public String getReview(String CustomerID) throws InterruptedException,
			ExecutionException {
		return this.webApiHttpsGet(
				"api/Review/getWebReviewByCustomerID?CustomerID=" + CustomerID,
				EnumData.request_mode.Query);
	}

	public String getReview(int MerchantID) throws InterruptedException,
			ExecutionException {
		return this.webApiHttpsGet(
				"api/Review/getWebReviewByMerchantID?MerchantID=" + MerchantID,
				EnumData.request_mode.Query);
	}

	public String getBarcode(String CustomerID) throws InterruptedException,
			ExecutionException {
		return this.webApiHttpsGet("api/Picture/GetCustomerBarCodeLogo/"
				+ CustomerID, EnumData.request_mode.Query);

	}

	public String getRetailCategories(String CategoryDesc)
			throws InterruptedException, ExecutionException {
		return this.webApiHttpsGet(
				"api/Categories/getSubcategoryDesc?CategoryDesc="
						+ CategoryDesc, EnumData.request_mode.Query);

	}

	public String getSearchBar(String search) throws InterruptedException,
			ExecutionException {
		return this.webApiHttpsGet("api/Search?query=" + search,
				EnumData.request_mode.Query);

	}

	public String postApiReviewReply(String json) throws InterruptedException,
			ExecutionException {
		Toast toast = null;
		String result = this.webApiHttpPost("api/Review/PostReviewReply", json,
				EnumData.request_mode.Query);
		String text = "";
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
		return result;
	}

	// Ryan addition
	public String postDeleteFavorite(String CustomerID, String StoreID)
			throws InterruptedException, ExecutionException {

		return webApiHttpPostDelete("api/Favourite/DeleteFavorite?StoreID="
				+ StoreID + "&CustomerID=" + CustomerID, null,
				EnumData.request_mode.Query);

	}

	public void postDeleteReply(String ReplyID) throws InterruptedException,
			ExecutionException {

		this.webApiHttpPostDelete(
				"api/Review/DeleteWebReviewReply?WebReviewReplyID=" + ReplyID,
				null, EnumData.request_mode.Query);

	}

	// Ryan addition
	public String getCustomerFavorites(String CustomerID)
			throws InterruptedException, ExecutionException {

		return this.webApiHttpsGet(
				"api/Merchant/GetMerchantsFavourited?customerID=" + CustomerID,
				EnumData.request_mode.Query);

	}

	public String checkFavorite(String CustomerID, String MerchantID)
			throws InterruptedException, ExecutionException {

		String result = this
				.webApiHttpPostFavorite(
						"api/Favourite/checkCustomerFavoruite?StoreID="
								+ MerchantID + "&CustomerID=" + CustomerID,
						null, EnumData.request_mode.Query);
		return result;

	}

	public String postReplyEdit(int replyID, String json)
			throws InterruptedException, ExecutionException {
		Toast toast = null;
		String result = this.webApiHttpPostEdit(
				"api/Review?ReplyID=" + replyID, json, EnumData.request_mode.Query);
		String text = "";
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
		return result;
	}

	public void postCustomerFavorite(String CustomerID, String MerchantID)
			throws InterruptedException, ExecutionException {
		this.webApiHttpsPost("api/Favourite/PostFavorite?StoreID=" + MerchantID
				+ "&CustomerID=" + CustomerID, null, EnumData.request_mode.Query);
	}

	public String getCheckInRequest(String CustomerID, int MerchantID)
			throws InterruptedException, ExecutionException {
		String temp = this.httpsServerAddress;
		// this.httpsServerAddress = "online.profitek.com/MobileAPI_Share";
		String result = this.webApiHttpsGet("api/Check"
				+ "In/checkInRequest?CustomerID=" + CustomerID + "&MerchantID="
				+ MerchantID, EnumData.request_mode.Query);
		/*
		 * return this.webApiHttpsGet("api/Check" +
		 * "In/checkInRequest?CustomerID=" + CustomerID + "&MerchantID=" +
		 * MerchantID, EnumData.request_mode.Query);
		 */
		this.httpsServerAddress = temp;
		return result;
	}

	public String getCheckInCancel(String CustomerID, int MerchantID)
			throws InterruptedException, ExecutionException {
		String temp = this.httpsServerAddress;
		// this.httpsServerAddress = "online.profitek.com/MobileAPI_Share";
		String result = this.webApiHttpsGet(
				"api/CheckIn/cancelCheckIn?CustomerID=" + CustomerID
						+ "&MerchantID=" + MerchantID, EnumData.request_mode.Query);
		this.httpsServerAddress = temp;
		return result;
	}

	public String getCheckInRequestVoucher(String CustomerID, int MerchantID,
			String Voucher) throws InterruptedException, ExecutionException {
		String temp = this.httpsServerAddress;
		// this.httpsServerAddress = "online.profitek.com/MobileAPI_Share";
		String result = this.webApiHttpsGet("api/CheckIn?" + "CustomerID="
				+ CustomerID + "&MerchantID=" + MerchantID + "&Voucher="
				+ Voucher, EnumData.request_mode.Query);
		this.httpsServerAddress = temp;
		return result;
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
	public String getCheckStatus(String CustomerID, int MerchantID)
			throws InterruptedException, ExecutionException {

		return this.webApiHttpsGet("api/CheckIn/checkStatus?CustomerID="
				+ CustomerID + "&MerchantID=" + MerchantID, EnumData.request_mode.Query);

	}

	public String getCancelCheckIn(String CustomerID, int MerchantID)
			throws InterruptedException, ExecutionException {

		return this.webApiHttpsGet("api/CheckIn/cancelCheckIn?CustomerID="
				+ CustomerID + "&MerchantID=" + MerchantID, EnumData.request_mode.Query);

	}

	public String getCuisine() throws InterruptedException, ExecutionException {

		return this.webApiHttpsGet("api/Categories/getWebCuisineDesc",
				EnumData.request_mode.Query);
	}
	
	public String getNeighbourhood() throws InterruptedException, ExecutionException{
		return this.webApiHttpsGet("api/Categories/getWebNeighbourhood", EnumData.request_mode.Query);
	}

	public String getRestaurantType() throws InterruptedException,
			ExecutionException {

		return this.webApiHttpsGet("api/Categories/getWebMealTypeDesc",
				EnumData.request_mode.Query);
	}

	public String getGoogleMapRoute(String loc1lat, String loc1long,
			String loc2lat, String loc2long) throws InterruptedException,
			ExecutionException {
		return this.webApiHttpGet(
				"maps.googleapis.com/maps/api/directions/xml?origin=+"
						+ loc1lat + "," + loc1long + "&destination=" + loc2lat
						+ "," + loc2long
						+ "&sensor=false&units=metric&mode=driving",
				EnumData.request_mode.Query);
	}

	public String getDeviceToken(String databaseID, String regid,
			String Application) throws InterruptedException, ExecutionException {

		return this.webApiHttpsGet("api/Token/get" + '/' + databaseID
				+ "?Token=" + regid + "&OS=Android" + "&App=" + Application,
				EnumData.request_mode.Query);
	}

	public String getCustomerInfoPointCard(String PointCard)
			throws InterruptedException, ExecutionException {
		return webApiHttpGet(httpServerAddress + '/'
				+ "api/Customer?PointCardCode=" + PointCard, EnumData.request_mode.Query);
	}

	public String getVoucherRedeemedGroupedPointCard(String customerPointCard,
			int MerchantID, int Grouped) throws InterruptedException,
			ExecutionException {
		return this.webApiHttpsGet("api/Voucher/Redeemed?customerPointCard="
				+ customerPointCard + "&MerchantID=" + MerchantID + "&Grouped="
				+ Grouped, EnumData.request_mode.Query);

	}

	public String getSpecificCustomerVoidTransaction(String MerchantID,
			String CustomerID) throws InterruptedException, ExecutionException {
		return this.webApiHttpGet(httpServerAddress + '/'
				+ "api/Transaction/Voidable?MerchantID=" + MerchantID
				+ "&CustomerID=" + CustomerID, EnumData.request_mode.Query);

	}

	public String GetCardCodeFromCustomerID(String CustomerID)
			throws InterruptedException, ExecutionException {
		return this.webApiHttpGet(httpServerAddress + '/'
				+ "api/Customer/GetCardCodeFromCustomerID?CustomerID="
				+ CustomerID, EnumData.request_mode.Query);
	}

	public void postApiToken(String token, String dataBaseID, String Application)
			throws InterruptedException, ExecutionException {
		Toast toast = null;
		String base = "api/Token/post";
		String toAppend = dataBaseID + "?" + "Token" + "=" + token + "&" + "OS"
				+ "=" + "Android" + "&" + "App" + "=" + Application;
		String result = this.webApiHttpsPost(base + '/' + toAppend, null,
				EnumData.request_mode.Query);
		String text = "";
		int duration = Toast.LENGTH_SHORT;
		if (result != null && result.equals("Created")) {
			text = "UserTokenAdded to database";
		} else {
			text = "UserToken cannot be added to database";
		}
		toast = Toast.makeText(this.activity, text, duration);
		toast.show();
	}

	public String postApiVouchersTransaction(String json)
			throws InterruptedException, ExecutionException {
		String result = this.HttpPostTransactionCheckIn(
				"api/Voucher/PostVoucherWithTransaction", json,
				EnumData.request_mode.Query);

		return result;
		//
	}

	public String postApiTransactions(String json) throws InterruptedException,
			ExecutionException {

		String result = this.HttpPostTransactionCheckIn(
				"api/Transaction/PostTransaction", json, EnumData.request_mode.Query);

		return result;

	}

	public void postVoidTransaction(String OrderID)
			throws InterruptedException, ExecutionException {

		this.webApiHttpsPost("api/Transaction/VoidTransaction?OrderID="
				+ OrderID, null, EnumData.request_mode.Query);

	}

	public String postSearchRestaurant(String json)
			throws InterruptedException, ExecutionException {
		String result = this.webApiHttpPostSearch("api/Search/restaurant",
				json, EnumData.request_mode.Query);
		return result;

	}

	public String postSearchRetail(String json) throws InterruptedException,
			ExecutionException {
		String result = this.webApiHttpPostSearch("api/Search/retail", json,
				EnumData.request_mode.Query);
		return result;
	}

	public String postApiReview(String json) throws InterruptedException,
			ExecutionException {
		Toast toast = null;
		String result = this.webApiHttpPost("api/Review/PostReview", json,
				EnumData.request_mode.Query);
		String text = "";

		int duration = Toast.LENGTH_SHORT;
		if (result == null) {
			text = "Server Error. Please try again later";
		} else if (result.equals("OK")) {
			text = "Thank you for submitting a review!";
		} else {
			text = "Review Can Not Sent";
		}
		toast = Toast.makeText(this.activity, text, duration);
		toast.show();
		return result;

	}

	// USED to post a voucher selected by customer
	public void postRedeemVoucher(String CustomerID, String VoucherCode)
			throws InterruptedException, ExecutionException {
		String json = null;
		this.webApiHttpPost("api/Voucher/ActiveToRedeemed?CustomerID="
				+ CustomerID + "&VoucherCode=" + VoucherCode, json,
				EnumData.request_mode.Query);

	}

	// USED TO get all vouchers from a merchant that a customer can redeem
	public String getAllRedeemableVouchersByMerchant(String MerchantID)
			throws InterruptedException, ExecutionException {
		String result = this.webApiHttpsGet(
				"api/Voucher/ActiveMerchant?MerchantID=" + MerchantID
						+ "&Grouped=1", EnumData.request_mode.Query);

		return result;

	}

	public String postApiPasswordReset(String json)
			throws InterruptedException, ExecutionException {
		Toast toast = null;
		String result = this.webApiHttpPost("api/ResetPassword/Post", json,
				EnumData.request_mode.Query);
		String text = "";

		int duration = Toast.LENGTH_SHORT;
		if (result.equals("OK")) {
			text = "Email Sent.";
		} else {
			text = "Email Not Sent.";
		}
		toast = Toast.makeText(this.activity, text, duration);
		toast.show();
		return result;
	}

	public String webApiHttpGet(String url, EnumData.request_mode rm)
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
			// activity.showProgressDialog();
			return (String)task.execute(urlToSend).get();
		} else {
			return null;
		}
	}

	public String webApiHttpsGet(String url, EnumData.request_mode rm)
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
			// activity.showProgressDialog();
			return (String)task.execute(urlToSend).get();
		} else {
			return null;
		}
	}

	public String webApiHttpPost(String url, String jsonString, EnumData.request_mode rm)
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
			return task.execute(input).get();
		} else {
			return null;
		}

	}

	// Used for Edit
	public String webApiHttpPostEdit(String url, String jsonString,
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
			// activity.showProgressDialog();
			return task.execute(input).get();
		} else {
			return null;
		}

	}

	// Used for Advanced Search
	public String webApiHttpPostSearch(String url, String jsonString,
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
			// activity.showProgressDialog();
			return task.execute(input).get();
		} else {
			return null;
		}

	}

	// USED FOR deleting favorites
	public String webApiHttpPostDelete(String url, String jsonString,
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
			// activity.showProgressDialog();
			return task.execute(input).get();
		} else {
			return null;
		}

	}

	// need this because we have a method "check favorites " that is a post
	// method that requires to retrieve some value
	public String webApiHttpPostFavorite(String url, String jsonString,
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
			// activity.showProgressDialog();
			return task.execute(input).get();
		} else {
			return null;
		}

	}

	public String webApiHttpsPost(String url, String jsonString, EnumData.request_mode rm)
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
			return task.execute(input).get();
		} else {
			return null;
		}

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

	public String webApiHttpsGet2(String url, EnumData.request_mode rm)
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
			return (String)task.execute(urlToSend).get();
		} else {
			return null;
		}
	}

	public String HttpPostTransactionCheckIn(String url, String jsonString,
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
			return task.execute(input).get();
		} else {
			return null;
		}
	}

	private void startMyTask(AsyncTask asyncTask, String param) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, param);
		else
			asyncTask.execute(param);

	}

	public void setJson(boolean value) {
		this.json = value;
	}




	public enum ListType {
		Voucher, Customer
	}

}