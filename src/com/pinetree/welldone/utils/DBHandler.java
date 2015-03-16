package com.pinetree.welldone.utils;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.pinetree.library.dbutils.BaseDBHandler;
import com.pinetree.welldone.models.LogModel;

public class DBHandler extends BaseDBHandler{
	private final int VERSION = 3;
	private DBHelper dbHelper;
	private SQLiteDatabase db;
	private static DBHandler handler;
	
	protected DBHandler(){
		dbHelper = null;
		db = null;
	}
	protected DBHandler(Context context){
		dbHelper = new DBHelper(context, VERSION);
	}
	public static DBHandler getInstance(Context context, boolean readOnly){
		if(handler==null){
			handler = new DBHandler(context);
		}
		handler.open(readOnly);
		return handler;
	}
	
	public void open(boolean readOnly) throws SQLException{
		if(db!=null && db.isOpen()){
			db.close();
		}
		if(readOnly){
			db = dbHelper.getReadableDatabase();
		}else{
			db = dbHelper.getWritableDatabase();
		}
	}
	
	public void close(){
		if(dbHelper!=null && db.isOpen()){
			dbHelper.close();			
		}
	}
	
	public void beginTransaction(){
		db.beginTransaction();
	}
	public void endTransaction(){
		db.endTransaction();
	}
	public void setTransactionSuccessful(){
		db.setTransactionSuccessful();
	}
	
	public long addLog(LogModel newLog){
		ContentValues values = new ContentValues();
		long rv = -1;
		values.put("log_date", newLog.getDate());
		values.put("result", newLog.getResult());
		values.put("usage_time", newLog.getUsage());
		values.put("limit_time", newLog.getLimit());
		values.put("apps", newLog.getApps());
		rv = db.insertWithOnConflict("Logs", null, values, SQLiteDatabase.CONFLICT_REPLACE);
		if(rv<0){
			throw new SQLException("log add Error:"+values);
		}
		return rv;
	}
	public ArrayList<LogModel> getWeekLogs(String startDate, String endDate){
		ArrayList<LogModel> objects = new ArrayList<LogModel>();
		ArrayList<LogModel> logs = getLogs(startDate, endDate);
		//Log.i("DebugPrint",startDate+":"+endDate+"'s logs:"+logs.size());
		for(int logIdx=0, i=0; i<7 ; i++){
			LogModel item = null;
			if(logIdx<logs.size()){
				item = logs.get(logIdx);
				if(item.getDayOfWeek()==i){
					logIdx++;
				}else{
					item = null;
				}
			}
			if(item==null){
				item = new LogModel();
			}
			objects.add(item);
		}
		return objects;
	}
	
	public ArrayList<LogModel> getLogs(String startDate, String endDate){
		ArrayList<LogModel> objects = new ArrayList<LogModel>();
		
		String sql = "SELECT * FROM Logs ";
		sql += "WHERE log_date>=? AND log_date<=? ";
		sql += "ORDER BY log_date ASC ";
		Cursor cursor = db.rawQuery(sql, new String[]{startDate, endDate});
		
		if(cursor != null && cursor.moveToFirst()){
			LogModel log = null;
			String date;
			String apps;
			int result;
			long usage, limit;
			do{
				log = new LogModel();
				date = cursor.getString(cursor.getColumnIndex("log_date"));
				result = cursor.getInt(cursor.getColumnIndex("result"));
				limit = cursor.getLong(cursor.getColumnIndex("limit_time"));
				usage = cursor.getLong(cursor.getColumnIndex("usage_time"));
				apps = cursor.getString(cursor.getColumnIndex("apps"));
				log.setDate(date);
				log.setLog(limit, usage, apps);
				log.setResult(result);
				objects.add(log);
			}while(cursor.moveToNext());
		}
		
		return objects;
	}
	public ArrayList<LogModel> getLogsInYearMonth(String yearMonth){
		ArrayList<LogModel> objects = new ArrayList<LogModel>();
		
		String sql = "SELECT * FROM Logs ";
		sql += "WHERE log_date LIKE '"+yearMonth+"%' ";
		sql += "ORDER BY log_date ASC ";
		Cursor cursor = db.rawQuery(sql, null);
		
		if(cursor != null && cursor.moveToFirst()){
			LogModel log = null;
			String date;
			String apps;
			int result;
			long usage, limit;
			do{
				log = new LogModel();
				date = cursor.getString(cursor.getColumnIndex("log_date"));
				result = cursor.getInt(cursor.getColumnIndex("result"));
				limit = cursor.getLong(cursor.getColumnIndex("limit_time"));
				usage = cursor.getLong(cursor.getColumnIndex("usage_time"));
				apps = cursor.getString(cursor.getColumnIndex("apps"));
				log.setDate(date);
				log.setResult(result);
				log.setLog(limit, usage, apps);
				objects.add(log);
			}while(cursor.moveToNext());
		}
		
		return objects;
	}
	
	public boolean isLogged(String Date){
        boolean ret;
        String sql = "SELECT * FROM Logs WHERE log_date=" + Date;
        Cursor cursor = db.rawQuery(sql, null);

        ret = cursor.getCount() > 0;
        cursor.close();
        return ret;
	}
    public void EraseRows(){
        db.delete("Logs", null, null);
    }
}
