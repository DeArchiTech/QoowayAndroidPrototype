package data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import framework.DataObject.CheckIn;
import framework.DataObject.Customer;
import framework.DataObject.CustomerTransaction;
import framework.DataObject.Favorite;
import framework.DataObject.Merchant;
import framework.DataObject.MerchantReview;
import framework.DataObject.Pass;
import framework.DataObject.PostObjectWrapper;
import framework.DataObject.Reply;
import framework.DataObject.Review;
import framework.DataObject.SearchCategoryObject;
import framework.DataObject.Token;
import framework.DataObject.Transaction;
import framework.DataObject.Voucher;
import framework.DataObject.VoucherGrouped;
import framework.DataObject.VoucherList;
import framework.DataObject.WebHeardSiteWay;

import java.util.ArrayList;
import java.util.List;


public class Deserialize {
	
	public Deserialize()
	{
		
	}
	private Gson gsonBasic = new Gson();
	private Gson gsonDate = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
	private Gson gsonNoNull = new GsonBuilder().serializeNulls().setDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss").registerTypeAdapter(String.class, new StringConverter()).create();
	
	public List<Merchant> getMerchantList(String result){
		if(result == null) {
			return new ArrayList<Merchant>();
		}
		List<Merchant> listMerchant = gsonDate.fromJson(result,
				new TypeToken<List<Merchant>>() {
				}.getType());
		
		return listMerchant;		
	}
	
	public Token getToken(String result){
		if(result == null) {
			return new Token();
		}
		Token token = gsonBasic.fromJson(result,
				new TypeToken<Token>() {
				}.getType());
		return token;
	}
	
	public Merchant getMerchant(String result){
		if(result == null) {
			return new Merchant();
		}
		Merchant merchant = gsonDate.fromJson(result,
				new TypeToken<Merchant>() {
				}.getType());
		return merchant;
	}
	
	public Customer getCustomer(String result){
		if(result == null) {
			return new Customer();
		}
		Customer customer = gsonBasic.fromJson(result,
				new TypeToken<Customer>() {
				}.getType());
		return customer;
	}

	public Pass getPass(String result){
		if(result == null) {
			return new Pass();
		}
		Pass pass = gsonBasic.fromJson(result,
				new TypeToken<Pass>() {
				}.getType());
		return pass;
	}
	
	public CheckIn getCheckIn(String result){
		if(result == null) {
			return new CheckIn();
		}
		CheckIn checkIn = gsonNoNull.fromJson(result,
				new TypeToken<CheckIn>() {
				}.getType());
		return checkIn;
	}
	
	public List<Voucher> getVoucherList(String result){
		if(result == null) {
			return new ArrayList<Voucher>();
		}
		List<Voucher> listVoucher = gsonDate.fromJson(result,
				new TypeToken<List<Voucher> >() {
				}.getType());
		return listVoucher;
	}
	
	//This is an aggregated voucherList for display
	public List<VoucherList> getListOfVoucherList(String result){
		if(result == null) {
			return new ArrayList<VoucherList>();
		}
		List<VoucherList> listVoucher = gsonDate.fromJson(result,
				new TypeToken<List<VoucherList> >() {
				}.getType());
		return listVoucher;
	}
	
	public List<VoucherGrouped> getVoucherGroupList(String result){
		if(result == null) {
			return new ArrayList<VoucherGrouped>();
		}
		List<VoucherGrouped> listVoucherGroup = gsonBasic.fromJson(result,
		new TypeToken<List<VoucherGrouped>>() {
		}.getType());
		return listVoucherGroup;
	}

	
	public List<Review> getReviewList(String result){
		if(result == null) {
			return new ArrayList<Review>();
		}
		List<Review> listReview = gsonDate.fromJson(result,
		new TypeToken<List<Review>>() {
		}.getType());
		return listReview;
	}
	
	public List<Reply> getReplyList(String result) {
		if(result == null) {
			return new ArrayList<Reply>();
		}
		List<Reply> listReply = gsonDate.fromJson(result,
				new TypeToken<List<Reply>>() {
				}.getType());
		return listReply;
	}
	
	public List<Favorite> getFavouriteList(String result){
		if(result == null) {
			return new ArrayList<Favorite>();
		}
		List<Favorite> listFavourite = gsonDate.fromJson(result,
				new TypeToken<List<Favorite>>() {
				}.getType());
		return listFavourite;
	}
	
	public List<CustomerTransaction> getCustomerTransactionList(String result){
		if(result == null) {
			return new ArrayList<CustomerTransaction>();
		}
		List<CustomerTransaction> listCustomerTransaction = gsonBasic.fromJson(result, new TypeToken<List<CustomerTransaction>>(){
		}.getType());
		return listCustomerTransaction;
	}
	
	public List<String> getStringList(String result){
		if(result == null) {
			return new ArrayList<String>();
		}
		List<String> listString = gsonDate.fromJson(result,
				new TypeToken<List<String>>() {
				}.getType());
		return listString;
	}
	
	 ///////////////////// ADDITION MERCHNAT---------------------------------------------------------------------------------------------------------------
	 
	 public List<MerchantReview> getMerchantReviewList(String result){
	  List<MerchantReview> merchantReviewList;
	  merchantReviewList = gsonDate.fromJson(result,
	  new TypeToken<List<MerchantReview>>() {
	  }.getType());
	  return merchantReviewList;
	 }
	 
	 public List<Reply> getMerchantReplyList(String result){
	  List<Reply> merchantReplyList;
	  merchantReplyList = gsonDate.fromJson(result,
	    new TypeToken<List<Reply>>() {
	    }.getType());
	  return merchantReplyList;
	 }
	 
	 public List<VoucherGrouped> getSelectedCustomerVoucherListGrouped(String result){
	  List<VoucherGrouped> voucherList;
	  voucherList = gsonDate.fromJson(result,
	    new TypeToken<List<VoucherGrouped>>() {
	    }.getType());
	  return voucherList;
	 }
	 
	 public PostObjectWrapper getTransactionResultWithVoucher(String result){
	  PostObjectWrapper transactionResult;
	  transactionResult = gsonDate.fromJson(result,
	    new TypeToken<PostObjectWrapper>() {
	    }.getType());
	  return transactionResult;
	 }
	 
	 public SearchCategoryObject getSearchCategoryObject (String result){
		 SearchCategoryObject searchCategoryObject;
		 searchCategoryObject = gsonBasic.fromJson(result, new TypeToken<SearchCategoryObject>(){
		 }.getType());
		 
		 return searchCategoryObject;
	 }
	 public Transaction getTransactionResultWithoutVoucher(String result){
	  Transaction transactionResult;
	  transactionResult = gsonDate.fromJson(result,
	    new TypeToken<Transaction>() {
	    }.getType());
	  return transactionResult;
	 }
	 
	 public Customer getCustomerDate(String result){
	  Customer customer;
	  customer = gsonDate.fromJson(result,
	    new TypeToken<Customer>() {
	    }.getType());
	  return customer;
	 }
	 
	 public List<Transaction> getTransactionList(String result){
	  List<Transaction> transactionList;
	  transactionList = gsonDate.fromJson(result,
	    new TypeToken<List<Transaction>>() {
	    }.getType());
	  return transactionList;
	 }	
	 
	 public List<WebHeardSiteWay> getWebHeardSiteWays(String result){
		  List<WebHeardSiteWay> WebSiteHeardWaysList;
		  WebSiteHeardWaysList = gsonDate.fromJson(result,
		    new TypeToken<List<WebHeardSiteWay>>() {
		    }.getType());
		  return WebSiteHeardWaysList;
		 }	
	
}
