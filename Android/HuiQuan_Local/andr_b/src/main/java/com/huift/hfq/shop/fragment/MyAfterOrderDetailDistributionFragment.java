package com.huift.hfq.shop.fragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.OrderInfo;
import com.huift.hfq.base.pojo.ProductList;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.shop.ShopApplication;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.ShopConst.Order;
import com.huift.hfq.shop.activity.MyAfterOrderDetailSettleActivity;
import com.huift.hfq.shop.adapter.ProductListAdapter;
import com.huift.hfq.shop.model.GetProductOrderInfoTask;
import com.huift.hfq.shop.model.UpdateProductOrderStatusTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 订单详情餐后支付--待接单和配送中
 * @author wensi.yu
 *
 */
public class MyAfterOrderDetailDistributionFragment extends Fragment {

	private final static String TAG = "MyAfterOrderDetailDistributionFragment";
	
	private static final String ORDER_ZERO = "0";
	public static final String ORDER_CODE = "orderCode";
	public static final int RETCODE_CODE = 60511;
	/** 返回图片**/
	private LinearLayout mIvBackup;
	/** 功能描述文本**/
	private TextView mTvdesc;
	/** 接单时间*/
	private LinearLayout mLyTime;
	/** 弹出层**/
	private LinearLayout mTvOrderMore;
	/** PopupWindow容器**/
	private PopupWindow mPopupWindow;
	/** 菜品列表*/
	private ListView mProductList;
	/** 新加菜品*/
	private ListView mProductNewList;
	private LinearLayout mLyNew;
	/** 得到订单的类*/
	private OrderInfo mOrderInfo;
	/** 得到订单编号*/
	private String mOrderCode;
	/** 电话*/
	private String userMobileNbr;
	/** 修改订单状态*/
	private String mOrderStatus;
	/** 之前的下单状态*/
	private String mBeforeOrderStatus;
	/** 联系用户*/
	private Button mBtnMyAfterOrderUser;
	/** 接单状况*/
	private Button mBtnMyAfterOrderOk;
	/** 撤销*/
	private Button mBtnMyAfterOrderBack;
	/** 取消*/
	private Button mBtnMyAfterOrderCancel;
	/** 进度条*/
	private ProgressDialog mProcessDialog;
	/** 支付状态*/
	private TextView mPayStatus;
	/** 定义全局变量*/
	private ShopApplication mShopApplication;
	/** 得到是否入驻的标示*/
	private boolean mSettledflag;
	/** 得到餐桌号的值*/
	private String mTable;
	/** 输入的餐桌号*/
	private String mInputTabe;
	/** 餐桌号的控件 */
	private TextView mealNbr;
	/** 得到餐桌号的值*/
	private String mTableNbr;
	
	public static MyAfterOrderDetailDistributionFragment newInstance() {
		Bundle args = new Bundle();
		MyAfterOrderDetailDistributionFragment fragment = new MyAfterOrderDetailDistributionFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	private Activity getMyActivity(){
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;       
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_myafterorderdetail,container, false);
		ViewUtils.inject(this, view);
		init(view);
		ActivityUtils.add(getActivity());
		Util.addActivity(getMyActivity());
		return view;
	}

	private void init(View view) {
		mShopApplication =  (ShopApplication) getActivity().getApplication();
		mSettledflag = mShopApplication.getSettledflag();
		//设置标题
		mIvBackup = (LinearLayout) view.findViewById(R.id.layout_turn_in);
		mIvBackup.setVisibility(View.VISIBLE);
		mTvdesc = (TextView) view.findViewById(R.id.tv_mid_content);
		mLyTime = (LinearLayout) view.findViewById(R.id.ly_after_heand);
		mTvOrderMore = (LinearLayout) view.findViewById(R.id.layout_msg);
		
		Intent intent = getMyActivity().getIntent();
		if (!Util.isEmpty(intent.getStringExtra(ORDER_CODE))){
			mOrderCode = intent.getStringExtra(ORDER_CODE);
		} else {
			return;
		}
		DB.saveStr(ShopConst.ORDER_CODE, mOrderCode);
		Log.d(TAG, "订单编码=="+mOrderCode);
		//订单详情
		getProductOrderInfo(view);
		mTvOrderMore.setOnTouchListener(orderTouchListener);
	}
	
