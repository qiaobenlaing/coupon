package com.huift.hfq.shop.fragment;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.huift.hfq.shop.R;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.OrderInfo;
import com.huift.hfq.base.pojo.ProductList;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.ShopConst.Coupon;
import com.huift.hfq.shop.adapter.ProductListAdapter;
import com.huift.hfq.shop.model.GetProductOrderInfoTask;
import com.huift.hfq.shop.model.UpdateDealRefundTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 退款订单详情页面
 * @author qian.zhou
 */
public class MyRefundOrderDetailFragment extends Fragment {
	private static final String TAG = "MyRefundOrderDetailFragment";
	public static final String ORDER_CODE = "orderCode";
	private String mOrderCode;
	/** 正在加载数据 */
	private LinearLayout mLyNodate;
	/** 正在加载的内容 */
	private LinearLayout mLyContent;
	/** 是否收缩 */
	private ImageView mIvArrow;
	/** 记录点击次数 */
	private int mClickNum;
	private LinearLayout mIvMsg;
	/** 弹出层 */     
	private PopupWindow mPopupWindow;
	private View mView;
	/** 菜单列表 */
	private ListView mLvFastOrder;
	private OrderInfo mOrderInfo;
	/** 改变前的状态 */
	private String mBeforeOrderStatus;
	/** 修改支付状态 */
	private String mStatus;
	/** 标题 */
	private TextView mTvContent;
	/** 抵扣一整行*/
	private LinearLayout mLyAllDiscount;

