package ui.home;

import java.util.ArrayList;
import java.util.List;

import ui.merchantList.MerchantListModelAdapter;
import framework.DataObject.Merchant;


public class HomeListModelAdapter{
	
	public static ArrayList<HomeDisplayListItem> Items;
	public final static int alphaIndex = 0;
	public static int omegaIndex = 20;
	public static int startIndex = 0;
	public static int recieveAmount = 20;
	public static int id = 0;

	
		
public static ArrayList<HomeDisplayListItem> LoadModel(List<Merchant> list) {
			
		if(Items == null){
			Items = new ArrayList<HomeDisplayListItem>();
		}
		ArrayList<HomeDisplayListItem> Items = new ArrayList<HomeDisplayListItem>();
		
		for (Merchant item : list) 
		{
			
			Items.add(new HomeDisplayListItem(id, item.Name, item.Address, item.LogoPath, item.SpecialOffer, item.StoreID));
			id++;
 
		}
		
		HomeListModelAdapter.Items.addAll(Items);
		return Items;

	}
	
	public static HomeDisplayListItem GetbyId(int id) {

		for (HomeDisplayListItem item : Items) {
			if (item.Id == id) {
				return item;
			}
		}
		return null;
	}
	
	public static void ClearList() {
		HomeListModelAdapter.Items=null;
		HomeListModelAdapter.id = 0 ;
		HomeListModelAdapter.startIndex = 0;
	}
	
}


