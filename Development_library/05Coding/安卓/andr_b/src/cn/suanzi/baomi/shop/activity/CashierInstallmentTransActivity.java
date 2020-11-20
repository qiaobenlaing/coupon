package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.cashier.TransInstallmentFragment;

/**
 * @author hsm
 * 收银台-分期付款交易
 */
public class CashierInstallmentTransActivity extends SingleFragmentActivity {	
	
	@Override
	protected Fragment createFragment() {
		return TransInstallmentFragment.newInstance();
	}
}
