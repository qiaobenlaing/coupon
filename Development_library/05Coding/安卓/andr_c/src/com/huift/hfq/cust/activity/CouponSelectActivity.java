package com.huift.hfq.cust.activity;



import java.util.List;

import net.minidev.json.JSONArray;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.CouponSelectBatch;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.Calculate;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.cust.R;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huift.hfq.cust.adapter.CouponSelectdeDiscountAdapter;
import com.huift.hfq.cust.adapter.CouponSelectdeDuctionAdapter;
import com.huift.hfq.cust.adapter.CouponShopDiscountAdapter;
import com.huift.hfq.cust.adapter.CouponShopDuctionAdapter;
import com.huift.hfq.cust.adapter.CouponSelectdeDuctionAdapter.ViewHolder;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.model.GrabCouponTask;
import com.huift.hfq.cust.model.ListUserCouponWhenPayTask;
import com.huift.hfq.cust.model.ListUserNoGrabCouponWhenPayTask;

/**
 *选择优惠券
 * @author yingchen
 *
 */
public class CouponSelectActivity extends Activity {
	private static final String TAG = CouponSelectActivity.class.getSimpleName();
	public  static final String SHOPE_CODE = "shopCode";
	public static final String COUPON_TYPE = "coupontype";
	public static final String CONSUME_AMOUNT="consumeamount";
	/**显示已经领取的优惠券列表*/
	private static final int SHOW_LIST = 1;
	/**不拥有优惠券   查询满足金额且商家发行的优惠券*/
	private static final int QUERY_COUPON = 2;
	/**显示商家发行的优惠券列表*/
	private static final int SHOW_SHOP_COUPON = 3;
	/**回退按钮*/
	private ImageView mBackImageView;
	
	/**标题*/
	private TextView mTitleTextView;
	
	/**优惠券列表*/
	private ListView mCouponListView;
	
	/**结算按钮*/
	private Button mGotoPayButton;
	
	/**已经领取的优惠券数据*/
	private List<CouponSelectBatch> mDataList;
	
	/**抵扣券适配器*/
	private CouponSelectdeDuctionAdapter mDuctionAdapter;
	
	/**折扣券适配器*/
	private CouponSelectdeDiscountAdapter mDiscountAdapter;
	
	/**点击列表的位置 默认为-1 未点击*/
	private int mClickPosition = -1; 
	
	/**商户编码*/
	private String mShopCode = "";
	
	/**优惠券类型 3--抵扣券  4--折扣券*/
	private String mCouponType = "";
	
	/**消费金额*/
	private String mConsumeAmount = "";

	/**使用抵扣券的数量*/
	private int mDuctionCount = 0; 
	
	/**抵扣券 满****/
	private int mDuctionAvaiblePrice = 0; 
	
	/**抵扣券  减****/
	private int mDductionInsteadPrice = 0; 
	
	/**折扣券的打折 9.5*/
	private double mDiscountPercent = 10; 
	
	/**优惠券批次编码  */
	private String mBatchCouponCode = ""; 
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SHOW_LIST: //显示已经拥有的优惠券列表
				showListView();
				break;
				
			case QUERY_COUPON:
				getShopPublishCoupon();//不拥有优惠券  查询满足消费金额的切商家发行的优惠券
				break;
				
