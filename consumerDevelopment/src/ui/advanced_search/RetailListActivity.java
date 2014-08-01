package ui.advanced_search;

import java.util.ArrayList;

import ui.MerchantDetailActivity;
import framework.QoowayActivity;
import ui.retail.ApparelListItemAdapter;
import ui.retail.ApparelListModelAdapter;
import ui.retail.ElectronicsListItemAdapter;
import ui.retail.ElectronicsListModelAdapter;
import ui.retail.EntertainmentListItemAdapter;
import ui.retail.EntertainmentListModelAdapter;
import ui.retail.FoodListItemAdapter;
import ui.retail.FoodListModelAdapter;
import ui.retail.HealthListItemAdapter;
import ui.retail.HealthListModelAdapter;

import ui.retail.ServicesListItemAdapter;
import ui.retail.ServicesListModelAdapter;
import ui.searchList.SearchListItemAdapter;
import ui.searchList.SearchListModelAdapter;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.qooway.consumerv01.R;
import data.WebApiManager;
import data.DataStorageManager;

public class RetailListActivity extends QoowayActivity {
	
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
	    if(whichSearchChosen.equals("searchClicked"))
	    {
	       setContentView(R.layout.fragment_advanced_search);
	    }
	    else{
	    	setContentView(R.layout.fragment_cuisine);
	    }
	    
	    this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	       
	    getActionBar().setDisplayHomeAsUpEnabled(false);
	    getActionBar().setHomeButtonEnabled(false);
        
        
      
        
        webApiManager = WebApiManager.getSingletonInstance();
		dataStorageManager = DataStorageManager.getSingletonInstance();
        
        if(whichSearchChosen.equals("Apparel"))
        {
	        this.listViewToDisplay = (ListView)findViewById(R.id.myCuisineList);
			ApparelListItemAdapter apparel_Adapter = null; 
			 ArrayList<String> apparelIDS = new ArrayList<String>();
			 for(int i = 0; i < ApparelListModelAdapter.Items.size(); i++ )
			{
				apparelIDS.add(Integer.toString(i));
			}
			 apparel_Adapter = new ApparelListItemAdapter(
					 this, R.layout.cuisine_list_item, apparelIDS);   // When the loading happens
			listViewToDisplay.setAdapter(apparel_Adapter);
        }
        
        else if(whichSearchChosen.equals("Food & Drink"))
        {
	        this.listViewToDisplay = (ListView)findViewById(R.id.myCuisineList);
			FoodListItemAdapter food_Adapter = null; 
			 ArrayList<String> foodIDS = new ArrayList<String>();
			 for(int i = 0; i < FoodListModelAdapter.Items.size(); i++ )
			{
				 foodIDS.add(Integer.toString(i));
			}
			 food_Adapter = new FoodListItemAdapter(
					 this, R.layout.cuisine_list_item, foodIDS);   // When the loading happens
			listViewToDisplay.setAdapter(food_Adapter);
        }
        /*
        else if(whichSearchChosen.equals("Home"))
        {
	        this.listViewToDisplay = (ListView)findViewById(R.id.myCuisineList);
			HomeListItemAdapter Home_Adapter = null; 
			 ArrayList<String> HomeIDS = new ArrayList<String>();
			 for(int i = 0; i < HomeListModelAdapter.Items.size(); i++ )
			{
				HomeIDS.add(Integer.toString(i));
			}
			 Home_Adapter = new HomeListItemAdapter(
					 this, R.layout.cuisine_list_item, HomeIDS);   // When the loading happens
			listViewToDisplay.setAdapter(Home_Adapter);
        }
        */
        
        else if(whichSearchChosen.equals("Electronics"))
        {
	        this.listViewToDisplay = (ListView)findViewById(R.id.myCuisineList);
			ElectronicsListItemAdapter Electronics_Adapter = null; 
			 ArrayList<String> ElectronicsIDS = new ArrayList<String>();
			 for(int i = 0; i < ElectronicsListModelAdapter.Items.size(); i++ )
			{
				ElectronicsIDS.add(Integer.toString(i));
			}
			 Electronics_Adapter = new ElectronicsListItemAdapter(
					 this, R.layout.cuisine_list_item, ElectronicsIDS);   // When the loading happens
			listViewToDisplay.setAdapter(Electronics_Adapter);
        }
        else if(whichSearchChosen.equals("Health & Beauty"))
        {
	        this.listViewToDisplay = (ListView)findViewById(R.id.myCuisineList);
			HealthListItemAdapter Health_Adapter = null; 
			 ArrayList<String> HealthIDS = new ArrayList<String>();
			 for(int i = 0; i < HealthListModelAdapter.Items.size(); i++ )
			{
				HealthIDS.add(Integer.toString(i));
			}
			 Health_Adapter = new HealthListItemAdapter(
					 this, R.layout.cuisine_list_item, HealthIDS);   // When the loading happens
			listViewToDisplay.setAdapter(Health_Adapter);
        }
        else if(whichSearchChosen.equals("Entertainment"))
        {
	        this.listViewToDisplay = (ListView)findViewById(R.id.myCuisineList);
			EntertainmentListItemAdapter Entertainment_Adapter = null; 
			 ArrayList<String> EntertainmentIDS = new ArrayList<String>();
			 for(int i = 0; i < EntertainmentListModelAdapter.Items.size(); i++ )
			{
				EntertainmentIDS.add(Integer.toString(i));
			}
			 Entertainment_Adapter = new EntertainmentListItemAdapter(
					 this, R.layout.cuisine_list_item, EntertainmentIDS);   // When the loading happens
			listViewToDisplay.setAdapter(Entertainment_Adapter);
        }
        
        else if(whichSearchChosen.equals("Services"))
        {
	        this.listViewToDisplay = (ListView)findViewById(R.id.myCuisineList);
			ServicesListItemAdapter Services_Adapter = null; 
			 ArrayList<String> ServicesIDS = new ArrayList<String>();
			 for(int i = 0; i < ServicesListModelAdapter.Items.size(); i++ )
			{
				ServicesIDS.add(Integer.toString(i));
			}
			 Services_Adapter = new ServicesListItemAdapter(
					 this, R.layout.cuisine_list_item, ServicesIDS);   // When the loading happens
			listViewToDisplay.setAdapter(Services_Adapter);
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
        	listViewToDisplay.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent,
						View view, int position, long id) {
					
					RetailListActivity activity = (RetailListActivity) parent.getTag(R.integer.activity);
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
