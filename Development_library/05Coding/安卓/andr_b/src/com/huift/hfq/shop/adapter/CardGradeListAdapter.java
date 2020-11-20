// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.Card;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.base.utils.Calculate;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * 会员卡的等级的查询
 * 
 * @author yanfang.li
 */
public class CardGradeListAdapter extends CommonListViewAdapter<Card> {

	private final static String TAG = "CardGradeListAdapter";

	public CardGradeListAdapter(Activity activity, List<Card> mdata) {
		super(activity, mdata);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView, parent, R.layout.item_gradelist,
				position);
		Card card = (Card) getItem(position);// 在数据源中获取实体类对象
		String cardName = null;
		int cardImage = 0;
		if ("1".equals(card.getCardLvl())) {
			cardName = "银卡";
			cardImage = R.drawable.set_silvercard1;
		} else if ("2".equals(card.getCardLvl())) {
			cardName = "金卡";
			cardImage = R.drawable.set_goalcard1;
		} else if ("3".equals(card.getCardLvl())) {
			cardName = "白金卡";
			cardImage = R.drawable.set_platinumcard;
		}

		Shop shop = DB.getObj(DB.Key.SHOP_INFO, Shop.class);
		String ShopNames = "";
		String shopLogo = "";
		if (shop != null) {
			String shopName = shop.getShopName();
			shopLogo = shop.getLogoUrl();
			if (!Util.isEmpty(shopName)) {
				ShopNames = shopName;
			} else {
				ShopNames = "商店名称";
			}
		}

		((TextView) holder.getView(R.id.tv_valitetime))
				.setText("积分有效期 :" + Calculate.getNum(card.getPointLifeTime()) + "月");
		// ((TextView)holder.getView(R.id.tv_cardno)).setText(card.getCardCode());
		// // TODO
		// ((TextView)holder.getView(R.id.tv_cardno)).setText("101"); // TODO

		ImageView ivshopLogo = holder.getView(R.id.iv_shoplogo); // TODO
		if (Util.isEmpty(shopLogo)) {
			ivshopLogo.setBackgroundResource(R.drawable.card_logo);
		} else {
			Util.showImage(mActivity, shopLogo, ivshopLogo);
		}

		((TextView) holder.getView(R.id.tv_cardpoint)).setText(num(card.getDiscountRequire()) + " 分");
		((TextView) holder.getView(R.id.tv_cardenjoy)).setText(num(card.getDiscount()) + " 折");
		((TextView) holder.getView(R.id.tv_cardmoney)).setText(num(card.getPointsPerCash()) + " 分");
		// ((TextView)holder.getView(R.id.tv_jimoney)).setText(outPointsPerCash+" 分");
		((TextView) holder.getView(R.id.tv_card)).setText(cardName);
		((TextView) holder.getView(R.id.tv_shopname)).setText(ShopNames);// TODO
																			// 商店名称
		((RelativeLayout) holder.getView(R.id.ly_cardimage)).setBackgroundResource(cardImage);
		return holder.getConvertView();
	}
	
	private String num (String str) {
		return Calculate.subZeroAndDot(Calculate.getNum(str));
	}

}
