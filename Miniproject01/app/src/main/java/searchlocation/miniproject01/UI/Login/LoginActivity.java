package searchlocation.miniproject01.UI.Login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import searchlocation.miniproject01.R;
import searchlocation.miniproject01.UI.Fragments.RememberMeFragment;
import searchlocation.miniproject01.UI.OnGoing.OnGoingActivity;
import searchlocation.miniproject01.UI.OnGoing.OnGoingEmptyActivity;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener {

	/**
	 * Id to identity READ_CONTACTS permission request.
	 */
	private static final int REQUEST_READ_CONTACTS = 0;


	public Boolean isRemember = null;
	// UI references.
	private AutoCompleteTextView mEmailView;
	private EditText mPasswordView;
	private LinearLayout linearLayout;
	private TextView textViewLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// Set up the login form.
		mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        linearLayout = findViewById(R.id.log_in_linear_layout);
        textViewLogin = findViewById(R.id.tv_login);
        linearLayout.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);

		populateAutoComplete();
		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});

		Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
		mEmailSignInButton.setOnClickListener(this);
		Button mSignUpButton = (Button) findViewById(R.id.sign_up_link);
		mSignUpButton.setOnClickListener(this);


	}

	private void populateAutoComplete() {
		if (!mayRequestContacts()) {
			return;
		}
	}

	private boolean mayRequestContacts() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return true;
		}
		if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
			return true;
		}
		if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
			Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
					.setAction(android.R.string.ok, new View.OnClickListener() {
						@Override
						@TargetApi(Build.VERSION_CODES.M)
						public void onClick(View v) {
							requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
						}
					});
		} else {
			requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
		}
		return false;
	}

	/**
	 * Callback received when a permissions request has been completed.
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
																				 @NonNull int[] grantResults) {
		if (requestCode == REQUEST_READ_CONTACTS) {
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				populateAutoComplete();
			}
		}
	}


	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	private void attemptLogin() {

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		String email = mEmailView.getText().toString();
		final String password = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password, if the user entered one.
		if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(email)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		}
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //	showProgress(true);
            // TODO: register the new account here.
            ParseQuery<ParseUser> user = ParseUser.getQuery();
            if (isEmailValid(email)) {
                user.whereEqualTo("email", email);

                user.setLimit(1);
                user.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (e == null && objects.size() == 1) {
                            String username = objects.get(0).getUsername();
                            ParseUser.logInInBackground(username, password, new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {
                                    if (e == null && user != null) {
										//TODO: successful login save username
										RememberMeFragment fragment = new RememberMeFragment();
										fragment.show(getFragmentManager(),"Open Diaglog");
                                    } else {
                                        mPasswordView.setError("wrong password");
                                    }
                                }
                            });
                        } else {
                            mEmailView.setError("invalid email");
                        }
                    }
                });
            }else {//user login by username
                ParseUser.logInInBackground(email, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e == null && user != null) {
							//TODO: successful login save username
							RememberMeFragment fragment = new RememberMeFragment();
							fragment.show(getFragmentManager(),"Open Diaglog");
                        } else {
                            Toast.makeText(LoginActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }
	}

	private boolean isEmailValid(String email) {
		//TODO: Replace this with your own logic
		return email.contains("@");
	}

	private boolean isPasswordValid(String password) {
		//TODO: Replace this with your own logic
		return password.length() > 4;
	}

<<<<<<< HEAD
	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

		private final String mEmail;
		private final String mPassword;

		UserLoginTask(String email, String password) {
			mEmail = email;
			mPassword = password;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				// Simulate network access.
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			}

			for (String credential : DUMMY_CREDENTIALS) {
				String[] pieces = credential.split(":");
				if (pieces[0].equals(mEmail)) {
					// Account exists, return true if the password matches.
					return pieces[1].equals(mPassword);
				} else {
					// If account does not exist then trigger fragment LoginToSignUp
					Toast.makeText(LoginActivity.this, "Account does not exist.", Toast.LENGTH_SHORT).show();
				}
			}
			// TODO: register the new account here.
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
		//	showProgress(false);
			if (success) {
			//	loadOnGoingActivity();
			//	loadIntroActivity();
				//Show dialog
				RememberMeFragment fragment = new RememberMeFragment();
				fragment.show(getFragmentManager(),"Open Diaglog");
//				if (isRemember==null){
//
//				}
//				if(isRemember) {
//					saveAccount();
//					loadIntroActivity();
//					Toast.makeText(LoginActivity.this, "Remember account", Toast.LENGTH_SHORT).show();
//				} else {
//					loadIntroActivity();
//					Toast.makeText(LoginActivity.this, "Not now", Toast.LENGTH_SHORT).show();
//				}

			} else {
				mPasswordView.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
		//	showProgress(false);
		}
	}



	private void loadOnGoingActivity() {
		Intent loadOnGoing = new Intent(LoginActivity.this, OnGoingActivity.class);
		startActivity(loadOnGoing);
	}

	private void loadIntroActivity() {
		Intent loadIntro = new Intent(LoginActivity.this,IntroActivity.class);
		startActivity(loadIntro);
	}


=======
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.log_in_linear_layout || v.getId() == R.id.tv_login){
            InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                if(getCurrentFocus()!=null)
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
            }
        }else if(v.getId()==R.id.email_sign_in_button){
            InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                if(getCurrentFocus()!=null) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    attemptLogin();
                }
            }
        }else if(v.getId()==R.id.sign_up_link){
            Intent loginActivity = new Intent(LoginActivity.this,SignUp.class);
            startActivity(loginActivity);
        }
    }
>>>>>>> 9164e700e607ca624cebe8ab1474939787183b37
}
