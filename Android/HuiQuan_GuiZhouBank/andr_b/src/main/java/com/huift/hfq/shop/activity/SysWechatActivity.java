package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.SysWechatFragment;

import android.app.Fragment;

/**
 *  微信公共帐号页面
 * @author qian.zhou
 */
public class SysWechatActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return SysWechatFragment.newInstance();
	}

}
