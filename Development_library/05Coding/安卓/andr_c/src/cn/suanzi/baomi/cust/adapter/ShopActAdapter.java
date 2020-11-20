package cn.suanzi.baomi.cust.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.suanzi.baomi.base.adapter.CommenViewHolder;
import cn.suanzi.baomi.base.adapter.CommonListViewAdapter;
import cn.suanzi.baomi.base.pojo.Activitys;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.activity.ActIcBcDetailActivity;
import cn.suanzi.baomi.cust.fragment.ActIcBcDetailFragment;

/**
 * 商店详情
 * @author ad
 *
 */
public class ShopActAdapter extends CommonListViewAdapter<Activitys> {
	private final static String TAG = ShopActAdapter.class.getSimpleName();
	
	public ShopActAdapter(Activity activity, List<Activitys> datas) {
		super(activity, datas);
	}

	@Override
	public void setItems(List<Activitys> shops) {
		super.setItems(shops);
	}
	@Override
	public void addItems(List<Activitys> shops) {
		super.addItems(shops);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView, parent,R.layout.item_shop_act, position);
		final Activitys act = mDatas.get(position);
		final TextView tvShopName = holder.getView(R.id.tv_shopact);
		tvShopName.setText(act.getActivityName() + act.getActNumber());
		View view = holder.getConvertView();
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String typeflag = "0"; 
				Intent intent = new Intent(mActivity, ActIcBcDetailActivity.class);
				intent.putExtra(ActIcBcDetailFragment.ACTIVITY_CODE, act.getActivityCode());
				intent.putExtra(ActIcBcDetailFragment.TYPE, typeflag);
				mActivity.startActivity(intent);
			}
		});
		return view;
	}
}
