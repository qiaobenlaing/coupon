package com.huift.hfq.cust.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONObject;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;
import com.huift.hfq.base.Const;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.LoginTask;
import com.huift.hfq.base.model.TheadDBhelper;
import com.huift.hfq.base.pojo.Activitys;
import com.huift.hfq.base.pojo.BatchCoupon;
import com.huift.hfq.base.pojo.Decoration;
import com.huift.hfq.base.pojo.Image;
import com.huift.hfq.base.pojo.Messages;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.base.pojo.ShopDecoration;
import com.huift.hfq.base.pojo.ShopDetail;
import com.huift.hfq.base.pojo.User;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.base.view.MyListView;
import com.huift.hfq.cust.R;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huift.hfq.cust.activity.BigPhotoActivity;
import com.huift.hfq.cust.activity.LoginActivity;
import com.huift.hfq.cust.activity.MyPhotoAlbumActivity;
import com.huift.hfq.cust.activity.ShopPayBillActivity;
import com.huift.hfq.cust.activity.SingleBigPhotoActivity;
import com.huift.hfq.cust.activity.VipChatActivity;
import com.huift.hfq.cust.adapter.CouponDetailSrollAdapter;
import com.huift.hfq.cust.adapter.ShopActAdapter;
import com.huift.hfq.cust.adapter.ShopProductAdapter;
import com.huift.hfq.cust.adapter.SortCouponAdapter;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.model.CancelCardTask;
import com.huift.hfq.cust.model.FocusCardListTask;
import com.huift.hfq.cust.model.GetUserInfo;
import com.huift.hfq.cust.model.ShopDetailTask;
import com.huift.hfq.cust.model.ShopDetailTask.Callback;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 店铺详情
 * @author Zhonghui.Dong , yanfang.li
 */
public class ShopDetailFragment extends Fragment {
	private static final String TAG = ShopDetailFragment.class.getSimpleName();
	public static final String SHOP_CODE = "shopCode";
	public static final String USER_CODE = "userCode";
	public static final String USER_COUPON_CODE = "userCouponCode";
	public static final String PAY_STATUS = "payStatus";
	public static final String FILM_SHOP_CODE = "2f7a5b30-ea05-18ce-b467-019a7293d4f2";
	public static final int REQ_CODE = 100;
	public static final int RESULT_SUCC = 101;
	/** 登陆成功 */
	public static final int LOGIN_SUCC = 201;
	/** 关注 */
	private static final int IS_FOLLEWED = 1;
	/** 不关注 */
	private static final int NO_FOLLEWED = 0;

	/** 更新打折信息的Handler.What */
	protected static final int DISCOUNT = 0;
	/** 商铺名称 */
	private TextView mTvShopName;
	/** 正在加载数据 */
	private LinearLayout mLyNodate;
	/** 正在加载的内容 */
	private LinearLayout mLyContent;
	/** 给商家留言 */
	private RelativeLayout mRlMessageShop;
	/** 取消关注 */
	private Button mBtnFollow;
	/** 滚动图片 */
	private ViewPager mViewPager;
	/** 产品图片 */
	private RelativeLayout mFlRecyclerPhoto;
	/** 优惠券的集合 */
	private MyListView mLvDetailCoupon;
	/** 滚屏图片的集合 */
	private List<ImageView> mImagesList;
	/** viewPage的图片 */
	private String[] mSrollPics;
	/** 优惠劵滚动 */
	private RecyclerView mRecyclerPhotoView;
	/** 产品的适配器*/
	private ShopProductAdapter mProductAdapter;
	/** RecyclerView的管理器*/
	private LinearLayoutManager mLayoutManager;
	/** 视图 */
	private View mView;
	/** 优惠券的适配器 */
	private SortCouponAdapter mCouponAdapter;
	/** 商家编码 */
	private String mShopCode;
	/** 商家详情 */
	private ShopDetail mShopDetail;
	/** 商店详情 */
	private Shop mShop;
	/** 用户优惠券 */
	private List<BatchCoupon> mUserCouponList;
	/** 工行打折描述 */
	private TextView mDisCountTextView;
	/** 买单*/
	private Button BtnPay;

