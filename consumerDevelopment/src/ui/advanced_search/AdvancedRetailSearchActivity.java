package ui.advanced_search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.qooway.consumerv01.R;

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
import ui.sortBy.SortByListItemAdapter;
import ui.sortBy.SortByListModelAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import data.Deserialize;
import data.WebApiManager;
import data.DataStorageManager;

public class AdvancedRetailSearchActivity extends QoowayActivity{
	
	final Context context = this;
	public String httpserverUrl = "online.profitek.com/testingAPi";
	public String httpsserverUrl = "online.profitek.com/testingAPi";	
	public WebApiManager webApiManager;
	public DataStorageManager dataStorageManager;
  	String SortBy = "";
  	String Order =  "";
	
	
	

	public static boolean FirstApparel;  // Used to determine if first time clicking apparel ( if true , set all items to 0's)
	public static ArrayList<Integer> ApparelSelectedList;   // hold 1 if selected and 0 if not selected
	
	
	public static boolean FirstFood;
	public static ArrayList<Integer> FoodSelectedList;

	
	public static boolean FirstHome;
	public static ArrayList<Integer> HomeSelectedList;
	
	
	public static boolean FirstElectronics;
	public static ArrayList<Integer> ElectronicsSelectedList;
	
	
	public static boolean FirstHealth;
	public static ArrayList<Integer> HealthSelectedList;
	
	
	public static boolean FirstEntertainment;
	public static ArrayList<Integer> EntertainmentSelectedList;
	
	
	public static boolean FirstServices;
	public static ArrayList<Integer> ServicesSelectedList;
	
	
	public static boolean FirstSortBy;
	public List<String> hardCodedSortByItem;
	
	
	public String whichSearchChosen;
	

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
         inflater.inflate(R.menu.menu_search, menu);
         
