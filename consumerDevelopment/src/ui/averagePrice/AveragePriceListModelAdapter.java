package ui.averagePrice;

import java.util.ArrayList;
import java.util.List;

public class AveragePriceListModelAdapter {
	
	public static ArrayList<AveragePriceDisplayListItem> Items;
	
	public static void LoadModel(List<String> list) {
		
		int id = 0;
		Items = new ArrayList<AveragePriceDisplayListItem>();
		for (String item : list) 
		{
			
			Items.add(new AveragePriceDisplayListItem(id, item));
			id++;
 
		}

	}
	public static AveragePriceDisplayListItem GetbyId(int id) {

		for (AveragePriceDisplayListItem item : Items) {
			if (item.Id == id) {
				return item;
			}
		}
		return null;
	}

}