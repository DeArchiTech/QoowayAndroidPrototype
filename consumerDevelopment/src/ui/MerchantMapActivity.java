package ui;

import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.MapFragment;
import com.qooway.consumerv01.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import framework.QoowayActivity;
import data.DataStorageManager;

public class MerchantMapActivity extends QoowayActivity {
	private GoogleMap map;
	public static Location mCurrentLocation;
	public static LocationClient mLocationClient;
	public String httpserverUrl = "online.profitek.com/appdevelopment";
	public String httpsserverUrl = "online.profitek.com/appdevelopment";
	
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
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {    	 	
    	super.onCreate(savedInstanceState);
	    setContentView(R.layout.fragment_map);
	    
		map = ((MapFragment) getFragmentManager()
				.findFragmentById(R.id.map)).getMap();
	    //kakakaooooooo deleted "map" from title
		getActionBar().setTitle(DataStorageManager.getSingletonInstance().selectedMerchant.Name);
		
	    //mLocationClient = new LocationClient(this, this, this);
	    //System.out.println(mLocationClient.getLastLocation());
	    mCurrentLocation = DataStorageManager.getSingletonInstance().currentLocation;
	    
	    Double MerchantLongitude = Double
				.parseDouble(DataStorageManager.getSingletonInstance().selectedMerchant.Longitude);
	    Double MerchantLatitude = Double
	    		.parseDouble((String) DataStorageManager.getSingletonInstance().selectedMerchant.Latitude);
	    Double ClientLongitude = mCurrentLocation
	    		.getLongitude();
	    Double ClientLatitude = mCurrentLocation.getLatitude();
	    String MerchantName = DataStorageManager.getSingletonInstance().selectedMerchant.Name;
	
	    //mainActivity.mCurrentLocation = mainActivity.mLocationClient
	    //		.getLastLocation();
	
	    map.addMarker(
		new MarkerOptions().position(
				new LatLng(MerchantLatitude, MerchantLongitude))
				.title(MerchantName)).showInfoWindow();
	    
	    
	    map.addMarker(
		new MarkerOptions().position(
				new LatLng(ClientLatitude, ClientLongitude)).title(
				"You are here")).showInfoWindow();
	    
		LatLng southwest = new LatLng(Math.min(MerchantLatitude,
				ClientLatitude), Math.min(MerchantLongitude,
				ClientLongitude));
		LatLng northeast = new LatLng(Math.max(MerchantLatitude,
				ClientLatitude), Math.max(MerchantLongitude,
				ClientLongitude));
		map.moveCamera(CameraUpdateFactory.newLatLngBounds(
				new LatLngBounds(southwest, northeast), 500, 500, 0));
		
		/*
		webApiManager.setJson(false);
		String result = "";
		try {
			result =webApiManager.getGoogleMapRoute(
					Double.toString(ClientLatitude),
					Double.toString(ClientLongitude),
					Double.toString(MerchantLatitude),
					Double.toString(MerchantLongitude));
			//Testing
			//System.out.println(result);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		webApiManager.setJson(true);
		InputStream is = new ByteArrayInputStream(result.getBytes());
		
		//Testing
		//System.out.println(is);
		RouteHelper helper = new RouteHelper();
		Document document = helper.getDocument(is);
		//Testing
		//System.out.println(document);
		ArrayList<LatLng> directionPoint = helper.getDirection(document);
		PolylineOptions rectLine = new PolylineOptions().width(10).color(
				Color.RED);
		
		for (int i = 0; i < directionPoint.size(); i++) {
			rectLine.add(directionPoint.get(i));
		}
		// Adding route on the map
		map.addPolyline(rectLine);	    
	    */
		
    }
    
    /*
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);    
	    WebApiManager webApiManager = new WebApiManager(this, httpserverUrl, httpsserverUrl);
	    
	    mLocationClient = new LocationClient(this, this, this);
	
	    Double MerchantLongitude = Double
				.parseDouble(DataStorageManager.getSingletonInstance().selectedMerchant.Longitude);
	    Double MerchantLatitude = Double
	    		.parseDouble((String) DataStorageManager.getSingletonInstance().selectedMerchant.Latitude);
	    Double ClientLongitude = mCurrentLocation
	    		.getLongitude();
	    Double ClientLatitude = mCurrentLocation.getLatitude();
	    String MerchantName = DataStorageManager.getSingletonInstance().selectedMerchant.Name;
	
	    //mainActivity.mCurrentLocation = mainActivity.mLocationClient
	    //		.getLastLocation();
	
	    map.addMarker(
		new MarkerOptions().position(
				new LatLng(MerchantLatitude, MerchantLongitude))
				.title(MerchantName)).showInfoWindow();
	    map.addMarker(
		new MarkerOptions().position(
				new LatLng(ClientLatitude, ClientLongitude)).title(
				"You are here")).showInfoWindow();
	
		LatLng southwest = new LatLng(Math.min(MerchantLatitude,
				ClientLatitude), Math.min(MerchantLongitude,
				ClientLongitude));
		LatLng northeast = new LatLng(Math.max(MerchantLatitude,
				ClientLatitude), Math.max(MerchantLongitude,
				ClientLongitude));
		map.moveCamera(CameraUpdateFactory.newLatLngBounds(
				new LatLngBounds(southwest, northeast), 500, 500, 0));
		webApiManager.setJson(false);
		String result = "";
		try {
			result =webApiManager.getGoogleMapRoute(
					Double.toString(ClientLatitude),
					Double.toString(ClientLongitude),
					Double.toString(MerchantLatitude),
					Double.toString(MerchantLongitude));
			//Testing
			//System.out.println(result);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		webApiManager.setJson(true);
		InputStream is = new ByteArrayInputStream(result.getBytes());
		
		//Testing
		//System.out.println(is);
		RouteHelper helper = new RouteHelper();
		Document document = helper.getDocument(is);
		//Testing
		//System.out.println(document);
		ArrayList<LatLng> directionPoint = helper.getDirection(document);
		PolylineOptions rectLine = new PolylineOptions().width(10).color(
				Color.RED);
		
		for (int i = 0; i < directionPoint.size(); i++) {
			rectLine.add(directionPoint.get(i));
		}
		// Adding route on the map
		map.addPolyline(rectLine);
	}
    */
	/*
    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
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
	*/
}
