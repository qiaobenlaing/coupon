package com.huift.hfq.cust.fragment;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.model.MyHomeBankDetailTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 银行卡详情
 * 
 * @author wensi.yu
 * 
 */
public class MyHomeBankDetailFragment extends Fragment {

	private final static String TAG = MyHomeBankDetailFragment.class.getSimpleName();

	/** 传过来的编号 **/
	public final static String BANK_CODE = "myHomeBankCode";

	private String backCode;

	private TextView mEtBankDetailCard, mEtBankDetailPoints, mEtBankDetailImit;

	private TextView mEtBankDetailRepayment, mEtBankDetailMinimumrepay, mEtBankDetailDate;

	public static MyHomeBankDetailFragment newInstance() {
		Bundle args = new Bundle();
		MyHomeBankDetailFragment fragment = new MyHomeBankDetailFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_myhomebankdetail, container, false);// 说明v，注释// e.g:Fragment的view
		ViewUtils.inject(this, v);
		init(v);
		Util.addLoginActivity(getMyActivity());
		// 调用异步任务
		initData();
		return v;
	}
	
	private Activity getMyActivity(){
		Activity act = getActivity();
		if(act==null){
			act=AppUtils.getActivity();
		}
		return act;
	}

	/**
	 * 初始化
	 */
	private void init(View view) {
		backCode = getMyActivity().getIntent().getStringExtra(BANK_CODE);
		mEtBankDetailCard = (TextView) view.findViewById(R.id.et_bankdetail_bankcard);
		mEtBankDetailPoints = (TextView) view.findViewById(R.id.et_bankdetail_points);
		mEtBankDetailImit = (TextView) view.findViewById(R.id.et_bankdetail_imit);
		mEtBankDetailRepayment = (TextView) view.findViewById(R.id.et_bankdetail_repayment);
		mEtBankDetailMinimumrepay = (TextView) view.findViewById(R.id.et_bankdetail_repayment2);
		mEtBankDetailDate = (TextView) view.findViewById(R.id.et_bankdetail_repayment3);
	}

	/**
	 * 银行卡卡详情的异步任务
	 */
	private void initData() {
		// 获得一个用户信息对象
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		String userCode = userToken.getUserCode();// 用户编码
		String tokenCode = userToken.getTokenCode();// 需要令牌认证

		// 银行卡详情的异步
		new MyHomeBankDetailTask(getMyActivity(), new MyHomeBankDetailTask.Callback() {

			@Override
			public void getResult(JSONObject result) {
				mEtBankDetailCard.setText((String) result.get("accountNbrLast4"));
				mEtBankDetailPoints.setText((String) result.get("points"));
			}
		}).execute(backCode, userCode, tokenCode);
	}

	/**
	 * 返回到银行卡列表
	 */
	@OnClick(R.id.backup)
	public void trunForgetPwd(View view) {
		getMyActivity().finish();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(MyHomeBankDetailFragment.class.getSimpleName()); 
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(MyHomeBankDetailFragment.class.getSimpleName()); //统计页面
	}
}
