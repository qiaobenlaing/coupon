package com.huift.hfq.shop.fragment;

import java.util.List;
import net.minidev.json.JSONObject;
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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.huift.hfq.shop.R;

import com.google.gson.reflect.TypeToken;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Bill;
import com.huift.hfq.base.pojo.PageData;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.base.view.XListView;
import com.huift.hfq.base.view.XListView.IXListViewListener;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.BillDetailActivity;
import com.huift.hfq.shop.adapter.GetBillListAdapter;
import com.huift.hfq.shop.model.GetBillListTask;
import com.lidroid.xutils.ViewUtils;

/**
 * 全部的账单
 * @author wensi.yu
 *
 */
public class BillAllFragment extends Fragment implements IXListViewListener{

	public static final String BILL_TYPE = "billType";
	public static final String BILL_INPUT = "billInput";
	/** 页码 **/
	private int mPage = Util.NUM_ONE;
	/** 顾客清单 **/
	private XListView mLvCustomer;
	/** 适配器 */
	private GetBillListAdapter mAdapter;
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
	/** 得到分类的标志*/
	private String mType;
	/** 得到输入的值*/
	private String mIntentInput;
	/** 输入的值*/
	private String mInput;
	/** 内容的控件*/
	private EditText mTvInput;

	public static BillAllFragment newInstance(Bundle args) {
		BillAllFragment fragment = new BillAllFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bill_today,container, false);
		ViewUtils.inject(this, view);
		ActivityUtils.add(getMyActivity());
		mType = getArguments().getString(BILL_TYPE);
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
		mAdapter = null;
		mDataFlag = true;
		mDataUpFlag = true;
		mLvCustomer = (XListView) view.findViewById(R.id.lv_customer_list);
		if (!mType.equals(String.valueOf(Util.NUM_ONE))) {
			mLvCustomer.setOnItemClickListener(customerClick);
		}
		mLvCustomer.setPullLoadEnable(true);
		mLvCustomer.setXListViewListener(this);
		mHandler = new Handler();
		mLyView = (LinearLayout) view.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) view.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) view.findViewById(R.id.prog_nodata);
		getBillList();
		mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.index_swipe_refresh);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.red);
		mSwipeRefreshLayout.setOnRefreshListener(refreshListener);

		mTvInput = (EditText) getActivity().findViewById(R.id.et_bill_input);//输入的内容
		TextView tvSelect = (TextView) getActivity().findViewById(R.id.tv_bill_select);//查询
		tvSelect.setOnClickListener(selectClick);
	}

	/**
	 * 查询
	 */
	private OnClickListener selectClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (!Util.isEmpty(mTvInput.getText().toString())) {
				mInput = mTvInput.getText().toString();
			} else {
				mInput = "";
			}
			mPage = 1;
			getBillList() ;
		}
	};

	/**
	 * 列表的点击事件
	 */
	private OnItemClickListener customerClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			Bill bill = (Bill) mLvCustomer.getItemAtPosition(position);
			Intent intent = new Intent(getMyActivity(), BillDetailActivity.class);
			intent.putExtra(BillDetailFragment.ORDER_NBR, bill.getConsumeCode());
			startActivity(intent);
		}
	};


	/**
	 * 顾客清单
	 */
	private void getBillList () {

		if (mDataFlag && mPage <= 1) {
			ViewSolveUtils.setNoData(mLvCustomer, mLyView, mIvView, mProgView, ShopConst.DATA.LOADIMG); // 正在加载
		}

		new GetBillListTask(getMyActivity(), new GetBillListTask.Callback() {

			@Override
			public void getResult(JSONObject result) {
				mDataFlag = true;
				mDataUpFlag = true;
				mLvCustomer.stopLoadMore(); // 上拉加载完成
				mSwipeRefreshLayout.setRefreshing(false);

				if (null == result){
					ViewSolveUtils.morePageOne(mLvCustomer, mLyView, mIvView, mProgView, mPage);
				} else {
					ViewSolveUtils.setNoData(mLvCustomer, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA); // 有数据
					mLvCustomer.setPullLoadEnable(true);
					PageData page = new PageData(result, "dataList", new TypeToken<List<Bill>>() {}.getType());
					mPage = page.getPage();
					if (page.hasNextPage() == false) {
						if (page.getPage() > 1) {
							Util.getContentValidate(R.string.no_more);
						}
						mLvCustomer.setPullLoadEnable(false);
					} else {
						mLvCustomer.setPullLoadEnable(true);
					}
					List<Bill> list = (List<Bill>)page.getList();
					if (null == list || list.size() <= 0) {
						ViewSolveUtils.morePageOne(mLvCustomer, mLyView, mIvView, mProgView, mPage);
					} else {
						ViewSolveUtils.setNoData(mLvCustomer, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA); // 有数据
						if (null == mAdapter) {
							mAdapter = new GetBillListAdapter(getMyActivity(), list,mType);
							mLvCustomer.setAdapter(mAdapter);
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
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		}).execute(String.valueOf(mPage),String.valueOf(Util.NUM_FOUR),mType,mInput);
	}

	/**
	 * 加载更多
	 */
	@Override
	public void onLoadMore() {
		if (mDataUpFlag) {
			mDataUpFlag = false;
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					getBillList();
				}
			}, 2000);
		}
	}

	OnRefreshListener refreshListener = new OnRefreshListener() {

		@Override
		public void onRefresh() {
			if (mDataFlag) {
				mDataFlag = false;
				mHandler.postDelayed(new Runnable() {
					public void run() {
						mPage = 1;
						getBillList();
					}
				}, 5000);
			}
		}
	};
}
