package com.pinetree.welldone.models;

import java.util.ArrayList;

public class PlanModel extends Model{
	private final float MIN = 60;
	private final float HOUR = 60 * MIN;
	private long time;
	private long usage;
	private int alarmType;
	//private int day_type;
	
	public PlanModel(){
		time = 0;
		usage = 0;
		/*
		 * 0:종료
		 * 1:1시간꼴
		 * 2:30분전
		 * 3:정각 <= 이건 삭제
		 */
		alarmType = 0;
	}
	public PlanModel(int hour, int min, int alarmType){
		setTime(hour,min);
		setAlarmType(alarmType);
	}
	
	public ArrayList<String> getAlarmTypes(){
		ArrayList<String> objects = new ArrayList<String>();
		objects.add("알람 없음");
		objects.add("한 시간에 한 번");
		objects.add("끝나기 전 30분");
		//objects.add("정각에");
		return objects;
	}
	public String[] getAlarmTypeArray(){
		String[] alarm = new String[getAlarmTypes().size()];
		for(int i=0; i<alarm.length; i++){
			alarm[i] = getAlarmTypes().get(i);
		}
		return alarm;
	}
	/**/
	public boolean isValid(){
		if(time>0 && time>=usage){
			return true;
		}else return false;
	}
	public boolean checkAlarm(){
		if(isValid()){
			/*
			 * 1:1시간꼴
			 * 2:30분전
			 */
			switch(alarmType){
			case 1:
				if((usage%HOUR) == 0){
					return true;
				}
				break;
			case 2:
				if((time-usage) == HOUR/2){
					return true;					
				}
				break;
			}
		}
		return false;
	}
	/**/
	public void setUsage(long usage){
		this.usage = usage;
	}
	public void updateUsage(int period){
		usage += period;
	}
	public long getUsage(){
		return usage;
	}
	public int getUsageHour(){
		return (int)(usage/HOUR);
	}
	public int getUsageMin(){
		return (int)((usage%HOUR)/MIN);
	}
	public void setTime(int hour, int min){
		time = (int)(hour * HOUR + min * MIN);
	}
	public void setTime(long sec){
		time = sec;
	}
	public long getTime(){
		return time;
	}
	public int getHour(){
		return (int)(time/HOUR);
	}
	public String getHourFormat(){
		return String.format("%02d", getHour());
	}
	public int getMin(){
		return (int)((int)(time%HOUR)/MIN);
	}
	public String getMinFormat(){
		return String.format("%02d", getMin());
	}
	public String getTimeFormat(){
		return String.format("%02d시간 %02d분", getHour(), getMin());
	}
	public void setAlarmType(int type){
		this.alarmType = type;
	}
	public int getAlarmType(){
		return alarmType;
	}
	public String getUsageFormat(){
		return String.format("%02d:%02d",getUsageHour(), getUsageMin());
	}
	public long getRemain(){
		if(time>usage){
			return time - usage;
		}else
			return 0;
	}
	public boolean isSuccess(){
		return time>usage?true:false;
	}
	public int getRemainHour(){
		return (int)(getRemain()/HOUR);
	}
	public int getRemainMin(){
		return (int)((getRemain()%HOUR)/MIN);
	}
	public String getRemainFormat(){
		return String.format("%02d:%02d", getRemainHour(), getRemainMin());
	}
	public int getUsageRate(){
		if(isValid()){
			return (int)(((double)usage/time)*100);
		}else
			return 100;
	}
}


