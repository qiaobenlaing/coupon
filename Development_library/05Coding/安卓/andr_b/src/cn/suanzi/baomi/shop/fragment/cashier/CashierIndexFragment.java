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
import cn.suanzi.baomi.shop.activity.CashierCancelTransActivity;
import cn.suanzi.baomi.shop.activity.CashierConfirmActivity;
import cn.suanzi.baomi.shop.activity.CashierInstallmentTransActivity;
import cn.suanzi.baomi.shop.activity.CashierPrintTransActivity;
import cn.suanzi.baomi.shop.activity.CashierQueryTransActivity;
import cn.suanzi.baomi.shop.activity.CashierRefundTransActivity;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class CashierIndexFragment extends Fragment{

	/**返回图片**/
	@ViewInject(R.id.layout_turn_in)
	LinearLayout mIvBackup;

	/**功能描述文本**/
	@ViewInject(R.id.tv_mid_content)
	TextView mTvdesc;
	/**金额输入框*/
	@ViewInject(R.id.money)
	EditText mMoneyEdit;
	
	private boolean mStartDateFlag = false;
	private boolean mEndDateFlag = false;
	
	public static CashierIndexFragment newInstance() {
		Bundle args = new Bundle();
		CashierIndexFragment fragment = new CashierIndexFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cashier_index,container, false);// 说明v，注释 e.g:Fragment的view
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

		mTvdesc.setText(R.string.my_cashier);
	}

	/**
	 * 点击返回按钮 返回主页
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	public void btnBackClick(View view) {		
		getActivity().finish();
	}
	
	@OnClick(R.id.trans_cancel)
	public void btnCancelClick(View view) {		
		Intent intent=new Intent(getActivity(),CashierCancelTransActivity.class);
		getActivity().startActivity(intent);
	}
	
	@OnClick(R.id.trans_refund)
	public void btnRefundClick(View view) {		
		Intent intent=new Intent(getActivity(),CashierRefundTransActivity.class);
		getActivity().startActivity(intent);
	}
	
	@OnClick(R.id.trans_query)
	public void btnQueryClick(View view) {		
		Intent intent=new Intent(getActivity(),CashierQueryTransActivity.class);
		getActivity().startActivity(intent);
	}
	
	@OnClick(R.id.trans_print)
	public void btnPrintClick(View view) {		
		Intent intent=new Intent(getActivity(),CashierPrintTransActivity.class);
		getActivity().startActivity(intent);
	}
	
	@OnClick(R.id.trans_installment)
	public void btnInstallmentClick(View view) {		
		Intent intent=new Intent(getActivity(),CashierInstallmentTransActivity.class);
		getActivity().startActivity(intent);
	}
	
	@OnClick(R.id.btn_consume)
	public void btnConsumeClick(View view) {	
		String money=mMoneyEdit.getText().toString().trim();
		if(money.length()==0){
			Util.showToastZH("请输入消费金额");
			return ;
		}
		Intent intent=new Intent(getActivity(),CashierConfirmActivity.class);
		getActivity().startActivity(intent);
	}
}
