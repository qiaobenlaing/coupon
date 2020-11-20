package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.ShopIcbcFragment;

/**
 * 工银特惠
 * @author yanfang.li
 */
public class ShopIcbcActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return ShopIcbcFragment.newInstance();
	}

}
