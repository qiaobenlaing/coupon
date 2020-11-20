package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.BuyPromotionFragment;

/**
 * 购买活动
 * @author yingchen
 */
public class BuyPromotionActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		BuyPromotionFragment fragment = BuyPromotionFragment.newInstance();
		return fragment;
	}

}
