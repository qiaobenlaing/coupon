package com.huift.hfq.shop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.utils.AppUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 扫一扫的结果
 * @author wensi.yu
 *
 */
public class ScanResultActivity extends Activity {

	private static final String TAG = "ScanResultActivity";
	
	private static final String SCAN_RESULT = "扫描的结果";
	/** 返回图片*/
	@ViewInject(R.id.layout_turn_in)
	private LinearLayout mIvBackup;
	/** 功能描述文本*/
	@ViewInject(R.id.tv_mid_content)
	private TextView mTvdesc;
	/** 结果类容**/
	@ViewInject(R.id.tv_scanresult)
	private TextView mScanResult;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scanresult);
		ViewUtils.inject(this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		mTvdesc.setText(SCAN_RESULT);
		mIvBackup.setVisibility(View.VISIBLE);
		Intent intent = this.getIntent();
		String scanResult = intent.getStringExtra("resultString");
		Log.d(TAG, "扫后的结果=="+scanResult);
		mScanResult.setText(scanResult);
	}
	
	/**
	 * 返回
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	public void btnActAddBackClick(View view) {		
		finish();
	}
	
	public void onResume(){
    	super.onResume();
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
    }
}
