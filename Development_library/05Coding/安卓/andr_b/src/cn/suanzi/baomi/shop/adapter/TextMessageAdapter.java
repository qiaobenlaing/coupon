package cn.suanzi.baomi.shop.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.suanzi.baomi.base.adapter.CommenViewHolder;
import cn.suanzi.baomi.base.adapter.CommonListViewAdapter;
import cn.suanzi.baomi.base.pojo.TextMessage;
import cn.suanzi.baomi.base.utils.DialogUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.model.DelMRecipient;

/**
 * 短信接受者
 * @author liyanfang
 */
public class TextMessageAdapter extends CommonListViewAdapter<TextMessage> {

	public TextMessageAdapter(Activity activity, List<TextMessage> datas) {
		super(activity, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView, parent,R.layout.item_textmsg, position);//fragment_myhomebank_item
		TextMessage textMessage = (TextMessage) getItem(position);// 在数据源中获取实体类对象
		((TextView) holder.getView(R.id.tv_name)).setText("姓名：" + textMessage.getStaffName());// 接收者姓名
		((TextView) holder.getView(R.id.tv_tel_no)).setText("电话号码：" + textMessage.getMobileNbr());// 电话号码
		
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
				final TextMessage textMessage = mDatas.get(position);
				
				DialogUtils.showDialog(mActivity, getStrings(R.string.cue), 
						getStrings(R.string.txt_dialog_title)+textMessage.getStaffName()+"接收短息?",
						getStrings(R.string.txt_dialog_ok), getStrings(R.string.txt_dialog_cancel), new DialogUtils().new OnResultListener() {

							@Override
							public void onOK() {
								// 确认删除
								delTextMsg(textMessage.getRecipientId(), position);
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
			return mActivity.getResources().getString(id);
		}
		
		/**
		 * 删除联系人
		 * @param id 接收者的Id
		 * @param position 列表中的位置
		 */
		public void delTextMsg(String id,final int position){
			new DelMRecipient(mActivity,new DelMRecipient.Callback() {
				@Override
				public void getResult(boolean result) {
					if(result){ //解除绑定成功
						//更新数据
						mDatas.remove(position);
						TextMessageAdapter.this.notifyDataSetChanged();
					}else{ //解除绑定失败
						//TODO
					}	
				}
			}).execute(id);
		}
		
	}
}
