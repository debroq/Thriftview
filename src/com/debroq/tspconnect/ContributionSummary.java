package com.debroq.tspconnect;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;


public class ContributionSummary  extends TabActivity {
	private static final String TAG = ContributionSummary.class.getName();
	Context context;
	TextView tview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.contribution_summary);

		tview = (TextView) findViewById(R.id.ytd_traditional);
		tview.setText(formatString(ParseOfx.ytdTrad));
		tview = (TextView) findViewById(R.id.ytd_agencyauto);
		tview.setText(formatString(ParseOfx.ytdAAuto));
		tview = (TextView) findViewById(R.id.ytd_agencymatch);
		tview.setText(formatString(ParseOfx.ytdAMatch));
		tview = (TextView) findViewById(R.id.ytd_traditionalcatchup);
		tview.setText(formatString(ParseOfx.ytdTradCatchup));
		tview = (TextView) findViewById(R.id.ytd_taxexempt);
		tview.setText(formatString(ParseOfx.ytdTaxExempt));
		tview = (TextView) findViewById(R.id.ytd_roth);
		tview.setText(formatString(ParseOfx.ytdRoth));
		tview = (TextView) findViewById(R.id.ytd_rothcatchup);
		tview.setText(formatString(ParseOfx.ytdRothCatchup));

		drawChart();
}
	
	private static String formatString(String st) {
		DecimalFormat numFormat = new DecimalFormat("$#,###,###.##");
		double dval = Double.valueOf(st);
		BigDecimal bd = BigDecimal.valueOf(dval).setScale(2);
		String totval = numFormat.format(bd);
		return totval;
	}

	public void drawChart() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.contchart);

		CategorySeries categorySeries = new CategorySeries("Funds");

		int[] colors = new int[] { Color.RED, Color.GREEN, 
				Color.MAGENTA, Color.YELLOW, Color.CYAN, Color.GRAY,
				Color.BLUE, Color.RED, Color.GREEN, Color.CYAN};

	    //  Only include funds with cont % not 0.0
	    Fund[] fundary = new Fund[] {ParseOfx.L2050, ParseOfx.L2040, ParseOfx.L2030, ParseOfx.L2020, ParseOfx.LIncome, ParseOfx.GFund, ParseOfx.FFund, ParseOfx.CFund, ParseOfx.SFund, ParseOfx.IFund};
	    int numfunds=0;
	    for (int knt=0; knt<10; knt++) {
	    	if (fundary[knt].getContAllocDouble()!=0.0) {
	    		numfunds++;
	    		categorySeries.add(fundary[knt].getName() + " - " + fundary[knt].getContAlloc() + "%", fundary[knt].getContAllocDouble());
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
