package com.pinetree.welldone.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.pinetree.objects.RemovableJSONArray;
import com.pinetree.welldone.models.LogModel;
import com.pinetree.welldone.models.PackageModel.RunningApp;
import com.pinetree.welldone.models.PackageModel.RunningApp.AppUsageDescCompare;
import com.pinetree.welldone.models.PlanModel;
import com.pinetree.welldone.models.ProfileModel;
import com.pinetree.welldone.models.PromiseModel;

public class DeviceInfo extends Application{
	private final String PACKAGE_NAME = "WellDone";
	private final float WIDTH = 360;
	private final float HEIGHT = 640;
	private SharedPreferences sharedPref;
	
	private float deviceWidth;
	private float deviceHeight;
	
	private float scaledRate;
	private float scaledDensity;
	private int rotation;
	
	public String getVersion(){
		String version = "";
		try {
			PackageInfo i = this.getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
			version = i.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}
	
	public double getWidth(){
		return (double)deviceWidth;
	}
	public double getHeight(){
		return (double)deviceHeight;
	}
	public float getScaledRate(){
		return scaledRate;
	}
	@Override
	public void onCreate(){
		super.onCreate();
		getSharedPref();
		getScreenInfo();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
	}
	
	private void getSharedPref(){
		sharedPref = getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
	}
	
	/*
	 * 목표시간과 관련된 정보 업데이트/가져오기
	 */
	public void updatePlan(PlanModel plan){
		JSONObject object = getJSONPlan();
		try {
			object.put("time", plan.getTime());
			object.put("usage", plan.getUsage());
			object.put("alarm_type", plan.getAlarmType());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		SharedPreferences.Editor sharedEditor = sharedPref.edit();
		sharedEditor.putString("plan", object.toString());
		sharedEditor.commit();
	}
	public JSONObject getJSONPlan(){
		JSONObject json = null;
		try {
			json = new JSONObject(sharedPref.getString("plan", "{}"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	public PlanModel getPlan(){
		PlanModel plan = new PlanModel();
		JSONObject object = getJSONPlan();
		try {
			long time = 0, usage = 0;
			int alarmType = 0;
			if(object.has("time"))
				time = object.getLong("time");
			if(object.has("usage"))
				usage = object.getLong("usage");
			if(object.has("alarm_type"))
				alarmType = object.getInt("alarm_type");
			
			plan.setTime(time);
			plan.setUsage(usage);
			plan.setAlarmType(alarmType);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return plan;
	}
	
	// TODO :여기서 약속 체킹
	public PromiseModel checkPromise(){
		ArrayList<PromiseModel> promises = getPromises();
		PromiseModel promise = null;
		Calendar cal = Calendar.getInstance();
		//요일체크
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		
		//Log.i("DebugPrint","thisday:"+dayOfWeek);
		
		for(int i=0; i<promises.size(); i++){
			promise = promises.get(i);
			if(promise.checkPromise(hour, min, dayOfWeek)){
				return promise;
			}
		}
		return null;
	}
	
	/*
	 * 자신과의 약속과 관련된 정보 업데이트/가져오기
	 */
	public void updatePromise(PromiseModel promise){
		updatePromise(promise, false);
	}
	public void allLockPromise(){
		JSONArray promises = getJSONPromises();
		JSONObject object = null;
		for(int i=0; i<promises.length(); i++){
			try {
				object = promises.getJSONObject(i);
				object.put("islock", true);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		SharedPreferences.Editor sharedEditor = sharedPref.edit();
		sharedEditor.putString("promises", promises.toString());
		sharedEditor.commit();
	}
	public void updatePromise(PromiseModel promise, boolean remove){
		JSONArray promises = getJSONPromises();
		if(remove){
			promises = RemovableJSONArray.removeJsonArray(promises, promise.getIndex());
		}else{
			try {
				int index = promise.getIndex();
				//갱신
				if(!promises.isNull(index)){
					JSONObject object = promises.getJSONObject(promise.getIndex());
					Log.d(PACKAGE_NAME, "update promise");
					object.put("subject", promise.getSubject());
					object.put("day_type", promise.getDayType());
					object.put("start_hour", promise.getStartHour());
					object.put("start_min", promise.getStartMin());
					object.put("end_hour", promise.getEndHour());
					object.put("end_min", promise.getEndMin());
					object.put("islock", promise.isLock());
				}
				//새로운것
				else{
					Log.d(PACKAGE_NAME, "new promise");
					JSONObject object = new JSONObject();
					object.put("subject", promise.getSubject());
					object.put("day_type", promise.getDayType());
					object.put("start_hour", promise.getStartHour());
					object.put("start_min", promise.getStartMin());
					object.put("end_hour", promise.getEndHour());
					object.put("end_min", promise.getEndMin());
					object.put("islock", promise.isLock());
					promises.put(object);
				}
			
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		SharedPreferences.Editor sharedEditor = sharedPref.edit();
		sharedEditor.putString("promises", promises.toString());
		sharedEditor.commit();
	}
	public ArrayList<PromiseModel> getPromises(){
		JSONArray array = getJSONPromises();
		return getPromises(array.length());
	}
	public ArrayList<PromiseModel> getPromises(int n){
		ArrayList<PromiseModel> objects = new ArrayList<PromiseModel>();
		JSONArray array = getJSONPromises();
		for(int i=0; i<array.length(); i++){
			try {
				JSONObject object = array.getJSONObject(i);
				PromiseModel promise = new PromiseModel();
				promise.setIndex(i);
				
				String subject = "";
				int dayType = 0, startHour = 0, startMin = 0, endHour = 0, endMin = 0;
				boolean isLock = true;
				if(object.has("subject"))
					subject = object.getString("subject");
				if(object.has("day_type"))
					dayType = object.getInt("day_type");
				if(object.has("start_hour"))
					startHour = object.getInt("start_hour");
				if(object.has("start_min"))
					startMin = object.getInt("start_min");
				if(object.has("end_hour"))
					endHour = object.getInt("end_hour");
				if(object.has("end_min"))
					endMin = object.getInt("end_min");
				if(object.has("islock"))
					isLock = object.getBoolean("islock");
					
				promise.setSubject(subject);
				promise.setDayType(dayType);
				promise.setStartTime(startHour, startMin);
				promise.setEndTime(endHour, endMin);
				promise.setLock(isLock);
				objects.add(promise);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		for(int i=objects.size()-1; i>=n; i--){
			objects.remove(i);
		}
		return objects;
	}
	public JSONArray getJSONPromises(){
		JSONArray json = null;
		try {
			json = new JSONArray(sharedPref.getString("promises", "[]"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	/*
	 * 어플 사용량과 관련된 정보 업데이트/가져오기
	 */
	public ArrayList<RunningApp> getUsedApps(int size){
		ArrayList<RunningApp> apps = new ArrayList<RunningApp>();
		try {
			JSONObject json = getJSONUsedApps();
			Iterator itrApps = json.keys();
			while(itrApps.hasNext()){
				String key = (String)itrApps.next();
				JSONObject app = json.getJSONObject(key);
				apps.add(
						new RunningApp(
								key,
								app.getInt("time"),
								app.getInt("count"))
						);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		//TODO : 앱순서
		Collections.sort(apps, new AppUsageDescCompare());
		//Collections.sort(apps, new AppCountDescCompare());
		for(int i=apps.size()-1; i>=size; i--){
			apps.remove(i);
		}
		return apps;
	}
	public void initPlan(){
		PlanModel plan = getPlan();
		plan.setUsage(0);
		updatePlan(plan);
	}
	public void initUsedApps(){
		SharedPreferences.Editor sharedEditor = sharedPref.edit();
		sharedEditor.putString("usedApps", "{}");
		sharedEditor.commit();
	}
	public JSONObject getJSONUsedApps(){
		JSONObject json = null;
		try {
			json = new JSONObject(sharedPref.getString("usedApps", "{}"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public void updateRunningApp(
			String pkgName,
			int period, boolean countup){
		JSONObject apps = getJSONUsedApps();
		try {
			JSONObject app = null;
			if(!apps.has(pkgName)){
				app = new JSONObject();
				app.put("time", period);
				app.put("count", 1);
				apps.put(pkgName, app);
			}else{
				app = apps.getJSONObject(pkgName);
				int time = app.getInt("time");
				int count = app.getInt("count");
				//Log.d(PACKAGE_NAME,pkgName+"["+time+"]+Time:"+period);
				app.put("time", time+period);
				app.put("count", countup?count+1:count);
				apps.put(pkgName, app);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (NullPointerException e){
			e.printStackTrace();
		}

		SharedPreferences.Editor sharedEditor = sharedPref.edit();
		sharedEditor.putString("usedApps", apps.toString());
		sharedEditor.commit();
	}
		
	/*
	 * 현재 날짜 정보
	 */
	public String getToday(){
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy.MM.dd",
				java.util.Locale.getDefault());
		Date date = new Date();
		return format.format(date);
	}
	/*
	 * 이번달 정보
	 */
	public String getYearMonthFormat(int month_offset){
		Calendar cal = getYearMonth(month_offset);
		int year = cal.get(cal.YEAR);
		int month = cal.get(cal.MONTH)+1;
		
		return String.format("%04d년 %02d월", year,month);
	}
	public Calendar getYearMonth(int month_offset){
		Calendar cal = Calendar.getInstance();
		cal.set(cal.DATE, 1);
		cal.add(cal.MONTH, month_offset);
		
		return cal;
	}
	
	/*
	 * Log 저장
	 */
	public void addLog(){
		DBHandler handler = DBHandler.getInstance(getApplicationContext(), false);
		LogModel newLog = new LogModel();
		Calendar cal = Calendar.getInstance();
		cal.add(cal.DATE, -1);
		newLog.setDate(cal);
		newLog.setLog(getPlan(), getJSONUsedApps().toString());
		if(handler.addLog(newLog)>0){
			//usedApp/Plan사용량 초기화
			//Log.d("DebugPrint", "addLogs");
			initPlan();
			initUsedApps();
		}
	}
	
	/*
	 * 자신의 프로필 사진과 알림말 설정 업데이트/가져오기
	 */
	public void updateProfile(ProfileModel profile){
		JSONObject object = getJSONProfile();
		try {
			object.put("filepath", profile.getFilePath());
			object.put("message", profile.getMessage());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		SharedPreferences.Editor sharedEditor = sharedPref.edit();
		sharedEditor.putString("profile", object.toString());
		sharedEditor.commit();
	}
	public ProfileModel getProfile(){
		ProfileModel profile = new ProfileModel();
		JSONObject object = getJSONProfile();
		try {
			String filePath = "", message = "";
			if(object.has("filepath"))
				filePath = object.getString("filepath");
			profile.setFilePath(filePath);
			if(object.has("message")){
				message = object.getString("message");
			}
			profile.setMessage(message);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return profile;
	}
	public JSONObject getJSONProfile(){
		JSONObject json = null;
		try {
			json = new JSONObject(sharedPref.getString("profile", "{}"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public float getScaledDensity(){
		return scaledDensity;
	}
	
	
	private void getScreenInfo(){
		WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		rotation = display.getRotation();
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(displayMetrics);

		//Log.d(PACKAGE_NAME,"density:"+displayMetrics.density);
		//Log.d(PACKAGE_NAME,"scaledDensity:"+displayMetrics.scaledDensity);
		
		scaledDensity = displayMetrics.scaledDensity;
		deviceWidth = displayMetrics.widthPixels;
		deviceHeight = displayMetrics.heightPixels;
		
		if(WIDTH*scaledDensity>deviceWidth){
			scaledRate = deviceWidth/(WIDTH*scaledDensity);
			//Log.d(PACKAGE_NAME,"rate"+scaledRate);
		}else{
			scaledRate = 1;
		}
	}
}
