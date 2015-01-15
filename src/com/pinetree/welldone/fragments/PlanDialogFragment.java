package com.pinetree.welldone.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.pinetree.welldone.R;
import com.pinetree.welldone.models.Model;
import com.pinetree.welldone.models.PlanModel;
import com.pinetree.welldone.models.PromiseModel;

public class PlanDialogFragment extends BaseDialogFragment {
	protected EditText editSubject;
	
	private TimePicker planTimePicker, startPicker, endPicker;
	private NumberPicker alarmPicker;
	
	protected Model object;
	
	protected TextView planTimer, promiseStartEnd, promiseAlarmRepeat;
	
	public static PlanDialogFragment getInstances(Model object){
		PlanDialogFragment dialog = new PlanDialogFragment();
		Bundle args = new Bundle();
		
		args.putSerializable("data", object);
		dialog.setArguments(args);
		
		return dialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		View view = null;
		
		if(object.getClass().equals(PlanModel.class)){
			view = inflater.inflate(R.layout.fragment_dialog_plan, null);
			setupPlanUI(view, (PlanModel)object);
		}else if(object.getClass().equals(PromiseModel.class)){
			view = inflater.inflate(R.layout.fragment_dialog_promise, null);
			setupPromiseUI(view);
		}
		setupUI(view);
		builder.setView(view);
		
		return builder.create();
	}
	public void setupUI(View view){
		ImageView btnOk = (ImageView)view.findViewById(R.id.btnOk);
		btnOk.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.btn_ok));
		btnOk.setTag("ok");
		btnOk.setOnClickListener(new OnBtnClickListener(this));
		ImageView btnNo = (ImageView)view.findViewById(R.id.btnNo);
		btnNo.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.btn_cancel));
		btnNo.setTag("no");
		btnNo.setOnClickListener(new OnBtnClickListener(this));
	}
	
	public void setupPromiseUI(View view){
		ImageView titlePromise = (ImageView)view.findViewById(R.id.TitlePromise);
		titlePromise.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.promise_title));
		
		ImageView bgSubject = (ImageView)view.findViewById(R.id.bgSubject);
		bgSubject.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.promise_bg));
		
		ImageView bgLblSubject = (ImageView)view.findViewById(R.id.bgLblSubject);
		bgLblSubject.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.promise_label));
		TextView textLblSubject = (TextView)view.findViewById(R.id.textLblSubject);
		fontLoader.setTextViewTypeFace(
				textLblSubject,
				R.string.promise_lbl_subject,
				R.string.NanumGothicB,
				(float)6.0);
		
		ImageView bgAlarmRepeat = (ImageView)view.findViewById(R.id.bgAlarmRepeat);
		bgAlarmRepeat.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.alarm_setting_bg));
		
		ImageView bgLblAlarmRepeat = (ImageView)view.findViewById(R.id.bgLblAlarmRepeat);
		bgLblAlarmRepeat.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.promise_label));
		
		TextView textLblAlarmRepeat = (TextView)view.findViewById(R.id.textLblAlarmRepeat);
		fontLoader.setTextViewTypeFace(
				textLblAlarmRepeat,
				R.string.promise_lbl_repeat,
				R.string.NanumGothicB,
				(float)6.0);
		
		ImageView bgStartEnd = (ImageView)view.findViewById(R.id.bgStartEnd);
		bgStartEnd.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.alarm_setting_bg));
		
		ImageView bgLblStartEnd = (ImageView)view.findViewById(R.id.bgLblStartEnd);
		bgLblStartEnd.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.promise_label));
		
		TextView textLblStartEnd = (TextView)view.findViewById(R.id.textLblStartEnd);
		fontLoader.setTextViewTypeFace(
				textLblStartEnd,
				R.string.promise_lbl_startend,
				R.string.NanumGothicB,
				(float)6.0);
		
		editSubject = (EditText)view.findViewById(R.id.editSubject);
		fontLoader.setTextViewTypeFace(
				editSubject,
				R.string.NanumGothicB,
				(float)6.0);
		
		ImageView bgTextAlarmRepeat = (ImageView)view.findViewById(R.id.bgTextAlarmRepeat);
		ImageView bgTextStartEnd = (ImageView)view.findViewById(R.id.bgTextStartEnd);
		bgTextAlarmRepeat.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.promise_text_bg));
		bgTextStartEnd.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.promise_text_bg));
		
		bgTextAlarmRepeat.setOnClickListener(new OnSelectorClickListener());
		bgTextStartEnd.setOnClickListener(new OnSelectorClickListener());
		
		promiseAlarmRepeat = (TextView)view.findViewById(R.id.textAlarmRepeat);
		promiseStartEnd = (TextView)view.findViewById(R.id.textStartEnd);
		
		updatePromise((PromiseModel)object);
	}
	
	public void setupPlanUI(View view, PlanModel plan){
		ImageView titlePlanTime = (ImageView)view.findViewById(R.id.TitlePlanTime);
		titlePlanTime.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.plan));
		
		planTimePicker = (TimePicker)view.findViewById(R.id.PlanTimePicker);
		planTimePicker.setIs24HourView(true);
		planTimePicker.setCurrentHour(plan.getHour());
		planTimePicker.setCurrentMinute(plan.getMin());
		
		alarmPicker = (NumberPicker)view.findViewById(R.id.PlanAlarmPicker);
		alarmPicker.setMinValue(0);
		alarmPicker.setMaxValue(plan.getAlarmTypes().size()-1);
		alarmPicker.setDisplayedValues(plan.getAlarmTypeArray());
		
		alarmPicker.setValue(plan.getAlarmType());
	}
	
	private void updatePromise(PromiseModel promise){
		editSubject.setHint(promise.getSubject());
		
		fontLoader.setTextViewTypeFace(
				promiseAlarmRepeat,
				promise.getAlarmRepeat(),
				R.string.NanumGothicB,
				(float)6.0);
		fontLoader.setTextViewTypeFace(
				promiseStartEnd,
				promise.getDuration(),
				R.string.NanumGothicB,
				(float)6.0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Bundle args = this.getArguments();
		
		if(args != null){
			object = (Model)args.getSerializable("data");
		}
	}
	
	@Override
	public void onStop(){
		super.onStop();
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	@Override
	public void onDestroyView(){
		super.onDestroyView();
	}
	@Override
	public void onDismiss(DialogInterface dialog){
		super.onDismiss(dialog);
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
	public void onStart(){
		super.onStart();
	}
	@Override
	public void onResume(){
		super.onResume();
	}
	
	private void setPromise(PromiseModel promise, final boolean isTimer){
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = null;
		
		// 시간설정
		if(isTimer){
			view = inflater.inflate(R.layout.alertdialog_promise_startend, null);
			
			TextView textStart = (TextView)view.findViewById(R.id.textStart);
			TextView textEnd = (TextView)view.findViewById(R.id.textEnd);
			
			fontLoader.setTextViewTypeFace(
					textStart, R.string.NanumGothicB, (float)6.5);
			fontLoader.setTextViewTypeFace(
					textEnd, R.string.NanumGothicB, (float)6.5);
			
			final TimePicker start = (TimePicker)view.findViewById(R.id.PromiseStart);
			final TimePicker end = (TimePicker)view.findViewById(R.id.PromiseEnd);
			
			start.setIs24HourView(true);
			start.setCurrentHour(promise.getStartHour());
			start.setCurrentMinute(promise.getStartMin());
			end.setIs24HourView(true);
			end.setCurrentHour(promise.getEndHour());
			end.setCurrentMinute(promise.getEndMin());
			
			start.setOnTimeChangedListener(new OnTimeChangedListener() {
				@Override
				public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
					if(hourOfDay>=end.getCurrentHour()){
						end.setCurrentHour(hourOfDay);
						if(minute>=end.getCurrentMinute())
							end.setCurrentMinute(minute);
					}
				}
			});
			end.setOnTimeChangedListener(new OnTimeChangedListener() {
				@Override
				public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
					if(hourOfDay<=start.getCurrentHour()){
						start.setCurrentHour(hourOfDay);
						if(minute<=start.getCurrentMinute())
							start.setCurrentMinute(minute);
					}
				}
			});
			
			alert.setView(view);
			alert.setPositiveButton("저장", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					PromiseModel promise = (PromiseModel)object;
					promise.setStartTime(
						start.getCurrentHour(),
						start.getCurrentMinute());
					/*/
					if(start.getCurrentHour()>=end.getCurrentHour()
							&& start.getCurrentMinute()>end.getCurrentMinute()){
						promise.setEndTime(
							start.getCurrentHour()+1,
							start.getCurrentMinute());
					}else{
						promise.setEndTime(
							end.getCurrentHour(),
							end.getCurrentMinute());						
					}
					/**/
					promise.setEndTime(
							end.getCurrentHour(),
							end.getCurrentMinute());						
					updatePromise(promise);
				}
			});
		}
		// 반복설정
		else{
			view = inflater.inflate(R.layout.alertdialog_promise_alarmrepeat, null);
			
			final NumberPicker repeat = (NumberPicker)view.findViewById(R.id.AlarmRepeat);
			
			repeat.setMinValue(0);
			repeat.setMaxValue(promise.getAlarmRepeatArray().length-1);
			repeat.setDisplayedValues(promise.getAlarmRepeatArray());
			
			repeat.setValue(promise.getDayType());
			alert.setView(view);
			alert.setPositiveButton("저장", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					PromiseModel promise = (PromiseModel)object;
					promise.setDayType(repeat.getValue());
					updatePromise(promise);
				}
			});
		}
		alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		alert.show();
	}
	protected class OnSelectorClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			
			if(v.getId()==R.id.bgTextAlarmRepeat){
				setPromise((PromiseModel)object, false);
			}
			else if(v.getId()==R.id.bgTextStartEnd){
				setPromise((PromiseModel)object, true);
			}
		}
	}
	
	protected class OnBtnClickListener implements OnClickListener{
		private BaseDialogFragment dialog;
		public OnBtnClickListener(BaseDialogFragment dialog){
			this.dialog = dialog;
		}
		
		@Override
		public void onClick(View v) {
			String tag = (String)v.getTag();
			
			if(tag.equals("ok")){
				if(object.getClass().equals(PlanModel.class)){
					PlanModel plan = (PlanModel)object;
					//사용량 초기화
					//plan.setUsage(0);
					
					plan.setTime(
							planTimePicker.getCurrentHour(),
							planTimePicker.getCurrentMinute()
							);
					plan.setAlarmType(alarmPicker.getValue());
					
					
					dialogListener.onDialogClick(
							dialog,
							plan);
				}else if(object.getClass().equals(PromiseModel.class)){
					PromiseModel promise = (PromiseModel)object;
					
					String subject = editSubject.getText().toString();
					if(subject.equals("")){
						subject = editSubject.getHint().toString();
					}
					promise.setSubject(subject);
					
					dialogListener.onDialogClick(dialog, promise);
				}
			}else if(tag.equals("no")){
			}
			dialog.dismiss();
		}
	}	
}
