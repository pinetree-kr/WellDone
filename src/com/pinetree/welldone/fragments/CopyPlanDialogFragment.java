package com.pinetree.welldone.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.pinetree.welldone.R;
import com.pinetree.welldone.adapters.SpinnerAdapter;
import com.pinetree.welldone.models.Model;
import com.pinetree.welldone.models.PlanModel;
import com.pinetree.welldone.models.PromiseModel;

public class CopyPlanDialogFragment extends BaseDialogFragment {
	protected EditText editHour, editMin;
	protected EditText editPromiseSubject;
	//protected EditText[] editPromiseStart, editPromiseEnd;
	protected Spinner spinner;
	
	protected Model object;
	protected TextView planTimer, promiseStart, promiseEnd;
	
	public static CopyPlanDialogFragment getInstances(Model object){
		CopyPlanDialogFragment dialog = new CopyPlanDialogFragment();
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
			setupPlanUI(view);
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
		/*/
		ImageView titlePlan = (ImageView)view.findViewById(R.id.titlePromise);
		titlePlan.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.promise_title));
		
		ImageView bgPromiseSubject = (ImageView)view.findViewById(R.id.bgPromiseSubject);
		bgPromiseSubject.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.promise_bg));
		ImageView bgLabelPromiseSubject = (ImageView)view.findViewById(R.id.bgLabelPromiseSubject);
		bgLabelPromiseSubject.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.label_promise));
		TextView textLabelPromiseSubject = (TextView)view.findViewById(R.id.textLabelPromiseSubject);
		textLabelPromiseSubject.setText("구분");

		ImageView bgAlarmRepeat = (ImageView)view.findViewById(R.id.bgAlarmRepeat);
		bgAlarmRepeat.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.alarm_setting_bg));
		ImageView bgLabelAlarmRepeat = (ImageView)view.findViewById(R.id.bgLabelAlarmRepeat);
		bgLabelAlarmRepeat.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.label_promise));
		TextView textLabelAlarmRepeat = (TextView)view.findViewById(R.id.textLabelAlarmRepeat);
		textLabelAlarmRepeat.setText("반복");
 
		ImageView bgPromiseStart = (ImageView)view.findViewById(R.id.bgPromiseStart);
		bgPromiseStart.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.promise_bg));
		ImageView bgLabelPromiseStart = (ImageView)view.findViewById(R.id.bgLabelPromiseStart);
		bgLabelPromiseStart.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.label_promise));
		TextView textLabelPromiseStart = (TextView)view.findViewById(R.id.textLabelPromiseStart);
		textLabelPromiseStart.setText("시작 시간");
		
		ImageView bgPromiseEnd = (ImageView)view.findViewById(R.id.bgPromiseEnd);
		bgPromiseEnd.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.promise_bg));
		ImageView bgLabelPromiseEnd = (ImageView)view.findViewById(R.id.bgLabelPromiseEnd);
		bgLabelPromiseEnd.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.label_promise));
		TextView textLabelPromiseEnd = (TextView)view.findViewById(R.id.textLabelPromiseEnd);
		textLabelPromiseEnd.setText("시작 시간");
		
		spinner = (Spinner)view.findViewById(R.id.SpinnerAlarmRepeat);
		SpinnerAdapter adapterAlarm = new SpinnerAdapter<String>(
				this.getActivity().getApplicationContext(),
				R.layout.spinner_alarm_row,
				((PromiseModel)object).getAlarmRepeats()
				);
		spinner.setAdapter(adapterAlarm);
		
		//editPromiseSubject = (EditText)view.findViewById(R.id.editPromiseSubject);
		
		promiseStart = (TextView)view.findViewById(R.id.promiseTimerStart);
		promiseEnd = (TextView)view.findViewById(R.id.promiseTimerEnd);
		promiseStart.setOnClickListener(new OnTimeSettingClickListner());
		promiseEnd.setOnClickListener(new OnTimeSettingClickListner());
		updatePromise((PromiseModel)object);
		/**/
	}
	
