package com.pinetree.welldone.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinetree.utils.FontLoader;
import com.pinetree.utils.ImageLoader;
import com.pinetree.utils.ViewHolder;
import com.pinetree.welldone.R;
import com.pinetree.welldone.utils.DeviceInfo;

public class SpinnerAdapter<T> extends ArrayAdapter<T>{
	protected LayoutInflater inflater;
	protected ArrayList<T> objects;
	protected DeviceInfo app;
	protected FontLoader fontLoader;
	protected ImageLoader imageLoader;
	
	public SpinnerAdapter(Context context, int resource, ArrayList<T> objects) {
		super(context, resource, objects);
		inflater = LayoutInflater.from(context);
		this.objects = objects;
		
		app = (DeviceInfo)context;
		fontLoader = new FontLoader(context);
		imageLoader = new ImageLoader(context.getResources(), app.getScaledRate());
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent){
		return getSpinnerView(position, convertView, parent);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		return getSpinnerView(position, convertView, parent);
	}
	
	public View getSpinnerView(int position, View convertView, ViewGroup parent){
		View view = convertView;
		
		T object = objects.get(position);
		
		if(view == null){
			view = inflater.inflate(R.layout.spinner_alarm_row, parent, false);
		}
		String type = (String)object;
		ImageView spinnerBg = ViewHolder.get(view, R.id.spinnerBg);
		
		spinnerBg.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.alarm_type_bg_blank));
		
		TextView spinnerText = ViewHolder.get(view, R.id.spinnerText);
		spinnerText.setText(type);
		
		return view;
	}
}
