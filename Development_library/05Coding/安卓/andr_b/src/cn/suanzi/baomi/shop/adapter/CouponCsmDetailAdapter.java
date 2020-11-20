// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.suanzi.baomi.base.pojo.CouponCsmList;
import cn.suanzi.baomi.shop.R;

/**
 * 某一批次优惠券的列表适配器
 * @author yanfang.li
 */
public class CouponCsmDetailAdapter extends CommonListViewAdapter<CouponCsmList>{

	public CouponCsmDetailAdapter(Context context, List<CouponCsmList> datas) {
		super(context, datas);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mContext , convertView , parent, 
				R.layout.item_couption_consume, position);
		CouponCsmList c = (CouponCsmList)getItem(position);//在数据源中获取实体类对象
		((TextView) holder.getView(R.id.tv_coupon_csm_id)).setText(c.getCouponNbr());//券号
		((TextView) holder.getView(R.id.tv_coupon_csm_pid)).setText(c.getUserName());//使用人
		((TextView) holder.getView(R.id.tv_coupon_csm_cost)).setText(c.getInsteadPrice());//面值
		((TextView) holder.getView(R.id.tv_coupon_csm_money)).setText(c.getConsumeAmount());//金额
		((TextView) holder.getView(R.id.tv_coupon_csm_date)).setText(c.getConsumeTime());//消费时间
		
		return holder.getConvertView();
	}

}
