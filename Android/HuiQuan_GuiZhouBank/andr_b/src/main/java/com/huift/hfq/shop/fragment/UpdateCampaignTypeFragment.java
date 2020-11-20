package com.huift.hfq.shop.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Campaign;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.adapter.CampaignTypeAdapter;
import com.huift.hfq.shop.model.GetActTypeTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.minidev.json.JSONArray;

import java.util.List;

/**
 * 活动类型
 *
 * @author wensi.yu
 */
public class UpdateCampaignTypeFragment extends Fragment {

    /**
     * 活动类型
     */
    public static final String ACT_TYPE = " actType ";
    private final static String CAMPAIGN_TITLE = "活动类型";
    /**
     * 类型列表
     */
    private ListView mLvCampaignType;
    /**
     * adapter
     */
    private CampaignTypeAdapter mGetactAdapter;
    /**
     * 存放的数据
     */
    private List<Campaign> mData;
    /**
     * 得到的value
     */
    private String mCampaignValue;
    /**
     * 得到的值
     */
    private String mCampaignName;
    /**
     * 活动类型
     */
    private String mActType;

    public static UpdateCampaignTypeFragment newInstance() {
        Bundle args = new Bundle();
        UpdateCampaignTypeFragment fragment = new UpdateCampaignTypeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_campaign_type, container, false);
        ViewUtils.inject(this, view);
        init(view);
        return view;
    }

    private Activity getMyActivity() {
        Activity act = getActivity();
        if (act == null) {
            act = AppUtils.getActivity();
        }
        return act;
    }

    private void init(View view) {
        Intent intent = getMyActivity().getIntent();
        mActType = intent.getStringExtra(ACT_TYPE);
        TextView tvCampaignName = (TextView) view.findViewById(R.id.tv_mid_content);//标题
        tvCampaignName.setText(CAMPAIGN_TITLE);
        TextView tvSet = (TextView) view.findViewById(R.id.tv_msg);//添加
        tvSet.setVisibility(View.GONE);
        mLvCampaignType = (ListView) view.findViewById(R.id.lv_campaign_type);
        mLvCampaignType.setOnItemClickListener(campaignClick);
        getActType();

    }

    /**
     * 给Listview添加点击事件
     */
    OnItemClickListener campaignClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
            for (int i = 0; i < mData.size(); i++) {
                if (i == position) {
                    mData.get(i).setChecked(true);
                    mCampaignValue = mData.get(i).getValue();
                    mCampaignName = mData.get(i).getName();
                } else {
                    mData.get(i).setChecked(false);
                }
            }
            mGetactAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 活动类型
     */
    private void getActType() {
        new GetActTypeTask(getMyActivity(), new GetActTypeTask.Callback() {
            @Override
            public void getResult(JSONArray result) {
                if (null == result) {
                    return;
                }
                mData = new Gson().fromJson(result.toString(), new TypeToken<List<Campaign>>() {
                }.getType());
                mGetactAdapter = new CampaignTypeAdapter(getMyActivity(), mData);
                mLvCampaignType.setAdapter(mGetactAdapter);

                //默认选中item
                if (!Util.isEmpty(mActType) && Integer.parseInt(mActType) - 1 >= 0) {
                    mData.get(Integer.parseInt(mActType) - 1).setChecked(true);
                    mGetactAdapter.notifyDataSetChanged();
                }
            }
        }).execute();
    }

    public void getCampaignValue() {
        Intent intent = new Intent();
        intent.putExtra(EditAcitityMsgFragment.CAMPAIGN_TYPE_KEY, mCampaignValue);
        intent.putExtra(EditAcitityMsgFragment.CAMPAIGN_TYPE_NAME, mCampaignName);
        getMyActivity().setResult(EditAcitityMsgFragment.UPP_CAMPAIGN_TYPE_SUCC, intent);
        getMyActivity().finish();
    }

    /**
     * 返回
     *
     * @param view
     */
    @OnClick(R.id.layout_turn_in)
    public void trunIdenCode(View view) {
        getCampaignValue();
    }
}
