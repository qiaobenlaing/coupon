package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.MyUppManagerFragment;

/**
 * 修改店长信息
 * @author qian.zhou 
 */
public class UppStaffActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return MyUppManagerFragment.newInstance();
	}
}
