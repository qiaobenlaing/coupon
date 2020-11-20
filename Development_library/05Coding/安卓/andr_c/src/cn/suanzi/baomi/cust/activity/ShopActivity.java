package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.ShopFragment;

/**
 * 商户列表
 * 
 * @author qian.zhou
 */
public class ShopActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return ShopFragment.newInstance();
	}

}
