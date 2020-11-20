// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.21
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.cust.fragment;

import java.io.Serializable;
import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
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
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.Messages;
import cn.suanzi.baomi.base.pojo.PageData;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.ViewSolveUtils;
import cn.suanzi.baomi.base.view.XListView;
import cn.suanzi.baomi.base.view.XListView.IXListViewListener;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.activity.VipChatActivity;
import cn.suanzi.baomi.cust.adapter.MsgListAdapter;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.model.GetMsgGroupTask;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 与会员交流信息
 * 
 * @author yanfang.li
 */
public class MsgVipFragment extends Fragment implements IXListViewListener {

	private final static String TAG = MsgVipFragment.class.getSimpleName();
	/** 会员卡消息列表 */
	private XListView mLvCardVip;
	private Handler mHandler;
	private int mPage = 1;
	/** 没有数据加载的布局 */
	private LinearLayout mLyView;
	/** 没有数据加载的图片 */
	private ImageView mIvView;
	/** 正在加载的进度条 */
	private ProgressBar mProgView;
	/** 判断是本次API有没有访问 */
	private boolean mFlagData = false;
	/** 下拉*/
	private boolean mDataUpFlag = false;
	/** 第一次更新*/
	private boolean mFirstFlag = true;
	/** 判断有没有消息更新 */
	private boolean mFlagMsgUpp = false;
	/** 会员聊天的数目 */
	private static int sVipCount;
	/** 适配器*/
	private MsgListAdapter mMsgListAdapter;
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
		Util.addLoginActivity(getMyActivity());
		mLvCardVip = (XListView) v.findViewById(R.id.lv_msg);
		mLvCardVip.setPullLoadEnable(true);
		mLvCardVip.setXListViewListener(this);
		mHandler = new Handler();
		mLyView = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) v.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) v.findViewById(R.id.prog_nodata);
		mFlagData = true;
		mDataUpFlag = true;
		mFlagMsgUpp = true;
		sVipCount = 0;
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
	 * 清零
	 * 
	 * @return
	 */
	public static int setMsgCleared() {
		return sVipCount;
	}

	/**
	 * 查询优惠券的消息
	 */
	private void listMsg() {
		if (mFirstFlag) {
			ViewSolveUtils.setNoData(mLvCardVip, mLyView, mIvView, mProgView, CustConst.DATA.LOADIMG);
		}
		new GetMsgGroupTask(getMyActivity(), new GetMsgGroupTask.Callback() {

			@Override
			public void getResult(JSONObject result) {
				mFlagData = true;
				mDataUpFlag = true;
				mFirstFlag = false;
				mLvCardVip.stopLoadMore(); // 上拉加载完成
				mSwipeRefreshLayout.setRefreshing(false); // 停止下拉
				if (result == null) {
					mLvCardVip.setPullLoadEnable(false);
					// 处理没有数据
					ViewSolveUtils.morePageOne(mLvCardVip, mLyView, mIvView, mProgView, mPage);
				} else {
					ViewSolveUtils.setNoData(mLvCardVip, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA);
					mLvCardVip.setPullLoadEnable(true);
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
						ViewSolveUtils.setNoData(mLvCardVip, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA);
						if (mMsgListAdapter == null) {
							mMsgListAdapter = new MsgListAdapter(getMyActivity(), list, CustConst.Massage.MSG_VIP);
							mLvCardVip.setAdapter(mMsgListAdapter);
						} else {
							if (page.getPage() == 1) {
								mMsgListAdapter.setItems(list);
							} else {
								mMsgListAdapter.addItems(list);
							}
						}
					}
				}
				
				// listView列表的点击事件
				mLvCardVip.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int position,
						long longId) {
					Messages message = (Messages) mLvCardVip.getItemAtPosition(position);
					Intent intent = new Intent(getMyActivity(), VipChatActivity.class);
					intent.putExtra(VipChatFragment.MSG_OBJ, (Serializable)message);
					startActivity(intent);
				}
				});
			}
		}).execute(mPage + "");

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
		if (mFlagData) {
			mFlagData = false;
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
		MobclickAgent.onPageEnd(MsgVipFragment.class.getSimpleName());
	}

	@Override
	public void onResume() {
		super.onResume();
		mFlagMsgUpp = DB.getBoolean(CustConst.Key.MSG_SEND);
		if (mFlagMsgUpp) {
			mFlagMsgUpp = false;
			DB.saveBoolean(CustConst.Key.MSG_SEND, false);
			// 消息列表
			listMsg();
		}
		MobclickAgent.onPageStart(MsgVipFragment.class.getSimpleName()); // 统计页面
	}
}
