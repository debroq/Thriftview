package com.debroq.tspconnect;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

public class RecentTransactions  extends TabActivity {
	private static final String TAG = RecentTransactions.class.getName();
	Context context;
	TextView l2050, l2040, l2030, l2020, lincome;
	TextView gfund, ffund, cfund, sfund, ifund;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.recent_transactions);

		l2050 = (TextView) findViewById(R.id.l2050_units);
		l2050.setText(ParseOfx.L2050T.getShares());
		l2050 = (TextView) findViewById(R.id.l2050_price);
		l2050.setText(formatString(ParseOfx.L2050T.getSharePrice()));
		l2050 = (TextView) findViewById(R.id.l2050_total);
		l2050.setText(formatString(ParseOfx.L2050T.getBalance()));

		l2040 = (TextView) findViewById(R.id.l2040_units);
		l2040.setText(ParseOfx.L2040T.getShares());
		l2040 = (TextView) findViewById(R.id.l2040_price);
		l2040.setText(formatString(ParseOfx.L2040T.getSharePrice()));
		l2040 = (TextView) findViewById(R.id.l2040_total);
		l2040.setText(formatString(ParseOfx.L2040T.getBalance()));

		l2030 = (TextView) findViewById(R.id.l2030_units);
		l2030.setText(ParseOfx.L2030T.getShares());
		l2030 = (TextView) findViewById(R.id.l2030_price);
		l2030.setText(formatString(ParseOfx.L2030T.getSharePrice()));
		l2030 = (TextView) findViewById(R.id.l2030_total);
		l2030.setText(formatString(ParseOfx.L2030T.getBalance()));

		l2020 = (TextView) findViewById(R.id.l2020_units);
		l2020.setText(ParseOfx.L2020T.getShares());
		l2020 = (TextView) findViewById(R.id.l2020_price);
		l2020.setText(formatString(ParseOfx.L2020T.getSharePrice()));
		l2020 = (TextView) findViewById(R.id.l2020_total);
		l2020.setText(formatString(ParseOfx.L2020T.getBalance()));

		lincome = (TextView) findViewById(R.id.lincome_units);
		lincome.setText(ParseOfx.LIncomeT.getShares());
		lincome = (TextView) findViewById(R.id.lincome_price);
		lincome.setText(formatString(ParseOfx.LIncomeT.getSharePrice()));
		lincome = (TextView) findViewById(R.id.lincome_total);
		lincome.setText(formatString(ParseOfx.LIncomeT.getBalance()));

		gfund = (TextView) findViewById(R.id.gfund_units);
		gfund.setText(ParseOfx.GFundT.getShares());
		gfund = (TextView) findViewById(R.id.gfund_price);
		gfund.setText(formatString(ParseOfx.GFundT.getSharePrice()));
		gfund = (TextView) findViewById(R.id.gfund_total);
		gfund.setText(formatString(ParseOfx.GFundT.getBalance()));


		ffund = (TextView) findViewById(R.id.ffund_units);
		ffund.setText(ParseOfx.FFundT.getShares());
		ffund = (TextView) findViewById(R.id.ffund_price);
		ffund.setText(formatString(ParseOfx.FFundT.getSharePrice()));
		ffund = (TextView) findViewById(R.id.ffund_total);
		ffund.setText(formatString(ParseOfx.FFundT.getBalance()));

		cfund = (TextView) findViewById(R.id.cfund_units);
		cfund.setText(ParseOfx.CFundT.getShares());
		cfund = (TextView) findViewById(R.id.cfund_price);
		cfund.setText(formatString(ParseOfx.CFundT.getSharePrice()));
		cfund = (TextView) findViewById(R.id.cfund_total);
		cfund.setText(formatString(ParseOfx.CFundT.getBalance()));

		sfund = (TextView) findViewById(R.id.sfund_units);
		sfund.setText(ParseOfx.SFundT.getShares());
		sfund = (TextView) findViewById(R.id.sfund_price);
		sfund.setText(formatString(ParseOfx.SFundT.getSharePrice()));
		sfund = (TextView) findViewById(R.id.sfund_total);
		sfund.setText(formatString(ParseOfx.SFundT.getBalance()));

		ifund = (TextView) findViewById(R.id.ifund_units);
		ifund.setText(ParseOfx.IFundT.getShares());
		ifund = (TextView) findViewById(R.id.ifund_price);
		ifund.setText(formatString(ParseOfx.IFundT.getSharePrice()));
		ifund = (TextView) findViewById(R.id.ifund_total);
		ifund.setText(formatString(ParseOfx.IFundT.getBalance()));
	}
	
	private static String formatString(String st) {
		DecimalFormat numFormat = new DecimalFormat("$#,###,###.##");
		double dval = Double.valueOf(st);
		BigDecimal bd = BigDecimal.valueOf(dval).setScale(2);
		String totval = numFormat.format(bd);
		return totval;
	}

}
