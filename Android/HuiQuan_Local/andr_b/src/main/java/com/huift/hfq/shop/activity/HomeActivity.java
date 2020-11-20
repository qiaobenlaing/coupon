// ---------------------------------------------------------
// @author    yanfang,li
// @version   1.0.0
// @createTime 2015.5.4
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.ShopApplication;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.fragment.BaseFragment;
import com.huift.hfq.shop.fragment.CardHomeFragment;
import com.huift.hfq.shop.fragment.CouponHomeFragment;
import com.huift.hfq.shop.fragment.HomeFragment;
import com.huift.hfq.shop.fragment.MyHomeFragment;
import com.huift.hfq.shop.receiver.MyReceiver;
import com.huift.hfq.shop.utils.DialogUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 主页Activity
 * 
 * @author yanfang.li
 */
public class HomeActivity extends Activity {

	private final static String TAG = "HomeActivity";
	/** 用于savedInstanceState的常量，当前fragment的index */
	public final static String FRAG_INDEX = "frag_index";
	/** 用于对返回键按下次数的计数 */
	private int mClickNum = 0;
	/** “主页”按钮 */
	@ViewInject(R.id.rbtn_main)
	private ImageView mIvMain;
	/** “优惠券”按钮 */
	@ViewInject(R.id.rbtn_coupon)
	private ImageView mIvCoupon;
	/** “会员卡”按钮 */
	@ViewInject(R.id.rbtn_card)
	private ImageView mIvCard;
	/** “我的”按钮 */
	@ViewInject(R.id.rbtn_my)
	private ImageView mIvMy;
	private TextView mTvMain;
	private TextView mTvCoupon;
	private TextView mTvCard;
	private TextView mTvMy;

	private BaseFragment mHomeFragment;
	private BaseFragment mCardHomeFragment;
	private BaseFragment mCouponHomeFragment;
	private BaseFragment mMyHomeFragment;

	public int currentTabIndex = 0;
	private BaseFragment currentFragment = null;

