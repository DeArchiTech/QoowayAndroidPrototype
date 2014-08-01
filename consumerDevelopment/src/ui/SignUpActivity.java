package ui;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ui.home.HomeListModelAdapter;
import ui.merchantList.MerchantListModelAdapter;
import ui.newSearch.SearchListActivity.SearchActivityOnClickListener;

import com.qooway.consumerv01.R;

import data.DataStorageManager;
import data.Deserialize;
import data.EnumData;
import data.Serialize;
import data.WebApiManager;
import data.WebApiManagerPageFragment;
import framework.QoowayActivity;
import framework.DataObject.Customer;
import framework.DataObject.Merchant;
import framework.DataObject.SignUp;
import framework.DataObject.WebHeardSiteWay;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends QoowayActivity {
	private EnumData.ActionType actionType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String value = extras.getString("ActionType");
			if (value.equalsIgnoreCase("SignUp"))
				this.actionType = EnumData.ActionType.SignUp;
			else if (value.equalsIgnoreCase("Activate"))
				this.actionType = EnumData.ActionType.Activate;
		}
		switch (this.actionType) {
		case SignUp:
			setContentView(R.layout.activity_signup);
			getActionBar().setTitle("Sign Up");
			final CheckBox mailCheckBox = (CheckBox) findViewById(R.id.mailCheckBox);
			mailCheckBox
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						View viewParent = (View) mailCheckBox.getParent()
								.getParent();

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							View additionalText = viewParent
									.findViewById(R.id.signUpAdditionalText);
							final ScrollView scroll = (ScrollView) viewParent
									.findViewById(R.id.signupScroll);
							if (!additionalText.isShown()) {
								additionalText.setVisibility(View.VISIBLE);
								scroll.post(new Runnable() {
									@Override
									public void run() {
										scroll.fullScroll(View.FOCUS_DOWN);
									}
								});
							} else {
								additionalText.setVisibility(View.GONE);
							}
						}
					});

			findViewById(R.id.signUpAdditionalText).setVisibility(View.GONE);
			Spinner provinceSpinner = (Spinner) findViewById(R.id.province_spinner);
			ArrayAdapter<CharSequence> adapter = adapter = ArrayAdapter
					.createFromResource(SignUpActivity.this,
							R.array.province_array,
							android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			provinceSpinner.setAdapter(adapter);

			Spinner countrySpinner = (Spinner) findViewById(R.id.country_spinner);
			adapter = ArrayAdapter
					.createFromResource(SignUpActivity.this,
							R.array.country_array,
							android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			countrySpinner.setAdapter(adapter);
			break;
		case Activate:
			setContentView(R.layout.activitiy_activation);
			getActionBar().setTitle("Activate");
		default:
			break;
		}
		setUpSignUpForm();
		
	}

	private void setUpSignUpForm() {
		View sign_up_form = (View) findViewById(R.id.sign_up_form);
		sign_up_form.findViewById(R.id.joinText4).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(SignUpActivity.this,
								TermsActivity.class);
						startActivity(i);
					}
				});
		EditText password = (EditText) sign_up_form
				.findViewById(R.id.passwordText);
		password.setTypeface(Typeface.DEFAULT);
		password.setTransformationMethod(new PasswordTransformationMethod());
		EditText confirmPassword = (EditText) sign_up_form
				.findViewById(R.id.confirmPasswordText);
		confirmPassword.setTypeface(Typeface.DEFAULT);
		confirmPassword
				.setTransformationMethod(new PasswordTransformationMethod());

		Spinner genderSpinner = (Spinner) sign_up_form
				.findViewById(R.id.gender_spinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				SignUpActivity.this, R.array.gender_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		genderSpinner.setAdapter(adapter);

		Spinner ageSpinner = (Spinner) sign_up_form
				.findViewById(R.id.age_spinner);
		adapter = ArrayAdapter.createFromResource(SignUpActivity.this,
				R.array.age_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ageSpinner.setAdapter(adapter);

		Spinner referenceSpinner = (Spinner) sign_up_form
				.findViewById(R.id.reference_spinner);
		List<String> references = getReferences();
		adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getReferences()
	              );

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		referenceSpinner.setAdapter(adapter);
	}
	
	//kakao dialog box reminding user of information deletion
	@Override
	public void onBackPressed(){
		new AlertDialog.Builder(new ContextThemeWrapper(this,android.R.style.Theme_Holo_Light))
		   .setMessage("If you leave this page, information entered will be lost.")
		   .setTitle("Are you sure?")
		   .setCancelable(true)
		   .setPositiveButton("Leave page", new DialogInterface.OnClickListener() {			
			   @Override
			   public void onClick(DialogInterface dialog, int etc) {
				   SignUpActivity.super.onBackPressed();
			   }
		   })
		   .setNegativeButton("Cancel", null)
		   .create().show();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		menu.findItem(R.id.join_button).setVisible(true);
		menu.findItem(R.id.information).setVisible(false);
		menu.findItem(R.id.logout_button).setVisible(false);
		menu.findItem(R.id.done_button).setVisible(false);
		return super.onPrepareOptionsMenu(menu);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int itemId = item.getItemId();
		if (itemId == R.id.join_button) {
			setupSignUpProcess();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	private void setupSignUpProcess() {

		EditText emailTextBox = (EditText) findViewById(R.id.emailText);
		String emailText = emailTextBox.getText().toString();

		EditText passwordTextBox = (EditText) findViewById(R.id.passwordText);
		String passwordText = passwordTextBox.getText().toString();

		EditText confirmPasswordTextBox = (EditText) findViewById(R.id.confirmPasswordText);
		String confirmPasswordText = confirmPasswordTextBox.getText()
				.toString();

		EditText firstNameTextBox = (EditText) findViewById(R.id.firstNameText);
		String firstNameText = firstNameTextBox.getText().toString();

		EditText lastNameTextBox = (EditText) findViewById(R.id.lastNameText);
		String lastNameText = lastNameTextBox.getText().toString();

		Spinner genderSpinner = (Spinner) findViewById(R.id.gender_spinner);
		String genderChoice = genderSpinner.getSelectedItem().toString();

		Spinner ageSpinner = (Spinner) findViewById(R.id.age_spinner);
		String ageChoice = ageSpinner.getSelectedItem().toString();
		int agePosition = ageSpinner.getSelectedItemPosition();

		CheckBox emailCheck = (CheckBox) findViewById(R.id.emailCheckBox);

		Spinner referenceSpinner = (Spinner) findViewById(R.id.reference_spinner);
		String referenceChoice = referenceSpinner.getSelectedItem().toString();
		int referencePosition = referenceSpinner.getSelectedItemPosition();
		String PointCardCode = "";
		if (this.actionType == EnumData.ActionType.Activate) {
			EditText cardCodeText = (EditText) findViewById(R.id.sixteen_digit_card);
			PointCardCode = cardCodeText.getText().toString();
		}
		View AdditionalText = (View) findViewById(R.id.signUpAdditionalText);
		Boolean SignUp = this.actionType == EnumData.ActionType.SignUp;
		Boolean AddtionalTextShown = AdditionalText != null
				&& AdditionalText.isShown();
		if (SignUp && AddtionalTextShown) {
			EditText addressTextBox = (EditText) findViewById(R.id.addressText);
			String addressText = addressTextBox.getText().toString();

			EditText cityTextBox = (EditText) findViewById(R.id.cityText);
			String cityText = cityTextBox.getText().toString();

			EditText postalTextBox = (EditText) findViewById(R.id.postalText);
			String postalText = postalTextBox.getText().toString();

			Spinner provinceSpinner = (Spinner) findViewById(R.id.province_spinner);
			String provinceChoice = provinceSpinner.getSelectedItem()
					.toString();

			if (signUpValidCheck(emailText, passwordText, confirmPasswordText,
					firstNameText, lastNameText, genderChoice, ageChoice,
					referenceChoice, addressText, cityText, postalText)) {
				String genderInt = genderChoice.equalsIgnoreCase("Female") ? "F"
						: "M";
				try {
					SignUp tran = new SignUp(agePosition, firstNameText,
							lastNameText, genderInt, referencePosition,
							emailText, passwordText, confirmPasswordText,
							emailCheck.isChecked(), true, 1, false,
							addressText, cityText, postalText, provinceChoice,
							PointCardCode);
					String result = WebApiManager.getSingletonInstance().postApiRegister(
							Serialize.signUp(tran));
					String Message = "";
					if(result.equals("OK"))
					{
						Message ="Thanks for signing up! Please check your email to verify your account.";
						this.promptDialog("Success", Message, "Close");
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// changeFragment(0);
			} else {

			}

		} else {
			if (signUpValidCheck(emailText, passwordText, confirmPasswordText,
					firstNameText, lastNameText, genderChoice, ageChoice,
					referenceChoice, "Gender", "Age Group", "Reference")) {
				String genderInt = genderChoice.equalsIgnoreCase("Female") ? "F"
						: "M";
				try {
					SignUp tran = new SignUp(agePosition, firstNameText,
							lastNameText, genderInt, referencePosition,
							emailText, passwordText, confirmPasswordText,
							emailCheck.isChecked(), true, 1, false, "", "", "",
							"", PointCardCode);
					String result = WebApiManager.getSingletonInstance().postApiRegister(
							Serialize.signUp(tran));
					String Message = "";
					if(result!= null && result.equals("OK"))
					{
						Message ="Thanks for signing up! Please check your email to verify your account.";
						this.promptDialog("Success", Message, "Close");
					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// changeFragment(0);
			} else {

			}

		}
	}

	private boolean signUpValidCheck(String email, String password,
			String confirmPassword, String firstName, String lastName,
			String gender, String age, String reference, String address,
			String city, String postal) {
		if (!isEmailValid(email)) {
			Toast toast = Toast.makeText(getBaseContext(),
					"Please enter a valid email address.", Toast.LENGTH_SHORT);
			toast.show();
			return false;
		} else if (!password.equals(confirmPassword)) {
			Toast toast = Toast.makeText(getBaseContext(),
					"Password and Confirm Password must be the same.",
					Toast.LENGTH_SHORT);
			toast.show();
			return false;
		} else if (email.length() <= 0 || password.length() <= 0
				|| confirmPassword.length() <= 0 || firstName.length() <= 0
				|| lastName.length() <= 0 || gender.equals("Gender")
				|| age.equals("Age Group") || reference.equals("Reference")
				|| address.length() <= 0 || city.length() <= 0
				|| postal.length() <= 0) {
			Toast toast = Toast.makeText(getBaseContext(),
					"All fields must be filled.", Toast.LENGTH_SHORT);
			toast.show();
			return false;
		}

		return true;
	}

	public boolean isEmailValid(CharSequence email) {
		if (email == null)
			return false;
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}
	
	@Override
	public void promptDialog(final String Title ,String Message , String PositiveButton) {
		//TODO
		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this,android.R.style.Theme_Holo_Light));
		builder.setMessage(Message)
			   .setTitle(Title)
		       .setCancelable(false)
		       .setPositiveButton(PositiveButton, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   DataStorageManager.getSingletonInstance().justSignedUp=true;
		        	   if(Title.equalsIgnoreCase("Success"))
		        		   finish();
		     		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
		TextView titleView = (TextView)alert .findViewById(this.getResources().getIdentifier("alertTitle", "id", "android"));
		if (titleView != null) {
		    titleView.setGravity(Gravity.CENTER);
		}
	}
	
	private List<String> getReferences()
	{
		List<String> result = new ArrayList<String>();
		List<WebHeardSiteWay> listSiteWays = DataStorageManager.getSingletonInstance().WebHeardSiteWay;
		for(WebHeardSiteWay item : listSiteWays)
		{
			result .add(item.HeardSiteWayDesc);
		}
		return result;
	}	

}

/*
	 * 
	 */