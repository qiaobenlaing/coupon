package com.huift.hfq.shop.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.google.gson.reflect.TypeToken;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.ListGrabBonus;
import com.huift.hfq.base.view.XListView;
import com.huift.hfq.base.view.XListView.IXListViewListener;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.adapter.ListGrabBonusAdapter;
import com.huift.hfq.shop.model.ListGrabBonusTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 红包领取人数
 * @author wensi.yu
 *
 */
public class MoneyReceiverFragment extends Fragment implements IXListViewListener {
	
	private final static String TAG = "MoneyReceiverFragment";
	
	public final static String BONUS_CODE = "mBounsCode";
	/** 返回*/
	@ViewInject(R.id.layout_turn_in)
	private LinearLayout mIvBackup;
	/** 功能描述文本*/
	@ViewInject(R.id.tv_mid_content)
	private TextView mTvdesc;
	/** 活动列表*/
	private XListView mLvActMoneyReceiverList;
	/** 页码 **/
	private int mPage = 1;
	/** 所属类别**/
	private Type mJsonType = new TypeToken<List<ListGrabBonus>>() {
	}.getType();
	/** 背景*/
	@ViewInject(R.id.ly_nodate)
	private LinearLayout mLyNoDate;
	/** 判断api是否请求*/
	private boolean mFlage = false;
	/** 传过来的红包编码*/
	private String mMoneyCode;
	/** 线程*/
	private Handler mHandler;
	private ListGrabBonusAdapter mQueryAdapter = null;
	
	public static MoneyReceiverFragment newInstance() {
		Bundle args = new Bundle();
		MoneyReceiverFragment fragment = new MoneyReceiverFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_money_receiver,container, false);
		ViewUtils.inject(this, view);
		init(view);
		Util.addLoginActivity(getActivity());
		return view;
	}

	/**
	 * 初始化
	 * @param view
	 */
	private void init(View view) {
		Util.addActivity(getActivity());
		//设置标题
		mTvdesc.setText(R.string.tv_actmoney_title);
		mIvBackup.setVisibility(View.VISIBLE);
		if (mPage == Util.NUM_ONE) {
			mFlage = true;
		}
		//获得传过来的红包编码
		Intent intent = getActivity().getIntent();
		mMoneyCode = intent.getStringExtra(BONUS_CODE);
		mHandler = new Handler();	
		//活动内容的列表
		mLvActMoneyReceiverList = (XListView) view.findViewById(R.id.lv_money_receiver_list);
		//刷新
		mLvActMoneyReceiverList.setPullLoadEnable(true); // 上拉刷新
		mLvActMoneyReceiverList.setXListViewListener(this);//实现xListviewListener接口
		listGrabBonus();
	}
	
	/**
	 * 调用商家列表任务
	 */
	public void listGrabBonus(){
		new ListGrabBonusTask(getActivity(),new ListGrabBonusTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mFlage = true;
				if (result == null) {
					if (mPage > 1) {
						mLyNoDate.setVisibility(View.GONE);
						mLvActMoneyReceiverList.setVisibility(View.VISIBLE);
					} else {
						mLyNoDate.setVisibility(View.VISIBLE);
						mLvActMoneyReceiverList.setVisibility(View.GONE);
					}
					mLvActMoneyReceiverList.setPullLoadEnable(false);
				}else{
					JSONArray mActListArray = (JSONArray) result.get("bonusList");
					// 总记录数
					int totalCount = (Integer.parseInt(result.get("totalCount").toString()));
					// 当前页数
					int page = (Integer.parseInt(result.get("page").toString()));
					// 显示记录数
					int count = (Integer.parseInt(result.get("count").toString()));
					
					if (page == totalCount) {
						mLvActMoneyReceiverList.setPullLoadEnable(false);
					}
					
					if (count < ShopConst.PAGE_NUM) {
						mLvActMoneyReceiverList.setPullLoadEnable(false);
					}
					
					List<ListGrabBonus> mActListData = new ArrayList<ListGrabBonus>();
					ListGrabBonus item = null;
					for (int i = 0; i < mActListArray.size(); i++) {
						JSONObject actListObject = (JSONObject) mActListArray.get(i);
						//把JsonObject对象转换为实体
						item = Util.json2Obj(actListObject.toString(), ListGrabBonus.class);
						mActListData.add(item);
					}
					
					if (null == mQueryAdapter) {
						mQueryAdapter = new ListGrabBonusAdapter(getActivity(),mActListData);
						mLvActMoneyReceiverList.setAdapter(mQueryAdapter);
					} else {
						if (mPage == 1) {
							mQueryAdapter.setItems(mActListData);
						} else {
							mQueryAdapter.addItems(mActListData);
						}
					}
				}
			}
		}).execute(mMoneyCode,String.valueOf(mPage));
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
					listGrabBonus();
					mLvActMoneyReceiverList.stopLoadMore();
				}
			}, 2000);
		}
		
	}
	
	/**
	 * 返回
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	public void backClick(View view){
		getActivity().finish();
	}
}
