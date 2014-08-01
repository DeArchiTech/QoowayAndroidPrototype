package ui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ui.favorite.FavoriteListItemAdapter;
import ui.favorite.FavoriteListModelAdapter;
import ui.home.HomeDisplayListItem;
import ui.home.HomeListItemAdapter;
import ui.home.HomeListModelAdapter;
import ui.merchantList.MerchantDisplayListItem;
import ui.merchantList.MerchantListItemAdapter;
import ui.merchantList.MerchantListModelAdapter;
import ui.newSearch.CategoryListModelAdapter;
import ui.newSearch.SearchListActivity;
import ui.review.ReviewListItemAdapter;
import ui.review.ReviewListModelAdapter;
import ui.voucher.ListVoucherListItemAdapter;
import ui.voucher.ListVoucherListModelAdapter;
import ui.voucher.MyVoucherActivity;
import CustomClass.CustomEditText;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.devspark.progressfragment.ProgressFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.qooway.consumerv01.R;

import data.Deserialize;
import data.WebApiManager;
import data.WebApiManagerPageFragment;
import data.DataStorageManager;
import data.EnumData;
import framework.PageFragment;
import framework.DataObject.Merchant;
import framework.DataObject.VoucherList;
import framework.Login.LoginManager;

@SuppressLint("ValidFragment")
public class CustomerPageFragment extends PageFragment {
	public static final String ARG_PAGE_NUMBER = "PAGE_number";
	private MainScreenActivity mainActivity;
	private WebApiManagerPageFragment WebApiManagerPageFragmentObj;
	private DataStorageManager dataStorageManager;
	private View rootView;
	private boolean isListLoading = false;
	private String fragment_name = "";
	private boolean refresh_voucher_list = false;
	public static FavoriteListItemAdapter f_Adapter; // needed for favorites
	public static ArrayList<String> favoriteIDS; // Ryan short cut to resolve
													// problem, need to fix for
													// efficiency and coding
													// standardsss
	public Context context;
	public ProgressDialog progressDialog = null;
	private Handler mHandler;
	private int MagicNumberAccountPageFragment = 12;
	public int MagicNumberNearByFragment = 3;
	public int MagicNumberReddemPointsFragment = 5;
	public MerchantListItemAdapter mAdapterTEMP;
	private ArrayAdapter listAdapter;
	private View footerView;
	private View mContentView;
	private LayoutInflater globalInflater;
	private Bundle lastBundle;
	private Activity globalAcitivty;
	private EnumData.FragmentName FragmentName;
	private EnumData.Mode LoadingMode = EnumData.Mode.Start;
	private LoadingMore LoadingMoreMode = LoadingMore.None;
	private Boolean requestMoreData = false;
	private ListScrollListener ScrollListener;
	public Boolean FragmentChanged = false;
	private Boolean ListLoading = false;
	private EnumData.GPSStatus GPSStatus = EnumData.GPSStatus.Off;

	private Runnable mShowContentRunnable = new Runnable() {
		@Override
		public void run() {
			if (CustomerPageFragment.this.IsConneted()) {
				Boolean allTasksCompleted = DataStorageManager
						.getSingletonInstance().getAsyncTaskCount() == 0;
				if (allTasksCompleted) {
					if (CustomerPageFragment.this.getLoadingMode() == EnumData.Mode.Start)
						CustomerPageFragment.this.loadRecievedData();
					else if (CustomerPageFragment.this.getLoadingMode() == EnumData.Mode.Refresh)
						CustomerPageFragment.this.RefreshData();
				} else {
					mHandler.postDelayed(mShowContentRunnable, 300);
				}
			} else {
				CustomerPageFragment.this.DisplayNoInternetImage();
				CustomerPageFragment.this.setContentShown(true);
				CustomerPageFragment.this.onPageFinish();
			}
		}
	};

	public static final CustomerPageFragment newInstance(MainScreenActivity MA) {
		CustomerPageFragment CPF = new CustomerPageFragment();
		CPF.mainActivity = MA;
		CPF.WebApiManagerPageFragmentObj = WebApiManagerPageFragment
				.getSingletonInstance(MA);
		CPF.dataStorageManager = DataStorageManager.getSingletonInstance();
		CPF.context = MA.getApplicationContext();
		return CPF;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, Bundle savedInstanceState) {
		this.setLoadingMode(EnumData.Mode.Start);
		// Quick Fix for Check In Confirmation Bug
		if (!dataStorageManager.currentUser.CustomerID.isEmpty()) {
			dataStorageManager.loggedIn = true;
		}

		int i = getArguments().getInt(ARG_PAGE_NUMBER);
		String menuItem = null;
		if (i < (getResources().getStringArray(R.array.menu_item).length + 1)) {
			menuItem = dataStorageManager.loggedIn ? getResources()
					.getStringArray(R.array.menu_item)[i] : getResources()
					.getStringArray(R.array.logout_menu_item)[i];
		} else {
			int index = i
					- getResources().getStringArray(R.array.menu_item).length;
			menuItem = getResources().getStringArray(
					R.array.addtional_Fragments)[index];
		}
		fragment_name = "";
		menuItem = menuItem.replace(" ", "");
		EnumData.FragmentName name = EnumData.FragmentName.valueOf(menuItem);
		try {
			switch (name) {
			case Login:
				if (this.dataStorageManager.loggedIn) {
					mainActivity.changeFragment(MagicNumberAccountPageFragment);
				} else {
					rootView = createLogin(inflater, container,
							savedInstanceState);
				}
				break;
			case Nearby:
				rootView = createNearby(inflater, container);
				break;
			// TAKE OUT FOR CHECK IN
			/*
			 * case CheckIn: rootView = createCheckIn(inflater, container);
			 * break;
			 */
			case SignUp:
				rootView = createSignUp(inflater, container);
				break;
			case MyAccount:
				rootView = createMyAccount(inflater, container);
				break;
			case MyReviews:
				rootView = createMyReviews(inflater, container);
				break;
			case Search:
				rootView = createSearch(inflater, container);
				break;
			case New:
				rootView = createNew(inflater, container);
				break;
			case RedeemPoints:
				rootView = createRedeemPoints(inflater, container);
				break;
			case Favourites:
				rootView = createMyFavourites(inflater, container);
				break;
			case About:
				rootView = createAbout(inflater, container);
				break;
			case Error:
				rootView = createError(inflater, container, savedInstanceState);
				break;
			default:
				rootView = inflater.inflate(R.layout.fragment_none, container,
						false);
				break;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		rootView.setOnTouchListener(new OnTouchListener() {
			private Activity activity = CustomerPageFragment.this.mainActivity;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mainActivity.hideSoftKeyboard(this.activity);
				shiftViewDown();
				return false;
			}
		});
		mContentView = rootView;
		setHasOptionsMenu(true);
		this.setCurrentName(PageFragment.FragmentName.valueOf(menuItem));
		this.setFragmentName(name);
		// dataStorageManager.setCurentPageFragment(this);
		this.lastBundle = savedInstanceState;
		this.setInstanceState(savedInstanceState);
		this.setInflator(inflater);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	private void setUpRedeemListAdapter(List<VoucherList> voucher,
			ListView listView) {
		ListVoucherListModelAdapter.LoadModel(voucher);
		String[] ids = new String[ListVoucherListModelAdapter.Items.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = Integer.toString(i + 1);
		}
		ListVoucherListItemAdapter Adapter = new ListVoucherListItemAdapter(
				getActivity(), R.layout.list_item_redeem,
				ListVoucherListModelAdapter.Items);
		listAdapter = Adapter;
		listView.setAdapter(Adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (dataStorageManager.loggedIn) {
					Intent i = new Intent(getActivity(),
							RedeemPointsActivity.class);
					String MerchantID = ListVoucherListModelAdapter.Items
							.get(position).MerchantID;
					String StoreName = ListVoucherListModelAdapter.Items
							.get(position).Name;
					String voucherName = ListVoucherListModelAdapter.Items
							.get(position).Description;
					i.putExtra("voucherName", voucherName );  // RYAN
					i.putExtra("MerchantID", MerchantID);
					i.putExtra("StoreName", StoreName);
					startActivity(i);
				} else {
					Toast.makeText(context,
							"You must be logged in to redeem points",
							Toast.LENGTH_LONG).show();
				}
			}
		});

	}

