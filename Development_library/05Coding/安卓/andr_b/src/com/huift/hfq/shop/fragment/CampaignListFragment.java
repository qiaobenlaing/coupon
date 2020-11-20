package com.huift.hfq.shop.fragment;

import java.util.List;
import net.minidev.json.JSONObject;
import com.huift.hfq.shop.R;

import com.google.gson.reflect.TypeToken;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.Campaign;
import com.huift.hfq.base.pojo.PageData;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.base.view.XListView;
import com.huift.hfq.base.view.XListView.IXListViewListener;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.ActivityManagerActivity;
import com.huift.hfq.shop.activity.CampaignAddActivity;
import com.huift.hfq.shop.adapter.GetActListAdapter;
import com.huift.hfq.shop.model.GetActListTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 活动列表
 * @author wensi.yu
 *
 */
public class CampaignListFragment extends Fragment implements IXListViewListener{
	
	private final static String TAG = "CampaignListFragment";
	
	private final static String CAMPAIGN_TITLE= "营销活动";
	/** 页码 **/
	private int mPage = Util.NUM_ONE;
	/** 活动内容 **/ 
	private XListView mLvCampaign;
	/** 适配器 */
	private GetActListAdapter mAdapter;
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
	/** 下拉加载 */
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private Handler mHandler;
	/** 删除活动列表*/
	public static final int REQUEST_DEL_ACT = 1001;
	public static final int RESULT_DEL_ACT = 1002;
	
	public static CampaignListFragment newInstance() {
		Bundle args = new Bundle();
		CampaignListFragment fragment = new CampaignListFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_campaign_list,container, false);
		ViewUtils.inject(this, view);
		init(view);
		return view;
	}
	
	private Activity getMyActivity(){
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;       
	}

	private void init(View view) {
		TextView tvCampaignName = (TextView) view.findViewById(R.id.tv_mid_content);//标题
		tvCampaignName.setText(CAMPAIGN_TITLE);
		TextView tvSet = (TextView) view.findViewById(R.id.tv_msg);//添加
		tvSet.setBackgroundResource(R.drawable.accntlist_add);
		
		mDataFlag = true;
		mDataUpFlag = true;
		mLvCampaign = (XListView) view.findViewById(R.id.lv_campaign_list);
		
		mLvCampaign.setOnItemClickListener(campaignClick);
		mLvCampaign.setPullLoadEnable(true);
		mLvCampaign.setXListViewListener(this);
		mHandler = new Handler();
		mLyView = (LinearLayout) view.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) view.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) view.findViewById(R.id.prog_nodata);
		sGetActListTask(); //活动列表
		mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.index_swipe_refresh);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.red);
		mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//sGetActListTask();
	}
	
	/**
	 * 给Listview添加点击事件
	 */
	OnItemClickListener campaignClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,long id) {	
			Campaign campaign = (Campaign) mLvCampaign.getItemAtPosition(position);
			Intent intent = new Intent(getMyActivity(), ActivityManagerActivity.class);
			intent.putExtra(ShopConst.UppActStatus.ACT_CODE, campaign.getActivityCode());
			DB.saveStr(ShopConst.UppActStatus.ACT_CODE, campaign.getActivityCode());
			startActivityForResult(intent, REQUEST_DEL_ACT);
		}
	};
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_DEL_ACT://对活动状态的改变
			if (resultCode == RESULT_DEL_ACT){
				Log.d(TAG, "进来了...........");
				sGetActListTask();
			}
			break;
		default:
			break;
		}
	};
	
	/**
	 * 活动列表
	 */
	private void sGetActListTask () {
		
		if (mDataFlag && mPage <= 1) {
			Log.d(TAG, "加载了------");
			ViewSolveUtils.setNoData(mLvCampaign, mLyView, mIvView, mProgView, ShopConst.DATA.LOADIMG); // 正在加载
		}
		
		new GetActListTask(getMyActivity(), new GetActListTask.Callback() {
			
			@Override
			public void getResult(JSONObject result) {
				mDataFlag = true;
				mDataUpFlag = true;
				mLvCampaign.stopLoadMore(); // 上拉加载完成
				mSwipeRefreshLayout.setRefreshing(false);
				
				if (null == result){
					ViewSolveUtils.morePageOne(mLvCampaign, mLyView, mIvView, mProgView, mPage);
				} else {
					ViewSolveUtils.setNoData(mLvCampaign, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA); // 有数据
					mLvCampaign.setPullLoadEnable(true);
					PageData page = new PageData(result, "activityList", new TypeToken<List<Campaign>>() {}.getType());
					mPage = page.getPage();
					Log.d(TAG, "page=" + mPage);
					if (page.hasNextPage() == false) {
						if (page.getPage() > 1) {
							Util.getContentValidate(R.string.no_more);
						}
						mLvCampaign.setPullLoadEnable(false);
					} else {
						mLvCampaign.setPullLoadEnable(true);
					}
					List<Campaign> list = (List<Campaign>)page.getList();
					Log.d(TAG, "campaign  list===" +list);
					if (null == list || list.size() <= 0) {
						ViewSolveUtils.morePageOne(mLvCampaign, mLyView, mIvView, mProgView, mPage);
					} else {
						ViewSolveUtils.setNoData(mLvCampaign, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA); // 有数据
						if (null == mAdapter) {
							mAdapter = new GetActListAdapter(getMyActivity(),list);
							mLvCampaign.setAdapter(mAdapter);
						} else {
							if (page.getPage() == 1) {
								mAdapter.setItems(list);
							} else {
								mAdapter.addItems(list);
							}
						}
					}
					try {
						mPage = Integer.parseInt(result.get("nextPage").toString());
						Log.d(TAG, "mPage == " +mPage);
					} catch (Exception e) {
						Log.e(TAG, "mPage------" + e.getMessage());
					}
				}
			}
		}).execute(String.valueOf(mPage));
	}
	
	/**
	 * 下拉加载
	 */
	private OnRefreshListener refreshListener = new OnRefreshListener() {
		@Override
		public void onRefresh() {
			if (mDataFlag) {
				mDataFlag = false;
				mHandler.postDelayed(new Runnable() {
					public void run() {
						mPage = 1;
						sGetActListTask();
					}
				}, 5000);
			}
		}
	};

	/**
	 * 上拉刷新
	 */
	@Override
	public void onLoadMore() {
		if (mDataUpFlag) {
			mDataUpFlag = false;
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					sGetActListTask();
				}
			}, 2000);
		}
	}
	
	/**
	 * 返回
	 * @param view
	 */
	@OnClick({R.id.layout_turn_in,R.id.layout_msg})
	public void trunIdenCode(View view){
		switch (view.getId()) {
		case R.id.layout_turn_in:
			getMyActivity().finish();
			break;
		case R.id.layout_msg:
			startActivity(new Intent(getMyActivity(), CampaignAddActivity.class));
			break;

		default:
			break;
		}
		
	}
}
