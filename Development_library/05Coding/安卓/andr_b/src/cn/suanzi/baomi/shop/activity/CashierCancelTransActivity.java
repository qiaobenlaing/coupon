package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.cashier.TransCancelFragment;

/**
 * @author hsm
 * 收银台-撤销交易
 */
public class CashierCancelTransActivity extends SingleFragmentActivity {	
	
	@Override
	protected Fragment createFragment() {
		return TransCancelFragment.newInstance();
	}
}
