//---------------------------------------------------------
//@author    Zhonghui.Dong
//@version   1.0.0
//@createTime 2015.6.2
//@copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
//---------------------------------------------------------
package com.huift.hfq.cust.fragment;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huift.hfq.base.Const;
import com.huift.hfq.base.SzApplication;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.adapter.CommenViewHolder;
import com.huift.hfq.base.adapter.CommonListViewAdapter;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.HomeModel;
import com.huift.hfq.base.model.TheadDBhelper;
import com.huift.hfq.base.pojo.AppUpdate;
import com.huift.hfq.base.pojo.Citys;
import com.huift.hfq.base.pojo.HomeNew;
import com.huift.hfq.base.pojo.HomePage;
import com.huift.hfq.base.pojo.HomeTemplate;
import com.huift.hfq.base.pojo.PageData;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.base.utils.LocationUtil;
import com.huift.hfq.base.utils.PhoneInfoUtils;
import com.huift.hfq.base.utils.SizeUtil;
import com.huift.hfq.base.view.XListView;
import com.huift.hfq.base.view.XListView.IXListViewListener;
import com.huift.hfq.cust.R;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huift.hfq.cust.activity.HomeActivity;
import com.huift.hfq.cust.activity.ScanActivity;
import com.huift.hfq.cust.adapter.HomeShopListAdapter;
import com.huift.hfq.cust.adapter.HomeSrollAdapter;
import com.huift.hfq.cust.adapter.ShopCircleAdapter;
import com.huift.hfq.cust.adapter.ShopTypeAdapter;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.model.CountUserNotUsedCouponTask;
import com.huift.hfq.cust.model.GetHomeInfoTask;
import com.huift.hfq.cust.model.GetHomeShopListTask;
import com.huift.hfq.cust.model.GetNewestClientAppVersionTask;
import com.huift.hfq.cust.model.GetShopCityTask;
import com.huift.hfq.cust.model.GetUserInfo;
import com.huift.hfq.cust.model.RecordUserAddressTask;
import com.huift.hfq.cust.service.UpdateService;
import com.huift.hfq.cust.util.CustUtil;
import com.huift.hfq.cust.util.SkipActivityUtil;
import com.lidroid.xutils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 首页显示界面
 * 
 * @author yanfang.li
 */
public class HomeFragment extends BaseFragment implements IXListViewListener {
	private static final String TAG = HomeFragment.class.getSimpleName();
	/** 优惠券消息张数提示提示 */
	private static final String KEY_CITY_FISRT_RUN = "homecitytips";
	/** 保存城市 */
	public static final String CITYS = "citys";
	/** 记录app升级 */
	private static final String UPP_APP = "1";
	/** 自动升级 */
	private static final String UPP_INFO = "uppinfo";
	/** 缓存 */
	private static final int CACHE = 0;
	/** 线上 */
	private static final int ONLINE = 1;
	/** 滚屏图片 */
	private ViewPager mViewPager;
	/** 优惠券列表 */
	private XListView mLvHome;
	/** 切换地区 */
	private TextView mTvArea;
	/** 切换地区 */
	private LinearLayout mLyArea;
	/** 滚屏图片数组 */
	private String[] mScrollPics;
	/** 经纬度 */
	private double mLongitude, mLatitude;
	/** 页码 */
	private int mPage = 1;
	/** 登陆判断 */
	private boolean mLoginFlag = false;
	/** 温馨提示 */
	private LinearLayout mLyHomeTips;
	/** 提示代码 */
	private TextView mTvHomeTips;
	/** 记录优惠券张数什么时候消失 */
	private int mTime = 0;
	/** 城市的数据源 */
	private List<Citys> mCityDates;
	/** 列表的底部 */
	private View mFooterView;
	/** 处理上拉加载的 */
	private Handler mHandler;
	/** 城市名称 */
	private String mCitysName;
	private View mView;
	/** 记录是否抢红包成功 */
	private boolean mFlagCoupon = false;
	/** 红包计时器 */
	private CountDownTimer mTimer;
	/** 滚动图片的适配器 */
	private List<ImageView> mImagesList;
	/** 滚屏图上对应的点 */
	private ImageView[] mPoints;
	/** 监测网络的标志 */
	private int mNetType = 1;
	/** 下拉加载 */
	private SwipeRefreshLayout mSwipeRefreshLayout;
	/** 滚屏的视图 */
	private View mViewScroll;
	/** 商家分类的视图 */
	private View mViewShopType;
	/** 商圈的视图 */
	private View mViewShopCircle;
	/** 商家活动的视图 */
	private View mViewShopAct;
	/** 商家列表 */
	private View mViewShopList;
	/** 视图所在位置的结合 */
	private List<View> mViewList;
	/** 商家适配器 */
	private HomeShopListAdapter mHomeShopListAdapter;
	/** “正在处理”进度条 */
	private ProgressDialog mProcessDialog = null;
	/** 切换地名的标志 */
	private boolean mFlagArea = false;
	/** 得到城市的字符串 */
	private String mCityResultString;
	/** 首页其他的数据 */
	private String mHomeResultString;
	/** 滚屏图片 当前显示页,从1开始 */
	private int mCurPosition = 1;
	/** 行数 */
	private int mLine = 0;

	// /** 版本必须更新*/
	// public boolean mUppFlag = false;

