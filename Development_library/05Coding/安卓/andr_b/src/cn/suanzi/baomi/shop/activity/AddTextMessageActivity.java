package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.AddTextMessageFragment;

/**
 * 添加短信接收者
 * @author liyanfang
 */
public class AddTextMessageActivity extends SingleFragmentActivity {	
	
	@Override
	protected Fragment createFragment() {
		return AddTextMessageFragment.newInstance();
	}
}
