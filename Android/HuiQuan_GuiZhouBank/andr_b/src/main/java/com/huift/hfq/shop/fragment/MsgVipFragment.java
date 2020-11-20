// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.21
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.reflect.TypeToken;
import com.huift.hfq.base.Const;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.Messages;
import com.huift.hfq.base.pojo.PageData;
import com.huift.hfq.base.pojo.UserCardVip;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.base.view.XXListView;
import com.huift.hfq.base.view.XXListView.IXXListViewListener;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.VipChatActivity;
import com.huift.hfq.shop.adapter.MsgListAdapter;
import com.huift.hfq.shop.model.GetMsgGroupTask;
import com.lidroid.xutils.ViewUtils;

import net.minidev.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * 与会员交流信息
 * 
 * @author yanfang.li
 */
public class MsgVipFragment extends Fragment implements IXXListViewListener {

	/** 会员卡消息列表 */
	private XXListView mLvCardVip;
	private Handler mHandler;
	private int mPage = 1;
	private MsgListAdapter mMsgAdapter;
	/** 没有数据加载的布局 */
	private LinearLayout mLyView;
	/** 没有数据加载的图片 */
	private ImageView mIvView;
	/** 正在加载的进度条 */
	private ProgressBar mProgView;
	/** 判断是本次API有没有访问 */
	private boolean mFlagDate = false;
	/** 判断有没有消息更新 */
	private boolean mFlagMsgUpp = false;

	private static int sVipCount;

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
		mLvCardVip = (XXListView) v.findViewById(R.id.lv_msg);
		mLvCardVip.setPullLoadEnable(true);
		mLvCardVip.setPullRefreshEnable(true);
		mLvCardVip.setXXListViewListener(this, Const.PullRefresh.SHOP_VIP_MSG);
		mLyView = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) v.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) v.findViewById(R.id.prog_nodata);
		mHandler = new Handler();
		mFlagDate = true;
		mFlagMsgUpp = true;
		sVipCount = 0;
		listMsg();
		return v;
	}

	/**
	 * 清零
	 * 
	 * @return
	 */
	public static int setMsgCleared() {
		return sVipCount;
	}

	@Override
	public void onStart() {
		super.onStart();
		// 显示消息数目可以拖动的
		/*
		 * CoverManager.getInstance().setMaxDragDistance(150);
		 * CoverManager.getInstance().setExplosionTime(150);
		 */
		mFlagMsgUpp = DB.getBoolean(ShopConst.Key.MSG_SEND);
		if (mFlagMsgUpp) {
			DB.saveBoolean(ShopConst.Key.MSG_SEND, false);
			// 消息列表
			listMsg();
		}
	}

	/**
	 * 查询优惠券的消息
	 */
	private void listMsg() {
		if (mPage <= 1) {
			// 正在加载
			ViewSolveUtils.setNoData(mLvCardVip, mLyView, mIvView, mProgView, ShopConst.DATA.LOADIMG);
		}
		mLvCardVip.setPullLoadEnable(false);
		new GetMsgGroupTask(getActivity(), new GetMsgGroupTask.Callback() {

			@Override
			public void getResult(JSONObject result) {
				mFlagDate = true;
				mLvCardVip.stopLoadMore(); // 上拉加载完成
				if (result != null) {
					mLvCardVip.setPullLoadEnable(true);
					ViewSolveUtils.setNoData(mLvCardVip, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA);
					PageData page = new PageData(result, "ret", new TypeToken<List<Messages>>() {
					}.getType());
					mPage = page.getPage();
					if (page.hasNextPage() == false) {
						if (page.getPage() > 1) {
							Util.getContentValidate(R.string.toast_moredata);
						}
						mLvCardVip.setPullLoadEnable(false);
					} else {
						mLvCardVip.setPullLoadEnable(true);
					}
					List<Messages> list = (List<Messages>) page.getList();
					if (null == list || list.size() <= 0) {
						ViewSolveUtils.morePageOne(mLvCardVip, mLyView, mIvView, mProgView, mPage);
					} else {
						ViewSolveUtils.setNoData(mLvCardVip, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA);
						if (mMsgAdapter == null) {
							mMsgAdapter = new MsgListAdapter(getActivity(), list, ShopConst.Massage.MSG_VIP);
							mLvCardVip.setAdapter(mMsgAdapter);
						} else {
							if (page.getPage() == 1) {
								mMsgAdapter.setItems(list);
							} else {
								mMsgAdapter.addItems(list);
							}
						}
					}
				} else {
					ViewSolveUtils.morePageOne(mLvCardVip, mLyView, mIvView, mProgView, mPage);
				}

				// listView列表的点击事件
				mLvCardVip.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> adapterView, View view, int position, long longId) {
						Messages message = (Messages) mLvCardVip.getItemAtPosition(position);
						UserCardVip cardVip = new UserCardVip();
						cardVip.setNickName(message.getUserName());
						cardVip.setAvatarUrl(message.getUserAvatar());
						cardVip.setUserCode(message.getUserCode());
						Intent intent = new Intent(getActivity(), VipChatActivity.class);
						intent.putExtra(VipChatFragment.USER_OBJ, (Serializable) cardVip);
						startActivity(intent);
					}
				});
			}
		}).execute(mPage + "");

	}

	// 下拉刷新
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				mPage = 1;
				listMsg();
				mLvCardVip.stopRefresh();
			}
		}, 2000);
	}

	// 上拉加载更多
	@Override
	public void onLoadMore() {
		if (mFlagDate) {
			mFlagDate = false;
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
