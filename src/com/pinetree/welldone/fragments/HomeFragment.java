package com.pinetree.welldone.fragments;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pinetree.views.RoundedDrawable;
import com.pinetree.welldone.R;
import com.pinetree.welldone.models.PlanModel;
import com.pinetree.welldone.models.ProfileModel;
import com.pinetree.welldone.utils.DBHandler;
import com.pinetree.welldone.utils.ViewUtil;

public class HomeFragment extends BaseFragment{
	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int REQUEST_IMAGE_ALBUM = 2;
	private static final int REQUEST_IMAGE_CROP = 3;
	
	protected ProgressDialog dialog;
	
	protected ImageView circle;
	protected AnimationDrawable ad;
	
	protected ImageView titleBar, profile, photo;
	protected TextView message, usage, remain;
	
	private Uri contentUri;
	private String currentFilePath;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(false);
		File path = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES);		
		//Log.i("DebugPrint","path:"+path.getAbsolutePath());
		if(!path.exists()) {
			path.mkdirs();	
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		setComponents(view);
		return view;
	}
	
	/**/
	private void setComponents(View view){
		ImageView dateBg = (ImageView)view.findViewById(R.id.dateBg);
		dateBg.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.bar));
		
		TextView date = (TextView)view.findViewById(R.id.date);
		fontLoader.setTextViewTypeFace(date, app.getToday(), R.string.NanumGothicB, (float)10);
		
		ImageView empty_title2circle = (ImageView)view.findViewById(R.id.empty_title2circle);
		empty_title2circle.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.empty_message2usage));
		ImageView empty_message2circle = (ImageView)view.findViewById(R.id.empty_message2circle);
		empty_message2circle.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.empty_message2circle));
		
		// set anime circle
		circle = (ImageView)view.findViewById(R.id.anime);
		
		photo = (ImageView)view.findViewById(R.id.photo);
		ProfileModel profile = app.getProfile();
		if(profile.getFilePath().equals("")){
			setProfile(
					imageLoader.getResizedBitmapWidely(
							R.drawable.cloud,
							(int)(186*app.getScaledDensity())));
		}else{
			try {
				setProfile(
						imageLoader.getResizedBitmapWidely(
								imageLoader.getBitmap(
										getActivity().getContentResolver(),
										profile.getFileUri()),
										(int)(186*app.getScaledDensity())
										));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		registerForContextMenu(photo);

		// 메시지
		ImageView bgMessage = (ImageView)view.findViewById(R.id.bgMessage);
		bgMessage.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.message));
		bgMessage.setOnLongClickListener(new OnBtnLongClickListener());
		ImageView empty_message_top = (ImageView)view.findViewById(R.id.empty_message_top);
		empty_message_top.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.empty_message_top));
		
		ImageView bgMessageBox = (ImageView)view.findViewById(R.id.bgMessageBox);
		bgMessageBox.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.message_text));
		
		ImageView empty_left2message = (ImageView)view.findViewById(R.id.empty_left2message);
		empty_left2message.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.empty_align2horizontal));
		ImageView empty_left2remain = (ImageView)view.findViewById(R.id.empty_left2remain);
		empty_left2remain.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.empty_align2horizontal));
		ImageView empty_message2usage = (ImageView)view.findViewById(R.id.empty_message2usage);
		empty_message2usage.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.empty_message2usage));
		ImageView empty_right2usage = (ImageView)view.findViewById(R.id.empty_right2usage);
		empty_right2usage.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.empty_align2horizontal));
		ImageView empty_circle2remain = (ImageView)view.findViewById(R.id.empty_circle2remain);
		empty_circle2remain.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.empty_message2usage));
		
		
		
		message = (TextView)view.findViewById(R.id.messageBox);
		
		
		// 고래
		ImageView whale = (ImageView)view.findViewById(R.id.whale);
		whale.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.whale_1));
		
		TextView usageText = (TextView)view.findViewById(R.id.usageText);
		TextView remainText = (TextView)view.findViewById(R.id.remainText);
		fontLoader.setTextViewTypeFace(
				usageText, R.string.NanumGothic, (float)6);
		fontLoader.setTextViewTypeFace(
				remainText, R.string.NanumGothic, (float)8);
		
		usage = (TextView)view.findViewById(R.id.usage);
		remain = (TextView)view.findViewById(R.id.remain);
		
		
		ImageView empty_circle2bottombar = (ImageView)view.findViewById(R.id.empty_circle2bottombar);
		empty_circle2bottombar.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.empty_message2usage));
		
		ImageView weekStatus = (ImageView)view.findViewById(R.id.weekStatus);
		weekStatus.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.weekstatus_bar));
		
		// 일주일
		LinearLayout weekInfo = (LinearLayout)view.findViewById(R.id.weekInfo);
		
		ViewUtil.addWeekLabel(getActivity().getApplicationContext(), weekInfo);
		ViewUtil.addWeek(
				getActivity().getApplicationContext(),
				DBHandler.getInstance(getActivity().getApplicationContext(), true),
				weekInfo);
			}
	
	private void updateView(){
		PlanModel plan = app.getPlan();
		//사용시간
		fontLoader.setTextViewTypeFace(
				usage, plan.getUsageFormat(), R.string.NanumGothicB, (float)18.0);
		// 남은시간
		fontLoader.setTextViewTypeFace(
				remain, plan.getRemainFormat(), R.string.NanumGothicB, (float)20.0);
		
		updateMessageBox();
		updateCircle(plan.getUsageRate());
	}
	private AnimationDrawable getAnime(int rate){
		int duration = 50;
		AnimationDrawable d = new AnimationDrawable();
		String[] frameIds = getResources().getStringArray(R.array.circle_images);
		
		int max = (int)((frameIds.length-1) * (rate/100.0));
		
		Drawable draw = null;
		for(int i=0; i==0 || i<=max; i++){
			draw = imageLoader.getResizedDrawable(
					getResources().getIdentifier(
							frameIds[i],"drawable",this.getActivity().getPackageName()));
			d.addFrame(draw, duration);
		}
		d.setOneShot(true);
		return d;
	}
	private void updateCircle(int rate){
		circle.setImageDrawable(getAnime(rate));
		ad = (AnimationDrawable)circle.getDrawable();
		if(ad!=null){
			ad.start();
		}
		//circle.setOnClickListener(new OnBtnClickListener());
	}
	
	private void updateMessageBox(){
		//메시지박스
		fontLoader.setTextViewTypeFace(
				message, app.getProfile().getMessage(), R.string.NanumGothic, (float)7.25);
	}
	
	private void onUpdateMessageBox(){
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final ProfileModel p = app.getProfile();

		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		View view = inflater.inflate(R.layout.alertdialog_profile_message, null);
		
		TextView checkMessage = (TextView)view.findViewById(R.id.checkMessage);
		fontLoader.setTextViewTypeFace(
				checkMessage,
				R.string.NanumGothic,
				(float)8.0);
		
		final EditText message = (EditText)view.findViewById(R.id.textMessage);
		message.setHint(p.getMessage());
		
		alert.setView(view);
		alert.setPositiveButton("저장", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String text = message.getText().toString();
				if(text.equals("")){
					text = message.getHint().toString();
				}
				p.setMessage(text);
				app.updateProfile(p);
				updateMessageBox();
			}
		});
		alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alert.show();
	}
	
	private File createImageFile() throws IOException{
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
				imageFileName,
				".jpg",
				storageDir
				);
		currentFilePath = image.getAbsolutePath();
		return image;
	}
	private void dispatchTakePictureIntent(){
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
			File photoFile = null;
			try {
	            photoFile = createImageFile();
	        } catch (IOException ex) {
	        	
	        }
			if (photoFile != null) {
				contentUri = Uri.fromFile(photoFile);
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						contentUri);
				startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
			}
		}
	}
	private void getAlbumPictureIntent(){
		Intent getAlbumPictureIntent = new Intent(Intent.ACTION_PICK,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI
				);
		startActivityForResult(getAlbumPictureIntent, REQUEST_IMAGE_ALBUM);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode == getActivity().RESULT_OK){
			switch(requestCode){
			case REQUEST_IMAGE_ALBUM:
				contentUri = data.getData();
			case REQUEST_IMAGE_CAPTURE:
				rotatePhoto();
				cropImage(contentUri);
				break;
			case REQUEST_IMAGE_CROP:
				Bundle extras = data.getExtras();
				if(extras != null){
					Bitmap bitmap = (Bitmap)extras.get("data");
					setProfile(
							imageLoader.getResizedBitmapWidely(
									bitmap,
									(int)(186*app.getScaledDensity())
									));
					
					//Uri uri = data.getData();
					Uri uri = imageLoader.getImageUri(
							getActivity().getApplicationContext(), bitmap);
					ProfileModel profile = app.getProfile();
					profile.setFilePath(uri);
					app.updateProfile(profile);
					
					if(currentFilePath != null){
						File f = new File(currentFilePath);
						if(f.exists()) {
							f.delete();
						}
						currentFilePath = null;
					}
				}
				break;
			}
		}
	}
	private void setProfile(Bitmap bitmap){
		RoundedDrawable rd = new RoundedDrawable(bitmap);
		photo.setImageDrawable(rd);
	}
	
	private void cropImage(Uri contentUri) {
		Intent cropIntent = new Intent("com.android.camera.action.CROP");
		cropIntent.setDataAndType(contentUri, "image/*");
		
		cropIntent.putExtra("crop", "true");
		cropIntent.putExtra("aspectX", 1);
		cropIntent.putExtra("aspectY", 1);
		cropIntent.putExtra("outputX", 256);
		cropIntent.putExtra("outputY", 256);
		cropIntent.putExtra("return-data", true);
		startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
	}
	
	public void rotatePhoto() {
		ExifInterface exif;
		try {
			if(currentFilePath == null) {
				currentFilePath = contentUri.getPath();
			}
			exif = new ExifInterface(currentFilePath);
		    int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);	    
		    int exifDegree = exifOrientationToDegrees(exifOrientation);
		    if(exifDegree != 0) {
		    	Bitmap bitmap = getBitmap();			    	
			    Bitmap rotatePhoto = rotate(bitmap, exifDegree);
			    saveBitmap(rotatePhoto);			    			    
		    }	    					
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	public int exifOrientationToDegrees(int exifOrientation){
		if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90){
			return 90;
		}else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180){
			return 180;
		}else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270){
			return 270;
		}else{
			return 0;
		}
	}
	public Bitmap getBitmap() {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inInputShareable = true;
	    options.inDither=false;
	    options.inTempStorage=new byte[32 * 1024];
	    options.inPurgeable = true;
	    options.inJustDecodeBounds = false;
	    
	    File f = new File(currentFilePath);
	    FileInputStream fs=null;
	    try {
			fs = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    Bitmap bm = null;
		try {
			if(fs!=null)
				bm=BitmapFactory.decodeFileDescriptor(fs.getFD(), null, options);
		} catch (IOException e) {
			e.printStackTrace();
		} finally{ 
	        if(fs!=null) {
	            try {
	                fs.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
		return bm;
	}
	public static Bitmap rotate(Bitmap image, int degrees){
		if(degrees != 0 && image != null){
			Matrix m = new Matrix();
			m.setRotate(degrees, (float)image.getWidth(), (float)image.getHeight());
			
			try{
				Bitmap b = Bitmap.createBitmap(
						image, 0, 0, image.getWidth(), image.getHeight(), m, true);
				
				if(image != b){
					image.recycle();
					image = b;
				}
				image = b;
			}catch(OutOfMemoryError ex){
				ex.printStackTrace();
			}
		}
		return image;
	}
	public void saveBitmap(Bitmap bitmap) {
		File file = new File(currentFilePath);
		OutputStream out = null;
		try {
			out = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bitmap.compress(CompressFormat.JPEG, 100, out) ;
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu,
			View v, ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.photo_menu, menu);
	}
	@Override
	public boolean onContextItemSelected(MenuItem item){
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		
		switch(item.getItemId()){
		case R.id.camera:
			dispatchTakePictureIntent();
			return true;
		case R.id.album:
			getAlbumPictureIntent();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onStart(){
		super.onStart();
		//ad.start();
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
	
	private class OnBtnLongClickListener implements OnLongClickListener{
		@Override
		public boolean onLongClick(View v) {
			if(v.getId()==R.id.bgMessage){
				onUpdateMessageBox();
				return true;
			}
			return false;
		}
	}
}
