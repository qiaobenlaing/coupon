package cn.suanzi.baomi.cust.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.adapter.CommenViewHolder;
import cn.suanzi.baomi.base.adapter.CommonListViewAdapter;
import cn.suanzi.baomi.base.pojo.Bonus;
import cn.suanzi.baomi.cust.R;

public class MyHomeRedBagAdapter extends CommonListViewAdapter<Bonus> {
	private List<Bonus> bonus;
	private Activity activity;
	
	
	public MyHomeRedBagAdapter(Activity activity, List<Bonus> datas) {
		super(activity, datas);
		this.activity = activity;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.redbag_item, position);
		final Bonus item = (Bonus) getItem(position);
		ImageView listUserImage = ((ImageView) holder.getView(R.id.redbag_logo));
		Util.showImage(mActivity, item.getLogoUrl(), listUserImage);
		
		TextView redBag = holder.getView(R.id.redbag_value);
		if ("".equals(item.getTotalValue()) || null == item.getTotalValue()) {
			redBag.setText("");
		}else{
			redBag.setText(item.getTotalValue() + "元");
		}
		((TextView) holder.getView(R.id.redbag_date)).setText(item.getGetDate());
		return holder.getConvertView();
	}

	/*public static class ViewHolder extends RecyclerView.ViewHolder {
		private TextView valueTextView, dateTextView;
		private ImageView logoUrlImageView;

		public ViewHolder(View arg0) {
			super(arg0);
			logoUrlImageView = (ImageView) arg0.findViewById(R.id.redbag_logo);
			valueTextView = (TextView) arg0.findViewById(R.id.redbag_value);
			dateTextView = (TextView) arg0.findViewById(R.id.redbag_date);
			
		}
	}

	public MyHomeRedBagAdapter(Activity activity, List<Bonus> bonus) {
		this.bonus = bonus;
		this.activity = activity;
	}

	@Override
	public int getItemCount() {
		return bonus.size();
	}

	@Override
	public void onBindViewHolder(final ViewHolder arg0, final int arg1) {
		Bonus bonu = bonus.get(arg1);
		arg0.itemView.setTag(bonu);
		Util.showImage(activity, bonu.getLogoUrl(), arg0.logoUrlImageView);
		arg0.valueTextView.setText(bonu.getTotalValue() + "元");
		arg0.dateTextView.setText(bonu.getGetDate());
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View v = LayoutInflater.from(arg0.getContext()).inflate(R.layout.redbag_item, arg0, false);
		ViewHolder viewHolder = new ViewHolder(v);
		return viewHolder;
	}*/

	
	
}