	public String displayTotalPoints() {
		return "You currently have: "
				+ dataStorageManager.currentUser.NetPoints + " QooPoints";
	}

	public void onPause() {
		super.onPause();
		if (fragment_name.equalsIgnoreCase("redeemMyPoints")) {
			{
				this.setLoadingMode(EnumData.Mode.Refresh);
			}
			refresh_voucher_list = true;
		} else {
			refresh_voucher_list = false;
		}
	}

	private boolean IsConneted() {
		ConnectivityManager connMgr = (ConnectivityManager) this.mainActivity
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		Boolean result = true;
		if (networkInfo == null) {
			result = false;
		}
		return networkInfo != null;
	}

	private void DisplayNoInternetImage() {
		setContentView(R.layout.fragment_update_app);
	}

	public void onResume() {
		super.onResume();
		if (DataStorageManager.getSingletonInstance().justSignedUp) {
			DataStorageManager.getSingletonInstance().justSignedUp = false;
			mainActivity.getActionBar().setTitle("Nearby");
			mainActivity.selectItem(MagicNumberNearByFragment);
		}
		if (!IsConneted()) {
			DisplayNoInternetImage();
		} else {
			getActivity().getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			if (CustomerPageFragment.this.getLoadingMode() == EnumData.Mode.Refresh) {
				this.RequestData(this.getFragmentName(), null);
				this.setLoadingMode(EnumData.Mode.Refresh);
				this.obtainData();
			}
			CustomerPageFragment.this.setListClickable(true);
			/*
			 * if (refresh_voucher_list) { String result = ""; // - AR-47 try {
			 * result = WebApiManager.getSingletonInstance().getVoucherAll(); }
			 * catch (InterruptedException e) { e.printStackTrace(); } catch
			 * (ExecutionException e) { e.printStackTrace(); } Deserialize
			 * deserializer = new Deserialize();
			 * dataStorageManager.RedeemableVouchers = deserializer
			 * .getVoucherList(result); mainActivity.listViewToDisplay =
			 * (ListView) rootView .findViewById(R.id.redeemableVoucherList);
			 * setUpRedeemListAdapter(dataStorageManager.RedeemableVoucherList,
			 * mainActivity.listViewToDisplay); TextView totalPoints =
			 * (TextView) rootView .findViewById(R.id.voucher_title); String
			 * total_points = displayTotalPoints();
			 * totalPoints.setText(total_points); }
			 */
		}
	}

	public void BackButtonPressed() {

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setContentView(mContentView);
		int j = getArguments().getInt(ARG_PAGE_NUMBER);
		String menuItem = "";
		if (j < (getResources().getStringArray(R.array.menu_item).length + 1)) {
			menuItem = dataStorageManager.loggedIn ? getResources()
					.getStringArray(R.array.menu_item)[j] : getResources()
					.getStringArray(R.array.logout_menu_item)[j];
		} else {
			int index = j
					- getResources().getStringArray(R.array.menu_item).length;
			menuItem = getResources().getStringArray(
					R.array.addtional_Fragments)[index];
		}
		menuItem = menuItem.replace(" ", "");
		EnumData.FragmentName name = EnumData.FragmentName.valueOf(menuItem);
		switch (name) {
		case Login:
			this.setupLogin(this.getActivity());
			break;
		case SignUp:
			this.setupSignUp(this.getActivity());
			break;
		case Nearby:
			this.setupNearby(this.getActivity());
			break;
		// TAKE OUT FOR CHECK IN
		/*
		 * case CheckIn: this.setupCheckIn(this.getActivity()); break;
		 */
		case MyAccount:
			this.setUpMyAccount(this.getActivity(), savedInstanceState);
			break;
		case MyReviews:
			this.setupMyReviews(this.getActivity());
		case Search:
			this.setupSearch(this.getActivity());
			break;
		case New:
			this.setupNew(this.getActivity());
			break;
		case Favourites:
			this.setupFavourites(this.getActivity());
			break;
		case RedeemPoints:
			this.setupRedeem(this.getActivity());
			break;
		case About:
			this.setupAbout(this.getActivity());
			break;
		case Error:
			this.setupError(this.getActivity());
			break;
		default:
			break;
		}
		if (ui.MainScreenActivity.fromNavigationDrawer == true) {
			// dataStorageManager.decrementAsyncTask();
			ui.MainScreenActivity.fromNavigationDrawer = false;
		}
		this.RequestData(this.getFragmentName(), null);
		this.setInstanceState(savedInstanceState);

	}

