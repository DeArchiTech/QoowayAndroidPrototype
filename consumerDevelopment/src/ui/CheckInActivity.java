package ui;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.qooway.consumerv01.R;
import data.Deserialize;
import data.Serialize;
import data.WebApiManager;
import framework.DataObject.CheckIn;
import data.DataStorageManager;
import framework.DataObject.Voucher;
import framework.QoowayActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CheckInActivity extends QoowayActivity {

	public WebApiManager webApiManager;
	public DataStorageManager dataStorageManager;
	// private ViewPager mPager;
	// private PagerAdapter mPagerAdapter;
	// private static final int NUM_PAGES = 5;
	// private int scrollViewXPosition = 0;
	private String checkInID = "";
	private LockableScrollView horizontalScroll;
	// private Boolean attachWithVoucher=false;
	private Boolean hasVouchersToAttach = false;
	private Boolean checkedStatus = false;
	private int MerchantID;
	private List<Voucher> VoucherList;
	private HolderFragment Bottom;
	private static String noPendingCheckIn = "No pending Check In";
	private static final String confirm = "Confirm";
	private Context context;
	// private static final String decline = "Decline";
	RelativeLayout secondFrame = null;
	RelativeLayout thirdFrame = null;
	int secondAnchor = 0;
	int thirdAnchor = 0;
	private TextView confirmationText;

	private BroadcastReceiver CheckInNotificationReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					"com.google.android.c2dm.intent.RECEIVE")) {
				Bundle extras = intent.getExtras();
				if (CheckInActivity.this.checkInID.equals((String) extras
						.get("CheckInID"))) {
					@SuppressWarnings("unused")
					String Action = (String) extras.get("Action");
					String CheckInID = (String) extras.get("CheckInID");
					String Qoopoints = (String) extras.get("Qoopoints");
					Log.i("CheckIn", CheckInID);
					DataStorageManager dataStorageManager = DataStorageManager
							.getSingletonInstance();
					CheckIn currentCheckIn = new CheckIn();
					if (dataStorageManager.checkInList.size() != 0) {
						for (int i = 0; i < dataStorageManager.checkInList
								.size(); i++) {
							CheckIn item = dataStorageManager.checkInList
									.get(i);
							if (item.CheckInID.equals(CheckInID)) {
								currentCheckIn = item;
							}
						}
					}
					currentCheckIn.Status = (String) extras.get("Status");
					if (currentCheckIn.Status.equals("Confirm")) {
						dataStorageManager = DataStorageManager
								.getSingletonInstance();
						webApiManager = WebApiManager.getSingletonInstance();
						String CustomerID = CheckInActivity.this.dataStorageManager.currentUser.CustomerID;
						String result = "";
						try {
							result = webApiManager
									.getCustomerInfo(dataStorageManager.currentUser.CustomerID);
							if (result != null) {
								Deserialize deserializer = new Deserialize();
								dataStorageManager.currentUser = deserializer
										.getCustomer(result);
							}

						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Log.i("CheckIn", currentCheckIn.Status);
						CheckInActivity.this.swipeToConfirmedPage();
						/*
						 * Toast.makeText(context,
						 * getString(R.string.check_approve),
						 * Toast.LENGTH_LONG).show();
						 */
					} else {
						if (hasVouchersToAttach)
							getVoucherFragment().displayYesNoImage(false);
						else {
							CheckInActivity.this.swipeToPendingPage();
							/*
							 * Toast.makeText(context,
							 * getString(R.string.check_decline),
							 * Toast.LENGTH_LONG).show();
							 */
							if (hasVouchersToAttach)
								getVoucherFragment().displayYesNoImage(false);
						}
					}
				}
			}
		}
	};

	// else if (currentCheckIn.Status.equals(decline))

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			dataStorageManager.loggedIn = true;
			finish();
			return true;
		}

		return super.onOptionsItemSelected(menuItem);
	}

	 @Override
	 protected void onResume() {
	  super.onResume();
	  DataStorageManager.getSingletonInstance().currentActivity = this;
	 }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getActionBar().setTitle("Claim Deal");
		setContentView(R.layout.fragment_checkin_process);
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		webApiManager = WebApiManager.getSingletonInstance();
		dataStorageManager = DataStorageManager.getSingletonInstance();
		context = getApplicationContext();
		Bundle bundleBottom = new Bundle();
		// boolean voucher = false;

		LocalBroadcastManager bManager = LocalBroadcastManager
				.getInstance(this);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.google.android.c2dm.intent.RECEIVE");
		bManager.registerReceiver(CheckInNotificationReceiver, intentFilter);
		horizontalScroll = (LockableScrollView) findViewById(R.id.horizontal_checkin);

		RelativeLayout Cancel = (RelativeLayout) findViewById(R.id.cancelButton);

		MerchantID = dataStorageManager.selectedMerchant.StoreID;

		this.confirmationText = (TextView) findViewById(R.id.confirmation_number);

		String CustomerID = this.dataStorageManager.currentUser.CustomerID;
		VoucherList = this.getVoucher(CustomerID, MerchantID);

		if (VoucherList != null && !VoucherList.equals("[]")) {
			bundleBottom.putString("Fragment", "Voucher");
			bundleBottom.putString("VoucherList",
					this.serializeList(VoucherList));
			this.setHasVouchersToBeAttached(true);
		} else {
			bundleBottom.putString("Fragment", "NoVoucher");
			this.setHasVouchersToBeAttached(false);
		}

		Bottom = new HolderFragment();
		Bottom.setArguments(bundleBottom);
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.Voucher_frame, Bottom)
				.commit();

		HandleCheckInAction();

		Cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// swipeToConfirmedPage();
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						CheckInActivity.this);

				// set title
				alertDialogBuilder.setTitle("Cancel");

				// set dialog message
				alertDialogBuilder
						.setMessage("Are you sure?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										checkInCancel();
										CheckInActivity.this.finish();
										if (hasVouchersToAttach) {
											getVoucherFragment().clearImage();
										}
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										dialog.cancel();
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
			}
		});

		secondFrame = (RelativeLayout) findViewById(R.id.secondFrame);
		thirdFrame = (RelativeLayout) findViewById(R.id.thirdFrame);

		secondAnchor = secondFrame.getLeft();
		thirdAnchor = thirdFrame.getLeft();

		Display display = getWindowManager().getDefaultDisplay();
		@SuppressWarnings("deprecation")
		int width = display.getWidth();

		secondFrame.getLayoutParams().width = width - 24;
		secondFrame.requestLayout();
		thirdFrame.getLayoutParams().width = width - 10;
		thirdFrame.requestLayout();
		if (!checkedStatus) {
			this.checkCheckInStatus(CustomerID, MerchantID);
		}
		checkedStatus = true;
		
		this.setCheckInNumber(DataStorageManager.getSingletonInstance().currentUser.PointCardCode);

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (!checkedStatus) {
			String CustomerID = this.dataStorageManager.currentUser.CustomerID;
			this.checkCheckInStatus(CustomerID, MerchantID);
		}
		checkedStatus = true;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.

	}

	private void checkCheckInStatus(String CustomerID, int MerchantID) {
		List<CheckIn> CheckInList = this.getCheckIn(CustomerID, MerchantID);
		if (CheckInList != null) {
			CheckIn checkIn = CheckInList.get(0);
			/*if (checkIn != null)
				this.setCheckInNumber(checkIn.PointCardCode);*/
			Boolean withVoucher = true;
			if (checkIn.VoucherCode == null
					|| checkIn.VoucherCode.equals("null")) {
				withVoucher = false;
			}
			// Display mDisplay = this.getWindowManager().getDefaultDisplay();
			// this.notifyHolderAboutYesnoImage(withVoucher);
			// this.getVoucherFragment().displayYesNoImage(false);
		/*COMMENTED OUT	if (withVoucher) {
				getVoucherFragment().displayYesNoImage(withVoucher);
			}*/
			this.swipeToPendingPage();
		}
		horizontalScroll.setScrollingEnabled(false);
	}

	private List<CheckIn> getCheckIn(String CustomerID, int MerchantID) {
		List<CheckIn> CheckInList = null;
		try {
			dataStorageManager.checkInActivity = true;
			String result = this.webApiManager.getCheckStatus(CustomerID,
					MerchantID);
			if (result == null)
				return CheckInList;
			else if (result.equalsIgnoreCase("\"Wrong Token\"")) {
				return CheckInList;
			} else if (CheckInActivity.noPendingCheckIn.equals(result
					.substring(1, result.length() - 1)))
				return CheckInList;
			else if (result != null && !result.equals("[]")) {
				Deserialize deserializer = new Deserialize();
				CheckIn CheckIn = deserializer.getCheckIn(result);
				CheckInList = new ArrayList<CheckIn>();
				CheckInList.add(CheckIn);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dataStorageManager.checkInActivity = false;
		return CheckInList;
	}

	private List<Voucher> getVoucher(String CustomerID, int MerchantID) {
		List<Voucher> voucherList = null;
		try {
			String result = this.webApiManager.getVoucherRedeemedWithID(
					CustomerID, MerchantID, 0);
			// String temp = result;
			if (result != null && !result.equals("[]")) {
				Deserialize deserializer = new Deserialize();
				voucherList = deserializer.getVoucherList(result);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return voucherList;
	}

	private String serializeList(List<Voucher> input) {
		return Serialize.listVoucher(input);
	}

	private String getFirstVoucherCode() {
		Voucher test = new Voucher();
		if (this.VoucherList != null) {
			test = VoucherList.get(0);
			String code = test.VoucherCode;
			return code;

		}
		return "";
	}

	private void checkInRequestAttempt(boolean withVoucher) {
		String CustomerID = dataStorageManager.currentUser.CustomerID;
		String result = "";
		try {
			if (withVoucher) {
				result = webApiManager.getCheckInRequestVoucher(CustomerID,
						MerchantID, this.getFirstVoucherCode());

			} else {
				result = webApiManager
						.getCheckInRequest(CustomerID, MerchantID);
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (result == null) {
			showErrorDialog();
			return;
		}

		this.HandleCheckInResult(result, withVoucher);

	}

	private Boolean failureMessage(String result) {
		return result
				.equalsIgnoreCase("\"Exceeded the number pending requests that are allowed at this moment\"")
				|| result
						.equalsIgnoreCase("\"Customer doesn't have this Voucher\"")
				|| result
						.equalsIgnoreCase("\"Customer doesn't have this Voucher\"")
		;
	}

	private Boolean pendingRequest(String result) {
		return  result
						.equalsIgnoreCase("\"Pending Request\"")			;
	}

	
	private void checkInCancel() {
		String CustomerID = dataStorageManager.currentUser.CustomerID;
		String result = "";
		try {
			result = webApiManager.getCheckInCancel(CustomerID, MerchantID);
			Log.i("CheckIn", "Result:" + result);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void HandleCheckInAction() {
		if (this.hasVouchersToAttach) {
			// COMMENTED OUT AGAIN MADABI!
			// CheckInActivity.this.promptVoucherDialog();
			checkInRequestAttempt(true);
		}

		else
			checkInRequestAttempt(false);
	}

	private void HandleCheckInResult(String result, boolean withVoucher) {
		if (!this.HasErrorAndDisplayError(result))
			this.AllowCheckIn(result, withVoucher);
	}

	private Boolean HasErrorAndDisplayError(String result) {

		Boolean Result = false;
		if (failureMessage(result)) {
	//		promptDialog("Error", result, "Close");
			Result = true;
		}
		return Result;
		/*
		 * String ErrorResponse[] = getResources().getStringArray(
		 * R.array.checkin_response); for (String item : ErrorResponse) { if
		 * (item.equals(result.substring(1, result.length() - 1))) {
		 * this.PromtMessage(item); return true; } }
		 */
	}

	private void AllowCheckIn(String result, boolean withVoucher) {
		LockableScrollView passInScroll = this.horizontalScroll;
		passInScroll.scrollToSecondPage();
		if(!pendingRequest(result))
		{
			CheckIn checkIn = null;
			Deserialize deserializer = new Deserialize();
			checkIn = deserializer.getCheckIn(result);
			if (checkIn != null) {
				if (dataStorageManager.checkInList == null)
					dataStorageManager.checkInList = new ArrayList<CheckIn>();
				dataStorageManager.checkInList.add(checkIn);
				CheckInActivity.this.checkInID = checkIn.CheckInID;
			}
			// this.notifyHolderAboutYesnoImage(withVoucher);
			if (withVoucher) {
				// this.getVoucherFragment().displayYesNoImage();
			}
		}
	}

	private void setCheckInNumber(String PointCardCode) {
		int PointCardCodeLenght = PointCardCode.length();
		String LastTwoDigit = PointCardCode.substring(PointCardCodeLenght - 2,
				PointCardCodeLenght);
		this.confirmationText.setText(LastTwoDigit);
	}

	private void promptVoucherDialog() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					checkInRequestAttempt(true);
					getVoucherFragment().displayYesNoImage(true);
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					checkInRequestAttempt(false);
					getVoucherFragment().displayYesNoImage(false);
					break;
				}
			}
		};
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Would you like to redeem your voucher now")
				.setPositiveButton("Yes", dialogClickListener)
				.setNegativeButton("No", dialogClickListener).show();
	}

	private void swipeToPendingPage() {
		secondAnchor = secondFrame.getLeft();
		horizontalScroll.smoothScrollTo(secondAnchor, 0);
	}

	private void swipeToConfirmedPage() {
		thirdAnchor = thirdFrame.getLeft();
		horizontalScroll.smoothScrollTo(thirdAnchor, 0);
	}

	private void setHasVouchersToBeAttached(boolean bool) {
		this.hasVouchersToAttach = bool;
	}

	private HolderFragment getVoucherFragment() {
		return this.Bottom;
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
			return new AlertDialog.Builder(getActivity())
					.setTitle(title)
					.setMessage(
							"\nServer Error\nPlease check your Internet connection and try again later\n")
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							}).create();
		}
	}

	public void showErrorDialog() {
		DialogFragment newFragment = MyAlertDialogFragment
				.newInstance(R.string.server_error);
		newFragment.show(getFragmentManager(), "dialog");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {

		}
	}

	@Override
	public void promptDialog(String Title, String Message, String PositiveButton) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light));
		builder.setMessage(Message)
				.setTitle(Title)
				.setCancelable(false)
				.setPositiveButton(PositiveButton,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								finish();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
		TextView titleView = (TextView) alert.findViewById(this.getResources()
				.getIdentifier("alertTitle", "id", "android"));
		if (titleView != null) {
			titleView.setGravity(Gravity.CENTER);
		}
	}
	// case CONNECTION_FAILURE_RESOLUTION_REQUEST:
	/*
	 * If the result code is Activity.RESULT_OK, try to connect again
	 */
	// switch (resultCode) {
	// case Activity.RESULT_OK:
	/*
	 * <service android:name="com.qooway.consumerv01.MainScreenActivity" >
	 * <meta-data android:name="onMessageOpen"
	 * android:value="com.qooway.consumerv01.MainScreenActivity" /> </service>
	 * 
	 * 
	 * Try the request again
	 * 
	 * 
	 * // break;
	 * 
	 * 
	 * 
	 * 
	 * 
	 * /* private void setAttachVoucher(boolean bool) {
	 * this.attachWithVoucher=bool; }
	 * 
	 * private int getMerchantID() { return this.MerchantID; }
	 * 
	 * private LockableScrollView getLockableScrollView() { return
	 * this.horizontalScroll; }
	 * 
	 * private void notifyHolderAboutYesnoImage(boolean withVoucher) {
	 * this.getVoucherFragment().setHasAYesNoImage(true); if(withVoucher) {
	 * this.getVoucherFragment().setYesNoImage(HolderFragment.YesNoImage.Yes); }
	 * else {
	 * this.getVoucherFragment().setYesNoImage(HolderFragment.YesNoImage.No);
	 * 
	 * } }
	 */

}
