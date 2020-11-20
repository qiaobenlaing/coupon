package com.huift.hfq.shop.fragment;

import java.lang.reflect.Type;
import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.google.gson.reflect.TypeToken;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.MyOrderItem;
import com.huift.hfq.base.pojo.PageData;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.base.view.XListView;
import com.huift.hfq.base.view.XListView.IXListViewListener;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.adapter.MyOrderManagerSusAdapter;
import com.huift.hfq.shop.model.MyOrderManagerTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 对账列表
 * @author qian.zhou
 */
public class MyBalanceManagerSusFragment extends Fragment implements IXListViewListener {
	
	private static final String TAG = "MyOrderManagerSusFragment";
	
	private static final String BANK_TITLE = "银行卡对账";
	/**所属类别**/
	private Type mJsonType = new TypeToken<List<MyOrderItem>>() {
	}.getType();
	/** 返回图片 */
	private LinearLayout mIvBackup;
	/** 功能描述文本 */
	private TextView mTvdesc;
	/** listview显示数据 */
	private XListView mLvOrder;
	/** 当前页为第1页*/
	private int mPage = Util.NUM_ONE;
	/** 判断状态*/
	private boolean mFlagData = false;
	/** 线程*/
	private Handler mHandler;
	/** 适配器 */
	private MyOrderManagerSusAdapter mAdapter = null;
	/** 没有数据加载*/
	private LinearLayout mLyView;
	/** 没有数据加载的图片*/
	private ImageView mIvView ;
	/** 正在加载的进度条*/
	private ProgressBar mProgView;
	
	/**
	 * 需要传递参数时有利于解耦
	 */
	public static MyBalanceManagerSusFragment newInstance() {
		Bundle args = new Bundle();
		MyBalanceManagerSusFragment fragment = new MyBalanceManagerSusFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_balance_sus_manager, container, false);
		Util.addLoginActivity(getActivity());
		ViewUtils.inject(this, view);
		init(view);
		mAdapter = null;
		return view;
	}
	
	/**
	 * 保证activity不为空
	 * @return activity
	 */
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	// 初始化方法
	private void init(View v) {
		if(mPage == Util.NUM_ONE){
			mFlagData = true;
		}
		mIvBackup = (LinearLayout) v.findViewById(R.id.layout_turn_in);
		mTvdesc = (TextView) v.findViewById(R.id.tv_mid_content);
		mIvBackup.setVisibility(View.VISIBLE);
		mTvdesc.setText(BANK_TITLE);
		mLvOrder = (XListView) v.findViewById(R.id.listView);
		mLvOrder.setXListViewListener(this);// 实现xListviewListener接口
		mLvOrder.setPullLoadEnable(true); // 上拉刷新
		mHandler = new Handler();
		mLyView = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) v.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) v.findViewById(R.id.prog_nodata);
		//调用订单管理的列表
		getOrderManager();
	}
	
	public void getOrderManager(){
		if (mPage <= 1) {
			ViewSolveUtils.setNoData(mLvOrder, mLyView, mIvView, mProgView, ShopConst.DATA.LOADIMG); // 正在加载
		}
		new MyOrderManagerTask(getActivity(), new MyOrderManagerTask.Callback() {
			@Override
			public void getResult(JSONObject JSONobject) {
				mFlagData = true;
				if (JSONobject == null) {
					ViewSolveUtils.morePageOne(mLvOrder, mLyView, mIvView, mProgView, mPage);
					mLvOrder.setPullLoadEnable(false);
				} else {
					ViewSolveUtils.setNoData(mLvOrder, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA); // 有数据
					mLvOrder.setPullLoadEnable(true);
					
					PageData page = new PageData(JSONobject,"orderList",mJsonType);
					mPage = page.getPage();
					if (page.hasNextPage() == false) {
						if (page.getPage() > 1) {
							Util.getContentValidate(R.string.toast_moredata);
						}
						mLvOrder.setPullLoadEnable(false);
					} else{
						mLvOrder.setPullLoadEnable(true);
					}
					
					List<MyOrderItem> list = (List<MyOrderItem>) page.getList();
					if (null == list || list.size() <= 0) {
						ViewSolveUtils.morePageOne(mLvOrder, mLyView, mIvView, mProgView, mPage);
					} else {
						ViewSolveUtils.setNoData(mLvOrder, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA); // 有数据
						if (mAdapter == null) {
							mAdapter = new MyOrderManagerSusAdapter(getActivity(), list);
							mLvOrder.setAdapter(mAdapter);
					    	} else {
							if (mPage == 1) {
								mAdapter.setItems(list);
							} else {
								mAdapter.addItems(list);
							}
						}
					}
				}
			}
		}).execute(String.valueOf(mPage));
	}
	
	@Override
	public void onLoadMore() {
		if (mFlagData) {
			mFlagData = false;
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mPage++;
					getOrderManager();
					mLvOrder.stopLoadMore();
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
