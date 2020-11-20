package cn.suanzi.baomi.cust.fragment;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.suanzi.baomi.base.Const;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.CancelPayTask;
import cn.suanzi.baomi.base.pojo.Shop;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.ImageDownloadCallback;
import cn.suanzi.baomi.base.utils.QrCodeUtils;
import cn.suanzi.baomi.base.utils.ThreeDES;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.activity.ICBCPaySuccessActivity;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.model.GetPayResultTask;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;
 
/**
 * 工行刷卡支付
 * 
 * @author Zhonghui.Dong
 */
public class ICBCCardFragment extends Fragment {

	private static final String TAG = ICBCCardFragment.class.getSimpleName();
	public static final String ORDER_CODE = "order_code";
	public static final String SHOP_OBJ = "shopObj";
	public static final String ORDER_STYPE = "orderstyle";
	public static final String SHOP_CODE = "shopCode";
	public static final String SHOP_URL = "shop.url";
	public static final String PAY_CARD = "0";
	public static final String PAY_MONEY = "1";
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
	
	/** 获得一个用户信息对象 **/
	private UserToken mUserToken;
	/** 用户编码 **/
	private String mUserCode;
	private String qType = "qr002";
	private String mPayType;//支付类型
	private String mConsumeCode;//订单编号
	
	private Handler UIhandler = new Handler();
	/** 判断支付是否成功*/
	private boolean isSuccess = true;
	/** 商店编码*/
	private String mShopCode;
	/**线程执行的次数*/
	private int mTime = 0;
	
	@ViewInject(R.id.tv_mid_content)
	private TextView tvContent;
	@ViewInject(R.id.iv_pay_twocode)
	private ImageView ivTwoCode;
	@ViewInject(R.id.iv_turn_in)
	private ImageView  mIvReturn;
	@ViewInject(R.id.pay_cancel)
	private Button mBtnDel;
	@ViewInject(R.id.tv_consumeCode)
	private TextView tvOrderNbr;
	
