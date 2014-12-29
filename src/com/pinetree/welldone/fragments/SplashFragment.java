package com.pinetree.welldone.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pinetree.welldone.MainActivity;
import com.pinetree.welldone.R;

public class SplashFragment extends BaseFragment {
	protected ProgressDialog dialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(false);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_splash, container, false);
		
		ImageView splashLogo = (ImageView)view.findViewById(R.id.splashLogo);
		
		splashLogo.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.splash_logo));
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onStart(){
		super.onStart();
		saInterface.switchActivity(new Intent(getActivity(), MainActivity.class), 2000, true);
	}
	@Override
	public void onResume(){
		super.onResume();
		
	}
	
	@Override
	public void onDestroyView(){
		super.onDestroyView();
	}
	
}
