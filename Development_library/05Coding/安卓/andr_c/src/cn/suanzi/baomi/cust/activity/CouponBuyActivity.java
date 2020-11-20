package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.CouponBuyFragment;

/**
 * 购买优惠券
 * @author yingchen
 */
public class CouponBuyActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return CouponBuyFragment.newInstance();
	}

}
