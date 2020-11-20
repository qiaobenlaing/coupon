package cn.suanzi.baomi.shop.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.suanzi.baomi.base.pojo.AccntStat;
import cn.suanzi.baomi.shop.R;

/**
 * @author wensi.yu
 * 银行卡对账列表日期
 */
public class AccntStatAdapter extends CommonListViewAdapter<AccntStat> {

	public AccntStatAdapter(Context context, List<AccntStat> datas) {
		super(context, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mContext , convertView , parent, 
				R.layout.activity_accntstat_item, position);
		AccntStat item = (AccntStat)getItem(position);//在数据源中获取实体类对象		
		//((TextView) holder.getView(R.id.tv_BankCode)).setText(item.getAcctCode());//编码
		((TextView) holder.getView(R.id.tv_accntstat_text)).setText(item.getAcctDay());//日期
		return holder.getConvertView();
	}
}
