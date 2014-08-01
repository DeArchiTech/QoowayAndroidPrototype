package ui.advanced_search;

import java.util.ArrayList;

import ui.MerchantDetailActivity;
import framework.QoowayActivity;
import ui.searchList.SearchListItemAdapter;
import ui.searchList.SearchListModelAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.qooway.consumerv01.R;
import data.WebApiManager;
import data.DataStorageManager;

public class SelectedRestaurantActivity extends QoowayActivity {

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
		setTitle("");
		Intent intent = getIntent();
	    whichSearchChosen = intent.getExtras().getString("whichSearchChosen");
	    
	    this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	       
	    getActionBar().setDisplayHomeAsUpEnabled(false);
	    getActionBar().setHomeButtonEnabled(false);
	    
	    setContentView(R.layout.fragment_advanced_search);
	    
        webApiManager = WebApiManager.getSingletonInstance();
		dataStorageManager = DataStorageManager.getSingletonInstance();
	    
		this.listViewToDisplay = (ListView) findViewById(R.id.listView1);
    	SearchListItemAdapter search_adapter = null;
    	ArrayList<String> searchIDS = new ArrayList<String>();
    	for(int i = 0; i < SearchListModelAdapter.Items.size(); i++ )
		{
    		searchIDS.add(Integer.toString(i));
		}
    	
    	search_adapter = new SearchListItemAdapter(this,
				R.layout.search_list_item, searchIDS);
    	
    	
    	listViewToDisplay.setTag(R.integer.activity,this);
    	listViewToDisplay.setAdapter(search_adapter);	
    	listViewToDisplay.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {
				
				DataStorageManager.getSingletonInstance().selectedMerchant = dataStorageManager.SearchList
						.get(position);
				 
				Intent i = new Intent(SelectedRestaurantActivity.this, MerchantDetailActivity.class);
				startActivity(i);
				
			}
		});		
		
	}	

	  @Override
	  public boolean onOptionsItemSelected(MenuItem menuItem)
	  {       
		  int itemId = menuItem.getItemId();
			
	      // Respond to the action bar's Up/Home button
	      if(itemId == R.id.done_button)
	      {
	    	  finish();
	    	  return true;
	      }
	      

	      return super.onOptionsItemSelected(menuItem);
	  } 

	  //
	  @Override
		public boolean onPrepareOptionsMenu(Menu menu) {
		  menu.findItem(R.id.information).setVisible(false);
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
