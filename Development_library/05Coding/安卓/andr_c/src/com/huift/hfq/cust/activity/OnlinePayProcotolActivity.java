package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.OnlinePayProcotolFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 在线支付协议
 * @author yingchen
 *
 */
public class OnlinePayProcotolActivity extends SingleFragmentActivity {
	
	private static final String TAG = OnlinePayProcotolActivity.class.getSimpleName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "LoginActivity create");
	}
	
	@Override
	protected Fragment createFragment() {
		return OnlinePayProcotolFragment.newInstance();
	}
}
