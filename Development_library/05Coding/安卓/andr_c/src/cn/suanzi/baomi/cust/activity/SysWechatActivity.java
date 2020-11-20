package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.SysWechatFragment;

/**
 * 微信公众号
 * @author ad
 */
public class SysWechatActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return SysWechatFragment.newInstance();
	}

}
