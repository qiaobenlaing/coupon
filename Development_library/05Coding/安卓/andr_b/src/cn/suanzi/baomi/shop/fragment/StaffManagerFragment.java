package cn.suanzi.baomi.shop.fragment;

import java.lang.reflect.Type;
import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.MyStaffItem;
import cn.suanzi.baomi.base.pojo.PageData;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.base.view.XListView;
import cn.suanzi.baomi.base.view.XListView.IXListViewListener;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopApplication;
import cn.suanzi.baomi.shop.activity.AddStaffActivity;
import cn.suanzi.baomi.shop.activity.MyStaffShopListActivity;
import cn.suanzi.baomi.shop.adapter.StaffManagerAdapter;
import cn.suanzi.baomi.shop.model.GetMainAdminTask;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 店员管理
 * @author qian.zhou
 */
public class StaffManagerFragment extends Fragment implements IXListViewListener {
	private static final String TAG = StaffManagerFragment.class.getSimpleName();
	/** 修改店长信息*/
	public static final int UPP_MANAGER_INFO = 1;
	public static final int UPP_IS_OK = 1;
	/** 添加店长*/
	public static final int ADD_MANAGER_INFO = 2;
	public static final int ADD_IS_OK = 2;
	/** 设置门店*/
	public static final int SET_STORE_SHOP = 3;
	public static final int SET_IS_OK = 3;
	/** 用listview显示所有员工的具体信息 **/
	private XListView mLvStaff;
	/** 页码 **/
	private int mPage = Util.NUM_ONE;  
	/** 大店长姓名*/
	private TextView mTvGreatManagerName;
	/** 大店长电话*/
	private TextView mTvGreatManagerPhone;
	/** 上拉加载*/
	private boolean mFlagData = false;
	/** 线程*/
	private Handler mHandler;
	/** 适配器*/
	private StaffManagerAdapter mAdapter;
	/** 添加店长*/
	private LinearLayout mAddManager;
	/** 店员管理*/
	private LinearLayout mStaffManager;
	/** 正在加载数据*/
	private LinearLayout mLyNodate;
	private MyStaffItem mMyStaffItem;
	/** 大店長信息*/
	private LinearLayout mLyGreatManager;
	/** 定义全局变量*/
	private ShopApplication mShopApplication;
	/** 得到是否入驻的标示*/
	private boolean mSettledflag;

