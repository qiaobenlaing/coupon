package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.ActAddSelectDetailFragment;

import android.app.Fragment;

/**
 * @author  wensi.yu
 * 营销活动的预览活动详情
 */
public class ActAddSelectDetailActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return ActAddSelectDetailFragment.newInstance();
	}
}
