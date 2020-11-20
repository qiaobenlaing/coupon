package com.huift.hfq.shop.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
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
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.Activitys;
import com.huift.hfq.base.pojo.BatchCoupon;
import com.huift.hfq.base.pojo.Image;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.base.pojo.ShopDecoration;
import com.huift.hfq.base.pojo.ShopDetail;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.view.MyListView;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopApplication;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.BigShopLogoActivity;
import com.huift.hfq.shop.activity.MyPhotoAlbumActivity;
import com.huift.hfq.shop.adapter.ActivityListAdapter;
import com.huift.hfq.shop.adapter.CouponPhotoListAdapter;
import com.huift.hfq.shop.adapter.ImageShopAdapter;
import com.huift.hfq.shop.adapter.ShopProductPhotoAdapter;
import com.huift.hfq.shop.model.MyShopInfoTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 店铺形象
 * @author qian.zhou
 */
public class MyImageShopFragment extends Fragment{
	public final static String SHOP_OBJ = "shop";
	/** 优惠劵 listview显示数据 */
	private MyListView mLvShopPhoto;
	/** 活动详情列表展示*/
	private MyListView mLvActivity;
	/** 滚动图片 */
	private ViewPager mViewPager;
	/** 小圆点的Layout父容器 */
	private List<ImageView> mImagesList;
	/** viewPage的图片*/
	private String[] mSrollPics;
	/** 展示商家产品图片*/
	private RecyclerView mCouponRecyclerView;
	/** 展示商家产品图片的适配器*/
	private Adapter<ShopProductPhotoAdapter.ViewHolder> mPhotosAdapter;
	/** 线性布局管理器*/
	private LinearLayoutManager mLayoutManager;
	/** 取值 */
	private Shop mShop;
	/** 全局视图*/
	private View mView;
	/** 人气*/
	private TextView mTvPopularity;
	/** 是否收缩*/
	private ImageView mIvArrow;
	/** 记录点击次数*/
	private int mClickNum;
	/**优惠券列表页*/
	private RelativeLayout mRyArrowCoupon;
	/** 判断子相册里面的图片是否修改了*/
	private boolean mUppFlag = false;
	/** 正在加载数据*/
	private LinearLayout mLyNodate;
	/** 正在加载的内容*/
	private LinearLayout mLyContent;
	/** 商家详情 */
	private ShopDetail mShopDetail;
	/** 将JSONobject转换成对象 */
	private Type mJsonType = new TypeToken<ShopDetail>() {}.getType();
	/** viewPager滑动界面的临界值*/
	private int THRESHOLD = 300;
	/** 定义全局变量*/
	private ShopApplication mShopApplication;
	/** 得到是否入驻的标示*/
	private boolean mSettledflag;
	/** 相册*/
	private RelativeLayout mRyPhoto;

