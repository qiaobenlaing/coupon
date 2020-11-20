package com.huift.hfq.shop.fragment;


import net.minidev.json.JSONObject;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.Const;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.utils.ImageDownloadCallback;
import com.huift.hfq.base.utils.QrCodeUtils;
import com.huift.hfq.base.utils.ThreeDES;
import com.huift.hfq.shop.ShopConst;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 我的二维码
 * @author qian.zhou
 */
public class MyTwoCodeFragment extends Fragment {
	private static final String TAG = "MyTwoCodeFragment";
	public static final String SHOPOBJ = "shop";
	private String qType = "qr001";//商家端二维码的标示
	/** 获得一个用户信息对象 **/
	private UserToken mUserToken;
	/** 商家编码 **/
	private String mShopCode;
	
	/**
	 * 需要传递参数时有利于解耦
	 */
	public static MyTwoCodeFragment newInstance() {
		Bundle args = new Bundle();
		MyTwoCodeFragment fragment = new MyTwoCodeFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mytwocode, container, false);
		ViewUtils.inject(this, view);
		Util.addActivity(getActivity());
		Util.addLoginActivity(getActivity());
		init(view);
		return view;
	}

	// 初始化方法
	private void init(View v) {
		// 获得一个用户信息对象
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mShopCode = mUserToken.getShopCode();// 商家编码
		LinearLayout ivBack = (LinearLayout) v.findViewById(R.id.layout_turn_in);// 返回
		ivBack.setVisibility(View.VISIBLE);// 显示
		TextView tvTitle = (TextView) v.findViewById(R.id.tv_mid_content);
		tvTitle.setText(getResources().getString(R.string.my_twocode));
		TextView tvMsg = (TextView) v.findViewById(R.id.tv_msg);
		tvMsg.setVisibility(View.GONE);

		TextView tvAddress = (TextView) v.findViewById(R.id.tv_shop_address);
		TextView tvShopName = (TextView) v.findViewById(R.id.iv_shoptetail_name);
		ImageView ivShow = (ImageView) v.findViewById(R.id.iv_shop_logo);
		final ImageView ivTwoCode = (ImageView) v.findViewById(R.id.shop_qr_code);

		//加密二维码SSRC
		String shopCodelen = mShopCode;
		String shopcode = shopCodelen.substring(shopCodelen.length()-6,shopCodelen.length());//截取后的字符串
		Log.d(TAG, "截取后的字符串：：：" + shopcode);
		String shopCodeEnd = ThreeDES.encryptMode(Util.KeyBytes, shopcode.getBytes()); 
		final JSONObject arObj = new JSONObject();
		arObj.put(ShopConst.QrCode.QTYPE, qType);
		arObj.put(ShopConst.QrCode.SHOP_CODE, mShopCode);
		arObj.put(ShopConst.QrCode.SCODE, shopCodeEnd);
		arObj.put(ShopConst.QrCode.SSRC, shopcode);
		
		Intent intent = getActivity().getIntent();
		Shop shop = (Shop) intent.getSerializableExtra(SHOPOBJ);
		if (shop != null) {
			if (!Util.isEmpty(shop.getShopName())) {
				// 赋值
				tvShopName.setText(shop.getShopName());
			} if (!Util.isEmpty(shop.getProvince()) || !Util.isEmpty(shop.getCity()) || !Util.isEmpty(shop.getStreet())) {
				tvAddress.setText(shop.getProvince() + shop.getCity() + shop.getStreet());
			}
			
			//加密后的所有的字符串
			Util.showImage(getActivity(), shop.getLogoUrl(), ivShow);
			if (!"".equals(shop.getLogoUrl())) {
				String url = Const.IMG_URL + shop.getLogoUrl();
				Util.getLocalOrNetBitmap(url, new ImageDownloadCallback(){
					@Override
					public void success(final Bitmap bitmap) {
						//生成二维码
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								ivTwoCode.setImageBitmap(QrCodeUtils.createQrCode(arObj.toString(), bitmap));
							}
						});
					}
					@Override
					public void fail() {
						Log.d(TAG, "出问题啦。。。。。。。。。。。");
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								ivTwoCode.setImageBitmap(QrCodeUtils.createQrCode(arObj.toString()));
							}
						});
					}
				});
			} else {
				ivTwoCode.setImageBitmap(QrCodeUtils.createQrCode(arObj.toString()));
			}
		}  else {
			ivTwoCode.setImageBitmap(QrCodeUtils.createQrCode(arObj.toString()));
		}
	}
	
	/**
	 * 点击事件
	 */
	@OnClick({ R.id.layout_turn_in })
	private void ivreturnClickTo(View v) {
		getActivity().finish();
	}
}
