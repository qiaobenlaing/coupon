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
 * 撤销交易
 *
 */
public class TransCancelFragment extends Fragment{

	/**返回图片**/
	@ViewInject(R.id.layout_turn_in)
	LinearLayout mIvBackup;

	/**功能描述文本**/
	@ViewInject(R.id.tv_mid_content)
	TextView mTvdesc;
	/**原参考号输入框*/
	@ViewInject(R.id.trans_refnum)
	EditText mOldRefNoEdit;
	
	private boolean mStartDateFlag = false;
	private boolean mEndDateFlag = false;
	
	public static TransCancelFragment newInstance() {
		Bundle args = new Bundle();
		TransCancelFragment fragment = new TransCancelFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cashier_cancel,container, false);// 说明v，注释 e.g:Fragment的view
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

		mTvdesc.setText(R.string.tv_check_back);
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
		String oldRefNo=mOldRefNoEdit.getText().toString().trim();
		if(oldRefNo.length()==0){
			Util.showToastZH("请输入原交易参考号");
			return ;
		}
		Intent intent=new Intent(getActivity(),CashierConfirmActivity.class);
		getActivity().startActivity(intent);
	}
}
