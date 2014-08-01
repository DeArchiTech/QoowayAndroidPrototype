package framework.DataObject;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class RetailCategories {
	
	 public ArrayList<String> Apparel;
	 public ArrayList<String> Electronics;
	 public ArrayList<String> Entertainment;
	 @SerializedName("Food & Drink") public ArrayList<String> FoodDrink;
	 @SerializedName("Health & Beauty") public ArrayList<String> HealthBeauty;
	 public ArrayList<String> Home;
	 public ArrayList<String> Services;
	 public ArrayList<String> Neighbourhood;
	 
	 
}
