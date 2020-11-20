package cn.suanzi.baomi.cust.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import cn.suanzi.baomi.base.pojo.BatchCoupon;

public class CouponsAdapter extends ArrayAdapter<BatchCoupon> {
	private Context context;
	private int resource;

	public CouponsAdapter(Context context, int resource, List<BatchCoupon> objects) {
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
