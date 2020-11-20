package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import android.view.KeyEvent;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.ConfirmOrderFragment;
 
/**
 * 扫码支付的确认订单界面
 * @author qian.zhou
 */
public class ConfirmOrderActivity extends SingleFragmentActivity {
	
	private final static String TAG = ConfirmOrderActivity.class.getSimpleName();
	
	private ConfirmOrderFragment mConfirmOrderFragment;
	
	@Override
	protected Fragment createFragment() {
		mConfirmOrderFragment =  ConfirmOrderFragment.newInstance();
		return mConfirmOrderFragment;
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mConfirmOrderFragment.cancelBankcardPay();
			return false;
		}
		return super.onKeyDown(keyCode, event);
		
	}
	
}
