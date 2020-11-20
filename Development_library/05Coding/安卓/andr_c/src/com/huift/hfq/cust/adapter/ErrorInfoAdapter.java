package com.huift.hfq.cust.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.huift.hfq.base.adapter.CommenViewHolder;
import com.huift.hfq.base.adapter.CommonListViewAdapter;
import com.huift.hfq.base.pojo.ErrorInfo;
import com.huift.hfq.cust.R;

public class ErrorInfoAdapter extends CommonListViewAdapter<ErrorInfo>{

	
	public ErrorInfoAdapter(Activity activity, List<ErrorInfo> datas) {
		super(activity, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_errorinfo, position);
		final ErrorInfo item = (ErrorInfo) getItem(position);//在数据源中获取实体类对象
		((TextView) holder.getView(R.id.tv_item_errorinfo)).setText(item.getInfo());
		return holder.getConvertView();
	}

}
