package com.pinetree.welldone.receivers;


import java.util.Calendar;
import java.util.List;

import com.pinetree.welldone.services.AppCounterService;
import com.pinetree.welldone.services.LogService;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.util.Log;

public class ServiceCaller {
	private static ServiceCaller instance;
	private AlarmManager am;
	private Intent screenIntent;
	private PendingIntent screenSender;

	// 1초마다 반복
	private long interval = 1000;
 
	private ServiceCaller() {}
	public static synchronized ServiceCaller getInstance() {
		if (instance == null) {
			instance = new ServiceCaller();
		}
		return instance;
	}
	
	public static class DateChangedBR extends BroadcastReceiver{
		@Override
        public void onReceive(Context context, Intent intent) {
            if(Intent.ACTION_DATE_CHANGED.equals(intent.getAction())){
            	//context.startService(new Intent(context, LogService.class));
            }
		}
	}
	public static class LogServiceCallBR extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("DebugPrint","logservice on");
			context.startService(new Intent(context, LogService.class));
		}
	}
	public static class ServiceCallBR extends BroadcastReceiver {
		@Override
        public void onReceive(Context context, Intent intent) {
			// Background Kill 을 당했을 때에도 ScreenOn Receiver 를 받기위해 이곳에 위치함.
            //ServiceCaller.getInstance().registScreenOnBR(context.getApplicationContext());
            
            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            	Log.i("DebugPrint", "rebooted");
                ServiceCaller.getInstance().startCall(context);
                ServiceCaller.getInstance().startAlarm(context);
            }
            else if(Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())){
            	Log.i("DebugPrint", "app removed");
            	//ServiceCaller.getInstance().stopCall(context);
            }
            else if(Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())){
            	Log.i("DebugPrint", "app replaced");
            	//ServiceCaller.getInstance().stopCall(context);
            }
            else {
            	Log.i("DebugPrint", "run");
            	context.startService(new Intent(context, AppCounterService.class));
            }
        }
	}
 
	public void setInterval(long interval) {
		this.interval = interval;
	}
	public void startAlarm(Context context){
		am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		//날짜바뀌는 브로드캐스트
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		
		Intent intent = new Intent(context, LogServiceCallBR.class);
		PendingIntent sender = PendingIntent.getBroadcast(
				context, 0, intent, 0);
		
		am.setRepeating(
				AlarmManager.RTC_WAKEUP,
				cal.getTimeInMillis(),
				//1000*60*2,
				1000*60*60*24,
				sender);
		//Log.i("DebugPrint","testCal");
	}
	public void startCall(Context context){
		context.startService(new Intent(context, AppCounterService.class));
		/*/
		am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, ServiceCallBR.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
		am.setRepeating(
				AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime(),
				interval,
				sender);
		/**/
		/*/
		am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, ServiceCallBR.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
		am.setRepeating(
				AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime(),
				interval,
				sender);
		/**/
	}
	public void stopCall(Context context){
		/*/
		am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ServiceCallBR.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.cancel(sender);
        sender.cancel();
        /**/
		context.stopService(new Intent(context, AppCounterService.class));
	}
	public void stopAlarm(Context context){
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, LogServiceCallBR.class);
        PendingIntent sender = PendingIntent.getBroadcast(
				context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(sender);
        sender.cancel();
	}
	public boolean isCall(Context context){
		/*/
		Intent intent = new Intent(context, ServiceCallBR.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
        return sender != null;
        /**/
		String strPackage = "";
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> services = am.getRunningServices(99);//.getRunningAppProcesses();
		        
		//프로세서 전체를 반복
		for(RunningServiceInfo service : services)
		{
		        if(service.foreground)
		        {
		             strPackage = service.process; //package이름과 동일함.
		             //if(strPackage.equals(""))
		             //return true;
		             if(strPackage.equals("com.pinetree.welldone")){
		            	 Log.i("DebugPrint","services:"+strPackage);
		            	 return true;
		             }
		         }
		}
		return false;
	}
	/*/
	BroadcastReceiver screenOnBR = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	ServiceCaller service = ServiceCaller.getInstance();
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                Log.d("DebugPrint", "ACTION_SCREEN_ON");
                if(!service.isCall(context))
                	service.startCall(context);

            } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                Log.d("DebugPrint", "ACTION_SCREEN_OFF");
                if(service.isCall(context))
                	service.stopCall(context);
            }
        }
    };
    
	public void registScreenOnBR(Context context){
		screenIntent = new Intent(context, screenOnBR.getClass());
        screenSender = PendingIntent.getBroadcast(context, 0, screenIntent, PendingIntent.FLAG_NO_CREATE);
        if (screenSender == null) {
        	Log.i("DebugPrint", "add screen actions");
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            context.registerReceiver(screenOnBR, filter);
        }
	}
	/**/
}