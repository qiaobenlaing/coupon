package com.huift.hfq.shop.view;

import com.huift.hfq.shop.activity.AccntStatActivity;
import com.huift.hfq.shop.adapter.CalendarAdapter;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class OnItemClickListenerImpl implements OnItemClickListener {
	
	private final static String TAG = "OnItemClickListenerImpl";
	
	private CalendarAdapter adapter = null;
	private AccntStatActivity activity = null;
	
	public OnItemClickListenerImpl(CalendarAdapter adapter, AccntStatActivity activity) {
		this.adapter = adapter;
		this.activity = activity;
	}
	
	public void onItemClick(AdapterView<?> gridView, View view, final int position, long id) {
		Log.i("TAG", "cccccccccccccccccc====1111");
		if (activity.mCurrList.get(position).isThisMonth() == false) {
			return;
		}
		Log.i("TAG", "cccccccccccccccccc====22222");
		adapter.setSelectedPosition(position);  
		adapter.notifyDataSetInvalidated(); 
		activity.mLastSelected = activity.mCurrList.get(position).getDate();
	}
}
