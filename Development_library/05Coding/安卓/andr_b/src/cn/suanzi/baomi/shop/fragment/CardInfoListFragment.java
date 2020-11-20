// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.fragment;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.Card;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopConst;
import cn.suanzi.baomi.shop.activity.CardGradeFirstActivity;
import cn.suanzi.baomi.shop.activity.HomeActivity;
import cn.suanzi.baomi.shop.adapter.CardInfoAdapter;
import cn.suanzi.baomi.shop.model.GetGeneralCardStasticsTask;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 会员卡查询界面
 * @author yanfang.li
 */
public class CardInfoListFragment extends Fragment {

	/** 显示会员卡的列表 **/
	@ViewInject(R.id.lv_card_list)
	private ListView mLvCard;
	/** 分页 **/
	private int mPage = 1;
	/** 适配器所需要的数据 **/
	private List<Card> cardData;
	
	public static CardInfoListFragment newInstance() {
		Bundle args = new Bundle();
		CardInfoListFragment fragment = new CardInfoListFragment();
		fragment.setArguments(args);
	return fragment;
	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cardinfo, container,false);
		ViewUtils.inject(this,view);
		cardData = new ArrayList<Card>();
		getGeneralCardStastics();
		Util.addLoginActivity(getActivity());
		return view;
	}
	
	/**
	 * 获得会员卡
	 */
	private void getGeneralCardStastics() {
		
		new GetGeneralCardStasticsTask(getActivity(), new GetGeneralCardStasticsTask.Callback() {
			
			@Override
			public void getResult(JSONArray result) {
				for (int i = 0; i < result.size(); i++) {
					Card card = Util.json2Obj(result.get(i).toString(), Card.class);
					cardData.add(card);
				}
				CardInfoAdapter adapter = new CardInfoAdapter(getActivity(), cardData);
				if (mPage > 1) {
					adapter.notifyDataSetChanged();
				} else {
					mLvCard.setAdapter(adapter);
				}
			}
		}).execute();
	}

	/**
	 * 点击跳转事件
	 * @param view
	 */
	@OnClick({R.id.iv_card_ls_return, R.id.iv_card_add })
	private void btnSkipClick(View view) {
		switch (view.getId()) {
		case R.id.iv_card_ls_return:
			Intent intent = new Intent(getActivity(),HomeActivity.class);
			intent.putExtra(ShopConst.FRAG_ID, Util.NUM_TWO);
			startActivity(intent);
			getActivity().finish();
			break;
		case R.id.iv_card_add:
			startActivity(new Intent(getActivity(),CardGradeFirstActivity.class));
			break;
		default:
			break;
		}
	}
}
