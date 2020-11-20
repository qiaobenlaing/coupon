//---------------------------------------------------------
//@author    Zhonghui.Dong
//@version   1.0.0
//@createTime 2015.6.2
//@copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
//---------------------------------------------------------
package cn.suanzi.baomi.cust.activity;

import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.R.integer;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.suanzi.baomi.base.Const;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.model.GuideImgModel;
import cn.suanzi.baomi.base.model.HomeModel;
import cn.suanzi.baomi.base.model.HomeTabModel;
import cn.suanzi.baomi.base.model.LoginTask;
import cn.suanzi.baomi.base.pojo.Citys;
import cn.suanzi.baomi.base.pojo.GuideImg;
import cn.suanzi.baomi.base.pojo.HomeNew;
import cn.suanzi.baomi.base.pojo.HomePage;
import cn.suanzi.baomi.base.pojo.HomeTab;
import cn.suanzi.baomi.base.pojo.Messages;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.Calculate;
import cn.suanzi.baomi.base.utils.CheckNetworkUtil;
import cn.suanzi.baomi.base.utils.ImageDownloadCallback;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.application.CustConst.IsOpenAct;
import cn.suanzi.baomi.cust.fragment.BaseFragment;
import cn.suanzi.baomi.cust.fragment.CardHomeFragment;
import cn.suanzi.baomi.cust.fragment.CouponHomeFragment;
import cn.suanzi.baomi.cust.fragment.HomeFragment;
import cn.suanzi.baomi.cust.fragment.MyHomeFragment;
import cn.suanzi.baomi.cust.model.CountAllTypeMsgTask;
import cn.suanzi.baomi.cust.model.GetHomeTabListTask;
import cn.suanzi.baomi.cust.model.GetSystemParamTask;
import cn.suanzi.baomi.cust.receiver.MyReceiver;
import cn.suanzi.baomi.cust.receiver.NetRecerve;
import cn.suanzi.baomi.cust.util.DialogUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 主界面
 * 
 * @author yanfang.li
 */
public class HomeActivity extends Activity {

