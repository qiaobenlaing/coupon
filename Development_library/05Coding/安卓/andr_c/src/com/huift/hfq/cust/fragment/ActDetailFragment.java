package com.huift.hfq.cust.fragment;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.Tools;
import com.huift.hfq.base.pojo.Activitys;
import com.huift.hfq.base.pojo.Share;
import com.huift.hfq.base.pojo.UserActivity;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.base.view.MyListView;
import com.huift.hfq.cust.R;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huift.hfq.cust.adapter.UserActAdapter;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.model.GetUserActivityInfoTask;
import com.huift.hfq.cust.util.ActUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的支付二维码
 * @author yanfang.li
 */
public class ActDetailFragment extends Fragment {
	
	private static final String TAG = ActDetailFragment.class.getSimpleName();
	/** 用户活动的编码*/
	public static final String ACT_CODE = "actCode";
	/** 没有数据加载 */
	private LinearLayout mLyView;
	/** 没有数据加载的图片 */
	private ImageView mIvView;
	/** 正在加载的进度条 */
	private ProgressBar mProgView;
	/** 列表*/
	private MyListView mLvAct;
//	/** 头部视图*/
//	private View mHeadView;
//	/** 底部视图*/
//	private View mBottomView;
	/** 活动的编码 */
	private String mActCode;
	/** 第一次运行*/
	private boolean mFirstFlag = true;
	/** 活动的适配器*/
	private UserActAdapter mActAdapter;
	/** 支付价格*/
	private double mTotalPaymengt;
	/** 全局视图*/
	private View mView;
	
	/**
	 * 需要传递参数时有利于解耦
	 */
	public static ActDetailFragment newInstance() {
		Bundle args = new Bundle();
		ActDetailFragment fragment = new ActDetailFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_actdetail, container, false);
		ViewUtils.inject(this,mView);
		findView(mView);
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
	 * @param v 视图
	 */
	private void findView(View v) {
		mLyView = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) v.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) v.findViewById(R.id.prog_nodata);
		mLvAct = (MyListView)v.findViewById(R.id.lv_act);
