package com.debroq.tspconnect;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import android.view.ViewGroup.LayoutParams;

@SuppressWarnings("deprecation")
public class AccountBalanceActivity extends TabActivity {
	private static final String TAG = AccountBalanceActivity.class.getName();
	static AccountBalanceActivity t1a;

	View lifecycleFunds, individualFunds;
	TextView accountbalance;
	TextView lcf_bal, l2050, l2040, l2030, l2020, lincome;
	TextView ind_bal, gfund, ffund, cfund, sfund, ifund;
	ImageView expandlcfunds, expandindfunds;
	Context context;
	static String currentDistType = "G";
	static Fund currentDistFund = null;
	Fund selectedFund;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		t1a = this;

		setContentView(R.layout.account_balance);

		// accountbalance = (TextView) findViewById(R.id.account_balance);
		lifecycleFunds = findViewById(R.id.lifecycleFunds);
		individualFunds = findViewById(R.id.individualFunds);
		expandlcfunds = (ImageView) findViewById(R.id.expandlcfunds);
		expandindfunds = (ImageView) findViewById(R.id.expandindfunds);

		// accountbalance.setText("Account Balance: $" +
		// String.valueOf(totalBalance()) + " - Civilian");
		lcf_bal = (TextView) findViewById(R.id.lcf_bal);
		lcf_bal.setText("Balance: " + formatBigDecimal(lifecycle_balance()));
		l2050 = (TextView) findViewById(R.id.l2050_bal);
		l2050.setText(formatString(ParseOfx.L2050.getBalance()));
		l2040 = (TextView) findViewById(R.id.l2040_bal);
		l2040.setText(formatString(ParseOfx.L2040.getBalance()));
		l2030 = (TextView) findViewById(R.id.l2030_bal);
		l2030.setText(formatString(ParseOfx.L2030.getBalance()));
		l2020 = (TextView) findViewById(R.id.l2020_bal);
		l2020.setText(formatString(ParseOfx.L2020.getBalance()));
		lincome = (TextView) findViewById(R.id.lincome_bal);
		lincome.setText(formatString(ParseOfx.LIncome.getBalance()));

		ind_bal = (TextView) findViewById(R.id.ind_bal);
		ind_bal.setText("Balance: " + formatBigDecimal(individual_balance()));
		gfund = (TextView) findViewById(R.id.gfund_bal);
		gfund.setText(formatString(ParseOfx.GFund.getBalance()));
		ffund = (TextView) findViewById(R.id.ffund_bal);
		ffund.setText(formatString(ParseOfx.FFund.getBalance()));
		cfund = (TextView) findViewById(R.id.cfund_bal);
		cfund.setText(formatString(ParseOfx.CFund.getBalance()));
		sfund = (TextView) findViewById(R.id.sfund_bal);
		sfund.setText(formatString(ParseOfx.SFund.getBalance()));
		ifund = (TextView) findViewById(R.id.ifund_bal);
		ifund.setText(formatString(ParseOfx.IFund.getBalance()));
		
