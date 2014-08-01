package ui.newSearch;

import java.util.ArrayList;
import java.util.List;

public class CategoryListModelAdapter {
	
	public static ArrayList<CategoryDisplayListItem> Items;
	
	public static void LoadModel(List<String> list) {
		
		int id = 0;
		Items = new ArrayList<CategoryDisplayListItem>();
		for (String item : list) 
		{
			Items.add(new CategoryDisplayListItem(id, item));
			id++;
		}

	}
	

	
	
	public static CategoryDisplayListItem GetbyId(int id) {

		for (CategoryDisplayListItem item : Items) {
			if (item.Id == id) {
				return item;
			}
		}
		return null;
	}
}