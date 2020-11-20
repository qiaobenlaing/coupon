package com.huift.hfq.shop.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.PageData;
import com.huift.hfq.base.pojo.StaffShop;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.base.view.XListView;
import com.huift.hfq.base.view.XListView.IXListViewListener;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.adapter.GetClerkAdminAdapter;
import com.huift.hfq.shop.model.EditStaffTask;
import com.huift.hfq.shop.model.GetClerkAdminTask;
import com.huift.hfq.shop.model.MyStaffDelTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.minidev.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 店员管理
 * @author wenis.yu
 */
public class MyStaffManagementFragment extends Fragment implements IXListViewListener {

	public static final String SHOP_CODE = "shopCode";
	public static final String SHOP_NAME = "shopName";
	public static final String SHOP_BRANDID = "brandId";
	/** 所属类别 */
	private Type jsonType = new TypeToken<List<StaffShop>>() {
	}.getType();
	/** 返回图片 **/
	private LinearLayout mIvBackup;
	/** 功能描述文本 **/
	private TextView mTvdesc;
	/** 编辑 */
	private LinearLayout mMsg;
	/** 店员列表 */
	private XListView mLvMyStaffManagerList;
	private Handler mHandler;
	/** 适配器 */
	private GetClerkAdminAdapter mClerkAdminAdapter;
	/** 商家编码 **/
	private String mShopCode;
	/** 页码 **/
	private int mPage = Util.NUM_ONE;
	/** 列表头部的视图 */
	private View mHeadView;
	/** 店长姓名 */
	private TextView mStoreName;
	/** 店长号码 */
	private TextView mStorePhone;
	/** 背景 */
	/** 没有数据加载 */
	private LinearLayout mLyView;
	/** 没有数据加载的图片 */
	private ImageView mIvView;
	/** 正在加载的进度条 */
	private ProgressBar mProgView;
	/** 上拉请求api */
	private boolean mDataUpFlag;
	/** 添加 */
	private LinearLayout mAddStatff;
	/** 添加的姓名 */
	private String mStaffName;
	/** 添加的号码 */
	private String mStaffPhone;
	private List<StaffShop> mStafflist;
	/** 显示编辑的标志*/
	public boolean mShowEdit = false;

