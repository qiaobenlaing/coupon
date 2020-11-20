package cn.suanzi.baomi.shop.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import cn.suanzi.baomi.base.pojo.ShopPicsItem;
import cn.suanzi.baomi.shop.R;
/**
 *
 * @author qian.zhou
 *
 */

/**
 * 装修图片
 * @author qian.zhou
 */
public class MyShopPicsAdapter extends CommonListViewAdapter<ShopPicsItem> {

	public MyShopPicsAdapter(Context context, List<ShopPicsItem> datas) {
		super(context, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mContext , convertView , parent, 
				R.layout.item_myshop_pics, position);
		ShopPicsItem shopitem = (ShopPicsItem)getItem(position);//在数据源中获取实体类对象
		((ImageView)holder.getView(R.id.iv_image_line)).setImageBitmap(shopitem.getPicsImage());
		return holder.getConvertView();
	}}
