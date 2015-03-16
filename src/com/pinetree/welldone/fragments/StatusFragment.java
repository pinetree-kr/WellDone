package com.pinetree.welldone.fragments;


import java.util.ArrayList;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pinetree.welldone.R;
import com.pinetree.welldone.models.PackageModel.RunningApp;
import com.pinetree.welldone.models.PlanModel;
import com.pinetree.welldone.models.ProfileModel;

public class StatusFragment extends BaseFragment{
	protected AnimationDrawable ad;
	protected ImageView titleBar, profile, circle, btnListExpand;
	protected TextView status, usageTime;
	protected ViewGroup listApp;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(false);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_status, container, false);
		setComponents(view);
		return view;
	}
	/**/
	private void setComponents(View view){
		ImageView dateBg = (ImageView)view.findViewById(R.id.dateBg);
		dateBg.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.bar));
		
		TextView date = (TextView)view.findViewById(R.id.date);
		fontLoader.setTextViewTypeFace(
				date, app.getToday(), R.string.NanumGothicB, (float)10);
		
		ImageView statusBg = (ImageView)view.findViewById(R.id.imageStatus);
		statusBg.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.status));
		
		// set anime circle
		circle = (ImageView)view.findViewById(R.id.anime);
		
		//상태메시지
		ImageView statusWhale = (ImageView)view.findViewById(R.id.statusWhale);
		statusWhale.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.whale_2));
		status = (TextView)view.findViewById(R.id.textStatus);
		
		//사용시간
		usageTime = (TextView)view.findViewById(R.id.usageTime);
		
		//별
		ImageView star = (ImageView)view.findViewById(R.id.star);
		star.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.star_2));
		//별
		ImageView ship = (ImageView)view.findViewById(R.id.ship);
		ship.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.ship_2));
		
		
		ImageView listHeadBg = (ImageView)view.findViewById(R.id.imageListHead);
		listHeadBg.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.list));
		ImageView appRankBg = (ImageView)view.findViewById(R.id.appRankBg);
		ImageView appNameBg = (ImageView)view.findViewById(R.id.appNameBg);
		ImageView appRateBg = (ImageView)view.findViewById(R.id.appRateBg);
		ImageView appCountBg = (ImageView)view.findViewById(R.id.appCountBg);
		ImageView appTimeBg = (ImageView)view.findViewById(R.id.appTimeBg);
		
		appRankBg.setImageDrawable(imageLoader.getResizedDrawable(R.drawable.app_rank));
		appNameBg.setImageDrawable(imageLoader.getResizedDrawable(R.drawable.app_name));
		appRateBg.setImageDrawable(imageLoader.getResizedDrawable(R.drawable.app_rate));
		appCountBg.setImageDrawable(imageLoader.getResizedDrawable(R.drawable.app_count));
		appTimeBg.setImageDrawable(imageLoader.getResizedDrawable(R.drawable.app_time));
		
		listApp = (LinearLayout)view.findViewById(R.id.ListApp);

		ImageView listTail = (ImageView)view.findViewById(R.id.imageListTail);
		listTail.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.list));
		btnListExpand = (ImageView)view.findViewById(R.id.btnListExpand);
		updateButton();
