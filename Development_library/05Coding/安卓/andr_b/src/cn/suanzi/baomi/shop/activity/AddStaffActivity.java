package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.MyAddManagerFragment;

/**
 * 添加员工信息
 * @author qian.zhou 
 */
public class AddStaffActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return MyAddManagerFragment.newInstance();
	}
}
