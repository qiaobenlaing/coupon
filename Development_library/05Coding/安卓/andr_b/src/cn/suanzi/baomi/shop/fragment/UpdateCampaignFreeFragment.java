package cn.suanzi.baomi.shop.fragment;

import java.util.ArrayList;
import java.util.List;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.Campaign;
import cn.suanzi.baomi.base.pojo.PromotionPrice;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.DialogUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.adapter.CampaignFreeAdapter;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 修改活动活动收费
 * @author qian.zhou
 */
public class UpdateCampaignFreeFragment extends Fragment {

	private final static String TAG = "CampaignFreeFragment";
	private final static String CAMPAIGN_TITLE = "活动收费";
	private final static String CAMPAIGN_SAVE = "保存";
	public final static String CAMPAIGN_OBJ = "camp";
	/** 规格的列表 */
	private ListView mLvCampaignFree;
	/** 传递过来的活动对象*/
	private Campaign mCampaign;
	/** 规格的数据*/
	private List<PromotionPrice> mData;
	/** adapter*/
	private CampaignFreeAdapter adapter;
	/** list*/
	private List<PromotionPrice> mDataList;

	public static UpdateCampaignFreeFragment newInstance() {
		Bundle args = new Bundle();
		UpdateCampaignFreeFragment fragment = new UpdateCampaignFreeFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_campaign_free, container, false);
		ViewUtils.inject(this, view);
		init(view);
		return view;
	}
	
	/**
	 * 当前的activity不为空
	 * @return
	 */
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	private void init(View view) {
		mCampaign = (Campaign) getMyActivity().getIntent().getSerializableExtra(CAMPAIGN_OBJ);
		mDataList = new ArrayList<PromotionPrice>();
		TextView tvCampaignName = (TextView) view.findViewById(R.id.tv_mid_content);// 标题
		tvCampaignName.setText(CAMPAIGN_TITLE);
		TextView tvSet = (TextView) view.findViewById(R.id.tv_msg);// 保存
		tvSet.setVisibility(View.VISIBLE);
		tvSet.setText(CAMPAIGN_SAVE);
		mLvCampaignFree = (ListView) view.findViewById(R.id.lv_campaign_free);// 规格的列表
		mLvCampaignFree.setOnItemLongClickListener(itemClick);
		LinearLayout campaignFreeAdd = (LinearLayout) getActivity().findViewById(R.id.ly_add_campaign);// 添加
		campaignFreeAdd.setOnClickListener(campaignAddClick);
		showPricListView();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	/**
	 * 默认显示两行输入价格
	 */
	private void showPricListView() {
		mData = mCampaign.getFeeScale();
		if(null == mData || mData.size()==0){
			mData = new ArrayList<PromotionPrice>();
			mData.add(new PromotionPrice("",1,0.0,0));
		}
		adapter = new CampaignFreeAdapter(getActivity(), mData);
		mLvCampaignFree.setAdapter(adapter);
	}
	
	/**
	 * 添加
	 */
	private OnClickListener campaignAddClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mData.add(new PromotionPrice("",mData.size()+1,0.0,0));
			adapter.notifyDataSetChanged();
		}
	};
	
	/**
	 * 删除
	 */
	private OnItemLongClickListener itemClick = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
			PromotionPrice  item = (PromotionPrice) mLvCampaignFree.getItemAtPosition(position);
			final int promotionCode = item.getId();
			Log.d(TAG, "promotionCode == "+promotionCode);
			DialogUtils.showDialog(getActivity(), getString(R.string.cue),getString(R.string.campaign_isdelete), getString(R.string.ok), getString(R.string.no), new DialogUtils().new OnResultListener() {
				@Override
				public void onOK() {
					for (int i = 0; i < mData.size(); i++) {
						PromotionPrice promotionPrice = mData.get(i);
						Log.d(TAG,"promotionPrice == " +promotionPrice.getId());
						if (i == position) {
							mData.remove(promotionPrice);
							adapter.notifyDataSetChanged();
						}
					}
				}
			});
			return false;
		}
	};

	/**
	 * 返回
	 * @param view
	 */
	@OnClick({R.id.layout_turn_in,R.id.tv_msg})
	public void trunIdenCode(View view) {
		Log.d(TAG, "shdajshd=="+mData.toString());
		switch (view.getId()) {
		case R.id.layout_turn_in:
			mData.clear();
			getActivity().finish();
			break;
		case R.id.tv_msg:
			ArrayList<PromotionPrice> newWithContent = new ArrayList<PromotionPrice>();
			for (int i = 0; i < mData.size(); i++) {
				PromotionPrice promotionPrice = mData.get(i);
				if(!Util.isEmpty(promotionPrice.getDes()) && promotionPrice.getPrice() > 0){
					newWithContent.add(promotionPrice);
				}
			}
			Intent intent = new Intent();
			intent.putExtra(EditAcitityMsgFragment.CAMPAIGN_FREE_LIST, (ArrayList<PromotionPrice>) newWithContent);
			getActivity().setResult(EditAcitityMsgFragment.CAMPAIGN_FREE_SUCC, intent);
			getActivity().finish();
			break;
		default:
			break;
		}
	}
}
