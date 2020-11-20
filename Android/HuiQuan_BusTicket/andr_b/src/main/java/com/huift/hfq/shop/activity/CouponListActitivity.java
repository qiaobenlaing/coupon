// ---------------------------------------------------------
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.BatchCoupon;
import com.huift.hfq.base.pojo.PageData;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.base.view.XListView;
import com.huift.hfq.base.view.XListView.IXListViewListener;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopApplication;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.adapter.CouponListAdapter;
import com.huift.hfq.shop.fragment.CouponHomeFragment;
import com.huift.hfq.shop.model.ListShopCouponTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.minidev.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 优惠券列表
 *
 * @author yanfang.li
 */
public class CouponListActitivity extends Activity implements IXListViewListener {

    public final static String COUPON_TYPE = "couponType";
    public final static String COUPON_STATUS = "changeCouponStatus";
    public final static String COUPON_CODE = "couponCode";
    public static final int COUPONS_FLAG = 100;
    public static final int COUPONS_SUCCESS = 101;
    public final static int STOP = 0;
    public final static int ENABLE = 1;
    public final static String STOP_STR = "停发";
    public final static String ENABLE_STR = "启用";

    /**
     * 优惠券列表
     **/
    private XListView mLvCouponList;
    /**
     * 按时间的条件查询参数
     **/
    private int mTime = 1;
    /**
     * 页码
     **/
    private int mPage = 1;
    /**
     * listview的适配器
     */
    private CouponListAdapter mCouponAdapter;
    private Handler mHandler;
    /**
     * 上拉加载
     */
    private boolean flagData = false;
    /**
     * 是否添加优惠券
     */
    private boolean mCouponAdd = false;
    private List<BatchCoupon> mBatchLists;
    private String mCouponCode;
    /**
     * 没有数据加载的布局
     */
    private LinearLayout mLyView;
    /**
     * 没有数据加载的图片
     */
    private ImageView mIvView;
    /**
     * 正在加载的进度条
     */
    private ProgressBar mProgView;
    /**
     * 定义全局变量
     */
    private ShopApplication mShopApplication;
    /**
     * 得到是否入驻的标示
     */
    private boolean mSettledflag;

