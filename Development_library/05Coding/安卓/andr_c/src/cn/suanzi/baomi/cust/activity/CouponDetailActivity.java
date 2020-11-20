package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.CouponDetailFragment;

/**
 * 优惠券详情界面
 * 
 * @author yanfang.li
 */
public class CouponDetailActivity extends SingleFragmentActivity {
	private static final String TAG = CouponDetailActivity.class.getSimpleName();

	@Override
	protected Fragment createFragment() {
		return CouponDetailFragment.newInstance();
	}
	
}
