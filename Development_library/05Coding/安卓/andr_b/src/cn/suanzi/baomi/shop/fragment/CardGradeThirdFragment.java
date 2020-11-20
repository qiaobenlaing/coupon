// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// 
package cn.suanzi.baomi.shop.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.Card;
import cn.suanzi.baomi.base.utils.Calculate;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopConst;
import cn.suanzi.baomi.shop.activity.CardGradeListActivity;
import cn.suanzi.baomi.shop.model.AddCardTask;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 会员卡第三等级设置
 * 
 * @author yanfang.li
 */
public class CardGradeThirdFragment extends Fragment {

	private final static String TAG = "CardGradeSetFragment";

	/** 卡名称 */
	@ViewInject(R.id.tv_card_gd)
	TextView mtvCardGrade;
	/** 卡名称 */
	@ViewInject(R.id.tv_cardname)
	TextView mtvCardName;
	/** 积分 */
	@ViewInject(R.id.edt_card_gdpoint)
	EditText mEdtCardPoint;
	/** 折扣 */
	@ViewInject(R.id.edt_card_gddc)
	EditText mEdtCardDc;
	/** 每1元记多少分 */
	@ViewInject(R.id.edt_card_gdcount)
	EditText mEdtCardCount;
	/** 每多少分积1元 */
	/*
	 * @ViewInject(R.id.edt_card_point) EditText mEdtMoneyCount;
	 */
	/** 积分有效期 */
	@ViewInject(R.id.edt_card_gdvalid)
	EditText mEdtCardValid;
	/** 会员卡的对象 */
	private Card cardIntent;
	/** 完成按钮 */
	private Button mBtnOver;
	/** 会员卡修改的集合 */
	private List<Card> mCardDatas;
	private SharedPreferences mSharedPreferences;
	private String mFlag;

	public static CardGradeThirdFragment newInstance() {
		Bundle args = new Bundle();
		CardGradeThirdFragment fragment = new CardGradeThirdFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_card_gradedetail, container, false);
		ViewUtils.inject(this, view);
		Util.addLoginActivity(getActivity());
		mtvCardGrade.setText(getResources().getString(R.string.card_third));
		mtvCardName.setText(getResources().getString(R.string.card_platinumcard));
		LinearLayout ivReturn = (LinearLayout) view.findViewById(R.id.layout_turn_in);
		ivReturn.setVisibility(View.VISIBLE);
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_mid_content);
		tvTitle.setText(getResources().getString(R.string.card_gradeset));
		mBtnOver = (Button) view.findViewById(R.id.btn_card_save);
		mSharedPreferences = getActivity().getSharedPreferences(ShopConst.Card.CARD_FALG, Context.MODE_PRIVATE);
		mFlag = mSharedPreferences.getString(ShopConst.Card.CARD_FALG, null);
		Intent intent = getActivity().getIntent();
		cardIntent = (Card) intent.getSerializableExtra(CardSetGradeFragment.newInstance().CART_OBJ);
		Button btnSave = (Button) view.findViewById(R.id.btn_card_save);
		btnSave.setText(getResources().getString(R.string.toast_setover));
		if (ShopConst.Card.CARD_UPP.equals(mFlag)) {
			mCardDatas = (List<Card>) intent.getSerializableExtra(ShopConst.Card.CARD_LIST);
			for (int i = 0; i < mCardDatas.size(); i++) {
				Card card = mCardDatas.get(i);
				if (String.valueOf(Util.NUM_THIRD).equals(card.getCardLvl())) {
					mEdtCardPoint.setText(num(card.getDiscountRequire()));
					mEdtCardDc.setText(num(card.getDiscount()));
					mEdtCardCount.setText(num(card.getPointsPerCash()));
					mEdtCardValid.setText(num(card.getPointLifeTime()));
					// mEdtMoneyCount.setText(card.getOutPointsPerCash());
				}
			}
		}
		return view;
	}

	/**
	 * 去掉多余的0
	 * 
	 * @param str
	 * @return
	 */
	private String num(String str) {
		return Calculate.subZeroAndDot(Calculate.getNum(str));
	}

	/**
	 * 选项卡的切换
	 * 
	 * @param v
	 */
	@OnClick({ R.id.layout_turn_in, R.id.btn_card_laststep, R.id.btn_card_save })
	private void btnSkipClick(View v) {
		switch (v.getId()) {
		case R.id.btn_card_save:
			if (getDataValidate()) {
				break;
			}

			String cardLvl = cardIntent.getCardLvl() + "|" + String.valueOf(Util.NUM_THIRD);
			String cardName = cardIntent.getCardName() + "|" + mtvCardName.getText().toString();
			String cardPoint = cardIntent.getDiscountRequire() + "|" + mEdtCardPoint.getText().toString();
			String cardDc = cardIntent.getDiscount() + "|" + mEdtCardDc.getText().toString();
			String cardCount = cardIntent.getPointsPerCash() + "|" + mEdtCardCount.getText().toString();
			String cardValid = cardIntent.getPointLifeTime() + "|" + mEdtCardValid.getText().toString();
			String cardMoneyPoint = cardIntent.getOutPointsPerCash() + "|" + "0";
			Log.d(TAG, "cardMoneyPoint*****=" + cardMoneyPoint);
			String url = "";
			Log.d(TAG, "card****cardLvl" + cardLvl);
			Log.d(TAG, "card****cardName" + cardName);
			Log.d(TAG, "card****cardPoint" + cardPoint);
			Log.d(TAG, "card****cardDc" + cardDc);
			Log.d(TAG, "card****cardCount" + cardCount);
			Log.d(TAG, "card****cardValid" + cardValid);
			Log.d(TAG, "flag=" + mFlag);
			final List<Card> cardDatas = new ArrayList<Card>();
			for (int i = 0; i < 3; i++) {
				Card card = new Card();
				card.setCardLvl((i + 1) + "");
				card.setCardName(cardName.split("\\|")[i]);
				card.setDiscountRequire(cardPoint.split("\\|")[i]);
				card.setDiscount(cardDc.split("\\|")[i]);
				card.setPointsPerCash(cardCount.split("\\|")[i]);
				card.setPointLifeTime(cardValid.split("\\|")[i]);
				card.setOutPointsPerCash(cardMoneyPoint.split("\\|")[i]);
				cardDatas.add(card);
			}
			mBtnOver.setEnabled(false);
			// 判断是添加还是修改
			new AddCardTask(getActivity(), new AddCardTask.Callback() {

				@Override
				public void getResult(JSONObject result) {
					if (result == null) {
						mBtnOver.setEnabled(true);// 添加失败
					} else {
						mBtnOver.setEnabled(true);
						DB.saveBoolean(ShopConst.Key.CARD_ADD, true);
						Intent intent = new Intent(getActivity(), CardGradeListActivity.class);
						for (int i = 0; i < cardDatas.size(); i++) {
							Card card = cardDatas.get(i);
						}
						intent.putExtra(ShopConst.Card.CARD_REUTRN, (Serializable) cardDatas);
						Util.exit();
						startActivity(intent);
						getActivity().finish();
					}
				}
			}).execute(cardName, cardLvl, url, cardPoint, cardDc, cardValid, cardCount, cardMoneyPoint);
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
	 * 
	 * @param cardValid
	 * @param cardCount
	 * @param cardDc
	 * @param cardPoint
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
				Log.e(TAG, "会员卡折扣出错=" + e.getMessage());
			}
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
