package ui.advanced_search;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import framework.QoowayActivity;
import ui.averagePrice.AveragePriceListItemAdapter;
import ui.averagePrice.AveragePriceListModelAdapter;
import ui.cuisine.CuisineListItemAdapter;
import ui.cuisine.CuisineListModelAdapter;
import ui.restaurantType.RestaurantTypeListItemAdapter;
import ui.restaurantType.RestaurantTypeListModelAdapter;
import ui.searchOptions.OptionListItemAdapter;
import ui.searchOptions.OptionListModelAdapter;
import ui.sortBy.SortByListItemAdapter;
import ui.sortBy.SortByListModelAdapter;
import com.qooway.consumerv01.R;
import data.Deserialize;
import data.WebApiManager;
import data.DataStorageManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;



public class AdvancedRestaurantSearchActivity extends QoowayActivity{
	
	final Context context = this;
	public String httpserverUrl = "online.profitek.com/testingAPi";
	public String httpsserverUrl = "online.profitek.com/testingAPi";	
	public WebApiManager webApiManager;
	public DataStorageManager dataStorageManager;
	
	
	public static boolean FirstCuisineSelection;   // on create it is always true
	public static ArrayList<Integer> cuisineSelectedList;
	
	public static boolean FirstRestaurantType;   
	public static ArrayList<Integer> restaurantTypeSelectedList;
	
	
	public static boolean FirstOption;
	public static ArrayList<Integer> optionSelectedList;
	public List<String> hardCodedOptionItem;
	
	public static boolean FirstAveragePrice;
	public static ArrayList<Integer> averagePriceSelectedList;
	public List<String> hardCodedAveragePriceItem;
	
