package com.pinetree.welldone;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.pinetree.welldone.fragments.HomeFragment;
import com.pinetree.welldone.fragments.PlanFragment;
import com.pinetree.welldone.fragments.ResultFragment;
import com.pinetree.welldone.fragments.StatusFragment;
import com.pinetree.welldone.models.Model;

public class MainActivity extends BaseActivity {
	
	private String currentFragment; 
	
	private Integer[] tabIconRes ={
		R.drawable.tab_01,
		R.drawable.tab_02,
		R.drawable.tab_03,
		R.drawable.tab_04
	};
	private Integer[] tabBtnId ={
			R.id.tabHome,
			R.id.tabTimeA,
			R.id.tabTimeB,
			R.id.tabGift,
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setWindowAnimations(android.R.style.Animation_Toast);
		
		// Version Display
        setContentView(R.layout.activity_main);
        
        //getApps();
        // Set TabMenu
        setTabMenu();
        
        if(savedInstanceState == null){
        	Fragment fragment = new HomeFragment();
        	currentFragment = "home";
			switchFragment(fragment, true);
		}
	}
	
	private void setTabMenu(){
		ImageView imageBg = (ImageView)findViewById(R.id.tabBg);
		
		imageBg.setImageDrawable(
				imageLoader.getResizedDrawable(R.drawable.main_frame));
		
		ImageView[] menu = new ImageView[4];
		for(int i=0; i<menu.length; i++){
			menu[i] = (ImageView) findViewById(tabBtnId[i]);
			switch(i){
			case 0:
				menu[i].setTag("home");
				break;
			case 1:
				menu[i].setTag("status");
				break;
			case 2:
				menu[i].setTag("plan");
				break;
			case 3:
				menu[i].setTag("result");
				break;
			}
			
			menu[i].setImageDrawable(
					imageLoader.getResizedDrawable(tabIconRes[i]));
			menu[i].setOnClickListener(new OnBtnClickListener());
		}
	}
	
	protected class OnBtnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			String tag = (String)v.getTag();
			Fragment fragment = null;
			if(tag.equals("home") && !tag.equals(currentFragment)){
				fragment = new HomeFragment();
			}else if(tag.equals("status") && !tag.equals(currentFragment)){
				fragment = new StatusFragment();
			}else if(tag.equals("plan") && !tag.equals(currentFragment)){
				fragment = new PlanFragment();
			}else if(tag.equals("result") && !tag.equals(currentFragment)){
				fragment = new ResultFragment();
			}
			
			if(fragment!=null){
				currentFragment = tag;
				switchFragment(fragment, true);
			}
		}
	}

	@Override
	public void onDialogClick(DialogFragment dialog, Model data) {
		BaseFragmentListener listener = (BaseFragmentListener)dialog.getTargetFragment();
		listener.onDialogClick(data);
	}
}
