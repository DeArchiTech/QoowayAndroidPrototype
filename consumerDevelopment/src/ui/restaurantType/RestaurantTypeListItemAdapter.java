package ui.restaurantType;

import java.util.ArrayList;

import ui.advanced_search.AdvancedRestaurantSearchActivity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qooway.consumerv01.R;

public class RestaurantTypeListItemAdapter extends  ArrayAdapter<String>{
	
	private final Context context;
	private final ArrayList<String> Ids;
	private final int rowResourceId;
	public static ArrayList<String> RestaurantTypeList;   // USED for Displaying cuisine names 
	
	
	public RestaurantTypeListItemAdapter(Context context, int textViewResourceId,
			ArrayList<String> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.Ids = objects;
		this.rowResourceId = textViewResourceId;
		
	}	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(rowResourceId, parent, false);
		
		LinearLayout linear = (LinearLayout) rowView.findViewById(R.id.linearLayout);
		TextView restaurantType = (TextView) rowView.findViewById(R.id.textView1);
		
		final int id = Integer.parseInt(Ids.get(position));
        RestaurantTypeDisplayListItem item = null;
        
        item = RestaurantTypeListModelAdapter.GetbyId(id);
        
        restaurantType.setText(item.restaurantTypeName);
        
        
        // Make sure that whenever you get list refresh, you check if it is selected 
        if(AdvancedRestaurantSearchActivity.restaurantTypeSelectedList.get(position)==1)
        {
        	linear.setBackgroundColor(Color.parseColor("#F29E37"));
        	restaurantType.setTextColor(Color.parseColor("#FFFFFF"));
        	
        }
        else
        {
        	linear.setBackgroundColor(Color.parseColor("#FFFFFF"));
        	restaurantType.setTextColor(Color.parseColor("#6D6E71"));
        }
         
        linear.setTag(R.integer.position,position);  // pass in the position of linear layout
        linear.setTag(R.integer.textView,restaurantType);  // pass in the text view
        
        linear.setOnClickListener(new View.OnClickListener(){
		public void onClick(View arg0) {
			
			int position = (Integer) arg0.getTag(R.integer.position);
			TextView text = (TextView)arg0.getTag(R.integer.textView);
			
			if(AdvancedRestaurantSearchActivity.restaurantTypeSelectedList.get(position)== 0)  // NOT SELECTED
			{
				
			arg0.setBackgroundColor(Color.parseColor("#F29E37"));
			text.setTextColor(Color.parseColor("#FFFFFF"));
			AdvancedRestaurantSearchActivity.restaurantTypeSelectedList.set(position, 1);   // changes it to 1
			RestaurantTypeList.add(RestaurantTypeListModelAdapter.Items.get(position).restaurantTypeName);   // add name to list
			System.out.println("HELLO");
			}
			else // ALREADY SELECTED
			{      
				arg0.setBackgroundColor(Color.parseColor("#FFFFFF"));
				text.setTextColor(Color.parseColor("#6D6E71"));
				AdvancedRestaurantSearchActivity.restaurantTypeSelectedList.set(position, 0); // changes it to 0
				RestaurantTypeList.remove(RestaurantTypeListModelAdapter.Items.get(position).restaurantTypeName); // remove name to list		
			}
	
		}

        });
		
		return rowView;
		
	}
}