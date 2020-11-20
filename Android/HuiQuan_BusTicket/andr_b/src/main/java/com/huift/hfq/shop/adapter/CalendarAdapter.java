package com.huift.hfq.shop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.huift.hfq.base.pojo.DateInfo;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.R.color;
import com.huift.hfq.shop.activity.AccntStatActivity;
import com.huift.hfq.shop.utils.DataUtils;

import java.util.List;


/**
 * 日历gridview适配器
 * */
public class CalendarAdapter extends BaseAdapter {

	private List<DateInfo> list = null;
	private Context context = null;
	private int selectedPosition = -1;
	AccntStatActivity activity;

	public CalendarAdapter(AccntStatActivity activity, List<DateInfo> list) {
		this.context = activity;
		this.list = list;
		this.activity = activity;
	}

	public List<DateInfo> getList() {
		return list;
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * 设置选中位置
	 * */
	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	/**
	 * 产生一个view
	 * */
	public View getView(final int position, View convertView, ViewGroup group) {
		//通过viewholder做一些优化
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.gridview_item, null);
			viewHolder.date = (TextView) convertView.findViewById(R.id.item_date);
			viewHolder.nongliDate = (TextView) convertView.findViewById(R.id.item_nongli_date);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		//根据数据源设置单元格的字体颜色、背景等
		viewHolder.date.setText(list.get(position).getDate() + "");
		//viewHolder.nongliDate.setText(list.get(position).getNongliDate());
		if (selectedPosition == position) {
			viewHolder.date.setTextColor(Color.WHITE);
			viewHolder.nongliDate.setTextColor(Color.WHITE);
			convertView.setBackgroundColor(Color.RED);

			/*SharedPreferences mSharedPreferences = activity.getSharedPreferences("mDate", Context.MODE_PRIVATE);
			final String mShowYearMonth = mSharedPreferences.getString("mShowYearMonth", "");*/


			//保存值
			/*SharedPreferences preferences = activity.getSharedPreferences("mDay", Context.MODE_PRIVATE);
		    Editor editor = mSharedPreferences.edit();
		    editor.putString("mDay", list.get(position).getDate()+"");
		    editor.commit();

			viewHolder.date.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Util.getContentValidate(activity, "你点击了="+list.get(position).getDate());
					Intent intent = new Intent(activity, AccntStatListActivity.class);
					
					String mGetDate = String.valueOf(list.get(position).getDate());
					intent.putExtra(AccntStatListFragment.newInstance().ACCNT_YEARORMONTH, mShowYearMonth);
					intent.putExtra(AccntStatListFragment.newInstance().ACCNT_DATE,mGetDate);
					
					activity.startActivity(intent);
					activity.finish();
				}
			});*/
		} else {
			//convertView.setBackgroundResource(R.drawable.backup);//item_bkg
			viewHolder.date.setTextColor(Color.BLACK);
			viewHolder.date.setTextSize(18);
			viewHolder.nongliDate.setTextColor(color.white);
			viewHolder.nongliDate.setTextSize(12);
			if (list.get(position).isHoliday())
				viewHolder.nongliDate.setTextColor(color.calendar_font);
			else if (list.get(position).isThisMonth() == false) {
				viewHolder.date.setTextColor(Color.rgb(210, 210, 210));
			}
			else if (list.get(position).isWeekend()) {
				viewHolder.date.setTextColor(Color.rgb(255, 97, 0));
			}
		}
		if (list.get(position).getNongliDate().length() > 3)
			viewHolder.nongliDate.setTextSize(10);
		if (list.get(position).getNongliDate().length() >= 5)
			viewHolder.nongliDate.setTextSize(8);
		convertView.setLayoutParams(new GridView.LayoutParams(LayoutParams.WRAP_CONTENT, DataUtils.getScreenWidth(activity) / 7));
		return convertView;
	}

	public class ViewHolder {
		public TextView date;
		public TextView nongliDate;
	}

}
