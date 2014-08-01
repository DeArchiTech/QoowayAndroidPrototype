package ui.voucher;

import java.util.ArrayList;

import ui.GlobalMap;
import ui.merchantList.MerchantDisplayListItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qooway.consumerv01.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import data.DataStorageManager;

public class ListVoucherListItemAdapter extends ArrayAdapter<ListVoucherDisplayListItem>{

	private final Context context;
	private int id;
	private final int rowResourceId;
	private ArrayList<ListVoucherDisplayListItem> Items;
	
	public ListVoucherListItemAdapter(Context context, int textViewResourceId, ArrayList<ListVoucherDisplayListItem> Items) {
		super(context, textViewResourceId, Items);
		this.context = context;
		this.rowResourceId = textViewResourceId;
		this.Items= Items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(rowResourceId, parent, false);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.voucherImage);
		TextView name = (TextView) rowView.findViewById(R.id.voucherShopName);
		TextView promotion= (TextView) rowView.findViewById(R.id.voucherPromotion);
		TextView redeemableVouchers= (TextView) rowView.findViewById(R.id.textView_tester);   // voucher counter text image

		@SuppressWarnings("unused")
		TextView distancePosition = (TextView) rowView.findViewById(R.id.distance);
        this.id = position;
        ListVoucherDisplayListItem item = null;
        
        item = ListVoucherListModelAdapter.GetbyId(id);
        name.setText(item.Name);
        promotion.setText(item.Description);
        
        // Ryan  making the text view equal to the total amount of vouchers left

       // redeemableVouchers.setText("" + item.VoucherCount);
        
        DisplayImageOptions options = new DisplayImageOptions.Builder().resetViewBeforeLoading(false)
        .cacheInMemory(true) 
        .build();
    	String baseUri = "https://" + DataStorageManager.getSingletonInstance().getApiUrl() + "/api/Picture/GetLogo/";
    		String MerchantID =item.MerchantID ;
		String imageUri = baseUri + MerchantID;
		ImageLoader.getInstance().displayImage(imageUri, imageView, options); 
			
		return rowView;
		
	}
}



