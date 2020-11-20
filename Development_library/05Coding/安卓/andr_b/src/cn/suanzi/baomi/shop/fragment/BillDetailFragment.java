package cn.suanzi.baomi.shop.fragment;

import net.minidev.json.JSONObject;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.OrderDetail;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.QrCodeUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopConst;
import cn.suanzi.baomi.shop.model.MyOrderDetailListTask;
import com.google.zxing.WriterException;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 订单详情
 * @author wensi.yu
 *
 */
public class BillDetailFragment extends Fragment{
	
	private static final String TAG = "BillDetailFragment";
	/** 传过来的订单编号*/
	public static final String ORDER_NBR = "orderNbr";
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
	/** 视图*/
	private View view;
	/** 订单编码*/
	private String mConsumeCode;

	public static BillDetailFragment newInstance() {
		Bundle args = new Bundle();
		BillDetailFragment fragment = new BillDetailFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_bill_detail,container, false);
		ViewUtils.inject(this, view);
		init();
		return view;
	}
	
	private Activity getMyActivity(){
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;       
	}
	
	private void init() {
		mConsumeCode = getMyActivity().getIntent().getStringExtra(ORDER_NBR);
		Log.d(TAG, "传过来的订单编码=="+mConsumeCode);
		TextView tvContent = (TextView) view.findViewById(R.id.tv_mid_content);
		tvContent.setText(getString(R.string.tv_bill_title));
		mTvOrderNbr = (TextView) view.findViewById(R.id.tv_ordernbr);//订单号
		mLyNodate = (LinearLayout) view.findViewById(R.id.ly_nodate);
		mLyContent = (LinearLayout) view.findViewById(R.id.ry_content);
		//导向箭头
		mIvArrow = (ImageView) view.findViewById(R.id.iv_arrow_down);
		RelativeLayout ryDiscount = (RelativeLayout) view.findViewById(R.id.ry_arrow_discount);//优惠金额一整行
		mLyAllDiscount = (LinearLayout) view.findViewById(R.id.ly_all_discount);//所有折扣
		mIvArrow.setImageResource(mClickNum % 2 == 0 ? R.drawable.upc_arrow : R.drawable.downc_arrow);
		ryDiscount.setOnClickListener(disCountListener);
		mClickNum = 0;
		//正在加载数据
		setData(0); 
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
				mIvArrow.setImageResource(R.drawable.upc_arrow);
				mLyAllDiscount.setVisibility(View.VISIBLE);
			} else {
				mIvArrow.setImageResource(R.drawable.downc_arrow);
				mLyAllDiscount.setVisibility(View.GONE);
			}
		}
	};
	
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
	 * 详情
	 */
	public void getOrderDeatil(){
		final TextView tvShopName = (TextView) view.findViewById(R.id.shop_name);//商店名称
		final TextView tvOrderTime = (TextView) view.findViewById(R.id.tv_order_time);//时间
		final TextView tvPrice = (TextView) view.findViewById(R.id.price_unit);//消费金额
		final TextView tvCouponUnit = (TextView) view.findViewById(R.id.tv_coupon_unit);//优惠劵抵扣
		final TextView tvCardUnit = (TextView) view.findViewById(R.id.tv_cardprice_unit);//会员卡抵扣
		final TextView tvShopBoundsUnit = (TextView) view.findViewById(R.id.tv_shop_bouns);//商家红包抵扣
		final TextView tvHuiquanBoundsUnit = (TextView) view.findViewById(R.id.tv_huiquan_bounds);//惠圈平台红包抵扣金额
		final TextView tvBankCardUnit = (TextView) view.findViewById(R.id.tv_bankcardcard_unit);//银行卡抵扣
		final TextView tvTotalUnit = (TextView) view.findViewById(R.id.tv_totalprice_unit);//优惠金额
		final TextView tvNewPriceUnit = (TextView) view.findViewById(R.id.tv_newprice_unit);//实际支付
		final TextView tvCouponType = (TextView) view.findViewById(R.id.tv_coupon_type);//优惠券类型
		final RelativeLayout ryCouponUnit = (RelativeLayout) view.findViewById(R.id.ry_couponUtil);//优惠券折扣一整行
		final TextView tvInsteadPrice = (TextView) view.findViewById(R.id.tv_insteadprice);//优惠劵抵扣金额
		final ImageView ivBarCode = (ImageView) view.findViewById(R.id.iv_barcode);//条形码
		final RelativeLayout ryIsFirst = (RelativeLayout) view.findViewById(R.id.ry_isfirst);//首单立减
		final TextView tvFirstMoney = (TextView) view.findViewById(R.id.tv_first_unit);//首单立减金额
		
		new MyOrderDetailListTask(getMyActivity(), new MyOrderDetailListTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if(result == null || "".equals(result)){
					return;
				} else{
					setData(1); // 有数据
					
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
			if(ShopConst.Coupon.N_BUY.equals(mOrderDetail.getCouponType())){
				tvCouponType.setText(getResources().getString(R.string.order_n_buy));
				if(!Util.isEmpty(mOrderDetail.getInsteadPrice())){
					tvInsteadPrice.setText("满" + mOrderDetail.getAvailablePrice() + "元可以使用");
				}
				//5-实物券
			} else if(ShopConst.Coupon.REAL_COUPON.equals(mOrderDetail.getCouponType())){
				tvCouponType.setText(getResources().getString(R.string.order_real_coupon));
				if(!Util.isEmpty(mOrderDetail.getFunction())){
					tvInsteadPrice.setText(mOrderDetail.getFunction());
				}
				//6-体验券
			} else if(ShopConst.Coupon.EXPERIENCE.equals(mOrderDetail.getCouponType())){
				tvCouponType.setText(getResources().getString(R.string.order_experience));
				if(!Util.isEmpty(mOrderDetail.getFunction())){
					tvInsteadPrice.setText(mOrderDetail.getFunction());
				}
				//3-抵扣券
			} else if(ShopConst.Coupon.DEDUCT.equals(mOrderDetail.getCouponType())){
				Log.d(TAG, "mOrderDetail.getCouponType()>>>>" + mOrderDetail.getCouponType());
					tvCouponType.setText(getResources().getString(R.string.order_coupon_deduct));
					if(!Util.isEmpty(mOrderDetail.getInsteadPrice())){
						if(!Util.isEmpty(mOrderDetail.getAvailablePrice())){
							tvInsteadPrice.setText("满" + mOrderDetail.getAvailablePrice() + "减" + mOrderDetail.getInsteadPrice() + ",共用" + mOrderDetail.getCouponUsed()+ "张");
						}
					}
					////4-折扣券
			} else if(ShopConst.Coupon.DISCOUNT.equals(mOrderDetail.getCouponType())){
					tvCouponType.setText(getResources().getString(R.string.order_coupon_discount));
					if(!Util.isEmpty(mOrderDetail.getAvailablePrice())){
						if(!Util.isEmpty(mOrderDetail.getDiscountPercent())){
							tvInsteadPrice.setText("满" + mOrderDetail.getAvailablePrice() + "打" + mOrderDetail.getDiscountPercent() + "折");
						}
					}
					////32-送给新用户的抵扣券
			} else if(ShopConst.Coupon.REG_DEDUCT.equals(mOrderDetail.getCouponType())){
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
	
	/**
	 * 返回
	 */
	@OnClick(R.id.layout_turn_in)
	public void trunBack(View view){
		getMyActivity().finish();
	}
	
	public void onResume(){
	    super.onResume();
	}
	
}
