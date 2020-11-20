package com.huift.hfq.cust.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.fragment.ICBCPaySuccessFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 工行支付输入密码
 * 
 * @author Zhonghui.Dong
 * 
 */
public class ICBCPasswordActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_icbc_password);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		ViewUtils.inject(this);
	}

	@OnClick({ R.id.cancel, R.id.submit })
	private void click(View view) {
		Intent intent;
		switch (view.getId()) {
		case R.id.cancel:
			
			finish();
			break;
		case R.id.submit:
			intent = new Intent(this, ICBCPaySuccessActivity.class);
			intent.putExtra(ICBCPaySuccessFragment.SUCCESS, ICBCPaySuccessFragment.SUCCESS_FALSE);
			startActivity(intent);
			break;
		}
	}
	
	 public void onResume(){
	    	super.onResume();
	        AppUtils.setActivity(this);
	        AppUtils.setContext(getApplicationContext());
	  }

}
