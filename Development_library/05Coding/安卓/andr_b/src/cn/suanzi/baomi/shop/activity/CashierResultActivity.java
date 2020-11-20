package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.cashier.CashierResultFragment;

/**
 * @author hsm
 * 收银台-打印交易
 */
public class CashierResultActivity extends SingleFragmentActivity {	
	
	@Override
	protected Fragment createFragment() {
		return CashierResultFragment.newInstance();
	}
}
