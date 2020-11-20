package cn.suanzi.baomi.cust.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.suanzi.baomi.base.adapter.CommenViewHolder;
import cn.suanzi.baomi.base.adapter.CommonListViewAdapter;
import cn.suanzi.baomi.base.pojo.BankList;
import cn.suanzi.baomi.cust.R;

public class CardSelectAdapter extends CommonListViewAdapter<BankList> {

	public CardSelectAdapter(Context context, List<BankList> datas) {
		super(context, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mContext, convertView, parent,
				R.layout.fragment_myhomebank_item, position);
		BankList item = (BankList) getItem(position);// 在数据源中获取实体类对象
		((TextView) holder.getView(R.id.tv_banklist_bankname)).setText(item.getBankName());// 银行卡名称
		((TextView) holder.getView(R.id.tv_banklist_pre_six)).setText(item.getAccountNbrPre6());// 银行卡前六位
		((TextView) holder.getView(R.id.tv_banklist_last_four)).setText(item.getAccountNbrLast4());// 银行卡后四位
		return holder.getConvertView();
	}

}