	public static StaffManagerFragment newInstance() {
		Bundle args = new Bundle();
		StaffManagerFragment fragment = new StaffManagerFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_my_staff, container, false);
		ViewUtils.inject(this, v);
		Util.addLoginActivity(getActivity());
		// 调用初始化方法
		init(v);
		return v;
	}

	/**
	 * 初始化数据
	 */
	private void init(View v) {
		mShopApplication =  (ShopApplication) getActivity().getApplication();
		mSettledflag = mShopApplication.getSettledflag();
		if (mPage == Util.NUM_ONE) {
			mFlagData = true;
		}
		//初始化视图
		initView(v);
		// 调用异步任务类，获得员工信息列表
		mLvStaff.setXListViewListener(this);// 实现xListviewListener接口
		mLvStaff.setPullLoadEnable(true); // 上拉刷新
		mHandler = new Handler();
		setData(0);//没有数据
		//查询大店长和店长信息
		getStaffShopList();
	}
	
	/**
	 * 初始化视图
	 */
	public void initView(View v){
		//头顶标题
		LinearLayout ivBack = (LinearLayout) v.findViewById(R.id.layout_turn_in);// 返回
		ivBack.setVisibility(View.VISIBLE);// 显示
		TextView tvTitle = (TextView) v.findViewById(R.id.tv_mid_content);
		tvTitle.setText(getResources().getString(R.string.assistant_manager));
		TextView tvMsg = (TextView) v.findViewById(R.id.tv_msg);
		tvMsg.setVisibility(View.GONE);
		//初始化数据
		mAddManager = (LinearLayout) getActivity().findViewById(R.id.ly_add_manager);
		mStaffManager = (LinearLayout) getActivity().findViewById(R.id.ly_staff_manager);
		mLyNodate = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mLvStaff = (XListView) v.findViewById(R.id.lv_staff);// 员工
		mAddManager.setOnClickListener(listener);
		mStaffManager.setOnClickListener(listener);
		//listview的头部
		View headView = getActivity().getLayoutInflater().inflate(R.layout.item_my_staff_head, null);
		mLvStaff.addHeaderView(headView);
		//头部视图
		mTvGreatManagerName = (TextView) headView.findViewById(R.id.tv_great_manager_name);//大店长姓名
		mTvGreatManagerPhone = (TextView) headView.findViewById(R.id.tv_great_manager_phone);//大店长电话
		mLyGreatManager = (LinearLayout) headView.findViewById(R.id.ly_great_manager);
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		if(String.valueOf(Util.NUM_TWO).equals(userToken.getUserLvl())){
			//店長登录
			mLyGreatManager.setVisibility(View.GONE);
			mAddManager.setVisibility(View.GONE);
		} else{
			//大店长登录
			mLyGreatManager.setVisibility(View.VISIBLE);
			mAddManager.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 添加店长和店员管理的监听事件
	 */
	OnClickListener listener  = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mSettledflag) {
				switch (v.getId()) {
				case R.id.ly_add_manager://添加店长
					Intent addintent = new Intent(getActivity(), AddStaffActivity.class);
					addintent.putExtra(MyAddManagerFragment.MYSTAFF_ITEM, mMyStaffItem);
					startActivityForResult(addintent, ADD_MANAGER_INFO);
					break;
	            case R.id.ly_staff_manager://店员管理
	            	Intent intent = new Intent(getActivity(), MyStaffShopListActivity.class);
	    			getActivity().startActivity(intent);
					break;
				}     
			} else {
				mShopApplication.getDateInfo(getActivity());
			}
		}
	};
	
	@Override
	public void onActivityResult(int reqCode, int respCode, Intent intent) {
		//添加店长
		if (reqCode == ADD_MANAGER_INFO) {// 如果是添加
			if (respCode == ADD_IS_OK) {
				//查询大店长和店长信息
				getStaffShopList();
			} else {
				// do nothing
			}
		}
		// 编辑店长
		if (reqCode == UPP_MANAGER_INFO) {
			if (respCode == UPP_IS_OK) {
				//查询大店长和店长信息
				getStaffShopList();
			} else {
				// do nothing
			}
		}
		//设置门店
		if (reqCode == SET_STORE_SHOP) {
			if (respCode == SET_IS_OK) {
				//查询大店长和店长信息
				getStaffShopList();
			} else {
				// do nothing
			}
		}
	}
	
	/**
	 * 查询大店长和店长信息
	 */
	public void getStaffShopList(){
		new GetMainAdminTask(getActivity(), new GetMainAdminTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mFlagData = true;
				if (result == null) {
					Util.getContentValidate(R.string.toast_select_fail);
					mLvStaff.setPullLoadEnable(false);
				} else {
					setData(1);//有数据
					mLvStaff.setPullLoadEnable(true);
					//取出大店长信息
					JSONObject jsonObject = (JSONObject) result.get("bManager");
					mMyStaffItem = Util.json2Obj(jsonObject.toString(), MyStaffItem.class);
					mTvGreatManagerName.setText(mMyStaffItem.getRealName() + ":");
					mTvGreatManagerPhone.setText(mMyStaffItem.getMobileNbr());
					//取出店长信息
					Type jsonType = new TypeToken<List<MyStaffItem>>() {}.getType();// 所属类别
					PageData page = new PageData(result,"manager",jsonType);
					mPage = page.getPage();       
					if (page.hasNextPage() == false) {
						if (page.getPage() > 1) {
							Util.getContentValidate(R.string.no_data);
						}
						mLvStaff.setPullLoadEnable(false);
					} else {
						mLvStaff.setPullLoadEnable(true);
					}
					List<MyStaffItem> list = (List<MyStaffItem>) page.getList();
					if (mAdapter == null) {
						mAdapter = new StaffManagerAdapter(getActivity(), list);
						mLvStaff.setAdapter(mAdapter);
					} else {
						if (mPage == 1) {
							mAdapter.setItems(list);
						} else {
							mAdapter.addItems(list);
						}
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
			mLvStaff.setVisibility(View.VISIBLE);
		} else {
			mLyNodate.setVisibility(View.VISIBLE);
			mLvStaff.setVisibility(View.GONE);
		}
	}

	/**
	 *上拉加载
	 */
	@Override
	public void onLoadMore() {
		if (mFlagData) {
			mFlagData = false;
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mPage++;
					getStaffShopList();
					mLvStaff.stopLoadMore();
				}
			}, 2000);
		}           
	}
	
	/**
	 * 返回
	 * @param view
	 */
	@OnClick({R.id.layout_turn_in, R.id.ly_add_manager, R.id.ly_staff_manager})
	public void trunIdenCode(View view){
		switch (view.getId()) {
		case R.id.layout_turn_in://返回
			getActivity().finish();
			break;
		}
	}
}