         return super.onCreateOptionsMenu(menu);
    }  
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.Advanced);
		setContentView(R.layout.fragment_advanced_search_retail_home);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);  
        getActionBar().setDisplayHomeAsUpEnabled(true);
	    
        
        webApiManager = WebApiManager.getSingletonInstance();
		dataStorageManager = DataStorageManager.getSingletonInstance();
        
	    FirstApparel = true;
	    ApparelListItemAdapter.ApparelList = new ArrayList<String>();
	    ApparelSelectedList = new ArrayList<Integer>();
	    
	   
	    
	    FirstFood = true;
	    FoodListItemAdapter.FoodList = new ArrayList<String>();
	    FoodSelectedList = new ArrayList<Integer>();
	   
	    
	    FirstHome = true;
	   // HomeListItemAdapter.HomeList = new ArrayList<String>();
	    HomeSelectedList = new ArrayList<Integer>();
	    

	    FirstElectronics = true;
	    ElectronicsListItemAdapter.ElectronicsList = new ArrayList<String>();
	    ElectronicsSelectedList = new ArrayList<Integer>();
	   
	    
	    FirstHealth= true;
	    HealthListItemAdapter.HealthList = new ArrayList<String>();
	    HealthSelectedList = new ArrayList<Integer>();
	   
	    
	    FirstEntertainment= true;
	    EntertainmentListItemAdapter.EntertainmentList = new ArrayList<String>();
	    EntertainmentSelectedList = new ArrayList<Integer>();
	    
	    
	    
	    FirstServices= true;
	    ServicesListItemAdapter.ServicesList = new ArrayList<String>();
	    ServicesSelectedList = new ArrayList<Integer>();
	    
	    
	    
	    
		FirstSortBy = true;
		SortByListItemAdapter.SortByList = new ArrayList<String>();
		AdvancedRestaurantSearchActivity.sortBySelectedList = new ArrayList<Integer>();   // have to do this in order to use same class ( in SortBy List Item Adapter )
		
		// INITIALIZE so that "Name: A-Z" is pre-selected and shown
		AdvancedRestaurantSearchActivity.sortBySelectedList.add(0,1);  
		SortByListItemAdapter.SortByList.add(0,"Name: A-Z");
		
		hardCodedSortByItem = new ArrayList<String>();
		hardCodedSortByItem.add("Name: A-Z");
		hardCodedSortByItem.add("Name: Z-A");
		hardCodedSortByItem.add("Price: $-$$$$");
	    hardCodedSortByItem.add("Price: $$$$-$");
		hardCodedSortByItem.add("Rating: 0-10");
		hardCodedSortByItem.add("Rating: 10-0");

	}
	
	// USED TO DISPLAY SELECTED ITEMS
	// Called After Create
	protected void onStart() {
		super.onStart();
	
			TextView apparelList = (TextView)findViewById(R.id.apparelList);		 	
			if(ApparelSelectedList.contains(1))  // If there are any 1's than show selection
		     { 
			 String temp =ApparelListItemAdapter.ApparelList.toString();
			 temp = temp.substring(1,temp.length()-1);  // cut off the brackets from toString method
			 apparelList.setText("Selected: " + temp);  	
			 apparelList.setVisibility(0);
		     }
			    
			 else{
			 
				 apparelList.setVisibility(View.GONE);
			 }
			
			
			TextView foodSelectedList = (TextView)findViewById(R.id.foodSelectedList);
			if(FoodSelectedList.contains(1))  // If there are any 1's than show selection
		     {  	
			
			 String temp =FoodListItemAdapter.FoodList.toString();
			 temp = temp.substring(1,temp.length()-1);  // cut off the brackets from toString method
			 foodSelectedList.setText("Selected: " + temp);  	
			 foodSelectedList.setVisibility(0);
		     }
			    
			 else{
			 
				 foodSelectedList.setVisibility(View.GONE);
			 }
			
			//TextView homeSelectedList = (TextView)findViewById(R.id.homeSelectedList);
			/*
			if(HomeSelectedList.contains(1))  // If there are any 1's than show selection
		     {  	
			
			 String temp =HomeListItemAdapter.HomeList.toString();
			 temp = temp.substring(1,temp.length()-1);  // cut off the brackets from toString method
			 homeSelectedList.setText("Selected: " + temp);  	
			 homeSelectedList.setVisibility(0);
		     }
			    
			 else{
			 
				 homeSelectedList.setVisibility(View.GONE);
			 }
			*/
			TextView electronicsSelectedList = (TextView)findViewById(R.id.electronicsSelectedList);
			if(ElectronicsSelectedList.contains(1))  // If there are any 1's than show selection
		     {  	
			
			 String temp =ElectronicsListItemAdapter.ElectronicsList.toString();
			 temp = temp.substring(1,temp.length()-1);  // cut off the brackets from toString method
			 electronicsSelectedList.setText("Selected: " + temp);  	
			 electronicsSelectedList.setVisibility(0);
		     }
			    
			 else{
			 
				 electronicsSelectedList.setVisibility(View.GONE);
			 }
			
			TextView healthSelectedList = (TextView)findViewById(R.id.healthSelectedList);
			if(HealthSelectedList.contains(1))  // If there are any 1's than show selection
		     {  	
			
			 String temp =HealthListItemAdapter.HealthList.toString();
			 temp = temp.substring(1,temp.length()-1);  // cut off the brackets from toString method
			 healthSelectedList.setText("Selected: " + temp);  	
			 healthSelectedList.setVisibility(0);
		     }
			    
			 else{
			 
				 healthSelectedList.setVisibility(View.GONE);
			 }
			
			TextView entertainmentSelectedList = (TextView)findViewById(R.id.entertainmentSelectedList);
			if(EntertainmentSelectedList.contains(1))  // If there are any 1's than show selection
		     {  	
			
			 String temp =EntertainmentListItemAdapter.EntertainmentList.toString();
			 temp = temp.substring(1,temp.length()-1);  // cut off the brackets from toString method
			 entertainmentSelectedList.setText("Selected: " + temp);  	
			 entertainmentSelectedList.setVisibility(0);
		     }
			    
			 else{
			 
				 entertainmentSelectedList.setVisibility(View.GONE);
			 }
			
			TextView servicestSelectedList = (TextView)findViewById(R.id.serviceSelectedList);
			if(ServicesSelectedList.contains(1))  // If there are any 1's than show selection
		     {  	
			
			 String temp =ServicesListItemAdapter.ServicesList.toString();
			 temp = temp.substring(1,temp.length()-1);  // cut off the brackets from toString method
			 servicestSelectedList.setText("Selected: " + temp);  	
			 servicestSelectedList.setVisibility(0);
		     }
			    
			 else{
			 
				 servicestSelectedList.setVisibility(View.GONE);
			 }
			
			
			TextView sortBy = (TextView)findViewById(R.id.sortBySelectedList);
		 	
			if(AdvancedRestaurantSearchActivity.sortBySelectedList.contains(1))  // If there are any 1's than show selection
		     {  	
			 String temp =SortByListItemAdapter.SortByList.toString();
			 temp = temp.substring(1,temp.length()-1);  // cut off the brackets from toString method
			 sortBy.setText("Selected: " + temp);  	
			 sortBy.setVisibility(0);
		     }
			    
			 else{
			 
				 sortBy.setVisibility(View.GONE);
			 }
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
    
     
      case R.id.search:
    	  searchRetailClicked(findViewById(R.id.button2));
    	  break;
    	  
      default:
    	  break;
      }
      
      return true;
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
  
  
	
  public void clickApparel(View v)
  {
	  	String result="";
	  	String whichSearchChosen = "Apparel";
	  	
	  	result = getRetailCategories(whichSearchChosen);
	  	
		Deserialize deserializer = new Deserialize();
		dataStorageManager.ApparelList = deserializer .getStringList(result);

		ApparelListModelAdapter.LoadModel(dataStorageManager.ApparelList);
		 if(FirstApparel)
		 {
			 for(int i = 0; i < ApparelListModelAdapter.Items.size(); i++ )  
			 {
				ApparelSelectedList.add(i, 0); 
			 }
			 FirstApparel = false;
		 }
		 
		 Intent i = new Intent(this, RetailListActivity.class);
	
		  i.putExtra("whichSearchChosen", whichSearchChosen);
		  startActivity(i);
  }
  
 
  public void clickFood(View v)
  {	
	  String result="";
	  String whichSearchChosen = "Food & Drink";
	
	  result = getRetailCategories(whichSearchChosen);
		
		Deserialize deserializer = new Deserialize();
		dataStorageManager.FoodList = deserializer.getStringList(result);
		
		FoodListModelAdapter.LoadModel(dataStorageManager.FoodList);
		 if(FirstFood)
		 {
			 for(int i = 0; i < FoodListModelAdapter.Items.size(); i++ )  
			 {
				FoodSelectedList.add(i, 0); 
			 }
			 FirstFood = false;
		 }
		 
		 Intent i = new Intent(this, RetailListActivity.class);
	
		  i.putExtra("whichSearchChosen", whichSearchChosen);
		  startActivity(i);
  }
  /*
  public void clickHome(View v)
  {
	  String result="";
	  String whichSearchChosen = "Home";
	  result = getRetailCategories(whichSearchChosen);
	  	
		dataStorageManager.HomeList = Deserialize.getStringList(result);
		
		HomeListModelAdapter.LoadModel(dataStorageManager.HomeList);
		 if(FirstHome)
		 {
			 for(int i = 0; i < HomeListModelAdapter.Items.size(); i++ )  
			 {
				HomeSelectedList.add(i, 0); 
			 }
			 FirstHome = false;
		 }
		 
		 Intent i = new Intent(this, RetailListActivity.class);

		  i.putExtra("whichSearchChosen", whichSearchChosen);
		  startActivity(i);
  }
  */
  
  public void clickElectronics(View v)
  {
	  String result="";
	  String whichSearchChosen = "Electronics";
	  result = getRetailCategories(whichSearchChosen);
	  	
		Deserialize deserializer = new Deserialize();
		dataStorageManager.ElectronicsList = deserializer.getStringList(result);
						
		ElectronicsListModelAdapter.LoadModel(dataStorageManager.ElectronicsList);
		 if(FirstElectronics)
		 {
			 for(int i = 0; i < ElectronicsListModelAdapter.Items.size(); i++ )  
			 {
				ElectronicsSelectedList.add(i, 0); 
			 }
			 FirstElectronics = false;
		 }
		 
		 Intent i = new Intent(this, RetailListActivity.class);

		  i.putExtra("whichSearchChosen", whichSearchChosen);
		  startActivity(i);
  }
  
  
  public void clickHealth(View v)
  {
	  String result="";
	  String whichSearchChosen = "Health & Beauty";
	  result = getRetailCategories(whichSearchChosen);
	  	
		Deserialize deserializer = new Deserialize();
		dataStorageManager.HealthList = deserializer.getStringList(result);
		
		HealthListModelAdapter.LoadModel(dataStorageManager.HealthList);
		 if(FirstHealth)
		 {
			 for(int i = 0; i < HealthListModelAdapter.Items.size(); i++ )  
			 {
				HealthSelectedList.add(i, 0); 
			 }
			 FirstHealth = false;
		 }
		 
		 Intent i = new Intent(this, RetailListActivity.class);

		  i.putExtra("whichSearchChosen", whichSearchChosen);
		  startActivity(i);
  }
  
  
  public void clickEntertainment(View v)
  {
	  String result="";
	  String whichSearchChosen = "Entertainment";
	  result = getRetailCategories(whichSearchChosen);
	  	
		Deserialize deserializer = new Deserialize();
		dataStorageManager.EntertainmentList = deserializer.getStringList(result);
		
		
		EntertainmentListModelAdapter.LoadModel(dataStorageManager.EntertainmentList);
		 if(FirstEntertainment)
		 {
			 for(int i = 0; i < EntertainmentListModelAdapter.Items.size(); i++ )  
			 {
				EntertainmentSelectedList.add(i, 0); 
			 }
			 FirstEntertainment = false;
		 }
		 
		 Intent i = new Intent(this, RetailListActivity.class);

		  i.putExtra("whichSearchChosen", whichSearchChosen);
		  startActivity(i);
  }
  
  public void clickServices(View v)
  {
	  String result="";
	  String whichSearchChosen = "Services";
	  result = getRetailCategories(whichSearchChosen);
	  	
		Deserialize deserializer = new Deserialize();
		dataStorageManager.ServicesList = deserializer.getStringList(result);
		
		
		ServicesListModelAdapter.LoadModel(dataStorageManager.ServicesList);
		 if(FirstServices)
		 {
			 for(int i = 0; i < ServicesListModelAdapter.Items.size(); i++ )  
			 {
				ServicesSelectedList.add(i, 0); 
			 }
			 FirstServices = false;
		 }
		 
		 Intent i = new Intent(this, RetailListActivity.class);

		  i.putExtra("whichSearchChosen", whichSearchChosen);
		  startActivity(i);
  }
  
  public void clickSortBy(View v)
  {
		String whichSearchChosen = "SortBy";
		
		SortByListModelAdapter.LoadModel(hardCodedSortByItem);
		 if(FirstSortBy)
		 {
			 for(int i = 1; i < SortByListModelAdapter.Items.size(); i++ )    // ignore first selection
			 {
				 AdvancedRestaurantSearchActivity.sortBySelectedList.add(i, 0); 
			 }
			 FirstSortBy = false;
		 }
		
		 Intent i = new Intent(this, RestaurantListActivity.class);

		  i.putExtra("whichSearchChosen", whichSearchChosen);
		 //   i.putExtra("merchantType", mainActivity.selectedMerchant.MerchantType);
		    startActivity(i);
	}
  
	public void searchRetailClicked(View v)
	{

		
		String[] Apparel = new String[ApparelListItemAdapter.ApparelList.size()];
		Apparel = ApparelListItemAdapter.ApparelList.toArray(Apparel);   
		
		String[] Food = new String[FoodListItemAdapter.FoodList.size()];
		Food = FoodListItemAdapter.FoodList.toArray(Food);
		/*
		String[] Home = new String[HomeListItemAdapter.HomeList.size()];
		Home = HomeListItemAdapter.HomeList.toArray(Home);
		*/
		
		String[] Electronics = new String[ElectronicsListItemAdapter.ElectronicsList.size()];
		Electronics = ElectronicsListItemAdapter.ElectronicsList.toArray(Electronics);
		
		String[] Entertainment = new String[EntertainmentListItemAdapter.EntertainmentList.size()];
		Entertainment = EntertainmentListItemAdapter.EntertainmentList.toArray(Entertainment);
		
		String[] Health = new String[HealthListItemAdapter.HealthList.size()];
		Health = HealthListItemAdapter.HealthList.toArray(Health);
		
		String[] Service = new String[ServicesListItemAdapter.ServicesList.size()];
		Service = ServicesListItemAdapter.ServicesList.toArray(Service);
		
		String[] SortBy_array = new String[SortByListItemAdapter.SortByList.size()];
		SortBy_array = SortByListItemAdapter.SortByList.toArray(SortBy_array);
	 

	  		
	  	ArrayList<String> totalArray = new ArrayList<String>();
	  	totalArray.addAll(Arrays.asList(Apparel));
	  	totalArray.addAll(Arrays.asList(Food));
	  	//totalArray.addAll(Arrays.asList(Home));
	  	totalArray.addAll(Arrays.asList(Electronics));
	  	totalArray.addAll(Arrays.asList(Entertainment));
	  	totalArray.addAll(Arrays.asList(Health));
	  	totalArray.addAll(Arrays.asList(Service));
	  	totalArray.addAll(Arrays.asList(SortBy_array));
	  	/*
	  	String [] concatedArrray = totalArray.toArray(new String[Apparel.length+Food.length
	  	                                                         +Home.length+Electronics.length+Entertainment.length
	  	                                                         +Health.length+Service.length + SortBy_array.length]);
  	*/
		if(SortByListItemAdapter.SortByList.size()>0)
		{
			String temp = SortByListItemAdapter.SortByList.get(0);
			if(temp.equals("Name: A-Z"))
			{
				Order = "1";
				SortBy = "0";
				
			}
			else if(temp.equals("Name: Z-A"))
			{
				Order = "0";
				SortBy = "0";
			}
			
			else if(temp.equals("Price: $-$$$$"))
			{
				Order = "1";
				SortBy = "2";
			}
			else if(temp.equals("Price: $$$$-$"))
			{
				Order = "0";
				SortBy = "2";
			}
			
			else if(temp.equals("Rating: 0-10"))
			{
				Order ="1";
				SortBy ="1";
			}
			else if(temp.equals("Rating: 10-0"))
			{
				Order = "0";
				SortBy ="1";
			}
		}
	
		//this.insertRecord(concatedArrray,SortBy,Order);
}
	


}