	public static HomeFragment newInstance() {
		Bundle args = new Bundle();
		HomeFragment fragment = new HomeFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void viewVisible() {
		super.viewVisible();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_home, container, false);
		ViewUtils.inject(this, mView);
		Util.addHomeActivity(getMyActivity());
		SzApplication.setCurrActivity(getMyActivity());
		findView(mView);
		init(mView);
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
	 * 首页信息
	 */
	private void getHomeInfo() {
		new GetHomeInfoTask(getMyActivity(), new GetHomeInfoTask.Callback() {

			@Override
			public void getResult(JSONArray result) {
				Log.d(TAG, "getHomeInfotest : type :☆☆☆getHomeInfos   228");
				mSwipeRefreshLayout.setRefreshing(false); // 上拉加载完成
				if (null != mProcessDialog && mFlagArea) {
					mProcessDialog.dismiss();
				}

				if (null != result) {
					mCityResultString = result.toString();
					Log.d(TAG, "getHomeInfo存 >>>>  1 " + mCityResultString);
					HomeModel.saveHomeFragment(mCityResultString, result.toString());
					List<HomePage> homePages = new Gson().fromJson(result.toString(), new TypeToken<List<HomePage>>() {
					}.getType());
					setHomeView(homePages, ONLINE); // 设置布局
				} else {
					if (mFlagArea) {
						DialogUtils.showDialogSingle(getActivity(), Util.getString(R.string.no_city_open), R.string.cue,
								R.string.ok, null);
					} else {
						// TODO 不用管
					}
				}
				mFlagArea = false;
			}
		}).execute(mCitysName);
	}

	/**
	 * 设置首页布局
	 * 
	 * @param homePages
	 *            布局的集合
	 */
	private void setHomeView(List<HomePage> homePages, int type) {
		if (null != homePages && homePages.size() > 0) {
			if (null != mViewList && mViewList.size() > 0) {
				for (int i = 0; i < mViewList.size(); i++) {
					try {
						View view = mViewList.get(i);
						mLvHome.removeHeaderView(view);
					} catch (Exception e) {
						Log.e(TAG, "mViewList >> 22" + e.getMessage());
					}
				}
				mViewList.clear();
			}
			Log.d(TAG, " homePages.size() :" + homePages.size());
			// 取值 加载headView的布局
			for (int i = 0; i < homePages.size(); i++) {
				HomePage homePage = homePages.get(i);
				if (homePage.getModuleValue() == CustConst.Home.SCROLL_VALUE) { // 滚屏
					mViewScroll = View.inflate(getMyActivity(), R.layout.head_home1, null);
					mViewList.add(homePage.getModulePosition() - 1, mViewScroll);
					try {
						setScrollInfo(homePage.getSubList(), homePage.getModuleValue() + "");
					} catch (Exception e) {
						Log.e(TAG, "mViewList >> setScrollInfo >>> " + e.getMessage());
					}
				} else if (homePage.getModuleValue() == CustConst.Home.SHOPTYPE_VALUE) { // 商家分类
					mViewShopType = View.inflate(getMyActivity(), R.layout.head_home2, null);
					mViewList.add(homePage.getModulePosition() - 1, mViewShopType);
					try {
						setShopType(homePage.getSubList(), homePage.getModuleValue() + "");
					} catch (Exception e) {
						Log.e(TAG, "mViewList >> setShopType >>> " + e.getMessage());
					}
				} else if (homePage.getModuleValue() == CustConst.Home.SHOPCIRCLE_VALUE) { // 商圈
					mViewShopCircle = View.inflate(getMyActivity(), R.layout.head_home3, null);
					mViewList.add(homePage.getModulePosition() - 1, mViewShopCircle);
					try {
						setShopCircle(homePage.getSubList(), homePage.getModuleValue() + ""); // 设置商圈
					} catch (Exception e) {
						Log.e(TAG, "mViewList >> setShopCircle >>> " + e.getMessage());
					}

				} else if (homePage.getModuleValue() == CustConst.Home.SHOPACT_VALUE
						|| homePage.getModuleValue() == CustConst.Home.FUNCTION_VALUE
						|| homePage.getModuleValue() == CustConst.Home.SHOPBRAND_VALUE) { // 活动,品牌,功能
					try {
						// 获取加载的活动布局模型
						String template = homePage.getTemplate(); // 类似 502
						// 拼接布局文件的名称 类似 head_home_act_502
						String actLayoutName = "head_home_act_" + template;
						// 获取布局文件的id
						int id = CustUtil.getResourceId(getActivity(), actLayoutName, "layout", "com.huift.hfq.cust");
						mViewShopAct = View.inflate(getActivity(), id, null);
						if (mViewShopAct != null) {
							mViewList.add(homePage.getModulePosition() - 1, mViewShopAct);
							setShopAct(homePage.getSubList(), homePage.getModuleValue() + "");
						} else {
							Log.d(TAG, "加载的活动模块的布局ERROR>>>>>>>>");
						}
					} catch (Exception e) {
						Log.e(TAG, "mViewList >> setShopAct >>> " + e.getMessage());
					}
				} else if (homePage.getModuleValue() == CustConst.Home.SHOPLIST_VALUE) { // 商家列表
					mViewShopList = View.inflate(getMyActivity(), R.layout.head_home6, null);
					try {
						mViewList.add(homePage.getModulePosition() - 1, mViewShopList);
					} catch (Exception e) {
						Log.e(TAG, "mViewList >> 11 >>> " + e.getMessage());
					}
					// 推荐商家
					TextView tvRecommendShop = (TextView) mViewShopList.findViewById(R.id.tv_recommend_shop);
					// 查询更多商家
					TextView tvEnterShop = (TextView) mViewShopList.findViewById(R.id.tv_enter_shop);
					tvRecommendShop.setText(homePage.getModuleTitle());
					if (!Util.isEmpty(homePage.getModuleTitleColor())) {
						tvRecommendShop.setTextColor(Color.parseColor(homePage.getModuleTitleColor()));
					}
					// tvEnterShop 进入店铺列表
					tvEnterShop.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							SkipActivityUtil.skipShopList();
						}
					});
					// 该方法被请求的api条用的时候才查询商家列表
					if (type == ONLINE) {
						getHomeShopList(); // 商家列表
					}
				}
			}

