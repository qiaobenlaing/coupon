package cn.suanzi.baomi.cust.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.adapter.CommenViewHolder;
import cn.suanzi.baomi.base.adapter.CommonListViewAdapter;
import cn.suanzi.baomi.base.pojo.Shop;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.util.SkipActivityUtil;

/**
 * 分类检索商户
 * 
 * @author qian.zhou ，yanfang.li
 */
public class ShopAdapter extends CommonListViewAdapter<Shop> {
	private final static String TAG = ShopAdapter.class.getSimpleName();
	private final static int HAS_SHOW = 1;

	public ShopAdapter(Activity activity, List<Shop> datas) {
		super(activity, datas);
	}

	@Override
	public void setItems(List<Shop> shops) {
		super.setItems(shops);
	}

	@Override
	public void addItems(List<Shop> shops) {
		super.addItems(shops);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView, parent, R.layout.item_shop,
				position);
		final Shop shop = mDatas.get(position);
		// 店铺详情
		final ImageView showImageView = holder.getView(R.id.shopImage);
		final TextView tvShopName = holder.getView(R.id.shopName);
		tvShopName.setText(shop.getShopName());
		Util.showImage(mActivity, shop.getLogoUrl(), showImageView);

		TextView tvShopType = holder.getView(R.id.tv_shop_type); // 商家类型
		TextView tvPopularity = holder.getView(R.id.tv_popularity); // 人气
		TextView tvGotoShop = holder.getView(R.id.tv_goto_shop); // 进入店铺
		TextView tvnCoupon = holder.getView(R.id.tv_shop_coupon); // 优惠券
		TextView tvnAct = holder.getView(R.id.tv_shop_act); // 活动
		TextView tvnNew = holder.getView(R.id.tv_shop_new); // 上新
		TextView tvnIsFisrt = holder.getView(R.id.tv_isfirst); // 首单立减
		TextView tvIcbcdiscount = holder.getView(R.id.tv_hasicbcdiscount); // 工行卡折扣

		// 首单立减
		if (shop.getIsFirst() == HAS_SHOW) {
			tvnIsFisrt.setVisibility(View.VISIBLE);
		} else {
			tvnIsFisrt.setVisibility(View.GONE);
		}

		// 工行卡折扣
		if (shop.getHasIcbcDiscount() == HAS_SHOW) {
			tvIcbcdiscount.setVisibility(View.VISIBLE);
		} else {
			tvIcbcdiscount.setVisibility(View.GONE);
		}
		// 商店优惠券
		if (shop.getHasCoupon() == HAS_SHOW) {
			tvnCoupon.setVisibility(View.VISIBLE);
		} else {
			tvnCoupon.setVisibility(View.GONE);
		}

		//
		if (shop.getIsFirst() == HAS_SHOW && shop.getHasIcbcDiscount() == HAS_SHOW && shop.getHasCoupon() == HAS_SHOW) {
			tvnAct.setVisibility(View.GONE);
			tvnNew.setVisibility(View.GONE);
		} else if ((shop.getIsFirst() != HAS_SHOW && shop.getHasIcbcDiscount() == HAS_SHOW && shop.getHasCoupon() == HAS_SHOW)
				|| (shop.getIsFirst() == HAS_SHOW && shop.getHasIcbcDiscount() != HAS_SHOW && shop.getHasCoupon() == HAS_SHOW)
				|| (shop.getIsFirst() == HAS_SHOW && shop.getHasIcbcDiscount() == HAS_SHOW && shop.getHasCoupon() != HAS_SHOW)) {
			// 商店活动
			if (shop.getHasAct() == HAS_SHOW) {
				tvnAct.setVisibility(View.VISIBLE);
			} else {
				tvnAct.setVisibility(View.GONE);
				// 商店上新
				if (shop.getHasNew() == HAS_SHOW) {
					tvnNew.setVisibility(View.VISIBLE);
				} else {
					tvnNew.setVisibility(View.GONE);
				}
			}
		} else {
			setLable(shop, tvnAct, tvnNew);
		}

		// 人气
		tvPopularity.setText(getString(R.string.popularity) + shop.getPopularity());
		// 商家类型 0-所有类型；1-美食；2-咖啡；3-健身；4-娱乐；5-服装；6-其他；

		String type = "";
		if (shop.getType() == CustConst.Shop.ALL_TYPE) { // 所有
			type = getString(R.string.type_all);
		} else if (shop.getType() == CustConst.Shop.CLOTHING) { // 服裝
			type = getString(R.string.type_Clothing);
		} else if (shop.getType() == CustConst.Shop.COFFEE) { // 咖啡
			type = getString(R.string.coffee_home);
		} else if (shop.getType() == CustConst.Shop.ENTERTAINMENT) { // 娱乐
			type = getString(R.string.type_entertainment);
		} else if (shop.getType() == CustConst.Shop.FITNESS) { // 健身
			type = getString(R.string.type_fitness);
		} else if (shop.getType() == CustConst.Shop.FOOD) { // 食物
			type = getString(R.string.type_food);
		} else if (shop.getType() == CustConst.Shop.OTHER) { // 其他
			type = getString(R.string.type_other);
		}
		tvShopType.setText(type);
		// 判断距离
		TextView tvDistance = holder.getView(R.id.distance);
		String distatnceSimple = "";
		if (Util.isEmpty(shop.getDistance())) {
			distatnceSimple = " 0 M" ;
		} else {
			String distanceSrc = shop.getDistance().replace(",", "").replace(".", "");
			try {
				int distance = Integer.parseInt(distanceSrc);
				if (distance > 1000) {
					int dist = distance / 1000;
					if (dist > 100) {
						distatnceSimple = ">100 Km";
					} else {
						distatnceSimple = String.valueOf(dist) + " Km";
					}
				} else {
					distatnceSimple = String.valueOf(distance) + " M";
				}
			} catch (Exception e) {
				return null;
			}
			tvDistance.setText(distatnceSimple);
		}
		// 街道
		((TextView) holder.getView(R.id.street)).setText(shop.getStreet());

		// 进入店铺
		OnClickListener gotoShopClick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.tv_goto_shop:
					String shopCode = mDatas.get(position).getShopCode();
					SkipActivityUtil.skipNewShopDetailActivity(mActivity, shopCode);
					break;

				default:
					break;
				}
			}
		};
		tvGotoShop.setOnClickListener(gotoShopClick);
		// 进入店铺
		View view = holder.getConvertView();
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String shopCode = mDatas.get(position).getShopCode();
				SkipActivityUtil.skipNewShopDetailActivity(mActivity, shopCode);
			}
		});
		return view;
	}

	/**
	 * 设置标签
	 * 
	 * @param shop
	 *            店铺对象
	 * @param tvnAct
	 *            活动
	 * @param tvnNew
	 *            上新
	 */
	private void setLable(Shop shop, TextView tvnAct, TextView tvnNew) {
		// 商店活动
		if (shop.getHasAct() == HAS_SHOW) {
			tvnAct.setVisibility(View.VISIBLE);
		} else {
			tvnAct.setVisibility(View.GONE);
		}
		// 商店上新
		if (shop.getHasNew() == HAS_SHOW) {
			tvnNew.setVisibility(View.VISIBLE);
		} else {
			tvnNew.setVisibility(View.GONE);
		}
	}

	private String getString(int stringid) {
		return mActivity.getResources().getString(stringid);
	}
}
