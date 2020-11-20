package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import android.content.Intent;
import android.util.Log;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.TextMessageFragment;

/**
 * 设置短信接收
 * @author liyanfang
 */
public class TextMessageActivity extends SingleFragmentActivity {	
	
	@Override
	protected Fragment createFragment() {
		return TextMessageFragment.newInstance();
	}
}
