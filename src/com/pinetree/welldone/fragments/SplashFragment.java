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
			setPasswd(profile);
		}else{
			moveNext();
		}
	}
	
	//패스워드 입력
	private void setPasswd(final ProfileModel profile){
		LayoutInflater inflater = getActivity().getLayoutInflater();

		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		View view = inflater.inflate(R.layout.alertdialog_passwd, null);
		
		TextView checkPasswd = (TextView)view.findViewById(R.id.checkPasswd);
		fontLoader.setTextViewTypeFace(
				checkPasswd,
				R.string.set_passwd,
				R.string.NanumGothic,
				(float)8.0);
		
		final EditText passwd = (EditText)view.findViewById(R.id.textPassword);
		passwd.setHint(R.string.passwd_message);
		
		alert.setView(view);
		
		alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String text = passwd.getText().toString();
				
				if(!text.equals("")){
					profile.setPasswd(text);
					setPasswd2(profile);
				}else{
					setPasswd(profile);
				}
			}
		});
		alert.show();
	}
	
	// 재확인
	private void setPasswd2(final ProfileModel profile){
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		View view = inflater.inflate(R.layout.alertdialog_passwd, null);
		
		TextView checkPasswd = (TextView)view.findViewById(R.id.checkPasswd);
		fontLoader.setTextViewTypeFace(
				checkPasswd,
				R.string.set_passwd2,
				R.string.NanumGothic,
				(float)8.0);
		
		final EditText passwd = (EditText)view.findViewById(R.id.textPassword);
		passwd.setHint(R.string.passwd_message);
		
		alert.setView(view);
		
		alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String text = passwd.getText().toString();
				
				if(!text.equals("")
						&& text.equals(profile.getPasswd())){
					app.updateProfile(profile);
					Toast.makeText(getActivity(), R.string.save_passwd, Toast.LENGTH_LONG).show();
					moveNext();
				}else{
					profile.setPasswd("");
					Toast.makeText(getActivity(), R.string.not_equal, Toast.LENGTH_LONG).show();
					setPasswd(profile);
				}
			}
		});
		alert.show();
	}
	
	private void moveNext(){
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
