// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.fragment;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.Card;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopConst;
import cn.suanzi.baomi.shop.activity.CardGradeFirstActivity;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 会员卡等级设置
 * 
 * @author yanfang.li
 */
public class CardSetGradeFragment extends Fragment {

	private final static String TAG = "CardSetGradeFragment";
	public final static String CART_OBJ = "card";

	/** 会员等级 **/
	@ViewInject(R.id.tv_card_gdsum)
	private TextView mTvCardGrade;
	/** 是否实名制 **/
	private String mIsRealName = "0";
	/** 是否转借 **/
	private String mIsSharable = "0";
	/** 会员卡修改的集合 */
	private List<Card> mCardDatas;
	private SharedPreferences mSharedPreferences;
	private String mFlag;

	public static CardSetGradeFragment newInstance() {
		Bundle args = new Bundle();
		CardSetGradeFragment fragment = new CardSetGradeFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_card_grade, container,
				false);
		ViewUtils.inject(this, v);
		Util.addLoginActivity(getActivity());
		mSharedPreferences = getActivity().getSharedPreferences(ShopConst.Card.CARD_FALG, Context.MODE_PRIVATE);
		Intent intent = getActivity().getIntent();

		mFlag = mSharedPreferences.getString(ShopConst.Card.CARD_FALG, null);
		Log.d(TAG, "*******" + mFlag);
		if (ShopConst.Card.CARD_UPP.equals(mFlag)) {
			mCardDatas = (List<Card>) intent.getSerializableExtra(ShopConst.Card.CARD_LIST);
		}
		LinearLayout ivReturn = (LinearLayout) v.findViewById(R.id.layout_turn_in);
		ivReturn.setVisibility(View.VISIBLE);
		TextView tvTitle = (TextView)v.findViewById(R.id.tv_mid_content);
		tvTitle.setText(getResources().getString(R.string.card_gradeset));
		return v;
	}

	/**
	 * 点击跳转事件
	 * 
	 * @param view
	 */
	@OnClick({ R.id.layout_turn_in, R.id.btn_card_next })
	private void btnSkipClick(View view) {
		switch (view.getId()) {
		case R.id.layout_turn_in:
			getActivity().finish();
			break;
		case R.id.btn_card_next:
			Intent intent = new Intent(getActivity(),CardGradeFirstActivity.class);
			Card card = new Card();
			card.setIsRealNameRequired(mIsRealName);
			card.setIsSharable(mIsSharable);
			intent.putExtra(CART_OBJ, card);
			intent.putExtra(ShopConst.Card.CARD_LIST, (Serializable) mCardDatas);
			Util.addActivity(getActivity());
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
