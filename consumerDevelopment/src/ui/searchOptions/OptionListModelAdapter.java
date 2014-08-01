package ui.searchOptions;

import java.util.ArrayList;
import java.util.List;

public class OptionListModelAdapter {
	
	public static ArrayList<OptionDisplayListItem> Items;
	
	public static void LoadModel(List<String> list) {
		
		int id = 0;
		Items = new ArrayList<OptionDisplayListItem>();
		for (String item : list) 
		{
			
			Items.add(new OptionDisplayListItem(id, item));
			id++;
 
		}

	}
	public static OptionDisplayListItem GetbyId(int id) {

		for (OptionDisplayListItem item : Items) {
			if (item.Id == id) {
				return item;
			}
		}
		return null;
	}

}
