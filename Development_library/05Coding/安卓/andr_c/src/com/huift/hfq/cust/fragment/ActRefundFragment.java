package com.huift.hfq.cust.fragment;

import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.adapter.CommenViewHolder;
import com.huift.hfq.base.adapter.CommonListViewAdapter;
import com.huift.hfq.base.pojo.Refund;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.view.MyListView;
import com.huift.hfq.cust.R;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huift.hfq.cust.model.ActCodeApplyRefundTask;
import com.huift.hfq.cust.model.GetUserActCodeInfoTask;
import com.huift.hfq.cust.util.ActUtils;
import com.huift.hfq.cust.util.CommUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 活动退款
 * @author yanfang.li
 */
public class ActRefundFragment extends Fragment {
	private static final String TAG = ActRefundFragment.class.getSimpleName();
	/** 活动编码 */
	public static final String ACT_CODE = "actCode";
	/** 显示优惠券 */
	private static final int SHOW = 1;
	/** 未付款，不可用 */
	public static final int STATUS_NO_PAY = 0;
	/** 已申请退款 */
	public static final int ALREADY_APPLIED = 1;
	/** 已退款 */
	public static final int REFUND = 2;
	/** 已验证 */
	public static final int VERIFIED = 3;
	/** 正在申请 */
	public static final int PENDING = 4;
	/** 退款金额 */
	private CheckBox mCkbArrow;
	/** 退款金额详情界面 */
	private LinearLayout mRlOrderInfo;
	/** 用户活动编码 */
	private String mActCode;
	/** 退款的状态 */
	private TextView mTvRefundStatus;
	/** 退款的原因 */
	private MyListView mLvReason;
	/** 下一步*/
	private Button mBtnNext;
	/** 其他原因*/
	private EditText mEdtRefundOther;
	/** 刷新提示*/
	private ProgressBar mProgNodata;
	/** 原因的集合*/
	private List<Refund> mListReason;
	/** 第一次运行*/
	private boolean mFirstFlag = true;
	/** 视图*/
	private View mView;
	/** 退款对象*/
	private Refund mRefund;
	
