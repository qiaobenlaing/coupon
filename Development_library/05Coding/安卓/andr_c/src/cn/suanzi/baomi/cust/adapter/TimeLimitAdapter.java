package cn.suanzi.baomi.cust.adapter;

import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.Const;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.adapter.CommenViewHolder;
import cn.suanzi.baomi.base.adapter.CommonListViewAdapter;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.BatchCoupon;
import cn.suanzi.baomi.base.utils.ShareCouponUtil;
import cn.suanzi.baomi.base.utils.TimeUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.activity.BatchCouponDetailActivity;
import cn.suanzi.baomi.cust.activity.CouponBuyActivity;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.fragment.BatchCouponDetailFragment;
import cn.suanzi.baomi.cust.fragment.CouponBuyFragment;
import cn.suanzi.baomi.cust.model.GrabCouponHaveCountTask;
import cn.suanzi.baomi.cust.util.SkipActivityUtil;

import com.umeng.analytics.MobclickAgent;

/**
 * 券市场
 * 
 * @author wensi.yu ， yanfang.li
 * 
 */
public class TimeLimitAdapter extends CommonListViewAdapter<BatchCoupon> {

	private final static String TAG = TimeLimitAdapter.class.getSimpleName();
	private final static String GRAD_RECIVE = "抢";
	private final static String GRAB_OVER = "抢完";
	private final static String GRAB_EXPIRED = "过期";
	private final static String BUY = "购买";
	private final static String COUNT = "张数";

	public TimeLimitAdapter(Activity activity, List<BatchCoupon> datas) {
		super(activity, datas);
		this.mDatas = datas;
	}

	@Override
	public void setItems(List<BatchCoupon> datas) {

		super.setItems(datas);
	}

