package ui;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

import org.apache.http.util.ByteArrayBuffer;

import ui.drawer.DrawerItemAdapter;
import ui.drawer.DrawerModelAdapter;

import com.qooway.consumerv01.R;
import com.qooway.consumerv01.R.color;
import com.devspark.progressfragment.ProgressFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

import data.Deserialize;
import data.EnumData;
import data.Serialize;
import data.WebApiManager;
import data.WebApiManager;
import data.WebApiManagerPageFragment;
import framework.DataObject.Customer;
import data.DataStorageManager;
import framework.DataObject.SignUp;
import framework.DataObject.Merchant;
import framework.DataObject.Subcategory;
import framework.DataObject.WebCuisine;
import framework.QoowayActivity;
import framework.GooglePlay.GooglePlayManager;
import framework.Search.ConsumerSearchManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TextView;

public class MainScreenActivity extends QoowayActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {
	public DrawerLayout mDrawerLayout;
	public ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mTitle;
	@SuppressWarnings("unused")
	private CharSequence mDrawerTitle;
	private String[] mPAGETitles;
	public String httpserverUrl = "";
	public String httpsserverUrl = "";
	public TextView displayText;
	public Activity currentActvity;
	public ListView listViewToDisplay;
	public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	public static Location mCurrentLocation;
	public static LocationClient mLocationClient;
	// public CustomClass.CustomEditText login;
	// public CustomClass.CustomEditText password;
	public EditText login;
	public EditText password;
	public Button loginButton = null;
	public Runnable mPendingRunnable;
	public TabHost mTabHost;
	public Merchant selectedMerchant;
	public WebCuisine selectedCuisine;
	public Subcategory selectedCategory;
	public ConsumerSearchManager consumerSearchManager;
	public CustomerPageFragment currentFragment;
	public GooglePlayManager googlePlayManager ;
	private boolean isLoginScreen = false; // Ryan -> isLoginScreen initialized
											// to false
	private boolean isMyVoucherScreen = false; // Ryan
	public boolean isMyAccountScreen = false;
	private boolean isSignUp = false;
	public static QoowayActivity tester;
	private boolean newArrayMenuItems = false;
	private Context context = this;
	public DataStorageManager dataStorageManager;
	private FrameLayout frame; // Ryan & Robert -> AR 44 , push content over
								// with side bar
	private float lastTranslate = 0.0f;
	public ProgressDialog progressDialog = null;
	private boolean doubleBackToExitPressedOnce = false;
	public static boolean fromNavigationDrawer = false;
	private version appVersion = version.Development;
	public int MagicNumberAccountPageFragment = 12;
	public int MagicNumberNearByFragment = 3;
	private boolean FragmentLoading = false;
	private Handler mHandler;
	private DrawerItemAdapter drawAdapter;
	private Runnable mShowContentRunnable = new Runnable() {
		@Override
		public void run() {
			Boolean connected = mLocationClient.isConnected();
			if (connected ) {
				MainScreenActivity.this.selectItem(MagicNumberNearByFragment);
			} else {
				mHandler.postDelayed(mShowContentRunnable, 300);
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// showProgressDialogLoad(2000);
		// setTitle(R.string.Login); // add Login to top of screen

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mLocationClient = new LocationClient(this, this, this);
		mPAGETitles = getResources().getStringArray(R.array.menu_item);
		setTitle(mPAGETitles[MagicNumberNearByFragment]);
		WebApiManager.setQoowayActivity(this);

		tester = this;
		context = this;
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		googlePlayManager = new GooglePlayManager(this);
		googlePlayManager.GCMregistration();

		currentActvity = this;
    //	WebApiManager.getSingletonInstance(this);


		// Checks if version is up to date, otherwise lock app
		/*
		 * if(!checkVersion()) { changeFragment(17); return; }
		 */

		// Checks for LoginToken on file
		dataStorageManager =DataStorageManager.getSingletonInstance();
		setUpDrawer(savedInstanceState, dataStorageManager.loggedIn);
		mHandler = new Handler();
		mHandler.postDelayed(mShowContentRunnable, 375);
	}

	@Override
	protected void onResume() {
		setUpDrawer(null, dataStorageManager.loggedIn);
		if(mLocationClient == null)
		{
			mLocationClient = new LocationClient(this, this, this);
		}
		if(!mLocationClient.isConnected())
			mLocationClient.connect();
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);

	}

	private void setUpHideKeyboardListener(View view)

	{
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				MainScreenActivity.hideSoftKeyboard(MainScreenActivity.this);
				return false;
			}
		});
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.information).setVisible(!drawerOpen);
		menu.findItem(R.id.logout_button).setVisible(false);
		menu.findItem(R.id.done_button).setVisible(false);
		if (isMyAccountScreen) {
			menu.findItem(R.id.logout_button).setVisible(true);
		} else {
			menu.findItem(R.id.logout_button).setVisible(false);
		}

		// Ryan if MyVoucherScreen = true , make visible
		if (isMyVoucherScreen) {
			menu.findItem(R.id.information).setVisible(true);
		} else {
			menu.findItem(R.id.information).setVisible(false);
		}

	/*	if (isSignUp && !drawerOpen) {
			menu.findItem(R.id.join_button).setVisible(true);
		} else {
			menu.findItem(R.id.join_button).setVisible(false);
		}*/
		menu.findItem(R.id.join_button).setVisible(false);
		
		if (newArrayMenuItems == true && isLoginScreen) {
			menu.findItem(R.id.logout_button).setVisible(true);

		} else {
			menu.findItem(R.id.logout_button).setVisible(false);
		}

		if (newArrayMenuItems == true && isMyAccountScreen) {
			menu.findItem(R.id.logout_button).setVisible(true);

		}
		return super.onPrepareOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		MainScreenActivity.hideSoftKeyboard(MainScreenActivity.this);
		//
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		int itemId = item.getItemId();

		if (itemId == R.id.search) {
			// create intent to perform web search for this PAGE
			Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
			intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
			// catch event that there's no activity to handle intent
			if (intent.resolveActivity(getPackageManager()) != null) {
				startActivity(intent);
			} else {
				Toast.makeText(this, R.string.app_not_available,
						Toast.LENGTH_LONG).show();
			}
			return true;
		}

		if (itemId == R.id.logout_button) {
			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.favorite_dialog);
			dialog.setTitle("Logout:");

			TextView textView1_information = (TextView) dialog
					.findViewById(R.id.textView1_information);
			textView1_information.setText(R.string.Logout_Dialog);

			Button cancelButton = (Button) dialog
					.findViewById(R.id.button_cancel);
			// if button is clicked, close the custom dialog
			cancelButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			Button confirmButton = (Button) dialog
					.findViewById(R.id.button_confirm);
			// if button is clicked, close the custom dialog
			confirmButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					// String temp_ID =""+ dataStorageManager.deviceToken.ID;
					try {
						WebApiManager.getSingletonInstance()
								.getCustomerSignOut(
										readFile("ConsumerLoginToken", context));
						deleteFile("ConsumerLoginToken", context);
						dataStorageManager.loggedIn = false;
						dataStorageManager.currentUser = new Customer();
						setUpDrawer(null, false);
						setTitle(mPAGETitles[MagicNumberNearByFragment]);
						// DataStorageManager.getSingletonInstance().incrementAsyncTask();
						changeFragment(MagicNumberNearByFragment);
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

					dialog.dismiss();
				}
			});

			dialog.show();
			return true;
		}

		// else
		if (itemId == R.id.information) { // if click information action button
											// on myVouchers page
			final Dialog dialog = new Dialog(context);
			dialog.setContentView(R.layout.information_dialog);
			dialog.setTitle("How to redeem vouchers:");

			Button dialogButton = (Button) dialog
					.findViewById(R.id.button1_information);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.show();
			return true;
		}

	/*	if (itemId == R.id.join_button) {
			setupSignUpProcess();
			return true;
		} else {

		}*/
		return super.onOptionsItemSelected(item);
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (!MainScreenActivity.this.isFragmentLoading()) {
				MainScreenActivity.this.setFragmentLoading(true);
				MainScreenActivity.this.currentFragment.FragmentChanged=true;
				fromNavigationDrawer = true;
				selectItem(position);
				MainScreenActivity.hideSoftKeyboard(MainScreenActivity.this);
			}
			drawAdapter.notifyDataSetChanged();
		}
	}

	public void selectItem(final int position) {

		/*
		 * mPendingRunnable = new Runnable() {
		 * 
		 * @Override public void run() { // update the main content by replacing
		 * fragments
		 * 
		 * 
		 * 
		 * } };
		 */
		// update selected item and title, then close the drawer
		// update the main content by replacing fragments
		// update selected item and title, then close the drawer
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				mDrawerLayout.closeDrawer(mDrawerList);
			}
		}, 10);

		changeFragment(position);
		mDrawerList.setItemChecked(position, true);
		if (position == 0 && dataStorageManager.loggedIn) // If user selects
															// Home, Title will
															// be equal to
															// "Sort By"
		{
			setTitle(dataStorageManager.getDisplayName());
			mTitle = dataStorageManager.getDisplayName();
		} // if not Home, than use string from mPageTitle Array
		else {
			setTitle(mPAGETitles[position]);
			mTitle = mPAGETitles[position];
		}

	}

	public void changeFragment(final int position) {
		if (position == 0) {
			isLoginScreen = true;
		} else {
			isLoginScreen = false;
		}

		if (position == 1 && !dataStorageManager.loggedIn) {
			isSignUp = true;
		} else {
			isSignUp = false;
		}

		if (position == 16) {
			isMyVoucherScreen = true;
		} else {
			isMyVoucherScreen = false;
		}

		if (position == MagicNumberAccountPageFragment) {
			isMyAccountScreen = true;
		} else {
			isMyAccountScreen = false;
		}

		invalidateOptionsMenu(); // create to call onPreparationsMenu()

		ProgressFragment fragment = CustomerPageFragment.newInstance(
				MainScreenActivity.this);
		Bundle args = new Bundle();

		// Ryan Bug Fix For MyAccount
		if (dataStorageManager.loggedIn == true && position == 0) // If Logged
																	// in and
																	// position
																	// is equal
																	// to 0 ,
																	// MyAccount
																	// is
																	// selected,
																	// switch to
																	// appropriate
																	// MyAccount
																	// position
																	// in Array
																	// (14)
		{
			args.putInt(CustomerPageFragment.ARG_PAGE_NUMBER,
					MagicNumberAccountPageFragment); // Set to Myaccount
		} else {
			args.putInt(CustomerPageFragment.ARG_PAGE_NUMBER, position);
		}
		fragment.setArguments(args);
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		// getSupportFragmentManager().beginTransaction().add(android.R.id.content,
		// fragment).commit();

		this.currentFragment = (CustomerPageFragment) fragment;

	}

	//
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	// private classes

	public static class ErrorDialogFragment extends DialogFragment {
		// Global field to contain the error dialog
		private Dialog mDialog;

		// Default constructor. Sets the dialog field to null
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		// Set the dialog to display
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		// Return a Dialog to the DialogFragment.
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {

		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			switch (resultCode) {
			case Activity.RESULT_OK:
				break;
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		mLocationClient = new LocationClient(this, this, this);
		mLocationClient.connect();
	}

	@Override
	protected void onStop() {
		// Disconnecting the client invalidates it.
		mLocationClient.disconnect();
		super.onStop();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle connectionHint) {

		// TODO Auto-generated method stub

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBackPressed() {
		if (doubleBackToExitPressedOnce) {
			super.onBackPressed();
			return;
		}
		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, "Please press BACK again to exit.",
				Toast.LENGTH_SHORT).show();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				doubleBackToExitPressedOnce = false;
			}
		}, 2000);
	}

	public void setUpDrawer(Bundle savedInstanceState, Boolean account) {
		//mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerLayout.setDrawerShadow(color.DarkGray, GravityCompat.START);

		String[] mMenuItem = getResources().getStringArray(R.array.menu_item);
		if (account) {
			mMenuItem[0] = "My Account";
			mPAGETitles[0] = "My Account";
			newArrayMenuItems = true;

		} else {
			mMenuItem = getResources().getStringArray(R.array.logout_menu_item);
			mPAGETitles = getResources().getStringArray(
					R.array.logout_menu_item);
			mMenuItem[0] = "Log In";
			mPAGETitles[0] = "Log In"; //kakaoooooooooooooofk
			newArrayMenuItems = false;
		}

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon

		String[] mMenuItemFiles = getResources().getStringArray(
				R.array.menu_item_image_name);
		if (!account) {
			mMenuItemFiles = getResources().getStringArray(
					R.array.logout_menu_item_image_name);
		}
		DrawerModelAdapter.LoadModel(mMenuItem, mMenuItemFiles);
		String[] ids = new String[DrawerModelAdapter.Items.size()];
		for (int i = 0; i < ids.length; i++) {

			ids[i] = Integer.toString(i + 1);
		}
		DrawerItemAdapter adapter = new DrawerItemAdapter(this,
				R.layout.drawer_list_item, ids);
		if (account) {
			adapter.mThumbSelected = new Integer[] {
					R.drawable.login_myaccount_active,
					R.drawable.menu_favourite_active,
					R.drawable.menu_new_active,
					R.drawable.nearby_active, 
					R.drawable.search_active, 
					R.drawable.menu_redeem_my_points_active,
					R.drawable.reviews_active,
					R.drawable.about_active};
		} else {
			adapter.mThumbSelected = new Integer[] {
					R.drawable.login_myaccount_active, R.drawable.sign_up,
					R.drawable.menu_new_active,R.drawable.nearby_active,
					R.drawable.search_active,
					R.drawable.menu_redeem_my_points_active ,
					R.drawable.about_active };
		}
		/*
		 * if (account) { adapter.mThumbSelected = new Integer[] {
		 * R.drawable.login_myaccount_active, R.drawable.check_ins_active,
		 * R.drawable.nearby_active, R.drawable.home_active,
		 * R.drawable.menu_redeem_my_points_active, R.drawable.search_active,
		 * R.drawable.vouchers_active, R.drawable.menu_favourite_active,
		 * R.drawable.reviews_active, }; } else { adapter.mThumbSelected = new
		 * Integer[] { R.drawable.login_myaccount_active, R.drawable.sign_up,
		 * R.drawable.home_active, R.drawable.nearby_active,
		 * R.drawable.search_active, R.drawable.menu_redeem_my_points_active}; }
		 */

		mDrawerList.setAdapter(adapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		drawAdapter = adapter;

		frame = (FrameLayout) findViewById(R.id.content_frame); // Ryan & Robert
																// AR-44

		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.menu_icon4, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			/*
			// Ryan & Robert AR-44 , push over screen method
			@SuppressLint("NewApi")
			public void onDrawerSlide(View drawerView, float slideOffset) {
				float moveFactor = (mDrawerList.getWidth() * slideOffset);

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					frame.setTranslationX(moveFactor);
				} else {
					TranslateAnimation anim = new TranslateAnimation(
							lastTranslate, moveFactor, 0.0f, 0.0f);
					anim.setDuration(0);
					anim.setFillAfter(true);
					frame.startAnimation(anim);

					lastTranslate = moveFactor;
				}
			}
			*/

			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()

				if (mPendingRunnable != null) {
					Handler mHandler = new Handler();
					mHandler.post(mPendingRunnable);
					mPendingRunnable = null;
				}

			}

			public void onDrawerOpened(View drawerView) {
				// getActionBar().setTitle(mDrawerTitle);

				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// took this out because it was making my "MyVoucher" Fragment go back
		// to "MyAccount" when toggled
		/*
		 * if (savedInstanceState == null) { selectItem(0); }
		 */
		// Testing
		setUpHideKeyboardListener(mDrawerLayout);
		//
		invalidateOptionsMenu();
		mPAGETitles = mMenuItem;
	}

	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), 0);
		if(activity!=null)
		{
			((MainScreenActivity) activity).shiftViewDown();

		}


	}

	public static void showSoftKeyboard(final Activity activity,
			final EditText editText) {
		(new Handler()).postDelayed(new Runnable() {

			public void run() {
				InputMethodManager inputMethodManager = (InputMethodManager) activity
						.getSystemService(Activity.INPUT_METHOD_SERVICE);
				inputMethodManager.showSoftInput(editText,
						InputMethodManager.SHOW_IMPLICIT);

			}
		}, 150);
	}

	public void shiftViewDown() {
		if(this.currentFragment!=null)
			this.currentFragment.shiftViewDown();
	}

	public static Bitmap DownloadFullFromUrl(String imageFullURL) {
		Bitmap bm = null;
		try {
			URL url = new URL(imageFullURL);
			URLConnection ucon = url.openConnection();
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			bm = BitmapFactory.decodeByteArray(baf.toByteArray(), 0,
					baf.toByteArray().length);
		} catch (IOException e) {
			Log.d("ImageManager", "Error: " + e);
		}
		return bm;
	}



	public boolean checkVersion() {
		String versionName = "";
		String currentVersion = "";
		try {
			versionName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
			currentVersion = WebApiManager.getSingletonInstance()
					.getVersionName();
			currentVersion = currentVersion.replaceAll("^\"|\"$", "");
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return versionName.equalsIgnoreCase(currentVersion);
	}

	public void promptServerError(int http) {
		alertDialogBuilder = new AlertDialog.Builder(MainScreenActivity.this);

		// set title
		alertDialogBuilder.setTitle("Server Error");

		// set dialog message
		alertDialogBuilder
				.setMessage(
						"\n" + http
								+ " Server Error.\nPlease try again later\n")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
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

	public boolean isFragmentLoading() {
		return this.FragmentLoading;
	}

	public boolean setFragmentLoading(Boolean bool) {
		return this.FragmentLoading = bool;
	}

	private void UpdateLocation()
	{
		Location newLocation =  MainScreenActivity.mLocationClient
				.getLastLocation();
		if(newLocation!=null)
			dataStorageManager.currentLocation =newLocation;
	}

	public enum version {
		Demo, Development, Release
	}
}