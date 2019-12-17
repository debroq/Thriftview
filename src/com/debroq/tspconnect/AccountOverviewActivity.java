package com.debroq.tspconnect;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
//import android.widget.TabHost.TabContentFactory;
//import android.widget.TabHost.TabSpec;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class AccountOverviewActivity extends TabActivity implements OnTabChangeListener {
	private final static String TAG = "AccountBalanceActivity";

	TextView balanceDate, statementDate, acctBalFund, acctBalCont, contSummary;
	TextView menuShowAcctBalance, menuShowDailyFundAct, menuShowContAlloc, menuShowInterTransfers,
			menuShowHistoricalFundAct, menuShowAbout;
	ScrollView scrollview;
	DatePicker dpResult;
	ImageView calendar;
	int year;
	int month;
	int day;
	final int DATE_DIALOG_ID = 999;
	TabHost tabHost;
	FrameLayout menuframe;
	Context context;
	ParseOfx pox;
	Dialog dialog;
	ProgressDialog pd;
	private AdView adView;

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pox = ParseOfx.getInstance();

		context = getApplicationContext();
		setContentView(R.layout.account_overview);
		Log.i(TAG, "AccountOverviewActivity created");

		adView = new AdView(this);
		// adView = (AdView) findViewById(R.id.adView);
		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId("ca-app-pub-7497413672008464/7335181979");
		RelativeLayout relayout = (RelativeLayout) findViewById(R.id.relayout);
		ScrollView sview = (ScrollView) findViewById(R.id.scrollview);
		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		// adParams.addRule(RelativeLayout.BELOW, sview.getId());
		relayout.addView(adView, adParams);

		AdRequest adRequest = new AdRequest.Builder()
//				 .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//				 .addTestDevice("612393409BA675AA834D0C982055BAD9")
				.build();
		adView.loadAd(adRequest);

		computeDistPercentages();

		tabHost = getTabHost();
		// tabHost = (TabHost) findViewById(R.id.tabhost);
		// tabHost.setup();
		scrollview = (ScrollView) findViewById(R.id.scrollview);
//		menuframe = (FrameLayout) findViewById(R.id.menuframe);

		TextView accountbalance = (TextView) findViewById(R.id.account_balance);
		DecimalFormat numFormat = new DecimalFormat("$#,###,###.##");
		String totval = numFormat.format(totalBalance());
		accountbalance.setText("Account Balance: " + totval);

//		menuShowDailyFundAct = (TextView) findViewById(R.id.menuShowDailyFundAct);
//		menuShowAbout = (TextView) findViewById(R.id.menuShowAbout);

		// Tab 1
		Intent act1 = new Intent().setClass(this, AccountBalanceActivity.class);
		setupTab(new TextView(this), "Account Balance by Fund", act1);

		// Tab 2
		Intent act2 = new Intent().setClass(this, RecentTransactions.class);
		setupTab(new TextView(this), "Recent Transactions", act2);

		// Tab 3
		if (ParseOfx.memoContrib == true) {
			Intent act3 = new Intent().setClass(this, ContributionSummary.class);
			setupTab(new TextView(this), "Contribution Summary", act3);
		}

		tabHost.setOnTabChangedListener(this);
		// tabHost.getTabWidget().setStripEnabled(true);

		// statementDate = (TextView) findViewById(R.id.statement_date);
		balanceDate = (TextView) findViewById(R.id.balance_date);
		String mn = ParseOfx.balanceDate.substring(4, 6);
		String dy = ParseOfx.balanceDate.substring(6, 8);
		String yr = ParseOfx.balanceDate.substring(0, 4);
		month = Integer.valueOf(mn);
		day = Integer.valueOf(dy);
		year = Integer.valueOf(yr);
		String newdate = mn + "/" + dy + "/" + yr;
		// statementDate.setText(newdate);
		balanceDate.setText(newdate);

		// Temporarily disable this listener
		// balanceDate.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// showDialog(DATE_DIALOG_ID);
		// }
		// });

		setCurrentDateOnView();
		// Temporarily disable this listener
		// calendar = (ImageView) findViewById(R.id.calendar);
		// calendar.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// showDialog(DATE_DIALOG_ID);
		// }
		// });
	}

	/** Called when leaving the activity */
	@Override
	public void onPause() {
		if (adView != null) {
			adView.pause();
		}
		super.onPause();
	}

	/** Called before the activity is destroyed */
	@Override
	public void onDestroy() {
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}

	private void setupTab(final View view, final String tag, final Intent intent) {
		View tabview = createTabView(tabHost.getContext(), tag);
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		TabSpec setContent = tabHost.newTabSpec(tag).setIndicator(tabview).setContent(intent);
		tabHost.addTab(setContent);
	}

	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}

