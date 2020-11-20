package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.ICBCPaySuccessFragment;

import android.app.Fragment;
import android.view.KeyEvent;
import com.huift.hfq.base.SingleFragmentActivity;

public class ICBCPaySuccessActivity extends SingleFragmentActivity {
		
	private ICBCPaySuccessFragment mICBCPaySuccessFragment;
	@Override
	protected Fragment createFragment() {
		mICBCPaySuccessFragment = ICBCPaySuccessFragment.newInstance();
		return mICBCPaySuccessFragment;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			mICBCPaySuccessFragment.onBackPressedFragment();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
