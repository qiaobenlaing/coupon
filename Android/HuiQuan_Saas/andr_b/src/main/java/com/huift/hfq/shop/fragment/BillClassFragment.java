package com.huift.hfq.shop.fragment;

import net.minidev.json.JSONObject;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Bill;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.activity.ScanActivity;
import com.huift.hfq.shop.model.GetBillStatisticsTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 账本的分类
 * @author wensi.yu
 *
 */
public class BillClassFragment extends Fragment{
	
	/** 标题的标示*/
	public static final String BILL_TITLE = "billTitle";
	public static final String BILL_MSG = "扫一扫";
	/** 扫描条形码的标志*/
	public static final int BAR_CODE = 101;
	/** 得到条形码成功的标志*/
	public static final int BAR_CODE_SUCC = 102;
	/** 得到的条形码的结果*/
	public static final String BAR_CODE_RESULT= "barCodeResult";
	public String IsFocus = String.valueOf(Util.NUM_ONE);
	/** 今天*/
	private TextView mTvBillToday;
	/** 最近一周*/
	private TextView mTvBillWeekday;
	/** 最近一月*/
	private TextView mTvBillMonth;
	/** 所有*/
	private TextView mBillAll;
	/** Fragment管理器 */
	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;
	private View view;
	/** 今天*/
	private BillTodayFragment mBillTodayFragment;
	/** 最近一周*/
	private BillWeekdayFragment mBillWeekdayFragment;
	/** 最近一月*/
	private BillMonthFragment mBillMonthFragment;
	/** 全部*/
	private BillAllFragment mBillAllFragment;
	/** 分类的标志*/
	private String mTypeFlag;
	/** 输入内容的控件*/
	private EditText mTvInput;
	/** 输入的内容*/
	private String mInput;
	/** 扫一扫*/
	private LinearLayout mTvMsg;
	/** 返回得到的条码的内容*/
	private String mBarCodeInfo;
	/** */
	private ImageView mMsg;
	
	public static BillClassFragment newInstance() {
		Bundle args = new Bundle();
		BillClassFragment fragment = new BillClassFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if (view != null) { return view; }
		view = inflater.inflate(R.layout.fragment_bill_class,container, false);
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
		// 标题
		LinearLayout ivTurnin = (LinearLayout) view.findViewById(R.id.layout_turn_in);
		ivTurnin.setVisibility(View.VISIBLE);
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_mid_content);
		mTvMsg = (LinearLayout) view.findViewById(R.id.layout_msg);//扫一扫
		mMsg = (ImageView) view.findViewById(R.id.tv_msg);
		displayClass(tvTitle);
		mTvBillToday = (TextView) view.findViewById(R.id.tv_today);//今天
		mTvBillWeekday = (TextView) view.findViewById(R.id.tv_weekday);//最近一周
		mTvBillMonth = (TextView) view.findViewById(R.id.tv_month);//最近一月
		mBillAll = (TextView) view.findViewById(R.id.tv_all);//所有
		mFragmentManager = getMyActivity().getFragmentManager();
		
		//默认显示今天
		setText(mTvBillToday);
		if(null == mBillTodayFragment){
			Bundle bundle = new Bundle();
			bundle.putString(BillTodayFragment.BILL_TYPE, mTypeFlag);
			mBillTodayFragment = BillTodayFragment.newInstance(bundle);
		}
		changeFragment(getMyActivity(), R.id.linear_bill_content,mBillTodayFragment);
		
