package ui.customerTransaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.qooway.consumerv01.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomerTransactionListItemAdapater extends ArrayAdapter<String>{
	
	private final Context context;
	private final String[] Ids;
	private final int rowResourceId;


	public String httpserverUrl = "online.profitek.com/testingAPi";
	public String httpsserverUrl = "online.profitek.com/testingAPi";
	
	
	public CustomerTransactionListItemAdapater(Context context, int textViewResourceId,
			String[] objects) {
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
		TextView name = (TextView) rowView.findViewById(R.id.merchant);
		TextView date = (TextView) rowView.findViewById(R.id.date);
		TextView sold = (TextView) rowView.findViewById(R.id.sales);
		TextView points = (TextView) rowView.findViewById(R.id.points);
		
		int id = Integer.parseInt(Ids[position]);
        CustomerTransactionDisplayListItem item = null;
        item = CustomerTransactionListModelAdapater.GetbyId(id);
        
        
        // Ryan date format addition
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");  // this is format of database time
        String test_hello = item.OrderTime; // this is what is hold string/time from database
        Date d = null;  // hold the day 
        Date temp = null; // hold the old time from data base
		try {
			d = sdf.parse(test_hello);    // put into correct format
			temp = sdf.parse(test_hello);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Get the day of the original time
        SimpleDateFormat tmp = new SimpleDateFormat("d");
		String date_t = tmp.format(d);  
        
        
        if(date_t.endsWith("1") && !date_t.endsWith("11"))
            tmp = new SimpleDateFormat("MMM d'st', yyyy - h:mm a");
        else if(date_t.endsWith("2") && !date_t.endsWith("12"))
            tmp = new SimpleDateFormat("MMM d'nd', yyyy - h:mm a");
        else if(date_t.endsWith("3") && !date_t.endsWith("13"))
            tmp = new SimpleDateFormat("MMM d'rd', yyyy - h:mm a");
        else 
            tmp = new SimpleDateFormat("MMM d'th', yyyy - h:mm a");
		
		String formattedTime = tmp.format(temp);   // changes temp the old date with newest tmp
        name.setText(item.MerchantName);
        date.setText(formattedTime);
        sold.setText(item.SoldAmount);
        points.setText(item.Points);
    
        return rowView;
	}
	
	String getDayOfMonthSuffix(final int n) {
	    if (n < 1 || n > 31) {
	        throw new IllegalArgumentException("Illegal day of month");
	    }
	    final String[] SUFFIX = new String[] { "th", "st", "nd", "rd" };
	    return (n >= 11 && n <= 13) || (n % 10 > 3) ? SUFFIX[0] : SUFFIX[n % 10];
	}
	

}
