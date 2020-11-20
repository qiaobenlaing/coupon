// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.adapter.CommonListViewAdapter;
import cn.suanzi.baomi.base.pojo.BatchCoupon;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopConst;

/**
 * 优惠券设置界面适配器
 * @author yanfang.li
 */
public class CouponSetAdapter extends CommonListViewAdapter<BatchCoupon> {
	
	private static final String TAG = CouponSetAdapter.class.getSimpleName();
	public CouponSetAdapter(Activity activity, List<BatchCoupon> mdata) {
		super(activity, mdata);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, 
				R.layout.item_coupon_set, position);
		BatchCoupon batch = (BatchCoupon)getItem(position); // 在数据源中获取实体类对象
		ImageView ivCoupon = holder.getView(R.id.iv_couponset);
		Util.showBannnerImage(mActivity, batch.getCouponImg(), ivCoupon);
		if (batch.getCouponType().equals(ShopConst.Coupon.N_BUY) 
			|| batch.getCouponType().equals(ShopConst.Coupon.REAL_COUPON)
			|| batch.getCouponType().equals(ShopConst.Coupon.EXPERIENCE)) {
			ivCoupon.setVisibility(View.GONE);
		} else {
			ivCoupon.setVisibility(View.VISIBLE);
		}
		return holder.getConvertView();
	}



}
