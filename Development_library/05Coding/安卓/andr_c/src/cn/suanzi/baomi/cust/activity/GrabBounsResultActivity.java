package cn.suanzi.baomi.cust.activity;

import android.app.Fragment;
import cn.suanzi.baomi.base.SingleFragmentActivity;
import cn.suanzi.baomi.cust.fragment.GrabBounsResultFragment;

 /**
  * 抢红包的结果页面
  * @author yanfang.li
  *
  */
public class GrabBounsResultActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		
		return GrabBounsResultFragment.newInstance();
	}
	
}
