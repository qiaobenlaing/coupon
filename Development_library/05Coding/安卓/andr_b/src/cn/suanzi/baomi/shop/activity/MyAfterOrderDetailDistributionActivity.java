package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.MyAfterOrderDetailDistributionFragment;

/**
 * 订单详情餐后支付--待配送
 * @author wensi.yu
 *
 */
public class MyAfterOrderDetailDistributionActivity extends SingleFragmentActivity {

	private final static String TAG = "MyAfterOrderDetailDistributionActivity";
	
	@Override
	protected Fragment createFragment() {
		return MyAfterOrderDetailDistributionFragment.newInstance();
	}

}
