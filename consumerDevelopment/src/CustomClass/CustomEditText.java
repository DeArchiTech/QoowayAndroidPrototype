package CustomClass;

import ui.MainScreenActivity;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

public class CustomEditText extends EditText {

	
	private Context context;
	
	public CustomEditText(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	
	public CustomEditText(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        this.context = context;
	}
	

	public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	        this.context = context;
	}
	    
	 @Override
	    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
	        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
	        	MainScreenActivity.hideSoftKeyboard((Activity)context);	
	        	return true; 

	          }
	       
	        return super.dispatchKeyEvent(event);
	    }
}
