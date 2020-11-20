// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Card;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * 会员卡信息列表
 * @author yanfang.li
 */
public class CardInfoAdapter extends CommonListViewAdapter<Card>{
	
	/**
	 * 确定泛型后必须写的构造方法
	 * @param context
	 * @param datas
	 */
	public CardInfoAdapter(Context context, List<Card> datas) {
		super(context, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		CommenViewHolder holder = CommenViewHolder.get(mContext , convertView , parent, 
				R.layout.item_cardinfo, position);
		Card card = (Card)getItem(position);//在数据源中获取实体类对象
		
		ImageView cardImage = ((ImageView) holder.getView(R.id.iv_cardinfo_pic));
		
		//根据对象属性改变界面
		Util.showImage(mActivity, card.getUrl(), cardImage);//显示图片
		//根据对象属性改变界面  
		((TextView) holder.getView(R.id.tv_cardinfo_code)).setText(card.getCardCode());
		((TextView) holder.getView(R.id.tv_cardinfo_create_date)).setText(card.getCreateTime());
		if ("1".equals(card.getStatus())) {
			((TextView) holder.getView(R.id.tv_cardinfo_status)).setText("启用状态");
		} else if ("0".equals(card.getStatus())){
			((TextView) holder.getView(R.id.tv_cardinfo_status)).setText("禁用状态");
		}
		if ("1000".equals(card.getCardType())) {
			((TextView) holder.getView(R.id.tv_cardinfo_type)).setText("会员卡");
		} else if ("2000".equals(card.getCardType())) {
			((TextView) holder.getView(R.id.tv_cardinfo_type)).setText("储蓄卡");
		}
		
		return holder.getConvertView();
	}

}