	public static boolean FirstSortBy;
	public static ArrayList<Integer>sortBySelectedList;
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
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);  
        getActionBar().setDisplayHomeAsUpEnabled(true);
	    
		
       
        webApiManager = WebApiManager.getSingletonInstance();  
		dataStorageManager = DataStorageManager.getSingletonInstance();
		/*
        Intent intent = getIntent();
        String whichScreen = intent.getExtras().getString("whichScreen");
	    */
       
      
	    setContentView(R.layout.fragment_advanced_search_restaurant_home);
	    	
	    FirstCuisineSelection = true;
	    CuisineListItemAdapter.cuisineList = new ArrayList<String>();  // used for Displaying Cuisine names
		cuisineSelectedList = new ArrayList<Integer>();   // used for which Cuisine are selected by user
			
			
		FirstRestaurantType = true;
		restaurantTypeSelectedList = new ArrayList<Integer>();
		RestaurantTypeListItemAdapter.RestaurantTypeList = new ArrayList<String>();  // used for Displaying Cuisine names
			
		FirstOption = true;
		OptionListItemAdapter.OptionList = new ArrayList<String>();
		optionSelectedList = new ArrayList<Integer>();
		hardCodedOptionItem = new ArrayList<String>();
		hardCodedOptionItem.add("Takeout");  //0
	    hardCodedOptionItem.add("Delivery"); //1
		    
		FirstAveragePrice = true;
		AveragePriceListItemAdapter.AveragePriceList = new ArrayList<String>();
	    averagePriceSelectedList = new ArrayList<Integer>();
		hardCodedAveragePriceItem = new ArrayList<String>();
	    hardCodedAveragePriceItem.add("$(<15)");  // 0
		hardCodedAveragePriceItem.add("$$(15-25)");  // 1
	    hardCodedAveragePriceItem.add("$$$(25-40)");  // 2
		hardCodedAveragePriceItem.add("$$$$(40+)");  // 3
		    
		FirstSortBy = true;
		SortByListItemAdapter.SortByList = new ArrayList<String>();
		sortBySelectedList = new ArrayList<Integer>();
		// INITIALIZE so that "Name: A-Z" is pre-selected and shown
		sortBySelectedList.add(0,1);  
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

			//	    *** CUISINE ***
			TextView cuisineList = (TextView)findViewById(R.id.selectedList);
			
			
		    if(cuisineSelectedList.contains(1))  // If there are any 1's than show selection
	        {  	
		    	String temp = CuisineListItemAdapter.cuisineList.toString();
		    	temp = temp.substring(1,temp.length()-1);  // cut off the brackets from toString method
		    	cuisineList.setText("Selected: " + temp);  	
		    	cuisineList.setVisibility(0);
		    	/*
		    	Button Button1 = (Button)findViewById(R.id.cuisine_arrow);
		    	Button1.invalidate();
		    	*/
	        }
		    
		    else{
		 
		    	cuisineList.setVisibility(View.GONE);
		    }
		    
		    //	    *** RESTAURANT TYPE ***
		    TextView restaurantList = (TextView)findViewById(R.id.restaurantSelectedList);
			
		    if(restaurantTypeSelectedList.contains(1))  // If there are any 1's than show selection
	        {  	
		    	String temp = RestaurantTypeListItemAdapter.RestaurantTypeList.toString();
		    	temp = temp.substring(1,temp.length()-1);  // cut off the brackets from toString method
		    	restaurantList.setText("Selected: " + temp);  	
		    	restaurantList.setVisibility(0);
	        }
		    
		    else{
		 
		    	restaurantList.setVisibility(View.GONE);
		    }
		    
		    /*
		    //    *** OPTIONS ***
		    TextView optionList = (TextView)findViewById(R.id.optionSelectedList);
			
		    if(optionSelectedList.contains(1))  // If there are any 1's than show selection
	        {  	
		    	String temp = OptionListItemAdapter.OptionList.toString();
		    	temp = temp.substring(1,temp.length()-1);  // cut off the brackets from toString method
		    	optionList.setText("Selected: " + temp);  	
		    	optionList.setVisibility(0);
	        }
		    
		    else{
		 
		    	optionList.setVisibility(View.GONE);
		    }
		    */
		    
		    //	    *** AveragePrice ***
			TextView averagePrice = (TextView)findViewById(R.id.averagePriceSelectedList);
				
			if(averagePriceSelectedList.contains(1))  // If there are any 1's than show selection
		     {  	
			 String temp =AveragePriceListItemAdapter.AveragePriceList.toString();
			 temp = temp.substring(1,temp.length()-1);  // cut off the brackets from toString method
			 averagePrice.setText("Selected: " + temp);  	
			 averagePrice.setVisibility(0);
		     }
			    
			 else{
			 
			 averagePrice.setVisibility(View.GONE);
			 }
			
			
			  //	    *** SortBy ***
			TextView sortBy = (TextView)findViewById(R.id.sortBySelectedList);
						 	
			if(sortBySelectedList.contains(1))  // If there are any 1's than show selection
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
	    	  searchClicked(findViewById(R.id.button2));
	    	  break;
	    	  
	      default:
	    	  break;
	      }
	      
	      return true;
	  } 
  
  public void clickCuisine(View v) {
	  String whichSearchChosen = "Cuisine";
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
				
		 CuisineListModelAdapter.LoadModel(dataStorageManager.Cuisine);
		 
		 if(FirstCuisineSelection) // If First selection, set cuisineSelectedList(indicator) to all zeros
		 {
			 for(int i = 0; i < CuisineListModelAdapter.Items.size(); i++ )  
			 {
				 cuisineSelectedList.add(i, 0); 
			 }
			 FirstCuisineSelection = false;
		 }
		  
	  Intent i = new Intent(this, RestaurantListActivity.class);	  
	  i.putExtra("whichSearchChosen", whichSearchChosen);
	 //   i.putExtra("merchantType", mainActivity.selectedMerchant.MerchantType);
	    startActivity(i);
	  
	}
  
 public void clickRestaurantType(View v){
	 String whichSearchChosen = "Restaurant";
	 
	 String result ="";
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
		dataStorageManager.RestaurantType = deserializer.getStringList(result);

		 RestaurantTypeListModelAdapter.LoadModel(dataStorageManager.RestaurantType);
		 
		 if(FirstRestaurantType) 
		 {
			 for(int i = 0; i < RestaurantTypeListModelAdapter.Items.size(); i++ )  
			 {
				 restaurantTypeSelectedList.add(i, 0); 
			 }
			 FirstRestaurantType = false;
		 }
		  
	 Intent i = new Intent(this, RestaurantListActivity.class);
	  i.putExtra("whichSearchChosen", whichSearchChosen);

	    startActivity(i);
 }
 
 public void clickOption(View v)
 {
	 
	 String whichSearchChosen = "Option";
	 
     OptionListModelAdapter.LoadModel(hardCodedOptionItem);
	 if(FirstOption)
	 {
		 for(int i = 0; i < OptionListModelAdapter.Items.size(); i++ )  
		 {
			optionSelectedList.add(i, 0); 
		 }
		 FirstOption = false;
	 }
	 
	 
	 Intent i = new Intent(this, RestaurantListActivity.class);
	  i.putExtra("whichSearchChosen", whichSearchChosen);
	 //   i.putExtra("merchantType", mainActivity.selectedMerchant.MerchantType);
	    startActivity(i);
 }
 
	public void clickAveragePrice(View v)
	{
		String whichSearchChosen = "AveragePrice";
		
		AveragePriceListModelAdapter.LoadModel(hardCodedAveragePriceItem);
		 if(FirstAveragePrice)
		 {
			 for(int i = 0; i < AveragePriceListModelAdapter.Items.size(); i++ )  
			 {
				averagePriceSelectedList.add(i, 0); 
			 }
			 FirstAveragePrice = false;
		 }
		 
		 Intent i = new Intent(this, RestaurantListActivity.class);
		  i.putExtra("whichSearchChosen", whichSearchChosen);
		 //   i.putExtra("merchantType", mainActivity.selectedMerchant.MerchantType);
		    startActivity(i);
	}
	
	public void clickSortBy(View v)
	{
		String whichSearchChosen = "SortBy";
		
		SortByListModelAdapter.LoadModel(hardCodedSortByItem);
		 if(FirstSortBy)
		 {
			 for(int i = 1; i < SortByListModelAdapter.Items.size(); i++ )   // ignore FIRST SELECTION
			 {
				sortBySelectedList.add(i, 0); 
			 }
			 FirstSortBy = false;
		 }
		
		 Intent i = new Intent(this, RestaurantListActivity.class);
		  i.putExtra("whichSearchChosen", whichSearchChosen);
		
		    startActivity(i);
	}
	
	public void searchClicked(View v)
	{

		String[] cuisine = new String[CuisineListItemAdapter.cuisineList.size()];
		cuisine = CuisineListItemAdapter.cuisineList.toArray(cuisine);   
		
		
		String[] restaurant = new String[RestaurantTypeListItemAdapter.RestaurantTypeList.size()];
		restaurant = RestaurantTypeListItemAdapter.RestaurantTypeList.toArray(restaurant); 
	    
    	String TakeOut = "";
    	String Delivery = "";
    	String Price = "";
    	String SortBy = "";
    	String Order =  "";
    	
    	if(OptionListItemAdapter.OptionList.size() > 0)    // make sure the size is greater than 0
    	{
    		if(OptionListItemAdapter.OptionList.contains("Takeout"));
    		{
    			TakeOut = "Y";
    		}
    		if(OptionListItemAdapter.OptionList.contains("Delivery"));
    		{
    			Delivery ="Y";
    		}
    	}
    	
    	if(AveragePriceListItemAdapter.AveragePriceList.size()>0)
    	{
    		String temp = AveragePriceListItemAdapter.AveragePriceList.get(0);
    		
    		if(temp.equals("$(<15)"))
    		{
    			Price ="0";
    		}
    		else if(temp.equals("$$(15-25)"))
    		{
    			Price = "1";
    		}
    		else if(temp.equals("$$$(25-40)"))
    		{
    			Price = "2";
    		}
    		else if(temp.equals("$$$$(40+)"))
    		{
    			Price = "3";
    		}
    	}
    	  
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
	
		this.insertRecord(restaurant,cuisine,TakeOut,Delivery,Price,SortBy,Order);
}
	
	private void insertRecord(String[] restaurant, String[] cuisine,
			String takeOut, String delivery, String price, String sortBy,
			String order) {
		/*
		whichSearchChosen = "searchClicked";
		
		AdvancedRestaurantSearchOrderHead tran = new AdvancedRestaurantSearchOrderHead(restaurant,cuisine,takeOut,delivery,price,sortBy,order);
		String result = "";
		try{
			result = this.webApiManager.postSearchRestaurant(Serialize.advRestaurantSearch(tran));
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		if(result != null){
		dataStorageManager.SearchList = Deserialize.getMerchantList(result);

		SearchListModelAdapter.LoadModel(dataStorageManager.SearchList);
		

		 Intent i = new Intent(this, RestaurantListActivity.class);
		  i.putExtra("whichSearchChosen", whichSearchChosen);
		 //   i.putExtra("merchantType", mainActivity.selectedMerchant.MerchantType);
		    startActivity(i);
		} else {
			Toast.makeText(context, "Please choose at least two selections", Toast.LENGTH_SHORT).show();
		}
		*/
	
	}
	

}
