package cn.suanzi.baomi.cust.fragment;

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
import cn.suanzi.baomi.base.pojo.Activitys;
import cn.suanzi.baomi.base.pojo.NewPageData;
import cn.suanzi.baomi.base.view.XListView;
import cn.suanzi.baomi.base.view.XListView.IXListViewListener;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.activity.ActDetailActivity;
import cn.suanzi.baomi.cust.adapter.MyPromotionAdapter;
import cn.suanzi.baomi.cust.model.GetUserActListTask;

import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
/**
 * 正在进行的活动
 * @author yingchen
 *
 */
public class ProcessPromotionFragment extends Fragment implements IXListViewListener ,OnRefreshListener{
	private static final String TAG = ProcessPromotionFragment.class.getSimpleName();
	private static final int mType = 1;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private XListView mXListView;
	private int mPage = 1;
	private List<Activitys> mData;
	private NewPageData mPageData;
	private RelativeLayout mNoData;
	private MyPromotionAdapter adapter;
	private ProgressBar mProgressBar;

	
	public static ProcessPromotionFragment newInstance() {
		Bundle args = new Bundle();
		ProcessPromotionFragment fragment = new ProcessPromotionFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_process_promotion,container, false);
		mData = new ArrayList<Activitys>();
		initView(view);
		initData();
		return view;
	}

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
			Log.d(TAG, "#####clear");
			mData.clear();
		}
		mData.addAll(activitys);
		Log.d(TAG, "#####size=="+mData.size());
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
	
	private void initView(View view) {
		mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.process_swipe_refresh);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.red);
		mSwipeRefreshLayout.setOnRefreshListener(this);
		mXListView = (XListView) view.findViewById(R.id.process_listView);
		mXListView.setXListViewListener(this);
		mNoData = (RelativeLayout) view.findViewById(R.id.rl_no_data);
		mProgressBar = (ProgressBar) view.findViewById(R.id.prog_nodata);
		
		mXListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent intent = new Intent(getActivity(), ActDetailActivity.class);
				intent.putExtra(ActDetailFragment.ACT_CODE, mData.get(position).getUserActivityCode());
				getActivity().startActivity(intent);
			}
		});
	}

	/**
	 * xlistview 上拉加载更多
	 */
	@Override
	public void onLoadMore() {
		mPage++;
		initData();
	}

	/**
	 * swiperefreshlayout下拉刷新
	 */
	@Override
	public void onRefresh() {
		mPage = 1;
		initData();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(ProcessPromotionFragment.class.getSimpleName()); 
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(ProcessPromotionFragment.class.getSimpleName()); //统计页面
	}
}
