//---------------------------------------------------------
//@author    Zhonghui.Dong
//@version   1.0.0
//@createTime 2015.6.2
//@copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
//---------------------------------------------------------
package cn.suanzi.baomi.cust.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.util.SkipActivityUtil;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 抢红包的结果页面
 * @author yanfang.li
 */
public class GrabBounsResultFragment extends Fragment {
	private static final String TAG = GrabBounsResultFragment.class.getSimpleName();
	/** 红包抢成功*/
	public static final String GRAB_SUCC = "grab.succ";
	/** 红包抢完了*/
	public static final String GRAB_OVER = "grab.over";
	/** 你已经抢到了*/
	public static final String GRAB_GET = "grab.get";
	/** 抢红包的状态*/
	public static final String GRAB_STATUS  = "grab.status";
	/** 你抢到的金额*/
	public static final String GRAB_MONEY = "grab.getmoeny";
	/** 商家头像*/
	public static final String SHOP_HEAD = "shophead";
	/** 商家shopCode*/
	public static final String SHOP_CODE = "shopcode";
	
	public static GrabBounsResultFragment newInstance() {
		Bundle args = new Bundle();
		GrabBounsResultFragment fragment = new GrabBounsResultFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.grab_redbouons, container, false);
		ViewUtils.inject(this, v);
		init(v);
		Util.addLoginActivity(getMyActivity());
		return v;
	}
	
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	private void init(View view) {
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_mid_content);
		tvTitle.setText(getString(R.string.title_bouns));
		String grabStatus = getMyActivity().getIntent().getStringExtra(GRAB_STATUS);
		String grabMoney = getMyActivity().getIntent().getStringExtra(GRAB_MONEY);
		final String shopCode = getMyActivity().getIntent().getStringExtra(SHOP_CODE);
		String shopHead = getMyActivity().getIntent().getStringExtra(SHOP_HEAD);
		// 领取过了
		ImageView ivGrabbounsGet = (ImageView) view.findViewById(R.id.iv_grabbouns_get);
		// 领完了
		ImageView ivGrabbounsOver = (ImageView) view.findViewById(R.id.iv_grabbouns_over);
		// 领到了
		ImageView ivGrabbounsSucc = (ImageView) view.findViewById(R.id.iv_grabbouns_succ);
		// 跳转
		ImageView ivGoto = (ImageView) view.findViewById(R.id.iv_goto);
		// 商家LOGO
		ImageView ivHead = (ImageView) view.findViewById(R.id.iv_shophead);
		// 抢到红包
		RelativeLayout rlGrabsucc = (RelativeLayout) view.findViewById(R.id.rl_grabsucc);
		// 抢到红包的金额
		TextView tvGrabMoeny = (TextView) view.findViewById(R.id.grab_money);
		
		if (GRAB_SUCC.equals(grabStatus)) {
			rlGrabsucc.setVisibility(View.VISIBLE);
			ivGrabbounsSucc.setVisibility(View.VISIBLE);
			tvGrabMoeny.setText(grabMoney);
		} else if (GRAB_OVER.equals(grabStatus)) {
			ivGrabbounsOver.setVisibility(View.VISIBLE);
		} else if (GRAB_GET.equals(grabStatus)) {
			ivGrabbounsGet.setVisibility(View.VISIBLE);
		}
		
		Util.showImage(getMyActivity(), shopHead, ivHead);
		
		ivGoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SkipActivityUtil.skipNewShopDetailActivity(getMyActivity(), shopCode);
				getMyActivity().finish();
			}
		});
	}

	/**
	 * 跳转
	 * @param view
	 */
	@OnClick({R.id.iv_turn_in})
	private void IvSkipClick(View view) {
		switch (view.getId()) {
		case R.id.iv_turn_in:
			getMyActivity().finish();
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(GrabBounsResultFragment.class.getSimpleName()); // 统计页面
	};
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(GrabBounsResultFragment.class.getSimpleName());
	}
}
