package ui;

import com.qooway.consumerv01.R;

import framework.QoowayActivity;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class HowDoIGetInstantDealsActivity   extends QoowayActivity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	 	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_instant_deals);
        getActionBar().setTitle("About");
	}
}
