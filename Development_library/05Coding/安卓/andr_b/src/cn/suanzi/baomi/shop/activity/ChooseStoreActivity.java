package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.ChooseStoreFragment;

/**
 * 选择店铺
 * @author qian.zhou 
 */
public class ChooseStoreActivity extends SingleFragmentActivity{
	@Override
	protected Fragment createFragment() {
		return ChooseStoreFragment.newInstance() ;
	}
}
