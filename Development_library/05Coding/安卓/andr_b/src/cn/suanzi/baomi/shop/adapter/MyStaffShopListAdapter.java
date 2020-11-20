package cn.suanzi.baomi.shop.adapter;

import java.util.List;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.suanzi.baomi.base.pojo.StaffShop;
import cn.suanzi.baomi.shop.R;

/**
 * 门店列表
 * @author wensi.yu
 *
 */
public class MyStaffShopListAdapter extends CommonListViewAdapter<StaffShop>{

	private final static String TAG = "MyStoreListAdapter";
	
	public MyStaffShopListAdapter(Activity activity, List<StaffShop> datas) {
		super(activity, datas);
	}
	

	@Override
	public void setItems(List<StaffShop> datas) {
		super.setItems(datas);
	}
	@Override
	public void addItems(List<StaffShop> datas) {
		super.addItems(datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_mystore_list, position);
		final StaffShop item = (StaffShop) getItem(position);
		
		((TextView) holder.getView(R.id.tv_storelist_branch)).setText(item.getShopName());//商家名字
		return holder.getConvertView();
	}
}
