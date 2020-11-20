package cn.suanzi.baomi.shop.activity;


import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.UpdateShopTimeFragment;

/**
 * 修改商家的营业时间
 * @author qian.zhou
 */
public class DeliveryProgramActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return UpdateShopTimeFragment.newInstance();
	}
}
