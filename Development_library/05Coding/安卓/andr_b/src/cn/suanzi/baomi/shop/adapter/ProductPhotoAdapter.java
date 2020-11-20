package cn.suanzi.baomi.shop.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.adapter.CommenViewHolder;
import cn.suanzi.baomi.base.pojo.Decoration;
import cn.suanzi.baomi.shop.R;

/**
 * 商家相册(产品)
 * @author qian.zhou
 */
public class ProductPhotoAdapter extends CommonListViewAdapter<Decoration>{
	
	public ProductPhotoAdapter(Activity activity, List<Decoration> list) {
		super(activity, list);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_photo_product, position);
		final Decoration decoration = mDatas.get(position);
		ImageView ivPhoto = holder.getView(R.id.iv_photo_product);//头像
		TextView tvProductName = holder.getView(R.id.tv_product_name);//产品名称
		TextView tvProductPrice = holder.getView(R.id.tv_product_price);//产品价格
		//赋值
		if(Util.isEmpty(decoration.getUrl())){
			ivPhoto.setBackgroundResource(R.drawable.activityimage);
		} else{
			Util.showImage(mActivity, decoration.getUrl() , ivPhoto);
		}
		tvProductName.setText(decoration.getTitle());
		tvProductPrice.setText("￥" + decoration.getPrice());
		
		return holder.getConvertView();
	}
}
