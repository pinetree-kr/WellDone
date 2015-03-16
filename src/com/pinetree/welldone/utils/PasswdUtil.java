package com.pinetree.welldone.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pinetree.library.utils.FontLoader;
import com.pinetree.welldone.R;
import com.pinetree.welldone.models.ProfileModel;

public class PasswdUtil {

	public static void setPasswd(final Activity activity, final FontLoader fontLoader,
	                             final ProfileModel profile) {
		setPasswd(activity, fontLoader, profile, null);
	}
	public static void setPasswd(final Activity activity, final FontLoader fontLoader,
	                             final ProfileModel profile, final Runnable runnable) {
		LayoutInflater inflater = activity.getLayoutInflater();

		AlertDialog.Builder alert = new AlertDialog.Builder(activity);
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
					setPasswd2(activity, fontLoader, profile, runnable);
				}else{
					setPasswd(activity, fontLoader, profile, runnable);
				}
			}
		});
		alert.show();
	}

	// 재확인
	private static void setPasswd2(final Activity activity, final FontLoader fontLoader,
	                               final ProfileModel profile, final Runnable runnable){
		LayoutInflater inflater = activity.getLayoutInflater();
		final DeviceInfo app = (DeviceInfo) activity.getApplicationContext();

		AlertDialog.Builder alert = new AlertDialog.Builder(activity);
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
					Toast.makeText(activity, R.string.save_passwd, Toast.LENGTH_LONG).show();
					if (runnable != null) runnable.run();
				}else{
					profile.setPasswd("");
					Toast.makeText(activity, R.string.not_equal, Toast.LENGTH_LONG).show();
					setPasswd(activity, fontLoader, profile, runnable);
				}
			}
		});
		alert.show();
	}
}
