package com.debroq.tspconnect;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;

import com.debroq.tspconnect.util.SqlUtils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class LoginActivity extends Activity {

	private final static String TAG = "LoginActivity";
	static Context context;
	ParseOfx pox;
	SqlUtils sql;
	ImageView saveChkbox;
	boolean doSaveID = false;
	static String displayId, nbr, pwd;
	TextView password;
	ProgressDialog pd;
	boolean initialId=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getApplicationContext();
		sql = SqlUtils.getInstance(context);
		Intent disclaimerIntent = new Intent(context, DisclaimerActivity.class);
		startActivity(disclaimerIntent);

		setContentView(R.layout.login_main);

		// final View contentView = findViewById(R.id.fullscreen_content);
		final TextView acctNbr = (TextView) findViewById(R.id.inputAcctNbr);
		password = (TextView) findViewById(R.id.inputPassword);
		saveChkbox = (ImageView) findViewById(R.id.saveUserId);
//		final RadioButton numberButton = (RadioButton) findViewById(R.id.rdo1);
//		final RadioButton useridButton = (RadioButton) findViewById(R.id.rdo2);
		final TextView acctNbrStr = (TextView) findViewById(R.id.acctNbrStr);

		displayId = new String(sql.getUserId());
		nbr = displayId;
		if (displayId.length() > 0) {
			displayId="";
			int len=nbr.length();
			for (int k=0; k<len-3; k++)
				displayId = displayId + "*";
			displayId = displayId + nbr.substring(nbr.length()-3, nbr.length());
			acctNbr.setText(displayId);
			saveChkbox.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.savechecked));
			doSaveID = true;
		} else {
			saveChkbox.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.saveunchecked));
			doSaveID = false;
		}

		acctNbr.addTextChangedListener(new TextWatcher(){
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//				Log.i(TAG + " beforeTextChanged", s.toString() + " s:" + start + " c:" + count + " a" + after);
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				Log.i(TAG + " onTextChanged", s.toString() + " s:" + start + " c:" + count);
			}
			@Override
			public void afterTextChanged(Editable s) {
				Log.i(TAG + " afterTextChanged", s.toString());
				String str = s.toString();
				int len = str.length();
				if (initialId) {
					if (len>1) {
						acctNbr.setText("");
						acctNbr.append(str.substring(len-1));
					}
				}
				initialId=false;
			}
			
		});

//		acctNbr.setOnKeyListener(new OnKeyListener() {
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				Log.i(TAG, "onKey key " + keyCode + " was pressed ");
//				if (initialId) {
//		            acctNbr.setText("");
//					initialId=false;
//		    		return false;
//		        }
//				else {
//					initialId=false;
//		        	return false;
//				}
//		    }
//		});
		
//		numberButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				useridButton.setChecked(false);
//				acctNbr.setText("");
//				initialId=false;
//				acctNbr.setInputType(InputType.TYPE_CLASS_NUMBER);
//				acctNbrStr.setText("TSP Account Number:");
//			}
//		});

//		useridButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				numberButton.setChecked(false);
//				acctNbr.setText("");
//				initialId=false;
//				acctNbr.setInputType(InputType.TYPE_CLASS_TEXT);
//				acctNbrStr.setText("User ID:");
//			}
//		});

		Button doLoginButton = (Button) findViewById(R.id.loginButton);
		doLoginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Context context = getApplicationContext();
				Log.i(TAG, "Log In button pressed");
				if (initialId==false)
					nbr = acctNbr.getText().toString();
				pwd = password.getText().toString();
				Log.i(TAG, "acctNbr:" + nbr + " password:" + pwd);
				if ((nbr.length() == 0) && (pwd.length() == 0))
					showToast(context, "Enter login and password");
				else if ((nbr.length() > 0) && pwd.length() == 0)
					showToast(context, "Please enter a password");
				else if ((nbr.length() == 0) && pwd.length() > 0)
					showToast(context, "Please enter an account number or user ID");
				else {
					doLogin(nbr, pwd);
					// String loginResult = doLogin(nbr, pwd);
					// if (loginResult.contains("SUCCESS")) {
					// password.setText("");
					// if (doSaveID)
					// sql.setUserId(nbr);
					// else
					// sql.setUserId("");
					// sql.updateOpts();
					// Intent activityIntent = new Intent(context,
					// AccountOverviewActivity.class);
					// startActivity(activityIntent);
					// } else
					// showToast(context, "Invalid login:" + loginResult);
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		Log.i(TAG, "Back button pressed");
		// super.onBackPressed(); // optional depending on your needs
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// ***Change Here***
		startActivity(intent);
		finish();
		System.exit(0);
	}

	public void toggle_saveUserId(View v) {
		Log.i(TAG, "toggle_saveUserId");
		if (doSaveID == false) {
			doSaveID = true;
			saveChkbox.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.savechecked));
		} else {
			doSaveID = false;
			saveChkbox.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.saveunchecked));
		}
	}

	@SuppressWarnings("deprecation")
	public void doLogin(String nbr, String pwd) {
		String result = "ERROR";

		if ((nbr.length() < 4) && (pwd.length() < 4)) {
			loginError();
			return;
		}

		pox = new ParseOfx(context, nbr, pwd, new Date());
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// pd.setMessage("Loading. Please wait...");
		pd.setIndeterminate(true);
		pd.setCancelable(false);
		new DownloadData().execute();
		pd.show();
	}

	public void saveUserID() {
		password.setText("");
		if (doSaveID)
			sql.setUserId(nbr);
		else
			sql.setUserId("");
		sql.updateOpts();
	}

	public void loginError() {
		showToast(getApplicationContext(),
				"The login information you entered is not valid. Please verify your TSP account number (or user ID) and password.");
	}

	public static void showToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	private class DownloadData extends AsyncTask {

		@Override
		protected Object doInBackground(Object... params) {
			if (nbr.startsWith("123456")) {
				pox.ParseOfxSample();
			} else {
				// pox.ParseOfxSample(); // for testing
				pox.parseOfxFromUrl();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object o) {
			pd.dismiss();
			String loginResult = pox.getLoginMessage();
			if (loginResult.contains("SUCCESS")) {
				saveUserID();
				Intent activityIntent = new Intent(context, AccountOverviewActivity.class);
				startActivity(activityIntent);
			} else
				showToast(context, "Invalid login:" + loginResult);

		}
	}
}