	public void setupPlanUI(View view){
		/*/
		ImageView titlePlan = (ImageView)view.findViewById(R.id.titlePlan);
		titlePlan.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.plan));
		ImageView bgTimeSet = (ImageView)view.findViewById(R.id.bgTimeSetting);
		bgTimeSet.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.promise_bg));
		
		
		ImageView labelTimeSet = (ImageView)view.findViewById(R.id.labelTimeSet);
		labelTimeSet.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.time_set));
		
		ImageView bgAlarmSet = (ImageView)view.findViewById(R.id.bgAlarmSetting);
		bgAlarmSet.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.alarm_setting_bg));
		ImageView labelAlarmSet = (ImageView)view.findViewById(R.id.labelAlarmSet);
		labelAlarmSet.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.alarm_set));
		
		spinner = (Spinner)view.findViewById(R.id.SpinnerAlarmType);
		SpinnerAdapter adapterAlarm = new SpinnerAdapter<String>(
				this.getActivity().getApplicationContext(),
				R.layout.spinner_alarm_row,
				((PlanModel)object).getAlarmTypes()
				);
		spinner.setAdapter(adapterAlarm);
		
		planTimer = (TextView)view.findViewById(R.id.planTimer);
		planTimer.setOnClickListener(new OnTimeSettingClickListner());
		/**/
		updatePlan((PlanModel)object);
	}
	private void updatePlan(PlanModel plan){
		fontLoader.setTextViewTypeFace(planTimer,
				plan.getHourFormat()+"시간 "+plan.getMinFormat()+"분",
				R.string.NanumGothic, (float)7.0);
		
		spinner.setSelection(plan.getAlarmType());
	}
	private void updatePromise(PromiseModel promise){
		editPromiseSubject.setHint(promise.getSubject());
		spinner.setSelection(promise.getDayType());
		
		fontLoader.setTextViewTypeFace(promiseStart,
				promise.getStartTime(),
				R.string.NanumGothic, (float)7.0);
		fontLoader.setTextViewTypeFace(promiseEnd,
				promise.getEndTime(),
				R.string.NanumGothic, (float)7.0);
	}
	/*/
	private void setPromise(PromiseModel promise){
		editPromiseSubject.setHint(promise.getSubject());
		spinner.setSelection(promise.getDayType());
		editPromiseStart[0].setHint(promise.getStartHourFormat());
		editPromiseStart[1].setHint(promise.getStartMinFormat());
		editPromiseEnd[0].setHint(promise.getEndHourFormat());
		editPromiseEnd[1].setHint(promise.getEndMinFormat());
	}
		/**/
	
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
	public void setTimer(int hour, int min, boolean is24){
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		
		final TimePicker picker = new TimePicker(getActivity());
		picker.setIs24HourView(is24);
		picker.setCurrentHour(hour);
		picker.setCurrentMinute(min);
		
		alert.setView(picker);
		alert.setPositiveButton("저장", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(object.getClass().equals(PlanModel.class)){
					PlanModel plan = (PlanModel)object;
					plan.setTime(picker.getCurrentHour(), picker.getCurrentMinute());
					updatePlan(plan);
				}
				else if(object.getClass().equals(PromiseModel.class)){
					PromiseModel promise = (PromiseModel)object;
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
	protected class OnTimeSettingClickListner implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(v.getId()==R.id.promiseTimerStart){
				PromiseModel promise = (PromiseModel)object;
				setTimer(promise.getStartHour(), promise.getStartMin(), false);
			}
			/*/
			else if(v.getId()==R.id.planTimer){
				PlanModel plan = (PlanModel)object;
				setTimer(plan.getHour(), plan.getMin(), true);
			}
			/**/
			else if(v.getId()==R.id.promiseTimerEnd){
				PromiseModel promise = (PromiseModel)object;
				setTimer(promise.getEndHour(), promise.getEndMin(), false);
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
			//Toast.makeText(getActivity(), tag, 1000).show();
			
			if(tag.equals("ok")){
				if(object.getClass().equals(PlanModel.class)){
					PlanModel plan = (PlanModel)object;
					plan.setUsage(0);
					plan.setAlarmType(spinner.getSelectedItemPosition());
					
					dialogListener.onDialogClick(
							dialog,
							plan);
				}else if(object.getClass().equals(PromiseModel.class)){
					
					PromiseModel promise = (PromiseModel)object;
					promise.setDayType(spinner.getSelectedItemPosition());
					
					String subject = editPromiseSubject.getText().toString();
					if(subject.equals("")){
						subject = editPromiseSubject.getHint().toString();
					}
					promise.setSubject(subject);
					
					//TODO:여기
					/*/
					String startHour = editPromiseStart[0].getText().toString();
					if(startHour.equals("")){
						startHour = editPromiseStart[0].getHint().toString();
					}
					String startMin = editPromiseStart[1].getText().toString();
					if(startMin.equals("")){
						startMin = editPromiseStart[1].getHint().toString();
					}
					
					String endHour = editPromiseEnd[0].getText().toString();
					if(endHour.equals("")){
						endHour = editPromiseEnd[0].getHint().toString();
					}
					String endMin = editPromiseEnd[1].getText().toString();
					if(endMin.equals("")){
						endMin = editPromiseEnd[1].getHint().toString();
					}
					
					promise.setStartTime(
							Integer.parseInt(startHour),
							Integer.parseInt(startMin));
					promise.setEndTime(
							Integer.parseInt(endHour),
							Integer.parseInt(endMin));
							/**/
					dialogListener.onDialogClick(dialog, promise);
				}
			}else if(tag.equals("no")){
			}
			dialog.dismiss();
		}
	}	
}
