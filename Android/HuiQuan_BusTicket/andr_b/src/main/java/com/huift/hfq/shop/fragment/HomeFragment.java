// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huift.hfq.base.Const;
import com.huift.hfq.base.SzApplication;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.SetPayResultTask;
import com.huift.hfq.base.pojo.EConsuming;
import com.huift.hfq.base.pojo.ICBCResult;
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
import com.huift.hfq.shop.activity.HomeActivity;
import com.huift.hfq.shop.activity.MassageActivity;
import com.huift.hfq.shop.activity.MyOrderManagerActivity;
import com.huift.hfq.shop.activity.ScanActivity;
import com.huift.hfq.shop.activity.ScanInputAmountActivity;
import com.huift.hfq.shop.activity.SecurityCodeActivity;
import com.huift.hfq.shop.activity.SettledActivity;
import com.huift.hfq.shop.activity.UserAccessActivity;
import com.huift.hfq.shop.adapter.HomeListAdapter;
import com.huift.hfq.shop.model.CancelOrderTask;
import com.huift.hfq.shop.model.CountAllTypeMsgTask;
import com.huift.hfq.shop.model.GetConsumeInfoTask;
import com.huift.hfq.shop.model.GetShopAllBrowseQuantityTask;
import com.huift.hfq.shop.model.GetStaffShopListTask;
import com.huift.hfq.shop.model.SgetShopBasicInfoTask;
import com.huift.hfq.shop.receiver.MyReceiver;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.minidev.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * HomeFragment 主页显示页面
 *
 * @author yanfang.li
 */
public class HomeFragment extends BaseFragment {

