package ui.review;

import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.qooway.consumerv01.R;

public class ReviewListItemAdapter extends ArrayAdapter<String> {

	private static Context context;
	private final String[] Ids;
	private final int rowResourceId;
	private int half_star = R.drawable.reviews_halfstar;
	private int yellow_star = R.drawable.reviews_yellow_star;

	public ReviewListItemAdapter(Context context, int textViewResourceId,
			String[] objects) {
		super(context, textViewResourceId, objects);
		ReviewListItemAdapter.context = context;
		this.Ids = objects;
		this.rowResourceId = textViewResourceId;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(rowResourceId, parent, false);
		TextView ReviewDate= (TextView) rowView.findViewById(R.id.ReviewDate);
		TextView ReviewerName= (TextView) rowView.findViewById(R.id.ReviewerName);
		TextView ReviewText= (TextView) rowView.findViewById(R.id.ReviewText);
		TextView RestaurantName = (TextView) rowView.findViewById(R.id.RestaurantName);
        int id = Integer.parseInt(Ids[position]);
        ReviewDisplayListItem item = ReviewListModelAdapter.GetbyId(id);
       
        
        // Parse Date the proper way
       String formattedTime = parseDate(item.Date);
       ReviewDate.setText(formattedTime);
        
  
        
        RestaurantName.setText(item.Restaurant);
        ReviewerName.setText("Reviewed by: " + item.Reviewer);
        ReviewText.setText(item.ReviewText);
        if(item.Food == 0 )
        {
        	TextView textView4 = (TextView) rowView.findViewById(R.id.textView4);
        	textView4.setVisibility(View.GONE);
        	LinearLayout foodLinearLayout = (LinearLayout)rowView.findViewById(R.id.foodLinearLayout);
        	foodLinearLayout.setVisibility(View.GONE);
        } else{
        	setUpFood(rowView, item);
        }
        setUpService(rowView, item);
        setUpAmbience(rowView, item);
        setUpMainStar(rowView, item);
        
        rowView.findViewById(R.id.ReviewAddDetail).setVisibility(View.GONE);
        
        rowView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
				View reviewAddDetail = v.findViewById(R.id.ReviewAddDetail);
				if(!reviewAddDetail.isShown()){
					reviewAddDetail.setVisibility(View.VISIBLE);
					slide_down(ReviewListItemAdapter.context, reviewAddDetail);					
				} else {
					slide_up(ReviewListItemAdapter.context, reviewAddDetail);
					reviewAddDetail.setVisibility(View.GONE);
				}
				

            }
         });
        
		return rowView;
	}
	
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
		
	private void setUpFood(View rowView , ReviewDisplayListItem item)
	{
		ImageView firstStar = (ImageView) rowView.findViewById(R.id.imageView1);
		ImageView secondStar = (ImageView) rowView.findViewById(R.id.imageView2);
		ImageView thirdStar = (ImageView) rowView.findViewById(R.id.imageView3);
		ImageView fourthStar = (ImageView) rowView.findViewById(R.id.imageView4);
		ImageView fifthStar = (ImageView) rowView.findViewById(R.id.imageView5);
		setupStars(firstStar, secondStar, thirdStar, fourthStar,fifthStar , item.Food);
	}
	
	private void  setUpService(View rowView ,ReviewDisplayListItem item)
	{
		ImageView firstStar = (ImageView) rowView.findViewById(R.id.imageView6);
		ImageView secondStar = (ImageView) rowView.findViewById(R.id.imageView7);
		ImageView thirdStar = (ImageView) rowView.findViewById(R.id.imageView8);
		ImageView fourthStar = (ImageView) rowView.findViewById(R.id.imageView9);
		ImageView fifthStar = (ImageView) rowView.findViewById(R.id.imageView10);
		setupStars(firstStar, secondStar, thirdStar, fourthStar,fifthStar , item.Service);
	}
	
	private void setUpAmbience(View rowView ,ReviewDisplayListItem item)
	{
		
		ImageView firstStar = (ImageView) rowView.findViewById(R.id.imageView11);
		ImageView secondStar = (ImageView) rowView.findViewById(R.id.imageView12);
		ImageView thirdStar = (ImageView) rowView.findViewById(R.id.imageView13);
		ImageView fourthStar = (ImageView) rowView.findViewById(R.id.imageView14);
		ImageView fifthStar = (ImageView) rowView.findViewById(R.id.imageView15);
		setupStars(firstStar, secondStar, thirdStar, fourthStar,fifthStar , item.Ambience);
	}
	
	private void setUpMainStar(View rowView , ReviewDisplayListItem item)
	{
		//Takes the average of the 3 ratings.
		ImageView firstStar = (ImageView) rowView.findViewById(R.id.imageView16);
		ImageView secondStar = (ImageView) rowView.findViewById(R.id.imageView17);
		ImageView thirdStar = (ImageView) rowView.findViewById(R.id.imageView18);
		ImageView fourthStar = (ImageView) rowView.findViewById(R.id.imageView19);
		ImageView fifthStar = (ImageView) rowView.findViewById(R.id.imageView20);
		setupStars(firstStar, secondStar, thirdStar, fourthStar,fifthStar , item.Score);
	}
	
	
	private void setupStars(ImageView first ,ImageView second,ImageView third,ImageView fourth,ImageView fifth, int rating )
	{
		
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
