package cn.suanzi.baomi.shop.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.ActAddImageFragment;

/**
 * @author wensi.yu
 * 添加营销活动中的预览
 */
public class ActAddImageActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {		
		return ActAddImageFragment.newInstance();
	}	
}