	/**
	 * 需要传递参数时有利于解耦
	 * 
	 * @return ShopDetailFragment
	 */
	public static ShopDetailFragment newInstance() {
		Bundle args = new Bundle();
		ShopDetailFragment fragment = new ShopDetailFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_shoptail, container, false);
		ViewUtils.inject(this, mView);
		Util.addHomeActivity(getMyActivity());
		Util.addActivity(getMyActivity()); // TODO
	
		Util.addLoginActivity(getMyActivity());
		ShareSDK.initSDK(getMyActivity());
		init(mView);
		
		return mView;
	}

	/**
	 * 显示打折信息
	 */
	public void showDiscount(double disCount, double discountUpperLimit) { // TODO
		if (disCount < 0 || disCount >= 10.0) { return; }
		SpannableString ss;
		ColorStateList redColor = ColorStateList.valueOf(0xFFFF0000);
		if (discountUpperLimit == 0) { // 打折优惠无上限
			String str = "*使用工行卡快捷支付可享受" + disCount + "折优惠";
			int start1 = str.indexOf("受") + 1;
			int end1 = str.indexOf("折优惠");
			ss = new SpannableString(str);
			ss.setSpan(new TextAppearanceSpan(null, 0, 0, redColor, null), start1, end1,
					Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
			mDisCountTextView.setText(ss);
		} else {// 优惠有上限
			String str = "*使用工行卡快捷支付可享受" + disCount + "折优惠,最高" + discountUpperLimit + "元";
			int start1 = str.indexOf("受") + 1;
			int end1 = str.indexOf("折优惠");
			int start2 = str.indexOf("高") + 1;
			int end2 = str.indexOf("元");
			ss = new SpannableString(str);
			ss.setSpan(new TextAppearanceSpan(null, 0, 0, redColor, null), start1, end1,
					Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
			ss.setSpan(new TextAppearanceSpan(null, 0, 0, redColor, null), start2, end2,
					Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
			mDisCountTextView.setText(ss);
		}
		if (mShopCode.equals(FILM_SHOP_CODE)) { // 电影院
			mDisCountTextView.setVisibility(View.GONE);
		} else {
			mDisCountTextView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 保证activity不为空
	 * 
	 * @return activity
	 */
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	/**
	 * 获取商店详情
	 * 
	 * @param view
	 *            视图
	 */
	private void getShopInfo(final View view) {

		String userCode = "";
		if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
			UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
			userCode = userToken.getUserCode();
		} else {
			userCode = "";
		}
		new ShopDetailTask(getMyActivity(), new Callback() {
			@Override
			public void getResult(JSONObject result) {
				setData(1); // 有数据
				if (result == null) {
					return;
				} else {
					Log.d(TAG, "shopdetailinfo = " + result.toString());
					Gson gson = new Gson();
					// 商店详情
					mShopDetail = gson.fromJson(result.toJSONString(), new TypeToken<ShopDetail>() {}.getType());
					// 商店上去不为空
					if (null != mShopDetail) {
						Log.d(TAG, "mShopDetail shop " + mShopDetail.getActList().size());
						for (int i = 0; i < mShopDetail.getActList().size(); i++) {
							Log.d(TAG, "mShopDetail shop " + mShopDetail.getActList().get(i).getActivityName());
						}
						mShop = mShopDetail.getShopInfo();
						if (null != mShop) { 
							setShopInfo(mShop); // 设置商家信息
						}
						// 店铺图片
						if (null != mShopDetail.getShopDecoration() && mShopDetail.getShopDecoration().size() > 0) {
							if (mShopDetail.getShopDecoration().size() == 0
									|| "[]".equals(mShopDetail.getShopDecoration().toString())) {
								mSrollPics = new String[1];
								initViewPager(view, mSrollPics);
							} else {
								mSrollPics = new String[mShopDetail.getShopDecoration().size()];
								// 滚动图片
								for (int i = 0; i < mShopDetail.getShopDecoration().size(); i++) {
									ShopDecoration decoration = mShopDetail.getShopDecoration().get(i);
									mSrollPics[i] = decoration.getImgUrl();
								}
								initViewPager(view, mSrollPics);
							}
						}
						// 判断是否是会员卡
						// if (null != mShopDetail.getShopCard() &&
						// mShopDetail.getShopCard().size() > 0) {
						// 判断是否关注商家了
						if (Util.isEmpty(mShop.getIsFollowed())) { // TOOD
							setBtnFollow(NO_FOLLEWED);
						} else {
							if (mShop.getIsFollowed().equals(IS_FOLLEWED + "")) {
								setBtnFollow(IS_FOLLEWED);
							} else {
								setBtnFollow(NO_FOLLEWED);
							}
						}
						// } else {
						// mBtnFollow.setVisibility(View.GONE);
						// }

						// 商家相册图片
						if (null != mShopDetail.getShopPhotoList() && mShopDetail.getShopPhotoList().size() > 0) {
							setPhotoImge(mShopDetail.getShopPhotoList(), view);
						} else {
							mFlRecyclerPhoto.setVisibility(View.GONE);
						}
						// 商店活动
						setShopAct(view, mShopDetail.getActList());
						// 优惠券列表
						if (null != mShopDetail.getCouponList()) {
							mUserCouponList.addAll(mShopDetail.getCouponList().getUserCoupon());
							Log.d(TAG, "mUserCouponList>>>>=1：" + mUserCouponList.size());
							Log.d(TAG, "mUserCouponList>>>>=2：" + mShopDetail.getCouponList().getUserCoupon().size());
							if (mCouponAdapter == null) {
								mCouponAdapter = new SortCouponAdapter(getMyActivity(), mShopDetail.getCouponList()
										.getUserCoupon(), mShopDetail.getCouponList().getShopCoupon(), mShop);
								mLvDetailCoupon.setAdapter(mCouponAdapter);
							} else {
								mCouponAdapter.setItems(mShopDetail.getCouponList().getUserCoupon(), mShopDetail
										.getCouponList().getShopCoupon(), 1);
							}

						}

					}
				}
			}
		}).execute(mShopCode, userCode);

	}

	/**
	 * 商店活动
	 * 
	 * @param view
	 *            全局视图
	 * @param activityList
	 *            活动列表
	 */
	private void setShopAct(View view, List<Activitys> activityList) {
		MyListView lvShopAct = (MyListView) view.findViewById(R.id.lv_shop_act); // 活动列表
		RelativeLayout rlShopAct = (RelativeLayout) view.findViewById(R.id.rl_shopact); // 列表
		List<Activitys> actList = new ArrayList<Activitys>();
		if (null != activityList && activityList.size() > 0) {
			rlShopAct.setVisibility(View.GONE);
			for (int i = 0; i < activityList.size(); i++) {
				Activitys act = activityList.get(i);
				act.setActNumber(getMyActivity().getResources().getString(R.string.shop_act_num) + (i + 1) + " "
						+ getMyActivity().getResources().getString(R.string.shop_act_num_end));
				actList.add(act);
			}
			ShopActAdapter actAdapter = new ShopActAdapter(getMyActivity(), activityList);
			lvShopAct.setAdapter(actAdapter);
		} else {
			rlShopAct.setVisibility(View.GONE);
		}

	}

	/**
	 * 设置商品集合的大图
	 * 
	 * @param decList
	 *            图片集合
	 */
	private void setPhotoImge(List<Decoration> decList, View view) {
		// 设置查看大图的集合
		final List<Image> imageList = new ArrayList<Image>();
		for (int i = 0; i < decList.size(); i++) {
			Decoration decoration = decList.get(i);
			Image image = new Image();
			image.setImageUrl(decoration.getUrl());
			imageList.add(image);
		}
		// 设置优惠券
		if (null != decList && decList.size() > 0) {
			mProductAdapter = new ShopProductAdapter(decList, getMyActivity());
			mProductAdapter.setOnItemClickLitener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int position, long longId) {
					Intent intent = new Intent(getMyActivity(), BigPhotoActivity.class);
					intent.putExtra(BigPhotoActivity.IMAGE_LIST, (Serializable) imageList);
					intent.putExtra(BigPhotoActivity.IMAG_INDEX, position);
					getMyActivity().startActivity(intent);
				}
			});
			mRecyclerPhotoView.setAdapter(mProductAdapter);
		} else {
			mFlRecyclerPhoto.setVisibility(View.GONE);
		}

		// 查看相册
		ImageView ivRecyclerPhoto = (ImageView) view.findViewById(R.id.iv_recycler_photo);
		OnClickListener lookPhotoClick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getMyActivity(), MyPhotoAlbumActivity.class);
				intent.putExtra(MyPhotoAlbumActivity.SHOP_CODE, mShopCode);
				startActivity(intent);
			}
		};
		ivRecyclerPhoto.setOnClickListener(lookPhotoClick);
		mFlRecyclerPhoto.setOnClickListener(lookPhotoClick);
	}

	/**
	 * 设置商家信息
	 */
	private void setShopInfo(final Shop shop) {
		// 商家logo
		ImageView ivShopLogo = (ImageView) mView.findViewById(R.id.shop_logo);
		// 商铺地址
		TextView tvAddress = (TextView) mView.findViewById(R.id.tv_address);
		// 商铺电话
		TextView tvPhone = (TextView) mView.findViewById(R.id.tv_phone);
		// 商店描述
		TextView tvShortDes = (TextView) mView.findViewById(R.id.tv_shop_des);
		// 人气
		TextView tvPopularity = (TextView) mView.findViewById(R.id.tv_popularity);
		// 回头客
		TextView tvRepeatcustomers = (TextView) mView.findViewById(R.id.tv_repeatcustomers);
		tvRepeatcustomers.setVisibility(View.GONE);
		// 营业时间
		TextView tvBusinessDate = (TextView) mView.findViewById(R.id.tv_business_date);
		// 电话
		RelativeLayout rlCallShop = (RelativeLayout) mView.findViewById(R.id.rl_callshop);

		if (null != shop) {
			// 商家logo
			Util.showImage(getMyActivity(), shop.getLogoUrl(), ivShopLogo);
			// 点击查看大图
			ivShopLogo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getMyActivity(), SingleBigPhotoActivity.class);
					intent.putExtra(SingleBigPhotoActivity.IMAGEURL, shop.getLogoUrl());
					startActivity(intent);

				}
			});

			// 商家名称
			if (!Util.isEmpty(shop.getShopName())) {
				mTvShopName.setText(shop.getShopName());
			}
			String address = "";
			if (!Util.isEmpty(shop.getCountry())) {
				address = address + shop.getCountry();
			}
			// 所在省份
			if (!Util.isEmpty(shop.getProvince())) {
				address = address + shop.getProvince();
			}
			// 所在城市
			if (!Util.isEmpty(shop.getCity())) {
				address = address + shop.getCity();
			}
			// 所在街道
			if (!Util.isEmpty(shop.getStreet())) {
				address = address + shop.getStreet();
			}

			if (!Util.isEmpty(address)) {
				tvAddress.setText(address);
			}
			// 人气
			tvPopularity.setText("人气：" + shop.getPopularity());
			// 回头客
			tvRepeatcustomers.setText("回头客：" + shop.getRepeatCustomers());
			// 电话
			if (!Util.isEmpty(shop.getMobileNbr())) {
				tvPhone.setText(shop.getTel());
			} else {
				tvPhone.setText(shop.getMobileNbr());
			}
			// 详情描述
			if (!Util.isEmpty(shop.getShortDes())) {
				tvShortDes.setText(shop.getShortDes());
			} else {
				tvShortDes.setText("暂无说明");
			}
			// 工行卡折扣
			if (!Util.isEmpty(shop.getOnlinePaymentDiscount())
					&& !Util.isEmpty(shop.getOnlinePaymentDiscountUpperLimit())) {
				showDiscount(Double.parseDouble(shop.getOnlinePaymentDiscount()),
						Double.parseDouble(shop.getOnlinePaymentDiscountUpperLimit()));
			}
			String startDate = getMyActivity().getResources().getString(R.string.start_day_date);
			String endDate = getMyActivity().getResources().getString(R.string.end_day_date);
			// 营业时间
			if (Util.isEmpty(mShop.getShopOpeningTime()) && Util.isEmpty(mShop.getShopClosedTime())) {
				tvBusinessDate.setText(getString(R.string.business_date) + getString(R.string.business_day));
			} else {
				if (mShop.getShopOpeningTime().equals(startDate) && mShop.getShopClosedTime().equals(endDate)) {
					tvBusinessDate.setText(getString(R.string.business_date) + getString(R.string.business_day));
				} else {
					tvBusinessDate.setText(getString(R.string.business_date) + mShop.getShopOpeningTime() + " - "
							+ mShop.getShopClosedTime());
				}
			}
			
			// 打电话
			rlCallShop.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					DialogUtils.showDialog(getMyActivity(), getStrings(R.string.dialog_title),
							getStrings(R.string.dialog_tel), getStrings(R.string.dialog_ok),
			   				getStrings(R.string.dialog_cancel), new DialogUtils().new OnResultListener() {

								@Override
								public void onOK() {
									if (!Util.isEmpty(shop.getTel())) {
										// if (Util.isPhone(getMyActivity(),
										// shop.getTel())) {
										Intent intent = new Intent(Intent.ACTION_CALL, Uri
												.parse(getString(R.string.tel) + shop.getTel()));
										startActivity(intent);
										// } else {
										// Util.getContentValidate(getMyActivity(),
										// getString(R.string.tel_error));
										// }
									} else if (!Util.isEmpty(shop.getMobileNbr())) {

										// if (Util.isPhone(getMyActivity(),
										// shop.getMobileNbr())) {   
										Intent intent = new Intent(Intent.ACTION_CALL, Uri 
												.parse(getString(R.string.tel) + shop.getMobileNbr()));
										startActivity(intent);
										// } else {
										// Util.getContentValidate(getMyActivity(),
										// getString(R.string.tel_error));
										// }

									} else {
										Util.getContentValidate(R.string.tel_null);
									}
									// 友盟统计
									MobclickAgent.onEvent(getMyActivity(), "myhome_fragment_phone");
								
								}

								@Override
								public void onCancel() {
									
								}
						
					});

				}
			});

		}
	}

	/**
	 * 初始化
	 * 
	 * @param v
	 */
	private void init(View v) {
		mUserCouponList = new ArrayList<BatchCoupon>();
		mShopCode = StringUtils.stripToEmpty(getMyActivity().getIntent().getStringExtra(SHOP_CODE));
		Log.d(TAG, "mShopCode=========" + mShopCode);
		if ("".equals(mShopCode) || mShopCode == null) {
			mShopCode = getMyActivity().getIntent().getDataString();
			Log.i(TAG, "" + mShopCode);
		}
		mDisCountTextView = (TextView) v.findViewById(R.id.tv_discount);
		mTvShopName = (TextView) v.findViewById(R.id.shop_name);
		mLyNodate = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mLyContent = (LinearLayout) v.findViewById(R.id.ly_content);
		mRlMessageShop = (RelativeLayout) v.findViewById(R.id.rl_messageshop);
		mRecyclerPhotoView = (RecyclerView) v.findViewById(R.id.recycler_photo);
		mLayoutManager = new LinearLayoutManager(getMyActivity().getApplicationContext());
		mLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
		mRecyclerPhotoView.setLayoutManager(mLayoutManager);
		mRecyclerPhotoView.setHasFixedSize(true);
		mFlRecyclerPhoto = (RelativeLayout) v.findViewById(R.id.fl_recycler_photo);
		mLvDetailCoupon = (MyListView) v.findViewById(R.id.lv_detail_coupon);
		mBtnFollow = (Button) getMyActivity().findViewById(R.id.btn_follow);
		ImageView ivReturn = (ImageView) getMyActivity().findViewById(R.id.iv_turn_in);
		BtnPay = (Button) v.findViewById(R.id.btn_pay);
		if (mShopCode.equals(FILM_SHOP_CODE)) { // 电影院
			BtnPay.setVisibility(View.GONE);
		} else {
			BtnPay.setVisibility(View.VISIBLE);
		}
		mBtnFollow.setOnClickListener(btnClickListener);
		mRlMessageShop.setOnClickListener(rlMessageListener);
		ivReturn.setOnClickListener(clickListerer);

		setData(0); // 正在加载数据
		getShopInfo(mView); // 获取商店详情
	}

	/**
	 * 给商家留言
	 */
	private OnClickListener rlMessageListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
				User user = DB.getObj(DB.Key.CUST_USER, User.class);
				if (null == user) {
					GetUserInfo.getUserInfo(getMyActivity());
				}
				// 友盟统计
				MobclickAgent.onEvent(getActivity(), "shopdetail_message");
				Messages message = new Messages();
				message.setShopCode(mShop.getShopCode());
				if (!Util.isEmpty(mShop.getShopName())) {
					message.setShopName(mShop.getShopName());
				} else {
					message.setShopName("商家");
				}
				Intent intent = new Intent(getActivity(), VipChatActivity.class);
				intent.putExtra(VipChatFragment.MSG_OBJ, (Serializable) message);
				startActivity(intent);
			} else {
				login(); // 先登录
			}
		}
	};

	/**
	 * 设置数据
	 * 
	 * @param type
	 *            有没有数据 1 是有数据 0 是没有数据
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
	 * 返回
	 */
	private OnClickListener clickListerer = new OnClickListener() {

		@Override
		public void onClick(View v) {
			getMyActivity().finish();
		}
	};

	/**
	 * 关注 ，取消关注
	 */
	private OnClickListener btnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.btn_follow) { // 关注
				if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
					final String focus = mBtnFollow.getText().toString();
					String focusCancel = getString(R.string.focus_cancele);
					if (focus.equals(focusCancel)) { // cancelFollowShop 取消关注
						// 友盟统计
						MobclickAgent.onEvent(getMyActivity(), "shopdetail_cancel");
						DialogUtils.showDialog(getMyActivity(), getStrings(R.string.dialog_title),
								getStrings(R.string.dialog_follow_conent), getStrings(R.string.dialog_ok),
								getStrings(R.string.dialog_cancel), new DialogUtils().new OnResultListener() {

									@Override
									public void onOK() {

										mBtnFollow.setEnabled(false);
										new CancelCardTask(getMyActivity(), CancelCardTask.INVALID_ITEM_POS,
												new CancelCardTask.Callback() {
													@Override
													public void getResult(int result, int itemPos) {
														mBtnFollow.setEnabled(true);
														if (ErrorCode.SUCC == result) {
															DB.saveBoolean(CustConst.Key.UPP_IS_FOCUS,true);
															setBtnFollow(NO_FOLLEWED);
														} else {
															setBtnFollow(IS_FOLLEWED);
														}
													}
												}).execute(mShopCode);
									}

									@Override
									public void onCancel() {
										Log.d(TAG, "取消");
									}
								});

					} else { // 获得关注
						// 友盟统计
						MobclickAgent.onEvent(getMyActivity(), "shopdetail_focus");
						new FocusCardListTask(getMyActivity(), new FocusCardListTask.Callback() {
							@Override
							public void getResult(JSONObject result) {
								mBtnFollow.setEnabled(true);
								if (result == null) {
									setBtnFollow(NO_FOLLEWED);
								} else {
									int code = 0;
									try {
										code = Integer.parseInt(result.get("code").toString());
									} catch (Exception e) {

									}
									if (code == ErrorCode.SUCC) {
										DB.saveBoolean(CustConst.Key.UPP_IS_FOCUS,true);
										setBtnFollow(IS_FOLLEWED);
									} else {
										setBtnFollow(NO_FOLLEWED);
									}
								}
							}
						}).execute(mShopCode);
					}
				} else {
					login(); // 登陆
				}
			}
		}
	};

	/**
	 * 设置关注的背景
	 * 
	 * @param status
	 */
	private void setBtnFollow(int status) {
		if (status == IS_FOLLEWED) { // 关注
			mBtnFollow.setText(getString(R.string.focus_cancele));
		} else {
			mBtnFollow.setText(getString(R.string.focus_imm));
		}
	}

	@OnClick({ R.id.iv_turn_in, R.id.btn_pay })
	private void click(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.iv_turn_in:
			getMyActivity().finish();
			break;
		case R.id.btn_pay:
			Log.d(TAG, "loginflag=" + DB.getBoolean(DB.Key.CUST_LOGIN));
			if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
				if (null == mShop) {
					getShopInfo(mView); // 获取商店详情
				} else {
					intent = new Intent(getMyActivity().getApplicationContext(), ShopPayBillActivity.class);
					intent.putExtra(ShopPayBillFragment.PAY_OBJ, (Serializable) mShop);
					getMyActivity().startActivity(intent);
					// 友盟统计
					MobclickAgent.onEvent(getMyActivity(), "shopdetail_pay");
				}
			} else {
				// TODO
				login();
			}
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQ_CODE:
			if (resultCode == RESULT_SUCC) {
				boolean payFlag = data.getBooleanExtra(PAY_STATUS, false);
				if (payFlag) {
					String userCouponCode = data.getStringExtra(USER_COUPON_CODE);
					List<BatchCoupon> userCouponList = mShopDetail.getCouponList().getUserCoupon();
					if (null != userCouponList && userCouponList.size() > 0) {
						for (int i = 0; i < userCouponList.size(); i++) {
							BatchCoupon coupon = userCouponList.get(i);
							if (coupon.getUserCouponCode().equals(userCouponCode)) {
								userCouponList.remove(coupon);
							}
						}
						mCouponAdapter.setItems(userCouponList, mShopDetail.getCouponList().getShopCoupon(), 1);
					}
				}
			} else if (resultCode == LOGIN_SUCC) {
				// TODO
				getShopInfo(mView); // 获取商店详情
				Log.d(TAG, "登陆成功>>>>>>>>>>>>>>>>>>> = " + ShopDetailFragment.class.getSimpleName());
			}
			break;

		default:
			break;
		}
	}

	private void initViewPager(View v, String[] ShopPics) {
		List<Image> imageList = new ArrayList<Image>();
		mImagesList = new ArrayList<ImageView>();
		for (int i = 0; i < ShopPics.length; i++) {
			final ImageView imageView = new ImageView(getMyActivity());
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			if (Util.isEmpty(ShopPics[i])) {
				imageView.setBackgroundResource(R.drawable.no_shopdetail_url);
			} else {
				Util.showShopDetailImage(getMyActivity(), ShopPics[i], imageView);
			}
			// 查看大图的图片集合
			Image image = new Image();
			image.setImageUrl(ShopPics[i]);
			imageList.add(image);
			// viewPage的图片集合
			mImagesList.add(imageView);
		}

		mViewPager = (ViewPager) v.findViewById(R.id.viewPager);
		mViewPager.setBackgroundColor(getMyActivity().getResources().getColor(R.color.gray_transparent));
		CouponDetailSrollAdapter adapter = new CouponDetailSrollAdapter(getMyActivity(), mImagesList, imageList,
				ShopPics, mCouponHandler, mSrollRunnable);
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(300);
		// 先关闭线程再启动线程
		if (mCouponHandler != null && mSrollRunnable != null) {
			mCouponHandler.removeCallbacks(mSrollRunnable);
		}
		mCouponHandler.postDelayed(mSrollRunnable, 1000);
	}

	/**
	 * 启动一个线程
	 */
	private Runnable mSrollRunnable = new Runnable() {
		@Override
		public void run() {
			if (mSrollPics == null || mSrollPics.length == 0) { return;// TODO
			}
			/*** 更新界面 **/
			mCouponHandler.obtainMessage().sendToTarget();
			mCouponHandler.postDelayed(this, 4000);
		}
	};

	private Handler mCouponHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 设置当前页面
			mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
		}
	};

	/**
	 * 跳转登
	 */
	private void login() {
		Intent intent = new Intent(getMyActivity(), LoginActivity.class);
		intent.putExtra(LoginTask.ALL_LOGIN, Const.Login.SHOP_DETAIL);
		getMyActivity().startActivityForResult(intent, REQ_CODE);
	}

	/**
	 * 友盟统计
	 */
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainScreen");
		TheadDBhelper.closeRunnable(mCouponHandler, mSrollRunnable); // 关闭线程
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MainScreen"); // 统计页面
		TheadDBhelper.runRunnable(mCouponHandler, mSrollRunnable); // 启动线程

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		TheadDBhelper.closeRunnable(mCouponHandler, mSrollRunnable); // 关闭线程
	}

	private String getStrings(int id) {
		return getMyActivity().getResources().getString(id);
	}

}
