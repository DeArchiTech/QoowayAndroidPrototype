package ui.newSearch;

import java.util.ArrayList;
import java.util.Map;

import com.qooway.consumerv01.R;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CategoryListItemAdapter extends  ArrayAdapter<String>{
	
	private final Context context;
	private final ArrayList<String> Ids;
	private final int rowResourceId;
	public static ArrayList<String> RestaurantTypeList;   // USED for Displaying cuisine names 
	public Map<String,Boolean> map;
	public String choiceMode;
	
	public static int lastPosition = -1;
	public static String lastString ="";
	
	public static int lastIndexMustChooseOne = 0;
	public static String lastStringMustChooseOne ="Name: A-Z";   // initialize it
	
	public CategoryListItemAdapter(Context context, int textViewResourceId,
			ArrayList<String> objects, Map<String,Boolean> map, String choiceMode) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.Ids = objects;
		this.rowResourceId = textViewResourceId;
		this.map = map;
		this.choiceMode = choiceMode;
		
	}	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(rowResourceId, parent, false);
		LinearLayout linearLayout = (LinearLayout) rowView.findViewById(R.id.linearLayout);
		TextView TextView = (TextView) rowView.findViewById(R.id.textView1);
		ListView  listView = (ListView) parent;
		
		final int id = Integer.parseInt(Ids.get(position));
		CategoryDisplayListItem item = null;
        
        item = CategoryListModelAdapter.GetbyId(id);
        
        TextView.setText(item.categoryName);
        
        // Make sure that whenever you get list refresh, you check if it is selected 
        if(map.get(item.categoryName))
        {
        	linearLayout.setBackgroundColor(Color.parseColor("#F29E37"));
        	TextView.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else
        {
        	linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        	TextView.setTextColor(Color.parseColor("#6D6E71"));
        }
        linearLayout.setTag(R.integer.position, item.categoryName);  // pass in the name of linear
    	linearLayout.setTag(R.integer.textView,TextView);  // pass in the text view
        
        if(choiceMode.equals("SingleChoice")){
        	
        	linearLayout.setTag(R.integer.listView, listView);
        	linearLayout.setTag(R.integer.actual_position, position);
        	linearLayout.setOnClickListener(new View.OnClickListener(){
    		public void onClick(View arg0) {
    			
    			ListView listView = (ListView)arg0.getTag(R.integer.listView);  // USED TO ACCess child elements
    			String categoryName = (String) arg0.getTag(R.integer.position);
    			int position = (Integer) arg0.getTag(R.integer.actual_position);
    			TextView text = (TextView)arg0.getTag(R.integer.textView);
    			
    			if(lastString.equals(""))  // First Selection
    			{
    				arg0.setBackgroundColor(Color.parseColor("#F29E37"));   
    				text.setTextColor(Color.parseColor("#FFFFFF"));
    				map.put(categoryName, true);  // select item
    				
    			}
    			
    			else if(lastString.equals(categoryName) && map.get(categoryName)==true)  // selected and clicked again equals NOT SELECTED
    			{
    				arg0.setBackgroundColor(Color.parseColor("#FFFFFF"));
    				text.setTextColor(Color.parseColor("#6D6E71"));
    				map.put(categoryName, false);  // unselect item
    				
    				
    			}
    			else if(lastString.equals(categoryName) && map.get(categoryName)==false)  // not selected and clicked again equals SELECTED
    			{
    				arg0.setBackgroundColor(Color.parseColor("#F29E37"));   
    				text.setTextColor(Color.parseColor("#FFFFFF"));
    				map.put(categoryName, true); // set int array to 1 ( selected)
    				
    			}
    			
    			else 
    			{
    				// SET CURRENT SELECTION TO orange and update arrays
    				arg0.setBackgroundColor(Color.parseColor("#F29E37"));   
    				text.setTextColor(Color.parseColor("#FFFFFF"));
    				map.put(categoryName, true); // set int array to 1 ( selected)
    				
    				// Get last child selected
    				View last = listView.getChildAt(lastPosition);
    				map.put(lastString, false);
    				
    				
    				TextView last_text = (TextView) last.findViewById(R.id.textView1);
    				LinearLayout last_linear = (LinearLayout) last.findViewById(R.id.linearLayout);
    				
    				last_linear.setBackgroundColor(Color.parseColor("#FFFFFF"));
    				last_text.setTextColor(Color.parseColor("#6D6E71"));
    			
    			}
    			lastPosition = position;
    			lastString = categoryName;
    			
    		}

            });
        	
        	
        } else if(choiceMode.equals("MultipleChoice")){
        
        	linearLayout.setOnClickListener(new View.OnClickListener(){
        			public void onClick(View arg0) {
        				
        				String categoryName = (String) arg0.getTag(R.integer.position);
        				TextView text = (TextView)arg0.getTag(R.integer.textView);
   
        				if(map.get(categoryName) == false)  // NOT SELECTED
        				{	
	        				arg0.setBackgroundColor(Color.parseColor("#F29E37"));
	        				text.setTextColor(Color.parseColor("#FFFFFF"));
	        				map.put(categoryName,true);   // changes it to 1
        				
        				}
        				else // ALREADY SELECTED
        				{      
        					arg0.setBackgroundColor(Color.parseColor("#FFFFFF"));
        					text.setTextColor(Color.parseColor("#6D6E71"));
        					map.put(categoryName,false); // changes it to 0
        				}
        		
        			}

        	        });
        	
        	
        } else if(choiceMode.equals("MustSelectOne")){
        	
        	linearLayout.setTag(R.integer.actual_position, position);
        	linearLayout.setTag(R.integer.listView, listView);
        	linearLayout.setOnClickListener(new View.OnClickListener(){
    		public void onClick(View arg0) {
    			
    			ListView listView = (ListView)arg0.getTag(R.integer.listView);  // USED TO ACCess child elements
    			int position = (Integer) arg0.getTag(R.integer.actual_position);
    			TextView text = (TextView)arg0.getTag(R.integer.textView);
    			String categoryName = (String) arg0.getTag(R.integer.position);
    			
    			if(!lastStringMustChooseOne.equals(categoryName) && map.get(categoryName)==false)
    			{
        		
    				// SET CURRENT SELECTION TO orange and update arrays
    				arg0.setBackgroundColor(Color.parseColor("#F29E37"));   
    				text.setTextColor(Color.parseColor("#FFFFFF"));
    				map.put(categoryName, true); // set int array to 1 ( selected)

    				
    				// Get last child selected
    				View last = listView.getChildAt(lastIndexMustChooseOne);
    				map.put(lastStringMustChooseOne, false);  // unselect last item
    				
    				
    				TextView last_text = (TextView) last.findViewById(R.id.textView1);
    				LinearLayout last_linear = (LinearLayout) last.findViewById(R.id.linearLayout);
    				
    				last_linear.setBackgroundColor(Color.parseColor("#FFFFFF"));
    				last_text.setTextColor(Color.parseColor("#6D6E71"));
    		
    			}
    			
    			lastIndexMustChooseOne = position;
    			lastStringMustChooseOne = categoryName;
    			
    		}

            });
        	
        }

		return rowView;
		
	}
}
