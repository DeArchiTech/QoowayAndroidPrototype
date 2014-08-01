package ui;

import com.qooway.consumerv01.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;
import data.DataStorageManager;
import data.WebApiManager;
import framework.QoowayActivity;
import framework.GooglePlay.GooglePlayManager;
import framework.QoowayActivity.MyAlertDialogFragment;

public class ConsumerActivity extends QoowayActivity{
	
	public GooglePlayManager googlePlayManager;
	@Override
	 protected void onCreate(Bundle savedInstanceState) {
	  
	  super.onCreate(savedInstanceState);
		googlePlayManager = new GooglePlayManager(this);
		googlePlayManager.GCMregistration();

	 }
	
	@Override
	protected void onResume() {
		super.onResume();
		if(IsConneted())
		{
			this.googlePlayManager.checkPlayServices();
		}
		else
		{
			DisplayNoInternetImage();
		}
	}
	
    private boolean IsConneted()
    {
		ConnectivityManager connMgr = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return networkInfo != null;
    }
    
    private void DisplayNoInternetImage()
    {
		setContentView(R.layout.fragment_update_app);
    }

	
}
