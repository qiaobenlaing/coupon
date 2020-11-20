package com.huift.hfq.shop.activity;

import java.util.Calendar;

import org.apache.commons.lang3.time.DateFormatUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.utils.MPosPluginHelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class ICBCPayActivity extends Activity {
	private String shopCode, userCode, mobilenbr, couponCode, generalBonusUsed;
	private String shopBonusUsed, totalPayment, actualPayment, scode, payTime,shopId;
	private String trace;
	private String shopName, couponPayment, bonusPayment;

	@ViewInject(R.id.shop_name)
	private TextView shopNameTextView;
	@ViewInject(R.id.price)
	private TextView priceTextView;
	@ViewInject(R.id.coupons_price)
	private TextView couponPriceTextView;
	@ViewInject(R.id.bonus_price)
	private TextView bonusPriceTextView;
	@ViewInject(R.id.new_price)
	private TextView newPriceTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.icbc_pay);
		ViewUtils.inject(this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		init();
		initData();
	}

	private void initData() {
		shopNameTextView.setText(shopName);
		priceTextView.setText(totalPayment);
		couponPriceTextView.setText(couponPayment);
		bonusPriceTextView.setText(bonusPayment);
		newPriceTextView.setText(actualPayment);
	}

	private void init() {
		shopCode = getIntent().getStringExtra(ShopConst.QrCode.SHOP_CODE);
		userCode = getIntent().getStringExtra(ShopConst.QrCode.USER_CODE);
		mobilenbr = getIntent().getStringExtra(ShopConst.QrCode.MOBILENBR);
		couponCode = getIntent().getStringExtra(ShopConst.QrCode.COUPONCODE);
		generalBonusUsed = getIntent().getStringExtra(ShopConst.QrCode.GENERAL_BONUSUSED);
		shopBonusUsed = getIntent().getStringExtra(ShopConst.QrCode.SHOP_BONUSUSED);
		totalPayment = getIntent().getStringExtra(ShopConst.QrCode.TOTAL_PAYMENT);
		actualPayment = getIntent().getStringExtra(ShopConst.QrCode.ACTUAL_PAYMENT);
		scode = getIntent().getStringExtra(ShopConst.QrCode.SCODE);
		payTime = getIntent().getStringExtra(ShopConst.QrCode.PAY_TIME);
	}

	@OnClick({ R.id.backup, R.id.submit })
	private void click(View view) {
		switch (view.getId()) {
		case R.id.backup:
			finish();
			break;
		case R.id.submit:
			trace = DateFormatUtils.format(Calendar.getInstance(), "yyyyMMddHHmmss");
			MPosPluginHelper.consume(this, actualPayment, trace, shopCode, "", "", "",shopId);
			break;
		}
	}
	
	public void onResume(){
    	super.onResume();
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
    }
}
