package ui.searchList;

import java.util.ArrayList;

import com.qooway.consumerv01.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import data.DataStorageManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchListItemAdapter extends ArrayAdapter<String>{

	private final Context context;
	private final ArrayList<String> Ids;
	private final int rowResourceId;


	public SearchListItemAdapter(Context context, int textViewResourceId,
			ArrayList<String> objects) {
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
		ImageView imageView = (ImageView) rowView.findViewById(R.id.list_image);
		TextView name = (TextView) rowView.findViewById(R.id.shopName);
		TextView address= (TextView) rowView.findViewById(R.id.address);
		TextView promotion= (TextView) rowView.findViewById(R.id.promotion);   // voucher counter text image
		
		final int id = Integer.parseInt(Ids.get(position));
        SearchDisplayListItem item = null;
		
        item = SearchListModelAdapter.GetbyId(id);
        
        name.setText(item.StoreName);
        promotion.setText(item.promotion);
        address.setText(item.Address);
       
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
        .cacheInMemory(true) 
        .build();
		String baseUri = "https://" + DataStorageManager.getSingletonInstance().getApiUrl() + "/api/Picture/GetLogo/";
		String MerchantID =item.StoreID ;
		String imageUri = baseUri + MerchantID;
		ImageLoader IM = ImageLoader.getInstance();
		IM.displayImage(imageUri, imageView, options); 
        
        
        
        return rowView;
       
	}
}
