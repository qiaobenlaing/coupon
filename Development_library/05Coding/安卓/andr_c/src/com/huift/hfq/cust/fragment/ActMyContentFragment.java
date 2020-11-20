package com.huift.hfq.cust.fragment;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.Citys;
import com.huift.hfq.base.pojo.ListUserAct;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.base.view.XListView;
import com.huift.hfq.base.view.XListView.IXListViewListener;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.activity.ActIcBcDetailActivity;
import com.huift.hfq.cust.activity.LoginActivity;
import com.huift.hfq.cust.adapter.ListUserActAdapter;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.model.ListUserActTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的活动列表
 * @author wensi.yu
 */
public class ActMyContentFragment extends Fragment implements IXListViewListener{
	
	private static final String TAG = "ActMyContentFragment";
	
	/** 活动的标题*/
	@ViewInject(R.id.tv_mid_content)
	private TextView mTvActContentTitle;
	/** 返回*/
	@ViewInject(R.id.iv_turn_in)
	private ImageView mTvBack;
	/** 活动列表*/
	private XListView mLvListUserAct;
	/** 页码 **/
	private int mPage = 1;
	/** 背景加载的图片*/
	private LinearLayout mLyView;
	private ImageView mIvView ;
	private ProgressBar mProgView;
	/** 经纬度*/
	private String mLongitude;
	private String mLatitude;
	private String mCitysName;
	/** 登录判断*/
	private boolean mLoginFlag;
	private UserToken mUserToken;
	private String mUserCode;
	private Handler mHandler;
	private ListUserActAdapter queryAdapter = null;
	/** 上拉请求api*/
	private boolean mDataUpFlag;
	/** 下拉请求api*/
	private boolean mDataFlag;
	/** 下拉加载 */
	private SwipeRefreshLayout mSwipeRefreshLayout;
	
