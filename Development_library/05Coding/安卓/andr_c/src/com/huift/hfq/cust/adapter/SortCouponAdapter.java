package com.huift.hfq.cust.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huift.hfq.base.Const;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.adapter.CommenViewHolder;
import com.huift.hfq.base.adapter.CommonListViewAdapter;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.LoginTask;
import com.huift.hfq.base.pojo.BatchCoupon;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.base.utils.ShareCouponUtil;
import com.huift.hfq.base.utils.TimeUtils;
import com.huift.hfq.base.utils.ViewSolveUtils;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.activity.CouponDetailActivity;
import com.huift.hfq.cust.activity.LoginActivity;
import com.huift.hfq.cust.activity.PayMoneyActivity;
import com.huift.hfq.cust.activity.ShopPayBillActivity;
import com.huift.hfq.cust.activity.UseCouponActivity;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.fragment.CouponDetailFragment;
import com.huift.hfq.cust.fragment.ShopDetailFragment;
import com.huift.hfq.cust.fragment.ShopPayBillFragment;
import com.umeng.analytics.MobclickAgent;

/**
 * 优惠券分类
 * 
 * @author yanfang.li
 */
public class SortCouponAdapter extends CommonListViewAdapter<BatchCoupon> {

	private final static String TAG = SortCouponAdapter.class.getSimpleName();
	private Shop mShop;

	private boolean tab1open = false;
	private boolean tab2open = false;

	private List<BatchCoupon> list1;
	private List<BatchCoupon> list2;

	public SortCouponAdapter(Activity activity, List<BatchCoupon> datas1, List<BatchCoupon> datas2, Shop shop) {

		super(activity, null);
		this.mShop = shop;
		this.list1 = datas1;
		this.list2 = datas2;
		tab1open = !tab1open;
		tab2open = !tab2open;
		initDatas(1);
	}

	public void setItems(List<BatchCoupon> datas1, List<BatchCoupon> datas2, int type) {

		this.list1 = datas1;
		this.list2 = datas2;
		if (type == 0) {
			initDatas(0);
		} else {
			initDatas(1);
		} 
	}

	private void initDatas(int type) {

		if (mDatas == null) {
			mDatas = new ArrayList<BatchCoupon>();
		} else {
			mDatas.clear();
		}
		if (type == 0) {
			BatchCoupon bc1 = new BatchCoupon();
			bc1.setCouponName("我的优惠券");
			bc1.setStatus(0);
			mDatas.add(bc1);
			BatchCoupon bc2 = new BatchCoupon();
			bc2.setCouponName("未领取的优惠券");
			bc2.setStatus(1);
			mDatas.add(bc2);
		}  else {
			BatchCoupon bc1 = new BatchCoupon();
			bc1.setCouponName("我的优惠券");
			bc1.setStatus(0);
			mDatas.add(bc1);
			if (tab1open) {
				mDatas.addAll(list1);
			}
			BatchCoupon bc2 = new BatchCoupon();
			bc2.setCouponName("未领取的优惠券");
			bc2.setStatus(1);
			mDatas.add(bc2);
			if (tab2open) {
				mDatas.addAll(list2);
			}
		}
		setItems(mDatas);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView, parent,
				R.layout.item_detailcoupon, position);
		Log.d(TAG, "shopdetail position =" +position);
		final BatchCoupon coupon = mDatas.get(position);
		View lyCouponLayout = holder.getView(R.id.ly_coupon_layout);
		View iconUpdown = holder.getView(R.id.icon_updown);
		View leftContent = holder.getView(R.id.left_content);
		ImageView upArrow = holder.getView(R.id.up_arrow); // 向上的箭头

