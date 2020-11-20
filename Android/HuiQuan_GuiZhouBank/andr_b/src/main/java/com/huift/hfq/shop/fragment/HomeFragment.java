// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.Messages;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.base.pojo.StaffShop;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.Calculate;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopApplication;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.BillHomeActivity;
import com.huift.hfq.shop.activity.CampaignListActivity;
import com.huift.hfq.shop.activity.CashierActivity;
import com.huift.hfq.shop.activity.ChooseStoreActivity;
import com.huift.hfq.shop.activity.MassageActivity;
import com.huift.hfq.shop.activity.MyOrderManagerActivity;
import com.huift.hfq.shop.activity.ScanActivity;
import com.huift.hfq.shop.activity.ScanInputAmountActivity;
import com.huift.hfq.shop.activity.SecurityCodeActivity;
import com.huift.hfq.shop.activity.SettledActivity;
import com.huift.hfq.shop.activity.UserAccessActivity;
import com.huift.hfq.shop.model.CountAllTypeMsgTask;
import com.huift.hfq.shop.model.GetShopAllBrowseQuantityTask;
import com.huift.hfq.shop.model.GetStaffShopListTask;
import com.huift.hfq.shop.model.SgetShopBasicInfoTask;
import com.lidroid.xutils.ViewUtils;

import net.minidev.json.JSONObject;

import java.util.List;

/**
 * HomeFragment 主页显示页面
 *
 * @author yanfang.li
 */
public class HomeFragment extends Fragment {
    //扫一扫标签
    public static final String FLAG = HomeFragment.class.getSimpleName();

    private static final String FISRT_RUN = "1";
    /**
     * intent跳转的时的请求参数
     */
    private static final int REQUEST_CODE = 10;
    /**
     * 消息返回的对象
     */
    public static final String MESSAGE_OBj = "messageHomeObj";
    /**
     * 视图
     */
    private View mView;
    private ShopApplication mApplication;
    /**
     * 提示消息数目
     */
    private TextView tvMsgPrompt;
    /**
     * 消息
     */
    private Messages mMessages;
    /**
     * 我的消息提示
     */
    private ImageView IvMyMsgpromt;
    //标题栏
    private LinearLayout ly_choose_store;
    /**
     * 头顶标题名称
     */
    private TextView mTvShopName;
    /**
     * 选择图标
     */
    private ImageView mIvChoose;
    /**
     * 入驻
     */
    private LinearLayout mIvSettled;
    /**
     * 得到是否入驻的标示
     */
    private boolean mSettledflag;

