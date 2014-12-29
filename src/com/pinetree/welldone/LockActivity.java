package com.pinetree.welldone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.WindowManager;

import com.pinetree.welldone.fragments.LockFragment;
import com.pinetree.welldone.models.Model;
import com.pinetree.welldone.models.PromiseModel;

public class LockActivity extends BaseActivity {
	protected MyBroadcastReceiver myBroadcastReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/*/
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		/**/
		setContentView(R.layout.activity_splash);
		myBroadcastReceiver = new MyBroadcastReceiver();
	}
	@Override
	protected void onResume(){
		super.onResume();
		PromiseModel promise = (PromiseModel)getIntent().getSerializableExtra("promise");
		
		Fragment fragment = new LockFragment(promise);
		switchFragment(fragment, true);

		// 화면꺼짐의 브로드캐스트 등록
		registerReceiver(myBroadcastReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		
		// 제거시에 브로드캐스트 삭제
		if(myBroadcastReceiver!=null)
			this.unregisterReceiver(myBroadcastReceiver);
	}
	
	@Override
	protected void onNewIntent(Intent newIntent){
		super.onNewIntent(newIntent);
		this.setIntent(newIntent);
	}
	
	// 화면 꺼짐의 브로드캐스트 등록
	protected class MyBroadcastReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
				// 화면이 꺼지면 락스크린을 끈다
				finish();
			}
		}
	}

	@Override
	public void onDialogClick(DialogFragment dialog, Model data) {
		// TODO Auto-generated method stub
		
	}
}
