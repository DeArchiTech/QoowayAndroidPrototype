package ui.searchList;

import java.util.ArrayList;
import java.util.List;
import framework.DataObject.Merchant;


public class SearchListModelAdapter {
	
	public static ArrayList<SearchDisplayListItem> Items;
	
	public static void LoadModel(List<Merchant> list) {
		
		int id = 0;
		Items = new ArrayList<SearchDisplayListItem>();
		for (Merchant item : list) 
		{
			
			Items.add(new SearchDisplayListItem(id, item.LogoPath , item.Name , item.Address,item.SpecialOffer, item.StoreID));
			id++;
			
 
		}

	}
	public static SearchDisplayListItem GetbyId(int id) {

		for (SearchDisplayListItem item : Items) {
			if (item.Id == id) {
				return item;
			}
		}
		return null;
	}

}
