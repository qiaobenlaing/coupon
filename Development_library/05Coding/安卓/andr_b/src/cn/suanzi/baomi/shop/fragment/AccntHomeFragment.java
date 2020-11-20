package cn.suanzi.baomi.shop.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.activity.MyBalanceManagerSusActivity;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 惠圈对账的主页
 * @author wensi.yu
 */
public class AccntHomeFragment extends Fragment {
	
	private final static String TAG = "AccntHomeFragment";
	
	private static final String ACCNT_TITLE = "惠圈对账";
	/** 返回图片**/
	@ViewInject(R.id.layout_turn_in)
	private LinearLayout mIvBackup;
	/** 功能描述文本**/
	@ViewInject(R.id.tv_mid_content)
	private TextView mTvdesc;
	/** 银行卡付费**/
	@ViewInject(R.id.accnt_bank)
	private LinearLayout mLyBank;
	/** 优惠券**/
	@ViewInject(R.id.annct_coupon)
	private LinearLayout mLyCoupon;
	/** 营销活动**/
	@ViewInject(R.id.accnt_activity)
	private LinearLayout mLyAct;
	
	public static AccntHomeFragment newInstance() {
		Bundle args = new Bundle();
		AccntHomeFragment fragment = new AccntHomeFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_accnt_home,container, false);
		ViewUtils.inject(this, view);
		Util.addLoginActivity(getActivity());
		init(view);
		return view;
	}

	/**
	 * 初始化
	 * @param view
	 */
	private void init(View view) {
		//设置标题
		mIvBackup.setVisibility(View.VISIBLE);
		mTvdesc.setText(ACCNT_TITLE);
		//点击事件
		mLyBank.setOnClickListener(clickListerner);
		mLyCoupon.setOnClickListener(clickListerner);
		mLyAct.setOnClickListener(clickListerner);
		mIvBackup.setOnClickListener(clickListerner);
	}
	
	/**
	 * 跳转
	 */
	OnClickListener clickListerner = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
			case R.id.accnt_bank://银行卡对账
				intent = new Intent(getActivity(), MyBalanceManagerSusActivity.class);
				getActivity().startActivity(intent);
				break;
			case R.id.annct_coupon://优惠券
				/*intent = new Intent(getActivity(), AccntCouponActivity.class);
				getActivity().startActivity(intent);*/
				break;
			case R.id.accnt_activity://营销活动
				/*intent = new Intent(getActivity(), AccntMarketActivity.class);
				getActivity().startActivity(intent);*/
				break;
			case R.id.layout_turn_in://返回
				getActivity().finish();
				break;
			default:
				break;
			}
		}
	};
}
