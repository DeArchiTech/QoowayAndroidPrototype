package data;

public class EnumData {

	public EnumData() {

	}

	public enum AppVersion {
		Demo, Development, Release
	}

	public enum request_mode {

		LogIN, Query

	}

	public enum ListType {
		Voucher, Customer , MerchantInfo, SearchBarMerchantList, CustomerReview , DeviceToken , VersionName
		,ReviewReplies, Transaction ,RedeemableVouchersByMerchant, ApparelList ,FoodList,HomeList,
		ElectronicsList ,HealthList ,EntertainmentList,ServicesList ,RestaurantType,Cuisine ,SearchList ,RedeemedVoucher,
		RedeemableVouchersSameMerchant ,VoucherList ,SelectedMerchantList ,Favorites ,Login ,Deprecated, SelectedMerchantReviews ,SelectedMerchantReplies
	,AppendSelectedMerchants , CheckFavourite ,WebSiteHeardWays, SearchCategory
	}
	
	public enum FragmentName {
		Login, Home, Search, Category, Nearby, MyAccount, Map, MyVouchers, QooPointsHistory, MyReviews, Favourites, RedeemPoints, SignUp, Error , About , New
	
	}
	
	public enum Mode {
		Start , Refresh
		}
	
	public enum GPSStatus {
		Off, JustOn , On
		
	}
	
	public enum ActionType {
		SignUp ,Activate
		
	}
	
	// TAKE OUT FOR CHECK IN
/*	public enum FragmentName {
		Login, Home, Search, Category, Nearby, MyAccount, Map, MyVouchers, QooPointsHistory, MyReviews, Favourites, RedeemPoints, SignUp, Error
	}*/
	// TAKE OUT FOR CHECK IN
	/*
	 * public enum FragmentName { Login, Home, Search, Category, Nearby,
	 * CheckIn, MyAccount, Map, MyVouchers, QooPointsHistory, MyReviews,
	 * MyFavorites, RedeemPoints, SignUp, Error }
	 */
}

