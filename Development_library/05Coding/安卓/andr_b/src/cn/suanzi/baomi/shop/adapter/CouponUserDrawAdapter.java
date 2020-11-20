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
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.UserCardVip;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopConst;

/**
 * 优惠券列表信息显示的适配器
 * @author yanfang.li
 *
 */
public class CouponUserDrawAdapter extends CommonListViewAdapter<UserCardVip>{
	
	private final static String TAG = CouponUserDrawAdapter.class.getSimpleName();

	public CouponUserDrawAdapter(Activity activity, List<UserCardVip> datas) {
		super(activity, datas);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_user_draw, position);
		final UserCardVip user = mDatas.get(position);//在数据源中获取实体类对象
		
		ImageView ivUserhead = holder.getView(R.id.iv_userhead); // 头像
		TextView tvUsername = holder.getView(R.id.tv_username); // 昵称
		TextView tvDate = holder.getView(R.id.tv_date); // 领取的时间
		TextView tvDrawcontent = holder.getView(R.id.tv_drawcontent); // 领取的内容
		Util.showImage(mActivity, user.getAvatarUrl(), ivUserhead);
		tvUsername.setText(user.getNickName());
		tvDate.setText(user.getApplyTime()); // 获取时间
		String drawcontent = "";
		if (ShopConst.Coupon.DEDUCT.equals(user.getCouponType()+"")) {
			drawcontent = "领取了一张面值" + user.getInsteadPrice() + "元的抵扣券";
		} else if (ShopConst.Coupon.DISCOUNT.equals(user.getCouponType()+"") ) {
			drawcontent = "领取了一张" + user.getDiscountPercent() + "折的折扣券";
		} else if (ShopConst.Coupon.REAL_COUPON.equals(user.getCouponType()+"")) {
			drawcontent = "领取了一张可以" + user.getFunction() + "折的实物券";
		} else if (ShopConst.Coupon.N_BUY.equals(user.getCouponType()+"")){
			drawcontent = "领取了一张面值" + user.getInsteadPrice() + "元的N元购";
		} else if (ShopConst.Coupon.EXPERIENCE.equals(user.getCouponType()+"") ){
			drawcontent = "领取了一张可以" + user.getFunction() + "折的体验券";
		} else {
			drawcontent = "领取了一张优惠券";
		}
		tvDrawcontent.setText(drawcontent);
		return holder.getConvertView();
	
	}
}
