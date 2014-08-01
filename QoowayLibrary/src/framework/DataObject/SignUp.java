package framework.DataObject;

public class SignUp {

	int AgeGroup;
	  String FirstName;
	  String LastName;
	  String Gender;
	  int HeardSiteWayID;
	  String Email;
	  String Password;
	  String ConfirmPassword;
	  boolean emailNotification;
	  boolean TermsOfUse;
	  int CardTypeID;
	  boolean testMessageNotification;
	  String Address;
	  String City;
	  String PostalCode;
	  String Province;
	  String CardCode;
	  boolean PhysicalCard = true;
	  
	  public SignUp(int ageGroup, String firstName, String lastName,
			String gender, int heardSiteWayID, String email, String password,
			String confirmPassword, boolean emailNotification,
			boolean termsOfUse, int cardTypeID, boolean testMessageNotification , String PointCardCode) {
		AgeGroup = ageGroup;
		FirstName = firstName;
		LastName = lastName;
		Gender = gender;
		HeardSiteWayID = heardSiteWayID;
		Email = email;
		Password = password;
		ConfirmPassword = confirmPassword;
		this.emailNotification = emailNotification;
		TermsOfUse = termsOfUse;
		CardTypeID = cardTypeID;
		this.testMessageNotification = testMessageNotification;
		this.CardCode = PointCardCode;
	}  
	  public SignUp(int ageGroup, String firstName, String lastName,
			String gender, int heardSiteWayID, String email, String password,
			String confirmPassword, boolean emailNotification,
			boolean termsOfUse, int cardTypeID, boolean testMessageNotification,
			String Address, String City, String PostalCode, String Province ,String PointCardCode
			  ) {
		AgeGroup = ageGroup;
		FirstName = firstName;
		LastName = lastName;
		Gender = gender;
		HeardSiteWayID = heardSiteWayID;
		Email = email;
		Password = password;
		ConfirmPassword = confirmPassword;
		this.emailNotification = emailNotification;
		TermsOfUse = termsOfUse;
		CardTypeID = cardTypeID;
		this.testMessageNotification = testMessageNotification;
		this.Address = Address;
		this.City = City;
		this.PostalCode = PostalCode;
		this.Province = Province;
		this.CardCode = PointCardCode;
	}  	  
	  
	  
	
	
	/*
		  "AgeGroup" : 1,
	  "FirstName" : "Alan",
	  "Gender" : "M",
	  "HeardSiteWayID" : 2,
	  "Email" : "joe@qooway.com",
	  "ConfirmPassword" : "abc123",
	  "Password" : "abc123",
	  "LastName" : "Yen",
	  "emailNotification" : true,
	  "TermsOfUse" : true,
	  "CardTypeID" : 1,
	  "testMessageNotification" : false
	  
	String Email;
	String Password;
	String ConfirmPassword;
	String FirstName;
	String LastName;
	String Gender;
	int AgeGroup;
	String Address;
	String Province;
	String City;
	String PostalCode;
	String MobileAreaCode;
	String MobileNumber;
	int HeardSiteWayID;
	String HeardSiteWayOther;
	String PreferedLanguage;
	boolean emailNotification;
	boolean testMessageNotification;
	boolean TermsOfUse;
	int CardTypeID;
	
	public SignUp (String Email, String Password, String ConfirmPassword, String FirstName, String LastName, String Gender, int AgeGroup, String Address,
			String Province, String City, String PostalCode, String MobileAreaCode, String MobileNumber, int HeardSityWayID, String HeardSiteWayOther,
			String PreferedLanguage, boolean emailNotification, boolean testMessageNotification, boolean TermsOfUse, int CardTypeID) {
		this.Email = Email;
		this.Password = Password;
		this.ConfirmPassword = ConfirmPassword;
		this.FirstName = FirstName;
		this.LastName = LastName;
		this.Gender = Gender;
		this.AgeGroup = AgeGroup;
		this.Address = Address;
		this.Province = Province;
		this.City = City;
		this.PostalCode = PostalCode;
		this.MobileAreaCode = MobileAreaCode;
		this.MobileNumber = MobileNumber;
		this.HeardSiteWayID = HeardSityWayID;
		this.HeardSiteWayOther = HeardSiteWayOther;
		this.PreferedLanguage = PreferedLanguage;
		this.emailNotification = emailNotification;
		this.testMessageNotification = testMessageNotification;
		this.TermsOfUse = TermsOfUse;
		this.CardTypeID = CardTypeID;
	}
	*/
}
