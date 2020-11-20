//---------------------------------------------------------
//@author    yanfang.li
//@version   1.0.0
//@copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
//---------------------------------------------------------
package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.CouponUseDetailFragment;

/**
 * 优惠券使用详情界面 单Fragment
 * @author yanfang.li
 */
public class CouponUseDetailActivity extends SingleFragmentActivity {
	private static final String TAG = CouponUseDetailActivity.class.getSimpleName();

	@Override
	protected Fragment createFragment() {
		return CouponUseDetailFragment.newInstance();
	}
}
