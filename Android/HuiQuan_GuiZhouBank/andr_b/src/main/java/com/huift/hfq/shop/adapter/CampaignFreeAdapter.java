package com.huift.hfq.shop.adapter;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.PromotionPrice;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.shop.R;

import java.util.List;

/**
 * 活动规格
 * @author wensi.yu
 *
 */
public class CampaignFreeAdapter extends CommonListViewAdapter<PromotionPrice>{
	
	private final static String TAG = "CampaignFreeAdapter";
	
	private List<PromotionPrice> mData;

	public CampaignFreeAdapter(Activity activity, List<PromotionPrice> datas) {
		super(activity, datas);
		this.mData = datas;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_campaign_free, position);
		final PromotionPrice item = (PromotionPrice) getItem(position);
		TextView tvCampaignFree = holder.getView(R.id.tv_camapginfree_title);
		EditText etCampaignFreeType = holder.getView(R.id.et_camapignfree_type);
		EditText etCampaignFreeMoney = holder.getView(R.id.et_camapignfree_money);
		LinearLayout lyCampignFree = holder.getView(R.id.ly_campaign_free);
		
		if(!Util.isEmpty(item.getDes())){
			etCampaignFreeType.setText(item.getDes());
		}
		
		if(!Util.isEmpty(item.getPrice()+"")){
			if (item.getPrice() == 0.0) {
				etCampaignFreeMoney.setText("");
			} else {
				etCampaignFreeMoney.setText(item.getPrice()+"");
			}
		} 
		
		if(item.getId() != 0){
			tvCampaignFree.setText("规格"+item.getId());
		}
		etCampaignFreeType.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				item.setDes(s.toString());
			}
		});
		
		etCampaignFreeMoney.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String priceStr = s.toString();
				if(Util.isEmpty(priceStr)){
					priceStr = "0";
				}
				item.setPrice(Double.parseDouble(priceStr));
			}
		});
		
		lyCampignFree.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final int promotionCode = item.getId();
				Log.d(TAG, "promotionCode == "+promotionCode);
				DialogUtils.showDialog(mActivity, mActivity.getString(R.string.cue),mActivity.getString(R.string.campaign_isdelete), mActivity.getString(R.string.ok), mActivity.getString(R.string.no), new DialogUtils().new OnResultListener() {
					@Override
					public void onOK() {
						for (int i = 0; i < mData.size(); i++) {
							PromotionPrice promotionPrice = mData.get(i);
							Log.d(TAG,"promotionPrice == " +promotionPrice.getId());
							if (i == position) {
								mData.remove(promotionPrice);
								notifyDataSetChanged();
							}
						}
					} 
					@Override
					public void onCancel(){
						
					}
				});
			}
		});
		
		return  holder.getConvertView();
	}
}
