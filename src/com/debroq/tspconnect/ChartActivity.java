package com.debroq.tspconnect;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;

import com.debroq.tspconnect.util.ScalingUtilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ChartActivity extends Activity {
	private final static String TAG = "ChartActivity";
	/** Wanted width of decoded image */
	private int mDstWidth;

	/** Wanted height of decoded image */
	private int mDstHeight;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i(TAG, "ChartActivity created");
		setContentView(R.layout.chart_layout);
		mDstWidth = getResources().getDimensionPixelSize(R.dimen.chart_width);
		mDstHeight = getResources().getDimensionPixelSize(R.dimen.chart_height);

		String mUrl = "http://www.google.com/finance/chart?q=NYSE:AGG&amp;tlf=12&amp;t=24";
		ImageView ffimage = (ImageView) findViewById(R.id.ffimage);
		new ImgDownload(mUrl, ffimage).execute();

		mUrl = "http://www.google.com/finance/chart?q=INDEXSP:.INX&amp;tlf=12&amp;t=24";
		ImageView cfimage = (ImageView) findViewById(R.id.cfimage);
		new ImgDownload(mUrl, cfimage).execute();

		mUrl = "http://www.google.com/finance/chart?q=INDEXDJX:DWCPF&amp;tlf=12&amp;t=24";
		ImageView sfimage = (ImageView) findViewById(R.id.sfimage);
		new ImgDownload(mUrl, sfimage).execute();

		mUrl = "http://www.google.com/finance/chart?q=NYSE:EFA&amp;tlf=12&amp;t=24";
		ImageView ifimage = (ImageView) findViewById(R.id.ifimage);
		new ImgDownload(mUrl, ifimage).execute();

	}

	public void showGoogleFinance(View v) {
		Resources res = getResources();
		Intent visit = new Intent(Intent.ACTION_VIEW, Uri.parse(res.getString(R.string.google_url)));
		startActivity(visit);
	}

	private class ImgDownload extends AsyncTask {
		private String requestUrl;
		private ImageView view;
		private Bitmap pic;
		private Bitmap spic;
		// Display display = getWindowManager().getDefaultDisplay();
		// int swidth = (int) (display.getWidth() * 0.8);

		private ImgDownload(String requestUrl, ImageView view) {
			this.requestUrl = requestUrl;
			this.view = view;
		}

		@Override
		protected Object doInBackground(Object... objects) {
			try {
				URL url = new URL(requestUrl);
				URLConnection conn = url.openConnection();
				pic = BitmapFactory.decodeStream(conn.getInputStream());
				spic = ScalingUtilities.createScaledBitmap(pic, mDstWidth, mDstHeight,
						ScalingUtilities.ScalingLogic.FIT);
			} catch (Exception ex) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object o) {
			view.setImageBitmap(spic);
		}
	}

}