			// 加headView
			for (int i = 0; i < mViewList.size(); i++) {
				try {
					View view = mViewList.get(i);
					mLvHome.addHeaderView(view);
				} catch (Exception e) {
					Log.e(TAG, "mViewList >> 22" + e.getMessage());
				}
			}
		} else {
			// TODO 未处理
		}
	}

	/**
	 * 设置活动
	 * 
	 * @param subList
	 *            活动小模块集合
	 */
	public void setShopAct(List<HomeTemplate> subList, final String moduleValue) {
		Log.d(TAG, "集合的长度是多少：" + mViewList.size());
		// 获取活动的数量
		int actCount = subList.size();
		for (int i = 1; i < actCount + 1; i++) {
			// 拼接活动视图中每个RelativeLayout的id名称
			String rl_id_name = "rl_model_" + i;
			// 根据名称获取id
			int widgetId = CustUtil.getWidgetId(getMyActivity(), rl_id_name, "test.com.huift.hfq.cust");
			// 获取RelativeLayout
			RelativeLayout rl = (RelativeLayout) mViewShopAct.findViewById(widgetId);
			// rl.setBackgroundResource(getActivity().getResources().getColor(R.color.red));
			final HomeTemplate homeT = subList.get(i - 1);
			if (rl != null) {
				// ActImgUtil.showActImg(rl, getActivity(), homeT);
				View centerView = View.inflate(getMyActivity(), R.layout.act_img_center, null);
				ImageView imageView = (ImageView) centerView.findViewById(R.id.iv_act);
				Log.d(TAG, "mImageView : ");
				// 设置图片
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
				rl.addView(centerView, layoutParams);
				LayoutParams params = (LayoutParams) imageView.getLayoutParams();
				params.width = (int) (Util.getWindowWidthAndHeight(getMyActivity())[0] * homeT.getScreenRate());
				params.height = (int) (params.width / homeT.getImgRate());
				imageView.setLayoutParams(params);
				if (moduleValue.equals(CustConst.Home.SHOPBRAND_VALUE + "")) {
					Util.showFirstImages(getMyActivity(), homeT.getShowImg(), imageView);
				} else {
					Util.showFirstImages(getMyActivity(), homeT.getImgUrl(), imageView);
				}
			}

			rl.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					SkipActivityUtil.skipHomeActivity(getMyActivity(), homeT, moduleValue);

				}
			});
		}
	}

	/**
	 * 百度打包的嵌套代码
	 */
	// private void getNestBaidu() {
	// // TODO
	// BDAutoUpdateSDK.uiUpdateAction(getMyActivity(), new
	// UICheckUpdateCallback() {
	//
	// @Override
	// public void onCheckComplete() {
	//
	// }
	// });
	// }

	/**
	 * 版本升级
	 */
	public void isUpdate() {
		new GetNewestClientAppVersionTask(getMyActivity(), new GetNewestClientAppVersionTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (result == null) {
					Log.d(TAG, "我进了了..........");
					return;
				} else {
					AppUpdate update = Util.json2Obj(result.toString(), AppUpdate.class);
					Log.d(TAG, "update=" + update.getVersionCode());
					String currentVesion = Util.getAppVersionCode(getMyActivity());
					String newVersion = update.getVersionCode();
					SharedPreferences sharedPreferences = PreferenceManager
							.getDefaultSharedPreferences(getMyActivity());
					boolean uppFlag = true;
					try {
						uppFlag = sharedPreferences.getBoolean(DB.Key.CUST_CANCEL_UPDATE, true);
					} catch (Exception e) {
						return; // TODO
					}
					int version = newVersion.compareTo(currentVesion);
					Log.d(TAG, "version=" + version);
					// 服务器上新版本比现在app的版本高的话就提示升级
					if (version > 0) { // 线上比当前版本高
						update.setCanUpdate(1); // 有跟新类容
						String url = update.getUpdateUrl();
						if (UPP_APP.equals(update.getIsMustUpdate())) {
							// mUppFlag = true;
							// 必须更新
							UpdateService.show(getMyActivity(), url, Integer.parseInt("1"));
						} else {
							String uppCode = DB.getStr(UPP_INFO);
							if (null == uppCode || Util.isEmpty(uppCode)) {
								uppFlag = true;
							} else {
								if (newVersion.compareTo(uppCode) > 0) {
									uppFlag = true;
								} else {
									uppFlag = false;
								}
							}
							// 更新
							if (uppFlag) {
								DB.saveStr(UPP_INFO, newVersion);
								UpdateService.show(getMyActivity(), url, Integer.parseInt("0"));
							}
						}
					} else {
						update.setNewVersionCode(0); // 把int类型的版本号放进更新对象中
						update.setCanUpdate(0); // 有跟新类容
					}
					DB.saveObj(CustConst.Key.APP_UPP, update); // 保存跟新的对象
				}
			}
		}).execute();
	}

	/**
	 * 查询控件
	 * 
	 * @param v
	 *            视图
	 */
	private void findView(View v) {
		mFooterView = android.view.View.inflate(getMyActivity(), R.layout.bottom_home, null);
		mLvHome = (XListView) v.findViewById(R.id.lv_home);
		mLvHome.addFooterView(mFooterView);
		// 下拉加载
		mLvHome.setPullLoadEnable(true);
		mLvHome.setXListViewListener(this);
		mTvArea = (TextView) v.findViewById(R.id.tv_area);
		mLyArea = (LinearLayout) v.findViewById(R.id.ly_area);
		// 搜索
		ImageView icSearchCity = (ImageView) v.findViewById(R.id.iv_searchcity);
		TextView rbLeftSearch = (TextView) v.findViewById(R.id.tv_search_left);
		TextView rbSearch = (TextView) v.findViewById(R.id.edt_search);
		ImageView icScan = (ImageView) v.findViewById(R.id.iv_scan);
		icScan.setOnClickListener(skipClickListener);
		rbSearch.setOnClickListener(skipClickListener);
		rbLeftSearch.setOnClickListener(skipClickListener);
		icSearchCity.setOnClickListener(skipClickListener);
		mTvArea.setOnClickListener(skipClickListener);
		// 优惠券提示
		mLyHomeTips = (LinearLayout) mView.findViewById(R.id.ly_hometips);
		mTvHomeTips = (TextView) mView.findViewById(R.id.tv_hometips);
		// 下拉刷新
		mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.index_swipe_refresh);
	}

	/**
	 * 初始化数据
	 * 
	 * @param v
	 *            视图
	 */
	private void init(View v) {
		mFlagCoupon = true;
		mViewList = new ArrayList<View>();
		DB.saveStr(CouponHomeFragment.COUPON_FIRST, Util.NUM_ONE + ""); // 初始化进入优惠券界面的次数
		mHandler = new Handler();
		mCityDates = new ArrayList<Citys>();
		mLoginFlag = DB.getBoolean(DB.Key.CUST_LOGIN);
		// 添加适配器
		if (mHomeShopListAdapter == null) {
			mHomeShopListAdapter = new HomeShopListAdapter(getMyActivity(), null);
			mLvHome.setAdapter(mHomeShopListAdapter);
		}
		getDbHomeDate(); // 得到数据库里的数据
		requestApi(); // 请求api
		mSwipeRefreshLayout.setColorSchemeResources(R.color.red);
		mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
	}

	/**
	 * 请求所有的定位
	 */
	private void requestApi() {
		if (Util.isNetworkOpen(getMyActivity())) {
			if (Const.APP_UPP) {
				// UpdateManager.checkUpdate(getMyActivity());
				// getNestBaidu();
			} else { // 惠圈
				isUpdate(); // 检测版本升级
			}
			// 获取个人信息
			if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
				GetUserInfo.getUserInfo(getMyActivity());
			}
			// 获取定位的城市和经纬度
			Citys citys = DB.getObj(LocationUtil.CITY_KEY, Citys.class);
			if (null == citys) {
				LocationUtil.getLocationClient();
				citys = DB.getObj(LocationUtil.CITY_KEY, Citys.class);
			}
			if (null != citys) {
				getLocationCity(citys);
			}
			// 获得当前用户拥有还未使用且有效的优惠券
			if (mLoginFlag) {
				countUserNotUsedCoupon();
			}

		} else {
			Util.getToastBottom(getMyActivity(), "请连接网络");
		}
	}

	private void getLocationCity(Citys city) {
		mCitysName = Util.isEmpty(city.getLocationName()) ? "" : city.getLocationName();
		mLongitude = city.getLongitude();
		mLatitude = city.getLatitude();
		if (null != mCitysName) {
			String mobileNbr = "";
			if (DB.getBoolean(DB.Key.CUST_LOGIN)) { // 登录的话获取用户手机号
				UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
				mobileNbr = userToken.getMobileNbr();
			} else {
				mobileNbr = "";
			}
			String params[] = { mCitysName, PhoneInfoUtils.getPhoneIMEI(getActivity()), mobileNbr };
			new RecordUserAddressTask(getMyActivity()).execute(params);
			Log.d(TAG, "api 上传成功吗");
		}

		getShopCity();
		saveCityName();
		Log.d(TAG, "☆☆☆  initLocationClients  BDLocation 2:lat:" + city.getLatitude() + ",lon:" + city.getLongitude()
				+ ",name:" + city.getLocationName());
	}

	/**
	 * 检测网络
	 * 
	 * @param flag
	 *            true 是有网络 false 是没有网络
	 * @param netType
	 *            1 是有网络 2 是没有网络
	 */
	public void netRecerve(boolean flag, int netType) {
		Log.d(TAG, " mNetType ==" + mNetType + ",flag =" + flag);
		if (flag) {
			if (mNetType == 2) {
				mNetType = netType;
				requestApi(); // 有网络的时候 重新请求api
			}
		} else {
			mNetType = netType;
		}
	}

	/**
	 * 得到数据库里的数据
	 */
	private void getDbHomeDate() {
		HomeNew home = HomeModel.getHomeById(HomeModel.HOME_ID);
		if (null != home) {
			// 城市
			if (strIsEmpty(home.getCitys())) {
				try {
					Log.d(TAG, "citysObj=" + home.getCitys());
					org.json.JSONArray citysObj = new org.json.JSONArray(home.getCitys());
					if (null != mCityDates && mCityDates.size() > 0) {
						mCityDates.clear(); // 清除之前的数据
					} else {
						// 不用清除数据
					}
					if (objIsEmpty(citysObj, 2)) { // 2 代表JSONArray类型的对象
						mCityDates = new Gson().fromJson(citysObj.toString(), new TypeToken<List<Citys>>() {
						}.getType());
						getBeforCitys(mCityDates, CACHE); // 得到之前的城市 , 0代表现在还是缓存
						Log.d(TAG, "shopcitys=" + mCityDates.size());
					}

				} catch (Exception e) {
					Log.e(TAG, "获取城市DBUtils有误=" + e.getMessage());
				}
			}
			// 首页缓存
			if (strIsEmpty(home.getHomeResultString())) {
				try {
					Log.d(TAG, "home.getHomeResultString >>>> " + home.getHomeResultString());
					org.json.JSONArray homePage = new org.json.JSONArray(home.getHomeResultString());
					List<HomePage> homePages = new Gson().fromJson(homePage.toString(),
							new TypeToken<List<HomePage>>() {
							}.getType());
					if (objIsEmpty(homePages, 1)) {
						setHomeView(homePages, CACHE); // 设置布局
					}
				} catch (Exception e) {
					Log.e(TAG, "获取城市主题DBUtils有误=" + e.getMessage());
				}

			}
		}
	}

	/**
	 * 判断字符不为空
	 * 
	 * @param str
	 *            输入参数
	 * @return
	 */
	private boolean strIsEmpty(String str) {
		boolean flag = false;
		if (null != str || !Util.isEmpty(str)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 对象不为空
	 * 
	 * @param object
	 *            输入的对象
	 * @param objType
	 *            1 代表jsonobject , 2 代表jsonArray
	 * @return 返回的是 true 为空 false 为空
	 */
	private boolean objIsEmpty(Object object, int objType) {
		boolean flag = false;
		if (objType == 1) { // 1 代表jsonobject
			if (null != object || !Util.isEmpty(object.toString())) {
				flag = true;
			}
		} else if (objType == 2) { // 2 代表jsonArray
			if (null != object || !"[]".equals(object.toString())) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 给滚屏赋值
	 * 
	 * @param actList
	 *            滚屏的集合
	 * @param moduleValue
	 *            模板号
	 */
	private void setScrollInfo(List<HomeTemplate> actList, String moduleValue) {
		mViewPager = (ViewPager) mViewScroll.findViewById(R.id.viewPager);
		FrameLayout fyScroll = (FrameLayout) mViewScroll.findViewById(R.id.fy_scroll);
		// 控制滑动时和下拉的冲突
		mViewPager.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_MOVE:
					Log.d(TAG, "mSwipeRefreshLayout  >>>> 1");
					mSwipeRefreshLayout.setEnabled(false);
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					Log.d(TAG, "mSwipeRefreshLayout  >>>> 2");
					mSwipeRefreshLayout.setEnabled(true);
					break;
				}
				return false;
			}
		});

		if (actList != null && actList.size() > 0) {
			mViewPager.setBackgroundResource(0); // 代表没有图片
			mScrollPics = new String[actList.size()];
			// 跳转的连接
			List<HomeTemplate> actTemplate = new ArrayList<HomeTemplate>();
			for (int i = 0; i < actList.size(); i++) {
				try {
					HomeTemplate homeTemplate = actList.get(i);
					mScrollPics[i] = homeTemplate.getImgUrl();
					actTemplate.add(i, homeTemplate);
					// 设置滚屏的高度
					if (i == 0) {
						LinearLayout.LayoutParams paramsScroll = (android.widget.LinearLayout.LayoutParams) fyScroll
								.getLayoutParams();
						paramsScroll.width = Util.getWindowWidthAndHeight(getMyActivity())[0];
						paramsScroll.height = (int) (Util.getWindowWidthAndHeight(getMyActivity())[0]
								/ homeTemplate.getImgRate());
						fyScroll.setLayoutParams(paramsScroll);
					}
				} catch (Exception e) {
					Log.e(TAG, "活动内容为内容为null>>>" + e.getMessage());// TODO内容为null
				}
			}
			initViewPager(mView, mScrollPics, actTemplate, moduleValue);
		}
	}

	/**
	 * 设置商店类型图标
	 * 
	 * @param jsonResult
	 *            集合
	 * @param moduleValue
	 *            模板号
	 */
	private void setShopType(List<HomeTemplate> moduleList, String moduleValue) {
		RecyclerView rvShopType = (RecyclerView) mViewShopType.findViewById(R.id.rv_shoptype);
		RelativeLayout.LayoutParams ryparams = (RelativeLayout.LayoutParams) rvShopType.getLayoutParams();
		if (null != moduleList && moduleList.size() > 0) {
			ryparams.width = Util.getWindowWidthAndHeight(getMyActivity())[0];
			if (moduleList.size() > 5 && moduleList.size() < 8) { // 大于等于5的
				ryparams.height = SizeUtil.dip2px(160);
				mLine = 2;
			} else {
				ryparams.height = SizeUtil.dip2px(80);
				mLine = 1;
			}
			rvShopType.setLayoutParams(ryparams);
			rvShopType.setLayoutManager(new StaggeredGridLayoutManager(mLine, StaggeredGridLayoutManager.HORIZONTAL));
			rvShopType.setAdapter(new ShopTypeAdapter(getMyActivity(), moduleList, moduleValue, rvShopType, mLine));
		}
	}

	/**
	 * 设置商圈
	 * 
	 * @param subList
	 *            商圈小模块集合
	 */
	private void setShopCircle(List<HomeTemplate> subList, String moduleValue) {
		RecyclerView rvShopCircle = (RecyclerView) mViewShopCircle.findViewById(R.id.rv_shopcircle);
		rvShopCircle.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
		if (null != subList && subList.size() > 0) {
			rvShopCircle.setAdapter(new ShopCircleAdapter(getMyActivity(), subList, moduleValue, rvShopCircle));
			// 设recyclerView的高度
			HomeTemplate template = subList.get(0);
			int imgWidth = (int) (Util.getWindowWidthAndHeight(getActivity())[0] * template.getScreenRate());
			int imgHeight = (int) (imgWidth / template.getImgRate());
			RelativeLayout.LayoutParams ryparams = (RelativeLayout.LayoutParams) rvShopCircle.getLayoutParams();
			ryparams.width = Util.getWindowWidthAndHeight(getActivity())[0];
			ryparams.height = imgHeight;
			rvShopCircle.setLayoutParams(ryparams);
		}
	}

	/**
	 * 获得当前用户拥有还未使用且有效的优惠券
	 */
	private void countUserNotUsedCoupon() {
		new CountUserNotUsedCouponTask(getMyActivity(), new CountUserNotUsedCouponTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (result != null) {
					try {
						String notUsedCouponNbr = result.get("notUsedCouponNbr").toString();
						if (Integer.parseInt(notUsedCouponNbr) > 0) {
							mLyHomeTips.setVisibility(View.VISIBLE);
							mTvHomeTips.setText("温馨提示：您有" + notUsedCouponNbr + "张优惠券未使用");
							mLyHomeTips.setOnClickListener(skipClickListener);
						}
					} catch (Exception e) {
						Log.e(TAG, "用户优惠券张数转换有误"); // TODO
					}
				} else {
					mLyHomeTips.setVisibility(View.GONE);
				}
			}
		}).execute();

	}

	/**
	 * 商家列表
	 */
	private void getHomeShopList() {
		String params[] = { mLongitude + "", mLatitude + "", mPage + "", mCitysName };
		new GetHomeShopListTask(getMyActivity(), new GetHomeShopListTask.Callback() {

			@Override
			public void getResult(JSONObject result) {
				mFlagCoupon = true;
				mLvHome.stopLoadMore();
				if (result == null) {
					mLvHome.setPullLoadEnable(false);
				} else {
					mLvHome.setPullLoadEnable(true);
					PageData page = new PageData(result, "shopList", new TypeToken<List<Shop>>() {
					}.getType());
					mPage = page.getPage();
					if (page.hasNextPage() == false) {
						if (mPage > 1) {
							Util.getContentValidate(R.string.no_more);
						}
						mLvHome.setPullLoadEnable(false);
					} else {
						mLvHome.setPullLoadEnable(true);
					}
					List<Shop> list = (List<Shop>) page.getList();

					if (mHomeShopListAdapter == null) {
						mHomeShopListAdapter = new HomeShopListAdapter(getMyActivity(), list);
						mLvHome.setAdapter(mHomeShopListAdapter);
					} else {
						if (page.getPage() == 1) {
							mHomeShopListAdapter.setItems(list);
						} else {
							mHomeShopListAdapter.addItems(list);
						}
					}
					try {
						mPage = Integer.parseInt(result.get("nextPage").toString());
					} catch (Exception e) {
						Log.e(TAG, "mPage >>> error ," + e.getMessage());
					}
				}
			}
		}).execute(params);
	}

	private void initViewPager(View v, String[] actPics, List<HomeTemplate> templates, String moduleValue) {
		mImagesList = new ArrayList<ImageView>();
		if (actPics.length != 2) {
			for (int i = 0; i < actPics.length; i++) {
				ImageView imageView = new ImageView(getMyActivity());
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				if (Util.isEmpty(actPics[i])) {
					imageView.setBackgroundResource(R.drawable.banner); // TODO
				} else {
					Util.showBannnerImage(getMyActivity(), actPics[i], imageView);
				}
				mImagesList.add(imageView);
			}
		} else {
			for (int i = 0; i < actPics.length + 2; i++) {
				ImageView imageView = new ImageView(getMyActivity());
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				if (i % 2 == 0) {
					Util.showBannnerImage(getMyActivity(), actPics[0], imageView);
				} else {
					Util.showBannnerImage(getMyActivity(), actPics[1], imageView);
				}
				mImagesList.add(imageView);
			}
			templates.addAll(templates);
			Log.d(TAG, "templates >>> " + templates.size());
		}

		LinearLayout mLyGroupViews = (LinearLayout) mViewScroll.findViewById(R.id.ly_gropview);
		mLyGroupViews.removeAllViews();

		mPoints = new ImageView[actPics.length];
		for (int i = 0; i < actPics.length; i++) {
			ImageView imageView = new ImageView(getMyActivity());
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(25, 25);
			p.leftMargin = 10;
			p.rightMargin = 10;
			imageView.setLayoutParams(p);
			mPoints[i] = imageView;
			if (i == 0) {
				// 默认选中第一张图片
				mPoints[i].setBackgroundResource(R.drawable.dot_focused);
			} else {
				mPoints[i].setBackgroundResource(R.drawable.dot_normal);
			}
			mLyGroupViews.addView(mPoints[i]);
		}
		HomeSrollAdapter SrollPicAdapter = new HomeSrollAdapter(getMyActivity(), mImagesList, templates, mSrollHandler,
				mSrollRunnable, moduleValue);
		mViewPager.setAdapter(SrollPicAdapter);
		// 切换图片时 点点发生改变
		mViewPager.setOnPageChangeListener(new MyListener());
		mViewPager.setCurrentItem(mCurPosition * 100);
		if (mSrollHandler != null && mSrollRunnable != null) {
			mSrollHandler.removeCallbacks(mSrollRunnable);
		}
		mSrollHandler.postDelayed(mSrollRunnable, 1000);
	}

	/**
	 * viewPage的改变事件
	 * 
	 * @author ad
	 */
	private class MyListener implements OnPageChangeListener {

		// 当滑动状态改变时调用
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		// 当当前页面被滑动时调用
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		// 当新的页面被选中时调用
		@Override
		public void onPageSelected(int position) {
			if (mPoints.length == 2) {
				position = position % 2;
			} else if (position > 2) {
				position = position % mImagesList.size();
			}
			Log.d(TAG, "positionmPoints >>> " + position + " mPoints >>> " + mPoints.length);
			for (int i = 0; i < mPoints.length; i++) {
				mPoints[position].setBackgroundResource(R.drawable.dot_focused);
				if (position != i) {
					mPoints[i].setBackgroundResource(R.drawable.dot_normal);
				}
			}

		}

	}

	/**
	 * 启动一个线程
	 */
	private Runnable mSrollRunnable = new Runnable() {

		@Override
		public void run() {
			if (mScrollPics == null || mScrollPics.length == 0) {
				return;// TODO
			}
			/*** 更新界面 **/
			mSrollHandler.obtainMessage().sendToTarget();
			mSrollHandler.postDelayed(this, 4000);
		}
	};

	/***
	 * 切换图片
	 * 
	 * @author ad
	 */
	private Handler mSrollHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 判断20秒后温馨提示自动关闭
			mTime++;
			if (mTime == 10) {
				mLyHomeTips.setVisibility(View.GONE);
			}
			mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
		}

	};

	/**
	 * 首页所有跳转到其他界面
	 */
	private OnClickListener skipClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
			case R.id.iv_scan:
				intent = new Intent(getMyActivity(), ScanActivity.class);
				intent.putExtra(ScanActivity.TYPE, CustConst.HactTheme.SCAN_ONT);
				startActivity(intent);
				MobclickAgent.onEvent(getMyActivity(), "home_scan");
				break;
			case R.id.tv_search_left:// 主界面搜索商户
			case R.id.edt_search:
				if (selCitys()) {
					break;
				}
				SkipActivityUtil.skipShopList(); // 跳转到商家列表
				MobclickAgent.onEvent(getMyActivity(), "home_searchshop");
				break;
			case R.id.tv_area:// 搜索城市
			case R.id.iv_searchcity:
				mFlagArea = true;
				searchArea();
				break;
			case R.id.ly_hometips: // 温馨提示
				mLyHomeTips.setVisibility(View.GONE);
				DB.saveBoolean(CouponHomeFragment.HOME_TIPS, true);
				HomeActivity home = (HomeActivity) getMyActivity();
				if (home == null) {
					return;
				}
				home.goToTab(2);
				break;
			}
		}
	};

	/**
	 * 切换地名
	 */
	public void searchArea() {
		View view = LayoutInflater.from(getMyActivity()).inflate(R.layout.popupwindow_homearea, null);
		final ListView lvArea = (ListView) view.findViewById(R.id.lv_area);
		CitysAdapter adapter = new CitysAdapter(getMyActivity(), mCityDates);
		lvArea.setAdapter(adapter);
		final PopupWindow popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
		popupWindow.setOutsideTouchable(true);
		popupWindow.showAsDropDown(mLyArea, 0, 0);
		lvArea.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long longId) {
				Citys citys = (Citys) lvArea.getItemAtPosition(position);
				String cityName = citys.getName();
				mTvArea.setText(cityName);
				Log.d(TAG, "shopcitys=>>>searchArea" + cityName);
				mCitysName = cityName;
				mPage = 1;
				if (mFlagArea) {
					mProcessDialog = new ProgressDialog(getMyActivity());
					mProcessDialog.setCancelable(false);
					mProcessDialog.setMessage(getMyActivity().getString(R.string.search_area));
					mProcessDialog.show();
				}
				// Log.d(TAG, "getHomeInfotest : type :☆☆☆ searchArea 1231");
				getHomeInfo();
				// 保存选择地名和经纬度
				saveCityNameSave();
				popupWindow.dismiss();
			}
		});
	}

	/**
	 * 获得城市
	 */
	private void getShopCity() {
		new GetShopCityTask(getMyActivity(), new GetShopCityTask.Callback() {

			@Override
			public void getResult(JSONArray result) {
				Citys citys = null;
				if (result == null) {
					Log.d(TAG, "mTvArea   >>>  1-1");
					if (null == mCityDates && mCityDates.size() <= 0) {
						mTvArea.setText(getMyActivity().getResources().getString(R.string.home_selcitys));
					}
				} else {
					Log.d(TAG, "mTvArea   >>>  1-2");
					mCityResultString = result.toString();
					HomeModel.saveHomeFragment(result.toString(), mHomeResultString);
					if (null != mCityDates && mCityDates.size() > 0) {
						mCityDates.clear();
					}
					for (int i = 0; i < result.size(); i++) {
						citys = new Citys();
						citys = Util.json2Obj(result.get(i).toString(), Citys.class);
						mCityDates.add(citys);
					}
					Log.d(TAG, "mTvArea   >>>  1-3");

					// 判断定位城市是否为空
					boolean flag = false;
					if (!Util.isEmpty(mCitysName)) {
						// 不为空的情况
						for (int i = 0; i < result.size(); i++) {
							citys = Util.json2Obj(result.get(i).toString(), Citys.class);
							if (mCitysName.equals(citys.getName())) {
								mTvArea.setText(mCitysName);
								Log.d(TAG, "mTvArea   >>>  1");
								// 保存城市
								saveCityNameSave();
								// 得到不同地方的信息
								Log.d(TAG, "getHomeInfotest : type :☆☆☆  getShopCitys  1280");
								getHomeInfo();
								flag = false;
								break;
							} else {
								Log.d(TAG, "getHomeInfotest   >>>  1-3-0");
								flag = (i == result.size() - 1) ? true : false;
							}
						}
						// 判断城市是否在开发城市里 flag 为true就是不在开放城市里面
						if (flag) {
							Log.d(TAG, "getHomeInfotest   >>>  1-3-1  ");
							// 定位的城市不在开放城市中
							mLvHome.setPullLoadEnable(false);
							/** 判断是否为第一次使用 app */
							SharedPreferences sharedPreferences = PreferenceManager
									.getDefaultSharedPreferences(getMyActivity());
							boolean firstRun = true;
							try {
								firstRun = sharedPreferences.getBoolean(KEY_CITY_FISRT_RUN, true);
							} catch (Exception e) {
								Log.e(TAG, "homeFragmetn firstRun error " + e.getMessage());
							}
							Editor editor = sharedPreferences.edit();
							if (firstRun) {
								editor.putBoolean(KEY_CITY_FISRT_RUN, false);// 修改信息
								editor.commit();
								View view = LayoutInflater.from(getMyActivity()).inflate(R.layout.popupwindow_nogpscity,
										null);
								LinearLayout lyCitysTips = (LinearLayout) view.findViewById(R.id.ly_nocity);
								final PopupWindow popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
										LayoutParams.WRAP_CONTENT);
								popupWindow.setFocusable(true);
								popupWindow
										.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
								popupWindow.setOutsideTouchable(true);
								popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
								lyCitysTips.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										popupWindow.dismiss();
									}
								});
							}
							Log.d(TAG, "getHomeInfotest   >>>  2 ONLINE 1320");
							getBeforCitys(mCityDates, ONLINE); // 1代表有网络加载的新数据

						}
					} else { // 定位失败
						Log.d(TAG, "getHomeInfotest   >>>  3  ONLINE  1325");
						getBeforCitys(mCityDates, ONLINE);
						// 等到之前的城市
						Util.getToastBottom(getMyActivity(),
								getMyActivity().getResources().getString(R.string.toast_gpsfail));
					}
				}
			}
		}).execute();

	}

	/**
	 * 得到城市之前
	 * 
	 * @param cityDates
	 * @param 0
	 *            代表缓存数据 1 获得城市后的数据
	 */
	private void getBeforCitys(List<Citys> cityDates, int type) {
		// 城市还没开放
		SharedPreferences mSharedPreferences = getMyActivity().getSharedPreferences(CustConst.Key.CITY_OBJ,
				Context.MODE_PRIVATE);
		String cityName = mSharedPreferences.getString(CustConst.Key.CITY_NAME, "");
		if (!Util.isEmpty(cityName)) {
			if (cityDates.size() == 1) {
				for (int i = 0; i < cityDates.size(); i++) {
					Citys citys = cityDates.get(i);
					mCitysName = citys.getName();
				}
			} else {
				mCitysName = cityName;
			}
		} else {
			if (null != cityDates && cityDates.size() > 0) {
				for (int i = 0; i < cityDates.size(); i++) {
					Citys citys = cityDates.get(i);
					if (i == 0) {
						mCitysName = citys.getName();
						break;
					}
				}
			} else {
				mCitysName = getString(R.string.home_selcitys);
			}
		}
		if (Util.isNetworkOpen(getMyActivity()) && type == ONLINE) { // 加载缓存完并且有网就去加载数据
			Log.d(TAG, "getHomeInfotest : type :☆☆☆  getBeforCityss   1369");
			getHomeInfo();
		}
		saveCityNameSave();
		mTvArea.setText(mCitysName);
	}

	/**
	 * 城市的列表
	 * 
	 * @author ad
	 * 
	 */
	private class CitysAdapter extends CommonListViewAdapter<Citys> {

		public CitysAdapter(Activity activity, List<Citys> datas) {
			super(activity, datas);

		}

		@Override
		public android.view.View getView(int position, android.view.View convertView, ViewGroup parent) {
			CommenViewHolder holder = CommenViewHolder.get(getMyActivity(), convertView, parent, R.layout.item_city,
					position);
			Citys citys = (Citys) getItem(position);
			((TextView) holder.getView(R.id.tv_homearea)).setText(citys.getName());

			return holder.getConvertView();
		}

	}

	/**
	 * 判断是否选择城市了
	 */
	private boolean selCitys() {
		boolean flag = false;
		String area = mTvArea.getText().toString();
		String selArea = getString(R.string.home_selcitys);
		if (mTvArea != null) {
			if (selArea.equals(area)) {
				Util.getToastBottom(getMyActivity(), getString(R.string.home_noselcity));
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 只保存经纬度
	 */
	public void saveCityName() {
		Citys city = new Citys();
		city.setName("");
		city.setLatitude(mLatitude);
		city.setLongitude(mLongitude);
		DB.saveObj(CITYS, city);
	}

	/**
	 * 保存城市和经纬度
	 */
	public void saveCityNameSave() {
		// 保存注册的用户名和密码
		SharedPreferences mSharedPreferences = getMyActivity().getSharedPreferences(CustConst.Key.CITY_OBJ,
				Context.MODE_PRIVATE);
		Editor editor = mSharedPreferences.edit();
		editor.putString(CustConst.Key.CITY_NAME, mCitysName);
		editor.putString(CustConst.Key.CITY_LAT, mLatitude + "");
		editor.putString(CustConst.Key.CITY_LONG, mLongitude + "");
		editor.commit();
	}

	/**
	 * 上拉加载更多
	 */
	@Override
	public void onLoadMore() {
		if (Util.isNetworkOpen(getMyActivity())) {
			if (mFlagCoupon) {
				mFlagCoupon = false;
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						getHomeShopList();
					}
				}, 2000);
			}
		} else {
			mLvHome.stopLoadMore();
			Util.getToastBottom(getMyActivity(), "请连接网络");
		}
	}

	/**
	 * 下拉加载
	 */
	private OnRefreshListener refreshListener = new OnRefreshListener() {

		@Override
		public void onRefresh() {
			if (Util.isNetworkOpen(getMyActivity())) {
				mHandler.postDelayed(new Runnable() {
					public void run() {
						// 隐藏温馨提示
						mLyHomeTips.setVisibility(View.GONE);
						// 平台活动
						if (null == mCitysName || Util.isEmpty(mCitysName)) {
							Citys city = DB.getObj(LocationUtil.CITY_KEY, Citys.class);
							if (null == city) {
								LocationUtil.getLocationClient(); // 没有定位成功 重新定位
								city = DB.getObj(LocationUtil.CITY_KEY, Citys.class);
							}
							if (null != city) {
								getLocationCity(city);
							}
						}
						mPage = 1;
						getHomeInfo();
					}
				}, 5000);
			} else {
				mLvHome.stopLoadMore();
				Util.getToastBottom(getMyActivity(), "请连接网络");
			}
		}
	};

	@Override
	public void onPause() {
		super.onPause();
		if (mTimer != null) {
			mTimer.cancel();
		}
		MobclickAgent.onPageEnd(HomeFragment.class.getSimpleName());
		TheadDBhelper.closeRunnable(mSrollHandler, mSrollRunnable); // 关闭线程
	}

	/**
	 * 友盟统计
	 */
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(HomeFragment.class.getSimpleName()); // 统计页面
		if (mTimer != null) {
			mTimer.start();
		}
		TheadDBhelper.runRunnable(mSrollHandler, mSrollRunnable); // 启动线程
		if (null != mLyHomeTips && mLyHomeTips.getVisibility() == View.VISIBLE) {
			countUserNotUsedCoupon();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		TheadDBhelper.closeRunnable(mSrollHandler, mSrollRunnable); // 关闭线程
	}

}