package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.cashier.TransQueryFragment;

/**
 * @author hsm
 * 收银台-查询明细交易
 */
public class CashierQueryTransActivity extends SingleFragmentActivity {	
	
	@Override
	protected Fragment createFragment() {
		return TransQueryFragment.newInstance();
	}
}
