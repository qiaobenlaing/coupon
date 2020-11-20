// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.fragment;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.Shop;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopConst;
import cn.suanzi.baomi.shop.adapter.DeliveryProgramsAdapter;
import cn.suanzi.baomi.shop.model.EditShopDeliveryTask;
import cn.suanzi.baomi.shop.model.ListShopDeliveryTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 修改商家的配送方案
 * @author qian.zhou
 */
public class UppDeliveryProgramFragment extends Fragment{
	private static final String TAG = "DeliveryProgramFragment";
	/** 完成*/
	private TextView mTvMsg;
	private ListView mLvDeliveryProgram;
	private DeliveryProgramsAdapter mDeliveryProgramsAdapter;
	/** 开门时间*/
	private Shop mShop;
	private List<Shop>  mShopList;
	/** 保存配送信息的集合*/
	private List<Shop>  mSaveShopList;
	/** 商家编码*/
	private String mShopCode ;
	/** 添加配送方案*/
	private LinearLayout mLyAddShopDelivery;
	
	/**
	 * 需要传递参数时有利于解耦 
	 * @return PosPayFragment
	 */
	public static UppDeliveryProgramFragment newInstance() {
		Bundle args = new Bundle();
		UppDeliveryProgramFragment fragment = new UppDeliveryProgramFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_delivery_program, container, false);
		ViewUtils.inject(this, v);
        Util.addLoginActivity(getActivity());
		init(v);
		return v;
	}

	/**
	 * 初始化数据
	 */
	private void init(View v) {
		//头部标题
		LinearLayout ivTurn = (LinearLayout) v.findViewById(R.id.layout_turn_in);
		ivTurn.setVisibility(View.VISIBLE);
		TextView tvContent = (TextView) v.findViewById(R.id.tv_mid_content);
		tvContent.setText(getResources().getString(R.string.delivery_program));
		mTvMsg = (TextView) v.findViewById(R.id.tv_msg);
		mTvMsg.setText(getString(R.string.toast_setover));
		mLyAddShopDelivery = (LinearLayout) getActivity().findViewById(R.id.ly_add_delivery);
		mLvDeliveryProgram = (ListView) v.findViewById(R.id.lv_upp_delivery_program);
		mLyAddShopDelivery.setOnClickListener(addShopTime);
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mShopCode = userToken.getShopCode();
		//获得商家的配送方案
		getListShopDelivery();
		mSaveShopList = new ArrayList<Shop>();
	}
	
	/**
	 * 获得商家的配送方案
	 */
	public void getListShopDelivery(){
		new ListShopDeliveryTask(getActivity(), new ListShopDeliveryTask.Callback() {
			@Override
			public void getResult(JSONArray result) {
				if (result == null) {
					return;
				} 
				mShopList = new Gson().fromJson(result.toString(), new TypeToken<List<Shop>>() {}.getType());
				if (null != mSaveShopList && mSaveShopList.size() > 0) {
					mSaveShopList.clear();
				}
				mSaveShopList.addAll(mShopList);
				mDeliveryProgramsAdapter = new DeliveryProgramsAdapter(getActivity(), mShopList);
				mLvDeliveryProgram.setAdapter(mDeliveryProgramsAdapter);
			}
		}).execute();
	}
	
	/**
	 * 添加店铺配送方案
	 */
	OnClickListener addShopTime = new OnClickListener() {
		@Override
		public void onClick(View v) {
			//判断集合是否大于3
			if (mShopList.size() >= 3) {
				mLyAddShopDelivery.setEnabled(false);
				Util.getContentValidate(R.string.add_shopdelivery_out);
			} else {
				DB.saveBoolean(ShopConst.Key.UPP_DEL_ADD_DELIVERY,true);
				if (null != mShopList && mShopList.size() > 0) {
					mShop = new Shop();
					mShop.setDeliveryId("");
					mShop.setShopCode(mShopCode);
					mShop.setDeliveryDistance("");
					mShop.setRequireMoney("");
					mShop.setDeliveryFee("");
				} else {
					mShopList = new ArrayList<Shop>();
					mShop = new Shop();
					mShop.setDeliveryId("");
					mShop.setShopCode(mShopCode);
					mShop.setDeliveryDistance("");
					mShop.setRequireMoney("");
					mShop.setDeliveryFee("");
				}
				mShopList.add(mShop);
				if (null != mSaveShopList && mSaveShopList.size() > 0) {
					mSaveShopList.clear();
				}
				mSaveShopList.addAll(mShopList);
				if (mDeliveryProgramsAdapter  == null) {
					mDeliveryProgramsAdapter = new DeliveryProgramsAdapter(getActivity(), mShopList);
					mLvDeliveryProgram.setAdapter(mDeliveryProgramsAdapter);
				} else {
					mDeliveryProgramsAdapter.setItems(mShopList);
				}
			}
		}
	};
	 
	/**
	 * 修改店铺的配送地址
	 */
	public void updateShopTime(){
		if (null == mSaveShopList && mSaveShopList.size() < 1) {
			return ;
		}
		StringBuffer deliveryId = new StringBuffer();
		StringBuffer deliveryDistance = new StringBuffer();
		StringBuffer requireMoney = new StringBuffer();
		StringBuffer deliveryFee = new StringBuffer();
		StringBuffer shopCode = new StringBuffer();
		for (int i = 0; i < mSaveShopList.size(); i++) {
			Shop shop = mSaveShopList.get(i);
			deliveryId.append(shop.getDeliveryId() + "|");
			deliveryDistance.append(shop.getDeliveryDistance() + "|");
			requireMoney.append(shop.getRequireMoney() + "|");
			deliveryFee.append(shop.getDeliveryFee() + "|");
			shopCode.append(shop.getShopCode() + "|");
		}
		new EditShopDeliveryTask(getActivity(), new EditShopDeliveryTask.Callback() {
			@Override
			public void getResult(int retCode) {
				if (ErrorCode.SUCC == retCode) {
					//TODO
				} else {
					Util.getContentValidate(R.string.upp_file);
				}
			}
		}).execute(deliveryId.toString(), shopCode.toString(), deliveryDistance.toString(), requireMoney.toString(), deliveryFee.toString());
	}
	
	/**
	 * 点击返回查看到活动列表
	 * @param view
	 */
	@OnClick({R.id.layout_turn_in, R.id.tv_msg})
	public void btnBackClick(View view) {
		switch (view.getId()) {
		case R.id.layout_turn_in:
			getActivity().finish();
			break;
		case R.id.tv_msg:
			updateShopTime();
			break;
		default:
			break;
		}
	}
	
	public void onResume(){
    	super.onResume();
    }
}
