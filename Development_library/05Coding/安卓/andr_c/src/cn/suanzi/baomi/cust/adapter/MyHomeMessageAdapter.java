package cn.suanzi.baomi.cust.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.suanzi.baomi.base.adapter.CommenViewHolder;
import cn.suanzi.baomi.base.adapter.CommonListViewAdapter;
import cn.suanzi.baomi.base.api.DateUtils;
import cn.suanzi.baomi.base.pojo.Message;
import cn.suanzi.baomi.cust.R;

/**
 * 
 * @author qian.zhou
 */
public class MyHomeMessageAdapter extends CommonListViewAdapter<Message> {
	/**数据源**/
	private List<Message> mMessage;
	
	private final static String FORMATTER = "yyyy-MM-dd HH:mm:ss"; 
	
	public MyHomeMessageAdapter(Activity activity, List<Message> message) {
		
		super(activity, message);
		mMessage = message;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_msgcoupon, position);
		Message msg = mMessage.get(position);
		((TextView)holder.getView(R.id.msg_couponname)).setText(msg.getTitle());
		((TextView)holder.getView(R.id.msg_couponcon)).setText(msg.getContent());
		String createTime = DateUtils.parseDate(DateUtils.stringToDate(msg.getCreateTime(), FORMATTER), FORMATTER);
		((TextView)holder.getView(R.id.msg_coupontime)).setText(createTime);
		return holder.getConvertView();
	}
}
