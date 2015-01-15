package com.pinetree.welldone.services;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.pinetree.welldone.LockActivity;
import com.pinetree.welldone.R;
import com.pinetree.welldone.models.PlanModel;
import com.pinetree.welldone.models.PromiseModel;
import com.pinetree.welldone.receivers.ServiceCaller;
import com.pinetree.welldone.receivers.ServiceCaller.ServiceCallBR;
import com.pinetree.welldone.utils.DeviceInfo;
import com.pinetree.welldone.views.NotificationToast;


public class AppCounterService extends Service{
	private static int ID = R.layout.notification_layout;
	private int period = 1000;
	private DeviceInfo app;
	private String lastApp = "";
	private Timer timer;
	//private ServiceCallBR receiver;
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
	/*/
	BroadcastReceiver screenOnBR = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	ServiceCaller service = ServiceCaller.getInstance();
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                Log.d("DebugPrint", "ACTION_SCREEN_ON");
                if(!service.isCall(context)){
                	service.startCall(context);
                }
            } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                Log.d("DebugPrint", "ACTION_SCREEN_OFF");
                if(service.isCall(context))
                	service.stopCall(context);
            }
        }
    };
	/**/
	@Override
	public void onCreate(){
		super.onCreate();
		//패키지 삭제의 리시버 추가
		/*/
		receiver = new ServiceCallBR();
		IntentFilter pFilter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
		pFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
		pFilter.addDataScheme("package");
		registerReceiver(receiver, pFilter);
		/**/
		/*/
		Intent screenIntent = new Intent(getApplicationContext(), screenOnBR.getClass());
        PendingIntent screenSender = PendingIntent.getBroadcast(getApplicationContext(), 0, screenIntent, PendingIntent.FLAG_NO_CREATE);
        if (screenSender == null) {
        	Log.i("DebugPrint", "add screen actions");
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            getApplicationContext().registerReceiver(screenOnBR, filter);
        }
        /**/
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
	
	private class CheckTask extends TimerTask{
		ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> services;
		String currentPkgName;
		PlanModel plan;
		PromiseModel promise;
		int startId;
		public CheckTask(int startId){
			this.startId = startId;
		}
		
		@Override
		public void run() {
			services = activityManager.getRunningTasks(1);
			currentPkgName = services.get(0).topActivity.getPackageName();
			// 앱의 사용기록을 갱신한다
			plan = app.getPlan();
			//TODO : 호출
			promise = app.checkPromise();
			if(promise!=null && promise.isLock()){
				//Lock이 안 걸려있으면 락을 건다
				if(!currentPkgName.equals("com.pinetree.welldone")){
					if(!isLockScreenOn()){
						lockIntent = new Intent(getApplicationContext(), LockActivity.class);
					}
					lockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("promise", promise);
					getApplicationContext().startActivity(lockIntent);
				}
				// 사용량 갱신 안함
			}else{
				if(plan.checkAlarm()){
				//else if(plan.getUsage()%10==0){
					//TODO : 여다가 팝업 띄우기
					onToastNotification(plan);
				}

				//화면켜졌을때
				if(((PowerManager)getApplicationContext().getSystemService(Context.POWER_SERVICE)).isScreenOn()){
					plan.updateUsage((int)(period/1000));
					app.updatePlan(plan);
					
					// 노티피케이션 업데이트
					NotificationCompat.Builder builder = getNotification();
					builder
						.setContentText("실시간 앱 체킹중입니다")
						.setSubText("남은 시간 "+plan.getRemainFormat());
					Notification noti =  builder.build();
					NotificationManagerCompat nm = NotificationManagerCompat.from(getApplicationContext());
					nm.notify(startId, noti);
					
					
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
				}
			}
			
		}
	}
	
	private NotificationCompat.Builder getNotification(){
		NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
		return builder.setContentTitle("칭찬바다")
				.setContentText("실시간 앱 체킹중입니다")//\n"+app.getPlan().getUsage())
				.setSmallIcon(R.drawable.welldone_icon);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		//Notification noti = new Notification(R.drawable.welldone_icon, "실시간 앱체킹 중", SystemClock.currentThreadTimeMillis());
		/*/
		NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
		noti = builder.setContentTitle("칭찬바다")
			.setContentText("실시간 앱 체킹중입니다")
			.setSmallIcon(R.drawable.welldone_icon)
			.build();
			/**/
		startForeground(startId, getNotification().build());
		//startForeground(id, notification);
		
		timer = new Timer();
		timer.scheduleAtFixedRate(new CheckTask(startId), 0, period);
		
		return START_STICKY;
		//return START_NOT_STICKY;
	}
	
	@Override
	public void onDestroy(){	
		Log.i("DebugPrint","Service Destoried");
		timer.cancel();
		stopForeground(true);
		/*/
		if(receiver != null)
			unregisterReceiver(receiver);
			/**/
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
}
