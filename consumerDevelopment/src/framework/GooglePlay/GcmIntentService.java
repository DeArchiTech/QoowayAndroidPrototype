package framework.GooglePlay;

import java.util.concurrent.ExecutionException;

import ui.MainScreenActivity;

import com.qooway.consumerv01.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import data.Deserialize;
import data.WebApiManager;
import framework.DataObject.Merchant;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
public class GcmIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	WebApiManager webApiManager;
	
	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());

			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification("Deleted messages on server: "
						+ extras.toString());
				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {

				String Status = (String) extras.get("Status");
				String SenderID = (String) extras.get("SenderID");
				@SuppressWarnings("unused")
				String SenderName = (String) extras.get("SenderName");
				
				webApiManager = WebApiManager.getSingletonInstance();
				
				String result = "";
				try {
					result = webApiManager.getMerchantInfo(SenderID);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String merchant = "A Merchant";
				if (result != null || result != "") {
					Deserialize deserializer = new Deserialize();
					Merchant merchantSent = deserializer.getMerchant(result);
					merchant = merchantSent.Name;
				}
				
				String message = " has declined your check in";
				if(Status.equalsIgnoreCase("Confirm")){
					message = " has confirmed your check in";
				} 
				
				// Post notification of received message.
				sendNotification(merchant + message);
			}
		}

		LocalBroadcastManager.getInstance(this).sendBroadcast(intent );
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(String msg) {
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		
		Intent notificationIntent = new Intent(getBaseContext(), MainScreenActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(getBaseContext(), 0, notificationIntent, 0);
 
        
        
		/*PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, MainScreenActivity.class), 0);*/
        Bitmap icon =((BitmapDrawable) getResources().getDrawable(R.drawable.beta)).getBitmap();
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setLargeIcon(icon)
				.setSmallIcon(R.drawable.android_noti_icon)
				.setContentTitle("Qooway Merchant confirmation")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg)
				.setAutoCancel(true);

		mBuilder.setContentIntent(intent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
	
	
	//for setting the BitMapFor Icon
	private Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);
	}
	
	//Helper method for calculating the icon size
	private int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

        final int halfHeight = height / 2;
        final int halfWidth = width / 2;

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while ((halfHeight / inSampleSize) > reqHeight
                && (halfWidth / inSampleSize) > reqWidth) {
            inSampleSize *= 2;
        }
    }

    return inSampleSize;
}
}