package framework.Login;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import com.devspark.progressfragment.ProgressFragment;
import com.qooway.consumerv01.R;

import android.app.ProgressDialog;
import android.content.Context;
import ui.CustomerPageFragment;
import ui.MainScreenActivity;
import ui.home.HomeListModelAdapter;
import ui.merchantList.MerchantListModelAdapter;
import data.Deserialize;
import data.EnumData;
import data.WebApiManager;
import data.DataStorageManager;
import data.WebApiManagerPageFragment;
import framework.DataObject.Token;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginManager {

	private WebApiManager webApiManager;
	private DataStorageManager dataStorageManager;
	private  MainScreenActivity mainScreenActivity;
	private View rootView;
	private Context context;	
	private Handler mHandler;
	public String SENDER_ID = "987553829283";
	private Button LoginButton;
	private Toast loadingMessage;
	private ProgressDialog progressDialog;
	private Runnable mShowContentRunnable = new Runnable() {
		@Override
		public void run() {
			LoginManager.this.CanecelMessage();
			if (DataStorageManager.getSingletonInstance().getAsyncTaskCount() == 0) {
				if(dataStorageManager.currentUser == null){
					LoginManager.this.mainScreenActivity.PromtMessage("User Name and Password do not match");
					if(dataStorageManager.lastCode < 200 || dataStorageManager.lastCode > 299){
						mainScreenActivity.promptServerError(dataStorageManager.lastCode);
					}
		
				}
				else if (!dataStorageManager.currentUser.CustomerID.isEmpty()) {
					dataStorageManager.loggedIn = true;
					String message = "Welcome "+DataStorageManager.getSingletonInstance().getDisplayName();
					String warning = "Google Registration has failed";
					String notice = (mainScreenActivity.googlePlayManager.regid.isEmpty() ? warning : message);
					Toast.makeText(context, notice, Toast.LENGTH_LONG).show();
					dataStorageManager.setloggedIn(true);
					mainScreenActivity.getActionBar().setHomeButtonEnabled(true);

					LoginManager.this.mainScreenActivity.saveFile("ConsumerLoginToken", dataStorageManager.loginCredentials.LoginToken ,context);
					DataStorageManager.getSingletonInstance().loginToken = dataStorageManager.loginCredentials.LoginToken;
					if (dataStorageManager.loggedIn == true) {
						// showProgressDialogAuth();
						mainScreenActivity.setTitle(dataStorageManager.getDisplayName()); // set
																					// title
																					// to
																					// my
																					// Account
						mainScreenActivity.setUpDrawer(null, true);
						mainScreenActivity.changeFragment(mainScreenActivity.MagicNumberAccountPageFragment);
					}
				} else {
					mainScreenActivity.PromtMessage("Failed to login please try again");
				}
				
			} else {
				mHandler.postDelayed(mShowContentRunnable, 300);
			}
			
			LoginManager.this.enableButtonTobeClickable(true);

		}
	};
	
	
	public LoginManager(WebApiManager webApiManager,
			DataStorageManager dataStorageManager, MainScreenActivity mainScrenActivity) {
		this.webApiManager = webApiManager;
		this.dataStorageManager = dataStorageManager;
		this.mainScreenActivity= mainScrenActivity;
		context = mainScrenActivity.getApplicationContext();
	}

	
	
	public void login(String username, String password , Button button) {
		this.RequestData(username, password);
		this.setLoginButton(button);
		String result = "";
		Token token = new Token();
		Deserialize deserializer = new Deserialize();
		if (dataStorageManager.userType == DataStorageManager.TypeOfUser.CUSTOMER) {
			String regID = "";
			regID = mainScreenActivity.googlePlayManager.regid; 
			if(regID.isEmpty()){
				regID = Secure.getString(context.getContentResolver(),
                        Secure.ANDROID_ID);
			}
			
		}
		mHandler = new Handler();
		this.enableButtonTobeClickable(false);
		this.PromptMessage("Logging in, please wait");
		mHandler.postDelayed(mShowContentRunnable, 375);
		
	}
	
	private void RequestData(String username, String password) {

		String regID = "";
		regID = mainScreenActivity.googlePlayManager.regid; 
			try {
				
				DataStorageManager.getSingletonInstance().setSerializationListType(EnumData.ListType.Login);
				
				if (dataStorageManager.userType == DataStorageManager.TypeOfUser.CUSTOMER) {
					WebApiManagerPageFragment.getSingletonInstance().getCustomerTokenLogin(username, password, regID);
				} else if (dataStorageManager.userType == DataStorageManager.TypeOfUser.MERCHANT) {
			
				WebApiManagerPageFragment.getSingletonInstance().getMerchantLogin(username, password);
			} 
			}catch (UnsupportedEncodingException e) {
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
	public void loadRecievedData() {


	}
	
	 private void PromptMessage(String input)
	 {
			Toast toast = null;
			int duration = toast.LENGTH_LONG;
			toast = Toast.makeText(this.context, input, duration);
			//mainScreenActivity.setContentView((View)rootView.findViewById(R.layout.activity_main));
			mainScreenActivity.showProgressSpinner(duration);
			//mainScreenActivity.showProgressDialogLoad(duration);
			toast.show();
	 }

	 private void CanecelMessage()
	 {
		 if(this.loadingMessage!=null)
			 this.loadingMessage.cancel();
	 }
	 
	
	private void enableButtonTobeClickable(Boolean bool)
	{
		this.getLoginButton().setClickable(bool);
	}
	
	private void setLoginButton(Button button)
	{
		this.LoginButton=button;
	}
	
	private Button getLoginButton()
	{
		return this.LoginButton;
	}


}