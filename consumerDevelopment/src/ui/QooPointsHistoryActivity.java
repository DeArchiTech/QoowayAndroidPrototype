package ui;

import java.util.List;
import java.util.concurrent.ExecutionException;
import ui.customerTransaction.CustomerTransactionListItemAdapater;
import ui.customerTransaction.CustomerTransactionListModelAdapater;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.qooway.consumerv01.R;
import data.Deserialize;
import data.WebApiManager;
import framework.DataObject.CustomerTransaction;
import data.DataStorageManager;

import framework.QoowayActivity;

public class QooPointsHistoryActivity  extends QoowayActivity {
	
	final Context context = this;
	public String httpserverUrl = "online.profitek.com/appdevelopment";
	public String httpsserverUrl = "online.profitek.com/appdevelopment";	
	public WebApiManager webApiManager;
	public DataStorageManager dataStorageManager;
	public ListView listViewToDisplay;
	String whichSearchChosen;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("QooPoints History");
		
	    setContentView(R.layout.fragment_qoopoints_history);
	    getActionBar().setDisplayHomeAsUpEnabled(true);   
	    
	    this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        webApiManager = WebApiManager.getSingletonInstance();
		dataStorageManager = DataStorageManager.getSingletonInstance();

		String result = "";

		try {
			result = webApiManager.getCustomerTransactions(dataStorageManager.currentUser.CustomerID);

			if(result == null) {
			    setContentView(R.layout.fragment_error);
			    return;
			}
			
			Deserialize  deserializer = new  Deserialize();
			dataStorageManager.customerTransaction = deserializer.getCustomerTransactionList(result);
					
			listViewToDisplay = (ListView)findViewById(R.id.qooPointsHistory);
			setUpCustomerTransactionListAdapater(dataStorageManager.customerTransaction,listViewToDisplay);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void setUpCustomerTransactionListAdapater(List<CustomerTransaction> ct, ListView listView)
	{
		CustomerTransactionListModelAdapater.LoadModel(ct);
		String[] ids = new String[CustomerTransactionListModelAdapater.Items.size()];
		for(int i = 0; i< ids.length; i++){
			ids[i] = Integer.toString(i + 1);
		}
		CustomerTransactionListItemAdapater Adapter = new CustomerTransactionListItemAdapater(
				this, R.layout.customer_transaction_list_item,ids); // pass 3 new things
		
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

      return super.onOptionsItemSelected(menuItem);
  } 

  //
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
	 
	

}