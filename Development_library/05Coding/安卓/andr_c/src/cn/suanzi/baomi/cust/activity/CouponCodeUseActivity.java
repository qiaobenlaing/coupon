package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.CouponCodeUseFragment;
/**
 * 优惠券兑换码使用之后界面
 * @author yingchen
 *
 */
public class CouponCodeUseActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return CouponCodeUseFragment.newInstance();
	}

}
