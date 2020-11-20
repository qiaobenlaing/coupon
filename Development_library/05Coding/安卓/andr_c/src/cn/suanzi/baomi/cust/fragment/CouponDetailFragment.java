package cn.suanzi.baomi.cust.fragment;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.model.TheadDBhelper;
import cn.suanzi.baomi.base.pojo.BatchCoupon;
import cn.suanzi.baomi.base.pojo.Image;
import cn.suanzi.baomi.base.pojo.Shop;
import cn.suanzi.baomi.base.pojo.ShopDecoration;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.QrCodeUtils;
import cn.suanzi.baomi.base.utils.ShareCouponUtil;
import cn.suanzi.baomi.base.utils.TimeUtils;
import cn.suanzi.baomi.base.utils.ViewSolveUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.activity.BigPhotoActivity;
import cn.suanzi.baomi.cust.activity.PayMoneyActivity;
import cn.suanzi.baomi.cust.activity.SingleBigPhotoActivity;
import cn.suanzi.baomi.cust.activity.UseCouponActivity;
import cn.suanzi.baomi.cust.adapter.CouponDetailSrollAdapter;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.model.GetUserCouponInfoTask;
import cn.suanzi.baomi.cust.util.SkipActivityUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.WriterException;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 模糊搜索商店
 * @author yanfang.li
 */
public class CouponDetailFragment extends Fragment {
	private static final String TAG = CouponDetailFragment.class.getSimpleName();
	public static final String USER_COUPON_CODE = "userCouponCode";
	public final static String DATE_FORMAT = "00:00";
	public static final String PAY_STATUS = "payStatus";
	public final static int REQU_CODE = 100;
	public final static int RESULT_SUCC = 101;
	/** 优惠券已失效4 */
	public final static int FAIL = 4;
	/** 优惠券已领取 1*/
	public final static int CAN_USE = 1;
	/** 优惠券已使用 2 */
	public final static int USE = 2;
	/** 优惠券待使用5*/
	public final static int WAIT_USE = 5;
	/** 图片 */
	private ViewPager mViewPager;
	/** 优惠券状态*/
	private TextView mTvCouponStatus;
	/** 正在加载数据 */
	private ProgressBar mProgNoData;
	/** 优惠券使用按钮*/
	private Button mBtnImmediatelyUse;
	/** 滚动图片的集合*/
	private String[] mSrollPics;
	/** 滚屏的集合*/
	private List<ImageView> mImagesList;
	/** 优惠券对象 */
	private BatchCoupon mCoupon;
	/** 商店对象*/
	private Shop mShop;
	/** 用户优惠券编码 */
	private String mUserCouponCode;
	/** 滚屏图片 当前显示页,从1开始  */
	private int mCurPosition = 1;
	
