package cn.suanzi.baomi.shop.fragment.cashier;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.activity.CashierConfirmActivity;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 
 * @author hsm
 * 分期付款交易
 *
 */
public class TransInstallmentFragment extends Fragment{

	/**返回图片**/
	@ViewInject(R.id.layout_turn_in)
	LinearLayout mIvBackup;

	/**功能描述文本**/
	@ViewInject(R.id.tv_mid_content)
	TextView mTvdesc;
	/**消费金额输入框*/
	@ViewInject(R.id.money)
	EditText mMoneyEdit;
	/**分期指数输入框*/
	@ViewInject(R.id.stage_num)
	EditText mStageNumEdit;
	
	private boolean mStartDateFlag = false;
	private boolean mEndDateFlag = false;
	
	public static TransInstallmentFragment newInstance() {
		Bundle args = new Bundle();
		TransInstallmentFragment fragment = new TransInstallmentFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cashier_installment,container, false);// 说明v，注释 e.g:Fragment的view
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

		mTvdesc.setText(R.string.tv_check_stages);
	}

	/**
	 * 点击返回按钮 返回主页
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	public void btnBackClick(View view) {		
		getActivity().finish();
	}
	
	@OnClick(R.id.btn_submit)
	public void btnSubmitClick(View view) {		

		String money=mMoneyEdit.getText().toString().trim();
		if(money.length()==0){
			Util.showToastZH("请输入消费金额");
			return ;
		}
		String stagenum=mStageNumEdit.getText().toString().trim();
		if(stagenum.length()==0){
			Util.showToastZH("请输入分期指数");
			return ;
		}
		Intent intent=new Intent(getActivity(),CashierConfirmActivity.class);
		getActivity().startActivity(intent);
	}
}
