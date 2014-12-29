package com.pinetree.welldone.receivers;


import com.pinetree.welldone.services.AppCounterService;
import com.pinetree.welldone.services.LogService;

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
	private Intent intent;
	private PendingIntent sender;
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
            	context.startService(new Intent(context, LogService.class));
            }
		}
	}
	public static class ServiceCallBR extends BroadcastReceiver {
		@Override
        public void onReceive(Context context, Intent intent) {
			// Background Kill 을 당했을 때에도 ScreenOn Receiver 를 받기위해 이곳에 위치함.
            ServiceCaller.getInstance().registScreenOnBR(context.getApplicationContext());
            
            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
                ServiceCaller.getInstance().startCall(context);
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
            	context.startService(new Intent(context, AppCounterService.class));
            }
        }
	}
 
	public void setInterval(long interval) {
		this.interval = interval;
	}
	public void startCall(Context context){
		am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		intent = new Intent(context, ServiceCallBR.class);
		sender = PendingIntent.getBroadcast(context, 0, intent, 0);
		am.setRepeating(
				AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime(),
				interval,
				sender);
	}
	public void stopCall(Context context){
		am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(context, ServiceCallBR.class);
        sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.cancel(sender);
        sender.cancel();
	}
	public boolean isCall(Context context){
		intent = new Intent(context, ServiceCallBR.class);
        sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
        return sender != null;
	}
	
	BroadcastReceiver screenOnBR = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                Log.d("DebugPrint", "ACTION_SCREEN_ON");
                ServiceCaller.getInstance().startCall(context);

            } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                Log.d("DebugPrint", "ACTION_SCREEN_OFF");
                ServiceCaller.getInstance().stopCall(context);
            }
        }
    };
    
	public void registScreenOnBR(Context context){
		screenIntent = new Intent(context, screenOnBR.getClass());
        screenSender = PendingIntent.getBroadcast(context, 0, screenIntent, PendingIntent.FLAG_NO_CREATE);
        if (screenSender == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            context.registerReceiver(screenOnBR, filter);
        }
	}
	
}