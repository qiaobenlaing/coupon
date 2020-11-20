package com.huift.hfq.cust.fragment;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.huift.hfq.base.Const;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.Tools;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.activity.ActMyContentActivity;
import com.huift.hfq.cust.activity.EnrollActivity;
import com.huift.hfq.cust.activity.LoginActivity;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.model.ExitActTask;
import com.huift.hfq.cust.model.IsUserJoinActTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 查看工行特惠活动详情
 * @author wensi.yu
 */
@SuppressLint("SetJavaScriptEnabled")
public class ActIcBcDetailFragment extends Fragment {
	
	private final static String TAG = "ActDetailFragment";
	
	public static final int ACT_EDIT = Util.NUM_ONE;
	public final static String ACTIVITY_CODE = "activityCode";
	public final static String ACTIVITY_NAME = "activityName";
	public final static String ACTIVITY_CONTENT = "activity_content";
	public final static String TYPE = "typeflag";
	private final static String ACTENROLL = "0";
	private final static String MYENROLL = "1";
	/** 加载网页版 **/
	private WebView mWebview;
	/** 活动编码*/
	private String mActivityCode;
	/** 报名*/
	private TextView mTvEnroll;
	/** 报名的标示*/
	private String mType;
	/** 退出活动*/
	private TextView mActListBack;
	/** 编辑活动人数*/
	private TextView mActListUpdate;
	/** 活动的编辑*/
	private ImageView mTmp;
	/** PopupWindow容器**/
	private PopupWindow mPopupWindow;
	/** 用户活动编码**/
	private String mUserActCode;
	/** 活动名称*/
	private String mActivityName;
	/** 数据加载进度条*/
	private LinearLayout mLyNoData;
	/** 正在加载的进度条*/
	private ProgressBar mProgView;
	/** 没有数据加载的图片*/
	private ImageView mIvView ;
	/** 活动的加载图标*/
	private ProgressBar mActProgress;
	/** 登录的标志*/
	private boolean mLoginFlag;
	
