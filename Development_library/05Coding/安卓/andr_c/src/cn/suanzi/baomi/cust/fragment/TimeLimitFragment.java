package cn.suanzi.baomi.cust.fragment;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.BatchCoupon;
import cn.suanzi.baomi.base.pojo.Citys;
import cn.suanzi.baomi.base.pojo.PageData;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.ViewSolveUtils;
import cn.suanzi.baomi.base.view.XListView;
import cn.suanzi.baomi.base.view.XListView.IXListViewListener;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.adapter.TimeLimitAdapter;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.model.ListCouponTask;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 限时购
 * @author wensi.yu
 *
 */
public class TimeLimitFragment extends Fragment implements IXListViewListener{

	private final static String TAG = TimeLimitFragment.class.getSimpleName();
	/** 限时购的列表*/
	private XListView mLvTimeLimitList;
	/** 页码 **/
	private int mPage = 1;
	/** 搜索**/
	private AutoCompleteTextView mEtSearch;
	/** 背景显示的加载图片*/
	private LinearLayout mLyView;
	private ImageView mIvView ;
	private ProgressBar mProgView;
	/** 上拉请求api*/
	private boolean mDataUpFlag;
	/** 下拉请求api*/
	private boolean mDataFlag;
	/** 第一次运行*/
	private boolean mFirstFlag = true;
	/** 下拉加载 */
	private SwipeRefreshLayout mSwipeRefreshLayout;
	/** 经度 */
	private String mLongitude;
	/** 纬度*/
	private String mLatitude;
	private String mCitysName;
	/** 线程*/
	private Handler mHandler;
	/** 限时购的适配器*/
	private TimeLimitAdapter mQueryAdapter;
	
	public static TimeLimitFragment newInstance() {
		Bundle args = new Bundle();
		TimeLimitFragment fragment = new TimeLimitFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_timelimit, container, false);
		ViewUtils.inject(this, v);
		Util.addActivity(getMyActivity());
		Util.addLoginActivity(getMyActivity());
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
		mLyView = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) v.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) v.findViewById(R.id.prog_nodata);
		SharedPreferences preferences = getMyActivity().getSharedPreferences(CustConst.Key.CITY_OBJ, Context.MODE_PRIVATE);
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
		//设置标题
		TextView mIvBack = (TextView) v.findViewById(R.id.tv_area);
		mIvBack.setBackgroundResource(R.drawable.backup);
		mHandler = new Handler();	
		//优惠券的列表
		mLvTimeLimitList = (XListView) v.findViewById(R.id.lv_timelimit_list);
		//刷新
		mLvTimeLimitList.setPullLoadEnable(true); // 上拉刷新
		mLvTimeLimitList.setXListViewListener(this);//实现xListviewListener接口
		mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.index_swipe_refresh);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.red);
		mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
		mEtSearch = (AutoCompleteTextView) v.findViewById(R.id.edt_search);
		//优惠券的列表
		getListCoupon();
		//输入框的改变事件
		mEtSearch.findFocus();
		mEtSearch.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					//友盟统计
					MobclickAgent.onEvent(getMyActivity(), "timelimit_search_coupon");
					mPage = 1;
					getListCoupon();
				}
				return false;
			}
		});
	}
	
	private void getListCoupon () {
		// 0表示所有优惠券
		String couponType = "0"; 
		// 输入的关键字
		String etSearch = TextUtils.isEmpty(mEtSearch.getText()) ? "" : mEtSearch.getText().toString();
		String []params = {couponType,etSearch,mLongitude,mLatitude,String.valueOf(mPage),mCitysName};
		if (mFirstFlag && mPage <= 1) {
			mFirstFlag = false;
			ViewSolveUtils.setNoData(mLvTimeLimitList, mLyView, mIvView, mProgView, CustConst.DATA.LOADIMG);
		}
		new ListCouponTask(getMyActivity(), new ListCouponTask.Callback() {
			
			@Override
			public void getResult(JSONObject result) {
				mDataFlag = true;
				mDataUpFlag = true;
				mLvTimeLimitList.stopLoadMore(); // 上拉加载完成
				mSwipeRefreshLayout.setRefreshing(false);
				if (null == result) { 
					// 处理没有数据
					ViewSolveUtils.morePageOne(mLvTimeLimitList, mLyView, mIvView, mProgView, mPage);
					mLvTimeLimitList.setPullLoadEnable(false);
				} else {
					Log.d(TAG, "mLvTimeLimitList  >>> " + result.toString());
					// 有数据
					ViewSolveUtils.setNoData(mLvTimeLimitList, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA);
					mLvTimeLimitList.setPullLoadEnable(true);
					PageData page = new PageData(result,"couponList",new TypeToken<List<BatchCoupon>>() {}.getType());
					mPage = page.getPage();
					if (page.hasNextPage() == false) {
						if (mPage > 1) {
							Util.getContentValidate(R.string.no_more);
						}
						mLvTimeLimitList.setPullLoadEnable(false);
					} else {
						mLvTimeLimitList.setPullLoadEnable(true);
					}
					List<BatchCoupon> list = (List<BatchCoupon>)page.getList();
					Log.d(TAG, "list。。。。。。。。。。。。。。"+list.get(0).getShopName()+";"+list.size());
					
					if (null == list || list.size() <= 0) {
						ViewSolveUtils.morePageOne(mLvTimeLimitList, mLyView, mIvView, mProgView, mPage);
					} else {
						ViewSolveUtils.setNoData(mLvTimeLimitList, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA); // 有数据
						if (null == mQueryAdapter) {    
							mQueryAdapter = new TimeLimitAdapter(getMyActivity(), list);
							mLvTimeLimitList.setAdapter(mQueryAdapter);
						} else {
							if (page.getPage() == 1) {
								mQueryAdapter.setItems(list);
							} else {
								mQueryAdapter.addItems(list);
							}
						}
					}
					
					try {
						mPage = Integer.parseInt(result.get("nextPage").toString());
					} catch (Exception e) {
						Log.e(TAG, "获取下一页出错  >>> " +e.getMessage());
					}
				}
			}
		}).execute(params);
	}
	
	
	/**
	 * 下拉刷星
	 */
	private OnRefreshListener refreshListener = new OnRefreshListener() {
		@Override
		public void onRefresh() {
			if (mDataFlag) {
				mDataFlag = false;
				mHandler.postDelayed(new Runnable() {
					public void run() {
						mPage = 1;
						getListCoupon();
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
					getListCoupon();
				}
			}, 2000);
		}
	}

	/**
	 * 返回
	 * @param view
	 */
	@OnClick(R.id.tv_area)
	public void ivBank(View view) {
		getMyActivity().finish();
	}
	
	/**
	 * 友盟统计
	 */
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TimeLimitFragment.class.getSimpleName()); 
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(TimeLimitFragment.class.getSimpleName()); //统计页面
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
}
