package com.huift.hfq.shop.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.base.view.MyListView;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopApplication;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.UppDeliveryProgramActivity;
import com.huift.hfq.shop.adapter.DeliveryProgramAdapter;
import com.huift.hfq.shop.model.ListShopDeliveryTask;
import com.huift.hfq.shop.model.SgetShopBasicInfoTask;
import com.huift.hfq.shop.model.UpdateShopInfoTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.List;

/**
 * 餐厅设置
 * @author qian.zhou
 */
public class RestaurantSetFragment extends Fragment {
	public static final String SHOP_OBJ = "shop";
	public final static int RE_UPP_DELIVERY = 101;
	public final static int RESULT_UPP_DELIVERY = 102;
	/** 外卖点餐*/
	private ImageView mIvisOuttake;
	/** 堂食点餐*/
	private ImageView mIvisOrderOn;
	/** 桌号管理*/
	private ImageView mIvSwitch;
	/** 外卖*/
	private String mIsOpenTakeout;
	/** 堂食*/
	private String mIsOpenEat;
	/** 桌号管理*/
	private String mTableNbrSwitch;
	/** 定义全局变量*/
	private ShopApplication mShopApplication;
	/** 得到是否入驻的标示*/
	private boolean mSettledflag;
	/** 表示的是修改头像还是修改外卖或点餐*/
	private String mParamsType;
	private Shop mShop;
	/** 配送方案*/
	private MyListView mLvDeliver;
	/** 保存商家配送方案的所有信息*/
	private List<Shop>  mShopList;
	/** 适配器*/
	private DeliveryProgramAdapter mDeliveryProgramAdapter;
	/** 点击配送方案进行配送方案的修改和添加*/
	private RelativeLayout ryDeliveryProGram;
	/** 全局视图*/
	private View mView;
	/** 当商家的店铺信息*/
	private boolean mUppFlag = false;
	/** */
	private RelativeLayout mRyDineOrder;
	/** 正在加载数据 */
	private LinearLayout mLyNodate;
	/** 正在加载的内容 */
	private LinearLayout mLyContent;
	
