package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.SetPayPwdFragment;
/**
 * 支付密码设置界面（300元以下）
 * @author yingchen
 *
 */
public class SetPayPwdActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		SetPayPwdFragment fragment = SetPayPwdFragment.newInstance();
		return fragment;
	}

}
