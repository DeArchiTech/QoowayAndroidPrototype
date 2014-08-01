
package ui.voucher;

import java.util.ArrayList;
import java.util.List;

import framework.DataObject.Voucher;
import framework.DataObject.VoucherList;
import ui.GlobalMap;

public class ListVoucherListModelAdapter {

	public static ArrayList<ListVoucherDisplayListItem> Items;
	
	public static void LoadModel(List<VoucherList> list) {
		
		int id = 0;
		ListVoucherListModelAdapter.Items = new ArrayList<ListVoucherDisplayListItem>();
		for (VoucherList item : list) 
		{
			Items.add(new ListVoucherDisplayListItem(id++, item.LogoPath , item.MerchantName , item.VoucherTypeDesc,item.MerchantID, item.VoucherCount));
			
			 
		}

	}
	public static ListVoucherDisplayListItem GetbyId(int id) {

		for (ListVoucherDisplayListItem item : ListVoucherListModelAdapter.Items) {
			if (item.Id == id) {
				return item;
			}
		}
		return null;
	}
}
