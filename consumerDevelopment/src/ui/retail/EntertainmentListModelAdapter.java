package ui.retail;

import java.util.ArrayList;
import java.util.List;

public class EntertainmentListModelAdapter {
	
	public static ArrayList<RetailDisplayListItem> Items;
	
	public static void LoadModel(List<String> list) {
		
		int id = 0;
		Items = new ArrayList<RetailDisplayListItem>();
		for (String item : list) 
		{
			
			Items.add(new RetailDisplayListItem(id, item));
			id++;
 
		}

	}
	public static RetailDisplayListItem GetbyId(int id) {

		for (RetailDisplayListItem item : Items) {
			if (item.Id == id) {
				return item;
			}
		}
		return null;
	}

}
