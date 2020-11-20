package cn.suanzi.baomi.shop.fragment;

import java.util.List;

import net.minidev.json.JSONArray;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.MyOrderItem;
import cn.suanzi.baomi.base.utils.TimeFormatUtil;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopConst;
import cn.suanzi.baomi.shop.adapter.MyStoreOrderTitleAdapter;
import cn.suanzi.baomi.shop.model.GetNTakeoutOrderTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 门店订单和外卖订单列表
 * @author qian.zhou
 */
public class MyStoreOrTakeoutFragment extends Fragment {
	
	private static final String TAG = MyStoreOrTakeoutFragment.class.getSimpleName();
	public static final String ORDER_TYPE = "orderType";
	public static final String ORDER_INPUT = "orderInput";
	/** 门店订单*/
	private ListView mLvStoreOrder;
	/** 适配器*/
	private MyStoreOrderTitleAdapter myStoreOrderTitleAdapter;
	/** 筛选*/
	private TextView mTvMsg;
	/**PopupWindow容器**/
	private PopupWindow mPopupWindow;
	/** 筛选输入框*/
	private EditText mEtPhone;
	/** 输入的搜索的内容*/
	private String mSearchWord;
	/** 时间数值*/
	private String mValue;
	/**系统当前日*/
	private String mDate;
	/**系统当前月*/
	private String mMonth;
	/**系统当前周*/
	private String mWeek;
	/** 时间单位*/
	private String mUnit;
	/** 订单的处理状态*/
	private String mStatus;
	/** 时间的改变*/
	private TextView mTvTimeSwitch;
	/** 表示用户选择的时间格式 (0 日  1 周   2 月)*/
	private String mTimeStatus;
	/** 订单类别（是外卖订单还是门店订单）*/
	private String mOrderType;
	/** 表示订单状态是否改变*/
	private boolean mUppFlag = false;
	/** 设置弹出层布局里面的控件*/
	private ViewGroup menuLayout;
	private ViewGroup menuLayoutContent;
	private ViewGroup menuLayoutTitle;
	/** 头顶标题*/
	private TextView mTvTitle;
	/** 正在加载数据*/
	private LinearLayout mLyNodate;
	/** 时间减*/
	private TextView mTvLess;
	/** 时间加*/
	private TextView mTvAdd;
	/** 查询*/
	private TextView mTvOrderNbr;
	/** “正在处理”进度条 */
	private ProgressDialog mProcessDialog = null;
	
