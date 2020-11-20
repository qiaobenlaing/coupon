package cn.suanzi.baomi.shop.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.Type;
import java.util.List;

import net.minidev.json.JSONObject;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.PageData;
import cn.suanzi.baomi.base.pojo.User;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.ViewSolveUtils;
import cn.suanzi.baomi.base.view.XListView;
import cn.suanzi.baomi.base.view.XListView.IXListViewListener;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopConst;
import cn.suanzi.baomi.shop.adapter.PredeterAdapter;
import cn.suanzi.baomi.shop.adapter.PredeterAdapter.ViewHolder;
import cn.suanzi.baomi.shop.model.ListActParticipantTask;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 預定名單
 * @author qian.zhou
 */
public class PredeterListFragment extends Fragment implements IXListViewListener{
	private static final String TAG = PredeterListFragment.class.getSimpleName();
	/** 活动编码*/
	public static final String ACTIVITY_CODE = "activityCode";
	/** listview*/
	private XListView mLvPredeter;
	/**所属类别**/
	private Type mJsonType = new TypeToken<List<User>>() {
	}.getType();
	/** 当前页为第1页*/
	private int mPage = Util.NUM_ONE;
	/** 活动编码*/
	private String mActivityCode;
	/** 判断状态*/
	private boolean mFlagData = false;
	/** 线程*/
	private Handler mHandler;
	/** 适配器 */
	private PredeterAdapter mAdapter = null;
	/** 没有数据加载*/
	private LinearLayout mLyView;
	/** 没有数据加载的图片*/
	private ImageView mIvView ;
	/** 正在加载的进度条*/
	private ProgressBar mProgView;
	/** 记录点击次数*/
	private int mClickNum;

	/**    
	 * 需要传递参数时有利于解耦
	 */
	public static PredeterListFragment newInstance() {
		Bundle args = new Bundle();
		PredeterListFragment fragment = new PredeterListFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_predeterlist, container, false);
		ViewUtils.inject(this, view);
		Util.addLoginActivity(getMyActivity());
		init(view);
		mAdapter = null;
		return view;
	}

	// 初始化方法
	private void init(View view) {
		//标题
		TextView tvContent = (TextView) view.findViewById(R.id.tv_mid_content);
		tvContent.setText(R.string.predetermined_list);
		if(mPage == Util.NUM_ONE){
			mFlagData = true;
		}
		//取值
		Intent intent = getMyActivity().getIntent();
		mActivityCode = intent.getStringExtra(ACTIVITY_CODE);
		mLvPredeter = (XListView) view.findViewById(R.id.lv_predeterlist);
		mLvPredeter.setXListViewListener(this);// 实现xListviewListener接口
		mLvPredeter.setPullLoadEnable(true); // 上拉刷新
		mHandler = new Handler();
		mLyView = (LinearLayout) view.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) view.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) view.findViewById(R.id.prog_nodata);
		mLvPredeter.setOnItemClickListener(predeterListener);
		//获得预定名单
		getListActParticipant();
	}
	
	OnItemClickListener predeterListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
			mClickNum++;
			final User user = (User) mLvPredeter.getItemAtPosition(position);
			final PredeterAdapter.ViewHolder holder = (ViewHolder) view.getTag();
			if (user.getTotalNbr() >= 1) {
				holder.tvOrderNum.setVisibility(View.VISIBLE);
				mLvPredeter.setEnabled(true);
			} else {
				holder.tvOrderNum.setVisibility(View.GONE);
				mLvPredeter.setEnabled(false);
			}
			if (mClickNum % 2 == 0) {
				holder.lvUser.setVisibility(View.GONE);
				holder.tvOrderNum.setVisibility(View.VISIBLE);
			} else {      
				holder.lvUser.setVisibility(View.VISIBLE);
				holder.tvOrderNum.setVisibility(View.GONE);
			}
		}
	};
	
	private Activity getMyActivity(){
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;       
	}
	
	/**
	 * 获得活动报名名单
	 */
	public void getListActParticipant(){
		if (mPage <= 1) {
			ViewSolveUtils.setNoData(mLvPredeter, mLyView, mIvView, mProgView, ShopConst.DATA.LOADIMG); // 正在加载
		}
		new ListActParticipantTask(getMyActivity(), new ListActParticipantTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mFlagData = true;
				if (result == null) {
					ViewSolveUtils.morePageOne(mLvPredeter, mLyView, mIvView, mProgView, mPage);
					mLvPredeter.setPullLoadEnable(false);
				} else {
					ViewSolveUtils.setNoData(mLvPredeter, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA); // 有数据
					mLvPredeter.setPullLoadEnable(true);
					PageData page = new PageData(result,"participantList",mJsonType);
					mPage = page.getPage();
					if (page.hasNextPage() == false) {
						if (page.getPage() > 1) {
							Util.getContentValidate(R.string.toast_moredata);
						}
						mLvPredeter.setPullLoadEnable(false);
					} else{
						mLvPredeter.setPullLoadEnable(true);
					}
					
					List<User> list = (List<User>) page.getList();
					if (null == list || list.size() <= 0) {
						ViewSolveUtils.morePageOne(mLvPredeter, mLyView, mIvView, mProgView, mPage);
					} else {
						ViewSolveUtils.setNoData(mLvPredeter, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA); // 有数据
						if (mAdapter == null) {
							mAdapter = new PredeterAdapter(getMyActivity(), list);
							mLvPredeter.setAdapter(mAdapter);
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
		}).execute(mActivityCode, String.valueOf(mPage));
	}
	
	/** 点击返回图标返回上一级 **/
	@OnClick(R.id.layout_turn_in)
	public void ivbackupClick(View view) {
		getMyActivity().finish();
	}

	@Override
	public void onLoadMore() {
		if (mFlagData) {
			mFlagData = false;
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mPage++;
					getListActParticipant();
					mLvPredeter.stopLoadMore();
				}
			}, 2000);
		}
	}
}
