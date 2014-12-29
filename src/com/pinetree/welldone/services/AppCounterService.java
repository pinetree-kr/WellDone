package com.pinetree.welldone.services;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.pinetree.welldone.LockActivity;
import com.pinetree.welldone.models.PlanModel;
import com.pinetree.welldone.models.PromiseModel;
import com.pinetree.welldone.receivers.ServiceCaller.ServiceCallBR;
import com.pinetree.welldone.utils.DeviceInfo;
import com.pinetree.welldone.views.NotificationToast;


public class AppCounterService extends Service{
	private int period = 1000;
	private DeviceInfo app;
	private String lastApp = "";
	
	private ServiceCallBR receiver;
	private static Intent lockIntent;
	
	public static void closeLockIntent(boolean close){
		if(close)
			lockIntent = null;
		else
			;
	}
	
	protected boolean isLockScreenOn(){
		if(lockIntent!=null)
			return true;
		else
			return false;
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		
		//패키지 삭제의 리시버 추가
		receiver = new ServiceCallBR();
		IntentFilter pFilter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
		pFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
		/*/
		pFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		/**/
		pFilter.addDataScheme("package");
		registerReceiver(receiver, pFilter);
		
		app = (DeviceInfo)getApplicationContext();
	}
	private class NotifyRunnable implements Runnable{
		private PlanModel plan;
		public NotifyRunnable(PlanModel plan){
			this.plan = plan;
		}
		@Override
		public void run() {
			NotificationToast.makeText(
					getApplicationContext(),
					plan,
					Toast.LENGTH_LONG).show();
		}
	}
	private void onToastNotification(PlanModel plan){
		new Handler(Looper.getMainLooper()).post(new NotifyRunnable(plan));
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		final List<RunningTaskInfo> services = activityManager.getRunningTasks(1);
		String currentPkgName = services.get(0).topActivity.getPackageName();
		// 앱의 사용기록을 갱신한다
		PlanModel plan = app.getPlan();
		//TODO : 호출
		PromiseModel promise = app.checkPromise();
		if(promise!=null && promise.isLock()){
			//Lock이 안 걸려있으면 락을 건다
			if(!currentPkgName.equals("com.pinetree.welldone")){
				if(!isLockScreenOn()){
					lockIntent = new Intent(getApplicationContext(), LockActivity.class);
				}
				lockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("promise", promise);
				this.getApplicationContext().startActivity(lockIntent);
			}

			// 사용량 갱신 안함
			return START_NOT_STICKY;
		}
		else if(plan.checkAlarm()){
		//else if(plan.getUsage()%10==0){
			//TODO : 여다가 팝업 띄우기
			onToastNotification(plan);
		}		
		
		plan.updateUsage((int)(period/1000));
		app.updatePlan(plan);
		
		//Log.i("DebugPrint","usage:"+plan.getUsage());
		
		// 현재 가장 최상위에 실행되고 있는 태스크를 가져온다.
		
		for(RunningTaskInfo rti : services){
			String pkgName = rti.baseActivity.getPackageName();
			
			boolean isNew = false;
			if(!lastApp.equals(pkgName)){
				isNew = true;
				lastApp = pkgName;
			}
			
			// 기록에 무시할 목록
			if(pkgName.equals("com.pinetree.welldone"))
				continue;
			if(pkgName.equals("com.android.systemui"))
				continue;
			if(pkgName.equals("com.android.launcher"))
				continue;
			if(pkgName.contains("launcher"))
				continue;
			
			//앱의 사용목록을 갱신한다
			app.updateRunningApp(pkgName, (int)(period/1000), isNew);
		}
		return START_NOT_STICKY;
	}
	
	@Override
	public void onDestroy(){	
		Log.i("DebugPrint","Service Destoried");
		if(receiver != null)
			unregisterReceiver(receiver);
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
}
