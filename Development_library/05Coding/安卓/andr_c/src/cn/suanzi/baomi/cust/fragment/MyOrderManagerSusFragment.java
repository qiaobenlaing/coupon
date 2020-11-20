package cn.suanzi.baomi.cust.fragment;

import java.lang.reflect.Type;
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
import cn.suanzi.baomi.base.pojo.MyOrderItem;
import cn.suanzi.baomi.base.pojo.PageData;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.ViewSolveUtils;
import cn.suanzi.baomi.base.view.XListView;
import cn.suanzi.baomi.base.view.XListView.IXListViewListener;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.adapter.MyOrderManagerSusAdapter;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.model.MyOrderManagerTask;

import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

/**
 * 订单管理 (已完成)
 * @author qian.zhou
 */
public class MyOrderManagerSusFragment extends Fragment implements IXListViewListener {
	private static final String TAG = "MyOrderManagerFragment";
	/** listview显示数据 */
	XListView mLvOrder;
	private int mPage = Util.NUM_ONE;
	/** 没有数据加载*/
	private LinearLayout mLyView;
	/** 没有数据加载的图片*/
	private ImageView mIvView ;
	/** 正在加载的进度条*/
	private ProgressBar mProgView;
	private Handler mHandler;
	/** 适配器 */
	private MyOrderManagerSusAdapter mAdapter = null;
	/** 1表示已经完成的订单*/
	private int mIsFinish  = Util.NUM_ONE;//已完成
	/** 下拉请求api */
	private boolean mDataUpFlag = false;
	/** 上拉请求api */
	private boolean mDataFlag = false;
	/** 下拉加载 */
	private SwipeRefreshLayout mSwipeRefreshLayout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_order_sus_manager, container, false);
		init(view);
		Util.addLoginActivity(getMyActivity());
		mAdapter = null;
		return view;
	}
	
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	// 初始化方法
	private void init(View v) {
		mDataUpFlag = true;
		mDataFlag = true;
		mLvOrder = (XListView) v.findViewById(R.id.listView);
		
		mLyView = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) v.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) v.findViewById(R.id.prog_nodata);
		mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.index_swipe_refresh);
		
		mLvOrder.setPullLoadEnable(true); // 上拉刷新
		mLvOrder.setXListViewListener(this);
		mHandler = new Handler();
		mSwipeRefreshLayout.setColorSchemeResources(R.color.red);
		mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
		
		//判断是否联网
		if (Util.isNetworkOpen(getMyActivity())) {
			//调用订单管理的列表
			getOrderManager();
		} else {
			Util.getToastBottom(getMyActivity(), "请连接网络");
		}
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
						getOrderManager();
					}
				}, 5000);
			}
		}
	};

	public void getOrderManager(){
		if (mDataUpFlag && mPage <= 1) {
			Log.d(TAG, "");
			ViewSolveUtils.setNoData(mLvOrder, mLyView, mIvView, mProgView, CustConst.DATA.LOADIMG); // 正在加载
		}
		new MyOrderManagerTask(getMyActivity(), new MyOrderManagerTask.Callback() {
			@Override
			public void getResult(JSONObject JSONobject) {
				mDataUpFlag = true;
				mDataFlag = true;
				mLvOrder.stopLoadMore();
				mSwipeRefreshLayout.setRefreshing(false); // 上拉加载完成
				if (JSONobject == null) {
					mLvOrder.setPullLoadEnable(false);
					ViewSolveUtils.morePageOne(mLvOrder, mLyView, mIvView, mProgView, mPage);
				} else {
					ViewSolveUtils.setNoData(mLvOrder, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA); // 有数据
					mLvOrder.setPullLoadEnable(true);
					Type jsonType = new TypeToken<List<MyOrderItem>>() {}.getType();// 所属类别
					PageData page = new PageData(JSONobject,"orderList",jsonType);
					mPage = page.getPage();
					if (page.hasNextPage() == false) {
						if (page.getPage() > 1) {
							Util.getContentValidate(R.string.no_more);
						}
						mLvOrder.setPullLoadEnable(false);
					} else {
						mLvOrder.setPullLoadEnable(true);
					}
					List<MyOrderItem> list = (List<MyOrderItem>) page.getList();
					if (null == list || list.size() <= 0) {
						ViewSolveUtils.morePageOne(mLvOrder, mLyView, mIvView, mProgView, mPage);
					} else {
						ViewSolveUtils.setNoData(mLvOrder, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA); // 有数据
					}
					if (mAdapter == null) {
						mAdapter = new MyOrderManagerSusAdapter(getMyActivity(), list);
						mLvOrder.setAdapter(mAdapter);
					} else {
						if (mPage == 1)  {
							mAdapter.setItems(list);
						} else {
							mAdapter.addItems(list);
						}
					}
				}
			}
		}).execute(String.valueOf(mIsFinish), String.valueOf(mPage));
	}
	
	@Override
	public void onLoadMore() {
		if(mDataFlag){
			mDataFlag = false;
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mPage++;
					getOrderManager();
				}
			}, 2000);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(MyOrderManagerSusFragment.class.getSimpleName()); 
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(MyOrderManagerSusFragment.class.getSimpleName()); //统计页面
	}
}
