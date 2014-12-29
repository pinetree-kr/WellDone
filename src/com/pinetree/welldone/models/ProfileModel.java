package com.pinetree.welldone.models;

import android.net.Uri;

public class ProfileModel extends Model{
	private final String DEFAULT_PASSWD = "whale2015"; 
	private String filePath;
	
	private String message;
	private String passwd;
	public ProfileModel(){
		filePath = "";
		message = "";
		passwd = DEFAULT_PASSWD;
	}
	public void setPasswd(String passwd){
		this.passwd = passwd;
	}
	public String getPasswd(){
		return passwd;
	}
	public void setFilePath(String path){
		filePath = path;
	}
	public void setFilePath(Uri path){
		filePath = path.toString();
	}
	public String getFilePath(){
		return filePath;
	}
	public Uri getFileUri(){
		return Uri.parse(filePath);
	}
	public void setMessage(String message){
		this.message = message;
	}
	public String getMessage(){
		if(message.trim().equals("")){
			return "나의 각오를 적어봅시다";
		}else{
			return message;
		}
	}
	
}


