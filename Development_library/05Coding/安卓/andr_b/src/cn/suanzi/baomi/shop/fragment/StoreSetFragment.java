package cn.suanzi.baomi.shop.fragment;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.MyStaffItem;
import cn.suanzi.baomi.base.pojo.PageData;
import cn.suanzi.baomi.base.utils.DialogUtils;
import cn.suanzi.baomi.base.view.MyListView;
import cn.suanzi.baomi.base.view.XListView;
import cn.suanzi.baomi.base.view.XListView.IXListViewListener;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.adapter.StoreSetAdapter;
import cn.suanzi.baomi.shop.adapter.StoreSetAdapter.ViewHolder;
import cn.suanzi.baomi.shop.model.EditOwnerTask;
import cn.suanzi.baomi.shop.model.GetStoreBelongTask;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 门店设置
 * @author qian.zhou
 */
public class StoreSetFragment extends Fragment {
	private static final String TAG = StoreSetFragment.class.getSimpleName();
	/** 员工编码*/
	public static final String STAFF_CODE = "staffCode";
	/** 员工编码*/
	private String mStaffCode;
	/** 页码 **/
	private int mPage = Util.NUM_ONE; 
	/** listview*/
	private MyListView mLvSetStore;
	/** 店长姓名*/
	private TextView mTvManagerName;
	/** 店长电话*/
	private TextView mTvManagerPhone;
	/** 适配器*/
	private StoreSetAdapter mStoreSetAdapter;
	/** 保存*/
	private TextView mTvMsg;
	/** 上拉加载*/
	private boolean mFlagData = false;
	/** 线程*/
	private Handler mHandler;
	private List<String> mShopCodeList;
	/** 正在加载数据*/
	private LinearLayout mLyNodate;
	/**有数据*/
	private LinearLayout mLyNoContent;
	
