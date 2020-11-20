package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.MyUppManagerFragment;

import android.app.Fragment;

/**
 * 修改店长信息
 * @author qian.zhou 
 */
public class UppStaffActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return MyUppManagerFragment.newInstance();
	}
}
