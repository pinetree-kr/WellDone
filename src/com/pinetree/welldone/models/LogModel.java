package com.pinetree.welldone.models;

import java.util.Calendar;

import android.util.Log;


public class LogModel extends Model{
	private final float MIN = 60;
	private final float HOUR = 60 * MIN;
	
	private String date;
	private int result;
	
	private long usage;
	private long limit;

	private String apps;
	
	public LogModel(){
		
		usage = 0;
		limit = 0;
		apps = "{}";
		
		/*
		 * -1: none;
		 * 1: ok;
		 * 0: nop;
		 */
		result = -1;
	}
	public void setLog(PlanModel log, String apps){
		usage = log.getUsage();
		limit = log.getTime();
		//Log.i("DebugPrint","usage:"+usage);
		result = log.isSuccess()?1:0;
		this.apps = apps;
	}
	public void setLog(long limit, long usage, String apps){
		this.limit = limit;
		this.usage = usage;
		result = limit>usage?1:0;
		this.apps = apps;
	}
	public void setApps(String apps){
		this.apps = apps;
	}
	public String getApps(){
		return apps;
	}
	public long getUsage(){
		return usage;
	}
	public long getLimit(){
		return limit;
	}
	public int getUsageHour(){
		return (int)(usage/HOUR);
	}
	public int getUsageMin(){
		return (int)((usage%HOUR)/MIN);
	}
	public int getLimitHour(){
		return (int)(limit/HOUR);
	}
	public int getLimitMin(){
		return (int)((int)(limit%HOUR)/MIN);
	}
	public String getUsageFormat(){
		return String.format("%02d:%02d",getUsageHour(), getUsageMin());
	}
	public String getUsageDialogFormat(){
		return String.format("%d시간 %d분",getUsageHour(), getUsageMin());
	}
	public String getLimitFormat(){
		return String.format("%02d:%02d", getLimitHour(), getLimitMin());
	}
	
	public void setDate(String date){
		this.date = date;
	}
	public void setDate(Calendar cal){
		int year = cal.get(cal.YEAR);
		int month = cal.get(cal.MONTH) + 1;
		int day = cal.get(cal.DAY_OF_MONTH);
		this.date = String.format("%04d%02d%02d", year,month,day);
	}
	public String getDate(){
		return date;
	}
	public String getDateFormat(){
		return String.format("%04d.%02d.%02d",getYear(),getMonth(),getDay());
	}
	public int getYear(){
		return Integer.parseInt(date.substring(0, 4));
	}
	public int getMonth(){
		return Integer.parseInt(date.substring(4, 6));
	}
	public int getDay(){
		return Integer.parseInt(date.substring(6, 8));
	}
	public int getDayOfWeek(){
		// 0: sunday ~ 6:saturday
		return getCalendar().get(Calendar.DAY_OF_WEEK) - 1;
	}
	public Calendar getCalendar(){
		Calendar cal = Calendar.getInstance();
		//cal.set(getYear(), getMonth(), getDay());
		cal.set(Calendar.YEAR, getYear());
		cal.set(Calendar.MONTH, getMonth()-1);
		cal.set(Calendar.DAY_OF_MONTH, getDay());
		return cal;
	}
	public void setResult(int result){
		this.result = result;
	}
	public int getResult(){
		return result;
	}
}


