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
import android.text.Editable;
import android.text.TextWatcher;
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
import com.huift.hfq.cust.model.ListFollowedShopTask;
import com.lidroid.xutils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 关注商家的会员卡列表
 * @author qian.zhou
 */
public class MyFocusListFragment extends Fragment implements IXListViewListener , TextWatcher{
	private static final String TAG = "MyFocusListFragment";
	/** 关注商家会员卡详情 **/
	private XListView mLvFocus;
	/** 页码 **/
	private int mPage = Util.NUM_ONE;
	/** 判断Fragment的状态 **/
	static MyFocusListFragment mInstance = null;
	/** 适配器 */
	private HomeShopListAdapter mAdapter = null;
	private Handler mHandler;
	/** 纬度*/
	private String mLongitude;
	/** 经度*/
	private String mLatitude;
	/** 没有数据加载*/
	private LinearLayout mLyView;
	/** 没有数据加载的图片*/
	private ImageView mIvView ;
	/** 正在加载的进度条*/
	private ProgressBar mProgView;
	/** 上拉请求api */
	private boolean mDataFlag;
	/** 判断api是否调用成功 下拉 */
	private boolean mFlagUpData = false;
	/** 判断用户是否关注或者取消了商家*/
	private boolean mUppFlag = false;
	/** 下拉加载 */
	private SwipeRefreshLayout mSwipeRefreshLayout;

	/**
	 * 需要传递参数时有利于解耦
	 * @return MyFocusListFragment
	 */
	public static MyFocusListFragment newInstance() {
		if (mInstance == null) {
			Bundle args = new Bundle();
			mInstance = new MyFocusListFragment();
			mInstance.setArguments(args);
		}
		return mInstance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_mywallet, container, false);
		ViewUtils.inject(this, v);
		Util.addActivity(getMyActivity());
		Util.addLoginActivity(getMyActivity());
		init(v);
		mywalletfocus();// 查询关注的商家
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
		mFlagUpData = true;
		SharedPreferences preferences = getMyActivity().getSharedPreferences(CustConst.Key.CITY_OBJ, Context.MODE_PRIVATE);
		String cityName = preferences.getString(CustConst.Key.CITY_NAME, null);
		Log.d(TAG, "取出DB的定位城市为 ：：：：：：： " + cityName);
		if (Util.isEmpty(cityName)) {
			Citys citys = DB.getObj(HomeFragment.CITYS, Citys.class);
			mLongitude = String.valueOf(citys.getLongitude());
			mLatitude = String.valueOf(citys.getLatitude());
			Log.d(TAG, "取出DB的定位城市的经度为 ：：：：：：： "        + mLongitude);
			Log.d(TAG, "取出DB的定位城市的纬度为 ：：：：：：： " + mLatitude);
		} else {
			mLongitude = preferences.getString(CustConst.Key.CITY_LONG, null);
			mLatitude = preferences.getString(CustConst.Key.CITY_LAT, null);
		}
		mLvFocus = (XListView) view.findViewById(R.id.lv_focus_merchant);
		mLyView = (LinearLayout) view.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) view.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) view.findViewById(R.id.prog_nodata);
		mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.index_swipe_refresh);
		
		mLvFocus.setPullLoadEnable(true); // 上拉刷新
		mLvFocus.setXListViewListener(this);
		mHandler = new Handler();
		mSwipeRefreshLayout.setColorSchemeResources(R.color.red);
		mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
	}
	
	/**
	 * 下拉加载
	 */
	private OnRefreshListener refreshListener = new OnRefreshListener() {
		@Override
		public void onRefresh() {
			if (mFlagUpData) {
				mFlagUpData = false;
				mHandler.postDelayed(new Runnable() {
					public void run() {
						mPage = 1;
						mywalletfocus();
					}
				}, 5000);
			}
		}
	};

	/**
	 * 查询关注商家
	 */
	public void mywalletfocus()//
	{
		if (mFlagUpData && mPage <= 1) {
			ViewSolveUtils.setNoData(mLvFocus, mLyView, mIvView, mProgView, CustConst.DATA.LOADIMG); // 正在加载
		}
		new ListFollowedShopTask(getMyActivity(), new ListFollowedShopTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mDataFlag = true;
				mFlagUpData = true;
				mLvFocus.stopLoadMore();
				mSwipeRefreshLayout.setRefreshing(false); // 上拉加载完成
				if (result == null) {
					mLvFocus.setPullLoadEnable(false);
					ViewSolveUtils.morePageOne(mLvFocus, mLyView, mIvView, mProgView, mPage);
				} else {
					ViewSolveUtils.setNoData(mLvFocus, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA); // 有数据
					mLvFocus.setPullLoadEnable(true);
					PageData page = new PageData(result,"shopList",new TypeToken<List<Shop>>() {}.getType());
					mPage = page.getPage();
					if (page.hasNextPage() == false) {            
						if (page.getPage() > 1) {
							Util.getContentValidate(R.string.no_more);
						}
						mLvFocus.setPullLoadEnable(false);
					}  else {
						mLvFocus.setPullLoadEnable(true);
					}
					List<Shop> list = (List<Shop>) page.getList();
					if (null == list || list.size() <= 0) {
						ViewSolveUtils.morePageOne(mLvFocus, mLyView, mIvView, mProgView, mPage);
					} else {
						ViewSolveUtils.setNoData(mLvFocus, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA); // 有数据
					}
					if (mAdapter == null) {
						mAdapter = new HomeShopListAdapter(getMyActivity(), list);
						mLvFocus.setAdapter(mAdapter);
					} else {
						if (page.getPage() == 1) {
							mAdapter.setItems(list);
						} else {
							mAdapter.addItems(list);
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
		}).execute(mLongitude, mLatitude, String.valueOf(mPage));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 上拉加载
	 */
	@Override
	public void onLoadMore() {
		if (mDataFlag) {
			mDataFlag = false;
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mywalletfocus();
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
		MobclickAgent.onPageEnd(MyFocusListFragment.class.getSimpleName()); 
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mUppFlag = DB.getBoolean(CustConst.Key.UPP_IS_FOCUS);
		Log.d(TAG, "flag11的状态为：：："  + mUppFlag);
		if (mUppFlag) {
			DB.saveBoolean(CustConst.Key.UPP_IS_FOCUS,false);
			mywalletfocus();
			Log.d(TAG, "flag22的状态为：：："  + mUppFlag);
		}
		MobclickAgent.onPageStart(MyFocusListFragment.class.getSimpleName()); //统计页面
	}

	@Override
	public void afterTextChanged(Editable s) {}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {}
}
