package ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import com.qooway.consumerv01.R;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import data.Serialize;
import data.WebApiManager;
import data.DataStorageManager;
import framework.DataObject.PostReview;
import framework.QoowayActivity;

public class WriteReviewActivity extends QoowayActivity  {
    
	//private MainScreenActivity mainActivity;
	//private DataStorageManager dataStorageManager = mainActivity.dataStorageManager;
	final Context context = this;
	private boolean anonymous = false;
	public String httpserverUrl = "online.profitek.com/appdevelopment";
	public String httpsserverUrl = "online.profitek.com/appdevelopment";		
	public WebApiManager webApiManager;
	public DataStorageManager dataStorageManager;
	private String review = "";
	private int foodScore = 0;
	private int serviceScore = 0;
	private int ambienceScore = 0;
	private int anonymousScore = 0;
	
		@Override
	    public boolean onCreateOptionsMenu(Menu menu) {
			MenuInflater inflater = getMenuInflater();
	         inflater.inflate(R.menu.main_review, menu);
	         
	         return super.onCreateOptionsMenu(menu);
	      }    
      
	  @Override
	  public boolean onOptionsItemSelected(MenuItem menuItem)
	  {       
	      switch (menuItem.getItemId()) {
	      // Respond to the action bar's Up/Home button
	      case android.R.id.home:
	    	  Intent returnIntent = new Intent();
	    	  setResult(RESULT_CANCELED, returnIntent);   
	          finish();
	          return true;
	          
	      case R.id.anonymousIcon:

	    	  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

	    	  if (!anonymous){
	  			// set title
	  			alertDialogBuilder.setTitle("Make Anonymous");
	   
	  			// set dialog message
	  			alertDialogBuilder
	  				.setMessage("Would you like to make your review anonymous?")
	  				.setCancelable(false)
	  				.setPositiveButton("Confirm",new DialogInterface.OnClickListener() {
	  					public void onClick(DialogInterface dialog,int id) {
	  						// if this button is clicked, close
	  						// current activity
	  						Toast.makeText(getApplicationContext(),
	  							"Your review is now anonymous.", Toast.LENGTH_SHORT).show();
	  						anonymous = true;
	  						getActionBar().setTitle("Anonymous");
	  						dialog.cancel();
	  					}
	  				  })
	  				.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
	  					public void onClick(DialogInterface dialog,int id) {
	  						// if this button is clicked, just close
	  						// the dialog box and do nothing
	  						dialog.cancel();
	  					}
	  				});
	    	  } else {
		  			alertDialogBuilder.setTitle("Remove Anonymous");
		  		   
		  			// set dialog message
		  			alertDialogBuilder
		  				.setMessage("Would you like to remove anonymous status from your review?")
		  				.setCancelable(false)
		  				.setPositiveButton("Confirm",new DialogInterface.OnClickListener() {
		  					public void onClick(DialogInterface dialog,int id) {
		  						// if this button is clicked, close
		  						// current activity
		  						Toast.makeText(getApplicationContext(),
		  							"Your review is no longer anonymous.", Toast.LENGTH_SHORT).show();
		  						anonymous = false;
		  						getActionBar().setTitle("By " + DataStorageManager.getSingletonInstance().getDisplayName());
		  						dialog.cancel();
		  					}
		  				  })
		  				.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
		  					public void onClick(DialogInterface dialog,int id) {
		  						// if this button is clicked, just close
		  						// the dialog box and do nothing
		  						dialog.cancel();
		  					}
		  				});  
	    	  }
				// create alert dialog
	  				AlertDialog alertDialog = alertDialogBuilder.create();
	   
	  				// show it
				alertDialog.show();
	    	  
	          return true;
	      }
	      
	      return super.onOptionsItemSelected(menuItem);
	  } 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	 	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_write_review);
        
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
        //getActionBar().setDisplayHomeAsUpEnabled(true);     

        webApiManager = WebApiManager.getSingletonInstance();
		dataStorageManager = DataStorageManager.getSingletonInstance();
        
		final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
		scrollView.post(new Runnable() {

		    public void run() {
		        scrollView.scrollTo(0,0 );
		    }
		});

        if(dataStorageManager.currentUser.FirstName!= "" &&  dataStorageManager.currentUser.LasName!= ""){
        	String displayName = dataStorageManager.currentUser.FirstName + " " + dataStorageManager.currentUser.LasName.charAt(0) + ".";
        	setTitle("By " + displayName);
        } else {
        String displayName = dataStorageManager.currentUser.Email;
        setTitle("By " + displayName);
        }
        
        String merchantType = dataStorageManager.selectedMerchant.MerchantType;

		//If selected merchant is not a restaurant, there is no food rating.
		if(merchantType.equals("T")){
			findViewById(R.id.foodRating).setVisibility(View.GONE);
		}
		

		//Adds character counter to the edit text box.
		
		EditText mEditText = (EditText) findViewById(R.id.editText);
		TextWatcher mTextEditorWatcher = new TextWatcher() {
		        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		        }

		        public void onTextChanged(CharSequence s, int start, int before, int count) {
		        	//This sets a textview to the current length
		        	TextView mTextView = (TextView) findViewById(R.id.textCount);
		        	mTextView.setText(String.valueOf(s.length()) + "/2000");
		        }

		        public void afterTextChanged(Editable s) {
		        }
		};
		
		mEditText.addTextChangedListener(mTextEditorWatcher);
		

		//Submit Review
		ImageButton submitButton = (ImageButton) findViewById(R.id.reviewSubmitButton);
		submitButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	//Toast.makeText(getApplicationContext(),
				//		"Review Submitted.", Toast.LENGTH_SHORT).show();
		    	
		    	
		    	
		    	EditText mEdit = (EditText)findViewById(R.id.editText);
		    	review = mEdit.getText().toString();
		    	
		    	Date today = Calendar.getInstance().getTime(); 
		    	
		    	String displayName = "";
		        if(dataStorageManager.currentUser.FirstName!= "" &&  dataStorageManager.currentUser.LasName!= ""){
		        	displayName = dataStorageManager.currentUser.FirstName + " " + dataStorageManager.currentUser.LasName.charAt(0) + ".";
		        } else {
		        	displayName = dataStorageManager.currentUser.Email;
		        }
		        		    	
		    	anonymousScore = anonymous ? 1 : 0;
		    
		    	insertReview(Integer.toString(dataStorageManager.selectedMerchant.StoreID), dataStorageManager.currentUser.CustomerID, 
		    			review, today, displayName, foodScore, serviceScore, ambienceScore, anonymousScore);
		    	
		    	Intent returnIntent = new Intent();
		    	returnIntent.putExtra("result", "OK");
		    	setResult(RESULT_OK,returnIntent);     
		    	finish(); 	
		    }
		});

		int[]hitBoxFood = new int[] {
				R.id.HitBox1, R.id.HitBox2, R.id.HitBox3, R.id.HitBox4, R.id.HitBox5,
				R.id.HitBox6, R.id.HitBox7, R.id.HitBox8, R.id.HitBox9, R.id.HitBox10,
		};
		
		int[]hitBoxService = new int[] {
				R.id.HitBox11, R.id.HitBox12, R.id.HitBox13, R.id.HitBox14, R.id.HitBox15,
				R.id.HitBox16, R.id.HitBox17, R.id.HitBox18, R.id.HitBox19, R.id.HitBox20,
		};
		
		int[]hitBoxAmbience = new int[] {
				R.id.HitBox21, R.id.HitBox22, R.id.HitBox23, R.id.HitBox24, R.id.HitBox25,
				R.id.HitBox26, R.id.HitBox27, R.id.HitBox28, R.id.HitBox29, R.id.HitBox30
		};
		
		ImageView b;
		for(int i = 0; i < hitBoxFood.length; i++){
	        final int index = (i + 1);
	        b = (ImageView) findViewById(hitBoxFood[i]);
	        b.setOnClickListener(new ImageView.OnClickListener() {
	            @Override
	            public void onClick(View v) {
			    	setupFood(findViewById(R.id.writeReviewPage), index);
			    	foodScore = index;
	            }
	        });
	    };
	    
		for(int i = 0; i < hitBoxService.length; i++){
	        final int index = (i + 1);
	        b = (ImageView) findViewById(hitBoxService[i]);
	        b.setOnClickListener(new ImageView.OnClickListener() {
	            @Override
	            public void onClick(View v) {
			    	setupService(findViewById(R.id.writeReviewPage), index);
			    	serviceScore = index;
	            }
	        });
	    };	    
	    
		for(int i = 0; i < hitBoxAmbience.length; i++){
	        final int index = (i + 1);
	        b = (ImageView) findViewById(hitBoxAmbience[i]);
	        b.setOnClickListener(new ImageView.OnClickListener() {
	            @Override
	            public void onClick(View v) {
			    	setupAmbience(findViewById(R.id.writeReviewPage), index);
			    	ambienceScore = index;
	            }
	        });
	    };		    
		
    }
    
   private void setTitle(String title){
	   getActionBar().setTitle(title);
   }
    
	private void setupFood(View view, int rating){
		ImageView firstStar = (ImageView) view.findViewById(R.id.ReviewStar1);
		ImageView secondStar = (ImageView) view.findViewById(R.id.ReviewStar2);
		ImageView thirdStar = (ImageView) view.findViewById(R.id.ReviewStar3);
		ImageView fourthStar = (ImageView) view.findViewById(R.id.ReviewStar4);
		ImageView fifthStar = (ImageView) view.findViewById(R.id.ReviewStar5);
		setupStars(firstStar, secondStar, thirdStar, fourthStar,fifthStar , rating);
	}
	
	private void setupService(View view, int rating){
		ImageView firstStar = (ImageView) view.findViewById(R.id.ReviewStar6);
		ImageView secondStar = (ImageView) view.findViewById(R.id.ReviewStar7);
		ImageView thirdStar = (ImageView) view.findViewById(R.id.ReviewStar8);
		ImageView fourthStar = (ImageView) view.findViewById(R.id.ReviewStar9);
		ImageView fifthStar = (ImageView) view.findViewById(R.id.ReviewStar10);
		setupStars(firstStar, secondStar, thirdStar, fourthStar,fifthStar , rating);
	}
	
	private void setupAmbience(View view, int rating){
		ImageView firstStar = (ImageView) view.findViewById(R.id.ReviewStar11);
		ImageView secondStar = (ImageView) view.findViewById(R.id.ReviewStar12);
		ImageView thirdStar = (ImageView) view.findViewById(R.id.ReviewStar13);
		ImageView fourthStar = (ImageView) view.findViewById(R.id.ReviewStar14);
		ImageView fifthStar = (ImageView) view.findViewById(R.id.ReviewStar15);
		setupStars(firstStar, secondStar, thirdStar, fourthStar,fifthStar , rating);
	}
	
	private void setupStars(ImageView first ,ImageView second,ImageView third,ImageView fourth,ImageView fifth, int rating )
	{
		
		switch (rating) {
		case 0:
			first.setImageResource(R.drawable.reviews_large_greystar);
			second.setImageResource(R.drawable.reviews_large_greystar);
			third.setImageResource(R.drawable.reviews_large_greystar);
			fourth.setImageResource(R.drawable.reviews_large_greystar);
			fifth.setImageResource(R.drawable.reviews_large_greystar);			
			break;
		case 1:
			first.setImageResource(R.drawable.reviews_large_halfstar);
			second.setImageResource(R.drawable.reviews_large_greystar);
			third.setImageResource(R.drawable.reviews_large_greystar);
			fourth.setImageResource(R.drawable.reviews_large_greystar);
			fifth.setImageResource(R.drawable.reviews_large_greystar);
			break;
		case 2:
			first.setImageResource(R.drawable.reviews_large_yellowstar);
			second.setImageResource(R.drawable.reviews_large_greystar);
			third.setImageResource(R.drawable.reviews_large_greystar);
			fourth.setImageResource(R.drawable.reviews_large_greystar);
			fifth.setImageResource(R.drawable.reviews_large_greystar);
			break;
		case 3:
			first.setImageResource(R.drawable.reviews_large_yellowstar);
			second.setImageResource(R.drawable.reviews_large_halfstar);
			third.setImageResource(R.drawable.reviews_large_greystar);
			fourth.setImageResource(R.drawable.reviews_large_greystar);
			fifth.setImageResource(R.drawable.reviews_large_greystar);
			break;
		case 4:
			first.setImageResource(R.drawable.reviews_large_yellowstar);
			second.setImageResource(R.drawable.reviews_large_yellowstar);
			third.setImageResource(R.drawable.reviews_large_greystar);
			fourth.setImageResource(R.drawable.reviews_large_greystar);
			fifth.setImageResource(R.drawable.reviews_large_greystar);
			break;
		case 5:
			first.setImageResource(R.drawable.reviews_large_yellowstar);
			second.setImageResource(R.drawable.reviews_large_yellowstar);
			third.setImageResource(R.drawable.reviews_large_halfstar);
			fourth.setImageResource(R.drawable.reviews_large_greystar);
			fifth.setImageResource(R.drawable.reviews_large_greystar);
			break;
		case 6:
			first.setImageResource(R.drawable.reviews_large_yellowstar);
			second.setImageResource(R.drawable.reviews_large_yellowstar);
			third.setImageResource(R.drawable.reviews_large_yellowstar);
			fourth.setImageResource(R.drawable.reviews_large_greystar);
			fifth.setImageResource(R.drawable.reviews_large_greystar);
			break;			
		case 7:
			first.setImageResource(R.drawable.reviews_large_yellowstar);
			second.setImageResource(R.drawable.reviews_large_yellowstar);
			third.setImageResource(R.drawable.reviews_large_yellowstar);
			fourth.setImageResource(R.drawable.reviews_large_halfstar);
			fifth.setImageResource(R.drawable.reviews_large_greystar);
			break;			
		case 8:
			first.setImageResource(R.drawable.reviews_large_yellowstar);
			second.setImageResource(R.drawable.reviews_large_yellowstar);
			third.setImageResource(R.drawable.reviews_large_yellowstar);
			fourth.setImageResource(R.drawable.reviews_large_yellowstar);
			fifth.setImageResource(R.drawable.reviews_large_greystar);
			break;			
		case 9:
			first.setImageResource(R.drawable.reviews_large_yellowstar);
			second.setImageResource(R.drawable.reviews_large_yellowstar);
			third.setImageResource(R.drawable.reviews_large_yellowstar);
			fourth.setImageResource(R.drawable.reviews_large_yellowstar);
			fifth.setImageResource(R.drawable.reviews_large_halfstar);		
			break;
		case 10:
			first.setImageResource(R.drawable.reviews_large_yellowstar);
			second.setImageResource(R.drawable.reviews_large_yellowstar);
			third.setImageResource(R.drawable.reviews_large_yellowstar);
			fourth.setImageResource(R.drawable.reviews_large_yellowstar);
			fifth.setImageResource(R.drawable.reviews_large_yellowstar);		
			break;			
		default:
			break;
		}
		
	}
	
	private void insertReview(String StoreID, String CustomerID, String Remark, Date DateCreated, String NickName, int Item1Score, int Item2Score, int Item3Score, int Anonymous){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CANADA);
		String strDate = sdf.format(DateCreated);
		int score = Item1Score == 0 ? (Item2Score + Item3Score)/2 : (Item1Score + Item2Score + Item3Score)/3;
		
		PostReview tran = new PostReview(StoreID, CustomerID, Remark, strDate, NickName, Item1Score, Item2Score, Item3Score, Anonymous, score);
		try{
			webApiManager.postApiReview(Serialize.postReview(tran));
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    
    
}
