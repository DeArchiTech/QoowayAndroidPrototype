package ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import ui.voucher.ListVoucherListModelAdapter;

import data.Deserialize;
import data.EnumData;
import data.WebApiManager;
import data.WebApiManagerPageFragment;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.qooway.consumerv01.R;
import com.devspark.progressfragment.ProgressFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import framework.CustomClass.ScrollViewExtend;
import framework.DataObject.Reply;
import framework.DataObject.Review;
import data.DataStorageManager;
import framework.QoowayActivity;

@SuppressLint({ "CutPasteId", "SimpleDateFormat" })
public class MerchantDetailActivity extends QoowayActivity {
	public String httpserverUrl = "online.profitek.com/appdevelopment";
	public String httpsserverUrl = "online.profitek.com/appdevelopment";
	public DataStorageManager dataStorageManager;  // Ryan addition
	public WebApiManager webApiManager;
	public ScrollViewExtend scrollView;
	public boolean isMerchantFavorited = false;
	private int half_star = R.drawable.reviews_halfstar;
	private int yellow_star = R.drawable.reviews_yellow_star;
	private Context context;
	private Handler mHandler;
	
	private Runnable mShowContentRunnable = new Runnable() {
		@Override
		public void run() {
			if(MerchantDetailActivity.this.IsConneted())
			{
				if (DataStorageManager.getSingletonInstance().getAsyncTaskCount() == 0) {
					MerchantDetailActivity.this.isMerchantFavorited = dataStorageManager.isMerchantFavorited;
					MerchantDetailActivity.this.invalidateOptionsMenu();
				} else {
					mHandler.postDelayed(mShowContentRunnable, 300);
				}	
			}
			else
			{
				MerchantDetailActivity.this.DisplayNoInternetImage();
			}
		}
	};
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
         inflater.inflate(R.menu.menu_merchant, menu);
         String result="";
	     context = getApplicationContext();

