// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.cust.fragment;

import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Messages;
import com.huift.hfq.base.pojo.PageData;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.base.view.XListView;
import com.huift.hfq.base.view.XListView.IXListViewListener;
import com.huift.hfq.cust.R;

import com.google.gson.reflect.TypeToken;
import com.huift.hfq.cust.activity.MessageActivity;
import com.huift.hfq.cust.adapter.MsgListAdapter;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.model.MessageListTask;
import com.huift.hfq.cust.model.ReadMessageTask;
import com.lidroid.xutils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 会员卡消息显示
 * 
 * @author yanfang.li
 */
public class MsgCardFragment extends Fragment implements IXListViewListener {

	private final static String TAG = MsgCardFragment.class.getSimpleName();
	/** 会员卡消息列表 */
	private XListView mLvCard;
	/** 线程*/
	private Handler mHandler;
	/** 页码*/
	private int mPage = 1;
	/** 没有数据加载*/
	private LinearLayout mLyView;
	/** 没有数据加载的图片*/
	private ImageView mIvView ;
	/** 正在加载的进度条*/
	private ProgressBar mProgView;
	/** 列表的适配器的数据源 */
	private MsgListAdapter mMsgListAdapter;
	/** 会员卡未读数目 */
	private static int sCardMsgCount;
	/** 消息对象 */
	private Messages mMessages;
	/** 上拉请求api*/
	private boolean mDataUpFlag;
	/** 下拉请求api*/
	private boolean mDataFlag;
	/** 下拉加载 */
	private SwipeRefreshLayout mSwipeRefreshLayout;
	
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
		mDataFlag = true;
		mDataUpFlag = true;
		Util.addLoginActivity(getMyActivity());
		mLyView = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) v.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) v.findViewById(R.id.prog_nodata);
		mLvCard = (XListView) v.findViewById(R.id.lv_msg);
		mLvCard.setPullLoadEnable(true);
		mLvCard.setXListViewListener(this);
		mHandler = new Handler();
		mMessages = (Messages) getMyActivity().getIntent().getSerializableExtra(MessageActivity.MESSAGE_OBJ);
		if (null != mMessages) {
			sCardMsgCount = mMessages.getCoupon();
		}
		listMsg();
		mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.index_swipe_refresh);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.red);
		mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
		return v;
	}
	
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	/**
	 * 查询优惠券的消息
	 */
	private void listMsg() {
		if (mDataUpFlag && mPage <= 1) {
			ViewSolveUtils.setNoData(mLvCard, mLyView, mIvView, mProgView, CustConst.DATA.LOADIMG);
		}
		new MessageListTask(getMyActivity(), new MessageListTask.Callback() {

			@Override
			public void getResult(JSONObject result) {
				mDataFlag = true;
				mDataUpFlag = true;
				mLvCard.stopLoadMore(); // 上拉加载完成
				mSwipeRefreshLayout.setRefreshing(false);
				if (result == null) {
					// 处理没有数据
					ViewSolveUtils.morePageOne(mLvCard, mLyView, mIvView, mProgView, mPage);
				} else {
					readMessage(); //阅读消息
					ViewSolveUtils.setNoData(mLvCard, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA);
					mLvCard.setPullLoadEnable(true);
					PageData page = new PageData(result, "messageList", new TypeToken<List<Messages>>() {
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
						ViewSolveUtils.setNoData(mLvCard, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA);
						if (mMsgListAdapter == null) {
							mMsgListAdapter = new MsgListAdapter(getMyActivity(), list, CustConst.Massage.MSG_CARD);
							mLvCard.setAdapter(mMsgListAdapter);
						} else {
							if (page.getPage() == 1) {
								mMsgListAdapter.setItems(list);
							} else {
								mMsgListAdapter.addItems(list);
							}
						}
					}

				}
			}
		}).execute(CustConst.Massage.MSG_CARD + "", mPage + "");

	}

	/**
	 * 阅读消息
	 */
	private void readMessage() {
		new ReadMessageTask(getMyActivity(), new ReadMessageTask.Callback() {

			@Override
			public void getResult(int result) {
				Log.d(TAG, "异常aaaaaaaaaaa");
				if (result == ErrorCode.SUCC) {
					sCardMsgCount = 0;
					Log.d(TAG, "异常ddddddddddddd");
				}
			}
		}).execute(CustConst.Massage.MSG_CARD + "");
	}

	/**
	 * 设置消息清零
	 * 
	 * @return
	 */
	public static int setMsgCleared() {
		return sCardMsgCount;
	}

	/**
	 * 下拉加载
	 */
	private OnRefreshListener refreshListener = new OnRefreshListener() {

		@Override
		public void onRefresh() {

			if (mDataUpFlag) {
				mDataUpFlag = false;
				mHandler.postDelayed(new Runnable() {
					public void run() {
						mPage = 1;
						listMsg();
					}
				}, 5000);
			}
		}
	};

	@Override
	public void onLoadMore() {
		if (mDataFlag) {
			mDataFlag = false;
			mHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					mPage++;
					listMsg();
				}
			}, 2000);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(MsgCardFragment.class.getSimpleName());
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(MsgCardFragment.class.getSimpleName()); // 统计页面
	}
}
