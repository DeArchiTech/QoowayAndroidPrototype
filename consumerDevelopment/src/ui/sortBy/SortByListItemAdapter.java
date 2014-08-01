package ui.sortBy;

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

public class SortByListItemAdapter  extends  ArrayAdapter<String>{
	
	private final Context context;
	private final ArrayList<String> Ids;
	private final int rowResourceId;
	public static ArrayList<String> SortByList;   // USED for Displaying cuisine names 
	
	public static int lastIndex = 0;
	
	
	public SortByListItemAdapter(Context context, int textViewResourceId,
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
		TextView SortBy = (TextView) rowView.findViewById(R.id.textView1);
		
		ListView  listView = (ListView) parent;
		
		final int id = Integer.parseInt(Ids.get(position));
        SortByDisplayListItem item = null;
        
        item = SortByListModelAdapter.GetbyId(id);
        
        SortBy.setText(item.sortBy);
        
        
        // Make sure that whenever you get list refresh, you check if it is selected 
        if(AdvancedRestaurantSearchActivity.sortBySelectedList.get(position)==1)
        {
        	linear.setBackgroundColor(Color.parseColor("#F29E37"));
        	SortBy.setTextColor(Color.parseColor("#FFFFFF"));
        	
        }
        else
        {
        	linear.setBackgroundColor(Color.parseColor("#FFFFFF"));
        	SortBy.setTextColor(Color.parseColor("#6D6E71"));
        }
         
        linear.setTag(R.integer.position,position);  // pass in the position of linear layout
        linear.setTag(R.integer.textView, SortBy);  // pass in the text view
        linear.setTag(R.integer.listView, listView);
        linear.setOnClickListener(new View.OnClickListener(){
		public void onClick(View arg0) {
			
			ListView listView = (ListView)arg0.getTag(R.integer.listView);  // USED TO ACCess child elements
			int position = (Integer) arg0.getTag(R.integer.position);
			TextView text = (TextView)arg0.getTag(R.integer.textView);

			
			if(position != lastIndex && AdvancedRestaurantSearchActivity.sortBySelectedList.get(position)== 0)
			{
				// SET CURRENT SELECTION TO orange and update arrays
				arg0.setBackgroundColor(Color.parseColor("#F29E37"));   
				text.setTextColor(Color.parseColor("#FFFFFF"));
				AdvancedRestaurantSearchActivity.sortBySelectedList.set(position, 1); // set int array to 1 ( selected)
				SortByList.add(SortByListModelAdapter.Items.get(position).sortBy);   // add string for displaying on home screen
				
				// Get last child selected
				View last = listView.getChildAt(lastIndex);
				AdvancedRestaurantSearchActivity.sortBySelectedList.set(lastIndex, 0);  // unselect last item
				SortByList.remove(SortByListModelAdapter.Items.get(lastIndex).sortBy); // remove last item 
				
				TextView last_text = (TextView) last.findViewById(R.id.textView1);
				LinearLayout last_linear = (LinearLayout) last.findViewById(R.id.linearLayout);
				
				last_linear.setBackgroundColor(Color.parseColor("#FFFFFF"));
				last_text.setTextColor(Color.parseColor("#6D6E71"));
		
			}
			
			lastIndex = position;
			
		}

        });
		
		return rowView;
		
	}
}