	public static CouponDetailFragment newInstance() {
		Bundle args = new Bundle();
		CouponDetailFragment fragment = new CouponDetailFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_coupondetail, container, false);
		ViewUtils.inject(this, view);
		Util.addActivity(getMyActivity());
		Util.addLoginActivity(getMyActivity());
		init(view);
		return view;
	}

	private void init(View v) {
		mProgNoData = (ProgressBar) v.findViewById(R.id.prog_center_nodata); // 正在加载数据
		mUserCouponCode = getMyActivity().getIntent().getStringExtra(USER_COUPON_CODE);
		mViewPager = (ViewPager) v.findViewById(R.id.viewPager);
		TextView tvTitle = (TextView) v.findViewById(R.id.tv_mid_content);
		tvTitle.setText(R.string.coupon_use_detail);
		mTvCouponStatus = (TextView) v.findViewById(R.id.tv_coupon_status); // 优惠券状态
		mBtnImmediatelyUse = (Button) v.findViewById(R.id.btn_immediately_use); // 使用
		getUserCouponInfo(v); // 优惠券详情
		// 设置progress的位置
		ViewSolveUtils.setProgLocation(getActivity(),mProgNoData);
	}

	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	/**
	 * ViewPager
	 * 
	 * @param v
	 * @param ShopPics
	 *            图片数组
	 */
	private void initViewPager(View v, String[] ShopPics) {
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

		mViewPager = (ViewPager) v.findViewById(R.id.viewPager);
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
	 * 优惠券详细信息
	 */
	private void getUserCouponInfo(final View v) {
		if (mUserCouponCode != null) {
			new GetUserCouponInfoTask(getMyActivity(), new GetUserCouponInfoTask.Callback() {
				@Override
				public void getResult(JSONObject result) {
					mProgNoData.setVisibility(View.GONE);
					if (result == null) {
						return;
					} else {
						Log.d(TAG, "resultCoupondetal >>> " +result.toString());
						mCoupon = new Gson().fromJson(result.toString(), new TypeToken<BatchCoupon>() {}.getType());
						if ( null != mCoupon && null != mCoupon.getShopDecoration() && mCoupon.getShopDecoration().size() > 0) {
							mSrollPics = new String[mCoupon.getShopDecoration().size()];
							// 滚动图片
							for (int i = 0; i < mCoupon.getShopDecoration().size(); i++) {
								ShopDecoration decoration = mCoupon.getShopDecoration().get(i);
								mSrollPics[i] = decoration.getImgUrl();
							}
						} else {
							mSrollPics = new String[1];
						}
						initViewPager(v, mSrollPics);
						couponBatch(mCoupon, v);
					}

				}
			}).execute(mUserCouponCode);
		}
	}

	/**
	 * 优惠券
	 * 
	 * @param result
	 */
	private void couponBatch(final BatchCoupon batchCoupon, View view) {

		TextView tvCouponCode = (TextView) view.findViewById(R.id.tv_couponcode); // 优惠券编码
		TextView tvCouponType = (TextView) view.findViewById(R.id.tv_coupontype); // 优惠券类型
		TextView tvAvailablePrice = (TextView) view.findViewById(R.id.tv_availableprice); // 满多少可以用
		TextView tvCouponUseDate = (TextView) view.findViewById(R.id.tv_couponuse_date); // 使用日期
		TextView tvCouponDayTime = (TextView) view.findViewById(R.id.tv_couponday_time); // 每天使用日期
		TextView tvCouponRemark = (TextView) view.findViewById(R.id.tv_coupon_remark); // 使用说明
		TextView tvShopName = (TextView) view.findViewById(R.id.shop_name); // 商店名称
		TextView tvPopularity = (TextView) view.findViewById(R.id.tv_popularity); // 人气
		TextView tvRepeat = (TextView) view.findViewById(R.id.tv_repeat); // 回头客
		tvRepeat.setVisibility(View.GONE);
		ImageView ivShopLogo = (ImageView) view.findViewById(R.id.shop_logo); // 头像

		TextView tvShopBottomName = (TextView) view.findViewById(R.id.tv_shopname); // 商家名称
		TextView tvShopAddres = (TextView) view.findViewById(R.id.tv_shopaddres); // 商家地址
		TextView tvShopTel = (TextView) view.findViewById(R.id.tv_shoptel); // 电话号码
		ImageView ivShopHead = (ImageView) view.findViewById(R.id.tv_shophead); // 头像
		RelativeLayout rlShopInfo = (RelativeLayout) view.findViewById(R.id.rl_shopinfo); // 头像
		ImageView ivBarCode = (ImageView) view.findViewById(R.id.iv_barcode);//条形码
		
		
		mShop = new Shop(); // 商店详情
		// -------------------- 商店信息 ----------------------------
		// 商店名称名称
		if (null != batchCoupon.getShopName() || !Util.isEmpty(batchCoupon.getShopName())) {
			tvShopName.setText(batchCoupon.getShopName());
			tvShopBottomName.setText(batchCoupon.getShopName());
			mShop.setShopName(batchCoupon.getShopName()); // 商家名称
		}
		// 人气
		tvPopularity.setText(getString(R.string.popularity) + batchCoupon.getPopularity());
		// 回头客
		tvRepeat.setText(getString(R.string.repeatcustomer) + batchCoupon.getRepeatCustomers());
		// 商店logo
		Util.showImage(getMyActivity(), batchCoupon.getLogoUrl(), ivShopLogo);
		// 点击头像查看大图
		ivShopLogo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getMyActivity(), SingleBigPhotoActivity.class);
				intent.putExtra(BigPhotoActivity.IMAGEURL, batchCoupon.getLogoUrl());
				startActivity(intent);
				
			}
		});
		
		Util.showImage(getMyActivity(), batchCoupon.getLogoUrl(), ivShopHead);
		mShop.setLogoUrl(batchCoupon.getLogoUrl()); // 商家logo
		mShop.setShopCode(batchCoupon.getShopCode()); // 商家code
		// 商店地址
		if (null != batchCoupon.getStreet() || !Util.isEmpty(batchCoupon.getStreet())) {
			tvShopAddres.setText(batchCoupon.getStreet());
		}
		// 电话号码
		if (null != batchCoupon.getTel() || !Util.isEmpty(batchCoupon.getTel())) {
			Log.d(TAG, "batchCoupon.getTel() >>> " +batchCoupon.getTel());
			tvShopTel.setText(batchCoupon.getTel());
		} else {
			tvShopTel.setText(batchCoupon.getMobileNbr());
		}

		// -------------------- 优惠券信息 ----------------------------
		// 优惠券编码
		if (null == batchCoupon.getUserCouponNbr() || Util.isEmpty(batchCoupon.getUserCouponNbr())) {
			tvCouponCode.setText("优惠劵编码：" + "XXXXXX");
		} else {
			tvCouponCode.setText("优惠劵编码：" + batchCoupon.getUserCouponNbr());
			//生成条形码
			try {
				ivBarCode.setImageBitmap(QrCodeUtils.CreateOneDCode(batchCoupon.getUserCouponNbr()));
			} catch (WriterException e) {
				Log.e(TAG, "生成条形码 >>> " + e.getMessage());
			}
		}
		
		Log.d(TAG, "batchCoupon.getStatus()="+batchCoupon.getStatus());
		// 优惠券的状态 ： 用户优惠券状 status 1-可使用；2-已使用；4-已过期； 5-待使用
		if (batchCoupon.getStatus() == FAIL) {
			mBtnImmediatelyUse.setEnabled(false);
			mTvCouponStatus.setText(getString(R.string.fail));
			mBtnImmediatelyUse.setText(getString(R.string.fail));
			mBtnImmediatelyUse.setBackgroundResource(R.drawable.shape_couponfail);
		} else if (batchCoupon.getStatus() == CAN_USE) {
			mBtnImmediatelyUse.setBackgroundResource(R.drawable.shape_couponuse);
			mBtnImmediatelyUse.setTextColor(getResources().getColor(R.color.white));
			mBtnImmediatelyUse.setEnabled(true);
			mTvCouponStatus.setText(getString(R.string.can_use));
			mBtnImmediatelyUse.setText(getString(R.string.immediately_use));
		} else if (batchCoupon.getStatus() == USE) {
			mBtnImmediatelyUse.setEnabled(false);
			mBtnImmediatelyUse.setBackgroundResource(R.drawable.shape_couponfail);
			mTvCouponStatus.setText(getString(R.string.use));
			mBtnImmediatelyUse.setText(getString(R.string.use));
		} else if (batchCoupon.getStatus() == WAIT_USE) {
			mBtnImmediatelyUse.setEnabled(false);
			mTvCouponStatus.setText(getString(R.string.wait_use));
			mBtnImmediatelyUse.setBackgroundResource(R.drawable.shape_couponfail);
			mBtnImmediatelyUse.setText(getString(R.string.wait_use));
		}

		// 可以干嘛
		String canMoney = "";
		// 面值
		String insteadPrice = "";
		// 优惠券类型名称
		String couponTypeName = "";
		if (CustConst.Coupon.DEDUCT.equals(mCoupon.getCouponType())
				|| CustConst.Coupon.REG_DEDUCT.equals(mCoupon.getCouponType()) 
				|| CustConst.Coupon.INVITE_DEDUCT.equals(mCoupon.getCouponType())) { // 抵扣券
			canMoney = "满" + mCoupon.getAvailablePrice() + "元立减" + mCoupon.getInsteadPrice() + "元";
			couponTypeName = getString(R.string.coupon_deduct);
		} else if (CustConst.Coupon.DISCOUNT.equals(mCoupon.getCouponType())) { // 折扣券
			canMoney = "满" + mCoupon.getAvailablePrice() + "元打" + mCoupon.getDiscountPercent() + "折";
			couponTypeName = getString(R.string.coupon_discount);

		} else if (CustConst.Coupon.N_BUY.equals(mCoupon.getCouponType())) { // N元购
			canMoney = mCoupon.getFunction();
			insteadPrice = mCoupon.getInsteadPrice();
			couponTypeName = getString(R.string.n_buy);
			// 实物券和体验券
		} else if (CustConst.Coupon.REAL_COUPON.equals(mCoupon.getCouponType())
				|| CustConst.Coupon.EXPERIENCE.equals(mCoupon.getCouponType())) {
			if (CustConst.Coupon.REAL_COUPON.equals(mCoupon.getCouponType())) {
				couponTypeName = getString(R.string.real_coupon);
			} else {
				couponTypeName = getString(R.string.experience);
			}
			canMoney = mCoupon.getFunction();
		}
		Log.d(TAG, "可以干嘛="+canMoney);
		tvAvailablePrice.setText(canMoney); // 满多少可干嘛
		tvCouponType.setText( couponTypeName); // 优惠券类型

		// 有效时间
		String useTime = TimeUtils.getTime(mCoupon); 
		useTime = Util.isEmpty(useTime) ? getString(R.string.no_limit_time) : useTime;
		tvCouponUseDate.setText(useTime);
		// 每天使用时间
		String useDate = TimeUtils.getDate(mCoupon);
		useDate = Util.isEmpty(useTime) ? getString(R.string.day_use) : useDate;
		tvCouponDayTime.setText(useDate);
		// 使用说明
		tvCouponRemark.setText(!Util.isEmpty(mCoupon.getRemark()) ? mCoupon.getRemark() : getString(R.string.no_remark));

		// 跳转到商店详情界面
		rlShopInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SkipActivityUtil.skipNewShopDetailActivity(getMyActivity(), batchCoupon.getShopCode());
			}
		});

		// 优惠券使用
		mBtnImmediatelyUse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TimeUtils.getCanCoupon(batchCoupon)) { // 判断优惠券在不在使用时间范围内
					if (CustConst.Coupon.REAL_COUPON.equals(batchCoupon.getCouponType())
							|| CustConst.Coupon.EXPERIENCE.equals(batchCoupon.getCouponType())) {
						if (null == batchCoupon) { return; }
						Intent intent = new Intent(getMyActivity(), UseCouponActivity.class);
						intent.putExtra(UseCouponActivity.USE_CPOUPON_CODE, batchCoupon.getUserCouponCode());
						intent.putExtra(UseCouponActivity.TYPE, UseCouponActivity.COUPON_DETAIL);
						startActivityForResult(intent, REQU_CODE);
					} else if (CustConst.Coupon.DEDUCT.equals(batchCoupon.getCouponType())
							|| CustConst.Coupon.DISCOUNT.equals(batchCoupon.getCouponType())) { 
						SkipActivityUtil.skipPayBillActivity(getMyActivity(), mShop, "",ShopPayBillFragment.JOIN_DISCOUNT,ShopPayBillFragment.NOT_MEAL);
					} else {
						Intent intent = new Intent(getMyActivity(), PayMoneyActivity.class);
						intent.putExtra(PayMoneyActivity.USE_CPOUPON_CODE, batchCoupon.getUserCouponCode());
						startActivity(intent);
					}
				}
			}
		});

	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQU_CODE:
			if (resultCode == RESULT_SUCC) {
				boolean payStatus = data.getBooleanExtra(PAY_STATUS, false);
				if (payStatus) {
					mBtnImmediatelyUse.setEnabled(false);
					mTvCouponStatus.setText(getString(R.string.use));
					mBtnImmediatelyUse.setText(getString(R.string.use));
				}
			}
			break;

		default:
			break;
		}
	}
	
	/**
	 * 点击返回查看到活动列表
	 */
	@OnClick({ R.id.iv_turn_in, R.id.iv_share_all })
	public void tvBackClick(View view) {
		switch (view.getId()) {
		case R.id.iv_turn_in:
			getMyActivity().finish();
			break;
		case R.id.iv_share_all:
			if (null == mCoupon) {
				return;
			}
			ShareCouponUtil.shareCoupon(getMyActivity(), mCoupon); // 分享
			break;

		default:
			break;
		}
	}

	/**
	 * 友盟统计
	 */
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(CouponDetailFragment.class.getSimpleName()); // 统计页面
		TheadDBhelper.runRunnable(mCouponHandler, mSrollRunnable); // 重新启动线程
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(CouponDetailFragment.class.getSimpleName());
		TheadDBhelper.closeRunnable(mCouponHandler, mSrollRunnable); // 关闭线程
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		TheadDBhelper.closeRunnable(mCouponHandler, mSrollRunnable); // 关闭线程
	}
}