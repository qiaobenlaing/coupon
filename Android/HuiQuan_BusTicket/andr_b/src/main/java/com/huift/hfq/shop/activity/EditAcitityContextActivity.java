package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.EditAcitityContextFragment;

import android.app.Fragment;

/**
 * 编辑活动
 * @author qian.zhou
 */
public class EditAcitityContextActivity extends SingleFragmentActivity {

	private final static String TAG = EditAcitityContextActivity.class.getSimpleName();
	
	@Override
	protected Fragment createFragment() {
		return EditAcitityContextFragment.newInstance();
	}
}
