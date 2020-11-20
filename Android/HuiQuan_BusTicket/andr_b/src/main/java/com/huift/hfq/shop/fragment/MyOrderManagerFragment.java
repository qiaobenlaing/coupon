package com.huift.hfq.shop.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.OrderInfo;
import com.huift.hfq.base.pojo.PageData;
import com.huift.hfq.base.utils.TimeFormatUtil;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.base.view.XListView;
import com.huift.hfq.base.view.XListView.IXListViewListener;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.MyStoreOrderActivity;
import com.huift.hfq.shop.activity.ScanActivity;
import com.huift.hfq.shop.adapter.MyOrderAdapter;
import com.huift.hfq.shop.model.CountOrderByTypeTask;
import com.huift.hfq.shop.model.GetOrderListForBTask;
import com.huift.hfq.shop.model.GetShopOrderTypeTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.minidev.json.JSONObject;

import java.util.List;

/**
 * 门店订单和外卖订单
 * @author qian.zhou
 */
public class MyOrderManagerFragment extends Fragment implements IXListViewListener{
	/** 扫描条形码的标志*/
	public static final int BAR_CODE = 1001;
	/** 得到条形码成功的标志*/
	public static final int BAR_CODE_SUCC = 1002;
	/** 得到的条形码的结果*/
	public static final String BAR_CODE_RESULT= "barCodeResult";
	/** 门店订单数量*/
	private TextView mTvStoreOrderCount;
	/** 外卖订单数量*/
	private TextView mTvOutSideOrderCount;
	/** 表示订单查看状态*/
	private boolean mUppFlag = false;
	private OrderInfo mOrderInfo;
	/** 门店订单*/
	private RelativeLayout mRyStoreOrder;
	/** 外卖订单*/
	private RelativeLayout mRyOutSideOrder;
	/** 餐饮*/
	private LinearLayout mRyContent;
	/** 除餐饮以外的订单*/
	private FrameLayout mFlOrder;
	/** 除餐饮以外的订单*/
	private XListView mLvOrder;
	/** 上拉的标志*/
	private boolean mFlagData = true;
	/** 第一次加载*/
	private boolean mFirstFlag = true;
	/** 页码*/
	private int mPage = 1;
	/** 没有数据加载 */
	private LinearLayout mLyView;
	/** 没有数据加载的图片 */
	private ImageView mIvView;
	/** 正在加载的进度条 */
	private ProgressBar mProgView;
	/** 适配器*/
	private MyOrderAdapter mMyOrderAdapter;
	/** 筛选输入框*/
	private EditText mEtPhone;
	/** 时间减*/
	private TextView mTvLess;
	/** 时间加*/
	private TextView mTvAdd;
	/** 时间的改变*/
	private TextView mTvTimeSwitch;
	/** 全局视图*/
	private View mView;
	/** 查询时间*/
	private String mSelTime;
	/** 扫一扫*/
	private ImageView mTvScan ;
	/** 查询*/
	private TextView mTvOrderNbr ;
	/** 得到的条码的值*/
	private String mBarCodeInfo;
	/** “正在处理”进度条 */
	private ProgressDialog mProcessDialog = null;
	/** 餐饮*/
	private EditText mEdtEatOrderNbr;
	/**
	 * 传递参数有利于解耦
	 */
	public static MyOrderManagerFragment newInstance() {
		Bundle args = new Bundle();
		MyOrderManagerFragment fragment = new MyOrderManagerFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.activity_order_home, container, false);
		ViewUtils.inject(this, mView);
		Util.addLoginActivity(getActivity());
		findView(mView);
		initData(mView);
		return mView;
	}

	/**
	 * 获取
	 * @param view
	 */
	private void findView(View view) {
		//标题
		LinearLayout ivBack = (LinearLayout) view.findViewById(R.id.layout_turn_in);// 返回
		ivBack.setVisibility(View.VISIBLE);// 显示
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_mid_content);
		tvTitle.setText(getResources().getString(R.string.order_manager));
		mTvScan = (ImageView) view.findViewById(R.id.tv_msg);
		mTvScan.setOnClickListener(scanClick);
		//初始化数据
		mRyStoreOrder = (RelativeLayout) view.findViewById(R.id.ry_store_order);//门店订单
		mRyOutSideOrder = (RelativeLayout) view.findViewById(R.id.ry_outside_order);//外卖订单
		mTvStoreOrderCount = (TextView) view.findViewById(R.id.tv_storeorder_count);
		mTvOutSideOrderCount = (TextView) view.findViewById(R.id.tv_outsideorder_count);
		mRyContent = (LinearLayout) view.findViewById(R.id.ry_content);
		mLvOrder = (XListView) view.findViewById(R.id.lv_order);
		mFlOrder = (FrameLayout)view.findViewById(R.id.fl_order);
		mLyView = (LinearLayout) view.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) view.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) view.findViewById(R.id.prog_nodata);
		mEdtEatOrderNbr = (EditText) view.findViewById(R.id.et_eat_ordernbr);
		// 列表
		mLvOrder.setPullLoadEnable(true);
		mLvOrder.setXListViewListener(this);
	}

	/**
	 * 扫描的点击事件
	 */
	private OnClickListener scanClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(), ScanActivity.class);
			intent.putExtra(ScanActivity.FLAG, String.valueOf(Util.NUM_TWO));
			startActivityForResult(intent, BAR_CODE);
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == BAR_CODE) {
			if (resultCode == BAR_CODE_SUCC) {
				mBarCodeInfo = data.getStringExtra(BAR_CODE_RESULT);
				mEtPhone.setText(Util.isEmpty(mBarCodeInfo) ? "" : mBarCodeInfo);
				getOrderListForB();
			}
		}
	};

	/**
	 * 初始化数据
	 * @param view
	 */
	private void initData(View view) {
		//调用方法
		getShopOrderType();
		//获得门店订单和外卖订单的消息数量
		getCountOrderByType();
//		getOrderListForB();
	}

	/**
	 * 非餐饮的订单
	 */
	public void getOrderListForB () {
		String keyWord = TextUtils.isEmpty(mEtPhone.getText()) ? "" : mEtPhone.getText().toString() ;
		String []params = {keyWord,mSelTime,mPage+""};
		if (mFirstFlag) {
			mFirstFlag = false;
			ViewSolveUtils.setNoData(mLvOrder, mLyView, mIvView, mProgView, ShopConst.DATA.LOADIMG); // 正在加载
		}
		new GetOrderListForBTask(getActivity(), new GetOrderListForBTask.Callback() {

			@Override
			public void getResult(JSONObject result) {
				mLvOrder.stopLoadMore(); // 停止加载
				mFlagData = true;
				if (null != mProcessDialog) {
					mProcessDialog.dismiss();
				}
				if (result == null) {
					mLvOrder.setPullLoadEnable(false);
					ViewSolveUtils.morePageOne(mLvOrder, mLyView, mIvView, mProgView, mPage);
				} else {
					ViewSolveUtils.setNoData(mLvOrder, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA); // 有数据
					mLvOrder.setPullLoadEnable(true);
					PageData page = new PageData(result, "orderList", new TypeToken<List<OrderInfo>>() {}.getType());
					mPage = page.getPage();
					if (page.hasNextPage() == false) {
						if (mPage > 1) {
							Util.getContentValidate(R.string.no_more);
						}
						mLvOrder.setPullLoadEnable(false);
					} else {
						mLvOrder.setPullLoadEnable(true);
					}
					List<OrderInfo> list = (List<OrderInfo>) page.getList();
					if (null == list || list.size() <= 0) {
						ViewSolveUtils.morePageOne(mLvOrder, mLyView, mIvView, mProgView, mPage);
					} else {
						ViewSolveUtils.setNoData(mLvOrder, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA); // 有数据
					}

					if (mMyOrderAdapter == null) {
						mMyOrderAdapter = new MyOrderAdapter(getActivity(), list);
						mLvOrder.setAdapter(mMyOrderAdapter);
					} else {
						if (page.getPage() == 1) {
							mMyOrderAdapter.setItems(list);
						} else {
							mMyOrderAdapter.addItems(list);
						}
					}
					try {
						mPage = Integer.parseInt(result.get("nextPage").toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).execute(params);
	}

	/**
	 * 调用api的进度条
	 */
	private void setProgressDialog() {
		mProcessDialog = new ProgressDialog(getActivity());
		mProcessDialog.setCancelable(false);
		mProcessDialog.setMessage("正在查询，请稍等");
		mProcessDialog.show();
	}

	/**
	 * 判断该商家是否有门店订单和外卖订单
	 */
	public void getShopOrderType(){
		new GetShopOrderTypeTask(getActivity(), new GetShopOrderTypeTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (result == null) {
					return;
				} else {
					String isCatering = result.get("isCatering").toString();
					String isOuttake = result.get("isOuttake").toString();
					if (String.valueOf(Util.NUM_ONE).equals(isCatering)) {//是否有门店订单，而且还是餐饮类
						mTvScan.setVisibility(View.GONE);
						mRyContent.setVisibility(View.VISIBLE);
						mFlOrder.setVisibility(View.GONE);
						setOrder(true);
						//是否有外卖订单
						mRyOutSideOrder.setVisibility(String.valueOf(Util.NUM_ONE).equals(isOuttake) ? View.VISIBLE : View.GONE);

					} else { // 不是餐饮类
						mFlOrder.setVisibility(View.VISIBLE);
						mRyContent.setVisibility(View.GONE);
						mTvScan.setVisibility(View.VISIBLE);
						//listview的头部
//						View headView = getActivity().getLayoutInflater().inflate(R.layout.item_listview_head, null);
//						mLvOrder.addHeaderView(headView);
						mEtPhone = (EditText) mView.findViewById(R.id.et_ordernbr);
						//头部视图
						mTvLess = (TextView) mView.findViewById(R.id.tv_less);//减
						mTvAdd = (TextView) mView.findViewById(R.id.tv_add);//加
						mTvTimeSwitch = (TextView) mView.findViewById(R.id.tv_time_switch);
						mTvTimeSwitch.setTextColor(getActivity().getResources().getColor(R.color.red));
						mEtPhone = (EditText) mView.findViewById(R.id.et_ordernbr);
						mEtPhone.setHint(Util.getString(R.string.hint_input));
						mTvOrderNbr = (TextView)mView.findViewById(R.id.tv_ordernbr);
						mSelTime = TimeFormatUtil.getNowTimeYMD(); // 获取年月日 2016-02-29
						setInputSel();
						getOrderListForB();
						setEnClick(mSelTime); // TODO 填写当天时间
						mTvLess.setOnClickListener(timeSelListener);
						mTvAdd.setOnClickListener(timeSelListener);
						mTvTimeSwitch.setOnClickListener(timeSelListener);
						mTvOrderNbr.setOnClickListener(timeSelListener);
					}
				}
			}
		}).execute();
	}

	/**
	 * 点击时间
	 */
	private void setEnClick (String cliclTime) {
		if (cliclTime.equals(TimeFormatUtil.getNowTimeYMD())) {
			mTvAdd.setEnabled(false);
			mTvAdd.setTextColor(getActivity().getResources().getColor(R.color.gray));
		} else {
			mTvAdd.setTextColor(getActivity().getResources().getColor(R.color.dark_gray));
			mTvAdd.setEnabled(true);
		}
	}

	/**
	 * 查询上一天下一天的订单
	 */
	private OnClickListener timeSelListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.tv_less: // 上一天
					mSelTime = TimeFormatUtil.lessTime(mSelTime);
					setTextColor(R.id.tv_less);
					break;
				case R.id.tv_add: // 下一天
					mSelTime = TimeFormatUtil.addTime(mSelTime);
					setTextColor(R.id.tv_add);
					break;
				case R.id.tv_time_switch: // 今天
					mSelTime = TimeFormatUtil.getNowTimeYMD();
					setTextColor(R.id.tv_time_switch);
					break;
				case R.id.tv_ordernbr: // 查询
					mPage = 1;
					getOrderListForB();
					break;

				default:
					break;
			}
		}
	};

	/**
	 * 三个选项卡的颜色
	 * @param id 控件对应的Id
	 */
	private void setTextColor (int id) {
		setProgressDialog();
		mPage = 1;
		setEnClick(mSelTime);
		getOrderListForB();
		mTvAdd.setTextColor(getActivity().getResources().getColor(R.color.dark_gray));
		mTvLess.setTextColor(getActivity().getResources().getColor(R.color.dark_gray));
		mTvTimeSwitch.setTextColor(getActivity().getResources().getColor(R.color.dark_gray));
		switch (id) {
			case R.id.tv_less:
				mTvLess.setTextColor(getActivity().getResources().getColor(R.color.red));
				break;
			case R.id.tv_add:
				mTvAdd.setTextColor(getActivity().getResources().getColor(R.color.red));
				break;
			case R.id.tv_time_switch:
				mTvTimeSwitch.setTextColor(getActivity().getResources().getColor(R.color.red));
				break;

			default:
				break;
		}
	}

	/**
	 * 输入事件
	 */
	private void setInputSel () {
		//输入手机号码搜索事件
		mEtPhone.findFocus();
		mEtPhone.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					mPage = 1;
					getOrderListForB();
				}
				return false;
			}
		});
		mEtPhone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mPage = 1;
				getOrderListForB();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	/**
	 * 设置外卖和门店订单是否显示
	 */
	public void setOrder(boolean isOrder){
		if (isOrder) {
			mRyStoreOrder.setVisibility(View.VISIBLE);
		} else {
			mRyStoreOrder.setVisibility(View.GONE);
			mRyOutSideOrder.setVisibility(View.GONE);
		}
	}

	/**
	 * 查询外卖订单和门店订单的消息数量
	 */
	public void getCountOrderByType(){
		new CountOrderByTypeTask(getActivity(), new CountOrderByTypeTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (result == null) {
					return;
				} else {
					mOrderInfo = Util.json2Obj(result.toString(), OrderInfo.class);
					mTvStoreOrderCount.setText(mOrderInfo.getEatIn());
					mTvOutSideOrderCount.setText(mOrderInfo.getTakeOut());
					//是否有门店订单
					mTvStoreOrderCount.setVisibility(String.valueOf(Util.NUM_ZERO).equals(mOrderInfo.getEatIn()) ? View.GONE : View.VISIBLE);
					//是否有外卖订单
					mTvOutSideOrderCount.setVisibility(String.valueOf(Util.NUM_ZERO).equals(mOrderInfo.getTakeOut()) ? View.GONE : View.VISIBLE);
				}
			}
		}).execute();
	}

	/**
	 * 添加fragment的跳转
	 * @param v
	 */
	@OnClick({ R.id.ry_store_order, R.id.ry_outside_order, R.id.layout_turn_in, })
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ry_store_order:// 加载到门店订单页面
				Intent intent = new Intent(getActivity(), MyStoreOrderActivity.class);
				intent.putExtra(MyStoreOrTakeoutFragment.ORDER_TYPE, "20");
				intent.putExtra(MyStoreOrTakeoutFragment.ORDER_INPUT, Util.isEmpty(mEdtEatOrderNbr.getText().toString()) ? "" : mEdtEatOrderNbr.getText().toString());
				startActivity(intent);
				break;
			case R.id.ry_outside_order:// 加载到外卖订单页面
				Intent outIntent = new Intent(getActivity(), MyStoreOrderActivity.class);
				outIntent.putExtra(MyStoreOrTakeoutFragment.ORDER_TYPE, "21");
				outIntent.putExtra(MyStoreOrTakeoutFragment.ORDER_INPUT, Util.isEmpty(mEdtEatOrderNbr.getText().toString()) ? "" : mEdtEatOrderNbr.getText().toString());
				startActivity(outIntent);
				break;
			case R.id.layout_turn_in://返回
				getActivity().finish();
				break;
			default:
				break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		mUppFlag = DB.getBoolean(ShopConst.Key.HAVE_READ);
		if (mUppFlag) {
			DB.saveBoolean(ShopConst.Key.HAVE_READ,false);
			getShopOrderType();
			getCountOrderByType();
		}
	}

	/**
	 * 上拉
	 */
	@Override
	public void onLoadMore() {
		if (mFlagData) {
			mFlagData = false;
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					getOrderListForB();
				}
			}, 2000);
		}
	}
}
