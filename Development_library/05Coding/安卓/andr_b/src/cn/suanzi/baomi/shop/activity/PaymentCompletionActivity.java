package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import android.view.KeyEvent;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.PaymentCompletionFragment;
 
/**
 * 扫码支付的确认订单界面(支付成功)
 * @author qian.zhou
 */
public class PaymentCompletionActivity extends SingleFragmentActivity {
	
	private final static String TAG = PaymentCompletionActivity.class.getSimpleName();
	
	private PaymentCompletionFragment mPaymentCompletion;
	@Override
	protected Fragment createFragment() {
		mPaymentCompletion =  PaymentCompletionFragment.newInstance();
		return mPaymentCompletion;
	}
	
	/**
	 * 点击返回
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mPaymentCompletion.scanBack();
			return false;
		}
		return true;
	}
}
