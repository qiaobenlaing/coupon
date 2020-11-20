package cn.suanzi.baomi.cust.fragment;

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
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.Activitys;
import cn.suanzi.baomi.base.pojo.PageData;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.ViewSolveUtils;
import cn.suanzi.baomi.base.view.XListView;
import cn.suanzi.baomi.base.view.XListView.IXListViewListener;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.adapter.ActIssueAdapter;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.model.GetActListTask;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 发布的活动
 * @author yanfang.li
 */
public class ActIssueFragment extends Fragment implements IXListViewListener {
	
	private static final String TAG = ActIssueFragment.class.getSimpleName();
	/** 一般商家会员卡详情 **/
	private XListView mLvActIss;
	/** 页码 **/
	private int mPage = 0;
	/** 适配器 */
	private Handler mHandler;
	/** 没有数据加载*/
	private LinearLayout mLyView;
	/** 没有数据加载的图片*/
	private ImageView mIvView ;
	/** 正在加载的进度条*/
	private ProgressBar mProgView;
	/** 上拉请求api*/
	private boolean mDataUpFlag;
	/** 下拉请求api*/
	private boolean mDataFlag;
	/** 第一次运行*/
	private boolean mFirstFlag = true;
	/** 下拉加载 */
	private SwipeRefreshLayout mSwipeRefreshLayout;
	/** 发布的活动*/
	private ActIssueAdapter mActIssueAdapter;
	
	/**
	 * 需要传递参数时有利于解耦
	 * @return MyStaffFragment
	 */
	public static ActIssueFragment newInstance() {
		Bundle args = new Bundle();
		ActIssueFragment fragment = new ActIssueFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_mymcardlist, container, false);
		ViewUtils.inject(this, v);
		Util.addActivity(getMyActivity());
		Util.addLoginActivity(getMyActivity());
		findView(v);
		initData();
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
	 * 获取控件
	 * @param v 视图
	 */
	private void findView(View view) {
		mLvActIss = (XListView) view.findViewById(R.id.lv_common_merchant);
		mLvActIss.setPullLoadEnable(true);
		mLvActIss.setXListViewListener(this);
		
		mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.index_swipe_refresh);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.red);
		mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
		
		// 加载的提示
		mLyView = (LinearLayout) view.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) view.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) view.findViewById(R.id.prog_nodata);
	}
	
	/**
	 * 获取数据
	 * @param view 视图
	 */
	private void initData() {
		mDataFlag = true;
		mDataUpFlag = true;
		mHandler = new Handler();
		getActList(); // 获取活动
	}
	
	/**
	 * 活动列表
	 */
	private void getActList () {
		if (mFirstFlag) {
			ViewSolveUtils.setNoData(mLvActIss, mLyView, mIvView, mProgView, CustConst.DATA.LOADIMG); // 正在加载
		}
		new GetActListTask(getMyActivity(), new GetActListTask.Callback() {
			
			@Override
			public void getResult(JSONObject result) {
				mFirstFlag = false;
				mDataUpFlag = true;
				mDataFlag = true;
				mLvActIss.stopLoadMore();
				mSwipeRefreshLayout.setRefreshing(false); // 上拉加载完成
				if (result == null) {
					mLvActIss.setPullLoadEnable(false);
					ViewSolveUtils.morePageOne(mLvActIss, mLyView, mIvView, mProgView, mPage);
				} else {
					Log.d(TAG, "result shopInfo >>> " + result.toString());
					ViewSolveUtils.setNoData(mLvActIss, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA); // 有数据
					mLvActIss.setPullLoadEnable(true);
					PageData page = new PageData(result, "activityList", new TypeToken<List<Activitys>>() {}.getType());
					mPage = page.getPage();
					if (page.hasNextPage() == false) {
						if (mPage > 1) {
							Util.getContentValidate(R.string.no_more);
						}
						mLvActIss.setPullLoadEnable(false);
					} else {
						mLvActIss.setPullLoadEnable(true);
					}
					List<Activitys> list = (List<Activitys>) page.getList();
					if (null == list || list.size() <= 0) {
						ViewSolveUtils.morePageOne(mLvActIss, mLyView, mIvView, mProgView, mPage);
					} else {
						ViewSolveUtils.setNoData(mLvActIss, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA); // 有数据
					}

					if (mActIssueAdapter == null) {
						mActIssueAdapter = new ActIssueAdapter(getMyActivity(), list);
						mLvActIss.setAdapter(mActIssueAdapter);
					} else {
						if (page.getPage() == 1) {
							mActIssueAdapter.setItems(list);
						} else {
							mActIssueAdapter.addItems(list);
						}
					}
					try {
						mPage = Integer.parseInt(result.get("nextPage").toString());
						Log.d(TAG, "Home >>> mPage ," + mPage);
					} catch (Exception e) {
						Log.e(TAG, "mPage >>> error ," + e.getMessage());
					}
				}

			}
		}).execute(String.valueOf(mPage));
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	/**
	 * 下拉刷新
	 */
	private OnRefreshListener refreshListener = new OnRefreshListener() {
		@Override
		public void onRefresh() {
			if (mDataFlag) {
				mDataFlag = false;
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						mPage = 1;
						getActList();
					} 
				}, 2000);
			}
		}
	};

	/**
	 * 上拉加载
	 */
	@Override
	public void onLoadMore() {
		if (mDataUpFlag) {
			mDataUpFlag = false;
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					getActList();
				}
			}, 2000);
		}
	}

	/**
	 * 友盟统计
	 */
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(ActIssueFragment.class.getSimpleName()); 
	}
	@Override
	public void onResume() {
		super.onResume();
		initData();
		MobclickAgent.onPageStart("MainScreen"); //统计页面
		MobclickAgent.onPageEnd(ActIssueFragment.class.getSimpleName());
	}
}
