// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.MyShopInfoActivity;
import com.huift.hfq.shop.adapter.UpdateTimeShopAdapter;
import com.huift.hfq.shop.model.SgetShopBasicInfoTask;
import com.huift.hfq.shop.model.UpdateShopTimeTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 修改商家的营业时间
 *
 * @author qian.zhou
 */
public class UpdateShopTimeFragment extends Fragment {
	/**
	 * 完成
	 */
	private TextView mTvMsg;
	private ListView mLvShopTime;
	private UpdateTimeShopAdapter mShopAdapter;
	/**
	 * 开门时间
	 */
	private Shop mShop;
	private List<Shop> mShopList;
	/**
	 * 保存配送信息的集合
	 */
	private List<Shop> mSaveShopList;
	/**
	 * 添加
	 */
	private LinearLayout mLyAddShopTime;
	private boolean mUppFlag = false;
	/**
	 * 是否修改了店铺时间
	 */
	public static final String UPDATE_TIME = "updatetime";

	/**
	 * 需要传递参数时有利于解耦
	 *
	 * @return PosPayFragment
	 */
	public static UpdateShopTimeFragment newInstance() {
		Bundle args = new Bundle();
		UpdateShopTimeFragment fragment = new UpdateShopTimeFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_upp_shoptime, container, false);
		ViewUtils.inject(this, v);
		Util.addLoginActivity(getActivity());
		init(v);
		MyShopInfo();
		mSaveShopList = new ArrayList<Shop>();
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
		tvContent.setText(getResources().getString(R.string.update_business_time));
		mTvMsg = (TextView) v.findViewById(R.id.tv_msg);
		mTvMsg.setText(getString(R.string.toast_setover));
		mLyAddShopTime = (LinearLayout) getActivity().findViewById(R.id.ly_add_time);
		mLvShopTime = (ListView) v.findViewById(R.id.lv_shop_time);
		mLyAddShopTime.setOnClickListener(addShopTime);
	}

	/**
	 * 修改店铺的营业时间
	 */
	public void updateShopTime() {
		String updateKey = "businessHours";
		StringBuffer stringBuffer = new StringBuffer();
		String businessHours = "";
		for (int i = 0; i < mSaveShopList.size(); i++) {
			Shop shop = (Shop) mSaveShopList.get(i);
			businessHours = shop.getOpen() + "," + shop.getClose();
			stringBuffer.append(businessHours + ";");
		}
		mTvMsg.setEnabled(false);
		new UpdateShopTimeTask(getActivity(), new UpdateShopTimeTask.Callback() {
			@Override
			public void getResult(int retCode) {
				mTvMsg.setEnabled(true);
				if (ErrorCode.SUCC == retCode) {
					DB.saveBoolean(ShopConst.Key.UPP_SHOPINFO, true);
					Intent intent = new Intent(getActivity(), MyShopInfoActivity.class);
					getActivity().setResult(MyShopInfoFragment.DE_UPDATE_OPTIME);
					getActivity().finish();
				} else {
					Util.getContentValidate(R.string.upp_file);
				}
			}
		}).execute(updateKey, stringBuffer.toString());
	}

	/**
	 * 商铺查询的异步任务类
	 */
	public void MyShopInfo() {
		// 调用MyHomeTask异步请求，然后修改UI控件
		new SgetShopBasicInfoTask(getActivity(), new SgetShopBasicInfoTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (result == null) {
					return;
				}
				//多段营业时间
				setShopTime(result);
			}
		}).execute();
	}

	/**
	 * 多段营业时间
	 *
	 * @param result
	 */
	public void setShopTime(JSONObject result) {
		JSONArray timeAr = (JSONArray) result.get("businessHours");
		if (timeAr != null) {

			mShopList = new Gson().fromJson(timeAr.toString(), new TypeToken<List<Shop>>() {
			}.getType());
			if (mShopAdapter == null) {
				mShopAdapter = new UpdateTimeShopAdapter(getActivity(), mShopList, new UpdateTimeShopAdapter.CallBackData() {
					@Override
					public void getItemData(List<Shop> shopList) {
						for (int i = 0; i < shopList.size(); i++) {
							Shop shop = shopList.get(i);
						}
						if (null != mSaveShopList && mSaveShopList.size() > 0) {
							mSaveShopList.clear();
						}
						mSaveShopList.addAll(shopList);
					}
				});
				mLvShopTime.setAdapter(mShopAdapter);
			} else {
				mShopAdapter.setItems(mShopList);
			}
		}
	}

	/**
	 * 添加店铺营业时间
	 */
	OnClickListener addShopTime = new OnClickListener() {
		@Override
		public void onClick(View v) {
			//判断集合是否大于3
			if (mShopList.size() >= 3) {
				mLyAddShopTime.setEnabled(false);
				Util.getContentValidate(R.string.add_shoptime_out);
			} else {
				if (null != mShopList && mShopList.size() > 0) {
					mShop = new Shop();
					mShop.setOpen("");
					mShop.setClose("");
					mShopList.add(mShop);
				} else {
					mShopList = new ArrayList<Shop>();
					mShop = new Shop();
					mShop.setOpen("");
					mShop.setClose("");
					mShopList.add(mShop);
				}
				if (mShopAdapter == null) {
					mShopAdapter = new UpdateTimeShopAdapter(getActivity(), mShopList, new UpdateTimeShopAdapter.CallBackData() {
						@Override
						public void getItemData(List<Shop> shopList) {
							if (null != mSaveShopList && mSaveShopList.size() > 0) {
								mSaveShopList.clear();
							}
							mSaveShopList.addAll(shopList);
						}
					});
					mLvShopTime.setAdapter(mShopAdapter);
				} else {
					mShopAdapter.setItems(mShopList);
				}
			}
		}
	};

	/**
	 * 点击返回查看到活动列表
	 *
	 * @param view
	 */
	@OnClick({R.id.layout_turn_in, R.id.tv_msg})
	public void btnBackClick(View view) {
		switch (view.getId()) {
			case R.id.layout_turn_in:
				getActivity().finish();
				break;
			case R.id.tv_msg:
				mUppFlag = DB.getBoolean(UPDATE_TIME);
				if (mUppFlag) {
					DB.saveBoolean(UPDATE_TIME, false);
					updateShopTime();
				}
				break;
			default:
				break;
		}
	}
}
