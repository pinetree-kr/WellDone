package com.pinetree.welldone.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pinetree.welldone.R;
import com.pinetree.welldone.models.LogModel;
import com.pinetree.welldone.models.Model;
import com.pinetree.welldone.models.PackageModel.RunningApp;
import com.pinetree.welldone.models.PackageModel.RunningApp.AppUsageDescCompare;

public class ResultDialogFragment extends BaseDialogFragment {
	protected Model object;
	
	public static ResultDialogFragment getInstances(Model object){
		ResultDialogFragment dialog = new ResultDialogFragment();
		Bundle args = new Bundle();
		
		args.putSerializable("data", object);
		dialog.setArguments(args);
		
		return dialog;
	}

	/*/
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.fragment_dialog_result, null);
		setupUI(view);
		builder.setView(view);
		return builder.create();
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		Window window = getDialog().getWindow();
		window.requestFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.fragment_dialog_result, container, true);
		setupUI(view);
		return view;
		
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
		Window window = getDialog().getWindow();
		/**/
		window.setLayout(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		/**/
		/*/
		window.setLayout(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		/**/
		window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		super.onResume();
	}
	
	public void setupUI(View view){
		LogModel log = (LogModel) object;
		
		ImageView resultBg = (ImageView)view.findViewById(R.id.resultBg);
		resultBg.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.result_bg));
		
		ImageView empty_top2datebar = (ImageView)view.findViewById(R.id.empty_top2datebar);
		empty_top2datebar.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.empty_top2datebar));
		
		ImageView datebar = (ImageView)view.findViewById(R.id.datebar);
		datebar.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.result_date));
		
		TextView resultDate = (TextView)view.findViewById(R.id.ResultDate);
		fontLoader.setTextViewTypeFace(
				resultDate,
				log.getDateFormat()+" 총 사용시간",
				R.string.NanumGothicB,
				(float)6.0);
		
		ImageView usageBg = (ImageView)view.findViewById(R.id.usageBg);
		usageBg.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.empty_usagetext));
		
		TextView resultUsage = (TextView)view.findViewById(R.id.ResultUsage);
		fontLoader.setTextViewTypeFace(
				resultUsage,
				log.getUsageDialogFormat(),
				R.string.NanumGothicB,
				(float)15.0);
		
		ImageView top3title = (ImageView)view.findViewById(R.id.top3title);
		top3title.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.result_app_bar));

		ImageView empty_appbar2app = (ImageView)view.findViewById(R.id.empty_appbar2app);
		empty_appbar2app.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.empty_appbar2app));
		
		ViewGroup top3App = (ViewGroup)view.findViewById(R.id.top3App);
		setTop3App(top3App, log);
		
		ImageView resultX = (ImageView)view.findViewById(R.id.resultX);
		resultX.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.result_dismiss));
		resultX.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}
	
	private void setTop3App(ViewGroup view, LogModel log){
		ArrayList<RunningApp> apps = getApps(log, 3);
		
		view.removeAllViews();
		
		LayoutInflater li = LayoutInflater.from(getActivity().getApplicationContext());
		FrameLayout appInfo = null;
		ApplicationInfo ai = null;
		PackageManager pm = getActivity().getApplicationContext().getPackageManager();
		
		String[] iconRanks = getResources().getStringArray(R.array.app_rank);
		
		for(int i=0; i<apps.size(); i++){
			appInfo = (FrameLayout)li.inflate(R.layout.layout_app_info, null);
			ImageView appInfoBg = (ImageView)appInfo.findViewById(R.id.appInfoBg);
			appInfoBg.setImageDrawable(
					imageLoader.getResizedDrawable(R.drawable.app_pack));
			
			ImageView appIconBg = (ImageView)appInfo.findViewById(R.id.appIconBg);
			appIconBg.setImageDrawable(
					imageLoader.getResizedDrawable(R.drawable.icon_pack));

			ImageView appIconWrapBg = (ImageView)appInfo.findViewById(R.id.appIconWrapBg);
			appIconWrapBg.setImageDrawable(
					imageLoader.getResizedDrawable(R.drawable.icon_bg));

			ImageView appIcon = (ImageView)appInfo.findViewById(R.id.appIcon);
			
			try {
				ai = pm.getApplicationInfo(apps.get(i).getPkgName(), 0);
				appIcon.setImageDrawable(pm.getApplicationIcon(ai));
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			
			ImageView appRankIcon = (ImageView)appInfo.findViewById(R.id.appRankIcon);
			appRankIcon.setImageDrawable(
					imageLoader.getResizedDrawable(
							getResources().getIdentifier(
									iconRanks[i],
									"drawable",
									getActivity().getPackageName()
							)));
			
			view.addView(appInfo);
		}
	}
	private ArrayList<RunningApp> getApps(LogModel log, int maxCount){
		ArrayList<RunningApp> objects = new ArrayList<RunningApp>();
		try {
			JSONObject json = new JSONObject(log.getApps());
			Iterator itrApps = json.keys();
			while(itrApps.hasNext()){
				String key = (String)itrApps.next();
				JSONObject app = json.getJSONObject(key);
				objects.add(
						new RunningApp(
								key,
								app.getInt("time"),
								app.getInt("count"))
						);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//TODO : 앱순서
		Collections.sort(objects, new AppUsageDescCompare());
		//Collections.sort(objects, new AppCountDescCompare());
		for(int i=objects.size()-1; i>=maxCount; i--){
			objects.remove(i);
		}
		return objects;
	}
}
