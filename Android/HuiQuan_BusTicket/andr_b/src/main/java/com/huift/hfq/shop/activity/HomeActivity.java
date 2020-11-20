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
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.AppUpdate;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopApplication;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.fragment.BaseFragment;
import com.huift.hfq.shop.fragment.CardHomeFragment;
import com.huift.hfq.shop.fragment.CouponHomeFragment;
import com.huift.hfq.shop.fragment.HomeFragment;
import com.huift.hfq.shop.fragment.MyHomeFragment;
import com.huift.hfq.shop.model.GetNewestShopAppVersionTask;
import com.huift.hfq.shop.receiver.MyReceiver;
import com.huift.hfq.shop.service.UpdateService;
import com.huift.hfq.shop.utils.DialogUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.minidev.json.JSONObject;

/**
 * 主页Activity
 *
 * @author yanfang.li
 */
public class HomeActivity extends Activity {

    /**
     * 用于savedInstanceState的常量，当前fragment的index
     */
    public final static String FRAG_INDEX = "frag_index";
    /**
     * 用于对返回键按下时间
     */
    private long mPressedTime = 0;
    @ViewInject(R.id.mIvMain)
    private ImageView mIvMain;
    @ViewInject(R.id.mIvCoupon)
    private ImageView mIvCoupon;
    @ViewInject(R.id.mIvCard)
    private ImageView mIvCard;
    @ViewInject(R.id.mIvMy)
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

    /**
     * Fragment管理器
     */
    private FragmentManager mFragmentManager;
    /**
     * 推送登录的记录数
     */
    private static int sJpusLoginData;
    /**
     * 保存全局
     */
    private ShopApplication mApplication;
    /**
     * 得到是否入驻的标示
     */
    private boolean mSettledflag;
    /**
     * 入驻
     */
    private LinearLayout mIvSettled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
        setContentView(R.layout.activity_home);
        ViewUtils.inject(this);
        mFragmentManager = getFragmentManager();
        if (savedInstanceState != null) {
            currentTabIndex = savedInstanceState.getInt(FRAG_INDEX, 0); // 默认为第一个（index为0）。
        }
        // 将Push推送
        init(null);
        sJpusLoginData = 0; // 初始化
        isUpdate();
    }

    /**
     * 检查更新
     */
    public void isUpdate() {
        new GetNewestShopAppVersionTask(this, new GetNewestShopAppVersionTask.Callback() {

            @Override
            public void getResult(JSONObject result) {
                if (result != null) {
                    AppUpdate update = Util.json2Obj(result.toString(), AppUpdate.class);
                    if (update != null) {
                        DB.saveObj(ShopConst.Key.APP_UPP, update); // 保存更新的对象
                        String currentVersion = Util.getAppVersionCode(HomeActivity.this);
                        String newVersion = update.getVersionCode();
                        long currentVersionNum = Long.parseLong(currentVersion.toLowerCase()
                                .replaceAll("v", "")
                                .replaceAll("．", "")
                                .replace(".", ""));
                        long newVersionNum = Long.parseLong(newVersion.toLowerCase()
                                .replaceAll("v", "")
                                .replaceAll("．", "")
                                .replace(".", ""));
                        // 服务器上新版本比现在app的版本高的话就提示升级
                        if (newVersionNum-currentVersionNum > 0) {
                            ShopApplication.canUpdate=true; // 有更新类容
                            UpdateService.show(HomeActivity.this);
                        }
                    }
                }
            }
        }).execute();
    }

    /**
     * Activity处于非活动状态而被销毁时保存数据。系统会自动调用。
     *
     * @param savedInstanceState 保存状态
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

        // 初始化Jpush退送登录的记录
        if (sJpusLoginData == MyReceiver.LOGIN_DATA) { // 有登录的退送
            MyReceiver.setInitLoginData();
            if (null != DialogUtils.dialog) {
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
        DialogUtils.close();
    }

    /**
     * 登录成功或者退出的操作
     */
    public static void setJpusLoginData() {
        sJpusLoginData = 0;
    }

    @OnClick({R.id.ly_main, R.id.ly_card, R.id.ly_coupon, R.id.ly_my})
    public void changeContent(View v) {
        switch (v.getId()) {
            case R.id.ly_main: {
                mApplication = (ShopApplication) getApplication();
                mSettledflag = mApplication.getSettledflag();
                if (mSettledflag) {
                    mIvSettled.setVisibility(View.GONE);
                } else {
                    mIvSettled.setVisibility(View.VISIBLE);
                }
                changeMain();
                break;
            }
            case R.id.ly_card: {
                mIvSettled.setVisibility(View.GONE);
                changeCard();
                break;
            }
            case R.id.ly_coupon: {
                mIvSettled.setVisibility(View.GONE);
                changeCoupon();
                break;
            }
            case R.id.ly_my: {
                mIvSettled.setVisibility(View.GONE);
                changeMy();
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

    private void changeBgOrColor(int fontMy, int fontCoupon, int fontMain, int fontCard,
                                 int ivMain, int ivCard, int ivCoupon, int ivMy) {
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
        mIvSettled = (LinearLayout) findViewById(R.id.ly_home_settled);//入驻
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
            trx.show(tofragment).commitAllowingStateLoss();
        }

    }

    /**
     * 重写点击返回按钮方法，点击一次土司提示，两次退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long mNowTime = System.currentTimeMillis();//获取第一次按键时间
            if ((mNowTime - mPressedTime) > 2000) {//比较两次按键时间差
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mPressedTime = mNowTime;
            } else {//退出程序
                UpdateService.clearNotification();
                this.finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
