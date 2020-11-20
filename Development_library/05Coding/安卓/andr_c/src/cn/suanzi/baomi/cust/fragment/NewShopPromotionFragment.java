package cn.suanzi.baomi.cust.fragment;

import java.util.ArrayList;

import cn.suanzi.baomi.base.pojo.Activitys;
import cn.suanzi.baomi.base.utils.ListViewHeight;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.adapter.ActIssueAdapter;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class NewShopPromotionFragment extends Fragment {
	private static final String TAG = NewShopPromotionFragment.class.getSimpleName();

	/**活动列表*/
	private ListView mPromotionListView;
	
	/**默认图片*/
	private LinearLayout mNodataLinearLayout;
	
	/**
	 * 需要传递参数时有利于解耦
	 */
	public static NewShopPromotionFragment newInstance(Bundle args) {
		NewShopPromotionFragment fragment = new NewShopPromotionFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.fragment_shop_promotion, null);
		initView(view);
		Bundle bundle = getArguments();
		ArrayList<Activitys> activitys = (ArrayList<Activitys>) bundle.getSerializable("Promotion");
		if(null == activitys || activitys.size() == 0){
			mPromotionListView.setVisibility(View.GONE);
			mNodataLinearLayout.setVisibility(View.VISIBLE);
		}else{
			Log.d(TAG, "活动数量==="+activitys.size());
			mPromotionListView.setVisibility(View.VISIBLE);
			mNodataLinearLayout.setVisibility(View.GONE);
			showView(activitys);
		}
		return view;
	}

	/**
	 * 显示视图
	 * @param activitys
	 */
	private void showView(ArrayList<Activitys> activitys) {
		mPromotionListView.setAdapter(new ActIssueAdapter(getActivity(), activitys));
		ListViewHeight.setListViewHeightBasedOnChildren(mPromotionListView);
	}

	/**
	 * 初始化视图
	 * @param view
	 */
	private void initView(View view) {
		mPromotionListView = (ListView) view.findViewById(R.id.lv_shop_promotion);
		
		mNodataLinearLayout = (LinearLayout) view.findViewById(R.id.ll_no_data);
	}
}
