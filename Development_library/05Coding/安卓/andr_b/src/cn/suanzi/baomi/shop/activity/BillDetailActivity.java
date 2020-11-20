package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.BillDetailFragment;

/**
 * 账单详情
 * @author wensi.yu
 *
 */
public class BillDetailActivity extends SingleFragmentActivity{

	@Override
	protected Fragment createFragment() {
		return BillDetailFragment.newInstance();
	}

}
