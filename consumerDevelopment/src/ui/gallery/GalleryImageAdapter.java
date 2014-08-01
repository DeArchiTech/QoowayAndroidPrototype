package ui.gallery;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.qooway.consumerv01.R;

@SuppressWarnings("deprecation")
public class GalleryImageAdapter extends BaseAdapter 
{

    private Context mContext;

    private Integer[] mImageIds = {
    		R.drawable.merchantdetail_testpic,
    		R.drawable.md_gallerypic2,
    		R.drawable.md_gallerypic3
    };

    public GalleryImageAdapter(Context context) 
    {
        mContext = context;
    }
    

	public int getCount() {
        return mImageIds.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    // Override this method according to your need
    @Override
    public View getView(int index, View view, ViewGroup viewGroup) 
    {
        // TODO Auto-generated method stub
        ImageView i = new ImageView(mContext);

        i.setImageResource(mImageIds[index]);
        i.setLayoutParams(new Gallery.LayoutParams(700, 330));
    
        i.setScaleType(ImageView.ScaleType.FIT_XY);

        return i;
    }
}