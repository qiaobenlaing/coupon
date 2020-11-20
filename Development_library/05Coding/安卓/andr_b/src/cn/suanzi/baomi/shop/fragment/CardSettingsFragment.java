// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.21
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.model.AddCardTask;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 会员卡设置
 * @author yanfang.li
 */
public class CardSettingsFragment extends Fragment {
	
	private final static String TAG = "CardSettingsFragment";
	
	/** 等级设置按钮 **/
	@ViewInject(R.id.btn_card_set_gd)
	Button mBtnGrade;
	/** 积分设置按钮 **/
	@ViewInject(R.id.btn_card_set_igl)
	Button mBtnIntegeral;

	/*** fragment管理器 **/
	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;
	
	public static CardSettingsFragment newInstance() {
		
		Bundle args = new Bundle();
		CardSettingsFragment fragment = new CardSettingsFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_cardsettings, container, false);
		ViewUtils.inject(this,v);
		init(v);
		return v;
	}

	/**
	 * 初始化操作
	 */
	public void init(View v) {
		mFragmentManager = getFragmentManager();
		//默认显示的界面
		changeFragment(R.id.ly_card_set_content, new CardGradeFirstFragment());
		mBtnGrade.setBackgroundResource(R.drawable.card_set_btn);
	}

	/**
	 * 更换页面
	 * @param id
	 * @param fragment
	 */
	private void changeFragment(int id, Fragment fragment) {
		mFragmentTransaction = mFragmentManager.beginTransaction();
		mFragmentTransaction.replace(id, fragment);
		mFragmentTransaction.commit();
	}

	/**
	 * 选项卡的切换
	 * @param v
	 */
	@OnClick({ R.id.btn_card_set_gd, R.id.btn_card_set_igl,
			R.id.iv_cardlist_return, R.id.iv_card_set_save })
	private void btnSkipClick(View v) {
		switch (v.getId()) {
		case R.id.btn_card_set_gd://选项卡等级设置
			changeFragment(R.id.ly_card_set_content, new CardGradeFirstFragment());
			// 变换选项卡切换的背景图片
			mBtnGrade.setBackgroundResource(R.drawable.card_set_btn);
			mBtnIntegeral.setBackgroundResource(R.drawable.shape_btncardset);
			break;
		case R.id.btn_card_set_igl://选项卡积分设置
			changeFragment(R.id.ly_card_set_content,new CardSetGradeFragment());
			mBtnGrade.setBackgroundResource(R.drawable.shape_btncardset);
			mBtnIntegeral.setBackgroundResource(R.drawable.card_set_btn);
			break;
		case R.id.iv_cardlist_return:
			getActivity().finish();
			break;
		case R.id.iv_card_set_save:
			
			SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cardadd", Context.MODE_PRIVATE);
	        String cardName = sharedPreferences.getString("cardName", null);
	        String cardLvl = sharedPreferences.getString("cardLvl", null);
	        String url = "";
	        String discountRequire = sharedPreferences.getString("discountRequire", null);
	        String discount = sharedPreferences.getString("discount", null);
	        String isRealNameRequired = sharedPreferences.getString("isRealNameRequired", "0");
	        String isSharable = sharedPreferences.getString("isSharable", "0");
	        String pointLifetime = sharedPreferences.getString("pointLifetime", "0");
	        String pointsPerCash = sharedPreferences.getString("pointsPerCash", "0");
			new AddCardTask(getActivity()).execute(cardName,cardLvl,url,discountRequire,discount,isRealNameRequired,isSharable,pointLifetime,pointsPerCash);
			
			break;

		default:
			break;
		}
	}
}
