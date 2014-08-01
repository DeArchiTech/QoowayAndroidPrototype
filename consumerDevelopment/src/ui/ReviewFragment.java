package ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import ui.favorite.FavoriteListItemAdapter;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devspark.progressfragment.ProgressFragment;
import com.qooway.consumerv01.R;

import data.DataStorageManager;
import data.Deserialize;
import data.EnumData;
import data.WebApiManager;
import data.WebApiManagerPageFragment;
import framework.QoowayActivity;
import framework.DataObject.Reply;
import framework.DataObject.Review;

@SuppressLint("ValidFragment")
public class ReviewFragment extends ProgressFragment {
	public static final String ARG_PAGE_NUMBER = "PAGE_number";

	private View rootView;
	private LayoutInflater globalInflater;
	public static FavoriteListItemAdapter f_Adapter; // needed for favorites
	public static ArrayList<String> favoriteIDS; // Ryan short cut to resolve
													// problem, need to fix for
													// efficiency and coding
													// standardsss
	public Context context;
	public ProgressDialog progressDialog = null;
	private Handler mHandler;
	private int half_star = R.drawable.reviews_halfstar;
	private int yellow_star = R.drawable.reviews_yellow_star;

	private View mContentView;
	private QoowayActivity Activity;
	private Runnable mShowContentRunnable = new Runnable() {
		@Override
		public void run() {
			if (DataStorageManager.getSingletonInstance().getAsyncTaskCount() == 0) {
				ReviewFragment.this.loadReviewView();
			} else {
				mHandler.postDelayed(mShowContentRunnable, 300);
			}


		}
	};

	/*
	 * mainActivity.getActionBar().setHomeButtonEnabled(true); if
	 * (DataStorageManager.getSingletonInstance().getAsyncTaskCount() == 0) {
	 * if(CustomerPageFragment.this.getLoadingMode() == EnumData.Mode.Start)
	 * CustomerPageFragment.this.loadRecievedData(); else
	 * if(CustomerPageFragment.this.getLoadingMode() == EnumData.Mode.Refresh)
	 * CustomerPageFragment.this.RefreshData(); } else {
	 * mHandler.postDelayed(mShowContentRunnable, 300); }
	 */
	/*
	 * private View mContentView; private Handler mHandler; private Runnable
	 * mShowContentRunnable = new Runnable() {
	 * 
	 * @Override public void run() { // setContentEmpty(true); //
	 * setContentShown(true); } };
	 */