	public static ActMyContentFragment newInstance() {
		Bundle args = new Bundle();
		ActMyContentFragment fragment = new ActMyContentFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_actmycontent, container, false);
		ViewUtils.inject(this, v);
		ActivityUtils.add(getActivity());
		init(v);
		return v;
	}
	
	/**
	 * 保证activity不为空
	 * @return activity
	 */
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	/**
	 * 初始化
	 * @param v
	 */
	private void init(View v) {
		mDataFlag = true;
		mDataUpFlag = true;
		mTvActContentTitle.setText(R.string.actmycontent_head);
		mLyView = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) v.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) v.findViewById(R.id.prog_nodata);
		
		//获得经纬度
		SharedPreferences preferences = getActivity().getSharedPreferences(CustConst.Key.CITY_OBJ, Context.MODE_PRIVATE);
		String cityName = preferences.getString(CustConst.Key.CITY_NAME, null);
		Log.d("TAG", "取出DB的定位城市为 ：：：：：：： " + cityName);
		if (Util.isEmpty(cityName)) {
			Citys citys = DB.getObj(HomeFragment.CITYS, Citys.class);
			mLongitude = String.valueOf(citys.getLongitude());
			mLatitude = String.valueOf(citys.getLatitude());
			mCitysName = "";
		} else {
			mLongitude = preferences.getString(CustConst.Key.CITY_LONG, null);
			mLatitude = preferences.getString(CustConst.Key.CITY_LAT, null);
			mCitysName = cityName;
		}
		
		mHandler = new Handler();	
		//活动内容的列表
		mLvListUserAct = (XListView) v.findViewById(R.id.lv_actmycontent_list);
		mLvListUserAct.setPullLoadEnable(true);
		mLvListUserAct.setXListViewListener(this);
		//刷新
		mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.index_swipe_refresh);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.red);
		mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
		//判断是否登录
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mLoginFlag = DB.getBoolean(DB.Key.CUST_LOGIN);
		if (mLoginFlag) {
			mUserCode = mUserToken.getUserCode();
		} else {
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			getActivity().startActivity(intent);
			getActivity().finish();
		}
		//获得活动列表
		listUserAct();
		//给列表添加事件
		mLvListUserAct.setOnItemClickListener(listener);
	}
	
	/**
	 * 给Listview添加点击事件
	 */
	OnItemClickListener listener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,long id) {	
			String typeflag = "1"; 
			ListUserAct item = (ListUserAct) mLvListUserAct.getItemAtPosition(position);
			Intent intent = new Intent(getActivity(), ActIcBcDetailActivity.class);
			intent.putExtra(ActIcBcDetailFragment.newInstance().ACTIVITY_CODE, item.getActivityCode());
			intent.putExtra(ActIcBcDetailFragment.newInstance().TYPE, typeflag);
			DB.saveStr(CustConst.JOIN.USERACT_CODE, item.getUserActivityCode());
			getActivity().startActivity(intent);
		}
	};
	
	/**
	 * 获得顾客报名的活动列表
	 */
	public void listUserAct(){
		if (mDataFlag && mPage <= 1) {
			ViewSolveUtils.setNoData(mLvListUserAct, mLyView, mIvView, mProgView, CustConst.DATA.LOADIMG); // 正在加载
		}
		new ListUserActTask(getMyActivity(),new ListUserActTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mDataFlag = true;
				mDataUpFlag = true;
				mLvListUserAct.stopLoadMore(); // 上拉加载完成
				mSwipeRefreshLayout.setRefreshing(false);
				
				if (null == result) {
					// 处理没有数据
					ViewSolveUtils.morePageOne(mLvListUserAct, mLyView, mIvView, mProgView, mPage);
				} else {
					// 有数据
					ViewSolveUtils.setNoData(mLvListUserAct, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA);
					mLvListUserAct.setPullLoadEnable(true);
					
					JSONArray mActListArray = (JSONArray) result.get("userActList");
					// 总记录数
					int totalCount = (Integer.parseInt(result.get("totalCount").toString()));
					// 当前页数
					int page = (Integer.parseInt(result.get("page").toString()));
					// 显示记录数
					int count = (Integer.parseInt(result.get("count").toString()));
					
					if (page == totalCount) {
						mLvListUserAct.setPullLoadEnable(false);
					}
					
					if (count < CustConst.PAGE_NUM) {
						mLvListUserAct.setPullLoadEnable(false);
					}
					
					List<ListUserAct> mActListData = new ArrayList<ListUserAct>();
					ListUserAct item = null;
					for (int i = 0; i < mActListArray.size(); i++) {
						JSONObject actListObject = (JSONObject) mActListArray.get(i);
						item = Util.json2Obj(actListObject.toString(), ListUserAct.class);
						mActListData.add(item);
					}
					
					if (null == mActListData || mActListData.size() <= 0) {
						ViewSolveUtils.morePageOne(mLvListUserAct, mLyView, mIvView, mProgView, mPage);
					} else {
						ViewSolveUtils.setNoData(mLvListUserAct, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA);
						
						if (queryAdapter == null) {
							queryAdapter = new ListUserActAdapter(getActivity(),mActListData);
							mLvListUserAct.setAdapter(queryAdapter);
						} else {
							if (mPage == 1) {
								queryAdapter.setItems(mActListData);
							} else {
								queryAdapter.addItems(mActListData);
							}
						}
					}
				}
			}
		}).execute(mUserCode,mLongitude,mLatitude,String.valueOf(mPage));
	}
	
	/**
	 * 返回
	 * @param view
	 */
	@OnClick(R.id.iv_turn_in)
	public void trunIdenCode(View view) {
		getMyActivity().finish();
	}
	
	/**
	 * 下来刷新
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
						listUserAct();
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
					mPage++;
					listUserAct();
				}
			}, 2000);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(ActMyContentFragment.class.getSimpleName()); 
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MainScreen"); //统计页面
		MobclickAgent.onPageEnd(ActMyContentFragment.class.getSimpleName());
	}
}
