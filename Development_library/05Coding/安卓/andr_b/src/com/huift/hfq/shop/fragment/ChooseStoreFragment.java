package com.huift.hfq.shop.fragment;

import java.lang.reflect.Type;
import java.util.List;
import net.minidev.json.JSONObject;
import com.huift.hfq.shop.R;

import com.google.gson.reflect.TypeToken;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.PageData;
import com.huift.hfq.base.pojo.StaffShop;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.base.view.XListView;
import com.huift.hfq.base.view.XListView.IXListViewListener;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.HomeActivity;
import com.huift.hfq.shop.adapter.ChooseShopListAdapter;
import com.huift.hfq.shop.adapter.MyStaffShopListAdapter;
import com.huift.hfq.shop.model.GetStaffShopListTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 选择店铺
 * @author qian.zhou
 */
public class ChooseStoreFragment extends Fragment implements IXListViewListener{
	
	private final static String TAG = ChooseStoreFragment.class.getSimpleName();
	/** 所属类别*/
	private Type mJsonType = new TypeToken<List<StaffShop>>() {}.getType();
	/**适配器*/
	private ChooseShopListAdapter chooseShopListAdapter = null;
	/** listview*/
	private XListView mLvStore;
	/** 正在加载数据*/
	private LinearLayout mLyNodate;
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
	
	/**
	 * 需要传递参数时有利于解耦
	 * @return SysAboutFragment
	 */
	public static ChooseStoreFragment newInstance() {
		Bundle args = new Bundle();
		ChooseStoreFragment fragment = new ChooseStoreFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater , ViewGroup container ,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_choose_store , container ,false);
		ViewUtils.inject(this, view) ;
		Util.addLoginActivity(getActivity());
		init(view);
		return view ;
	}
	
	//初始化数据
	private void init(View view) {
		//设置标题
		TextView tvContent = (TextView) view.findViewById(R.id.tv_mid_content);
		tvContent.setText("选择店铺");
		LinearLayout ivTurn = (LinearLayout) view.findViewById(R.id.layout_turn_in);
		ivTurn.setVisibility(View.VISIBLE);
		//初始化数据
		mLvStore  = (XListView) view.findViewById(R.id.lv_mystorelist_list);
		mLvStore.setOnItemClickListener(lvstoreListener);
		mLyNodate = (LinearLayout) view.findViewById(R.id.ly_nodate);
		//刷新
		mDataFlag = true;
		mDataUpFlag = true;
		mHandler = new Handler();
		// 调用异步任务类，获得店铺信息列表
		mLvStore.setXListViewListener(this);// 实现xListviewListener接口
		mLvStore.setPullLoadEnable(true); // 上拉刷新
		mLyView = (LinearLayout) view.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) view.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) view.findViewById(R.id.prog_nodata);
		mHandler = new Handler();
		//没有数据
		setData(0);		
		getStaffShopList();
		mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.index_swipe_refresh);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.red);
		mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
	}
	
	/**
	 * listview的点击事件
	 */
	OnItemClickListener  lvstoreListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position, long l) {
			StaffShop staffShop = (StaffShop) mLvStore.getItemAtPosition(position);
			Intent intent = new Intent(getActivity(), HomeActivity.class);
			intent.putExtra(HomeFragment.SHOP_STAFF, staffShop);
			UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
			userToken.setShopCode(staffShop.getShopCode());
			DB.saveObj(DB.Key.CUST_USER_TOKEN,userToken);
			getActivity().startActivity(intent);
			getActivity().finish();
			Util.exitHome();
		}
	};
	
	/**
	 * 分店列表
	 */
	private void getStaffShopList(){
		if (mDataFlag && mPage <= 1) {
			ViewSolveUtils.setNoData(mLvStore, mLyView, mIvView, mProgView, ShopConst.DATA.LOADIMG); // 正在加载
		}
		new GetStaffShopListTask(getActivity(), new GetStaffShopListTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mDataFlag = true;
				mDataUpFlag = true;
				mLvStore.stopLoadMore(); // 上拉加载完成
				mSwipeRefreshLayout.setRefreshing(false);
				if (null == result){
					Util.getContentValidate(R.string.toast_select_fail);
					mLvStore.setPullLoadEnable(false);
				} else {
					setData(1);//有数据
					ViewSolveUtils.setNoData(mLvStore, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA); // 有数据
					mLvStore.setPullLoadEnable(true);   
					PageData page = new PageData(result, "shopList", new TypeToken<List<StaffShop>>() {}.getType());
					mPage = page.getPage();
					Log.d(TAG, "page=" + mPage);
					if (page.hasNextPage() == false) {
						if (page.getPage() > 1) {
							Util.getContentValidate(R.string.no_data);
						}
						mLvStore.setPullLoadEnable(false);
					} else {
						mLvStore.setPullLoadEnable(true);
					}
					
					List<StaffShop> list = (List<StaffShop>)page.getList();
					if (null == list || list.size() <= 0) {
						ViewSolveUtils.morePageOne(mLvStore, mLyView, mIvView, mProgView, mPage);
					} else {
						ViewSolveUtils.setNoData(mLvStore, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA); // 有数据
						if (null == chooseShopListAdapter) {
							chooseShopListAdapter = new ChooseShopListAdapter(getActivity(),list);
							mLvStore.setAdapter(chooseShopListAdapter);
						} else {
							if (page.getPage() == 1) {
								chooseShopListAdapter.setItems(list);
							} else {
								chooseShopListAdapter.addItems(list);
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
	 * 设置数据
	 * @param type 有没有数据 1 是有数据 0 是没有数据
	 */
	private void setData (int type) {
		if (type == 1) {
			mLyNodate.setVisibility(View.GONE);
			mLvStore.setVisibility(View.VISIBLE);
		} else {
			mLyNodate.setVisibility(View.VISIBLE);
			mLvStore.setVisibility(View.GONE);
		}
	}

	/**点击返回图标返回上一级**/
	@OnClick(R.id.layout_turn_in)
	public void ivbackupClick(View view) {
		getActivity().finish() ;
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
}