	/**
	 * 传递参数有利于解耦
	 */
	public static MyStoreOrTakeoutFragment newInstance() {
		Bundle args = new Bundle();
		MyStoreOrTakeoutFragment fragment = new MyStoreOrTakeoutFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_store_order, container, false);
		ViewUtils.inject(this, v);
		Util.addLoginActivity(getActivity());
		//初始化视图
		initView(v);
		init(v);
		return v;
	}

	// 初始化数据
	private void init(View view) {
		//初始化数据
		initData(view);
		// 正在加载数据
		setData(0);
	}
	
	/**
	 * 初始化视图
	 */
	public void initView(View view){
		//标题
		LinearLayout ivBack = (LinearLayout) view.findViewById(R.id.layout_turn_in);// 返回
		ivBack.setVisibility(View.VISIBLE);// 显示
		mLyNodate = (LinearLayout) view.findViewById(R.id.ly_nodate);
		mTvTitle = (TextView) view.findViewById(R.id.tv_mid_content);
		//初始化数据
		mLvStoreOrder = (ListView) view.findViewById(R.id.lv_store_order_title);
		mTvMsg = (TextView) view.findViewById(R.id.tv_msg);//筛选
		//listview的头部
		View headView = getActivity().getLayoutInflater().inflate(R.layout.item_listview_head, null);
		mLvStoreOrder.addHeaderView(headView);
		//头部视图
		mTvLess = (TextView) headView.findViewById(R.id.tv_less);//减
		mTvAdd = (TextView) headView.findViewById(R.id.tv_add);//加
		mTvTimeSwitch = (TextView) headView.findViewById(R.id.tv_time_switch);
		mEtPhone = (EditText) headView.findViewById(R.id.et_ordernbr);
		mTvTimeSwitch.setTextColor(getActivity().getResources().getColor(R.color.red));
		mTvOrderNbr = (TextView)headView.findViewById(R.id.tv_ordernbr);
	}
	
	/**
	 * 初始化数据
	 */
	public void initData(View view){
		//取值
		Intent intent = getActivity().getIntent();
		mOrderType = intent.getStringExtra(ORDER_TYPE);
		String orderInput = intent.getStringExtra(ORDER_INPUT);
		mEtPhone.setText(Util.isEmpty(orderInput) ? "" : orderInput);
		if(!Util.isEmpty(mOrderType)){
			
			mTvTitle.setText("20".equals(mOrderType) ? R.string.store_order : R.string.outside_order);
		}
//		mLvStoreOrder.setPullLoadEnable(false); // 上拉刷新
//		mLvStoreOrder.setPullRefreshEnable(true);
//		// 实现xListviewListener接口
//		mLvStoreOrder.setXXListViewListener(this, ShopConst.PullRefresh.SHOP_ORDER_FAIL_LIST_PULL);
		//输入手机号码搜索事件
		
		//赋值
		mTvMsg.setText(R.string.selection);
		mTvMsg.setOnClickListener(selectListener);
		searchOrder(); // 手动输入查询
	    //默认为当前日期
	    mStatus = "0";
	    mTimeStatus = "0";
	    mValue = TimeFormatUtil.getNowTimeYMD(); // 当前时间
	    setEnClick(TimeFormatUtil.getNowTimeYMD()); // 下一天的点击事件
	    mUnit = "D"; // 默认参数
	    getStoreOrder();
	    //获得门店列表信息
	  //增加日期减掉日期的监听事件
		mTvLess.setOnClickListener(changeListener);
		mTvAdd.setOnClickListener(changeListener);
		mTvTimeSwitch.setOnClickListener(changeListener);
		mTvOrderNbr.setOnClickListener(changeListener);
		
	}
	
	/**
	 * 手动输入查查
	 */
	private void searchOrder () {
		mEtPhone.findFocus();
		mEtPhone.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					getStoreOrder();
				}
				return false;
			}
		});
	}
	
	/**
	 * 点击时间
	 */
	private void setEnClick (String cliclTime) {
		if (cliclTime.equals(TimeFormatUtil.getNowTimeYMD())) {
			mTvAdd.setEnabled(false);
			mTvAdd.setTextColor(getActivity().getResources().getColor(R.color.gray));
		} else {
			mTvAdd.setEnabled(true);
			mTvAdd.setTextColor(getActivity().getResources().getColor(R.color.dark_gray));
		}
	}
	
	/**
	 * 设置数据
	 * @param type 有没有数据 1 是有数据 0 是没有数据
	 */
	private void setData (int type) {
		if (type == 1) {
			mLyNodate.setVisibility(View.GONE);
			mLvStoreOrder.setVisibility(View.VISIBLE);
		} else {
			mLyNodate.setVisibility(View.VISIBLE);
			mLvStoreOrder.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 时间改变的事件
	 */
	private OnClickListener changeListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_less:// 减
				mValue = TimeFormatUtil.lessTime(mValue); 
				setTextColor(R.id.tv_less);
				break;
			case R.id.tv_add:// 加
				mValue = TimeFormatUtil.addTime(mValue);
				setTextColor(R.id.tv_add);
				break;
			case R.id.tv_time_switch:// 今天
				mValue = TimeFormatUtil.getNowTimeYMD(); 
				setTextColor(R.id.tv_time_switch);
				break;
			case R.id.tv_ordernbr: // 查询
				getStoreOrder();
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
		mUnit = "D";
		setEnClick(mValue);
		getStoreOrder(); 
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
	 * 筛选
	 */
	private OnClickListener selectListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			getPopupWindow();
		}
	};
	
	/**
	 * 弹出层
	 */
	public void getPopupWindow(){
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindow_selection_order, null);
		mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mPopupWindow.setFocusable(true);       
		mPopupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.showAtLocation(view, Gravity.RIGHT|Gravity.TOP, 0, 0);
		
		menuLayout = (ViewGroup)view.findViewById(R.id.menuLayout);
		menuLayoutContent = (ViewGroup)view.findViewById(R.id.ry_content);
		menuLayoutTitle = (ViewGroup)view.findViewById(R.id.ry_title);
		
		TextView TvCancle = (TextView) view.findViewById(R.id.tv_cancel);//取消
		TextView TvSelecTion = (TextView) view.findViewById(R.id.tv_selection);//筛选
		TextView TvSure = (TextView) view.findViewById(R.id.tv_sure);//确定
		TextView TvDate = (TextView) view.findViewById(R.id.tv_date);//日
		TextView TvWeek = (TextView) view.findViewById(R.id.tv_week);//周
		TextView TvMonth = (TextView) view.findViewById(R.id.tv_month);//月
		Button btnUntreated = (Button) view.findViewById(R.id.btn_untreated_order);//未处理
		Button btnPayment = (Button) view.findViewById(R.id.btn_payment_order);//待付款
		Button btnDeliveries = (Button) view.findViewById(R.id.btn_deliveries_order);//配送中
		Button btnTradSuccess = (Button) view.findViewById(R.id.btn_trading_success);//交易成功
		Button btnTradeCancel = (Button) view.findViewById(R.id.btn_trade_cancellation);//交易取消
		
		TvCancle.setOnClickListener(fastOrderListener);
		TvSelecTion.setOnClickListener(fastOrderListener);
		TvSure.setOnClickListener(fastOrderListener);
		TvDate.setOnClickListener(fastOrderListener);
		TvWeek.setOnClickListener(fastOrderListener);
		TvMonth.setOnClickListener(fastOrderListener);
		btnUntreated.setOnClickListener(fastOrderListener);
		btnPayment.setOnClickListener(fastOrderListener);
		btnDeliveries.setOnClickListener(fastOrderListener);
		btnTradSuccess.setOnClickListener(fastOrderListener);
		btnTradeCancel.setOnClickListener(fastOrderListener);
	}
	
	/**
	 *  0-未选择
	 * 	1-未处理；
		2-待付款；
		3-配送中；
		4-交易成功；
		5-交易取消；
	 */
	OnClickListener fastOrderListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int datecount = menuLayout.getChildCount();
			for(int i = 0; i < datecount ; i++){
				menuLayout.getChildAt(i).setSelected(false);
			}
			int contentcount = menuLayoutContent.getChildCount();
			for(int i = 0; i < contentcount ; i++){
				menuLayoutContent.getChildAt(i).setSelected(false);
			}
			int titlecount = menuLayoutTitle.getChildCount();
			for(int i = 0; i < titlecount ; i++){
				menuLayoutTitle.getChildAt(i).setSelected(false);
			}
			v.setSelected(true);
			switch (v.getId()) {
			case R.id.tv_cancel://取消
				mPopupWindow.dismiss();
				break;
			case R.id.tv_selection://筛选
				
				break;
			case R.id.tv_sure://确定
				getStoreOrder();
				mPopupWindow.dismiss();
				break;
			case R.id.tv_date://日
				mValue = mMonth + "-" + mDate;
				mUnit = "D";
				mTimeStatus = "0";
//				mTvTimeSwitch.setText(mValue);
				break;
			case R.id.tv_week://周
				mValue = mWeek;
				mUnit = "W";
				mTimeStatus = "1";
//				mTvTimeSwitch.setText(mValue);
				break;
			case R.id.tv_month://月
				mValue = mMonth;
				mUnit = "M";
				mTimeStatus = "2";
//				mTvTimeSwitch.setText(mValue);
				break;
			case R.id.btn_untreated_order://已下单
				mStatus = "20";
				break;
			case R.id.btn_payment_order://已接单
				mStatus = "21";
				break;
			case R.id.btn_deliveries_order://已派送
				mStatus = "22";
				break;
			case R.id.btn_trading_success://已送达
				mStatus = "23";
				break;
			case R.id.btn_trade_cancellation://已撤销
				mStatus = "24";
				break;
			default:
				break;
			}
		}
	};
	
	/**
	 * 查询门店订单列表
	 */
	public void getStoreOrder(){
		mSearchWord = TextUtils.isEmpty(mEtPhone.getText()) ? "" : mEtPhone.getText().toString();
		new GetNTakeoutOrderTask(getActivity(), new GetNTakeoutOrderTask.Callback() {
			@Override
			public void getResult(JSONArray result) {
				if (null != mProcessDialog) {
					mProcessDialog.dismiss();
				}
//				mLvStoreOrder.stopRefresh();
				if (result == null) {
					return;
				} else {
					setData(1); // 有数据
					// 商店详情
					List<MyOrderItem> list= new Gson().fromJson(result.toJSONString(), new TypeToken<List<MyOrderItem>>() {}.getType());
					myStoreOrderTitleAdapter = new MyStoreOrderTitleAdapter(getActivity(), list);
					mLvStoreOrder.setAdapter(myStoreOrderTitleAdapter);
				}
			}
		}).execute(mOrderType, mSearchWord, mValue, mUnit, mStatus);
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
	 * 添加fragment的跳转
	 * @param v
	 */
	@OnClick({ R.id.layout_turn_in})
	public void onClick(View v) {
		switch (v.getId()) {
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
		mUppFlag = DB.getBoolean(ShopConst.Key.UPP_ORDERSTATUS);
		if (mUppFlag) {
			DB.saveBoolean(ShopConst.Key.UPP_ORDERSTATUS,false);
			getStoreOrder();
		}
	}

}
