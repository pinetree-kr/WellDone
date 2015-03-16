package com.pinetree.welldone.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.pinetree.welldone.R;
import com.pinetree.welldone.models.PromiseModel;
import com.pinetree.welldone.utils.DeviceInfo;

public class LockFragment extends BaseFragment {
	protected ProgressDialog dialog;
	private PromiseModel promise;
//	public LockFragment(PromiseModel promise){
//		this.promise = promise;
//	}
	private void setArguments(PromiseModel prom) {
		this.promise = prom;
	}
	public static LockFragment newInstance(PromiseModel prom) {
		LockFragment lf = new LockFragment();
		//Bundle args = new Bundle();
		//args.putSerializable("promise", prom);
		lf.setArguments(prom);
		return lf;
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
				// LockActivity is of its own Task, so do not try to juggle the tasks.
				// Activity may be finished by AppCounterService with "unlock" intent, but
				// it's not implemented that way now...
				//turnOff(); // deprecated...
				getActivity().finish();
			}else if(v.getId()==R.id.BtnLock){
				// To keep locking, there is no need to finish LockFragment
				// unless a "promise" window has ended.
				//turnOff();
				DeviceInfo app = (DeviceInfo) getActivity().getApplicationContext();
				if (app.checkPromise(true)==null) getActivity().finish();
				else {
					Toast.makeText(getActivity().getApplicationContext(),
							  "화면이 꺼질때까지 기다리거나 "
							+ "화면을 직접 꺼 주세요.", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
/*
	private void turnOff(){
		getActivity().moveTaskToBack(true);
		getActivity().finish();
	}
	private void turnDownScreenBrightness(){
		WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
		
		params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.screenBrightness = 0.1f;
		getActivity().getWindow().setAttributes(params);

	}
*/
}
