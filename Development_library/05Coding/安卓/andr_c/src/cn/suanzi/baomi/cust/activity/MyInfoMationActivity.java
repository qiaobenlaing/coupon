package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import android.view.KeyEvent;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.MyInfoMationFragment;

/**
 * 个人信息页面
 * @author qian.zhou
 */
public class MyInfoMationActivity extends SingleFragmentActivity {
	/** setResult()用的，如果确实修改了信息 */
	public final static int INTENT_RESP_SAVED = 1;
	/** setResult()用的，如果取消了修改信息 */
	public final static int INTENT_RESP_CANCELED = 2;
	MyInfoMationFragment instance;
	@Override
	protected Fragment createFragment() {
		instance = MyInfoMationFragment.newInstance();
		return instance;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			instance = null;
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
