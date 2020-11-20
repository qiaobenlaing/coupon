// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minidev.json.JSONArray;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.BatchCoupon;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.CouponSetDetailActivity;
import com.huift.hfq.shop.adapter.CouponSetAdapter;
import com.huift.hfq.shop.model.GetCouponInfoByTypeTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 优惠券详细设置
 * @author yanfang.li
 */
public class CouponSettingFragment extends Fragment {
	
	private final static String TAG = CouponSettingFragment.class.getSimpleName();
	
	// 功能布局
	@ViewInject(R.id.lv_coupon_set)
	private ListView mLvCouponSet;
	/** 没有数据加载的布局 */
	private LinearLayout mLyView;
	/** 没有数据加载的图片 */
	private ImageView mIvView;
	/** 正在加载的进度条 */
	private ProgressBar mProgView;
	/**
	 * 需要传递参数时有利于解耦
	 * 
	 * @return CouponSettingFragment
	 */
	public static CouponSettingFragment newInstance() {

		Bundle args = new Bundle();
		CouponSettingFragment fragment = new CouponSettingFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_couponsettings, container,false);
		ViewUtils.inject(this, v);
		Util.addActivity(getActivity());
		Util.addLoginActivity(getActivity());
		init(v);
		return v;
	}
	
	/**
	 * 
	 * @param v
	 */
	private void init(View v) {
		
		TextView tvSet = (TextView) v.findViewById(R.id.tv_mid_content);
		tvSet.setText(getResources().getString(R.string.coupon_set));
		LinearLayout ivReturn = (LinearLayout) v.findViewById(R.id.layout_turn_in);
		mLyView = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) v.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) v.findViewById(R.id.prog_nodata);
		ivReturn.setVisibility(View.VISIBLE);
		View headView = View.inflate(getActivity(), R.layout.top_couponset, null);
		mLvCouponSet.addHeaderView(headView);
		
		/*Intent intent = getActivity().getIntent();
		// 准备GridView的适配器所用的数据
		List<BatchCoupon> batchTypeData = (List<BatchCoupon>) intent.getSerializableExtra(ShopConst.Coupon.COUPON_LIST);*/
		// 给GridView添加监听事件
		getCouponInfoByType();
		mLvCouponSet.setOnItemClickListener(LvItemlistener);

	}
	/**
	 *  获得不同类型的优惠券
	 */
	private void getCouponInfoByType() {
		ViewSolveUtils.setNoData(mLvCouponSet, mLyView, mIvView, mProgView, ShopConst.DATA.LOADIMG);
		new GetCouponInfoByTypeTask(getActivity(), new GetCouponInfoByTypeTask.Callback() {
			@Override
			public void getResult(JSONArray result) {
				if (result == null) {
					ViewSolveUtils.setNoData(mLvCouponSet, mLyView, mIvView, mProgView, ShopConst.DATA.NO_DATA);
				} else {
					Log.d(TAG, "getCouponInfoByType  >>> " + result.toString());
					ViewSolveUtils.setNoData(mLvCouponSet, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA);
					// 准备GridView的适配器所用的数据
					List<BatchCoupon> batchTypeData = new ArrayList<BatchCoupon>();
					// 获取自定义个数组信息
					for (int i = 0; i < result.size(); i++) {
						BatchCoupon batch = Util.json2Obj(result.get(i).toString(), BatchCoupon.class);
						batchTypeData.add(batch);
					}
					// 给GridView绑定适配器
					CouponSetAdapter adapter = new CouponSetAdapter(getActivity(), batchTypeData);
					mLvCouponSet.setAdapter(adapter);
				}
			}
		}).execute();
	}
	
	/**
	 * mGvCouponSet的监听事件
	 */
	private OnItemClickListener LvItemlistener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position,
				long longId) {
			BatchCoupon batch = (BatchCoupon) mLvCouponSet.getItemAtPosition(position);
//			if (batch.getIsAcceptBankCard() == 0 && (batch.getCouponType().equals(ShopConst.Coupon.EXCHANGE_VOUCHER)
//					|| batch.getCouponType().equals(ShopConst.Coupon.VOUCHER))) { // 0 就是没有受理银行卡 不能发行优惠券  1 受理了工行卡 可以发行优惠券
//				DialogUtils.showDialogSingle(getActivity(), Util.getString(R.string.accept_icbc_bank), R.string.cue, R.string.ok, null);
//			} else {
				Intent intent = new Intent(getActivity(),CouponSetDetailActivity.class);
				intent.putExtra(CouponSetFullCutFragment.BATCH, batch);
				startActivity(intent);
//			}
			//友盟统计
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("couponType", batch.getCouponName()); 
			map.put("couponTypeName", batch.getCouponTypeName()); 
			MobclickAgent.onEvent(getActivity(), "couponset_fragment_type", map);
		}
	};
	

	/**
	 * 返回
	 * 
	 * @param v
	 */
	@OnClick(R.id.layout_turn_in)
	private void ivReturnClick(View v) {
		if (v.getId() == R.id.layout_turn_in) {
			getActivity().finish();
		}
	}
	
	/**
	 * 友盟统计
	 */
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainScreen"); 
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MainScreen"); //统计页面
	}
}
