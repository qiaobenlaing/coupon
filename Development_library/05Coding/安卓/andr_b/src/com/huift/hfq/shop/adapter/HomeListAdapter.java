// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.EConsuming;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * 首页查询E支付的消费和优惠券的使用
 * @author ad
 */
public class HomeListAdapter extends CommonListViewAdapter<EConsuming> {

	private final static String TAG = HomeListAdapter.class.getSimpleName();
	/**
	 * 确定泛型后必须写的构造方法
	 * @param context
	 * @param datas
	 */
	public HomeListAdapter(Activity activity, List<EConsuming> datas) {
		super(activity, datas);
	}
		
	/**
	 * 重写父类的getView方法
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, 
				R.layout.item_home_listview, position);
		EConsuming eConsume = (EConsuming)getItem(position);//在数据源中获取实体类对象
		ImageView ivUserHead = holder.getView(R.id.iv_ecsm_pic);
		Util.showImage(mActivity, eConsume.getAvatarUrl(), ivUserHead);
		((TextView) holder.getView(R.id.tv_ecsm_money)).setText("消费 "+ Util.saveTwoDecima(Double.parseDouble(eConsume.getTotalPay())) + " 元");//E支付的 消费总金额
		((TextView) holder.getView(R.id.tv_couponddt_money)).setText("优惠券抵扣 "+ Util.saveTwoDecima(Double.parseDouble(eConsume.getCouponPay())) +" 元");//抵扣金额
		((TextView) holder.getView(R.id.tv_couponuse_nbr)).setText("优惠券使用 "+ eConsume.getCouponUsed() +" 张");//优惠券使用张数
		((TextView) holder.getView(R.id.tv_bounsddt_money)).setText("红包抵扣 "+ Util.saveTwoDecima(Double.parseDouble(eConsume.getBonusPay())) + " 元");//红包抵扣
		((TextView) holder.getView(R.id.tv_ecsm_pay)).setText("支付 "+ Util.saveTwoDecima(Double.parseDouble(eConsume.getRealPay())) + " 元");// 支付金额
		((TextView) holder.getView(R.id.tv_ecsm_code)).setText("标识码 "+ eConsume.getIdentityCode() + "");//标识码
		((TextView) holder.getView(R.id.tv_ecsm_point)).setText("积分 "+ eConsume.getPoint() + " 分");//积分
		String time = "";
		if (!Util.isEmpty(eConsume.getConsumeTime())) {
		    time = eConsume.getConsumeTime().replace("-", ".");
		}
		((TextView) holder.getView(R.id.tv_ecsm_time)).setText(time);//消费的时间
		return holder.getConvertView();
	} 
	 

}
