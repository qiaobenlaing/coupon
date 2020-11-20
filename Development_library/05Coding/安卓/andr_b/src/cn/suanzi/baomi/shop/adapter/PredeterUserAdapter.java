// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.UserMessage;
import cn.suanzi.baomi.shop.R;

/**
 * 预定名单的具体用户适配器
 * @author qian.zhou
 */
public class PredeterUserAdapter extends CommonListViewAdapter<UserMessage>{
	private final static String TAG = PredeterUserAdapter.class.getSimpleName();
	
	public PredeterUserAdapter(Activity activity, List<UserMessage> datas) {
		super(activity, datas);
	}
	
	@Override      
	public void setItems(List<UserMessage> myorderItem) {
		super.setItems(myorderItem);
	}
	@Override
	public void addItems(List<UserMessage> myorderItem) {
		super.addItems(myorderItem);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_act_user, position);
		final UserMessage userMessage = mDatas.get(position);//在数据源中获取实体类对象
		TextView tvTicketType =  holder.getView(R.id.tv_ticket_type);//票的种类
		TextView tvTicketNum =  holder.getView(R.id.tv_ticket_num);//票的数量
		TextView tvTicketPrice =  holder.getView(R.id.tv_ticket_price);//购票价格
		
		//票的种类
		tvTicketType.setText(!Util.isEmpty(userMessage.getDes()) ? userMessage.getDes() + "票" : "");
		//票的数量
		tvTicketNum.setText(!Util.isEmpty(userMessage.getCount()) ? "X" + userMessage.getCount() : "");
		//票的数量
		tvTicketPrice.setText(!Util.isEmpty(userMessage.getPrice()) ? "￥" + userMessage.getPrice() : "");
		return holder.getConvertView();
	}
}
