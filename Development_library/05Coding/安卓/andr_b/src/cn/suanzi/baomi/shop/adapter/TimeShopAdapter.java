package cn.suanzi.baomi.shop.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.suanzi.baomi.base.pojo.Shop;
import cn.suanzi.baomi.shop.R;

/**
 * 营业时间
 * @author qian.zhou
 */
public class TimeShopAdapter extends CommonListViewAdapter<Shop>{
	private final static String TAG = "TimeShopAdapter";
	
	public TimeShopAdapter(Activity activity, List<Shop> datas) {
		super(activity, datas);
	}
	
	@Override
	public void setItems(List<Shop> datas) {
		super.setItems(datas);
	}
	@Override
	public void addItems(List<Shop> datas) {
		super.addItems(datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_set_myshopinfo_time, position);
		final Shop shop = (Shop) getItem(position);
		
		((TextView) holder.getView(R.id.tv_shop_time)).setText(shop.getOpen() + " - " +  shop.getClose());//商家时间
		
		return holder.getConvertView();
	}
}
