package com.huift.hfq.shop.fragment;

import net.minidev.json.JSONObject;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.huift.hfq.base.utils.DateUtils;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

/**
 * 账本的分类
 * @author wensi.yu
 *
 */
public class BillClassFragment extends Fragment{
	
	private static final String TAG = "BillClassFragment";
	/** 标题的标示*/
	public static final String BILL_TITLE = "billTitle";
	/** 扫描条形码的标志*/
	public static final int BAR_CODE = 101;
	/** 得到条形码成功的标志*/
	public static final int BAR_CODE_SUCC = 102;
	/** 得到的条形码的结果*/
	public static final String BAR_CODE_RESULT= "barCodeResult";
	public String IsFocus = String.valueOf(Util.NUM_ONE);
	/** Fragment管理器 */
	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;
	private View view;
	/** 今天*/
	private BillTodayFragment mBillTodayFragment;
	/** 分类的标志*/
	private String mTypeFlag;
	/** 输入内容的控件*/
	private EditText mTvInput;
	/** 扫一扫*/
	private LinearLayout mTvMsg;
	/** 返回得到的条码的内容*/
	private String mBarCodeInfo;
	/** */
	private ImageView mMsg;
	private Button b_startDate,b_endDate;
	
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
		mFragmentManager = getMyActivity().getFragmentManager();
		