	public static final ReviewFragment newInstance(QoowayActivity Activity)
	{
		ReviewFragment f = new ReviewFragment();
		f.Activity = Activity;
		return f;
	}


	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, Bundle savedInstanceState) {
		this.setInflator(inflater);
		rootView = inflater.inflate(R.layout.fragment_empty, container, false);
		mContentView = rootView;
		setHasOptionsMenu(true);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setContentView(mContentView);
		setEmptyText(R.string.empty);
		obtainData();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mHandler.removeCallbacks(mShowContentRunnable);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	//	inflater.inflate(R.menu.menu_merchant, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	private void obtainData() {
		// Show indeterminate progress
		setContentShown(false);
		this.RequestReview();
		this.RequestReply();
		mHandler = new Handler();
		mHandler.postDelayed(mShowContentRunnable, 3000);
	}

	private void loadReviewView() {

		DataStorageManager dataStorageManager = DataStorageManager
				.getSingletonInstance();
		
		if (!dataStorageManager.SelectedMerchantReviews.isEmpty()) {
	/*		View noReviewView = mContentView.findViewById(R.id.cardNum);

			noReviewView.setVisibility(View.GONE);*/
			View noReviewView = mContentView.findViewById(R.id.noReviewsYet);
			noReviewView.setVisibility(View.GONE);
			LayoutInflater layoutInflater = (LayoutInflater) this.Activity
					.getBaseContext().getSystemService(
							Context.LAYOUT_INFLATER_SERVICE);

			LinearLayout container= (LinearLayout) mContentView.findViewById(R.id.container);
			// Add reviews
			for (Review item : dataStorageManager.SelectedMerchantReviews) {
				final View addView = layoutInflater.inflate(
						R.layout.list_item_review_reply, null);

				TextView ReviewDate = (TextView) addView
						.findViewById(R.id.ReviewDate);
				TextView ReviewerName = (TextView) addView
						.findViewById(R.id.ReviewerName);
				TextView ReviewText = (TextView) addView
						.findViewById(R.id.ReviewText);
				TextView RestaurantName = (TextView) addView
						.findViewById(R.id.RestaurantName);

				String formattedTime = parseDate(item.DateCreated);
				String displayName = "Anonymous";
				if(item.Anonymous==0)
				{
					String[] names = item.NickName.split("\\s+");
					if(names.length>1)
					{
						displayName = names[0] + " " + names[1].substring(0, 1) + ".";
					}
				}

				ReviewDate.setText(formattedTime);
				// BE PROACTIVE
				// String temp = "ALIBABA";
				// RestaurantName.setText(temp);
				RestaurantName.setText(item.MerchantName);
				ReviewerName.setText("Reviewed by: " + displayName);
				ReviewText.setText(item.Remark);
				if (item.Item1Score == 0) {
					TextView textView4 = (TextView) addView
							.findViewById(R.id.textView4);
					textView4.setVisibility(View.GONE);
					LinearLayout foodLinearLayout = (LinearLayout) addView
							.findViewById(R.id.foodLinearLayout);
					foodLinearLayout.setVisibility(View.GONE);
				} else {
					setUpFood(addView, item);
				}
				setUpService(addView, item);
				setUpAmbience(addView, item);
				setUpMainStar(addView, item);

				addView.findViewById(R.id.ReviewAddDetail).setVisibility(
						View.GONE);

				addView.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						View reviewAddDetail = v
								.findViewById(R.id.ReviewAddDetail);
						if (!reviewAddDetail.isShown()) {
							reviewAddDetail.setVisibility(View.VISIBLE);
							slide_down(ReviewFragment.this.Activity
									.getBaseContext(), reviewAddDetail);
						} else {
							slide_up(ReviewFragment.this.Activity
									.getBaseContext(), reviewAddDetail);
							reviewAddDetail.setVisibility(View.GONE);
						}

					}
				});

				if (dataStorageManager.SelectedMerchantReplies.isEmpty()) {
					addView.findViewById(R.id.reply).setVisibility(View.GONE);
				} else if (checkReply(dataStorageManager.SelectedMerchantReplies, item)) {
					Reply currentReply = getReply(
							dataStorageManager.SelectedMerchantReplies, item);
					TextView replyDate = (TextView) addView
							.findViewById(R.id.replyDate);
					TextView replyName = (TextView) addView
							.findViewById(R.id.ReplyName);
					TextView replyText = (TextView) addView
							.findViewById(R.id.ReplyText);

					String replyTime = currentReply.DateCreated;
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd'T'HH:mm:ss", Locale.CANADA);
					Date replyFormatDate = new Date();
					
						try {
							replyFormatDate = sdf.parse(replyTime);
						} catch (java.text.ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				
					replyDate.setText(parseDate(replyFormatDate));
					replyName.setText(dataStorageManager.selectedMerchant.Name);
					replyText.setText(currentReply.Remark);

				} else {
					addView.findViewById(R.id.reply).setVisibility(View.GONE);
				}

				container.addView(addView);
			}
			
		}
		this.onFragmentFinish();
	}
	
	private void RequestData() {
		this.RequestReview();
	}
	
	private void RequestReview()
	{
		DataStorageManager dataStorageManager = DataStorageManager
				.getSingletonInstance();
		DataStorageManager.getSingletonInstance().setSerializationListType(EnumData.ListType.SelectedMerchantReviews);
		try {
			WebApiManagerPageFragment.getSingletonInstance().getReview(dataStorageManager.selectedMerchant.StoreID);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void RequestReply()
	{
		DataStorageManager dataStorageManager = DataStorageManager
				.getSingletonInstance();
		String storeID = Integer.toString(dataStorageManager.selectedMerchant.StoreID);
		DataStorageManager.getSingletonInstance().setSerializationListType(EnumData.ListType.SelectedMerchantReplies);
		try {
			WebApiManagerPageFragment.getSingletonInstance().getReplies(Integer.parseInt(storeID));
		} catch (NumberFormatException e) {
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
	public String parseDate(Date oldDate) {
		SimpleDateFormat tmp = new SimpleDateFormat("d", Locale.CANADA);
		String date_t = tmp.format(oldDate);

		if (date_t.endsWith("1") && !date_t.endsWith("11"))
			tmp = new SimpleDateFormat("MMM d'st', yyyy - h:mm a",
					Locale.CANADA);
		else if (date_t.endsWith("2") && !date_t.endsWith("12"))
			tmp = new SimpleDateFormat("MMM d'nd', yyyy - h:mm a",
					Locale.CANADA);
		else if (date_t.endsWith("3") && !date_t.endsWith("13"))
			tmp = new SimpleDateFormat("MMM d'rd', yyyy - h:mm a",
					Locale.CANADA);
		else
			tmp = new SimpleDateFormat("MMM d'th', yyyy - h:mm a",
					Locale.CANADA);

		String formattedTime = tmp.format(oldDate); // changes temp the old date
													// with newest tmp

		return formattedTime;
	}

	public static void slide_down(Context ctx, View v) {
		Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);
		if (a != null) {
			a.reset();
			if (v != null) {
				v.clearAnimation();
				v.startAnimation(a);
			}
		}
	}

	public static void slide_up(Context ctx, View v) {
		Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);
		if (a != null) {
			a.reset();
			if (v != null) {
				v.clearAnimation();
				v.startAnimation(a);
			}
		}
	}

	private void setUpFood(View rowView, Review item) {
		ImageView firstStar = (ImageView) rowView.findViewById(R.id.imageView1);
		ImageView secondStar = (ImageView) rowView
				.findViewById(R.id.imageView2);
		ImageView thirdStar = (ImageView) rowView.findViewById(R.id.imageView3);
		ImageView fourthStar = (ImageView) rowView
				.findViewById(R.id.imageView4);
		ImageView fifthStar = (ImageView) rowView.findViewById(R.id.imageView5);
		setupStars(firstStar, secondStar, thirdStar, fourthStar, fifthStar,
				item.Item1Score);
	}

	private void setUpService(View rowView, Review item) {
		ImageView firstStar = (ImageView) rowView.findViewById(R.id.imageView6);
		ImageView secondStar = (ImageView) rowView
				.findViewById(R.id.imageView7);
		ImageView thirdStar = (ImageView) rowView.findViewById(R.id.imageView8);
		ImageView fourthStar = (ImageView) rowView
				.findViewById(R.id.imageView9);
		ImageView fifthStar = (ImageView) rowView
				.findViewById(R.id.imageView10);
		setupStars(firstStar, secondStar, thirdStar, fourthStar, fifthStar,
				item.Item2Score);
	}

	private void setUpAmbience(View rowView, Review item) {

		ImageView firstStar = (ImageView) rowView
				.findViewById(R.id.imageView11);
		ImageView secondStar = (ImageView) rowView
				.findViewById(R.id.imageView12);
		ImageView thirdStar = (ImageView) rowView
				.findViewById(R.id.imageView13);
		ImageView fourthStar = (ImageView) rowView
				.findViewById(R.id.imageView14);
		ImageView fifthStar = (ImageView) rowView
				.findViewById(R.id.imageView15);
		setupStars(firstStar, secondStar, thirdStar, fourthStar, fifthStar,
				item.Item3Score);
	}

	private void setUpMainStar(View rowView, Review item) {
		// Takes the average of the 3 ratings.
		ImageView firstStar = (ImageView) rowView
				.findViewById(R.id.imageView16);
		ImageView secondStar = (ImageView) rowView
				.findViewById(R.id.imageView17);
		ImageView thirdStar = (ImageView) rowView
				.findViewById(R.id.imageView18);
		ImageView fourthStar = (ImageView) rowView
				.findViewById(R.id.imageView19);
		ImageView fifthStar = (ImageView) rowView
				.findViewById(R.id.imageView20);
		setupStars(firstStar, secondStar, thirdStar, fourthStar, fifthStar,
				item.Score);
	}

	private void setupStars(ImageView first, ImageView second, ImageView third,
			ImageView fourth, ImageView fifth, int rating) {
		switch (rating) {
		case 0:
			break;
		case 1:
			first.setImageResource(half_star);
			break;
		case 2:
			first.setImageResource(yellow_star);

			break;
		case 3:
			first.setImageResource(yellow_star);
			second.setImageResource(half_star);
			break;
		case 4:
			first.setImageResource(yellow_star);
			second.setImageResource(yellow_star);
			break;
		case 5:
			first.setImageResource(yellow_star);
			second.setImageResource(yellow_star);
			third.setImageResource(half_star);
		case 6:
			first.setImageResource(yellow_star);
			second.setImageResource(yellow_star);
			third.setImageResource(yellow_star);
			break;
		case 7:
			first.setImageResource(yellow_star);
			second.setImageResource(yellow_star);
			third.setImageResource(yellow_star);
			fourth.setImageResource(half_star);
			break;
		case 8:
			first.setImageResource(yellow_star);
			second.setImageResource(yellow_star);
			third.setImageResource(yellow_star);
			fourth.setImageResource(yellow_star);
			break;
		case 9:

			first.setImageResource(yellow_star);
			second.setImageResource(yellow_star);
			third.setImageResource(yellow_star);
			fourth.setImageResource(yellow_star);
			fifth.setImageResource(half_star);
			break;
		case 10:

			first.setImageResource(yellow_star);
			second.setImageResource(yellow_star);
			third.setImageResource(yellow_star);
			fourth.setImageResource(yellow_star);
			fifth.setImageResource(yellow_star);
			break;
		default:
			break;
		}

	}

	private boolean checkReply(List<Reply> list, Review review) {
		for (Reply reply : list) {
			if (reply.ReviewID == review.ReviewID) {
				return true;
			}
		}
		return false;
	}

	private Reply getReply(List<Reply> list, Review review) {
		for (Reply reply : list) {
			if (reply.ReviewID == review.ReviewID) {
				return reply;
			}
		}
		return new Reply();
	}
	
	private void onFragmentFinish()
	{
		this.setContentShown(true);
	}

	private void setInflator(LayoutInflater Inflater) {
		this.globalInflater = Inflater;
	}
	

	private LayoutInflater getInflator() {
		return this.globalInflater;
	}
}