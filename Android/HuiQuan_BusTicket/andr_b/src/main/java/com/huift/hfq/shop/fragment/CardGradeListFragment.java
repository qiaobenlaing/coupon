// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Card;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.CardSetGradeActivity;
import com.huift.hfq.shop.adapter.CardGradeListAdapter;
import com.huift.hfq.shop.model.GetGeneralCardStasticsTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.minidev.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 所有会员卡的信息显示,会员卡列表
 * @author yanfang.li
 */
public class CardGradeListFragment extends Fragment {


	/** 显示会员卡等级的集合*/
	private ListView mLvCardGrade;
	/** 适配器的数据源*/
	private List<Card> mCardDatas ;
	private View mHeadView;
	/** Listview头部数据*/
	/** 保存值*/
	private SharedPreferences mSharedPreferences;
	private Editor mEditor;

	public static CardGradeListFragment newInstance() {
		Bundle args = new Bundle();
		CardGradeListFragment fragment = new CardGradeListFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_card_gradelist, container, false);
		ViewUtils.inject(this, v);
		init(v);
		mSharedPreferences = getActivity().getSharedPreferences(ShopConst.Card.CARD_FALG, Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();

		Intent intent =  getActivity().getIntent();
		String cardFlag = intent.getStringExtra(ShopConst.Card.CARD_VALUE);
		if (ShopConst.Card.CARD_NULL.equals(cardFlag)) {
			getGeneralCardStastics(v);

		} else if (ShopConst.Card.CARD_VALUE.equals(cardFlag)) {
			mCardDatas = (List<Card>) intent.getSerializableExtra(ShopConst.Card.CARD_LIST);
			mEditor.putString(ShopConst.Card.CARD_FALG, ShopConst.Card.CARD_UPP);
			mEditor.commit();
			for (int i = 0; i < mCardDatas.size(); i++) {
				Card card = mCardDatas.get(i);
			}

			CardGradeListAdapter adapter = new CardGradeListAdapter(getActivity(), mCardDatas);
			mLvCardGrade.setAdapter(adapter);
		} else {
			mCardDatas = (List<Card>) intent.getSerializableExtra(ShopConst.Card.CARD_REUTRN);
			mEditor.putString(ShopConst.Card.CARD_FALG, ShopConst.Card.CARD_UPP);
			mEditor.commit();
			for (int i = 0; i < mCardDatas.size(); i++) {
				Card card = mCardDatas.get(i);
			}
			CardGradeListAdapter adapter = new CardGradeListAdapter(getActivity(), mCardDatas);
			mLvCardGrade.setAdapter(adapter);
		}
		return v;
	}

	private void init(View v) {
		Util.addLoginActivity(getActivity());
		mLvCardGrade = (ListView) v.findViewById(R.id.lv_gradelist);
		mHeadView = View.inflate(getActivity(), R.layout.top_cardgrade, null);
		mLvCardGrade.addHeaderView(mHeadView);
		LinearLayout ivReurn = (LinearLayout) v.findViewById(R.id.layout_turn_in);
		ivReurn.setVisibility(View.VISIBLE);
		TextView tvTitle = (TextView) v.findViewById(R.id.tv_mid_content);
		TextView tvEdit = (TextView) v.findViewById(R.id.tv_msg);
		tvTitle.setText(getResources().getString(R.string.card_grade));
		tvEdit.setText(getResources().getString(R.string.card_edit));
	}

	/**
	 * 首先查询会员卡
	 * @param v
	 */
	private void getGeneralCardStastics(final View v) {

		new GetGeneralCardStasticsTask(getActivity(), new GetGeneralCardStasticsTask.Callback() {

			@Override
			public void getResult(JSONArray result) {
				// 准备GridView的适配器所用的数据
				mCardDatas = new ArrayList<Card>();
				// 获取自定义个数组信息
				if (result == null) {
					mEditor.putString(ShopConst.Card.CARD_FALG, ShopConst.Card.CARD_ADD);
					for (int i = 0; i < 3; i++) {
						Card card = new Card();
						card.setCardLvl((i+1)+"");
						card.setDiscountRequire("0");
						card.setDiscount("0");
						card.setPointsPerCash("0");
						card.setPointLifeTime("0");
						mCardDatas.add(card);
					}
				} else {
					mEditor.putString(ShopConst.Card.CARD_FALG, ShopConst.Card.CARD_UPP);
					for (int i = 0; i < result.size(); i++) {
						Card card = Util.json2Obj(result.get(i).toString(), Card.class);
						mCardDatas.add(card);
					}
				}
				mEditor.commit();
				CardGradeListAdapter adapter = new CardGradeListAdapter(getActivity(), mCardDatas);
				mLvCardGrade.setAdapter(adapter);
			}
		}).execute();
	}

	/**
	 * 返回和多种选择查询
	 * @param v
	 */
	@OnClick({ R.id.layout_turn_in,R.id.tv_msg })
	private void ivSkipClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
			case R.id.layout_turn_in:
		    /*intent = new Intent(getActivity(), HomeActivity.class);
			intent.putExtra(ShopConst.FRAG_ID, 2);
			startActivity(intent);*/
				getActivity().finish();
				break;
			case R.id.tv_msg:
				intent = new Intent(getActivity(), CardSetGradeActivity.class);
				intent.putExtra(ShopConst.Card.CARD_LIST, (Serializable)mCardDatas);
				Util.addActivity(getActivity());
				startActivity(intent);
				break;
			default:
				break;
		}
	}
}