	/**
	 * 需要传递参数时有利于解耦
	 * @return MyShopInfoFragment
	 */
	public static MyImageShopFragment newInstance() {
		Bundle args = new Bundle();
		MyImageShopFragment fragment = new MyImageShopFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_imageshop, container, false);
		ViewUtils.inject(this, view);
		Util.addHomeActivity(getMyActivity());
		init(view);
		return view;
	}

	// 初始化数据
	private void init(View view) {
		mClickNum = 0;
		mShopApplication =  (ShopApplication) getActivity().getApplication();
		mSettledflag = mShopApplication.getSettledflag();
		//取值
		Intent intent = getMyActivity().getIntent();
		mShop = (Shop) intent.getSerializableExtra(SHOP_OBJ);
		initView(view);//初始化视图
		initData(view);//初始化数据
		setView(0); // 正在加载数据
		getCouponList(view);// 商店详情
	}

	/**
	 * 初始化视图控件
	 */
	public void initView(View view){
		//悬浮的返回按钮和装修按钮
		LinearLayout ivBack = (LinearLayout) getActivity().findViewById(R.id.layout_turn_in);//返回
		TextView tvDecoration = (TextView) getActivity().findViewById(R.id.btn_follow);//装修
		ivBack.setOnClickListener(clickListener);
		tvDecoration.setOnClickListener(clickListener);
		//我的优惠劵列表
		mLvShopPhoto = (MyListView) view.findViewById(R.id.lv_coupon);
		//我的活动列表
		mLvActivity = (MyListView) view.findViewById(R.id.lv_activity);
		mLvActivity.setVisibility(View.GONE);
		//相册
		mCouponRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_photo_view);
		mTvPopularity = (TextView) view.findViewById(R.id.tv_popularity);//人气
		mIvArrow = (ImageView) view.findViewById(R.id.iv_guide_arrow);
		//没有数据
		mLyNodate = (LinearLayout) view.findViewById(R.id.ly_nodate);
		//有数据
		mLyContent = (LinearLayout) view.findViewById(R.id.ly_content);
		mViewPager = (ViewPager) view.findViewById(R.id.vp_viewPager);
		//优惠劵一整行
		mRyArrowCoupon = (RelativeLayout) view.findViewById(R.id.ry_arrow_coupon);
		mRyPhoto = (RelativeLayout) view.findViewById(R.id.ry_shop_decoration);
		mRyArrowCoupon.setOnClickListener(arrowListener);
		mIvArrow.setBackgroundResource(mClickNum % 2 == 0 ? R.drawable.upc_arrow : R.drawable.downc_arrow);
	}

	/**
	 * 悬浮的返回按钮和装修按钮
	 */
	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.layout_turn_in:
					getMyActivity().finish();
					break;
				case R.id.btn_follow:
					if (mSettledflag) {
						startActivity(new Intent(getMyActivity(), MyPhotoAlbumActivity.class));
					} else {
						mShopApplication.getDateInfo(getActivity());
					}
					break;
				default:
					break;
			}
		}
	};

	/**
	 * 初始化数据
	 */
	public void initData(View view){
		ImageView ivshowImage = (ImageView) view.findViewById(R.id.shop_logo);// 商铺主图
		TextView tvStreet = (TextView) view.findViewById(R.id.address);// 本商家地址街道
		TextView tvTel = (TextView) view.findViewById(R.id.phone);// 电话
		TextView tvName = (TextView) view.findViewById(R.id.shop_name);// 店铺名称
		TextView tvShopDes = (TextView) view.findViewById(R.id.tv_shop_des);//说明
		mLayoutManager = new LinearLayoutManager(getMyActivity().getApplicationContext());
		mLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
		mCouponRecyclerView.setLayoutManager(mLayoutManager);
		mCouponRecyclerView.setHasFixedSize(true);
		if (null != mShop) {
			// 赋值
			Util.showImage(getActivity(), mShop.getLogoUrl(), ivshowImage);
			ivshowImage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getMyActivity(), BigShopLogoActivity.class);
					intent.putExtra(BigShopLogoActivity.IMAGEURL, mShop.getLogoUrl());
					startActivity(intent);
				}
			});
			if (!Util.isEmpty(mShop.getProvince()) && !Util.isEmpty(mShop.getCity()) && !Util.isEmpty(mShop.getStreet())) {
				tvStreet.setText(mShop.getProvince() + mShop.getCity() + mShop.getStreet());
			} if (!Util.isEmpty(mShop.getShopName())) {
				tvName.setText(mShop.getShopName());
			} if (!Util.isEmpty(mShop.getShortDes())) {
				tvShopDes.setText(mShop.getShortDes());
			}
			//判断电话号码
			if (mShop.getTel() == null) {
				if (!Util.isEmpty(mShop.getMobileNbr())) {
					tvTel.setText(mShop.getMobileNbr());
				}
			} else {
				tvTel.setText(mShop.getTel());
			}
		}
	}

	/**
	 * 设置数据
	 * @param type 有没有数据 1 是有数据 0 是没有数据
	 */
	private void setView (int type) {
		if (type == 1) {
			mLyNodate.setVisibility(View.GONE);
			mLyContent.setVisibility(View.VISIBLE);
		} else {
			mLyNodate.setVisibility(View.VISIBLE);
			mLyContent.setVisibility(View.GONE);
		}
	}

	/**
	 * 收缩优惠劵列表
	 */
	OnClickListener arrowListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mClickNum++;
			if (mClickNum % 2 == 0) {
				mIvArrow.setBackgroundResource(0);
				mIvArrow.setImageResource(R.drawable.upc_arrow);
				mLvShopPhoto.setVisibility(View.VISIBLE);
			} else {
				mIvArrow.setBackgroundResource(0);
				mIvArrow.setImageResource(R.drawable.downc_arrow);
				mLvShopPhoto.setVisibility(View.GONE);
			}
		}
	};

	/**
	 * 保证activity不为空
	 * @return activity
	 */
	private Activity getMyActivity(){
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	// 获得商店详情
	public void getCouponList(final View v) {
		new MyShopInfoTask(getMyActivity(), new MyShopInfoTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (result == null) {
					return;
				} else {
					setView(1); // 有数据
					//产品相册
					getProductAlbum(result);
					// 商店详情
					getEnvironCarousel(result);
					//回头客
					JSONObject jsonObject = (JSONObject) result.get("shopInfo");
					String popularity = (String) jsonObject.get("popularity");//人气
					mTvPopularity.setText("人气 :" + popularity);
					//优惠劵
					getCoupon(result);
					// 商店活动
					setShopAct(v, mShopDetail.getActList());
				}
			}
		}).execute();
	}

	/**
	 * 查询优惠券
	 */
	public void getCoupon(JSONObject result){
		if (result == null) {
			return;
		}
		List<BatchCoupon> CouponsList = new  ArrayList<BatchCoupon>();
		JSONArray array = (JSONArray) result.get("shopCoupon");
		if (array.size() > 0) {
			//设置优惠券视图
			setCouponView(true);
			for (int k = 0; k < array.size(); k++) {
				JSONObject couponObject = (JSONObject) array.get(k);
				BatchCoupon batchCoupon = Util.json2Obj(couponObject.toString(), BatchCoupon.class);
				CouponsList.add(batchCoupon);
			}
			CouponPhotoListAdapter adapter = new CouponPhotoListAdapter(getActivity(), CouponsList);
			mLvShopPhoto.setAdapter(adapter);
		} else {
			//设置优惠券视图
			setCouponView(false);
		}
	}

	/**
	 * 产品相册
	 */
	public void getProductAlbum(JSONObject result){
		if (result == null) {
			setPhoto(false);
			return;
		}
		List<Image> decorationList = new ArrayList<Image>();
		JSONArray jsonArray = (JSONArray) result.get("shopProductPhoto");
		if (jsonArray.size() > 0) {
			mCouponRecyclerView.setVisibility(View.VISIBLE);
			setPhoto(true);
			for (int m = 0; m < jsonArray.size(); m++) {
				JSONObject decorationObject = (JSONObject) jsonArray.get(m);
				ShopDecoration decoration = Util.json2Obj(decorationObject.toString(), ShopDecoration.class);
				Image image = new Image();
				image.setImageUrl(decoration.getUrl());
				decorationList.add(image);
			}
			mPhotosAdapter = new ShopProductPhotoAdapter(getActivity(), decorationList);
			mCouponRecyclerView.setAdapter(mPhotosAdapter);
		} else {
			setPhoto(false);
			mCouponRecyclerView.setVisibility(View.GONE);
		}
	}

	/**
	 * 获得商店的环境滚屏图片
	 */
	public void getEnvironCarousel(JSONObject result){
		if (result == null) {
			return;
		}
		mShopDetail = new Gson().fromJson(result.toJSONString(), mJsonType);
		if (null == mShopDetail || mShopDetail.getShopDecoration().size() == 0 || "[]".equals(mShopDetail.getShopDecoration().toString())) {
			//默认给一张图
			mSrollPics = new String[1];
			ProductPhotoPager(mView, mSrollPics);
		} else {
			mSrollPics = new String[mShopDetail.getShopDecoration().size()];
			// 滚动图片
			for (int i = 0; i < mShopDetail.getShopDecoration().size(); i++) {
				ShopDecoration decoration = mShopDetail.getShopDecoration().get(i);
				mSrollPics[i] = decoration.getImgUrl();
			}
			ProductPhotoPager(mView, mSrollPics);
		}
	}

	/**
	 * 商店活动
	 * @param activityList
	 * 活动列表
	 */
	private void setShopAct(View view, List<Activitys> activityList) {
		List<Activitys> actList = new ArrayList<Activitys>();
		RelativeLayout rlShopAct = (RelativeLayout) view.findViewById(R.id.ry_activity); // 列表
		if (null != activityList && activityList.size() > 0) {
			rlShopAct.setVisibility(View.GONE);
			for (int i = 0; i < activityList.size(); i++) {
				Activitys act = activityList.get(i);
				act.setActNumber(getMyActivity().getResources().getString(R.string.shop_act_num) + (i + 1) + " "
						+ getString(R.string.shop_act_num_end));
				actList.add(act);
			}
			ActivityListAdapter activityListAdapter = new ActivityListAdapter(getMyActivity(), activityList);
			mLvActivity.setAdapter(activityListAdapter);
		} else {
			rlShopAct.setVisibility(View.GONE);
			mLvActivity.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置优惠券视图
	 */
	public void setCouponView(boolean couponview){
		if (couponview) {
			mRyArrowCoupon.setVisibility(View.VISIBLE);
			mLvShopPhoto.setVisibility(View.VISIBLE);
		} else {
			mRyArrowCoupon.setVisibility(View.GONE);
			mLvShopPhoto.setVisibility(View.GONE);
		}
	}


	/**
	 * 轮播商家产品图片
	 * @param v
	 * @param ShopPics 图片路径的集合
	 */
	private void ProductPhotoPager(View v, String[] ShopPics) {
		List<Image> imageList = new ArrayList<Image>();
		mImagesList = new ArrayList<ImageView>();
		for (int i = 0; i < ShopPics.length; i++) {
			final ImageView imageView = new ImageView(getMyActivity());
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			Util.showImage(getMyActivity(), ShopPics[i], imageView);
			Image image = new Image();
			image.setImageUrl(ShopPics[i]);
			imageList.add(image);
			mImagesList.add(imageView);
		}
		ImageShopAdapter adapter = new ImageShopAdapter(getMyActivity(), mImagesList, imageList, ShopPics, mCouponHandler,mSrollRunnable);
		mViewPager.setAdapter(adapter);
		//切换页面的临界值
		mViewPager.setCurrentItem(THRESHOLD);
		// 先关闭线程再启动线程
		if (mCouponHandler != null && mSrollRunnable != null) {
			mCouponHandler.removeCallbacks(mSrollRunnable);
		}
		//每1秒执行一次mSrollRunnable
		mCouponHandler.postDelayed(mSrollRunnable, 1000);
	}

	/**
	 * 设置相册隐藏或显示
	 */
	public void setPhoto(boolean isphoto){
		if (isphoto) {
			mRyPhoto.setVisibility(View.VISIBLE);
		} else {
			mRyPhoto.setVisibility(View.GONE);
		}
	}

	/**
	 * 启动一个线程
	 */
	private Runnable mSrollRunnable = new Runnable() {
		@Override
		public void run() {
			if (mSrollPics == null || mSrollPics.length == 0) {
				return;// TODO
			}
			/*** 更新界面 **/
			mCouponHandler.obtainMessage().sendToTarget();
			//每4秒执行一次mCouponHandler
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
	 * 点击事件
	 */
	@OnClick(R.id.ry_shop_decoration)
	private void ivClickTo(View v) {
		startActivity(new Intent(getMyActivity(), MyPhotoAlbumActivity.class));
	}

	@Override
	public void onResume() {
		super.onResume();
		mUppFlag = DB.getBoolean(ShopConst.Key.UPP_ALBUM_PHOTO);
		if (mUppFlag) {
			DB.saveBoolean(ShopConst.Key.UPP_ALBUM_PHOTO, false);
			getCouponList(mView);
		}
	}
}
