package com.huift.hfq.shop.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.huift.hfq.shop.R;

import com.google.gson.reflect.TypeToken;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.ActListMoneyItem;
import com.huift.hfq.base.pojo.PageData;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.base.view.XListView;
import com.huift.hfq.base.view.XListView.IXListViewListener;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.ActMoneyDetailActivity;
import com.huift.hfq.shop.adapter.ActListMoneyAdapter;
import com.huift.hfq.shop.model.ActListMoneyTask;
import com.lidroid.xutils.ViewUtils;

/***
 * @author wensi.yu
 * 红包列表
 */
public class ActListMoneyFragment extends Fragment implements IXListViewListener{
	
	private final static String TAG = "ActListMoneyFragment";
	
	public final static String MONEY_STATUS = "changeMoneyStatus";
	public final static String BOUBNS_CODE = "bounsCode";
	public static final int BOUNS_FLAG = 100;
	public static final int BOUNS_SUCCESS = 101;
	public final static String STOP_STR = "停发";
	public final static String ENABLE_STR = "启用";
	public final static int STOP = 0;
	public final static int ENABLE = 1;
	/** 紅包列表*/
	private XListView mLvAclistMoneytList;
	/** 页码 */    
	private int mPage = 1; 
	/** 所属类别*/
	private Type mJsonType = new TypeToken<List<ActListMoneyItem>>() {
	}.getType();
	/** 背景*/
	/** 没有数据加载*/
	private LinearLayout mLyView;
	/** 没有数据加载的图片*/
	private ImageView mIvView ;
	/** 正在加载的进度条*/
	private ProgressBar mProgView;
	/** 判断api是否请求*/
	private boolean mFlage = false;
	/** 线程*/
	private Handler mHandler;
	/** adapter*/
	private ActListMoneyAdapter mQueryAdapter = null;
	/** 填充的数据*/
	private List<ActListMoneyItem> mBounLists;
	private ActListMoneyItem mItem;
	
	public static ActListMoneyFragment newInstance() {
		Bundle args = new Bundle();
		ActListMoneyFragment fragment = new ActListMoneyFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_actlist_money_list, container, false);
		ViewUtils.inject(this,view);
		Util.addActivity(getActivity());
		init(view);
		return view;
	}

	/**
	 * 初始化
	 */
	private void init(View v) {
		mLyView = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) v.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) v.findViewById(R.id.prog_nodata);
		mBounLists = new ArrayList<ActListMoneyItem>();
		if (mBounLists.size() > 0) {
			mBounLists.clear();
		}
		if (mPage == Util.NUM_ONE) {
			mFlage = true;
		}
		final ImageView ivAdd = (ImageView) getActivity().findViewById(R.id.iv_actlist_add);
		ivAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*Intent intent=new Intent(getActivity(), ActAddMoneyActivity.class);
				startActivity(intent);*/
			}
		});
		//红包列表
		mLvAclistMoneytList = (XListView) v.findViewById(R.id.lv_aclist_money_list);
		mHandler = new Handler();
		//刷新
		mLvAclistMoneytList.setPullLoadEnable(true); // 上拉刷新
		mLvAclistMoneytList.setXListViewListener(this);//实现xListviewListener接口
		actListMoney();
		//给列表添加事件
		mLvAclistMoneytList.setOnItemClickListener(listener);
	}
	
	/**
	 * 列表的点击事件
	 */
	private OnItemClickListener listener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
			mItem = (ActListMoneyItem) mLvAclistMoneytList.getItemAtPosition(position);
			Intent intent = new Intent(getActivity(), ActMoneyDetailActivity.class);
			intent.putExtra(ActMoneyDetailActivity.ACTMONEY_CODE, mItem.getBonusCode());
			intent.putExtra(ActMoneyDetailActivity.BOUNS_SOURCE,ActMoneyDetailActivity.LIST_BONUS);
			startActivityForResult(intent, BOUNS_FLAG);
		}
	};
	
	/**
	 * 活动列表的异步
	 */
	public void actListMoney(){
		if (mPage <= 1) {
			ViewSolveUtils.setNoData(mLvAclistMoneytList, mLyView, mIvView, mProgView, ShopConst.DATA.LOADIMG); // 正在加载
		}
		new ActListMoneyTask(getActivity(), new ActListMoneyTask.Callback() {			
			@Override
			public void getResult(JSONObject result) {
				mFlage = true;
				if (result == null ) {
					Log.d(TAG, "没有数据");
					// 处理没有数据
					ViewSolveUtils.morePageOne(mLvAclistMoneytList, mLyView, mIvView, mProgView, mPage);
					mLvAclistMoneytList.setPullLoadEnable(false);
				} else {
					// 有数据
					ViewSolveUtils.setNoData(mLvAclistMoneytList, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA);
					mLvAclistMoneytList.setPullLoadEnable(true);
					
					PageData page = new PageData(result,"bonusList",mJsonType);
					mPage = page.getPage();
					Log.i(TAG, "mPage========"+mPage);
					if (page.hasNextPage() == false) {
						if (page.getPage() > 1) {
							Util.getContentValidate(R.string.toast_moredata);
						}
						mLvAclistMoneytList.setPullLoadEnable(false);
					} else {
						mLvAclistMoneytList.setPullLoadEnable(true);
					}
					
					List<ActListMoneyItem> list = (List<ActListMoneyItem>)page.getList();
					
					if (null == list || list.size() <= 0) {
						ViewSolveUtils.morePageOne(mLvAclistMoneytList, mLyView, mIvView, mProgView, mPage);
						
					} else {
						ViewSolveUtils.setNoData(mLvAclistMoneytList, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA);
						// 赋值
						mBounLists.addAll(list);
						if (mQueryAdapter == null) {
							mQueryAdapter = new ActListMoneyAdapter(getActivity(), list);
							mLvAclistMoneytList.setAdapter(mQueryAdapter);
						} else {
							if (page.getPage() == 1) {
								mQueryAdapter.setItems(list);
							} else {
								mQueryAdapter.addItems(list);
							}
						}
					}
				}
			}
		}).execute(String.valueOf(mPage));		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "requestCode=" + requestCode);
		switch (requestCode) {
		case BOUNS_FLAG:
			Log.d(TAG, "resultCode=" + resultCode);
			if (resultCode == BOUNS_SUCCESS) {
				String moneyStatus = data.getExtras().getString(MONEY_STATUS);
				String moneyCode = data.getExtras().getString(BOUBNS_CODE); 
				Log.d(TAG, "moneyStatus="+moneyStatus);
				int status = 0;
				if (moneyStatus.equals(STOP_STR)) { // 如果是停发就是启用状态    
					status = ENABLE;
				} else {
					status = STOP;
				}
				Log.d(TAG, "mBounLists="+mBounLists.size());
				for (int i = 0; i < mBounLists.size(); i++) {
					ActListMoneyItem moneyItem = (ActListMoneyItem) mBounLists.get(i);
					if (moneyItem.getBonusCode().equals(moneyCode)) {
						moneyItem.setStatus(status);
					}
				}
				mQueryAdapter.setItems(mBounLists);
			}
			break;
		}
	}
	
	/**
	 * 上拉刷新
	 */
	@Override
	public void onLoadMore() {
		if (mFlage) {
			mFlage = false;
			mHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					mPage++;
					actListMoney();
					mLvAclistMoneytList.stopLoadMore();
				}
			}, 2000);
		}
	}
}
