package framework.CustomClass;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

@SuppressWarnings("deprecation")
public class ScrollViewExtend extends ScrollView{
	LinearLayout merchantButton1;
	ImageView merchantButton1Image;
	LinearLayout merchantArrow1;
	
	RelativeLayout anchor1;
	Gallery anchor2;
	LinearLayout anchor3;
	
	LinearLayout merchantButton2;
	ImageView merchantButton2Image;
	LinearLayout merchantArrow2;
	
	
	LinearLayout merchantButton3;
	ImageView merchantButton3Image;
	LinearLayout merchantArrow3;
	
	
	LinearLayout merchantButton4;
	ImageView merchantButton4Image;
	LinearLayout merchantArrow4;

	
    public ScrollViewExtend(Context context) {
        super(context);
     
    }

    public ScrollViewExtend(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewExtend(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public void setButtons(){
    /*	LinearLayout inflater = (LinearLayout)this.getTag(R.integer.layout);
    	merchantButton1 = (LinearLayout)inflater.findViewById(R.id.merchantButton1);
    	merchantButton1Image = (ImageView)inflater.findViewById(R.id.merchantButton1image);
    	merchantArrow1 = (LinearLayout)inflater.findViewById(R.id.merchantArrow1);
    	
    //	anchor1 = (RelativeLayout) inflater.findViewById(R.id.checkIn);
    	anchor2 = (Gallery)inflater.findViewById(R.id.gallery1);
    	anchor3 = (LinearLayout) inflater.findViewById(R.id.writeReviewLayout);
    	
    	merchantButton2 = (LinearLayout)inflater.findViewById(R.id.merchantButton2);
    	merchantButton2Image = (ImageView)inflater.findViewById(R.id.merchantButton2image);
    	merchantArrow2 = (LinearLayout)inflater.findViewById(R.id.merchantArrow2);
    	   	
    	merchantButton3 = (LinearLayout)inflater.findViewById(R.id.merchantButton3);
    	merchantButton3Image = (ImageView)inflater.findViewById(R.id.merchantButton3image);
    	merchantArrow3 = (LinearLayout)inflater.findViewById(R.id.merchantArrow3);
    	
    	merchantButton4 = (LinearLayout)inflater.findViewById(R.id.merchantButton4);
    	merchantButton4Image = (ImageView)inflater.findViewById(R.id.merchantButton4image);
    	merchantArrow4 = (LinearLayout)inflater.findViewById(R.id.merchantArrow4);
    	*/
    }
 //


@Override
protected void onScrollChanged(int l, int t, int oldl, int oldt) {
	//BE PROACTIVE
	/*
	    View view = (View) getChildAt(getChildCount()-1);
	    int diff = (view.getBottom()-(getHeight()+getScrollY()));
	    
        if(diff == 0){  // if diff is zero, then the bottom has been reached
			merchantButton3.setBackgroundColor(Color.parseColor("#DA563F"));
			merchantButton3Image.setImageResource(R.drawable.reviews);
			merchantArrow3.setVisibility(View.VISIBLE);
			
			merchantButton2.setBackgroundColor(Color.parseColor("#E6E7E8"));
			merchantButton2Image.setImageResource(R.drawable.information_inactive);
			merchantArrow2.setVisibility(View.INVISIBLE);
			
			merchantButton1.setBackgroundColor((getResources().getColor(R.color.LightGrey)));
			merchantButton1Image.setImageResource(R.drawable.checkin_inactive);
			merchantArrow1.setVisibility(View.INVISIBLE);
        } else if(t < anchor2.getTop()) {	
			merchantButton1.setBackgroundColor(Color.parseColor("#DA563F"));
			merchantButton1Image.setImageResource(R.drawable.checkin);
			merchantArrow1.setVisibility(View.VISIBLE);
			
			merchantButton2.setBackgroundColor(Color.parseColor("#E6E7E8"));
			merchantButton2Image.setImageResource(R.drawable.information_inactive);
			merchantArrow2.setVisibility(View.INVISIBLE);
			
			merchantButton3.setBackgroundColor((getResources().getColor(R.color.LightGrey)));
			merchantButton3Image.setImageResource(R.drawable.reviews_inactive);
			merchantArrow3.setVisibility(View.INVISIBLE);
		} else if(t >= anchor3.getTop()) {
			merchantButton3.setBackgroundColor(Color.parseColor("#DA563F"));
			merchantButton3Image.setImageResource(R.drawable.reviews);
			merchantArrow3.setVisibility(View.VISIBLE);
			
			merchantButton2.setBackgroundColor(Color.parseColor("#E6E7E8"));
			merchantButton2Image.setImageResource(R.drawable.information_inactive);
			merchantArrow2.setVisibility(View.INVISIBLE);
			
			merchantButton1.setBackgroundColor((getResources().getColor(R.color.LightGrey)));
			merchantButton1Image.setImageResource(R.drawable.checkin_inactive);
			merchantArrow1.setVisibility(View.INVISIBLE);
		} else if(t >= anchor2.getTop() && t < anchor3.getTop() && oldt >= anchor2.getTop()) {	
			merchantButton2.setBackgroundColor((getResources().getColor(R.color.Red)));
			merchantButton2Image.setImageResource(R.drawable.information);
			merchantArrow2.setVisibility(View.VISIBLE);
			
			
			merchantButton1.setBackgroundColor((getResources().getColor(R.color.LightGrey)));
			merchantButton1Image.setImageResource(R.drawable.checkin_inactive);
			merchantArrow1.setVisibility(View.INVISIBLE);
			
			
			merchantButton3.setBackgroundColor((getResources().getColor(R.color.LightGrey)));
			merchantButton3Image.setImageResource(R.drawable.reviews_inactive);
			merchantArrow3.setVisibility(View.INVISIBLE);
							
		} else if(t >= anchor3.getTop()) {
			merchantButton3.setBackgroundColor(Color.parseColor("#DA563F"));
			merchantButton3Image.setImageResource(R.drawable.reviews);
			merchantArrow3.setVisibility(View.VISIBLE);
			
			merchantButton2.setBackgroundColor(Color.parseColor("#E6E7E8"));
			merchantButton2Image.setImageResource(R.drawable.information_inactive);
			merchantArrow2.setVisibility(View.INVISIBLE);
			
			merchantButton1.setBackgroundColor((getResources().getColor(R.color.LightGrey)));
			merchantButton1Image.setImageResource(R.drawable.checkin_inactive);
			merchantArrow1.setVisibility(View.INVISIBLE);
		}	
		*/
	//BE PROACTIVE			
	}


}