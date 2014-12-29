package com.pinetree.welldone.utils;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pinetree.utils.ImageLoader;
import com.pinetree.welldone.R;
import com.pinetree.welldone.fragments.ResultDialogFragment;
import com.pinetree.welldone.fragments.ResultFragment;
import com.pinetree.welldone.models.LogModel;

public class ViewUtil {
	
	public static void addWeekLabel(Context context, ViewGroup view){
		addWeek(context, null, null, view, "label", null, null);
	}
	// this week
	public static void addWeek(Context context, DBHandler handler, ViewGroup view){
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
		
		// make first day of week (sunday);
		cal.add(Calendar.DATE, -dayOfWeek);
		int startYear = cal.get(Calendar.YEAR);
		int startMonth = cal.get(Calendar.MONTH) + 1;
		int startDay = cal.get(Calendar.DATE);
		String startDate = String.format("%04d%02d%02d", startYear, startMonth, startDay);
		
		cal.add(Calendar.DATE, 6);
		int endYear = cal.get(Calendar.YEAR);
		int endMonth = cal.get(Calendar.MONTH) + 1;
		int endDay = cal.get(Calendar.DATE);
		String endDate = String.format("%04d%02d%02d", endYear, endMonth, endDay);
	
		addWeek(context, null, handler, view, "week", startDate, endDate);
	}
	
	public static void updateMonth(Context context, FragmentManager fm, DBHandler handler, ViewGroup view, Calendar calendar){
		// 초기화
		view.removeAllViews();
		
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		//int year = calendar.get(Calendar.DAY_OF_MONTH);
		
		//Sunday : 0 ~ Saturday : 6
		int startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1 ;
		int endOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		int lastWeek = (int)Math.ceil((double)(endOfMonth+startDayOfWeek)/7);
		
		String startDate = null, endDate = null;
		int startDay, endDay;
		int weekOffset = 0;
		
		//초기화
		calendar.add(Calendar.DAY_OF_MONTH, -startDayOfWeek);
		for(int i=0; i<lastWeek; i++){
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH) + 1;
			// 일요일
			startDay = calendar.get(Calendar.DAY_OF_MONTH);
			// 토요일
			endDay = startDay + 6;
			
			startDate = String.format("%04d%02d%02d", year,month,startDay);
			endDate = String.format("%04d%02d%02d", year,month,endDay);
			
			//첫주
			if(i==0){
				weekOffset = startDayOfWeek;
			}
			//마지막주
			else if(i==lastWeek-1){
				weekOffset = (startDayOfWeek + endOfMonth)%7 - 7;
			}
			//그외
			else{
				weekOffset = 0;
			}
			addWeekRow(context, fm, handler, view, "month", startDate, endDate, weekOffset);
			
			//일주일후
			calendar.add(Calendar.DAY_OF_MONTH, 7);
		}
	}
	/**/
	public static void addWeek(Context context, FragmentManager fm, DBHandler handler, ViewGroup view, String type, String startDate, String endDate){
		addWeekRow(context, fm, handler, view, type, startDate, endDate, 0);
	}
	/**/
	public static void addWeekRow(final Context context, final FragmentManager fm, DBHandler handler, ViewGroup view, String type, String startDate, String endDate, int weekOffset){
		Resources res = context.getResources();
		DeviceInfo app = (DeviceInfo)context;
		ImageLoader imageLoader = new ImageLoader(res, app.getScaledRate());
		
		LayoutInflater li = LayoutInflater.from(context);
		LinearLayout weekRow = (LinearLayout)li.inflate(R.layout.layout_week, null);
		
		FrameLayout day = null;
		ImageView dayBg = null;
		
		ImageView dayResult = null;
		TextView dayText = null;
		
		// label
		if(type.equals("label")){
			String[] lblDrawableIds = res.getStringArray(R.array.weekdays_images);
			
			for(int i=0; i<lblDrawableIds.length; i++){
				day = (FrameLayout) li.inflate(R.layout.layout_day, null);
				dayBg = (ImageView) day.findViewById(R.id.dayBg);
				dayBg.setImageDrawable(
						imageLoader.getResizedDrawable(
								res.getIdentifier(
										lblDrawableIds[i],
										"drawable",
										context.getPackageName()
									)));
				weekRow.addView(day);
			}
		}
		// this week
		else if(type.equals("week") || type.equals("month")){
			//Log.i("DebugPrint",startDate+":"+endDate);
			
			ArrayList<LogModel> logs = handler.getWeekLogs(startDate, endDate);
			
			int dayOfWeek = 0;
			if(type.equals("week")){
				Calendar cal = Calendar.getInstance();
				
				// 0:sunday ~ 6:saturday
				dayOfWeek = (cal.get(Calendar.DAY_OF_WEEK) - 1);
			}else if(type.equals("month")){
				dayOfWeek = weekOffset;
			}
			
			for(int i=0; i<logs.size(); i++){
				day = (FrameLayout) li.inflate(R.layout.layout_day, null);
				dayBg = (ImageView) day.findViewById(R.id.dayBg);
				dayResult = (ImageView) day.findViewById(R.id.dayResult);
				
				if(type.equals("week")){
					//오늘
					if(dayOfWeek==i){
						dayBg.setImageDrawable(
								imageLoader.getResizedDrawable(R.drawable.slot_2));
					}
					else{
						dayBg.setImageDrawable(
								imageLoader.getResizedDrawable(R.drawable.slot_1));
						LogModel log = logs.get(i);
						switch(log.getResult()){
						case 0:
							dayResult.setImageDrawable(
									imageLoader.getResizedDrawable(R.drawable.nop));
							break;
						case 1:
							dayResult.setImageDrawable(
									imageLoader.getResizedDrawable(R.drawable.ok));
							break;				
						}
					}
				}else if(type.equals("month")){
					// 첫주의 시작전
					if(dayOfWeek>0 && i<dayOfWeek){
						dayBg.setImageDrawable(
								imageLoader.getResizedDrawable(R.drawable.slot_0));
					}
					// 마지막주의 마지막후
					else if(dayOfWeek<0 && dayOfWeek>-7 && i>=((7+dayOfWeek)%7)){
						dayBg.setImageDrawable(
								imageLoader.getResizedDrawable(R.drawable.slot_0));
					}else{
						dayBg.setImageDrawable(
								imageLoader.getResizedDrawable(R.drawable.slot_1));
						LogModel log = logs.get(i);
						//Log.i("DebugPrint","log:"+log.getResult());
						switch(log.getResult()){
						case 0:
							dayResult.setImageDrawable(
									imageLoader.getResizedDrawable(R.drawable.nop));
							break;
						case 1:
							dayResult.setImageDrawable(
									imageLoader.getResizedDrawable(R.drawable.ok));
							break;				
						}
						if(log.getResult()>=0){
							dayResult.setTag(log);
							dayResult.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									LogModel log = (LogModel)v.getTag();
									ResultDialogFragment dialog = ResultDialogFragment.getInstances(log);
									
									dialog.show(fm, "resultDialog");
									//Log.i("DebugPrint","clickresult");
								}
							});
						}
					}
				}
				
				weekRow.addView(day);
			}
		}
		view.addView(weekRow);
	}
}
