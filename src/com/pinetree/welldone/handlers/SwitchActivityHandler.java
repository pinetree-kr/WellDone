package com.pinetree.welldone.handlers;

import android.content.Intent;

import com.pinetree.welldone.interfaces.SwitchActivityInterface;

// observer pattern을 통한 call back
public class SwitchActivityHandler implements Runnable{
	protected SwitchActivityInterface saInterface;
	//protected Class<?> activity;
	protected Intent intent;
	protected boolean close;
	
	/*
	public SwitchActivityHandler(SwitchActivityInterface saInterface, Class<?> activity, boolean close){
		this.saInterface = saInterface;
		this.activity = activity;
		this.close = close;
	}
	*/
	public SwitchActivityHandler(SwitchActivityInterface saInterface, Intent intent, boolean close){
		this.saInterface = saInterface;
		this.intent = intent;
		this.close = close;
	}
	
	@Override
	public void run(){
		//saInterface.switchActivity(activity, close);
		saInterface.switchActivity(intent, close);
	}
}
