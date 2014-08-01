package ui.advanced_search;

import java.util.ArrayList;

import ui.MerchantDetailActivity;
import framework.QoowayActivity;
import ui.averagePrice.AveragePriceListItemAdapter;
import ui.averagePrice.AveragePriceListModelAdapter;
import ui.cuisine.CuisineListItemAdapter;
import ui.cuisine.CuisineListModelAdapter;

import ui.restaurantType.RestaurantTypeListItemAdapter;
import ui.restaurantType.RestaurantTypeListModelAdapter;
import ui.searchList.SearchListItemAdapter;
import ui.searchList.SearchListModelAdapter;
import ui.searchOptions.OptionListItemAdapter;
import ui.searchOptions.OptionListModelAdapter;
import ui.sortBy.SortByListItemAdapter;
import ui.sortBy.SortByListModelAdapter;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.WindowManager;


import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;


import com.qooway.consumerv01.R;


import data.WebApiManager;
import data.DataStorageManager;

public class RestaurantListActivity extends QoowayActivity {
	
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
		setTitle("");
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		Intent intent = getIntent();
	    whichSearchChosen = intent.getExtras().getString("whichSearchChosen");
	    if(whichSearchChosen.equals("searchClicked"))
	    {
	       setContentView(R.layout.fragment_advanced_search);
	    }
	    else{
	    	setContentView(R.layout.fragment_cuisine);
	    }

	
      
	  getActionBar().setDisplayHomeAsUpEnabled(false);
      getActionBar().setHomeButtonEnabled(false);
      
        
      
        webApiManager = WebApiManager.getSingletonInstance();
		dataStorageManager = DataStorageManager.getSingletonInstance();
        
        if(whichSearchChosen.equals("Cuisine"))
        {
	        this.listViewToDisplay = (ListView)findViewById(R.id.myCuisineList);
			 CuisineListItemAdapter c_Adapter = null; 
			 ArrayList<String> cuisineIDS = new ArrayList<String>();
			 for(int i = 0; i < CuisineListModelAdapter.Items.size(); i++ )
			{
				 cuisineIDS.add(Integer.toString(i));
			}
			 c_Adapter = new CuisineListItemAdapter(
					 this, R.layout.cuisine_list_item, cuisineIDS);   // When the loading happens
			listViewToDisplay.setAdapter(c_Adapter);
        }
        else if(whichSearchChosen.equals("Restaurant"))
        {
	        this.listViewToDisplay = (ListView)findViewById(R.id.myCuisineList);
	   		 RestaurantTypeListItemAdapter r_Adapter = null; 
	   		 ArrayList<String> restaurantIDS = new ArrayList<String>();
	   		 //Null Pointer Exception here
	   		 for(int i = 0; i < RestaurantTypeListModelAdapter.Items.size(); i++ )
	   		{
	   			 restaurantIDS.add(Integer.toString(i));
	   		}
	   		 r_Adapter = new RestaurantTypeListItemAdapter(
	   				 this, R.layout.cuisine_list_item, restaurantIDS);   // When the loading happens
	   		listViewToDisplay.setAdapter(r_Adapter);
        }
        else if(whichSearchChosen.equals("Option"))
        {
        	 this.listViewToDisplay = (ListView) findViewById(R.id.myCuisineList);
             OptionListItemAdapter o_Adapter = null;
             ArrayList<String> optionIDS = new ArrayList<String>();
             for(int i = 0; i < OptionListModelAdapter.Items.size(); i++ )
     		{
     			optionIDS.add(Integer.toString(i));
     		}
             o_Adapter = new OptionListItemAdapter(
             		this, R.layout.cuisine_list_item, optionIDS);
             listViewToDisplay.setAdapter(o_Adapter);
        }
        else if(whichSearchChosen.equals("AveragePrice"))
        {
        	   this.listViewToDisplay = (ListView) findViewById(R.id.myCuisineList);
               AveragePriceListItemAdapter average_Adapter = null;
               ArrayList<String> averagePriceIDS = new ArrayList<String>();
               for(int i = 0; i < AveragePriceListModelAdapter.Items.size(); i++ )
       		{
               	averagePriceIDS.add(Integer.toString(i));
       		}
               average_Adapter = new AveragePriceListItemAdapter(
               		this, R.layout.cuisine_list_item, averagePriceIDS);
               listViewToDisplay.setAdapter(average_Adapter);
               
        }
        
        else if(whichSearchChosen.equals("SortBy"))
        {
        	this.listViewToDisplay = (ListView) findViewById(R.id.myCuisineList);
            SortByListItemAdapter s_Adapter = null;
            ArrayList<String> sortByIDS = new ArrayList<String>();
            for(int i = 0; i < SortByListModelAdapter.Items.size(); i++ )
    		{
            	sortByIDS.add(Integer.toString(i));
    		}
            s_Adapter = new SortByListItemAdapter(
            		this, R.layout.cuisine_list_item, sortByIDS);
            
            //AdvancedSearchActivity.sortBySelectedList.set(0, 1); // Hard core to A-Z 
            
            listViewToDisplay.setAdapter(s_Adapter);
        }
        else if(whichSearchChosen.equals("searchClicked"))
        {
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
        	listViewToDisplay
			.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent,
						View view, int position, long id) {
					
					RestaurantListActivity activity = (RestaurantListActivity) parent.getTag(R.integer.activity);
					DataStorageManager.getSingletonInstance().selectedMerchant = dataStorageManager.SearchList
							.get(position);
					 
					Intent i = new Intent(activity, MerchantDetailActivity.class);
					startActivity(i);
					
				}
			});
        	
        	
        }
       
        
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