package framework;
import data.DataStorageManager;
import data.WebApiManager;
import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class QoowayActivity extends Activity{
	public WebApiManager webApiManager;
	public DataStorageManager dataStorageManager;
	public AlertDialog.Builder alertDialogBuilder;
	private Boolean isLoading =false;
	private ProgressDialog progressDialog;

	@Override
	 protected void onCreate(Bundle savedInstanceState) {
	  
	  super.onCreate(savedInstanceState);
	  //DAVID2014724
	  DataStorageManager.getSingletonInstance().currentActivity = this;
	  //DAVID2014724
	  
	 }

	 @Override
	 protected void onResume() {
	  super.onResume();
	  this.setIsLoading(false);
	  //DAVID2014724
	  DataStorageManager.getSingletonInstance().currentActivity = this;
	  //DAVID2014724
	 }
	 
	 @Override
	 protected void onStart(){
	  super.onStart();
	
	 }
	 
	 @Override
		public boolean onOptionsItemSelected(MenuItem menuItem)
		{       
			switch (menuItem.getItemId()) {
			// Respond to the action bar's Up/Home button
			case android.R.id.home:
				//NavUtils.navigateUpFromSameTask(this);
				finish();
				return true;
			}  
		    return super.onOptionsItemSelected(menuItem);
		} 		

	public void showProgressDialogLoad(final int time) {
		if (progressDialog == null) {
			//progressDialog = ProgressDialog.show(this, "Please wait ...",
			//		"Loading ...", true);
			progressDialog = new ProgressDialog(this);
			progressDialog.setIndeterminate(false);
			progressDialog.setMessage("Loading...");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.show();
			/*
			progressDialog.setCancelable(true);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);*/
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Boolean quitLoop = false;
						while (!quitLoop) {
							Thread.sleep(time);
							int x = DataStorageManager.getSingletonInstance()
									.getAsyncTaskCount();
							if (DataStorageManager.getSingletonInstance()
									.getAsyncTaskCount() == 0) {
								quitLoop = true;
								QoowayActivity.this.cancelProgressDialog();
							}
							
						}

					} catch (Exception e) {

					}

				}
			}).start();

		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

	public void cancelProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	
	

	public void PromtMessage(String message) {

		Toast toast = null;
		int duration = Toast.LENGTH_SHORT;
		toast = Toast.makeText(this, message, duration);
		toast.show();
	}
	
	void OnRestart() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean getIsLoading()
	{
		return this.isLoading;
	}
	
	public void setIsLoading(boolean bool)
	{
		this.isLoading= bool;
	}
	
	public Boolean LocationServicesOn() {

		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String bestProvider = lm.getBestProvider(criteria, false);
		Location lastKnownLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		return lastKnownLocation !=null;
	}
	public void promptDialog(String Title ,String Message , String PositiveButton) {
		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this,android.R.style.Theme_Holo_Light));
		builder.setMessage(Message)
			   .setTitle(Title)
		       .setCancelable(false)
		       .setPositiveButton(PositiveButton, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		     		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	

	
	public static class MyAlertDialogFragment extends DialogFragment {

	    public static MyAlertDialogFragment newInstance(int title) {
	        MyAlertDialogFragment frag = new MyAlertDialogFragment();
	        Bundle args = new Bundle();
	        args.putInt("title", title);
	        frag.setArguments(args);
	        return frag;
	    }

	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        int title = getArguments().getInt("title");
	        DataStorageManager dataStorageManager = DataStorageManager.getSingletonInstance();
	        int code = dataStorageManager.lastCode;
	        String message = dataStorageManager.lastMessage;
	        return new AlertDialog.Builder(getActivity())
	                .setTitle(title)
	                .setMessage("\n" + code + " Server Error\n" + message + "\nPlease try again later\n")
	                .setPositiveButton(R.string.ok,
	                    new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int whichButton) {

	                        }
	                    }
	                )
	                .create();
	    }
	}
	
	public void showDialog() {
	    DialogFragment newFragment = MyAlertDialogFragment.newInstance(
	            R.string.dialog_alert_title);
	    //newFragment.show(getFragmentManager(), "dialog");
	}	
	
	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {


			return super.onPrepareOptionsMenu(menu);
		

	}
	

	
	
}
