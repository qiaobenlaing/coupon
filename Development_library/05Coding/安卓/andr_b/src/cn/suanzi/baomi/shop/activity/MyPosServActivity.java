package cn.suanzi.baomi.shop.activity;
import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.shop.fragment.MyPosServFragment;

/**
 * POS服务显示界面
 * @author qian.zhou
 */
public class MyPosServActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return MyPosServFragment.newInstance();
	}
}
