package com.huift.hfq.cust.fragment;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.huift.hfq.base.pojo.Activitys;
import com.huift.hfq.base.pojo.NewPageData;
import com.huift.hfq.base.view.XListView;
import com.huift.hfq.base.view.XListView.IXListViewListener;
import com.huift.hfq.cust.R;

import com.google.gson.reflect.TypeToken;
import com.huift.hfq.cust.activity.ActDetailActivity;
import com.huift.hfq.cust.activity.ActThemeDetailActivity;
import com.huift.hfq.cust.adapter.MyPromotionAdapter;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.model.GetUserActListTask;
import com.umeng.analytics.MobclickAgent;
/**
 * 已经完成的活动
 * @author yingchen
 */
public class FinishPromotionFragment extends Fragment implements IXListViewListener ,OnRefreshListener{
	private static final String TAG = FinishPromotionFragment.class.getSimpleName();
	private static final int mType = 2;
	/**下拉刷新*/
	private SwipeRefreshLayout mSwipeRefreshLayout;
	/**列表*/
	private XListView mXListView;
	/**页码*/
	private int mPage = 1;
	/**数据（用来装服务器返回的所有数据）*/
	private List<Activitys> mData; 
	/**接收服务器返回的数据*/
	private NewPageData mPageData;
	/**没有数据时显示的视图*/
	private RelativeLayout mNoData;
	/**适配器*/
	private MyPromotionAdapter adapter;
	/**进度条*/
	private ProgressBar mProgressBar;
	
	public static FinishPromotionFragment newInstance() {
		Bundle args = new Bundle();
		FinishPromotionFragment fragment = new FinishPromotionFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_finish_promotion,container, false);
		mData = new ArrayList<Activitys>();
		initView(view);
		initData();
		return view;
	}
	
	/**
	 * 获取活动列表
	 */
	private void initData() {
		new GetUserActListTask(getActivity(), new GetUserActListTask.CallBack() {
			@Override
			public void getResult(JSONObject result) {
				mSwipeRefreshLayout.setRefreshing(false);
				mPageData = new NewPageData(result, new TypeToken<List<Activitys>>() {}.getType());
				if(mPageData.getTotalCount()==0){
					mNoData.setVisibility(View.VISIBLE);
					mSwipeRefreshLayout.setVisibility(View.GONE);
					mProgressBar.setVisibility(View.GONE);
				}else{
					mNoData.setVisibility(View.GONE);
					mSwipeRefreshLayout.setVisibility(View.VISIBLE);
				}
				List<Activitys> activitys = (List<Activitys>) mPageData.getList();
				if(null!=activitys && activitys.size()!=0){
					showAct(activitys);
				}
			}
		}).execute(String.valueOf(mType),String.valueOf(mPage));
	}
	
	/**
	 * 显示活动
	 */
	private void showAct(List<Activitys> activitys){
		if(mPage==1){
			mData.clear();
		}
		mData.addAll(activitys);
		if(null==adapter){
			adapter = new MyPromotionAdapter(getActivity(), activitys, mType);
		}else{
			adapter.setDatas(mData);
			//上拉加载更多时listview滑动到底部
			if(mPage!=1){
				mXListView.setSelection(mXListView.getCount()-1);
			}
		}
		mXListView.setAdapter(adapter);
		if(mPageData.getPage()>=mPageData.getNextPage()){
			mXListView.setPullLoadEnable(false);
		}else{
			mXListView.setPullLoadEnable(true);
		}
	}
	
	/**
	 * 初始化视图
	 * @param view
	 */
	private void initView(View view){
		mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.finish_swipe_refresh);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.red);
		mSwipeRefreshLayout.setOnRefreshListener(this);
		mXListView = (XListView) view.findViewById(R.id.finish_listView);
		mXListView.setXListViewListener(this);
		mNoData = (RelativeLayout) view.findViewById(R.id.rl_no_data);
		mProgressBar = (ProgressBar) view.findViewById(R.id.prog_nodata);
		
		mXListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				/*Intent intent = new Intent(getActivity(), ActDetailActivity.class);
				intent.putExtra(ActDetailFragment.ACT_CODE, mData.get(position).getUserActivityCode());
				getActivity().startActivity(intent);*/
				Intent intent = new Intent(getActivity(), ActThemeDetailActivity.class);
				intent.putExtra(ActThemeDetailActivity.TYPE, CustConst.HactTheme.HOME_ACTIVITY);
				intent.putExtra(ActThemeDetailActivity.THEME_URL, "Browser/getActInfo?activityCode=" + mData.get(position).getActivityCode() + "&appType=1");
				getActivity().startActivity(intent);
			}
		});
	}

	/**
	 * swiperefreshlayout下拉刷新 重置page=1
	 */
	@Override
	public void onRefresh() {
		mPage = 1;
		initData();
	}

	/**
	 * xlistview 上拉加载更多  page++
	 */
	@Override
	public void onLoadMore() {
		mPage ++;
		initData();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(FinishPromotionFragment.class.getSimpleName()); // 统计页面
	};
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(FinishPromotionFragment.class.getSimpleName());
	}
}