    @Override
    public void onResume() {
        super.onResume();
        mCouponAdd = DB.getBoolean(ShopConst.Key.COUPON_ADD);
        if (mCouponAdd) {
            listShopCoupon();
        }
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_couponlist);
        ViewUtils.inject(this);
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
        // 调用出事化方法
        init();
    }

    /**
     * 初始化方法
     */
    private void init() {
        mShopApplication = (ShopApplication) getApplication();
        mSettledflag = mShopApplication.getSettledflag();
        mCouponCode = getIntent().getExtras().getString(COUPON_CODE);
        mBatchLists = new ArrayList<BatchCoupon>();
        if (mBatchLists.size() > 0) {
            mBatchLists.clear();
        }
        flagData = true;
        LinearLayout ivReturn = (LinearLayout) findViewById(R.id.layout_turn_in);
        ivReturn.setVisibility(View.VISIBLE);
        TextView ivListQuery = (TextView) findViewById(R.id.tv_msg);
        ivListQuery.setBackgroundResource(R.drawable.accntlist_add);
        TextView tvTitle = (TextView) findViewById(R.id.tv_mid_content);
        mLyView = (LinearLayout) findViewById(R.id.ly_nodate);
        mIvView = (ImageView) findViewById(R.id.iv_nodata);
        mProgView = (ProgressBar) findViewById(R.id.prog_nodata);
        tvTitle.setText(getResources().getString(R.string.add_coupon));
        mLvCouponList = (XListView) findViewById(R.id.lv_coupon_list);
        mLvCouponList.setPullLoadEnable(true);
        mLvCouponList.setXListViewListener(this);
        mHandler = new Handler();
        // mLvCouponList.setOnItemClickListener(listener);
        // 调用查询优惠券列表方法
        listShopCoupon();
    }

    /**
     * 获得优惠券
     */
    private void listShopCoupon() {
        if (mPage <= 1) {
            // 正在加载
            ViewSolveUtils.setNoData(mLvCouponList, mLyView, mIvView, mProgView, ShopConst.DATA.LOADIMG);
        }
        String[] params = {mTime + "", mPage + ""};
        new ListShopCouponTask(this, new ListShopCouponTask.Callback() {

            @Override
            public void getResult(JSONObject result) {
                flagData = true;
                if (result == null) {
                    mLvCouponList.setPullLoadEnable(false);
                    ViewSolveUtils.morePageOne(mLvCouponList, mLyView, mIvView, mProgView, mPage);
                } else {
                    ViewSolveUtils.setNoData(mLvCouponList, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA);
                    mLvCouponList.setPullLoadEnable(true);
                    PageData page = new PageData(result, "couponList", new TypeToken<List<BatchCoupon>>() {
                    }.getType());
                    mPage = page.getPage();
                    if (page.hasNextPage() == false) {
                        if (page.getPage() > 1) {
                            Util.getContentValidate(R.string.toast_moredata);
                        }
                        mLvCouponList.setPullLoadEnable(false);
                    } else {
                        mLvCouponList.setPullLoadEnable(true);
                    }
                    for (int i = 0; i < page.getList().size(); i++) {
                        mBatchLists.add((BatchCoupon) page.getList().get(i));
                    }
                    List<BatchCoupon> list = (List<BatchCoupon>) page.getList();
                    if (null == list || list.size() <= 0) {
                        ViewSolveUtils.morePageOne(mLvCouponList, mLyView, mIvView, mProgView, mPage);
                    } else {
                        ViewSolveUtils.setNoData(mLvCouponList, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA);
                        if (mCouponAdapter == null) {
                            mCouponAdapter = new CouponListAdapter(CouponListActitivity.this, list);
                            mCouponAdapter.setOnItemClickLitener(listener);
                            mLvCouponList.setAdapter(mCouponAdapter);
                        } else {
                            if (page.getPage() == 1) {
                                mCouponAdapter.setItems(list);
                            } else {
                                mCouponAdapter.addItems(list);
                            }
                        }
                    }
                }

            }
        }).execute(params);
    }

    /**
     * 返回
     *
     * @param view
     */
    @OnClick(R.id.layout_turn_in)
    private void ivReturnClick(View view) {
        if (Util.isEmpty(mCouponCode)) {
            finish();
        } else {
            finishClick();
        }
    }

    /**
     * 重写点击返回按钮方法，点击一次土司提示，两次退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (Util.isEmpty(mCouponCode)) {
                finish();
            } else {
                finishClick();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 回退
     */
    private void finishClick() {
        Intent intent = new Intent(this, HomeActivity.class);
        ;
        String statuStr = "";
        if (mBatchLists.size() > 0) {
            int status = 0;
            for (int i = 0; i < mBatchLists.size(); i++) {
                BatchCoupon coupon = (BatchCoupon) mBatchLists.get(i);
                if (coupon.getBatchCouponCode().equals(mCouponCode)) {
                    status = coupon.getIsAvailable();
                }
            }

            if (status == STOP) { // 如果等于0 的时候 停发
                statuStr = ENABLE_STR;
            } else {
                statuStr = STOP_STR;
            }
        }
        intent.putExtra(CouponListActitivity.COUPON_STATUS, statuStr);
        intent.putExtra(CouponListActitivity.COUPON_CODE, mCouponCode);
        setResult(CouponHomeFragment.HOME_COUPON_SUCC, intent);
        finish();
    }

    /**
     * listview每列的点击事件
     */
    private OnItemClickListener listener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            BatchCoupon cp = (BatchCoupon) mLvCouponList.getItemAtPosition(position);
            Intent intent = new Intent(CouponListActitivity.this, CouponDetailActivity.class);
            intent.putExtra(CouponDetailActivity.COUPON_CODE, cp.getBatchCouponCode());
            intent.putExtra(CouponDetailActivity.COUPON_SOURCE, CouponDetailActivity.LIST_COUPON);
            startActivityForResult(intent, CouponListActitivity.COUPONS_FLAG);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case COUPONS_FLAG:
                if (resultCode == COUPONS_SUCCESS) {
                    String couponStatus = data.getExtras().getString(COUPON_STATUS); // 如果是启动就是停发
                    String couponCode = data.getExtras().getString(COUPON_CODE);
                    int status = 0;
                    if (couponStatus.equals(STOP_STR)) { // 如果是停发就是启发状态
                        status = ENABLE;
                    } else {
                        status = STOP;
                    }
                    for (int i = 0; i < mBatchLists.size(); i++) {
                        BatchCoupon coupon = (BatchCoupon) mBatchLists.get(i);
                        if (coupon.getBatchCouponCode().equals(couponCode)) {
                            coupon.setIsAvailable(status);
                        }
                    }
                    mCouponAdapter.setItems(mBatchLists);
                }
                break;
        }
    }

    /**
     * 添加优惠券
     *
     * @param view
     */
    @OnClick(R.id.tv_msg)
    private void couponAddClick(View view) {
        if (mSettledflag) {
            Intent intent = new Intent(this, CouponSettingActitivity.class);
            startActivity(intent);
        } else {
            mShopApplication.getDateInfo(this);
        }
    }

    @Override
    public void onLoadMore() {
        if (flagData) {
            flagData = false;
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    mPage++;
                    listShopCoupon();
                    mLvCouponList.stopLoadMore();
                }
            }, 2000);
        }

    }

}
