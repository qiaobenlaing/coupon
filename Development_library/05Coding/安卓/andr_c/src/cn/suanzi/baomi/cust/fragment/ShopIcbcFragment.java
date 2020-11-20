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
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.Citys;
import cn.suanzi.baomi.base.pojo.PageData;
import cn.suanzi.baomi.base.pojo.Shop;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.ViewSolveUtils;
import cn.suanzi.baomi.base.view.XListView;
import cn.suanzi.baomi.base.view.XListView.IXListViewListener;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.adapter.HomeShopIcbcAdapter;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.model.ShopListTask;
import cn.suanzi.baomi.cust.model.ShopListTask.Callback;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 工银特惠
 * @author yanfang.li
 */
public class ShopIcbcFragment extends Fragment implements IXListViewListener {
	
	private static final String TAG = ShopIcbcFragment.class.getSimpleName();
	/** 列表 */
	private XListView mLvShop;
	/** 没有数据加载 */
	private LinearLayout mLyView;
	/** 没有数据加载的图片 */
	private ImageView mIvView;
	/** 正在加载的进度条 */
	private ProgressBar mProgView;
	/** 下拉加载 */
	private SwipeRefreshLayout mSwipeRefreshLayout;
	/** 线程 **/
	private Handler mHandler;
	/** 适配器*/
	private HomeShopIcbcAdapter mHomeShopIcbcAdapter;
	/** 页码*/
	private int mPage;
	/** 经度*/
	private String mLongitude;
	/** 纬度*/
	private String mLatitude;
	/** 当前的城市*/
	private  String mCitysName;
	/** 第一次请求api*/
	private  boolean mFirstRunFlag;
	/** 判断api是否调用成功 下拉 */
	private boolean mFlagUpData = false;
	/** 上拉 */
	private boolean mFlagData = false;
	
	/**
	 * 需要传递参数时有利于解耦
	 */
	public static ShopIcbcFragment newInstance() {
		Bundle args = new Bundle();
		ShopIcbcFragment fragment = new ShopIcbcFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_shop_icbc, container, false);
		ViewUtils.inject(this, view);
		findView(view);
		init(view);
		getShopList();// 全查询
		return view;
	}
	
	
	/**
	 * 获取控件
	 * @param v 视图
	 */
	private void findView(View v) {
		// 标题
		TextView tvMidContent = (TextView) v.findViewById(R.id.tv_mid_content);
		tvMidContent.setText(Util.getString(R.string.shop_icbc));
		mLvShop = (XListView) v.findViewById(R.id.listView);
		mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.index_swipe_refresh);
		mLyView = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) v.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) v.findViewById(R.id.prog_nodata);
		mIvView.setImageResource(R.drawable.icbc_nodata);
	}
	
	/**
	 * 初始化数据
	 * @param v 视图
	 */
	private void init(View v) {
		mFlagData = true;
		mFlagUpData = true;
		mFirstRunFlag = true;
		// 获取值
		SharedPreferences preferences = getMyActivity().getSharedPreferences(CustConst.Key.CITY_OBJ,Context.MODE_PRIVATE);
		String cityName = preferences.getString(CustConst.Key.CITY_NAME, null);
		Log.d(TAG, "取出DB的定位城市为 ：：：：：：： " + cityName);
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
		// 上拉刷新
		mLvShop.setPullLoadEnable(true);
		mLvShop.setXListViewListener(this);
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
						getShopList();
					}
				}, 5000);
			}
		}
	};

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
	 * 列表
	 */
	public void getShopList() {
		String searchWord = ""; // 关键字
		String type = "0"; // 所有类型
		String moduleValue = "0"; // 模板号
		String content = ""; // 与模板号对应的内容 ，模板号为0 此值为空
		String order = "0"; // 排序
		String filter = "3"; // 筛选 3为工行折扣
		String params[] = {searchWord, type, mLongitude, mLatitude, mCitysName, String.valueOf(mPage),moduleValue,content,order,filter};
		if (mFirstRunFlag && mPage <= 1) {
			ViewSolveUtils.setNoData(mLvShop, mLyView, mIvView, mProgView, CustConst.DATA.LOADIMG); // 正在加载
		}
		// 获得一个用户信息对象
		new ShopListTask(getMyActivity(), new Callback() {
			@Override
			public void getResult(JSONObject result) {
				mFirstRunFlag = false;
				mFlagData = true;
				mFlagUpData = true;
				mLvShop.stopLoadMore();
				mSwipeRefreshLayout.setRefreshing(false); // 上拉加载完成
				if (result == null) {
					mLvShop.setPullLoadEnable(false);
					ViewSolveUtils.morePageOne(mLvShop, mLyView, mIvView, mProgView, mPage);
				} else {
					Log.d(TAG, "result shopInfo >>> " + result.toString());
					ViewSolveUtils.setNoData(mLvShop, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA); // 有数据
					mLvShop.setPullLoadEnable(true);
					PageData page = new PageData(result, "shopList", new TypeToken<List<Shop>>() {
					}.getType());
					mPage = page.getPage();
					if (page.hasNextPage() == false) {
						if (page.getPage() > 1) {
							Util.getContentValidate(R.string.no_more);
						}
						mLvShop.setPullLoadEnable(false);
					} else {
						mLvShop.setPullLoadEnable(true);
					}
					List<Shop> list = (List<Shop>) page.getList();
					if (null == list || list.size() <= 0) {
						ViewSolveUtils.morePageOne(mLvShop, mLyView, mIvView, mProgView, mPage);
					} else {
						ViewSolveUtils.setNoData(mLvShop, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA); // 有数据
					}

					if (mHomeShopIcbcAdapter == null) {
						mHomeShopIcbcAdapter = new HomeShopIcbcAdapter(getMyActivity(), list);
						mLvShop.setAdapter(mHomeShopIcbcAdapter);
					} else {
						if (page.getPage() == 1) {
							mHomeShopIcbcAdapter.setItems(list);
						} else {
							mHomeShopIcbcAdapter.addItems(list);
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
		}).execute(params);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 点击返回查看到活动列表
	 */
	@OnClick(R.id.iv_turn_in)
	public void tvBackClick(View view) {
		getMyActivity().finish();
	}

	/**
	 * 友盟统计
	 */
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(ShopIcbcFragment.class.getSimpleName());
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(ShopIcbcFragment.class.getSimpleName()); // 统计页面
	}


	/**
	 * 上拉
	 */
	@Override
	public void onLoadMore() {
		if (mFlagData) {
			mFlagData = false;
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					getShopList();
				}
			}, 2000);
		}
	}
}