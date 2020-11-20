package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.CouponVerificationFragment;

/**
 * 对兑换券和代金券的验证
 * @author qian.zhou
 */
public class CouponVerificationActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return CouponVerificationFragment.newInstance();
	}
}
