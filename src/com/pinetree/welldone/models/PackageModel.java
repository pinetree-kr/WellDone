package com.pinetree.welldone.models;

import java.util.Comparator;

public class PackageModel{
	public static class RunningApp{
		private final float MIN = 60;
		private final float HOUR = 60 * MIN;
		
		private String pkgName;
		private String appName;
		private int runningTime;
		private int runningCount;
		public RunningApp(){
			pkgName = "";
			appName = "";
			runningTime = 0;
			runningCount = 0;
		}
		public RunningApp(String pkgName
				//, String appName
				, int time, int count){
			this.pkgName = pkgName;
			//this.appName = appName;
			runningTime = time;
			runningCount = count;
		}
		public void setPkgName(String name){
			pkgName = name;
		}
		public String getPkgName(){
			return pkgName;
		}
		public void setAppName(String name){
			appName = name;
		}
		public String getAppName(){
			return appName;
		}
		public void setTime(int time){
			runningTime = time;
		}
		public void updateTime(){
			runningTime++;
		}
		public int getTime(){
			return runningTime;
		}
		public int getHour(){
			return (int)(runningTime/HOUR);
		}
		public int getMin(){
			return (int)((runningTime%HOUR)/MIN);
		}
		public String getTimeFormat(){
			if(getHour()>0){
				if(getMin()>0){
					return String.format("%3dh %2dm", getHour(), getMin());
				}else{
					return String.format("%8dh", getHour());					
				}
			}else{
				return String.format("%8dm", getMin());
			}
		}
		public void setCount(int count){
			runningCount = count;
		}
		public void updateCount(){
			runningCount++;
		}
		public int getCount(){
			return runningCount;
		}
		
		public static class AppUsageDescCompare implements Comparator<RunningApp>{
			@Override
			public int compare(RunningApp lhs, RunningApp rhs) {
				return rhs.getTime() < lhs.getTime() ? -1 :
						rhs.getTime() > lhs.getTime() ? 1 : 0;
			}
		}
		public static class AppCountDescCompare implements Comparator<RunningApp>{
			@Override
			public int compare(RunningApp lhs, RunningApp rhs) {
				return rhs.getCount() < lhs.getCount() ? -1 :
					rhs.getCount() > lhs.getCount() ? 1 : 0;
			}
		}
	}
}


