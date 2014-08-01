package framework;

import com.devspark.progressfragment.ProgressFragment;


public class PageFragment extends ProgressFragment{
	
	public FragmentName currentName;
	
	
	public PageFragment() {

	}

	public void loadRecievedData()
	{
		
	}
	
	public FragmentName getCurrentName()
	{
		return this.currentName;
	}
	
	public void setCurrentName(FragmentName Name)
	{
		this.currentName=Name;
	}
	
	public enum FragmentName {
		Login, Home, Search, Category, Nearby,  MyAccount, Map, MyVouchers, QooPointsHistory, MyReviews, Favourites, RedeemPoints, SignUp, Error, About , New
	}
}

