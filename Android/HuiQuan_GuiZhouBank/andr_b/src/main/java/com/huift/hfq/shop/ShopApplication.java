package com.huift.hfq.shop;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.huift.hfq.base.Util;
import com.huift.hfq.shop.icbcPay.DeviceService;
import com.huift.hfq.shop.icbcPay.TransService;
import com.huift.hfq.shop.utils.ExampleUtil;

/**
 * Shop Application
 * .
 *
 * @author Weiping
 */
public class ShopApplication extends Application {

    /* 是否正在下载Apk */
    public static boolean isApkDownloading = false;
    /* 是否有可用更新 */
    public static boolean canUpdate = false;
    /* 商家是否入驻*/
    private boolean settledflag = true;

    private final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    private final String JOIN_ACT = "JOIN_ACTIVITY";
    private final String EXIT_ACT = "EXIT_ACTIVITY";
    private final String CONSUME = "CONSUME";
    private final String LOGIN = "LOGIN";
    private final String INVITE_SHOP = "INVITE_SHOP";
    private final String REMIND_SHOP = "REMIND_SHOP";

    @Override
    public void onCreate() {
        super.onCreate();
        //绑定设备驱动服务
        DeviceService.getInstance(this).bindDeviceService();
        //绑定支付服务
        TransService.getInstance(this).bindTransService();
        registerMessageReceiver();
    }

    public void registerMessageReceiver() {
        //用于接收来自服务器jpush客户信息
        MessageReceiver mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    /**
     * jPush的广播接受者
     */
    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra("message");
                String extras = intent.getStringExtra("extras");
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(messge + "");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(extras + "");
                }
                if (JOIN_ACT.equals(messge)) {//报名活动
                } else if (EXIT_ACT.equals(messge)) {//退出活动
                } else if (CONSUME.equals(messge)) {//订单
                } else if (LOGIN.equals(messge)) {//登录
                } else if (INVITE_SHOP.equals(messge)) {//邀请入驻惠圈
                } else if (REMIND_SHOP.equals(messge)) {//添加商品展示
                }
            }
        }
    }

    public boolean getSettledflag() {
        return settledflag;
    }

    public void setSettledflag(boolean settledflag) {
        this.settledflag = settledflag;
    }

    /**
     * 提示的消息
     */
    public void getDateInfo(Activity activity) {
        Util.getContentValidate(R.string.shop_no_settled);
    }

}