		drawChart();
	}

	private String formatBigDecimal(BigDecimal bd) {
		DecimalFormat numFormat = new DecimalFormat("$#,###,###.##");
		String totval = numFormat.format(bd);
		return totval;
	}

	private static String formatString(String st) {
		DecimalFormat numFormat = new DecimalFormat("$#,###,###.##");
		double dval = Double.valueOf(st);
		BigDecimal bd = BigDecimal.valueOf(dval).setScale(2);
		String totval = numFormat.format(bd);
		return totval;
	}

	public BigDecimal lifecycle_balance() {
		BigDecimal total = new BigDecimal(0);
		total = total.add(ParseOfx.L2050.getBalanceValue());
		total = total.add(ParseOfx.L2040.getBalanceValue());
		total = total.add(ParseOfx.L2030.getBalanceValue());
		total = total.add(ParseOfx.L2020.getBalanceValue());
		total = total.add(ParseOfx.LIncome.getBalanceValue());
		return total;
	}

	public BigDecimal individual_balance() {
		BigDecimal total = new BigDecimal(0);
		total = total.add(ParseOfx.GFund.getBalanceValue());
		total = total.add(ParseOfx.FFund.getBalanceValue());
		total = total.add(ParseOfx.CFund.getBalanceValue());
		total = total.add(ParseOfx.SFund.getBalanceValue());
		total = total.add(ParseOfx.IFund.getBalanceValue());
		return total;
	}

	public void toggle_lifecycle_funds(View v) {
		if (lifecycleFunds.isShown() == true) {
			lifecycleFunds.setVisibility(View.GONE);
			expandlcfunds.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.iconarrowright));
		} else {
			lifecycleFunds.setVisibility(View.VISIBLE);
			expandlcfunds.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.iconarrowdown));
		}
		Log.i(TAG, "toggle_lifecycle_funds");
	}

	public void toggle_individual_funds(View v) {
		if (individualFunds.isShown() == true) {
			individualFunds.setVisibility(View.GONE);
			expandindfunds.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.iconarrowright));
		} else {
			individualFunds.setVisibility(View.VISIBLE);
			expandindfunds.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.iconarrowdown));
		}
		Log.i(TAG, "toggle_individual_funds");
	}

	public void showDistL2050(View v) {
		Log.i(TAG, "showL2050Fund");
//		currentDistType = "5";
		currentDistFund = ParseOfx.L2050;
		showDistDialog(ParseOfx.L2050);
	}

	public void showDistL2040(View v) {
		Log.i(TAG, "showL2040Fund");
		currentDistFund = ParseOfx.L2040;
		showDistDialog(ParseOfx.L2040);
	}

	public void showDistL2030(View v) {
		Log.i(TAG, "showL2030Fund");
		currentDistFund = ParseOfx.L2030;
		showDistDialog(ParseOfx.L2030);
	}

	public void showDistL2020(View v) {
		Log.i(TAG, "showL2020Fund");
		currentDistFund = ParseOfx.L2020;
		showDistDialog(ParseOfx.L2020);
	}

	public void showDistLIncome(View v) {
		Log.i(TAG, "showLIncome");
		currentDistFund = ParseOfx.LIncome;
		showDistDialog(ParseOfx.LIncome);
	}

	public void showDistGFund(View v) {
		Log.i(TAG, "showDistGFund");
		currentDistFund = ParseOfx.GFund;
		showDistDialog(ParseOfx.GFund);
	}

	public void showDistFFund(View v) {
		Log.i(TAG, "showDistFFund");
		selectedFund = ParseOfx.IFund;
		currentDistFund = ParseOfx.FFund;
		showDistDialog(ParseOfx.FFund);
	}

	public void showDistCFund(View v) {
		Log.i(TAG, "showDistCFund");
		selectedFund = ParseOfx.IFund;
		currentDistFund = ParseOfx.CFund;
		showDistDialog(ParseOfx.CFund);
	}

	public void showDistSFund(View v) {
		Log.i(TAG, "showDistSFund");
		currentDistFund = ParseOfx.SFund;
		showDistDialog(ParseOfx.SFund);
	}

	public void showDistIFund(View v) {
		Log.i(TAG, "showDistIFund");
		currentDistFund = ParseOfx.IFund;
		showDistDialog(ParseOfx.IFund);
	}

	public void showDistDialog(Fund fund) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout;
		if (ParseOfx.memoContrib==true)
			layout = inflater.inflate(R.layout.fund_distribution, null);
		else
			layout = inflater.inflate(R.layout.fund_distribution_short, null);

				
		dialog.setTitle(currentDistFund.getName());
		TableLayout tl = (TableLayout) findViewById(R.id.tabLayout);
		
		TextView shamt = (TextView) layout.findViewById(R.id.sharesAmt);
		shamt.setText(fund.getShares());
		TextView shprc = (TextView) layout.findViewById(R.id.priceAmt);
		shprc.setText("$" + fund.getSharePrice());
		TextView bal = (TextView) layout.findViewById(R.id.balanceAmt);
		bal.setText(formatString(fund.getBalance()));
		TextView dist = (TextView) layout.findViewById(R.id.distPctAmt);
		dist.setText(fund.getDistOfAcct() + "%");
		if (ParseOfx.memoContrib==true) {
			TextView cont = (TextView) layout.findViewById(R.id.contPctAmt);
			cont.setText(fund.getContAlloc() + "%");
			TextView trad = (TextView) layout.findViewById(R.id.traditionalAmt);
			trad.setText(formatString(fund.getTraditional()));
			TextView taxex = (TextView) layout.findViewById(R.id.taxExemptAmt);
			taxex.setText(formatString(fund.getTaxExempt()));
			TextView roth = (TextView) layout.findViewById(R.id.rothAmt);
			roth.setText(formatString(fund.getRoth()));
			TextView aa = (TextView) layout.findViewById(R.id.agencyAutoAmt);
			aa.setText(formatString(fund.getAgencyAuto()));
			TextView am = (TextView) layout.findViewById(R.id.agencyMatchAmt);
			am.setText(formatString(fund.getAgencyMatch()));
		}
		dialog.setView(layout);
		dialog.show();

	}
	
	public void drawChart() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.chart);

		CategorySeries categorySeries = new CategorySeries("Funds");

		int[] colors = new int[] { Color.RED, Color.GREEN, 
				Color.MAGENTA, Color.YELLOW, Color.CYAN, Color.GRAY,
				Color.BLUE, Color.RED, Color.GREEN, Color.CYAN};

	    //  Only include funds with money
	    Fund[] fundary = new Fund[] {ParseOfx.L2050, ParseOfx.L2040, ParseOfx.L2030, ParseOfx.L2020, ParseOfx.LIncome, ParseOfx.GFund, ParseOfx.FFund, ParseOfx.CFund, ParseOfx.SFund, ParseOfx.IFund};
	    int numfunds=0;
	    for (int knt=0; knt<10; knt++) {
	    	if (fundary[knt].getBalanceDouble()!=0.0) {
	    		numfunds++;
	    		categorySeries.add(fundary[knt].getName() + " - " + fundary[knt].getDistOfAcct() + "%", fundary[knt].getBalanceDouble());
	    	}
	    }
	    int[] colorAry = new int[numfunds];
	    for (int cnt=0; cnt<numfunds; cnt++)
	    	colorAry[cnt] = colors[cnt];

		DefaultRenderer renderer = buildCategoryRenderer(colorAry);
		renderer.setZoomEnabled(false);
	    renderer.setPanEnabled(false);
//	   	renderer.setApplyBackgroundColor(false);
	   	renderer.setBackgroundColor(Color.WHITE);
//	    renderer.setChartTitle("Account Distribution");
//	    renderer.setChartTitleTextSize((float)60.0);
	    renderer.setLabelsColor(Color.BLACK);
	    renderer.setLabelsTextSize(40);
	    renderer.setShowLabels(true);
	    renderer.setLegendTextSize(60);
	    renderer.setDisplayValues(false);
	    renderer.setShowLegend(false);

		View mChartView = ChartFactory.getPieChartView(this, categorySeries, renderer);
		layout.addView(mChartView,
				new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT));
	}

	protected DefaultRenderer buildCategoryRenderer(int[] colors) {
		DefaultRenderer renderer = new DefaultRenderer();
		for (int color : colors) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(color);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}

}
