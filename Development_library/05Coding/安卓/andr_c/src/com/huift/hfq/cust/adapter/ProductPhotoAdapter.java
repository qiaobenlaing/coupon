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
 * 商家相册(产品)
 * @author yanfang.li
 */
public class ProductPhotoAdapter extends CommonListViewAdapter<Decoration>{
	private static final String TAG = ProductPhotoAdapter.class.getSimpleName();
	public ProductPhotoAdapter(Activity activity, List<Decoration> list) {
		super(activity, list);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_photo_product, position);
		final Decoration decoration = mDatas.get(position);
		
		ImageView ivPhoto = holder.getView(R.id.iv_photo_product); // 头像
		TextView tvProductName = holder.getView(R.id.tv_product_name); // 产品名称
		TextView tvProductPrice = holder.getView(R.id.tv_product_price); // 产品价格
		// 赋值
		Util.showImage(mActivity, decoration.getUrl() , ivPhoto);
		if (null == decoration.getTitle() || Util.isEmpty(decoration.getTitle())) {
			tvProductName.setVisibility(View.GONE);
		} else {
			tvProductName.setVisibility(View.VISIBLE);
			tvProductName.setText(decoration.getTitle());
		}
		tvProductPrice.setText(getString(R.string.money_symbol) + decoration.getPrice());
		
		return holder.getConvertView();
	}
	
	/**
	 * 得到String里面的字符串
	 */
	private String getString (int id) {
		if (mActivity == null) {
			return null ;
		} else {
			return mActivity.getResources().getString(id);
		}
	}
}
