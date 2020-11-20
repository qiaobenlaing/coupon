package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.BillHomeFragment;

public class BillHomeActivity extends SingleFragmentActivity{

	@Override
	protected Fragment createFragment() {
		return BillHomeFragment.newInstance();
	}

}