//	public void showDailyFundActivity(View v) {
//		Log.i(TAG, "showDailyFundActivity");
//		clear_menu();
//		Resources res = getResources();
//		int color = res.getColor(R.color.yellow);
//		menuShowDailyFundAct.setBackgroundColor(color);
//		toggle_menu(null);
//		Intent intent = new Intent().setClass(context, ChartActivity.class);
//		startActivity(intent);
//	}

//	public void showHistoricalFundActivity(View v) {
//		Log.i(TAG, "showHistoricalFundActivity");
//		clear_menu();
//		Resources res = getResources();
//		int color = res.getColor(R.color.yellow);
//		// menuShowHistoricalFundAct.setBackgroundColor(color);
//		toggle_menu(null);
//	}

//	public void showAbout(View v) {
//		Log.i(TAG, "menuShowAbout");
//		clear_menu();
//		Resources res = getResources();
//		int color = res.getColor(R.color.yellow);
//		menuShowAbout.setBackgroundColor(color);
//		toggle_menu(null);
//		DisclaimerActivity.forceDisclaimer = true;
//		Intent disclaimerIntent = new Intent(context, DisclaimerActivity.class);
//		startActivity(disclaimerIntent);
//		// DisclaimerActivity.forceDisclaimer=false;
//	}

	public BigDecimal totalBalance() {
		BigDecimal total = new BigDecimal(0);
		total = total.add(ParseOfx.L2050.getBalanceValue());
		total = total.add(ParseOfx.L2040.getBalanceValue());
		total = total.add(ParseOfx.L2030.getBalanceValue());
		total = total.add(ParseOfx.L2020.getBalanceValue());
		total = total.add(ParseOfx.LIncome.getBalanceValue());
		total = total.add(ParseOfx.GFund.getBalanceValue());
		total = total.add(ParseOfx.FFund.getBalanceValue());
		total = total.add(ParseOfx.CFund.getBalanceValue());
		total = total.add(ParseOfx.SFund.getBalanceValue());
		total = total.add(ParseOfx.IFund.getBalanceValue());
		Log.i(TAG, "total account balance: " + total);
		return total;
	}

	public void computeDistPercentages() {
		DecimalFormat numFormat = new DecimalFormat("#.##");
		double total = totalBalance().doubleValue();
		double percent = (Double.valueOf(ParseOfx.L2050.getBalance()) / total) * 100.0;
		ParseOfx.L2050.setDistOfAcct(numFormat.format(percent));
		percent = (Double.valueOf(ParseOfx.L2040.getBalance()) / total) * 100.0;
		ParseOfx.L2040.setDistOfAcct(numFormat.format(percent));
		percent = (Double.valueOf(ParseOfx.L2030.getBalance()) / total) * 100.0;
		ParseOfx.L2030.setDistOfAcct(numFormat.format(percent));
		percent = (Double.valueOf(ParseOfx.L2020.getBalance()) / total) * 100.0;
		ParseOfx.L2020.setDistOfAcct(numFormat.format(percent));
		percent = (Double.valueOf(ParseOfx.LIncome.getBalance()) / total) * 100.0;
		ParseOfx.LIncome.setDistOfAcct(numFormat.format(percent));
		percent = (Double.valueOf(ParseOfx.GFund.getBalance()) / total) * 100.0;
		ParseOfx.GFund.setDistOfAcct(numFormat.format(percent));
		percent = (Double.valueOf(ParseOfx.FFund.getBalance()) / total) * 100.0;
		ParseOfx.FFund.setDistOfAcct(numFormat.format(percent));
		percent = (Double.valueOf(ParseOfx.CFund.getBalance()) / total) * 100.0;
		ParseOfx.CFund.setDistOfAcct(numFormat.format(percent));
		percent = (Double.valueOf(ParseOfx.SFund.getBalance()) / total) * 100.0;
		ParseOfx.SFund.setDistOfAcct(numFormat.format(percent));
		percent = (Double.valueOf(ParseOfx.IFund.getBalance()) / total) * 100.0;
		ParseOfx.IFund.setDistOfAcct(numFormat.format(percent));
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i(TAG, "On Start .....");
	}

	/** Called when returning to the activity */
	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "On Resume .....");
		if (adView != null) {
			adView.resume();
		}
