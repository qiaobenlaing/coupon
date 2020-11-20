package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.cashier.CashierIndexFragment;

/**
 * @author hsm
 * 收银台首页
 */
public class CashierActivity extends SingleFragmentActivity {	
	
	@Override
	protected Fragment createFragment() {
		return CashierIndexFragment.newInstance();
	}
}
