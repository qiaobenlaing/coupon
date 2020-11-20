package com.huift.hfq.cust.activity;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.model.CancelPayTask;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.QrCodeUtils;
import com.huift.hfq.cust.R;

import com.google.zxing.WriterException;
import com.huift.hfq.cust.fragment.ICBCPaySuccessFragment;
import com.huift.hfq.cust.model.GetPayResultTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 付款失败再次支付二维码
 * @author qian.zhou
 */
public class MyOrderQrcodeActivity extends Activity {
	public static final String TAG = "MyOrderQrcodeActivity";
	public static final String CONSUME_CODE = "consumeCode";
	public static final String IMAGE_URL = "shopLogo";
	private ImageView qrCodeImg;
	private String mConsumeCode;
	/** 取消支付*/
	private Button mBtnDel;
	private Handler UIhandler = new Handler();
	/**线程执行的次数*/
	private int mTime = 0;
	/** 未付款*/
	private static final int UNPAYED = 1;
	/** 付款中*/
	private static final int PAYING = 2;
	/** 已付款*/
	private static final int PAYED = 3;
	/** 已取消付款*/
	private static final int CANCELED = 4;
	/** 付款失败*/
	private static final int FAIL = 5;
	/** 判断支付是否成功*/
	private boolean isSuccess = true;
	/** 商店编码*/
	private String mShopCode;
	/** 订单编码*/
	public static final String ORDER_NBR = "ordernbr";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_icbc_pay);
		ViewUtils.inject(this);
		Util.addActivity(this);
		AppUtils.setActivity(this);
		init();
		UIhandler.postDelayed(runnable, 1000);// 子线程发送消息
	}
	
	private Runnable runnable = new Runnable() {
		public void run() {
			getPayResult();
			UIhandler.postDelayed(this, 1000);
		}
	};

	private void init() {
		 Intent intent = getIntent();
		 qrCodeImg = (ImageView) findViewById(R.id.iv_pay_twocode);
		 String orderNbr = intent.getStringExtra(ORDER_NBR);
		 try {
			qrCodeImg.setImageBitmap(QrCodeUtils.CreateOneDCode(orderNbr));
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 付款成功
	 */
	private void getPayResult() {
		if (!Util.isEmpty(mConsumeCode)) {
			new GetPayResultTask(MyOrderQrcodeActivity.this, new GetPayResultTask.Callback() {
				@Override
				public void getResult(JSONObject result) {
					mTime++;
					if (result == null) {
						return ;
					} else {
						try {
							String payStatus = result.get("status").toString(); 
							int status = Integer.parseInt(payStatus);
							if (PAYED == status) { // 已付款
								if (mTime % 2 == 0) {
									isSuccess = true;
									Intent intent = new Intent(MyOrderQrcodeActivity.this, ICBCPaySuccessActivity.class);
									intent.putExtra(ICBCPaySuccessFragment.SUCCESS,  isSuccess);
									intent.putExtra(ICBCPaySuccessFragment.SHOP_CODE,  mShopCode);
									startActivity(intent);
									Util.exit();
								}
							} /*else if (FAIL == status) { // 失败
								if (mTime % 2 == 0) {
									isSuccess = false;
									Intent intent = new Intent(MyOrderQrcodeActivity.this, ICBCPaySuccessActivity.class);
									intent.putExtra(ICBCPaySuccessFragment.SUCCESS,  isSuccess);
									startActivity(intent);
									Util.exit();
								}*/
								/*mIvReturn.setVisibility(View.VISIBLE);
								ICBCCardPayActivity.setPayFlag ();
							} */else if (CANCELED == status) { // 取消付款
								Util.getContentValidate(R.string.cancel_pay);
								finish();
							}
						} catch (Exception e) {
							Log.e(TAG, "支付出错"+e.getMessage());//TODO
						}
					}
				}
			}).execute(mConsumeCode);
		}
	}
	
	/**
	 * 取消订单
	 */
	public void delOrder(){
		mBtnDel.setEnabled(false);
		new CancelPayTask(MyOrderQrcodeActivity.this, new CancelPayTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mBtnDel.setEnabled(true);
				if (result != null) {
					if(String.valueOf(ErrorCode.SUCC).equals(result.get("code").toString())){
						Util.getContentValidate(R.string.del_order_success);
						finish();
					} else{
						Util.getContentValidate(R.string.del_order_fail);
					}
				} 
			}
		}).execute(mConsumeCode);
	}

	//取消订单的事件
	OnClickListener delOrder = new OnClickListener() {
		@Override
		public void onClick(View v) {
			delOrder();
		}
	};
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (runnable != null) {
			UIhandler.removeCallbacks(runnable);
		}
	}
	
	@OnClick({ R.id.iv_turn_in })
	private void click(View v) {
		switch (v.getId()) {
		case R.id.iv_turn_in:
			finish();
			break;
		}
	}
	
	 public void onResume(){
	    	super.onResume();
	        AppUtils.setActivity(this);
	 }
}
