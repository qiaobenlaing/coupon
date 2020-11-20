// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.21
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.fragment;

import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.huift.hfq.shop.R;

import com.google.gson.reflect.TypeToken;
import com.huift.hfq.base.Const;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Messages;
import com.huift.hfq.base.pojo.PageData;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.base.view.XXListView;
import com.huift.hfq.base.view.XXListView.IXXListViewListener;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.MassageActivity;
import com.huift.hfq.shop.adapter.MsgListAdapter;
import com.huift.hfq.shop.model.ListMsgTask;
import com.huift.hfq.shop.model.ReadMessageTask;
import com.lidroid.xutils.ViewUtils;

/**
 * 异业广播
 * 
 * @author yanfang.li
 */
public class MsgShopFragment extends Fragment implements IXXListViewListener {

	private final static String TAG = MsgShopFragment.class.getSimpleName();
	/** 会员卡消息列表 */
	private XXListView mLvShop;
	private Handler mHandler;
	private Messages mMessages;
	private MsgListAdapter mAdapter;
	private int mPage = 1;
	/** 没有数据加载的布局 */
	private LinearLayout mLyView;
	/** 没有数据加载的图片 */
	private ImageView mIvView;
	/** 正在加载的进度条 */
	private ProgressBar mProgView;
	/** 上拉加载的记录 */
	private boolean mFlagData = false;
	/** 下拉加载的记录 */
	private boolean mUpFlagData = false;
	/** 消息条数 */
	private static int sShopMsgCount = 0;
	/** 列表的适配器的数据源 */
	public static MsgCardFragment newInstance() {

		Bundle args = new Bundle();
		MsgCardFragment fragment = new MsgCardFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_massage_list, container, false);
		ViewUtils.inject(this, v);
		Util.addLoginActivity(getActivity());
		mLyView = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) v.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) v.findViewById(R.id.prog_nodata);
		mLvShop = (XXListView) v.findViewById(R.id.lv_msg);
		mLvShop.setPullLoadEnable(true);
		mLvShop.setPullRefreshEnable(true);
		mLvShop.setXXListViewListener(this, Const.PullRefresh.SHOP_SHOP_MSG);
		mHandler = new Handler();
		mMessages = (Messages) getActivity().getIntent().getSerializableExtra(MassageActivity.MESSAGE_OBJ);
		if (null != mMessages) {
			sShopMsgCount = mMessages.getShop();
		}
		listMsg();
		mFlagData = true;
		mUpFlagData = true;
		return v;
	}

	/**
	 * 查询优惠券的消息
	 */
	private void listMsg() {
		if (mPage <= 1) {
			// 正在加载
			ViewSolveUtils.setNoData(mLvShop, mLyView, mIvView, mProgView, ShopConst.DATA.LOADIMG);
		}
		new ListMsgTask(getActivity(), new ListMsgTask.Callback() {

			@Override
			public void getResult(JSONObject result) {
				mFlagData = true;
				mUpFlagData = true;
				mLvShop.stopLoadMore(); // 上拉加载完成
				if (result != null) {
					readMessage(); // 阅读消息
					mLvShop.setPullLoadEnable(true);
					JSONObject shopMsg = (JSONObject) result.get("shopMsg");
					if (shopMsg != null || !"".equals(shopMsg.toString())) {
						ViewSolveUtils.setNoData(mLvShop, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA);
						PageData page = new PageData(shopMsg, "shopMsgList", new TypeToken<List<Messages>>() {}.getType());
						mPage = page.getPage();
						if (page.hasNextPage() == false) {
							if (page.getPage() > 1) {
								Util.getContentValidate(R.string.toast_moredata);
							}
							mLvShop.setPullLoadEnable(false);
						} else {
							mLvShop.setPullLoadEnable(true);
						}
						List<Messages> list = (List<Messages>) page.getList();
						if (null == list || list.size() <= 0) {
							ViewSolveUtils.morePageOne(mLvShop, mLyView, mIvView, mProgView, mPage);
						} else {
							ViewSolveUtils.setNoData(mLvShop, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA);
							if (mAdapter == null) {
								mAdapter = new MsgListAdapter(getActivity(), list, ShopConst.Massage.MSG_SHOP);
								mLvShop.setAdapter(mAdapter);
							} else {
								if (page.getPage() == 1) {
									mAdapter.setItems(list);
								} else {
									mAdapter.addItems(list);
								}
							}
						}
					} else {
						ViewSolveUtils.morePageOne(mLvShop, mLyView, mIvView, mProgView, mPage);
					}
				} else {
					ViewSolveUtils.morePageOne(mLvShop, mLyView, mIvView, mProgView, mPage);
				}
			}
		}).execute(mPage + "");

	}

	private void readMessage() {
		new ReadMessageTask(getActivity(), new ReadMessageTask.Callback() {
			@Override
			public void getResult(int result) {
				if (result == ErrorCode.SUCC) {
					sShopMsgCount = 0;
				}
			}
		}).execute(ShopConst.Massage.READ_SHOP + "");
	}

	/**
	 * 设置消息清零
	 * 
	 * @return
	 */
	public static int setMsgCleared() {
		return sShopMsgCount;
	}

	/**
	 * 下拉刷新
	 */
	@Override
	public void onRefresh() {
		if (mFlagData) {
			mFlagData = false;
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					listMsg();
					mPage = 1;
					mLvShop.stopRefresh();
				}
			}, 2000);
		}
	}

	/**
	 * 上拉刷新
	 */
	@Override
	public void onLoadMore() {
		if (mUpFlagData) {
			mUpFlagData = false;
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					mPage++;
					listMsg();
				}
			}, 2000);
		}
	}

}
