	package ui.merchantList;
	import java.util.ArrayList;

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

public class NewHomeListItemAdapter extends ArrayAdapter<NewHomeListDisplayListItem>{

		private final Context context;
		private final int rowResourceId;
		private int id;
		private ArrayList<NewHomeListDisplayListItem> Items;
		private DataStorageManager dataStorageManager;
		
		public NewHomeListItemAdapter(Context context, int textViewResourceId , ArrayList<NewHomeListDisplayListItem> Items) {
			super(context, textViewResourceId, Items);
			this.context = context;
			this.rowResourceId = textViewResourceId;
			this.Items= Items;
			this.dataStorageManager = DataStorageManager.getSingletonInstance();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if(position<this.Items.size())
			{
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(rowResourceId, parent, false);
			ImageView imageView = (ImageView) rowView.findViewById(R.id.list_image);
			TextView namePosition = (TextView) rowView.findViewById(R.id.shopName);
			TextView addressPosition = (TextView) rowView.findViewById(R.id.address);
			TextView promotionPosition = (TextView) rowView.findViewById(R.id.promotion);
			TextView distancePosition = (TextView) rowView.findViewById(R.id.distance);
	        this.id = position;
	        MerchantDisplayListItem item = MerchantListModelAdapter.GetbyId(id);
	        System.out.println(item.Info[0]);
			namePosition.setText(item.Info[0]);
			addressPosition.setText(item.Info[1]);
			promotionPosition.setText(item.Info[2]);
			if(item.Distance>=0)
				distancePosition.setText(item.Info[3]);
			else
			{
				distancePosition.setText("");
			}

			DisplayImageOptions options = new DisplayImageOptions.Builder()
	        .cacheInMemory(true) 
	        .build();
			
			String baseUri = "https://" + DataStorageManager.getSingletonInstance().getApiUrl() + "/api/Picture/GetLogo/";
			String MerchantID =item.StoreID ;
			String imageUri = baseUri + MerchantID;
			ImageLoader IM = ImageLoader.getInstance();
			IM.displayImage(imageUri, imageView, options); 
			//IM.displayImage(baseUri, imageView, options); 
			
			return rowView;
			}
			return null;
		}
}



