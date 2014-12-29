package com.pinetree.welldone.fragments;


import java.util.Calendar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pinetree.welldone.R;
import com.pinetree.welldone.models.LogModel;
import com.pinetree.welldone.utils.DBHandler;
import com.pinetree.welldone.utils.ViewUtil;

public class ResultFragment extends BaseFragment{
	protected ProgressDialog dialog;
	protected TextView yearMonth;
	protected LinearLayout monthView, weekView; 
	
	protected int month_offset;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(false);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_result, container, false);
		
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
		
		// 일주일
		weekView = (LinearLayout)view.findViewById(R.id.weekInfo);
		
		//고래
		ImageView whale = (ImageView)view.findViewById(R.id.whale);
		whale.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.whale_4));
		//별
		ImageView star = (ImageView)view.findViewById(R.id.star);
		star.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.starwater_4));
		//선물박스
		ImageView giftbox = (ImageView)view.findViewById(R.id.giftbox);
		giftbox.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.giftbox_4));
		//타이틀바
		ImageView titleResult = (ImageView)view.findViewById(R.id.titleResult);
		titleResult.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.result_title));
		
		// 버튼
		ImageView leftBtn = (ImageView)view.findViewById(R.id.leftBtn);
		leftBtn.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.circlearrow_a));
		leftBtn.setTag(-1);
		leftBtn.setOnClickListener(new OnBtnClickListener());
		ImageView rightBtn = (ImageView)view.findViewById(R.id.rightBtn);
		rightBtn.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.circlearrow_b));
		rightBtn.setTag(1);
		rightBtn.setOnClickListener(new OnBtnClickListener());
		
		yearMonth = (TextView)view.findViewById(R.id.yearMonth);
		monthView = (LinearLayout)view.findViewById(R.id.monthInfo);
		
		LinearLayout monthLabelView = (LinearLayout)view.findViewById(R.id.monthLabel);
		ViewUtil.addWeekLabel(getActivity().getApplicationContext(), monthLabelView);
	}
	
	private void updateView(){
		updateThisWeek(weekView);
		
		month_offset = 0;
		updateYearMonth(0);
	}
	
	private void updateThisWeek(ViewGroup view){
		view.removeAllViews();
		ViewUtil.addWeekLabel(getActivity().getApplicationContext(), view);
		ViewUtil.addWeek(
				getActivity().getApplicationContext(),
				DBHandler.getInstance(getActivity().getApplicationContext(), true),
				view);
	}
	
	private void updateYearMonth(int offset){
		month_offset += offset;
		fontLoader.setTextViewTextSize(
				yearMonth,
				app.getYearMonthFormat(month_offset),
				(float)8.0);
		
		ViewUtil.updateMonth(
				getActivity().getApplicationContext(),
				getFragmentManager(),
				DBHandler.getInstance(getActivity().getApplicationContext(), true),
				monthView,
				app.getYearMonth(month_offset)
				);
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
		updateView();
	}
	
	@Override
	public void onDestroyView(){
		super.onDestroyView();
	}
	
	private class OnBtnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			int offset = (Integer)v.getTag();
			updateYearMonth(offset);
		}
	}
}
