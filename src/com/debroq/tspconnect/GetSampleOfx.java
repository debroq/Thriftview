package com.debroq.tspconnect;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class GetSampleOfx {
	private final static String TAG = "GetSampleOfx";
	String ofxBuffer;
	
	public GetSampleOfx(Context context) {
		try {
//            InputStream is = context.getAssets().open("SampleOfxResponse.xml");
			BufferedReader in = new BufferedReader(new InputStreamReader
					(context.getAssets().open("SampleOfxResponse.xml")));
            		
            String line;
            StringBuilder buffer = new StringBuilder();
            line = in.readLine();
            buffer.append(line).append('\n');
            in.readLine();
            while ((line = in.readLine()) != null)
            {
//                Log.i(TAG, line);
                buffer.append(line).append('\n');
            } 

            in.close();
            ofxBuffer = new String(buffer);
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}
	
	public String getInput() {
		return ofxBuffer;
	}
}
