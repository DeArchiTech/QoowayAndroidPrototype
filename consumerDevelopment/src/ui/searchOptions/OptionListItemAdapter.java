package ui.searchOptions;

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

public class OptionListItemAdapter extends  ArrayAdapter<String>{
	
	private final Context context;
	private final ArrayList<String> Ids;
	private final int rowResourceId;
	public static ArrayList<String> OptionList;   // USED for Displaying cuisine names 
	
	
	public OptionListItemAdapter(Context context, int textViewResourceId,
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
		TextView Option = (TextView) rowView.findViewById(R.id.textView1);
		
		final int id = Integer.parseInt(Ids.get(position));
        OptionDisplayListItem item = null;
        
        item = OptionListModelAdapter.GetbyId(id);
        
        Option.setText(item.optionName);
        
        
        // Make sure that whenever you get list refresh, you check if it is selected 
        if(AdvancedRestaurantSearchActivity.optionSelectedList.get(position)==1)
        {
        	linear.setBackgroundColor(Color.parseColor("#F29E37"));
        	Option.setTextColor(Color.parseColor("#FFFFFF"));
        	
        }
        else
        {
        	linear.setBackgroundColor(Color.parseColor("#FFFFFF"));
        	Option.setTextColor(Color.parseColor("#6D6E71"));
        }
         
        linear.setTag(R.integer.position,position);  // pass in the position of linear layout
        linear.setTag(R.integer.textView, Option);  // pass in the text view
        
        linear.setOnClickListener(new View.OnClickListener(){
		public void onClick(View arg0) {
			
			int position = (Integer) arg0.getTag(R.integer.position);
			TextView text = (TextView)arg0.getTag(R.integer.textView);
			
			if(AdvancedRestaurantSearchActivity.optionSelectedList.get(position)== 0)  // NOT SELECTED
			{
				
			arg0.setBackgroundColor(Color.parseColor("#F29E37"));
			text.setTextColor(Color.parseColor("#FFFFFF"));
			AdvancedRestaurantSearchActivity.optionSelectedList.set(position, 1);   // changes it to 1
			OptionList.add(OptionListModelAdapter.Items.get(position).optionName);   // add name to list
			
			}
			else // ALREADY SELECTED
			{      
				arg0.setBackgroundColor(Color.parseColor("#FFFFFF"));
				text.setTextColor(Color.parseColor("#6D6E71"));
				AdvancedRestaurantSearchActivity.optionSelectedList.set(position, 0); // changes it to 0
				OptionList.remove(OptionListModelAdapter.Items.get(position).optionName); // remove name to list		
			}
	
		}

        });
		
		return rowView;
		
	}
}