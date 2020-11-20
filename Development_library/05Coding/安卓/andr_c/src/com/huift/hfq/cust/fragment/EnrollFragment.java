package com.huift.hfq.cust.fragment;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.activity.ActMyContentActivity;
import com.huift.hfq.cust.activity.LoginActivity;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.model.EditUserActInfoTask;
import com.huift.hfq.cust.model.GetUserActInfo;
import com.huift.hfq.cust.model.JoinActivityTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 报名
 * @author wensi.yu
 *
 */
public class EnrollFragment extends Fragment {

	private final static String TAG = EnrollFragment.class.getSimpleName();
	
	public final static String ENROLL_GLAF ="enrollFlag";
	private final static String ACTENROLL = "0";
	private final static String MYENROLL = "1";
	/** 返回图片 */
	@ViewInject(R.id.iv_turn_in)
	private ImageView mIvBackup;
	/** 功能描述文本 */
	@ViewInject(R.id.tv_mid_content)
	private TextView mTvdesc;
	/** 添加**/
	@ViewInject(R.id.iv_add)
	private ImageView mTvAdd;
	/** 大人 男*/
	@ViewInject(R.id.et_enroll_man)
	private EditText mEtAdultMan;
	/** 大人 女*/
	@ViewInject(R.id.et_enroll_woman)
	private EditText mEtAdultWoman;
	/** 小孩 男*/
	@ViewInject(R.id.et_enroll_chrman)
	private EditText mEtchildMan;
	/** 小孩 女*/
	@ViewInject(R.id.et_enroll_chrwoman)
	private EditText mEtChildWoman;
	/** 报名总人数*/
	@ViewInject(R.id.tv_enroll_all)
	private TextView mTvEnrollAll;
	/** 报名**/
	@ViewInject(R.id.btn_enroll)
	private Button mBtnOk;
	/** 活动编码*/
	private String mActivityCode;
	/** 输入内容*/
	private String mAdultMan;
	private String mAdultWoman;
	private String mChildMan;
	private String mChildWoman;
	private String mTotlPerson;
	/** 得到报名的标示*/
	private String mEnrollFlag;
	/** 用户活动编码**/
	private String mUserActCode;
	/** 判断是否登录*/
	private boolean mLoginFlag;
	private UserToken mUserToken;
	private String mUserCode = null;
	/** 总人数*/
	private int mAllPerson;
	/** 进度条*/
	private ProgressBar mPrLogin;
	
