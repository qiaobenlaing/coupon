package com.huift.hfq.cust.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.application.CustConst;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 扫码下单  订餐
 * @author yanfang
 */
public class ScanMealActivity extends Activity {
    
	/** 商店对象*/
	private Shop mShop;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_scan_meal);
    	AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
    	ViewUtils.inject(this);
    	Util.addActActivity(this); // 买单 
    	Util.addActivity(this);
    	mShop = (Shop) this.getIntent().getSerializableExtra(ScanActivity.SHOP_OBJ);
    }
    
    @OnClick({R.id.iv_back_up,R.id.btn_scan_meal})
    private void skipClick(View view) {
    	switch (view.getId()) {
		case R.id.iv_back_up: // 返回
			H5ShopDetailActivity.setCurrentPage("backup");
			finish();
			break;
		case R.id.btn_scan_meal: // 跳转去扫一扫界面
			if (null != mShop) {
				Intent intent = new Intent(ScanMealActivity.this, ScanActivity.class);
				intent.putExtra(ScanActivity.SHOP_OBJ, mShop);
				intent.putExtra(ScanActivity.TYPE, CustConst.HactTheme.SCAN_TWO);
				startActivity(intent);
				finish();
			}
			break;

		default:
			break;
		}
    }
    
	/**
	 * 重写点击返回按钮方法，点击一次土司提示，两次退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			H5ShopDetailActivity.setCurrentPage("backup");
			Util.exitAct();
		}
		return super.onKeyDown(keyCode, event);
	}
    
    public void onResume(){
    	super.onResume();
        AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
    }
    
}
