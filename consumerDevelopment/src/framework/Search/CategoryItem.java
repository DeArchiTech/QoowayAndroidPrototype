package framework.Search;

public class CategoryItem {

    public int Id;
    public String name;
    public String type;
    public int count;
   
    public CategoryItem(int id ,String name ,String type, int categoryCount)
    {
    	Id = id;
    	this.name=name;
    	this.type=type;
    	this.count= categoryCount;
    }

}
