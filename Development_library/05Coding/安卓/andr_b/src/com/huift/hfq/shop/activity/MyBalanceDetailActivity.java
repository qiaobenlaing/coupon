package com.huift.hfq.shop.activity;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.model.SetPayResultTask.OnResultListener;
import com.huift.hfq.base.pojo.OrderDetail;
import com.huift.hfq.base.pojo.UserCardVip;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.fragment.VipChatFragment;
import com.huift.hfq.shop.model.MyOrderDetailListTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
 
/**
 * 订单详情页面
 * @author qian.zhou
 */
public class MyBalanceDetailActivity extends Activity {
	
	private static final String TAG = "MyOrderDetailActivity";
	
	public static final String CONSUME_CODE = "consumeCode";
	/** 标记是完成的订单页面还是未完成的订单页面进来的*/
	public static final String ORDER_STATUS = "order_sratus";
	public static final double STATUS_NUM = 0.00;
	/** 订单编码*/
	private String mConsumeCode;
	/** 正在加载数据*/
	private LinearLayout mLyNodate;
	/** 正在加载的内容*/
	private LinearLayout mLyContent;
	/** 是否收缩*/
	private ImageView mIvArrow;
	/** 记录点击次数*/
	private int mClickNum;
	/** 订单对象*/
	private OrderDetail mOrderDetail;
	/** 批次*/
	private TextView tvBatchNb;
	/** 优惠金额的一行*/
	private RelativeLayout mRyDiscount;
	/** 批次的一行*/
	private RelativeLayout rlBatchNb;
	/** 兑换码的一行*/
	private RelativeLayout rlExchangecode;
	/** 商家红包的一行*/
	private RelativeLayout rlShopBouns;
	/** 惠圈红包*/
	private RelativeLayout rlHuiquanBouns;
	/** 不参与优惠金额的一行*/
	private RelativeLayout rlPart;
	/** 支付类型的一行*/
	private RelativeLayout rlOrderPayType;
	/** 不参与优惠金额的一行*/
	private TextView tvPart;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ViewUtils.inject(this);
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
        init();
    }
    
	private void init() {
		mClickNum = 0;
		//标题
		TextView tvContent = (TextView) findViewById(R.id.tv_mid_content);
		tvContent.setText(getString(R.string.order_detail));
		LinearLayout ivTurnin = (LinearLayout) findViewById(R.id.layout_turn_in);
		ivTurnin.setVisibility(View.VISIBLE);
		mLyNodate = (LinearLayout) findViewById(R.id.ly_nodate);
		mLyContent = (LinearLayout) findViewById(R.id.ry_content);
		//取值
		Intent intent = this.getIntent();
		mConsumeCode = intent.getStringExtra(CONSUME_CODE);
		//导向箭头
		mIvArrow = (ImageView) findViewById(R.id.iv_arrow_down);
		mRyDiscount = (RelativeLayout) findViewById(R.id.ry_arrow_discount);//优惠金额一整行
		final LinearLayout lyAllDiscount = (LinearLayout) findViewById(R.id.ly_all_discount);//所有折扣
		if (mClickNum % 2 == 0) {
			mIvArrow.setImageResource(R.drawable.downc_arrow);
			lyAllDiscount.setVisibility(View.GONE);
		} else {
			mIvArrow.setImageResource(R.drawable.upc_arrow);
			lyAllDiscount.setVisibility(View.VISIBLE);
		}
		mRyDiscount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mClickNum++;
				if (mClickNum % 2 == 0) {
					mIvArrow.setImageResource(R.drawable.downc_arrow);
					lyAllDiscount.setVisibility(View.GONE);
					Log.d(TAG, "进来了2..");
				} else {
					mIvArrow.setImageResource(R.drawable.upc_arrow);
					lyAllDiscount.setVisibility(View.VISIBLE);
					Log.d(TAG, "进来了1..");
				}
			}
		});
		//订单详情方法
		getOrderDeatil();
	}
	
	/**
	 * 查询获得对账订单详情
	 */
	public void getOrderDeatil(){
		
		new MyOrderDetailListTask(MyBalanceDetailActivity.this, new MyOrderDetailListTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if(null == result){
					return;
				} else{
					mOrderDetail = Util.json2Obj(result.toString(), OrderDetail.class);
					//设置数据
					initView(mOrderDetail);
				}
			}
		}).execute(mConsumeCode);
	}
	
	/**
	 * 初始化视图
	 */
	public void initView(final OrderDetail orderDetail){
		//订单的基本情况
		setOrder(orderDetail);
		//支付类型
		orderType(orderDetail);
		//是否是兑换券或者代金券
		exchangeOrVoucher(orderDetail);
		//判断订单状态
		setOrderStatus(orderDetail);
		//折扣金额
		setMoney(orderDetail);
		//优惠券使用情况
		setCoupon(orderDetail);
	}
	
	/**
	 * 基本赋值情况
	 */
	private void setOrder(final OrderDetail orderDetail) {
		ImageView tvOrderImage = (ImageView) findViewById(R.id.iv_image);//用户头像
		TextView tvOrderName = (TextView) findViewById(R.id.tv_order_name);//用户名称
		TextView tvOrderTel = (TextView) findViewById(R.id.tv_order_tell);//用户电话
		TextView tvOrderNbr = (TextView) findViewById(R.id.tv_ordernbr);//订单号
		TextView tvOrderTime = (TextView) findViewById(R.id.tv_order_time);//时间
		TextView tvPrice = (TextView) findViewById(R.id.price_unit);//消费金额
		RelativeLayout rlClickTell = (RelativeLayout) findViewById(R.id.rl_order_tell);//点击电话
		rlClickTell.setOnClickListener(rlTellListener);
		RelativeLayout rlUserClick = (RelativeLayout) findViewById(R.id.rl_order_click);//用户的点击
		//用户一行的点击事件
		rlUserClick.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UserCardVip cardVipe = new UserCardVip();
				cardVipe.setUserCode(orderDetail.getUserCode());
				cardVipe.setNickName(orderDetail.getNickName());
				Intent intent = new Intent(MyBalanceDetailActivity.this,VipChatActivity.class );
				intent.putExtra(VipChatFragment.USER_OBJ, cardVipe);
				startActivity(intent);
			}
		});
		
		Util.showImage(MyBalanceDetailActivity.this, orderDetail.getAvatarUrl() , tvOrderImage);
		tvOrderName.setText(!Util.isEmpty(orderDetail.getNickName()) ? orderDetail.getNickName() : "");
		tvOrderTel.setText(!Util.isEmpty(orderDetail.getUserMobileNbr()) ? orderDetail.getUserMobileNbr() : "");
		tvOrderNbr.setText(!Util.isEmpty(orderDetail.getOrderNbr()) ? orderDetail.getOrderNbr() : "");
		tvOrderTime.setText(!Util.isEmpty(orderDetail.getConsumeTime()) ? orderDetail.getConsumeTime() : "");
		tvPrice.setText(!Util.isEmpty(orderDetail.getOrderAmount()) ? orderDetail.getOrderAmount() + "元": "0.00" + "元");
	}
	
	/**
	 * 支付类型
	 */
	private void orderType (OrderDetail orderDetail) {
		TextView tvOrderPayType = (TextView) findViewById(R.id.tv_ordernbr_paytype);//支付类型
		//支付类型
		if (!Util.isEmpty(orderDetail.getPayType())) {
			if (Integer.parseInt(orderDetail.getPayType()) == Util.NUM_ONE) {
				tvOrderPayType.setText(R.string.order_paytype_one);
			} else if (Integer.parseInt(orderDetail.getPayType()) == Util.NUM_TWO) {
				tvOrderPayType.setText(R.string.order_paytype_two);
			} else if (Integer.parseInt(orderDetail.getPayType()) == Util.NUM_THIRD) {
				tvOrderPayType.setText(R.string.order_paytype_three);
			} else if (Integer.parseInt(orderDetail.getPayType()) == Util.NUM_FOUR) {
				tvOrderPayType.setText(R.string.order_paytype_four);
			} else if (Integer.parseInt(orderDetail.getPayType()) == Util.NUM_FIVE) {
				tvOrderPayType.setText(R.string.order_paytype_five);
			}
		}
	}

	/**
	 * 打电话
	 */
	OnClickListener rlTellListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			DialogUtils.showDialog(MyBalanceDetailActivity.this, getString(R.string.cue),getString(R.string.myafter_order_phone_ok), getString(R.string.ok), getString(R.string.no),new DialogUtils().new OnResultListener() {
				@Override 
				public void onOK() {
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+mOrderDetail.getUserMobileNbr()));
					startActivity(intent);
				}
			});
		}
	};
	
	/**
	 * 折扣金额
	 * @param v
	 */
	public void setMoney(OrderDetail orderDetail){
		TextView tvNewPriceUnit = (TextView) findViewById(R.id.tv_newprice_unit);//实际支付
		TextView tvBankCardUnit = (TextView) findViewById(R.id.tv_bankcardcard_unit);//银行卡抵扣
		TextView tvCardUnit = (TextView) findViewById(R.id.tv_cardprice_unit);//会员卡抵扣
		TextView tvShopBoundsUnit = (TextView) findViewById(R.id.tv_shop_bouns);//商家红包抵扣
		TextView tvHuiquanBoundsUnit = (TextView) findViewById(R.id.tv_huiquan_bounds);//惠圈平台红包抵扣金额
		TextView tvTotalUnit = (TextView) findViewById(R.id.tv_totalprice_unit);//总抵扣
		
		//实际金额
		tvNewPriceUnit.setText(!Util.isEmpty(orderDetail.getRealPay()) ? orderDetail.getRealPay() + "元" : "0.00" + "元");
		//银行卡抵扣
		tvBankCardUnit.setText(!Util.isEmpty(orderDetail.getBankCardDeduction()) ? orderDetail.getBankCardDeduction() + "元" : "0.00" + "元");
		//会员卡抵扣
		tvCardUnit.setText(!Util.isEmpty(orderDetail.getCardDeduction()) ? orderDetail.getCardDeduction() + "元" : "0.00" + "元");
		//商家红包抵扣
		tvShopBoundsUnit.setText(!Util.isEmpty(orderDetail.getShopBonusDeduction()) ? orderDetail.getShopBonusDeduction() + "元" : "0.00" + "元");
		//惠圈平台红包抵扣
		tvHuiquanBoundsUnit.setText(!Util.isEmpty(orderDetail.getPlatBonusDeduction()) ? orderDetail.getPlatBonusDeduction() + "元" : "0.00" + "元");
		//总抵扣支付
		tvTotalUnit.setText(!Util.isEmpty(orderDetail.getDeduction()) ? orderDetail.getDeduction() + "元" : "0.00" + "元");
	}
	
	/**
	 * 给订单状态赋值
	 * @param v
	 */
	public void setOrderStatus(OrderDetail orderDetail){
		TextView tvOrderStatus = (TextView) findViewById(R.id.tv_order_status);//订单状态
		TextView tvOrderMoney = (TextView) findViewById(R.id.tv_order_money);//实际金额
		
		Log.d(TAG, "getStatus()==="+orderDetail.getStatus());
		//判断订单的状态
		if (Integer.parseInt(orderDetail.getStatus()) == 3){
			tvOrderStatus.setText("交易成功");
			//金额
			if(Util.isEmpty(orderDetail.getRealPay())){
				tvOrderMoney.setText("0" + "元");
			} else{
				tvOrderMoney.setText("+" + orderDetail.getRealPay() + "元");
			}
		}
		else if (Integer.parseInt(orderDetail.getStatus()) == 6){
			tvOrderStatus.setText("退款申请中");
			//金额
			if(Util.isEmpty(orderDetail.getRealPay())){
				tvOrderMoney.setText("0" + "元");
			} else{
				tvOrderMoney.setText("-" + orderDetail.getRealPay());
			}
		}
		else if (Integer.parseInt(orderDetail.getStatus()) == 7){
			tvOrderStatus.setText("已退款");
			//金额
			if(Util.isEmpty(orderDetail.getRealPay())){
				tvOrderMoney.setText("0" + "元");
			} else{
				tvOrderMoney.setText("-" + orderDetail.getRealPay());
			}
		}
	}
	
	/**
	 * 是否是兑换券或者代金券
	 */
	private void exchangeOrVoucher (OrderDetail orderDetail) {
		TextView tvBatchNb = (TextView) findViewById(R.id.tv_batchnbr);//批次
		TextView tvPayPrice = (TextView) findViewById(R.id.tv_batch_function);//内容
		rlBatchNb = (RelativeLayout) findViewById(R.id.rl_orderdetail_batch);//批次的一行
		
		TextView tvCouponCode = (TextView) findViewById(R.id.tv_exchangecode);//兑换码
		rlExchangecode = (RelativeLayout) findViewById(R.id.rl_orderdetail_exchangecode);//兑换码的一行
		rlShopBouns = (RelativeLayout) findViewById(R.id.rl_ordershop_bouns);//商家红包
		rlHuiquanBouns = (RelativeLayout) findViewById(R.id.rl_orderhuiquan_bouns);//惠圈红包
		rlPart =  (RelativeLayout) findViewById(R.id.rl_part);//不参与优惠金额的一行
		rlOrderPayType = (RelativeLayout) findViewById(R.id.rl_orderdetail_paytype);//支付类型的一行
		tvPart = (TextView) findViewById(R.id.price_part);//不参与优惠金额
		TextView tvShopBouns = (TextView) findViewById(R.id.tv_ordershop_bouns);//商家红包
		TextView tvHuiquanBouns = (TextView) findViewById(R.id.tv_orderhuiquan_bounds);//惠圈红包
		
		if (!Util.isEmpty(orderDetail.getCouponType())) {
			if (Integer.parseInt(orderDetail.getCouponType()) == Util.NUM_SEVEN) {//兑换券
				//批次
				tvBatchNb.setText(!Util.isEmpty(orderDetail.getBatchNbr()) ? orderDetail.getBatchNbr() : "");
				//兑换码
				tvCouponCode.setText(!Util.isEmpty(orderDetail.getCouponCode()) ? orderDetail.getCouponCode() : "");
				//内容
				tvPayPrice.setText(!Util.isEmpty(orderDetail.getFunction()) ? orderDetail.getFunction() : "");
				//如果是兑换券
				setVisibility(true,true,true,true,false,false,false);
				setOtherCoupon(tvShopBouns,tvHuiquanBouns,orderDetail);//显示的金额
			} else if (Integer.parseInt(orderDetail.getCouponType()) == Util.NUM_EIGTH) {//代金券
				//批次
				tvBatchNb.setText(!Util.isEmpty(orderDetail.getBatchNbr()) ? orderDetail.getBatchNbr() : "");
				//兑换码
				tvCouponCode.setText(!Util.isEmpty(orderDetail.getCouponCode()) ? orderDetail.getCouponCode() : "");
				//内容
				tvPayPrice.setText(!Util.isEmpty(orderDetail.getFunction()) ? orderDetail.getFunction() : "");
				//如果是代金券
				setVisibility(true,true,true,true,false,false,false);
				setOtherCoupon(tvShopBouns,tvHuiquanBouns,orderDetail);
			} else if (Integer.parseInt(orderDetail.getCouponType()) == Util.NUM_ONE) {//N元购
				setVisibility(false,false,false,false,true,true,true);
				noDiscountCoupon (orderDetail);
			} else if (Integer.parseInt(orderDetail.getCouponType()) == Util.NUM_THIRD) {//抵扣券
				setVisibility(false,false,false,false,true,true,true);
				noDiscountCoupon (orderDetail);
			} else if (Integer.parseInt(orderDetail.getCouponType()) == Util.NUM_FOUR) { //折扣券
				setVisibility(false,false,false,false,true,true,true);
				noDiscountCoupon (orderDetail);
			} else if (Integer.parseInt(orderDetail.getCouponType()) == Util.NUM_FIVE) { //实物券
				setVisibility(false,false,false,false,true,true,true);
				noDiscountCoupon (orderDetail);
			} else if (Integer.parseInt(orderDetail.getCouponType()) == Util.NUM_SIX) { //体验券
				setVisibility(false,false,false,false,true,true,true);
				noDiscountCoupon (orderDetail);
			} else if (Integer.parseInt(orderDetail.getCouponType()) == Util.NUM_THIRTYTHREE) {//注册送的抵扣券
				setVisibility(false,false,false,false,true,true,true);
				noDiscountCoupon (orderDetail);
			} else if (Integer.parseInt(orderDetail.getCouponType()) == Util.NUM_THIRTYTHREE) {//送邀请人的抵扣券
				setVisibility(false,false,false,false,true,true,true);
				noDiscountCoupon (orderDetail);
			}
		} else {
			setVisibility(false,false,false,false,true,true,true);
			noDiscountCoupon (orderDetail);
		}
	}
	
	/**
	 * @param bathNb
	 * @param changeCode
	 * @param tvBatchNb
	 * @param shopBouns
	 * @param huiquanBouns
	 * @param discount
	 * @param part
	 * @param orderType
	 */
	private void setVisibility (boolean bathNb,boolean changeCode,boolean shopBouns,boolean huiquanBouns,boolean discount,boolean part,boolean orderType) {
		if (bathNb) {
			rlBatchNb.setVisibility(View.VISIBLE);
		} else {
			rlBatchNb.setVisibility(View.GONE);
		}
		
		if (changeCode) {
			rlExchangecode.setVisibility(View.VISIBLE);
		} else {
			rlExchangecode.setVisibility(View.GONE);
		}
		
		if (shopBouns) {
			rlShopBouns.setVisibility(View.VISIBLE);
		} else {
			rlShopBouns.setVisibility(View.GONE);
		}
		
		if (huiquanBouns) {
			rlHuiquanBouns.setVisibility(View.VISIBLE);
		} else {
			rlHuiquanBouns.setVisibility(View.GONE);
		}
		
		if (discount) {
			mRyDiscount.setVisibility(View.VISIBLE);
		} else {
			mRyDiscount.setVisibility(View.GONE);
		}
		
		if (part) {
			rlPart.setVisibility(View.VISIBLE);
		} else {
			rlPart.setVisibility(View.GONE);
		}
		
		if (orderType) {
			rlOrderPayType.setVisibility(View.VISIBLE);
		} else {
			rlOrderPayType.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 如果是兑换券或者是代金券
	 */
	private void setOtherCoupon(TextView tvShopBouns,TextView tvHuiquanBouns, OrderDetail orderDetail) {
		//商家红包抵扣
		tvShopBouns.setText(!Util.isEmpty(orderDetail.getShopBonusDeduction()) ? orderDetail.getShopBonusDeduction() + "元" : "0.00" + "元");
		//惠圈平台红包抵扣
		tvHuiquanBouns.setText(!Util.isEmpty(orderDetail.getPlatBonusDeduction()) ? orderDetail.getPlatBonusDeduction() + "元" : "0.00" + "元");
	}
	
	/**
	 * 不参与优惠金额的判断
	 */
	private void noDiscountCoupon (OrderDetail orderDetail) {
		if (!Util.isEmpty(orderDetail.getNoDiscountPrice())) {
			if (Double.parseDouble(orderDetail.getNoDiscountPrice()) == STATUS_NUM) {
				rlPart.setVisibility(View.GONE);
			} else {
				rlPart.setVisibility(View.VISIBLE);
				tvPart.setText(orderDetail.getNoDiscountPrice() + "元");
			}
			tvPart.setText(orderDetail.getNoDiscountPrice() + "元");
		} else {
			tvPart.setText("0.00" + "元");
		}
	}
	
	/**
	 * 优惠券的使用情况
	 * @param v
	 */
	public void setCoupon(OrderDetail  orderDetail){
		RelativeLayout ryCouponUnit = (RelativeLayout) findViewById(R.id.ry_couponUtil);//优惠券折扣一整行
		TextView tvCouponType = (TextView) findViewById(R.id.tv_coupon_type);//优惠券类型
		TextView tvInsteadPrice = (TextView) findViewById(R.id.tv_insteadprice);//优惠劵抵扣金额
		TextView tvCouponUnit = (TextView) findViewById(R.id.tv_coupon_unit);//优惠劵抵扣
		
		//判断是否使用了优惠劵
		if(!String.valueOf(Util.NUM_ZERO).equals(orderDetail.getCouponUsed())){//用了
			ryCouponUnit.setVisibility(View.VISIBLE);
			//1-N元购
			if(ShopConst.Coupon.N_BUY.equals(orderDetail.getCouponType())){
				tvCouponType.setText(getResources().getString(R.string.order_n_buy));
				if(!Util.isEmpty(orderDetail.getInsteadPrice())){
					tvInsteadPrice.setText("满" + orderDetail.getAvailablePrice() + "元可以使用");
				}
				//5-实物券
			} else if(ShopConst.Coupon.REAL_COUPON.equals(orderDetail.getCouponType())){
				tvCouponType.setText(getResources().getString(R.string.order_real_coupon));
				if(!Util.isEmpty(orderDetail.getFunction())){
					tvInsteadPrice.setText(orderDetail.getFunction());
				}
				//6-体验券
			} else if(ShopConst.Coupon.EXPERIENCE.equals(orderDetail.getCouponType())){
				tvCouponType.setText(getResources().getString(R.string.order_experience));
				if(!Util.isEmpty(orderDetail.getFunction())){
					tvInsteadPrice.setText(orderDetail.getFunction());
				}
				//3-抵扣券
			} else if(ShopConst.Coupon.DEDUCT.equals(orderDetail.getCouponType())){
				Log.d(TAG, "mOrderDetail.getCouponType()>>>>" + orderDetail.getCouponType());
					tvCouponType.setText(getResources().getString(R.string.order_coupon_deduct));
					if(!Util.isEmpty(orderDetail.getInsteadPrice())){
						if(!Util.isEmpty(orderDetail.getAvailablePrice())){
							tvInsteadPrice.setText("满" + orderDetail.getAvailablePrice() + "减" + orderDetail.getInsteadPrice() + ",共用" + orderDetail.getCouponUsed()+ "张");
						}
					}
					////4-折扣券
			} else if(ShopConst.Coupon.DISCOUNT.equals(orderDetail.getCouponType())){
					tvCouponType.setText(getResources().getString(R.string.order_coupon_discount));
					if(!Util.isEmpty(orderDetail.getAvailablePrice())){
						if(!Util.isEmpty(orderDetail.getDiscountPercent())){
							tvInsteadPrice.setText("满" + orderDetail.getAvailablePrice() + "打" + orderDetail.getDiscountPercent() + "折");
						}
					}
					////32-送给新用户的抵扣券
			} else if(ShopConst.Coupon.REG_DEDUCT.equals(orderDetail.getCouponType())){
				tvCouponType.setText(getResources().getString(R.string.order_coupon_deduct));
				if(!Util.isEmpty(orderDetail.getInsteadPrice())){
					if(!Util.isEmpty(orderDetail.getAvailablePrice())){
						tvInsteadPrice.setText("满" + orderDetail.getAvailablePrice() + "减" + orderDetail.getInsteadPrice() + ",共用" + orderDetail.getCouponUsed()+ "张");
					}
				}
			}
			tvCouponUnit.setText(!Util.isEmpty(orderDetail.getCouponDeduction()) ? orderDetail.getCouponDeduction() + "元" : "0.00"+ "元");
		} else{
			ryCouponUnit.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 返回
	 * @param v
	 */
	@OnClick(R.id.layout_turn_in)
	private void click(View v) {
		finish();
	}
	
    public void onResume(){
    	super.onResume();
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
   }
}
