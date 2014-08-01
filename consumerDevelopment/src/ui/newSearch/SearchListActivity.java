package ui.newSearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ui.advanced_search.SelectedRestaurantActivity;
import ui.searchList.SearchListModelAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;



import com.google.gson.Gson;

import data.DataStorageManager;
import data.Deserialize;
import data.WebApiManagerPageFragment;
import framework.DataObject.AdvancedRestaurantSearchOrderHead;
import framework.DataObject.AdvancedRetailSearchOrderHead;
import framework.QoowayActivity;
import data.WebApiManager;

import com.qooway.consumerv01.R;

@SuppressLint("UseSparseArrays")
public class SearchListActivity extends QoowayActivity {
	public WebApiManager webApiManager;
	public WebApiManagerPageFragment webApiManagerPageFragment;
	public DataStorageManager dataStorageManager;
	public String whichSearchChosen;
	final Context context = this;

	public final int Zero = 0;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.Advanced);
		
        webApiManager = WebApiManager.getSingletonInstance();  
		dataStorageManager = DataStorageManager.getSingletonInstance();
		webApiManagerPageFragment = WebApiManagerPageFragment.getSingletonInstance();
		
		 Intent intent = getIntent();
		 whichSearchChosen = intent.getExtras().getString("whichScreen");
		 

		 
		 if(whichSearchChosen.equals("Restaurant")){
			 setContentView(R.layout.fragment_advanced_search_restaurant_home);
			 initializeRestaurantSearchMap();
		 } else{
			 setContentView(R.layout.fragment_advanced_search_retail_home);
			 initializeRetailSearchMap();
		 }		
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);  
		        
	}
	
	public void onStart(){
		super.onStart();
		if(whichSearchChosen.equals("Restaurant")){
			setUpRestaurant();
		}
		if(whichSearchChosen.equals("Retail")){
			setUpRetail();
		}
		
	}
	private void setUpRetail(){
		TextView apparelList = (TextView)findViewById(R.id.apparelList);
		if(dataStorageManager.apparel_map.containsValue(true)){
			String temp = "";
			for(int i = 0;i<dataStorageManager.ApparelList.size();i++){
				if(dataStorageManager.apparel_map.get(dataStorageManager.ApparelList.get(i))){
					temp = temp + dataStorageManager.ApparelList.get(i) +", ";
				}
			}
			String officialCuisine =temp.substring(0, temp.length()-2);
	    	apparelList.setText("Selected: " + officialCuisine);  	
	    	apparelList.setVisibility(0);
			
		} else{
			apparelList.setVisibility(View.GONE);
		}
		
		//
		TextView electronicsSelectedList = (TextView)findViewById(R.id.electronicsSelectedList);
		if(dataStorageManager.electronics_map.containsValue(true)){
			String temp = "";
			for(int i = 0;i<dataStorageManager.ElectronicsList.size();i++){
				if(dataStorageManager.electronics_map.get(dataStorageManager.ElectronicsList.get(i))){
					temp = temp + dataStorageManager.ElectronicsList.get(i) +", ";
				}
			}
			String officialCuisine =temp.substring(0, temp.length()-2);
			electronicsSelectedList.setText("Selected: " + officialCuisine);  	
			electronicsSelectedList.setVisibility(0);
			
		} else{
			electronicsSelectedList.setVisibility(View.GONE);
		}
		
		TextView entertainmentSelectedList = (TextView)findViewById(R.id.entertainmentSelectedList);
		if(dataStorageManager.entertainment_map.containsValue(true)){
			String temp = "";
			for(int i = 0;i<dataStorageManager.EntertainmentList.size();i++){
				if(dataStorageManager.entertainment_map.get(dataStorageManager.EntertainmentList.get(i))){
					temp = temp + dataStorageManager.EntertainmentList.get(i) +", ";
				}
			}
			String officialCuisine =temp.substring(0, temp.length()-2);
			entertainmentSelectedList.setText("Selected: " + officialCuisine);  	
			entertainmentSelectedList.setVisibility(0);
			
		} else{
			entertainmentSelectedList.setVisibility(View.GONE);
		}
		
		TextView foodSelectedList = (TextView)findViewById(R.id.foodSelectedList);
		if(dataStorageManager.food_map.containsValue(true)){
			String temp = "";
			for(int i = 0;i<dataStorageManager.FoodList.size();i++){
				if(dataStorageManager.food_map.get(dataStorageManager.FoodList.get(i))){
					temp = temp + dataStorageManager.FoodList.get(i) +", ";
				}
			}
			String officialCuisine =temp.substring(0, temp.length()-2);
			foodSelectedList.setText("Selected: " + officialCuisine);  	
			foodSelectedList.setVisibility(0);
			
		} else{
			foodSelectedList.setVisibility(View.GONE);
		}
		
		TextView healthSelectedList = (TextView)findViewById(R.id.healthSelectedList);
		if(dataStorageManager.health_map.containsValue(true)){
			String temp = "";
			for(int i = 0;i<dataStorageManager.HealthList.size();i++){
				if(dataStorageManager.health_map.get(dataStorageManager.HealthList.get(i))){
					temp = temp + dataStorageManager.HealthList.get(i) +", ";
				}
			}
			String officialCuisine =temp.substring(0, temp.length()-2);
			healthSelectedList.setText("Selected: " + officialCuisine);  	
			healthSelectedList.setVisibility(0);
			
		} else{
			healthSelectedList.setVisibility(View.GONE);
		}
		
		TextView homeSelectedList = (TextView)findViewById(R.id.homeSelectedList);
		if(dataStorageManager.home_map.containsValue(true)){
			String temp = "";
			for(int i = 0;i<dataStorageManager.HomeList.size();i++){
				if(dataStorageManager.home_map.get(dataStorageManager.HomeList.get(i))){
					temp = temp + dataStorageManager.HomeList.get(i) +", ";
				}
			}
			String officialCuisine =temp.substring(0, temp.length()-2);
			homeSelectedList.setText("Selected: " + officialCuisine);  	
			homeSelectedList.setVisibility(0);
			
		} else{
			homeSelectedList.setVisibility(View.GONE);
		}
		
		TextView serviceSelectedList = (TextView)findViewById(R.id.serviceSelectedList);
		if(dataStorageManager.service_map.containsValue(true)){
			String temp = "";
			for(int i = 0;i<dataStorageManager.ServicesList.size();i++){
				if(dataStorageManager.service_map.get(dataStorageManager.ServicesList.get(i))){
					temp = temp + dataStorageManager.ServicesList.get(i) +", ";
				}
			}
			String officialCuisine =temp.substring(0, temp.length()-2);
			serviceSelectedList.setText("Selected: " + officialCuisine);  	
			serviceSelectedList.setVisibility(0);
			
		} else{
			serviceSelectedList.setVisibility(View.GONE);
		}
		
		
		TextView NeighbourhoodList = (TextView)findViewById(R.id.NeighbourhoodselectedList);
		if(dataStorageManager.neighbourhood_map.containsValue(true)){
			String temp = "";
			for(int i = 0;i<dataStorageManager.Neighbourhood.size();i++){
				if(dataStorageManager.neighbourhood_map.get(dataStorageManager.Neighbourhood.get(i))){
					temp = temp + dataStorageManager.Neighbourhood.get(i) +", ";
				}
			}
			String officialCuisine =temp.substring(0, temp.length()-2);
	    	NeighbourhoodList.setText("Selected: " + officialCuisine);  	
	    	NeighbourhoodList.setVisibility(0);
			
		} else{
			NeighbourhoodList.setVisibility(View.GONE);
		}
		
		
		TextView sortBySelectedList = (TextView)findViewById(R.id.sortBySelectedList);
		if(dataStorageManager.sort_by_map.containsValue(true)){
			String temp = "";
			for(int i = 0;i<dataStorageManager.SortBy.size();i++){
				if(dataStorageManager.sort_by_map.get(dataStorageManager.SortBy.get(i))){
					temp = temp + dataStorageManager.SortBy.get(i) +", ";
				}
			}
			String officialCuisine =temp.substring(0, temp.length()-2);
			sortBySelectedList.setText("Selected: " + officialCuisine);  	
			sortBySelectedList.setVisibility(0);
			
		} else{
			sortBySelectedList.setVisibility(View.GONE);
		}
		
		
	}
	
	private void setUpRestaurant(){
		TextView cuisineList = (TextView)findViewById(R.id.selectedList);
		if(dataStorageManager.cuisine_map.containsValue(true)){
			String temp = "";
			for(int i = 0;i<dataStorageManager.Cuisine.size();i++){
				if(dataStorageManager.cuisine_map.get(dataStorageManager.Cuisine.get(i))){
					temp = temp + dataStorageManager.Cuisine.get(i) +", ";
				}
			}
			String officialCuisine =temp.substring(0, temp.length()-2);
	    	cuisineList.setText("Selected: " + officialCuisine);  	
	    	cuisineList.setVisibility(0);
			
		} else{
			cuisineList.setVisibility(View.GONE);
		}
		
		TextView NeighbourhoodList = (TextView)findViewById(R.id.NeighbourhoodselectedList);
		if(dataStorageManager.neighbourhood_map.containsValue(true)){
			String temp = "";
			for(int i = 0;i<dataStorageManager.Neighbourhood.size();i++){
				if(dataStorageManager.neighbourhood_map.get(dataStorageManager.Neighbourhood.get(i))){
					temp = temp + dataStorageManager.Neighbourhood.get(i) +", ";
				}
			}
			String officialCuisine =temp.substring(0, temp.length()-2);
	    	NeighbourhoodList.setText("Selected: " + officialCuisine);  	
	    	NeighbourhoodList.setVisibility(0);
			
		} else{
			NeighbourhoodList.setVisibility(View.GONE);
		}
		
		
		TextView restaurantTypeList = (TextView)findViewById(R.id.restaurantSelectedList);
		if(dataStorageManager.restaurant_type_map.containsValue(true)){
			String temp = "";
			for(int i = 0;i<dataStorageManager.RestaurantType.size();i++){
				if(dataStorageManager.restaurant_type_map.get(dataStorageManager.RestaurantType.get(i))){
					temp = temp + dataStorageManager.RestaurantType.get(i) +", ";
				}
			}
			String officialCuisine =temp.substring(0, temp.length()-2);
			restaurantTypeList.setText("Selected: " + officialCuisine);  	
			restaurantTypeList.setVisibility(0);
			
		} else{
			restaurantTypeList.setVisibility(View.GONE);
		}
		/*
		TextView optionSelectedList = (TextView)findViewById(R.id.optionSelectedList);
		if(dataStorageManager.option_map.containsValue(true)){
			String temp = "";
			for(int i = 0;i<dataStorageManager.Option.size();i++){
				if(dataStorageManager.option_map.get(dataStorageManager.Option.get(i))){
					temp = temp + dataStorageManager.Option.get(i) +", ";
				}
			}
			String officialCuisine =temp.substring(0, temp.length()-2);
			optionSelectedList.setText("Selected: " + officialCuisine);  	
			optionSelectedList.setVisibility(0);
			
		} else{
			optionSelectedList.setVisibility(View.GONE);
		}
		*/
		//
		TextView averagePriceSelectedList = (TextView)findViewById(R.id.averagePriceSelectedList);
		if(dataStorageManager.average_price_map.containsValue(true)){
			String temp = "";
			for(int i = 0;i<dataStorageManager.AveragePrice.size();i++){
				if(dataStorageManager.average_price_map.get(dataStorageManager.AveragePrice.get(i))){
					temp = temp + dataStorageManager.AveragePrice.get(i) +", ";
				}
			}
			String officialCuisine =temp.substring(0, temp.length()-2);
			averagePriceSelectedList.setText("Selected: " + officialCuisine);  	
			averagePriceSelectedList.setVisibility(0);
			
		} else{
			averagePriceSelectedList.setVisibility(View.GONE);
		}
		
		TextView sortBySelectedList = (TextView)findViewById(R.id.sortBySelectedList);
		if(dataStorageManager.sort_by_map.containsValue(true)){
			String temp = "";
			for(int i = 0;i<dataStorageManager.SortBy.size();i++){
				if(dataStorageManager.sort_by_map.get(dataStorageManager.SortBy.get(i))){
					temp = temp + dataStorageManager.SortBy.get(i) +", ";
				}
			}
			String officialCuisine =temp.substring(0, temp.length()-2);
			sortBySelectedList.setText("Selected: " + officialCuisine);  	
			sortBySelectedList.setVisibility(0);
			
		} else{
			sortBySelectedList.setVisibility(View.GONE);
		}
		
	}


	private void initializeRestaurantSearchMap() {
		initializeCuisine();
		intializeNeighbourhood();
		initializeRestaurantType();
		//initializeOptionType();
		initializeAveragePricePerPerson();
		initializeSortBy();
	}
	
	private void initializeRetailSearchMap(){
		initializeApparel();
		initializeElectronics();
		initializeEntertainment();
		initializeFood();
		initializeHealth();
		initializeHome();
		intializeNeighbourhood();
		initializeService();
		initializeSortBy();
	}
	
	private void initializeService(){
		/*
		String result ="";
		String retailSearchChosen = "Services";
	  	
	  	result = getRetailCategories(retailSearchChosen);
		Deserialize deserializer = new Deserialize();
		*/
		String retailSearchChosen = "Services";
	  	dataStorageManager.ServicesList = dataStorageManager.searchCategoryObject.RetailCategories.Services;
	  	dataStorageManager.service_map = new HashMap<String,Boolean>();
		
	  	for(int i = 0; i <dataStorageManager.ServicesList.size();i++){
	  		dataStorageManager.service_map.put(dataStorageManager.ServicesList.get(i), false);
	  	}
	  	TableRow ServiceRow = (TableRow)findViewById(R.id.ServiceRow);
	  	ServiceRow.setOnClickListener(new SearchActivityOnClickListener(dataStorageManager.ServicesList, retailSearchChosen));
	}
	
	
	private void initializeHome(){
		/*
		String result ="";
		String retailSearchChosen = "Home";
	  	
	  	result = getRetailCategories(retailSearchChosen);
		Deserialize deserializer = new Deserialize();
		*/
		String retailSearchChosen = "Home";
	  	dataStorageManager.HomeList = dataStorageManager.searchCategoryObject.RetailCategories.Home;
	  	dataStorageManager.home_map = new HashMap<String,Boolean>();
		
	  	for(int i = 0; i <dataStorageManager.HomeList.size();i++){
	  		dataStorageManager.home_map.put(dataStorageManager.HomeList.get(i), false);
	  	}
	  	
	  	TableRow HomeRow = (TableRow)findViewById(R.id.HomeRow);
	  	HomeRow.setOnClickListener(new SearchActivityOnClickListener(dataStorageManager.HomeList, retailSearchChosen));
	}
	
	private void initializeHealth(){
		/*
		String result ="";
		String retailSearchChosen = "Health & Beauty";
	  	
	  	result = getRetailCategories(retailSearchChosen);
		Deserialize deserializer = new Deserialize();
		*/
		String retailSearchChosen = "Health & Beauty";
	  	dataStorageManager.HealthList = dataStorageManager.searchCategoryObject.RetailCategories.HealthBeauty;
	  	dataStorageManager.health_map = new HashMap<String,Boolean>();
		
	  	for(int i = 0; i <dataStorageManager.HealthList.size();i++){
	  		dataStorageManager.health_map.put(dataStorageManager.HealthList.get(i), false);
	  	}
	  	
	  	TableRow HealthRow = (TableRow)findViewById(R.id.HealthRow);
	  	HealthRow.setOnClickListener(new SearchActivityOnClickListener(dataStorageManager.HealthList, retailSearchChosen));

	}
	
	private void initializeFood(){
		/*
		String result ="";
		String retailSearchChosen = "Food & Drink";
	  	
	  	result = getRetailCategories(retailSearchChosen);
		Deserialize deserializer = new Deserialize();
		*/
		String retailSearchChosen = "Food & Drink";
	  	dataStorageManager.FoodList = dataStorageManager.searchCategoryObject.RetailCategories.FoodDrink;
	  	dataStorageManager.food_map = new HashMap<String,Boolean>();
		
	  	for(int i = 0; i <dataStorageManager.FoodList.size();i++){
	  		dataStorageManager.food_map.put(dataStorageManager.FoodList.get(i), false);
	  	}
	  	
	  	TableRow FoodRow = (TableRow)findViewById(R.id.FoodRow);
	  	FoodRow.setOnClickListener(new SearchActivityOnClickListener(dataStorageManager.FoodList, retailSearchChosen));

	}
	
	private void initializeEntertainment(){
		/*
		String result ="";
		String retailSearchChosen = "Entertainment";
	  	
	  	result = getRetailCategories(retailSearchChosen);
		Deserialize deserializer = new Deserialize();
		*/
		String retailSearchChosen = "Entertainment";
	  	dataStorageManager.EntertainmentList = dataStorageManager.searchCategoryObject.RetailCategories.Entertainment;
	  	dataStorageManager.entertainment_map = new HashMap<String,Boolean>();
		
	  	for(int i = 0; i <dataStorageManager.EntertainmentList.size();i++){
	  		dataStorageManager.entertainment_map.put(dataStorageManager.EntertainmentList.get(i), false);
	  	}
	  	
	  	TableRow EntertainmentRow = (TableRow)findViewById(R.id.EntertainmentRow);
	  	EntertainmentRow.setOnClickListener(new SearchActivityOnClickListener(dataStorageManager.EntertainmentList, retailSearchChosen));
	}
	
	private void initializeApparel(){
		/*
		String result ="";
		String retailSearchChosen = "Apparel";
	  	
	  	result = getRetailCategories(retailSearchChosen);
		Deserialize deserializer = new Deserialize();
		*/
		String retailSearchChosen = "Apparel";
	  	dataStorageManager.ApparelList = dataStorageManager.searchCategoryObject.RetailCategories.Apparel;
	  	dataStorageManager.apparel_map = new HashMap<String,Boolean>();
		
	  	for(int i = 0; i <dataStorageManager.ApparelList.size();i++){
	  		dataStorageManager.apparel_map.put(dataStorageManager.ApparelList.get(i), false);
	  	}
	  	
	  	TableRow ApparelRow = (TableRow)findViewById(R.id.ApparelRow);
	  	ApparelRow.setOnClickListener(new SearchActivityOnClickListener(dataStorageManager.ApparelList, retailSearchChosen));
	}
	
	private void initializeElectronics(){
		/*
		String result ="";
		String retailSearchChosen = "Electronics";
	  	
	  	result = getRetailCategories(retailSearchChosen);
		Deserialize deserializer = new Deserialize();
		*/
		String retailSearchChosen = "Electronics";
	  	dataStorageManager.ElectronicsList = dataStorageManager.searchCategoryObject.RetailCategories.Electronics;
	  	dataStorageManager.electronics_map = new HashMap<String,Boolean>();
		
	  	for(int i = 0; i <dataStorageManager.ElectronicsList.size();i++){
	  		dataStorageManager.electronics_map.put(dataStorageManager.ElectronicsList.get(i), false);
	  	}
	  	
	  	TableRow ElectronicsRow = (TableRow)findViewById(R.id.ElectronicsRow);
	  	ElectronicsRow.setOnClickListener(new SearchActivityOnClickListener(dataStorageManager.ElectronicsList, retailSearchChosen));
	}
	
	public String getRetailCategories(String category)
	  {
		  String result="";
		  if(category.equals("Food & Drink")){
			  category="Food%20%26%20Drink";
		  }
		  else if(category.equals("Health & Beauty")){
			  category ="Health%20%26%20Beauty";
		  }
		  
		  try {
			result = webApiManager.getRetailCategories(category);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  return result;
	  }
	
	public void searchRetailClicked(View V){
		ArrayList<String> neighbourhood = new ArrayList<String>();
		if(dataStorageManager.neighbourhood_map.containsValue(true)){
			for(int i = 0; i < dataStorageManager.Neighbourhood.size();i++){
				if(dataStorageManager.neighbourhood_map.get(dataStorageManager.Neighbourhood.get(i))){
					neighbourhood.add(dataStorageManager.Neighbourhood.get(i));
				}
			}
		}
		
		ArrayList<String> Apparel =  new ArrayList<String>();
		if(dataStorageManager.apparel_map.containsValue(true)){
			for(int i = 0 ; i < dataStorageManager.ApparelList.size();i++){
				if(dataStorageManager.apparel_map.get(dataStorageManager.ApparelList.get(i))){
					Apparel.add(dataStorageManager.ApparelList.get(i));
				}
			}
		}
		
		ArrayList<String> Electronics =  new ArrayList<String>();
		if(dataStorageManager.electronics_map.containsValue(true)){
			for(int i = 0 ; i < dataStorageManager.ElectronicsList.size();i++){
				if(dataStorageManager.electronics_map.get(dataStorageManager.ElectronicsList.get(i))){
					Electronics.add(dataStorageManager.ElectronicsList.get(i));
				}
			}
		}
		
		ArrayList<String> Entertainment =  new ArrayList<String>();
		if(dataStorageManager.entertainment_map.containsValue(true)){
			for(int i = 0 ; i < dataStorageManager.EntertainmentList.size();i++){
				if(dataStorageManager.entertainment_map.get(dataStorageManager.EntertainmentList.get(i))){
					Entertainment.add(dataStorageManager.EntertainmentList.get(i));
				}
			}
		}
		
		ArrayList<String> Food =  new ArrayList<String>();
		if(dataStorageManager.food_map.containsValue(true)){
			for(int i = 0 ; i < dataStorageManager.FoodList.size();i++){
				if(dataStorageManager.food_map.get(dataStorageManager.FoodList.get(i))){
					Entertainment.add(dataStorageManager.FoodList.get(i));
				}
			}
		}
		
		ArrayList<String> Health =  new ArrayList<String>();
		if(dataStorageManager.health_map.containsValue(true)){
			for(int i = 0 ; i < dataStorageManager.HealthList.size();i++){
				if(dataStorageManager.health_map.get(dataStorageManager.HealthList.get(i))){
					Health.add(dataStorageManager.HealthList.get(i));
				}
			}
		}
		
		ArrayList<String> Home =  new ArrayList<String>();
		if(dataStorageManager.home_map.containsValue(true)){
			for(int i = 0 ; i < dataStorageManager.HomeList.size();i++){
				if(dataStorageManager.home_map.get(dataStorageManager.HomeList.get(i))){
					Home.add(dataStorageManager.HomeList.get(i));
				}
			}
		}
		
		ArrayList<String> Service =  new ArrayList<String>();
		if(dataStorageManager.service_map.containsValue(true)){
			for(int i = 0 ; i < dataStorageManager.ServicesList.size();i++){
				if(dataStorageManager.service_map.get(dataStorageManager.ServicesList.get(i))){
					Service.add(dataStorageManager.ServicesList.get(i));
				}
			}
		}
		
		
		String Order="";
		String SortBy="";
		
		
		if(dataStorageManager.sort_by_map.containsValue(true)){
    		
    		if(dataStorageManager.sort_by_map.get("Name: A-Z")){
    			Order ="1";
    			SortBy="0";
    		}
    		else if(dataStorageManager.sort_by_map.get("Name: Z-A"))
			{
				Order = "0";
				SortBy = "0";
			}
			
			else if(dataStorageManager.sort_by_map.get("Price: $-$$$$"))
			{
				Order = "1";
				SortBy = "2";
			}
			else if(dataStorageManager.sort_by_map.get("Price: $$$$-$"))
			{
				Order = "0";
				SortBy = "2";
			}
			
			else if(dataStorageManager.sort_by_map.get("Rating: 0-10"))
			{
				Order ="1";
				SortBy ="1";
			}
			else if(dataStorageManager.sort_by_map.get("Rating: 10-0"))
			{
				Order = "0";
				SortBy ="1";
			}
    	}
		
		ArrayList<String> totalArray = new ArrayList<String>();
		totalArray.addAll(Apparel);
		totalArray.addAll(Electronics);
		totalArray.addAll(Entertainment);
		totalArray.addAll(Food);
		totalArray.addAll(Health);
		totalArray.addAll(Home);
		totalArray.addAll(Service);
		this.insertRecord(totalArray,neighbourhood,SortBy,Order);

	}
		
		private void insertRecord(ArrayList<String> concatedArrray, ArrayList<String>neighbourhood,String sortBy, String order) {
			
			whichSearchChosen = "searchClicked";
			
			AdvancedRetailSearchOrderHead tran = new AdvancedRetailSearchOrderHead(concatedArrray,neighbourhood,sortBy,order);
			Gson gson= new Gson();
			String json ="";
			String result="";
			try{
				json = gson.toJson(tran);
				result = this.webApiManager.postSearchRetail(json);
			}catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(result != null){
				Deserialize deserializer = new Deserialize();
				dataStorageManager.SearchList = deserializer.getMerchantList(result);
				
				SearchListModelAdapter.LoadModel(dataStorageManager.SearchList);

				 Intent i = new Intent(this, CategorySearchListActivity.class);

				  i.putExtra("whichSearchChosen", whichSearchChosen);
				   startActivity(i);
				} else {
					Toast.makeText(context, "Please make more selections", Toast.LENGTH_SHORT).show();
				}			
			


		
		}
	
	
	
	public void SearchRestaurantButton(View v){
		ArrayList<String> cuisine = new ArrayList<String>();
		if(dataStorageManager.cuisine_map.containsValue(true)){
			for(int i = 0; i < dataStorageManager.Cuisine.size(); i++){
				if(dataStorageManager.cuisine_map.get(dataStorageManager.Cuisine.get(i))){
					cuisine.add(dataStorageManager.Cuisine.get(i));
				}
			}
		}
		ArrayList<String> restaurant = new ArrayList<String>();
		if(dataStorageManager.restaurant_type_map.containsValue(true)){
			for(int i = 0; i < dataStorageManager.RestaurantType.size();i++){
				if(dataStorageManager.restaurant_type_map.get(dataStorageManager.RestaurantType.get(i))){
					restaurant.add(dataStorageManager.RestaurantType.get(i));
				}
			}
		}
		ArrayList<String> neighbourhood = new ArrayList<String>();
		if(dataStorageManager.neighbourhood_map.containsValue(true)){
			for(int i = 0; i < dataStorageManager.Neighbourhood.size();i++){
				if(dataStorageManager.neighbourhood_map.get(dataStorageManager.Neighbourhood.get(i))){
					neighbourhood.add(dataStorageManager.Neighbourhood.get(i));
				}
			}
		}
		
		
		String TakeOut = "";
    	String Delivery = "";
    	String Price = "";
    	String SortBy = "";
    	String Order =  "";
    	
    	/*
    	if(dataStorageManager.option_map.containsValue(true)){   // contains delivery or takeout
    		if(dataStorageManager.option_map.get("Takeout")){
    			TakeOut = "Y";
    		}
    		if(dataStorageManager.option_map.get("Delivery")){
    			Delivery="Y";
    		}
    	}
    	*/
    	
    	if(dataStorageManager.average_price_map.containsValue(true)){  // contains average price
    		if(dataStorageManager.average_price_map.get("$(<15)")){
    			Price = "0";
    		}
    		else if (dataStorageManager.average_price_map.get("$$(15-25)")){
    			Price = "1";
    		}
    		else if (dataStorageManager.average_price_map.get("$$$(25-40)")){
    			Price ="2";
    		}
    		else if (dataStorageManager.average_price_map.get("$$$$(40+)")){
    			Price="3";
    		}
    	}
    	
    	if(dataStorageManager.sort_by_map.containsValue(true)){
    		
    		if(dataStorageManager.sort_by_map.get("Name: A-Z")){
    			Order ="1";
    			SortBy="0";
    		}
    		else if(dataStorageManager.sort_by_map.get("Name: Z-A"))
			{
				Order = "0";
				SortBy = "0";
			}
			
			else if(dataStorageManager.sort_by_map.get("Price: $-$$$$"))
			{
				Order = "1";
				SortBy = "2";
			}
			else if(dataStorageManager.sort_by_map.get("Price: $$$$-$"))
			{
				Order = "0";
				SortBy = "2";
			}
			
			else if(dataStorageManager.sort_by_map.get("Rating: 0-10"))
			{
				Order ="1";
				SortBy ="1";
			}
			else if(dataStorageManager.sort_by_map.get("Rating: 10-0"))
			{
				Order = "0";
				SortBy ="1";
			}
    	}
		//
		//String searchChosen = "searchClicked";
		//
    	//
		AdvancedRestaurantSearchOrderHead tran = new AdvancedRestaurantSearchOrderHead(restaurant,cuisine,neighbourhood,TakeOut,Delivery,Price,SortBy,Order);
		Gson gson= new Gson();
		String json ="";
		String result="";
		try{
			json = gson.toJson(tran);
			result = this.webApiManager.postSearchRestaurant(json);
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		if(result != null){
			Deserialize deserializer = new Deserialize();
			dataStorageManager.SearchList = deserializer.getMerchantList(result);
			
			SearchListModelAdapter.LoadModel(dataStorageManager.SearchList);
			
			
			Intent i = new Intent(this, SelectedRestaurantActivity.class);
			i.putExtra("whichSearchChosen", whichSearchChosen);
			 //   i.putExtra("merchantType", mainActivity.selectedMerchant.MerchantType);
			startActivity(i);
		} else {
			Toast.makeText(context, "Please choose at least two selections", Toast.LENGTH_SHORT).show();
		}
    	
    	
    	//this.insertRecord;
	}
		/*
		private void insertRecord(ArrayList<String> restaurant, ArrayList<String> cuisine,
				String takeOut, String delivery, String price, String sortBy,
				String order) {
			

			

		}
	*/
	public void initializeCuisine(){
		/*
		String result ="";
		try {
			result = webApiManager.getCuisine();  
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Deserialize deserializer = new Deserialize();
		dataStorageManager.Cuisine = deserializer.getStringList(result);
		*/
		dataStorageManager.Cuisine = dataStorageManager.searchCategoryObject.RestaurantCategories.Cuisine;
		
		dataStorageManager.cuisine_map = new HashMap<String, Boolean>();
		for(int i = 0; i<dataStorageManager.Cuisine.size() ; i++){
			dataStorageManager.cuisine_map.put(dataStorageManager.Cuisine.get(i), false);
		}
		
		TableRow CuisineRow = (TableRow)findViewById(R.id.CuisineRow);
		
		CuisineRow.setOnClickListener(new SearchActivityOnClickListener(dataStorageManager.Cuisine, "cuisine"));
		
	
	}
	public void intializeNeighbourhood(){
		/*
		String result ="";
		try{
			result = webApiManager.getNeighbourhood();
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Deserialize deserializer = new Deserialize();
		*/
		dataStorageManager.Neighbourhood = dataStorageManager.searchCategoryObject.RestaurantCategories.Neighbourhood;
		dataStorageManager.neighbourhood_map = new HashMap<String, Boolean>();
		for(int i = 0; i<dataStorageManager.Neighbourhood.size();i++){
			dataStorageManager.neighbourhood_map.put(dataStorageManager.Neighbourhood.get(i), false);
		}
		TableRow NeighbourhoodRow = (TableRow)findViewById(R.id.NeighbourhoodRow);
		NeighbourhoodRow.setOnClickListener(new SearchActivityOnClickListener(dataStorageManager.Neighbourhood, "neighbourhood"));
	}
	
	public void initializeRestaurantType(){
		/*
		String result = "";
		
		try {
			result = webApiManager.getRestaurantType();  
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Deserialize deserializer = new Deserialize();
		*/
		dataStorageManager.RestaurantType = dataStorageManager.searchCategoryObject.RestaurantCategories.RestaurantType;
		dataStorageManager.restaurant_type_map = new HashMap<String,Boolean>();
		
		for(int i =0; i<dataStorageManager.RestaurantType.size();i++){
			dataStorageManager.restaurant_type_map.put(dataStorageManager.RestaurantType.get(i), false);
		}

		TableRow RestaurantTypeRow = (TableRow)findViewById(R.id.RestaurantTypeRow);

		RestaurantTypeRow.setOnClickListener(new SearchActivityOnClickListener(dataStorageManager.RestaurantType, "restaurantType"));


	}
	
	/*
	public void initializeOptionType(){
		dataStorageManager.Option = new ArrayList<String>();
		dataStorageManager.Option.add("Takeout");
		dataStorageManager.Option.add("Delivery");
		dataStorageManager.option_map = new HashMap<String,Boolean>();
		
		for(int i =0; i<dataStorageManager.Option.size();i++){
			dataStorageManager.option_map.put(dataStorageManager.Option.get(i), false);
		}
	 
	    
	    TableRow OptionRow = (TableRow)findViewById(R.id.OptionRow);
	    OptionRow.setOnClickListener(new SearchActivityOnClickListener(dataStorageManager.Option, "option"));

	}
	*/
	
	public void initializeAveragePricePerPerson(){
		dataStorageManager.AveragePrice = new ArrayList<String>();
		dataStorageManager.AveragePrice.add("$(<15)"); 
		dataStorageManager.AveragePrice.add("$$(15-25)");  
		dataStorageManager.AveragePrice.add("$$$(25-40)"); 
		dataStorageManager.AveragePrice.add("$$$$(40+)");  
		dataStorageManager.average_price_map = new HashMap<String,Boolean>();
		
		for(int i =0; i<dataStorageManager.AveragePrice.size();i++){
			dataStorageManager.average_price_map.put(dataStorageManager.AveragePrice.get(i), false);
		}
		
		TableRow AveragePriceRow = (TableRow)findViewById(R.id.AveragePriceRow);
		AveragePriceRow.setOnClickListener(new SearchActivityOnClickListener(dataStorageManager.AveragePrice, "price"));

	}
	
	public void initializeSortBy(){
		
		dataStorageManager.SortBy = new ArrayList<String>();
		dataStorageManager.SortBy.add("Name: A-Z");
		dataStorageManager.SortBy.add("Name: Z-A");
		dataStorageManager.SortBy.add("Price: $-$$$$");
		dataStorageManager.SortBy.add("Price: $$$$-$");
		dataStorageManager.SortBy.add("Rating: 0-10");
		dataStorageManager.SortBy.add("Rating: 10-0");
		dataStorageManager.sort_by_map = new HashMap<String, Boolean>();
		
		for(int i =0; i<dataStorageManager.SortBy.size();i++){
			if(i == 0){
				dataStorageManager.sort_by_map.put(dataStorageManager.SortBy.get(i), true);
			} else{
				dataStorageManager.sort_by_map.put(dataStorageManager.SortBy.get(i), false);
			}
		
		}

		
		TableRow SortByRow = (TableRow)findViewById(R.id.SortByRow);
		SortByRow.setOnClickListener(new SearchActivityOnClickListener(dataStorageManager.SortBy, "sort"));
	}
	
	public void startNewActivity(String searchChosen){
		Intent i = new Intent(this, CategorySearchListActivity.class);	  
		  i.putExtra("whichSearchChosen", searchChosen);
		 //   i.putExtra("merchantType", mainActivity.selectedMerchant.MerchantType);
		    startActivity(i);
	}
	

	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
         inflater.inflate(R.menu.menu_search, menu);
         
         return super.onCreateOptionsMenu(menu);
    }    	
	
	  
	  @Override
	  public boolean onOptionsItemSelected(MenuItem menuItem)
	  {       
	      switch (menuItem.getItemId()) {
	      // Respond to the action bar's Up/Home button
	      case android.R.id.home:
	          //NavUtils.navigateUpFromSameTask(this);
	          finish();
	          return true;
	    
	      /*    
	      case R.id.search:
	    	  View view = new View (context);

	  		if(whichSearchChosen.equals("Restaurant")){
	  			SearchRestaurantButton(view);
	  			return true;
			}
	  		else if(whichSearchChosen.equals("Retail")){
				searchRetailClicked(view);
				return true;
			}	    	  
	    	  

	    	  break;
	    	  
	    */	  
	    	  
	      default:
	    	  break;
	      }
	      
	      return true;
	  } 
	
	  public class SearchActivityOnClickListener implements OnClickListener
	   {
	     List<String> subCategory;
	     String searchOption;

	     public SearchActivityOnClickListener(List<String> subCategory , String searchOption) {
	          this.subCategory = subCategory;
	          this.searchOption = searchOption;

	     }

	     @Override
	     public void onClick(View v)
	     {
	    	 if(!SearchListActivity.this.getIsLoading())
	    	 {
	    		 SearchListActivity.this.setIsLoading(true);
				 CategoryListModelAdapter.LoadModel(subCategory);
				 startNewActivity(searchOption);
	    	 }

	     }

	  };
}

	
