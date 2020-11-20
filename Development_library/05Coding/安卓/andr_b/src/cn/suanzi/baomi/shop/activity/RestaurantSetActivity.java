package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.RestaurantSetFragment;

/**
 * 餐厅设置
 * @author qian.zhou 
 */
public class RestaurantSetActivity extends SingleFragmentActivity{
	@Override
	protected Fragment createFragment() {
		return RestaurantSetFragment.newInstance() ;
	}
}
