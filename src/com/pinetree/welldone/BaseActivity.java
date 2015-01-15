
package com.pinetree.welldone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.pinetree.utils.FontLoader;
import com.pinetree.utils.ImageLoader;
import com.pinetree.welldone.fragments.BaseDialogFragment.DialogFragmentListener;
import com.pinetree.welldone.handlers.SwitchActivityHandler;
import com.pinetree.welldone.handlers.SwitchFragmentHandler;
import com.pinetree.welldone.interfaces.SwitchActivityInterface;
import com.pinetree.welldone.interfaces.SwitchFragmentInterface;
import com.pinetree.welldone.models.Model;
import com.pinetree.welldone.utils.DeviceInfo;

public abstract class BaseActivity extends FragmentActivity
		implements SwitchActivityInterface, SwitchFragmentInterface, DialogFragmentListener{
	protected DeviceInfo app;
    protected FontLoader fontLoader;
    protected ImageLoader imageLoader;
    
    /*/
    // 강제종료 체크용
    private final Thread.UncaughtExceptionHandler _unCaughtExceptionHandler = new Thread.UncaughtExceptionHandler(){
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			PendingIntent myActivity = PendingIntent.getActivities(
					getApplicationContext(), requestCode, intents, flags);
		}
    };
	/**/
    
    public interface BaseFragmentListener{
		public void onDialogClick(Model data);
	}
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setWindowAnimations(android.R.style.Animation);
        app = (DeviceInfo)this.getApplicationContext();
        fontLoader = new FontLoader(getApplicationContext());
        imageLoader = new ImageLoader(getResources(), app.getScaledRate());
    }
	private void Clear(){
		app = null;
		fontLoader.Clear();
		imageLoader.Clear();
	}
	
	@Override
	protected void onDestroy(){
		//stopService(new Intent(this, AppCounterService.class));
		super.onDestroy();
		Clear();
	}
    
	@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
	
	@Override
	protected void onResume(){
		super.onResume();
	}
	
	@Override
	protected void onStart(){
		super.onStart();
	}
	@Override
	protected void onStop(){
		super.onStop();
	}
	
	@Override
	public void reloadFragment(){
		Fragment fragment = this.getSupportFragmentManager().findFragmentById(R.id.base_fragment);
		FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
		transaction.detach(fragment);
		transaction.attach(fragment);
		transaction.commit();
	}
	
	@Override
	public void switchFragment(Fragment fragment, boolean close) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		
		if(close){
			transaction.replace(R.id.base_fragment, fragment);
		}else{
			transaction.replace(R.id.base_fragment, fragment);
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}

	@Override
	public void switchFragment(Fragment fragment, int time, boolean close) {
		Handler handler = new Handler();
		handler.postDelayed(new SwitchFragmentHandler(this, fragment, close),  time);
	}

	@Override
	public void switchActivity(Intent intent, boolean close) {
		startActivity(intent);
		
		if(close)
			this.finish();
	}

	@Override
	public void switchActivity(Intent intent, int time, boolean close) {
		Handler handler = new Handler();
		handler.postDelayed(new SwitchActivityHandler(this, intent, close),  time);
	}
}
