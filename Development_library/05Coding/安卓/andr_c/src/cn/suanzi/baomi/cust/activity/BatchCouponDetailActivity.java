package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.BatchCouponDetailFragment;

/**
 * 同一批次优惠券详情
 * @author yanfang.li
 */
public class BatchCouponDetailActivity extends SingleFragmentActivity {
	
	private static final String TAG = BatchCouponDetailActivity.class.getSimpleName();

	@Override
	protected Fragment createFragment() {
		return BatchCouponDetailFragment.newInstance();
	}
}