//		mHeadView = View.inflate(getMyActivity().getApplicationContext(), R.layout.head_actdetail, null);
//		mBottomView = View.inflate(getMyActivity().getApplicationContext(), R.layout.bottom_actdetail, null);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		mActCode = getMyActivity().getIntent().getStringExtra(ACT_CODE);
//		mLvAct.addHeaderView(mHeadView);
//		mLvAct.addFooterView(mBottomView);
		// 添加适配器
		if (mActAdapter == null) {
			mActAdapter = new UserActAdapter(getMyActivity(), null);
			mLvAct.setAdapter(mActAdapter);
		}
		GetUserActivityInfo();
	}
	
	/**
	 * 获取活动信息
	 */
	private void GetUserActivityInfo () {
		if (mFirstFlag) {
			ViewSolveUtils.setNoData(mLvAct, mLyView, mIvView, mProgView, CustConst.DATA.LOADIMG); // 正在加载
		}
		new GetUserActivityInfoTask(getMyActivity(), new GetUserActivityInfoTask.Callback() {
			
			@Override
			public void getResult(JSONObject result) {
				mFirstFlag = false;
				ViewSolveUtils.setNoData(mLvAct, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA); // 有数据
				if (null != result) {
					Activitys activitys = new Gson().fromJson(result.toString(),  new TypeToken<Activitys>() {}.getType());
					if (null != activitys) {
						setAct(activitys);
					}
				}
			}
		}).execute(mActCode);
	}
	
	/**
	 * 设置全部内容
	 * @param activity 活动对象
	 */
	private void setAct (Activitys activitys) {
		// 设置活动基本信息
		if (null != activitys.getActivityInfo()) {
			try {
				setActivityInfo(activitys.getActivityInfo()); 
			} catch (Exception e) {
				Log.e(TAG, "活动详细信息  设置头部出错 : " + e.getMessage());
			}
		}
		
		// 预购的活动列表
		if (null != activitys.getUserActCodeList() && activitys.getUserActCodeList().size() > 0) {
			for (int i = 0; i < activitys.getUserActCodeList().size(); i++) {
				UserActivity act = activitys.getUserActCodeList().get(i);
				act.setTotalPayment(mTotalPaymengt);
			}
			if (mActAdapter == null) {
				mActAdapter = new UserActAdapter(getMyActivity(), activitys.getUserActCodeList());
				mLvAct.setAdapter(mActAdapter);
			} else {
				mActAdapter.setItems(activitys.getUserActCodeList());
			}
		} else {
			TextView tvBookVouchers = (TextView) mView.findViewById(R.id.tv_book_vouchers); // 预定凭证
			tvBookVouchers.setVisibility(View.GONE);
		}
		
		// 分享
		RelativeLayout rlShareAct = (RelativeLayout) mView.findViewById(R.id.ry_share_act); // 分享
		if (null != activitys.getShareArr()) {
			final Share share = activitys.getShareArr();
			// 分享
			rlShareAct.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String describe = share.getContent();
					String title = share.getTitle();
					String filePath = Util.isEmpty(share.getIcon()) ? Tools.getFilePath(getMyActivity()) + Tools.APP_ICON : "" ;
					String logoUrl = Util.isEmpty(share.getIcon()) ? "" : share.getIcon();
					if (!Util.isEmpty(share.getLink())) {
						Tools.showGrameShare(getMyActivity(), share.getLink(), describe, title, filePath,logoUrl);
					}
				}
			});
		} else {
			rlShareAct.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 设置活动信息
	 * @param activitys
	 */
	private void setActivityInfo (final Activitys activitys) {
		ImageView ivPromotionIcon = (ImageView) mView.findViewById(R.id.iv_promotion_icon); // 背景图片
		// 设置图片的宽高
		FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) ivPromotionIcon.getLayoutParams();
		params.width = Util.getWindowWidthAndHeight(getMyActivity())[0];
		params.height = Util.getWindowWidthAndHeight(getMyActivity())[0]*32/75;
		ivPromotionIcon.setLayoutParams(params);
		TextView tvPromotionDescrible = (TextView) mView.findViewById(R.id.tv_promotion_describle); // 描述文字
		TextView tvPromotionStatus = (TextView) mView.findViewById(R.id.tv_promotion_status); // 报名人数
		TextView tvPromotionPrice = (TextView) mView.findViewById(R.id.tv_promotion_price); // 价格
		Util.showBannnerImage(getActivity(), activitys.getActivityImg(), ivPromotionIcon);
		
		// 描述文字
		tvPromotionDescrible.setText(activitys.getActivityName());
		mTotalPaymengt = activitys.getTotalPayment();
		// 价格
		if (activitys.getTotalPayment() == 0) {
			tvPromotionStatus.setVisibility(View.GONE);
			tvPromotionPrice.setText("免费");
		} else {
			tvPromotionStatus.setVisibility(View.VISIBLE);
			// 活动人数限制
			if (activitys.getLimitedParticipators() == 0) { // 活动人数不限制
				tvPromotionStatus.setText("已报名：" + activitys.getParticipators());
			} else {
				tvPromotionStatus.setText("已报名：" + activitys.getParticipators() + "/" + activitys.getLimitedParticipators());
			}
			String price = "";
			if (activitys.getMinPrice() == activitys.getTotalPayment()) {
				price = ActUtils.formatPrice(activitys.getTotalPayment()+"");
			} else {
				price = ActUtils.formatPrice(activitys.getMinPrice()+"") + " - " + ActUtils.formatPrice(activitys.getTotalPayment()+"");
			}
			tvPromotionPrice.setText("￥" + price);
		}
	}
	
	@OnClick({R.id.backup})
	private void allClickEvent (View view) {
		switch (view.getId()) {
		case R.id.backup:
			getMyActivity().finish();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 友盟统计
	 */
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(ActDetailFragment.class.getSimpleName());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		initData();
		MobclickAgent.onPageStart(ActDetailFragment.class.getSimpleName()); // 统计页面
	}
}
