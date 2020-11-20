package com.huift.hfq.cust.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.huift.hfq.base.pojo.BatchCoupon;

public class HomeCouponsAdapter extends ArrayAdapter<BatchCoupon> {
	private Context context;
	private int resource;

	public HomeCouponsAdapter(Context context, int resource,
			List<BatchCoupon> objects) {
		super(context, resource, objects);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(context, resource, null);
		}

		BatchCoupon coupon = (BatchCoupon) getItem(position);

		return convertView;
	}

}
