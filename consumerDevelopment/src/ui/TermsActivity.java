package ui;


import com.qooway.consumerv01.R;

import framework.QoowayActivity;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class TermsActivity extends QoowayActivity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	 	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_term);
        
        getActionBar().setTitle("Terms of Use");
        
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("https://www.qooway.com/TermsOfUse.aspx");
        
	}
}
