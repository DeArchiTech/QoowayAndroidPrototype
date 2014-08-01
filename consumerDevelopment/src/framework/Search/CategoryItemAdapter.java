package framework.Search;

import com.qooway.consumerv01.R;
import framework.DataObject.Merchant;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CategoryItemAdapter extends ArrayAdapter<String> {

	private final Context context;
	private final String[] Ids;
	private final int rowResourceId;
	

	public CategoryItemAdapter(Context context, int textViewResourceId,
			String[] objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.Ids = objects;
		this.rowResourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int id = Integer.parseInt(Ids[position]);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(rowResourceId, parent, false);
		TextView nameView = (TextView) rowView.findViewById(R.id.nameView);
		nameView.setText(CategoryModelAdapter.GetbyId(id).name);
		if(CategoryModelAdapter.GetbyId(id).type!=Merchant.class.getName())
		{
			TextView countView = (TextView) rowView.findViewById(R.id.categoryCount);
			countView.setText(Integer.toString(CategoryModelAdapter.GetbyId(id).count));
		}
		return rowView;

	}

}