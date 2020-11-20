// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.reflect.TypeToken;
import com.huift.hfq.base.Const;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Messages;
import com.huift.hfq.base.pojo.PageData;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.base.view.XXListView;
import com.huift.hfq.base.view.XXListView.IXXListViewListener;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.MassageActivity;
import com.huift.hfq.shop.adapter.MsgListAdapter;
import com.huift.hfq.shop.model.ListMsgTask;
import com.huift.hfq.shop.model.ReadMessageTask;
import com.lidroid.xutils.ViewUtils;

import net.minidev.json.JSONObject;

import java.util.List;

/**
 * 会员卡消息显示
 *
 * @author yanfang.li
 */
public class MsgCardFragment extends Fragment implements IXXListViewListener {

	private final static int HAVE_DATA = 1;
	private final static int NO_DATA = 0;

	/** 会员卡消息列表 */
	private XXListView mLvCard;
	private Handler mHandler;
	private LinearLayout lyNodate;
	/** 消息对象 */
	private Messages mMessages;
	private int mPage = 1;
	private MsgListAdapter mAdapter;
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
	private static int sCouponMsgCount = 0;

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
		mLvCard = (XXListView) v.findViewById(R.id.lv_msg);
		mLvCard.setPullLoadEnable(true);
		mLvCard.setPullRefreshEnable(true);
		mLvCard.setXXListViewListener(this,Const.PullRefresh.SHOP_CARD_MSG);
		mHandler = new Handler();
		mMessages = (Messages) getActivity().getIntent().getSerializableExtra(MassageActivity.MESSAGE_OBJ);
		if (null != mMessages) {
			sCouponMsgCount = mMessages.getCard();
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
			ViewSolveUtils.setNoData(mLvCard, mLyView, mIvView, mProgView, ShopConst.DATA.LOADIMG);
		}
		new ListMsgTask(getActivity(), new ListMsgTask.Callback() {

			@Override
			public void getResult(JSONObject result) {
				mFlagData = true;
				mUpFlagData = true;
				mLvCard.stopLoadMore(); // 上拉加载完成
				if (result != null) {
					readMessage(); // 阅读消息
					mLvCard.setPullLoadEnable(true);
					JSONObject cardMsg = (JSONObject) result.get("cardMsg");
					if (cardMsg != null || !"".equals(cardMsg.toJSONString())) {
						ViewSolveUtils.setNoData(mLvCard, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA);
						PageData page = new PageData(cardMsg, "cardMsgList", new TypeToken<List<Messages>>() {
						}.getType());
						mPage = page.getPage();
						if (page.hasNextPage() == false) {
							if (page.getPage() > 1) {
								Util.getContentValidate(R.string.toast_moredata);
							}
							mLvCard.setPullLoadEnable(false);
						} else {
							mLvCard.setPullLoadEnable(true);
						}
						List<Messages> list = (List<Messages>) page.getList();
						if (null == list || list.size() <= 0) {
							ViewSolveUtils.morePageOne(mLvCard, mLyView, mIvView, mProgView, mPage);
						} else {
							ViewSolveUtils.setNoData(mLvCard, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA);
							if (mAdapter == null) {
								mAdapter = new MsgListAdapter(getActivity(), list, ShopConst.Massage.MSG_CARD);
								mLvCard.setAdapter(mAdapter);
							} else {
								if (page.getPage() == 1) {
									mAdapter.setItems(list);
								} else {
									mAdapter.addItems(list);
								}
							}
						}
					} else {
						ViewSolveUtils.morePageOne(mLvCard, mLyView, mIvView, mProgView, mPage);
					}
				} else {
					ViewSolveUtils.morePageOne(mLvCard, mLyView, mIvView, mProgView, mPage);
				}
			}
		}).execute(mPage + "");

	}

	private void readMessage() {
		new ReadMessageTask(getActivity(), new ReadMessageTask.Callback() {

			@Override
			public void getResult(int result) {
				if (result == ErrorCode.SUCC) {
					sCouponMsgCount = 0;
				}
			}
		}).execute(ShopConst.Massage.MSG_CARD + "");
	}

	/**
	 * 设置消息清零
	 *
	 * @return
	 */
	public static int setMsgCleared() {
		return sCouponMsgCount;
	}

	@Override
	public void onRefresh() {
		if (mFlagData) {
			mFlagData = false;
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					mPage = 1;
					listMsg();
					mLvCard.stopRefresh();
				}
			}, 2000);
		}
	}

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
