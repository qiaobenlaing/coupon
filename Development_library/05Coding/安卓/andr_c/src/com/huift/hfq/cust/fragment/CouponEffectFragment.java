//---------------------------------------------------------
//@author    yanfang.li
//@version   1.0.0
//@createTime 2015.6.2
//@copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
//---------------------------------------------------------
package com.huift.hfq.cust.fragment;

import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.TheadDBhelper;
import com.huift.hfq.base.pojo.BatchCoupon;
import com.huift.hfq.base.pojo.Citys;
import com.huift.hfq.base.pojo.PageData;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.base.view.XListView;
import com.huift.hfq.base.view.XListView.IXListViewListener;
import com.huift.hfq.cust.R;

import com.google.gson.reflect.TypeToken;
import com.huift.hfq.cust.adapter.CouponEffectCardAdapter;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.model.GetMyAvailableCouponTask;
import com.lidroid.xutils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 优惠券有效
 * @author yanfang.li
 */
public class CouponEffectFragment extends Fragment implements IXListViewListener {

	private final static String TAG = CouponEffectFragment.class.getSimpleName();
	/** couponList列表 **/
	private XListView mLvCouponEffect;
	/** 查询的页码 **/
	private int mPage = 1;
	private View mView;
	/** 适配器 */
	private CouponEffectCardAdapter mEffectAdapter;
	/** 经度 */
	private String mLongitude;
	/** 纬度 */
	private String mLatitude;
	/** 没有数据加载 */
	private LinearLayout mLyView;
	/** 没有数据加载的图片 */
	private ImageView mIvView;
	/** 正在加载的进度条 */
	private ProgressBar mProgView;
	/** 线程 **/
	private Handler mHandler;
	/** 上拉请求api */
	private boolean mDataUpFlag;
	/** 下拉请求api */
	private boolean mDataFlag;
	/** 第一次运行*/
	private boolean mFirstFlag = true;
	/** 下拉加载 */
	private SwipeRefreshLayout mSwipeRefreshLayout;

	public static CouponEffectFragment newInstance() {

		Bundle args = new Bundle();
		CouponEffectFragment mFragment = new CouponEffectFragment();
		mFragment.setArguments(args);
		return mFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mView = inflater.inflate(R.layout.fragment_couponeffect, container, false);
		ViewUtils.inject(this, mView);
		init(mView);
		Util.addLoginActivity(getMyActivity());
		Util.addRecommonedActivity(getMyActivity());
		return mView;

	}
	
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	/**
	 * 初始化方法
	 */
	private void init(View v) {
		mDataUpFlag = true;
		mDataFlag = true;
		// 取出定位的城市
		SharedPreferences preferences = getMyActivity().getSharedPreferences(CustConst.Key.CITY_OBJ, Context.MODE_PRIVATE);
		String cityName = preferences.getString(CustConst.Key.CITY_NAME, null);
		if (Util.isEmpty(cityName)) {
			Citys citys = DB.getObj(HomeFragment.CITYS, Citys.class);
			mLongitude = String.valueOf(citys.getLongitude());
			mLatitude = String.valueOf(citys.getLatitude());
		} else {
			mLongitude = preferences.getString(CustConst.Key.CITY_LONG, null);
			mLatitude = preferences.getString(CustConst.Key.CITY_LAT, null);
		}
		
		mLyView = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) v.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) v.findViewById(R.id.prog_nodata);
		mLvCouponEffect = (XListView) v.findViewById(R.id.lv_coupone_effect);
		mLvCouponEffect.setPullLoadEnable(true);
		mLvCouponEffect.setXListViewListener(this);
		mHandler = new Handler();
		getMyAvailableCoupon();
		mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.index_swipe_refresh);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.red);
		mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
	}

	/**
	 * 获取用户已领的优惠券列表
	 * 
	 * @param flag
	 * @param status
	 * @param
	 */
	public void getMyAvailableCoupon() {
		// 获得一个用户信息对象
		if (mPage <= 1 && mFirstFlag) {
			mFirstFlag = false;
			ViewSolveUtils.setNoData(mLvCouponEffect, mLyView, mIvView, mProgView, CustConst.DATA.LOADIMG);
		}
		new GetMyAvailableCouponTask(getMyActivity(), new GetMyAvailableCouponTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mDataFlag = true;
				mDataUpFlag = true;
				mLvCouponEffect.stopLoadMore(); // 停止上拉刷新
				mSwipeRefreshLayout.setRefreshing(false); // 停止刷新
				if (result == null) {
					// 处理没有数据
					ViewSolveUtils.morePageOne(mLvCouponEffect, mLyView, mIvView, mProgView, mPage);
				} else {
					Log.d(TAG, "listUserCouponByStatus >>> " + result.toString());
					ViewSolveUtils.setNoData(mLvCouponEffect, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA);
					mLvCouponEffect.setPullLoadEnable(true);
					PageData page = new PageData(result, "userCouponList", new TypeToken<List<BatchCoupon>>() {
					}.getType());
					mPage = page.getPage();
					if (page.hasNextPage() == false) {
						if (mPage > 1) {
							Util.getContentValidate(R.string.toast_moredata);
						}
						mLvCouponEffect.setPullLoadEnable(false);
					} else {
						mLvCouponEffect.setPullLoadEnable(true);                       
					}
					List<BatchCoupon> list = (List<BatchCoupon>) page.getList();
					if (null == list || list.size() <= 0) {
						ViewSolveUtils.morePageOne(mLvCouponEffect, mLyView, mIvView, mProgView, mPage);
					} else {
						ViewSolveUtils
								.setNoData(mLvCouponEffect, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA);
						if (mEffectAdapter == null) {
							mEffectAdapter = new CouponEffectCardAdapter(getMyActivity(), list);
							mLvCouponEffect.setAdapter(mEffectAdapter);
						} else {
							if (page.getPage() == 1) {
								mEffectAdapter.setItems(list);
							} else {
								mEffectAdapter.addItems(list);
							}
						}
					}
					try {
						mPage = Integer.parseInt(result.get("nextPage").toString());
						Log.d(TAG, "Home >>> mPage ," + mPage);
					} catch (Exception e) {
						Log.e(TAG, "mPage >>> error ," + e.getMessage());
					}
				}

			}
		}).execute(String.valueOf(Util.NUM_ONE), String.valueOf(mPage), mLongitude, mLatitude);
	}

	/**
	 * 下拉加载
	 */
	private OnRefreshListener refreshListener = new OnRefreshListener() {

		@Override
		public void onRefresh() {

			if (mDataFlag) {
				mDataFlag = false;
				mHandler.postDelayed(new Runnable() {
					public void run() {
						mPage = 1;
						getMyAvailableCoupon();
					}
				}, 5000);
			}
		}
	};
	
	/**
	 * 上拉
	 */
	@Override
	public void onLoadMore() {
		if (mDataUpFlag) {
			mDataFlag = false;
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					getMyAvailableCoupon();
				}
			}, 2000);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(CouponEffectFragment.class.getSimpleName()); // 统计页面
		mPage = 1;
		getMyAvailableCoupon();
	};
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(CouponEffectFragment.class.getSimpleName());
	}
}
