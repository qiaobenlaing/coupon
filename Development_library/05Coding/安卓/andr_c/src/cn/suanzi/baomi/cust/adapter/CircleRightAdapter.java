package cn.suanzi.baomi.cust.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.suanzi.baomi.base.pojo.CircleItem;
import cn.suanzi.baomi.cust.R;

/**
 * @author chengying
 */
public class CircleRightAdapter extends BaseAdapter {
	private List<CircleItem> data;
	private Context context;
	private int redIndex = -1;
	
	public CircleRightAdapter(List<CircleItem> data,Context context){
		this.data = data;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		if(data == null){
			return 0;
		}
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		if(data == null){
			return null;
		}
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
		
		CircleItem circleItem = data.get(position);
		tv.setText(circleItem.getName());
		return convertView;
	}
	
	public void updataData(List<CircleItem> data){
		this.data = data;
		notifyDataSetChanged();
	}
	
	public void updataRedIndex(int position){
		this.redIndex =position;
		notifyDataSetChanged();
	}

}
