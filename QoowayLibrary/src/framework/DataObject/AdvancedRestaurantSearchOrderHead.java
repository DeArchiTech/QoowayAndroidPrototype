package framework.DataObject;

import java.util.ArrayList;

public class AdvancedRestaurantSearchOrderHead 
{

 private String TakeOut;
 private String Delivery;
 private String PriceLegend;
 private String SortBy;
 private String OrderBy;
 
 private ArrayList<String> WebMealType;
 private ArrayList<String> WebCuisine;
 private ArrayList<String> Neighbourhood;

 
 public AdvancedRestaurantSearchOrderHead(ArrayList<String> WebMealType, ArrayList<String> WebCuisineList, ArrayList<String> Neighbourhood, String TakeOut, String Delivery, String PriceLegend, String SortBy, String OrderBy)
 {
  this.WebMealType = WebMealType;
  this.WebCuisine = WebCuisineList;
  this.TakeOut= TakeOut;
  this.Delivery=Delivery;
  this.PriceLegend=PriceLegend;
  this.SortBy=SortBy;
  this.OrderBy=OrderBy;
  this.Neighbourhood = Neighbourhood;
  
 }
}