	@Override
	public void addItems(List<BatchCoupon> datas) {

		super.addItems(datas);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView, parent, R.layout.item_grab_coupon,
				position);
		final BatchCoupon coupon = mDatas.get(position);
		// 商家Logo
		ImageView ivCardImage = ((ImageView) holder.getView(R.id.iv_couponpic));
		Util.showImage(mActivity, coupon.getLogoUrl(), ivCardImage);// 商家图片显示图片
		// 店铺名称
		((TextView) holder.getView(R.id.tv_shop_name)).setText(coupon.getShopName());
		// 距离
		((TextView) holder.getView(R.id.tv_distance)).setText(TimeUtils.getDistance(coupon.getDistance()));
		// 批次号
		((TextView) holder.getView(R.id.tv_coupon_nbr)).setText(Util.getString(R.string.tv_batch_nbr) + coupon.getBatchNbr());
		// 折
		TextView tvSymbol = holder.getView(R.id.tv_symbol);
		TextView tvMoney = holder.getView(R.id.tv_money);
		TextView tvCouponEffet = holder.getView(R.id.tv_coupon_effet); // 有效期
		TextView tvCouponDate = holder.getView(R.id.tv_coupon_effet2);
		TextView tvInsteadPrice = holder.getView(R.id.tv_coupon_price);
		TextView tvCouponType = holder.getView(R.id.tv_coupontype); // 优惠券类型
		LinearLayout tvRight = holder.getView(R.id.right_content); // 右边布局
		RelativeLayout tvLeft = holder.getView(R.id.left_content); // 走遍布局
		ImageView ivShare = holder.getView(R.id.iv_share); // 分享
		final TextView tvGrabCoupon = ((TextView) holder.getView(R.id.tv_couponedraw)); // 抢券
		// 说明优惠券
		String canMoney = "";
		String insteadPrice = "";
		String couponType = "";
		if (CustConst.Coupon.DEDUCT.equals(coupon.getCouponType())
				|| CustConst.Coupon.REG_DEDUCT.equals(coupon.getCouponType())
				|| CustConst.Coupon.INVITE_DEDUCT.equals(coupon.getCouponType())) { // 抵扣券
			canMoney = "满" + coupon.getAvailablePrice() + "减" + coupon.getInsteadPrice() + "元";
			tvSymbol.setVisibility(View.GONE);
			tvMoney.setVisibility(View.VISIBLE);
			tvRight.setBackgroundResource(R.drawable.full_send);
			insteadPrice = coupon.getInsteadPrice();
			couponType = Util.getString(R.string.coupon_deduct);
			ivShare.setVisibility(View.VISIBLE);
			tvInsteadPrice.setVisibility(View.VISIBLE);
		} else if (CustConst.Coupon.DISCOUNT.equals(coupon.getCouponType())) { // 折扣券
			canMoney = "满" + coupon.getAvailablePrice() + "打" + coupon.getDiscountPercent() + "折";
			insteadPrice = coupon.getDiscountPercent();
			tvSymbol.setVisibility(View.VISIBLE);
			tvMoney.setVisibility(View.GONE);
			tvRight.setBackgroundResource(R.drawable.full_cut);
			couponType = Util.getString(R.string.coupon_discount);
			ivShare.setVisibility(View.VISIBLE);
			tvInsteadPrice.setVisibility(View.VISIBLE);
		} else if (CustConst.Coupon.N_BUY.equals(coupon.getCouponType())) { // N元购
			canMoney = coupon.getFunction();
			tvSymbol.setVisibility(View.GONE);
			tvRight.setBackgroundResource(R.drawable.n_buy);
			couponType = Util.getString(R.string.n_buy);
			ivShare.setVisibility(View.VISIBLE);
			double money = Util.isEmpty(coupon.getInsteadPrice()) ? 0 : Double.parseDouble(coupon.getInsteadPrice());
			canMoney = coupon.getFunction();
			if (money > 0) {
				tvInsteadPrice.setVisibility(View.VISIBLE);
				tvMoney.setVisibility(View.VISIBLE);
				insteadPrice = coupon.getInsteadPrice();
			} else {
				tvInsteadPrice.setVisibility(View.GONE);
				tvMoney.setVisibility(View.GONE);
			}
		} else if (CustConst.Coupon.REAL_COUPON.equals(coupon.getCouponType())) {// 实物券
			canMoney = coupon.getFunction();
			tvSymbol.setVisibility(View.GONE);
			tvMoney.setVisibility(View.GONE);
			tvRight.setBackgroundResource(R.drawable.real_coupon);
			couponType = Util.getString(R.string.real_coupon);
			ivShare.setVisibility(View.VISIBLE);
			tvInsteadPrice.setVisibility(View.GONE);
		} else if (CustConst.Coupon.EXPERIENCE.equals(coupon.getCouponType())) { // 体验券
			canMoney = coupon.getFunction();
			tvSymbol.setVisibility(View.GONE);
			tvMoney.setVisibility(View.GONE);
			tvRight.setBackgroundResource(R.drawable.experience_coupon);
			couponType = Util.getString(R.string.experience);
			ivShare.setVisibility(View.VISIBLE);
			tvInsteadPrice.setVisibility(View.GONE);
		} else if (CustConst.Coupon.EXCHANGE_VOUCHER.equals(coupon.getCouponType())) { // 兑换券
			canMoney = coupon.getFunction();
			insteadPrice = coupon.getInsteadPrice();
			tvSymbol.setVisibility(View.GONE);
			tvMoney.setVisibility(View.VISIBLE);
			tvRight.setBackgroundResource(R.drawable.limmit_buy);
			couponType = Util.getString(R.string.exchange_voucher);
			double money = Util.isEmpty(coupon.getInsteadPrice()) ? 0 : Double.parseDouble(coupon.getInsteadPrice());
			if (money > 0) {
				ivShare.setVisibility(View.GONE);
				tvInsteadPrice.setVisibility(View.VISIBLE);
				tvMoney.setVisibility(View.VISIBLE);
			} else {
				ivShare.setVisibility(View.VISIBLE);
				tvInsteadPrice.setVisibility(View.GONE);
				tvMoney.setVisibility(View.GONE);
			}
		} else if (CustConst.Coupon.VOUCHER.equals(coupon.getCouponType())) { // 代金券
			tvSymbol.setVisibility(View.GONE);
			tvRight.setBackgroundResource(R.drawable.voucher);
			couponType = Util.getString(R.string.voucher);
			double payMoeny = Util.isEmpty(coupon.getPayPrice()) ? 0 : Double.parseDouble(coupon.getPayPrice());
			if (payMoeny > 0) {
				canMoney = "代" + coupon.getInsteadPrice();
				insteadPrice = coupon.getPayPrice();
				ivShare.setVisibility(View.GONE);
				tvInsteadPrice.setVisibility(View.VISIBLE);
				tvMoney.setVisibility(View.VISIBLE);
			} else {
				canMoney = coupon.getFunction();
				ivShare.setVisibility(View.VISIBLE);
				tvInsteadPrice.setVisibility(View.GONE);
				tvMoney.setVisibility(View.GONE);
			}
		}
		// 抵用金额
		tvInsteadPrice.setText(insteadPrice);
		// 达到多少金额可用
		((TextView) holder.getView(R.id.tv_coupon_gd)).setText(canMoney);
		// 优惠券类型
		tvCouponType.setText(couponType);
		// 有效时间
		String useTime = TimeUtils.getTime(coupon);
		useTime = Util.isEmpty(useTime) ? Util.getString(R.string.no_limit_time) : useTime;
		tvCouponDate.setText(useTime);
		// 使用时间
		String useDate = TimeUtils.getDate(coupon);
		useDate = Util.isEmpty(useDate) ? Util.getString(R.string.day_use) : useDate;
		tvCouponEffet.setText(Util.getString(R.string.use_coupon_time) + useDate);
		// 使用说明
		((TextView) holder.getView(R.id.tv_coupon_remark)).setText(Util.isEmpty(coupon.getRemark()) ? Util.getString(R.string.no_remark) : coupon.getRemark());
		// 距离
		TextView tvDistance = holder.getView(R.id.tv_distance);
		tvDistance.setText(TimeUtils.getDistance(coupon.getDistance()));
		// 分享
		ivShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ShareCouponUtil.shareCoupon(mActivity, coupon); // 分享
			}
		});
		// 判断是抢还是已经拥有优惠券
		setCouponGrab(coupon,tvGrabCoupon);
		// 抢优惠券
		final LinearLayout rightContent = holder.getView(R.id.right_content);
		String grabStatus = GRAD_RECIVE; // 初始状态是抢
		// 为空和抢
		if (Util.isEmpty(coupon.getCountMyReceived()) || coupon.getCountMyReceived().equals(GRAD_RECIVE)) {
			grabStatus = GRAD_RECIVE;
		} else if (coupon.getCountMyReceived().equals(BUY)) { // 购买
			grabStatus = BUY;
		} else if (coupon.getCountMyReceived().equals(COUNT)) { // 张数
			grabStatus = "X" + coupon.getCountMyActiveReceived();
		} else if (coupon.getCountMyReceived().equals(GRAB_OVER)) { // 抢完
			grabStatus = GRAB_OVER;
		} else if (coupon.getCountMyReceived().equals(GRAB_EXPIRED)) {
			grabStatus = GRAB_EXPIRED;
		} else {
			grabStatus = GRAD_RECIVE;
		}
		tvGrabCoupon.setText(grabStatus);
		
		// 抢优惠券
		rightContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String batchCouponCode = coupon.getBatchCouponCode();
				if (DB.getBoolean(DB.Key.CUST_LOGIN)) {
					double money = Util.isEmpty(coupon.getInsteadPrice()) ? 0 : Double.parseDouble(coupon
							.getInsteadPrice());
					double payMoeny = Util.isEmpty(coupon.getPayPrice()) ? 0 : Double.parseDouble(coupon.getPayPrice());

					if (CustConst.Coupon.VOUCHER.equals(coupon.getCouponType()) && payMoeny > 0
							|| CustConst.Coupon.EXCHANGE_VOUCHER.equals(coupon.getCouponType()) && money > 0) {
						Intent intent = new Intent(mActivity, CouponBuyActivity.class);
						intent.putExtra(CouponBuyFragment.COUPON, coupon.getBatchCouponCode());
						mActivity.startActivity(intent);
					} else {
						// 友盟统计
						MobclickAgent.onEvent(mActivity, "timelimit_robcoupon");
						rightContent.setEnabled(false);
						new GrabCouponHaveCountTask(mActivity, new GrabCouponHaveCountTask.Callback() {
							@Override
							public void getResult(JSONObject result) {
								rightContent.setEnabled(true);
								if (null == result) {
									// 优惠券抢券成功
								} else {
									int resultCode = Integer.parseInt(result.get("code").toString());
									if (resultCode == CustConst.Coupon.GRAB_SHOP_COUPON) { // 已经抢到同批次的优惠
										setGrabCouponStatus(position, GRAB_OVER,tvGrabCoupon);
									} else if (resultCode == CustConst.Coupon.GRAB_EXPIRED) {// 优惠券过期了
										setGrabCouponStatus(position, GRAB_EXPIRED,tvGrabCoupon);
									} else if (resultCode == CustConst.Coupon.GRAB_LIMIT) {// 优惠券抢的数量达到上限
										setGrabCouponStatus(position, GRAB_OVER,tvGrabCoupon);
									} else if (resultCode == CustConst.Coupon.GRAB_OVER) {// 抢完了
										setGrabCouponStatus(position, GRAB_OVER,tvGrabCoupon);
									} else if (resultCode == ErrorCode.SUCC) {
										mDatas.get(position).setCountMyReceived(COUNT); // 抢购的数量
										try {
											// 抢购的数量
											int count = Integer.parseInt(result.get("userCount").toString());
											mDatas.get(position).setCountMyActiveReceived(count);
											tvGrabCoupon.setText("X" + count);
										} catch (Exception e) {
											Log.e(TAG, "grab  coupon error >>> " + e.getMessage());
										}
									}  
								}
							}

						}).execute(batchCouponCode, String.valueOf(Util.NUM_TWO));

					}
				} else {
					SkipActivityUtil.login(Const.Login.SHOP_DETAIL); //
				}
			}

		});
		// 进入批次优惠券
		tvLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "getBatchCouponCode() >>> " + coupon.getBatchCouponCode());
				Intent intent = new Intent(mActivity, BatchCouponDetailActivity.class);
				intent.putExtra(BatchCouponDetailFragment.BATCH_COUPON_CODE, coupon.getBatchCouponCode());
				intent.putExtra(BatchCouponDetailFragment.BATCH_COUPON_TYPE, coupon.getCouponType());
				mActivity.startActivity(intent);
			}
		});

		final ImageView ivMoreShow = holder.getView(R.id.iv_coupon_up);
		final RelativeLayout rlMoreShow = holder.getView(R.id.hideOrShow);
		final Integer selectFlag = coupon.getShowMore();
		LinearLayout lyArrow = holder.getView(R.id.ly_arrow);
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
		return holder.getConvertView();

	}

	/**
	 * 抢优惠券
	 * @param position 位置
	 * @param status 抢到的状态
	 */
	private void setGrabCouponStatus (int position , String status,TextView tvGrabCoupon) {
		if (mDatas.get(position).getCountMyActiveReceived() > 0) {
			mDatas.get(position).setCountMyReceived(COUNT); // 抢购的数量
			tvGrabCoupon.setText(mDatas.get(position).getCountMyActiveReceived());
		} else {
			mDatas.get(position).setCountMyReceived(status);
			tvGrabCoupon.setText(status);
		}
	}
	
	/**
	 * 是抢优惠券还是已经拥有优惠券
	 * @param couponCount 张数
	 * @param tvCouponCount 张数的空间
	 * @param tvGrabCoupon 抢票的空间
	 */
	private void setCouponGrab(BatchCoupon coupon,TextView tvGrabCoupon) {
		if (CustConst.Coupon.VOUCHER.equals(coupon.getCouponType())) { // 代金券
			double payMoeny = Util.isEmpty(coupon.getPayPrice()) ? 0 : Double.parseDouble(coupon.getPayPrice());
			setCouponStatus(payMoeny, coupon, tvGrabCoupon);
		} else if (CustConst.Coupon.EXCHANGE_VOUCHER.equals(coupon.getCouponType())) {
			double money = Util.isEmpty(coupon.getInsteadPrice()) ? 0 : Double.parseDouble(coupon.getInsteadPrice());
			setCouponStatus(money, coupon, tvGrabCoupon);
		} else {
			if (coupon.getCountMyActiveReceived() > 0) {
				coupon.setCountMyReceived(COUNT); // 显示张数
			} else {
				coupon.setCountMyReceived(GRAD_RECIVE); // 去抢购
			}
		}
	}

	/**
	 * 重新设置优惠券的状态
	 * 
	 * @param money
	 * @param coupon
	 */
	private void setCouponStatus(double money, BatchCoupon coupon, TextView tvGrabCoupon) {
		if (money > 0 && coupon.getCountMyActiveReceived() == 0) {
			coupon.setCountMyReceived(BUY); // 购买
		} else if (money > 0 && coupon.getCountMyActiveReceived() > 0) {
			coupon.setCountMyReceived(COUNT); // 显示张数
		} else {
			coupon.setCountMyReceived(GRAD_RECIVE); // 去抢购
		}
	}

}
