package ui.newSearch;

import java.util.ArrayList;

import ui.MerchantDetailActivity;
import ui.searchList.SearchListItemAdapter;
import ui.searchList.SearchListModelAdapter;


import data.DataStorageManager;
import framework.QoowayActivity;
import data.WebApiManager;
import com.qooway.consumerv01.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class CategorySearchListActivity extends QoowayActivity{
	
	public String whichSearchChosen="";
	public ListView listViewToDisplay;
	public ArrayList<String> categoryIDS;
	public WebApiManager webApiManager;
	public DataStorageManager dataStorageManager;
	public String choiceMode;

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("");
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getActionBar().setDisplayHomeAsUpEnabled(false);
	    getActionBar().setHomeButtonEnabled(false);
	    webApiManager = WebApiManager.getSingletonInstance();  
		dataStorageManager = DataStorageManager.getSingletonInstance();
	    
		Intent intent = getIntent();
	    whichSearchChosen = intent.getExtras().getString("whichSearchChosen");
	    
	    if(whichSearchChosen.equals("searchClicked"))
	    {
	       setContentView(R.layout.fragment_advanced_search);
	    }
	    else{
	    	setContentView(R.layout.fragment_cuisine);
	    }
	    
	    this.listViewToDisplay = (ListView)findViewById(R.id.myCuisineList);
	    CategoryListItemAdapter category_adapter = null;
	    categoryIDS = new ArrayList<String>();
	    
	    if(whichSearchChosen.equals("cuisine"))
        {
	    	choiceMode = "MultipleChoice";
			 for(int i = 0; i < CategoryListModelAdapter.Items.size(); i++ )
			{
				 categoryIDS.add(Integer.toString(i));
			}
			 category_adapter = new CategoryListItemAdapter(
					 this, R.layout.cuisine_list_item, categoryIDS,dataStorageManager.cuisine_map,choiceMode);   // When the loading happens
			listViewToDisplay.setAdapter(category_adapter);    	
			
        }
	    
	    else if(whichSearchChosen.equals("neighbourhood"))
        {
	    	choiceMode = "MultipleChoice";
			 for(int i = 0; i < CategoryListModelAdapter.Items.size(); i++ )
			{
				 categoryIDS.add(Integer.toString(i));
			}
			 category_adapter = new CategoryListItemAdapter(
					 this, R.layout.cuisine_list_item, categoryIDS,dataStorageManager.neighbourhood_map,choiceMode);   // When the loading happens
			listViewToDisplay.setAdapter(category_adapter);    	
			
        }
	    
	    else if(whichSearchChosen.equals("restaurantType")){
	    	choiceMode = "MultipleChoice";
			for(int i = 0; i < CategoryListModelAdapter.Items.size(); i++ )
			{
				 categoryIDS.add(Integer.toString(i));
			}
			 category_adapter = new CategoryListItemAdapter(
					 this, R.layout.cuisine_list_item, categoryIDS,dataStorageManager.restaurant_type_map,choiceMode);   // When the loading happens
			listViewToDisplay.setAdapter(category_adapter);
	    	
	    }
	    
	    else if(whichSearchChosen.equals("option")){
	    	choiceMode = "MultipleChoice";
			for(int i = 0; i < CategoryListModelAdapter.Items.size(); i++ )
			{
				 categoryIDS.add(Integer.toString(i));
			}
			 category_adapter = new CategoryListItemAdapter(
					 this, R.layout.cuisine_list_item, categoryIDS,dataStorageManager.option_map,choiceMode);   // When the loading happens
			listViewToDisplay.setAdapter(category_adapter);
	    	
	    }
	    else if(whichSearchChosen.equals("price")){
	    	choiceMode = "SingleChoice";
			for(int i = 0; i < CategoryListModelAdapter.Items.size(); i++ )
			{
				 categoryIDS.add(Integer.toString(i));
			}
			 category_adapter = new CategoryListItemAdapter(
					 this, R.layout.cuisine_list_item, categoryIDS,dataStorageManager.average_price_map,choiceMode);   // When the loading happens
			listViewToDisplay.setAdapter(category_adapter);
	    	
	    }
	    else if(whichSearchChosen.equals("sort")){
	    	choiceMode = "MustSelectOne";
			for(int i = 0; i < CategoryListModelAdapter.Items.size(); i++ )
			{
				 categoryIDS.add(Integer.toString(i));
			}
			 category_adapter = new CategoryListItemAdapter(
					 this, R.layout.cuisine_list_item, categoryIDS,dataStorageManager.sort_by_map,choiceMode);   // When the loading happens
			listViewToDisplay.setAdapter(category_adapter);
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
					
					CategorySearchListActivity activity = (CategorySearchListActivity) parent.getTag(R.integer.activity);
					DataStorageManager.getSingletonInstance().selectedMerchant = dataStorageManager.SearchList
							.get(position);
					 
					Intent i = new Intent(activity, MerchantDetailActivity.class);
					startActivity(i);	
				}
			});
        }
	    else if(whichSearchChosen.equals("Apparel")){
	    	choiceMode = "MultipleChoice";
			 for(int i = 0; i < CategoryListModelAdapter.Items.size(); i++ )
			{
				 categoryIDS.add(Integer.toString(i));
			}
			 category_adapter = new CategoryListItemAdapter(
					 this, R.layout.cuisine_list_item, categoryIDS,dataStorageManager.apparel_map,choiceMode);   // When the loading happens
			listViewToDisplay.setAdapter(category_adapter);    	
	    	
	    }
	    else if(whichSearchChosen.equals("Electronics")){
	    	choiceMode = "MultipleChoice";
			 for(int i = 0; i < CategoryListModelAdapter.Items.size(); i++ )
			{
				 categoryIDS.add(Integer.toString(i));
			}
			 category_adapter = new CategoryListItemAdapter(
					 this, R.layout.cuisine_list_item, categoryIDS,dataStorageManager.electronics_map,choiceMode);   // When the loading happens
			listViewToDisplay.setAdapter(category_adapter);    	
	    }
	    else if(whichSearchChosen.equals("Entertainment")){
	    	choiceMode = "MultipleChoice";
			 for(int i = 0; i < CategoryListModelAdapter.Items.size(); i++ )
			{
				 categoryIDS.add(Integer.toString(i));
			}
			 category_adapter = new CategoryListItemAdapter(
					 this, R.layout.cuisine_list_item, categoryIDS,dataStorageManager.entertainment_map,choiceMode);   // When the loading happens
			listViewToDisplay.setAdapter(category_adapter);    	
	    }
		else if(whichSearchChosen.equals("Food & Drink")){
			choiceMode = "MultipleChoice";
			 for(int i = 0; i < CategoryListModelAdapter.Items.size(); i++ )
			{
				 categoryIDS.add(Integer.toString(i));
			}
			 category_adapter = new CategoryListItemAdapter(
					 this, R.layout.cuisine_list_item, categoryIDS,dataStorageManager.food_map,choiceMode);   // When the loading happens
			listViewToDisplay.setAdapter(category_adapter);    	
		}
		else if(whichSearchChosen.equals("Health & Beauty")){
			choiceMode = "MultipleChoice";
			 for(int i = 0; i < CategoryListModelAdapter.Items.size(); i++ )
			{
				 categoryIDS.add(Integer.toString(i));
			}
			 category_adapter = new CategoryListItemAdapter(
					 this, R.layout.cuisine_list_item, categoryIDS,dataStorageManager.health_map,choiceMode);   // When the loading happens
			listViewToDisplay.setAdapter(category_adapter);    	
		}
		else if(whichSearchChosen.equals("Home")){
			choiceMode = "MultipleChoice";
			 for(int i = 0; i < CategoryListModelAdapter.Items.size(); i++ )
			{
				 categoryIDS.add(Integer.toString(i));
			}
			 category_adapter = new CategoryListItemAdapter(
					 this, R.layout.cuisine_list_item, categoryIDS,dataStorageManager.home_map,choiceMode);   // When the loading happens
			listViewToDisplay.setAdapter(category_adapter);    	
		}
		else if(whichSearchChosen.equals("Services")){
			choiceMode = "MultipleChoice";
			 for(int i = 0; i < CategoryListModelAdapter.Items.size(); i++ )
			{
				 categoryIDS.add(Integer.toString(i));
			}
			 category_adapter = new CategoryListItemAdapter(
					 this, R.layout.cuisine_list_item, categoryIDS,dataStorageManager.service_map,choiceMode);   // When the loading happens
			listViewToDisplay.setAdapter(category_adapter);    	
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
