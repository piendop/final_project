package searchlocation.miniproject01.UI.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import searchlocation.miniproject01.UI.ParseApplication;

import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.ParseException;

import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Fragments.RememberMeFragment;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    /************View components in sign up layout***********************/
    private  LinearLayout linearLayout;
    private TextView textViewCreateAccount;
    private EditText _nameText;
    private EditText _emailText;
    private EditText _passwordText;
    private ScrollView mScroll;
	// UI references.

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
        //Auto hide keyboard when tap outside Edittext
        linearLayout = findViewById(R.id.sign_up_linear_layout);
        textViewCreateAccount = findViewById(R.id.tv_create_new_account);
        linearLayout.setOnClickListener(this);
        textViewCreateAccount.setOnClickListener(this);
		Button mLoginButton = (Button) findViewById(R.id.login_link);
		mLoginButton.setOnClickListener(this);
		Button mSignUpButton = (Button) findViewById(R.id.sign_up_button);
		mSignUpButton.setOnClickListener(this);
		_nameText = (EditText) findViewById(R.id.input_name);
		_emailText = (EditText) findViewById(R.id.input_email);
		_passwordText = (EditText
				) findViewById(R.id.input_password);
//
//		mScroll = (ScrollView) findViewById(R.id.sign_up_container);
//		mScroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//			@Override
//			public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//				InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//				if(scrollX!=oldScrollX){
//					inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//				}
//			}
//		});
	}

	public void attempSignUp(){


		if (!validate()) {
			onSignupFailed();
		} else {
            String name = _nameText.getText().toString();
            String email = _emailText.getText().toString();
            String password = _passwordText.getText().toString();


            // TODO: Implement your own signup logic here.

            if(ParseUser.getCurrentUser()==null) {
                ParseUser user = new ParseUser();
                user.setUsername(name);
                user.setEmail(email);
                user.setPassword(password);

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        if (e == null) {
                            Log.i("Sign up: ", "Successful");
                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            onSignupSuccess();
                                        }
                                    }, 3000);
                        }else{
                            Log.i("Signup: ","failed");
                            _nameText.setError("username already used");
                            _emailText.setError("email already used");
                            e.printStackTrace();
                        }
                    }
                });
            }
		}
	}

	public void onSignupSuccess() {
		View _signupButton = (View) findViewById(R.id.sign_up_button);
		_signupButton.setEnabled(true);
		setResult(RESULT_OK, null);
        //TODO: successful login save username
        RememberMeFragment fragment = new RememberMeFragment();
        fragment.show(getFragmentManager(),"Open Diaglog");
	}


	public void onSignupFailed() {
		Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
		View _signupButton = (View) findViewById(R.id.sign_up_button);
		_signupButton.setEnabled(true);
	}
	public boolean validate() {
		EditText _reEnterPasswordText = (EditText) findViewById(R.id.input_repassword);
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

	// TODO: on click
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.sign_up_linear_layout || v.getId() == R.id.tv_create_new_account){
            InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                if(getCurrentFocus()!=null)
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
            }
        }else if(v.getId()==R.id.sign_up_button){
            InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                if(getCurrentFocus()!=null)
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                attempSignUp();

            }else{
                attempSignUp();
            }


        }else if(v.getId()==R.id.login_link){
            Intent loginActivity = new Intent(SignUp.this,LoginActivity.class);
            startActivity(loginActivity);
        }
    }


}
