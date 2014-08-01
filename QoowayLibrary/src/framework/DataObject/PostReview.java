package framework.DataObject;

public class PostReview {

	public int ReviewID;
	public String StoreID;
	public String CustomerID;
	public short SourceType;
	public String Topic;
	public String Remark;
	public int Score;
	String DateCreated;
	public String NickName;
	public short Status;
	public int ReviewLevel;
	public int Item1Score;
	public int Item2Score;
	public int Item3Score;
	public int Item4Score;
	public int Item5Score;
	public int AgreeNum;
	public int DisagreeNum;
	public String StoreMealType;
	public int NoiceLevel;
	public int Anonymous;
	
	public PostReview (String StoreID, String CustomerID, String Remark, String DateCreated, String NickName, int Item1Score, int Item2Score, int Item3Score, int Anonymous, int score){
		
		this.ReviewID = 1;
		this.StoreID = StoreID;
		this.CustomerID = CustomerID;
		this.SourceType = 1;
		this.Topic = "1";
		//There is no food rating for the merchant as they are retail
		this.Score = score;
		this.Remark = Remark;
		this.DateCreated = DateCreated;
		this.NickName = NickName;
		this.Status = 0;
		this.ReviewLevel = 1;
		this.Item1Score = Item1Score;
		this.Item2Score = Item2Score;
		this.Item3Score = Item3Score;
		this.Item4Score = 0;
		this.Item5Score = 0;
		this.AgreeNum = 0;
		this.DisagreeNum = 0;
		this.StoreMealType = "1";
		this.NoiceLevel = 1;
		this.Anonymous = Anonymous;
		
	}
	
}