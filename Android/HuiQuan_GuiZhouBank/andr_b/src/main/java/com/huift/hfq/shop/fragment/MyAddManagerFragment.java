package com.huift.hfq.shop.fragment;

import android.app.Fragment;
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
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.model.MyStaffAddTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 添加店长信息
 * @author qian.zhou
 */
public class MyAddManagerFragment extends Fragment {
	public static final String MYSTAFF_ITEM = "mystaff_item";
	/** 添加账号 **/
	private String mMobileNbr;
	/** 添加名称 **/
	private String mRealName;
	/** 账号输入 */
	private EditText mMssNum;
	/** 姓名输入 */
	private EditText mMssName;
	/** 提交 */
	private TextView mTvMsg;

	/**
	 * 传递参数有利于解耦
	 */
	public static MyAddManagerFragment newInstance() {
		Bundle args = new Bundle();
		MyAddManagerFragment fragment = new MyAddManagerFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_addstaff, container, false);
		ViewUtils.inject(this, v);
		Util.addLoginActivity(getActivity());
		init(v);
		return v;
	}

	// 初始化数据
	private void init(View v) {
		//标题
		LinearLayout ivBack = (LinearLayout) v.findViewById(R.id.layout_turn_in);// 返回
		ivBack.setVisibility(View.VISIBLE);// 显示
		TextView tvTitle = (TextView) v.findViewById(R.id.tv_mid_content);
		tvTitle.setText(getResources().getString(R.string.mystaff_add));
		mTvMsg = (TextView) v.findViewById(R.id.tv_msg);
		mTvMsg.setText(getResources().getString(R.string.submit_message));// 提交
		mMssNum = (EditText) v.findViewById(R.id.et_number);// 账号输入框
		mMssName = (EditText) v.findViewById(R.id.et_name);// 姓名输入框
		mTvMsg.setOnClickListener(subListener);// 点击提交，提交添加的数据
	}

	/**
	 * 点击提交，提交添加的数据
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
				if(Util.isPhone(getActivity(), mMobileNbr)){
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
	 * 添加店长操作
	 */
	public void updateManager(){
		mTvMsg.setEnabled(false);
		new MyStaffAddTask(getActivity(), new MyStaffAddTask.Callback() {
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
		}).execute("", mMobileNbr, mRealName, "2", "");
	}

	/** 点击返回 **/
	@OnClick(R.id.layout_turn_in)
	private void ivaddBackClick(View v) {
		getActivity().finish();
	}
}
