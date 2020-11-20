package cn.suanzi.baomi.shop.fragment.cashier;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.shop.R;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 
 * @author hsm
 * 收银台--处理结果
 *
 */
public class CashierResultFragment extends Fragment{

	/**返回图片**/
	@ViewInject(R.id.layout_turn_in)
	LinearLayout mIvBackup;

	/**功能描述文本**/
	@ViewInject(R.id.tv_mid_content)
	TextView mTvdesc;
	
	private boolean mStartDateFlag = false;
	private boolean mEndDateFlag = false;
	
	public static CashierResultFragment newInstance() {
		Bundle args = new Bundle();
		CashierResultFragment fragment = new CashierResultFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cashier_result,container, false);// 说明v，注释 e.g:Fragment的view
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

		mTvdesc.setText("操作结果");
	}

	/**
	 * 点击返回按钮 返回主页
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	public void btnActAddBackClick(View view) {		
		getActivity().finish();
	}
}
