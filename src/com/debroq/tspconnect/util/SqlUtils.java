package com.debroq.tspconnect.util;

import com.debroq.tspconnect.AccountBalanceActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqlUtils extends SQLiteOpenHelper {
	private static final String TAG = "SqlUtils";
	private static SqlUtils instance = null;
	String userID;
	int showMsg;
	public static final String DATABASE_NAME = "TSPOpts.db";
	public static final String OPTS_TABLE_NAME = "TSPOpts";
	public static final String OPTS_USER_ID = "UserID";
	public static final String OPTS_SHOW_MSG = "ShowMsg";
	
	public static SqlUtils getInstance(Context context) {
	      if(instance == null) {
	          instance = new SqlUtils(context);
	       }
	       return instance;

	}
	public SqlUtils(Context context) {
		super(context, DATABASE_NAME , null, 1);
		Log.i(TAG, "SqlUtils");
		getOpts();
//		SQLiteDatabase mydatabase = openOrCreateDatabase("TSPOpts", SQLiteDatabase.MODE_PRIVATE, null);
//		mydatabase.execSQL("CREATE TABLE IF NOT EXISTS TSPUser(UserID VARCHAR, ShowMsg VARCHAR);");
//	//	mydatabase.execSQL("INSERT INTO TutorialsPoint VALUES('admin,'admin');");
//		Cursor resultSet = mydatabase.rawQuery("Select * from TSPUser", null);
//		resultSet.moveToFirst();
//		UserID = resultSet.getString(1);
//		ShowMsg = resultSet.getInt(2);
	}

	public String getUserId() {
		return userID;
	}
	
	public boolean getShowMsg() {
		return showMsg==1;
	}
	
	public void setUserId(String userid) {
		userID = userid;
	}

	public void setShowMsg(boolean show) {
		if (show) showMsg=1; else showMsg=0;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(TAG, "SqlUtils:onCreate");
		db.execSQL("CREATE TABLE IF NOT EXISTS TSPOpts(UserID text, ShowMsg integer);");
		ContentValues contentValues = new ContentValues();
		userID=""; showMsg=1;
		contentValues.put("UserID", userID);
		contentValues.put("ShowMsg", showMsg);
		db.insert("TSPOpts", null, contentValues);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "SqlUtils:onUpgrade");
		db.execSQL("DROP TABLE IF EXISTS TSPOpts");
		onCreate(db);
	}
	
//	public boolean insertOpts(String UserID, int ShowMsg) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		ContentValues contentValues = new ContentValues();
//		contentValues.put("UserID", UserID);
//		contentValues.put("ShowMsg", ShowMsg);
//		db.insert("TSPOpts", null, contentValues);
//		return true;
//	}
	
//	public Cursor getData(){
//		Log.i(TAG, "SqlUtils:getData");
//	    SQLiteDatabase db = this.getReadableDatabase();
//	    Cursor res =  db.rawQuery( "select * from TSPOpts", null );
//	    return res;
//	   }
	
//	public int numberOfRows(){
//	      SQLiteDatabase db = this.getReadableDatabase();
//	      int numRows = (int) DatabaseUtils.queryNumEntries(db, OPTS_TABLE_NAME);
//	      return numRows;
//	   }

	public boolean updateOpts()
	{
		Log.i(TAG, "SqlUtils:updateOpts");
	    SQLiteDatabase db = this.getWritableDatabase();
	    ContentValues contentValues = new ContentValues();
	    contentValues.put("UserID", userID);
	    contentValues.put("ShowMsg", showMsg);
	    db.update("TSPOpts", contentValues, null, null );
	    return true;
	}

//	public boolean updateOpts(String UserID, int ShowMsg)
//	   {
//	      SQLiteDatabase db = this.getWritableDatabase();
//	      ContentValues contentValues = new ContentValues();
//	      contentValues.put("UserID", UserID);
//	      contentValues.put("ShowMsg", ShowMsg);
//	      db.update("TSPOpts", contentValues, null, null );
//	      return true;
//	   }
	
	public Integer deleteOpts() {
		Log.i(TAG, "SqlUtils:deleteOpts");
		SQLiteDatabase db = this.getWritableDatabase();
	      return db.delete("TSPOpts", null, null);
	}
	
	public void getOpts() {
		SQLiteDatabase db = this.getWritableDatabase();
	    Cursor res =  db.rawQuery( "select * from TSPOpts", null );
	    res.moveToFirst();
		userID = res.getString(0);
		showMsg = res.getInt(1);
		Log.i(TAG, "SqlUtils:getOpts UserID=" + userID + " ShowMsg=" + showMsg);
	}
}
