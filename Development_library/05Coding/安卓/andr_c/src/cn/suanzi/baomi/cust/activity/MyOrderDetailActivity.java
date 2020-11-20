 package cn.suanzi.baomi.cust.activity;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.OrderDetail;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.DialogUtils;
import cn.suanzi.baomi.base.utils.QrCodeUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.fragment.ICBCOnlinePayFragment;
import cn.suanzi.baomi.cust.model.CancelBankcardPayTask;
import cn.suanzi.baomi.cust.model.MyOrderDetailListTask;

import com.google.zxing.WriterException;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
 
/**
 * 订单详情页面
 * @author qian.zhou
 */
public class MyOrderDetailActivity extends Activity implements TextWatcher{
	private static final String TAG = "MyOrderDetailActivity";
	public static final String CONSUME_CODE = "consumeCode";
	/** 标记是完成的订单页面还是未完成的订单页面进来的 */
	public static final String ORDER_STATUS = "order_sratus";
	/** 是否是支付完成的订单 */
	private boolean mOrderSuccess ; 
	private String mConsumeCode;
	/**在线支付按钮*/
	private Button mPayOnlineButton;
	/**取消支付按钮*/
	private Button mPayCancleButton;
	/** 正在加载数据*/
	private LinearLayout mLyNodate;
	/** 正在加载的内容*/
	private LinearLayout mLyContent;
	/** 是否收缩*/
	private ImageView mIvArrow;
	/** 记录点击次数*/
	private int mClickNum; 
	/** 订单号*/
	private TextView mTvOrderNbr;
	private OrderDetail mOrderDetail;
	/** 折扣*/
	private LinearLayout mLyAllDiscount;
	/** 下拉加载 */
	private SwipeRefreshLayout mSwipeRefreshLayout;
	/** 下拉请求api */
	private boolean mDataUpFlag = false;
	private Handler mHandler;
	
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
		//标题
		TextView tvContent = (TextView) findViewById(R.id.tv_mid_content);
		tvContent.setText(getString(R.string.order_detail));
		mTvOrderNbr = (TextView) findViewById(R.id.tv_ordernbr);//订单号
		mLyNodate = (LinearLayout) findViewById(R.id.ly_nodate);
		mLyContent = (LinearLayout) findViewById(R.id.ry_content);
		//导向箭头
		mIvArrow = (ImageView) findViewById(R.id.iv_arrow_down);
		RelativeLayout ryDiscount = (RelativeLayout) findViewById(R.id.ry_arrow_discount);//优惠金额一整行
		mLyAllDiscount = (LinearLayout) findViewById(R.id.ly_all_discount);//所有折扣
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.index_swipe_refresh);
		mIvArrow.setImageResource(mClickNum % 2 == 0 ? R.drawable.up_arrow : R.drawable.down_arrow);
		ryDiscount.setOnClickListener(disCountListener);
		mHandler = new Handler();
		mSwipeRefreshLayout.setColorSchemeResources(R.color.red);
		mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
		initData();//初始化数据
		setData(0); // 正在加载数据
		//根据不同类型的订单显示或者隐藏按钮
		showOrHideButton();
		//订单详情方法
		getOrderDeatil();
	}
	
	/**
	 * 各种抵扣
	 */
	OnClickListener disCountListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mClickNum++;
			if (mClickNum % 2 == 0) {
				mIvArrow.setImageResource(R.drawable.up_arrow);
				mLyAllDiscount.setVisibility(View.VISIBLE);
			} else {
				mIvArrow.setImageResource(R.drawable.down_arrow);
				mLyAllDiscount.setVisibility(View.GONE);
			}
		}
	};
	
	/**
	 * 下拉加载
	 */
	private OnRefreshListener refreshListener = new OnRefreshListener() {
		@Override
		public void onRefresh() {
			if (mDataUpFlag) {
				mDataUpFlag = false;
				mHandler.postDelayed(new Runnable() {
					public void run() {
						getOrderDeatil();
					}
				}, 5000);
			}
		}
	};
	
	/**
	 * 初始数据
	 */
	public void initData(){
		mClickNum = 0;
		Intent intent = this.getIntent();
		mConsumeCode = intent.getStringExtra(CONSUME_CODE);
		mOrderSuccess = intent.getBooleanExtra(ORDER_STATUS, true);
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
	 * 根据不同类型的订单显示或者隐藏按钮     成功---隐藏    失败---显示
	 */
	private void showOrHideButton() {
		mPayOnlineButton = (Button) findViewById(R.id.online_pay);
		mPayCancleButton = (Button) findViewById(R.id.cancel_pay);
		if(mOrderSuccess){ //支付成功成功订单详情
			mPayOnlineButton.setVisibility(View.GONE);
			mPayCancleButton.setVisibility(View.GONE);
			mPayOnlineButton.setEnabled(false);
			mPayCancleButton.setEnabled(false);
		}else{//支付失败订单详情
			mPayOnlineButton.setVisibility(View.VISIBLE);
			mPayCancleButton.setVisibility(View.VISIBLE);
			mPayOnlineButton.setEnabled(true);
			mPayCancleButton.setEnabled(true);
		}
	}

	public void getOrderDeatil(){
		new MyOrderDetailListTask(MyOrderDetailActivity.this, new MyOrderDetailListTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mDataUpFlag = true;
				mSwipeRefreshLayout.setRefreshing(false); // 上拉加载完成
				if(result == null || "".equals(result)){
					return;
				} else{
					setData(1); // 有数据
					//初始化数据
					final TextView tvShopName = (TextView) findViewById(R.id.shop_name);//商店名称
					TextView tvOrderTime = (TextView) findViewById(R.id.tv_order_time);//时间
					TextView tvPrice = (TextView) findViewById(R.id.price_unit);//消费金额
					TextView tvCouponUnit = (TextView) findViewById(R.id.tv_coupon_unit);//优惠劵抵扣
					TextView tvCardUnit = (TextView) findViewById(R.id.tv_cardprice_unit);//会员卡抵扣
					TextView tvShopBoundsUnit = (TextView) findViewById(R.id.tv_shop_bouns);//商家红包抵扣
					TextView tvHuiquanBoundsUnit = (TextView) findViewById(R.id.tv_huiquan_bounds);//惠圈平台红包抵扣金额
					TextView tvBankCardUnit = (TextView) findViewById(R.id.tv_bankcardcard_unit);//银行卡抵扣
					TextView tvTotalUnit = (TextView) findViewById(R.id.tv_totalprice_unit);//优惠金额
					TextView tvNewPriceUnit = (TextView) findViewById(R.id.tv_newprice_unit);//实际支付
					TextView tvCouponType = (TextView) findViewById(R.id.tv_coupon_type);//优惠券类型
					RelativeLayout ryCouponUnit = (RelativeLayout) findViewById(R.id.ry_couponUtil);//优惠券折扣一整行
					TextView tvInsteadPrice = (TextView) findViewById(R.id.tv_insteadprice);//优惠劵抵扣金额
					TextView tvOrderStatus = (TextView) findViewById(R.id.tv_order_status);//订单状态
					ImageView ivBarCode = (ImageView) findViewById(R.id.iv_barcode);//条形码
					RelativeLayout ryIsFirst = (RelativeLayout) findViewById(R.id.ry_isfirst);//首单立减
					TextView tvFirstMoney = (TextView) findViewById(R.id.tv_first_unit);//首单立减金额
					
					mOrderDetail = Util.json2Obj(result.toString(), OrderDetail.class);
					//赋值
					tvShopName.setText(mOrderDetail.getShopName());
					mTvOrderNbr.setText("订单号：" + mOrderDetail.getOrderNbr());
					tvOrderTime.setText(mOrderDetail.getConsumeTime());
					//给各种折扣赋值
					setDiscountMoney(tvPrice, tvNewPriceUnit, tvFirstMoney, ryIsFirst, tvBankCardUnit,
							tvCardUnit, tvShopBoundsUnit, tvHuiquanBoundsUnit, tvTotalUnit);
					//生成条形码
					try {
						ivBarCode.setImageBitmap(QrCodeUtils.CreateOneDCode(mOrderDetail.getOrderNbr()));
					} catch (WriterException e) {
						Log.d(TAG, "生成条形码 >>> " + e.getMessage());
					}
					//判断订单的状态是否完成
					tvOrderStatus.setText(mOrderSuccess ? getString(R.string.order_success) : getString(R.string.order_fail));
					//优惠券的使用情况
					useCouponDetail(ryCouponUnit, tvCouponType, tvInsteadPrice, tvCouponUnit);
				}
			}
		}).execute(mConsumeCode);
	}
	
	/**
	 * 各种抵扣和消费金额
	 * @param v
	 */
	public void setDiscountMoney(TextView tvPrice, TextView tvNewPriceUnit, TextView tvFirstMoney, RelativeLayout ryIsFirst,
			TextView tvBankCardUnit, TextView tvCardUnit, TextView tvShopBoundsUnit, TextView tvHuiquanBoundsUnit, TextView tvTotalUnit){
		//消费金额
		tvPrice.setText(!Util.isEmpty(mOrderDetail.getOrderAmount()) ? mOrderDetail.getOrderAmount() + "元" : "0.00" + "元");
		//总抵扣
		tvNewPriceUnit.setText(!Util.isEmpty(mOrderDetail.getRealPay()) ? mOrderDetail.getRealPay() + "元" : "0.00" + "元");
		//首单立减
		if("0.00".equals(mOrderDetail.getFirstDeduction())){
			ryIsFirst.setVisibility(View.GONE);
		} else{
			ryIsFirst.setVisibility(View.VISIBLE);
			tvFirstMoney.setText(mOrderDetail.getFirstDeduction() + "元");
		}
		//银行卡抵扣
		tvBankCardUnit.setText(!Util.isEmpty(mOrderDetail.getBankCardDeduction()) ? mOrderDetail.getBankCardDeduction() + "元" : "0.00" + "元");
		//会员卡抵扣
		tvCardUnit.setText(!Util.isEmpty(mOrderDetail.getCardDeduction()) ? mOrderDetail.getCardDeduction() + "元" : "0.00" + "元");
		//商家红包抵扣
		tvShopBoundsUnit.setText(!Util.isEmpty(mOrderDetail.getShopBonusDeduction()) ? mOrderDetail.getShopBonusDeduction() + "元" : "0.00" + "元");
		//惠圈平台红包抵扣
		tvHuiquanBoundsUnit.setText(!Util.isEmpty(mOrderDetail.getPlatBonusDeduction()) ? mOrderDetail.getPlatBonusDeduction() + "元" : "0.00" + "元");
		//优惠金额
		tvTotalUnit.setText(!Util.isEmpty(mOrderDetail.getDeduction()) ? mOrderDetail.getDeduction() + "元" : "0.00" + "元");
	}
	
	/**
	 * 优惠券的使用情况
	 * @param v
	 */
	public void useCouponDetail(RelativeLayout ryCouponUnit, TextView tvCouponType, TextView tvInsteadPrice, TextView tvCouponUnit){
		//判断是否使用了优惠劵
		if(!String.valueOf(Util.NUM_ZERO).equals(mOrderDetail.getCouponUsed())){//用了
			ryCouponUnit.setVisibility(View.VISIBLE);
			//1-N元购
			if(CustConst.Coupon.N_BUY.equals(mOrderDetail.getCouponType())){
				tvCouponType.setText(getResources().getString(R.string.order_n_buy));
				if(!Util.isEmpty(mOrderDetail.getInsteadPrice())){
					tvInsteadPrice.setText("满" + mOrderDetail.getAvailablePrice() + "元可以使用");
				}
				//5-实物券
			} else if(CustConst.Coupon.REAL_COUPON.equals(mOrderDetail.getCouponType())){
				tvCouponType.setText(getResources().getString(R.string.order_real_coupon));
				if(!Util.isEmpty(mOrderDetail.getFunction())){
					tvInsteadPrice.setText(mOrderDetail.getFunction());
				}
				//6-体验券
			} else if(CustConst.Coupon.EXPERIENCE.equals(mOrderDetail.getCouponType())){
				tvCouponType.setText(getResources().getString(R.string.order_experience));
				if(!Util.isEmpty(mOrderDetail.getFunction())){
					tvInsteadPrice.setText(mOrderDetail.getFunction());
				}
				//3-抵扣券
			} else if(CustConst.Coupon.DEDUCT.equals(mOrderDetail.getCouponType())){
				Log.d(TAG, "mOrderDetail.getCouponType()>>>>" + mOrderDetail.getCouponType());
					tvCouponType.setText(getResources().getString(R.string.order_coupon_deduct));
					if(!Util.isEmpty(mOrderDetail.getInsteadPrice())){
						if(!Util.isEmpty(mOrderDetail.getAvailablePrice())){
							tvInsteadPrice.setText("满" + mOrderDetail.getAvailablePrice() + "减" + mOrderDetail.getInsteadPrice() + ",共用" + mOrderDetail.getCouponUsed()+ "张");
						}
					}
					////4-折扣券
			} else if(CustConst.Coupon.DISCOUNT.equals(mOrderDetail.getCouponType())){
					tvCouponType.setText(getResources().getString(R.string.order_coupon_discount));
					if(!Util.isEmpty(mOrderDetail.getAvailablePrice())){
						if(!Util.isEmpty(mOrderDetail.getDiscountPercent())){
							tvInsteadPrice.setText("满" + mOrderDetail.getAvailablePrice() + "打" + mOrderDetail.getDiscountPercent() + "折");
						}
					}
					////32-送给新用户的抵扣券
			} else if(CustConst.Coupon.REG_DEDUCT.equals(mOrderDetail.getCouponType())){
				tvCouponType.setText(getResources().getString(R.string.order_coupon_deduct));
				if(!Util.isEmpty(mOrderDetail.getInsteadPrice())){
					if(!Util.isEmpty(mOrderDetail.getAvailablePrice())){
						tvInsteadPrice.setText("满" + mOrderDetail.getAvailablePrice() + "减" + mOrderDetail.getInsteadPrice() + ",共用" + mOrderDetail.getCouponUsed()+ "张");
					}
				}
			}
			tvCouponUnit.setText(!Util.isEmpty(mOrderDetail.getCouponDeduction()) ? mOrderDetail.getCouponDeduction() + "元" : "0.00"+ "元");
		} else{
			ryCouponUnit.setVisibility(View.GONE);
		}
	}
	
	@OnClick({ R.id.iv_turn_in ,R.id.online_pay,R.id.cancel_pay})
	private void click(View v) {
		switch (v.getId()) {
		case R.id.iv_turn_in:
			finish();
			break;
		case R.id.online_pay://在线支付  跳转到工行在线支付页面
			Intent intent = new Intent(this, ICBCOnlinePayActivity.class);
			intent.putExtra(ICBCOnlinePayFragment.REAL_CONSUMECODE, mConsumeCode); //
			intent.putExtra(ICBCOnlinePayFragment.ORDERNBR,mOrderDetail.getOrderNbr());
			intent.putExtra(ICBCOnlinePayFragment.SHOP_NAME, mOrderDetail.getShopName());
			intent.putExtra(ICBCOnlinePayFragment.SHOP_CODE, mOrderDetail.getShopCode());
			intent.putExtra(ICBCOnlinePayFragment.REAL_PAY, mOrderDetail.getRealPay());
			startActivity(intent);
			finish();
			break;
		case R.id.cancel_pay:
			DialogUtils.showDialog(this,Util.getString(R.string.dialog_pay),Util.getString(R.string.dialog_pay_content), Util.getString(R.string.dialog_ok),
					Util.getString(R.string.dialog_cancel), new DialogUtils().new OnResultListener() {
						@Override
						public void onOK() {
							//确认取消支付
							canclePay();
						}
			});
			break;
		}
	}
	
	/**
	 * 确认取消支付
	 */
	public  void  canclePay(){
		//Toast.makeText(this, "确认取消支付", Toast.LENGTH_SHORT).show();
		new CancelBankcardPayTask(this, new CancelBankcardPayTask.Callback() {
			@Override
			public void getResult(boolean result) {
				if(result){//取消支付成功  
					MyOrderDetailActivity.this.finish();
					DB.saveBoolean(CustConst.Key.CANCEL_ORDER_ISSUCCESS, true);
				}else{
					//TODO
				}
			}
		}).execute(mConsumeCode);
	}
	
	 public void onResume(){
	    	super.onResume();
	        AppUtils.setActivity(this);
	        AppUtils.setContext(getApplicationContext());
	   }

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}
}
