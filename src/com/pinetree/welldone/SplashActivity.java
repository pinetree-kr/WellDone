package com.pinetree.welldone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.pinetree.welldone.fragments.SplashFragment;
import com.pinetree.welldone.models.Model;
import com.pinetree.welldone.receivers.ServiceCaller;

public class SplashActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().setWindowAnimations(android.R.style.Animation_Toast);
		
		// Version Display
        setContentView(R.layout.activity_splash);
        
 		if(savedInstanceState == null){
			if (MainActivity.isRunning())
				switchActivity(new Intent(this, MainActivity.class), true);
			else {
				Fragment fragment = new SplashFragment();
				switchFragment(fragment, true);
			}
		}
	}

	@Override
	public void onDialogClick(DialogFragment dialog, Model data) {
		
	}
}