	/**
	 * 需要传递参数时有利于解耦
	 */
	public static ICBCCardFragment newInstance() {
		Bundle args = new Bundle();
		ICBCCardFragment fragment = new ICBCCardFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_shop_icbc_card_pay, container, false);
		ViewUtils.inject(this, v);
		Util.addActivity(getMyActivity());
		Util.addLoginActivity(getMyActivity());
		init(v);
		UIhandler.postDelayed(runnable, 1000);// 子线程发送消息
		return v;
	}
	
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}
	
	private Runnable runnable = new Runnable() {
		public void run() {
			getPayResult();
			UIhandler.postDelayed(this, 5000);
		}
	};
	
	private void init(View v) {
		mTime = 0;
		tvContent.setText(getResources().getString(R.string.pay_twocode));
		mIvReturn.setVisibility(View.GONE);
		mBtnDel.setOnClickListener(delOrder);
		
		// 获得一个用户信息对象
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mUserCode = mUserToken.getUserCode();// 用户编码
		//截取userCode的末六位
		String userCodeEnd =  mUserCode.substring(mUserCode.length()-6, mUserCode.length());
		//取值
		Intent intent = getMyActivity().getIntent();
		mConsumeCode = intent.getStringExtra(ORDER_CODE);//订单编号
		String orderType = intent.getStringExtra(ORDER_STYPE);//支付类别
		if(!Util.isEmpty(orderType)){
			if(orderType.equals(PAY_CARD)){
				mPayType = PAY_CARD;
			} else if(orderType.equals(PAY_MONEY)){
				mPayType = PAY_MONEY;
			}
		}
		// 加密SSRC
		String UserCodeEnd = ThreeDES.encryptMode(Util.KeyBytes, userCodeEnd.getBytes());
		final JSONObject arObj = new JSONObject();
		arObj.put(CustConst.TwoCode.QTYPE, qType);// 标示
		if(!Util.isEmpty(mConsumeCode)){
			arObj.put(CustConst.TwoCode.ORDERNBR, mConsumeCode);// 订单号
		}
		if(!Util.isEmpty(mPayType)){
			arObj.put(CustConst.TwoCode.PTYPE, mPayType);// 支付方式
		}
		arObj.put(CustConst.TwoCode.SSRC, userCodeEnd);//加密字符串 -- 登陆用户编码截取的后六位
		arObj.put(CustConst.TwoCode.SCODE, UserCodeEnd);// 可逆加密编码
		
		String imgUrl = intent.getStringExtra(SHOP_URL);
		if(!Util.isEmpty(imgUrl)){
			String url = Const.IMG_URL + imgUrl;
			if(!"".equals(imgUrl)){
				Util.getLocalOrNetBitmap(url, new ImageDownloadCallback(){
					@Override
					public void success(final Bitmap bitmap) {
						//生成二维码
						getMyActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								ivTwoCode.setImageBitmap(QrCodeUtils.createQrCode(arObj.toString(), bitmap));
								if(bitmap.isRecycled() == false){
									bitmap.recycle();
								}
							}
						});
					}
					@Override
					public void fail() {
						Log.d(TAG, "出问题啦。。。。。。。。。。。");
						//生成二维码
						getMyActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								ivTwoCode.setImageBitmap(QrCodeUtils.createQrCode(arObj.toString()));
							}
						});
					}
				});
			} else{
				ivTwoCode.setImageBitmap(QrCodeUtils.createQrCode(arObj.toString()));
			}
		} else {
			ivTwoCode.setImageBitmap(QrCodeUtils.createQrCode(arObj.toString()));
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		UIhandler.postDelayed(runnable, 1000);// 子线程发送消息
		MobclickAgent.onPageStart(ICBCCardFragment.class.getSimpleName()); //统计页面
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (runnable != null) {
			UIhandler.removeCallbacks(runnable);
		}
	}
	
	/**
	 * 付款成功
	 */
	private void getPayResult() {
		if (!Util.isEmpty(mConsumeCode)) {
			new GetPayResultTask(getMyActivity(), new GetPayResultTask.Callback() {
				@Override
				public void getResult(JSONObject result) {
					mTime++;
					if (result == null) {
						return ;
					} else {
						try {
							String payStatus = result.get("status").toString(); 
							String userCouponCode = "";
							if (null != result.get("userCouponCode").toString()) {
								userCouponCode = result.get("userCouponCode").toString();
							} else {
								userCouponCode = "";
							}
							Shop shop = (Shop) getMyActivity().getIntent().getSerializableExtra(SHOP_OBJ);
							int status = Integer.parseInt(payStatus);
							if (PAYED == status) { // 已付款
//								if (UNPAYED == status) { // 已付款
								if (mTime % 2 == 0) {
									isSuccess = true;
									Intent intent = new Intent(getMyActivity(), ICBCPaySuccessActivity.class);
									intent.putExtra(ICBCPaySuccessFragment.SUCCESS,  isSuccess);
									intent.putExtra(ICBCPaySuccessFragment.SHOP_CODE,  mShopCode);
									intent.putExtra(ICBCPaySuccessFragment.USER_COUPON_CODE,  userCouponCode);
									intent.putExtra(ICBCPaySuccessFragment.SHOP_OBJ,  shop);
									startActivity(intent);
									Util.exit();
								}
							} else if (FAIL == status) { // 失败
								if (mTime % 2 == 0) {
									isSuccess = false;
									Intent intent = new Intent(getMyActivity(), ICBCPaySuccessActivity.class);
									intent.putExtra(ICBCPaySuccessFragment.SUCCESS,  isSuccess);
									startActivity(intent);
									Util.exit();
								}
								/*mIvReturn.setVisibility(View.VISIBLE);
								ICBCCardPayActivity.setPayFlag ();*/
							} else if (CANCELED == status) { // 取消付款
								Util.getContentValidate(R.string.cancel_pay);
								getMyActivity().finish();
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
		new CancelPayTask(getMyActivity(), new CancelPayTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mBtnDel.setEnabled(true);
				if (result != null) {
					if(String.valueOf(ErrorCode.SUCC).equals(result.get("code").toString())){
						Util.getContentValidate(R.string.del_order_success);
						getMyActivity().finish();
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
			//友盟统计
			MobclickAgent.onEvent(getMyActivity(), "icbccard_fragment_exitpay");
			delOrder();
		}
	};
	
	/**
	 * 点击事件
	 */
	@OnClick({ R.id.iv_turn_in })
	private void lineBankClick(View v) {
		switch (v.getId()) {
		case R.id.iv_turn_in:
			getMyActivity().finish();
			break;
		}
	}
	
	/**
	 * 友盟统计
	 */
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(ICBCCardFragment.class.getSimpleName()); 
		if (runnable != null) {
			UIhandler.removeCallbacks(runnable);
		}
	}
}
