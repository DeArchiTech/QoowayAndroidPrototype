package ui.favorite;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ui.MerchantDetailActivity;
import ui.CustomerPageFragment;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.TouchDelegate;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qooway.consumerv01.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import data.Deserialize;
import data.WebApiManager;
import data.DataStorageManager;
import framework.DataObject.Merchant;

public class FavoriteListItemAdapter extends ArrayAdapter<String>{

	private final Context context;
	private final ArrayList<String> Ids;
	private final int rowResourceId;
	private WebApiManager webApiManager;  // Ryan addition
	private DataStorageManager dataStorageManager;
	protected Button cancelButton;

	public FavoriteListItemAdapter(Context context, int textViewResourceId,
			ArrayList<String> objects, WebApiManager wam, DataStorageManager dsm) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.Ids = objects;
		this.rowResourceId = textViewResourceId;
		this.webApiManager = wam;
		this.dataStorageManager = dsm;
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
        FavoriteDisplayListItem item = null;
		
        item = FavoriteListModelAdapter.GetbyId(id);
        
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
		
		//connie implemented touchdelegate to enlarge clickable field
		//on the cancelbutton "x" kakao
		cancelButton = (Button) rowView.findViewById(R.id.cancel);
		View cancelParent = rowView.findViewById(R.id.cancelButton);
		
		cancelParent.post(new Runnable() {
			public void run(){
				Rect delegateArea = new Rect();
				Button delegate = FavoriteListItemAdapter.this.cancelButton;
				delegate.getHitRect(delegateArea);
				delegateArea.top -= 200;
				delegateArea.bottom += 200;
				delegateArea.left -= 200;
				delegateArea.right += 200;
				TouchDelegate expandedArea = new TouchDelegate(delegateArea, delegate);
				
				if(View.class.isInstance(delegate.getParent()))
					((View) delegate.getParent()).setTouchDelegate(expandedArea);
			}
		}); //increase hitbox area by 10px on each side
		
		// Ryan addition for removing item in void FAVORITES
				
		cancelButton.setTag(R.integer.StoreID,item.StoreID);  // need for deletion		
		cancelButton.setTag(R.integer.position,position);   // need for position of each button clicked
		
				
		cancelButton.setOnClickListener(new View.OnClickListener() {
				    @Override
				    public void onClick(View v) {
				    	
				    	final Dialog dialog = new Dialog(context);
						dialog.setContentView(R.layout.favorite_dialog);
						
						dialog.setTitle("WARNING:");
						Button dialogButton_yes = (Button) dialog.findViewById(R.id.button_confirm);
						Button dialogButton_no = (Button)dialog.findViewById(R.id.button_cancel);
						
						dialogButton_yes.setTag(R.integer.position,v.getTag(R.integer.position));   // need position
						dialogButton_yes.setTag(R.integer.StoreID,v.getTag(R.integer.StoreID));  
						// Yes dialog button
						dialogButton_yes.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								
							     int y = (Integer)v.getTag(R.integer.position);  
							     String storeID = (String)v.getTag(R.integer.StoreID);   
							     
							     FavoriteListModelAdapter.Items.remove(y);
							     changeID(y);  // this is the position/ index removed
							     CustomerPageFragment.f_Adapter.notifyDataSetChanged();
					             
							 
							     try {
							    	 
							    	 webApiManager.postDeleteFavorite(dataStorageManager.currentUser.CustomerID,storeID);  // Adds a reverse entry in data base to cancel out current selection
							    	 
									} catch (InterruptedException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									} catch (ExecutionException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
							
								dialog.dismiss();
							}
						});
						
						// NO dialog button
						dialogButton_no.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								
								dialog.dismiss();
							}
						});

						dialog.show();
				    }       
				});
				
		rowView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				final FavoriteDisplayListItem favouriteItem = FavoriteListModelAdapter.GetbyId(id);
				
				String result = "";
				String temp_merchantID = favouriteItem.StoreID;
				try {
					result = webApiManager.getMerchantInfo(temp_merchantID);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				if(result == null){
					return;
				}					
				Deserialize deserializer = new Deserialize();
				Merchant selectedMerchant = deserializer.getMerchant(result);
				dataStorageManager.selectedMerchant = selectedMerchant;
				Intent i = new Intent(context, MerchantDetailActivity.class);
				context.startActivity(i);
			}
		});
		return rowView;
	}
	
	
	// Ryan addition for removing item in favorite transaction
	public void changeID(int index)
	{
		while(index < FavoriteListModelAdapter.Items.size())
		{ 
			FavoriteListModelAdapter.Items.get(index).Id = FavoriteListModelAdapter.Items.get(index).Id - 1 ;
			index++;
		}
		CustomerPageFragment.favoriteIDS.remove(CustomerPageFragment.favoriteIDS.size()-1);  // remove number off of arraylist
	}

}