         return super.onCreateOptionsMenu(menu);
    }    
	
	 @Override
	 protected void onResume() {
	  super.onResume();
	  DataStorageManager.getSingletonInstance().currentActivity = this;
	 }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{       
		switch (menuItem.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			finish();
			return true;
	    
		case R.id.favouriteIcon:
			if(dataStorageManager.loggedIn){
				String temp_merchantStoreID = "" + dataStorageManager.selectedMerchant.StoreID;
				String result="";
				try {
					result = webApiManager.checkFavorite(dataStorageManager.currentUser.CustomerID, temp_merchantStoreID);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
					return false;
				} catch (ExecutionException e1) {
					e1.printStackTrace();
					return false;
				}
				if(result.equals("false")){
					try {
						webApiManager.postCustomerFavorite(dataStorageManager.currentUser.CustomerID, temp_merchantStoreID);
					} catch (InterruptedException e) {
						e.printStackTrace();
						return false;
					} catch (ExecutionException e) {
						e.printStackTrace();
						return false;
					}
					
					isMerchantFavorited = true;
					invalidateOptionsMenu();
					//
				} else{
					try {
						webApiManager.postDeleteFavorite(dataStorageManager.currentUser.CustomerID, temp_merchantStoreID);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					isMerchantFavorited = false;
					invalidateOptionsMenu();
				}
			} else {
				Toast.makeText(context, "You must be logged in to favourite", Toast.LENGTH_LONG).show();
			}
			return true;
		}  
	    return super.onOptionsItemSelected(menuItem);
	} 
	//
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if(isMerchantFavorited == true){
			menu.findItem(R.id.favouriteIcon).setIcon(R.drawable.md_favorite_yes);   // favorited,  RED
		} else{ 
			menu.findItem(R.id.favouriteIcon).setIcon(R.drawable.menu_favourite_active);   // not favorited, white
		}
		return super.onPrepareOptionsMenu(menu);	
	}
	

    @SuppressLint("CutPasteId")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
    	
    	dataStorageManager = DataStorageManager.getSingletonInstance();
    	webApiManager = WebApiManager.getSingletonInstance();

    	context = getApplicationContext();
    	
        getActionBar().setTitle("Merchant Details");
	
        setContentView(R.layout.fragment_merchant_details);

        try {
        
        // Sets the scroll to the initial position of Screen
        scrollView = (ScrollViewExtend)findViewById(R.id.merchantScroll); 
        /*BE PROACTIVE
        final View scrollAnchorStarting = findViewById(R.id.gallery1);

        scrollView.postDelayed(new Runnable() {    
        	@Override    
        	public void run() {      scrollView.setScrollY(scrollAnchorStarting.getTop());        }
        	},0);
                BE PROACTIVE*/
        
        LinearLayout linearlayout = (LinearLayout)findViewById(R.id.merchantDetailPage);
        scrollView.setTag(R.integer.layout, linearlayout);
        
        scrollView.setButtons();  // Initlializes all the layouts for color changes

		final String MerchantID = Integer.toString(dataStorageManager.selectedMerchant.StoreID);
		final String StoreName = dataStorageManager.selectedMerchant.Name;


		TextView promotion = (TextView) findViewById(R.id.promotion);
		promotion.setText(dataStorageManager.selectedMerchant.SpecialOffer);
		/*BE PROACTIVE
		Button checkIn = (Button) findViewById(R.id.checkInButton);
	
		checkIn.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	if(dataStorageManager.loggedIn){
			    	Intent intent = new Intent(context, CheckInActivity.class);
			    	startActivity(intent);
		    	} else {
		    		Toast.makeText(context, getString(R.string.login_message_checkin), Toast.LENGTH_LONG).show();
		    	}
		    }
		});
			BE PROACTIVE*/
		final ScrollView scroll = (ScrollView) findViewById(R.id.merchantScroll);
	//	View checkin = findViewById(R.id.merchantButton1);
	    
	/*	checkin.setOnClickListener(new View.OnClickListener() {
			
			View scrollAnchor1 = findViewById(R.id.checkIn);
		    @Override
		    public void onClick(View v) {
		    	resetMerchantBar();	
		     	scroll.scrollTo((int)scrollView.getX(), scrollAnchor1.getTop());
		    	v.setBackgroundColor(getResources().getColor(R.color.Red));
		    	ImageView icon = (ImageView) v.findViewById(R.id.merchantButton1image);
		    	icon.setImageResource(R.drawable.checkin);
		    	findViewById(R.id.merchantArrow1).setVisibility(View.VISIBLE);
		    }
		});*/
		//BE PROACTIVE
		/*
		View info = findViewById(R.id.merchantButton2);	
		info.setOnClickListener(new View.OnClickListener() {
			 
			View scrollAnchor2 = findViewById(R.id.gallery1);
		    @Override
		    public void onClick(View v) {
		    	resetMerchantBar();	
		    	scroll.smoothScrollTo((int)scrollView.getX(), scrollAnchor2.getTop());
		    	v.setBackgroundColor(getResources().getColor(R.color.Red));
		    	ImageView icon = (ImageView) v.findViewById(R.id.merchantButton2image);
		    	icon.setImageResource(R.drawable.information);
		    	findViewById(R.id.merchantArrow2).setVisibility(View.VISIBLE);
		    }
		});
		
		View review = findViewById(R.id.merchantButton3);				
		review.setOnClickListener(new View.OnClickListener() {
			View scrollAnchor3 = findViewById(R.id.writeReviewLayout);
		    @SuppressLint("CutPasteId")
			@Override
		    public void onClick(View v) {
		    	resetMerchantBar();
		    	scroll.smoothScrollTo((int)scrollView.getX(), scrollAnchor3.getTop());
		    	v.setBackgroundColor(getResources().getColor(R.color.Red));
		    	ImageView icon = (ImageView) v.findViewById(R.id.merchantButton3image);
		    	icon.setImageResource(R.drawable.reviews);
		    	findViewById(R.id.merchantArrow3).setVisibility(View.VISIBLE);		    	
		    	
		    }
		});
		View gallery = findViewById(R.id.merchantButton4);
		gallery.setOnClickListener(new View.OnClickListener() {
			@Override
		    public void onClick(View v) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						MerchantDetailActivity.this);
					alertDialogBuilder.setTitle(getString(R.string.gallery_title));
					alertDialogBuilder
						.setMessage(getString(R.string.gallery_message))
						.setCancelable(false)
						.setPositiveButton(getString(R.string.yes),new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
							}
						});
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
		    }
		});*/
		//BE PROACTIVE
		//Gallery Code - Robb
		/*
		Gallery galleryView = (Gallery) findViewById(R.id.gallery1);
		galleryView.setSpacing(5);
		galleryView.setAdapter(new GalleryImageAdapter(this));

        // clicklistener for Gallery
		galleryView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

			}
		});			
		*/
	
		View writeReview = (View) findViewById(R.id.write_review_button);	
		writeReview.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {	
		    	if(dataStorageManager.loggedIn){
			    	Intent intent = new Intent(MerchantDetailActivity.this, WriteReviewActivity.class);
			    	startActivity(intent);
		    	} else {
		    		Toast.makeText(context, getString(R.string.login_message_merchant), Toast.LENGTH_SHORT).show();
		    	}
		    }
			
		});
		View instantDealButton = (View) findViewById(R.id.instantDealButton);
		instantDealButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {	
		    	if(dataStorageManager.loggedIn){
			    	Intent intent = new Intent(MerchantDetailActivity.this, CheckInActivity.class);
			    	startActivity(intent);
		    	} else {
		    		Toast.makeText(context, getString(R.string.login_message_checkin), Toast.LENGTH_LONG).show();
		    	}
		    }
		    
		    
		    
			
		});
		View redeemPointsButton = (View) findViewById(R.id.redeemPointsButton);
		redeemPointsButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {	
				if (dataStorageManager.loggedIn) {
					Intent i = new Intent(MerchantDetailActivity.this, 
							RedeemPointsActivity.class);
					final String MerchantID = Integer.toString(dataStorageManager.selectedMerchant.StoreID);
					final String StoreName = dataStorageManager.selectedMerchant.Name;
			        i.putExtra("MerchantID", MerchantID);
			        i.putExtra("StoreName", StoreName);
					startActivity(i);
				} else {
					Toast.makeText(context,
							"You must be logged in to redeem points",
							Toast.LENGTH_LONG).show();
				}
		    }
		});
		TextView merchantShopName = (TextView) findViewById(R.id.shopName);
		TextView addressSmall = (TextView) findViewById(R.id.address);
		TextView addressBig = (TextView) findViewById(R.id.addressBigger);
		TextView phone = (TextView) findViewById(R.id.phoneField);
		TextView websiteTitle = (TextView) findViewById(R.id.webSite);
		TextView websiteField = (TextView) findViewById(R.id.webSiteField);
		
		ImageView list_image = (ImageView) findViewById(R.id.list_image);  // middle image
		//ImageView checkInImage = (ImageView) findViewById(R.id.checkInImage); // above image
		DisplayImageOptions options = new DisplayImageOptions.Builder()
        .cacheInMemory(true) 
        .build();
		String baseUri = "https://" + DataStorageManager.getSingletonInstance().getApiUrl() + "/api/Picture/GetLogo/";
		String mID = "" + MerchantID;
		String imageUri = baseUri + mID;
		ImageLoader IM = ImageLoader.getInstance();
		IM.displayImage(imageUri, list_image, options); 
		//Be Proactive
		merchantShopName.setText(dataStorageManager.selectedMerchant.Name);
		addressSmall.setText(dataStorageManager.selectedMerchant.Address);
		
		String BiggerAddress = dataStorageManager.selectedMerchant.City
				+ ", "
				+ dataStorageManager.selectedMerchant.Province
				+ ", "
				+ dataStorageManager.selectedMerchant.PostalCode;
		String Phone = "("
				+ dataStorageManager.selectedMerchant.AreaCode + ") "
				+ dataStorageManager.selectedMerchant.Phone;
		Phone = Phone.substring(0, 9) + "-" + Phone.substring(9, Phone.length());
		addressBig.setText(BiggerAddress);
		phone.setText(" " +Phone);
		if(dataStorageManager.selectedMerchant.Website.equalsIgnoreCase(""))
		{
			((ViewGroup)websiteTitle.getParent()).removeView(websiteTitle);
			((ViewGroup)websiteField.getParent()).removeView(websiteField);
		}
		else
		{
			websiteField.setText(dataStorageManager.selectedMerchant.Website);
		}

		
		String hoursOfOperation = "";
		Boolean firstEntryInLoop =true;
		String[] hourOfOperations = { dataStorageManager.selectedMerchant.OpenClosOther1,
				dataStorageManager.selectedMerchant.OpenClosOther2,
				dataStorageManager.selectedMerchant.OpenClosOther3,
				dataStorageManager.selectedMerchant.OpenClosOther4,
				dataStorageManager.selectedMerchant.OpenClosOther5,
				dataStorageManager.selectedMerchant.OpenClosOther6};
		for (String item : hourOfOperations)
		{
			if(item!="")
			{
				if(firstEntryInLoop)
					hoursOfOperation += item;
				else
					hoursOfOperation += "\n" +item;
				firstEntryInLoop=false;
			}

		}
		
		TextView storeHours = (TextView) findViewById(R.id.storeHoursDetails);
		storeHours.setText(hoursOfOperation);
		
		// Ryan addition, check for comments , if no comments than hide 2 textviews
		String s_features = (String) dataStorageManager.selectedMerchant.AdditionalInfo;
		checkForComments(s_features);
		ArrayList<String> listNames = new ArrayList<String>();
		listNames.add("WebCuisines");
		listNames.add("WebMealTypes");
		listNames.add("WebCategories");
		String PayOption = dataStorageManager.selectedMerchant.PayOption;
		String Parking = dataStorageManager.selectedMerchant.Parking;
		
		TextView addInfoCommentsView = (TextView) findViewById(R.id.additionalInfoDetails);
		String addInfoComments = "";

		firstEntryInLoop =true;
		if (dataStorageManager.selectedMerchant.MerchantType.equals("T")){

		} else if (dataStorageManager.selectedMerchant.MerchantType.equals("R")){
			if (dataStorageManager.selectedMerchant.CanTakeout != null){
				if (dataStorageManager.selectedMerchant.CanTakeout.equals("Y"))
				{
					addInfoComments += ("Take-out: Yes"); 
					firstEntryInLoop = false;
				}
				else if (dataStorageManager.selectedMerchant.CanTakeout.equals("N"))
				{
					addInfoComments += ("Take-out: No"); 
					firstEntryInLoop = false;
				}

			}
			
			if (dataStorageManager.selectedMerchant.CanDelivery != null){
				if (dataStorageManager.selectedMerchant.CanDelivery.equals("Y"))
				{
					if(!firstEntryInLoop)
					{
						addInfoComments += "\n";
					}
					addInfoComments += ("Delivery: Yes"); 
					firstEntryInLoop = false;
				}
					
				else if (dataStorageManager.selectedMerchant.CanDelivery.equals("N"))
				{
					if(!firstEntryInLoop)
					{
						addInfoComments += "\n";
					}
					addInfoComments += ("Delivery: No"); 
					firstEntryInLoop = false;
				}
			}
			
		}
		
		if (!dataStorageManager.selectedMerchant.PayOption.isEmpty())
		{
			if(!firstEntryInLoop)
			{
				addInfoComments += "\n";
			}
			addInfoComments += ("Payment Opitions: " + PayOption );
			firstEntryInLoop = false;
		}
			
		if (!dataStorageManager.selectedMerchant.Parking.isEmpty())
		{
			if(!firstEntryInLoop)
			{
				addInfoComments += "\n";
			}
			addInfoComments += ("Parking: " + Parking);
			firstEntryInLoop = false;
		}

		
		addInfoCommentsView.setText(addInfoComments);
		
		int PriceLegend = dataStorageManager.selectedMerchant.PriceLegend;
		int Score = dataStorageManager.selectedMerchant.Score;
		
	
		setUpPriceLegend(PriceLegend, findViewById(R.id.merchantDetailPage));
		setUpScore(Score, findViewById(R.id.merchantDetailPage));			
		
		ImageButton earnQooPoints = (ImageButton) findViewById(R.id.imageButton2);
		if(dataStorageManager.selectedMerchant.MerchantPrograms.equalsIgnoreCase("1000")){
			earnQooPoints.setImageResource(getResources()
					.getIdentifier(
							"merchantdetails_earnqoopoints_yes",
							"drawable",
							getApplicationContext()
									.getPackageName()));
		}
		
		ReviewFragment fragment = ReviewFragment.newInstance(this);
		// ProgressFragment fragment = new CustomerPageFragment(null);

		Bundle args = new Bundle();

		fragment.setArguments(args);
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		loadMap(); 
		requestData();
		obtainData();
	/*	loadMap();  // This loads the map onto nearby , and sets it to on click listener
		
		String result = "";
		try {
			result = webApiManager.getReview((Integer
					.parseInt(storeID)));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Deserialize  deserializer = new  Deserialize();
		dataStorageManager.SelectedMerchantReviews = deserializer.getReviewList(result);		
		
		if (!dataStorageManager.SelectedMerchantReviews.isEmpty()) {			
			View noReviewView = findViewById(R.id.noReviewsYet);
			noReviewView.setVisibility(View.GONE);
			
		    LayoutInflater layoutInflater = 
		    	      (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
		    LinearLayout container = (LinearLayout)findViewById(R.id.container);
		    
		    String replyResult = "";
			try {
				replyResult = webApiManager.getReplies(Integer.parseInt(storeID));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    if(replyResult == null) {
		    	dataStorageManager.merchantReply = new ArrayList<Reply>();
		    } else if(replyResult.equalsIgnoreCase("\"Wrong Token\"")) {
		    	dataStorageManager.merchantReply = new ArrayList<Reply>();
		    } else {
		    	dataStorageManager.merchantReply = deserializer.getReplyList(replyResult);
		    }
		    //Add reviews
			for(Review item : dataStorageManager.SelectedMerchantReviews) {
			    final View addView = layoutInflater.inflate(R.layout.list_item_review_reply, null);
			    
				TextView ReviewDate= (TextView) addView.findViewById(R.id.ReviewDate);
				TextView ReviewerName= (TextView) addView.findViewById(R.id.ReviewerName);
				TextView ReviewText= (TextView) addView.findViewById(R.id.ReviewText);
				TextView RestaurantName = (TextView) addView.findViewById(R.id.RestaurantName);			    
			    
		       String formattedTime = parseDate(item.DateCreated);
		       ReviewDate.setText(formattedTime);
		        //BE PROACTIVE
		  //     String temp = "ALIBABA";
		      // RestaurantName.setText(temp);
		        RestaurantName.setText(item.MerchantName);
		        ReviewerName.setText("Reviewed by: " + item.NickName);
		        ReviewText.setText(item.Remark);
		        if(item.Item1Score == 0 ) {
		        	TextView textView4 = (TextView) addView.findViewById(R.id.textView4);
		        	textView4.setVisibility(View.GONE);
		        	LinearLayout foodLinearLayout = (LinearLayout)addView.findViewById(R.id.foodLinearLayout);
		        	foodLinearLayout.setVisibility(View.GONE);
		        } else {
		        	setUpFood(addView, item);
		        }
		        setUpService(addView, item);
		        setUpAmbience(addView, item);
		        setUpMainStar(addView, item);
		        
		        addView.findViewById(R.id.ReviewAddDetail).setVisibility(View.GONE);
		        
		        addView.setOnClickListener(new View.OnClickListener() {

		            public void onClick(View v) {
						View reviewAddDetail = v.findViewById(R.id.ReviewAddDetail);
						if(!reviewAddDetail.isShown()){
							reviewAddDetail.setVisibility(View.VISIBLE);
							slide_down(context, reviewAddDetail);					
						} else {
							slide_up(context, reviewAddDetail);
							reviewAddDetail.setVisibility(View.GONE);
						}
						
		            }
		        });
		        
		        
		        if(dataStorageManager.merchantReply.isEmpty()){
		        	addView.findViewById(R.id.reply).setVisibility(View.GONE);
		        } else if(checkReply(dataStorageManager.merchantReply, item)) {
		        	Reply currentReply = getReply(dataStorageManager.merchantReply, item);
		        	TextView replyDate = (TextView) addView.findViewById(R.id.replyDate);
		        	TextView replyName = (TextView) addView.findViewById(R.id.ReplyName);
		        	TextView replyText = (TextView) addView.findViewById(R.id.ReplyText);
		        	
		        	
		        	String replyTime = currentReply.DateCreated;
		        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CANADA);
		        	Date replyFormatDate = new Date();
					try {
						replyFormatDate = sdf.parse(replyTime);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        	replyDate.setText(parseDate(replyFormatDate));
		        	replyName.setText(dataStorageManager.selectedMerchant.Name);
		        	replyText.setText(currentReply.Remark);
		        	
		        } else {
		        	addView.findViewById(R.id.reply).setVisibility(View.GONE);
		        }
	        	
	        	container.addView(addView);
			}

		}*/ } catch (Exception e) {
			e.printStackTrace();
		}
    }	
 
    /*
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    	  if (requestCode == 1) {

    	     if(resultCode == RESULT_OK) {      
    	 		String result = "";
    			try {
    				result = webApiManager.getReview((dataStorageManager.selectedMerchant.StoreID));
    			} catch (NumberFormatException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (ExecutionException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		
    			if(result == null) {
    				return;
    			}
    			
    			List<Review> updatedReviewList = Deserialize.getReviewList(result);
    			if (!updatedReviewList.isEmpty()) {
    				Review submitReview = updatedReviewList.get(0);
    				
    			    LayoutInflater layoutInflater = 
    			    	      (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    				
    			    LinearLayout container = (LinearLayout)findViewById(R.id.container);    				
    				
    			    final View addView = layoutInflater.inflate(R.layout.list_item_review_reply, null);
    			    
    				TextView ReviewDate= (TextView) addView.findViewById(R.id.ReviewDate);
    				TextView ReviewerName= (TextView) addView.findViewById(R.id.ReviewerName);
    				TextView ReviewText= (TextView) addView.findViewById(R.id.ReviewText);
    				TextView RestaurantName = (TextView) addView.findViewById(R.id.RestaurantName);			    
    			    
    		       String formattedTime = parseDate(submitReview.DateCreated);
    		       ReviewDate.setText(formattedTime);
    		        
    		        RestaurantName.setText("Restaurant Name: " + submitReview.MerchantName);
    		        ReviewerName.setText("Reviewed by: " + submitReview.NickName);
    		        ReviewText.setText(submitReview.Remark);
    		        if(submitReview.Item1Score == 0 ) {
    		        	TextView textView4 = (TextView) addView.findViewById(R.id.textView4);
    		        	textView4.setVisibility(View.GONE);
    		        	LinearLayout foodLinearLayout = (LinearLayout)addView.findViewById(R.id.foodLinearLayout);
    		        	foodLinearLayout.setVisibility(View.GONE);
    		        } else {
    		        	setUpFood(addView, submitReview);
    		        }
    		        setUpService(addView, submitReview);
    		        setUpAmbience(addView, submitReview);
    		        setUpMainStar(addView, submitReview);
    		        
    		        addView.findViewById(R.id.ReviewAddDetail).setVisibility(View.GONE);
    		        
    		        addView.setOnClickListener(new View.OnClickListener() {

    		            public void onClick(View v) {
    						View reviewAddDetail = v.findViewById(R.id.ReviewAddDetail);
    						if(!reviewAddDetail.isShown()){
    							reviewAddDetail.setVisibility(View.VISIBLE);
    							slide_down(context, reviewAddDetail);					
    						} else {
    							slide_up(context, reviewAddDetail);
    							reviewAddDetail.setVisibility(View.GONE);
    						}
    						
    		            }
    		        });
    		        addView.findViewById(R.id.reply).setVisibility(View.GONE);
    		        container.addView(addView);
    			}
    	    	 
    	     }
    	     if(resultCode == RESULT_CANCELED) {    

    	     }
    	  }
    	}
    	*/
    // if empty string hides comment text Views, if comments than set TextView
    private void requestData()
    {
        if(dataStorageManager.loggedIn){


                 String temp_merchantStoreID = Integer.toString(dataStorageManager.selectedMerchant.StoreID);
            
        	
				try {
					DataStorageManager.getSingletonInstance().setSerializationListType(EnumData.ListType.CheckFavourite);
					WebApiManagerPageFragment.getSingletonInstance().checkFavorite(dataStorageManager.currentUser.CustomerID, temp_merchantStoreID);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }

    	
    }
    private void obtainData()
    {
    		mHandler = new Handler();
    		mHandler.postDelayed(mShowContentRunnable, 1000);
    }
    private void checkForComments(String comments)
    {
    	boolean isComments = false;
    	if(comments == null || comments.equalsIgnoreCase("")) {
    		TextView storeCommentsDetails = (TextView) findViewById(R.id.storeCommentsDetails);
			TextView storeComments = (TextView)findViewById(R.id.storeComments);
			storeCommentsDetails.setVisibility(View.GONE);  
			storeComments.setVisibility(View.GONE);
			return;
    	} else // if comments does not equal empty string
    	{
    		isComments = true;
    	}    	
    	TextView storeCommentsDetails = (TextView) findViewById(R.id.storeCommentsDetails);
    	if(isComments)
		{
			storeCommentsDetails.setText(comments);
		}
		else{
			TextView storeComments = (TextView)findViewById(R.id.storeComments);
			storeCommentsDetails.setVisibility(View.GONE);  
			storeComments.setVisibility(View.GONE);
			
		}
    }
	
	private boolean checkReply (List<Reply> list, Review review) {
		for(Reply reply : list) {
			if(reply.ReviewID == review.ReviewID) {
				return true;
			}
		}
		return false;
	}
   
	private Reply getReply (List<Reply> list, Review review) {
		for(Reply reply : list) {
			if(reply.ReviewID == review.ReviewID) {
				return reply;
			}
		}
		return new Reply();		
	}
	
	private void setUpPriceLegend(int priceLegend,
			final View view) {
		switch (priceLegend) {
		case 2:
			break;
		case 3:
			setPicture(view, R.id.imageView6,
					"md_dollar_ful");
			break;
		case 4:
			setPicture(view, R.id.imageView6,
					"md_dollar_ful");
			setPicture(view, R.id.imageView7,
					"md_dollar_ful");
			break;
		case 5:
			setPicture(view, R.id.imageView6,
					"md_dollar_ful");
			setPicture(view, R.id.imageView7,
					"md_dollar_ful");
			setPicture(view, R.id.imageView7,
					"md_dollar_right");
			break;
		default:
			break;
		}
	}

	private void setUpScore(int score, final View view) {
		switch (score) {
		case 0:
			break;
		case 1:
			setPicture(view, R.id.imageView9,
					"md_left_star_half");
			break;
		case 2:
			setPicture(view, R.id.imageView9,
					"md_left_star_full");

			break;
		case 3:
			setPicture(view, R.id.imageView9,
					"md_left_star_full");
			setPicture(view, R.id.imageView10,
					"md_mid_star_half");
			break;
		case 4:

			setPicture(view, R.id.imageView9,
					"md_left_star_full");
			setPicture(view, R.id.imageView10,
					"md_mid_star_full");

			break;
		case 5:

			setPicture(view, R.id.imageView9,
					"md_left_star_full");
			setPicture(view, R.id.imageView10,
					"md_mid_star_full");
			setPicture(view, R.id.imageView11,
					"md_mid_star_half");

		case 6:
			setPicture(view, R.id.imageView9,
					"md_left_star_full");
			setPicture(view, R.id.imageView10,
					"md_mid_star_full");
			setPicture(view, R.id.imageView11,
					"md_mid_star_full");

			break;
		case 7:
			setPicture(view, R.id.imageView9,
					"md_left_star_full");
			setPicture(view, R.id.imageView10,
					"md_mid_star_full");
			setPicture(view, R.id.imageView11,
					"md_mid_star_full");
			setPicture(view, R.id.imageView12,
					"md_mid_star_half");
			break;
		case 8:

			setPicture(view, R.id.imageView9,
					"md_left_star_full");
			setPicture(view, R.id.imageView10,
					"md_mid_star_full");
			setPicture(view, R.id.imageView11,
					"md_mid_star_full");
			setPicture(view, R.id.imageView12,
					"md_mid_star_full");

			break;
		case 9:

			setPicture(view, R.id.imageView9,
					"md_left_star_full");
			setPicture(view, R.id.imageView10,
					"md_mid_star_full");
			setPicture(view, R.id.imageView11,
					"md_mid_star_full");
			setPicture(view, R.id.imageView12,
					"md_mid_star_full");
			setPicture(view, R.id.imageView13,
					"md_right_star_half");
			break;
		case 10:

			setPicture(view, R.id.imageView9,
					"md_left_star_full");
			setPicture(view, R.id.imageView10,
					"md_mid_star_full");
			setPicture(view, R.id.imageView11,
					"md_mid_star_full");
			setPicture(view, R.id.imageView12,
					"md_mid_star_full");
			setPicture(view, R.id.imageView13,
					"md_right_star_full");

			break;
		default:
			break;
		}
	}
	
    private boolean IsConneted()
    {
		ConnectivityManager connMgr = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		Boolean result = true;
		if(networkInfo == null)
		{
			result=false;
		}
		return networkInfo != null;
    }
    
    private void DisplayNoInternetImage()
    {
		setContentView(R.layout.fragment_update_app);
    }
	
	private void loadMap()
	{
	    if(	dataStorageManager.currentLocation == null)
	    {
	    	dataStorageManager.currentLocation= MainScreenActivity.mLocationClient.getLastLocation();
	    }
	    Location mCurrentLocation= dataStorageManager.currentLocation;
		if(mCurrentLocation ==null)
			loadMapWithOutGPS( mCurrentLocation);
		else
			loadMapWithGPS( mCurrentLocation);
	}
	
	private void loadMapWithGPS(Location mCurrentLocation)
	{
		   //mLocationClient = new LocationClient(this, this, this);
	    //System.out.println(mLocationClient.getLastLocation());
		GoogleMap map = ((MapFragment) getFragmentManager()
				.findFragmentById(R.id.map_fragment)).getMap();
	    Double MerchantLongitude = Double
				.parseDouble(dataStorageManager.selectedMerchant.Longitude);
	    Double MerchantLatitude = Double
	    		.parseDouble((String) dataStorageManager.selectedMerchant.Latitude);
	    Double ClientLongitude = mCurrentLocation.getLongitude();
	    Double ClientLatitude = mCurrentLocation.getLatitude();
	    String MerchantName = dataStorageManager.selectedMerchant.Name;
	
	    //mainActivity.mCurrentLocation = mainActivity.mLocationClient
	    //		.getLastLocation();
	
	    map.addMarker(
		new MarkerOptions().position(
				new LatLng(MerchantLatitude, MerchantLongitude))
				.title(MerchantName)).showInfoWindow();
	    
	    
	    map.addMarker(
		new MarkerOptions().position(
				new LatLng(ClientLatitude, ClientLongitude)).title(
				"You are here")).showInfoWindow();
	    //WTF quick Fix
	    /*  LatLng southwest = new LatLng(Math.min(MerchantLatitude,
				ClientLatitude - Math.abs(MerchantLatitude -ClientLatitude )), Math.min(MerchantLongitude,
				ClientLongitude- Math.abs(MerchantLongitude -ClientLongitude )));
		LatLng northeast = new LatLng(Math.max(MerchantLatitude,
				ClientLatitude)+ Math.abs(MerchantLatitude -ClientLatitude ), Math.max(MerchantLongitude,
				ClientLongitude)+Math.abs(MerchantLongitude -ClientLongitude ));
		*/
		LatLng southwest = new LatLng(Math.min(MerchantLatitude,
				ClientLatitude), Math.min(MerchantLongitude,
				ClientLongitude));
		LatLng northeast = new LatLng(Math.max(MerchantLatitude,
				ClientLatitude), Math.max(MerchantLongitude,
				ClientLongitude));
		map.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(southwest, northeast), 400,125,0));
	/*	map.moveCamera(CameraUpdateFactory.newLatLngBounds(
				new LatLngBounds(southwest, northeast), 900, 500, 0));
		map.animateCamera( CameraUpdateFactory.zoomTo( 2.0f ) );  */
		map.setOnMapClickListener(new OnMapClickListener(){

			@Override
			public void onMapClick(LatLng point) {
				Intent intent = new Intent(MerchantDetailActivity.this, MerchantMapActivity.class);
				startActivity(intent);
				
			}
	
		});
	}

	private void loadMapWithOutGPS(Location mCurrentLocation)
	{
		   //mLocationClient = new LocationClient(this, this, this);
	    //System.out.println(mLocationClient.getLastLocation());
		GoogleMap map = ((MapFragment) getFragmentManager()
				.findFragmentById(R.id.map_fragment)).getMap();
	    Double MerchantLongitude = Double
				.parseDouble(dataStorageManager.selectedMerchant.Longitude);
	    Double MerchantLatitude = Double
	    		.parseDouble((String) dataStorageManager.selectedMerchant.Latitude);
	    String MerchantName = dataStorageManager.selectedMerchant.Name;
	
	    //mainActivity.mCurrentLocation = mainActivity.mLocationClient
	    //		.getLastLocation();
	
	    map.addMarker(
		new MarkerOptions().position(
				new LatLng(MerchantLatitude, MerchantLongitude)));
				//.title(MerchantName)).showInfoWindow();
	    
	
	    //WTF quick Fix
	    /*  LatLng southwest = new LatLng(Math.min(MerchantLatitude,
				ClientLatitude - Math.abs(MerchantLatitude -ClientLatitude )), Math.min(MerchantLongitude,
				ClientLongitude- Math.abs(MerchantLongitude -ClientLongitude )));
		LatLng northeast = new LatLng(Math.max(MerchantLatitude,
				ClientLatitude)+ Math.abs(MerchantLatitude -ClientLatitude ), Math.max(MerchantLongitude,
				ClientLongitude)+Math.abs(MerchantLongitude -ClientLongitude ));
		*/

		
		 LatLng latLng = new LatLng(MerchantLatitude, MerchantLongitude);
		    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 13);
		  map.moveCamera(cameraUpdate );
	/*	map.moveCamera(CameraUpdateFactory.newLatLngBounds(
				new LatLngBounds(southwest, northeast), 900, 500, 0));
		map.animateCamera( CameraUpdateFactory.zoomTo( 2.0f ) );  */
		map.setOnMapClickListener(new OnMapClickListener(){

			@Override
			public void onMapClick(LatLng point) {
				Intent intent = new Intent(MerchantDetailActivity.this, MerchantMapActivity.class);
				startActivity(intent);
				
			}
	
		});
	}
	
	private void setPicture(View parent, int id, String picture) {
		ImageView IV = (ImageView) parent.findViewById(id);
		IV.setImageResource(this.getResources().getIdentifier(picture,
				"drawable",
				getApplicationContext().getPackageName()));
	}	
	
	public static void getItemHeightofListView(ListView listView) {

	    ListAdapter mAdapter = listView.getAdapter();

	    int totalHeight = 0;

	    for (int i = 0; i < mAdapter.getCount(); i++) {
	        View mView = mAdapter.getView(i, null, listView);

	        mView.measure(
	                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),

	                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

	        totalHeight += mView.getMeasuredHeight();
	    }

	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    params.height = totalHeight;
	    //        + (listView.getDividerHeight() * (mAdapter.getCount()-1));
	    params.height = 2 * params.height - (listView.getDividerHeight() * mAdapter.getCount());
	    listView.setLayoutParams(params);
	    listView.requestLayout();

	}
		
	//BE PROACTIVE	
	/*
	public void resetMerchantBar() {
		findViewById(R.id.merchantButton1).setBackgroundColor(getResources().getColor(R.color.LightGrey));
		findViewById(R.id.merchantButton2).setBackgroundColor(getResources().getColor(R.color.LightGrey));
		findViewById(R.id.merchantButton3).setBackgroundColor(getResources().getColor(R.color.LightGrey));
		findViewById(R.id.merchantButton4).setBackgroundColor(getResources().getColor(R.color.LightGrey));
		((ImageView) findViewById(R.id.merchantButton1image)).setImageResource(R.drawable.checkin_inactive);
		((ImageView) findViewById(R.id.merchantButton2image)).setImageResource(R.drawable.information_inactive);
		((ImageView) findViewById(R.id.merchantButton3image)).setImageResource(R.drawable.reviews_inactive);
		((ImageView) findViewById(R.id.merchantButton4image)).setImageResource(R.drawable.gallery_inactive);
		findViewById(R.id.merchantArrow1).setVisibility(View.INVISIBLE);
		findViewById(R.id.merchantArrow2).setVisibility(View.INVISIBLE);
		findViewById(R.id.merchantArrow3).setVisibility(View.INVISIBLE);
		findViewById(R.id.merchantArrow4).setVisibility(View.INVISIBLE);
	}	
	*/
	//BE PROACTIVE
	public String parseDate(Date oldDate)
	{
		 SimpleDateFormat tmp = new SimpleDateFormat("d", Locale.CANADA);
			String date_t = tmp.format(oldDate);  
	        
	        
	        if(date_t.endsWith("1") && !date_t.endsWith("11"))
	            tmp = new SimpleDateFormat("MMM d'st', yyyy - h:mm a", Locale.CANADA);
	        else if(date_t.endsWith("2") && !date_t.endsWith("12"))
	            tmp = new SimpleDateFormat("MMM d'nd', yyyy - h:mm a", Locale.CANADA);
	        else if(date_t.endsWith("3") && !date_t.endsWith("13"))
	            tmp = new SimpleDateFormat("MMM d'rd', yyyy - h:mm a", Locale.CANADA);
	        else 
	            tmp = new SimpleDateFormat("MMM d'th', yyyy - h:mm a", Locale.CANADA);
	        
	        String formattedTime = tmp.format(oldDate);   // changes temp the old date with newest tmp
		
		
		return formattedTime;
	}
	
	public static void slide_down(Context ctx, View v){
		Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);
			if(a != null){
			a.reset();
				if(v != null){
				v.clearAnimation();
				v.startAnimation(a);
				}
			}
	}
	
	public static void slide_up(Context ctx, View v){
		Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);
			if(a != null){
			a.reset();
				if(v != null){
				v.clearAnimation();
				v.startAnimation(a);
				}
			}
	}	
		
	private void setUpFood(View rowView, Review item)
	{
		ImageView firstStar = (ImageView) rowView.findViewById(R.id.imageView1);
		ImageView secondStar = (ImageView) rowView.findViewById(R.id.imageView2);
		ImageView thirdStar = (ImageView) rowView.findViewById(R.id.imageView3);
		ImageView fourthStar = (ImageView) rowView.findViewById(R.id.imageView4);
		ImageView fifthStar = (ImageView) rowView.findViewById(R.id.imageView5);
		setupStars(firstStar, secondStar, thirdStar, fourthStar,fifthStar , item.Item1Score);
	}
	
	private void  setUpService(View rowView, Review item)
	{
		ImageView firstStar = (ImageView) rowView.findViewById(R.id.imageView6);
		ImageView secondStar = (ImageView) rowView.findViewById(R.id.imageView7);
		ImageView thirdStar = (ImageView) rowView.findViewById(R.id.imageView8);
		ImageView fourthStar = (ImageView) rowView.findViewById(R.id.imageView9);
		ImageView fifthStar = (ImageView) rowView.findViewById(R.id.imageView10);
		setupStars(firstStar, secondStar, thirdStar, fourthStar,fifthStar , item.Item2Score);
	}
	
	private void setUpAmbience(View rowView, Review item)
	{
		
		ImageView firstStar = (ImageView) rowView.findViewById(R.id.imageView11);
		ImageView secondStar = (ImageView) rowView.findViewById(R.id.imageView12);
		ImageView thirdStar = (ImageView) rowView.findViewById(R.id.imageView13);
		ImageView fourthStar = (ImageView) rowView.findViewById(R.id.imageView14);
		ImageView fifthStar = (ImageView) rowView.findViewById(R.id.imageView15);
		setupStars(firstStar, secondStar, thirdStar, fourthStar,fifthStar , item.Item3Score);
	}
	
	private void setUpMainStar(View rowView, Review item)
	{
		//Takes the average of the 3 ratings.
		ImageView firstStar = (ImageView) rowView.findViewById(R.id.imageView16);
		ImageView secondStar = (ImageView) rowView.findViewById(R.id.imageView17);
		ImageView thirdStar = (ImageView) rowView.findViewById(R.id.imageView18);
		ImageView fourthStar = (ImageView) rowView.findViewById(R.id.imageView19);
		ImageView fifthStar = (ImageView) rowView.findViewById(R.id.imageView20);
		setupStars(firstStar, secondStar, thirdStar, fourthStar,fifthStar , item.Score);
	}
	
	
	private void setupStars(ImageView first ,ImageView second,ImageView third,ImageView fourth,ImageView fifth, int rating ){
		switch (rating) {
		case 0:
			break;
		case 1:
			first.setImageResource(half_star);
			break;
		case 2:
			first.setImageResource(yellow_star);

			break;
		case 3:
			first.setImageResource(yellow_star);
			second.setImageResource(half_star);
			break;
		case 4:
			first.setImageResource(yellow_star);
			second.setImageResource(yellow_star);
			break;
		case 5:
			first.setImageResource(yellow_star);
			second.setImageResource(yellow_star);
			third.setImageResource(half_star);
		case 6:
			first.setImageResource(yellow_star);
			second.setImageResource(yellow_star);
			third.setImageResource(yellow_star);
			break;
		case 7:
			first.setImageResource(yellow_star);
			second.setImageResource(yellow_star);
			third.setImageResource(yellow_star);
			fourth.setImageResource(half_star);
			break;
		case 8:
			first.setImageResource(yellow_star);
			second.setImageResource(yellow_star);
			third.setImageResource(yellow_star);
			fourth.setImageResource(yellow_star);
			break;
		case 9:

			first.setImageResource(yellow_star);
			second.setImageResource(yellow_star);
			third.setImageResource(yellow_star);
			fourth.setImageResource(yellow_star);
			fifth.setImageResource(half_star);
			break;
		case 10:

			first.setImageResource(yellow_star);
			second.setImageResource(yellow_star);
			third.setImageResource(yellow_star);
			fourth.setImageResource(yellow_star);
			fifth.setImageResource(yellow_star);
			break;
		default:
			break;
		}
		
	}	
	
}

