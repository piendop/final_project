package searchlocation.miniproject01.UI.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import searchlocation.miniproject01.R;

public class SignUp extends AppCompatActivity {

	// UI references.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		Button mLoginButton = (Button) findViewById(R.id.login_link);
		mLoginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent loginActivity = new Intent(SignUp.this,LoginActivity.class);
				startActivity(loginActivity);
			}
		});


		Button mSignUpButton = (Button) findViewById(R.id.sign_up_button);
		mSignUpButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				attempSignUp();
			}
		});
	}

	public void attempSignUp(){
		EditText _nameText = (EditText) findViewById(R.id.input_name);
		EditText _emailText = (EditText) findViewById(R.id.input_email);
		EditText _passwordText = (EditText) findViewById(R.id.input_password);
		EditText _reEnterPasswordText = (EditText) findViewById(R.id.input_Repassword);
		View _signupButton = (View) findViewById(R.id.sign_up_button);
		if (!validate()) {
			onSignupFailed();
			return;
		}
		_signupButton.setEnabled(false);
/*
		final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
				R.style.AppTheme_Dark_Dialog);
		progressDialog.setIndeterminate(true);
		progressDialog.setMessage("Creating Account...");
		progressDialog.show();*/

		String name = _nameText.getText().toString();
		String email = _emailText.getText().toString();
		String password = _passwordText.getText().toString();
		String reEnterPassword = _reEnterPasswordText.getText().toString();

		// TODO: Implement your own signup logic here.

		new android.os.Handler().postDelayed(
				new Runnable() {
					public void run() {
						// On complete call either onSignupSuccess or onSignupFailed
						// depending on success
						onSignupSuccess();
						// onSignupFailed();
					//	progressDialog.dismiss();
					}
				}, 3000);
	}

	public void onSignupSuccess() {
		View _signupButton = (View) findViewById(R.id.sign_up_button);
		_signupButton.setEnabled(true);
		setResult(RESULT_OK, null);
		loadIntroActivity();
	}

	public void onSignupFailed() {
		Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
		View _signupButton = (View) findViewById(R.id.sign_up_button);
		_signupButton.setEnabled(true);
	}
	public boolean validate() {
		EditText _nameText = (EditText) findViewById(R.id.input_name);
		EditText _emailText = (EditText) findViewById(R.id.input_email);
		EditText _passwordText = (EditText) findViewById(R.id.input_password);
		EditText _reEnterPasswordText = (EditText) findViewById(R.id.input_Repassword);
		boolean valid = true;

		String name = _nameText.getText().toString();
		String email = _emailText.getText().toString();
		String password = _passwordText.getText().toString();
		String reEnterPassword = _reEnterPasswordText.getText().toString();

		if (name.isEmpty() || name.length() < 3) {
			_nameText.setError("at least 3 characters");
			valid = false;
		} else {
			_nameText.setError(null);
		}



		if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
			_emailText.setError("enter a valid email address");
			valid = false;
		} else {
			_emailText.setError(null);
		}


		if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
			_passwordText.setError("between 4 and 10 alphanumeric characters");
			valid = false;
		} else {
			_passwordText.setError(null);
		}

		if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
			_reEnterPasswordText.setError("Password Do not match");
			valid = false;
		} else {
			_reEnterPasswordText.setError(null);
		}

		return valid;
	}
	public void loadIntroActivity() {
		Intent loadIntro = new Intent(SignUp.this,IntroActivity.class);
		startActivity(loadIntro);
	}

}
