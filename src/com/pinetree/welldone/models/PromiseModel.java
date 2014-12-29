package com.pinetree.welldone.models;

import java.util.ArrayList;

import android.util.Log;


public class PromiseModel extends Model{
	private final float MIN = 60;
	private final float HOUR = 60 * MIN;
	private int index;
	private String subject;
	private int day_type;
	// HHMM
	private int startHour;
	private int startMin;
	private int endHour;
	private int endMin;
	private boolean isLock;
	public PromiseModel(){
		index = 0;
		subject = "";
		/*
		 * 0:일
		 * ~6:토
		 * -1:매일
		 */
		day_type = 7;
		startHour = 0;
		startMin = 0;
		endHour = 0;
		endMin = 0;
		isLock = true;
	}
	public PromiseModel(int index){
		this();
		this.index = index;
	}
	public PromiseModel(String subject, int dayType){
		this();
		this.subject = subject;
		day_type = dayType;
	}
	public void setLock(boolean lock){
		isLock = lock;
	}
	public boolean isLock(){
		return isLock;
	}
	public String getAlarmRepeat(){
		return getAlarmRepeats().get(day_type);
	}
	
	public ArrayList<String> getAlarmRepeats(){
		ArrayList<String> objects = new ArrayList<String>();
		objects.add("일요일");
		objects.add("월요일");
		objects.add("화요일");
		objects.add("수요일");
		objects.add("목요일");
		objects.add("금요일");
		objects.add("토요일");
		objects.add("매일");
		return objects;
	}
	public String[] getAlarmRepeatArray(){
		String[] repeat = new String[getAlarmRepeats().size()];
		for(int i=0; i< getAlarmRepeats().size(); i++){
			repeat[i] = getAlarmRepeats().get(i);
		}
		return repeat;
	}
	
	public void setStartTime(int hour, int min){
		startHour = hour;
		startMin = min;
	}
	public void setEndTime(int hour, int min){
		endHour = hour;
		endMin = min;
	}
	public int getStartHour(){
		return startHour;
	}
	public String getStartHourFormat(){
		return String.format("%02d", startHour);
	}
	public int getStartMin(){
		return startMin;
	}
	public String getStartMinFormat(){
		return String.format("%02d", startMin);
	}
	public String getStartTime(){
		return String.format("%02d:%02d", startHour, startMin);
	}
	public int getEndHour(){
		return endHour;
	}
	public String getEndHourFormat(){
		return String.format("%02d", endHour);
	}
	public int getEndMin(){
		return endMin;
	}
	public String getEndMinFormat(){
		return String.format("%02d", endMin);
	}
	public String getEndTime(){
		return String.format("%02d:%02d", endHour, endMin);
	}
	public long getStart(){
		return (int)(startHour*HOUR)+(int)(startMin*MIN);
	}
	public long getEnd(){
		return (int)(endHour*HOUR)+(int)(endMin*MIN);
	}
	public String getDuration(){
		return getStartTime() + " - " + getEndTime();
	}
	
	public void setIndex(int id){
		index = id;
	}
	public int getIndex(){
		return index;
	}
	public void setSubject(String subject){
		this.subject = subject;
	}
	public String getSubject(){
		if(subject.trim().equals("")){
			return "구분";
		}else
			return subject;
	}
	public void setDayType(int type){
		day_type = type;
	}
	public int getDayType(){
		return day_type;
	}
	
	public boolean checkPromise(int hour, int min, int dayOfWeek){
		long checkTime = (int)(hour*HOUR) + (int)(min*MIN);
		if(checkTime>=getStart() && checkTime<=getEnd() && isLock){
			if(day_type==7 || (day_type)==dayOfWeek)
				return true;
		}
		return false;
		
	}
}


