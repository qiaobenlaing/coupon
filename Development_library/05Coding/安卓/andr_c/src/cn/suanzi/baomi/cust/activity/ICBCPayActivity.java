package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.ICBCPayFragment;
/**
 * 登录Activity 单Fragment
 * 
 * @author Zhonghui.Dong
 */
public class ICBCPayActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return ICBCPayFragment.newInstance();
	}

}
