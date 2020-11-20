// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.cust.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.Message;
import com.huift.hfq.cust.R;

/**
 * 提交用户反馈
 * @author qian.zhou
 */
public class SuggestListAdapter extends BaseAdapter {
	
	public final static String TAG = SuggestListAdapter.class.getSimpleName();
	public final static String SEND_MSG = "sendmsg";
	public final static String READ_MSG = "readmsg";
	private Activity mActivity;
	private TextView tvReadMsg = null;
	
	public static interface IMsgViewType {
		int IMVT_COM_MSG = 0;// 收到对方的消息
		int IMVT_TO_MSG = 1;// 自己发送出去的消息
	}

	private static final int ITEMCOUNT = 2;// 消息类型的总数
	private List<Message> coll;// 消息对象数组
	private LayoutInflater mInflater;

	public SuggestListAdapter(Context context, List<Message> coll) {
		this.coll = coll;
		mInflater = LayoutInflater.from(context);
		mActivity = (Activity) context;
	}

	public int getCount() {
		return coll.size();
	}

	public Object getItem(int position) {
		return coll.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * 得到Item的类型，是对方发过来的消息，还是自己发送出去的
	 */
	public int getItemViewType(int position) {
		Message msg = coll.get(position);

		if (msg.isMsgflag()) {// 收到的消息
			return IMsgViewType.IMVT_COM_MSG;
		} else {// 自己发送的消息
			return IMsgViewType.IMVT_TO_MSG;
		}
	}

	/**
	 * Item类型的总数
	 */
	public int getViewTypeCount() {
		return ITEMCOUNT;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		Message msg = coll.get(position);
		boolean isComMsg = msg.isMsgflag();
		// int isComMsg = msg.getFrom_where();
		Log.d("isComMsg", "isComMsg="+isComMsg);
		ViewHolder viewHolder = null;
		ImageView ivSendState = null;
		if (convertView == null) {
			if (isComMsg) {
				Log.d(TAG, "################1");
				convertView = mInflater.inflate(R.layout.item_chatmsg_left, null);
				tvReadMsg = (TextView) convertView.findViewById(R.id.tv_readcontent);
			} else {
				Log.d(TAG, "################2");
				convertView = mInflater.inflate(R.layout.item_chatmsg_right, null);
				ivSendState = (ImageView)convertView.findViewById(R.id.iv_sendstate);
			}

			viewHolder = new ViewHolder();
			viewHolder.tvSendTime = (TextView) convertView.findViewById(R.id.tv_sendtime);
			viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_chatcontent);
			viewHolder.icon = (ImageView) convertView.findViewById(R.id.iv_userhead);
			if (tvReadMsg != null) {
				viewHolder.tvReadMsg = tvReadMsg;
			}
			if (ivSendState != null) {
				viewHolder.ivSendState = ivSendState;
			}
			viewHolder.isComMsg = isComMsg;

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tvSendTime.setText(msg.getCreateTime()+"");
		viewHolder.tvContent.setText(msg.getContent());
		if (isComMsg) {
			viewHolder.icon.setBackgroundResource(R.drawable.ic_launcher);
		} else {
			Util.showImage(mActivity, msg.getAvatarUrl(), viewHolder.icon);
		}
		
		String sendState = DB.getStr(SEND_MSG);
		String succCode = ErrorCode.SUCC+"";
		// 判断消息是否发送成功
		if (!Util.isEmpty(sendState)) {
			if (ivSendState != null) {
				if (succCode.equals(succCode)) { // 发送成功
					ivSendState.setVisibility(View.GONE);
				} else { // 发送失败
					ivSendState.setVisibility(View.VISIBLE);
				}
			}
		}
		
		String readState = DB.getStr(READ_MSG);
		// 我是否已经读过消息
		if (!Util.isEmpty(readState)) {
				
			if (tvReadMsg != null) {
				if (succCode.equals(readState)) {
					Log.d(TAG, "readState=12"+readState);
					tvReadMsg.setText(mActivity.getResources().getString(R.string.msg_read));
				} else {
					tvReadMsg.setText(mActivity.getResources().getString(R.string.msg_noread));
				}
			}
		}
		
		return convertView;
	}

	static class ViewHolder {
		public TextView tvSendTime;
		public TextView tvContent;
		public TextView tvReadMsg;
		public ImageView ivSendState;
		public ImageView icon;
		public boolean isComMsg = true;
	}

}
