package com.debroq.tspconnect;

import com.debroq.tspconnect.util.SqlUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class DisclaimerActivity extends Activity {
	Context context;
	private final static String TAG = "DisclaimerActivity";
	static boolean forceDisclaimer = false;
	ImageView loadChkbox;
	boolean doShow = true;
	Button closeButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getApplicationContext();
		final SqlUtils sql = SqlUtils.getInstance(context);

		if ((sql.getShowMsg() == true) || (forceDisclaimer == true)) {
			setContentView(R.layout.disclaimer);

			closeButton = (Button) findViewById(R.id.closeButton);
			loadChkbox = (ImageView) findViewById(R.id.loadCheckBox);
			
			if (sql.getShowMsg() == true) {
				doShow = true;
				loadChkbox.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.show));
			} else {
				doShow = false;
				loadChkbox.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.dontshow));
			}

			forceDisclaimer=false;
			
			closeButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					sql.setShowMsg(doShow);
					sql.updateOpts();
					finish();
				}
			});
		} else
			finish();
	}
	
	public void toggle_load(View v) {
		Log.i(TAG, "toggle_saveUserId");
		if (doShow == false) {
			doShow = true;
			loadChkbox.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.show));
		} else {
			doShow = false;
			loadChkbox.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.dontshow));
		}
	}

}
