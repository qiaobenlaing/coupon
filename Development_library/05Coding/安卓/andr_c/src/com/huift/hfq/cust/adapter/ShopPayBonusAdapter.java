package com.huift.hfq.cust.adapter;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import com.huift.hfq.base.pojo.Bonus;
import com.huift.hfq.cust.R;

public class ShopPayBonusAdapter extends Adapter<ShopPayBonusAdapter.ViewHolder> {
	private static final String TAG = ShopPayBonusAdapter.class.getSimpleName();
	private List<Bonus> bonus;
	private OnItemClickListener mOnItemClickLitener;

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private TextView bonusCodeTextView, bonusValueTextView;

		public ViewHolder(View arg0) {
			super(arg0);
			bonusCodeTextView = (TextView) arg0.findViewById(R.id.bonus_code);
			bonusValueTextView = (TextView) arg0.findViewById(R.id.bonus_value);
		}
	}

	public ShopPayBonusAdapter(List<Bonus> bonus) {
		this.bonus = bonus;
		Log.d(TAG, "userBonusList="+bonus.size());
	}

	@Override
	public int getItemCount() {
		return bonus.size();
	}

	@Override
	public void onBindViewHolder(final ViewHolder arg0, final int arg1) {
		Bonus bonu = bonus.get(arg1);
		arg0.itemView.setTag(bonu);
//		arg0.bonusCodeTextView.setText(bonu.getUserBonusCode());
		arg0.bonusCodeTextView.setText("10001");
		arg0.bonusValueTextView.setText(bonu.getValue());
		// 如果设置了回调，则设置点击事件
		if (mOnItemClickLitener != null) {
			arg0.itemView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mOnItemClickLitener.onItemClick(null, arg0.itemView, arg1, arg0.getItemId());
				}
			});
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View v = LayoutInflater.from(arg0.getContext()).inflate(R.layout.shop_detail_bonus_item, arg0, false);
		ViewHolder viewHolder = new ViewHolder(v);
		return viewHolder;
	}

	public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
		this.mOnItemClickLitener = mOnItemClickLitener;
	}
}
