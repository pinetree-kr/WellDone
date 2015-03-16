package com.pinetree.welldone.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pinetree.welldone.MainActivity;
import com.pinetree.welldone.R;
import com.pinetree.welldone.models.Model;
import com.pinetree.welldone.models.ProfileModel;
import com.pinetree.welldone.utils.PasswdUtil;

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
		
		ProfileModel profile = app.getProfile();

		if(profile.getPasswd().equals("")){
			Runnable r = new Runnable() {
				@Override
				public void run() {
					moveNext();
				}
			};
			PasswdUtil.setPasswd(getActivity(), fontLoader, profile, r);
		}else{
			moveNext();
		}
	}

	private void moveNext(){
		saInterface.switchActivity(new Intent(getActivity(), MainActivity.class), 500, true);
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
