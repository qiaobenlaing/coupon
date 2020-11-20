package cn.suanzi.baomi.shop.fragment;

import java.lang.reflect.Type;
import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.MyOrderItem;
import cn.suanzi.baomi.base.pojo.PageData;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.base.utils.ViewSolveUtils;
import cn.suanzi.baomi.base.view.XListView;
import cn.suanzi.baomi.base.view.XListView.IXListViewListener;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopConst;
import cn.suanzi.baomi.shop.adapter.MyOrderManagerFailAdapter;
import cn.suanzi.baomi.shop.model.MyOrderManagerTask;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 订单管理(未完成)
 * @author qian.zhou
 */
public class MyBalanceManagerFailFragment extends Fragment implements IXListViewListener {
	private static final String TAG = "MyOrderManagerFailFragment";
	/** 获得一个用户信息对象 **/
	private UserToken mUserToken;
	/** 商家编码 **/
	private String mShopCode;
	/** 令牌验证*/
	private String mTokenCode;
	/** listview显示数据 */
	XListView mLvOrder;
	/** 当前页默认为第一页*/
	private int mPage = Util.NUM_ONE;
	/** 判断状态*/
	private boolean mFlagData = false;
	/** 线程*/
	private Handler mHandler;
	/** 适配器 */
	private MyOrderManagerFailAdapter mAdapter = null;
	/** 0表示是未完成的订单*/
	private int mIsFinish  = Util.NUM_ZERO;//未完成
	/** 没有数据加载*/
	private LinearLayout mLyView;
	/** 没有数据加载的图片*/
	private ImageView mIvView ;
	/** 正在加载的进度条*/
	private ProgressBar mProgView;
	
	/**
	 * 需要传递参数时有利于解耦
	 */
	public static MyBalanceManagerFailFragment newInstance() {
		Bundle args = new Bundle();
		MyBalanceManagerFailFragment fragment = new MyBalanceManagerFailFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_balance_fail_manager, container, false);
		init(view);
		Util.addLoginActivity(getActivity());
		mAdapter = null;
		return view;
	}

	// 初始化方法
	private void init(View v) {
		if (mPage == Util.NUM_ONE) {
			mFlagData = true;
		}
		// 获得一个用户信息对象
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mShopCode = mUserToken.getShopCode();// 商家编码
		mTokenCode = mUserToken.getTokenCode();//令牌
		
		mLvOrder = (XListView) v.findViewById(R.id.lv_fail);
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
			Log.d(TAG, "");
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
					Type jsonType = new TypeToken<List<MyOrderItem>>() {}.getType();// 所属类别
					PageData page = new PageData(JSONobject,"orderList",jsonType);
					mPage = page.getPage();       
					if (page.hasNextPage() == false) {
						if (page.getPage() > 1) {
							Util.getContentValidate(R.string.no_data);
						}
						mLvOrder.setPullLoadEnable(false);
					} else {
						mLvOrder.setPullLoadEnable(true);
					}
					List<MyOrderItem> list = (List<MyOrderItem>) page.getList();
					if (null == list || list.size() <= 0) {
						ViewSolveUtils.morePageOne(mLvOrder, mLyView, mIvView, mProgView, mPage);
					} else {
						ViewSolveUtils.setNoData(mLvOrder, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA); // 有数据
					}
					if (mAdapter == null) {
						mAdapter = new MyOrderManagerFailAdapter(getActivity(), list);
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
		}).execute(mShopCode, String.valueOf(mIsFinish), String.valueOf(mPage) ,mTokenCode);
	}
	
	/**
	 * 点击事件
	 */
	@OnClick({ R.id.layout_turn_in })
	private void ivreturnClickTo(View v) {
		getActivity().finish();
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
}
