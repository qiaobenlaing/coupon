package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.StoreSetFragment;

/**
 * 门店设置
 * @author qian.zhou 
 */
public class StoreSetActivity extends SingleFragmentActivity{
	@Override
	protected Fragment createFragment() {
		return StoreSetFragment.newInstance() ;
	}
}
