// ---------------------------------------------------------
// @author    
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huift.hfq.base.Const;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.base.pojo.UserCardVip;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopApplication;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.LoginActivity;
import com.huift.hfq.shop.activity.MyHomeAboutActivity;
import com.huift.hfq.shop.activity.MyHomeSettingActivity;
import com.huift.hfq.shop.activity.MyImageShopActivity;
import com.huift.hfq.shop.activity.MyOrderManagerActivity;
import com.huift.hfq.shop.activity.MyShopInfoActivity;
import com.huift.hfq.shop.activity.OriginalPwdActivity;
import com.huift.hfq.shop.activity.StaffManagerActivity;
import com.huift.hfq.shop.activity.TextMessageActivity;
import com.huift.hfq.shop.activity.VipChatActivity;
import com.huift.hfq.shop.model.LogoffTask;
import com.huift.hfq.shop.model.SgetShopBasicInfoTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.minidev.json.JSONObject;

/**
 * 我的
 *
 * @author qian.zhou
 */
public class MyHomeFragment extends Fragment {
    public static final String MSUGGEST = String.valueOf(Util.NUM_ONE);
    /**
     * 展示的商家头像
     */
    private ImageView mIvShow;
    /**
     * 商店对象
     */
    private Shop mShop;
    /**
     * 对商家一间的消息数目
     */
    private TextView mTvMsuggestCount;
    /**
     * 对惠圈建议的消息数目
     */
    private int mMsuggestCount;
    /**
     * 店员管理
     */
    private LinearLayout mLyStaffManager;
    /**
     * 当商家的店铺信息
     */
    private boolean mUppFlag = false;
    /**
     * 定义全局变量
     */
    private ShopApplication mShopApplication;
    /**
     * 得到是否入驻的标示
     */
    private boolean mSettledflag;

