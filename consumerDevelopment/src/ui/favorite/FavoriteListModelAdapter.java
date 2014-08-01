package ui.favorite;

import java.util.ArrayList;
import java.util.List;

import framework.DataObject.Favorite;

public class FavoriteListModelAdapter {

	public static ArrayList<FavoriteDisplayListItem> Items;
	
	public static void LoadModel(List<Favorite> list) {
		
		int id = 0;
		Items = new ArrayList<FavoriteDisplayListItem>();
		for (Favorite item : list) 
		{
			
			Items.add(new FavoriteDisplayListItem(++id, item.LogoPath , item.Name , item.Address,item.SpecialOffer, item.StoreID));
 
		}

	}
	public static FavoriteDisplayListItem GetbyId(int id) {

		for (FavoriteDisplayListItem item : Items) {
			if (item.Id == id) {
				return item;
			}
		}
		return null;
	}
}