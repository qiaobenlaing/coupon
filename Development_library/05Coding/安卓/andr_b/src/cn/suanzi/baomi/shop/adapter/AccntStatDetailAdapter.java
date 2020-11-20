package cn.suanzi.baomi.shop.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.suanzi.baomi.base.pojo.AccntStatDetail;
import cn.suanzi.baomi.shop.R;

/**
 * @author wensi.yu
 * 银行卡对账详情
 */
public class AccntStatDetailAdapter extends CommonListViewAdapter<AccntStatDetail> {

	public AccntStatDetailAdapter(Context context, List<AccntStatDetail> datas) {
		super(context, datas);
	}
	
	@Override
	public void setItems(List<AccntStatDetail> datas) {
		
		super.setItems(datas);
	}
	@Override
	public void addItems(List<AccntStatDetail> datas) {
		
		super.addItems(datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mContext , convertView , parent, 
				R.layout.activity_accntstat_detail_item, position);
		AccntStatDetail item = (AccntStatDetail)getItem(position);//在数据源中获取实体类对象		
		((TextView) holder.getView(R.id.tv_accntdetail_time)).setText(item.getTime());//交易时间
		((TextView) holder.getView(R.id.tv_accntdetail_accountNbr)).setText(item.getAccountNbr());//交易卡号
		((TextView) holder.getView(R.id.tv_accntdetail_price)).setText(item.getPrice());//交易金额
		((TextView) holder.getView(R.id.tv_accntdetail_bankName)).setText(item.getBankName());//发卡银行
		((TextView) holder.getView(R.id.tv_accntdetail_Type)).setText(item.getType());//交易类型
		((TextView) holder.getView(R.id.tv_accntdetail_channel)).setText(item.getChannel());//交易渠道
		((TextView) holder.getView(R.id.tv_accntdetail_serialNbr)).setText(item.getSerialNbr());//流水号
		((TextView) holder.getView(R.id.tv_accntdetail_coderievalNbr)).setText(item.getCoderievalNbr());//检索号
		((TextView) holder.getView(R.id.tv_accntdetail_rebate)).setText(item.getRebate());//交易回佣
		((TextView) holder.getView(R.id.tv_accntdetail_situation)).setText(item.getSituation());//对账情况
		((TextView) holder.getView(R.id.tv_accntdetail_installmentFee)).setText(item.getInstallmentFee());//分期手续费
		((TextView) holder.getView(R.id.tv_accntdetail_installmentNbr)).setText(item.getInstallmentNbr());//分期期数
		((TextView) holder.getView(R.id.tv_accntdetail_rate)).setText(item.getRate());//持卡人费率
		((TextView) holder.getView(R.id.tv_accntdetail_fee)).setText(item.getFee());//持卡人手续费
		return holder.getConvertView();
	}
}
