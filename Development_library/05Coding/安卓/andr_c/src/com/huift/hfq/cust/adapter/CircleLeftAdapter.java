package com.huift.hfq.cust.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.huift.hfq.base.pojo.SubShopSortType;
import com.huift.hfq.cust.R;

public class CircleLeftAdapter extends BaseAdapter {
	private List<SubShopSortType> data;
	private Context context;
	private int redIndex = -1;
	
	public CircleLeftAdapter(List<SubShopSortType> data,Context context){
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
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = View.inflate(context, R.layout.tv_item, null);
		}
		TextView tv = (TextView) convertView;
		if(position==redIndex){
			tv.setTextColor(Color.RED);
		}else{
			tv.setTextColor(Color.BLACK);
		}
		
		SubShopSortType subShopSortType = data.get(position);
		tv.setText(subShopSortType.getQueryName());
		return convertView;
	}
	
	public void updataData(List<SubShopSortType> data){
		this.data = data;
		notifyDataSetChanged();
	}
	
	public void updataRedIndex(int position){
		this.redIndex =position;
		notifyDataSetChanged();
	}

}