		if(null == mBillTodayFragment){
			Bundle bundle = new Bundle();
			bundle.putString(BillTodayFragment.BILL_TYPE, mTypeFlag);
			mBillTodayFragment = BillTodayFragment.newInstance(bundle);
		}
		changeFragment(getMyActivity(), R.id.linear_bill_content,mBillTodayFragment);
		
	}
	
	/**
	 * 显示标题
	 */
	private void displayClass(TextView tvTitle) {
		mTypeFlag = getMyActivity().getIntent().getStringExtra(BILL_TITLE);
		Log.d(TAG, "mTypeFlag == " +mTypeFlag);
		LinearLayout lyBillPlay = (LinearLayout) view.findViewById(R.id.ly_bill_billpay);//清算
		LinearLayout lyBillSubsidy = (LinearLayout) view.findViewById(R.id.ly_bill_subsidy);//补贴

		LinearLayout rlBillRefund = (LinearLayout) getActivity().findViewById(R.id.rlBill_refund);
		LinearLayout rlBillMonetary = (LinearLayout) getActivity().findViewById(R.id.ly_billclass_monetary);//消费金额
		LinearLayout rlBillRefundone = (LinearLayout) getActivity().findViewById(R.id.ly_billclass_refund);//退款
		LinearLayout rlBillSubsidy = (LinearLayout) getActivity().findViewById(R.id.ly_billclass_subsidy);//补贴
		LinearLayout rlBillTotal = (LinearLayout) getActivity().findViewById(R.id.ly_bill_total);
		LinearLayout lyBillPay = (LinearLayout) getActivity().findViewById(R.id.rlBill_pay);
		LinearLayout lyBillSubsidyOne = (LinearLayout) getActivity().findViewById(R.id.ly_bill_subsidy);//补贴金额
		
		mTvInput = (EditText) view.findViewById(R.id.et_bill_input);//输入的内容
        b_startDate=view.findViewById(R.id.b_startDate);
        b_endDate=view.findViewById(R.id.b_endDate);
        b_startDate.setText(DateUtils.getDateString("yyyy-MM-dd"));
        b_endDate.setText(DateUtils.getDateString("yyyy-MM-dd"));

        b_startDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				chooseDate("开始时间","2000-01-01",b_startDate.getText().toString(),b_endDate.getText().toString());
			}
		});
        b_endDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				chooseDate("结束时间",b_startDate.getText().toString(),b_endDate.getText().toString(),DateUtils.getDateString("yyyy-MM-dd"));
			}
		});

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
			lyBillPlay.setVisibility(View.VISIBLE);
			
		} else if (String.valueOf(Util.NUM_SIX).equals(mTypeFlag)) {//补贴结算对账
			tvTitle.setText(R.string.tv_billclass_six);
			rlBillTotal.setVisibility(View.VISIBLE);
			lyBillSubsidy.setVisibility(View.VISIBLE);
			lyBillSubsidyOne.setVisibility(View.VISIBLE);
			
		} else if (String.valueOf(Util.NUM_SEVEN).equals(mTypeFlag)) {//账单查询
			tvTitle.setText(R.string.tv_billclass_select);
			rlBillTotal.setVisibility(View.VISIBLE);
			mTvMsg.setVisibility(View.VISIBLE);
			mMsg.setVisibility(View.VISIBLE);
			mTvMsg.setOnClickListener(msgClick);//扫一扫的点击事件
			
		}
		
	}

	private void chooseDate(final String title, String startDate, String defaultDate, String endDate){
		String [] def=defaultDate.split("-");
		String [] start=startDate.split("-");
		String [] end=endDate.split("-");
		Calendar defaultCalender = Calendar.getInstance();
		defaultCalender.set(Integer.parseInt(def[0]), Integer.parseInt(def[1])-1, Integer.parseInt(def[2]));
		Calendar startCalender = Calendar.getInstance();
		startCalender.set(Integer.parseInt(start[0]), Integer.parseInt(start[1])-1, Integer.parseInt(start[2]));
		Calendar endCalender = Calendar.getInstance();
		endCalender.set(Integer.parseInt(end[0]), Integer.parseInt(end[1])-1, Integer.parseInt(end[2]));
		//时间选择器
		TimePickerView pvTime = new TimePickerBuilder(getMyActivity(), new OnTimeSelectListener() {
			@Override
			public void onTimeSelect(Date date, View v) {
				if (title.equals("开始时间")){
					b_startDate.setText(DateUtils.dateToString(date,"yyyy-MM-dd"));
				}else {
					b_endDate.setText(DateUtils.dateToString(date,"yyyy-MM-dd"));
				}

			}
		}).setRangDate(startCalender,endCalender)
				.setDate(defaultCalender)
				.setTitleText(title)
				.build();
		pvTime.show();
	}
	
	/**
	 * 扫一扫
	 */
	private OnClickListener msgClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Log.d(TAG, "点击了扫一扫");
			Intent intent = new Intent(getMyActivity(), ScanActivity.class);
			intent.putExtra(ScanActivity.FLAG, String.valueOf(Util.NUM_ONE));
			startActivityForResult(intent, BAR_CODE);
			
		}

	};
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "data == " + data);
		if (requestCode == BAR_CODE) {
			if (resultCode == BAR_CODE_SUCC) {
				mBarCodeInfo = data.getStringExtra(BAR_CODE_RESULT);
				Log.d(TAG, "mBarCodeInfo == " + mBarCodeInfo);
				mTvInput.setText(!Util.isEmpty(mBarCodeInfo) ? mBarCodeInfo: "");
			}
		}
	};

	/**
	 * 获得统计信息
	 */
	public void getBillStatistics () {
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
		final TextView tv_useCount= (TextView) getActivity().findViewById(R.id.tv_useCount);
		final TextView tv_useMoney= (TextView) getActivity().findViewById(R.id.tv_useMoney);
		final TextView tv_totalCount= (TextView) getActivity().findViewById(R.id.tv_totalCount);
		final TextView tv_totalMoney= (TextView) getActivity().findViewById(R.id.tv_totalMoney);
		final TextView tvConsummoneyone= (TextView) getActivity().findViewById(R.id.tv_consummoneyone);
		final TextView tvDate= (TextView) view.findViewById(R.id.tv_bill_date);
		final TextView tvPlatform= (TextView) view.findViewById(R.id.tv_bill_platform);
		//补贴结算对账
		final TextView tvSubsidy = (TextView) getActivity().findViewById(R.id.tv_subsidy);
		final TextView tvSubsidydata = (TextView) view.findViewById(R.id.tv_bill_subsidydata);
		final TextView tvSubsidymoney = (TextView) view.findViewById(R.id.tv_bill_subsidymoney);

		String timeLimit=b_startDate.getText().toString().replaceAll("-","")+"_"+b_endDate.getText().toString().replaceAll("-","");

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
					tv_totalCount.setText(!Util.isEmpty(bill.getConsumptionNbr()) ? bill.getConsumptionNbr() + "笔": "");
					tv_totalMoney.setText(!Util.isEmpty(bill.getConsumptionAmount()) ? bill.getConsumptionAmount() + "元": "");
					tvConsummoneyone.setText(!Util.isEmpty(bill.getPayAmount()) ? bill.getPayAmount() + "元": "");
					billDate = bill.getStartDate() + bill.getEndDate();
					tvDate.setText(!Util.isEmpty(billDate) ? "从" + bill.getStartDate() + "至" +bill.getEndDate(): "");
					tvPlatform.setText(!Util.isEmpty(bill.getPayAmount()) ? bill.getPayAmount() + "元": "");

				} else if ((String.valueOf(Util.NUM_SIX)).equals(mTypeFlag)) {
					tv_totalCount.setText(!Util.isEmpty(bill.getConsumptionNbr()) ? bill.getConsumptionNbr() + "笔": "");
					tv_totalMoney.setText(!Util.isEmpty(bill.getConsumptionAmount()) ? bill.getConsumptionAmount() + "元": "");
					tvConsummoneyone.setText(!Util.isEmpty(bill.getPayAmount()) ? bill.getPayAmount() + "元": "");
					tvSubsidy.setText(!Util.isEmpty(bill.getSubsidyAmount()) ? bill.getSubsidyAmount() + "元": "");
					billDate = bill.getStartDate() + bill.getEndDate();
					tvSubsidydata.setText(!Util.isEmpty(billDate) ? "从" + bill.getStartDate() + "至" +bill.getEndDate(): "");
					tvSubsidymoney.setText(!Util.isEmpty(bill.getSubsidyAmount()) ? bill.getSubsidyAmount() + "元": "");

				} else if ((String.valueOf(Util.NUM_SEVEN)).equals(mTypeFlag)) {
					tv_useCount.setText(!Util.isEmpty(bill.getCouponHasUsed()) ? bill.getCouponHasUsed() + "笔": "");
					tv_useMoney.setText(!Util.isEmpty(bill.getHasUsedMoney()) ? bill.getHasUsedMoney() + "元": "");
					tv_totalCount.setText(!Util.isEmpty(bill.getConsumptionNbr()) ? bill.getConsumptionNbr() + "笔": "");
					tv_totalMoney.setText(!Util.isEmpty(bill.getConsumptionAmount()) ? bill.getConsumptionAmount() + "元": "");
					tvConsummoneyone.setText(!Util.isEmpty(bill.getPayAmount()) ? bill.getPayAmount() + "元": "");
				}

			}
		}).execute(timeLimit,mTypeFlag,mTvInput.getText().toString());
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
	 * 返回
	 */
	@OnClick(R.id.layout_turn_in)
	public void trunBack(View view){
		getMyActivity().finish();
	}
}
