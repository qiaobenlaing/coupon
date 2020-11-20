package cn.suanzi.baomi.cust.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.adapter.CommenViewHolder;
import cn.suanzi.baomi.base.adapter.CommonListViewAdapter;
import cn.suanzi.baomi.base.pojo.Shop;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.util.SkipActivityUtil;

/**
 * 首页优惠券
 * @author yanfang.li
 */
public class HomeShopIcbcAdapter extends CommonListViewAdapter<Shop> {
	
	private final static String TAG = HomeShopIcbcAdapter.class.getSimpleName();
	private final static int HAS_SHOW = 1;

	public HomeShopIcbcAdapter(Activity activity, List<Shop> datas) {
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
		final CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView, parent, R.layout.item_shop_icbc_list,position);
		final Shop shop = mDatas.get(position);
		// 店铺详情
		final ImageView showImageView = holder.getView(R.id.shopImage);
		final TextView tvShopName = holder.getView(R.id.shopName);
		String shopName = "";
		if (shop.getShopName().length() >= 8) {
			shopName = shop.getShopName().substring(0, 8) + "...";
		} else {
			shopName = shop.getShopName();
		}
		tvShopName.setText(shopName);
		Util.showImage(mActivity, shop.getLogoUrl(), showImageView);

		TextView tvShopType = holder.getView(R.id.tv_shop_type); // 商家类型
		TextView tvPopularity = holder.getView(R.id.tv_popularity); // 人气
		LinearLayout lyHomeBenefit = holder.getView(R.id.ly_home_benefit); // 惠
		ImageView ivHomeIcbcdiscount = holder.getView(R.id.iv_icbc); // 工
		LinearLayout lyHomeAct = holder.getView(R.id.ly_home_act); // 活
		TextView tvIcbcDiscount = holder.getView(R.id.icbc_discount); // 工行卡折扣
		ImageView ivIsnew = holder.getView(R.id.iv_isnew); // 头像上新
		tvIcbcDiscount.setText(shop.getOnlinePaymentDiscount() + Util.getString(R.string.discount_unit));
		// 商家上新
		if (shop.getIsNewShop() == HAS_SHOW) {
			ivIsnew.setVisibility(View.VISIBLE);
		} else {
			ivIsnew.setVisibility(View.GONE);
		}
		// 首单立减
		if (shop.getHasCoupon() == HAS_SHOW) {
			lyHomeBenefit.setVisibility(View.VISIBLE);
		} else {
			lyHomeBenefit.setVisibility(View.GONE);
		}
		// 工行卡折扣
		if (shop.getHasIcbcDiscount() == HAS_SHOW) {
			ivHomeIcbcdiscount.setVisibility(View.VISIBLE);
		} else {
			ivHomeIcbcdiscount.setVisibility(View.GONE);
		}
		// 活动
		if (shop.getHasAct() == HAS_SHOW) {
			lyHomeAct.setVisibility(View.VISIBLE);
		} else {
			lyHomeAct.setVisibility(View.GONE);
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
		String street = "";
		if (!Util.isEmpty(shop.getCity())) {
			street = shop.getCity();
		} else {
			street = "";
		}
		if (Util.isEmpty(shop.getStreet())) {
			street = street + "";
		} else {
			street = shop.getStreet();
		}
		((TextView) holder.getView(R.id.street)).setText(street);
		
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
	
	
	private String getString(int stringid) {
		return mActivity.getResources().getString(stringid);
	}
}
