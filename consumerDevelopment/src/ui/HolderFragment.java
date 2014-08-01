package ui;


import java.util.List;

import ui.voucher.VoucherListItemAdapter;

import com.qooway.consumerv01.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import data.DataStorageManager;
import framework.DataObject.Voucher;
import data.Deserialize;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HolderFragment  extends Fragment {

    public VoucherListItemAdapter Adapter;
    public DataStorageManager dataStorageManager;
    private RelativeLayout VoucherView;
    /*
    private HolderFragment.YesNoImage yesNoImage;
    private Boolean hasAYesNoImage = false;
	*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	dataStorageManager = DataStorageManager.getSingletonInstance();
    	Bundle args = getArguments();
    	@SuppressWarnings("unused")
		int index = args.getInt("index", 0);
        String fragmentName = this.getArguments().getString("Fragment");
        HolderFragmentName name = HolderFragmentName.valueOf(fragmentName);
        View view = null;
		switch (name) {
		case CheckIn:
			view= inflater.inflate(R.layout.fragment_pre_checkin, container, false);
		     break;
		case Voucher:
			view= inflater.inflate(R.layout.fragment_check_in_voucher, container, false);
			List<Voucher> voucherList = deserializeList(this.getArguments().getString("VoucherList"));
			this.VoucherView= (RelativeLayout) view.findViewById(R.id.voucherItem);
			setUpVoucherView(VoucherView,voucherList);
	        View tempView = inflater.inflate(R.layout.pb_layout, null);
	        @SuppressWarnings("unused")
			ProgressBar pb = (ProgressBar) tempView.findViewById(R.id.progressBar);	
		     break;
		case NoVoucher:
			view= inflater.inflate(R.layout.fragment_check_in_no_voucher, container, false);
			  break;
		}
		return view;
    }

	private List<Voucher> deserializeList(String input)
	{
		Deserialize deserializer = new Deserialize();
		return deserializer.getVoucherList(input);
	}
	
	public void setUpVoucherView(RelativeLayout VoucherView, List<Voucher> voucherList)
	{
		ImageView imageView = (ImageView) VoucherView.findViewById(R.id.voucherImage);
		TextView name = (TextView) VoucherView.findViewById(R.id.voucherShopName);
		TextView promotion = (TextView) VoucherView.findViewById(R.id.voucherPromotion);
		@SuppressWarnings("unused")
		TextView distancePosition = (TextView) VoucherView.findViewById(R.id.distance);
		Voucher item = voucherList.get(0);
		name.setText(item.MerchantName);
		promotion.setText(item.VoucherTypeDesc);
		DisplayImageOptions options = new DisplayImageOptions.Builder()
	        .cacheInMemory(true) 
	        .build();
		String baseUri = "https://" + DataStorageManager.getSingletonInstance().getApiUrl()  + "/api/Picture/GetLogo/";
		String MerchantID =Integer.toString(item.MerchantID);
	    String imageUri = baseUri + MerchantID;
	    ImageLoader IM = ImageLoader.getInstance();
	    IM.displayImage(imageUri, imageView, options); 
	    //if(this.hasAYesNoImage)
	    //this.displayYesNoImage();
	    //displayYesNoImage(false);
	}
	
	public void displayYesNoImage(boolean displayVoucher)
	{
		if (VoucherView.findViewById(R.id.yes_no) != null){
			ImageView imageView = (ImageView) VoucherView.findViewById(R.id.yes_no);
			if(displayVoucher){
				imageView.setImageResource(R.drawable.check_in_yes);
			}
			else {
				imageView.setImageResource(R.drawable.check_in_no);
			}
		}	
	}
	
	public void clearImage()
	{
		if (VoucherView.findViewById(R.id.yes_no) != null){
			ImageView imageView = (ImageView) VoucherView.findViewById(R.id.yes_no);
			imageView.setImageResource(android.R.color.transparent);
		}

	}
	
	/*
	public void setYesNoImage(HolderFragment.YesNoImage imageValue)
	{
		this.yesNoImage = imageValue;
	}
	
	public HolderFragment.YesNoImage getYesNoImage(HolderFragment.YesNoImage imageValue)
	{
		return this.yesNoImage;
	}
	
	public void setHasAYesNoImage(boolean hasImage)
	{
		this.hasAYesNoImage = hasImage;
	}
	*/
    public enum HolderFragmentName {

    	CheckIn, Voucher ,NoVoucher
	}
    
    public enum YesNoImage {

    	Yes, No 
	}
	
}
    
    