// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.adapter;

import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.UserCardVip;
import cn.suanzi.baomi.shop.R;

/**
 * 会员卡列表
 * @author yanfang.li
 */
public class CardListAdapter extends CommonListViewAdapter<UserCardVip>{
	
	/**
	 * 确定泛型后必须写的构造方法
	 * @param context
	 * @param datas
	 */
	public CardListAdapter(Activity activity, List<UserCardVip> datas) {
		super(activity, datas);
	}
	
	@Override
	public void setItems(List<UserCardVip> datas) {
		// TODO Auto-generated method stub
		super.setItems(datas);
	}
	@Override
	public void addItems(List<UserCardVip> datas) {
		// TODO Auto-generated method stub
		super.addItems(datas);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, 
				R.layout.item_cardlist, position);
		UserCardVip user = (UserCardVip)getItem(position);//在数据源中获取实体类对象
		
		ImageView cardImage = ((ImageView) holder.getView(R.id.iv_card_list_pic));
		//根据对象属性改变界面
		Util.showImage(mActivity, user.getAvatarUrl(), cardImage);//显示图片
		Log.d("TAG", "aaaaa"+user.getRealName());
		//根据对象属性改变界面  
		((TextView) holder.getView(R.id.tv_cardvip_ls_name)).setText(user.getNickName());//昵称
		((TextView) holder.getView(R.id.tv_cardvip_ls_csm_times)).setText(user.getConsumeTimes()+" 次");//优惠券使用次数
		((TextView) holder.getView(R.id.tv_cardvip_ls_coupon_sum)).setText(user.getCouponUseAmount()+" 张");//优惠券使用多少张
		((TextView) holder.getView(R.id.tv_card_ls_id)).setText(user.getCardNbr());//会员卡的id
		((TextView) holder.getView(R.id.tv_card_ls_csm_total)).setText(user.getConsumePriceAmount()+" 元");//消费总额
		((TextView) holder.getView(R.id.tv_card_ls_ddt_total)).setText(user.getDeductionPrice()+" 元");//折扣
		
		return holder.getConvertView();
	}

}
