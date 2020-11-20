package com.huift.hfq.cust.activity;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.huift.hfq.base.Const;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.CancelPayTask;
import com.huift.hfq.base.pojo.BatchCoupon;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.ImageDownloadCallback;
import com.huift.hfq.base.utils.QrCodeUtils;
import com.huift.hfq.base.utils.ThreeDES;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.fragment.ICBCPaySuccessFragment;
import com.huift.hfq.cust.model.GetPayResultTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 店铺二维码
 * @author Zhonghui.Dong
 */
public class CouponQrcodeActivity extends Activity {
	public static final String BATCH_CPOUPON ="batchcoupon";
	public static final String CONSUMER_CODE ="consumecode";
	public static final String TAG = CouponQrcodeActivity.class.getSimpleName();
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
	
	private ImageView qrCodeImg;
	private String qType = "qr002";
	private BatchCoupon mBatchCoupon;
	/** 获得一个用户信息对象 **/
	private UserToken mUserToken;
	/** 用户编码*/
	private String mUserCode;
	/** 消费编码*/
	private String mConsumeCode;
	private String mPayType;//支付类型
	private ImageView  mIvReturn;
	private Button mBtnDel;
	private Handler UIhandler = new Handler();
	/** 判断支付是否成功*/
	private boolean isSuccess = true;
	private int mTime = 0;
	/** 判断什么时候支付失败*/
	private boolean payFlag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_shop_icbc_card_pay);
		ViewUtils.inject(this);
		Util.addActivity(this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		init();
		mTime = 0;
		UIhandler.postDelayed(runnable, 1000);// 子线程发送消息
	}
	
	private Runnable runnable = new Runnable() {
		public void run() {
			getPayResult();
			UIhandler.postDelayed(this, 1000);
		}
	};

	private void init() {
		TextView tvContent = (TextView) findViewById(R.id.tv_mid_content);
		tvContent.setText(getResources().getString(R.string.pay_twocode));
		final ImageView ivTwoCode = (ImageView) findViewById(R.id.iv_pay_twocode);// 二维码
		mIvReturn = (ImageView) findViewById(R.id.iv_turn_in);
		mIvReturn.setVisibility(View.GONE);
		mBtnDel = (Button) findViewById(R.id.pay_cancel);
		mBtnDel.setOnClickListener(delOrder);
		//qrCodeImg = (ImageView) findViewById(R.id.shop_qr_code);
		//取值
		Intent intent = this.getIntent();
		mBatchCoupon = (BatchCoupon) intent.getSerializableExtra(BATCH_CPOUPON);
		mConsumeCode = intent.getStringExtra(CONSUMER_CODE);
		// 获得一个用户信息对象
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mUserCode = mUserToken.getUserCode();// 用户编码
		 if(mBatchCoupon != null){
			//截取userCode的末六位
			String userCodeEnd =  mUserCode.substring(mUserCode.length()-6, mUserCode.length());
			 //加密SSRC
			 String encryUserCode = ThreeDES.encryptMode(Util.KeyBytes, userCodeEnd.getBytes()); 
			 final JSONObject arObj = new JSONObject();
			 arObj.put(CustConst.TwoCode.QTYPE, qType);
			 arObj.put(CustConst.TwoCode.SSRC, userCodeEnd);
			 arObj.put(CustConst.TwoCode.SCODE, encryUserCode);
			 arObj.put(CustConst.TwoCode.ORDERNBR, mConsumeCode);
			 arObj.put(CustConst.TwoCode.SHOP_PRICE, mBatchCoupon.getInsteadPrice());
			 arObj.put(CustConst.TwoCode.PTYPE, "3"); //3表示实物劵和体验券的消费方式
			
			 String url = Const.IMG_URL + mBatchCoupon.getLogoUrl();
			 if(!"".equals(mBatchCoupon.getLogoUrl())){
				 Log.d("tag", "有图片的路径：：：：" +url);
				 Util.getLocalOrNetBitmap(url, new ImageDownloadCallback(){
						@Override
						public void success(final Bitmap bitmap) {
							//生成二维码
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									ivTwoCode.setImageBitmap(QrCodeUtils.createQrCode(arObj.toString(), bitmap));
								}
							});
						}
						@Override
						public void fail() {      
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Log.d("TAG", "出问题啦。。。。。。。。。。。");
									ivTwoCode.setImageBitmap(QrCodeUtils.createQrCode(arObj.toString()));
								}
							});
						}
					 });
			 } else{
				 Log.d("tag", "没有图片的路径：：：：" +url);
				 ivTwoCode.setImageBitmap(QrCodeUtils.createQrCode(arObj.toString()));
			 }
		 } 
	}
	

	/**
	 * 取消订单
	 */
	public void delOrder(){
		mBtnDel.setEnabled(false);
		new CancelPayTask(this, new CancelPayTask.Callback() {
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
	
	private void getPayResult() {
		if (!Util.isEmpty(mConsumeCode)) {
			new GetPayResultTask(CouponQrcodeActivity.this, new GetPayResultTask.Callback() {
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
									Intent intent = new Intent(CouponQrcodeActivity.this, ICBCPaySuccessActivity.class);
									intent.putExtra(ICBCPaySuccessFragment.SUCCESS,  isSuccess);
									intent.putExtra(ICBCPaySuccessFragment.SHOP_CODE,  mBatchCoupon.getShopCode());
									startActivity(intent);
									Util.exit();
								}
							   if (runnable != null) {
									UIhandler.removeCallbacks(runnable);
								}
							} else if (FAIL == status) { // 失败
								if (mTime % 2 == 0) {
									isSuccess = false;
									Intent intent = new Intent(CouponQrcodeActivity.this, ICBCPaySuccessActivity.class);
									intent.putExtra(ICBCPaySuccessFragment.SUCCESS,  isSuccess);
									startActivity(intent);
									Util.exit();
								}
								if (runnable != null) {
									UIhandler.removeCallbacks(runnable);
								}
								payFlag = true;
								/*mIvReturn.setVisibility(View.VISIBLE);
								ICBCCardPayActivity.setPayFlag ();*/
							} else if (CANCELED == status) { // 取消付款
								if (mTime % 2 == 0) {
									Util.getContentValidate(R.string.cancel_pay);
									CouponQrcodeActivity.this.finish();
								}
								if (runnable != null) {
									UIhandler.removeCallbacks(runnable);
								}
							}
						} catch (Exception e) {
							Log.e(TAG, "支付出错"+e.getMessage());//TODO
						}
					}
				}
			}).execute(mConsumeCode);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		UIhandler.postDelayed(runnable, 1000);// 子线程发送消息
		MobclickAgent.onPageStart("MainScreen"); //统计页面
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (runnable != null) {
			UIhandler.removeCallbacks(runnable);
		}
	}
	
	/**
	 * 友盟统计
	 */
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainScreen"); 
		if (runnable != null) {
			UIhandler.removeCallbacks(runnable);
		}
	}
	
	/**
	 * 点击事件
	 */
	@OnClick({ R.id.iv_turn_in })
	private void lineBankClick(View v) {
		switch (v.getId()) {
		case R.id.iv_turn_in:
			finish();
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			if (!payFlag) {
				Util.getContentValidate(R.string.pay_return);
				return payFlag;
			} else {
				this.finish();
			}
			
		}
		return super.onKeyDown(keyCode, event);
	}
}
