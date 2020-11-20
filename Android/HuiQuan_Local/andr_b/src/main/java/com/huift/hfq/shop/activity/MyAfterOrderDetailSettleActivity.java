package com.huift.hfq.shop.activity;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.OrderInfo;
import com.huift.hfq.base.pojo.ProductList;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.adapter.ProductListAdapter;
import com.huift.hfq.shop.model.GetProductOrderInfoTask;
import com.huift.hfq.shop.model.SubmitEndTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 订单详情餐后支付--结算
 * @author wensi.yu
 *
 */
public class MyAfterOrderDetailSettleActivity extends Activity{

	private final static String TAG = "MyAfterOrderDetailSettleActivity";
	
	private static final String ORDER_TITLE = "结算";
	private static final String ORDER_ZERO = "0";
	public static final String ORDER_CODE = "orderCode";
	/** 返回图片*/
	private LinearLayout mIvBackup;
	/** 功能描述文本**/
	private TextView mTvdesc;
	/** 菜品列表*/
	private ListView mProductList;    
	/** 新添加的菜品*/
	private LinearLayout mLyNew;
	private ListView mProductNewList;
	/** 得到订单的类*/
	private OrderInfo mOrderInfo;
	/** 得到订单编码*/
	private String mOrderCode = null;
	/** 全选的图标*/
	private CheckBox mCkOrderAll;
	/** 输入的金额*/
	private EditText mInputMoney;
	/** 选中一行*/
	private RelativeLayout mProductLine;
	/** 选中的初始值*/
	private boolean mSelectAll = true;
	/** 列表加载的数据*/
	private List<ProductList> mOrderListData;
	private List<ProductList> mOrderNewListData;
	/** 加载数据的适配器*/
	private ProductListAdapter mProductListAdapter;
	private ProductListAdapter mProductNewListAdapter;
	/** 提交*/
	private Button mProductCommit;
	/** 输入金额*/
	private String mInputAmount;
	/** 勾选的产品id*/
	private String mOrderProductId;    
	/** 等待支付*/
	private Button mOrderProductWai;
	/** 修改价格*/
	private Button mOrderProductUpdate;
	/** 支付完成*/
	private Button mOrderProductPaySucc;
	/** 存放选中的菜单*/
	private List<String> orderProductList;
	/** 传入的总金额*/
	private String mInputAllAmount;
	/** 下拉请求api*/
	private boolean mDataFlag;
	private boolean mDataFlagMark = true;
	/** 下拉加载 */
	private SwipeRefreshLayout mSwipeRefreshLayout;
	/** 线程*/
	private Handler mHandler;
	private View view;
	/** 列表头部的视图*/
	private View mHeadView;
	/** 列表底部的视图*/
	private View mBottomView;
	/** 订单数量*/
	private TextView oldAmount;
	/** 新加的订单数量*/
	private TextView newAmount;
	/** 订单总金额*/
	private TextView orderAmount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_myafterorderdetailsettle);
		ViewUtils.inject(this);
		Util.addActivity(this);
		ActivityUtils.add(this);
		init();
	}

	private void init() {
		initView ();
		//下拉加载
		mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.index_swipe_refresh);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.red);
		mSwipeRefreshLayout.setEnabled(false);
		
		mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
		mHandler = new Handler();
		mLyNew = (LinearLayout) mBottomView.findViewById(R.id.ly_after_new);//新增
		mProductNewList = (ListView)mBottomView.findViewById(R.id.lv_afterorder_new_list);//新增
		mProductNewList.setOnItemClickListener(selectNewClick);
		//调用详情
		getProductOrderInfo(view);
		
	}
	
	private void initView () {
		//加入头部和尾部
		mHeadView = android.view.View.inflate(this, R.layout.top_afterorder, null);
		mBottomView = android.view.View.inflate(this, R.layout.bottom_afterorder, null);
		mDataFlag = true;
		//设置标题
		mIvBackup = (LinearLayout)findViewById(R.id.layout_turn_in);
		mTvdesc = (TextView)findViewById(R.id.tv_mid_content);
		mTvdesc.setText(ORDER_TITLE);
		mIvBackup.setVisibility(View.VISIBLE);
		//得到orderCode
		if ("".equals(mOrderCode) && mOrderCode == null) {
			mOrderCode = "";
		} else {
			mOrderCode = DB.getStr(ShopConst.ORDER_CODE);
		}
		Log.d(TAG, "mOrderCode11=="+mOrderCode);
		mProductCommit = (Button) mBottomView.findViewById(R.id.btn_myafterorder_commit);//提交
		mProductCommit.setOnClickListener(lyClickProductCommit);
		mOrderProductWai = (Button) mBottomView.findViewById(R.id.btn_myafterorder_wait);//等待支付
		mOrderProductUpdate = (Button) mBottomView.findViewById(R.id.btn_myafterorder_update);//修改价格
		mOrderProductPaySucc = (Button) mBottomView.findViewById(R.id.btn_myafterorder_payok);//支付完成
		oldAmount = (TextView) mHeadView.findViewById(R.id.tv_afterorder_orderproductamount);
		newAmount = (TextView) mBottomView.findViewById(R.id.tv_afterorder_newAmount);
		orderAmount = (TextView) mBottomView.findViewById(R.id.tv_aftetorder_orderamount);
		mCkOrderAll = (CheckBox) mBottomView.findViewById(R.id.ck_afterorder_all);
		mInputMoney = (EditText) mBottomView.findViewById(R.id.et_myafterorder_input);//输入的总金额
		mProductLine = (RelativeLayout) mBottomView.findViewById(R.id.rl_afterorder_line_all);//全选的一行
		
		mProductLine.setOnClickListener(clickListener);		
		mOrderProductPaySucc.setOnClickListener(succListener);
		mProductList = (ListView)findViewById(R.id.lv_afterorder_list);
		mProductList.setOnItemClickListener(selectClick);
		
		mProductList.addHeaderView(mHeadView);
		mProductList.addFooterView(mBottomView);
	}
	
	/**
	 * 选中的事件改变的金额
	 */
	private OnItemClickListener selectClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapterview, View view, int position, long id) {
			Log.d(TAG, "position==="+position);
			ProductList productList = (ProductList) mProductList.getItemAtPosition(position);
			productList.setChecked(!productList.getChecked());
			String amountStr = orderAmount.getText().toString().substring(0, orderAmount.getText().toString().length()-1);
			Double newPrice = 0.0;
			if(!productList.getChecked()){
				mCkOrderAll.setButtonDrawable(R.drawable.radio_no);
				newPrice = Double.parseDouble(amountStr)-Double.parseDouble(productList.getProductUnitPrice())*Double.parseDouble(productList.getProductNbr());
			}else{
				newPrice = Double.parseDouble(amountStr)+Double.parseDouble(productList.getProductUnitPrice())*Double.parseDouble(productList.getProductNbr());
			}
			Log.d(TAG, "金额=======1111");
			orderAmount.setText(String.valueOf(newPrice) + "元");
			mProductListAdapter.notifyDataSetChanged();
		}
	}; 
	
	/**
	 * 选中的新加的菜品改变的金额
	 */
	private OnItemClickListener selectNewClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapterview, View view, int position, long id) {
			Log.d(TAG, "position==="+position);
			ProductList productList = (ProductList) mProductNewList.getItemAtPosition(position);
			productList.setChecked(!productList.getChecked());
			String amountStr = orderAmount.getText().toString().substring(0, orderAmount.getText().toString().length()-1);
			Double newPrice = 0.0;
			if (!productList.getChecked()) {
				mCkOrderAll.setButtonDrawable(R.drawable.radio_no);
				newPrice = Double.parseDouble(amountStr)-Double.parseDouble(productList.getProductUnitPrice())*Double.parseDouble(productList.getProductNbr());
			} else {
				newPrice = Double.parseDouble(amountStr)+Double.parseDouble(productList.getProductUnitPrice())*Double.parseDouble(productList.getProductNbr());
			}
			Log.d(TAG, "金额=======2222");
			orderAmount.setText(String.valueOf(newPrice) + "元");
			mProductNewListAdapter.notifyDataSetChanged();
		}
	};
	
	/**
	 * 点击完成
	 */
	private OnClickListener succListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Util.exit();
		}
	};
	
	/**
	 * 全选事件
	 */
	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d(TAG, "全选点击");
			if (mSelectAll){   //全选----》全不选
				if (mOrderListData != null){
					for (ProductList pdl:mOrderListData) {
						pdl.setChecked(false);
						if (mProductListAdapter != null) {
							mProductListAdapter.notifyDataSetChanged();
						}
					}
				}
				
				if (mOrderNewListData != null){
					for (ProductList pdl:mOrderNewListData) {
						pdl.setChecked(false);
						if (mProductNewListAdapter != null) {
							mProductNewListAdapter.notifyDataSetChanged();
						}
					}
				}
			mCkOrderAll.setButtonDrawable(R.drawable.radio_no);
			Log.d(TAG, "金额=======3333");
			orderAmount.setText(ORDER_ZERO + "元");
			} else {   //全不选---》全选
				if (mOrderListData != null){
					for (ProductList pdl:mOrderListData) {
						pdl.setChecked(true);
						if (mProductListAdapter != null) {
							mProductListAdapter.notifyDataSetChanged();
						}
					}
				}
				if (mOrderNewListData != null){
					for (ProductList pdl:mOrderNewListData) {
						pdl.setChecked(true);
						if (mProductNewListAdapter != null) {
							mProductNewListAdapter.notifyDataSetChanged();
						}
					}
				}
				mCkOrderAll.setButtonDrawable(R.drawable.radio_yes);
				totalPrice();
				Log.d(TAG, "getOldAmount==="+mOrderInfo.getOldAmount());
			}
			mSelectAll =! mSelectAll;
		}
	};
	
	/**
	 * 获得订单详情
	 */
	private void getProductOrderInfo(final View v){
		
		new GetProductOrderInfoTask(this, new GetProductOrderInfoTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mDataFlag = true;
				mSwipeRefreshLayout.setRefreshing(false);
				if (null == result){
					return;
				}
				Log.d(TAG, "------"+result.toString());
				mOrderInfo = Util.json2Obj(result.toString(), OrderInfo.class);
				//旧的商品数量
				if (Util.isEmpty(mOrderInfo.getOldAmount())){
					oldAmount.setText(ORDER_ZERO);
				} else {
					oldAmount.setText("(" + mOrderInfo.getOldAmount() + "个商品)");
				}
				//新的的商品数量
				if (Util.isEmpty(mOrderInfo.getNewAmount())){
					newAmount.setText(ORDER_ZERO);
				} else {
					newAmount.setText("(" + mOrderInfo.getNewAmount() + "个商品)");
				}
				//消费金额
				/*if (Util.isEmpty(mOrderInfo.getOrderAmount())){
					orderAmount.setText(ORDER_ZERO);
				} else {
					Log.d(TAG, "金额=======4444"+mOrderInfo.getOrderAmount());
					orderAmount.setText(mOrderInfo.getOrderAmount() + "元");
				}*/
				
				JSONArray orderListArray = (JSONArray) result.get("productList");
				Log.d(TAG, "orderListArray11=="+orderListArray);
				mOrderListData = new ArrayList<ProductList>();
				mOrderNewListData = new ArrayList<ProductList>();
				if (orderListArray.size() > 0){
					for (int i = 0; i < orderListArray.size(); i++) {
						JSONObject ordertObject = (JSONObject) orderListArray.get(i);
						ProductList productList = Util.json2Obj(ordertObject.toString(), ProductList.class);	
						Log.d(TAG, "解析结果==="+productList.toString());
						Log.d(TAG, "getIsNewlyAdd11=="+productList.getIsNewLyAdd());
						if (Integer.parseInt(productList.getIsNewLyAdd()) == 0){//0没有上新  
							mOrderListData.add(productList);
						} else if (Integer.parseInt(productList.getIsNewLyAdd()) == 1){//1有上新
							mOrderNewListData.add(productList);
						}
					}
					
					if (mOrderListData.size() > 0) {
						mProductList.setVisibility(View.VISIBLE);
					} else {
						mProductList.setVisibility(View.GONE);
					}
					
					if (mOrderNewListData.size() > 0) {
						mLyNew.setVisibility(View.VISIBLE);
					} else {
						mLyNew.setVisibility(View.GONE);
					}
					
					
					mProductListAdapter = new ProductListAdapter(MyAfterOrderDetailSettleActivity.this, mOrderListData, true);
					mProductList.setAdapter(mProductListAdapter);
					Util.setListViewHeight(mProductList);
					mProductNewListAdapter = new ProductListAdapter(MyAfterOrderDetailSettleActivity.this, mOrderNewListData, true);
					mProductNewList.setAdapter(mProductNewListAdapter);
					mProductNewListAdapter.notifyDataSetChanged();
					
				} 
				
				//默认进来全选
				if (mSelectAll && mDataFlagMark) { 
					if (mOrderListData != null) {
						for (ProductList pdl:mOrderListData) {
							pdl.setChecked(true);
							if (mProductListAdapter != null) {
								mProductListAdapter.notifyDataSetChanged();
							}
						}
					}
					if (mOrderNewListData != null) {
						for (ProductList pdl:mOrderNewListData) {
							pdl.setChecked(true);
							if (mProductNewListAdapter != null) {
								mProductNewListAdapter.notifyDataSetChanged();
							}
						}
					}
					mCkOrderAll.setButtonDrawable(R.drawable.radio_yes);
					Log.d(TAG, "金额=======5555");
					orderAmount.setText(0.0 + "元");
					totalPrice();
				} 
				
				//得到的支付状态
				getOrderStatus ();
				
				//消费金额
				if (Util.isEmpty(mOrderInfo.getOrderAmount())){
					orderAmount.setText(ORDER_ZERO);
				} else {
					if (Integer.parseInt(mOrderInfo.getStatus()) == Util.NUM_THIRD) {
						orderAmount.setText(mOrderInfo.getOrderAmount() + "元");
					} else {
						getAllMoney();
					}
				}
				
			}
		}).execute(mOrderCode);
	}
	
	/**
	 * 勾选菜单计算的总金额
	 */
	private void getAllMoney () {
		//总金额
		Double price = 0.0;
		for (int i = 0; i < mOrderListData.size(); i++) {
			if(mOrderListData.get(i).getChecked()) {
				price += Double.parseDouble(mOrderListData.get(i).getProductPrice());
			}
		}
		for (int i = 0; i < mOrderNewListData.size(); i++) {
			if (mOrderNewListData.get(i).getChecked()) {
				price += Double.parseDouble(mOrderNewListData.get(i).getProductPrice());
			}
		}
		orderAmount.setText(String.valueOf(price+"元"));
	}
	
	/**
	 * 得到的支付状态
	 */
	private void getOrderStatus () {
		//支付的状态
		Log.d(TAG, "支付的状态 set===="+mOrderInfo.getStatus());
		if (!Util.isEmpty(mOrderInfo.getStatus())) {
			if (Integer.parseInt(mOrderInfo.getStatus()) == Util.NUM_ONE) {//未支付（已经提交了）
				//保存选好了的菜品
				getSaveStatus ();
				//根据支付状态改变下面的按钮状态
				getPayVisibility(false,true,true,false);
				getClick();
				mOrderProductUpdate.setOnClickListener(updateClick);
				
			} else if (Integer.parseInt(mOrderInfo.getStatus()) == Util.NUM_TWO) {//支付中（提交之后）
				//清除之前选择的菜品
				DB.deleteKey(ShopConst.ORDER_MENUE);
				//根据支付状态改变下面的按钮状态
				getPayVisibility(false,true,true,false);
				getClick();
				
			} else if (Integer.parseInt(mOrderInfo.getStatus()) == Util.NUM_THIRD) {//已支付（提交之后）
				Log.d(TAG, "已支付。。。。。。。。。。。。。。");
				//清除之前选择的菜品
				DB.deleteKey(ShopConst.NEW_ORDER_MENUE);
				//根据支付状态改变下面的按钮状态
				getPayVisibility(false,false,false,true);
				getClick();
				
			} else if (Integer.parseInt(mOrderInfo.getStatus()) == Util.NUM_FOUR) {//已取消（提交之后）
				//根据支付状态改变下面的按钮状态
				getPayVisibility(false,false,false,true);
				mOrderProductPaySucc.setText(R.string.myafter_order_pay_cancel);
				getClick();
				
			} else if (Integer.parseInt(mOrderInfo.getStatus()) == Util.NUM_FIVE) {//支付失败
				//根据支付状态改变下面的按钮状态
				getPayVisibility(false,false,false,true);
				mOrderProductPaySucc.setText(R.string.myafter_order_pay_error);
				getClick();
				
			} else if (Integer.parseInt(mOrderInfo.getStatus()) == Util.NUM_SIX) {//退款申请中
				//根据支付状态改变下面的按钮状态
				getPayVisibility(false,false,false,true);
				mOrderProductPaySucc.setText(R.string.myafter_order_pay_back);
				getClick();
				
			} else if (Integer.parseInt(mOrderInfo.getStatus()) == Util.NUM_SEVEN) {//已退款
				//根据支付状态改变下面的按钮状态
				getPayVisibility(false,false,false,true);
				mOrderProductPaySucc.setText(R.string.refunded);
				getClick();
				
			} else if (Integer.parseInt(mOrderInfo.getStatus()) == Util.NUM_ZERO) {//不能支付（未提交）
				//根据支付状态改变下面的按钮状态
				getPayVisibility(true,false,false,false);
				getFailClick();
				//Util.getContentValidate(R.string.pay_order_fail);
			}
		}
	}
	
	/**
	 * 根据支付状态判断按钮的状态
	 */
	private void getPayVisibility (boolean commit,boolean wait,boolean update,boolean succ) {
		if (commit) {
			mProductCommit.setVisibility(View.VISIBLE);
		} else {
			mProductCommit.setVisibility(View.GONE);
		}
		
		if (wait) {
			mOrderProductWai.setVisibility(View.VISIBLE);
		} else {
			mOrderProductWai.setVisibility(View.GONE);
		}
		
		if (update) {
			mOrderProductUpdate.setVisibility(View.VISIBLE);
		} else {
			mOrderProductUpdate.setVisibility(View.GONE);
		}
		
		if (succ) {
			mOrderProductPaySucc.setVisibility(View.VISIBLE);
		} else {
			mOrderProductPaySucc.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 保存提交之后选择的菜品的状态
	 */
	private void getSaveStatus () {
		//取出之前存储的菜品
		String product = DB.getStr(ShopConst.ORDER_MENUE);
		Log.d(TAG, "存储的原先产品==="+product);
		String[] orderSpilt = product.split(",");
		Log.d(TAG,"orderSpilt=="+orderSpilt[0]);
		//现将mOrderListData的中的数据设置为false
		for (int i = 0; i < mOrderListData.size(); i++) {
			mOrderListData.get(i).setChecked(false);
		}
		for (int i = 0; i < orderSpilt.length; i++) {
			for (int j = 0; j < mOrderListData.size(); j++) {
				ProductList productList = mOrderListData.get(j);
				if(orderSpilt[i].equals(productList.getProductId())){
					productList.setChecked(true);
				}
			}
		}
		mProductListAdapter.notifyDataSetChanged();
		
		//取出之前存储的新加的菜品
		String newProduct = DB.getStr(ShopConst.NEW_ORDER_MENUE);
		Log.d(TAG, "存储的新加产品==="+newProduct);
		String[] newSplit = newProduct.split(",");
		//现将mOrderNewListData的中的数据设置为false
		for (int i = 0; i < mOrderNewListData.size(); i++) {
			mOrderNewListData.get(i).setChecked(false);
		}
		for (int i=0; i<newSplit.length; i++) {
			for (int j = 0; j < mOrderNewListData.size(); j++) {
				ProductList productList = mOrderNewListData.get(j);
				if(newSplit[i].equals(productList.getProductId())){
					productList.setChecked(true);
				}
			}
		}
		mProductNewListAdapter.notifyDataSetChanged();
		
		//全选按钮
		Log.d(TAG, "size"+newSplit.length);
		Log.d(TAG, "size"+orderSpilt.length);
		Log.d(TAG, "size"+mOrderNewListData.size());
		Log.d(TAG, "size"+mOrderListData.size());
		
		if(isSaveAll(product, newProduct)){
			mCkOrderAll.setButtonDrawable(R.drawable.radio_yes);
		}else{
			mCkOrderAll.setButtonDrawable(R.drawable.radio_no);
		}
		
		//总金额
		getAllMoney();
	}
	
	/**
	 * 支付状态的改变与菜单是否点击
	 */
	private void getClick(){
		mProductListAdapter.setClick(false);
		mProductListAdapter.notifyDataSetChanged();
		mProductNewListAdapter.setClick(false);
		mProductNewListAdapter.notifyDataSetChanged();
		mProductLine.setEnabled(false);
		mInputMoney.setEnabled(false);
		mProductList.setEnabled(false);
		mProductNewList.setEnabled(false);
	}
	
	/**
	 * 支付失败(状态为0)
	 */
	private void getFailClick(){
		mProductListAdapter.setClick(true);
		mProductListAdapter.notifyDataSetChanged();
		mProductNewListAdapter.setClick(true);
		mProductNewListAdapter.notifyDataSetChanged();
		mProductLine.setEnabled(true);
		mInputMoney.setEnabled(true);
		mProductList.setEnabled(true);
		mProductNewList.setEnabled(true);
	}
	
	/**
	 * 统计总金额
	 */
	private void totalPrice () {
		Double totalPrice = 0.0;
		for (int i = 0; i < mOrderListData.size(); i++) {
			totalPrice += Double.parseDouble(mOrderListData.get(i).getProductPrice());
		}
		for (int i = 0; i < mOrderNewListData.size(); i++) {
			totalPrice += Double.parseDouble(mOrderNewListData.get(i).getProductPrice());
		}
		orderAmount.setText(String.valueOf(totalPrice+"元"));
	}
	
	
	/**
	 * 判断是否保存了全部的菜单
	 */
	public boolean  isSaveAll(String order,String newOrder){
		StringBuffer sbOrder = new StringBuffer();
		for (int i = 0; i < mOrderListData.size(); i++) {
			ProductList productList = mOrderListData.get(i);
			sbOrder.append(productList.getProductId()+",");
		}
		
		StringBuffer sbNewOrder = new StringBuffer();
		for (int i = 0; i < mOrderNewListData.size(); i++) {
			ProductList productList = mOrderNewListData.get(i);
			sbNewOrder.append(productList.getProductId()+",");
		}
		Log.d(TAG, "------1"+order);
		Log.d(TAG, "------2"+sbOrder.toString());
		Log.d(TAG, "------3"+newOrder);
		Log.d(TAG, "------4"+sbNewOrder.toString());
		
		return order.equals(sbOrder.toString())&&newOrder.equals(sbNewOrder.toString());
	}
	
	/**
	 * 提交订单
	 */
	private void submitEnd(){
		mInputAmount = mInputMoney.getText().toString();          
		if (mInputAmount == null && "".equals(mInputAmount)){
			mInputAllAmount = mOrderInfo.getOrderAmount();//消费金额
		} else {
			mInputAllAmount = mInputAmount;
		}
		Log.d(TAG, "jine======"+mInputAllAmount);
		
		mProductCommit.setEnabled(false);
		mSwipeRefreshLayout.setEnabled(true);
		
		new SubmitEndTask(MyAfterOrderDetailSettleActivity.this, new SubmitEndTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mProductCommit.setEnabled(true);
				if (null == result){
					return;
				}
				   
				if (result.get("code").toString().equals(String.valueOf(ErrorCode.SUCC))) {
					Util.getContentValidate(R.string.myafter_order_commit_ok);
					mProductCommit.setVisibility(View.GONE);
					mOrderProductWai.setVisibility(View.VISIBLE);
					mOrderProductWai.setEnabled(false);
					mOrderProductUpdate.setVisibility(View.VISIBLE);
					mProductLine.setEnabled(false);
					mInputMoney.setEnabled(false);
					
					mProductListAdapter.setClick(false);
					mProductListAdapter.notifyDataSetChanged();
					mProductNewListAdapter.setClick(false);
					mProductNewListAdapter.notifyDataSetChanged();
					mProductList.setEnabled(false);
					mProductNewList.setEnabled(false);
					//点击修改价格的按钮
					mOrderProductUpdate.setOnClickListener(updateClick);
				} 
			}
		}).execute(mOrderCode,mInputAllAmount,orderProductList.toString());
	}

	/**
	 * 点击修改价格
	 */
	OnClickListener updateClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mProductListAdapter.setClick(true);
			mProductListAdapter.notifyDataSetChanged();
			mProductNewListAdapter.setClick(true);
			mProductNewListAdapter.notifyDataSetChanged();
			mProductList.setEnabled(true);
			mProductNewList.setEnabled(true);
			mProductLine.setEnabled(true);
			mInputMoney.setEnabled(true); 
			mProductCommit.setVisibility(View.VISIBLE);
			mOrderProductWai.setVisibility(View.GONE);
			mOrderProductUpdate.setVisibility(View.GONE);
			selectOrder();
		}
	};
	
	/**
	 * 选择的菜品
	 */
	private void selectOrder() {
		orderProductList = new ArrayList<String>();
		int count = 0;
		for (ProductList item:mOrderListData){
			if (item.getChecked()){
				DB.saveBoolean(ShopConst.CLICK_MENUE, true);
				mOrderProductId = item.getOrderProductId();
				orderProductList.add(mOrderProductId);
				count++;
			} else {
				DB.saveBoolean(ShopConst.CLICK_MENUE, false);
				mOrderProductId = "";
			}
			Log.d(TAG, "mOrderProductId11==="+mOrderProductId);
		}
		
		for (ProductList item:mOrderNewListData){
			if (item.getChecked()){
				DB.saveBoolean(ShopConst.CLICK_MENUE, true);
				mOrderProductId = item.getOrderProductId();
				orderProductList.add(mOrderProductId);
				count++;
			} else {
				DB.saveBoolean(ShopConst.CLICK_MENUE, false);
				mOrderProductId = "";
			}
			Log.d(TAG, "mOrderProductId22==="+mOrderProductId);
		}
		Log.d(TAG, "orderProductList==="+orderProductList.toString());
		
		//原先的产品边编号集合
		String orderSelect = "";
		for (int i = 0; i < mOrderListData.size(); i++) {
			ProductList productList = mOrderListData.get(i);
			if(productList.getChecked()){
				orderSelect = orderSelect + productList.getProductId()+",";
			}
		}
		Log.d(TAG, "newOrderMenue aa =="+orderSelect);
		DB.saveStr(ShopConst.ORDER_MENUE, orderSelect);
		
		//新加产品的编号集合
		String newOrderSelect = "";
		for (int i = 0; i < mOrderNewListData.size(); i++) {
			ProductList productList = mOrderNewListData.get(i);
			if(productList.getChecked()){
				newOrderSelect = newOrderSelect+productList.getProductId()+",";
			}
		}
		Log.d(TAG, "newOrderSelect aa =="+newOrderSelect);
		DB.saveStr(ShopConst.NEW_ORDER_MENUE, newOrderSelect);
		
		if (orderProductList.size() == 0 || "[]".equals(orderProductList.toString())){ 
			Log.d(TAG, "为空。。。。。。。。。11");
			Util.getContentValidate(R.string.myafter_order_pay_order);
			return;
		}
	}
	
	/**
	 * 点击提交
	 */
	OnClickListener lyClickProductCommit = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			selectOrder();
			if (orderProductList.size() != 0 || !"[]".equals(orderProductList.toString())){ 
				//提交订单
				submitEnd();
			}
		}
	};
	
	/**
	 * 返回
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	public void trunIdenCode(View view){
		switch (view.getId()) {
		case R.id.layout_turn_in://返回
			getBack();
			finish();
			break;

		default:
			break;
		}
	}
	
	/**
	 * 重写点击返回按钮方法，点击一次土司提示，两次退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			getBack ();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 返回的状态
	 */
	private void getBack() {
		if (Integer.parseInt(mOrderInfo.getStatus()) == Util.NUM_THIRD) {
			ActivityUtils.finishAll();
		} else if (Integer.parseInt(mOrderInfo.getStatus()) == Util.NUM_FOUR) {
			ActivityUtils.finishAll();
		} else if (Integer.parseInt(mOrderInfo.getStatus()) == Util.NUM_FIVE) {
			ActivityUtils.finishAll();
		} else if (Integer.parseInt(mOrderInfo.getStatus()) == Util.NUM_SIX) {
			ActivityUtils.finishAll();
		} else if (Integer.parseInt(mOrderInfo.getStatus()) == Util.NUM_SEVEN) {
			ActivityUtils.finishAll();
		} else {
			finish();
		}
	}
	
	/**
	 * 下拉刷新
	 */
	OnRefreshListener refreshListener = new OnRefreshListener() {
		
		@Override
		public void onRefresh() {
			if (mDataFlag) {
				mDataFlag = false;
				mDataFlagMark = false;
				mHandler.postDelayed(new Runnable() {
					public void run() {
						getProductOrderInfo(view);
					}
				}, 5000);
			}
		}
	};
}
