package com.huift.hfq.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
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
import com.huift.hfq.shop.fragment.CardHomeFragment;
import com.huift.hfq.shop.fragment.CouponHomeFragment;
import com.huift.hfq.shop.fragment.HomeFragment;
import com.huift.hfq.shop.fragment.MyHomeFragment;
import com.huift.hfq.shop.model.GetNewestShopAppVersionTask;
import com.huift.hfq.shop.service.UpdateService;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.minidev.json.JSONObject;

/**
 * 主页Activity
 *
 * @author yanfang.li
 */
public class HomeActivity extends FragmentActivity {

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
    private TextView mTvCard;
    private TextView mTvCoupon;
    private TextView mTvMy;

    private HomeFragment mHomeFragment;
    private CardHomeFragment mCardHomeFragment;
    private CouponHomeFragment mCouponHomeFragment;
    private MyHomeFragment mMyHomeFragment;
    public int currentTabIndex = 0;

    /**
     * Fragment管理器
     */
    private FragmentTransaction fragmentTransaction;
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
        if (savedInstanceState != null) {
            currentTabIndex = savedInstanceState.getInt(FRAG_INDEX, 0); // 默认为第一个（index为0）。
        }
        setContentView(R.layout.activity_home);
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
        ViewUtils.inject(this);
        init(null);
        isUpdate();
    }

    /**
     * 初始化操作
     */
    public void init(Intent intent) {
        mIvSettled = (LinearLayout) findViewById(R.id.ly_home_settled);//入驻
        mTvMain = (TextView) findViewById(R.id.tv_main);
        mTvCard = (TextView) findViewById(R.id.tv_card);
        mTvCoupon = (TextView) findViewById(R.id.tv_coupon);
        mTvMy = (TextView) findViewById(R.id.tv_my);

        if (intent != null) {
            currentTabIndex = intent.getIntExtra(ShopConst.FRAG_ID, 0);
        }
        switchFragment(currentTabIndex);
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
                        if (newVersionNum - currentVersionNum > 0) {
                            ShopApplication.canUpdate = true; // 有更新类容
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
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(FRAG_INDEX, currentTabIndex);
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        init(intent);
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
                switchFragment(0);
                break;
            }
            case R.id.ly_card: {
                mIvSettled.setVisibility(View.GONE);
                if (mCardHomeFragment != null) {
                    mCardHomeFragment.viewVisible();
                }
                switchFragment(1);
                break;
            }
            case R.id.ly_coupon: {
                mIvSettled.setVisibility(View.GONE);
                if (mCouponHomeFragment != null) {
                    mCouponHomeFragment.viewVisible();
                }
                switchFragment(2);
                break;
            }
            case R.id.ly_my: {
                mIvSettled.setVisibility(View.GONE);
                if (mMyHomeFragment != null) {
                    mMyHomeFragment.viewVisible();
                }
                switchFragment(3);
                break;
            }
        }
    }

    private void switchFragment(int position) {
        currentTabIndex = position;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        cleanBackground();
        switch (position) {
            case 0:
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.fragmentRoot, mHomeFragment);
                }
                fragmentTransaction.show(mHomeFragment);
                mTvMain.setTextColor(getResources().getColor(R.color.tab_fontsel));
                mIvMain.setBackgroundResource(R.drawable.main_btn_1_);
                break;
            case 1:
                if (mCardHomeFragment == null) {
                    mCardHomeFragment = new CardHomeFragment();
                    fragmentTransaction.add(R.id.fragmentRoot, mCardHomeFragment);
                }
                fragmentTransaction.show(mCardHomeFragment);
                mTvCard.setTextColor(getResources().getColor(R.color.tab_fontsel));
                mIvCard.setBackgroundResource(R.drawable.main_btn_2_);
                break;
            case 2:
                if (mCouponHomeFragment == null) {
                    mCouponHomeFragment = new CouponHomeFragment();
                    fragmentTransaction.add(R.id.fragmentRoot, mCouponHomeFragment);
                }
                fragmentTransaction.show(mCouponHomeFragment);
                mTvCoupon.setTextColor(getResources().getColor(R.color.tab_fontsel));
                mIvCoupon.setBackgroundResource(R.drawable.main_btn_3_);
                break;
            case 3:
                if (mMyHomeFragment == null) {
                    mMyHomeFragment = new MyHomeFragment();
                    fragmentTransaction.add(R.id.fragmentRoot, mMyHomeFragment);
                }
                fragmentTransaction.show(mMyHomeFragment);
                mTvMy.setTextColor(getResources().getColor(R.color.tab_fontsel));
                mIvMy.setBackgroundResource(R.drawable.main_btn_4_);
                break;
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void cleanBackground() {
        for (Fragment frag : getSupportFragmentManager().getFragments()) {
            fragmentTransaction.hide(frag);
        }
        mTvMain.setTextColor(getResources().getColor(R.color.tab_fontnosel));
        mTvCard.setTextColor(getResources().getColor(R.color.tab_fontnosel));
        mTvCoupon.setTextColor(getResources().getColor(R.color.tab_fontnosel));
        mTvMy.setTextColor(getResources().getColor(R.color.tab_fontnosel));
        mIvMain.setBackgroundResource(R.drawable.main_btn_1);
        mIvCard.setBackgroundResource(R.drawable.main_btn_2);
        mIvCoupon.setBackgroundResource(R.drawable.main_btn_3);
        mIvMy.setBackgroundResource(R.drawable.main_btn_4);
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
