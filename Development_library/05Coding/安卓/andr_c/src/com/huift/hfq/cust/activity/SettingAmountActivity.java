package com.huift.hfq.cust.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioGroup;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.cust.R;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class SettingAmountActivity extends Activity {
	@ViewInject(R.id.small_amount_switch)
	private CheckBox mCheckBox;
	@ViewInject(R.id.small_amount)
	private RadioGroup mRadioGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_myhome_setting_amount);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		init();
		
	}

	private void init() {
		ViewUtils.inject(this);
		if (mCheckBox.isChecked()) {
			mRadioGroup.setVisibility(View.VISIBLE);
		} else {
			mRadioGroup.setVisibility(View.GONE);
		}
		mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					mRadioGroup.setVisibility(View.VISIBLE);
				} else {
					mRadioGroup.setVisibility(View.GONE);
				}
			}
		});
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
