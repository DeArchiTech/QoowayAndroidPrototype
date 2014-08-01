package ui;

import java.util.concurrent.ExecutionException;

import ui.voucher.ListVoucherListModelAdapter;

import com.qooway.consumerv01.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import data.Deserialize;
import data.WebApiManager;
import data.DataStorageManager;
import framework.DataObject.Merchant;
import framework.DataObject.VoucherGrouped;
import framework.QoowayActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RedeemPointsActivity extends QoowayActivity {
	
	final Context context = this;
	public WebApiManager webApiManager;
	public DataStorageManager dataStorageManager;
	public int Position;
	public String MerchantID;
	public String StoreName;
	public String voucherDescription;
	public int Quantity;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	dataStorageManager = DataStorageManager.getSingletonInstance();
    	webApiManager = WebApiManager.getSingletonInstance();
    	super.onCreate(savedInstanceState);
        getActionBar().setTitle("Redeem Points");   
        setContentView(R.layout.fragment_redeem_details);
        
        
        ImageView btnNew = (ImageView) findViewById(R.id.imageView1);
        btnNew.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) 
              {
					//If user has less than 1000 Qoopoints, they cannot redeem a voucher
					if(dataStorageManager.currentUser.NetPoints < 1000) {
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);
							alertDialogBuilder.setTitle(getString(R.string.voucher_title));
							alertDialogBuilder
								.setMessage(getString(R.string.voucher_error))
								.setCancelable(false)
								.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
	
									}
								  });
								AlertDialog alertDialog = alertDialogBuilder.create();
								alertDialog.show();
								return;
					}            	  
            	  
            	  	final Dialog dialog = new Dialog(context);
        			dialog.setContentView(R.layout.dialog_redeem);
        			dialog.setTitle("Redeem Points");
        			
        			Button confirmButton = (Button)dialog.findViewById(R.id.button_confirm);
        			confirmButton.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View v) {							
							String result = "";
							try {
								result= webApiManager.getAllRedeemableVouchersByMerchant(MerchantID);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ExecutionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Deserialize deserializer = new Deserialize();
							dataStorageManager.RedeemableVouchersSpecificMerchant = deserializer.getVoucherGroupList(result);

							VoucherGrouped chosenVoucher = null;
							String voucherCode = "";
							int index = 0;
							while(voucherCode.equals("") )
							{
								chosenVoucher = dataStorageManager.RedeemableVouchersSpecificMerchant.get(index);// there are three types
								
								if(chosenVoucher.VoucherTypeDesc.equalsIgnoreCase("$10 Food Voucher"))   // if correct voucher type, simplified till updated
								{
									voucherCode = chosenVoucher.VoucherCodes[0];
								}
								else if(chosenVoucher.VoucherTypeDesc.equalsIgnoreCase("$10 Gift Voucher"))
								{
									voucherCode = chosenVoucher.VoucherCodes[0];
								}
								else if(chosenVoucher.VoucherTypeDesc.equalsIgnoreCase("$10 Service Voucher"))
								{
									voucherCode = chosenVoucher.VoucherCodes[0];
								} else if (chosenVoucher.VoucherTypeDesc.equalsIgnoreCase("$10 Car Wash Voucher")) {
									voucherCode = chosenVoucher.VoucherCodes[0];
								}
								
								index++;
							}
							redeemVoucherForCustomer(voucherCode);  
							
							refreshLayout();
							//kakao revised wording of toast
							Toast.makeText(context, "You have successfully redeemed a voucher.", Toast.LENGTH_SHORT).show();
		
							dialog.dismiss();	
						}
						
        				
        			});
        			
        			Button cancelButton = (Button) dialog
        			.findViewById(R.id.button_cancel);
        			// if button is clicked, close the custom dialog
        			cancelButton.setOnClickListener(new OnClickListener() {
        			@Override
        			public void onClick(View v) {
        			dialog.dismiss();
        			}
        			});
        			
        			dialog.show();
                
              }

            });  
       
    	}
    
	@Override
	protected void onResume() {
		super.onResume();
	}
    
    public void redeemVoucherForCustomer(String VoucherCode)
    {
    	
		try {
			webApiManager.postRedeemVoucher(dataStorageManager.currentUser.CustomerID,VoucherCode);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
    }
    
    	protected void onStart() {
    		super.onStart();
			
			// Update voucher screen for selected merchant
    		Bundle extras = getIntent().getExtras();
            Position = extras.getInt("position");          
            MerchantID = extras.getString("MerchantID"); 
            StoreName = extras.getString("StoreName"); 
            voucherDescription = extras.getString("voucherName");
      
	        ImageView restaurantLogo = (ImageView)findViewById(R.id.restaurantLogo);
	        TextView storeName = (TextView)findViewById(R.id.TextView);
	        
	        // Ryan
	        TextView voucherName = (TextView)findViewById(R.id.vText);
	        voucherName.setText(voucherDescription);
	        
			//Comment out to not display Voucher Quantity
	        //setTotalVoucherAvailable(ListVoucherListModelAdapter.Items.get(Position).VoucherCount);   // update available vouchers
	        setTotalPoints();   // update total points on screen
	        storeName.setText(StoreName);
	        
	        DisplayImageOptions options = new DisplayImageOptions.Builder()
	        .cacheInMemory(true) 
	        .build();
	        
	        String baseUri = "https://" + DataStorageManager.getSingletonInstance().getApiUrl()+ "/api/Picture/GetLogo/";
			String imageUri = baseUri + MerchantID;
			ImageLoader IM = ImageLoader.getInstance();
			IM.displayImage(imageUri, restaurantLogo, options); 
	        
    	}
 /*   	
    	public void setTotalVoucherAvailable(int count)
    	{
    		 TextView quantity = (TextView)findViewById(R.id.vLeft);
    		 quantity.setText("" + "Available: " +count);
    	}
    	*/
    	public void setTotalPoints()
    	{
    		TextView totalPoints = (TextView)findViewById(R.id.voucher_title);
    		String result="";
			try {
				result = webApiManager.getCustomerInfo(dataStorageManager.currentUser.CustomerID);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Deserialize deserializer = new Deserialize();
			dataStorageManager.currentUser = deserializer.getCustomer(result);
					
			String updatedPoints = "You Currently have: " + dataStorageManager.currentUser.NetPoints + " QooPoints";
			totalPoints.setText(updatedPoints);
    	}
    	
    	// used after voucher is redeemed have to refresh list and text View, and total points and vouchers available
    	public void refreshLayout()
    	{
    	
    		GlobalMap.test_map.clear();  // CLEAR MAP so it is empty

		/*	String result ="";
			try {
				result = webApiManager.getVoucherAll();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Deserialize deserializer = new Deserialize();
			dataStorageManager.RedeemableVoucherList= deserializer.getListOfVoucherList(result);
			
			ListVoucherListModelAdapter.LoadModel(dataStorageManager.RedeemableVoucherList);*/

			//Comment out to not display Voucher Quantity
    		//setTotalVoucherAvailable(ListVoucherListModelAdapter.Items.get(Position).VoucherCount-1);
			setTotalPoints();
		
    	}

        @Override
        public boolean onOptionsItemSelected(MenuItem menuItem)
        {       
      	  int itemId = menuItem.getItemId();
      		
            // Respond to the action bar's Up/Home button
            if(itemId == android.R.id.home)
            {
            //NavUtils.navigateUpFromSameTask(this);
            finish();
            return true;
            
            }
            //
            if (itemId == R.id.information) 
            { // if click information action button
      			// on myVouchers page
      			final Dialog dialog = new Dialog(context);
      			dialog.setContentView(R.layout.favorite_dialog);
      			dialog.setTitle("How to redeem vouchers:");
      			
      			Button dialogButton = (Button) dialog
      			.findViewById(R.id.button1_information);
      			// if button is clicked, close the custom dialog
      			dialogButton.setOnClickListener(new OnClickListener() {
      			@Override
      			public void onClick(View v) {
      			dialog.dismiss();
      			}
      			});
      			
      			dialog.show();
      			return true;
            }
           
            
            return super.onOptionsItemSelected(menuItem);
        } 


        
        @Override
      	public boolean onPrepareOptionsMenu(Menu menu) {
      	  menu.findItem(R.id.information).setVisible(false);
      	  menu.findItem(R.id.done_button).setVisible(false);
      	  menu.findItem(R.id.logout_button).setVisible(false);
      	  menu.findItem(R.id.join_button).setVisible(false);
      	  
      	  return super.onPrepareOptionsMenu(menu);
        }
        
        @Override
      	public boolean onCreateOptionsMenu(Menu menu) {
      		// Inflate the menu; this adds items to the action bar if it is present.
      		getMenuInflater().inflate(R.menu.main, menu);
      		return super.onCreateOptionsMenu(menu);
      		
      	}
        
        
        public void clickMerchantDetails(View v)
        {
        	
        	String result ="";
        	try {
				result = WebApiManager.getSingletonInstance().getMerchantInfo(MerchantID);
				/*
				Gson gsonDate = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
				Merchant merchant = gsonDate.fromJson(result,
						new TypeToken<Merchant>() {
						}.getType());*/
				Deserialize  deserializer = new  Deserialize();
				Merchant merchant = deserializer.getMerchant(result);
				dataStorageManager.selectedMerchant = merchant;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
			
						
        	Intent i = new Intent(this, MerchantDetailActivity.class);
			startActivity(i);
        }
      	 
      	

}