package ui;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ui.searchList.SearchDisplayListItem;
import ui.searchList.SearchListItemAdapter;
import ui.searchList.SearchListModelAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.qooway.consumerv01.R;

import data.Deserialize;
import data.WebApiManager;
import data.DataStorageManager;
import framework.QoowayActivity;
import framework.DataObject.Merchant;

public class SearchBarActivity extends QoowayActivity{
	public WebApiManager webApiManager;
	public DataStorageManager dataStorageManager;
	public ListView listViewToDisplay;
	public String whichSearchBar = "";
	
	 protected void onCreate(Bundle savedInstanceState){
		 
		 	Intent intent = getIntent();
		 	whichSearchBar = intent.getExtras().getString("whichSearchBar");

		 
	    	dataStorageManager = DataStorageManager.getSingletonInstance();
	    	webApiManager = WebApiManager.getSingletonInstance();
	    	super.onCreate(savedInstanceState);
	        getActionBar().setTitle("Search Result");
	        
	        //If results are not found
			if(dataStorageManager.SearchBarMerchantList.isEmpty()) {
				setContentView(R.layout.search_bar_empty);
			} else {	        
	        setContentView(R.layout.search_bar);
	        
	        this.listViewToDisplay = (ListView)findViewById(R.id.listView1);
			SearchListItemAdapter c_Adapter = null; 
			ArrayList<String> searchIDS = new ArrayList<String>();
			
			SearchListModelAdapter.LoadModel(dataStorageManager.SearchBarMerchantList);
			 for(int i = 0; i < SearchListModelAdapter.Items.size(); i++ )
			{
				 searchIDS.add(Integer.toString(i));
			}
			 c_Adapter = new SearchListItemAdapter(
					 this, R.layout.search_list_item, searchIDS);   // When the loading happens
			listViewToDisplay.setAdapter(c_Adapter);
			
			listViewToDisplay.setTag(R.integer.activity,this);
			
			if(whichSearchBar.equals("search")){
				listViewToDisplay
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent,
							View view, int position, long id) {
						SearchBarActivity activity = (SearchBarActivity) parent.getTag(R.integer.activity);
						DataStorageManager.getSingletonInstance().selectedMerchant = dataStorageManager.SearchBarMerchantList
								.get(position);
						Intent i = new Intent(activity, MerchantDetailActivity.class);
						startActivity(i);
						
					}
				});
				
			} else if(whichSearchBar.equals("checkin")){
				listViewToDisplay
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent,
							View view, int position, long id) {
						SearchDisplayListItem merchant = SearchListModelAdapter.Items.get(position);
						String result = "";
						try {
							result = webApiManager.getMerchantInfo(merchant.StoreID);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Deserialize deserializer = new Deserialize();
						Merchant selectedMerchant = deserializer.getMerchant(result);
						SearchBarActivity activity = (SearchBarActivity) parent.getTag(R.integer.activity);
						dataStorageManager.selectedMerchant = selectedMerchant;
						Intent intent = new Intent(activity, CheckInActivity.class);
					    startActivity(intent);
						
					}
				});
			}
			
			
			}
			setUpTitle();  // sets up red header
	
	        
	 }
	 
	
	 private void setUpTitle(){
		 TextView search_bar_title = (TextView) findViewById(R.id.search_bar_title);
		 if(dataStorageManager.loggedIn){
			 String updatedPoints = "You currently have: " + dataStorageManager.currentUser.NetPoints + " QooPoints";
			 search_bar_title.setText(updatedPoints);
			 
		 } else{
			 search_bar_title.setText(R.string.sign_up);
		 }
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
