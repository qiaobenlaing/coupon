//---------------------------------------------------------
//@author    yanfang.li
//@version   1.0.0
//@createTime 2015.6.2
//@copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
//---------------------------------------------------------
package com.huift.hfq.cust.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.huift.hfq.cust.activity.ActThemeDetailActivity;
import com.huift.hfq.cust.activity.BigPhotoActivity;
import com.huift.hfq.cust.application.CustConst;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.adapter.CommenViewHolder;
import com.huift.hfq.base.adapter.CommonListViewAdapter;
import com.huift.hfq.base.pojo.Image;
import com.huift.hfq.cust.R;

/**
 * 商圈的适配器
 * @author yanfang.li
 */
public class HomeShopPicAdapter extends CommonListViewAdapter<Image> {

	private final static String TAG = HomeShopPicAdapter.class.getSimpleName();
	
	public HomeShopPicAdapter(Activity activity, List<Image> datas) {
		super(activity, datas);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView,parent, R.layout.item_home_up_newphoto, position);
		View view = holder.getConvertView();
	    final Image image = mDatas.get(position);
		ImageView ivShopCircle = (ImageView) holder.getView(R.id.iv_up_newphoto);
		ImageView ivIsNew = (ImageView) holder.getView(R.id.iv_isnew);
		if (Util.isEmpty(image.getProductImg())) {
			ivIsNew.setVisibility(View.GONE);
		} else {
			ivIsNew.setVisibility(View.VISIBLE);
		}
		Util.showFirstImages(mActivity, image.getProductImg(), ivShopCircle);
		
		// 点击事件
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = null;
				if (image.getLinkType() == 0 && !Util.isEmpty(image.getContent())) { // linkType 为0 时跳转到H5界面  其他的查看大图
					intent = new Intent(mActivity, ActThemeDetailActivity.class);
					intent.putExtra(ActThemeDetailActivity.TYPE, CustConst.HactTheme.HOME_ACTIVITY);
					intent.putExtra(ActThemeDetailActivity.THEME_URL, image.getContent());
					mActivity.startActivity(intent);
					
				} else { // 查看大图
					List<Image> imageList = new ArrayList<Image>();
					for (int i = 0; i < mDatas.size(); i++) {
						Image image = mDatas.get(i);
						image.setImageUrl(image.getProductImg());
						imageList.add(image);
					}
					intent = new Intent(mActivity, BigPhotoActivity.class);
					intent.putExtra(BigPhotoActivity.IMAGE_LIST, (Serializable)imageList);
					intent.putExtra(BigPhotoActivity.IMAG_INDEX, position);
					mActivity.startActivity(intent);
				}
			}
		});
		return view;
	}
}
