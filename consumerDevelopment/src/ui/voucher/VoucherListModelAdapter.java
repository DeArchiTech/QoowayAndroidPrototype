package ui.voucher;

import java.util.ArrayList;
import java.util.List;

import framework.DataObject.Voucher;

import ui.GlobalMap;

public class VoucherListModelAdapter {

	public static ArrayList<VoucherDisplayListItem> Items;
	
	public static void LoadModel(List<Voucher> list) {
		
		int id = 0;
		Items = new ArrayList<VoucherDisplayListItem>();
		int temp;
		for (Voucher item : list) 
		{
			
			int merchant_id_test = item.MerchantID;   // Gets merchant Id of each element in list
			  
			String merchant_id_string = "" + merchant_id_test;
			if(GlobalMap.test_map.containsKey(merchant_id_string))  // if map contains merchant already
			{			  
				temp = GlobalMap.test_map.get(merchant_id_string);
				temp = temp + 1;
				GlobalMap.test_map.put(merchant_id_string, temp);  // overwrites  current integer
			}
			else   // IF not in map add to Items and set integer to 1
			{
			
				Items.add(new VoucherDisplayListItem(++id, item.LogoPath , item.MerchantName , item.VoucherTypeDesc,item.MerchantID));
				GlobalMap.test_map.put(merchant_id_string, 1);
			}
			
			//Items.add(new VoucherDisplayListItem(++id, item.LogoPath , item.MerchantName , item.VoucherTypeDesc,item.MerchantID));   //Ryan W  added merchant ID
			 
			 
		}

	}
	public static VoucherDisplayListItem GetbyId(int id) {

		for (VoucherDisplayListItem item : Items) {
			if (item.Id == id) {
				return item;
			}
		}
		return null;
	}
}
