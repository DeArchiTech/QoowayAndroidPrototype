package ui;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.crashlytics.android.Crashlytics;
import com.qooway.consumerv01.R;

import data.DataStorageManager;
import data.Deserialize;
import data.EnumData;
import data.WebApiManager;
import framework.QoowayActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
	 

	public class MainActivity extends QoowayActivity {
		private Context context = this;
		private Boolean readyToStartNewIntend=false;
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.setUpStartUpSettings();
	        if(IsConneted())
	        {
	        Crashlytics.start(this);
	        
			setContentView(R.layout.splash);
			disableClicking();
	        Thread logoTimer = new Thread() {
	            public void run(){
	                try{
	                    int logoTimer = 0;
	                    checkLoginToken();
	                    while(logoTimer < 500){
	                        sleep(100);
	                        logoTimer = logoTimer +100;
	                    };

	                    int countTimer = 0 ;
	                    while(!readyToStartNewIntend )
	                    {
	                    	readyToStartNewIntend=LocationServicesOn();
	                    	sleep(100);
	                    	if(countTimer > 100)
	                    		readyToStartNewIntend= true;
	                    	countTimer++;
	                    }
	                    	
	                    startActivity(new Intent("com.qooway.consumerv01.CLEARSCREEN"));
	                } catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                 
	                finally{
	                    finish();
	                }
	            }
	        };  
	    	if(!LocationServicesOn()) {
				promptUserToTurnOnLocationServices() ;
			}
	        logoTimer.start();
	        }
	        else
	        {
	        	this.DisplayNoInternetImage();
	        }
	    }
	    
	    private void setUpStartUpSettings()
	    {
	    	DataStorageManager.getSingletonInstance().currentContext =this;
	    	DataStorageManager.getSingletonInstance().appVersion = EnumData.AppVersion.Development;
	    }
	    
	    
	    private void disableClicking()
	    {
	    	 View wholeView = (View) findViewById(R.id.splash_view);
	 		wholeView.setOnClickListener( new OnClickListener(){
	 		    @Override
	 		public void onClick(View v) {

	 		}});
	    }
	   
	    
	    private void checkLoginToken()
	    {
	    	DataStorageManager dataStorageManager = DataStorageManager.getSingletonInstance();
			dataStorageManager.setUserType(DataStorageManager.TypeOfUser.CUSTOMER);
			dataStorageManager.serverURL = dataStorageManager.getApiUrl();
			context = getApplicationContext();
			if (checkFile("ConsumerLoginToken", context)) {
				String loginToken = readFile("ConsumerLoginToken", context);
				if (loginToken != "") {

					dataStorageManager.loginToken = loginToken;
					try {
						DataStorageManager.getSingletonInstance()
								.setSerializationListType(
										EnumData.ListType.Customer);
						
						String result = WebApiManager.getSingletonInstance(this).getCustomerInfoByToken(loginToken);
						if(!result.equals("\"Cannot find user in Database\""))
						{
							Deserialize deserializer = new Deserialize();
							dataStorageManager.currentUser = deserializer
									.getCustomer(result);
							dataStorageManager.loggedIn = true;
						}
						// setTitle(dataStorageManager.getDisplayName());
						// Testing
						// showProgressDialogLoad(2000);
						// googlePlayManager.GCMregistration();

					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
	    }
	    
		@Override
		protected void onResume() {
			super.onResume();
			if(!IsConneted())
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
	    
		public void saveFile(String fileName, String fileContents, Context context) {
			FileOutputStream outputStream;

			try {
				outputStream = context.openFileOutput(fileName,
						Context.MODE_PRIVATE);
				outputStream.write(fileContents.getBytes());
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public String readFile(String fileName, Context context) {
			String ret = "";
			try {
				InputStream inputStream = context.openFileInput(fileName);

				if (inputStream != null) {
					InputStreamReader inputStreamReader = new InputStreamReader(
							inputStream);
					BufferedReader bufferedReader = new BufferedReader(
							inputStreamReader);
					String receiveString = "";
					StringBuilder stringBuilder = new StringBuilder();

					while ((receiveString = bufferedReader.readLine()) != null) {
						stringBuilder.append(receiveString);
					}

					inputStream.close();
					ret = stringBuilder.toString();
				} else {
					Toast.makeText(context, "File not found", Toast.LENGTH_SHORT)
							.show();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return ret;
		}

		public boolean deleteFile(String fileName, Context context) {
			File dir = context.getFilesDir();
			File file = new File(dir, fileName);
			boolean deleted = file.delete();
			return deleted;
		}

		public boolean checkFile(String fileName, Context context) {
			File dir = context.getFilesDir();
			File file = new File(dir, fileName);
			return file.exists();
		}
	

		public void promptUserToTurnOnLocationServices() {
			AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light));
			builder.setMessage(R.string.enable_location_services)
				   .setTitle("Location Services")
			       .setCancelable(false)
			       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			           }
			       })
			       .setNegativeButton("Not now", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   readyToStartNewIntend = true;
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
		}
	
	}

