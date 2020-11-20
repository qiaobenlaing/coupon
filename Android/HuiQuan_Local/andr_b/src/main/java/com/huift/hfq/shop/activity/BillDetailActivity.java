package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.BillDetailFragment;

import android.app.Fragment;
/**
 * 账单详情
 * @author wensi.yu
 *
 */
public class BillDetailActivity extends SingleFragmentActivity{

	@Override
	protected Fragment createFragment() {
		return BillDetailFragment.newInstance();
	}

}
