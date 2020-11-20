package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.EditAcitityMsgFragment;

import android.app.Fragment;

/**
 * 编辑活动所有信息
 * @author qian.zhou
 */
public class EditAcitityMsgActivity extends SingleFragmentActivity {

	private final static String TAG = EditAcitityMsgActivity.class.getSimpleName();
	
	@Override
	protected Fragment createFragment() {
		return EditAcitityMsgFragment.newInstance();
	}
}
