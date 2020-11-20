package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.ActivityManagerFragment;

import android.app.Fragment;
 
/**
 * 活动管理界面
 * @author qian.zhou
 */
public class ActivityManagerActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return ActivityManagerFragment.newInstance();
	}
}
