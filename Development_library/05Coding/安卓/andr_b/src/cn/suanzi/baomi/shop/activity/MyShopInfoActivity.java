package cn.suanzi.baomi.shop.activity;


import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.MyShopInfoFragment;

/**
 * 店铺信息显示界面
 * @author qian.zhou
 */
public class MyShopInfoActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return MyShopInfoFragment.newInstance();
	}
}
