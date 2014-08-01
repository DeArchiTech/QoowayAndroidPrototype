package ui;

import java.util.concurrent.ExecutionException;
import com.qooway.consumerv01.R;
import data.Serialize;
import data.WebApiManager;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPasswordActivity extends Activity {
	WebApiManager webApiManager = WebApiManager.getSingletonInstance();
 
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {       
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
        setContentView(R.layout.fragment_forgot);
        getActionBar().setTitle("Forgot Password");
        findViewById(R.id.passwordSubmitButton).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
		    	EditText mEdit = (EditText)findViewById(R.id.editText);
		    	String email = mEdit.getText().toString();
		    	if(isEmailValid(email)){
			    	insertPassword(email);
			    	finish();
		    	} else {
					Toast toast = Toast.makeText(getApplicationContext(), "Please enter a valid email address.", Toast.LENGTH_SHORT);
					toast.show();
		    	}
		    	
			}
        });
    }
	
	private boolean isEmailValid(CharSequence email) {
		   return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}
	
	private void insertPassword(String email){
		try{
			String json = Serialize.password(email); 
			webApiManager.postApiPasswordReset(json);
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}	
	
}