//		menuframe.setVisibility(View.GONE);
		scrollview.scrollTo(0, 0);
		scrollview.smoothScrollTo(0, 0);
	}

	public void logout(View v) {
		Log.i(TAG, "logout");
		finish();
	}

	// display current date
	public void setCurrentDateOnView() {
		Log.i(TAG, "setCurrentDateOnView");

		// final Calendar c = Calendar.getInstance();
		// year = c.get(Calendar.YEAR);
		// month = c.get(Calendar.MONTH);
		// day = c.get(Calendar.DAY_OF_MONTH);

		// set current date into textview
		balanceDate.setText(new StringBuilder()
				// Month is 0 based, just add 1
				.append(month).append("/").append(day).append("/").append(year).append(" "));
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Log.i(TAG, "onCreateDialog");
		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			dialog = new DatePickerDialog(this, datePickerListener, year, month - 1, day);
			dialog.setTitle("Set Date");
			return dialog;
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			Log.i(TAG, "onDateSet year:" + selectedYear + " month:" + selectedMonth + " day:" + selectedDay);

			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			// dialog.dismiss();
			// set selected date into textview
			// balanceDate.setText(
			// new
			// StringBuilder().append(month).append("-").append(day).append("-").append(year).append("
			// "));
			Log.i(TAG, "Query for new data");
			ParseOfx pox = ParseOfx.getInstance();
			java.util.Date date = new Date(year - 1900, month, day);
			pox.setNewDate(date);
			pd = new ProgressDialog(view.getContext());
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			// pd.setMessage("Loading. Please wait...");
			pd.setIndeterminate(true);
			pd.setCancelable(false);
			new DownloadData().execute();
			pd.show();
		}
	};

	private class DownloadData extends AsyncTask {

		@Override
		protected Object doInBackground(Object... params) {
			if (LoginActivity.nbr.startsWith("12345")) {
				pox.ParseOfxSample();
			} else {
				pox.parseOfxFromUrl();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object o) {
			pd.dismiss();
			Log.i(TAG, "AccountOverviewActivity restarting");
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}
	}

//	private void clear_menu() {
//		Resources res = getResources();
//		int color = res.getColor(R.color.white);
//		menuShowDailyFundAct.setBackgroundColor(color);
//		// menuShowHistoricalFundAct.setBackgroundColor(color);
//		menuShowAbout.setBackgroundColor(color);
//	}

//	public void toggle_menu(View v) {
//		menuframe.setVisibility(menuframe.isShown() ? View.GONE : View.VISIBLE);
//		Log.i(TAG, "toggle_lifecycle_funds");
//	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void onTabChanged(String tabId) {
		Log.i(TAG, "selected tab " + tabId);
		scrollview.scrollTo(0, 0);
		scrollview.smoothScrollTo(0, 0);
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (NoSuchMethodException e) {
					Log.e("", "onMenuOpened", e);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.daily_fund_activity:
			Intent intent = new Intent().setClass(context, ChartActivity.class);
			startActivity(intent);
			return true;
		case R.id.about_tsp:
			DisclaimerActivity.forceDisclaimer = true;
			Intent disclaimerIntent = new Intent(context, DisclaimerActivity.class);
			startActivity(disclaimerIntent);
			return true;
		case R.id.exit:
			finishAffinity();
//			Toast.makeText(this, "Exit", Toast.LENGTH_SHORT);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// public void showDialog(MenuItem item) {
	// new MoreInfo().show(getFragmentManager(), TAG);
	// }

}
