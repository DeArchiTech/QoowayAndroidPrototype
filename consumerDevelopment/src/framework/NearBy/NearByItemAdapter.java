package framework.NearBy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ui.DisplayListItem;

import com.qooway.consumerv01.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import data.DataStorageManager;


public class NearByItemAdapter extends ArrayAdapter<String>{

	private final Context context;
	private final String[] Ids;
	private final int rowResourceId;

	public NearByItemAdapter(Context context, int textViewResourceId,
			String[] objects) {
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
		TextView namePosition = (TextView) rowView.findViewById(R.id.shopName);
		TextView addressPosition = (TextView) rowView.findViewById(R.id.address);
		TextView promotionPosition = (TextView) rowView.findViewById(R.id.promotion);
		TextView distancePosition = (TextView) rowView.findViewById(R.id.distance);
        int id = Integer.parseInt(Ids[position]);

        DisplayListItem item = NearByModelAdapter.GetbyId(id);
		namePosition.setText(item.Info[0]);
		addressPosition.setText(item.Info[1]);
		promotionPosition.setText(item.Info[2]);
		distancePosition.setText(item.Info[3]);

		//Boolean imageRecieved = false;
		
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
        .cacheInMemory(true) 
        .build();
		String baseUri = "https://" + DataStorageManager.getSingletonInstance().getApiUrl() + "/api/Picture/GetLogo/";
		String MerchantID =item.StoreID;
		String imageUri = baseUri + MerchantID;
		ImageLoader IM = ImageLoader.getInstance();
		IM.displayImage(imageUri, imageView, options); 
        
        
		return rowView;
	}
	

}


