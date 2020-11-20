package com.huift.hfq.shop.fragment.cashier;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.Util;
import com.huift.hfq.shop.activity.CashierConfirmActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 
 * @author hsm
 * 打印交易
 *
 */
public class TransQueryFragment extends Fragment{

	/**返回图片**/
	@ViewInject(R.id.layout_turn_in)
	LinearLayout mIvBackup;

	/**功能描述文本**/
	@ViewInject(R.id.tv_mid_content)
	TextView mTvdesc;
	/**查询开始日期输入框*/
	@ViewInject(R.id.start)
	EditText mStartDateEdit;
	/**查询结束日期输入框*/
	@ViewInject(R.id.end)
	EditText mEndDateEdit;
	
	private boolean mStartDateFlag = false;
	private boolean mEndDateFlag = false;
	
	public static TransQueryFragment newInstance() {
		Bundle args = new Bundle();
		TransQueryFragment fragment = new TransQueryFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cashier_query,container, false);// 说明v，注释 e.g:Fragment的view
		ViewUtils.inject(this, view);
		init();
		return view;
	}
	
	/**
	 * 初始化
	 */
	private void init() {
		//保存
		Util.addActivity(getActivity());
		mIvBackup.setVisibility(View.VISIBLE);

		mTvdesc.setText(R.string.tv_check_detail);
	}

	/**
	 * 点击返回按钮 返回主页
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	public void btnBackClick(View view) {		
		getActivity().finish();
	}
	@OnClick(R.id.start)
	public void btnStartDateClick(View view) {		
		Util.getTimeDialog(getActivity(), mStartDateEdit).show();
	}
	@OnClick(R.id.end)
	public void btnEndDateClick(View view) {		
		Util.getTimeDialog(getActivity(), mEndDateEdit).show();
	}
	
	@OnClick(R.id.btn_submit)
	public void btnSubmitClick(View view) {		
		Intent intent=new Intent(getActivity(),CashierConfirmActivity.class);
		getActivity().startActivity(intent);
	}
}
