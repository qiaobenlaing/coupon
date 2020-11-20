package com.huift.hfq.shop.view;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.huift.hfq.shop.activity.AccntStatActivity;
import com.huift.hfq.shop.adapter.CalendarAdapter;

public class OnItemClickListenerImpl implements OnItemClickListener {

	private CalendarAdapter adapter = null;
	private AccntStatActivity activity = null;

	public OnItemClickListenerImpl(CalendarAdapter adapter, AccntStatActivity activity) {
		this.adapter = adapter;
		this.activity = activity;
	}

	public void onItemClick(AdapterView<?> gridView, View view, final int position, long id) {
		if (activity.mCurrList.get(position).isThisMonth() == false) {
			return;
		}
		adapter.setSelectedPosition(position);
		adapter.notifyDataSetInvalidated();
		activity.mLastSelected = activity.mCurrList.get(position).getDate();
	}
}
