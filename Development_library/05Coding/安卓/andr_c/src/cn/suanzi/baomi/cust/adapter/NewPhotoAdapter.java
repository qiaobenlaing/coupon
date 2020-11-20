package cn.suanzi.baomi.cust.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.adapter.CommenViewHolder;
import cn.suanzi.baomi.base.adapter.CommonListViewAdapter;
import cn.suanzi.baomi.base.pojo.Image;
import cn.suanzi.baomi.base.pojo.Photo;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.activity.BigPhotoActivity;

/**
 * 上新图片
 * @author qian.zhou
 */
public class NewPhotoAdapter extends CommonListViewAdapter<Photo> {
	
	public NewPhotoAdapter(Activity activity, List<Photo> datas) {
		super(activity, datas);
	}

	@Override
	public void setItems(List<Photo> photos) {
		super.setItems(photos);
	}
	@Override
	public void addItems(List<Photo> photos) {
		super.addItems(photos);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView, parent,R.layout.item_up_newphoto, position);
		Photo photo = (Photo) getItem(position);
		final ImageView showImageView = holder.getView(R.id.iv_up_newphoto);//上新图片
		ImageView ivIsNew = holder.getView(R.id.iv_isnew);//是否上新
		
		if(Util.isEmpty(photo.getUrl())){
			showImageView.setBackgroundResource(R.drawable.shoplogo);
		} else{
			Util.showImage(mActivity, photo.getUrl() , showImageView);
		}
		//判断图片是否为上新图片
		if("0".equals(photo.getIsNew())){
			ivIsNew.setVisibility(View.GONE);
		} else{
			ivIsNew.setVisibility(View.VISIBLE);
		}
		
		//点击图片查看大图
		showImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<Image> imageList = new ArrayList<Image>();
				for (int i = 0; i < mDatas.size(); i++) {
					Photo photo = mDatas.get(i);
					Image image = new Image();
					image.setImageUrl(photo.getUrl());
					imageList.add(image);
				}
				Intent intent = new Intent(mActivity, BigPhotoActivity.class);
				intent.putExtra(BigPhotoActivity.IMAGE_LIST, (Serializable)imageList);
				intent.putExtra(BigPhotoActivity.IMAG_INDEX, position);
				mActivity.startActivity(intent);
			}
		});
		return holder.getConvertView();
	}
}