	/**
	 * 需要传递参数时有利于解耦
	 */
	public static ActRefundFragment newInstance() {
		Bundle args = new Bundle();
		ActRefundFragment fragment = new ActRefundFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_act_refund, container, false);
		ViewUtils.inject(this, mView);
		findView();
		initData();
		return mView;
	}

	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	/**
	 * 查找控件
	 */
	private void findView() {
		mCkbArrow = (CheckBox) mView.findViewById(R.id.ckb_arrow);
		mLvReason = (MyListView) mView.findViewById(R.id.lv_refund);
		mRlOrderInfo = (LinearLayout) mView.findViewById(R.id.rl_order_info);
		mProgNodata = (ProgressBar)mView.findViewById(R.id.prog_nodata);
		mBtnNext = (Button)mView.findViewById(R.id.btn_refund);
		mBtnNext.setEnabled(false); // 要选中退款原因才可以点击
		mCkbArrow.setOnCheckedChangeListener(chbCheckListener);
		mEdtRefundOther = (EditText)mView.findViewById(R.id.edt_refund_other);
	}

	/**
	 * 活动退款详情
	 */
	private void getUserActCodeInfo() {
		if (mFirstFlag) {
			mProgNodata.setVisibility(View.VISIBLE);
		}
		new GetUserActCodeInfoTask(getMyActivity(), new GetUserActCodeInfoTask.Callback() {

			@Override
			public void getResult(JSONObject result) {
				mFirstFlag = false;
				mProgNodata.setVisibility(View.GONE);
				if (null != result) {
					mRefund = new Gson().fromJson(result.toString(), new TypeToken<Refund>() {
					}.getType());
					if (null != mRefund) {
						setControlAssignment(mRefund);
					}
				}
			}
		}).execute(mActCode);
	}

	/**
	 * 给控件赋值
	 * 
	 * @param refund
	 */
	private void setControlAssignment(final Refund refund) {
		mTvRefundStatus = (TextView) mView.findViewById(R.id.tv_refund_status); // 退款状态
		TextView tvRefundExplan = (TextView) mView.findViewById(R.id.tv_refund_explan); // 退款说明
		TextView tvOrderCode = (TextView) mView.findViewById(R.id.tv_order_code); // 订单编码
		TextView tvTelComplaints = (TextView) mView.findViewById(R.id.tv_tel_complaints); // 投诉电话
		TextView tvRefundPrice = (TextView) mView.findViewById(R.id.tv_refund_price); // 退款金额
		TextView tvBankPrice = (TextView) mView.findViewById(R.id.tv_bank_price); // 工行卡的金额
		TextView tvHuiquanBouns = (TextView) mView.findViewById(R.id.tv_huiquan_bouns); // 惠圈红包
		TextView tvShopBouns = (TextView) mView.findViewById(R.id.tv_shop_bouns); // 商家红包
		setReFundStatus(refund.getStatus()); // 申请退款的状态
		// 订单编码
		tvOrderCode.setText(Util.getString(R.string.refund_order_code) + refund.getOrderNbr());
		// 退款金额
		tvRefundPrice.setText(ActUtils.formatPrice(String.valueOf(refund.getToRefundAmount())) + "元");
		tvBankPrice.setText(ActUtils.formatPrice(String.valueOf(refund.getBankcardRefund())) + "元");
		tvHuiquanBouns.setText(ActUtils.formatPrice(String.valueOf(refund.getPlatBonusRefund())) + "元");
		tvShopBouns.setText(ActUtils.formatPrice(String.valueOf(refund.getShopBonusRefund())) + "元");
		// 退款说明
		tvRefundExplan.setText(Util.getString(R.string.refund_explain) + refund.getRefundExplain());
		// 电话号码
		tvTelComplaints.setText(Util.getString(R.string.tel_complaints) + refund.getHotLine());
		// 备注
		mEdtRefundOther.setText(Util.isEmpty(refund.getRefundRemark()) ? "" : refund.getRefundRemark());
		// 退款原因
		mListReason = refund.getSltReason();
		if (null != mListReason && mListReason.size() > 0) {
			for (int i = 0; i < refund.getSltReason().size(); i++) {
				Refund reason = refund.getSltReason().get(i);
				reason.setStatus(refund.getStatus());
			}
			ActReasonAdapter actReasonAdapter = new ActReasonAdapter(getMyActivity(), refund.getSltReason());
			mLvReason.setAdapter(actReasonAdapter);
		}
		// 拨打电话
		tvTelComplaints.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommUtils.takePhone(refund.getHotLine());
			}
		});
	}

	/**
	 * 退款状态
	 * @param statusType 退款的状态
	 */
	private void setReFundStatus(int statusType) {
		String statusTypeString = "";
		switch (statusType) {
		case ALREADY_APPLIED: // 已申请退款
			statusTypeString = Util.getString(R.string.already_applied);
			break;
		case REFUND: // 已退款
			statusTypeString = Util.getString(R.string.act_refund);
			break;
		case PENDING: // 正在申请
			statusTypeString = Util.getString(R.string.pending);
			break;

		default:
			statusTypeString = Util.getString(R.string.refund_status);
			break;
		}
		mTvRefundStatus.setText(statusTypeString);
	}

	/**
	 * CheckBox的选中事件
	 */
	private OnCheckedChangeListener chbCheckListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			switch (buttonView.getId()) {
			case R.id.ckb_arrow: // 查看退款金额详细信息
				if (isChecked) {
					mRlOrderInfo.setVisibility(View.VISIBLE);
					mCkbArrow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.up_arrow, 0, 0, 0);
				} else {
					mRlOrderInfo.setVisibility(View.GONE);
					mCkbArrow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0);
				}
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 初始化数据
	 */
	private void initData() {
		mActCode = getMyActivity().getIntent().getStringExtra(ACT_CODE);
		getUserActCodeInfo();
	}

	/**
	 * 原因适配器
	 * @author liyanfang
	 * 
	 */
	class ActReasonAdapter extends CommonListViewAdapter<Refund> {

		public ActReasonAdapter(Activity activity, List<Refund> datas) {
			super(activity, datas);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView, parent, R.layout.item_refund_reason,
					position);
			final Refund refund = mDatas.get(position);
			final ImageView ivPromotionIcon = holder.getView(R.id.iv_radio); // 选中图片
			TextView tvRefundTxt = holder.getView(R.id.tv_refund_txt); // 退款内容
			tvRefundTxt.setText(Util.isEmpty(refund.getText()) ? "位置" : refund.getText());
			View view = holder.getConvertView();
			// 是否选中
			if (refund.getSelected() == SHOW) {
				ivPromotionIcon.setBackgroundResource(R.drawable.radio_yes);
			} else {
				ivPromotionIcon.setBackgroundResource(R.drawable.radio_no);
			}
			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (refund.getSelected() == SHOW) {
						refund.setSelected(0);
						ivPromotionIcon.setBackgroundResource(R.drawable.radio_no);
					} else {
						cleanOtherSelectItems(refund);
						ivPromotionIcon.setImageResource(R.drawable.radio_yes);
						refund.setSelected(1);
					}
					notifyDataSetChanged();
				}
			});
			// 判断是否被选中
			if (refund.getStatus() != PENDING) {
				view.setEnabled(false);
				mBtnNext.setVisibility(View.GONE);
			} else {
				view.setEnabled(true);
				mBtnNext.setVisibility(View.VISIBLE);
				for (int i = 0; i < mDatas.size(); i++) {
					Refund ref = mDatas.get(i);
					if (ref.getSelected() == SHOW) {
						mBtnNext.setEnabled(true);
						mBtnNext.setBackgroundResource(R.drawable.shape_red_btn);
						break;
					} else {
						mBtnNext.setEnabled(false);
						mBtnNext.setBackgroundResource(R.drawable.shape_gray_btn);
					}
				}
			}
			return view;
		}
		
		private void cleanOtherSelectItems(Refund refund){
			for(Refund ref:mDatas){
				if(ref == refund){
					continue;
				}
				ref.setSelected(0);
			}
		}

	}
	

	@OnClick({ R.id.backup, R.id.btn_refund })
	private void allClickEvent(View view) {
		switch (view.getId()) {
		case R.id.backup:
			getMyActivity().finish();
			break;
		case R.id.btn_refund:
			int selId = 0; // 选中的Id
			for (int i = 0; i < mListReason.size(); i++) {
				Refund refund = mListReason.get(i);
				Log.d(TAG, "选中原因是："+refund.getSelected() + ",selId=" + refund.getId());
				if (refund.getSelected() == SHOW) {
					selId = refund.getId();
					break;
				}
			}
			mBtnNext.setEnabled(false);
			actCodeApplyRefund(selId);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 退款
	 */
	private void actCodeApplyRefund (int selectId) {
		if (null == mRefund) {
			return;
		}
		String refundRemark = TextUtils.isEmpty(mEdtRefundOther.getText()) ? "" : mEdtRefundOther.getText().toString();
		String []params = {mRefund.getOrderCode(),mActCode,mRefund.getBankcardRefund()+"",mRefund.getPlatBonusRefund()+"",mRefund.getShopBonusRefund()+"",String.valueOf(selectId),refundRemark}; 
		new ActCodeApplyRefundTask(getMyActivity(), new ActCodeApplyRefundTask.Callback() {
			
			@Override
			public void getResult(int retCode) {
				mBtnNext.setEnabled(true);
				switch (retCode) {
				case ErrorCode.SUCC:
					initData();
					break;
				default:
					break;
				}
			}
		}).execute(params);
	}

	/**
	 * 友盟统计
	 */
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(ActRefundFragment.class.getSimpleName());
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(ActRefundFragment.class.getSimpleName()); // 统计页面
	}
}
