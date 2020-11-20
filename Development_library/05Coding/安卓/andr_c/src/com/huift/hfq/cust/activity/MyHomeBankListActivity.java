package com.huift.hfq.cust.activity;

import com.huift.hfq.cust.fragment.MyHomeBankListFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.cust.R;

/**
 * 银行卡列表
 * @author wensi.yu
 *
 */
public class MyHomeBankListActivity extends Activity {

	private final static String TAG = MyHomeBankListActivity.class.getSimpleName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "RegisterActivity Create");
		setContentView(R.layout.activity_register);
		/** Fragment管理器 */
		FragmentManager mFragmentManager = getFragmentManager();
		FragmentTransaction trx = mFragmentManager.beginTransaction();
		trx.add(R.id.ly_register,new MyHomeBankListFragment());
		trx.commit();
		AppUtils.setActivity(this);
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
				MyHomeBankListActivity.this.finish();
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
