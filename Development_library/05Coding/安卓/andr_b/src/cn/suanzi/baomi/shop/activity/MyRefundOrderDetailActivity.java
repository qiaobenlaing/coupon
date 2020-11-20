package cn.suanzi.baomi.shop.activity;
import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.MyRefundOrderDetailFragment;
 
/**
 * 退款订单详情页面
 * @author qian.zhou
 */
public class MyRefundOrderDetailActivity extends SingleFragmentActivity {
	private static final String TAG = "MyRefundOrderDetailActivity";

	@Override
	protected Fragment createFragment() {
		return MyRefundOrderDetailFragment.newInstance();
	}
}
