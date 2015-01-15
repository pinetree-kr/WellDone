package com.pinetree.welldone.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pinetree.welldone.R;
import com.pinetree.welldone.models.Model;
import com.pinetree.welldone.models.PlanModel;
import com.pinetree.welldone.models.ProfileModel;
import com.pinetree.welldone.models.PromiseModel;
import com.pinetree.welldone.receivers.ServiceCaller;

public class PlanFragment extends BaseFragment{
	protected ImageView btnPromiseAdd;
	protected TextView planTime;
	protected LinearLayout promiseList;
	
	protected PromiseModel selectedPromise;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(false);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_plan, container, false);
		
		setComponents(view);
		return view;
	}
	
	private void setComponents(View view){
		ImageView dateBg = (ImageView)view.findViewById(R.id.dateBg);
		dateBg.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.bar));
		
		TextView date = (TextView)view.findViewById(R.id.date);
		fontLoader.setTextViewTypeFace(
				date, app.getToday(), R.string.NanumGothicB, (float)10);
		
		ImageView statusBg = (ImageView)view.findViewById(R.id.bgPlan);
		statusBg.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.plan));
		
		ImageView btnPlanMod = (ImageView)view.findViewById(R.id.btnPlanMod);
		btnPlanMod.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.plan_mod));
		btnPlanMod.setTag(app.getPlan());
		btnPlanMod.setOnClickListener(new OnBtnClickListener());
		
		planTime = (TextView)view.findViewById(R.id.PlanTime);
		
		//등대
		ImageView lighthouse = (ImageView)view.findViewById(R.id.lighthouse);
		lighthouse.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.lighthouse_3));
		
		//물
		ImageView water = (ImageView)view.findViewById(R.id.water);
		water.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.water1_3));
		
		//등대
		ImageView whale = (ImageView)view.findViewById(R.id.whale);
		whale.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.whale_3));
		
		
		ImageView titlePromise = (ImageView)view.findViewById(R.id.titlePromise);
		titlePromise.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.promise_title));
		
		ImageView bgPromiseAdd = (ImageView)view.findViewById(R.id.bgPromiseAdd);
		bgPromiseAdd.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.promise_bg));

		btnPromiseAdd = (ImageView)view.findViewById(R.id.btnPromiseAdd);
		btnPromiseAdd.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.promise_add));
		//btnPromiseAdd.setTag(new PromiseModel(app.getPromises(5).size()));
		btnPromiseAdd.setOnClickListener(new OnBtnClickListener());
		
		promiseList = (LinearLayout)view.findViewById(R.id.PromiseList);
		
	}
	private void updateView(){
		PlanModel plan = app.getPlan();
		updatePlan(plan);
		updatePromiseList();
	}
	
	protected void updatePlan(PlanModel plan){
		fontLoader.setTextViewTextSize(planTime, plan.getTimeFormat(), (float)20.0);
	}
	
	protected void updatePromise(PromiseModel promise){
		LayoutInflater li = LayoutInflater.from(getActivity().getApplicationContext());
		
		FrameLayout row = (FrameLayout)li.inflate(R.layout.promise_row, null);
		
		ImageView rowBg = (ImageView)row.findViewById(R.id.bgPromise);
		rowBg.setImageDrawable(imageLoader.getResizedDrawable(R.drawable.promise_bg));
		
		ImageView bgSubject = (ImageView)row.findViewById(R.id.bgSubject);
		bgSubject.setImageDrawable(imageLoader.getResizedDrawable(R.drawable.subject_bg));
		ImageView bgRepeat = (ImageView)row.findViewById(R.id.bgRepeat);
		bgRepeat.setImageDrawable(imageLoader.getResizedDrawable(R.drawable.repeat_bg));
		ImageView bgDuration = (ImageView)row.findViewById(R.id.bgDuration);
		bgDuration.setImageDrawable(imageLoader.getResizedDrawable(R.drawable.duration_bg));
		
		TextView subject = (TextView)row.findViewById(R.id.Subject);
		TextView repeat = (TextView)row.findViewById(R.id.Repeat);
		TextView duartion = (TextView)row.findViewById(R.id.Duration);
		
		fontLoader.setTextViewTextSize(subject, promise.getSubject(), (float)6.0);
		fontLoader.setTextViewTextSize(repeat, promise.getAlarmRepeat(), (float)6.0);
		fontLoader.setTextViewTextSize(duartion, promise.getDuration(), (float)6.0);
		
		row.setTag(promise);
		registerForContextMenu(row);
		row.setOnClickListener(new OnBtnClickListener());
		promiseList.addView(row);
		
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu,
			View v, ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);
		selectedPromise = (PromiseModel)v.getTag();
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.promise_menu, menu);
	}
	@Override
	public boolean onContextItemSelected(MenuItem item){
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		
		switch(item.getItemId()){
		case R.id.modify:
			this.openDialog(selectedPromise);
			return true;
		case R.id.delete:
			this.removePromise(selectedPromise);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
	
	protected void updatePromiseList(){
		promiseList.removeAllViews();
		for(PromiseModel promise : app.getPromises(5)){
			updatePromise(promise);
		}
		//Log.i("DebugPrint","setTag:"+app.getPromises(5).size());
		btnPromiseAdd.setTag(new PromiseModel(app.getPromises(5).size()));
	}
	protected void removePromise(PromiseModel promise){
		app.updatePromise(promise, true);
		updatePromiseList();
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onStart(){
		super.onStart();
		//ad.start();
	}
	@Override
	public void onResume(){
		super.onResume();
		updateView();
	}
	
	@Override
	public void onDestroyView(){
		super.onDestroyView();
	}
	
	@Override
	public void onDialogClick(Model data){
		if(data.getClass().equals(PlanModel.class)){
			PlanModel plan = (PlanModel)data;
			app.updatePlan(plan);
			//app.initUsedApps();
			updatePlan(plan);
			
			ServiceCaller service = ServiceCaller.getInstance();
			if(plan.getTime()>0){
				service.stopCall(getActivity().getApplicationContext());
				service.stopAlarm(getActivity().getApplicationContext());
				if(!service.isCall(getActivity().getApplicationContext())){
					service.startCall(getActivity().getApplicationContext());
					service.startAlarm(getActivity().getApplicationContext());
					Toast.makeText(
						getActivity().getApplicationContext(),
						"실시간 앱체킹을 시작합니다", Toast.LENGTH_LONG).show();
				}
			}
			else{
				if(service.isCall(getActivity().getApplicationContext())){
					service.stopCall(getActivity().getApplicationContext());
					service.stopAlarm(getActivity().getApplicationContext());
					Toast.makeText(
						getActivity().getApplicationContext(),
						"실시간 앱체킹을 종료합니다", Toast.LENGTH_LONG).show();
				}
			}
			
		}
		else if(data.getClass().equals(PromiseModel.class)){		
			PromiseModel promise = (PromiseModel)data;
			
			app.updatePromise(promise);
			updatePromiseList();
			
			/*/
			// 수정
			if(promise.getIndex()<app.getPromises(5).size()){
				app.updatePromise(promise);
				updatePromiseList();
			}
			// 신규
			else{
				app.updatePromise(promise);
				updatePromiseList();
				//updatePromise(promise);
			}
			/**/
		}
	}
	
	
	protected class OnBtnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			Model object = (Model)v.getTag();
			
			if(object.getClass().equals(PromiseModel.class)){
				if(app.getPromises(5).size()<5){
					openDialog(object);
					//onCheckPasswd(object);
				}else{
					Toast.makeText(
							getActivity().getApplicationContext(),
							"5개까지 등록가능합니다", Toast.LENGTH_LONG).show();
				}
			}
			else if(object.getClass().equals(PlanModel.class)){
				//openDialog(object);
				onCheckPasswd(object);
			}
		}
	}
	
	private void onCheckPasswd(final Model object){
		final ProfileModel profile = app.getProfile();
		LayoutInflater inflater = getActivity().getLayoutInflater();

		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		View view = inflater.inflate(R.layout.alertdialog_passwd, null);
		
		TextView checkPasswd = (TextView)view.findViewById(R.id.checkPasswd);
		fontLoader.setTextViewTypeFace(
				checkPasswd,
				R.string.check_passwd,
				R.string.NanumGothic,
				(float)8.0);
		
		final EditText passwd = (EditText)view.findViewById(R.id.textPassword);
		passwd.setHint(R.string.passwd_message);
		
		alert.setView(view);
		
		alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String text = passwd.getText().toString();
				if(text.equals(profile.getPasswd())){
					openDialog(object);
				}else{
					Toast.makeText(getActivity(), R.string.wrong_passwd, Toast.LENGTH_LONG).show();
				}
			}
		});
		alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alert.show();
	}
	
	protected void openDialog(Model object){
		PlanDialogFragment dialog = PlanDialogFragment.getInstances(object);
		dialog.setTargetFragment(this, 0);
		dialog.show(getFragmentManager(), "planDialog");
	}
	
	
}