	/** Fragment管理器 */
	private FragmentManager mFragmentManager;
	/** 推送登录的记录数*/
	private static int sJpusLoginData;
	/** 保存全局*/
	private ShopApplication mApplication;
	/** 得到是否入驻的标示*/
	private boolean mSettledflag;
	/** 入驻*/
	private LinearLayout mIvSettled;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		setContentView(R.layout.activity_home);
		// 友盟统计
		MobclickAgent.openActivityDurationTrack(false);
		MobclickAgent.updateOnlineConfig(HomeActivity.this);
		ViewUtils.inject(this);
		mFragmentManager = getFragmentManager();
		if (savedInstanceState != null) {
			currentTabIndex = savedInstanceState.getInt(FRAG_INDEX, 0); // 默认为第一个（index为0）。
		}
		Log.d(TAG, "onCreate()中mCurrFragIndex=" + this.currentTabIndex);
		// 将Push推送
		init(null);
		sJpusLoginData = 0; // 初始化
	}
	
	/**
	 * Activity处于非活动状态而被销毁时保存数据。系统会自动调用。
	 * 
	 * @param savedInstanceState
	 *            保存状态
	 */
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putInt(FRAG_INDEX, this.currentTabIndex);
	}

	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		init(intent);
	}
	
	/**
	 * 接受就是登录的记录数据
	 * @param data
	 */
	public static void getJPushLoginData (int data) {
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
		
		// 初始化Jpush退送登录的记录
		Log.d(TAG, "sJpusLoginData="+sJpusLoginData);
		if (sJpusLoginData == MyReceiver.LOGIN_DATA) { // 有登录的退送
			MyReceiver.setInitLoginData();
			if(null != DialogUtils.dialog){
				DialogUtils.dialog.show();
			}
		}
	}
	
	/**
	 * 监听home键
	 */
	@Override  
    protected void onUserLeaveHint() {  
        super.onUserLeaveHint(); 
        Log.d(TAG,"onUserLeaveHint");  
        DialogUtils.close();
    } 
	
	/**
	 * 登录成功或者退出的操作
	 */
	public static void setJpusLoginData () {
		sJpusLoginData = 0;
		Log.d(TAG, "sJpusLoginData>>>>"+sJpusLoginData);
	}
	
	/**
	 * 再次启动时
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		/*if(null != DialogUtils.dialog){
			DialogUtils.dialog.show();
		}*/
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	

	@OnClick({ R.id.rbtn_main, R.id.rbtn_card, R.id.rbtn_coupon, R.id.rbtn_my,
			R.id.ly_main, R.id.ly_card, R.id.ly_coupon, R.id.ly_my })
	public void changeContent(View v) {
		mClickNum = 0;
		switch (v.getId()) {
		case R.id.rbtn_main: {
			mApplication = (ShopApplication) getApplication();
			mSettledflag = mApplication.getSettledflag();
			Log.d(TAG, "mSettledflag aa======="+mSettledflag);
			if (mSettledflag) {
				mIvSettled.setVisibility(View.GONE);
			} else {
				mIvSettled.setVisibility(View.VISIBLE);
			}
			changeMain();
			MobclickAgent.onEvent(HomeActivity.this, "home_home");
			break;
		}
		case R.id.ly_main: {
			mApplication = (ShopApplication) getApplication();
			mSettledflag = mApplication.getSettledflag();
			Log.d(TAG, "mSettledflag aa======="+mSettledflag);
			if (mSettledflag) {
				mIvSettled.setVisibility(View.GONE);
			} else {
				mIvSettled.setVisibility(View.VISIBLE);
			}
			changeMain();
			MobclickAgent.onEvent(HomeActivity.this, "home_home");
			break;
		}
		case R.id.rbtn_card: {
			mIvSettled.setVisibility(View.GONE);
			changeCard();
			MobclickAgent.onEvent(HomeActivity.this, "home_card");
			break;
		}
		case R.id.ly_card: {
			mIvSettled.setVisibility(View.GONE);
			changeCard();
			MobclickAgent.onEvent(HomeActivity.this, "home_card");
			break;
		}
		case R.id.rbtn_coupon: {
			mIvSettled.setVisibility(View.GONE);
			changeCoupon();
			MobclickAgent.onEvent(HomeActivity.this, "home_coupon");
			break;
		}
		case R.id.ly_coupon: {
			mIvSettled.setVisibility(View.GONE);
			changeCoupon();
			MobclickAgent.onEvent(HomeActivity.this, "home_coupon");
			break;
		}
		case R.id.rbtn_my: {
			mIvSettled.setVisibility(View.GONE);
			changeMy();
			MobclickAgent.onEvent(HomeActivity.this, "home_my");
			break;
		}
		case R.id.ly_my: {
			mIvSettled.setVisibility(View.GONE);
			changeMy();
			MobclickAgent.onEvent(HomeActivity.this, "home_my");
			break;
		}
		default:
			break;
		}
		// changeRadioButtonImage(v.getId());
	}

	/**
	 * 切换到主界面
	 */
	private void changeMain() {
		if (mHomeFragment != null) {
			mHomeFragment.viewVisible();
		}
		if (mHomeFragment == null) {
			mHomeFragment = new HomeFragment();
		}
		changeFrament(mHomeFragment, null, "HomeFragment");
		currentTabIndex = 0;
		changeBgOrColor(R.color.tab_fontnosel, R.color.tab_fontnosel,
				R.color.tab_fontsel, R.color.tab_fontnosel,
				R.drawable.main_btn_1_, R.drawable.main_btn_2,
				R.drawable.main_btn_3, R.drawable.main_btn_4);
	}

	/**
	 * 切换到优惠券界面
	 */
	private void changeCoupon() {
		if (mCouponHomeFragment != null) {
			mCouponHomeFragment.viewVisible();
		}
		if (mCouponHomeFragment == null) {
			mCouponHomeFragment = new CouponHomeFragment();
		}
		changeFrament(mCouponHomeFragment, null, "CouponHomeFragment");
		currentTabIndex = 2;
		changeBgOrColor(R.color.tab_fontnosel, R.color.tab_fontsel,
				R.color.tab_fontnosel, R.color.tab_fontnosel,
				R.drawable.main_btn_1, R.drawable.main_btn_2,
				R.drawable.main_btn_3_, R.drawable.main_btn_4);
	}

	/**
	 * 切换到会员卡界面
	 */
	private void changeCard() {
		if (mCardHomeFragment != null) {
			mCardHomeFragment.viewVisible();
		}
		if (mCardHomeFragment == null) {
			mCardHomeFragment = new CardHomeFragment();
		}
		changeFrament(mCardHomeFragment, null, "CardHomeFragment");
		currentTabIndex = 1;
		changeBgOrColor(R.color.tab_fontnosel, R.color.tab_fontnosel,
				R.color.tab_fontnosel, R.color.tab_fontsel,
				R.drawable.main_btn_1, R.drawable.main_btn_2_,
				R.drawable.main_btn_3, R.drawable.main_btn_4);
	}

	/**
	 * 切换到我的界面
	 */
	private void changeMy() {
		if (mMyHomeFragment != null) {
			mMyHomeFragment.viewVisible();
		}
		if (mMyHomeFragment == null) {
			mMyHomeFragment = new MyHomeFragment();
		}
		changeFrament(mMyHomeFragment, null, "MyHomeFragment");
		currentTabIndex = 3;
		changeBgOrColor(R.color.tab_fontsel, R.color.tab_fontnosel,
				R.color.tab_fontnosel, R.color.tab_fontnosel,
				R.drawable.main_btn_1, R.drawable.main_btn_2,
				R.drawable.main_btn_3, R.drawable.main_btn_4_);
	}

	private void changeBgOrColor(int fontMy, int fontCoupon, int fontMain,
			int fontCard, int ivMain, int ivCard, int ivCoupon, int ivMy) {
		mTvMy.setTextColor(getResources().getColor(fontMy));
		mTvCoupon.setTextColor(getResources().getColor(fontCoupon));
		mTvMain.setTextColor(getResources().getColor(fontMain));
		mTvCard.setTextColor(getResources().getColor(fontCard));
		mIvMain.setBackgroundResource(ivMain);
		mIvCard.setBackgroundResource(ivCard);
		mIvCoupon.setBackgroundResource(ivCoupon);
		mIvMy.setBackgroundResource(ivMy);
	}

	/**
	 * 初始化操作
	 */
	public void init(Intent intent) {
		mIvSettled = (LinearLayout)findViewById(R.id.ly_home_settled);//入驻
		mTvMain = (TextView) findViewById(R.id.tv_main);
		mTvCoupon = (TextView) findViewById(R.id.tv_coupon);
		mTvCard = (TextView) findViewById(R.id.tv_card);
		mTvMy = (TextView) findViewById(R.id.tv_my);
		if (intent == null) {
			intent = this.getIntent();
		}
		int fragid = intent.getIntExtra(ShopConst.FRAG_ID, -1);
		if (fragid > 0) {
			if (fragid == Util.NUM_ONE) { // 跳转到优惠券界面
				Log.d(TAG, "mCouponHomeFragment = " + mCouponHomeFragment);
				if (mCouponHomeFragment == null) {
					mCouponHomeFragment = new CouponHomeFragment();
				}
				currentTabIndex = 2;
				showFragment(null, mCouponHomeFragment);
				mIvCoupon.setBackgroundResource(R.drawable.main_btn_3_);
				mTvCoupon.setTextColor(getResources().getColor(
						R.color.tab_fontsel));

			} else if (fragid == Util.NUM_TWO) { // 跳转到会员卡界面
				if (mCardHomeFragment == null) {
					mCardHomeFragment = new CardHomeFragment();
				}
				currentTabIndex = 1;
				showFragment(null, mCardHomeFragment);
				mIvCard.setBackgroundResource(R.drawable.main_btn_2_);
				mTvCard.setTextColor(getResources().getColor(
						R.color.tab_fontsel));
			} else if (fragid == Util.NUM_THIRD) {
				if (mMyHomeFragment == null) { // 跳转到我的界面
					mMyHomeFragment = new MyHomeFragment();
				}
				currentTabIndex = 3;
				showFragment(null, mMyHomeFragment);
				mIvMy.setBackgroundResource(R.drawable.main_btn_4_);
				mTvMy.setTextColor(getResources().getColor(R.color.tab_fontsel));
			} else if (fragid == Util.NUM_ZERO) {
				if (mHomeFragment == null) { // 跳转到首页
					mHomeFragment = new HomeFragment();
				}
				currentTabIndex = 0;
				showFragment(null, mHomeFragment);
				mIvMain.setBackgroundResource(R.drawable.main_btn_1_);
				mTvMain.setTextColor(getResources().getColor(
						R.color.tab_fontsel));
			}
		} else {
			if (mHomeFragment == null) {
				mHomeFragment = new HomeFragment();
			}
			// changeFrament(mHomeFragment, null, "HomeFragment");
			showFragment(null, mHomeFragment);
			mIvMain.setBackgroundResource(R.drawable.main_btn_1_);
			mTvMain.setTextColor(getResources().getColor(R.color.tab_fontsel));
		}

	}

	// 更换Fragment
	public void changeFrament(BaseFragment tofragment, Bundle bundle, String tag) {
		/*
		 * for (int i = 0, count = mFragmentManager.getBackStackEntryCount(); i
		 * < count; i++) { mFragmentManager.popBackStack(); }
		 */
		BaseFragment framfragment = currentFragment;
		if (framfragment == null) {
			if (currentTabIndex == 0) {
				framfragment = mHomeFragment;
			} else if (currentTabIndex == 1) {
				framfragment = mCardHomeFragment;
			} else if (currentTabIndex == 2) {
				framfragment = mCouponHomeFragment;
			} else if (currentTabIndex == 3) {
				framfragment = mMyHomeFragment;
			} else {
				return;
			}
		}

		if (framfragment.getClass() == tofragment.getClass()) {
			return;
		}
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
			tofragment = mCouponHomeFragment;
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
		if (currentFragment != null) {
			trx.hide(currentFragment);
		} else if (fromfragment != null) {
			trx.hide(fromfragment);
		}
		if (!tofragment.isAdded()) {
			trx.add(R.id.fragmentRoot, tofragment);
		}
		currentFragment = tofragment;
		try {
			trx.show(tofragment).commit();
		} catch (Exception e) {
			try {
				trx.show(tofragment).commitAllowingStateLoss();
			} catch (Exception ee) {
			}
		}

	}

	/**
	 * 根据点击按钮的ID调用不同参的changeImage()方法
	 * 
	 * @param viewId
	 */
	public void changeRadioButtonImage(int viewId) {
		int[] imageh = { R.drawable.main_btn_2, R.drawable.main_btn_3,
				R.drawable.main_btn_4, R.drawable.main_btn_1 };
		int[] imagel = { R.drawable.main_btn_2_, R.drawable.main_btn_3_,
				R.drawable.main_btn_4_, R.drawable.main_btn_1_ };
		int[] rbtn = { R.id.rbtn_card, R.id.rbtn_coupon, R.id.rbtn_my,
				R.id.rbtn_main };
		switch (viewId) {
		case R.id.rbtn_card:
			changeImage(imageh, imagel, rbtn, 0);
			break;
		case R.id.rbtn_coupon:
			changeImage(imageh, imagel, rbtn, 1);
			break;
		case R.id.rbtn_my:
			changeImage(imageh, imagel, rbtn, 2);
			break;
		case R.id.rbtn_main:
			changeImage(imageh, imagel, rbtn, 3);
			break;
		default:
			break;
		}
	}

	/**
	 * 更换底部图标
	 * 
	 * @param image1
	 *            原图片数据源
	 * @param image2
	 *            更改图片数据源
	 * @param rbtnid
	 *            RadioBtn的Id
	 * @param index
	 *            角标
	 */
	public void changeImage(int[] image1, int[] image2, int[] rbtnid, int index) {
		for (int i = 0; i < image1.length; i++) {
			if (i != index) {
				((ImageView) findViewById(rbtnid[i]))
						.setBackgroundResource(image1[i]);
			} else {
				((ImageView) findViewById(rbtnid[i]))
						.setBackgroundResource(image2[i]);
			}
		}
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
				this.finish();
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
