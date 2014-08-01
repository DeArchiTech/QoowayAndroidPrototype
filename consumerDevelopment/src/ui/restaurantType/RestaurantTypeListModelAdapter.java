package ui.restaurantType;

import java.util.ArrayList;
import java.util.List;

public class RestaurantTypeListModelAdapter {
	
	public static ArrayList<RestaurantTypeDisplayListItem> Items = new ArrayList<RestaurantTypeDisplayListItem>();
	
	public static void LoadModel(List<String> list) {
		
		int id = 0;
		Items = new ArrayList<RestaurantTypeDisplayListItem>();
		for (String item : list) 
		{
			
			Items.add(new RestaurantTypeDisplayListItem(id, item));
			id++;
 
		}

	}
	public static RestaurantTypeDisplayListItem GetbyId(int id) {

		for (RestaurantTypeDisplayListItem item : Items) {
			if (item.Id == id) {
				return item;
			}
		}
		return null;
	}

}