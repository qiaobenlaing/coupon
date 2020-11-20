// ---------------------------------------------------------
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.BatchCoupon;
import com.huift.hfq.base.pojo.PageData;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.base.view.XListView;
import com.huift.hfq.base.view.XListView.IXListViewListener;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.CouponSettingActitivity;
import com.huift.hfq.shop.adapter.CouponListAdapter;
import com.huift.hfq.shop.model.ListShopCouponTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.minidev.json.JSONObject;

import java.util.List;

/**
 * 优惠券列表
 * 
 * @author yanfang.li
 */
public class CouponListFragment extends Fragment implements IXListViewListener {

	private final static String TAG = CouponListFragment.class.getSimpleName();
	public final static String COUPON_TYPE = "couponType";
	public final static String COUPON_STATUS = "changeCouponStatus";
	public static final int COUPON_FLAG = 100;
	public static final int COUPON_SUCCESS = 101;

	/** 优惠券列表 **/
	private XListView mLvCouponList;
	/** 按时间的条件查询参数 **/
	private int mTime = 1;
	/** 页码 **/
	private int mPage = 1;
	/** listview的适配器 */
	private CouponListAdapter mCouponAdapter;
	private Handler mHandler;
	/** 没有数据加载的布局 */
	private LinearLayout mLyView;
	/** 没有数据加载的图片 */
	private ImageView mIvView;
	/** 正在加载的进度条 */
	private ProgressBar mProgView;
	/** 上拉加载 */
	private boolean flagData = false;
	/** 是否添加优惠券 */
	private boolean mCouponAdd = false;

	/**
	 * 需要传递参数时有利于解耦
	 * 
	 * @return CouponListFragment
	 */
	public static CouponListFragment newInstance() {
		Bundle args = new Bundle();
		// 获取Bundle Arguments
		CouponListFragment fragment = new CouponListFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_couponlist, container, false);
		ViewUtils.inject(this, v);
		// 调用出事化方法
		init(v);
		Util.addLoginActivity(getActivity());
		return v;
	}

	/**
	 * 初始化方法
	 * 
	 * @param v
	 *            视图
	 */
	private void init(View v) {
		flagData = true;
		LinearLayout ivReturn = (LinearLayout) v.findViewById(R.id.layout_turn_in);
		ivReturn.setVisibility(View.VISIBLE);
		TextView ivListQuery = (TextView) v.findViewById(R.id.tv_msg);
		ivListQuery.setBackgroundResource(R.drawable.accntlist_add);
		TextView tvTitle = (TextView) v.findViewById(R.id.tv_mid_content);
		tvTitle.setText(getResources().getString(R.string.add_coupon));
		mLyView = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) v.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) v.findViewById(R.id.prog_nodata);
		mLvCouponList = (XListView) v.findViewById(R.id.lv_coupon_list);
		mLvCouponList.setPullLoadEnable(true);
		mLvCouponList.setXListViewListener(this);
		mHandler = new Handler();
		// mLvCouponList.setOnItemClickListener(listener);
		// 调用查询优惠券列表方法
		listShopCoupon();
	}

	@Override
	public void onResume() {
		super.onResume();
		mCouponAdd = DB.getBoolean(ShopConst.Key.COUPON_ADD);
		if (mCouponAdd) {
			listShopCoupon();
		}
	}

	/**
	 * 获得优惠券
	 */
	private void listShopCoupon() {
		if (mPage <= 1) { 
			Log.d(TAG, "listShopCoupon=");
			// 正在加载
			ViewSolveUtils.setNoData(mLvCouponList, mLyView, mIvView, mProgView, ShopConst.DATA.LOADIMG);
		}
		String[] params = { mTime + "", mPage + "" };
		new ListShopCouponTask(getActivity(), new ListShopCouponTask.Callback() {

			@Override
			public void getResult(JSONObject result) {
				mLvCouponList.stopLoadMore();
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
					List<BatchCoupon> list = (List<BatchCoupon>) page.getList();
					if (null == list || list.size() <= 0) {
						ViewSolveUtils.morePageOne(mLvCouponList, mLyView, mIvView, mProgView, mPage);
					} else {
						ViewSolveUtils.setNoData(mLvCouponList, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA);
						if (mCouponAdapter == null) {
							mCouponAdapter = new CouponListAdapter(getActivity(), list);
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
		if (view.getId() == R.id.layout_turn_in) {
			getActivity().finish();
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "requestCode=" + requestCode);
		switch (requestCode) {
		case COUPON_FLAG:
			Log.d(TAG, "resultCode=" + resultCode);
			if (resultCode == COUPON_SUCCESS) {
				String couponStatus = data.getExtras().getString(COUPON_STATUS);
				Log.d(TAG, "couponStatus=" + couponStatus);
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
		Intent intent = new Intent(getActivity(), CouponSettingActitivity.class);
		startActivity(intent);
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
				}
			}, 2000);
		}
	}
}
