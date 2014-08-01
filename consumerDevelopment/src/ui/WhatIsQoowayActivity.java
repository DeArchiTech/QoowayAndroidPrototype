package ui;

import com.qooway.consumerv01.R;
import framework.QoowayActivity;
import android.os.Bundle;

public class WhatIsQoowayActivity  extends QoowayActivity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	 	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_qooway);
        getActionBar().setTitle("About");
	}
}
