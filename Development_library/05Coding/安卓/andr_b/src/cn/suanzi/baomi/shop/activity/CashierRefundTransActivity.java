package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.cashier.TransRefundFragment;

/**
 * @author hsm
 * 收银台-退货交易
 */
public class CashierRefundTransActivity extends SingleFragmentActivity {	
	
	@Override
	protected Fragment createFragment() {
		return TransRefundFragment.newInstance();
	}
}