    /**
     * 用户总浏览量
     */
    private TextView mTvUserAccessCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_home, container, false);
            ViewUtils.inject(this, mView);
            Util.addHomeActivity(getMyActivity());
            init();// 初始化数据
        }
        return mView;
    }

    /**
     * 保证activity不为空
     *
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
     * 初始化控件
     */
    private void init() {
        DB.saveStr(CouponHomeFragment.COUPON_FISRT, FISRT_RUN); // 出事化进入优惠券界面的次数
        DB.saveStr(CardHomeFragment.CARD_FISRT, FISRT_RUN); // 出事化进入会员卡界面的次数
        mApplication = (ShopApplication) getMyActivity().getApplicationContext();
        IvMyMsgpromt = (ImageView) getMyActivity().findViewById(R.id.my_msgpromt);
        ImageView ivScan = (ImageView) mView.findViewById(R.id.iv_scan);
        mIvChoose = (ImageView) mView.findViewById(R.id.iv_choose);
        mTvShopName = (TextView) mView.findViewById(R.id.tv_mid_content);
        ly_choose_store = mView.findViewById(R.id.ly_choose_store);
        tvMsgPrompt = (TextView) mView.findViewById(R.id.tv_allprompt);
        mTvUserAccessCount = (TextView) mView.findViewById(R.id.tv_access_count);
        mIvSettled = (LinearLayout) getActivity().findViewById(R.id.ly_home_settled);// 入驻

        // TODO商家名称
        ImageView ivCard = (ImageView) mView.findViewById(R.id.rbtn_homecard);
        ImageView ivCoupon = (ImageView) mView.findViewById(R.id.rbtn_homecoupon);
        ImageView ivMarketAct = (ImageView) mView.findViewById(R.id.rbtn_marketact);
        ImageView ivPost = (ImageView) mView.findViewById(R.id.rbtn_audit);
        ImageView ivCashier = (ImageView) mView.findViewById(R.id.rbtn_cashier);
        ImageView ivMassage = (ImageView) mView.findViewById(R.id.rbtn_msg);
        ImageView ivBtn = (ImageView) mView.findViewById(R.id.rbtn_order);

        ivCard.setOnClickListener(ivSkipClick);
        ivCoupon.setOnClickListener(ivSkipClick);
        ivMarketAct.setOnClickListener(ivSkipClick);
        ivPost.setOnClickListener(ivSkipClick);
        ivCashier.setOnClickListener(ivSkipClick);
        ivMassage.setOnClickListener(ivSkipClick);
        ivScan.setOnClickListener(ivSkipClick);
        ivBtn.setOnClickListener(ivSkipClick);
        mIvSettled.setOnClickListener(ivSkipClick);

        // 是否入驻
        getSettled();
        // 获得商店的信息
        myShopInfo();
        // 得到所有消息
        countAllTypeMsg();
        // 查询分店列表
        getStaffShopList();
        // 获得用户总浏览量
        getUserAccess();
    }

    /**
     * 判断商家是否入驻
     */
    private void getSettled() {
        new SgetShopBasicInfoTask(getMyActivity(), new SgetShopBasicInfoTask.Callback() {
            @Override
            public void getResult(JSONObject result) {
                Shop shop = Util.json2Obj(result.toString(), Shop.class);
                if (Integer.parseInt(shop.getShopStatus()) == 1) {// 未入住
                    mIvSettled.setVisibility(View.VISIBLE);
                } else if (Integer.parseInt(shop.getShopStatus()) == 2) {// 入驻
                    mIvSettled.setVisibility(View.GONE);
                }
                mApplication.setSettledflag(Integer.parseInt(shop.getShopStatus()) == 2);
                mSettledflag = mApplication.getSettledflag();
            }
        }).execute();
    }

    /**
     * 获得商店的信息
     */
    private void myShopInfo() {
        new SgetShopBasicInfoTask(getMyActivity(), new SgetShopBasicInfoTask.Callback() {
            @Override
            public void getResult(JSONObject object) {
                if (object == null) {
                    DB.saveObj(DB.Key.SHOP_INFO, null);
                } else {
                    Shop shop = Util.json2Obj(object.toString(), Shop.class);
                    if (!Util.isEmpty(shop.getShopName())) {
                        String shopName = shop.getShopName();
                        String shopTitle = "";
                        if (shopName.indexOf("（") > 0) {
                            shopTitle = shopName.substring(0, shopName.indexOf("（"));
                        } else if (shopName.indexOf("(") > 0) {
                            shopTitle = shopName.substring(0, shopName.indexOf("("));
                        } else {
                            shopTitle = shop.getShopName();
                        }
                        String shopSimple = "";
                        if (shopTitle.length() > 10) {
                            shopSimple = shopTitle.substring(0, 10) + "...";
                        } else {
                            shopSimple = shopTitle;
                        }

                        shop.setShopTitle(shopSimple);

                        DB.saveObj(DB.Key.SHOP_INFO, shop);
                        mTvShopName.setText(shopSimple);
                    } else {
                        shop.setShopTitle("");
                        DB.saveObj(DB.Key.SHOP_INFO, shop);
                    }
                }
            }
        }).execute();
    }

    /**
     * 得到所有消息
     */
    private void countAllTypeMsg() {
        new CountAllTypeMsgTask(getMyActivity(), new CountAllTypeMsgTask.Callback() {
            @Override
            public void getResult(JSONObject result) {
                if (null == result) {
                    return;
                }
                mMessages = Util.json2Obj(result.toString(), Messages.class);
                int countFirst = (int) Calculate.add(mMessages.getCommunication(), mMessages.getShop());
                int countSecond = (int) Calculate.add(mMessages.getCard(), mMessages.getCoupon());
                int sumCount = (int) Calculate.add(countFirst, countSecond);
                // 对惠圈的建议
                int feedback = (int) mMessages.getFeedback();
                if (feedback > 0) {
                    IvMyMsgpromt.setVisibility(View.VISIBLE);
                } else {
                    IvMyMsgpromt.setVisibility(View.GONE);
                }
                DB.saveInt(ShopConst.Massage.FEED_BACK, 0);
                if (sumCount > 0) {
                    tvMsgPrompt.setVisibility(View.VISIBLE);
                    if (sumCount > 99) {
                        tvMsgPrompt.setText(99 + "+");
                    } else {
                        tvMsgPrompt.setText(sumCount + "");
                    }
                } else {
                    tvMsgPrompt.setVisibility(View.GONE);
                }
            }
        }).execute();
    }

    /**
     * 分店列表
     */
    private void getStaffShopList() {
        new GetStaffShopListTask(getActivity(), new GetStaffShopListTask.Callback() {
            @Override
            public void getResult(JSONObject result) {
                if (null == result) {
                    Util.getContentValidate(R.string.toast_select_fail);
                } else {
                    List<StaffShop> list = (List<StaffShop>) result.get("shopList");
                    if (list.size() > 0) {
                        ly_choose_store.setEnabled(true);
                        mIvChoose.setVisibility(View.VISIBLE);
                    } else {
                        ly_choose_store.setEnabled(false);
                        mIvChoose.setVisibility(View.GONE);
                    }
                }
            }
        }).execute("0");
    }

    /**
     * 获得今日总浏览量
     */
    public void getUserAccess() {
        new GetShopAllBrowseQuantityTask(getMyActivity(), new GetShopAllBrowseQuantityTask.Callback() {
            @Override
            public void getResult(String result) {
                if (!Util.isEmpty(result)) {
                    mTvUserAccessCount.setText(result);
                    DB.saveStr(ShopConst.Access.USER_ACCESS_COUNT, result);
                    RelativeLayout ryAccessCount = (RelativeLayout) mView.findViewById(R.id.ry_access_count);
                    ryAccessCount.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getMyActivity(), UserAccessActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        }).execute();
    }

    /**
     * 点击会员卡跳转到会员卡界面
     *
     * @param view
     */
    private OnClickListener ivSkipClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.rbtn_homecard:// 收款
                    startActivity(new Intent(getMyActivity(), ScanInputAmountActivity.class));
                    break;
                case R.id.rbtn_homecoupon:// 验券
                    startActivity(new Intent(getActivity(), SecurityCodeActivity.class));
                    break;
                case R.id.rbtn_marketact:// 活动
                    intent = new Intent(getMyActivity(), CampaignListActivity.class);
                    startActivity(intent);
                    break;
                case R.id.rbtn_audit:// 惠付券账本
                    intent = new Intent(getMyActivity(), BillHomeActivity.class);
                    startActivity(intent);
                    break;
                case R.id.rbtn_cashier:// 收银台
                    intent = new Intent(getMyActivity(), CashierActivity.class);
                    getMyActivity().startActivity(intent);
                    break;
                case R.id.rbtn_msg:// 我的消息
                    tvMsgPrompt.setVisibility(View.GONE);
                    intent = new Intent(getMyActivity(), MassageActivity.class);
                    intent.putExtra(MassageActivity.MESSAGE_OBJ, mMessages);
                    startActivityForResult(intent, REQUEST_CODE);
                    break;
                case R.id.rbtn_order:// 订单
                    intent = new Intent(getMyActivity(), MyOrderManagerActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ly_home_settled:// 入驻
                    intent = new Intent(getMyActivity(), SettledActivity.class);
                    startActivity(intent);
                    break;
                case R.id.iv_scan://右上角扫描
                    if (mSettledflag) {
                        intent = new Intent(getMyActivity(), ScanActivity.class);
                        intent.putExtra(ScanActivity.FLAG, FLAG);
                        startActivity(intent);
                    } else {
                        mApplication.getDateInfo(getActivity());
                    }
                    break;
                case R.id.ly_choose_store:// 选择店铺
                    intent = new Intent(getMyActivity(), ChooseStoreActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                mMessages = (Messages) data.getSerializableExtra(MESSAGE_OBj);
                break;
            default:
                break;
        }
    }
}
