package framework.GooglePlay;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import ui.MainScreenActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import data.DataStorageManager;
import framework.QoowayActivity;
import framework.DataObject.Token;
import data.Deserialize;


public class GooglePlayManager {
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public MainScreenActivity mainScreenActivity;
	public String SENDER_ID = "987553829283";
	public static final String TAG = "GCMDemo";
	public TextView mDisplay;
	public GoogleCloudMessaging gcm;
	public AtomicInteger msgId = new AtomicInteger();
	public SharedPreferences prefs;
	public Context context;
	public String regid;
	public String serverApiKey = "AIzaSyAk_cQkE99UF3gaFLMmp2F1tU8QvtpE_sg";
	public Handler handler;
	DataStorageManager dataStorageManager = DataStorageManager.getSingletonInstance();

	public GooglePlayManager(QoowayActivity MA) {
		this.mainScreenActivity = (MainScreenActivity)MA;
		this.context = MA.getApplicationContext();

	}

	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	public boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this.mainScreenActivity);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode,
						this.mainScreenActivity,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i("tag", "This device is not supported.");
				mainScreenActivity.finish();
			}
			return false;
		}
		return true;
	}

	public void GCMregistration() {

		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(this.mainScreenActivity);
			regid = getRegistrationId(context);
			registerInBackground();
			if (regid.isEmpty()) {

			}
		} else {
			Log.i(TAG, "No valid Google Play Services APK found.");
		}
	}

	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 * 
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	public String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return this.mainScreenActivity.getSharedPreferences(
				MainScreenActivity.class.getSimpleName(), Context.MODE_PRIVATE);
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {

		new AsyncTask<Void, String, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(SENDER_ID);
					msg = "Device registered, registration ID=" + regid;

					// You should send the registration ID to your server over
					// HTTP,
					// so it can use GCM/HTTP or CCS to send messages to your
					// app.
					// The request to your server should be authenticated if
					// your app
					// is using accounts.

					// int dataBaseID
					// =GooglePlayManager.this.mainScreenActivity.dataStorageManager.currentUser.StoreID;

					// For this demo: we don't need to send it because the
					// device
					// will send upstream messages to a server that echo back
					// the
					// message using the 'from' address in the message.

					// Persist the regID - no need to register again.

				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				storeRegistrationId(context, regid);
				/*
				try {
					GooglePlayManager.this.getIDFromProfitekServer(regid);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
			}

		}.execute(null, null, null);

	}

	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 * 
	 * @param context
	 *            application's context.
	 * @param regId
	 *            registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	private void sendRegistrationIdToBackend(String regid, String dataBaseID,
			String App) {
		try {
			this.mainScreenActivity.webApiManager.postApiToken(regid,
					dataBaseID, App);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getIDFromProfitekServer(String regid)
			throws InterruptedException, ExecutionException {
		String result = "";
		String databaseID = "";
		if (this.mainScreenActivity.dataStorageManager.currentUser != null)
			databaseID = this.mainScreenActivity.dataStorageManager.currentUser.CustomerID;
		result = this.mainScreenActivity.webApiManager.getDeviceToken(
				databaseID, regid, "Consumer");

		while (result == null  || result == "" || result.equals("[]")|| result.equals("null")) {
			this.sendRegistrationIdToBackend(regid, databaseID, "Consumer");
			result = this.mainScreenActivity.webApiManager.getDeviceToken(
					databaseID, regid, "Consumer");
		}
		Deserialize deserializer = new Deserialize();
		Token deviceToken = deserializer.getToken(result);

		dataStorageManager.deviceToken = deviceToken;
	}
		
}