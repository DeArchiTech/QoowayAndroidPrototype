package ui.cuisine;

import java.util.ArrayList;
import java.util.List;

public class CuisineListModelAdapter {
	
	public static ArrayList<CuisineDisplayListItem> Items;
	
	public static void LoadModel(List<String> list) {
		
		int id = 0;
		Items = new ArrayList<CuisineDisplayListItem>();
		for (String item : list) 
		{
			
			Items.add(new CuisineDisplayListItem(id, item));
			id++;
 
		}

	}
	public static CuisineDisplayListItem GetbyId(int id) {

		for (CuisineDisplayListItem item : Items) {
			if (item.Id == id) {
				return item;
			}
		}
		return null;
	}

}
