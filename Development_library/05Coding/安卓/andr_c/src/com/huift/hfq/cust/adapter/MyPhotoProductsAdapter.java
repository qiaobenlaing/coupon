package com.huift.hfq.cust.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.adapter.CommenViewHolder;
import com.huift.hfq.base.adapter.CommonListViewAdapter;
import com.huift.hfq.base.pojo.ShopDecoration;
import com.huift.hfq.cust.R;

/**
 * 商家相册(产品)
 * @author qian.zhou
 */
public class MyPhotoProductsAdapter extends CommonListViewAdapter<ShopDecoration>{
	
	public static final String TAG = MyPhotoProductsAdapter.class.getSimpleName();

	public MyPhotoProductsAdapter(Activity activity, List<ShopDecoration> datas) {
		super(activity, datas);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_photo_products, position);
		final ShopDecoration shopDecoration = mDatas.get(position);
		
		ImageView ivPhotoTitle = holder.getView(R.id.iv_photo_title);
		TextView tvPhotoTypeName = holder.getView(R.id.tv_productype_name);
		TextView tvPhotoCount = holder.getView(R.id.tv_photo_count);//一共有多少张
		
		//赋值
		tvPhotoTypeName.setText(shopDecoration.getName());
		tvPhotoCount.setText(shopDecoration.getPhotoCount());
		Util.showImage(mActivity, shopDecoration.getUrl(), ivPhotoTitle);
		
		return  holder.getConvertView();
	}
}