		final RelativeLayout rlMoreShow = holder.getView(R.id.hideOrShow); // 更多类容显示
		TextView tvSortName = holder.getView(R.id.tv_sortname); // 优惠券类型
		LinearLayout lyArrow = holder.getView(R.id.icon_updown); // 显示更多类容
		final ImageView ivMoreShow = holder.getView(R.id.iv_coupon_arrow); // 显示更多类容
		Button ivShare = holder.getView(R.id.iv_share); // 分享
		if (CustConst.Coupon.REG_DEDUCT.equals(coupon.getCouponType()) 
				|| CustConst.Coupon.INVITE_DEDUCT.equals(coupon.getCouponType())) {
			ivShare.setVisibility(View.GONE);
		} else {
			ivShare.setVisibility(View.VISIBLE);
		}
		FrameLayout rlCouponBg = holder.getView(R.id.right_content); // 优惠券背景
		tvSortName.setText(coupon.getCouponName());

		if ((null == list2 || list2.size() <= 0) && (null == list1 || list1.size() <= 0)) {
			lyCouponLayout.setVisibility(View.GONE);
		} else {
			lyCouponLayout.setVisibility(View.VISIBLE);
		} 
		
		if (coupon.getStartUsingTime() == null) {// 代表是标题栏
			lyCouponLayout.setVisibility(View.VISIBLE);
			iconUpdown.setVisibility(View.GONE);
			rlCouponBg.setVisibility(View.GONE);
			rlMoreShow.setVisibility(View.GONE);
			leftContent.setVisibility(View.GONE);
			if (coupon.getCouponName().equals("我的优惠券")) {
				if (tab1open) {
					upArrow.setBackgroundResource(R.drawable.up_arrow);
					if (null == list1 || list1.size() <= 0) {
						lyCouponLayout.setVisibility(View.GONE);
					} else {
						lyCouponLayout.setVisibility(View.VISIBLE);
					}
				} else {
					upArrow.setBackgroundResource(R.drawable.down_arrow);
					if (null == list1 || list1.size() <= 0) {
						lyCouponLayout.setVisibility(View.GONE);
					} else {
						lyCouponLayout.setVisibility(View.VISIBLE);
					}
				}
			} else {
				if (tab2open) {
					upArrow.setBackgroundResource(R.drawable.up_arrow);
					if (null == list2 || list2.size() <= 0) {
						lyCouponLayout.setVisibility(View.GONE);
					} else {
						lyCouponLayout.setVisibility(View.VISIBLE);
					}
				} else {
					upArrow.setBackgroundResource(R.drawable.down_arrow);
					if (null == list2 || list2.size() <= 0) {
						lyCouponLayout.setVisibility(View.GONE);
					} else {
						lyCouponLayout.setVisibility(View.VISIBLE);
					}
				}
			}

			View v = holder.getConvertView();
			v.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d(TAG, "couponName=" + coupon.getCouponName());
					if (coupon.getStatus() == 0) {
						// 代表第1栏
						tab1open = !tab1open;
					} else {
						tab2open = !tab2open;
					}
					initDatas(1);
				}
			});
		} else {

			lyCouponLayout.setVisibility(View.GONE);
			iconUpdown.setVisibility(View.VISIBLE);
			rlCouponBg.setVisibility(View.VISIBLE);
			rlMoreShow.setVisibility(View.GONE);
			leftContent.setVisibility(View.VISIBLE);

			// 判断优惠券类型
			getCoupon(holder, coupon, position, tvSortName);

			// 分享
			ivShare.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 友盟统计
					HashMap<String, String> map = new HashMap<String, String>();
					MobclickAgent.onEvent(mActivity, "coupon_share", map);
					ShareCouponUtil.shareCoupon(mActivity, coupon); // 分享
				}
			});

			final Integer selectFlag = coupon.getShowMore();
			// 显示更多
			if (coupon.getShowMore() == 0) {
				rlMoreShow.setVisibility(View.GONE);
				ivMoreShow.setImageResource(R.drawable.arrow_down);
			} else {
				rlMoreShow.setVisibility(View.VISIBLE);
				ivMoreShow.setImageResource(R.drawable.arrow_up);
			}

			lyArrow.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (selectFlag == null || selectFlag == 0) {
						coupon.setShowMore(1);
						ivMoreShow.setImageResource(R.drawable.arrow_up);
					} else {
						coupon.setShowMore(0);
						ivMoreShow.setImageResource(R.drawable.arrow_down);
					}
					notifyDataSetChanged();
				}
			});
			
		}

		return holder.getConvertView();
	}

	// 优惠券布局
	private void getCoupon(CommenViewHolder holder, final BatchCoupon coupon, final int position,
			final TextView tvSortName) {
		TextView tvSymbol = holder.getView(R.id.tv_coupon_symbol); // 金额的符号
		TextView tvCouponMoney = holder.getView(R.id.tv_coupon_money); // 优惠券金额
		TextView tvCouponFunction = holder.getView(R.id.tv_coupon_function); // 优惠券可以干嘛
		TextView tvUserCouponCode = holder.getView(R.id.tv_usercoupon_code); // 优惠券编码
		TextView tvCouponDate = holder.getView(R.id.tv_coupon_date); // 有效期 日期
		TextView tvCouponTime = holder.getView(R.id.tv_coupon_time); // 有效期 时间
		TextView tvCouponRemark = holder.getView(R.id.tv_coupon_use); // 优惠券说明
		final TextView tvCouponDraw = holder.getView(R.id.tv_coupon_draw); // 优惠券领取或者使用
		final FrameLayout rlCouponBg = holder.getView(R.id.right_content); // 优惠券背景
		View leftContent = holder.getView(R.id.left_content);

		// 批次号
		if (Util.isEmpty(coupon.getUserCouponCode())) {
			tvUserCouponCode.setVisibility(View.GONE);
			tvCouponDraw.setText(getString(R.string.coupon_draw));
		} else {
			tvUserCouponCode.setVisibility(View.VISIBLE);
			tvUserCouponCode.setText(getString(R.string.user_coupon_code) + coupon.getUserCouponNbr());
			tvCouponDraw.setText(getString(R.string.coupon_use));
		}

		// 领取或者使用
		rlCouponBg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) { 
				// 等于领取
				if (getString(R.string.coupon_draw).equals(ViewSolveUtils.getValue(tvCouponDraw))) {
					grabCoupon(coupon, rlCouponBg, position, tvCouponDraw);
				} else {
					if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
						if (TimeUtils.getCanCoupon(coupon)) { // 判断优惠券在不在使用时间范围内
							if (CustConst.Coupon.REAL_COUPON.equals(coupon.getCouponType())
									|| CustConst.Coupon.EXPERIENCE.equals(coupon.getCouponType())) { // 实物券和体验券

								if (null == mShop) { return; }
								Intent intent = new Intent(mActivity, UseCouponActivity.class);
								intent.putExtra(UseCouponActivity.BATCH_CPOUPON, coupon);
								intent.putExtra(UseCouponActivity.SHOP_OBJ, mShop);
								intent.putExtra(UseCouponActivity.TYPE, UseCouponActivity.SHOP_DETAIL);
								mActivity.startActivityForResult(intent, ShopDetailFragment.REQ_CODE);

							} else if (CustConst.Coupon.DEDUCT.equals(coupon.getCouponType())
									|| CustConst.Coupon.DISCOUNT.equals(coupon.getCouponType())
									|| CustConst.Coupon.REG_DEDUCT.equals(coupon.getCouponType())  
									|| CustConst.Coupon.INVITE_DEDUCT.equals(coupon.getCouponType())) { // 抵扣券和折扣券

								Intent intent = new Intent(mActivity, ShopPayBillActivity.class);
								intent.putExtra(ShopPayBillFragment.PAY_OBJ, mShop);
								mActivity.startActivity(intent);

							} else { // N元购

								Intent intent = new Intent(mActivity, PayMoneyActivity.class);
								intent.putExtra(PayMoneyActivity.BATCH_CPOUPON, coupon);
								intent.putExtra(PayMoneyActivity.SHOP_OBJ, mShop);
								mActivity.startActivity(intent);
							}
						} 
					} else {
						login();
					}
				}
			}
		});
		
		// 跳转优惠券详情界面
		leftContent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 
				// 等于领取
				if (getString(R.string.coupon_draw).equals(ViewSolveUtils.getValue(tvCouponDraw))) {
					// TODO
				} else {
					if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
						Intent intent = new Intent(mActivity, CouponDetailActivity.class);
						intent.putExtra(CouponDetailFragment.USER_COUPON_CODE, coupon.getUserCouponCode());
						mActivity.startActivity(intent);
					} else {
						login();
					}
				}
			}
		});
		
		// 使用时间
		String useTime = TimeUtils.getTime(coupon);
		if (Util.isEmpty(useTime)) {
			tvCouponDate.setText(getString(R.string.no_limit_time));
		} else {
			Log.d(TAG, "startDate=" + useTime);
			tvCouponDate.setText(useTime);
		}
		// 使用时间
		String useDate = TimeUtils.getDate(coupon);
		if (Util.isEmpty(useDate)) {
			tvCouponTime.setText(getString(R.string.use_coupon_time) + getString(R.string.day_use));
		} else {
			tvCouponTime.setText(getString(R.string.use_coupon_time) + useDate);
		}

		// 使用说明
		if (Util.isEmpty(coupon.getRemark())) {
			tvCouponRemark.setText(getString(R.string.no_remark));
		} else {
			tvCouponRemark.setText(coupon.getRemark());
		}
		// 说明优惠券
		String canMoney = "";
		String insteadPrice = "";
		String couponType = "";
		if (CustConst.Coupon.DEDUCT.equals(coupon.getCouponType())
				|| CustConst.Coupon.REG_DEDUCT.equals(coupon.getCouponType())
				|| CustConst.Coupon.INVITE_DEDUCT.equals(coupon.getCouponType())) { // 抵扣券
			getCouponBg(holder, getColor(R.color.dedcut_bg), getColor(R.color.dedcut_bottom), R.drawable.small_deduct);
			couponType = getString(R.string.coupon_deduct);
			canMoney = "满" + coupon.getAvailablePrice() + "元立减" + coupon.getInsteadPrice() + "元";
			tvSymbol.setVisibility(View.GONE);
			tvCouponMoney.setVisibility(View.GONE);
			tvSymbol.setVisibility(View.GONE);
			insteadPrice = "";
			tvCouponFunction.setTextSize(22);
			tvCouponFunction.setTextColor(getColor(R.color.coupon_price));
		} else if (CustConst.Coupon.DISCOUNT.equals(coupon.getCouponType())) { // 折扣券
			getCouponBg(holder, getColor(R.color.discount_bg), getColor(R.color.discount_bottom),
					R.drawable.small_discount);
			couponType = getString(R.string.coupon_discount);
			canMoney = "满" + coupon.getAvailablePrice() + "元打" + coupon.getDiscountPercent() + "折";
			tvSymbol.setVisibility(View.VISIBLE);
			tvCouponMoney.setVisibility(View.GONE);
			tvSymbol.setVisibility(View.GONE);
			tvCouponFunction.setTextSize(22);
			tvCouponFunction.setTextColor(getColor(R.color.coupon_price));
			insteadPrice = "";
		} else if (CustConst.Coupon.N_BUY.equals(coupon.getCouponType())) { // N元购
			getCouponBg(holder, getColor(R.color.nbuy_bg), getColor(R.color.nbuy_bottom), R.drawable.small_nbuy);
			couponType = getString(R.string.n_buy);
			canMoney = coupon.getFunction();
			tvCouponMoney.setVisibility(View.VISIBLE);
			tvSymbol.setVisibility(View.VISIBLE);
			tvCouponFunction.setTextSize(14);
			tvCouponFunction.setTextColor(getColor(R.color.home_font));
			insteadPrice = coupon.getInsteadPrice();
			// 实物券和体验券
		} else if (CustConst.Coupon.REAL_COUPON.equals(coupon.getCouponType())) {// 实物券
			getCouponBg(holder, getColor(R.color.real_bg), getColor(R.color.real_bottom), R.drawable.small_real);
			canMoney = coupon.getFunction();
			tvCouponMoney.setVisibility(View.GONE);
			tvSymbol.setVisibility(View.GONE);
			insteadPrice = "";
			tvCouponFunction.setTextSize(22);
			tvCouponFunction.setTextColor(getColor(R.color.coupon_price));
		} else if (CustConst.Coupon.EXPERIENCE.equals(coupon.getCouponType())) {
			getCouponBg(holder, getColor(R.color.experience_bg), getColor(R.color.experience_bottom),
					R.drawable.small_expre);
			canMoney = coupon.getFunction();
			tvCouponMoney.setVisibility(View.GONE);
			tvSymbol.setVisibility(View.GONE);
			insteadPrice = "";
			tvCouponFunction.setTextSize(22);
			tvCouponFunction.setTextColor(getColor(R.color.coupon_price));
		}
		if (Util.isEmpty(insteadPrice)) {
			tvCouponMoney.setVisibility(View.GONE);
		} else {
			tvCouponMoney.setText(insteadPrice);
			tvCouponMoney.setVisibility(View.VISIBLE);
		}
		tvCouponFunction.setText(canMoney);

	}

	/**
	 * 设置优惠券
	 * 
	 * @param batchCoupon
	 *            优惠券对象
	 * @param rlCouponBg
	 *            抢优惠券
	 * @param position
	 *            每列的位置
	 * @param tvSortName
	 */
	private void grabCoupon(final BatchCoupon batchCoupon, final FrameLayout rlCouponBg, final int position,
			final TextView tvSortName) {

		final String batchCouponCode = batchCoupon.getBatchCouponCode();
		if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
			String[] params = { batchCouponCode, "2" }; // 2代表分享程度
			// 友盟统计
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("batchCouponCode", batchCouponCode);
			map.put("shopName", batchCoupon.getShopName());
			map.put("couponType", batchCoupon.getCouponType());
			MobclickAgent.onEvent(mActivity, "home_getcoupon", map);

			rlCouponBg.setEnabled(false);
			 // TODO 抢优惠券
		} else {
			login();
		}

	}

	/**
	 * 得到颜色
	 * 
	 * @param id
	 * @return
	 */
	private int getColor(int id) {
		return mActivity.getResources().getColor(id);
	}

	/**
	 * 跳转登
	 */
	private void login() {
		Intent intent = new Intent(mActivity, LoginActivity.class);
		intent.putExtra(LoginTask.ALL_LOGIN, Const.Login.SHOP_DETAIL);
		mActivity.startActivityForResult(intent, ShopDetailFragment.REQ_CODE);
	}

	/**
	 * 获取string类型的字符串
	 * 
	 * @param stringid
	 * @return
	 */
	private String getString(int stringid) {
		return mActivity.getResources().getString(stringid);
	}

	/**
	 * 设置背景
	 * 
	 * @param holder
	 */
	private void getCouponBg(CommenViewHolder holder, int couponBgId, int topColorId, int styleId) {
		View rlCouponBg = holder.getView(R.id.right_content); // 优惠券背景
		View couponStyle = holder.getView(R.id.coupon_style); // 优惠券样式
		View bottomColor = holder.getView(R.id.bottom_color); // 优惠券样式
		rlCouponBg.setBackgroundColor(couponBgId);
		bottomColor.setBackgroundColor(topColorId);
		couponStyle.setBackgroundResource(styleId);
	}

}
