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
import com.huift.hfq.base.pojo.Citys;
import com.huift.hfq.base.pojo.PageData;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.base.view.XListView;
import com.huift.hfq.base.view.XListView.IXListViewListener;
import com.huift.hfq.cust.R;

import com.google.gson.reflect.TypeToken;
import com.huift.hfq.cust.adapter.HomeShopListAdapter;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.model.ListNearShopTask;
import com.lidroid.xutils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

/**
 *附近商家列表信息
 * @author qian.zhou
 */
public class MyNearlyListFragment extends Fragment implements IXListViewListener {
	
	private static final String TAG = MyNearlyListFragment.class.getSimpleName();
	/** 一般商家会员卡详情 **/
	private XListView mLvCommon;
	/** 页码 **/
	private int mPage = Util.NUM_ONE;
	/** 适配器 */
	private HomeShopListAdapter mAdapter;
	private Handler mHandler;
	/** 经度*/
	private String mLongitude;
	/** 纬度*/
	private String mLatitude;
	/** 没有数据加载*/
	private LinearLayout mLyView;
	/** 没有数据加载的图片*/
	private ImageView mIvView ;
	/** 正在加载的进度条*/
	private ProgressBar mProgView;
	/** 城市名称*/
	private String mCityName;
	/** 上拉请求api*/
	private boolean mDataUpFlag;
	/** 下拉请求api*/
	private boolean mDataFlag;
	/** 下拉加载 */
	private SwipeRefreshLayout mSwipeRefreshLayout;
	
	/**
	 * 需要传递参数时有利于解耦
	 * @return MyStaffFragment
	 */
	public static MyNearlyListFragment newInstance() {
		Bundle args = new Bundle();
		MyNearlyListFragment fragment = new MyNearlyListFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_mymcardlist, container, false);
		ViewUtils.inject(this, v);
		Util.addActivity(getMyActivity());
		Util.addLoginActivity(getMyActivity());
		init(v);
		return v;
	}
	
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}
	
	// 初始化方法
	private void init(View view) {
		mDataFlag = true;
		mDataUpFlag = true;
		SharedPreferences preferences = getMyActivity().getSharedPreferences(CustConst.Key.CITY_OBJ, Context.MODE_PRIVATE);
		mCityName = preferences.getString(CustConst.Key.CITY_NAME, null);
		Log.d("TAG", "取出DB的定位城市为 ：：：：：：： " + mCityName);
		if (Util.isEmpty(mCityName)) {
			Citys citys = DB.getObj(HomeFragment.CITYS, Citys.class);
			mLongitude = String.valueOf(citys.getLongitude());
			mLatitude = String.valueOf(citys.getLatitude());
		} else {
			mLongitude = preferences.getString(CustConst.Key.CITY_LONG, null);
			mLatitude = preferences.getString(CustConst.Key.CITY_LAT, null);
		}
		mLvCommon = (XListView) view.findViewById(R.id.lv_common_merchant);
		mLvCommon.setPullLoadEnable(true);
		mLvCommon.setXListViewListener(this);
		mHandler = new Handler();
		
		mLyView = (LinearLayout) view.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) view.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) view.findViewById(R.id.prog_nodata);
		mycardcommon();// 查询一般的商家
		mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.index_swipe_refresh);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.red);
		mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
	}

	/**
	 * 附近商家列表
	 */
	public void mycardcommon() {
		if (mDataFlag && mPage <=1  ) {
			ViewSolveUtils.setNoData(mLvCommon, mLyView, mIvView, mProgView, CustConst.DATA.LOADIMG); // 正在加载
		}
		new ListNearShopTask(getMyActivity(), new ListNearShopTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mDataFlag = true;
				mDataUpFlag = true;
				mLvCommon.stopLoadMore(); // 上拉加载完成
				mSwipeRefreshLayout.setRefreshing(false);
				
				if (result == null) {
					ViewSolveUtils.morePageOne(mLvCommon, mLyView, mIvView, mProgView, mPage);
				} else {
					ViewSolveUtils.setNoData(mLvCommon, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA); // 有数据
					mLvCommon.setPullLoadEnable(true);
					PageData page = new PageData(result, "shopList", new TypeToken<List<Shop>>() {}.getType());
					mPage = page.getPage();
					Log.d(TAG, "page=" + mPage);
					if (page.hasNextPage() == false) {
						if (page.getPage() > 1) {
							Util.getContentValidate(R.string.no_more);
						}
						mLvCommon.setPullLoadEnable(false);
					} else {
						mLvCommon.setPullLoadEnable(true);
					}
					List<Shop> shopList = (List<Shop>)page.getList();
					if (null == shopList || shopList.size() <= 0) {
						ViewSolveUtils.morePageOne(mLvCommon, mLyView, mIvView, mProgView, mPage);
					} else {
						ViewSolveUtils.setNoData(mLvCommon, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA); // 有数据
						if (mAdapter == null) {
							mAdapter = new HomeShopListAdapter(getMyActivity(), shopList);
							mLvCommon.setAdapter(mAdapter);
						} else{
							if (page.getPage() == 1) {
								mAdapter.setItems(shopList);
							} else {
								mAdapter.addItems(shopList);
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
		}).execute(mCityName, mLongitude, mLatitude,String.valueOf(mPage));
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	/**
	 * 下拉刷新
	 */
	OnRefreshListener refreshListener = new OnRefreshListener() {
		@Override
		public void onRefresh() {
			if (mDataFlag) {
				mDataFlag = false;
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						mPage = 1;
						mycardcommon();
					}
				}, 2000);
			}
		}
	};

	/**
	 * 上拉加载
	 */
	@Override
	public void onLoadMore() {
		if (mDataUpFlag) {
			mDataUpFlag = false;
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mycardcommon();
				}
			}, 2000);
		}
	}

	/**
	 * 友盟统计
	 */
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(MyNearlyListFragment.class.getSimpleName()); 
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(MyNearlyListFragment.class.getSimpleName()); //统计页面
	}
}
