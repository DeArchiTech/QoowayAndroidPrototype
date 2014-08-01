package ui.home;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qooway.consumerv01.R;

import data.DataStorageManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeListItemAdapter extends ArrayAdapter<HomeDisplayListItem>{

	private final Context context;
	private final int rowResourceId;
	private int id;
	private ArrayList<HomeDisplayListItem> Items;
	
	public HomeListItemAdapter(Context context, int textViewResourceId , ArrayList<HomeDisplayListItem> Items) {
		super(context, textViewResourceId, Items);
		this.context = context;
		this.rowResourceId = textViewResourceId;
		this.Items= Items;
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
        this.id = position;
        HomeDisplayListItem item = HomeListModelAdapter.GetbyId(id);
		namePosition.setText(item.StoreName);
		addressPosition.setText(item.Address);
		promotionPosition.setText(item.SpecialDeal);


		DisplayImageOptions options = new DisplayImageOptions.Builder()
        .cacheInMemory(true) 
        .build();
		String baseUri = "https://" + DataStorageManager.getSingletonInstance().getApiUrl() + "/api/Picture/GetLogo/";
		String MerchantID ="" + item.StoreID ;
		String imageUri = baseUri + MerchantID;
		ImageLoader IM = ImageLoader.getInstance();
		IM.displayImage(imageUri, imageView, options); 
		
		
		return rowView;
		}
		return null;
	}
}