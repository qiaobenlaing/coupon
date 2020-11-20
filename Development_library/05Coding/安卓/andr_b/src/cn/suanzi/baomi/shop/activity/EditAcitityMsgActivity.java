package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.EditAcitityMsgFragment;

/**
 * 编辑活动所有信息
 * @author qian.zhou
 */
public class EditAcitityMsgActivity extends SingleFragmentActivity {

	private final static String TAG = EditAcitityMsgActivity.class.getSimpleName();
	
	@Override
	protected Fragment createFragment() {
		return EditAcitityMsgFragment.newInstance();
	}
}