	/**
	 * 需要传递参数时有利于解耦
	 */
	public static MyRefundOrderDetailFragment newInstance() {
		Bundle args = new Bundle();
		MyRefundOrderDetailFragment fragment = new MyRefundOrderDetailFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.activity_fasting_order_distribution_detail, container, false);
		ViewUtils.inject(this, mView);
		Util.addLoginActivity(getActivity());
		init(mView);
		return mView;
	}

	// 初始化方法
	private void init(View view) {
		//取值
		Intent intent = getActivity().getIntent();
		mOrderCode = intent.getStringExtra(ORDER_CODE);
		mClickNum = 0;
		LinearLayout ivTurnin = (LinearLayout) view.findViewById(R.id.layout_turn_in);
		ivTurnin.setVisibility(View.VISIBLE);
		ivTurnin.setOnClickListener(turnListener);
		mIvMsg = (LinearLayout) view.findViewById(R.id.layout_msg);
		mIvMsg.setOnClickListener(orderClickListener);
		// 加载数据
		mLyNodate = (LinearLayout) view.findViewById(R.id.ly_nodate);
		mLyContent = (LinearLayout) view.findViewById(R.id.ly_content);
		// 导向箭头
		mIvArrow = (ImageView) view.findViewById(R.id.iv_arrow_down);
		RelativeLayout ryDiscount = (RelativeLayout) view.findViewById(R.id.ry_arrow_discount);// 优惠金额一整行
		mLyAllDiscount = (LinearLayout) view.findViewById(R.id.ly_all_discount);// 所有折扣
		ryDiscount.setOnClickListener(discountListener);
		mIvArrow.setImageResource(mClickNum % 2 == 0 ? R.drawable.upc_arrow : R.drawable.downc_arrow);
		setData(0); // 正在加载数据
		// 订单详情方法
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
				Log.d(TAG, "进来了1..");
			} else {
				mIvArrow.setImageResource(R.drawable.downc_arrow);
				mLyAllDiscount.setVisibility(View.GONE);
				Log.d(TAG, "进来了2..");
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
	 * 弹出层
	 */
	OnClickListener orderClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mView = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindow_myafterorder, null);
			mView.setBackgroundColor(Color.TRANSPARENT);
			mPopupWindow = new PopupWindow(mView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			ScaleAnimation animation = new ScaleAnimation(1.0f, 1.0f, 0F, 1f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 1.0f);
			animation.setDuration(500);
			animation.setFillAfter(true);
			mPopupWindow.setFocusable(true);
			mPopupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
			mPopupWindow.setOutsideTouchable(true);
			mView.setAnimation(animation);

			Button mBtnMyAfterOrderUser = (Button) mView.findViewById(R.id.btn_myafterorder_user);
			Button mBtnMyAfterOrderOk = (Button) mView.findViewById(R.id.btn_myafterorder_order_ok);
			mBtnMyAfterOrderOk.setText(R.string.noagree_refund);
			Button mBtnMyAfterOrderBack = (Button) mView.findViewById(R.id.btn_myafterorder_order_back);
			mBtnMyAfterOrderBack.setText(R.string.agree_refund);
			Button mBtnMyAfterOrderCancel = (Button) mView.findViewById(R.id.btn_myafterorder_cancel);

			mBtnMyAfterOrderUser.setOnClickListener(afterOrderListener);
			mBtnMyAfterOrderOk.setOnClickListener(afterOrderListener);
			mBtnMyAfterOrderBack.setOnClickListener(afterOrderListener);
			mBtnMyAfterOrderCancel.setOnClickListener(afterOrderListener);

			mPopupWindow.showAtLocation(mView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		}
	};

	OnClickListener afterOrderListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_myafterorder_user:// 联系用户
				DialogUtils.showDialog(getActivity(), getString(R.string.cue), getString(R.string.dialog_tel),
						getString(R.string.ok), getString(R.string.no), new DialogUtils().new OnResultListener() {
							@Override
							public void onOK() {
								Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(getResources().getString(R.string.user_tel)+ mOrderInfo.getReceiverMobileNbr()));
								startActivity(intent);
							}
						});
				break;
			case R.id.btn_myafterorder_order_ok:// 拒绝退款
				DialogUtils.showDialog(getActivity(), getString(R.string.cue), getString(R.string.myflat_content),
						getString(R.string.ok), getString(R.string.no), new DialogUtils().new OnResultListener() {
							@Override
							public void onOK() {
								mStatus = "0";
								getSubmitEnd(mStatus);
							}
							@Override
							public void onCancel() {
							}
						});
				mPopupWindow.dismiss();
				break;
			case R.id.btn_myafterorder_order_back:// 同意退款
				mStatus = "1";
				getSubmitEnd(mStatus);
				mPopupWindow.dismiss();
				break;
			case R.id.btn_myafterorder_cancel:// 取消
				mPopupWindow.dismiss();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 设置数据
	 * @param type
	 * 有没有数据 1 是有数据 0 是没有数据
	 */
	private void setData(int type) {
		if (type == 1) {
			mLyNodate.setVisibility(View.GONE);
			mLyContent.setVisibility(View.VISIBLE);
		} else {
			mLyNodate.setVisibility(View.VISIBLE);
			mLyContent.setVisibility(View.GONE);
		}
	}

	/**
	 * 商家是否同意退款
	 */
	public void getSubmitEnd(String orderstatus) {
		new UpdateDealRefundTask(getActivity(), new UpdateDealRefundTask.Callback() {
			@Override
			public void getResult(int retCode) {
				if (retCode == 0) {
					// 调用API失败
					mStatus = mBeforeOrderStatus;
				} else {
					if (ErrorCode.SUCC == retCode) {
						getOrderDeatil();
						DB.saveBoolean(ShopConst.Key.UPP_ORDERSTATUS, true);
						DB.saveBoolean(ShopConst.Key.HAVE_READ, true);
					} else {
						// 调用API失败
						mStatus = mBeforeOrderStatus;
					}
				}
			}
		}).execute(mOrderInfo.getOrderCode(), orderstatus);
	}
	
	/**
	 * 显示支付状态
	 * @param status
	 * @param tvPayStatus
	 */
	private void getStatusType(String status, TextView tvPayStatus){
		if (String.valueOf(Util.NUM_ZERO).equals(status)) {//
			tvPayStatus.setText(getString(R.string.pend_store));
			
		} else if (String.valueOf(Util.NUM_ONE).equals(status)) {
			tvPayStatus.setText(getString(R.string.unpaid));
			
		} else if (String.valueOf(Util.NUM_TWO).equals(status)) {
			tvPayStatus.setText(getString(R.string.payment));
			
		} else if (String.valueOf(Util.NUM_THIRD).equals(status)) {
			tvPayStatus.setText(getString(R.string.paid));
			
		} else if (String.valueOf(Util.NUM_FOUR).equals(status)) {
			tvPayStatus.setText(getString(R.string.order_cancel));
			
		} else if (String.valueOf(Util.NUM_FIVE).equals(status)) {
			tvPayStatus.setText(getString(R.string.fail_pay));
			
		} else if (String.valueOf(Util.NUM_SIX).equals(status)) {
			tvPayStatus.setText(getString(R.string.refund_application));
			
		} else if (String.valueOf(Util.NUM_SEVEN).equals(status)) {
			tvPayStatus.setText(getString(R.string.refunded));
		}
	}

	/**
	 * 获得订单详情
	 */
	public void getOrderDeatil() {
		new GetProductOrderInfoTask(getActivity(), new GetProductOrderInfoTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (null == result) { return; }
				setData(1); // 有数据
				mOrderInfo = Util.json2Obj(result.toString(), OrderInfo.class);
				// 初始化数据
				final TextView tvUserName = (TextView) mView.findViewById(R.id.tv_username);// 商店名称
				TextView tvOrderNbr = (TextView) mView.findViewById(R.id.tv_ordernbr);// 订单号
				TextView tvOrderTime = (TextView) mView.findViewById(R.id.tv_order_time);// 时间
				TextView tvPrice = (TextView) mView.findViewById(R.id.price_unit);// 消费金额
				TextView tvCouponUnit = (TextView) mView.findViewById(R.id.tv_coupon_unit);// 优惠劵抵扣
				TextView tvCardUnit = (TextView) mView.findViewById(R.id.tv_cardprice_unit);// 会员卡抵扣
				TextView tvShopBoundsUnit = (TextView) mView.findViewById(R.id.tv_shop_bouns);// 商家红包抵扣
				TextView tvHuiquanBoundsUnit = (TextView) mView.findViewById(R.id.tv_huiquan_bounds);// 惠圈平台红包抵扣金额
				TextView tvBankCardUnit = (TextView) mView.findViewById(R.id.tv_bankcardcard_unit);// 银行卡抵扣
				TextView tvTotalUnit = (TextView) mView.findViewById(R.id.tv_totalprice_unit);// 优惠金额
				TextView tvNewPriceUnit = (TextView) mView.findViewById(R.id.tv_newprice_unit);// 实际支付
				TextView tvCouponType = (TextView) mView.findViewById(R.id.tv_coupon_type);// 优惠券类型
				RelativeLayout ryCouponUnit = (RelativeLayout) mView.findViewById(R.id.ry_couponUtil);// 优惠券折扣一整行
				TextView tvInsteadPrice = (TextView) mView.findViewById(R.id.tv_insteadprice);// 优惠劵抵扣金额
				mLvFastOrder = (ListView) mView.findViewById(R.id.lv_fast_order_content);// 订单的具体内容
				TextView tvOrderNumber = (TextView) mView.findViewById(R.id.order_number);// 餐号
				TextView tvOrderType = (TextView) mView.findViewById(R.id.tv_order_type);// 订单类型
				TextView tvOrderRemark = (TextView) mView.findViewById(R.id.tv_remark);// 备注信息
				TextView tvOrderCount = (TextView) mView.findViewById(R.id.tv_count);// 订单数量
				TextView tvOrderTel = (TextView) mView.findViewById(R.id.tv_tel);// 电话
				TextView tvRefundTime = (TextView) mView.findViewById(R.id.tv_refund_time);// 退款申请时间
				TextView tvRefundFunction = (TextView) mView.findViewById(R.id.tv_refund_function);// 退款说明
				TextView tvRefundRemark = (TextView) mView.findViewById(R.id.tv_refundremark);// 退款备注
				TextView tvRefundReceiver = (TextView) mView.findViewById(R.id.tv_apply_receiver);// 收货人
				TextView tvRefundTel = (TextView) mView.findViewById(R.id.tv_apply_tel);// 收货人电话
				TextView tvRefundAddress = (TextView) mView.findViewById(R.id.tv_apply_address);// 收货人地址
				TextView tvPayStatus = (TextView) mView.findViewById(R.id.tv_pay_status);//支付状态
				// 标题
				mTvContent = (TextView) mView.findViewById(R.id.tv_mid_content);
				//赋值
				try {
					tvOrderNumber.setText("餐号：" + mOrderInfo.getMealNbr());
					tvUserName.setText("用户：" + mOrderInfo.getReceiver());
					tvOrderTime.setText("下单时间：" + mOrderInfo.getOrderTime());
					tvOrderNbr.setText("订单号：" + mOrderInfo.getOrderNbr());
					tvOrderRemark.setText(mOrderInfo.getRemark());
					tvOrderCount.setText("(" + mOrderInfo.getOrderProductAmount() + "个商品 )");
					tvOrderTel.setText("电话：" + mOrderInfo.getReceiverMobileNbr());
				} catch (Exception e) {
					Log.e(TAG, "Exception  >>> ************* -1" + e.getMessage());
				}
				//给退款的具体信息赋值
				try {
					setRefund(tvRefundTime, tvRefundFunction, tvRefundRemark, tvRefundReceiver,
							tvRefundTel, tvRefundAddress);
				} catch (Exception e) {
					Log.e(TAG, "Exception  >>> ************* 0" + e.getMessage());
				}
				//支付状态
				try {
					getStatusType(mOrderInfo.getStatus(), tvPayStatus);
				} catch (Exception e) {
					Log.e(TAG, "Exception  >>> *************" + e.getMessage());
				}
				//头顶标题
				try {
					setHeadTitle();
				} catch (Exception e) {
					Log.e(TAG, "Exception  >>> *************1" + e.getMessage());
				}
				//订单类型
				try {
					orderType(tvOrderType);
				} catch (Exception e) {
					Log.e(TAG, "Exception  >>> *************2" + e.getMessage());
				}
				//各种抵扣金额赋值
				try {
					discountMoney(tvPrice, tvNewPriceUnit, tvBankCardUnit, 
							tvCardUnit, tvShopBoundsUnit, tvHuiquanBoundsUnit, tvTotalUnit);
				} catch (Exception e) {
					Log.e(TAG, "Exception  >>> *************3" + e.getMessage());
				}
				//优惠券的使用情况
				try {
					useCoupon(ryCouponUnit, tvCouponType, tvInsteadPrice, tvCouponUnit);
				} catch (Exception e) {
					Log.e(TAG, "Exception  >>> *************4" + e.getMessage());
				}
				//订单详情
				try {
					orderDetail(result);
				} catch (Exception e) {
					Log.e(TAG, "Exception  >>> *************5" + e.getMessage());
				}
			}
		}).execute(mOrderCode);
	}
	
	/**
	 * 给退款的具体信息赋值
	 */
	public void setRefund(TextView tvRefundTime, TextView tvRefundFunction, TextView tvRefundRemark,
			TextView tvRefundReceiver, TextView tvRefundTel, TextView tvRefundAddress){
		// 退款申请时间
		tvRefundTime.setText(!Util.isEmpty(mOrderInfo.getRefundApplyTime()) ? "退款申请:   " + "(" + mOrderInfo.getRefundApplyTime() + ")" : "退款申请:   " + "(" + "00:00" + ")");
		// 退款理由
		tvRefundFunction.setText(!Util.isEmpty(mOrderInfo.getRefundReason()) ? "退款说明：" + mOrderInfo.getRefundReason() : "退款说明：" + "");
		// 退款备注
		tvRefundRemark.setText(!Util.isEmpty(mOrderInfo.getRefundRemark()) ? "备注信息：" + mOrderInfo.getRefundRemark() : "备注信息：" + "");
		// 收货人
		tvRefundReceiver.setText(!Util.isEmpty(mOrderInfo.getReceiver()) ? "收货人：" + mOrderInfo.getReceiver() : "收货人：" + "");
		// 收货人电话
		tvRefundTel.setText(!Util.isEmpty(mOrderInfo.getReceiverMobileNbr()) ? "电话：" + mOrderInfo.getReceiverMobileNbr() : "电话：" + "");
		// 收货人地址
		tvRefundAddress.setText(!Util.isEmpty(mOrderInfo.getDeliveryAddress()) ? "地址：" + mOrderInfo.getDeliveryAddress() : "地址：" + "");
	}
	
	/**
	 * 头顶标题
	 * @param v
	 */
	public void setHeadTitle(){
		// 根据支付状态判断头顶标题
		if (String.valueOf(Util.NUM_SIX).equals(mOrderInfo.getStatus())) {//退款申请中
			
			mTvContent.setText(R.string.refund_application);
			
		} else if (String.valueOf(Util.NUM_SEVEN).equals(mOrderInfo.getStatus())) {//退款成功
			
			mTvContent.setText(R.string.refund_application_success);
			
		} else if (String.valueOf(Util.NUM_THIRD).equals(mOrderInfo.getStatus())) {//已支付
			
			mTvContent.setText(R.string.refund_application_payed);
		}
	}
	
	/**
	 * 订单类型
	 * @param v
	 */
	public void orderType(TextView tvOrderType){
		// 订单类型
		if (Util.isEmpty(mOrderInfo.getOrderType())) {
			tvOrderType.setText("订单类型：" + "");
		} else {
			if (Integer.parseInt(mOrderInfo.getOrderType()) == 10) {
				tvOrderType.setText("订单类型：" + "其他订单");
			} else if (Integer.parseInt(mOrderInfo.getOrderType()) == 20) {
				tvOrderType.setText("订单类型：" + "堂食订单");
			} else if (Integer.parseInt(mOrderInfo.getOrderType()) == 21) {
				tvOrderType.setText("订单类型：" + "外卖订单");
			}
		}
	}
	
	/**
	 * 各种抵扣金额
	 * @param v
	 */
	public void discountMoney(TextView tvPrice, TextView tvNewPriceUnit, TextView tvBankCardUnit, 
			TextView tvCardUnit, TextView tvShopBoundsUnit, TextView tvHuiquanBoundsUnit, TextView tvTotalUnit){
		// 消费金额
		tvPrice.setText(!Util.isEmpty(mOrderInfo.getOrderAmount()) ? mOrderInfo.getOrderAmount() + "元" : "0.00" + "元");
		// 实际支付
		tvNewPriceUnit.setText(!Util.isEmpty(mOrderInfo.getRealPay()) ? mOrderInfo.getRealPay() + "元" : "0.00" + "元");
		// 银行卡抵扣
		tvBankCardUnit.setText(!Util.isEmpty(mOrderInfo.getBankCardDeduction()) ? mOrderInfo.getBankCardDeduction() + "元" : "0.00" + "元");
		// 会员卡抵扣
		tvCardUnit.setText(!Util.isEmpty(mOrderInfo.getCardDeduction()) ? mOrderInfo.getCardDeduction() + "元" : "0.00" + "元");
		// 商家红包抵扣
		tvShopBoundsUnit.setText(!Util.isEmpty(mOrderInfo.getShopBonus()) ? mOrderInfo.getShopBonus() + "元" : "0.00" + "元");
		// 惠圈平台红包抵扣
		tvHuiquanBoundsUnit.setText(!Util.isEmpty(mOrderInfo.getPlatBonus()) ? mOrderInfo.getPlatBonus() + "元" : "0.00" + "元");
		// 优惠金额
		tvTotalUnit.setText(!Util.isEmpty(mOrderInfo.getDeduction()) ? mOrderInfo.getDeduction() + "元" : "0.00" + "元");
	}
	
	/**
	 * 优惠券的使用情况
	 * @param v
	 */
	public void useCoupon(RelativeLayout ryCouponUnit, TextView tvCouponType, TextView tvInsteadPrice,
			TextView tvCouponUnit){
		// 判断是否使用了优惠劵
		if (String.valueOf(Util.NUM_ONE).equals(mOrderInfo.getCouponUsed())) {// 用了
			ryCouponUnit.setVisibility(View.VISIBLE);
			if (Coupon.DEDUCT.equals(mOrderInfo.getCouponType())) {
				tvCouponType.setText(getResources().getString(R.string.order_coupon_deduct));
				if (!Util.isEmpty(mOrderInfo.getInsteadPrice())) {
					if (!Util.isEmpty(mOrderInfo.getAvailablePrice())) {
						tvInsteadPrice.setText("满" + mOrderInfo.getAvailablePrice() + "减"
								+ mOrderInfo.getInsteadPrice());
					}
				}
			} else if (Coupon.DISCOUNT.equals(mOrderInfo.getCouponType())) {
				tvCouponType.setText(getResources().getString(R.string.order_coupon_discount));
				if (!Util.isEmpty(mOrderInfo.getAvailablePrice())) {
					if (!Util.isEmpty(mOrderInfo.getDiscountPercent())) {
						tvInsteadPrice.setText("满" + mOrderInfo.getAvailablePrice() + "打"
								+ mOrderInfo.getDiscountPercent() + "折");
					}
				}
			} else if (Coupon.REG_DEDUCT.equals(mOrderInfo.getCouponType())) {
				tvCouponType.setText(getResources().getString(R.string.order_coupon_deduct));
				if (!Util.isEmpty(mOrderInfo.getInsteadPrice())) {
					if (!Util.isEmpty(mOrderInfo.getAvailablePrice())) {
						tvInsteadPrice.setText("满" + mOrderInfo.getAvailablePrice() + "减"
								+ mOrderInfo.getInsteadPrice());
					}
				}
			}
			if (Util.isEmpty(mOrderInfo.getCouponDeduction())) {
				tvCouponUnit.setText("0.00" + "元");
			} else {
				tvCouponUnit.setText(mOrderInfo.getCouponDeduction() + "元");
			}
		} else {
			ryCouponUnit.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 订单详情
	 * @param v
	 */
	public void orderDetail(JSONObject result){
		JSONArray orderListArray = (JSONArray) result.get("productList");
		List<ProductList> orderListData = new ArrayList<ProductList>();
		if (orderListArray.size() > 0) {
			mLvFastOrder.setVisibility(View.VISIBLE);
			for (int i = 0; i < orderListArray.size(); i++) {
				JSONObject ordertObject = (JSONObject) orderListArray.get(i);
				Log.d(TAG, "ordertObject==" + orderListArray.get(i));
				ProductList productList = Util.json2Obj(ordertObject.toString(), ProductList.class);
				Log.d(TAG, "productList==" + productList);
				Log.d(TAG, "getIsNewlyAdd==" + productList.getIsNewLyAdd());
				orderListData.add(productList);
			}
			ProductListAdapter productListAdapter = new ProductListAdapter(getActivity(), orderListData, false);
			mLvFastOrder.setAdapter(productListAdapter);
			Util.setListViewHeight(mLvFastOrder);
		} else {
			mLvFastOrder.setVisibility(View.GONE);
		}
	}

	@OnClick(R.id.layout_turn_in)
	private void click(View v) {
		getActivity().finish();
	}
}
