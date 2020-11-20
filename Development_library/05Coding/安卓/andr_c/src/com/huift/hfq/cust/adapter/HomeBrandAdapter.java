//---------------------------------------------------------
//@author    yanfang.li
//@version   1.0.0
//@createTime 2015.6.2
//@copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
//---------------------------------------------------------
package com.huift.hfq.cust.adapter;

import java.util.List;

import com.huift.hfq.cust.util.SkipActivityUtil;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.adapter.CommenViewHolder;
import com.huift.hfq.base.adapter.CommonListViewAdapter;
import com.huift.hfq.base.pojo.HomeTemplate;
import com.huift.hfq.cust.R;

/**
 * 品牌
 * @author yanfang.li
 */
public class HomeBrandAdapter extends CommonListViewAdapter<HomeTemplate> {

	private final static String TAG = HomeBrandAdapter.class.getSimpleName();
	/** 模板号*/
	private String mModuleValue;
	public HomeBrandAdapter(Activity activity, List<HomeTemplate> datas,String moduleValue) {
		super(activity, datas);
		this.mModuleValue = moduleValue;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView,parent, R.layout.item_head_home4, position);
		final HomeTemplate homeTemplate = mDatas.get(position);
		View view = holder.getConvertView();
		TextView tvShopName = (TextView) holder.getView(R.id.tv_shop_name);
		TextView tvShopDescription = (TextView) holder.getView(R.id.tv_shop_description);
		ImageView ivShopBrand = (ImageView) holder.getView(R.id.iv_shop_brand);
		tvShopName.setText(homeTemplate.getTitle());
		if (!Util.isEmpty(homeTemplate.getTitleColor())) {
			tvShopName.setTextColor(Color.parseColor(homeTemplate.getTitleColor()));
		} else {
			tvShopName.setTextColor(mActivity.getResources().getColor(R.color.deep_textcolor));
		}
		if (Util.isEmpty(homeTemplate.getSubTitle())) {
			tvShopDescription.setVisibility(View.GONE);
		} else {
			tvShopDescription.setVisibility(View.VISIBLE);
			tvShopDescription.setText(homeTemplate.getSubTitle());
		}
		if (!Util.isEmpty(homeTemplate.getSubTitleColor())) {
			tvShopDescription.setTextColor(Color.parseColor(homeTemplate.getSubTitleColor()));
		} else {
			tvShopDescription.setTextColor(mActivity.getResources().getColor(R.color.deep_textcolor));
		}
		Util.showFirstImages(mActivity, homeTemplate.getImgUrl(), ivShopBrand);
		if (!Util.isEmpty(homeTemplate.getBgColor())) {
			view.setBackgroundColor(Color.parseColor(homeTemplate.getBgColor()));
		} else {
			view.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
		}
		// 点击事件
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SkipActivityUtil.skipHomeActivity(mActivity, homeTemplate,mModuleValue);
			}
		});
		return view;
	}
}