	/**
	 * 订单状态
	 * @param orderStatus
	 */
	private void getStatus(String orderStatus){
		//根据订单状态显示用户可选择的操作
		if (null == orderStatus) {
			return;
			
		} else {
			Log.d(TAG, "getOrderStatus== "+mOrderInfo.getOrderStatus());
			if (String.valueOf(Order.ORDERED).equals(orderStatus)){//已下单
				Log.d(TAG, "已下单(待接单)。。。。。。。。。。。。。。。");
				mTvdesc.setText(R.string.waitinglist);
				mLyTime.setVisibility(View.VISIBLE);
				setVisibility(true,true,true,true);
				mBtnMyAfterOrderOk.setText(R.string.myafter_confirm_orders);    
				
			} else if (String.valueOf(Order.PEND_ORDER).equals(orderStatus)) {//待下单 
				Log.d(TAG, "待下单 (待接单)。。。。。。。。。。。。。。。");
				mTvdesc.setText(R.string.waitinglist);
				mLyTime.setVisibility(View.VISIBLE);   
				setVisibility(true,true,true,true);
				mBtnMyAfterOrderOk.setText(R.string.myafter_confirm_orders);
				
			} else if(String.valueOf(Order.DELIVERED).equals(orderStatus)) {//已派送
				Log.d(TAG, "已派送(配送中)。。。。。。。。。。。。。。。");
				mTvdesc.setText(R.string.distributionlist);
				mLyTime.setVisibility(View.GONE);
				setVisibility(true,true,true,true);
				mBtnMyAfterOrderOk.setText(R.string.myafter_order_pay);
				
			} else if(String.valueOf(Order.HAS_ORDER).equals(orderStatus)) {//已接单
				Log.d(TAG, "已接单(配送中)。。。。。。。。。。。。。。。");
				mTvdesc.setText(R.string.distributionlist);
				mLyTime.setVisibility(View.GONE);
				setVisibility(true,true,true,true);
				mBtnMyAfterOrderOk.setText(R.string.myafter_order_pay);
				
			} else if(String.valueOf(Order.REVOKED).equals(orderStatus)) {//已撤销
				Log.d(TAG, "已撤销(交易取消)。。。。。。。。。。。。。。。");
				mLyTime.setVisibility(View.GONE);
				mTvdesc.setText(R.string.transactioncancellation);
				setVisibility(true,false,false,true);
				
			} else if(String.valueOf(Order.BEEN_SERVED).equals(mOrderInfo.getOrderStatus())) {//已送达
				if (Integer.parseInt(mOrderInfo.getStatus()) == 3){//支付成功
					Log.d(TAG, "交易成功。。。。。。。。。。。。。。。");
					mTvdesc.setText(R.string.tradingsuccess);
					setVisibility(true,false,false,true);
				} else {//未支付
					Log.d(TAG, "待付款。。。。。。。。。。。。。。。");
					mTvdesc.setText(R.string.orderobligation);
					setVisibility(true,false,false,true);
					mBtnMyAfterOrderOk.setText(R.string.myafter_order_pay);
				}
			}
		}
	}
	
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
	 *
	 */
	OnTouchListener orderTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				View view = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindow_myafterorder, null);
				view.setBackgroundColor(Color.TRANSPARENT);
				mPopupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				ScaleAnimation animation = new ScaleAnimation(1.0f, 1.0f, 0F, 1f, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
				animation.setDuration(500);
				animation.setFillAfter(true);
				mPopupWindow.setFocusable(true);
				mPopupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
				mPopupWindow.setOutsideTouchable(true);
				mPopupWindow.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
				view.setAnimation(animation);
				
				mBtnMyAfterOrderUser = (Button) view.findViewById(R.id.btn_myafterorder_user);
				mBtnMyAfterOrderOk = (Button) view.findViewById(R.id.btn_myafterorder_order_ok);
				mBtnMyAfterOrderBack = (Button) view.findViewById(R.id.btn_myafterorder_order_back);
				mBtnMyAfterOrderCancel = (Button) view.findViewById(R.id.btn_myafterorder_cancel);
				getStatus(mOrderStatus);
				mBtnMyAfterOrderUser.setOnClickListener(afterOrderListener);
				mBtnMyAfterOrderOk.setOnClickListener(afterOrderListener);
				mBtnMyAfterOrderBack.setOnClickListener(afterOrderListener);
				mBtnMyAfterOrderCancel.setOnClickListener(afterOrderListener);
			}
			return true;
		}
	};
	
	private OnClickListener afterOrderListener = new OnClickListener() {
		
		@Override
		public void onClick(final View v) {
			switch (v.getId()) {
			case R.id.btn_myafterorder_user://联系用户
				mPopupWindow.dismiss();
				if (!Util.isEmpty(userMobileNbr)) {
					DialogUtils.showDialog(getActivity(), getString(R.string.cue),getString(R.string.myafter_order_phone_ok), getString(R.string.ok), getString(R.string.no),new DialogUtils().new OnResultListener() {
						@Override
						public void onOK() {
							Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+userMobileNbr));
							getActivity().startActivity(intent);
						}

					});
					
				} else {
					DialogUtils.showDialogSingle(getMyActivity(),  getString(R.string.myafter_order_phone), R.string.cue, R.string.ok, null);
					
				}
				break;
			case R.id.btn_myafterorder_order_ok://确认接单或者去结算
				if (mSettledflag) {
					getTable (v);
					
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
						getSubmitEnd(mOrderStatus,v,Util.NUM_ZERO);
						Log.d(TAG, "撤销了" +mOrderStatus);
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
	 * 弹出餐桌号
	 */
	private void  getTable (final View v) {
		Log.d(TAG, "开启餐桌的管理 == " +mTable);
		if (Integer.parseInt(mTable) == Util.NUM_ONE) {//开启
			
			if (String.valueOf(ShopConst.Order.ORDERED).equals(mOrderStatus)){//确认接单
				//弹出输入餐桌号的对话框
				DialogUtils.showDialogTable(getMyActivity(), getString(R.string.myafter_order__table_title), getString(R.string.ok), getString(R.string.no), new DialogUtils().new OnResListener() {
					@Override
					public void onOk(String... params) {
						LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
						reqParams.get(params[0]);
						mInputTabe = params[0];
						Log.d(TAG, "输入的餐桌号===" + params[0]);
						
						mOrderStatus = ShopConst.Order.HAS_ORDER;
						mBeforeOrderStatus = ShopConst.Order.ORDERED;
						getSubmitEnd(mOrderStatus,v,Util.NUM_ONE);
						
						Log.d(TAG, "接单成功。。。。。。。。。");
					}
				});
				
			} else {//去结算
				Log.d(TAG,"结算。。。。。。。。。");
				Intent intent = new Intent(getMyActivity(), MyAfterOrderDetailSettleActivity.class);
				getMyActivity().startActivity(intent);
			}
			
		} else { //未开启   
			
			if (String.valueOf(ShopConst.Order.ORDERED).equals(mOrderStatus)){//确认接单
				mOrderStatus = ShopConst.Order.HAS_ORDER;
				mBeforeOrderStatus = ShopConst.Order.ORDERED;
				getSubmitEnd(mOrderStatus,v,Util.NUM_ONE);
				Log.d(TAG, "接单成功。。。。。。。。。");
				
			} else {//去结算
				Log.d(TAG,"结算。。。。。。。。。");
				Intent intent = new Intent(getMyActivity(), MyAfterOrderDetailSettleActivity.class);
				getMyActivity().startActivity(intent);
			}
		}
	}
	
	/**
	 * 修改订单状态
	 */
	public void getSubmitEnd(String orderstatus,final View view,int flag){
		mProcessDialog = new ProgressDialog(getMyActivity());
		mProcessDialog.setCancelable(false);
		if (flag == Util.NUM_ONE) {
			mProcessDialog.setMessage(Util.getString(R.string.get_order_processing));
		} else {
			mProcessDialog.setMessage(Util.getString(R.string.get_no_order_processing));
		}
		mProcessDialog.show();
		
		new UpdateProductOrderStatusTask(getMyActivity(), new UpdateProductOrderStatusTask.Callback() {
			
			@Override
			public void getResult(int retCode) {
				if (mProcessDialog != null) {
					mProcessDialog.dismiss();
				}
				if (retCode == 0){
					// api调用失败，状态返回之前的状态
					mOrderStatus = mBeforeOrderStatus;
				} else {
					if (ErrorCode.SUCC == retCode){
						Log.d(TAG, "状态改变了。。。。。。。。。");
						getStatus(mOrderStatus); // 改变状态
						DB.saveBoolean(ShopConst.Key.HAVE_READ,true);
						DB.saveBoolean(ShopConst.Key.UPP_ORDERSTATUS,true);
						//餐号
						Log.d(TAG, "getTableNbr==="+mOrderInfo.getTableNbr());
						
						if (Integer.parseInt(mOrderInfo.getTableNbrSwitch()) == 1) {//开启了餐桌管理
							if (Util.isEmpty(mInputTabe)) {
								mealNbr.setText(mOrderInfo.getMealNbr());
							} else {
								mealNbr.setText(mInputTabe);
							}
							Log.d(TAG, "mTableNbr==="+mTableNbr);
						} else {//没有开启餐桌管理
							mealNbr.setText(mOrderInfo.getMealNbr());
						}

					} else {
						// api调用失败，状态返回之前的状态
						mOrderStatus = mBeforeOrderStatus;
					}
				}
			}
		}).execute(mOrderInfo.getOrderCode(), orderstatus, mOrderInfo.getStatus(),mInputTabe);
	}
	
	/**
	 * 获得订单详情
	 */
	private void getProductOrderInfo(final View v){
		Log.d(TAG, "进来了。。。。。。。。11");
		new GetProductOrderInfoTask(getMyActivity(), new GetProductOrderInfoTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				Log.d(TAG, "进来了。。。。。。。。");
				if (null == result){
					return;
				}
				
				mOrderInfo = Util.json2Obj(result.toString(), OrderInfo.class);
				mBeforeOrderStatus = mOrderInfo.getOrderStatus();
				mOrderStatus = mOrderInfo.getOrderStatus();
				//得到订单详情
				getProductDetail(v,result);
				
				getStatus(mOrderInfo.getOrderStatus());
			}
		}).execute(mOrderCode);
	}
	
	/**
	 * 订单详情
	 */
	private void getProductDetail (View v,JSONObject result) {
		TextView time = (TextView) v.findViewById(R.id.tv_afterorder_time);//确认下单时间
		mealNbr = (TextView) v.findViewById(R.id.tv_afterorder_mealnbr);//餐号
		TextView orderType = (TextView) v.findViewById(R.id.tv_afterorder_ordertype);//订单类型
		TextView receiver = (TextView) v.findViewById(R.id.tv_afterorder_clientcode);//用户
		TextView receiverMobileNbr = (TextView) v.findViewById(R.id.tv_afterorder_receivermobileNbr);//电话
		TextView orderTime = (TextView) v.findViewById(R.id.tv_afterorder_ordertime);//订单时间
		TextView orderNbr = (TextView) v.findViewById(R.id.tv_afterorder_ordernbr);//订单号
		TextView oldAmount = (TextView) v.findViewById(R.id.tv_afterorder_orderproductamount);//旧的商品数量
		TextView newAmount = (TextView) v.findViewById(R.id.tv_afterorder_newAmount);//新的商品数量
		TextView remark = (TextView) v.findViewById(R.id.tv_afterorder_remark);//备注
		TextView orderAmount = (TextView) v.findViewById(R.id.tv_aftetorder_orderamount);//实际支付
		TextView orderPay = (TextView) v.findViewById(R.id.tv_afterorder_pay);//支付状态
		mProductList = (ListView) v.findViewById(R.id.lv_afterorder_list);//没有新加的列表
		mLyNew = (LinearLayout) v.findViewById(R.id.ly_after_new);//
		mProductNewList = (ListView) v.findViewById(R.id.lv_afterorder_new_list);//新增
		
		//是否开启桌号管理的值
		mTable = mOrderInfo.getTableNbrSwitch();
		//确认接单时间
		if (Util.isEmpty(mOrderInfo.getReceiveTime())){
			time.setText("");
		} else {
			time.setText(mOrderInfo.getReceiveTime());
		}
		//餐号
		if (Util.isEmpty(mOrderInfo.getMealNbr())){
			mealNbr.setText("");
		} else {
			mealNbr.setText(mOrderInfo.getMealNbr());
		}
		//订单类型
		if (Util.isEmpty(mOrderInfo.getOrderType())){
			orderType.setText("");
		} else {
			if (Integer.parseInt(mOrderInfo.getOrderType()) == 10){
				orderType.setText("其他订单");
			} 
			else if (Integer.parseInt(mOrderInfo.getOrderType()) == 20) {
				orderType.setText("堂食订单");
			}
			else if (Integer.parseInt(mOrderInfo.getOrderType()) == 21) {
				orderType.setText("外卖订单");   
			}
		}
		//支付状态
		if (Util.isEmpty(mOrderInfo.getStatus())){
			orderPay.setText("");        
		} else {
			Log.d(TAG, "支付状态======="+mOrderInfo.getStatus());
			if (Integer.parseInt(mOrderInfo.getStatus()) == Util.NUM_ONE){//未支付（已经提交了）
				orderPay.setText(R.string.unpaid);
			} else if (Integer.parseInt(mOrderInfo.getStatus()) == Util.NUM_TWO){//支付中（提交之后）
				orderPay.setText(R.string.payment);
			} else if (Integer.parseInt(mOrderInfo.getStatus()) == Util.NUM_THIRD){//已支付（提交之后）
				orderPay.setText(R.string.paid);
			} else if (Integer.parseInt(mOrderInfo.getStatus()) == Util.NUM_FOUR){//已取消（提交之后）
				orderPay.setText(R.string.order_cancel);
			} else if (Integer.parseInt(mOrderInfo.getStatus()) == Util.NUM_FIVE){//支付失败
				orderPay.setText(R.string.fail_pay);
			} else if (Integer.parseInt(mOrderInfo.getStatus()) == Util.NUM_SIX){//退款申请中
				orderPay.setText(R.string.refund_application);
			} else if (Integer.parseInt(mOrderInfo.getStatus()) == Util.NUM_SEVEN){//已退款
				orderPay.setText(R.string.refunded);
			} else if (Integer.parseInt(mOrderInfo.getStatus()) == Util.NUM_ZERO){//等待商家结算
				orderPay.setText(R.string.pend_store);
			}
		}
		//用户
		if (Util.isEmpty(mOrderInfo.getReceiver())){
			receiver.setText("");
		} else {
			receiver.setText(mOrderInfo.getReceiver());
		}
		//电话
		userMobileNbr = mOrderInfo.getReceiverMobileNbr();
		if (Util.isEmpty(mOrderInfo.getReceiverMobileNbr())){
			receiverMobileNbr.setText("");
		} else {
			receiverMobileNbr.setText(mOrderInfo.getReceiverMobileNbr());
		}
		//订单时间
		if (Util.isEmpty(mOrderInfo.getOrderTime())){
			orderTime.setText("00:00");
		} else {
			orderTime.setText(mOrderInfo.getOrderTime());   
		}
		//订单号
		if (Util.isEmpty(mOrderInfo.getOrderNbr())){
			orderNbr.setText("");
		} else {
			orderNbr.setText(mOrderInfo.getOrderNbr());
		}
		//旧的商品数量
		if (Util.isEmpty(mOrderInfo.getOldAmount())){
			oldAmount.setText(ORDER_ZERO);
		} else {
			oldAmount.setText("(" + mOrderInfo.getOldAmount() + "个商品)");
		}
		//新的商品数量
		if (Util.isEmpty(mOrderInfo.getNewAmount())){
			newAmount.setText(ORDER_ZERO);
		} else {
			newAmount.setText("(" + mOrderInfo.getNewAmount() + "个商品)");
		}
		//备注
		if (Util.isEmpty(mOrderInfo.getRemark())){
			remark.setText("");
		} else {
			remark.setText(mOrderInfo.getRemark());
		}
		//消费金额
		if (Util.isEmpty(mOrderInfo.getRealPay())){
			orderAmount.setText(ORDER_ZERO);
		} else {
			orderAmount.setText(mOrderInfo.getRealPay() + "元");
		}
		
		JSONArray orderListArray = (JSONArray) result.get("productList");
		Log.d(TAG, "orderListArray11=="+orderListArray);
		List<ProductList> orderListData = new ArrayList<ProductList>();
		List<ProductList> orderNewListData = new ArrayList<ProductList>();
		if (orderListArray.size() > 0){
			mProductList.setVisibility(View.VISIBLE);
			for (int i = 0; i < orderListArray.size(); i++) {
				JSONObject ordertObject = (JSONObject) orderListArray.get(i);
				ProductList productList = Util.json2Obj(ordertObject.toString(), ProductList.class);
				Log.d(TAG, "getIsNewlyAdd11=="+productList.getIsNewLyAdd());
				if (Integer.parseInt(productList.getIsNewLyAdd()) == 0){//0没有上新  
					mLyNew.setVisibility(View.GONE);
					orderListData.add(productList);
				} else if (Integer.parseInt(productList.getIsNewLyAdd()) == 1){//1有上新
					mLyNew.setVisibility(View.VISIBLE);
					orderNewListData.add(productList);
				}
			}
			ProductListAdapter productListAdapter = new ProductListAdapter(getMyActivity(), orderListData, false);
			mProductList.setAdapter(productListAdapter);
			Util.setListViewHeight(mProductList);
			ProductListAdapter productNewListAdapter = new ProductListAdapter(getMyActivity(), orderNewListData, false);
			mProductNewList.setAdapter(productNewListAdapter);
			Util.setListViewHeight(mProductNewList);
			
		} else {
			mProductList.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 返回
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	public void trunIdenCode(View view){
		getMyActivity().finish();
	}
}
