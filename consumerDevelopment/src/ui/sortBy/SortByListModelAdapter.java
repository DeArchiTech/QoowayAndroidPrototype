package ui.sortBy;

import java.util.ArrayList;
import java.util.List;

public class SortByListModelAdapter {
	
	public static ArrayList<SortByDisplayListItem> Items;
	
	public static void LoadModel(List<String> list) {
		
		int id = 0;
		Items = new ArrayList<SortByDisplayListItem>();
		for (String item : list) 
		{
			
			Items.add(new SortByDisplayListItem(id, item));
			id++;
 
		}

	}
	public static SortByDisplayListItem GetbyId(int id) {

		for (SortByDisplayListItem item : Items) {
			if (item.Id == id) {
				return item;
			}
		}
		return null;
	}

}