	/**
	 * 需要传递参数时有利于解耦
	 */
	public static StoreSetFragment newInstance() {
		Bundle args = new Bundle();
		StoreSetFragment fragment = new StoreSetFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_store_set, container, false);
		Util.hintKbTwo(getActivity());// 关闭软键盘
		init(v);
		Util.addLoginActivity(getActivity());
		ViewUtils.inject(this, v);
		return v;
	}

	// 初始化方法
	private void init(View v) {
		//取值
		Intent intent = getActivity().getIntent();
		mStaffCode = intent.getStringExtra(STAFF_CODE);
		if (mPage == Util.NUM_ONE) {
			mFlagData = true;
		}
		mShopCodeList = new ArrayList<String>();
		//初始化视图
		initView(v);
		//初始化数据
		initData();
		setData(0);//没有数据
		//获得门店设置
		getStoreBelong();
	}
	
	/**
	 * 初始化视图
	 */
	public void initView(View v){
		//标题
		LinearLayout ivBack = (LinearLayout) v.findViewById(R.id.layout_turn_in);// 返回
		ivBack.setVisibility(View.VISIBLE);// 显示
		TextView tvTitle = (TextView) v.findViewById(R.id.tv_mid_content);
		tvTitle.setText(getResources().getString(R.string.ser_type));
		mTvMsg = (TextView) v.findViewById(R.id.tv_msg);//保存
		mTvMsg.setText(getResources().getString(R.string.submit_message));
		mTvMsg.setOnClickListener(subListener);// 点击保存，提交数据
		//初始化数据
		mLvSetStore = (MyListView) v.findViewById(R.id.lv_store_list);
		mLyNodate = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mTvManagerName = (TextView) v.findViewById(R.id.tv_set_manager_name);
		mTvManagerPhone = (TextView) v.findViewById(R.id.tv_set_manager_phone);
		mLyNoContent = (LinearLayout) v.findViewById(R.id.ly_content);
	}
	
	/**
	 * 初始化数据
	 */
	public void initData(){
		mHandler = new Handler();
		mLvSetStore.setOnItemClickListener(storeListener);
	}
	
	/**
	 * 门店设置（listView的点击事件）
	 */
	OnItemClickListener storeListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position, long k) {
			final MyStaffItem myStaffItem = (MyStaffItem) mLvSetStore.getItemAtPosition(position);
			final StoreSetAdapter.ViewHolder holder = (ViewHolder) view.getTag();
			if (String.valueOf(Util.NUM_ONE).equals(myStaffItem.getIsOwner())) {
				 holder.ivIsCheck.setImageResource(R.drawable.iv_noselected);
				 myStaffItem.setIsOwner("0");
				 //delete
				 mShopCodeList.remove(myStaffItem.getShopCode());
			} else {
				if (!"".equals(myStaffItem.getManagerName())) {
					 DialogUtils.showSetStoreDialog(getActivity(), myStaffItem.getShopName(), myStaffItem.getManagerName(), getString(R.string.dialog_ok), getString(R.string.dialog_think), new DialogUtils().new OnResultListener() {
							public void onOK() {
								holder.ivIsCheck.setImageResource(R.drawable.iv_selected);
								myStaffItem.setIsOwner("1");
								//save
								mShopCodeList.add(myStaffItem.getShopCode());
							}
					});
				} else {
					if (!"".equals(myStaffItem.getManagerName())) {
						 DialogUtils.showSetStoreDialog(getActivity(), myStaffItem.getShopName(), myStaffItem.getManagerName(), getString(R.string.dialog_ok), getString(R.string.dialog_think), new DialogUtils().new OnResultListener() {
								public void onOK() {
									holder.ivIsCheck.setImageResource(R.drawable.iv_selected);
									myStaffItem.setIsOwner("1");
									//save
									mShopCodeList.add(myStaffItem.getShopCode());
								}
						});
					} else {
						holder.ivIsCheck.setImageResource(R.drawable.iv_selected);
						myStaffItem.setIsOwner("1");
						//save
						mShopCodeList.add(myStaffItem.getShopCode());
					}
				}
			}
		}
	};
	
	/**
	 * 点击提交，提交修改的数据
	 */
	OnClickListener subListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			editOwner();
		}
	};
	
	/**
	 * 获得门店设置
	 */
	public void getStoreBelong(){
		new GetStoreBelongTask(getActivity(), new GetStoreBelongTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (result == null) {
					Util.getContentValidate(R.string.toast_select_fail);
				} else {
					setData(1);//有数据
					//取出大店长信息
					JSONObject jsonObject = (JSONObject) result.get("manager");
					MyStaffItem myStaffItem = Util.json2Obj(jsonObject.toString(), MyStaffItem.class);
					mTvManagerName.setText(myStaffItem.getRealName() + ":");
					mTvManagerPhone.setText(myStaffItem.getMobileNbr());
					//取出店长管理的店铺
					Type jsonType = new TypeToken<List<MyStaffItem>>() {}.getType();// 所属类别
					PageData page = new PageData(result,"shopList",jsonType);
					List<MyStaffItem> list = (List<MyStaffItem>) page.getList();
					for(MyStaffItem item:list){
						if ("1".equals(item.getIsOwner())) {
							mShopCodeList.add(item.getShopCode());
						}
					}
					if (mStoreSetAdapter == null) {
						mStoreSetAdapter = new StoreSetAdapter(getActivity(), list);
						mLvSetStore.setAdapter(mStoreSetAdapter);
					} else {
						if (mPage == 1) {
							mStoreSetAdapter.setItems(list);
						} else {
							mStoreSetAdapter.addItems(list);
						}
					}
				}
			}
		}).execute(mStaffCode, String.valueOf(mPage));
	}
	
	/**
	 * 设置数据
	 * @param type 有没有数据 1 是有数据 0 是没有数据
	 */
	private void setData (int type) {
		if (type == 1) {
			mLyNodate.setVisibility(View.GONE);
			mLyNoContent.setVisibility(View.VISIBLE);
		} else {
			mLyNodate.setVisibility(View.VISIBLE);
			mLyNoContent.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 设置门店或者取消门店
	 */
	public void editOwner(){
		StringBuffer stringBuffer = new StringBuffer();
		mTvMsg.setEnabled(false);
		for (int i = 0; i < mShopCodeList.size(); i++) {
			stringBuffer.append("|" + mShopCodeList.get(i));
		}
		new EditOwnerTask(getActivity(), new EditOwnerTask.Callback() {
			@Override
			public void getResult(int retCode) {
				mTvMsg.setEnabled(true);
				if (ErrorCode.SUCC == retCode) {
					
				} else {
					Util.getContentValidate(R.string.set_fail);
				}
			}
		}).execute(mStaffCode, stringBuffer.toString());
	}
	
	/**
	 * 点击事件
	 */
	@OnClick(R.id.layout_turn_in)
	private void ivTurnTo(View v) {
		getActivity().finish();
	}

}
