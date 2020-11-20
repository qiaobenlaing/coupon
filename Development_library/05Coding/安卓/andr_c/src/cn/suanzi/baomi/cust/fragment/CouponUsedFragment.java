//---------------------------------------------------------
//@author    yanfang.li
//@version   1.0.0
//@createTime 2015.6.2
//@copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
//---------------------------------------------------------
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import cn.suanzi.baomi.cust.adapter.CouponUsedCardAdapter;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.model.GetMyAvailableCouponTask;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 优惠券历史页面 -> 已使用
 * @author yanfang.li
 */
public class CouponUsedFragment extends Fragment implements IXListViewListener {

	private final static String TAG = CouponUsedFragment.class.getSimpleName();
	private static final int type = 2; //  优惠券类型， 3-> 已失效  2->已使用
	
	private int mPage = 1;
	/** 历史优惠券列表 **/
	private XListView mLvHistory;
	private View mView;
	/** 適配器*/
	private CouponUsedCardAdapter mCardAdapter;
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
	/** 上拉刷新*/
	private boolean mDataUpFlag;
	/** 下拉刷新*/
	private boolean mDataFlag;
	/** 第一次运行*/
	private boolean mFirstFlag = true;
	/** 下拉加载 */
	private SwipeRefreshLayout mSwipeRefreshLayout;
	
	public static CouponUsedFragment newInstance() {
		Bundle args = new Bundle();
		CouponUsedFragment mFragment = new CouponUsedFragment();
		mFragment.setArguments(args);
		return mFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_couponhistory_list, container, false);
		ViewUtils.inject(this, mView);
		init();
		Util.addLoginActivity(getMyActivity());
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
	private void init() {
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
		
		mLyView = (LinearLayout) mView.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) mView.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) mView.findViewById(R.id.prog_nodata);
		mLvHistory = (XListView) mView.findViewById(R.id.lv_coupone_history);
		mLvHistory.setPullLoadEnable(true);
		mLvHistory.setXListViewListener(this);
		mHandler = new Handler();
		// 调用异步任务类
		getMyAvailableCoupon();
		mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.index_swipe_refresh);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.red);
		mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
		
	}
	
	/**
	 * 获取用户已领的优惠券列表
	 * @param flag 
	 * @param status 
	 * @param  
	 */
	public void getMyAvailableCoupon() {
		// 获得一个用户信息对象
		if (mFirstFlag && mPage <=1) {
			mFirstFlag = false;
			ViewSolveUtils.setNoData(mLvHistory, mLyView, mIvView, mProgView, CustConst.DATA.LOADIMG);
		}
		new GetMyAvailableCouponTask(getMyActivity(), new GetMyAvailableCouponTask.Callback() {
			
			@Override
			public void getResult(JSONObject result) {
				mLvHistory.stopLoadMore(); // 停止上拉
				mSwipeRefreshLayout.setRefreshing(false);// 停止下拉刷新
				mDataUpFlag = true;
				mDataFlag = true;
				if (result == null) {
					// 处理没有数据
					ViewSolveUtils.morePageOne(mLvHistory, mLyView, mIvView, mProgView, mPage);
				} else {
					ViewSolveUtils.setNoData(mLvHistory, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA);
					mLvHistory.setPullLoadEnable(true);
					PageData page = new PageData(result, "userCouponList", new TypeToken<List<BatchCoupon>>() {
					}.getType());
					mPage = page.getPage();
					if (page.hasNextPage() == false) {
						if (page.getPage() > 1) {
							Util.getContentValidate(R.string.toast_moredata);
						}
						mLvHistory.setPullLoadEnable(false);
					} else {
						mLvHistory.setPullLoadEnable(true);
					}
					List<BatchCoupon> list = (List<BatchCoupon>) page.getList();
					if (null == list || list.size() <= 0) {
						ViewSolveUtils.morePageOne(mLvHistory, mLyView, mIvView, mProgView, mPage);
					} else {
						ViewSolveUtils
								.setNoData(mLvHistory, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA);
						if (mCardAdapter == null) {
							mCardAdapter = new CouponUsedCardAdapter(getMyActivity(), list);
							mLvHistory.setAdapter(mCardAdapter);
						} else {
							if (page.getPage() == 1) {
								mCardAdapter.setItems(list);
							} else {
								mCardAdapter.addItems(list);
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
		}).execute(String.valueOf(type),String.valueOf(mPage),mLongitude, mLatitude);
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
	
	@Override
	public void onLoadMore() {
		if (mDataUpFlag) {
			mDataUpFlag = false;
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
		mPage = 1;
		getMyAvailableCoupon();
		MobclickAgent.onPageStart(CouponUsedFragment.class.getSimpleName()); // 统计页面
	};
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(CouponUsedFragment.class.getSimpleName());
	}
}
