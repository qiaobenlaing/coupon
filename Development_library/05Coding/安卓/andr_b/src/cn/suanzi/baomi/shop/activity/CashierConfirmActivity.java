package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.cashier.CashierConfirmFragment;

/**
 * @author hsm
 * 收银台-操作确认
 */
public class CashierConfirmActivity extends SingleFragmentActivity {	
	
	@Override
	protected Fragment createFragment() {
		return CashierConfirmFragment.newInstance();
	}
}
