package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.CardInfoFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 会员卡列表
 * @author qian.zhou
 */
public class CardInfoActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return CardInfoFragment.newInstance();
	}
}
