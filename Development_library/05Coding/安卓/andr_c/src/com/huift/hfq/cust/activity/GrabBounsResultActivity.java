package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.GrabBounsResultFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

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
