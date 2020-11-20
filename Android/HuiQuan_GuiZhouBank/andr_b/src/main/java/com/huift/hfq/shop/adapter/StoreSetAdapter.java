package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.MyStaffItem;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * 门店设置适配器
 * @author qian.zhou
 */
public class StoreSetAdapter extends BaseAdapter{
    private Context mContext;
	
	private List<MyStaffItem> list;
	
	public StoreSetAdapter(Context context, List<MyStaffItem> list) {
		super();
		this.mContext = context;
		this.list = list;
	}
	
	public void addItems(List<MyStaffItem> datas){
		if (datas == null) {
			return ;
		}
		if (list == null) {
			list = datas;
		}else{
			list.addAll(datas);
		}
		notifyDataSetChanged();
	}
	
	public void setItems(List<MyStaffItem> datas){
		list = datas;
		notifyDataSetChanged();
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_store_set, null);
			holder.ivIsCheck = (ImageView) convertView.findViewById(R.id.iv_is_store);
			holder.managerName = (TextView) convertView.findViewById(R.id.tv_storeshop_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		MyStaffItem item = list.get(position);
		holder.managerName.setText(item.getShopName());
		holder.ivIsCheck.setImageResource(String.valueOf(Util.NUM_ONE).equals(item.getIsOwner()) ? R.drawable.iv_selected : R.drawable.iv_noselected);
		return convertView;
	}
	
	public class ViewHolder{
		public ImageView ivIsCheck;
		public TextView managerName;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
}
