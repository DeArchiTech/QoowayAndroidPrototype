package ui.merchantList;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qooway.consumerv01.R;

import data.DataStorageManager;

public class MerchantListItemAdapter extends ArrayAdapter<MerchantDisplayListItem>{

	private final Context context;
	private final int rowResourceId;
	private int id;
	private ArrayList<MerchantDisplayListItem> Items;
	public ImageView imageView;
	
	public MerchantListItemAdapter(Context context, int textViewResourceId , ArrayList<MerchantDisplayListItem> Items) {
		super(context, textViewResourceId, Items);
		this.context = context;
		this.rowResourceId = textViewResourceId;
		this.Items= Items;
	}

	public void setArrayListItems(ArrayList<MerchantDisplayListItem> Items) {
		this.Items= Items;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if(position<this.Items.size())
		{
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(rowResourceId, parent, false);
		imageView = (ImageView) rowView.findViewById(R.id.list_image);
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

		//Universal Image Loader attempt

		
		DisplayImageOptions options = new DisplayImageOptions.Builder().resetViewBeforeLoading(false)
        .cacheInMemory(true)
        .build();

		//String baseUri = "https://online.profitek.com/appdevelopment/api/Picture/GetLogo/109";
		String baseUri = "https://" + DataStorageManager.getSingletonInstance().getApiUrl() + "/api/Picture/GetLogo/";	String MerchantID = item.StoreID ;
		String imageURL = baseUri + MerchantID;
		ImageLoader.getInstance().displayImage(imageURL, imageView, options);
		//ImageLoader.getInstance().displayImage(baseUri, imageView, options);

		
		/*
		 WebApi Attempt
		
		String MerchantID = item.StoreID;
		
		//InputStream result = new ByteArrayInputStream(new String("Qooway").getBytes());
		
		InputStream result = null;
		
		try {
			result = webApiManager.getLogo(MerchantID);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		/*
		if(result == null)
			result = new ByteArrayInputStream(new String("").getBytes());
		*/
		//String result2 = result.toString();
		//Bitmap mIcon1 = BitmapFactory.decodeStream(result);
		/*
		BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inSampleSize = 2;
	    options.inScaled = true;
	    options.inPurgeable = true;

	    Bitmap bitmap = BitmapFactory.decodeStream(result, null, options);
		imageView.setImageBitmap(bitmap);
		*/
		
		
		
		
		//Picasso Attempt
		/*
		Picasso.with(context)
		  .load("https://online.profitek.com/appdevelopment/api/Picture/GetLogo/109_100x100.jpeg" +
		  		"")
		  .resize(50, 50)
		  .centerCrop()
		  .into(imageView);
		*/
		
		
		
		/*
		//Google Volley Attempt
		ImageLoader imageLoader = AppController.getInstance().getImageLoader();
		
		 
		// If you are using normal ImageView
		imageLoader.get("https://online.profitek.com/appdevelopment/api/Picture/GetLogo/109", new ImageListener() {
		 
		    @Override
		    public void onErrorResponse(VolleyError error) {
		        Log.e("Google Volley", "Image Load Error: " + error.getMessage());
		    }
		 
		    @Override
		    public void onResponse(ImageContainer response, boolean arg1) {
		        if (response.getBitmap() != null) {
		            // load image into imageview
		            imageView.setImageBitmap(response.getBitmap());
		        }
		    }
		});
		*/

		return rowView;
		}
		return null;
	}
	
	
}



