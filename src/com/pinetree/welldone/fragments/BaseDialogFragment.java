package com.pinetree.welldone.fragments;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import com.pinetree.utils.FontLoader;
import com.pinetree.utils.ImageLoader;
import com.pinetree.welldone.models.Model;
import com.pinetree.welldone.utils.DeviceInfo;

public abstract class BaseDialogFragment extends DialogFragment {
	public interface DialogFragmentListener{
		public void onDialogClick(DialogFragment dialog, Model data);
	}
	
	protected DialogFragmentListener dialogListener;
	protected DeviceInfo app;
	protected ImageLoader imageLoader;
	protected FontLoader fontLoader;
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		dialogListener = (DialogFragmentListener)activity;
		app = (DeviceInfo)activity.getApplicationContext();
		fontLoader = new FontLoader(activity.getApplicationContext());
		imageLoader = new ImageLoader(getResources(), app.getScaledRate());
	}
	@Override
	public void onDestroyView(){
		super.onDestroyView();
		imageLoader.Clear();
	}
}
