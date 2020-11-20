package cn.suanzi.baomi.cust.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.cust.R;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 扫描支付成功界面（推送界面）
 * @author yanfang.li
 */
public class ScanPaySuccFragment extends Fragment {
	private static final String TAG = ScanPaySuccFragment.class.getSimpleName();
	
	/**
	 * 需要传递参数时有利于解耦
	 */
	public static ScanPaySuccFragment newInstance() {
		Bundle args = new Bundle();
		ScanPaySuccFragment fragment = new ScanPaySuccFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_pay_succ, container, false);
		ViewUtils.inject(this, v);
		findView(v);
		initData();
		return v;
	}
	
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}
	
	/**
	 * 查找控件
	 * @param v 视图
	 */
	private void findView(View v) {
		
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		
	}
	
	@OnClick({R.id.backup})
	private void allClickEvent (View view) {
		switch (view.getId()) {
		case R.id.backup:
			getMyActivity().finish();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 友盟统计
	 */
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(ScanPaySuccFragment.class.getSimpleName());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(ScanPaySuccFragment.class.getSimpleName()); // 统计页面
	}
}
