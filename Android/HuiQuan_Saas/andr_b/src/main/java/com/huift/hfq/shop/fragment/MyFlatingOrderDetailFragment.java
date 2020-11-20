package com.huift.hfq.shop.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.OrderInfo;
import com.huift.hfq.base.pojo.ProductList;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopApplication;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.ShopConst.Order;
import com.huift.hfq.shop.adapter.ProductListAdapter;
import com.huift.hfq.shop.model.GetProductOrderInfoTask;
import com.huift.hfq.shop.model.UpdateProductOrderStatusTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 餐前订单详情页面
 * @author qian.zhou
 */
public class MyFlatingOrderDetailFragment extends Fragment {
	public static final String ORDER_CODE = "orderCode";
	private String mOrderCode;
	/** 正在加载数据*/
	private LinearLayout mLyNodate;
	/** 正在加载的内容*/
	private LinearLayout mLyContent;
	/** 是否收缩*/
	private ImageView mIvArrow;
	/** 记录点击次数*/
	private int mClickNum; 
	/** 弹出层*/
	private LinearLayout mIvMsg;
	private PopupWindow mPopupWindow;
	private View mView;
	/** 菜单列表*/
	private ListView mLvFastOrder;
	/** 取出用户手机号码*/
	private OrderInfo mOrderInfo;
	/** 订单状态标题*/
	private TextView mTvContentTitle;
	/** 接单时间*/
	private LinearLayout mRyTextContent;
	/** 修改订单状态*/
	private String mOrderStatus;
	/** 改变前的状态*/
	private String mBeforeOrderStatus;
	/** 抵扣一整行*/
	private LinearLayout mLyAllDiscount;
	/** 输入的餐桌号*/
	private String mInputTabe;
	/** 进度条*/
	private ProgressDialog mProcessDialog;
	/** 桌号*/
	private TextView mTvOrderNumber;
	/** 联系用户*/
	private Button mBtnMyAfterOrderUser;
	/** 接单状况*/
	private Button mBtnMyAfterOrderOk;
	/** 撤销*/
	private Button mBtnMyAfterOrderBack;
	/** 取消*/
	private Button mBtnMyAfterOrderCancel;
	/** 定义全局变量*/
	private ShopApplication mShopApplication;
	/** 得到是否入驻的标示*/
	private boolean mSettledflag;
	
	
	/**
	 * 需要传递参数时有利于解耦
	 */
	public static MyFlatingOrderDetailFragment newInstance() {
		Bundle args = new Bundle();
		MyFlatingOrderDetailFragment fragment = new MyFlatingOrderDetailFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.activity_fasting_order_detail, container, false);
		ViewUtils.inject(this, mView);
		Util.addLoginActivity(getActivity());
		//初始化数据
		initData();
		return mView;
	}

	// 初始化方法
	private void initData() {
		mShopApplication =  (ShopApplication) getActivity().getApplication();
		mSettledflag = mShopApplication.getSettledflag();
		//标题
		mTvContentTitle = (TextView) getActivity().findViewById(R.id.tv_mid_content);
		LinearLayout ivTurnin = (LinearLayout) getActivity().findViewById(R.id.layout_turn_in);
		ivTurnin.setVisibility(View.VISIBLE);
		ivTurnin.setOnClickListener(turnListener);
		mRyTextContent = (LinearLayout) getActivity().findViewById(R.id.ly_text_content);//接单时间
		mIvMsg = (LinearLayout) getActivity().findViewById(R.id.layout_msg);
		mIvMsg.setOnClickListener(orderClickListener);
		//加载数据
		mLyNodate = (LinearLayout) mView.findViewById(R.id.ly_nodate);
		mLyContent = (LinearLayout) mView.findViewById(R.id.ly_content);
		//导向箭头
		mClickNum = 0;
		mIvArrow = (ImageView) mView.findViewById(R.id.iv_arrow_down);
		RelativeLayout ryDiscount = (RelativeLayout) mView.findViewById(R.id.ry_arrow_discount);//优惠金额一整行
		mLyAllDiscount = (LinearLayout) mView.findViewById(R.id.ly_all_discount);//所有折扣
		ryDiscount.setOnClickListener(discountListener);
		//取值
		Intent intent = getActivity().getIntent();
		mOrderCode = intent.getStringExtra(ORDER_CODE);
		//赋值
		mIvArrow.setImageResource(mClickNum % 2 == 0 ? R.drawable.upc_arrow : R.drawable.downc_arrow);
		// 正在加载数据
		setData(0); 
		//订单详情方法
		getOrderDeatil();
	}
	
	/**
	 * 点击抵扣金额这一行的事件
	 */
	OnClickListener discountListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mClickNum++;
			if (mClickNum % 2 == 0) {
				mIvArrow.setImageResource(R.drawable.upc_arrow);
				mLyAllDiscount.setVisibility(View.VISIBLE);
			} else {
				mIvArrow.setImageResource(R.drawable.downc_arrow);
				mLyAllDiscount.setVisibility(View.GONE);            
			}
		}
	};
	
	/**
	 * 返回
	 */
	OnClickListener turnListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			getActivity().finish();
		}
	};
	
	/**
	 * 根据订单状态得到按钮的状态
	 */
	private void setVisibility (boolean user ,boolean ok ,boolean back, boolean cancel) {
		if (user) {
			mBtnMyAfterOrderUser.setVisibility(View.VISIBLE);
		}else{
			mBtnMyAfterOrderUser.setVisibility(View.GONE);
		}
		
		if (ok) {
			mBtnMyAfterOrderOk.setVisibility(View.VISIBLE);
		} else {
			mBtnMyAfterOrderOk.setVisibility(View.GONE);
		}
		
		if (back) {
			mBtnMyAfterOrderBack.setVisibility(View.VISIBLE);
		} else {
			mBtnMyAfterOrderBack.setVisibility(View.GONE);
		}
		
		if (cancel) {
			mBtnMyAfterOrderCancel.setVisibility(View.VISIBLE);
		} else {
			mBtnMyAfterOrderCancel.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 弹出层
	 */
	OnClickListener orderClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mView = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindow_myafterorder, null);
			mView.setBackgroundColor(Color.TRANSPARENT);
			mPopupWindow = new PopupWindow(mView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			ScaleAnimation animation = new ScaleAnimation(1.0f, 1.0f, 0F, 1f, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
			animation.setDuration(500);
			animation.setFillAfter(true);
			mPopupWindow.setFocusable(true);
			mPopupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
			mPopupWindow.setOutsideTouchable(true);
			mView.setAnimation(animation);
			
			mBtnMyAfterOrderUser = (Button) mView.findViewById(R.id.btn_myafterorder_user);//联系用户
			mBtnMyAfterOrderOk = (Button) mView.findViewById(R.id.btn_myafterorder_order_ok);//确认接单
			mBtnMyAfterOrderBack = (Button) mView.findViewById(R.id.btn_myafterorder_order_back);//撤销订单
			mBtnMyAfterOrderCancel = (Button) mView.findViewById(R.id.btn_myafterorder_cancel);//取消
			
			//根据订单状态显示用户可选择的操作
			getStatus(mOrderStatus);
			mBtnMyAfterOrderUser.setOnClickListener(afterOrderListener);
			mBtnMyAfterOrderOk.setOnClickListener(afterOrderListener);
			mBtnMyAfterOrderBack.setOnClickListener(afterOrderListener);
			mBtnMyAfterOrderCancel.setOnClickListener(afterOrderListener);
			mPopupWindow.showAtLocation(mView, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		}
	};
	
    OnClickListener afterOrderListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_myafterorder_user://联系用户
				DialogUtils.showDialog(getActivity(), getString(R.string.cue),getString(R.string.dialog_tel), getString(R.string.ok), getString(R.string.no),new DialogUtils().new OnResultListener() {
					@Override
					public void onOK() {
						Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(getResources().getString(R.string.user_tel)+ mOrderInfo.getReceiverMobileNbr()));
						startActivity(intent);
					}
				});
				break;
			case R.id.btn_myafterorder_order_ok://确认接单
				if (mSettledflag) {
					getTable (mView);
					
					mPopupWindow.dismiss();
					
				} else {
					mShopApplication.getDateInfo(getActivity());
				}
				break;
			case R.id.btn_myafterorder_order_back://撤销订单
				DialogUtils.showDialog(getActivity(), getString(R.string.cue),getString(R.string.myafter_order_back), getString(R.string.ok), getString(R.string.no), new DialogUtils().new OnResultListener() {
					@Override
					public void onOK() {
						mOrderStatus = Order.REVOKED;
						getSubmitEnd(mOrderStatus, Util.NUM_ZERO);
						mPopupWindow.dismiss();
					}
				});
				break;
			case R.id.btn_myafterorder_cancel://取消
				mPopupWindow.dismiss();
				break;
			default:
				break;
			}
		}
	};
	
	/**
	 * 订单状态
	 * @param orderStatus
	 */
	private void getStatus(String orderStatus){
		//根据订单状态显示用户可选择的操作
		if (null == orderStatus) {
			return;
			
		} else {
			if (String.valueOf(Order.ORDERED).equals(orderStatus)){//已下单
				mTvContentTitle.setText(R.string.waitinglist);
				mRyTextContent.setVisibility(View.VISIBLE);
				setVisibility(true,true,true,true);
				mBtnMyAfterOrderOk.setText(R.string.myafter_confirm_orders);    
				
			} else if (String.valueOf(Order.PEND_ORDER).equals(orderStatus)) {//待下单 
				mTvContentTitle.setText(R.string.waitinglist);
				mRyTextContent.setVisibility(View.VISIBLE);   
				setVisibility(true,true,true,true);
				mBtnMyAfterOrderOk.setText(R.string.myafter_confirm_orders);
				
			} else if(String.valueOf(Order.DELIVERED).equals(orderStatus)) {//已派送
				mTvContentTitle.setText(R.string.distributionlist);
				mRyTextContent.setVisibility(View.GONE);
				setVisibility(true,false,true,true);
				mBtnMyAfterOrderOk.setText(R.string.myafter_confirm_orders);
				
			} else if(String.valueOf(Order.HAS_ORDER).equals(orderStatus)) {//已接单
				mTvContentTitle.setText(R.string.distributionlist);
				mRyTextContent.setVisibility(View.GONE);
				setVisibility(true,false,true,true);
				mBtnMyAfterOrderOk.setText(R.string.myafter_order_pay);
				
			} else if(String.valueOf(Order.REVOKED).equals(orderStatus)) {//已撤销
				mRyTextContent.setVisibility(View.GONE);
				mTvContentTitle.setText(R.string.transactioncancellation);
				setVisibility(true,false,false,true);
				
			} else if(String.valueOf(Order.BEEN_SERVED).equals(mOrderInfo.getOrderStatus())) {//已送达
				if (Integer.parseInt(mOrderInfo.getStatus()) == 3){//支付成功
					mTvContentTitle.setText(R.string.tradingsuccess);
					setVisibility(true,false,false,true);
				} else {//未支付
					mTvContentTitle.setText(R.string.orderobligation);
					setVisibility(true,false,false,true);
					mBtnMyAfterOrderOk.setText(R.string.myafter_order_pay);
				}
			}
		}
	}
	
	/**
	 * 弹出餐桌号
	 */
	private void  getTable (final View v) {
		if ("20".equals(mOrderInfo.getOrderType())) {//门店订单
			 if (String.valueOf(Util.NUM_ONE).equals(mOrderInfo.getTableNbrSwitch())) {//开启桌号管理
				 DialogUtils.showDialogTable(getActivity(), getString(R.string.myafter_order__table_title), getString(R.string.ok), getString(R.string.no), new DialogUtils().new OnResListener() {
						@Override
						public void onOk(String... params) {
							LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
							reqParams.get(params[0]);
							mInputTabe = params[0];
							mOrderStatus = ShopConst.Order.HAS_ORDER;
							getSubmitEnd(mOrderStatus, Util.NUM_ONE);
						}
					});
			 } else {
				 mInputTabe = "";
				 mOrderStatus = Order.HAS_ORDER;
				 getSubmitEnd(mOrderStatus, Util.NUM_ONE);
			 }
 			
		} else {
			mInputTabe = "";
			mOrderStatus = Order.HAS_ORDER;
			getSubmitEnd(mOrderStatus, Util.NUM_ONE);
		}
	}
	
	/**
	 * 设置数据
	 * @param type 有没有数据 1 是有数据 0 是没有数据
	 */
	private void setData (int type) {
		if (type == 1) {
			mLyNodate.setVisibility(View.GONE);
			mLyContent.setVisibility(View.VISIBLE);
		} else {
			mLyNodate.setVisibility(View.VISIBLE);
			mLyContent.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 修改订单状态
	 * @param orderstatus 订单状态
	 * @param flag 
	 */
	public void getSubmitEnd(String orderstatus, int flag){
		mProcessDialog = new ProgressDialog(getActivity());
		mProcessDialog.setCancelable(false);
		if (flag == Util.NUM_ONE) {
			mProcessDialog.setMessage(getActivity().getResources().getString(R.string.get_order_processing));
		} else {
			mProcessDialog.setMessage(getActivity().getResources().getString(R.string.get_no_order_processing));
		}
		mProcessDialog.show();
		new UpdateProductOrderStatusTask(getActivity(), new UpdateProductOrderStatusTask.Callback() {
			@Override
			public void getResult(int retCode) {
				if (mProcessDialog != null) {
					mProcessDialog.dismiss();
				}
				if (retCode == 0) {
					//修改订单状态失败
					mOrderStatus = mBeforeOrderStatus;
					Util.getContentValidate(R.string.cancel_order);
				} else {
					if (ErrorCode.SUCC == retCode) {
						getStatus(mOrderStatus);//改变订单状态
						DB.saveBoolean(ShopConst.Key.UPP_ORDERSTATUS,true);
						DB.saveBoolean(ShopConst.Key.HAVE_READ,true);
						//桌号
						if (Util.isEmpty(mInputTabe)) {
							mTvOrderNumber.setText("餐号：" + mOrderInfo.getMealNbr());
						} else {
							mTvOrderNumber.setText("桌号：" + mInputTabe);
						}
					} else {
						//修改订单状态失败
						mOrderStatus = mBeforeOrderStatus;
					}
				}
			}
		}).execute(mOrderInfo.getOrderCode(), orderstatus, mOrderInfo.getStatus() , mInputTabe);
	}
	
	/**
	 * 显示支付状态
	 * @param status
	 * @param tvPayStatus
	 */
	private void getStatusType(String status, TextView tvPayStatus){
		if (String.valueOf(Util.NUM_ZERO).equals(status)) {//
			tvPayStatus.setText("支付状态：" + getString(R.string.pend_store));
			
		} else if (String.valueOf(Util.NUM_ONE).equals(status)) {
			tvPayStatus.setText("支付状态：" + getString(R.string.unpaid));
			
		} else if (String.valueOf(Util.NUM_TWO).equals(status)) {
			tvPayStatus.setText("支付状态：" + getString(R.string.payment));
			
		} else if (String.valueOf(Util.NUM_THIRD).equals(status)) {
			tvPayStatus.setText("支付状态：" + getString(R.string.paid));
			
		} else if (String.valueOf(Util.NUM_FOUR).equals(status)) {
			tvPayStatus.setText("支付状态：" + getString(R.string.order_cancel));
			
		} else if (String.valueOf(Util.NUM_FIVE).equals(status)) {
			tvPayStatus.setText("支付状态：" + getString(R.string.fail_pay));
			
		} else if (String.valueOf(Util.NUM_SIX).equals(status)) {
			tvPayStatus.setText("支付状态：" + getString(R.string.refund_application));
			
		} else if (String.valueOf(Util.NUM_SEVEN).equals(status)) {
			tvPayStatus.setText("支付状态：" + getString(R.string.refunded));
		}
	}
	
	/**
	 * 获取订单详情
	 */
	public void getOrderDeatil(){
		new GetProductOrderInfoTask(getActivity(), new GetProductOrderInfoTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (null == result) {    
					return;
				} 
				setData(1); // 有数据
				mOrderInfo = Util.json2Obj(result.toString(), OrderInfo.class);
				mBeforeOrderStatus =  mOrderInfo.getOrderStatus();
				mOrderStatus = mOrderInfo.getOrderStatus();
				//赋值
				initView(mView);
				//订单详情
				orderDetail(result);
				getStatus(mOrderInfo.getOrderStatus());
			}
		}).execute(mOrderCode);
	}
	
	/**
	 * 初始化视图
	 */
	public void initView(View view){
		//初始化数据
		final TextView tvUserName = (TextView) mView.findViewById(R.id.tv_username);//商店名称
		TextView tvOrderNbr = (TextView) mView.findViewById(R.id.tv_ordernbr);//订单号
		TextView tvOrderTime = (TextView) mView.findViewById(R.id.tv_order_time);//时间
		TextView tvPrice = (TextView) mView.findViewById(R.id.price_unit);//消费金额
		TextView tvCouponUnit = (TextView) mView.findViewById(R.id.tv_coupon_unit);//优惠劵抵扣
		TextView tvCardUnit = (TextView) mView.findViewById(R.id.tv_cardprice_unit);//会员卡抵扣
		TextView tvShopBoundsUnit = (TextView) mView.findViewById(R.id.tv_shop_bouns);//商家红包抵扣
		TextView tvHuiquanBoundsUnit = (TextView) mView.findViewById(R.id.tv_huiquan_bounds);//惠圈平台红包抵扣金额
		TextView tvBankCardUnit = (TextView) mView.findViewById(R.id.tv_bankcardcard_unit);//银行卡抵扣
		TextView tvTotalUnit = (TextView) mView.findViewById(R.id.tv_totalprice_unit);//优惠金额
		TextView tvNewPriceUnit = (TextView) mView.findViewById(R.id.tv_newprice_unit);//实际支付
		TextView tvCouponType = (TextView) mView.findViewById(R.id.tv_coupon_type);//优惠券类型
		RelativeLayout ryCouponUnit = (RelativeLayout) mView.findViewById(R.id.ry_couponUtil);//优惠券折扣一整行
		TextView tvInsteadPrice = (TextView) mView.findViewById(R.id.tv_insteadprice);//优惠劵抵扣金额
		mLvFastOrder = (ListView) mView.findViewById(R.id.lv_fast_order_content);//订单的具体内容
		mTvOrderNumber = (TextView) mView.findViewById(R.id.order_number);//餐号
		TextView tvOrderType = (TextView) mView.findViewById(R.id.tv_order_type);//订单类型
		TextView tvOrderRemark = (TextView) mView.findViewById(R.id.tv_remark);//备注信息
		TextView tvOrderCount = (TextView) mView.findViewById(R.id.tv_count);//订单数量
		TextView tvReceiveTime = (TextView) getActivity().findViewById(R.id.tv_flat_receive);//确认时间
		TextView tvOrderTel = (TextView) mView.findViewById(R.id.tv_tel);//电话
		TextView tvPayStatus = (TextView) mView.findViewById(R.id.tv_pay_status);//支付状态
		TextView tvRefundReceiver = (TextView) mView.findViewById(R.id.tv_apply_receiver);// 收货人
		TextView tvRefundTel = (TextView) mView.findViewById(R.id.tv_apply_tel);// 收货人电话
		TextView tvRefundAddress = (TextView) mView.findViewById(R.id.tv_apply_address);// 收货人地址
		RelativeLayout ryIsFirst = (RelativeLayout) mView.findViewById(R.id.ry_isfirst);//首单立减
		TextView tvFirstMoney = (TextView) mView.findViewById(R.id.tv_first_unit);//首单立减金额
		try {
			//支付状态
			getStatusType(mOrderInfo.getStatus(), tvPayStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			//接单时间
			tvReceiveTime.setText(Util.isEmpty(mOrderInfo.getReceiveTime()) ? "00:00" : mOrderInfo.getReceiveTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			//订单类型
			setOrderType(tvOrderType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			//桌号判断
			if (Util.isEmpty(mOrderInfo.getTableNbr())) {
				mTvOrderNumber.setText("餐号：" + mOrderInfo.getMealNbr());
			} else {
				mTvOrderNumber.setText("桌号：" + mOrderInfo.getTableNbr());
			}
			tvUserName.setText("用户：" + mOrderInfo.getReceiver());
			tvOrderTime.setText("下单时间：" + mOrderInfo.getOrderTime());
			tvOrderNbr.setText("订单号：" + mOrderInfo.getOrderNbr());
			tvOrderRemark.setText(mOrderInfo.getRemark());
			tvOrderCount.setText("(" + mOrderInfo.getOrderProductAmount() + "个商品 )");
			tvOrderTel.setText("电话：" + mOrderInfo.getReceiverMobileNbr());
			// 收货人
			tvRefundReceiver.setText(!Util.isEmpty(mOrderInfo.getReceiver()) ? "收货人：" + mOrderInfo.getReceiver() : "收货人：" + "");
			//收货人电话
			tvRefundTel.setText(!Util.isEmpty(mOrderInfo.getReceiverMobileNbr()) ? "电话：" + mOrderInfo.getReceiverMobileNbr() : "电话：" + "");
			//收货人地址
			tvRefundAddress.setText(!Util.isEmpty(mOrderInfo.getDeliveryAddress()) ? "地址：" + mOrderInfo.getDeliveryAddress() : "地址：" + "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			//抵扣金额赋值
			setDiscount(tvPrice, tvNewPriceUnit, tvBankCardUnit, tvCardUnit, tvShopBoundsUnit,
					tvHuiquanBoundsUnit, tvTotalUnit, tvFirstMoney, ryIsFirst);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			//优惠券的使用情况     
			userCoupon(ryCouponUnit, tvCouponType, tvCouponUnit, tvInsteadPrice);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 抵扣金额
	 */
	public void setDiscount(TextView tvPrice, TextView tvNewPriceUnit, TextView tvBankCardUnit,
			TextView tvCardUnit, TextView tvShopBoundsUnit, TextView tvHuiquanBoundsUnit,
			TextView tvTotalUnit, TextView tvIsFirstMoney , RelativeLayout ryIsFirst){
		//消费金额
		tvPrice.setText(!Util.isEmpty(mOrderInfo.getOrderAmount()) ? mOrderInfo.getOrderAmount() + "元" : "0.00" + "元");
		//实际支付
		tvNewPriceUnit.setText(!Util.isEmpty(mOrderInfo.getRealPay()) ? mOrderInfo.getRealPay() + "元" : "0.00" + "元");
		//银行卡抵扣
		tvBankCardUnit.setText(!Util.isEmpty(mOrderInfo.getBankCardDeduction()) ? mOrderInfo.getBankCardDeduction() + "元" : "0.00" + "元");
		//会员卡抵扣
		tvCardUnit.setText(!Util.isEmpty(mOrderInfo.getCardDeduction()) ? mOrderInfo.getCardDeduction() + "元" : "0.00" + "元");
		//商家红包抵扣
		tvShopBoundsUnit.setText(!Util.isEmpty(mOrderInfo.getShopBonus()) ? mOrderInfo.getShopBonus() + "元" : "0.00" + "元");
		//惠圈平台红包抵扣
		tvHuiquanBoundsUnit.setText(!Util.isEmpty(mOrderInfo.getPlatBonus()) ? mOrderInfo.getPlatBonus() + "元" : "0.00" + "元");
		//优惠金额
		tvTotalUnit.setText(!Util.isEmpty(mOrderInfo.getDeduction()) ? mOrderInfo.getDeduction() + "元" : "0.00" + "元");
		//首单立减
		if ("0.00".equals(mOrderInfo.getFirstDeduction())) {
			ryIsFirst.setVisibility(View.GONE);
		} else {
			ryIsFirst.setVisibility(View.VISIBLE);
			tvIsFirstMoney.setText(mOrderInfo.getFirstDeduction() + "元");
		}
	}
	
	/**
	 * 优惠券的使用情况
	 */
	public void userCoupon(RelativeLayout ryCouponUnit, TextView tvCouponType, TextView tvCouponUnit, TextView tvInsteadPrice){
		//判断是否使用了优惠劵
		if (!String.valueOf(Util.NUM_ZERO).equals(mOrderInfo.getCouponUsed()) && !"".equals(mOrderInfo.getCouponUsed())) {//用了
			ryCouponUnit.setVisibility(View.VISIBLE);
			if (ShopConst.Coupon.DEDUCT.equals(mOrderInfo.getCouponType())) {
					tvCouponType.setText(getResources().getString(R.string.order_coupon_deduct));
					if (!Util.isEmpty(mOrderInfo.getInsteadPrice())) {
						if (!Util.isEmpty(mOrderInfo.getAvailablePrice())) {
							tvInsteadPrice.setText("满" + mOrderInfo.getAvailablePrice() + "减" + mOrderInfo.getInsteadPrice());
						}
					}
			} else if (ShopConst.Coupon.DISCOUNT.equals(mOrderInfo.getCouponType())) {
					tvCouponType.setText(getResources().getString(R.string.order_coupon_discount));
					if (!Util.isEmpty(mOrderInfo.getAvailablePrice())) {
						if (!Util.isEmpty(mOrderInfo.getDiscountPercent())) {
							tvInsteadPrice.setText("满" + mOrderInfo.getAvailablePrice() + "打" + mOrderInfo.getDiscountPercent() + "折");
						}
					}
			} else if (ShopConst.Coupon.REG_DEDUCT.equals(mOrderInfo.getCouponType())) {
				tvCouponType.setText(getResources().getString(R.string.order_coupon_deduct));
				if (!Util.isEmpty(mOrderInfo.getInsteadPrice())) {
					if (!Util.isEmpty(mOrderInfo.getAvailablePrice())) {
						tvInsteadPrice.setText("满" + mOrderInfo.getAvailablePrice() + "减" + mOrderInfo.getInsteadPrice());
					}
				}
			}
			tvCouponUnit.setText(!Util.isEmpty(mOrderInfo.getCouponDeduction()) ? mOrderInfo.getCouponDeduction() + "元" : "0.00"+ "元");
		} else {
			ryCouponUnit.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 订单详情
	 */
	public void orderDetail(JSONObject result){
		//订单详情
		JSONArray orderListArray = (JSONArray) result.get("productList");
		List<ProductList> orderListData = new ArrayList<ProductList>();
		if (orderListArray.size() > 0) {
			mLvFastOrder.setVisibility(View.VISIBLE);
			for (int i = 0; i < orderListArray.size(); i++) {
				JSONObject ordertObject = (JSONObject) orderListArray.get(i);
				ProductList productList = Util.json2Obj(ordertObject.toString(), ProductList.class);
				orderListData.add(productList);
			}         
			ProductListAdapter productListAdapter = new ProductListAdapter(getActivity(), orderListData, false);
			mLvFastOrder.setAdapter(productListAdapter);
			Util.setListViewHeight(mLvFastOrder);
		} else {
			mLvFastOrder.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 订单类型
	 * @param v
	 */
	public void setOrderType(TextView tvOrderType){
		if (Util.isEmpty(mOrderInfo.getOrderType())) {
			tvOrderType.setText("订单类型：" +  "");
		} else {
			if (Integer.parseInt(mOrderInfo.getOrderType()) == 10) {
				tvOrderType.setText("订单类型：" + "其他订单");
			} 
			else if (Integer.parseInt(mOrderInfo.getOrderType()) == 20) {
				tvOrderType.setText("订单类型：" + "堂食订单");
			}
			else if (Integer.parseInt(mOrderInfo.getOrderType()) == 21) {
				tvOrderType.setText("订单类型：" + "外卖订单");
			}
		}
	}
	
	@OnClick(R.id.layout_turn_in)
	private void click(View v) {
		getActivity().finish();
	}
}