    /**
     * Log打印的文件名
     */
    public final static String SHOP_STAFF = "shopStaff";
    public final static String MPOS_SUCC = "交易成功";
    public final static String MPOS_FAIL = "取消交易";
    private static final String FISRT_RUN = "1";
    /**
     * intent跳转的时的请求参数
     */
    private static final int REQUEST_CODE = 100;
    /**
     * 返回成功
     */
    public static final int REQUEST_SUCC = 101;
    /**
     * intent跳转到选择店铺页面
     */
    private static final int CHOOSE_STORE = 201;
    /**
     * intent跳转到选择店铺页面成功
     */
    public static final int CHOOSE_STORE_SUCCESS = 102;
    /**
     * 消息返回的对象
     */
    public static final String MESSAGE_OBj = "messageHomeObj";
    /**
     * ViewPager列表
     */
    private ListView mLvHomeCard;
    /**
     * 没有数据
     */
    private LinearLayout mLyNoDate;
    /**
     * 查询数据的页码
     */
    private int mPage = 0;
    /**
     * 传递数据
     */
    static Bundle sArgs = new Bundle();
    /**
     * 视图
     */
    private View mView;
    /**
     * 适配器的数据源
     **/
    private List<EConsuming> homeCardDatas;
    private HomeListAdapter adapter = null;
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
     * 首页消息
     */
    private TextView mTvShopMsg;
    /***/
    private FrameLayout mFyShopMsg;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                mSettledflag = mApplication.getSettledflag();
            }
        }

        ;
    };
    /**
     * 用户总浏览量
     */
    private TextView mTvUserAccessCount;

    /**
     * 需要传递参数时有利于解耦
     *
     * @return CardHomeFragment
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(sArgs);
        return fragment;
    }

    @Override
    public void viewVisible() {
        super.viewVisible();
        viewJpush();
        DB.saveStr(ShopConst.Key.HOME, getClass().getSimpleName());
    }

    /**
     * JPush的信息
     */
    public void viewJpush() {
        if (adapter != null) {
            adapter.setItems(mApplication.getShareData());
        }
        if (null == mApplication.getShareData() || mApplication.getShareData().size() <= 0) {
            mLyNoDate.setVisibility(View.VISIBLE);
        } else {
            mLyNoDate.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sArgs = getArguments();
        if (mView != null) {
            return mView;
        }
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        ViewUtils.inject(this, mView);
        // 保存互动的activity
        SzApplication.setCurrActivity(getMyActivity());
        // 获取Jpush推送的内容
        ShopApplication app = (ShopApplication) getMyActivity().getApplicationContext();
        app.setHomeFragment(this);
        // 第一次进来 判断首页是否要显示进度条
        DB.saveBoolean(ShopConst.Key.IS＿FIRST_RUN, false);

        Util.addHomeActivity(getMyActivity());
        // 保存首页的对象
        DB.saveStr(ShopConst.Key.HOME_MAIN, getClass().getSimpleName());
        DB.saveStr(ShopConst.Key.HOME, getClass().getSimpleName());
        init(mView);// 初始化数据
        return mView;
    }

    public void setMessage(String message) {
        if (!Util.isEmpty(message)) {
            mTvShopMsg.setVisibility(View.VISIBLE);
            // 设置消息获得焦点并只滚动一次
            mTvShopMsg.requestFocus();
            mTvShopMsg.setMarqueeRepeatLimit(1);
            mTvShopMsg.setText(message);
        }
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
        }).execute(String.valueOf(mPage));
    }

    /**
     * 消费订单
     */
    public void mPosPay(String mPoyCsm) {

        if (!Util.isEmpty(mPoyCsm)) {
            ICBCResult icbcResult = new ICBCResult(mPoyCsm);
            String consumeCode = DB.getStr(Const.MPOS_CSMCODE);
            if (null != icbcResult.getReturnCode()) {
                if (ICBCResult.CODE_SUCCESS.equals(icbcResult.getReturnCode())) {
                    new SetPayResultTask(getMyActivity()).execute(consumeCode, icbcResult.getOrderNo(), "SUCCESS");
                } else if (ICBCResult.CODE_01.equals(icbcResult.getReturnCode())) {
                    new CancelOrderTask(getMyActivity()).execute(consumeCode);
                } else if (ICBCResult.CODE_02.equals(icbcResult.getReturnCode())
                        || ICBCResult.CODE_03.equals(icbcResult.getReturnCode())
                        || ICBCResult.CODE_04.equals(icbcResult.getReturnCode())) {
                    new SetPayResultTask(getMyActivity()).execute(consumeCode, icbcResult.getOrderNo(), "FAIL");
                }
            } else {
                new SetPayResultTask(getMyActivity()).execute(consumeCode, icbcResult.getOrderNo(), "FAIL");
            }
        }
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
                // sumCount = 100; // 测试数据 TODO
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
     * 获得今日总浏览量
     */
    public void getUserAccess(final View view) {
        new GetShopAllBrowseQuantityTask(getMyActivity(), new GetShopAllBrowseQuantityTask.Callback() {
            @Override
            public void getResult(String result) {
                if (!Util.isEmpty(result)) {
                    mTvUserAccessCount.setText(result);
                    DB.saveStr(ShopConst.Access.USER_ACCESS_COUNT, result);
                    RelativeLayout ryAccessCount = (RelativeLayout) view.findViewById(R.id.ry_access_count);
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
     * 初始化控件
     *
     * @param v 视图
     */
    private void init(View v) {
        DB.saveStr(CouponHomeFragment.COUPON_FISRT, FISRT_RUN); // 出事化进入优惠券界面的次数
        DB.saveStr(CardHomeFragment.CARD_FISRT, FISRT_RUN); // 出事化进入会员卡界面的次数
        mApplication = (ShopApplication) getMyActivity().getApplicationContext();
        View headerView = View.inflate(getMyActivity(), R.layout.top_home, null);
        IvMyMsgpromt = (ImageView) getMyActivity().findViewById(R.id.my_msgpromt);
        mLvHomeCard = (ListView) v.findViewById(R.id.lv_home);
        ImageView tvScan = (ImageView) v.findViewById(R.id.tv_msg);
        mIvChoose = (ImageView) v.findViewById(R.id.iv_choose);
        mTvShopName = (TextView) v.findViewById(R.id.tv_mid_content);
        ly_choose_store = v.findViewById(R.id.ly_choose_store);
        mLvHomeCard.addHeaderView(headerView);
        tvMsgPrompt = (TextView) headerView.findViewById(R.id.tv_allprompt);
        mLyNoDate = (LinearLayout) headerView.findViewById(R.id.ly_nodate);
        mTvUserAccessCount = (TextView) headerView.findViewById(R.id.tv_access_count);
        tvMsgPrompt.setVisibility(View.GONE);
        mIvSettled = (LinearLayout) getActivity().findViewById(R.id.ly_home_settled);// 入驻
        mTvShopMsg = (TextView) headerView.findViewById(R.id.tv_shop_msg);
        mFyShopMsg = (FrameLayout) headerView.findViewById(R.id.fy_shop_msg);
        ImageView ivDelMsg = (ImageView) headerView.findViewById(R.id.iv_del_prompt);
        ivDelMsg.setOnClickListener(delMsgListener);
        if (adapter != null) {
            adapter.setItems(mApplication.getShareData());
        } else {
            for (int i = 0; i < mApplication.getShareData().size(); i++) {
                EConsuming consuming = mApplication.getShareData().get(i);
            }
            if (mApplication.getShareData().size() > 0) {
                adapter = new HomeListAdapter(getMyActivity(), mApplication.getShareData());
                mLvHomeCard.setAdapter(adapter);
            }
        }

        if (null == mApplication.getShareData() || mApplication.getShareData().size() <= 0) {
            mLyNoDate.setVisibility(View.VISIBLE);
        } else {
            mLyNoDate.setVisibility(View.GONE);
        }

        // TODO商家名称
        ImageView ivCard = (ImageView) headerView.findViewById(R.id.rbtn_homecard);
        ImageView ivCoupon = (ImageView) headerView.findViewById(R.id.rbtn_homecoupon);
        ImageView ivMarketAct = (ImageView) headerView.findViewById(R.id.rbtn_marketact);
        ImageView ivPost = (ImageView) headerView.findViewById(R.id.rbtn_audit);
        ImageView ivCashier = (ImageView) headerView.findViewById(R.id.rbtn_cashier);
        ImageView ivMassage = (ImageView) headerView.findViewById(R.id.rbtn_msg);
        ImageView ivBtn = (ImageView) headerView.findViewById(R.id.rbtn_msg1);
        // 是否入驻
        getSettled();

        ivCard.setOnClickListener(ivSkipClick);
        ivCoupon.setOnClickListener(ivSkipClick);
        ivMarketAct.setOnClickListener(ivSkipClick);
        ivPost.setOnClickListener(ivSkipClick);
        ivCashier.setOnClickListener(ivSkipClick);
        ivMassage.setOnClickListener(ivSkipClick);
        tvScan.setOnClickListener(ivSkipClick);
        ivBtn.setOnClickListener(ivSkipClick);
        mIvSettled.setOnClickListener(ivSkipClick);

        homeCardDatas = new ArrayList<EConsuming>();
        if (adapter == null) {
            adapter = new HomeListAdapter(getMyActivity(), null);
            mLvHomeCard.setAdapter(adapter);
        }

        myShopInfo();
        countAllTypeMsg();
        // 查询分店列表
        getStaffShopList();
        // 获得用户总浏览量
        getUserAccess(headerView);
    }

    /**
     * 删除消息
     */
    OnClickListener delMsgListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mFyShopMsg.setVisibility(View.GONE);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        DB.saveStr(ShopConst.Key.IS_HOME_MAIN, "");
    }

    @Override
    public void onPause() {
        super.onPause();
        DB.saveStr(ShopConst.Key.IS_HOME_MAIN, ShopConst.Key.IS_HOME_MAIN);
    }

    @Override
    public void onResume() {
        super.onResume();
        String homeClass = DB.getStr(ShopConst.Key.HOME_MAIN); // 获取首页的类
        String runClass = DB.getStr(ShopConst.Key.HOME); // 获取运行的类
        if (homeClass.equals(runClass)) {
            MyReceiver.resetNotiNum();
        }
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
     * 点击会员卡跳转到会员卡界面
     *
     * @param view
     */
    private OnClickListener ivSkipClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            HomeActivity home = (HomeActivity) getMyActivity();
            if (home == null) {
                return;
            }
            switch (v.getId()) {
                case R.id.rbtn_homecard:
                    startActivity(new Intent(getMyActivity(), ScanInputAmountActivity.class));
                    break;
                case R.id.rbtn_homecoupon: {
                    startActivity(new Intent(getActivity(), SecurityCodeActivity.class));
                    break;
                }
                case R.id.rbtn_marketact:// 活动
                    intent = new Intent(getMyActivity(), CampaignListActivity.class);
                    startActivity(intent);
                    break;
                case R.id.rbtn_audit: // 刷卡对账
                    intent = new Intent(getMyActivity(), BillHomeActivity.class);
                    startActivity(intent);
                    break;
                case R.id.rbtn_cashier:
                    intent = new Intent(getMyActivity(), CashierActivity.class);
                    getMyActivity().startActivity(intent);
                    break;
                case R.id.rbtn_msg:
                    tvMsgPrompt.setVisibility(View.GONE);
                    intent = new Intent(getMyActivity(), MassageActivity.class);
                    intent.putExtra(MassageActivity.MESSAGE_OBJ, mMessages);
                    startActivityForResult(intent, REQUEST_CODE);
                    break;
                case R.id.tv_msg:
                    if (mSettledflag) {
                        intent = new Intent(getMyActivity(), ScanActivity.class);
                        intent.putExtra(ScanActivity.INTENG_FLAG, String.valueOf(Util.NUM_ONE));
                        intent.putExtra(ScanActivity.FLAG, String.valueOf(Util.NUM_THIRD));
                        startActivity(intent);
                    } else {
                        mApplication.getDateInfo(getActivity());
                    }
                    break;
                case R.id.rbtn_msg1:// 订单
                    intent = new Intent(getMyActivity(), MyOrderManagerActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ly_home_settled:// 入驻
                    intent = new Intent(getMyActivity(), SettledActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

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
                mHandler.sendEmptyMessage(0);
            }
        }).execute();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == REQUEST_SUCC) {
                    mMessages = (Messages) data.getSerializableExtra(MESSAGE_OBj);
                }
                break;
            default:
                break;
        }
    }

    public void getConsumeInfo(String consumeCode) {
        new GetConsumeInfoTask(getMyActivity(), new GetConsumeInfoTask.Callback() {
            @Override
            public void getResult(JSONObject result) {
                if (result == null) {
                    return;
                } else {
                    EConsuming consuming = Util.json2Obj(result.toString(), EConsuming.class);
                    homeCardDatas.add(consuming);
                }
                if (adapter == null) {
                    adapter = new HomeListAdapter(getMyActivity(), homeCardDatas);
                    mLvHomeCard.setAdapter(adapter);
                } else {
                    adapter.setItems(homeCardDatas);
                }
            }
        }).execute(consumeCode);
    }

    /**
     * 点击事件
     */
    @OnClick({R.id.ly_choose_store})
    private void ivTurnTo(View v) {
        switch (v.getId()) {
            case R.id.ly_choose_store:
                Intent intent = new Intent(getActivity(), ChooseStoreActivity.class);
                startActivityForResult(intent, CHOOSE_STORE);
                break;
        }
    }
}
