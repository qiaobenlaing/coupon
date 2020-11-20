package com.huift.hfq.shop.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.ImageUploadActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @author wensi.yu
 * 营销活动中的人数限制
 */
public class ActAddPeopleFragment extends Fragment {
	
	private final static String TAG = "ActAddPeopleFragment";
	
	/** 是否预付费*/
	private final static String IS_PREPAy = "1";
	private final static String NO_PREPAy = "0";
	/** 是否报名*/
	private final static String IS_REGISTER = "1";
	private final static String NO_REGISTER = "0";
	/** 返回图片*/
	@ViewInject(R.id.layout_turn_in)
	private LinearLayout mIvBackup;
	/** 功能描述文本*/
	@ViewInject(R.id.tv_mid_content)
	private TextView mTvdesc;
	/** 上一步*/
	@ViewInject(R.id.btn_actaddexplain_laststep)
	private Button mBtnActAddLastStep;
	/** 下一步*/
	@ViewInject(R.id.btn_actaddexplain_nextstep)
	private Button mBtnActAddNextStep;
	/** 活动参与人数上限*/
	@ViewInject(R.id.et_actadd_people)
	private EditText mEtActAddPeople;
	/** 是否报名*/
	@ViewInject(R.id.cb_actadd_name)
	private CheckBox mCbActaddName;
	/** 是否付费*/
	@ViewInject(R.id.cb_actadd_amountadvanced)
	private CheckBox  mCbActaddAmountadvanced;
	/** 付费金额*/
	@ViewInject(R.id.et_actadd_PrePayment)
	private EditText mEtActaddPrepayment;
	
	public static ActAddPeopleFragment newInstance() {
		Bundle args = new Bundle();
		ActAddPeopleFragment fragment = new ActAddPeopleFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_actlist_addpeople,container, false);// 说明v，注释 e.g:Fragment的view
		ViewUtils.inject(this,v);
		init();
		return v;
	}
	
	private Activity getMyActivity(){
		Activity act = getActivity();
		if(act == null){
			act = AppUtils.getActivity();
		}
		return act;       
	}
	
	/**
	 * 初始化
	 */
	private void init() {
		//添加
		Util.addActivity(getMyActivity());
		ActivityUtils.add(getActivity());
		// 去空格jdjahdjashdj
		Util.inputFilterSpace(mEtActaddPrepayment);
		//设置标题
		mIvBackup.setVisibility(View.VISIBLE);
		mTvdesc.setText(R.string.tv_actlist_title);
		//选填事件
		/*mCbActaddAmountadvanced.setOnCheckedChangeListener(checklistener);
		mCbActaddName.setOnCheckedChangeListener(checklistener);*/
	}
	
	/**
	 * checkbox的选填事件
	 */
	/*OnCheckedChangeListener checklistener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			switch (buttonView.getId()) {
			case R.id.cb_actadd_amountadvanced:
				if(isChecked){
					mCbActaddName.setChecked(false);
					mEtActaddPrepayment.setText("");
				}
				break;
			case R.id.cb_actadd_name:
				if(isChecked){
					mCbActaddAmountadvanced.setChecked(false);
					mEtActaddPrepayment.setText("0");
				}
				break;
			default:
				break;
			}
			
		}
	};*/

	/**
	 * 点击下一步到活动图片预览
	 * @param view
	 */
	@OnClick(R.id.btn_actaddexplain_nextstep)
	public void btnActAddNextStepClick(View view) {
		String limitedParticipators = mEtActAddPeople.getText().toString().trim();
		String prePayment = mEtActaddPrepayment.getText().toString().trim();
		String isPrepayRequired = " ";
		String isRegisterRequired = " ";
		
		switch (view.getId()) {
		case R.id.btn_actaddexplain_nextstep:
			Util.addActivity(getMyActivity());
			if (Util.isEmpty(limitedParticipators)){
				Util.getContentValidate(R.string.toast_actadd_limitedparticipators);
				mEtActAddPeople.findFocus();
				break;
			}
			
			//预付费和报名
			if (mCbActaddAmountadvanced.isChecked() == false && mCbActaddName.isChecked() == false){
				mEtActaddPrepayment.setText("0");
			}
			
			//是否预付费
			if (mCbActaddAmountadvanced.isChecked() && !mCbActaddName.isChecked()){
				 isPrepayRequired = IS_PREPAy;
				 if (Util.isEmpty(prePayment)){
					 Util.getContentValidate(R.string.toast_actadd_money);
					 mEtActaddPrepayment.findFocus();
					 break;
				} 
			} else {
				isPrepayRequired = NO_PREPAy;
			}
			
			//是否报名
			if (mCbActaddName.isChecked() && !mCbActaddAmountadvanced.isChecked()){
				 isRegisterRequired = IS_REGISTER;
				 if(!Util.isEmpty(prePayment)){
					 Util.getContentValidate(R.string.toast_actadd_no);
					 break;
				 }
			} else {
				 isRegisterRequired = NO_REGISTER;
			}
			
			//选择两者
			if (mCbActaddAmountadvanced.isChecked() && mCbActaddName.isChecked()){
				 isPrepayRequired = IS_PREPAy;
				 if (Util.isEmpty(prePayment)){
					 Util.getContentValidate(R.string.toast_actadd_money);
					 mEtActaddPrepayment.findFocus();
					 break;
				 }
				 isRegisterRequired = IS_REGISTER;
			}
			
			//保存值
			SharedPreferences mSharedPreferences = getMyActivity().getSharedPreferences(ShopConst.ActAdd.ACT_ADD, Context.MODE_PRIVATE);
		    Editor editor = mSharedPreferences.edit();       
		    editor.putString("limitedParticipators", limitedParticipators);
		    editor.putString("isPrepayRequired", isPrepayRequired);
		    editor.putString("isRegisterRequired", isRegisterRequired);
		    editor.putString("prePayment", prePayment);
		    editor.commit();
		    
		    Intent intent = new Intent(getMyActivity(), ImageUploadActivity.class);		
			getMyActivity().startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 点击上一步到添加活动说明
	 * @param view
	 */
	@OnClick(R.id.btn_actaddexplain_laststep)
	public void btnActAddLastStepClick(View view) {
		getMyActivity().finish();
	}
	
	/**
	 * 点击返回按钮  
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	public void btnActAddBackClick(View view) {
		getMyActivity().finish();
	}
}
