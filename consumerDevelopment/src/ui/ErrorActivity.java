package ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.qooway.consumerv01.R;

public class ErrorActivity extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	 	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_error);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setHomeButtonEnabled(false);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Error")
			   .setMessage("Please check your Internet connection")
		       .setCancelable(false)
		       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                //do things
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();		
	}
	
}
