package cn.suanzi.baomi.cust.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.suanzi.baomi.base.adapter.CommenViewHolder;
import cn.suanzi.baomi.base.adapter.CommonListViewAdapter;
import cn.suanzi.baomi.base.pojo.BankList;
import cn.suanzi.baomi.base.utils.DialogUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.model.TerminateBankAccount;

public class BankListAdapter extends CommonListViewAdapter<BankList> {

	public BankListAdapter(Context context, List<BankList> datas) {
		super(context, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mContext, convertView, parent,
				R.layout.bank_list_item, position);//fragment_myhomebank_item
		BankList item = (BankList) getItem(position);// 在数据源中获取实体类对象
		((TextView) holder.getView(R.id.tv_banklist_bankname)).setText(item.getBankName());// 银行卡名称
		((TextView) holder.getView(R.id.tv_banklist_pre_six)).setText(item.getAccountNbrPre6());// 银行卡前六位
		((TextView) holder.getView(R.id.tv_banklist_last_four)).setText(item.getAccountNbrLast4());// 银行卡后四位
		
		View view = holder.getView(R.id.bank_item_delete);
		view.setTag(position);
		view.setOnClickListener(new DeleteOnclickListener());
		return holder.getConvertView();
	}
	
	class DeleteOnclickListener implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			if(v.getId()==R.id.bank_item_delete){
				final int position = (Integer) v.getTag();
				//获取对应位置的数据(银行卡信息)
				if(mDatas==null){
					return;
				}
				final BankList bankList = mDatas.get(position);
				
				DialogUtils.showDialog((Activity)mContext, getStrings(R.string.dialog_ubblind_card), 
						getStrings(R.string.dialog_ubblind_card_content)+bankList.getAccountNbrLast4()+"?",
						getStrings(R.string.dialog_ok), getStrings(R.string.dialog_cancel), new DialogUtils().new OnResultListener() {

							@Override
							public void onOK() {
								//确认取消支付
								cancleBlindCard(bankList.getBankAccountCode(),position);
							}

				});
			}
		}
		
		/**
		 * 获取字符串
		 * @param id
		 * @return
		 */
		private String getStrings (int id) {
			return mContext.getResources().getString(id);
		}
		
		/**
		 *解除绑定银行卡
		 */
		public void cancleBlindCard(String bankAccountCode,final int position){
			//Toast.makeText(mContext, bankAccountCode, 0).show();
			
			new TerminateBankAccount((Activity)mContext,new TerminateBankAccount.Callback() {
				@Override
				public void getResult(boolean result) {
					if(result){ //解除绑定成功
						//更新数据
						mDatas.remove(position);
						BankListAdapter.this.notifyDataSetChanged();
					}else{ //解除绑定失败
						//TODO
					}	
				}
			}).execute(bankAccountCode);
		}
		
	}
}