	private View createError(LayoutInflater inflater, ViewGroup container,
			final Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_error, container, false);
		mainActivity.setUpDrawer(savedInstanceState, false);
		mainActivity.getActionBar().setDisplayHomeAsUpEnabled(false);
		mainActivity.getActionBar().setHomeButtonEnabled(false);
		mainActivity.getActionBar().setTitle("");
		mainActivity.mDrawerLayout
				.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		return rootView;
	}

	private View createMyAccount(LayoutInflater inflater, ViewGroup container) {
		rootView = inflater.inflate(R.layout.fragment_my_account, container,
				false);
		rootView.findViewById(R.id.clickMyVouchers).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(getActivity(),
								MyVoucherActivity.class);
						startActivity(i);
						//TODO highlight stuff here
					}
				});
		rootView.findViewById(R.id.clickQooPointsHistory).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(getActivity(),
								QooPointsHistoryActivity.class);
						startActivity(i);
					}
				});
		mContentView = rootView;
		return rootView;
	}

	private View createAbout(LayoutInflater inflater, ViewGroup container) {
		rootView = inflater.inflate(R.layout.fragment_about, container, false);

		rootView.findViewById(R.id.about_one_box).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(getActivity(),
								WhatIsQoowayActivity.class);
						startActivity(i);
					}
				});
		rootView.findViewById(R.id.about_two_box).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(getActivity(),
								HowDoIGetInstantDealsActivity.class);
						startActivity(i);
					}
				});
		rootView.findViewById(R.id.about_three_box).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(getActivity(),
								HowDoIredeemQooPointsActivity.class);
						startActivity(i);
					}
				});

		mContentView = rootView;
		return rootView;
	}

	private View createLogin(LayoutInflater inflater, ViewGroup container,
			final Bundle savedInstanceState)
			throws UnsupportedEncodingException, InterruptedException,
			ExecutionException {
		View rootView = null;
		rootView = inflater.inflate(R.layout.fragment_login, container, false);
		/*
		 * getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.
		 * SOFT_INPUT_STATE_ALWAYS_HIDDEN); mainActivity.login =
		 * (CustomClass.CustomEditText) rootView.findViewById(R.id.userName);
		 * mainActivity.password = (CustomClass.CustomEditText)
		 * rootView.findViewById(R.id.password);
		 * setUpDoneButtonEditText(mainActivity.login);
		 * setUpDoneButtonEditText(mainActivity.password);
		 */

		mainActivity.login = (EditText) rootView.findViewById(R.id.userName);
		mainActivity.password = (EditText) rootView.findViewById(R.id.password);
		mainActivity.loginButton = (Button) rootView
				.findViewById(R.id.logInButton);
		mainActivity.loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					mainActivity.hideSoftKeyboard(getActivity());
					shiftViewDown();
					this.login(CustomerPageFragment.this.mainActivity.loginButton);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			private void login(Button button) throws IOException,
					InterruptedException, ExecutionException {
				LoginManager loginManager = new LoginManager(WebApiManager
						.getSingletonInstance(),
						mainActivity.dataStorageManager, mainActivity);
				loginManager.login(mainActivity.login.getText().toString(),
						mainActivity.password.getText().toString(), button);

			}
		});

		rootView.findViewById(R.id.forgotPassword).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						Intent i = new Intent(getActivity(),
								ForgotPasswordActivity.class);
						startActivity(i);
					}
				});

		/*
		 * mainActivity.password.setOnClickListener(new View.OnClickListener() {
		 * public void onClick(View v) { PageFragment.this.shiftViewUp(); } });
		 * 
		 * mainActivity.password.setOnFocusChangeListener(new
		 * OnFocusChangeListener() { public void onFocusChange(View arg0,
		 * boolean arg1) { if (arg1) { PageFragment.this.shiftViewUp(); } } });
		 * 
		 * mainActivity.login.setOnClickListener(new View.OnClickListener() {
		 * public void onClick(View v) { PageFragment.this.shiftViewUp(); } });
		 * 
		 * mainActivity.login.setOnFocusChangeListener(new
		 * OnFocusChangeListener() { public void onFocusChange(View arg0,
		 * boolean arg1) { if (arg1) { PageFragment.this.shiftViewUp(); } } });
		 */

		mContentView = rootView;
		return rootView;
	}

	private View createRedeemPoints(LayoutInflater inflater, ViewGroup container) {

		rootView = inflater.inflate(R.layout.fragment_vouchers, container,
				false);
		globalInflater = inflater;
		mContentView = rootView;
		return rootView;

	}

	private View createNew(LayoutInflater inflater, ViewGroup container) {

		HomeListModelAdapter.ClearList();
		rootView = inflater.inflate(R.layout.fragment_nearby, container, false);
		return rootView;
	}

	private View createSignUp(LayoutInflater inflater, ViewGroup container) {
		View rootView = inflater.inflate(R.layout.fragment_signup, container,
				false);
		rootView.findViewById(R.id.sign_up_view).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						Intent i = new Intent(getActivity(),
								SignUpActivity.class);
						i.putExtra("ActionType", "SignUp");
						startActivity(i);
					}
				});
		rootView.findViewById(R.id.activate_card_view).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(getActivity(),
								SignUpActivity.class);
						i.putExtra("ActionType", "Activate");
						startActivity(i);
					}
				});
		
		mContentView = rootView;
		return rootView;
	}

	private View createNearby(LayoutInflater inflater, ViewGroup container) {

		MerchantListModelAdapter.ClearList();
		rootView = inflater.inflate(R.layout.fragment_nearby, container, false);
		return rootView;
	}

	private View createMyReviews(LayoutInflater inflater, ViewGroup container) {

		rootView = inflater.inflate(R.layout.fragment_my_review, container,
				false);
		mContentView = rootView;
		return rootView;
	}

	private View createSearch(LayoutInflater inflater, ViewGroup container) {
		rootView = inflater.inflate(R.layout.fragment_search, container, false);
		final ImageButton restaurantsView = (ImageButton) rootView
				.findViewById(R.id.restaurantsView);
		final ImageButton retailView = (ImageButton) rootView
				.findViewById(R.id.retailView);
		final EditText inputSearch = (EditText) rootView
				.findViewById(R.id.inputSearch);
		inputSearch.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (MotionEvent.ACTION_UP == event.getAction()) {
					// ROBERT? inputSearch.setGravity(Gravity.LEFT|
					// Gravity.TOP);
				}
				return false;
			}
		});
		inputSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				if (s.length() > 0) {
					
					// RYAN
					if(s.toString().trim().length() == 0){  // All white spaces
						
						inputSearch.setText("");
					}
				} else {
					
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
		inputSearch
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView arg0, int arg1,
							KeyEvent arg2) {

						String inputSearchText = (arg0.getText().toString())
								.replaceAll(" ", "%20");
						if (arg0.getText().toString().isEmpty()) {
							Toast.makeText(context, R.string.no_search_results,
									Toast.LENGTH_LONG).show();
							return false;
						}

						setRequestmoreData(true);
						RequestData(
								CustomerPageFragment.this.getFragmentName(),
								inputSearchText);
						obtainData();
						return false;
					}
				});
		restaurantsView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isConnectingToInternet()
						&& !CustomerPageFragment.this.mainActivity
								.isFragmentLoading()) {
					String whichScreen = "Restaurant";
					Intent i = new Intent(getActivity(),
							SearchListActivity.class);
					i.putExtra("whichScreen", whichScreen);
					startActivity(i);
				} else {
					showErrorDialog();
				}
			}
		});
		retailView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isConnectingToInternet()
						&& !CustomerPageFragment.this.mainActivity
								.isFragmentLoading()) {
					String whichScreen = "Retail";
					Intent i = new Intent(getActivity(),
							SearchListActivity.class);
					i.putExtra("whichScreen", whichScreen);
					startActivity(i);
				} else {
					showErrorDialog();
				}
			}
		});
		mContentView = rootView;
		return rootView;
	}

	private View createMyFavourites(LayoutInflater inflater, ViewGroup container) {

		rootView = inflater.inflate(R.layout.fragment_myfavorites, container,
				false);

		mContentView = rootView;
		return rootView;
	}

	private void setupLogin(Activity act) {
		mainActivity.mDrawerList.setItemChecked(0, true);
		// setContentView(rootView);
		setEmptyText(R.string.empty);
		obtainData();
	}

	private void setupError(Activity act) {
	}

	private void setupSignUp(Activity act) {
		// setContentView(rootView);
		setEmptyText(R.string.empty);
		obtainData();
	}

	private void setupNearby(Activity act) {

		// setContentView(rootView);
		setEmptyText(R.string.empty);
		obtainData();
	}

	/*
	 * BE PROACTIVE private View createCheckIn(LayoutInflater inflater,
	 * ViewGroup container) { MainScreenActivity.mCurrentLocation =
	 * MainScreenActivity.mLocationClient .getLastLocation();
	 * dataStorageManager.currentLocation = MainScreenActivity.mLocationClient
	 * .getLastLocation(); try { String result =
	 * WebApiManagerPageFragment.getApiMerchantSort(
	 * MerchantListModelAdapter.startIndex,
	 * MerchantListModelAdapter.recieveAmount,
	 * dataStorageManager.currentLocation.getLatitude(),
	 * dataStorageManager.currentLocation.getLongitude()); if (result == null) {
	 * dataStorageManager.SelectedMerchantList = new ArrayList<Merchant>();
	 * showErrorDialog(); } else if (result .equalsIgnoreCase(
	 * "\"Index Array Out of Range, please use a smaller startIndex or Count\""
	 * )) { // In this case, do nothing } else if (result
	 * .equalsIgnoreCase("\"There are no more merchants to load\"")) { // In
	 * this case, do nothing } else if
	 * (result.equalsIgnoreCase("\"No more merchants to show\"")) { // In this
	 * case, do nothing } else { Deserialize deserializer = new Deserialize();
	 * dataStorageManager.SelectedMerchantList = deserializer
	 * .getMerchantList(result); } rootView =
	 * inflater.inflate(R.layout.fragment_checkin, container, false);
	 * mainActivity.listViewToDisplay = (ListView) rootView
	 * .findViewById(R.id.listView1); View tempView =
	 * inflater.inflate(R.layout.pb_layout, null);
	 * mainActivity.listViewToDisplay.setTag(R.integer.layout, tempView); // for
	 * // access // to // footerView ImageView pullDown = (ImageView) tempView
	 * .findViewById(R.id.pullDown); pullDown.setVisibility(0);
	 * mainActivity.listViewToDisplay.addFooterView(tempView); this.footerView =
	 * tempView; } catch (InterruptedException e1) { e1.printStackTrace(); }
	 * catch (ExecutionException e1) { e1.printStackTrace(); } String
	 * whichScroll = "nearby"; mainActivity.listViewToDisplay
	 * .setOnScrollListener(new ListScrollListener(
	 * mainActivity.listViewToDisplay, inflater, whichScroll));
	 * setUpListSearchBar(rootView, R.id.inputSearch); // setup search bar in //
	 * check ins mContentView = rootView; return rootView; }
	 */

	// sets up search bar in check ins
	private void setUpListSearchBar(View rootView, final int search_R_ID) {

		final EditText inputSearch = (EditText) rootView
				.findViewById(search_R_ID);
		/*
		 * BE PROACTIVE inputSearch.setOnTouchListener(new OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) {
		 * if(MotionEvent.ACTION_UP == event.getAction()) { //ROBERT?
		 * inputSearch.setGravity(Gravity.LEFT| Gravity.TOP); } return false; }
		 * }); inputSearch.addTextChangedListener(new TextWatcher() {
		 * 
		 * @Override public void onTextChanged(CharSequence s, int arg1, int
		 * arg2, int arg3) { if (s.length() > 0){ // position the text type in
		 * the left top corner //ROBERT? inputSearch.setGravity(Gravity.LEFT|
		 * Gravity.TOP); } else { // no text entered. Center the hint text.
		 * //ROBERT? inputSearch.setGravity(Gravity.CENTER); } }
		 * 
		 * @Override public void beforeTextChanged(CharSequence arg0, int arg1,
		 * int arg2, int arg3) { }
		 * 
		 * @Override public void afterTextChanged(Editable arg0) { } });BE
		 * PROACTIVE
		 */
		inputSearch
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView arg0, int arg1,
							KeyEvent arg2) {
						if (arg0.getText().toString().isEmpty()) {
							Toast.makeText(context, R.string.no_search_results,
									Toast.LENGTH_LONG).show();
							return false;
						}
						String searchString = arg0.getText().toString();
						CustomerPageFragment.this
								.setLoadingMoreMode(LoadingMoreMode.TapSearch);
						CustomerPageFragment.this.RequestData(
								CustomerPageFragment.this.getFragmentName(),
								searchString);
						CustomerPageFragment.this.obtainData();
						return false;
					}
				});

		inputSearch.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					AddOverLayToBackGround();

				} else {
					RemoveOverLayFromBackGround();
				}
			}
		});
		inputSearch.requestFocus();
		inputSearch.clearFocus();
		inputSearch.requestFocus();
		mainActivity.showSoftKeyboard(CustomerPageFragment.this.mainActivity,
				inputSearch);

	}

	@SuppressWarnings("static-access")
	private void setupCheckIn(Activity act) {
		MerchantListModelAdapter.LoadJsonModel(
				dataStorageManager.SelectedMerchantList,
				dataStorageManager.currentLocation);
		MerchantListItemAdapter mAdapterCheckin = new MerchantListItemAdapter(
				getActivity(), R.layout.merchant_list_item,
				MerchantListModelAdapter.Items);
		mainActivity.listViewToDisplay.setAdapter(mAdapterCheckin);
		mainActivity.listViewToDisplay
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if (position != dataStorageManager.SelectedMerchantList
								.size() && !isListLoading) {
							CustomerPageFragment.this.setListClickable(false);
							if (MerchantListModelAdapter.Items.isEmpty()) {
								showErrorDialog();
								return;
							}
							MerchantDisplayListItem merchant = MerchantListModelAdapter.Items
									.get(position);
							String result = "";
							try {
								result = WebApiManager.getSingletonInstance()
										.getMerchantInfo(merchant.StoreID);
							} catch (InterruptedException e) {
								e.printStackTrace();
							} catch (ExecutionException e) {
								e.printStackTrace();
							}
							if (result == null) {
								showErrorDialog();
								return;
							}
							Deserialize deserializer = new Deserialize();
							Merchant selectedMerchant = deserializer
									.getMerchant(result);
							dataStorageManager.selectedMerchant = selectedMerchant;
							Intent intent = new Intent(getActivity(),
									CheckInActivity.class);
							startActivity(intent);
						}

					}
				});
		final EditText inputSearch = (EditText) rootView
				.findViewById(R.id.inputSearch);
		inputSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				if (s.length() > 0) {
					// position the text type in the left top corner
					inputSearch.setGravity(Gravity.LEFT);
				} else {
					// no text entered. Center the hint text.
					inputSearch.setGravity(Gravity.CENTER);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
		inputSearch.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View view, MotionEvent motionEvent) {
				return true;
			}
		});
		// setContentView(rootView);
		// Setup text for empty content
		setEmptyText(R.string.empty);
		obtainData();
	}

	private void setUpMyAccount(Activity act, Bundle savedInstanceState) {

		// setContentView(rootView);
		setEmptyText(R.string.empty);
		obtainData();
	}

	private void setupMyReviews(Activity act) {
		// setContentView(rootView);
		setEmptyText(R.string.empty);
		obtainData();
	}

	private void setupSearch(Activity act) {
		// setContentView(rootView);
		setEmptyText(R.string.empty);
		obtainData();
	}

	private void setupAbout(Activity act) {
		// setContentView(rootView);
		setEmptyText(R.string.empty);
		obtainData();
	}

	private void setupNew(Activity act) {

		// setContentView(rootView);
		setEmptyText(R.string.empty);
		obtainData();
	}

	private void setupRedeem(Activity act) {

		setEmptyText(R.string.empty);

		globalAcitivty = act;
		mContentView = rootView;
		obtainData();
	}

	private void setupFavourites(Activity act) {
		// setContentView(rootView);
		setEmptyText(R.string.empty);
		obtainData();
	}

	public void loadMoreData(EnumData.FragmentName fragmentName) {
		switch (fragmentName) {
		case Nearby:
			if (!dataStorageManager.getMaxedMerchants()) {
				int SubListStartIndex = CalculateStartIndex(
						dataStorageManager.SelectedMerchantList.size(),
						MerchantListModelAdapter.recieveAmount);
				int SubListEndIndex = dataStorageManager.SelectedMerchantList
						.size();
				List<Merchant> dataToAppend = dataStorageManager.SelectedMerchantList
						.subList(SubListStartIndex, SubListEndIndex);
				MerchantListModelAdapter.LoadJsonModel(dataToAppend,
						dataStorageManager.currentLocation);
				this.mainActivity.listViewToDisplay.requestLayout();
			} else {
				RemoveFooterView();
			}
			break;
		case New:
			if (!dataStorageManager.getMaxedMerchants()) {
				int SubListStartIndex = CalculateStartIndex(
						dataStorageManager.SelectedMerchantList.size(),
						HomeListModelAdapter.recieveAmount);
				int SubListEndIndex = dataStorageManager.SelectedMerchantList
						.size();
				List<Merchant> dataToAppend = dataStorageManager.SelectedMerchantList
						.subList(SubListStartIndex, SubListEndIndex);
				HomeListModelAdapter.LoadModel(dataToAppend);
				this.mainActivity.listViewToDisplay.requestLayout();
			} else {
				RemoveFooterView();
			}
			break;
		default:
			break;
		}

		dataStorageManager.slidingAction = isListLoading = false;
		CustomerPageFragment.this.setRequestmoreData(false);
	}

	private int CalculateStartIndex(int listSize, int GroupSize) {
		int result;
		int remainder = listSize % GroupSize;
		if (remainder == 0) {
			result = listSize - GroupSize;
		} else {
			result = (listSize / GroupSize) * GroupSize;
		}
		return result;
	}

	public class ListScrollListener implements OnScrollListener {
		private int currentVisibleItemCount;
		private int currentFirstVisibleItem;
		private int currentScrollState;
		private int totalItemCount;
		private String whichScroll;
		private ListView Lv;

		public ListScrollListener() {
		}

		public ListScrollListener(ListView lv, LayoutInflater inflater,
				String whichScroll) {
			this.Lv = lv;
			this.whichScroll = whichScroll;
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if (!isListLoading) {
				this.currentFirstVisibleItem = firstVisibleItem;
				this.currentVisibleItemCount = visibleItemCount;
				this.totalItemCount = totalItemCount;
			}

		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (!isListLoading
					&& CustomerPageFragment.this.LoadingMoreMode == LoadingMore.None) {
				this.currentScrollState = scrollState;
				this.isScrollCompleted();
			}

		}

		private void isScrollCompleted() {
			if (this.currentVisibleItemCount + this.currentFirstVisibleItem == this.totalItemCount
					&& this.currentScrollState == SCROLL_STATE_IDLE) {
				LinearLayout footer = (LinearLayout) Lv
						.getTag(R.integer.layout);
				ImageView pullDown = (ImageView) footer
						.findViewById(R.id.pullDown);
				pullDown.setVisibility(View.GONE);
				ProgressBar progressBar = (ProgressBar) footer
						.findViewById(R.id.progressBar);
				progressBar.setVisibility(View.VISIBLE);
				final Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					private Boolean GPSJustTurnedOn() {
						Boolean result = false;
						EnumData.GPSStatus previous = CustomerPageFragment.this
								.GetGPSStatus();
						if (dataStorageManager.currentLocation != null)
							CustomerPageFragment.this
									.setGPSStatus(EnumData.GPSStatus.On);
						result = (previous == EnumData.GPSStatus.Off && CustomerPageFragment.this
								.GetGPSStatus() == EnumData.GPSStatus.On);
						return result;
					}

					@Override
					public void run() {
						mainActivity.getActionBar().setHomeButtonEnabled(false);
						if (!isListLoading) {
							isListLoading = true;
							CustomerPageFragment.this.setRequestmoreData(true);
							if (whichScroll.equals("nearby")) {
								CustomerPageFragment.this.UpdateLocation();
								if (GPSJustTurnedOn())
									mainActivity
											.selectItem(MagicNumberNearByFragment);
								else {
									CustomerPageFragment.this.RequestData(
											EnumData.FragmentName.Nearby, null);
									obtainData();
								}
							} else if (whichScroll.equals("new")) {
								CustomerPageFragment.this.RequestData(
										EnumData.FragmentName.New, null);
								obtainData();
							}

						}
					}
				}, 1000);
			}
		}

		public void resetFooterView() {
			LinearLayout footer = (LinearLayout) Lv.getTag(R.integer.layout);
			ImageView pullDown = (ImageView) footer.findViewById(R.id.pullDown);
			pullDown.setVisibility(View.VISIBLE);
			ProgressBar progressBar = (ProgressBar) footer
					.findViewById(R.id.progressBar);
			progressBar.setVisibility(View.GONE);
		}
	}

	public boolean checkVersion() {
		String versionName = "";
		String currentVersion = "";
		try {
			versionName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
			currentVersion = WebApiManager.getSingletonInstance()
					.getVersionName();
			if (currentVersion == null) {
				return false;
			}
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

	public void showProgressDialogAuth() {
		progressDialog = ProgressDialog.show(getActivity(), "Please wait ...",
				"Authenticating ...", true);
		progressDialog.setCancelable(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
				}
				progressDialog.dismiss();
			}
		}).start();
	}

	public void showProgressDialogLoad(final int time) {
		progressDialog = ProgressDialog.show(getActivity(), "Please wait ...",
				"Loading ...", true);
		progressDialog.setCancelable(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(time);
				} catch (Exception e) {
				}
				progressDialog.dismiss();
			}
		}).start();
	}

	public void shiftViewUp() {
		int heightDiff = rootView.getHeight()
				- rootView.getRootView().getHeight()
				- CustomerPageFragment.this.mainActivity.getActionBar()
						.getHeight();
		rootView.setY(heightDiff);
	}

	public void shiftViewDown() {
		// rootView.setY(0);
	}

	public void setUpDoneButtonEditText(CustomClass.CustomEditText editText) {
		editText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {

					InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
							.getSystemService(Activity.INPUT_METHOD_SERVICE);
					inputMethodManager.hideSoftInputFromWindow(getActivity()
							.getCurrentFocus().getWindowToken(), 0);

					Handler mHandler = new Handler(); // / have to delay or else
														// little bug
					mHandler.postDelayed(new Runnable() {
						public void run() {
							rootView.setY(0);
						}
					}, 100);

					return true;
				}
				return false;

			}
		});
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
		// DialogFragment newFragment =
		// MyAlertDialogFragment.newInstance(R.string.server_error);
		// newFragment.show(getFragmentManager(), "dialog");
	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		return false;
	}

	private void obtainData() {

		this.mainActivity.setFragmentLoading(true);
		mainActivity.getActionBar().setHomeButtonEnabled(false);
		mHandler = new Handler();
		mHandler.postDelayed(mShowContentRunnable, 375);
	}

	private void loadLogin() {

	}

	private void loadNearBy() {
		/*
		 * if (result == null) { dataStorageManager.SelectedMerchantList = new
		 * ArrayList<Merchant>(); showErrorDialog(); } else if (result
		 * .equalsIgnoreCase
		 * ("\"Index Array Out of Range, please use a smaller startIndex or Count\""
		 * )) { // In this case, do nothing } else if (result
		 * .equalsIgnoreCase("\"There are no more merchants to load\"")) { // In
		 * this case, do nothing } else if
		 * (result.equalsIgnoreCase("\"No more merchants to show\"")) { // In
		 * this case, do nothing } else { }
		 */
		if (this.getRequestmoreDataBool()) {
			this.loadMoreData(this.getFragmentName());
		} else {
			if (this.LoadingMoreMode == LoadingMore.TapSearch) {
				if (dataStorageManager.SearchBarMerchantList.isEmpty()) {
					Toast.makeText(context, R.string.no_search_results,
							Toast.LENGTH_LONG).show();
					//kakao taking tint away
					RelativeLayout dimLayout = (RelativeLayout) rootView.findViewById(R.id.dimView);
					dimLayout.setVisibility(View.INVISIBLE);

				} else {
					RemoveFooterView();
					MerchantListModelAdapter.ClearList();
					MerchantListModelAdapter
							.LoadJsonModel(
									dataStorageManager.SearchBarMerchantList,
									DataStorageManager.getSingletonInstance().currentLocation);
					MerchantListItemAdapter mAdapter = new MerchantListItemAdapter(
							getActivity(), R.layout.merchant_list_item,
							MerchantListModelAdapter.Items);
					mainActivity.listViewToDisplay.setAdapter(mAdapter);
					mainActivity.listViewToDisplay
							.setOnItemClickListener(new OpenMerchantDetailOnItemClickListener(
									dataStorageManager.SearchBarMerchantList));
					mainActivity.listViewToDisplay.requestLayout();
					//kakao taking tint away
					RelativeLayout dimLayout = (RelativeLayout) rootView.findViewById(R.id.dimView);
					dimLayout.setVisibility(View.INVISIBLE);
				}
			} else {
				mainActivity.listViewToDisplay = (ListView) rootView
						.findViewById(R.id.listView1);
				View tempView = this.getInflator().inflate(R.layout.pb_layout,
						null);
				mainActivity.listViewToDisplay.setTag(R.integer.layout,
						tempView);
				ImageView pullDown = (ImageView) tempView
						.findViewById(R.id.pullDown);
				// pullDown.setVisibility(0);
				mainActivity.listViewToDisplay.addFooterView(tempView);
				this.footerView = tempView;

				View tab_search_merchant_button_view = rootView
						.findViewById(R.id.tab_search_merchant_button_view);
				final Button searchButton = (Button) rootView
						.findViewById(R.id.search_merchant_button);
				final LayoutInflater inflaterToPassIn = this.getInflator();
				searchButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						;
						setSearchButton(searchButton, inflaterToPassIn);
						setUpListSearchBar(rootView, R.id.inputSearchBar);
						//kakao adding tint
						RelativeLayout dimLayout = (RelativeLayout)rootView.findViewById(R.id.dimView);
						dimLayout.setVisibility(View.VISIBLE);
						
						//kakao removing tint if tapping it
						if (isVisible() == true){
							dimLayout.setOnClickListener(new View.OnClickListener(){
								public void onClick(View v){
									RelativeLayout dimLayout = (RelativeLayout)rootView.findViewById(R.id.dimView);
									dimLayout.setVisibility(View.INVISIBLE);
									MainScreenActivity.hideSoftKeyboard(getActivity());
									searchButton.setVisibility(View.VISIBLE);
									revertSearchButton(searchButton); //kakao putting red taphere button back
								}
							});
						}
					}

					/* public boolean onKeyDown(int keyCode, KeyEvent event){
						if(keyCode == KeyEvent.KEYCODE_BACK){
							RelativeLayout dimLayout = (RelativeLayout)rootView.findViewById(R.id.dimView);
							dimLayout.setVisibility(View.INVISIBLE);
						}
							return true;
					} */
					public boolean onBackPressed(int keyCode, KeyEvent event){
						if(keyCode == KeyEvent.KEYCODE_BACK){
							RelativeLayout dimLayout = (RelativeLayout)rootView.findViewById(R.id.dimView);
							dimLayout.setVisibility(View.INVISIBLE);
							revertSearchButton(searchButton);
						}
							return true;						
					}
				});
				/*
				 * BE PROACTIVE TextView nearby_title = (TextView)
				 * rootView.findViewById(R.id.nearby_title);
				 * 
				 * // If logged in display points if(dataStorageManager.loggedIn
				 * == true){ String total_points = displayTotalPoints();
				 * nearby_title.setText(total_points); } else { // if not logged
				 * in display sign up nearby_title.setText(R.string.sign_up); }
				 * BE PROACTIVE
				 */

				String whichScroll = "nearby";
				mainActivity.listViewToDisplay
						.setOnScrollListener(new ListScrollListener(
								mainActivity.listViewToDisplay, this
										.getInflator(), whichScroll));
				mContentView = rootView;

				MerchantListModelAdapter.LoadJsonModel(
						dataStorageManager.SelectedMerchantList,
						dataStorageManager.currentLocation);
				MerchantListItemAdapter mAdapter = new MerchantListItemAdapter(
						getActivity(), R.layout.merchant_list_item,
						MerchantListModelAdapter.Items);
				mAdapterTEMP = mAdapter;
				mainActivity.listViewToDisplay.setAdapter(mAdapter);
				mainActivity.listViewToDisplay
						.setOnItemClickListener(new OpenMerchantDetailOnItemClickListener(
								dataStorageManager.SelectedMerchantList));
			}
		}

	}
	
	//helper function
	public void toggleTint(){
		
	}

	private void loadSignUp() {
	}

	private void loadSearch() {

		if (this.getRequestmoreDataBool()) {
			if (dataStorageManager.SearchBarMerchantList.isEmpty()) {
				Toast.makeText(context, R.string.no_search_results,
						Toast.LENGTH_LONG).show();
			} else {
				Intent i = new Intent(getActivity(), SearchBarActivity.class);
				String whichSearchBar = "search";
				i.putExtra("whichSearchBar", whichSearchBar);
				startActivity(i);
			}
		}
	}

	private void loadNew() {
		/*
		 * BE PROACTIVE TextView nearby_title = (TextView)
		 * rootView.findViewById(R.id.nearby_title); // If logged in display
		 * points
		 * 
		 * if(dataStorageManager.loggedIn == true){
		 * 
		 * String total_points = displayTotalPoints();
		 * nearby_title.setText(total_points); } else { // if not logged in
		 * display sign up nearby_title.setText(R.string.sign_up); }
		 * 
		 * nearby_title.setText("BE PROACTIVE"); BE PROACTIVE
		 */
		if (this.getRequestmoreDataBool()) {
			this.loadMoreData(this.getFragmentName());
		} else {
			if (this.LoadingMoreMode == LoadingMore.TapSearch) {
				if (dataStorageManager.SearchBarMerchantList.isEmpty()) {
					Toast.makeText(context, R.string.no_search_results,
							Toast.LENGTH_LONG).show();
					//kakao taking tint away
					RelativeLayout dimLayout = (RelativeLayout)rootView.findViewById(R.id.dimView);
					dimLayout.setVisibility(View.INVISIBLE);
					
				} else {
					
					RemoveFooterView();
					HomeListModelAdapter.ClearList();
					HomeListModelAdapter
							.LoadModel(dataStorageManager.SearchBarMerchantList);
					HomeListItemAdapter mAdapter = new HomeListItemAdapter(
							getActivity(), R.layout.home_list_item,
							HomeListModelAdapter.Items);
					mainActivity.listViewToDisplay.setAdapter(mAdapter);
					mainActivity.listViewToDisplay
							.setOnItemClickListener(new OpenMerchantDetailOnItemClickListener(
									dataStorageManager.SearchBarMerchantList));
					mainActivity.listViewToDisplay.requestLayout();
					//kakao taking tint away
					RelativeLayout dimLayout = (RelativeLayout)rootView.findViewById(R.id.dimView);
					dimLayout.setVisibility(View.INVISIBLE);

				}

			} else {

				mainActivity.listViewToDisplay = (ListView) rootView
						.findViewById(R.id.listView1);
				View tempView = this.getInflator().inflate(R.layout.pb_layout,
						null);
				mainActivity.listViewToDisplay.setTag(R.integer.layout,
						tempView); // for
									// access
									// to
									// footerView
				ImageView pullDown = (ImageView) tempView
						.findViewById(R.id.pullDown);
				View tab_search_merchant_button_view = rootView
						.findViewById(R.id.tab_search_merchant_button_view);
				final Button searchButton = (Button) rootView
						.findViewById(R.id.search_merchant_button);
				final LayoutInflater inflaterToPassIn = this.getInflator();
				searchButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						CustomerPageFragment.this.setSearchButton(searchButton,
								inflaterToPassIn);
						setUpListSearchBar(rootView, R.id.inputSearchBar);
						//kakao adding tint
						RelativeLayout dimLayout = (RelativeLayout)rootView.findViewById(R.id.dimView);
						dimLayout.setVisibility(View.VISIBLE);
						//kakao removing tint if tapping it
						if (isVisible() == true){
							dimLayout.setOnClickListener(new View.OnClickListener(){
								public void onClick(View v){
									RelativeLayout dimLayout = (RelativeLayout)rootView.findViewById(R.id.dimView);
									dimLayout.setVisibility(View.INVISIBLE);
									MainScreenActivity.hideSoftKeyboard(getActivity());
									searchButton.setVisibility(View.VISIBLE);
									revertSearchButton(searchButton); //kakao putting red taphere button back
								}
							});
						}
					}
				});

				mainActivity.listViewToDisplay.addFooterView(tempView);
				this.footerView = tempView;
				String whichScroll = "new";
				View buttonView = rootView
						.findViewById(R.id.tab_search_merchant_button_view);
				mainActivity.listViewToDisplay
						.setOnScrollListener(new ListScrollListener(
								mainActivity.listViewToDisplay, this
										.getInflator(), whichScroll));

				HomeListModelAdapter
						.LoadModel(dataStorageManager.SelectedMerchantList);

				HomeListItemAdapter HomeAdapter = new HomeListItemAdapter(
						getActivity(), R.layout.home_list_item,
						HomeListModelAdapter.Items);

				mainActivity.listViewToDisplay.setAdapter(HomeAdapter);
				mainActivity.listViewToDisplay
						.setOnItemClickListener(new OpenMerchantDetailOnItemClickListener(
								dataStorageManager.SelectedMerchantList));
				mContentView = rootView;
			}
		}

	}

	private void loadMyReviews() {
		/*
		 * if (result == null) { rootView =
		 * inflater.inflate(R.layout.fragment_error, container, false);
		 * showErrorDialog(); return rootView; } Deserialize deserializer = new
		 * Deserialize(); dataStorageManager.SelectedCustomerReviews =
		 * deserializer .getReviewList(result); } catch (InterruptedException
		 * e2) { e2.printStackTrace(); } catch (ExecutionException e2) {
		 * e2.printStackTrace(); } if
		 * (dataStorageManager.SelectedCustomerReviews.isEmpty()) { rootView =
		 * inflater.inflate(R.layout.fragment_no_reviews, container, false); }
		 * else { rootView = inflater.inflate(R.layout.fragment_my_review,
		 * container, false);
		 */
		// KAKAO BANZAI
		ListView lv = (ListView) rootView.findViewById(R.id.customerReviewList);
		TextView noReviews = (TextView) rootView
				.findViewById(android.R.id.empty);
		lv.setEmptyView(noReviews); // inserts noReviews TextView --> empty
									// textView
									// for when user has not made any reviews

		ReviewListModelAdapter
				.LoadModel(dataStorageManager.SelectedCustomerReviews);
		String[] ids01 = new String[ReviewListModelAdapter.Items.size()];
		for (int i = 0; i < ids01.length; i++) {
			ids01[i] = Integer.toString(i + 1);
		}
		ReviewListItemAdapter Adapter01 = new ReviewListItemAdapter(
				getActivity(), R.layout.list_item_review, ids01);
		ListView reviewList01 = (ListView) rootView
				.findViewById(R.id.customerReviewList);
		reviewList01.setAdapter(Adapter01);
	}

	private void loadRedeemPoints() {
		mainActivity.listViewToDisplay = (ListView) rootView
				.findViewById(R.id.redeemableVoucherList);
		View tempView = globalInflater.inflate(R.layout.pb_layout, null);
		mainActivity.listViewToDisplay.addFooterView(tempView);

		fragment_name = "redeemMyPoints";
		// String result =
		// mainActivity.WebApiManagerPageFragment.getVoucherAll();
		if (dataStorageManager.RedeemableVoucherList == null) {
			TextView totalPoints = (TextView) rootView
					.findViewById(R.id.voucher_title);
			totalPoints.setText("You currently have: - QooPoints");
			showErrorDialog();

		}

		TextView totalPoints = (TextView) rootView
				.findViewById(R.id.voucher_title);

		/*
		 * dataStorageManager.RedeemableVouchers = deserializer
		 * .getVoucherList(vouchersResult);
		 */

		if (dataStorageManager.loggedIn) {

			totalPoints.setText(displayTotalPoints());
		} else {
			totalPoints.setText("Sign up to start getting deals!");
		}
		mainActivity.listViewToDisplay = (ListView) rootView
				.findViewById(R.id.redeemableVoucherList);

		setUpRedeemListAdapter(dataStorageManager.RedeemableVoucherList,
				mainActivity.listViewToDisplay);

		mContentView = rootView;
	}

	private void loadError() {

	}

	private void loadMyFavourites() {
		// KAKAO PLAYS A MEAN LEE SIN
		ListView lv = (ListView) rootView.findViewById(R.id.listView1);
		TextView noFavs = (TextView) rootView.findViewById(android.R.id.empty);
		lv.setEmptyView(noFavs); // inserts noFavs --> empty TextView
									// for when user has not favourited anything
		mainActivity.listViewToDisplay = (ListView) rootView
				.findViewById(R.id.listView1);
		f_Adapter = null;
		FavoriteListModelAdapter.LoadModel(dataStorageManager.Favorites);
		favoriteIDS = new ArrayList<String>();
		for (int i = 0; i < FavoriteListModelAdapter.Items.size(); i++) {
			favoriteIDS.add(Integer.toString(i + 1));
		}
		f_Adapter = new FavoriteListItemAdapter(getActivity(),
				R.layout.favorite_list_item, favoriteIDS,
				WebApiManager.getSingletonInstance(), this.dataStorageManager);
		mainActivity.listViewToDisplay.setAdapter(f_Adapter);
		mainActivity.listViewToDisplay
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
					}
				});
	}

	private void loadMyAccount() {

		TextView cardNum = (TextView) rootView.findViewById(R.id.cardNum);
		TextView qooPoints = (TextView) rootView.findViewById(R.id.qooPoints);
		TextView vouchersCount = (TextView) rootView
				.findViewById(R.id.vouchersCount);
		ImageView barcode = (ImageView) rootView.findViewById(R.id.barcode);
		final InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);

		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY)
				.build();
		String baseUri = "https://" + dataStorageManager.getApiUrl()
				+ "/api/Picture/GetCustomerBarCodeLogo/"
				+ dataStorageManager.currentUser.CustomerID;
		String imageUri = baseUri;
		ImageLoader IM = ImageLoader.getInstance();
		IM.displayImage(imageUri, barcode, options);
		String pointCardCodeWithSpacing = dataStorageManager.currentUser.PointCardCode
				.substring(0, 4)
				+ " "
				+ dataStorageManager.currentUser.PointCardCode.substring(4, 8)
				+ " "
				+ dataStorageManager.currentUser.PointCardCode.substring(8, 12)
				+ " "
				+ dataStorageManager.currentUser.PointCardCode
						.substring(12, 16);
		cardNum.setText(pointCardCodeWithSpacing);
		qooPoints.setText("QooPoints: "
				+ Integer.toString(dataStorageManager.currentUser.NetPoints));
		vouchersCount
				.setText("Qty: "
						+ Integer
								.toString(dataStorageManager.currentUser.CustomerVoucher.length));
		mainActivity.setUpDrawer(this.lastBundle, true);
		mainActivity.mDrawerList.setItemChecked(0, true);

		// ryan addition
		TextView youHaveEnough = (TextView) rootView
				.findViewById(R.id.youHaveEnough);
		RelativeLayout enoughQoopoints = (RelativeLayout) rootView
				.findViewById(R.id.enoughQoopoints);

		if (dataStorageManager.currentUser.NetPoints >= 1000) {
			enoughQoopoints.setVisibility(View.VISIBLE);
			youHaveEnough.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mainActivity.getActionBar().setTitle("Redeem Points");
					mainActivity.selectItem(MagicNumberReddemPointsFragment);
				}
			});
		} else {
			enoughQoopoints.setVisibility(View.GONE);
		}
		// setContentView(rootView);
		setEmptyText(R.string.empty);
	}

	public void RefreshData() {

		switch (this.getFragmentName()) {
		case Login:
			break;
		case Nearby:
			break;
		case SignUp:
			break;
		case MyAccount:
			break;
		case MyReviews:
			break;
		case Search:
			break;
		case New:
			break;
		case RedeemPoints:
			ListVoucherListModelAdapter
					.LoadModel(dataStorageManager.RedeemableVoucherList);
			this.listAdapter.notifyDataSetChanged();
			this.mainActivity.listViewToDisplay.requestLayout();
			break;
		case Favourites:
			break;
		case About:
			break;
		case Error:
			break;
		default:
			break;

		}
		this.setContentShown(true);
		this.onPageFinish();

	}

	public void loadRecievedData() {

		switch (this.getFragmentName()) {
		case Login:
			loadLogin();
			break;
		case Nearby:
			loadNearBy();
			break;
		// TAKE OUT FOR CHECK IN
		/*
		 * case CheckIn: rootView = createCheckIn(inflater, container); break;
		 */
		case SignUp:
			loadSignUp();
			break;
		case MyAccount:
			loadMyAccount();
			break;
		case MyReviews:
			this.loadMyReviews();
			break;
		case Search:
			this.loadSearch();
			break;
		case New:
			this.loadNew();
			break;
		case RedeemPoints:
			this.loadRedeemPoints();
			break;
		case Favourites:
			this.loadMyFavourites();
			break;
		case Error:
			this.loadError();
			break;
		case About:
			this.loadError();
			break;
		default:

			break;

		}
		if (!this.FragmentChanged) {
			this.setContentShown(true);
		}

		this.onPageFinish();

	}

	private void RequestData(EnumData.FragmentName FragmentName, String input) {
		try {

			switch (FragmentName) {
			case Login:

				break;
			case SignUp:

				DataStorageManager.getSingletonInstance()
						.setSerializationListType(
								EnumData.ListType.WebSiteHeardWays);
				WebApiManagerPageFragment.getSingletonInstance()
						.getWebHeardSiteWays();

				break;
			case Nearby:
				UpdateLocation();
				if (this.getRequestmoreDataBool()) {
					dataStorageManager.slidingAction = true;
					DataStorageManager.getSingletonInstance()
							.setSerializationListType(
									EnumData.ListType.AppendSelectedMerchants);
					UpdateLocation();
					MerchantListModelAdapter.startIndex += MerchantListModelAdapter.recieveAmount;
					MerchantListModelAdapter.omegaIndex += MerchantListModelAdapter.recieveAmount;
					if (dataStorageManager.currentLocation == null) {
						WebApiManagerPageFragment.getSingletonInstance()
								.getApiMerchant(
										MerchantListModelAdapter.startIndex,
										MerchantListModelAdapter.recieveAmount);
					} else {
						WebApiManagerPageFragment.getSingletonInstance()
								.getApiMerchantSort(
										MerchantListModelAdapter.startIndex,
										MerchantListModelAdapter.recieveAmount,
										dataStorageManager.currentLocation
												.getLatitude(),
										dataStorageManager.currentLocation
												.getLongitude());
					}
				} else {
					if (this.LoadingMoreMode == LoadingMore.TapSearch) {
						DataStorageManager
								.getSingletonInstance()
								.setSerializationListType(
										EnumData.ListType.SearchBarMerchantList);
						WebApiManagerPageFragment.getSingletonInstance()
								.getSearchBar(input);

					} else {
						dataStorageManager.setMaxedMerchants(false);
						DataStorageManager.getSingletonInstance()
								.setSerializationListType(
										EnumData.ListType.SelectedMerchantList);
						if (dataStorageManager.currentLocation == null) {
							WebApiManagerPageFragment
									.getSingletonInstance()
									.getApiMerchant(
											MerchantListModelAdapter.startIndex,
											MerchantListModelAdapter.recieveAmount);
						} else {
							this.setGPSStatus(EnumData.GPSStatus.On);
							WebApiManagerPageFragment
									.getSingletonInstance()
									.getApiMerchantSort(
											MerchantListModelAdapter.startIndex,
											MerchantListModelAdapter.recieveAmount,
											dataStorageManager.currentLocation
													.getLatitude(),
											dataStorageManager.currentLocation
													.getLongitude());
						}
					}

				}

				break;
			// TAKE OUT FOR CHECK IN
			/*
			 * case CheckIn: this.setupCheckIn(this.getActivity()); break;
			 */
			case MyAccount:
				DataStorageManager.getSingletonInstance()
						.setSerializationListType(EnumData.ListType.Customer);

				WebApiManagerPageFragment.getSingletonInstance()
						.getCustomerInfo(
								dataStorageManager.currentUser.CustomerID);
				break;
			case MyReviews:

				DataStorageManager.getSingletonInstance()
						.setSerializationListType(
								EnumData.ListType.CustomerReview);
				String customerID = mainActivity.dataStorageManager.currentUser.CustomerID;

				WebApiManagerPageFragment.getSingletonInstance().getReview(
						customerID);

				break;

			case Search:

				DataStorageManager.getSingletonInstance().setSerializationListType(EnumData.ListType.SearchCategory);
				WebApiManagerPageFragment.getSingletonInstance().getSearchCategories();
				if (this.getRequestmoreDataBool()) {
					DataStorageManager.getSingletonInstance()
							.setSerializationListType(
									EnumData.ListType.SearchBarMerchantList);
					WebApiManagerPageFragment.getSingletonInstance()
							.getSearchBar(input);
				}
				break;
			case New:
				if (this.getRequestmoreDataBool()) {

					dataStorageManager.slidingAction = true;
					DataStorageManager.getSingletonInstance()
							.setSerializationListType(
									EnumData.ListType.AppendSelectedMerchants);
					HomeListModelAdapter.startIndex += HomeListModelAdapter.recieveAmount;
					HomeListModelAdapter.omegaIndex += HomeListModelAdapter.recieveAmount;
					dataStorageManager.slidingAction = true;
					WebApiManagerPageFragment.getSingletonInstance()
							.getApiMerchant(HomeListModelAdapter.startIndex,
									HomeListModelAdapter.recieveAmount);
				} else {
					if (this.LoadingMoreMode == LoadingMore.TapSearch) {
						DataStorageManager
								.getSingletonInstance()
								.setSerializationListType(
										EnumData.ListType.SearchBarMerchantList);
						WebApiManagerPageFragment.getSingletonInstance()
								.getSearchBar(input);

					} else {
						dataStorageManager.setMaxedMerchants(false);
						DataStorageManager.getSingletonInstance()
								.setSerializationListType(
										EnumData.ListType.SelectedMerchantList);

						WebApiManagerPageFragmentObj.getApiMerchant(
								HomeListModelAdapter.startIndex,
								HomeListModelAdapter.recieveAmount);
					}

				}

				break;
			case Favourites:
				DataStorageManager.getSingletonInstance()
						.setSerializationListType(EnumData.ListType.Favorites);
				WebApiManager.getSingletonInstance().getCustomerFavorites(
						dataStorageManager.currentUser.CustomerID);
				break;
			case RedeemPoints:
				DataStorageManager
						.getSingletonInstance()
						.setSerializationListType(EnumData.ListType.VoucherList);
				WebApiManagerPageFragment.getSingletonInstance()
						.getVoucherList();
				if (dataStorageManager.loggedIn) {
					DataStorageManager.getSingletonInstance()
							.setSerializationListType(
									EnumData.ListType.Customer);

					WebApiManagerPageFragment.getSingletonInstance()
							.getCustomerInfo(
									dataStorageManager.currentUser.CustomerID);
				}

				break;
			case Error:

				break;
			default:
				break;
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Helper Methods
	private void setSearchButton(Button button, LayoutInflater inflater) {
		ViewGroup linearParent = (ViewGroup) button.getParent();
		((ViewGroup) linearParent).removeView(button);
		View search_text_view_parent = (View) inflater.inflate(
				R.layout.search_text_field, null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1);
		search_text_view_parent.setLayoutParams(params);
		linearParent.addView(search_text_view_parent);
	}
	
	private void revertSearchButton(Button button){
		View text_field = (View) rootView.findViewById(R.id.inputSearchBar);
		ViewGroup linearParent = (ViewGroup) text_field.getParent();
		linearParent.removeView(text_field);
		linearParent.addView(button);
	} //kakaoootalk


	private void RemoveFooterView() {
		this.mainActivity.listViewToDisplay.removeFooterView(this.footerView);
	}

	private void onPageFinish() {
		mainActivity.getActionBar().setHomeButtonEnabled(true);
		this.mainActivity.setFragmentLoading(false);
		this.isListLoading = false;
	}

	private void setFragmentName(EnumData.FragmentName FragmentName) {
		this.FragmentName = FragmentName;
	}

	private EnumData.FragmentName getFragmentName() {
		return this.FragmentName;
	}

	private void setInflator(LayoutInflater Inflater) {
		this.globalInflater = Inflater;
	}

	private LayoutInflater getInflator() {
		return this.globalInflater;
	}

	private void setInstanceState(Bundle state) {
		this.lastBundle = state;
	}

	private Bundle getInstanceState() {
		return this.lastBundle;
	}

	private void setLoadingMode(EnumData.Mode Mode) {
		this.LoadingMode = Mode;
	}

	private EnumData.Mode getLoadingMode() {
		return this.LoadingMode;
	}

	private void setListClickable(Boolean bool) {
		if (this.mainActivity.listViewToDisplay != null)
			this.mainActivity.listViewToDisplay.setClickable(bool);
	}

	private void setRequestmoreData(Boolean bool) {
		this.requestMoreData = bool;
	}

	private Boolean getRequestmoreDataBool() {
		return this.requestMoreData;
	}

	private Boolean isListViweClickable() {
		return this.mainActivity.listViewToDisplay.isClickable();
	}

	private void setLoadingMoreMode(LoadingMore Mode) {
		this.LoadingMoreMode = Mode;
	}

	private LoadingMore getLoadingMoreMode() {
		return this.LoadingMoreMode;
	}

	private void UpdateLocation() {
		if (MainScreenActivity.mLocationClient != null) {
			if (MainScreenActivity.mLocationClient.isConnected()) {

				Location newLocation = MainScreenActivity.mLocationClient
						.getLastLocation();
				if (newLocation != null)
					dataStorageManager.currentLocation = newLocation;
			}

		}
	}

	private EnumData.GPSStatus GetGPSStatus() {
		return this.GPSStatus;
	}

	private void setGPSStatus(EnumData.GPSStatus status) {
		this.GPSStatus = status;
	}

	public class OpenMerchantDetailOnItemClickListener implements
			OnItemClickListener {
		List<Merchant> mert;

		public OpenMerchantDetailOnItemClickListener(List<Merchant> mert) {
			this.mert = mert;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (CustomerPageFragment.this.isListViweClickable()) {
				if (position != dataStorageManager.SelectedMerchantList.size()
						&& !isListLoading) {
					CustomerPageFragment.this.setListClickable(false);
					if (MerchantListModelAdapter.Items.isEmpty()) {
						showErrorDialog();
						return;
					}
					/*
					 * int MerchantID = 0 ;
					 * if(CustomerPageFragment.this.getFragmentName()
					 * ==EnumData.FragmentName.Nearby) { MerchantDisplayListItem
					 * merchant = MerchantListModelAdapter.Items .get(position);
					 * MerchantID = position; } else { HomeDisplayListItem
					 * merchant = HomeListModelAdapter.Items .get(position);
					 * MerchantID = position; }
					 */

					dataStorageManager.selectedMerchant = this.mert
							.get(position);
					Intent i = new Intent(getActivity(),
							MerchantDetailActivity.class);
					startActivity(i);
				}

			}

		}

	};

	public void AddOverLayToBackGround() {
	}

	public void RemoveOverLayFromBackGround() {
	}

	/*
	 * public enum FragmentName { Login, Home, Search, Category, Nearby,
	 * CheckIn, MyAccount, Map, MyVouchers, QooPointsHistory, MyReviews,
	 * MyFavorites, RedeemPoints, SignUp, Error }
	 */
	// TAKE OUT FOR CHECK IN
	public enum FragmentName {
		Login, Home, Search, Category, Nearby, MyAccount, Map, MyVouchers, QooPointsHistory, MyReviews, Favourites, RedeemPoints, SignUp, Error, About
	}

	public enum LoadingMore {
		TapSearch, LoadMore, None

	}

	// TAKE OUT FOR CHECK IN
}