	public static EnrollFragment newInstance() {
		Bundle args = new Bundle();
		EnrollFragment fragment = new EnrollFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_enroll, container,false);
		ViewUtils.inject(this, view);
		ActivityUtils.add(getActivity());
		init(view);
		return view;
	}
	
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}
	
	/**
	 * 初始化
	 * @param view
	 */
	private void init(View view) {
		mPrLogin = (ProgressBar) view.findViewById(R.id.pr_login_data);
		Util.addActivity(getMyActivity());
		Util.addLoginActivity(getMyActivity());
		//设置标题
		mIvBackup.setVisibility(View.VISIBLE);
		mTvAdd.setVisibility(View.GONE);
		Intent intent = getMyActivity().getIntent();
		mEnrollFlag = intent.getStringExtra(ENROLL_GLAF);
		Log.d(TAG, "mEnrollFlag========="+mEnrollFlag);
		//得到activityCode
		mActivityCode = DB.getStr(CustConst.JOIN.ACTIVITY_CODE);
		Log.d(TAG, "activityCode"+mActivityCode);
		//得到用户活动编码
		mUserActCode = DB.getStr(CustConst.JOIN.USER_ACT_CODE);
		Log.d(TAG, "用户活动编码"+mUserActCode);
		//判断是否登录
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mLoginFlag = DB.getBoolean(DB.Key.CUST_LOGIN);
		if (mLoginFlag) {
			mUserCode = mUserToken.getUserCode();
		} else {
			Intent it = new Intent(getMyActivity(), LoginActivity.class);
			getMyActivity().startActivity(it);
		}
		if (MYENROLL.equals(mEnrollFlag)) {//编辑
			mTvdesc.setText(R.string.enroll_edititle);
			getUserActInfo();
		} else {
			mTvdesc.setText(R.string.enroll_title);
		}
		
		mEtAdultMan.addTextChangedListener(enrollWatcher);
		mEtAdultWoman.addTextChangedListener(enrollWatcher);
		mEtchildMan.addTextChangedListener(enrollWatcher);
		mEtChildWoman.addTextChangedListener(enrollWatcher);
	}
	
	/**
	 * 输入框的改变事件
	 */
	TextWatcher enrollWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			mAdultMan = mEtAdultMan.getText().toString();
			mAdultWoman = mEtAdultWoman.getText().toString();
			mChildMan = mEtchildMan.getText().toString();
			mChildWoman = mEtChildWoman.getText().toString();
			mTotlPerson = mTvEnrollAll.getText().toString();
			
			if (Util.isEmpty(mAdultMan)) {
				mAdultMan = ACTENROLL;
			}
			if (Util.isEmpty(mAdultWoman)) {
				mAdultWoman = ACTENROLL;
			}
			if (Util.isEmpty(mChildMan)) {
				mChildMan = ACTENROLL;
			}
			if (Util.isEmpty(mChildWoman)) {
				mChildWoman = ACTENROLL;
			}
			mAllPerson = Integer.parseInt(mAdultMan) + Integer.parseInt(mAdultWoman) + Integer.parseInt(mChildMan) + Integer.parseInt(mChildWoman); 
			mTvEnrollAll.setText(String.valueOf(mAllPerson)+"人");
			Log.d(TAG, "总人数========="+mAllPerson);
		}
	};
	
	/**
	 * 确定按钮
	 * @param view
	 */
	@OnClick(R.id.btn_enroll)
	public void btnClickOk(View view) {
		//输入的属性
		mAdultMan = mEtAdultMan.getText().toString();
		mAdultWoman = mEtAdultWoman.getText().toString();
		mChildMan = mEtchildMan.getText().toString();
		mChildWoman = mEtChildWoman.getText().toString();
		mTotlPerson = mTvEnrollAll.getText().toString();
		
		switch (view.getId()) {
		case R.id.btn_enroll:
			if (Util.isEmpty(mAdultMan)) {
				mAdultMan = ACTENROLL;
			}
			if (Util.isEmpty(mAdultWoman)) {
				mAdultWoman = ACTENROLL;
			}
			if (Util.isEmpty(mChildMan)) {
				mChildMan = ACTENROLL;
			}
			if (Util.isEmpty(mChildWoman)) {
				mChildWoman = ACTENROLL;
			}
			
			Log.d(TAG, "总人数"+mTotlPerson);
			if (mAllPerson == Util.NUM_ZERO) {
				Util.getContentValidate(R.string.toast_enrollallperson);
				break;
			}
			
			if (ACTENROLL.equals(mEnrollFlag)) {//报名
				mBtnOk.setEnabled(false);
				new JoinActivityTask(getMyActivity(), new JoinActivityTask.Callback() {
					@Override
					public void getResult(JSONObject result) {
						mBtnOk.setEnabled(true);
						if (null == result) {
							return;
						}
						
						if (result.get("code").toString().equals(String.valueOf(ErrorCode.SUCC))) {
							Intent intent = new Intent(getMyActivity(), ActMyContentActivity.class);
							getMyActivity().startActivity(intent);
						}
					}
				}).execute(mUserCode,mActivityCode,mAdultMan,mAdultWoman,mChildMan,mChildWoman);
				
			}else if (MYENROLL.equals(mEnrollFlag)) {//编辑
				mBtnOk.setEnabled(false);
				new EditUserActInfoTask(getMyActivity(), new EditUserActInfoTask.Callback() {
					@Override
					public void getResult(int result) {
						mBtnOk.setEnabled(true);
						if (result == ErrorCode.SUCC) {
							Util.getContentValidate(R.string.actmycontent_edit);
						}
					}
				}).execute(mUserActCode,mAdultMan,mAdultWoman,mChildMan,mChildWoman);
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 查询用户活动详情
	 */
	private void getUserActInfo(){
		mBtnOk.setEnabled(false);
		new GetUserActInfo(getMyActivity(), new GetUserActInfo.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mPrLogin.setVisibility(View.GONE);
				mBtnOk.setEnabled(true);
				if (result == null) {
					return;
				}
				String adultM = result.get(CustConst.USERJOIN.ADULTM).toString();
				String adultF = result.get(CustConst.USERJOIN.ADULTF).toString();
				String kidM = result.get(CustConst.USERJOIN.KIDM).toString();
				String kidF = result.get(CustConst.USERJOIN.KIDF).toString();
				
				mEtAdultMan.setText(adultM);
				mEtAdultWoman.setText(adultF);
				mEtchildMan.setText(kidM);
				mEtChildWoman.setText(kidF);
			}
		}).execute(mUserActCode);
	}
	
	/**
	 * 返回
	 * @param view
	 */
	@OnClick(R.id.iv_turn_in)
	public void backClick(View view) {
		getMyActivity().finish();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(EnrollFragment.class.getSimpleName()); // 统计页面
	};
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(EnrollFragment.class.getSimpleName());
	}
}