	/**
	 * 需要传递参数时有利于解耦
	 * @return SysAboutFragment
	 */
	public static RestaurantSetFragment newInstance() {
		Bundle args = new Bundle();
		RestaurantSetFragment fragment = new RestaurantSetFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater , ViewGroup container ,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_restaurant_set , container ,false);
		ViewUtils.inject(this, mView) ;
		Util.addLoginActivity(getMyActivity());
		init(mView);
		return mView ;
	}
	
	/**
	 * 判断activity不为空
	 * @return
	 */
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}
	
	//初始化数据
	private void init(View view) {
		mShopApplication =  (ShopApplication) getMyActivity().getApplication();
		mSettledflag = mShopApplication.getSettledflag();
		//设置标题
		TextView tvContent = (TextView) view.findViewById(R.id.tv_mid_content);
		tvContent.setText(R.string.restaurant_set);
		LinearLayout ivTurn = (LinearLayout) view.findViewById(R.id.layout_turn_in);
		ivTurn.setVisibility(View.VISIBLE);
		// 加载数据
		mLyNodate = (LinearLayout) view.findViewById(R.id.ly_nodate);
		mLyContent = (LinearLayout) view.findViewById(R.id.ly_content);
		mIvisOuttake = (ImageView) view.findViewById(R.id.iv_isouttake);
		mIvisOrderOn = (ImageView) view.findViewById(R.id.iv_isDine);
		mIvSwitch = (ImageView) view.findViewById(R.id.iv_tablenum_manager);
		mLvDeliver = (MyListView) view.findViewById(R.id.lv_delivery_program);
		mRyDineOrder = (RelativeLayout) view.findViewById(R.id.ry_dine_order);
		ryDeliveryProGram = (RelativeLayout) view.findViewById(R.id.ry_delivery_program);
		setData(0); // 没有数据
		//查询店铺信息
		MyShopInfo(mView);
		//查询商家的配送方案
		getListShopDelivery(mView);
	}
	
	/**
	 * 设置数据
	 * @param type
	 * 有没有数据 1 是有数据 0 是没有数据
	 */
	private void setData(int type) {
		if (type == 1) {
			mLyNodate.setVisibility(View.GONE);
			mLyContent.setVisibility(View.VISIBLE);
		} else {
			mLyNodate.setVisibility(View.VISIBLE);
			mLyContent.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 拍照和调用图库时要执行的方法
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	  if (requestCode == RE_UPP_DELIVERY) {//修改商家的配送方案
		   if (resultCode == RESULT_UPP_DELIVERY) {
			   MyShopInfo(mView);
			   getListShopDelivery(mView);
		   }
	   }
	};
	
	/**
	 * 商铺查询的异步任务类
	 */
	public void MyShopInfo(final View v) {
		// 调用MyHomeTask异步请求，然后修改UI控件
		new SgetShopBasicInfoTask(getActivity(), new SgetShopBasicInfoTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (result == null) {
					return;
				}
				setData(1); // 有数据
				mShop = Util.json2Obj(result.toString(), Shop.class);
				if(mShop != null){
					//判断该商家是否是餐饮类商铺  
					mRyDineOrder.setVisibility(mShop.getIsCatering() == 1 ? View.VISIBLE : View.GONE);
					//外卖点餐
					mIvisOuttake.setImageResource(String.valueOf(Util.NUM_ONE).equals(mShop.getIsOpenTakeout()) ? R.drawable.on : R.drawable.off);
					//堂食点餐
					mIvisOrderOn.setImageResource(String.valueOf(Util.NUM_ONE).equals(mShop.getIsOpenEat()) ? R.drawable.on : R.drawable.off);
					//桌号管理
					mIvSwitch.setImageResource(String.valueOf(Util.NUM_ONE).equals(mShop.getTableNbrSwitch()) ? R.drawable.on : R.drawable.off);
					//外卖点餐
					mIvisOuttake.setOnClickListener(clickListener);
					//堂食点餐
					mIvisOrderOn.setOnClickListener(clickListener);
					mIvSwitch.setOnClickListener(clickListener);
				}
			}
		}).execute();
	}
	
	/**
	 * 获得商家的配送方案
	 */
	public void getListShopDelivery(final View view){
		new ListShopDeliveryTask(getMyActivity(), new ListShopDeliveryTask.Callback() {
			@Override
			public void getResult(JSONArray result) {
				if (result == null) {
					return;
				} 
				setData(1); // 有数据
				mShopList = new Gson().fromJson(result.toString(), new TypeToken<List<Shop>>() {}.getType());
				if (mDeliveryProgramAdapter == null) {
					mDeliveryProgramAdapter = new DeliveryProgramAdapter(getMyActivity(), mShopList);
					mLvDeliver.setAdapter(mDeliveryProgramAdapter);
				} else {
					mDeliveryProgramAdapter.setItems(mShopList);
				}
				//点击配送方案
				ryDeliveryProGram.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getMyActivity(), UppDeliveryProgramActivity.class);
						startActivityForResult(intent, RE_UPP_DELIVERY);
					}
				});
			}
		}).execute();
	}
	
	/**
	 * 修改商铺信息
	 */
	public void updatShop(){
		String updateKey = "";
		String updateValue = "";
		 if (mParamsType.equals(ShopConst.Params.UPDATE_SHOP_ISOPENTAKEOUT)) {
			updateKey = "isOpenTakeout";
			updateValue = mIsOpenTakeout;
			//堂食点餐
		} else if (mParamsType.equals(ShopConst.Params.UPDATE_SHOP_ISOPENEAT)) {
			updateKey = "isOpenEat";
			updateValue = mIsOpenEat;
		} else if (mParamsType.equals(ShopConst.Params.UPDATE_SHOP_TABLENBR_SWITCH)) {
			updateKey = "tableNbrSwitch";
			updateValue = mTableNbrSwitch;
		}
		new UpdateShopInfoTask(getMyActivity(), new UpdateShopInfoTask.Callback() {
			@Override
			public void getResult(int retCode) {
				if (ErrorCode.SUCC == retCode) {
					//DB.saveBoolean(ShopConst.Key.UPP_SHOPINFO,true);
				} else {
					mIsOpenTakeout = mShop.getIsOpenTakeout();
					mIsOpenEat = mShop.getIsOpenEat();
					mTableNbrSwitch = mShop.getTableNbrSwitch();
					Util.getContentValidate(R.string.upp_file);
				}
			}
		}).execute(updateKey , updateValue);
	}
	
	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_isouttake://外卖
				if (mSettledflag) {
					mParamsType  = ShopConst.Params.UPDATE_SHOP_ISOPENTAKEOUT;
					if (String.valueOf(Util.NUM_ONE).equals(mShop.getIsOpenTakeout())) {
						mIvisOuttake.setImageResource(R.drawable.off);
						mIsOpenTakeout = "0";
						mShop.setIsOpenTakeout(mIsOpenTakeout);
					} else {
						mIvisOuttake.setImageResource(R.drawable.on);
						mIsOpenTakeout = "1";
						mShop.setIsOpenTakeout(mIsOpenTakeout);
					}
					//修改商家信息
					updatShop();
				} else {
					mShopApplication.getDateInfo(getMyActivity());
				}
				break;
			case R.id.iv_isDine:// 堂食点餐
				if (mSettledflag) {
					//首先判断该商家是否有上架的商品
					String isHaveProduct = DB.getStr(ShopConst.Key.IS_HAVE_PRODUCT);
					mParamsType = ShopConst.Params.UPDATE_SHOP_ISOPENEAT;
					if (String.valueOf(Util.NUM_ONE).equals(mShop.getIsOpenEat())) {
						mIvisOrderOn.setImageResource(R.drawable.off);
						mIsOpenEat = "0";
						mShop.setIsOpenEat(mIsOpenEat);
						//修改商家信息
						updatShop();
					} else {
						if (isHaveProduct.equals(String.valueOf(Util.NUM_ONE))) {//表示有
							mIvisOrderOn.setImageResource(R.drawable.on);
							mIsOpenEat = "1";
							mShop.setIsOpenEat(mIsOpenEat);
							//修改商家信息
							updatShop();
						} else {//表示没有
							DialogUtils.showSetShopDialog(getMyActivity(), getString(R.string.dialog_noDine),getString(R.string.diaolog_title), new DialogUtils().new OnResultListener() {});
						}
					}
				} else {
					mShopApplication.getDateInfo(getMyActivity());
				}
				break;
			case R.id.iv_tablenum_manager:// 桌号管理
				if (mSettledflag) {
					mParamsType = ShopConst.Params.UPDATE_SHOP_TABLENBR_SWITCH;
					if (String.valueOf(Util.NUM_ONE).equals(mShop.getTableNbrSwitch())) {
						mIvSwitch.setImageResource(R.drawable.off);
						mTableNbrSwitch = "0";
						mShop.setTableNbrSwitch(mTableNbrSwitch);
					} else {
						mIvSwitch.setImageResource(R.drawable.on);
						mTableNbrSwitch = "1";
						mShop.setTableNbrSwitch(mTableNbrSwitch);
					}
					//修改商家信息
					updatShop();
				} else {
					mShopApplication.getDateInfo(getMyActivity());
				}
				break;
			default:
				break;
			}  
		}
	};
	
	@Override
	public void onResume() {
		super.onResume();
		Activity act = getMyActivity();
		if (act == null) {
			return;
		}
		mUppFlag = DB.getBoolean(ShopConst.Key.UPP_DEL_ADD_DELIVERY);
		if (mUppFlag) {
			DB.saveBoolean(ShopConst.Key.UPP_DEL_ADD_DELIVERY,false);
			getListShopDelivery(mView);
			MyShopInfo(mView);
		}
	};
	
	/**点击返回图标返回上一级**/
	@OnClick(R.id.layout_turn_in)
	public void ivbackupClick(View view) {
		getMyActivity().finish() ;
	}
}