	public static MyStaffManagementFragment newInstance() {
		Bundle args = new Bundle();
		MyStaffManagementFragment fragment = new MyStaffManagementFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mystaffmanager, container, false);
		ViewUtils.inject(this, view);
		init(view);
		return view;
	}

	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	private void init(View view) {
		mDataUpFlag = true;
		mStafflist = new ArrayList<StaffShop>();
		// 加入头部和尾部
		mHeadView = android.view.View.inflate(getActivity(), R.layout.top_mystaffmanmger, null);
		mStoreName = (TextView) mHeadView.findViewById(R.id.tv_myemployee_name);// 店长姓名
		mStorePhone = (TextView) mHeadView.findViewById(R.id.tv_myemployee_phone);// 店长号码

		mAddStatff = (LinearLayout) getActivity().findViewById(R.id.ly_add_staff);
		mAddStatff.setOnClickListener(staffListener);//添加员工
		// 设置标题
		mIvBackup = (LinearLayout) view.findViewById(R.id.layout_turn_in);
		mTvdesc = (TextView) view.findViewById(R.id.tv_mid_content);
		Intent intent = getActivity().getIntent();
		String staffTitle = intent.getStringExtra(SHOP_NAME);
		mShopCode = intent.getStringExtra(SHOP_CODE);
		mTvdesc.setText(staffTitle);
		mIvBackup.setVisibility(View.VISIBLE);
		mMsg = (LinearLayout) view.findViewById(R.id.layout_msg);// 编辑
		mMsg.setVisibility(View.VISIBLE);
		// 刷新
		mLvMyStaffManagerList = (XListView) view.findViewById(R.id.lv_staffmanmger_list);
		mLvMyStaffManagerList.setPullLoadEnable(true); // 上拉刷新
		mLvMyStaffManagerList.addHeaderView(mHeadView);
		mLvMyStaffManagerList.setXListViewListener(this);//实现xListviewListener接口
		mHandler = new Handler();
		// 背景
		mLyView = (LinearLayout) view.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) view.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) view.findViewById(R.id.prog_nodata);
		// 判断适配器是否为空
		if (null == mClerkAdminAdapter) {
			mClerkAdminAdapter = new GetClerkAdminAdapter(getMyActivity(), null);
			mLvMyStaffManagerList.setAdapter(mClerkAdminAdapter);
		}
		// 店员列表
		getClerkAdmin();
	}

	/**
	 * 点击添加员工
	 */
	private OnClickListener staffListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			DialogUtils.showDialogEditor(getMyActivity(), getString(R.string.staff_shop_title), getString(R.string.ok), getString(R.string.no), new DialogUtils().new OnResListener() {
				@Override
				public void onOk(String... params) {
					LinkedHashMap<String, Object> reqParams = new LinkedHashMap<String, Object>();
					reqParams.get(params[0]);
					reqParams.get(params[1]);
					mStaffName = params[0];
					mStaffPhone = params[1];
					editStaff();
				}
			});
		}
	};

	/**
	 * 列表的长按事件
	 */
	private OnItemLongClickListener itemClickListener = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {
			StaffShop  item = (StaffShop) mLvMyStaffManagerList.getItemAtPosition(position);
			final String staffCode = item.getStaffCode();
			DialogUtils.showDialog(getActivity(), getString(R.string.cue),getString(R.string.myafter_order_staff_detail), getString(R.string.ok), getString(R.string.no), new DialogUtils().new OnResultListener() {
				@Override
				public void onOK() {
					new MyStaffDelTask(getMyActivity(), new MyStaffDelTask.Callback() {
						@Override
						public void getResult(int retCode) {
							if (retCode == ErrorCode.SUCC) {
								for (int i = 0; i < mStafflist.size(); i++) {
									StaffShop staffShop = mStafflist.get(i);
									if (staffCode.equals(staffShop.getStaffCode())) {
										mStafflist.remove(staffShop);
									}
								}
								mClerkAdminAdapter.setItems(mStafflist);
								Util.getContentValidate(R.string.myafter_order_detail_ok);
							}else{
								Util.getContentValidate(R.string.myafter_order_detail_error);
							}

						}
					}).execute(staffCode);
				}
			});

			return false;
		}
	};

	/**
	 * 添加员工
	 */
	private void editStaff(){
		new EditStaffTask(getMyActivity(), new EditStaffTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (result.get("code").toString().equals(String.valueOf(ErrorCode.SUCC))) {
					String staffCode = result.get("staffCode").toString();
					StaffShop staffShop = new StaffShop();
					staffShop.setStaffCode(staffCode);
					staffShop.setRealName(mStaffName);
					staffShop.setMobileNbr(mStaffPhone);
					mStafflist.add(0,staffShop);
					mClerkAdminAdapter.setItems(mStafflist);
					Util.getContentValidate(R.string.toast_staffmanamer_add);
					mMsg.setVisibility(View.VISIBLE);
				} else {
					if (Integer.parseInt(result.get("code").toString()) == ShopConst.Staff.NO_MOBILENBR) {
						Util.getContentValidate(R.string.num_inputtype);
					} else if (Integer.parseInt(result.get("code").toString()) == ShopConst.Staff.MOBILENBR_ERROR) {
						Util.getContentValidate(R.string.mobilenbr_error);
					} else if (Integer.parseInt(result.get("code").toString()) == ShopConst.Staff.MOBILENBR_BEENUSED) {
						Util.getContentValidate(R.string.mobilenbr_used);
					} else if (Integer.parseInt(result.get("code").toString()) == ShopConst.Staff.NO_STAFFNAME) {
						Util.getContentValidate(R.string.name_inputtype);
					} else if (Integer.parseInt(result.get("code").toString()) == ShopConst.Staff.NO_STAFFTYPE) {
						Util.getContentValidate(R.string.select_type);
					} else if (Integer.parseInt(result.get("code").toString()) == ShopConst.Staff.PHONE_USERED) {
						Util.getContentValidate(R.string.mobilenbr_used);
					}
				}
			}
		}).execute("",mStaffPhone,mStaffName,String.valueOf(Util.NUM_ONE),mShopCode);
	}

	/**
	 * 员工列表
	 */
	private void getClerkAdmin() {
		if (mPage <= 1) {
			ViewSolveUtils.setNoData(mLvMyStaffManagerList, mLyView, mIvView, mProgView, ShopConst.DATA.LOADIMG); // 正在加载
		}

		new GetClerkAdminTask(getMyActivity(), new GetClerkAdminTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mDataUpFlag = true;
				if (null == result) {
					ViewSolveUtils.morePageOne(mLvMyStaffManagerList, mLyView, mIvView, mProgView, mPage);
					mLvMyStaffManagerList.setPullLoadEnable(false);
					mMsg.setVisibility(View.GONE);
				} else {
					ViewSolveUtils.setNoData(mLvMyStaffManagerList, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA); // 有数据
					mLvMyStaffManagerList.setPullLoadEnable(true);
					mMsg.setVisibility(View.VISIBLE);
					PageData page = new PageData(result, "staffList", jsonType);
					mPage = page.getPage();
					List<StaffShop> stafflist = (List<StaffShop>) page.getList();
					Object object = result.get("managerInfo");
					Object objectStaff = result.get("staffList");
					if (null != object && !"[]".equals(object.toString())) {//店长不是空
						JSONObject staffObject = (JSONObject) object;
						String storeName = staffObject.get("realName").toString();
						String storePhone = staffObject.get("mobileNbr").toString();
						mStoreName.setText(storeName);
						mStorePhone.setText(storePhone);
					} else {
						mStoreName.setText("");
						mStorePhone.setText("");
					}

					if (null != objectStaff && !"[]".equals(objectStaff.toString())) {
						mMsg.setVisibility(View.VISIBLE);
					} else {
						mMsg.setVisibility(View.GONE);
					}

					if (page.hasNextPage() == false) {
						if (page.getPage() > 1) {
							Util.getContentValidate(R.string.toast_moredata);
						}
						mLvMyStaffManagerList.setPullLoadEnable(false);
					} else {
						mLvMyStaffManagerList.setPullLoadEnable(true);
					}

					if (null == stafflist || stafflist.size() <= 0 ) {
						//ViewSolveUtils.morePageOne(mLvMyStaffManagerList, mLyView, mIvView, mProgView, mPage);
					} else {
						ViewSolveUtils.setNoData(mLvMyStaffManagerList, mLyView, mIvView, mProgView, ShopConst.DATA.HAVE_DATA); // 有数据
						mStafflist.addAll(stafflist);
						if (null == mClerkAdminAdapter) {
							mClerkAdminAdapter = new GetClerkAdminAdapter(getMyActivity(), stafflist);
							mLvMyStaffManagerList.setAdapter(mClerkAdminAdapter);
						} else {
							if (page.getPage() == 1) {
								mClerkAdminAdapter.setItems(stafflist);
							} else {
								mClerkAdminAdapter.addItems(stafflist);
							}
						}
					}
				}
			}
		}).execute(mShopCode, String.valueOf(mPage));
	}

	/**
	 *设置编辑按钮不可见
	 */
	public void setEditInVisibile() {
		mShowEdit = false;
		mMsg.setVisibility(View.VISIBLE);
		for (int i = 0; i < mStafflist.size(); i++) {
			mStafflist.get(i).setShowEdit(false);
		}
		mClerkAdminAdapter.notifyDataSetChanged();
		mLvMyStaffManagerList.setOnItemLongClickListener(null);
	}

	/**
	 * 返回
	 * @param view
	 */
	@OnClick({ R.id.layout_turn_in, R.id.tv_msg })
	public void trunIdenCode(View view) {
		switch (view.getId()) {
			case R.id.layout_turn_in:// 返回
				if (mShowEdit) {
					setEditInVisibile();
				} else {
					getActivity().finish();
				}

				break;

			case R.id.tv_msg:// 编辑员工
				mShowEdit = true;
				mMsg.setVisibility(View.GONE);
				try {
					for (int i = 0; i < mStafflist.size(); i++) {
						mStafflist.get(i).setShowEdit(true);
					}
					mClerkAdminAdapter.notifyDataSetChanged();
					mLvMyStaffManagerList.setOnItemLongClickListener(itemClickListener);
				} catch (Exception e) {
					e.printStackTrace();
				}

				break;
			default:
				break;
		}
	}

	/**
	 * 上拉加载
	 */
	@Override
	public void onLoadMore() {
		if (mDataUpFlag) {
			mDataUpFlag = false;
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					mPage++;
					getClerkAdmin();
					mLvMyStaffManagerList.stopLoadMore();
				}
			}, 2000);
		}
	}
}
