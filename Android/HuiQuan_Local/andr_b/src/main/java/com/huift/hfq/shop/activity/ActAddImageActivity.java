package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.ActAddImageFragment;

import android.app.Fragment;

/**
 * @author wensi.yu
 * 添加营销活动中的预览
 */
public class ActAddImageActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {		
		return ActAddImageFragment.newInstance();
	}	
}
