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
import com.huift.hfq.cust.model.ListCommonShopTask;
import com.lidroid.xutils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 足迹商家的列表信息
 * @author qian.zhou
 */
public class MyCommonListFragment extends Fragment implements IXListViewListener {
	private static final String TAG = "MyCommonListFragment";
	/** 足迹商家详情 **/ 
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
	/** 上拉请求api*/
	private boolean mDataUpFlag;
	/** 下拉请求api*/
	private boolean mDataFlag;
	/** 下拉加载 */
	private SwipeRefreshLayout mSwipeRefreshLayout;
	
	public static MyCommonListFragment newInstance() {
		Bundle args = new Bundle();
		MyCommonListFragment fragment = new MyCommonListFragment();
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

	/**
	 * 初始化方法
	 * @param view
	 */
	private void init(View view) {
		mDataFlag = true;
		mDataUpFlag = true;
		SharedPreferences preferences = getMyActivity().getSharedPreferences(CustConst.Key.CITY_OBJ, Context.MODE_PRIVATE);
		String cityName = preferences.getString(CustConst.Key.CITY_NAME, null);
		Log.d("TAG", "取出DB的定位城市为 ：：：：：：： " + cityName);
		if (Util.isEmpty(cityName)) {
			Citys citys = DB.getObj(HomeFragment.CITYS, Citys.class);
			mLongitude = String.valueOf(citys.getLongitude());
			mLatitude = String.valueOf(citys.getLatitude());
			Log.d("TAG", "取出DB的定位城市的经度为 ：：：：：：： " + mLongitude);
			Log.d("TAG", "取出DB的定位城市的纬度为 ：：：：：：： " + mLatitude);
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
		// 查询一般的商家
		myCardCommon();
		mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.index_swipe_refresh);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.red);
		mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
	}

	/**
	 * 一般商家
	 */
	public void myCardCommon() {
		if (mDataFlag && mPage <= 1) {
			ViewSolveUtils.setNoData(mLvCommon, mLyView, mIvView, mProgView, CustConst.DATA.LOADIMG); // 正在加载
		}
		new ListCommonShopTask(getMyActivity(), new ListCommonShopTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mDataFlag = true;
				mDataUpFlag = true;
				mLvCommon.stopLoadMore(); // 上拉加载完成
				mSwipeRefreshLayout.setRefreshing(false);
				
				if (null == result) {
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
					List<Shop> list = (List<Shop>)page.getList();
					if (null == list || list.size() <= 0) {
						ViewSolveUtils.morePageOne(mLvCommon, mLyView, mIvView, mProgView, mPage);
					} else {
						ViewSolveUtils.setNoData(mLvCommon, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA); // 有数据
						if (null == mAdapter) {
							mAdapter = new HomeShopListAdapter(getMyActivity(),list);
							mLvCommon.setAdapter(mAdapter);
						} else {
							if (page.getPage() == 1) {
								mAdapter.setItems(list);
							} else {
								mAdapter.addItems(list);
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
		}).execute(mLongitude, mLatitude,String.valueOf(mPage));
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
						myCardCommon();
					}
				}, 5000);
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
					myCardCommon();
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
		MobclickAgent.onPageEnd(MyCommonListFragment.class.getSimpleName()); 
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(MyCommonListFragment.class.getSimpleName()); //统计页面
	}
}
