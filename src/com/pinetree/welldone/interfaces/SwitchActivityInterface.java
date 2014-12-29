package com.pinetree.welldone.interfaces;

import android.content.Intent;

public interface SwitchActivityInterface {
	//public void switchActivity(Class<?> name, boolean close);
	//public void switchActivity(Class<?> name, int time, boolean close);	
	public void switchActivity(Intent intent, boolean close);
	public void switchActivity(Intent intent, int time, boolean close);	
}
