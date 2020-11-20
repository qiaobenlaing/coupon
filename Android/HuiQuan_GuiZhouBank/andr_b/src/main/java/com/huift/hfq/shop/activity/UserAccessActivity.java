package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.UserAccessFragment;

import android.app.Fragment;

/**
 * 用户统计
 * @author qian.zhou
 */
public class UserAccessActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return UserAccessFragment.newInstance();
	}
}
