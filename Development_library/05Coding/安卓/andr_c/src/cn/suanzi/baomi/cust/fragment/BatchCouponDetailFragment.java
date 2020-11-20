// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.12.14
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.cust.fragment;
import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.BatchCoupon;
import cn.suanzi.baomi.base.pojo.BatchCouponDetail;
import cn.suanzi.baomi.base.pojo.Citys;
import cn.suanzi.baomi.base.pojo.Image;
import cn.suanzi.baomi.base.pojo.Shop;
import cn.suanzi.baomi.base.pojo.ShopDecoration;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.ListViewHeight;
import cn.suanzi.baomi.base.utils.ShareCouponUtil;
import cn.suanzi.baomi.base.utils.TimeUtils;
import cn.suanzi.baomi.base.utils.ViewSolveUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.adapter.BatchDifferShopAdapter;
import cn.suanzi.baomi.cust.adapter.CouponDetailSrollAdapter;
import cn.suanzi.baomi.cust.adapter.UserBatchCouponAdapter;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.model.GetBatchCouponInfoTask;
import cn.suanzi.baomi.cust.util.SkipActivityUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 同一批次优惠券详情
 * @author yanfang.li
 */
public class BatchCouponDetailFragment extends Fragment {

	private final static String TAG = BatchCouponDetailFragment.class.getSimpleName();
	/** 同一批次优惠券的code*/
	public final static String BATCH_COUPON_CODE = "batchCouponCode";
	/** 同一批次优惠券的类型*/
	public final static String BATCH_COUPON_TYPE = "batchCouponType";
	/** 批次会*/
	private String mBatchCouponCode;
	/** 头部视图*/
	private View mHeadView;
	/** 底部视图*/
	private View mBottomView;
	/** 我得到优惠券列表*/
	private ListView mLvBatch;
	/** 分享*/
	private ImageView mIvShare;
	/** 图片 */
	private ViewPager mViewPager;
	/** 加载*/
	private LinearLayout mLyView;
	private ImageView mIvView ;
	private ProgressBar mProgView;
	/** 滚动图片的集合*/
	private String[] mSrollPics;
	/** 滚屏的集合*/
	private List<ImageView> mImagesList;
	/** 滚屏图片 当前显示页,从1开始  */
	private int mCurPosition = 1;
	/** 第一次进来*/
	private boolean mFisrtFlag;
	/** 判断api是否调用成功 下拉 */
	private boolean mFlagUpData = false;
	/** 顾客拥有的优惠券列表*/
	private UserBatchCouponAdapter mBatchCouponAdapter;
	/** 下拉加载*/
	private SwipeRefreshLayout mSwipeRefresh;
	/** 当前城市*/
	private String mCity;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_batchcoupondetail, container, false);  
		ViewUtils.inject(this, v);
		findView(v);
		init();
		return v;
	}
	
	private Activity getMyActivity() {
		Activity act = getActivity(); 
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}
	
	public static BatchCouponDetailFragment newInstance() {
		Bundle args = new Bundle();
		BatchCouponDetailFragment fragment = new BatchCouponDetailFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	/**
	 * 获取空间
	 */
	private void findView(View view) {
		mHeadView = View.inflate(getMyActivity().getApplicationContext(), R.layout.head_batchcoupon, null);
		mBottomView = View.inflate(getMyActivity().getApplicationContext(), R.layout.bottom_batchcoupon, null);
		mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.index_swipe_refresh);
		mLvBatch = (ListView) view.findViewById(R.id.lv_batch_listView);
		mIvShare = (ImageView) view.findViewById(R.id.iv_share_all);
		mLyView = (LinearLayout) view.findViewById(R.id.ly_nodate);
		mIvView = (ImageView) view.findViewById(R.id.iv_nodata);
		mProgView = (ProgressBar) view.findViewById(R.id.prog_nodata);
		mIvShare.setVisibility(View.GONE);
		TextView tvCouponName = (TextView) view.findViewById(R.id.tv_mid_content);
		setCouponName(tvCouponName); // 设置优惠券名称 
		// 下拉
		mSwipeRefresh.setColorSchemeResources(R.color.red);
		mSwipeRefresh.setOnRefreshListener(refreshListener);
	}
	
	/**
	 * 下拉加载
	 */
	private OnRefreshListener refreshListener = new OnRefreshListener() {

		@Override
		public void onRefresh() {

			if (mFlagUpData) {
				mFlagUpData = false;
				new Handler().postDelayed(
					new Runnable() {
						public void run() {
							getBatchCouponInfo();
						}
					}, 5000);
			}
		}
	};
	
	/**
	 * 设置优惠券的名字
	 * @param tvCouponName 优惠券名字的空间
	 */
	private void setCouponName (TextView tvCouponName) {
		String couponType = getMyActivity().getIntent().getStringExtra(BATCH_COUPON_TYPE);
		if (!Util.isEmpty(couponType)) {
			int type = ViewSolveUtils.getInputNum(couponType);
			switch (type) {
			case CustConst.Coupon.INT_DEDUCT: // 抵扣券
				tvCouponName.setText(Util.getString(R.string.detail_deduct));
				break;
			case CustConst.Coupon.INT_DISCOUNT: // 折扣券
				tvCouponName.setText(Util.getString(R.string.detail_discount));
				break;
			case CustConst.Coupon.INT_REAL_COUPON: // 实物券
				tvCouponName.setText(Util.getString(R.string.detail_real_coupon));
				break;
			case CustConst.Coupon.INT_EXPERIENCE: // 体验券
				tvCouponName.setText(Util.getString(R.string.detail_experience));
				break;
			case CustConst.Coupon.INT_N_BUY: // N元购
				tvCouponName.setText(Util.getString(R.string.detail_nbuy));
				break;
			case CustConst.Coupon.INT_EXCHANGE_VOUCHER: // 兑换券
				tvCouponName.setText(Util.getString(R.string.detail_ex_voucher));
				break;
			case CustConst.Coupon.INT_VOUCHER: // 代金券
				tvCouponName.setText(Util.getString(R.string.detail_voucher));
				break;

			default:
				break;
			}
		}
	}
	
	/**
	 * 初始化方法
	 */
	private void init() {
		mBatchCouponCode = getMyActivity().getIntent().getStringExtra(BATCH_COUPON_CODE);
		mFisrtFlag = true;
		mLvBatch.addHeaderView(mHeadView);
		mLvBatch.addFooterView(mBottomView);
		// 添加适配器
		if (mBatchCouponAdapter == null) {
			mBatchCouponAdapter = new UserBatchCouponAdapter(getMyActivity(), null);
			mLvBatch.setAdapter(mBatchCouponAdapter);
		}
		getBatchCouponInfo();
	}
	
	/**
	 * ViewPager
	 * 
	 * @param v
	 * @param ShopPics
	 *            图片数组
	 */
	private void initViewPager(String[] ShopPics) {
		List<Image> imageList = new ArrayList<Image>();
		mImagesList = new ArrayList<ImageView>();
		if (ShopPics.length != 2) {
			for (int i = 0; i < ShopPics.length; i++) {
				final ImageView imageView = new ImageView(getMyActivity());
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				Util.showBannnerImage(getMyActivity(), ShopPics[i], imageView);
				// 查看大图的图片集合
				Image image = new Image();
				image.setImageUrl(ShopPics[i]);
				imageList.add(image);
				mImagesList.add(imageView);
			}
		} else {
			for (int i = 0; i < ShopPics.length + 2; i++) {
				final ImageView imageView = new ImageView(getMyActivity());
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				Image image = new Image();
				if (i % 2 == 0) {
					Util.showBannnerImage(getMyActivity(), ShopPics[0], imageView);
					image.setImageUrl(ShopPics[0]);
				} else {
					Util.showBannnerImage(getMyActivity(), ShopPics[1], imageView);
					image.setImageUrl(ShopPics[1]);
				}
				// 查看大图的图片集合
				if (i < 2) {
					imageList.add(image);
				}
				mImagesList.add(imageView);
			}
		}

		mViewPager = (ViewPager) mHeadView.findViewById(R.id.viewPager);
		mViewPager.setBackgroundColor(getMyActivity().getResources().getColor(R.color.gray_transparent));
		CouponDetailSrollAdapter adapter = new CouponDetailSrollAdapter(getMyActivity(), mImagesList, imageList,ShopPics,mCouponHandler,mSrollRunnable);
		mViewPager.setAdapter(adapter);
		// 图片从第一张开始 
		mViewPager.setCurrentItem(mCurPosition * 100);
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
			if (mSrollPics == null || mSrollPics.length == 0) { 
				return;
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
	 * 同一批次优惠券
	 */
	private void getBatchCouponInfo () {
		Log.d(TAG, "mBatchCouponCode >>> " + mBatchCouponCode);
		// 获取值
		SharedPreferences preferences = getMyActivity().getSharedPreferences(CustConst.Key.CITY_OBJ,Context.MODE_PRIVATE);
		String cityName = preferences.getString(CustConst.Key.CITY_NAME, null);
		Log.d(TAG, "取出DB的定位城市为 ：：：：：：： " + cityName);
		String longitude = "";
		String latitude = "";
		if (Util.isEmpty(cityName)) {
			Citys citys = DB.getObj(HomeFragment.CITYS, Citys.class);
			longitude = String.valueOf(citys.getLongitude());
			latitude = String.valueOf(citys.getLatitude());
		} else {
			longitude = preferences.getString(CustConst.Key.CITY_LONG, null);
			latitude = preferences.getString(CustConst.Key.CITY_LAT, null);
		}
		if (mFisrtFlag) {
			mFisrtFlag = false;
			ViewSolveUtils.setNoData(mLvBatch, mLyView, mIvView, mProgView, CustConst.DATA.LOADIMG);
		}
		new GetBatchCouponInfoTask(getMyActivity(), new GetBatchCouponInfoTask.Callback() {
			
			@Override
			public void getResult(JSONObject result) { 
				mSwipeRefresh.setRefreshing(false); // 上拉加载完成
				mFlagUpData = true;
				if (null == result) {
					ViewSolveUtils.setNoData(mLvBatch, mLyView, mIvView, mProgView, CustConst.DATA.NO_DATA);
				} else {
					ViewSolveUtils.setNoData(mLvBatch, mLyView, mIvView, mProgView, CustConst.DATA.HAVE_DATA);
					Log.d(TAG, "getBatchCouponInfo >>> " + result.toString());
					BatchCouponDetail batchCouponDetail = new Gson().fromJson(result.toString(),  new TypeToken<BatchCouponDetail>() {}.getType());
					setBatchCoupon(batchCouponDetail);
				}
			}
		}).execute(mBatchCouponCode,longitude,latitude); 
	}
	
	/**
	 * 给控件赋值
	 * @param batchCouponDetail 对象
	 */
	private void setBatchCoupon(BatchCouponDetail batchCouponDetail) {
		if (null == batchCouponDetail) {
			return;
		}
		// 头部商家信息
		if (null != batchCouponDetail.getShopInfo()) {
			try {
				setHeadContent(batchCouponDetail); 
			} catch (Exception e) {
				Log.e(TAG, "批次优惠券详细信息  设置头部出错 : " + e.getMessage());
			}
		}
		// 优惠券信息
		if (null != batchCouponDetail.getBatchCouponInfo()) {
			try {
				setCouponContent(batchCouponDetail);
			} catch (Exception e) {
				Log.e(TAG, "批次优惠券详细信息  设置优惠券出错: " + e.getMessage());
			}
		}
		// 异业商家
		ListView lvDifferShop = (ListView) mBottomView.findViewById(R.id.lv_differ_shop);
		TextView tvDifferShop = (TextView) mBottomView.findViewById(R.id.tv_differ_shop);
		TextView tvLine = (TextView) mBottomView.findViewById(R.id.tv_line);
		List<Shop> shopList = batchCouponDetail.getRecomShop();
		if (null != shopList && shopList.size() > 0) {
			BatchDifferShopAdapter differShopAdapter = new BatchDifferShopAdapter(getMyActivity(), shopList);
			lvDifferShop.setAdapter(differShopAdapter);
			ListViewHeight.setListViewHeightBasedOnChildren(lvDifferShop); // 设置集合的高度
			tvDifferShop.setVisibility(View.VISIBLE);
			tvLine.setVisibility(View.VISIBLE);
		} else {
			tvLine.setVisibility(View.GONE);
			tvDifferShop.setVisibility(View.GONE);
		}
	}
	
	
	/**
	 * 设置优惠券内容
	 * @param batchCouponDetail 对象
	 */
	private void setCouponContent (BatchCouponDetail batchCouponDetail) {
		// 优惠券对象
		final BatchCoupon batchCoupon = batchCouponDetail.getBatchCouponInfo();
		TextView tvCouponUseDate = (TextView) mBottomView.findViewById(R.id.tv_couponuse_date); // 使用日期
		TextView tvCouponDayTime = (TextView) mBottomView.findViewById(R.id.tv_couponday_time); // 每天使用时间
		TextView tvCouponRemark = (TextView) mBottomView.findViewById(R.id.tv_coupon_remark); // 优惠券使用说明
		RelativeLayout rlFunction = (RelativeLayout) mBottomView.findViewById(R.id.rl_function); // 优惠券功能
		TextView tvFunction = (TextView) mBottomView.findViewById(R.id.tv_function); // 优惠券功能
		TextView tvBatchNber = (TextView) mHeadView.findViewById(R.id.tv_batch_nber); // 优惠券批次
		TextView tvDeductAmount = (TextView) mHeadView.findViewById(R.id.tv_deduct_amount); // 优惠券金额计算
		// 说明
		tvCouponRemark.setText(Util.isEmpty(batchCoupon.getRemark()) ? Util.getString(R.string.no_remark) : batchCoupon.getRemark());
		// 是否分享
		setShareCoupon(batchCoupon);
		// 有效时间
		String useTime = TimeUtils.getTime(batchCoupon); 
		useTime = Util.isEmpty(useTime) ? getString(R.string.no_limit_time) : useTime;
		tvCouponUseDate.setText(useTime);
		// 每天使用时间
		String useDate = TimeUtils.getDate(batchCoupon);
		useDate = Util.isEmpty(useTime) ? getString(R.string.day_use) : useDate;
		tvCouponDayTime.setText(useDate);
		// 优惠券批次
		tvBatchNber.setText(Util.getString(R.string.batch_code)+batchCoupon.getBatchNbr());
		// 优惠券功能
		if (Util.isEmpty(batchCoupon.getFunction())) {
			rlFunction.setVisibility(View.GONE);
		} else {
			rlFunction.setVisibility(View.VISIBLE);
			tvFunction.setText(batchCoupon.getFunction());
		}
		// 优惠券面值
		setCouponMoney(batchCoupon, tvDeductAmount);
		// 我拥有的优惠券
		List<BatchCoupon> batchList = batchCouponDetail.getUserCouponList();
		if (null != batchList && batchList.size() > 0) {
			if (mBatchCouponAdapter == null) {
				mBatchCouponAdapter = new UserBatchCouponAdapter(getMyActivity(), batchList);
				mLvBatch.setAdapter(mBatchCouponAdapter);
			} else {
				mBatchCouponAdapter.setItems(batchList);
			}
		}
		// 分享
		mIvShare.setOnClickListener(new View.OnClickListener() {
			
			@Override 
			public void onClick(View v) {
				batchCoupon.setCity(Util.isEmpty(mCity) ? "" : mCity);
				ShareCouponUtil.shareCoupon(getMyActivity(), batchCoupon); // 分享
			}
		});
	}
	
	/**
	 * 设置是否分享优惠券
	 */
	private void setShareCoupon (BatchCoupon coupon) {
		if (coupon.getCouponType().equals(CustConst.Coupon.DEDUCT)
	            || coupon.getCouponType().equals(CustConst.Coupon.DISCOUNT)
	            || coupon.getCouponType().equals(CustConst.Coupon.N_BUY) 
	            || coupon.getCouponType().equals(CustConst.Coupon.REAL_COUPON)
	            || coupon.getCouponType().equals(CustConst.Coupon.EXPERIENCE)) {
			mIvShare.setVisibility(View.VISIBLE);
		} else if (coupon.getCouponType().equals(CustConst.Coupon.EXCHANGE_VOUCHER)) { // 兑换券
			double money = Util.isEmpty(coupon.getInsteadPrice()) ? 0 : Double.parseDouble(coupon.getInsteadPrice());
			if (money > 0) {
				mIvShare.setVisibility(View.GONE);
			} else {
				mIvShare.setVisibility(View.VISIBLE);
			}
		} else {
			double payMoeny = Util.isEmpty(coupon.getPayPrice()) ? 0 : Double.parseDouble(coupon.getPayPrice());
			if (payMoeny > 0) {
				mIvShare.setVisibility(View.GONE);
			} else {
				mIvShare.setVisibility(View.VISIBLE);
			}
		}
	}
	
	/**
	 * 设置优惠券的金额
	 * @param batchCoupon 优惠券对象
	 */
	private void setCouponMoney (BatchCoupon coupon,TextView tvDeductAmount) {
		String canMoney = "";
		int type = ViewSolveUtils.getInputNum(coupon.getCouponType());
		switch (type) {
		case CustConst.Coupon.INT_DEDUCT: // 抵扣券
			canMoney = "满" + coupon.getAvailablePrice() + "元减" + coupon.getInsteadPrice() + "元";
			break;
		case CustConst.Coupon.INT_DISCOUNT: // 折扣券
			canMoney = "满" + coupon.getAvailablePrice() + "元打" + coupon.getDiscountPercent() + "折";
			break;
		case CustConst.Coupon.INT_N_BUY: // N元购
		case CustConst.Coupon.INT_EXCHANGE_VOUCHER: // 兑换券
			double money = Util.isEmpty(coupon.getInsteadPrice()) ? 0 : Double.parseDouble(coupon.getInsteadPrice());
			if (money > 0) {
				canMoney = "1张" + coupon.getInsteadPrice() + "元";
			}
			break;
		case CustConst.Coupon.INT_VOUCHER: // 代金券
			double payMoeny = Util.isEmpty(coupon.getPayPrice()) ? 0 : Double.parseDouble(coupon.getPayPrice());
			if (payMoeny > 0) {
				canMoney = coupon.getPayPrice() + "元代" +coupon.getInsteadPrice();
			}
			break;
		default:
			break;
		}
		tvDeductAmount.setText(canMoney);
	
	}
	
	/**
	 * 头部商家信息
	 * @param batchCouponDetail 对象
	 */
	private void setHeadContent (BatchCouponDetail batchCouponDetail) {
		// 商店信息
		final Shop shop = batchCouponDetail.getShopInfo();
		mCity = shop.getCity(); // 获取城市
		// 滚屏
		if (null != shop.getShopImg() && shop.getShopImg().size() > 0) {
			mSrollPics = new String[shop.getShopImg().size()];
			// 滚动图片
			for (int i = 0; i < mSrollPics.length; i++) {
				ShopDecoration decoration = shop.getShopImg().get(i);
				mSrollPics[i] = decoration.getImgUrl();
			}
		} else {
			mSrollPics = new String[1];
		}
		initViewPager(mSrollPics);
		
		TextView tvShopName = (TextView) mHeadView.findViewById(R.id.shop_name);  // 商家名称
		TextView tvPopularity = (TextView) mHeadView.findViewById(R.id.tv_popularity); // 人气
		TextView tvRepeat = (TextView) mHeadView.findViewById(R.id.tv_repeat); // 回头客
		TextView tvBusinessHours = (TextView) mHeadView.findViewById(R.id.tv_business_hours); // 营业时间
		ImageView ivShopLogo = (ImageView) mHeadView.findViewById(R.id.shop_logo); // 商家logo
		RelativeLayout lyGotoShop = (RelativeLayout) mHeadView.findViewById(R.id.ly_goto_shop); // 进入店铺
		//	商家logo
		Util.showFirstImages(getMyActivity(), shop.getLogoUrl(), ivShopLogo);
		// 商家名称
		if (!Util.isEmpty(shop.getShopName())) {
			tvShopName.setText(shop.getShopName());
		}
		// 商家人气和回头客
		tvPopularity.setText(Util.getString(R.string.popularity) + shop.getPopularity());
		tvRepeat.setText(Util.getString(R.string.repeatcustomer) + shop.getRepeatCustomers());
		// 商家营业时间 开门和关门字段都为空或者 开门时间和关门时间为 00:00 - 23:59 为全天营业
		tvBusinessHours.setText(Util.getString(R.string.business_date) + shop.getBusinessHoursString());
		
		// 进入店铺
		lyGotoShop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(TAG, "ddddddddd");
				SkipActivityUtil.skipNewShopDetailActivity(getMyActivity(), shop.getShopCode());				
			}
		});
	}
	
	/**
	 * 各种点击事件
	 * @param view
	 */
	@OnClick(R.id.iv_turn_in)
	private void ivBackUpClick (View view) {
			getMyActivity().finish();
	}
	
	/**
	 * 友盟统计
	 */
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(BatchCouponDetailFragment.class.getSimpleName());
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(BatchCouponDetailFragment.class.getSimpleName()); // 统计页面
		getBatchCouponInfo();
	}
}
