package framework.DataObject;

import java.util.ArrayList;

public class AdvancedRetailSearchOrderHead 
 {
  private ArrayList<String>Subcategory;
  private ArrayList<String> Neighbourhood;
  private String SortBy;
  private String OrderBy;
  
  public AdvancedRetailSearchOrderHead(ArrayList<String> Subcategory, ArrayList<String> Neighbourhood,String SortBy, String OrderBy)
  {
   this.Subcategory = Subcategory;
   this.SortBy=SortBy;
   this.OrderBy=OrderBy;
   this.Neighbourhood = Neighbourhood;
  }
 }