package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.SysWechatFragment;

/**
 *  微信公共帐号页面
 * @author qian.zhou
 */
public class SysWechatActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return SysWechatFragment.newInstance();
	}

}
