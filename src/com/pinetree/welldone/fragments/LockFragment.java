package com.pinetree.welldone.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.pinetree.welldone.R;
import com.pinetree.welldone.models.PromiseModel;

public class LockFragment extends BaseFragment {
	protected ProgressDialog dialog;
	private PromiseModel promise;
	public LockFragment(PromiseModel promise){
		this.promise = promise;
	}
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(false);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_lock, container, false);
		ImageView lockBg = (ImageView)view.findViewById(R.id.lock_bg);
		lockBg.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.lock_bg));
		
		ImageView unlock = (ImageView)view.findViewById(R.id.BtnUnlock);
		unlock.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.unlock));
		
		ImageView lock = (ImageView)view.findViewById(R.id.BtnLock);
		lock.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.lock));
		
		unlock.setOnClickListener(new OnLockClickListener());
		lock.setOnClickListener(new OnLockClickListener());
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onStart(){
		super.onStart();
	}
	@Override
	public void onResume(){
		super.onResume();
		
	}
	
	@Override
	public void onDestroyView(){
		super.onDestroyView();
	}
	
	private class OnLockClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(v.getId()==R.id.BtnUnlock){
				promise.setLock(false);
				app.updatePromise(promise);
				turnOff();
			}else if(v.getId()==R.id.BtnLock){
				turnOff();
				//getActivity().finish();
			}
		}
	}
	private void turnOff(){
		getActivity().moveTaskToBack(true);
		getActivity().finish();
	}
	private void turnOffDevice(){
		WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
		
		params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.screenBrightness = 0.1f;
		getActivity().getWindow().setAttributes(params);

	}
}
