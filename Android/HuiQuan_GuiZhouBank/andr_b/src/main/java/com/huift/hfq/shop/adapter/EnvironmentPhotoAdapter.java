package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Decoration;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * 商家相册(产品)
 * @author qian.zhou
 */
public class EnvironmentPhotoAdapter extends CommonListViewAdapter<Decoration>{
	
	public EnvironmentPhotoAdapter(Activity activity, List<Decoration> list) {
		super(activity, list);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_photo_environment, position);
		final Decoration decoration = mDatas.get(position);
		ImageView ivPhoto = holder.getView(R.id.iv_enviroment_photo);//头像
		TextView tvTitle = holder.getView(R.id.tv_environment_title);//标题
		//赋值
		Util.showImage(mActivity, decoration.getImgUrl() , ivPhoto);
		tvTitle.setText(decoration.getTitle());
		return holder.getConvertView();
	}
}