	public static ActIcBcDetailFragment newInstance() {
		Bundle args = new Bundle();
		ActIcBcDetailFragment fragment = new ActIcBcDetailFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_actdetail, container, false);
		ViewUtils.inject(this, view);
		Util.addActivity(getActivity());
		ActivityUtils.add(getActivity());
		init(view);
		//活动详情的对象
		DB.saveStr(CustConst.ActDetailKey.ACT_MAIN, getClass().getSimpleName());
		DB.saveStr(CustConst.ActDetailKey.ACT, getClass().getSimpleName());
		Log.d(TAG, "ACT_MAIN===="+CustConst.ActDetailKey.ACT_MAIN);
		Log.d(TAG, "ACT===="+CustConst.ActDetailKey.ACT);
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		DB.saveStr(CustConst.ActDetailKey.IS_ACT_MAIN,"");
	};
	
	@Override
	public void onPause() {
		super.onPause();
		DB.saveStr(CustConst.ActDetailKey.IS_ACT_MAIN, CustConst.ActDetailKey.IS_ACT_MAIN);
		MobclickAgent.onPageEnd(ActIcBcDetailFragment.class.getSimpleName());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageEnd(ActIcBcDetailFragment.class.getSimpleName());
		String actClass = DB.getStr(CustConst.ActDetailKey.ACT_MAIN); // 获取活动的类
		String runClass = DB.getStr(CustConst.ActDetailKey.ACT); // 获取运行的类
		if (actClass.equals(runClass)) {
			Log.d(TAG, "更改了活动详情页面");
		}
	}
	
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	private void init(View view) {
		TextView tvcontent = (TextView) view.findViewById(R.id.tv_mid_content);
		tvcontent.setText(getResources().getString(R.string.tv_activity_detail));
		mLyNoData =  (LinearLayout) view.findViewById(R.id.ly_nodata);
		mProgView = (ProgressBar) view.findViewById(R.id.prog_nodata);
		mIvView = (ImageView) view.findViewById(R.id.iv_nodata);
		mActProgress = (ProgressBar) view.findViewById(R.id.pr_act_data);
		
		// 获取传过来的编码值
		Intent intent = getMyActivity().getIntent();
		mActivityCode = intent.getStringExtra(ACTIVITY_CODE);
		DB.saveStr(CustConst.JOIN.ACTIVITY_CODE, mActivityCode);
		mActivityName = intent.getStringExtra(ACTIVITY_NAME);
		Log.d(TAG, "activityName==="+mActivityName);
		mType = intent.getStringExtra(TYPE);
		mWebview = (WebView) view.findViewById(R.id.wb_actdetail);
		
		// 设置WebView属性，能够执行Javascript脚本
		mWebview.getSettings().setJavaScriptEnabled(true);
		
		 if (Util.isNetworkOpen(getMyActivity())) {
	        	ViewSolveUtils.setNoData(mWebview, mLyNoData, mIvView, mProgView, CustConst.DATA.LOADIMG); // 正在加载
	        	mWebview.setWebChromeClient(new WebChromeClient(){
	      		   @Override
	      			public void onProgressChanged(WebView view, int newProgress) {
	      				super.onProgressChanged(view, newProgress);
	      				 if(newProgress == 100){  
	      					 ViewSolveUtils.setNoData(mWebview, mLyNoData, mIvView, mProgView, CustConst.DATA.HAVE_DATA); // 已经加载完了
	      			      } 
	      			}
	      	    });
	        	
	        } else {
	        	Util.getToastBottom(getMyActivity(), "请连接网络");
	        }
		// 加载需要显示的网页
		mWebview.loadUrl(Const.H5_URL +"Browser/cGetActInfo?actCode="+mActivityCode);
		// 设置Web视图
		mWebview.setWebViewClient(new HelloWebViewClient());
		
		//判断是哪个页面进来的
		if ("0".equals(mType)) {//是工银特惠界面
			mTmp = (ImageView) view.findViewById(R.id.iv_share);
			mTmp.setVisibility(View.VISIBLE);
		} else if ("1".equals(mType)){//我的活动
			mTmp = (ImageView) view.findViewById(R.id.iv_share);
			mTmp.setBackgroundResource(R.drawable.accntlist_add);
		}
		
		//分享
		mTmp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String title =  mActivityName; 
				String describe = getActivity().getIntent().getStringExtra(ACTIVITY_CONTENT);
				String filePath = Tools.getFilePath(getMyActivity()) + Tools.APP_ICON;
				//String logoUrl = Const.IMG_URL + coupon.getLogoUrl();
				String logoUrl = "";
				Tools.showActivityShare(getActivity(), "Activity/share?activityCode=" ,describe,title,mActivityCode,filePath,logoUrl);
			}
		});
		// 报名tv_enroll
		mTvEnroll = (TextView) view.findViewById(R.id.tv_enroll);
		//是否报名
		isUserJoinAct();
		// 报名
		mTvEnroll.setOnClickListener(enrollListener);
		//得到用户活动编码
		mUserActCode = DB.getStr(CustConst.JOIN.USER_ACT_CODE);
		Log.d(TAG, "用户活动编码============"+mUserActCode);
		mLoginFlag = DB.getBoolean(DB.Key.CUST_LOGIN);
	}

	/**
	 * 报名
	 */
	OnClickListener enrollListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// 判断userCode是否为空
			if (mLoginFlag) {
				Intent it = new Intent(getMyActivity(), EnrollActivity.class);
				it.putExtra(EnrollFragment.newInstance().ENROLL_GLAF, ACTENROLL);
				startActivity(it);
			} else {
				Intent it = new Intent(getMyActivity(), LoginActivity.class);
				startActivity(it);
				getActivity().finish();
			}
		}
	};
	
	/**
	 * 是否报名
	 * @author ad
	 *
	 */
	private void isUserJoinAct(){
		
		new IsUserJoinActTask(getActivity(), new IsUserJoinActTask.Callback() {
			@Override
			public void getResult(boolean result) {
				//判断是哪个页面进来的
				if ("0".equals(mType)) {//是工银特惠界面
					mTvEnroll.setVisibility(View.GONE);
					Log.d(TAG, "是工银特惠界面");
					if (result) {
						mTvEnroll.setVisibility(View.GONE);
					} else {
						mTvEnroll.setVisibility(View.VISIBLE);
					}
				} else if ("1".equals(mType)) {//我的活动
					mTvEnroll.setVisibility(View.GONE);
					mTmp.setVisibility(View.VISIBLE);
					mTmp.setBackgroundResource(R.drawable.accntlist_add);
					//触摸添加的事件
					mTmp.setOnTouchListener(onTouchListener);
				}
				
			}
		}).execute(mActivityCode);
	}
	
	OnTouchListener onTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				
				View view = LayoutInflater.from(getActivity()).inflate(R.layout.popupwinddow_listuser, null);
				view.setBackgroundColor(Color.TRANSPARENT);
				mPopupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				mPopupWindow.setFocusable(true);
				mPopupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
				mPopupWindow.setOutsideTouchable(true);
				mPopupWindow.showAsDropDown(v, 10, 0);
				
				mActListBack = (TextView) view.findViewById(R.id.tv_actlistuser_back);//退出
				mActListUpdate = (TextView) view.findViewById(R.id.tv_actlistuser_update);//编辑
				
				OnClickListener accntClick = new OnClickListener() {
					@Override
					public void onClick(View v) {
						switch (v.getId()) {
						case R.id.tv_actlistuser_back: //退出
							exitAct();
							mPopupWindow.dismiss();
							break;
						case R.id.tv_actlistuser_update://编辑
							Intent intent = new Intent(getActivity(), EnrollActivity.class);
							intent.putExtra(EnrollFragment.newInstance().ENROLL_GLAF, MYENROLL);
							startActivity(intent);
							mPopupWindow.dismiss();
							break;
						default:
							break;
						}
					}
				};
				
				mActListBack.setOnClickListener(accntClick);
				mActListUpdate.setOnClickListener(accntClick);
			}			
			return true;
		}
	};
	
	/**
	 * 退出活动
	 */
	private void exitAct(){
		
		mActListBack.setEnabled(false);
		new ExitActTask(getActivity(), new ExitActTask.Callback() {
			@Override
			public void getResult(int result) {
				mActListBack.setEnabled(true);
				if (result == ErrorCode.SUCC) {
					Intent intent = new Intent(getActivity(), ActMyContentActivity.class);
					getActivity().startActivity(intent);
				} else if (result == ErrorCode.FAIL) {
					Util.getContentValidate(R.string.actmycontent_error);
				}
			}
		}).execute(mUserActCode);
	}
	
	/**
	 * Web视图
	 */
	private class HelloWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	/**
	 * 点击返回查看到活动列表
	 * @param view
	 */
	@OnClick(R.id.iv_turn_in)
	public void btnActAddDetailBackClick(View view) {
		getActivity().finish();
	}
	
}
