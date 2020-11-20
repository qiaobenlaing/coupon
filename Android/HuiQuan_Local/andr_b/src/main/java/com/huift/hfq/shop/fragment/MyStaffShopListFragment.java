package com.huift.hfq.shop.fragment;

import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.google.gson.reflect.TypeToken;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.PageData;
import com.huift.hfq.base.pojo.StaffShop;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.base.view.XListView;
import com.huift.hfq.base.view.XListView.IXListViewListener;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.MyStaffManagementActivity;
import com.huift.hfq.shop.adapter.MyStaffShopListAdapter;
import com.huift.hfq.shop.model.GetStaffShopListTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

/**
 * 门店列表
 * @author wensi.yu
 *
 */
public class MyStaffShopListFragment extends Fragment implements IXListViewListener{
	
	private final static String TAG = "MyStoreListFragment";
	
	private static final String STORE_TITLE = "分店";  
	/** 店员列表*/
	private XListView mLvMyStaffShopList;
	/** 适配器*/
	private MyStaffShopListAdapter myStaffShopListAdapter;
	/** 页码 **/
	private int mPage = Util.NUM_ONE;
	/** 背景*/
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
	
	public static MyStaffShopListFragment newInstance() {
		Bundle args = new Bundle();
		MyStaffShopListFragment fragment = new MyStaffShopListFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mystorelist,container, false);
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
	
	/**
	 * 初始化
	 * @param view
	 */
	private void init(View view) {
		//设置标题
		LinearLayout ivBackup = (LinearLayout) view.findViewById(R.id.layout_turn_in);
		ivBackup.setVisibility(View.VISIBLE);
		TextView tvdesc = (TextView) view.findViewById(R.id.tv_mid_content);
		tvdesc.setText(STORE_TITLE);
		
		//刷新
		mDataFlag = true;
		mDataUpFlag = true;
		mHandler = new Handler();
		mLvMyStaffShopList = (XListView) view.findViewById(R.id.lv_mystorelist_list);
		mLvMyStaffShopList.setOnItemClickListener(clickListener);
		mLvMyStaffShopList.setPullLoadEnable(true);
		mLvMyStaffShopList.setXListViewListener(this);
		mLyView = (LinearLayout) view.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) view.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) view.findViewById(R.id.prog_nodata);
		getStaffShopList();//列表
		mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.index_swipe_refresh);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.red);
		mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
		
	}
	
	/**
	 * 给Listview添加点击事件
	 */
	OnItemClickListener clickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,long id) {	
			StaffShop item = (StaffShop) mLvMyStaffShopList.getItemAtPosition(position);
			Intent intent = new Intent(getMyActivity(), MyStaffManagementActivity.class);
			intent.putExtra(MyStaffManagementFragment.SHOP_CODE,item.getShopCode());
			intent.putExtra(MyStaffManagementFragment.SHOP_NAME,item.getShopName());
			intent.putExtra(MyStaffManagementFragment.SHOP_BRANDID,item.getBrandId());
			getMyActivity().startActivity(intent);
		}
	};
	
	/**
	 * 分店列表
	 */
	private void getStaffShopList(){
		if (mDataFlag && mPage <= 1) {
			ViewSolveUtils.setNoData(mLvMyStaffShopList, mLyView, mIvView, mProgView, ShopConst.DATA.LOADIMG); // 正在加载
		}
		new GetStaffShopListTask(getMyActivity(), new GetStaffShopListTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mDataFlag = true;
				mDataUpFlag = true;
				mLvMyStaffShopList.stopLoadMore(); // 上拉加载完成
				mSwipeRefreshLayout.setRefreshing(false);
				
				if (null == result) {
					ViewSolveUtils.morePageOne(mLvMyStaffShopList, mLyView, mIvView, mProgView, mPage);
					mLvMyStaffShopList.setPullLoadEnable(false);
				} else {
					ViewSolveUtils.setNoData(mLvMyStaffShopList, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA); // 有数据
					mLvMyStaffShopList.setPullLoadEnable(true);   
					PageData page = new PageData(result, "shopList", new TypeToken<List<StaffShop>>() {}.getType());
					mPage = page.getPage();
					Log.d(TAG, "page=" + mPage);
					if (page.hasNextPage() == false) {
						if (page.getPage() > 1) {
							Util.getContentValidate(R.string.no_data);
						}
						mLvMyStaffShopList.setPullLoadEnable(false);
					} else {
						mLvMyStaffShopList.setPullLoadEnable(true);
					}
					
					List<StaffShop> list = (List<StaffShop>)page.getList();
					if (null == list || list.size() <= 0) {
						ViewSolveUtils.morePageOne(mLvMyStaffShopList, mLyView, mIvView, mProgView, mPage);
					} else {
						ViewSolveUtils.setNoData(mLvMyStaffShopList, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA); // 有数据
						if (null == myStaffShopListAdapter) {
							myStaffShopListAdapter = new MyStaffShopListAdapter(getMyActivity(),list);
							mLvMyStaffShopList.setAdapter(myStaffShopListAdapter);
						} else {
							if (page.getPage() == 1) {
								myStaffShopListAdapter.setItems(list);
							} else {
								myStaffShopListAdapter.addItems(list);
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
	OnRefreshListener refreshListener = new OnRefreshListener() {
		
		@Override
		public void onRefresh() {
			if (mDataFlag) {
				mDataFlag = false;
				mHandler.postDelayed(new Runnable() {
					public void run() {
						mPage = 1;
						getStaffShopList();
					}
				}, 5000);
			}
		}
	};

	@Override
	public void onLoadMore() {
		if (mDataUpFlag) {
			mDataUpFlag = false;
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					getStaffShopList();
				}
			}, 2000);
		}
		
	}
	
	/**
	 * 返回
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	public void trunIdenCode(View view){
		getMyActivity().finish();
	}

}
