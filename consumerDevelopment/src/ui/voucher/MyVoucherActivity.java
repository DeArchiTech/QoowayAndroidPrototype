package ui.voucher;

import java.util.List;
import java.util.concurrent.ExecutionException;
import ui.GlobalMap;
import framework.QoowayActivity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.qooway.consumerv01.R;
import data.Deserialize;
import data.WebApiManager;
import data.DataStorageManager;
import framework.DataObject.Voucher;

public class MyVoucherActivity extends QoowayActivity {
	
	final Context context = this;
	public String httpserverUrl = "online.profitek.com/testingAPi";
	public String httpsserverUrl = "online.profitek.com/testingAPi";	
	public WebApiManager webApiManager;
	public DataStorageManager dataStorageManager;
	public ListView listViewToDisplay;
	
	String whichSearchChosen;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("My Vouchers");
		
		
	    setContentView(R.layout.fragment_my_vouchers);
	    getActionBar().setDisplayHomeAsUpEnabled(true);   
	    
	    this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	       
      
  
        
        webApiManager = WebApiManager.getSingletonInstance();
		dataStorageManager = DataStorageManager.getSingletonInstance();
		
		String result = "";
		
		try{
			GlobalMap.test_map.clear();
			result = webApiManager.getVoucherRedeemed(dataStorageManager.currentUser.CustomerID);  

			if(result == null) {
			    setContentView(R.layout.fragment_error);
			    return;
			}
			Deserialize deserializer = new Deserialize();
			dataStorageManager.RedeemedVoucher = deserializer.getVoucherList(result); 
			
			listViewToDisplay = (ListView)findViewById(R.id.myVoucherList);

			// KAKAO PLAYS A MEAN LEE SIN

			TextView noVouchersText = (TextView) findViewById(android.R.id.empty);
			listViewToDisplay .setEmptyView(noVouchersText); 
			setUpVocherListAdapter(dataStorageManager.RedeemedVoucher,listViewToDisplay);
			
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			} 
        
        
	}
	
	private void setUpVocherListAdapter(List<Voucher> voucher, ListView listView) {


		VoucherListModelAdapter.LoadModel(voucher);
		String[] ids = new String[VoucherListModelAdapter.Items.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = Integer.toString(i + 1);
		}
		VoucherListItemAdapter Adapter = new VoucherListItemAdapter(
				this, R.layout.list_item_voucher, ids);
		listView.setAdapter(Adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});
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
      
      if (itemId == R.id.information) 
      { // if click information action button
			// on myVouchers page
			final Dialog dialog = new Dialog(context);
			dialog.setContentView(R.layout.information_dialog);
			dialog.setTitle("How to Redeem Vouchers");
			
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

  //
  @Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	  menu.findItem(R.id.information).setVisible(true);
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
	 
	

}
