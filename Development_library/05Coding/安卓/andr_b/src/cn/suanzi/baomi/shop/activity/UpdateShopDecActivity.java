package cn.suanzi.baomi.shop.activity;


import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.UpdateShopDecFragment;

/**
 * 修改商家的简介信息
 * @author qian.zhou
 */
public class UpdateShopDecActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return UpdateShopDecFragment.newInstance();
	}
}
