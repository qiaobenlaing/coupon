package com.huift.hfq.shop.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.MyStaffItem;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.model.MyStaffUppTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 修改店长信息
 * @author qian.zhou
 */
public class MyUppManagerFragment extends Fragment {
	public static final String STAFF_ITEM = "staffItem";
	/** 修改账号 **/
	private String mMobileNbr;
	/** 修改名称 **/
	private String mRealName;
	/** 账号输入 */
	private EditText mMssNum;
	/** 姓名输入 */
	private EditText mMssName;
	/** 提交 */
	private TextView mTvMsg;
	/** 保存店长信息的对象*/
	private MyStaffItem mStaffItem;

	/**
	 * 传递参数有利于解耦
	 * @return
	 */
	public static MyUppManagerFragment newInstance() {
		Bundle args = new Bundle();
		MyUppManagerFragment fragment = new MyUppManagerFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_uppstaff, container, false);
		ViewUtils.inject(this, v);
		Util.addLoginActivity(getActivity());
		init(v);
		return v;
	}

	// 初始化数据
	private void init(View v) {
		//取值
		Intent intent = getActivity().getIntent();
		mStaffItem = (MyStaffItem) intent.getSerializableExtra(STAFF_ITEM);
		//标题
		LinearLayout ivBack = (LinearLayout) v.findViewById(R.id.layout_turn_in);// 返回
		ivBack.setVisibility(View.VISIBLE);// 显示
		TextView tvTitle = (TextView) v.findViewById(R.id.tv_mid_content);
		tvTitle.setText(getResources().getString(R.string.assistant_update));
		mTvMsg = (TextView) v.findViewById(R.id.tv_msg);
		mTvMsg.setText(getResources().getString(R.string.submit_message));
		mMssNum = (EditText) v.findViewById(R.id.et_upp_number);// 账号输入框
		mMssName = (EditText) v.findViewById(R.id.et_upp_name);// 姓名输入框
		if (mStaffItem != null) {
			mMssNum.setText(!Util.isEmpty(mStaffItem.getMobileNbr()) ? mStaffItem.getMobileNbr() : "");
			mMssName.setText(!Util.isEmpty(mStaffItem.getRealName()) ? mStaffItem.getRealName() : "");
		}
		mTvMsg.setOnClickListener(subListener);// 点击提交，提交修改的数据
	}

	/**
	 * 点击提交，提交修改的数据
	 */
	OnClickListener subListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mMobileNbr = mMssNum.getText().toString();
			mRealName = mMssName.getText().toString();
			switch (v.getId()) {
			case R.id.tv_msg:
				if ("".equals(mMssNum.getText().toString())) {
					Util.getContentValidate(R.string.num_nocontent);
					break;
				}
				if (mMobileNbr.length() != 11) {
					Util.getContentValidate(R.string.mobilenbr_error);
					break;
				}
				//手机号码格式不正确
				if (Util.isPhone(getActivity(), mMobileNbr)) {
					Util.getContentValidate(R.string.mobilenbr_error);
					break;
				}
				if ("".equals(mMssName.getText().toString())) {
					Util.getContentValidate(R.string.name_nocontent);
					break;
				}
			default:
				updateManager();
				break;
			}
		}
	};
	
	/**
	 * 修改店长操作
	 */
	public void updateManager(){
		mTvMsg.setEnabled(false);
		new MyStaffUppTask(getActivity(), new MyStaffUppTask.Callback() {
			@Override
			public void getResult(int retCode) {
				mTvMsg.setEnabled(true);
				if (retCode == ErrorCode.SUCC) {
					
				} else {
					if (ShopConst.Staff.NO_MOBILENBR == retCode) {
						
						Util.getContentValidate(R.string.num_inputtype);
						
					} else if (ShopConst.Staff.MOBILENBR_ERROR == retCode) {
						
						Util.getContentValidate(R.string.mobilenbr_error);
						
					} else if (ShopConst.Staff.MOBILENBR_BEENUSED == retCode) {
						
						Util.getContentValidate(R.string.mobilenbr_used);
						
					} else if (ShopConst.Staff.NO_STAFFNAME == retCode) {
						
						Util.getContentValidate(R.string.name_inputtype);
						
					} else if (ShopConst.Staff.NO_STAFFTYPE == retCode) {
						
						Util.getContentValidate(R.string.select_type);
						
					} else if (ShopConst.Staff.PHONE_USERED == retCode) {
						
						Util.getContentValidate(R.string.mobilenbr_used);
					}
				}
			}
		}).execute(mStaffItem.getStaffCode(), mMobileNbr, mRealName, mStaffItem.getUserLvl(), "");
	}

	/** 点击返回 **/
	@OnClick(R.id.layout_turn_in)
	private void ivuppBackClick(View v) {
		getActivity().finish();
	}
}
