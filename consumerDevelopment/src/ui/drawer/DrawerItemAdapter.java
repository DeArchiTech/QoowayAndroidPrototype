package ui.drawer;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import com.qooway.consumerv01.R;


public class DrawerItemAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] Ids;
    private final int rowResourceId;

    public DrawerItemAdapter(Context context, int textViewResourceId, String[] objects) {

        super(context, textViewResourceId, objects);

        this.context = context;
        this.Ids = objects;
        this.rowResourceId = textViewResourceId;

    }
    
    

	public Integer[] mThumbSelected = 
        {
	        R.drawable.login_myaccount_active, 
	        /*TAKE OUT FOR CHECK IN
	         * 
	         * 	        R.drawable.checkin,
	         */
	        R.drawable.menu_favourite_active, 
	        R.drawable.menu_new_active,
	        R.drawable.nearby, 
	        R.drawable.search, 
	        R.drawable.menu_redeem_my_points_active,
	        R.drawable.reviews,
	        R.drawable.about
        };

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(rowResourceId, parent, false);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.menuImage);
        TextView textView = (TextView) rowView.findViewById(R.id.menuText);

        ListView mDrawerList = (ListView) parent.findViewById(R.id.left_drawer);
     
        
        int id = Integer.parseInt(Ids[position]);
        String imageFile = DrawerModelAdapter.GetbyId(id).IconFile;
//mm
        textView.setText(DrawerModelAdapter.GetbyId(id).Name);
        // get input stream
        @SuppressWarnings("unused")
		InputStream ims = null;
        try {
            ims = context.getAssets().open(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Rob AR-44
        if(mDrawerList.isItemChecked(position)){
            imageView.setBackgroundResource(mThumbSelected[position]);
        }else{
            imageView.setBackgroundResource(context.getResources().getIdentifier(imageFile,
                    "drawable", context.getPackageName()));
        }

        
        return rowView;
    }
}