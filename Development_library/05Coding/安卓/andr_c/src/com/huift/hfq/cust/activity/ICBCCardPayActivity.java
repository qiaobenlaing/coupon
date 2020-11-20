package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.ICBCCardFragment;

import android.app.Fragment;
import android.view.KeyEvent;
import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.base.Util;
import com.huift.hfq.cust.R;

public class ICBCCardPayActivity extends SingleFragmentActivity {

	private static boolean payFlag = false;
	@Override
	protected Fragment createFragment() {
		return ICBCCardFragment.newInstance();
	}
	
	public static void setPayFlag () {
		payFlag = true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			if (!payFlag) {
				Util.getContentValidate(R.string.pay_return);
				return payFlag;
			} else {
				this.finish();
			}
			
		}
		return super.onKeyDown(keyCode, event);
	}

}
