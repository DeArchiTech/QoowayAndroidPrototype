package framework.DataObject;

public class Subcategory {

	public String SubcategoryDesc;
	public int SubcategoryID;
	public int CategoryCount;
	
	public Subcategory(String desc , int ID , int Count)
	{
		SubcategoryDesc = desc;
		SubcategoryID = ID;
		CategoryCount = Count;
	}
}
