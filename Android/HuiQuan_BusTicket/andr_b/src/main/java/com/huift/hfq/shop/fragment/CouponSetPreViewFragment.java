// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.BatchCoupon;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.base.utils.TimeUtils;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.model.AddBatchCouponTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 优惠券设置预览界面，预览后提交，下面还有分享功能
 * @author yanfang.li
 */
public class CouponSetPreViewFragment extends Fragment {

	/** 每批次的对象 */
	private BatchCoupon batch;
	/** 保存按钮*/
	private Button mBtnSave;

	public static CouponSetPreViewFragment newInstance() {

		Bundle args = new Bundle();
		CouponSetPreViewFragment fragment = new CouponSetPreViewFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_coupon_set_preview,container, false);
		ViewUtils.inject(this, v);
		Util.addActivity(getActivity());
		Util.addLoginActivity(getActivity());
		init(v);
		return v;
	}

	/**
	 * 初始化方法
	 * @param v
	 */
	private void init(View v) {

		Intent intent = getActivity().getIntent();
		batch = (BatchCoupon) intent.getSerializableExtra(CouponSetFullCutFragment.BATCH);
		Shop shop = DB.getObj(DB.Key.SHOP_INFO,Shop.class);
		mBtnSave = (Button) v.findViewById(R.id.btn_coupon_commit);
		LinearLayout inReturn = (LinearLayout) v.findViewById(R.id.layout_turn_in);
		inReturn.setVisibility(View.VISIBLE);
		TextView tvCouponName = (TextView) v.findViewById(R.id.tv_mid_content);
		tvCouponName.setText(getResources().getString(R.string.coupon_perview));
		// 保存activity
		Util.addActivity(getActivity());
		// 优惠券面值
		TextView tvPreviewMoney = (TextView) v.findViewById(R.id.tv_insteadPrice);
		// 满多少元可以消费
		TextView tvPreviewCanCsm = (TextView) v.findViewById(R.id.tv_availableMoney);
		// 每次可消费的张数
		TextView tvUseRule= (TextView) v.findViewById(R.id.tv_userule);
		// 消费券有效日期
		TextView tvUseDayDate = (TextView) v.findViewById(R.id.tv_usetime);
		// 温馨提示
		TextView tvCouponTips = (TextView) v.findViewById(R.id.tv_couponadd_tips);
		// 钱的标识符
		TextView tvMoney = (TextView) v.findViewById(R.id.tv_money);
		// 折扣
		TextView tvSymbol= (TextView) v.findViewById(R.id.tv_symbol);
		// 优惠券头像
		ImageView ivCouponpic = (ImageView) v.findViewById(R.id.iv_couponpic);
		// 商店名称
		TextView tvShopName = (TextView) v.findViewById(R.id.tv_shopname);
		// 优惠券类型名称
		TextView tvCouponTypeName = (TextView) v.findViewById(R.id.tv_couponname);
		// 限时购限使用的时间
		TextView tvUseLimitDate = (TextView) v.findViewById(R.id.tv_limitdate);
		// 优惠券的被景图
		RelativeLayout rlPrice = (RelativeLayout)v.findViewById(R.id.rl_price);

		CheckBox ckbArrow = (CheckBox) v.findViewById(R.id.ckb_arrow);
		final LinearLayout moreContent = (LinearLayout) v.findViewById(R.id.ly_previewmore);

		if (shop != null) {
			Util.showImage(getActivity(), shop.getLogoUrl(), ivCouponpic);
			if (!Util.isEmpty(shop.getShopName())) {
				tvShopName.setText(shop.getShopName());
			} else {
				tvShopName.setText(getString(R.string.shopname));
			}
		}

		// 优惠券介绍
		String canMoney = "";
		// 面值
		String insteadPrice = "";
		// 优惠券类型名称
		String couponTypeName = "";
		if (ShopConst.Coupon.DEDUCT.equals(batch.getCouponType())) { // 抵扣券
			rlPrice.setBackgroundResource(R.drawable.full_send);
			canMoney = "满" + batch.getAvailablePrice() + "元立减" +  batch.getInsteadPrice() + "元";
			couponTypeName = getString(R.string.coupon_deduct);
			tvSymbol.setVisibility(View.GONE);
			tvMoney.setVisibility(View.VISIBLE);
			insteadPrice = batch.getInsteadPrice();
		} else if (ShopConst.Coupon.DISCOUNT.equals(batch.getCouponType())) { //折扣券
			rlPrice.setBackgroundResource(R.drawable.full_cut);
			canMoney = "满" + batch.getAvailablePrice()+"元打" + batch.getDiscountPercent() + "折";
			couponTypeName = getString(R.string.coupon_discount);
			insteadPrice = batch.getDiscountPercent()+"";
			tvSymbol.setVisibility(View.VISIBLE);
			tvMoney.setVisibility(View.GONE);
		} else if (ShopConst.Coupon.EXCHANGE_VOUCHER.equals(batch.getCouponType())) { // 兑换券
			rlPrice.setBackgroundResource(R.drawable.limmit_buy);// TODO
			canMoney = batch.getFunction();
			tvSymbol.setVisibility(View.GONE);
			tvMoney.setVisibility(View.VISIBLE);
			insteadPrice = batch.getInsteadPrice();
			couponTypeName = getString(R.string.exchange_voucher);
		} else if (ShopConst.Coupon.VOUCHER.equals(batch.getCouponType())) { // 代金券
			rlPrice.setBackgroundResource(R.drawable.voucher);// TODO
			canMoney = batch.getFunction();
			tvSymbol.setVisibility(View.GONE);
			tvMoney.setVisibility(View.VISIBLE);
			insteadPrice = batch.getInsteadPrice();
			couponTypeName = getString(R.string.voucher);
		}

		tvPreviewCanCsm.setText(canMoney);
		tvCouponTypeName.setText(couponTypeName); // 优惠券类型名称
		tvUseRule.setText(batch.getRemark());
		tvCouponTips.setText(getResources().getString(R.string.coupon_tips));
		// 使用时间
		String useTime = TimeUtils.getTime(batch);
		if (Util.isEmpty(useTime)) {
			tvUseLimitDate.setText(getActivity().getResources().getString(R.string.no_limit_time));
		} else {
			tvUseLimitDate.setText(useTime);
		}

		// 每天使用时间
		String useDate = TimeUtils.getDate( batch);
		if (Util.isEmpty(useDate)) {
			tvUseDayDate.setText(getActivity().getResources().getString(R.string.use_day_time)
					+ getActivity().getResources().getString(R.string.day_use));
		} else {
			tvUseDayDate.setText(getActivity().getResources().getString(R.string.use_day_time) + useDate);
		}
		tvPreviewMoney.setText(insteadPrice); //预览金额

		ckbArrow.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					moreContent.setVisibility(View.VISIBLE);
				}else{
					moreContent.setVisibility(View.GONE);
				}
			}
		});

	}

	/**
	 * 预览和第二个上一步的时间
	 *
	 * @param v
	 */
	@OnClick({ R.id.btn_coupon_commit, R.id.btn_last_step ,R.id.layout_turn_in})
	private void btnNextClic(View v) {
		switch (v.getId()) {
			case R.id.layout_turn_in:// 返回
				getActivity().finish();
				break;
			case R.id.btn_last_step:// 第二个上一步
				getActivity().finish();
				break;
			case R.id.btn_coupon_commit://
				// 获得一个用户信息对象
				String []params = {batch.getCouponType(),batch.getTotalVolume()+"",batch.getStartUsingTime(),
						batch.getExpireTime(),batch.getDayStartUsingTime(),batch.getDayEndUsingTime(),
						batch.getStartTakingTime(),batch.getEndTakingTime(),batch.getIsSend(),
						batch.getSendRequired(),batch.getRemark(),batch.getDiscountPercent()+"",
						batch.getInsteadPrice(),batch.getAvailablePrice(),batch.getFunction(),
						batch.getLimitedNbr(),batch.getNbrPerPerson(),batch.getLimitedSendNbr(),batch.getPayPrice() };
				mBtnSave.setEnabled(false);
				new AddBatchCouponTask(getActivity(),new AddBatchCouponTask.Callback() {

					@Override
					public void getResult(int result) {
						if (result == ErrorCode.SUCC) {
							mBtnSave.setEnabled(true);
							DB.saveBoolean(ShopConst.Key.COUPON_ADD, true);
						} else {
							mBtnSave.setEnabled(true);
						}
					}
				}).execute(params);
				break;

			default:
				break;
		}

	}


}
