package com.huift.hfq.shop.activity;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.ConfirmOrderFragment;

import android.app.Fragment;
import android.view.KeyEvent;
 
/**
 * 扫码支付的确认订单界面
 * @author qian.zhou
 */
public class ConfirmOrderActivity extends SingleFragmentActivity {
	
	@Override
	protected Fragment createFragment() {
		return ConfirmOrderFragment.newInstance();
	}
	
}
