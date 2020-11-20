// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.21
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.huift.hfq.cust.util.SkipActivityUtil;
import com.lidroid.xutils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 异业广播
 * @author yanfang.li
 */
public class MsgShopFragment extends Fragment implements IXListViewListener {

	private final static String TAG = MsgShopFragment.class.getSimpleName();
	/** 会员卡消息列表 */
	private XListView mLvShop;
	private Handler mHandler;
	private int mPage = 1;
	/** 没有数据加载的布局*/
	private LinearLayout mLyView;
	/** 没有数据加载的图片*/
	private ImageView mIvView ;
	/** 正在加载的进度条*/
	private ProgressBar mProgView;
	/** 列表的适配器的数据源 */
	private MsgListAdapter mMsgListAdapter;
	/** 消息的条数*/
	private static int sShopCount;
	/** 消息的对象*/
	private Messages mMessages;
	/** 上拉请求api*/
	private boolean mDataUpFlag;
	/** 下拉请求api*/
	private boolean mDataFlag;
	/** 下拉加载 */
	private SwipeRefreshLayout mSwipeRefreshLayout;
	
	public static MsgShopFragment newInstance() {

		Bundle args = new Bundle();
		MsgShopFragment fragment = new MsgShopFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_massage_list, container,
				false);
		ViewUtils.inject(this, v);
		mDataFlag = true;
		mDataUpFlag = true;
		Util.addLoginActivity(getMyActivity());
		mLyView = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) v.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) v.findViewById(R.id.prog_nodata);
		mLvShop = (XListView) v.findViewById(R.id.lv_msg);
		mLvShop.setPullLoadEnable(true);
		mLvShop.setXListViewListener(this);
		mHandler = new Handler();
		mMessages = (Messages)getMyActivity().getIntent().getSerializableExtra(MessageActivity.MESSAGE_OBJ);
		if (null != mMessages) {
			sShopCount = mMessages.getShop();
		}
		listMsg();
		mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.index_swipe_refresh);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.red);
		mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
		return v;
	}
	
	private Activity getMyActivity(){
		Activity act = getActivity();
		if(act==null){
			act=AppUtils.getActivity();
		}
		return act;
	}

	/**
	 * 查询优惠券的消息
	 */
	private void listMsg() {
		if (mDataUpFlag && mPage <= 1) {
			ViewSolveUtils.setNoData(mLvShop, mLyView, mIvView, mProgView,CustConst.DATA.LOADIMG);
		}
		new MessageListTask(getMyActivity(), new MessageListTask.Callback() {

			@Override
			public void getResult(JSONObject result) {
				mDataFlag = true;
				mDataUpFlag = true;
				mLvShop.stopLoadMore(); // 上拉加载完成
				mSwipeRefreshLayout.setRefreshing(false);
				if (result == null) {
					// 处理没有数据
					ViewSolveUtils.morePageOne(mLvShop, mLyView, mIvView, mProgView, mPage);
				} else {
					readMessage(); //阅读消息
					ViewSolveUtils.setNoData(mLvShop, mLyView, mIvView, mProgView,CustConst.DATA.HAVE_DATA);
					mLvShop.setPullLoadEnable(true);
					PageData page = new PageData(result, "messageList", new TypeToken<List<Messages>>() {
					}.getType());
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
						ViewSolveUtils.setNoData(mLvShop, mLyView, mIvView, mProgView,CustConst.DATA.HAVE_DATA);
						if (mMsgListAdapter == null) {
							mMsgListAdapter = new MsgListAdapter(getMyActivity(), list, CustConst.Massage.MSG_CARD);
							mLvShop.setAdapter(mMsgListAdapter);
						} else {
							if (page.getPage() == 1) {
								mMsgListAdapter.setItems(list);
							} else {
								mMsgListAdapter.addItems(list);
							}
						}
					}

					mLvShop.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
							Messages messages = (Messages) mLvShop.getItemAtPosition(position);
							SkipActivityUtil.skipNewShopDetailActivity(getMyActivity(), messages.getShopCode());
							
						}
					});
				}
			
			}
			
		}).execute(CustConst.Massage.READ_SHOP + "", mPage + "");

	}
	
	/**
	 * 阅读消息
	 */
	private void readMessage() {
		new ReadMessageTask(getMyActivity(),new ReadMessageTask.Callback() {
			
			@Override
			public void getResult(int result) {
					if (result == ErrorCode.SUCC) {
						sShopCount = 0;
				}
			}
		}).execute(CustConst.Massage.READ_SHOP+"");
	}
	
	/**
	 * 设置消息清零
	 * @return
	 */
	public static int setMsgCleared() {
		return sShopCount;
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
		MobclickAgent.onPageEnd(MsgShopFragment.class.getSimpleName());
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(MsgShopFragment.class.getSimpleName()); // 统计页面
	}
}
