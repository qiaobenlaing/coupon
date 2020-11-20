// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// 
package com.huift.hfq.shop.fragment;

import java.io.Serializable;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Card;
import com.huift.hfq.base.utils.Calculate;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.CardGradeSecondActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 会员卡第一等级设置
 * @author yanfang.li
 */
public class CardGradeFirstFragment extends Fragment {
	
	private final static String TAG = CardGradeFirstFragment.class.getSimpleName();
	
	/** 卡名称*/
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
	/** 每多少分积1元*//*
	@ViewInject(R.id.edt_card_point)
	EditText mEdtMoneyCount;*/
	/** 积分有效期*/
	@ViewInject(R.id.edt_card_gdvalid)
	EditText mEdtCardValid;
	/** 会员卡修改的集合*/
	private List<Card> mCardDatas;
	private Card cardIntent ;
	/** 保存判断会员是添加还是修改*/
	private SharedPreferences mSharedPreferences;
	private  String mFlag;
	public static CardGradeFirstFragment newInstance() {
		Bundle args = new Bundle();
		CardGradeFirstFragment fragment = new CardGradeFirstFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	   View view = inflater.inflate(R.layout.fragment_card_gradedetail, container, false);
	    ViewUtils.inject(this,view);
	    Util.addLoginActivity(getActivity());
	 // 获取会员卡设置传过来的值
 		Intent intent =  getActivity().getIntent();
 	    cardIntent = (Card) intent.getSerializableExtra(CardSetGradeFragment.newInstance().CART_OBJ);
		
	 	    mSharedPreferences = getActivity().getSharedPreferences(ShopConst.Card.CARD_FALG, Context.MODE_PRIVATE);
			mFlag = mSharedPreferences.getString(ShopConst.Card.CARD_FALG, null);
			Log.d(TAG, "*******"+mFlag);
			if (ShopConst.Card.CARD_UPP.equals(mFlag)) {
				mCardDatas = (List<Card>) intent.getSerializableExtra(ShopConst.Card.CARD_LIST);
				for (int i = 0; i < mCardDatas.size(); i++) {
					Card card = mCardDatas.get(i);
					if (String.valueOf(Util.NUM_ONE).equals(card.getCardLvl())) {
						mEdtCardPoint.setText(num(card.getDiscountRequire()));
						mEdtCardDc.setText(num(card.getDiscount()));
						mEdtCardCount.setText(num(card.getPointsPerCash()));
						mEdtCardValid.setText(num(card.getPointLifeTime()));
						/*mEdtMoneyCount.setText(card.getOutPointsPerCash());*/
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
	 * 下一步下一步的点击事件
	 * @param v
	 */
	@OnClick({R.id.layout_turn_in,R.id.btn_card_laststep, R.id.btn_card_save })
	private void btnSkipClick(View v) {
		switch (v.getId()) {
		case R.id.btn_card_save:
			Util.addActivity(getActivity());
		    // 为空验证
	    	if (getDataValidate()){
	    		break;
	    	}
			Intent intent = new Intent(getActivity(), CardGradeSecondActivity.class);
			Card card = new Card();
			card.setIsRealNameRequired(cardIntent.getIsRealNameRequired());
			card.setIsSharable(cardIntent.getIsSharable());
			
			card.setCardLvl(String.valueOf(Util.NUM_ONE));
			card.setCardName(mtvCardName.getText().toString());
			card.setDiscountRequire(mEdtCardPoint.getText().toString());
			card.setDiscount(mEdtCardDc.getText().toString());
			card.setPointsPerCash(mEdtCardCount.getText().toString());
			card.setPointLifeTime(mEdtCardValid.getText().toString());
			card.setOutPointsPerCash("0");
			intent.putExtra(CardSetGradeFragment.newInstance().CART_OBJ, (Serializable)card);
			intent.putExtra(ShopConst.Card.CARD_LIST, (Serializable)mCardDatas);
			startActivity(intent);
			break;
		case R.id.layout_turn_in:
			getActivity().finish();
			break;
		case R.id.btn_card_laststep:
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
			String content = getResources().getString(R.string.toast_inputdenull);
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
				Log.d(TAG, "数字="+newCardDc);
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
					Log.e(TAG, "会员卡折扣出错="+e.getMessage());
				}
				
				/*if (Util.isEmpty(mEdtMoneyCount.getText().toString())) {
					Util.getContentValidate(getActivity(), content);
					flag = true;
					break;
				} */
				if (Util.isEmpty(mEdtCardValid.getText().toString())) {
					Util.getContentValidate(R.string.toast_inputdenull);
					flag = true;
					break;
				}
				
				break;
	
			default:
				break;
			}
			return flag;
		}
}
