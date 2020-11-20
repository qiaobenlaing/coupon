package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.SettledShopInfoFragment;

/**
 * 填写门店信息
 * @author wensi.yu
 */
public class SettledShopInfoActivity extends SingleFragmentActivity{

	@Override
	protected Fragment createFragment() {
		return SettledShopInfoFragment.newInstance();
	}
	
}
