package com.huift.hfq.shop.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.ActListContentItem;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.base.view.XListView;
import com.huift.hfq.base.view.XListView.IXListViewListener;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopApplication;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.ActAddActivity;
import com.huift.hfq.shop.activity.ActContentDetailActivity;
import com.huift.hfq.shop.adapter.ActListContentAdapter;
import com.huift.hfq.shop.model.ActListContentTask;
import com.lidroid.xutils.ViewUtils;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wensi.yu
 * 营销活动的活动页
 */
public class ActListContentFragment extends Fragment implements IXListViewListener {

	/** 活动列表*/
	private XListView mLvAclistContentlist;
	/** 页码 */
	private int mPage = 1;
	/** 背景*/
	/** 没有数据加载*/
	private LinearLayout mLyView;
	/** 没有数据加载的图片*/
	private ImageView mIvView ;
	/** 正在加载的进度条*/
	private ProgressBar mProgView;
	/** 上拉加载**/
	private boolean mFlage = false;
	/** 开启线程*/
	private Handler mHandler;
	private ActListContentAdapter mQueryAdapter = null;
	/** 定义全局变量*/
	private ShopApplication mShopApplication;
	/** 得到是否入驻的标示*/
	private boolean mSettledflag;
	
	public static ActListContentFragment newInstance() {
		Bundle args = new Bundle();
		ActListContentFragment fragment = new ActListContentFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_actlist_content_list,container, false);
		ViewUtils.inject(this, view);
		init(view);
		Util.addActivity(getMyActivity());
		ActivityUtils.add(getActivity());
		return view;
	}
	
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}
	
	
	/**
	 * 初始化
	 */
	private void init(View v) {
		mShopApplication =  (ShopApplication) getActivity().getApplication();
		mSettledflag = mShopApplication.getSettledflag();
		if (mPage == Util.NUM_ONE) {
			mFlage = true;
		}
		mLyView = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) v.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) v.findViewById(R.id.prog_nodata);
		
		ImageView ivAdd = (ImageView) getMyActivity().findViewById(R.id.iv_actlist_add);
		ivAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mSettledflag) {
					Intent intent=new Intent(getMyActivity(), ActAddActivity.class);
					startActivity(intent);
				} else {
					mShopApplication.getDateInfo(getActivity());
				}
			} 	
		});
		mHandler = new Handler();	
		//活动内容的列表
		mLvAclistContentlist = (XListView) v.findViewById(R.id.lv_aclist_content_list);
		//刷新
		mLvAclistContentlist.setPullLoadEnable(true); // 上拉刷新
		mLvAclistContentlist.setXListViewListener(this);//实现xListviewListener接口
		//给列表添加事件
		mLvAclistContentlist.setOnItemClickListener(listener);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		actListCount();
	};
	
	/**
	 * 给Listview添加点击事件
	 */
	OnItemClickListener listener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,long id) {	
			ActListContentItem item = (ActListContentItem) mLvAclistContentlist.getItemAtPosition(position);
			String type = ShopConst.WebPage.ACT_DETAIL;
			Intent intent = new Intent(getMyActivity(), ActContentDetailActivity.class);
			intent.putExtra(ActContentDetailActivity.ACT_CODE,item.getActivityCode());
			intent.putExtra(ActContentDetailActivity.TYPE, type);
			getMyActivity().startActivity(intent);
		}
	};
	
	/**
	 * 调用商家列表任务
	 */
	public void actListCount(){
		if (mPage <= 1) {
			ViewSolveUtils.setNoData(mLvAclistContentlist, mLyView, mIvView, mProgView, ShopConst.DATA.LOADIMG); // 正在加载
		}
		
		new ActListContentTask(getMyActivity(),new ActListContentTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mFlage = true;
				if (result == null) {
					// 处理没有数据
					ViewSolveUtils.morePageOne(mLvAclistContentlist, mLyView, mIvView, mProgView, mPage);
					mLvAclistContentlist.setPullLoadEnable(false);
				} else {
					// 有数据
					ViewSolveUtils.setNoData(mLvAclistContentlist, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA);
					mLvAclistContentlist.setPullLoadEnable(true);
					
					JSONArray mActListArray = (JSONArray) result.get("activityList");
					// 总记录数
					int totalCount = (Integer.parseInt(result.get("totalCount").toString()));
					// 当前页数
					int page = (Integer.parseInt(result.get("page").toString()));
					// 显示记录数
					int count = (Integer.parseInt(result.get("count").toString()));
					
					if (page == totalCount) {
						mLvAclistContentlist.setPullLoadEnable(false);
					}
					
					if (count < ShopConst.PAGE_NUM) {
						mLvAclistContentlist.setPullLoadEnable(false);
					}
					
					List<ActListContentItem> mActListData = new ArrayList<ActListContentItem>();
					ActListContentItem item = null;
					for (int i = 0; i < mActListArray.size(); i++) {
						JSONObject actListObject = (JSONObject) mActListArray.get(i);
						//把JsonObject对象转换为实体
						item = Util.json2Obj(actListObject.toString(), ActListContentItem.class);
						item.getShopName();
						mActListData.add(item);
						
						//保存商家名字
						SharedPreferences mSharedPreferences = getMyActivity().getSharedPreferences(ShopConst.ActListName.ACT_SHOPNAME, Context.MODE_PRIVATE);
					    Editor editor = mSharedPreferences.edit(); 
					    editor.putString("shopName", item.getShopName());
					    editor.commit();
					}
					
					if (null == mActListData || mActListData.size() <= 0) {
						ViewSolveUtils.morePageOne(mLvAclistContentlist, mLyView, mIvView, mProgView, mPage);
					} else {
						ViewSolveUtils.setNoData(mLvAclistContentlist, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA);
						if (null == mQueryAdapter) {
							mQueryAdapter = new ActListContentAdapter(getMyActivity(),mActListData);
							mLvAclistContentlist.setAdapter(mQueryAdapter);
						} else {
							if (mPage == 1) {
								mQueryAdapter.setItems(mActListData);
							} else {
								mQueryAdapter.addItems(mActListData);
							}
						}
					}
				}
			}
		}).execute(String.valueOf(mPage));
	}
	
	/**
	 * 上拉加载
	 */
	@Override
	public void onLoadMore() {
		if (mFlage) {
			mFlage = false;
			mHandler.postDelayed(new Runnable() {    
				@Override
				public void run() {
					mPage++;
					actListCount();
					mLvAclistContentlist.stopLoadMore();
				}
			}, 2000);
		}
	}
}