    /**
     * 需要传递参数时有利于解耦
     *
     * @return MyHomeFragment
     */
    public static MyHomeFragment newInstance() {
        Bundle args = new Bundle();
        MyHomeFragment fragment = new MyHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_myhome, container, false);
        ViewUtils.inject(this, v);
        Util.addHomeActivity(getActivity());
        init(v);
        return v;
    }

    public void viewVisible() {
        mMsuggestCount = DB.getInt(ShopConst.Massage.FEED_BACK);
        setMsuggestCount(mMsuggestCount);
    }

    /**
     * 判断activity不为空
     *
     * @return
     */
    private Activity getMyActivity() {
        Activity act = getActivity();
        if (act == null) {
            act = AppUtils.getActivity();
        }
        return act;
    }

    /**
     * 设置商家建议
     */
    private void setMsuggestCount(int count) {
        if (count > 0) {
            if (count > 99) {
                mTvMsuggestCount.setText(99 + "+");
            } else {
                mTvMsuggestCount.setText(count + "");
            }
            mTvMsuggestCount.setVisibility(View.VISIBLE);
        } else {
            mTvMsuggestCount.setVisibility(View.GONE);
        }
    }

    // 初始化方法
    private void init(View v) {
        mShopApplication = (ShopApplication) getActivity().getApplication();
        mSettledflag = mShopApplication.getSettledflag();
        // 获得一个用户信息对象
        UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
        //初始化视图
        initView(v);
        //当登陆者的状态为员工的时候，员工管理隐藏 否则显示
        mLyStaffManager.setVisibility(String.valueOf(Util.NUM_ONE).equals(userToken.getUserLvl()) ? View.GONE : View.VISIBLE);
        // 得到商家建议
        mMsuggestCount = DB.getInt(ShopConst.Massage.FEED_BACK);
        // 设置商家建议
        setMsuggestCount(mMsuggestCount);
        //全查询商家信息
        MyShopInfo();
    }

    /**
     * 初始化视图
     */
    public void initView(View v) {
        //标题
        Shop myshop = DB.getObj(DB.Key.SHOP_INFO, Shop.class);
        LinearLayout ivTurn = (LinearLayout) v.findViewById(R.id.layout_turn_in);
        ivTurn.setVisibility(View.GONE);
        TextView tvTitle = (TextView) v.findViewById(R.id.tv_mid_content);
        TextView tvMsg = (TextView) v.findViewById(R.id.tv_msg);
        tvMsg.setVisibility(View.GONE);
        //初始化数据
        TextView tvAddress = (TextView) v.findViewById(R.id.tv_myaddress);
        tvAddress.requestFocus();
        TextView tvShopName = (TextView) v.findViewById(R.id.tv_shopname);
        mIvShow = (ImageView) v.findViewById(R.id.iv_showlogo);
        mTvMsuggestCount = (TextView) v.findViewById(R.id.tv_msuggest_coun);
        mLyStaffManager = (LinearLayout) v.findViewById(R.id.layout_staff_manager);
        //给控件赋值
        if (myshop != null) {
            if (!Util.isEmpty(myshop.getShopTitle())) {
                tvTitle.setText(myshop.getShopTitle());
            }
            if (!Util.isEmpty(myshop.getShopName())) {
                tvShopName.setText(myshop.getShopName());
            }
            if (!Util.isEmpty(myshop.getProvince()) && !Util.isEmpty(myshop.getCity())
                    && !Util.isEmpty(myshop.getStreet())) {
                tvAddress.setText(myshop.getProvince() + myshop.getCity() + myshop.getStreet());
            }
        } else {
            if (!Util.isEmpty(mShop.getShopTitle())) {
                tvTitle.setText(mShop.getShopTitle());
            }
            if (!Util.isEmpty(mShop.getShopName())) {
                tvShopName.setText(mShop.getShopName());
            }
            if (!Util.isEmpty(mShop.getProvince()) && !Util.isEmpty(mShop.getCity())
                    && !Util.isEmpty(mShop.getStreet())) {
                tvAddress.setText(mShop.getProvince() + mShop.getCity() + mShop.getStreet());
            }
        }
    }

    /**
     * 商铺查询的异步任务类
     */
    public void MyShopInfo() {
        // 调用MyHomeTask异步请求，然后修改UI控件
        new SgetShopBasicInfoTask(getActivity(), new SgetShopBasicInfoTask.Callback() {
            @Override
            public void getResult(JSONObject jsonObject) {
                if (jsonObject == null) {
                    return;
                }
                mShop = Util.json2Obj(jsonObject.toString(), Shop.class);
                Util.showImage(getActivity(), mShop.getLogoUrl(), mIvShow);
            }
        }).execute();
    }

    /**
     * 点击事件
     */
    @OnClick({R.id.layout_shop_info, R.id.layout_myhome_advice, R.id.layout_myhome_about, R.id.layout_my_shop, R.id.layout_update_pwd,
            R.id.layout_my_order, R.id.layout_myhome_set, R.id.layout_staff_manager, R.id.ly_set_message, R.id.btn_logout})
    private void ivTurnTo(View v) {
        switch (v.getId()) {
            //进入商店详情
            case R.id.layout_shop_info:
                Intent intent1 = new Intent(getActivity(), MyShopInfoActivity.class);
                startActivity(intent1);
                break;
            //进入店员管理
            case R.id.layout_staff_manager:
                Intent managerIntent = new Intent(getActivity(), StaffManagerActivity.class);
                startActivity(managerIntent);
                break;
            //进入店铺形象
            case R.id.layout_my_shop:
                Intent shopIntent = new Intent(getActivity(), MyImageShopActivity.class);
                shopIntent.putExtra(MyImageShopFragment.SHOP_OBJ, mShop);
                startActivity(shopIntent);
                break;
            //进入对惠圈的建议
            case R.id.layout_myhome_advice:
                if (mSettledflag) {
                    Intent adviceIntent = new Intent(getActivity(), VipChatActivity.class);
                    UserCardVip userCardVip = new UserCardVip();
                    userCardVip.setUserCode(Const.HQ_CODE);
                    adviceIntent.putExtra(VipChatFragment.USER_OBJ, userCardVip);
                    adviceIntent.putExtra(VipChatFragment.MSUGGEST, MSUGGEST);
                    mTvMsuggestCount.setVisibility(View.GONE); // 隐藏商家建议
                    ImageView ivMyMsgpromt = (ImageView) getActivity().findViewById(R.id.my_msgpromt);
                    ivMyMsgpromt.setVisibility(View.GONE);
                    startActivity(adviceIntent);
                } else {
                    mShopApplication.getDateInfo(getActivity());
                }
                break;
            //进入关于页面
            case R.id.layout_myhome_about:
                startActivity(new Intent(getActivity(), MyHomeAboutActivity.class));
                break;
            //进入修改密码页面
            case R.id.layout_update_pwd:
                if (mSettledflag) {
                    startActivity(new Intent(getActivity(), OriginalPwdActivity.class));
                } else {
                    mShopApplication.getDateInfo(getActivity());
                }
                break;
            //进入我的订单页面
            case R.id.layout_my_order:
                startActivity(new Intent(getActivity(), MyOrderManagerActivity.class));
                break;
            // 短信接受设置
            case R.id.ly_set_message:
                startActivity(new Intent(getActivity(), TextMessageActivity.class));
                break;
            //进入设置页面
            case R.id.layout_myhome_set:
                startActivity(new Intent(getActivity(), MyHomeSettingActivity.class));
                break;
            case R.id.btn_logout:
                DialogUtils.showDialog(getActivity(), getString(R.string.cue), getString(R.string.quit_ok), getString(R.string.ok), getString(R.string.no), new DialogUtils().new OnResultListener() {
                    @Override
                    public void onOK() {
                        logoff();
                    }
                });
                break;
        }
    }

    /**
     * 退出登录
     */
    public void logoff() {
        String APP_TYPE = "0";
        String mRegistrationId = DB.getStr(ShopConst.JPUSH_REGID);
        new LogoffTask(getActivity(), new LogoffTask.Callback() {
            @Override
            public void getResult(JSONObject object) {
                if (object == null) {
                    return;
                } else {
                    if (String.valueOf(ErrorCode.SUCC).equals(object.get("code").toString())) {
                        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences(ShopConst.LOGIN_KEEP, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        String mobileNbr = mSharedPreferences.getString("mobileNbr", "");
                        String password = mSharedPreferences.getString("password", "");
                        editor.clear();
                        editor.commit();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        Util.exitHome();
                    } else {
                        Util.showToastZH("退出不成功");
                    }
                }
            }
        }).execute(APP_TYPE, mRegistrationId);
    }

    @Override
    public void onResume() {
        super.onResume();
        Activity act = getMyActivity();
        if (act == null) {
            return;
        }
        mUppFlag = DB.getBoolean(ShopConst.Key.UPP_SHOPINFO);
        if (mUppFlag) {
            DB.saveBoolean(ShopConst.Key.UPP_SHOPINFO, false);
            MyShopInfo();
        }
    }
}
