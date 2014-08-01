package data;

import java.util.List;

import com.google.gson.Gson;

import framework.DataObject.AdvancedRestaurantSearchOrderHead;
import framework.DataObject.AdvancedRetailSearchOrderHead;
import framework.DataObject.Password;
import framework.DataObject.PostObjectWrapper;
import framework.DataObject.PostReview;
import framework.DataObject.SignUp;
import framework.DataObject.Voucher;
import framework.DataObject.WebPointOrderHead;

public class Serialize {
	private static Gson gsonBasic = new Gson();
	private static String json = "";
	
	public static String password(String email){
		Password tran = new Password(email);
		json = gsonBasic.toJson(tran);
		return json;
	}

	public static String listVoucher(List<Voucher> listVoucher){
		json = gsonBasic.toJson(listVoucher);
		return json;
	}
	
	public static String postReview(PostReview review){
		json = gsonBasic.toJson(review);
		return json;
	}
	
	public static String advRestaurantSearch(AdvancedRestaurantSearchOrderHead tran){
		json = gsonBasic.toJson(tran);
		return json;
	}
	
	public static String advRetailSearch(AdvancedRetailSearchOrderHead tran){		
		json = gsonBasic.toJson(tran);
		return json;
	}
	
	public static String signUp(SignUp object){
		json = gsonBasic.toJson(object);
		return json;
	}
		
	///////////////////// ADDITION MERCHNAT---------------------------------------------------------------------------------------------------------------
		 
	public static String postTransactionWithoutVoucher(WebPointOrderHead tran){
	json = gsonBasic.toJson(tran);
	return json;
	}
	
	public static String postTransactionWithVoucher(PostObjectWrapper tran){
	json = gsonBasic.toJson(tran);
	return json;
	}	
	
}