//		if ( (app.getProfile().getNStatusList() >= 0 && getWantedAppNum() > 3) ||
//				(app.getProfile().getNStatusList() < 0 && app.getUsedApps().size() > 3) )
//			btnListExpand.setOnClickListener(new onBtnClickListener());
		btnListExpand.setOnLongClickListener(new onBtnLongClickListener());
	}
	protected class onBtnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
            // There is only one clickable right now.
            // If it increases in the future, R.Id should be checked...
			// toggle nstatuslist
			ProfileModel profile = app.getProfile();
			if (profile.getNStatusList() == 3) {
				profile.setNStatusList(0);
			} else {
				profile.setNStatusList(3);
			}
			app.updateProfile(profile);

			updateAppStatus(profile.getNStatusList());
			updateButton();
		}
	}
    private class onBtnLongClickListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            // toggle nstatuslist
            ProfileModel profile = app.getProfile();
            if (profile.getNStatusList() == -1) {
                profile.setNStatusList(3);
            } else {
                profile.setNStatusList(-1);
            }
            app.updateProfile(profile);

            updateAppStatus(profile.getNStatusList());
            updateButton();
            return true;
        }
    }
	private void updateButton(){
		ProfileModel profile = app.getProfile();
		if ( (profile.getNStatusList() == 0 && getWantedAppNum() > 3) ||
				(profile.getNStatusList() < 0 && app.getUsedApps().size() > 3) ) {
			btnListExpand.setImageDrawable(
					imageLoader.getResizedDrawable(R.drawable.list_shrink));
			btnListExpand.setOnClickListener(new onBtnClickListener());
		}
		else if (profile.getNStatusList() > 0 && getWantedAppNum() > 3) {
			btnListExpand.setImageDrawable(
					imageLoader.getResizedDrawable(R.drawable.list_expand));
			btnListExpand.setOnClickListener(new onBtnClickListener());
		}
		else
			btnListExpand.setImageDrawable(
					imageLoader.getResizedDrawable(R.drawable.list_blank));
	}
	private void updateView(){
		PlanModel plan = app.getPlan();

		setStatus(plan.getUsageRate());
		
		fontLoader.setTextViewTextSize(usageTime, plan.getUsageFormat(), (float)20.0);
		
		updateCircle(plan.getUsageRate());
		ProfileModel profile = app.getProfile();
		updateAppStatus(profile.getNStatusList());
	}

    private int getWantedAppNum(){
        ArrayList<RunningApp> apps = app.getUsedApps();
        int ret = 0;
        for(int i=0; i < apps.size(); i++){
            if (isNotWanted(apps.get(i).getPkgName())) continue;
            ret++;
        }
        return ret;
    }
	private void updateAppStatus(int N){
		ArrayList<RunningApp> apps = app.getUsedApps(/*3*/);
		listApp.removeAllViews();
		int limit, count = 0;
		if (N <= 0) limit = apps.size();
		else if (N < apps.size()) limit = N;
		else limit = apps.size();

		for(int i=0; (i < apps.size() && count < limit); i++){
			//if (apps.get(i).getTime() > 0) // TODO: why doesn't it work??
            if (N >= 0 && isNotWanted(apps.get(i).getPkgName())) continue;
			this.addAppInfo((LinearLayout)listApp, apps.get(i), count);
			count++;
		}
	}
    private boolean isNotWanted(String pkgName) {
        if (pkgName.equals("com.pinetree.welldone")) return true;
        if (pkgName.equals("com.android.systemui")) return true;
        if (pkgName.equals("com.android.launcher")) return true;
        if (pkgName.contains("launcher")) return true;
        return false;
    }
	private void updateCircle(int rate){
		String[] frameIds = getResources().getStringArray(R.array.circle_images);
		int i = (int)((frameIds.length-1) * (rate/100.0));
		circle.setImageDrawable(
				imageLoader.getResizedDrawable(
						getResources().getIdentifier(
								frameIds[i],"drawable",this.getActivity().getPackageName())));
		/*/
		circle.setImageDrawable(getAnime(rate));
		ad = (AnimationDrawable)circle.getDrawable();
		if(ad!=null){
			ad.start();
		}
		/**/
	}
	private void setStatus(int usage){
		String message = "["+usage+"%] ";
		switch(usage/10){
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
			message += "지금 이대로 괜찮아!";
			break;
		case 5:
		case 6:
			message += "절반 밖에 안남았네!";
			break;
		case 7:
		case 8:
			message += "얼마 안남았네. 조심하자!";
			break;
		case 9:
			message += "다 써간다. 그만하자!";
			break;
		case 10:
			message += "사용시간 종료!";
			break;
		}
		fontLoader.setTextViewTextSize(status, message, (float)7.25);
	}
	
	private void addAppInfo(LinearLayout view, RunningApp runningApp, int rank){
		PackageManager pm = getActivity().getApplicationContext().getPackageManager();
		ApplicationInfo appInfo;
		
		LayoutInflater li = LayoutInflater.from(getActivity().getApplicationContext());
		
		LinearLayout row = (LinearLayout)li.inflate(R.layout.app_row, null);
		
		ImageView bgRank = (ImageView)row.findViewById(R.id.bgAppRank);
		bgRank.setImageDrawable(imageLoader.getResizedDrawable(R.drawable.app_rank));
		ImageView bgInfo = (ImageView)row.findViewById(R.id.bgAppInfo);
		bgInfo.setImageDrawable(imageLoader.getResizedDrawable(R.drawable.app_name));
		ImageView bgRate = (ImageView)row.findViewById(R.id.bgAppRate);
		bgRate.setImageDrawable(imageLoader.getResizedDrawable(R.drawable.app_rate));
		ImageView bgCount = (ImageView)row.findViewById(R.id.bgAppCount);
		bgCount.setImageDrawable(imageLoader.getResizedDrawable(R.drawable.app_count));
		ImageView bgTime = (ImageView)row.findViewById(R.id.bgAppTime);
		bgTime.setImageDrawable(imageLoader.getResizedDrawable(R.drawable.app_time));
		
		ImageView appIcon = (ImageView)row.findViewById(R.id.AppIcon);
		
		TextView tRank = (TextView)row.findViewById(R.id.AppRank);
		TextView tName = (TextView)row.findViewById(R.id.AppName);
		TextView tRate = (TextView)row.findViewById(R.id.AppRate);
		TextView tCount = (TextView)row.findViewById(R.id.AppCount);
		TextView tTime = (TextView)row.findViewById(R.id.AppTime);
		
		String appName = "???";
		try{
			appInfo = pm.getApplicationInfo(runningApp.getPkgName(), 0);
			appName = (String) pm.getApplicationLabel(appInfo);
			appIcon.setImageDrawable(pm.getApplicationIcon(appInfo));
		}catch(NameNotFoundException e){
			e.printStackTrace();
		}
		fontLoader.setTextViewTextSize(tRank, String.valueOf(rank+1), (float)6.0);
		fontLoader.setTextViewTextSize(tName, appName, (float)6.0);
		
		//TODO:전체 앱 사용량에서의 비중?
		String rate = String.format("%5.1f", 
				(runningApp.getTime()*100/(float)app.getPlan().getUsage()))+"%";
		fontLoader.setTextViewTextSize(tRate, rate, (float)6.0);
		
		fontLoader.setTextViewTextSize(tCount, String.valueOf(runningApp.getCount()), (float)6.0);
		fontLoader.setTextViewTextSize(tTime, runningApp.getTimeFormat(), (float)6.0);
		
		ImageView appLine = (ImageView)row.findViewById(R.id.appLine);
		appLine.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.line));
		
		view.addView(row);
	}
	/*/
	private AnimationDrawable getAnime(int per){
		
		int duration = 50;
		AnimationDrawable d = new AnimationDrawable();
		String[] frameIds = getResources().getStringArray(R.array.circle_images);
		
		int max = (int)(frameIds.length * (per/100.0));
		
		Drawable draw = null;
		
		for(int i=0; i==0 || i<max; i++){
			draw = imageLoader.getResizedDrawable(getResources().getIdentifier(frameIds[i],"drawable",this.getActivity().getPackageName()));
			d.addFrame(draw, duration);
		}
		d.setOneShot(true);
		return d;
	}
	/**/
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
		updateView();
	}
	
	@Override
	public void onDestroyView(){
		super.onDestroyView();
	}
	
}
