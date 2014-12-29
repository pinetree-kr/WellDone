package com.pinetree.welldone.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.pinetree.welldone.utils.DeviceInfo;

public class LogService extends Service{
	private DeviceInfo app;
	
	@Override
	public void onCreate(){
		super.onCreate();
		
		app = (DeviceInfo)getApplicationContext();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		super.onStartCommand(intent, flags, startId);
		// 로그추가
		app.addLog();
		// 약속시간을 전부 락 걸어둔다
		app.allLockPromise();
		
		return START_NOT_STICKY;
	}
	
	@Override
	public void onDestroy(){	
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
}
