package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.ActIcBcDetailFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 查看工行特惠的活动详情
 * @author wensi.yu
 */
public class ActIcBcDetailActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return ActIcBcDetailFragment.newInstance();
	}		
}
