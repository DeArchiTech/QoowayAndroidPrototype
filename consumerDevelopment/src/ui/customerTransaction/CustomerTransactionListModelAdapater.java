package ui.customerTransaction;

import java.util.ArrayList;
import java.util.List;

import framework.DataObject.CustomerTransaction;
public class CustomerTransactionListModelAdapater {
	
	public static ArrayList<CustomerTransactionDisplayListItem> Items;
	
	public static void LoadModel(List<CustomerTransaction> ct){
		int id = 0;
		Items = new ArrayList<CustomerTransactionDisplayListItem>();
		for(CustomerTransaction item : ct)
		{
			Items.add(new CustomerTransactionDisplayListItem(++id, item.MerchantName, item.OrderTime , item.SoldAmount,item.Points));
		}
	}
	
	public static CustomerTransactionDisplayListItem GetbyId(int id) {

		for (CustomerTransactionDisplayListItem item : Items) {
			if (item.Id == id) {
				return item;
			}
		}
		return null;
	}

}
