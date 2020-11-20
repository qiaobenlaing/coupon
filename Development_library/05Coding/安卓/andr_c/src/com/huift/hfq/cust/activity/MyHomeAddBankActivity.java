package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.MyHomeAddBankFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.cust.R;

/**
 * 添加银行卡
 * @author wensi.yu
 *
 */
public class MyHomeAddBankActivity extends SingleFragmentActivity {

	private final static String TAG = MyHomeAddBankActivity.class.getSimpleName();
	
	@Override
	protected Fragment createFragment() {
		return MyHomeAddBankFragment.newInstance();
	}
	
	protected void onResume(){
		
		super.onResume();
		View view = findViewById(R.id.iv_bank_function_backup);
		if(view == null){
			Log.e(getLocalClassName(), "onResume view is null");
			return;
		}
		
		view.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				MyHomeAddBankActivity.this.finish();
			}
		});
		
		 Bundle bundle = getIntent().getExtras();
	        if(bundle == null){
	        	Log.e("===About===", "error ...");
	        	return;
	        }
	        String title = bundle.getString("title");
	        TextView titleView = (TextView) findViewById(R.id.tv_bank_function_desc);
	        titleView.setText(title);
	}

}
