// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.adapter;

import java.math.BigDecimal;
import java.util.List;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Card;
import com.huift.hfq.shop.ShopConst;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * 优惠券设置界面适配器
 * @author yanfang.li
 */
public class CardQueryAdapter extends CommonListViewAdapter<Card> {
	private final static String TAG = CardQueryAdapter.class.getSimpleName();
	public CardQueryAdapter(Activity activity, List<Card> mdata) {
		super(activity, mdata);
		Log.d("TAG","mdata="+mdata.size());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_cardlv, position);
		Card card = (Card)getItem(position);//在数据源中获取实体类对象
		RelativeLayout rlCardStyle = holder.getView(R.id.rl_card_style);
		TextView tvCardPerson = holder.getView(R.id.tv_pnum);
		Log.d("TAG","card.getCardLvl()="+card.getCardLvl());
		String cardName = null;
		if (ShopConst.Card.LV_FIRST.equals(card.getCardLvl())) {
			rlCardStyle.setBackgroundResource(R.drawable.silver_card);
			cardName = mActivity.getResources().getString(R.string.card_silvercard);
		} else if (ShopConst.Card.LV_SECOND.equals(card.getCardLvl())) {
			rlCardStyle.setBackgroundResource(R.drawable.gold_card);
			cardName = mActivity.getResources().getString(R.string.card_goldcard);
		} else if (ShopConst.Card.LV_THIRD.equals(card.getCardLvl())) {
			rlCardStyle.setBackgroundResource(R.drawable.platinum_card);
			cardName = mActivity.getResources().getString(R.string.card_platinumcard);
		}
		if (Util.isEmpty(card.getVipNbr())) {
			tvCardPerson.setText("0人");
		} else {
			tvCardPerson.setText(card.getVipNbr()+"人");
		}
		
		// 消费积分
		((TextView)holder.getView(R.id.tv_cardnames)).setText(cardName);
		double cmsMoney = 0;
		if (!Util.isEmpty(card.getConsumeAmountCount())) {
			cmsMoney = Double.parseDouble(card.getConsumeAmountCount());
		} else {
			cmsMoney = 0;
		}
		double simplifyCsm = 0;
		Log.d(TAG, card.getConsumeAmountCount()+"消费");
		Log.d(TAG, cmsMoney+"1消费");
		if (cmsMoney >= 10000) {
			simplifyCsm = cmsMoney / 10000;
			// 装换成两位小数
			BigDecimal bdCsm = Util.saveTwoDecima(simplifyCsm);
			// 消费总金额
			((TextView)holder.getView(R.id.tv_cms)).setText("消费："+bdCsm+"万元");
		} else {
			BigDecimal bdCsmMoney = Util.saveTwoDecima(cmsMoney);
			// 消费总金额
			((TextView)holder.getView(R.id.tv_cms)).setText("消费："+bdCsmMoney+"元");
		}
		
		int point = 0;
		if (!Util.isEmpty(card.getPoints())) {
			point = Integer.parseInt(card.getPoints());
		} else {
			cmsMoney = 0;
		}
		double simplifyPoint = 0;
		if (point >= 10000) {
			simplifyPoint =(double) point / 10000;
			// 消费积分
			((TextView)holder.getView(R.id.tv_point)).setText("积分："+simplifyPoint+"万分");
		} else {
			// 消费积分
			((TextView)holder.getView(R.id.tv_point)).setText("积分："+point+"分");
		}
		
		return holder.getConvertView();
	}



}
