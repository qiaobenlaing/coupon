//---------------------------------------------------------
//@author    yanfang.li
//@version   1.0.0
//@copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
//---------------------------------------------------------
package cn.suanzi.baomi.cust.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.suanzi.baomi.cust.R;

import com.lidroid.xutils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 优惠券使用详情界面 单Fragment
 * @author yanfang.li
 */
public class CouponUseDetailFragment extends Fragment {
	private static final String TAG = CouponUseDetailFragment.class.getSimpleName();

	public static CouponUseDetailFragment newInstance() {
		Bundle args = new Bundle();
		CouponUseDetailFragment fragment = new CouponUseDetailFragment();
		fragment.setArguments(args);
	return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_couponhome, container,false);
		ViewUtils.inject(this, v);
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(CouponUseDetailFragment.class.getSimpleName()); // 统计页面
	};
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(CouponUseDetailFragment.class.getSimpleName());
	}
	
}
