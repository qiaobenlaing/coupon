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
import com.huift.hfq.base.pojo.Decoration;
import com.huift.hfq.cust.R;

/**
 * 商家产品相册
 * @author ad
 */
public class PhotoProductImageAdapter extends CommonListViewAdapter<Decoration> {
	
	private final static String TAG = PhotoProductImageAdapter.class.getSimpleName();
	
	public PhotoProductImageAdapter(Activity activity, List<Decoration> datas) {
		super(activity, datas);
		mDatas = datas;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView, parent,R.layout.item_photo_product_img, position);
		final Decoration decoration = mDatas.get(position);
		TextView tvProductName = holder.getView(R.id.tv_product_name);
		TextView tvProductPrice = holder.getView(R.id.tv_product_price);
		ImageView productImage = holder.getView(R.id.iv_product_img);
		Util.showImage(mActivity, decoration.getUrl() , productImage);
		tvProductName.setText(decoration.getTitle());
		tvProductPrice.setText(getString(R.string.money_symbol) + decoration.getPrice());		
		
		
		return holder.getConvertView();
	}
	
	private String getString (int id) {
		return mActivity.getResources().getString(id);
	}

}
