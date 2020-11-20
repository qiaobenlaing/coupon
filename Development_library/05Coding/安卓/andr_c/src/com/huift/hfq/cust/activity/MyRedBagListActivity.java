package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.MyRedBagListFragment;

import android.app.Fragment;
import com.huift.hfq.base.SingleFragmentActivity;

/**
 * 红包详情页面
 * @author qian.zhou
 */
public class MyRedBagListActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		return MyRedBagListFragment.newInstance();
	}
	
//protected void onResume(){
//		
//		super.onResume();
//		View view = findViewById(R.id.iv_turn_in);
//		if(view == null){
//			Log.e(getLocalClassName(), "onResume view is null");
//			return;
//		}
//		
//		view.setOnClickListener(new OnClickListener() {
//			
//			public void onClick(View v) {
//				MyRedBagListActivity.this.finish();
//			}
//		});
//		
//		 Bundle bundle = getIntent().getExtras();
//	        if(bundle == null){
//	        	Log.e("===About===", "error ...");
//	        	return;
//	        }
//	        String title = bundle.getString("title");
//	        TextView titleView = (TextView) findViewById(R.id.tv_mid_content);
//	        titleView.setText(title);
//	}


}
