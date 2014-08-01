package ui.voucher;

import ui.GlobalMap;
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

public class VoucherListItemAdapter  extends ArrayAdapter<String>{

	private final Context context;
	private final String[] Ids;
	private final int rowResourceId;

	public VoucherListItemAdapter(Context context, int textViewResourceId,
			String[] objects ) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.Ids = objects;
		this.rowResourceId = textViewResourceId;

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
        int id = Integer.parseInt(Ids[position]);
        VoucherDisplayListItem item = null;
        
        item = VoucherListModelAdapter.GetbyId(id);
        name.setText(item.Name);
        promotion.setText(item.Description);
        
        // Ryan  making the text view equal to the total amount of vouchers left
        int temp_integer = GlobalMap.test_map.get(item.MerchantID);
        redeemableVouchers.setText("" + temp_integer);
        
        DisplayImageOptions options = new DisplayImageOptions.Builder().resetViewBeforeLoading(false)
        .cacheInMemory(true) 
        .build();
		String baseUri = "https://" + DataStorageManager.getSingletonInstance().getApiUrl() + "/api/Picture/GetLogo/";	String MerchantID =item.MerchantID ;
		String imageUri = baseUri + MerchantID;
		ImageLoader.getInstance().displayImage(imageUri, imageView, options); 
			
		return rowView;
		
	}
}



