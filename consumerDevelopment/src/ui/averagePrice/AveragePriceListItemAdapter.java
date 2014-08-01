package ui.averagePrice;

import java.util.ArrayList;

import ui.advanced_search.AdvancedRestaurantSearchActivity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qooway.consumerv01.R;

public class AveragePriceListItemAdapter extends  ArrayAdapter<String>{
	
	private final Context context;
	private final ArrayList<String> Ids;
	private final int rowResourceId;
	public static ArrayList<String> AveragePriceList;   // USED for Displaying cuisine names 
	
	public static int lastPosition = -1;
	
	
	public AveragePriceListItemAdapter(Context context, int textViewResourceId,
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
		
		ListView  listView = (ListView) parent;
		LinearLayout linear = (LinearLayout) rowView.findViewById(R.id.linearLayout);
		TextView AveragePrice = (TextView) rowView.findViewById(R.id.textView1);
		
		final int id = Integer.parseInt(Ids.get(position));
        AveragePriceDisplayListItem item = null;
        
        item = AveragePriceListModelAdapter.GetbyId(id);
        
        AveragePrice.setText(item.averagePrice);
        
        
        // Make sure that whenever you get list refresh, you check if it is selected 
        if(AdvancedRestaurantSearchActivity.averagePriceSelectedList.get(position)==1)
        {
        	linear.setBackgroundColor(Color.parseColor("#F29E37"));
        	AveragePrice.setTextColor(Color.parseColor("#FFFFFF"));
        	
        }
        else
        {
        	linear.setBackgroundColor(Color.parseColor("#FFFFFF"));
        	AveragePrice.setTextColor(Color.parseColor("#6D6E71"));
        }
         
        linear.setTag(R.integer.position,position);  // pass in the position of linear layout
        linear.setTag(R.integer.textView, AveragePrice);  // pass in the text view
        linear.setTag(R.integer.listView, listView);
        linear.setOnClickListener(new View.OnClickListener(){
		public void onClick(View arg0) {
			ListView listView = (ListView)arg0.getTag(R.integer.listView);  // USED TO ACCess child elements
			int position = (Integer) arg0.getTag(R.integer.position);
			TextView text = (TextView)arg0.getTag(R.integer.textView);
			
			if(lastPosition == -1)  // First Selection
			{
				arg0.setBackgroundColor(Color.parseColor("#F29E37"));   
				text.setTextColor(Color.parseColor("#FFFFFF"));
				AdvancedRestaurantSearchActivity.averagePriceSelectedList.set(position, 1); // set int array to 1 ( selected)
				AveragePriceList.add(AveragePriceListModelAdapter.Items.get(position).averagePrice);   // add string for displaying on home screen
				lastPosition = position;
			}
			
			else if(lastPosition == position && AdvancedRestaurantSearchActivity.averagePriceSelectedList.get(position) == 1)  // selected and clicked again equals NOT SELECTED
			{
				arg0.setBackgroundColor(Color.parseColor("#FFFFFF"));
				text.setTextColor(Color.parseColor("#6D6E71"));
				AdvancedRestaurantSearchActivity.averagePriceSelectedList.set(position, 0);  // unselect last item
				AveragePriceList.remove(AveragePriceListModelAdapter.Items.get(position).averagePrice); // remove last item 
			}
			else if(lastPosition == position && AdvancedRestaurantSearchActivity.averagePriceSelectedList.get(position) == 0)  // not selected and clicked again equals SELECTED
			{
				arg0.setBackgroundColor(Color.parseColor("#F29E37"));   
				text.setTextColor(Color.parseColor("#FFFFFF"));
				AdvancedRestaurantSearchActivity.averagePriceSelectedList.set(position, 1); // set int array to 1 ( selected)
				AveragePriceList.add(AveragePriceListModelAdapter.Items.get(position).averagePrice);   // add string for displaying on home screen
				lastPosition = position;
			}
			
			else 
			{
				// SET CURRENT SELECTION TO orange and update arrays
				arg0.setBackgroundColor(Color.parseColor("#F29E37"));   
				text.setTextColor(Color.parseColor("#FFFFFF"));
				AdvancedRestaurantSearchActivity.averagePriceSelectedList.set(position, 1); // set int array to 1 ( selected)
				AveragePriceList.add(AveragePriceListModelAdapter.Items.get(position).averagePrice);   // add string for displaying on home screen
				
				// Get last child selected
				View last = listView.getChildAt(lastPosition);
				AdvancedRestaurantSearchActivity.averagePriceSelectedList.set(lastPosition, 0);  // unselect last item
				AveragePriceList.remove(AveragePriceListModelAdapter.Items.get(lastPosition).averagePrice); // remove last item 
				
				TextView last_text = (TextView) last.findViewById(R.id.textView1);
				LinearLayout last_linear = (LinearLayout) last.findViewById(R.id.linearLayout);
				
				last_linear.setBackgroundColor(Color.parseColor("#FFFFFF"));
				last_text.setTextColor(Color.parseColor("#6D6E71"));
			
			}
			
			lastPosition = position;
			
		}

        });
		
		return rowView;
		
	}
}
