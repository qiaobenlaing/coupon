package com.huift.hfq.cust.fragment;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.Tools;
import com.huift.hfq.base.model.LoginTask;
import com.huift.hfq.base.pojo.Invite;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.activity.HomeActivity;
import com.huift.hfq.cust.activity.MyRedBagActivity;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.model.GetUserInviteCodeTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 推荐码
 * @author wensi.yu
 * 
 */
public class MyRecommonedFragment extends Fragment {

	private static final String TAG = MyRecommonedFragment.class.getSimpleName();

	private static final String RECOMMONED_PER = "0";
	/** 返回图片 */
	@ViewInject(R.id.iv_turn_in)
	private ImageView mIvBackup;
	/** 功能描述文本 */
	@ViewInject(R.id.tv_mid_content)
	private TextView mTvdesc;
	/** 推荐人数 */
	private TextView mTvRecommonedPerson;
	/** 奖励红包 */
	private TextView mTvRecommonedBonus;
	/** 奖励优惠券 */
	private TextView mTvRecommonedCoupons;
	/** 推荐码 */
	private TextView mTvRecommonedCode;
	/** 分享 */
	private TextView mTvRecommonedShare;
	/** 推荐得多少钱 */
	private ImageView mIvRecommonedMoney;
	/** 推荐码类 */
	private Invite mInvite;
	/** 获得的钱 */
	private TextView mRecommonDetail;
	private String mContentDetail;
	private String mMsgDetail;
	/** checkbox的选择事件 */
	private CheckBox mCkMyRecommonedRule;
	/** 显示更多 */
	private LinearLayout mLyMyMoreRule;
	/** 进度条 */
	private ProgressBar mPrRecommonedData;
	/** 规则 */
	private TextView mTvRules;

	public static MyRecommonedFragment newInstance() {
		Bundle args = new Bundle();
		MyRecommonedFragment fragment = new MyRecommonedFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_myrecommoned, container, false);
		ViewUtils.inject(this, v);
		Util.addRecommonedActivity(getMyActivity());
		init(v);
		return v;
	}

	/**
	 * 保证activity不为空
	 * 
	 * @return activity
	 */
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	/**
	 * 初始化
	 * 
	 * @param v
	 */
	private void init(View v) {
		// 设置标题
		ImageView msg = (ImageView) v.findViewById(R.id.iv_add);
		msg.setVisibility(View.GONE);
		mTvdesc.setText(R.string.my_recommoned);
		mIvBackup.setVisibility(View.VISIBLE);
		mTvRecommonedPerson = (TextView) v.findViewById(R.id.tv_myrecommoned_person);
		mTvRecommonedBonus = (TextView) v.findViewById(R.id.tv_myrecommoned_bonus);
		mTvRecommonedCoupons = (TextView) v.findViewById(R.id.tv_myrecommoned_coupons);
		mTvRecommonedCode = (TextView) v.findViewById(R.id.tv_recommoned_code);
		mTvRecommonedShare = (TextView) v.findViewById(R.id.tv_myrecommoned_share);
		mLyMyMoreRule = (LinearLayout) v.findViewById(R.id.ly_more_rule);
		mCkMyRecommonedRule = (CheckBox) v.findViewById(R.id.ck_myrecommoned_sel);
		mCkMyRecommonedRule.setOnCheckedChangeListener(ckListener);
		mPrRecommonedData = (ProgressBar) v.findViewById(R.id.pr_recommoned_data);
		mIvRecommonedMoney = (ImageView) v.findViewById(R.id.tv_myrecommoned_bonusimg);
		mTvRules = (TextView) v.findViewById(R.id.tv_recommoned_rules);
		// 调用
		getUserInviteCode(v);
	}

	/**
	 * 推荐规则的事件
	 */
	private OnCheckedChangeListener ckListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isChecked) {
				mLyMyMoreRule.setVisibility(View.VISIBLE);
			} else {
				mLyMyMoreRule.setVisibility(View.GONE);
			}
		}
	};

	private void getUserInviteCode(final View v) {

		new GetUserInviteCodeTask(getMyActivity(), new GetUserInviteCodeTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mPrRecommonedData.setVisibility(View.GONE);
				if (null == result) { return; }

				mInvite = Util.json2Obj(result.toString(), Invite.class);
				if (Util.isEmpty(mInvite.getRecomCount())) {
					mTvRecommonedPerson.setText(RECOMMONED_PER);
				} else {
					mTvRecommonedPerson.setText(mInvite.getRecomCount());
				}

				if (Util.isEmpty(mInvite.getBonusAmount())) {
					mTvRecommonedBonus.setText(RECOMMONED_PER);  
				} else {
					mTvRecommonedBonus.setText(mInvite.getBonusAmount());
				}

				if (Util.isEmpty(mInvite.getCouponAmount())) {
					mTvRecommonedCoupons.setText(RECOMMONED_PER);
				} else {
					mTvRecommonedCoupons.setText(mInvite.getCouponAmount());
				}

				if (Util.isEmpty(mInvite.getInviteCode())) {
					mTvRecommonedCode.setText("");
				} else {
					mTvRecommonedCode.setText(mInvite.getInviteCode());
				}

				if (Util.isEmpty(mInvite.getRules())) {
					mTvRules.setText("");
				} else {
					mTvRules.setText(mInvite.getRules());
				}
				Util.showImage(getMyActivity(), mInvite.getImgUrl(), mIvRecommonedMoney);

				mTvRecommonedShare.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (Util.isEmpty(mInvite.getInviteCode())) {
							Util.getContentValidate(R.string.get_recommoned);
							return;
						} 
						String filePath = Tools.getFilePath(getMyActivity()) + Tools.APP_ICON;
						String logoUrl = "";
						Tools.showRecommShare(getMyActivity(), "Index/invitationCodeShare?code=", mInvite.getShareContent(), mInvite.getShareTitle(), mTvRecommonedCode.getText().toString(),
								filePath, logoUrl);
					}
				});

				mRecommonDetail = (TextView) v.findViewById(R.id.tv_myRecommoned_detail);
				mContentDetail = getMyActivity().getString(R.string.my_recommoned_detail);
				mMsgDetail = getMyActivity().getString(R.string.my_recommoned_details);
				String money = mInvite.getReward();

				if (Util.isEmpty(mInvite.getReward())) {
					mRecommonDetail.setText(RECOMMONED_PER);
				} else {
					mRecommonDetail.setText(mInvite.getReward());
				}
			}
		}).execute();
	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	@OnClick({ R.id.iv_turn_in, R.id.ly_mybonus, R.id.ly_mycoupons })
	public void trunIdenCode(View view) {
		Intent intent;
		switch (view.getId()) {
		case R.id.iv_turn_in:
			getMyActivity().finish();
			break;
		case R.id.ly_mybonus:// 我的红包
			intent = new Intent(getMyActivity(), MyRedBagActivity.class);
			startActivity(intent);
			break;
		case R.id.ly_mycoupons:// 我的优惠券
			intent = new Intent(getMyActivity(), HomeActivity.class);
			intent.putExtra(LoginTask.ALL_LOGIN, CustConst.Login.COUPON_LOGIN);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(MyRecommonedFragment.class.getSimpleName()); 
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(MyRecommonedFragment.class.getSimpleName()); //统计页面
	}
}