	private final static String TAG = HomeActivity.class.getSimpleName();
	/** 用于savedInstanceState的常量，当前fragment的index */
	public final static String FRAG_INDEX = "frag_index";
	/** 用于对返回键按下次数的计数 */
	public int mClickNum = 0;
	/** 第一个Tab的按钮 */
	@ViewInject(R.id.rbtn_main)
	private ImageView mIvFirst;
	/** “第三”按钮 */
	@ViewInject(R.id.rbtn_coupon)
	private ImageView mIvThird;
	/** “第二个”按钮 */
	@ViewInject(R.id.rbtn_card)
	private ImageView mIvSecond;
	/** “第三个”按钮 */
	@ViewInject(R.id.rbtn_my)
	private ImageView mIvFourth;
	/** 第一个Tab的文字 */
	private TextView mTvFirst;
	/** 第三个Tab的文字 */
	private TextView mTvThird;
	/** 第二个Tab文字 */
	private TextView mTvSecond;
	/** 第四个Tab的文字 */
	private TextView mTvFourth;
	// private BaseFragment mHomeFragment;
	private HomeFragment mHomeFragment;
	private BaseFragment mCardHomeFragment;
	private BaseFragment mCouponHomeFragment;
	private BaseFragment mMyHomeFragment;
	public int mCurrentTabIndex = 0;
	private BaseFragment mCurrentFragment = null;
	/** Fragment管理器 */
	private static FragmentManager mFragmentManager;
	/** 新人注册 */
	private ImageView mIvNewRegister;
	/** 是否已经注册 */
	private boolean FLAG = false;
	/** 推送登录的记录数 */
	private static int sJpusLoginData;
	/** 判断是否有新人注册送豪礼活动 */
	private String mIsOpenAct;
	/** 显示是否有消息 */
	private ImageView mIVMsgPromt;
	/** 设置网络 */
	private RelativeLayout mRlSetNet;
	/** NetRecerve */
	private NetRecerve mNetRecerve;
	/** 选项卡列表 */
	private List<HomeTab> mTabList;
	/** 第一个tab对象 */
	private static HomeTab mFirstTab;
	/** 第二个tab对象 */
	private static HomeTab mSecondTab;
	/** 第三个tab对象 */
	private static HomeTab mThirdTab;
	/** 第四个tab对象 */
	private static HomeTab mFourthTab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_home);
		// 设置状态栏
		// StatusBarView.statusBar(this);
		// 将本来添加到对象中
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		// 友盟统计
		MobclickAgent.openActivityDurationTrack(false);
		MobclickAgent.updateOnlineConfig(HomeActivity.this);
		ViewUtils.inject(this);
		mFragmentManager = getFragmentManager();
		if (savedInstanceState != null) {
			mCurrentTabIndex = savedInstanceState.getInt(FRAG_INDEX); // 默认为第一个（index为0）。
		}
		Log.d(TAG, "onCreate()中mCurrFragIndex=" + this.mCurrentTabIndex);
		findView();
		Log.d(TAG, "tab test ☆☆1：");
		getHomeTabList();
		Log.d(TAG, "tab test ☆☆2：");
		initData();
		initTab(null);
		// 新人注册送豪礼
		if (Util.isNetworkOpen(this)) {
			getSysTemParam();
		}
		sJpusLoginData = 0; // 初始化
	}

	/**
	 * 得到数据库里的数据
	 */
	private void getDbHomeTabDate() {
		HomeTab homeTab = HomeTabModel.getHomeTabById(HomeTabModel.ID);
		Log.d(TAG, "testHOME ********  0_0 test0:");
		if (null != homeTab) {
			// 城市
			if (!Util.isEmpty(homeTab.getHomeTabs())) {
				try {
					Log.d(TAG, "testHOME  ****** ： " + homeTab.getHomeTabs());
					org.json.JSONArray tabArray = new org.json.JSONArray(homeTab.getHomeTabs());
					if (null != mTabList && mTabList.size() > 0) {
						mTabList.clear(); // 清除之前的数据
					} else {
						// 不用清除数据
					}
					Log.d(TAG, "testHOME ********  0_0 test1:");
					mTabList = new Gson().fromJson(tabArray.toString(), new TypeToken<List<HomeTab>>() {}.getType());
					Log.d(TAG, "testHOME ********  0_0 test:" +mTabList.size() );
					initHomeTabData();
						
				} catch (Exception e) {
					Log.e(TAG, "获取城市DBUtils有误=" + e.getMessage());
				}
			}
		}
	}

	/**
	 * 初始化选项卡
	 * 
	 * @param intent
	 */
	private void initTab(Intent intent) {
		if (intent == null) {
			intent = this.getIntent();
		}
		String fragLogin = intent.getStringExtra(LoginTask.ALL_LOGIN);
		Log.d(TAG, "fragLogin=" + fragLogin);
		if (!Util.isEmpty(fragLogin)) {
			if (fragLogin.equals(CustConst.Login.COUPON_LOGIN)) {
				if (mCouponHomeFragment == null) {
					mCouponHomeFragment = new CouponHomeFragment();
				}
				showFragment(null, mCouponHomeFragment);
				mCurrentTabIndex = 2;
				if (null != mFirstTab && null != mThirdTab) {
					setTabFocused(2, mIvThird, mTvThird, mThirdTab);
					setTabFocused(0, mIvFirst, mTvFirst, mFirstTab);
				}
			} else if (fragLogin.equals(CustConst.Login.CARD_LOGIN)) {
				if (mCardHomeFragment == null) {
					mCardHomeFragment = new CardHomeFragment();
				}
				showFragment(null, mCardHomeFragment);
				mIvSecond.setBackgroundResource(R.drawable.main_btn_2_);
				mTvSecond.setTextColor(getResources().getColor(R.color.tab_fontsel));
				mIvFirst.setBackgroundResource(R.drawable.main_btn_1);
				mTvFirst.setTextColor(getResources().getColor(R.color.tab_fontnosel));
				mCurrentTabIndex = 1;
				if (null != mFirstTab && null != mSecondTab) {
					setTabFocused(1, mIvSecond, mTvSecond, mSecondTab);
					setTabFocused(0, mIvFirst, mTvFirst, mFirstTab);
				}
			} else if (fragLogin.equals(CustConst.Login.MY_LOGIN)) {
				if (mMyHomeFragment == null) {
					mMyHomeFragment = new MyHomeFragment();
				}
				showFragment(null, mMyHomeFragment);
				mCurrentTabIndex = 3;
				if (null != mFirstTab && null != mFourthTab) {
					setTabFocused(3, mIvFourth, mTvFourth, mFourthTab);
					setTabFocused(0, mIvFirst, mTvFirst, mFirstTab);
				}
			}
		} else {
			Log.d(TAG, "tab test ☆☆4：");
			if (mHomeFragment == null) {
				mHomeFragment = new HomeFragment();
			}
			showFragment(null, mHomeFragment);
			mCurrentTabIndex = 0;
			if (null != mFirstTab) {
				setTabFocused(0, mIvFirst, mTvFirst, mFirstTab);
			}
		}

	}

	/**
	 * 获取控件
	 */
	private void findView() {
		mTvFirst = (TextView) findViewById(R.id.tv_main);
		mTvThird = (TextView) findViewById(R.id.tv_coupon);
		mTvSecond = (TextView) findViewById(R.id.tv_card);
		mTvFourth = (TextView) findViewById(R.id.tv_my);
		mIVMsgPromt = (ImageView) findViewById(R.id.my_msgpromt);
		// 新人注册
		mIvNewRegister = (ImageView) findViewById(R.id.tv_new_register);
		mRlSetNet = (RelativeLayout) findViewById(R.id.rl_set_net); // 没有网络设置网络
		getDbHomeTabDate(); // 先获取数据库里面的homeTab的数据
	}

	/**
	 * 选项卡
	 */
	private void getHomeTabList() {
		new GetHomeTabListTask(this, new GetHomeTabListTask.Callback() {
			@Override
			public void getResult(JSONArray result) {
				Log.d(TAG, "tab resule : " + result.toString());
				if (null != result) {
					if (null != mTabList && mTabList.size() > 0) {
						mTabList.clear();
					}
					mTabList = new Gson().fromJson(result.toString(), new TypeToken<List<HomeTab>>() {}.getType());
//					getBitMap(mTabList);
					if (null != mTabList && mTabList.size() > 0) {
						for (int i = 0; i < mTabList.size(); i++) {
							HomeTab homeTab = mTabList.get(i);
							if (homeTab.getSubModulePosition() == 1) { // 表示该模块在第一个位置
								mFirstTab = homeTab;
								setBgColor(mIvFirst, mTvFirst, homeTab.getFocusedUrl(), homeTab.getFocusedBitmap(),homeTab.getFocusedColor(),homeTab);
							} else if (homeTab.getSubModulePosition() == 2) { // 表示在第二个位置
								mSecondTab = homeTab;
								setBgColor(mIvSecond, mTvSecond, homeTab.getNotFocusedUrl(),homeTab.getNotFocusedBitmap(),homeTab.getNotFocusedColor(), homeTab);
							} else if (homeTab.getSubModulePosition() == 3) { // 表示该模块在第三个位置
								mThirdTab = homeTab;
								setBgColor(mIvThird, mTvThird, homeTab.getNotFocusedUrl(),homeTab.getNotFocusedBitmap(),homeTab.getNotFocusedColor(), homeTab);
							} else if (homeTab.getSubModulePosition() == 4) { // 表示该模块在第四个位置
								mFourthTab = homeTab;
								setBgColor(mIvFourth, mTvFourth, homeTab.getNotFocusedUrl(),homeTab.getNotFocusedBitmap(),homeTab.getNotFocusedColor(), homeTab);
							}
							Log.d(TAG, "homeTabhomeTab : " + homeTab.getFocusedBitmap());
						}
					}
					HomeTabModel.saveHomeTab(new HomeTab(HomeTabModel.ID, new Gson().toJson(mTabList).toString()));// 保存选项卡的代码
					// 初始化选项卡
					initTab(null);

				}
			}
		}).execute();
	}

	/**
	 * 初始化数据
	 */
	private void initHomeTabData () {
		if (null != mTabList && mTabList.size() > 0) {
			Log.d(TAG, "testHOME ********  0_0");
			for (int i = 0; i < mTabList.size(); i++) {
				HomeTab homeTab = mTabList.get(i);
				if (homeTab.getSubModulePosition() == 1) { // 表示该模块在第一个位置
					mFirstTab = homeTab;
					if (null != homeTab.getFocusedBitmap()) {
						setBgColor(mIvFirst, mTvFirst, "", homeTab.getFocusedBitmap(),homeTab.getFocusedColor(),homeTab);
					} else {
						setBgColor(mIvFirst, mTvFirst, homeTab.getFocusedUrl(), null,homeTab.getFocusedColor(),homeTab);
					}
				} else if (homeTab.getSubModulePosition() == 2) { // 表示在第二个位置
					mSecondTab = homeTab;
					if (null != homeTab.getFocusedBitmap()) {
						setBgColor(mIvSecond, mTvSecond, "",homeTab.getNotFocusedBitmap(),homeTab.getNotFocusedColor(), homeTab);
					} else {
						setBgColor(mIvSecond, mTvSecond, homeTab.getNotFocusedUrl(),null,homeTab.getNotFocusedColor(), homeTab);
					}
				} else if (homeTab.getSubModulePosition() == 3) { // 表示该模块在第三个位置
					mThirdTab = homeTab;
					if (null != homeTab.getFocusedBitmap()) {
						setBgColor(mIvThird, mTvThird, "",homeTab.getNotFocusedBitmap(),homeTab.getNotFocusedColor(), homeTab);
					} else {
						setBgColor(mIvThird, mTvThird, homeTab.getNotFocusedUrl(),null,homeTab.getNotFocusedColor(), homeTab);
					}
				} else if (homeTab.getSubModulePosition() == 4) { // 表示该模块在第四个位置
					mFourthTab = homeTab;
					if (null != homeTab.getFocusedBitmap()) {
						setBgColor(mIvFourth, mTvFourth,"",homeTab.getNotFocusedBitmap(),homeTab.getNotFocusedColor(), homeTab);
					} else {
						setBgColor(mIvFourth, mTvFourth, homeTab.getNotFocusedUrl(),null,homeTab.getNotFocusedColor(), homeTab);
					}
				}
			}
			// 初始化选项卡
		}
	}
	
	/**
	 * 得到bitMap的图片
	 * @param url 图片的地址
	 * @param position 图片存在的位置
	 * @param sel 现在是否被选中
	 */
	private void getBitMap(List<HomeTab> tabList) {
		for (int i = 0; i < tabList.size(); i++) {
			final HomeTab homeTab = tabList.get(i);
			Util.getLocalOrNetBitmap(Const.IMG_URL + homeTab.getFocusedUrl(), new ImageDownloadCallback() {
				@Override
				public void success(final Bitmap bitmap) {
					homeTab.setFocusedBitmap(bitmap);
				}
				@Override
				public void fail() {
					Log.d(TAG, "bitmap fail : " );
				}
			});
			Util.getLocalOrNetBitmap(Const.IMG_URL + homeTab.getNotFocusedUrl(), new ImageDownloadCallback() {
				@Override
				public void success(final Bitmap bitmap) {
					homeTab.setNotFocusedBitmap(bitmap);
				}
				@Override
				public void fail() {
					Log.d(TAG, "bitmap fail : " );
				}
			});
		}
		HomeTabModel.saveHomeTab(new HomeTab(HomeTabModel.ID, new Gson().toJson(tabList).toString()));// 保存选项卡的代码
	}

	/**
	 * 设置每个模块中的图片和文字
	 * 
	 * @param imageView
	 * @param textView
	 * @param url
	 * @param title
	 * @param titleColor
	 */
	private void setBgColor(ImageView imageView, TextView textView, String url,Bitmap bitmap, String titleColor, HomeTab homeTab) {
		imageView.setBackgroundResource(0);
		if (null != bitmap) {
			imageView.setImageBitmap(bitmap);
		} else {
			Util.showFirstImages(HomeActivity.this, url, imageView);
		}
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
		params.width = (int) (Util.getWindowWidthAndHeight(this)[0] * homeTab.getScreenRate());
		params.height = (int) (params.width / homeTab.getImgRate());
		imageView.setLayoutParams(params);
		textView.setText(homeTab.getTitle());
		if (!Util.isEmpty(titleColor)) {
			textView.setTextColor(Color.parseColor(titleColor));
		}
	}
	
	/**
	 * Activity处于非活动状态而被销毁时保存数据。系统会自动调用。
	 * 
	 * @param savedInstanceState
	 *            保存状态
	 */
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putInt(FRAG_INDEX, this.mCurrentTabIndex);
	}

	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		initTab(intent);
	}

	/**
	 * 判断首页是否有新人注册送豪礼
	 */
	private void getSysTemParam() {
		new GetSystemParamTask(HomeActivity.this, new GetSystemParamTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				String isOpenRegAct = result.get("isOpenRegAct").toString();
				Log.d(TAG, "活动参数为：：" + isOpenRegAct);
				DB.saveStr(Const.IS_OPENREGACT, isOpenRegAct);
				// DB.saveStr(Const.IS_OPENREGACT, "1");
			}
		}).execute();
	}

	/**
	 * 接受就是登录的记录数据
	 * 
	 * @param data
	 */
	public static void getJPushLoginData(int data) {
		sJpusLoginData = data;
	}

	/**
	 * 友盟统计
	 */
	public void onResume() {
		super.onResume();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		MobclickAgent.onResume(this);
		if (mCurrentTabIndex == 3) {
			changeMy();
		}
		// 初始化Jpush退送登录的记录
		Log.d(TAG, "sJpusLoginData=" + sJpusLoginData);
		if (sJpusLoginData == MyReceiver.LOGIN_DATA) { // 有登录的退送
			MyReceiver.setInitLoginData();
			View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.popupw_dialog, null);
			if (null != DialogUtils.showLoginDialog) {
				DialogUtils.showLoginDialog.showAtLocation(view, Gravity.CENTER, 0, 0);
			}
		}
	}

	/**
	 * 监听home键
	 */
	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		Log.d(TAG, "onUserLeaveHint");
		DialogUtils.close(this);
	}

	/**
	 * 再次启动时
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	/**
	 * 登录成功 或者退出的操作
	 */
	public static void setJpusLoginData() {
		sJpusLoginData = 0;
		Log.d(TAG, "sJpusLoginData>>>>" + sJpusLoginData);
	}

	/**
	 * 跳转
	 * 
	 * @param v视图
	 */
	@OnClick({ R.id.rbtn_main, R.id.rbtn_card, R.id.rbtn_coupon, R.id.rbtn_my, R.id.ly_main, R.id.ly_card,
			R.id.ly_home_coupon, R.id.ly_my, R.id.rl_set_net })
	public void changeContent(View v) {
		checkNet(); // 检测是否有网络
		mClickNum = 0;
		switch (v.getId()) {
		case R.id.rbtn_main:
		case R.id.ly_main: { // 首页
			changeMain();
			MobclickAgent.onEvent(HomeActivity.this, "home_home");
			break;
		}
		case R.id.rbtn_card:
		case R.id.ly_card: { // 会员卡
			changeCard();
			MobclickAgent.onEvent(HomeActivity.this, "home_card");
			break;
		}
		case R.id.rbtn_coupon:
		case R.id.ly_home_coupon: { // 优惠券
			changeCoupon();
			MobclickAgent.onEvent(HomeActivity.this, "home_coupon");
			break;
		}
		case R.id.rbtn_my:
		case R.id.ly_my: { // 我的
			changeMy();
			MobclickAgent.onEvent(HomeActivity.this, "home_my");
			mIVMsgPromt.setVisibility(View.GONE);
			break;
		}
		case R.id.rl_set_net: { // 跳转设置设置网络
			// 跳转到无线网络设置界面
			startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
			mRlSetNet.setVisibility(View.GONE);
			break;
		}
		}
	}

	/**
	 * 注册时是否有活动
	 * 
	 * @param isOPenAct
	 */
	private void setIsOpenAct(String isOPenAct) {
		if (IsOpenAct.OPEN.equals(isOPenAct)) {
			mIvNewRegister.setVisibility(View.VISIBLE);
		} else {
			mIvNewRegister.setVisibility(View.GONE);
		}
	}

	/**
	 * 切换到主界面
	 */
	private void changeMain() {
		if (mHomeFragment == null) {
			mHomeFragment = new HomeFragment();
		}
		changeFrament(mHomeFragment, null, "HomeFragment");
		mCurrentTabIndex = 0;
		setClickTab();
		setIsOpenAct(mIsOpenAct);
	}

	/**
	 * 切换到会员卡界面
	 */
	private void changeCard() {

		if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
			if (mCardHomeFragment == null) {
				mCardHomeFragment = new CardHomeFragment();
			}
			changeFrament(mCardHomeFragment, null, "CardHomeFragment");
			mCurrentTabIndex = 1;
		} else {
			Intent intent = new Intent(this, LoginActivity.class);
			intent.putExtra(LoginTask.ALL_LOGIN, CustConst.Login.CARD_LOGIN);
			this.startActivity(intent);
		}
		setIsOpenAct(CustConst.IsOpenAct.CLOSE);
		if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
			setClickTab();
		} else {
			if (null != mFirstTab) {
				setTabFocused(0, mIvFirst, mTvFirst, mFirstTab);
			}
		}

	}

	/**
	 * 切换到优惠券界面
	 */
	private void changeCoupon() {
		if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
			Log.d(TAG, "couponFlaghomeActivity4=" + DB.getBoolean(DB.Key.CUST_LOGIN));
			if (mCouponHomeFragment == null) {
				mCouponHomeFragment = new CouponHomeFragment();
			}
			changeFrament(mCouponHomeFragment, null, "CouponHomeFragment");
			mCurrentTabIndex = 2;
		} else {
			Intent intent = new Intent(this, LoginActivity.class);
			intent.putExtra(LoginTask.ALL_LOGIN, CustConst.Login.COUPON_LOGIN);
			this.startActivity(intent);
		}
		setIsOpenAct(CustConst.IsOpenAct.CLOSE);
		if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
			setClickTab();
		} else {
			if (null != mFirstTab) {
				setTabFocused(0, mIvFirst, mTvFirst, mFirstTab);
			}
		}

	}

	/**
	 * 切换到我的界面
	 */
	private void changeMy() {

		if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
			if (mMyHomeFragment == null) {
				mMyHomeFragment = new MyHomeFragment();
			}
			changeFrament(mMyHomeFragment, null, "mMyHomeFragment");
			mCurrentTabIndex = 3;
		} else {
			Intent intent = new Intent(this, LoginActivity.class);
			intent.putExtra(LoginTask.ALL_LOGIN, CustConst.Login.MY_LOGIN);
			this.startActivity(intent);
		}
		setIsOpenAct(CustConst.IsOpenAct.CLOSE);
		if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
			setClickTab();
		} else {
			// 没有点击过去
			if (null != mFirstTab) {
				setTabFocused(0, mIvFirst, mTvFirst, mFirstTab);
			}
		}
	}

	/**
	 * 点击的时候设置tab的信息
	 */
	private void setClickTab() {
		if (null != mTabList && mTabList.size() > 0) {
			for (int i = 0; i < mTabList.size(); i++) {
				HomeTab homeTab = mTabList.get(i);
				if (homeTab.getSubModulePosition() == 1) { // 表示该模块在第一个位置
					setTabFocused(0, mIvFirst, mTvFirst, homeTab);
				} else if (homeTab.getSubModulePosition() == 2) { // 表示在第二个位置
					setTabFocused(1, mIvSecond, mTvSecond, homeTab);
				} else if (homeTab.getSubModulePosition() == 3) { // 表示该模块在第三个位置
					setTabFocused(2, mIvThird, mTvThird, homeTab);
				} else if (homeTab.getSubModulePosition() == 4) { // 表示该模块在第四个位置
					setTabFocused(3, mIvFourth, mTvFourth, homeTab);
				}
			}

		}
	}

	/**
	 * 当前是否被选中
	 * 
	 * @param index点击的标示
	 * @param imageView点击的图片
	 * @param textView点击的文字
	 * @param homeTab模块的对象
	 */
	private void setTabFocused(int index, ImageView imageView, TextView textView, HomeTab homeTab) {
		if (mCurrentTabIndex == index) {
			setBgColor(imageView, textView, homeTab.getFocusedUrl(),homeTab.getFocusedBitmap(), homeTab.getFocusedColor(), homeTab);
		} else {
			setBgColor(imageView, textView, homeTab.getNotFocusedUrl(),homeTab.getNotFocusedBitmap(), homeTab.getNotFocusedColor(), homeTab);
		}
	}

	/**
	 * 初始化操作
	 */
	public void initData() {
		checkNet(); // 检测是否有网络
		registerReceiver(); // 网络加测的广播
		if (Util.isNetworkOpen(this)) {
			countAllTypeMsg(); // 是否有消息
		}

		mIsOpenAct = DB.getStr(Const.IS_OPENREGACT);
		setIsOpenAct(mIsOpenAct); // 注册是否有活动

		mIvNewRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, ActThemeDetailActivity.class);
				intent.putExtra(ActThemeDetailActivity.TYPE, CustConst.HactTheme.NEW_REGISTER);
				startActivity(intent);
			}
		});

	}

	/**
	 * 注册网络广播
	 */
	private void registerReceiver() {
		IntentFilter intentFilterNet = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		intentFilterNet.addCategory(Intent.CATEGORY_DEFAULT);
		mNetRecerve = new NetRecerve();
		registerReceiver(mNetRecerve, intentFilterNet);
		mNetRecerve.setHomeFragment(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 注销广播
		unregisterReceiver(mNetRecerve);
	}

	/**
	 * 检测网络
	 * 
	 * @param flag
	 *            true 是有网络 false 是没有网络
	 * @param netType
	 *            1 是有网络 2 是没有网络
	 */
	public void netRecerve(boolean flag) {
		HomeFragment homeFragment = null;
		MyHomeFragment myHomeFragment = null;
		// 首页
		if (null != mMyHomeFragment) {
			homeFragment = (HomeFragment) mHomeFragment;
			if (flag) {
				mRlSetNet.setVisibility(View.GONE);
				if (null != homeFragment) {
					homeFragment.netRecerve(true, 1);
				}
			} else {
				if (null != homeFragment) {
					homeFragment.netRecerve(false, 2);
				}
				mRlSetNet.setVisibility(View.VISIBLE);
			}
		}
		if (null != mMyHomeFragment) {
			myHomeFragment = (MyHomeFragment) mMyHomeFragment;
			myHomeFragment.netRecerve(flag);
		}
	}

	/**
	 * 检测网络
	 */
	private void checkNet() {
		boolean checkFlag = CheckNetworkUtil.checkNetWorkInfo(this);
		if (checkFlag) {
			mRlSetNet.setVisibility(View.VISIBLE);
		} else {
			mRlSetNet.setVisibility(View.GONE);
		}
	}

	// 更换Fragment
	public void changeFrament(BaseFragment tofragment, Bundle bundle, String tag) {
		/*
		 * for (int i = count = mFragmentManager.getBackStackEntryCount(); i <
		 * count; i++) { mFragmentManager.popBackStack(); }
		 */
		BaseFragment framfragment = mCurrentFragment;
		if (framfragment == null) {
			if (mCurrentTabIndex == 0) {
				framfragment = mHomeFragment;
			} else if (mCurrentTabIndex == 1) {
				framfragment = mCardHomeFragment;
			} else if (mCurrentTabIndex == 2) {
				framfragment = mCouponHomeFragment;
			} else if (mCurrentTabIndex == 3) {
				framfragment = mMyHomeFragment;
			} else {
				return;
			}
		}

		if (framfragment.getClass() == tofragment.getClass()) { return; }
		showFragment(framfragment, tofragment);
	}

	public void goToTab(int tabIndex) {
		BaseFragment tofragment = null;
		if (tabIndex == 0) {
			if (mHomeFragment == null) {
				mHomeFragment = new HomeFragment();
			}
			tofragment = mHomeFragment;
		} else if (tabIndex == 1) {
			if (mCardHomeFragment == null) {
				mCardHomeFragment = new CardHomeFragment();
			}
			tofragment = mCardHomeFragment;
		} else if (tabIndex == 2) {
			if (mCouponHomeFragment == null) {
				mCouponHomeFragment = new CouponHomeFragment();
			}
			if (null != mThirdTab && null != mFirstTab) {
				setTabFocused(2, mIvThird, mTvThird, mThirdTab);
				setTabFocused(2, mIvFirst, mTvFirst, mFirstTab);
			}
			tofragment = mCouponHomeFragment;
			tofragment.viewVisible();
		} else if (tabIndex == 3) {
			if (mMyHomeFragment == null) {
				mMyHomeFragment = new MyHomeFragment();
			}
			tofragment = mMyHomeFragment;
		} else {
			return;
		}
		showFragment(null, tofragment);
	}

	public void showFragment(BaseFragment fromfragment, BaseFragment tofragment) {
		FragmentTransaction trx = mFragmentManager.beginTransaction();
		if (mCurrentFragment != null) {
			trx.hide(mCurrentFragment);
		} else if (fromfragment != null) {
			trx.hide(fromfragment);
		}
		if (!tofragment.isAdded()) {
			trx.add(R.id.fragmentRoot, tofragment);
		}
		mCurrentFragment = tofragment;
		try {
			trx.show(tofragment).commit();
		} catch (Exception e) {
			try {
				trx.show(tofragment).commitAllowingStateLoss();
			} catch (Exception ee) {
			}
		}
		if (fromfragment != null) {
			tofragment.viewVisible();
		}
	}

	public void getCityName() {
		Citys city = new Citys();
		city.setName("");
		city.setLatitude(0.0);
		city.setLongitude(0.0);
		DB.saveObj(CustConst.Key.CITY_OBJ, city);
	}

	/**
	 * 得到所有消息
	 */
	private void countAllTypeMsg() {
		new CountAllTypeMsgTask(this, new CountAllTypeMsgTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (null == result) { return; }
				Messages messages = Util.json2Obj(result.toString(), Messages.class);
				int countFirst = (int) Calculate.add(messages.getCommunication(), messages.getShop());
				// int countSecond = (int) Calculate.add(mMessages.getCard(),
				// mMessages.getCoupon());
				int feedback = (int) Calculate.add(countFirst, messages.getFeedback());
				int sumCount = (int) Calculate.add(feedback, messages.getCoupon());
				// sumCount = 100; // 测试数据 TODO
				if (sumCount > 0) {
					mIVMsgPromt.setVisibility(View.VISIBLE);
				} else {
					mIVMsgPromt.setVisibility(View.GONE);
				}

			}
		}).execute();
	}

	/**
	 * 重写点击返回按钮方法，点击一次土司提示，两次退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mClickNum % 2 == 0) {// 若只按下一次
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mClickNum++;
			} else {// 若按下两次
				if (Util.activityRecommonedList.size() > 0 && null != Util.activityRecommonedList) {
					Util.exitRecommoned();
				}
				getCityName();// 清空之前选择的城市
				DB.saveBoolean(CustConst.Key.GRAB_COUPON, false);
				this.finish();
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}