package cn.suanzi.baomi.shop.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.suanzi.baomi.base.adapter.CommenViewHolder;
import cn.suanzi.baomi.base.pojo.ShopDecoration;
import cn.suanzi.baomi.shop.R;

/**
 * 商家相册(产品)(选择子相册类别)
 * @author qian.zhou
 */
public class ProductPhotoCheckTypeAdapter extends CommonListViewAdapter<ShopDecoration>{
	
	public ProductPhotoCheckTypeAdapter(Activity activity, List<ShopDecoration> list) {
		super(activity, list);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_check_phototype, position);
		final ShopDecoration shopDecoration = (ShopDecoration) getItem(position);
		TextView tvProducPhotoType = holder.getView(R.id.tv_check_phototype);//子相册类别名称
		//赋值
		tvProducPhotoType.setText(shopDecoration.getName());
		return holder.getConvertView();
	}
}