			case SHOW_SHOP_COUPON:
				showShopPublishCoupon();//显示满足消费金额 且商家发行的优惠券
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_coupon_select);
		Intent intent = getIntent();
		mShopCode = intent.getStringExtra(SHOPE_CODE);
		mCouponType = intent.getStringExtra(COUPON_TYPE);
		mConsumeAmount = intent.getStringExtra(CONSUME_AMOUNT);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		
		initView();	
		initData();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}
	
	/**
	 * 显示满足消费金额 且商家发行的优惠券
	 */
	public void showShopPublishCoupon(){
		if(CustConst.Coupon.DEDUCT.equals(mCouponType)){ //显示商家发行的抵扣券
			mCouponListView.setAdapter( new CouponShopDuctionAdapter(this, mDataList));
		}else if(CustConst.Coupon.DISCOUNT.equals(mCouponType)){ //显示商家发行的折扣券
			mCouponListView.setAdapter(new CouponShopDiscountAdapter(this, mDataList));
		}
		
		//列表的监听。
		mCouponListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				final CouponSelectBatch couponSelectBatch = mDataList.get(position);
				new GrabCouponTask(CouponSelectActivity.this, new GrabCouponTask.Callback() {
					@Override
					public void getResult(int resultCode) {
						Log.d(TAG, "GrabCouponTask  retCode==="+resultCode);
						if(resultCode==ErrorCode.SUCC){
							//记录优惠券类型
							mCouponType = couponSelectBatch.getCouponType();
							//记录优惠券批次编码
							mBatchCouponCode = couponSelectBatch.getBatchCouponCode();
							//记录折扣券的满**
							mDuctionAvaiblePrice = couponSelectBatch.getAvailablePrice();
							if(CustConst.Coupon.DEDUCT.equals(mCouponType)){ //抵扣券的话  
								//记录抵扣券的减**
								mDductionInsteadPrice = couponSelectBatch.getInsteadPrice();
								// 默认使用一张抵扣券
								mDuctionCount = 1;
							}else if(CustConst.Coupon.DISCOUNT.equals(mCouponType)){
								//折扣券的打折数  类似9.5
								mDiscountPercent = couponSelectBatch.getDiscountPercent();
							}
							backToShopPayBill(true);
						}else{
							Util.getContentValidate(R.string.toast_drawlimit);
						}
					}
				}).execute(couponSelectBatch.getBatchCouponCode(),String.valueOf(Util.NUM_TWO));
			}
		});
	}
	
	/***
	 * 查询满足消费金额的切商家发行的优惠券
	 */
	public void getShopPublishCoupon(){
		Log.d(TAG, "getShopPublishCoupon===");
		new ListUserNoGrabCouponWhenPayTask(this, new ListUserNoGrabCouponWhenPayTask.CallBack() {
			@Override
			public void getResult(JSONArray result) {
				try {
					mDataList = new Gson().fromJson(result.toString(),new TypeToken<List<CouponSelectBatch>>(){}.getType());
					Log.d(TAG, "转化正常");
					mHandler.sendEmptyMessage(SHOW_SHOP_COUPON);
				} catch (Exception e) {
					Log.e(TAG, "转化错误");
				}
			}
		}).execute(mShopCode,mConsumeAmount,mCouponType);
	}
	
	
	/**
	 * 显示已经拥有的优惠券列表
	 */
	private void showListView() {
		if(CustConst.Coupon.DEDUCT.equals(mCouponType)){  //显示拥有的抵扣券
			mDuctionAdapter = new CouponSelectdeDuctionAdapter(this, mDataList);
			mCouponListView.setAdapter(mDuctionAdapter);
			listenOnCouponDuctionListview();
		}else if(CustConst.Coupon.DISCOUNT.equals(mCouponType)){  //显示拥有的折扣券
			mDiscountAdapter = new CouponSelectdeDiscountAdapter(this, mDataList);
			mCouponListView.setAdapter(mDiscountAdapter);
			listenOnCouponDiscountListview();
		}
	}

	/**
	 * 折扣券列表的监听
	 */
	private void listenOnCouponDiscountListview() {
		mCouponListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				//设置结算按钮可以点击
				setButtonEnable(true);
				
				CouponSelectBatch couponSelectBatch = mDataList.get(position);
				for(int i=0;i<mDataList.size();i++){
					if(i==position){
						mDataList.get(i).setCheck(true);
					}else{
						mDataList.get(i).setCheck(false);
					}
				}
				
				mDiscountAdapter.notifyDataSetChanged();
				//折扣券的打折数  类似9.5
				mDiscountPercent = couponSelectBatch.getDiscountPercent();
				//记录折扣券的满**
				mDuctionAvaiblePrice = couponSelectBatch.getAvailablePrice();
				//记录优惠券批次编码
				mBatchCouponCode = couponSelectBatch.getBatchCouponCode();
			}
		});
	}

	/**
	 * 抵扣券列表的监听
	 */
	private void listenOnCouponDuctionListview() {
		mCouponListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				//设置结算按钮可以点击
				setButtonEnable(true);
				
				final CouponSelectBatch couponSelectBatch = mDataList.get(position);
				final CouponSelectdeDuctionAdapter.ViewHolder holder = (ViewHolder) view.getTag();
				if(mClickPosition!=position){
					
					//显示最大可以使用的优惠券数量
					int maxCanUseCount = getMaxCanUseCount(couponSelectBatch.getUserCount(),couponSelectBatch.getLimitedNbr()
							,Double.parseDouble(mConsumeAmount),couponSelectBatch.getAvailablePrice());
					holder.use.setText(String.valueOf(maxCanUseCount));
					mClickPosition = position;
				}
				for(int i=0;i<mDataList.size();i++){
					if(i==position){
						mDataList.get(i).setShowCouponCount(false);
					}else{
						mDataList.get(i).setShowCouponCount(true);
					}
				}	
				mDuctionAdapter.notifyDataSetChanged();
				
				mDuctionCount = Integer.parseInt(holder.use.getText().toString());
				
				holder.add.setOnClickListener(new OnClickListener() { //+的按钮
					
					@Override
					public void onClick(View v) {
						int count = Integer.parseInt(holder.use.getText().toString())+1;
						
						//检查使用抵扣券的张数
						int avaibleUseCount = checkUseCount(count,couponSelectBatch.getUserCount(),couponSelectBatch.getLimitedNbr()
								,Double.parseDouble(mConsumeAmount),couponSelectBatch.getAvailablePrice());
						holder.use.setText(String.valueOf(avaibleUseCount));
						//记录抵扣券的使用数量
						mDuctionCount = Integer.parseInt(holder.use.getText().toString());
					}
				});
				
				holder.minus.setOnClickListener(new OnClickListener() { //-的按钮
					
					@Override
					public void onClick(View v) {
						int count = Integer.parseInt(holder.use.getText().toString())-1;
						
						int avaibleUseCount = checkUseCount(count,couponSelectBatch.getUserCount(),couponSelectBatch.getLimitedNbr()
								,Double.parseDouble(mConsumeAmount),couponSelectBatch.getAvailablePrice());
						
						holder.use.setText(String.valueOf(avaibleUseCount));
						//记录抵扣券的使用数量
						mDuctionCount = Integer.parseInt(holder.use.getText().toString());
					}
				});
				
				
				//记录抵扣券的满**
				mDuctionAvaiblePrice = couponSelectBatch.getAvailablePrice();
				//记录抵扣券的减**
				mDductionInsteadPrice = couponSelectBatch.getInsteadPrice();
				//记录优惠券批次编码
				mBatchCouponCode = couponSelectBatch.getBatchCouponCode();
			}
		});
	}
	
	/**
	 * 确定可以使用优惠券的最大数量
	 * @param userCount ---用户拥有该批次的优惠券数量
	 * @param limitCount---商家的优惠券使用的最大数量限制
	 * @param cusumeAmount---消费金额
	 * @param availablePrice---满**
	 * @return
	 */
	protected int getMaxCanUseCount(int userCount, int limitCount, double cusumeAmount, int availablePrice) {
		int count = (int) (cusumeAmount/availablePrice);
		
		if(count>userCount){
			count = userCount;
		}
		
		if(count>limitCount){
			count = limitCount;
		}
		
		return count;
	}

	/**
	 * 检查使用抵扣券的张数
	 * 大于0
	 * 小于领取的数量  userCount
	 * 小于限制的数量 limitCount
	 * 小于 输入金额/满xx 的数量    cusumeAmount/availablePrice
	 */
	public int checkUseCount(int count,int userCount,int limitCount,double cusumeAmount,int availablePrice){
		//不能小于0
		if(count<0){
			count=0;
			showMentionedDialog(Util.getString(R.string.not_use_coupon));
		}
		
		//不能大于领取的数量
		if(count>userCount){
			count = userCount;
			showMentionedDialog(Util.getString(R.string.no_coupon_left));
		}
		
		//不能大于限制的数量  limitCount=0 没有限制
		if(limitCount>0&&count>limitCount){
			count = limitCount;
			showMentionedDialog(Util.getString(R.string.coupon_top_limit));
		}
	
		//不能大于 cusumeAmount/availablePrice数量
		if(count>(cusumeAmount/availablePrice)){
			count =(int) (cusumeAmount/availablePrice);
			showMentionedDialog(getResources().getString(R.string.add_money_pre)+
					(mDuctionAvaiblePrice-cusumeAmount%mDuctionAvaiblePrice)+getResources().getString(R.string.add_money_last));
		}
			
		return count;
	}
	
	/**
	 * 优惠券使用的异常提示框
	 * @param text
	 */
	public void showMentionedDialog(String text){
		DialogUtils.showDialogSingle(this, text, R.string.cue, R.string.ok,null);
	}
	/**
	 * 初始化数据
	 */
	private void initData() {
		new ListUserCouponWhenPayTask(this, new ListUserCouponWhenPayTask.CallBack() {
			
			@Override
			public void getResult(JSONArray result) {
				if(null!=result&&!"[]".equals(result.toString())){
					Log.d(TAG, "已经拥有的优惠券");
					mDataList = new Gson().fromJson(result.toString(),new TypeToken<List<CouponSelectBatch>>(){}.getType());
					//显示已经领取的优惠券
					mHandler.sendEmptyMessage(SHOW_LIST);
				}else{
					//不拥有优惠券   查询满足消费金额的商家发行的优惠券数据
					Log.d(TAG, "查询商家发行的优惠券=====");
					mHandler.sendEmptyMessage(QUERY_COUPON);
				}
			}
		}).execute(mShopCode,mConsumeAmount,mCouponType);
	}

	/**
	 * 初始化控件
	 */
	public  void initView(){
		mBackImageView = (ImageView) findViewById(R.id.iv_turn_in);
		mTitleTextView = (TextView) findViewById(R.id.tv_mid_content);
		mTitleTextView.setText(this.getResources().getString(R.string.order_check));
		mCouponListView = (ListView) findViewById(R.id.lv_coupon_select);
		mGotoPayButton = (Button) findViewById(R.id.btn_go_pay);
		
		
		mBackImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				backToShopPayBill(false);//返回按钮---->不使用优惠券
			}
		});
		
		mGotoPayButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				backToShopPayBill(true); //去结算----->使用优惠券
			}
		});
	}
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mHandler!=null){
			mHandler.removeCallbacksAndMessages(null); //移除消息
		}
	}
	
	public void setButtonEnable(boolean flag){
		if(flag){
			mGotoPayButton.setBackgroundResource(R.drawable.login_btn);
			mGotoPayButton.setEnabled(true);
		}else{
			mGotoPayButton.setBackgroundResource(R.drawable.shape_paybill);
			mGotoPayButton.setEnabled(false);
		}
	}
	
	/**
	 * 回退到买单界面
	 * @param useCoupon---->是否使用优惠券    点击返回按钮--->未使用   点击去结算---->使用
	 */
	public void backToShopPayBill(boolean useCoupon){
		Intent data = getIntent();
		if(useCoupon){ //使用了优惠券
			data.putExtra("COUPON_TYPE", mCouponType);
			data.putExtra("BATCHCOUPONCODE",mBatchCouponCode); //string 优惠券批次编码
			/*//若mBatchCouponCode为空(即未选择优惠券) 则不可以结算
			if("".equals(mBatchCouponCode)){
				// 请选择优惠券
				Util.getContentValidate(R.string.sel_coupon);
				return;
			}*/
			
			if(CustConst.Coupon.DEDUCT.equals(mCouponType)){  //抵扣券
				data.putExtra("COUNT", mDuctionCount + ""); //String 抵扣券数量
				data.putExtra("FAVOURABLE_DUCTION", mDuctionCount*mDductionInsteadPrice + ""); //String  抵扣券优惠的金额  = 数量*减
				data.putExtra("DISCRIBLE_DUCTION", "满"+mDuctionAvaiblePrice+"减"+mDductionInsteadPrice);//string 抵扣券的描述
			}else if(CustConst.Coupon.DISCOUNT.equals(mCouponType)){ //折扣券

				Log.d("TAG", " 计算金额  >>> " +Calculate.ceilBigDecimal(Calculate.mul(Double.parseDouble(mConsumeAmount), 
					      Calculate.suBtraction(1, Calculate.div(mDiscountPercent, 10)))));
				data.putExtra("FAVOURABLE_DUCTION",Calculate.ceilBigDecimal(Calculate.mul(Double.parseDouble(mConsumeAmount), 
						      Calculate.suBtraction(1, Calculate.div(mDiscountPercent, 10)))) + ""); // String 折扣券优惠的金额  = 总金额*(1-折扣数/10)

				data.putExtra("DISCRIBLE_DUCTION", "满"+mDuctionAvaiblePrice+"打"+mDiscountPercent+"折");//string 折扣券的描述
			}
			
		}else{
			data.putExtra("COUPON_TYPE", ""); //未使用优惠券
		}
		setResult(RESULT_OK, data);
		finish();
	}
}
