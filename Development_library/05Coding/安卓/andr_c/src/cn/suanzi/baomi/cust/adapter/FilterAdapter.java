package cn.suanzi.baomi.cust.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.SubShopSortType;
import cn.suanzi.baomi.cust.R;

public class FilterAdapter extends BaseAdapter {
	private List<SubShopSortType> data;
	private Context context;
	
	public FilterAdapter(List<SubShopSortType> data, Context context) {
		super();
		this.data = data;
		this.context = context;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHoder holder = null;
		if(convertView == null){
			holder = new ViewHoder();
			convertView = View.inflate(context, R.layout.img_tv_item, null);
			holder.icon = (ImageView) convertView.findViewById(R.id.iv_icon_item);
			holder.describle = (TextView) convertView.findViewById(R.id.tv_describle_item);
			holder.selected = (ImageView) convertView.findViewById(R.id.iv_seleted_item);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHoder) convertView.getTag();
		}
		
		SubShopSortType subShopSortType = data.get(position);
		holder.describle.setText(subShopSortType.getQueryName());
		Util.showFirstImages((Activity)context, subShopSortType.getFocusedUrl(), holder.icon);
		if(subShopSortType.isCheck()){
			holder.describle.setTextColor(Color.RED);
			holder.selected.setVisibility(View.VISIBLE);
		}else{
			holder.describle.setTextColor(Color.BLACK);
			holder.selected.setVisibility(View.GONE);
		}
		
		return convertView;
	}

	class ViewHoder{
		public ImageView icon;
		public TextView describle;
		public ImageView selected;
	}
}
