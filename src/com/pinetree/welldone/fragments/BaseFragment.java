package com.pinetree.welldone.fragments;

import java.util.Locale;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.pinetree.utils.FontLoader;
import com.pinetree.utils.ImageLoader;
import com.pinetree.welldone.R;
import com.pinetree.welldone.BaseActivity.BaseFragmentListener;
import com.pinetree.welldone.interfaces.SwitchActivityInterface;
import com.pinetree.welldone.interfaces.SwitchFragmentInterface;
import com.pinetree.welldone.models.Model;
import com.pinetree.welldone.utils.DeviceInfo;

public abstract class BaseFragment extends Fragment implements BaseFragmentListener{
	protected SwitchActivityInterface saInterface;
	protected SwitchFragmentInterface sfInterface;
	protected CharSequence fragmentTitle;
	protected DeviceInfo app;
	protected ImageLoader imageLoader;
	protected FontLoader fontLoader;
	
	// 액티비티에 Add될때의 이벤트
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		saInterface = (SwitchActivityInterface)activity;
		sfInterface = (SwitchFragmentInterface)activity;
		app = (DeviceInfo)activity.getApplicationContext();
		imageLoader = new ImageLoader(getResources(), app.getScaledRate());
		fontLoader = new FontLoader(activity.getApplicationContext());
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		fragmentTitle = this.getClass().getSimpleName();
	}
	
	@Override
	public void onDetach(){
		super.onDetach();
		
	}	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);	
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}
	@Override
	public void onPause(){
		super.onPause();
	}
	@Override
	public void onStop(){
		super.onStop();
	}
	@Override
	public void onStart(){
		super.onStart();
	}
	@Override
	public void onDestroyView(){
		super.onDestroyView();
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
	@Override
	public void onDialogClick(Model data){
		
	}
}
