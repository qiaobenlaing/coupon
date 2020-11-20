package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.SysWechatFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 微信公众号
 * @author ad
 */
public class SysWechatActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return SysWechatFragment.newInstance();
	}

}