		//默认进来
		getBillStatistics (String.valueOf(Util.NUM_ONE));
				
	}
	
	/**
	 * 显示标题
	 */
	private void displayClass(TextView tvTitle) {
		mTypeFlag = getMyActivity().getIntent().getStringExtra(BILL_TITLE);
		LinearLayout lyBillDate = (LinearLayout) view.findViewById(R.id.ly_bill_date);//分类的日期
		LinearLayout lyBillPlay = (LinearLayout) view.findViewById(R.id.ly_bill_billpay);//清算
		LinearLayout lyBillSubsidy = (LinearLayout) view.findViewById(R.id.ly_bill_subsidy);//补贴
		RelativeLayout rlBillInput = (RelativeLayout) view.findViewById(R.id.rl_bill_input);//查询
		
		LinearLayout rlBillRefund = (LinearLayout) getActivity().findViewById(R.id.rlBill_refund);
		LinearLayout rlBillMonetary = (LinearLayout) getActivity().findViewById(R.id.ly_billclass_monetary);//消费金额
		LinearLayout rlBillRefundone = (LinearLayout) getActivity().findViewById(R.id.ly_billclass_refund);//退款
		LinearLayout rlBillSubsidy = (LinearLayout) getActivity().findViewById(R.id.ly_billclass_subsidy);//补贴
		LinearLayout rlBillTotal = (LinearLayout) getActivity().findViewById(R.id.ly_bill_total);
		LinearLayout lyBillPay = (LinearLayout) getActivity().findViewById(R.id.rlBill_pay);
		LinearLayout lyBillSubsidyOne = (LinearLayout) getActivity().findViewById(R.id.ly_bill_subsidy);//补贴金额
		
		mTvInput = (EditText) view.findViewById(R.id.et_bill_input);//输入的内容
		TextView tvSelect = (TextView) view.findViewById(R.id.tv_bill_select);//查询
		tvSelect.setOnClickListener(selectClick);
		
		if (String.valueOf(Util.NUM_ONE).equals(mTypeFlag)) {//顾客清单
			tvTitle.setText(R.string.tv_billclass_one);
			LinearLayout lyBillCust = (LinearLayout) getActivity().findViewById(R.id.rlBill_cust);
			lyBillCust.setVisibility(View.VISIBLE);
			lyBillPay.setVisibility(View.VISIBLE);
			
		} else if (String.valueOf(Util.NUM_TWO).equals(mTypeFlag)) {//退款清单
			tvTitle.setText(R.string.tv_billclass_two);
			rlBillRefund.setVisibility(View.VISIBLE);
			rlBillMonetary.setVisibility(View.VISIBLE);
			rlBillRefundone.setVisibility(View.VISIBLE);
			
		} else if (String.valueOf(Util.NUM_THIRD).equals(mTypeFlag)) {//消费未结算账单
			tvTitle.setText(R.string.tv_billclass_three);
			rlBillRefund.setVisibility(View.VISIBLE);
			rlBillMonetary.setVisibility(View.VISIBLE);
			
		} else if (String.valueOf(Util.NUM_FOUR).equals(mTypeFlag)) {//补贴未结算账单
			tvTitle.setText(R.string.tv_billclass_four);
			rlBillRefund.setVisibility(View.VISIBLE);
			rlBillMonetary.setVisibility(View.VISIBLE);
			rlBillSubsidy.setVisibility(View.VISIBLE);
			
		} else if (String.valueOf(Util.NUM_FIVE).equals(mTypeFlag)) {//支付结算对账
			tvTitle.setText(R.string.tv_billclass_five);
			rlBillTotal.setVisibility(View.VISIBLE);
			lyBillDate.setVisibility(View.GONE);
			lyBillPlay.setVisibility(View.VISIBLE);
			
		} else if (String.valueOf(Util.NUM_SIX).equals(mTypeFlag)) {//补贴结算对账
			tvTitle.setText(R.string.tv_billclass_six);
			rlBillTotal.setVisibility(View.VISIBLE);
			lyBillDate.setVisibility(View.GONE);
			lyBillSubsidy.setVisibility(View.VISIBLE);
			lyBillSubsidyOne.setVisibility(View.VISIBLE);
			
		} else if (String.valueOf(Util.NUM_SEVEN).equals(mTypeFlag)) {//账单查询
			tvTitle.setText(R.string.tv_billclass_select);
			rlBillTotal.setVisibility(View.VISIBLE);
			rlBillInput.setVisibility(View.VISIBLE);
			mTvMsg.setVisibility(View.VISIBLE);
			mMsg.setVisibility(View.VISIBLE);
			mTvMsg.setOnClickListener(msgClick);//扫一扫的点击事件
			
		}
		
	}
	
	/**
	 * 扫一扫
	 */
	private OnClickListener msgClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getMyActivity(), ScanActivity.class);
			intent.putExtra(ScanActivity.FLAG, String.valueOf(Util.NUM_ONE));
			startActivityForResult(intent, BAR_CODE);
			
		}

	};
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == BAR_CODE) {
			if (resultCode == BAR_CODE_SUCC) {
				mBarCodeInfo = data.getStringExtra(BAR_CODE_RESULT);
				mTvInput.setText(!Util.isEmpty(mBarCodeInfo) ? mBarCodeInfo: "");
			}
		}
	};
	
	/**
	 * 查询
	 */
	private OnClickListener selectClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (!Util.isEmpty(mTvInput.getText().toString())) {
				mInput = mTvInput.getText().toString();
			} else {
				mInput = "";
			}
		}
	};
	
	/**
	 * 获得统计信息
	 */
	private void getBillStatistics (final String flag) {
		//顾客清单
		final TextView tvConsumnumber = (TextView) getActivity().findViewById(R.id.tv_consumnumber);
		final TextView tvConsummoney = (TextView) getActivity().findViewById(R.id.tv_consummoney);
		final TextView tvPaymoney = (TextView) getActivity().findViewById(R.id.tv_paymoney);
		final TextView tvDiscountmoney = (TextView) getActivity().findViewById(R.id.tv_discountmoney);
		//退款清单 消费未结算账单  补贴未结算账单
		final TextView tvMonetary = (TextView) getActivity().findViewById(R.id.tv_monetary);
		final TextView tvRefund = (TextView) getActivity().findViewById(R.id.tv_refund);
		final TextView tvSubsidyOne = (TextView) getActivity().findViewById(R.id.tv_billclass_subsidy);
		//支付结算对账
		final TextView tvTotal= (TextView) getActivity().findViewById(R.id.tv_total);
		final TextView tvConsummoneyone= (TextView) getActivity().findViewById(R.id.tv_consummoneyone);
		final TextView tvPay= (TextView) getActivity().findViewById(R.id.tv_pay);
		final TextView tvDate= (TextView) view.findViewById(R.id.tv_bill_date);
		final TextView tvPlatform= (TextView) view.findViewById(R.id.tv_bill_platform);
		//补贴结算对账
		final TextView tvSubsidy = (TextView) getActivity().findViewById(R.id.tv_subsidy);
		final TextView tvSubsidydata = (TextView) view.findViewById(R.id.tv_bill_subsidydata);
		final TextView tvSubsidymoney = (TextView) view.findViewById(R.id.tv_bill_subsidymoney);
		
		new GetBillStatisticsTask(getMyActivity(), new GetBillStatisticsTask.Callback() {
			
			@Override
			public void getResult(JSONObject result) {
				
				if (null == result) {
					return ;
				}
				
				Bill  bill = Util.json2Obj(result.toString(), Bill.class);
				
				String billDate = null;
				if ((String.valueOf(Util.NUM_ONE)).equals(mTypeFlag)) {
					tvConsumnumber.setText(!Util.isEmpty(bill.getConsumptionNbr()) ? bill.getConsumptionNbr() + "次": "");
					tvConsummoney.setText(!Util.isEmpty(bill.getConsumptionAmount()) ? bill.getConsumptionAmount() + "元" : "");
					tvPaymoney.setText(!Util.isEmpty(bill.getPayAmount()) ? bill.getPayAmount() + "元": "");
					tvDiscountmoney.setText(!Util.isEmpty(bill.getDiscountAmount()) ? bill.getDiscountAmount() + "元": "");
					
				} else if ((String.valueOf(Util.NUM_TWO)).equals(mTypeFlag)) {
					tvMonetary.setText(!Util.isEmpty(bill.getConsumptionAmount()) ? bill.getConsumptionAmount() + "元": "");
					tvRefund.setText(!Util.isEmpty(bill.getRefundAmount()) ? bill.getRefundAmount() + "元": "");
					
				} else if ((String.valueOf(Util.NUM_THIRD)).equals(mTypeFlag)) {
					tvMonetary.setText(!Util.isEmpty(bill.getConsumptionAmount()) ? bill.getConsumptionAmount() + "元": "");
					
				} else if ((String.valueOf(Util.NUM_FOUR)).equals(mTypeFlag)) {
					tvMonetary.setText(!Util.isEmpty(bill.getConsumptionAmount()) ? bill.getConsumptionAmount() + "元": "");
					tvSubsidyOne.setText(!Util.isEmpty(bill.getSubsidyAmount()) ? bill.getSubsidyAmount() + "元": "");
					
				} else if ((String.valueOf(Util.NUM_FIVE)).equals(mTypeFlag)) {
					tvTotal.setText(!Util.isEmpty(bill.getConsumptionNbr()) ? bill.getConsumptionNbr() + "次": "");
					tvConsummoneyone.setText(!Util.isEmpty(bill.getConsumptionAmount()) ? bill.getConsumptionAmount() + "元": "");
					tvPay.setText(!Util.isEmpty(bill.getPayAmount()) ? bill.getPayAmount() + "元": "");
					billDate = bill.getStartDate() + bill.getEndDate();
					tvDate.setText(!Util.isEmpty(billDate) ? "从" + bill.getStartDate() + "至" +bill.getEndDate(): "");
					tvPlatform.setText(!Util.isEmpty(bill.getPayAmount()) ? bill.getPayAmount() + "元": "");
					
				} else if ((String.valueOf(Util.NUM_SIX)).equals(mTypeFlag)) {
					tvTotal.setText(!Util.isEmpty(bill.getConsumptionNbr()) ? bill.getConsumptionNbr() + "次": "");
					tvConsummoneyone.setText(!Util.isEmpty(bill.getConsumptionAmount()) ? bill.getConsumptionAmount() + "元": "");
					tvPay.setText(!Util.isEmpty(bill.getPayAmount()) ? bill.getPayAmount() + "元": "");
					tvSubsidy.setText(!Util.isEmpty(bill.getSubsidyAmount()) ? bill.getSubsidyAmount() + "元": "");
					billDate = bill.getStartDate() + bill.getEndDate();
					tvSubsidydata.setText(!Util.isEmpty(billDate) ? "从" + bill.getStartDate() + "至" +bill.getEndDate(): "");
					tvSubsidymoney.setText(!Util.isEmpty(bill.getSubsidyAmount()) ? bill.getSubsidyAmount() + "元": "");
				
				} else if ((String.valueOf(Util.NUM_SEVEN)).equals(mTypeFlag)) {
					tvTotal.setText(!Util.isEmpty(bill.getConsumptionNbr()) ? bill.getConsumptionNbr() + "次": "");
					tvConsummoneyone.setText(!Util.isEmpty(bill.getConsumptionAmount()) ? bill.getConsumptionAmount() + "元": "");
					tvPay.setText(!Util.isEmpty(bill.getPayAmount()) ? bill.getPayAmount() + "元": "");
				}
				
			}
		}).execute(flag,mTypeFlag,mInput);
	}

	/**
	 * 更换页面
	 * @param id
	 * @param fragment
	 */
	public void changeFragment(Activity activity, int id, Fragment fragment) {
		if (null != activity) {
			mFragmentManager = activity.getFragmentManager();
			mFragmentTransaction = mFragmentManager.beginTransaction();
			mFragmentTransaction.replace(id, fragment);
			mFragmentTransaction.addToBackStack(null);
			mFragmentTransaction.commit();
		}
	}
	
	/**
	 * 添加fragment的跳转
	 * @param v
	 */
	@OnClick({ R.id.tv_today, R.id.tv_weekday, R.id.tv_month, R.id.tv_all})
	public void onClick(View v) {
		setAllText();
		switch (v.getId()) {
		case R.id.tv_today://今天
			if(null == mBillTodayFragment){
				Bundle bundle = new Bundle();
				bundle.putString(BillTodayFragment.BILL_TYPE, mTypeFlag);
				mBillTodayFragment = BillTodayFragment.newInstance(bundle);
			}
			IsFocus = String.valueOf(Util.NUM_ONE);
			setText(mTvBillToday);
			changeFragment(getMyActivity(), R.id.linear_bill_content,mBillTodayFragment);
			getBillStatistics(String.valueOf(Util.NUM_ONE));
			break;
		case R.id.tv_weekday:// 最近一周
			if(null == mBillWeekdayFragment){
				Bundle bundle = new Bundle();
				bundle.putString(BillWeekdayFragment.BILL_TYPE, mTypeFlag);
				mBillWeekdayFragment = BillWeekdayFragment.newInstance(bundle);
			}
			changeFragment(getMyActivity(), R.id.linear_bill_content, mBillWeekdayFragment);
			IsFocus = String.valueOf(Util.NUM_ZERO);
			setText(mTvBillWeekday);
			getBillStatistics(String.valueOf(Util.NUM_TWO));
			break;
		case R.id.tv_month://最近一月
			if(null == mBillMonthFragment){
				Bundle bundle = new Bundle();
				bundle.putString(BillMonthFragment.BILL_TYPE, mTypeFlag);
				mBillMonthFragment = BillMonthFragment.newInstance(bundle);
			}
			changeFragment(getMyActivity(), R.id.linear_bill_content, mBillMonthFragment);
			IsFocus = String.valueOf(Util.NUM_ZERO);
			setText(mTvBillMonth);
			getBillStatistics(String.valueOf(Util.NUM_THIRD));
			break;
		case R.id.tv_all://所有
			if(null == mBillAllFragment){
				Bundle bundle = new Bundle();
				bundle.putString(BillAllFragment.BILL_TYPE, mTypeFlag);
				mBillAllFragment = BillAllFragment.newInstance(bundle);
			}
			changeFragment(getMyActivity(), R.id.linear_bill_content, mBillAllFragment);
			IsFocus = String.valueOf(Util.NUM_ZERO);
			setText(mBillAll);
			getBillStatistics(String.valueOf(Util.NUM_FOUR));
			break;
		default:
			break;
		}
	}
	
	/**
	 * 设置所有
	 */
	private void setAllText() {
		mTvBillToday.setEnabled(true);
		mTvBillWeekday.setEnabled(true);
		mTvBillMonth.setEnabled(true);
		mBillAll.setEnabled(true);
		// 设置字体
		mTvBillToday.setTextColor(getResources().getColor(R.color.gray));
		mTvBillWeekday.setTextColor(getResources().getColor(R.color.gray));
		mTvBillMonth.setTextColor(getResources().getColor(R.color.gray));
		mBillAll.setTextColor(getResources().getColor(R.color.gray));
		// 设置背景
		mTvBillToday.setBackgroundResource(0);
		mTvBillWeekday.setBackgroundResource(0);
		mTvBillMonth.setBackgroundResource(0);
		mBillAll.setBackgroundResource(0);
	}
	
	/**
	 * 设置控件
	 * @param textView
	 */
	private void setText(TextView textView) {
		textView.setEnabled(false);
		textView.setTextColor(getActivity().getResources().getColor(R.color.red));
		textView.setBackgroundResource(R.drawable.bottom_red_border);
	}
	
	/**
	 * 返回
	 */
	@OnClick(R.id.layout_turn_in)
	public void trunBack(View view){
		getMyActivity().finish();
	}
}
