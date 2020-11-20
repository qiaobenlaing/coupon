package com.huift.hfq.cust.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.cust.R;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class SettingSetPasswordActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_myhome_setting_setpassword);
		ViewUtils.inject(this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}

	@OnClick({ R.id.iv_turn_in })
	public void click(View v) {
		switch (v.getId()) {
		case R.id.iv_turn_in:
			finish();
			break;
		}
	}
	
	 public void onResume(){
	    	super.onResume();
	        AppUtils.setActivity(this);
	        AppUtils.setContext(getApplicationContext());
	    }
}
