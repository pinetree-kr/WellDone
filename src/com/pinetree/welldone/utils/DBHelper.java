package com.pinetree.welldone.utils;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.pinetree.dbutils.BaseDBHelper;

public class DBHelper extends BaseDBHelper{
	protected static String DB_NAME = "WellDone";
	protected static int DB_VERSION = 1;
	protected static String PACKAGE_NAME;
	public DBHelper(Context context){
		this(context, DB_NAME, null, DB_VERSION);
	}
	public DBHelper(Context context, int db_version){
		this(context, DB_NAME, null, db_version);
	}
	
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		PACKAGE_NAME = context.getPackageName();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(PACKAGE_NAME,"DB Create");
		// 동기화를 위한 테이블
		String createResultTbl =
				"CREATE TABLE Logs (" +
					"result_no INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
					"log_date TEXT NOT NULL, " +
					"result INTEGER NOT NULL, " +
					"limit_time INTEGER NOT NULL, " +
					"usage_time INTEGER NOT NULL, " +
					// 앱정보 (JSON)
					"apps TEXT NOT NULL, " +
					"UNIQUE(log_date) ON CONFLICT IGNORE );";
		
		// Create Table
		db.execSQL(createResultTbl);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(PACKAGE_NAME,"DB Upgrade");
		db.execSQL("DROP TABLE IF EXISTS Logs");
		
		onCreate(db);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db){
		super.onOpen(db);
		Log.d(PACKAGE_NAME,"DB Open");
		
	}

}
