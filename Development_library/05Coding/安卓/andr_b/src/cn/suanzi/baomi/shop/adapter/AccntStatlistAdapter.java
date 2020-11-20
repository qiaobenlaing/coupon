package cn.suanzi.baomi.shop.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.suanzi.baomi.base.pojo.AccntStatItem;
import cn.suanzi.baomi.shop.R;

/**
 * @author wensi.yu
 * 获得银行卡刷卡对账的统计列表
 *
 */
public class AccntStatlistAdapter extends CommonListViewAdapter<AccntStatItem> {

	public AccntStatlistAdapter(Context mContext, List<AccntStatItem> datas) {
		super(mContext, datas);
	}
	
	@Override
	public void setItems(List<AccntStatItem> datas) {
		
		super.setItems(datas);
	}
	@Override
	public void addItems(List<AccntStatItem> datas) {
		
		super.addItems(datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mContext , convertView , parent, 
				R.layout.activity_accntstat_list_item, position);
		
		AccntStatItem item = (AccntStatItem) getItem(position);//在数据源中获取实体类对象
		((TextView) holder.getView(R.id.tv_accnt_banklistcode)).setText(item.getAccntBankListCode());//对账日期编号
		((TextView) holder.getView(R.id.tv_accnt_data)).setText(item.getDate());//对账日期	
		((TextView) holder.getView(R.id.tv_accnt_consumeCount)).setText(item.getConsumeCount());//刷卡笔数
		((TextView) holder.getView(R.id.tv_accnt_consumeAmount)).setText(item.getConsumeAmount());//刷卡金额		
		((TextView) holder.getView(R.id.tv_accnt_rebateAmount)).setText(item.getRebateAmount());//回佣金额
		((TextView) holder.getView(R.id.tv_accnt_theAmountCredited)).setText(item.getTheAmountCredited());//入账金额
		((TextView) holder.getView(R.id.tv_accnt_saving)).setText(item.getSaving());//总存款数
		
		return holder.getConvertView();
		
	}
}
