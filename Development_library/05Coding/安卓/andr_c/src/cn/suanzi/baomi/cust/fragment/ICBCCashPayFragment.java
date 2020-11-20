package cn.suanzi.baomi.cust.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.suanzi.baomi.cust.R;

import com.lidroid.xutils.ViewUtils;
/**
 * 工行现金支付
 * 
 * @author Zhonghui.Dong
 * 
 */
public class ICBCCashPayFragment extends Fragment {
	/**
	 * 需要传递参数时有利于解耦
	 */
	public static ICBCCashPayFragment newInstance() {
		Bundle args = new Bundle();
		ICBCCashPayFragment fragment = new ICBCCashPayFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_shop_icbc_cash_pay, container, false);
		ViewUtils.inject(this, v);
		init(v);
		return v;
	}

	private void init(View v) {
		
	}
	
}
