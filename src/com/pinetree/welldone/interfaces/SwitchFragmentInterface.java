package com.pinetree.welldone.interfaces;

import android.support.v4.app.Fragment;

public interface SwitchFragmentInterface {
	public void reloadFragment();
	public void switchFragment(Fragment fragment, boolean close);
	public void switchFragment(Fragment fragment, int time, boolean close);
}
