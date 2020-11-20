// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// 
package com.huift.hfq.shop.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Card;
import com.huift.hfq.base.utils.Calculate;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.CardGradeThirdActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 会员卡第二等级设置
 * @author yanfang.li
 */
public class CardGradeSecondFragment extends Fragment {

	/** 卡名称*/
	@ViewInject(R.id.tv_card_gd)
	TextView mtvCardGrade;
	/** 会员卡名称*/
	@ViewInject(R.id.tv_cardname)
	TextView mtvCardName;
	/** 积分*/
	@ViewInject(R.id.edt_card_gdpoint)
	EditText mEdtCardPoint;
	/** 折扣*/
	@ViewInject(R.id.edt_card_gddc)
	EditText mEdtCardDc;
	/** 每1元记多少分*/
	@ViewInject(R.id.edt_card_gdcount)
	EditText mEdtCardCount;
	/*	*//** 每多少分积1元*//*
	@ViewInject(R.id.edt_card_point)
	EditText mEdtMoneyCount;*/
	/** 积分有效期*/
	@ViewInject(R.id.edt_card_gdvalid)
	EditText mEdtCardValid;
	/** 会员卡修改的集合*/
	private List<Card> mCardDatas;
	private List<Card> mCardsReturn;
	/** 传过来的对象*/
	private Intent intentobj;
	/** 保存判断会员是添加还是修改*/
	private SharedPreferences mSharedPreferences;
	private  String mFlag;

	public static CardGradeSecondFragment newInstance() {
		Bundle args = new Bundle();
		CardGradeSecondFragment fragment = new CardGradeSecondFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_card_gradedetail, container, false);
		ViewUtils.inject(this,view);
		Util.addLoginActivity(getActivity());
		mtvCardGrade.setText(getResources().getString(R.string.card_second));
		mtvCardName.setText(getResources().getString(R.string.card_goldcard));
		intentobj =  getActivity().getIntent();
		mSharedPreferences = getActivity().getSharedPreferences(ShopConst.Card.CARD_FALG, Context.MODE_PRIVATE);
		mFlag = mSharedPreferences.getString(ShopConst.Card.CARD_FALG, null);
		if (ShopConst.Card.CARD_UPP.equals(mFlag)) {
			mCardDatas = (List<Card>) intentobj.getSerializableExtra(ShopConst.Card.CARD_LIST);
			for (int i = 0; i < mCardDatas.size(); i++) {
				Card card = mCardDatas.get(i);
				if (String.valueOf(Util.NUM_TWO).equals(card.getCardLvl())) {
					mEdtCardPoint.setText(num(card.getDiscountRequire()));
					mEdtCardDc.setText(num(card.getDiscount()));
					mEdtCardCount.setText(num(card.getPointsPerCash()));
					mEdtCardValid.setText(num(card.getPointLifeTime()));
//						mEdtMoneyCount.setText(card.getOutPointsPerCash());
				}
			}
		}
		LinearLayout ivReturn = (LinearLayout) view.findViewById(R.id.layout_turn_in);
		ivReturn.setVisibility(View.VISIBLE);
		TextView tvTitle = (TextView)view.findViewById(R.id.tv_mid_content);
		tvTitle.setText(getResources().getString(R.string.card_gradeset));
		return view;
	}

	/**
	 * 去掉多余的0
	 * @param str
	 * @return
	 */
	private String num (String str) {
		return Calculate.subZeroAndDot(Calculate.getNum(str));
	}
	/**
	 * 下一步
	 * @param v
	 */
	@OnClick({R.id.layout_turn_in,R.id.btn_card_laststep, R.id.btn_card_save })
	private void btnSkipClick(View v) {
		switch (v.getId()) {
			case R.id.btn_card_save:
				Util.addActivity(getActivity());
				if (getDataValidate()) {
					break;
				}
				Intent intent = new Intent(getActivity(), CardGradeThirdActivity.class);
				Card cardIntent = (Card) intentobj.getSerializableExtra(CardSetGradeFragment.newInstance().CART_OBJ);
				Card card = new Card();
				card.setCardLvl(cardIntent.getCardLvl() +"|"+ String.valueOf(Util.NUM_TWO));
				card.setCardName(cardIntent.getCardName() +"|"+ mtvCardName.getText().toString());
				card.setDiscountRequire(cardIntent.getDiscountRequire()+"|"+ mEdtCardPoint.getText().toString());
				card.setDiscount(cardIntent.getDiscount()+"|"+ mEdtCardDc.getText().toString());
				card.setPointsPerCash(cardIntent.getPointsPerCash() +"|"+ mEdtCardCount.getText().toString());
				card.setPointLifeTime(cardIntent.getPointLifeTime() +"|"+ mEdtCardValid.getText().toString());
				card.setOutPointsPerCash(cardIntent.getOutPointsPerCash()+"|"+"0");
				intent.putExtra(CardSetGradeFragment.newInstance().CART_OBJ, (Serializable)card);
				intent.putExtra(ShopConst.Card.CARD_LIST, (Serializable)mCardDatas);
				startActivity(intent);
				break;
			case R.id.layout_turn_in:
				getActivity().finish();
				break;
			case R.id.btn_card_laststep:
				// 保存添加的数据，以便返回时还存在
				mCardsReturn = new ArrayList<Card>();
				getActivity().finish();
				break;
			default:
				break;
		}
	}

	/**
	 * 为空验证
	 */
	private boolean getDataValidate() {
		boolean flag = false;
		switch (1) {
			case 1:
				if (Util.isEmpty(mEdtCardPoint.getText().toString())) {
					Util.getContentValidate(R.string.toast_inputdenull);
					flag = true;
					break;
				}
				if (Util.isEmpty(mEdtCardCount.getText().toString())) {
					Util.getContentValidate(R.string.toast_inputdenull);
					flag = true;
					break;
				}
				if (Util.isEmpty(mEdtCardDc.getText().toString())) {
					Util.getContentValidate(R.string.toast_inputdenull);
					flag = true;
					break;
				}
				String newCardDc = Calculate.getNum(mEdtCardDc.getText().toString());
				newCardDc = Calculate.subZeroAndDot(newCardDc); // 去掉小数位后的零
				if (newCardDc.length() > 3) {
					Util.getContentValidate(R.string.card_gddc);
					flag = true;
					break;
				}
				try {
					double discount = Double.parseDouble(mEdtCardDc.getText().toString());
					if (discount >= 0 && discount <= 10) {
						flag = false;
					} else {
						Util.getContentValidate(R.string.card_gddc);
						flag = true;
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				/*if (Util.isEmpty(mEdtMoneyCount.getText().toString())) {
					Util.getContentValidate(getActivity(), content);
					flag = true;
					break;
				}*/
				if (Util.isEmpty(mEdtCardValid.getText().toString())) {
					Util.getContentValidate(R.string.card_gddc);
					flag = true;
					break;
				}

				break;

			default:
				break;
		}
		return flag;
	}


	/**
	 * 保存要添加的数据
	 * @param activity

	public void saveSharedPreferences(String savaPojo,Activity activity, String [][]value) {
	mSharedPreferences = activity.getSharedPreferences(savaPojo, Context.MODE_PRIVATE);
	Editor editor = mSharedPreferences.edit();
	for (int i = 0; i < value.length; i++) {
	editor.putString(value[i][0], value[i][1]);
	}
	editor.commit();

	}*/

}
