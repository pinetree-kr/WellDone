package com.pinetree.welldone.views;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pinetree.library.utils.FontLoader;
import com.pinetree.library.utils.ImageLoader;
import com.pinetree.welldone.R;
import com.pinetree.welldone.models.PlanModel;
import com.pinetree.welldone.utils.DeviceInfo;

public class NotificationToast extends Toast{
	private DeviceInfo app;
	private ImageLoader imageLoader;
	private FontLoader fontLoader;
	
	private TextView message;
	
	private View view;
	private static NotificationToast toast;
	
	public NotificationToast(Context context) {
		super(context);
		app = (DeviceInfo)context.getApplicationContext();
		fontLoader = new FontLoader(context.getApplicationContext());
		imageLoader = new ImageLoader(context.getResources(), app.getScaledRate());
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		
		view = inflater.inflate(R.layout.toast_notification, null);
		setupUI(view);
		
		setGravity(Gravity.CENTER, 0, 0);
		setView(view);
	}

	private void setupUI(View view){
		ImageView bg = (ImageView)view.findViewById(R.id.notification_bg);
		bg.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.notification_bg));
		
		message = (TextView)view.findViewById(R.id.notification_message);
	}
	
	public static NotificationToast makeText(Context context, PlanModel plan, int duration){
		if(toast!=null){
			toast.cancel();
		}
		toast = new NotificationToast(context);
		toast.setText(plan);
		toast.setDuration(duration);
		return toast;
	}
	
	public void setText(PlanModel plan){
		String notification = "오늘 현재까지 스마트폰을\n";
		
		if(plan.getUsageHour()>0){
			notification += plan.getUsageHour()+"시간 "; 
		}
		if(plan.getUsageMin()>0){
			notification += plan.getUsageMin()+"분 "; 			
		}
		notification += "사용하였습니다\n남은 시간은 ";
		if(plan.getRemain()>0){
			if(plan.getRemainHour()>0){
				notification += plan.getRemainHour()+"시간 ";
			}
			if(plan.getRemainMin()>0){
				notification += plan.getRemainMin()+"분 ";				
			}
			notification += "입니다";
		}else{
			notification += "없습니다";
		}
		
		//String notification = String.format("현재까지 총 %d시간 %분을 사용하였습니다.\n남은 시간은 ", args)
		fontLoader.setTextViewTextSize(message, notification, (float)6.0);
	}
	
	@Override
	public View getView(){
		return view;
	}
		
}
