package com.huift.hfq.shop.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huift.hfq.shop.R;
import com.lidroid.xutils.ViewUtils;

/**
 * 活动收费
 * @author wensi.yu
 *
 */
public class CampaignFeeFragment extends Fragment {

	private final static String CAMPAIGN_TITLE = "活动收费";
	private final static String CAMPAIGN_SAVE = "保存";
	
	public static CampaignAddFragment newInstance() {
		Bundle args = new Bundle();
		CampaignAddFragment fragment = new CampaignAddFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_campaign_free,container, false);
		ViewUtils.inject(this, view);
		init(view);
		return view;
	}

	private void init(View view) {
		TextView tvCampaignName = (TextView) view.findViewById(R.id.tv_mid_content);// 标题
		tvCampaignName.setText(CAMPAIGN_TITLE);
		TextView tvSet = (TextView) view.findViewById(R.id.tv_msg);// 保存
		tvSet.setVisibility(View.VISIBLE);
		tvSet.setText(CAMPAIGN_SAVE);
	}
}
