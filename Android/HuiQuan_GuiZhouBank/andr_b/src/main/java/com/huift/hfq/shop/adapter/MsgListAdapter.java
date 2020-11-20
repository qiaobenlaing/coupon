// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Messages;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopConst;

import java.util.List;

/**
 * 消息列表
 * 
 * @author yanfang.li
 */
public class MsgListAdapter extends CommonListViewAdapter<Messages> {

	private final static String TAG = MsgListAdapter.class.getSimpleName();
	private int msg_flag;

	/**
	 * 确定泛型后必须写的构造方法
	 * 
	 * @param activity
	 * @param datas
	 */
	public MsgListAdapter(Activity activity, List<Messages> datas, int flag) {
		super(activity, datas);
		this.msg_flag = flag; // 0 代表是会员沟通 1代表的是会员卡 2优惠券 3异业广播
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView, parent, R.layout.item_massage_list, position);
		final Messages msg = (Messages) getItem(position);// 在数据源中获取实体类对象
		// 显示未读消息的
		String avatarUrl = null;
		String message = null;
		String name = null;
		if (msg_flag == ShopConst.Massage.MSG_SHOP) {
			avatarUrl = msg.getLogoUrl();
			if (null == msg.getShopName() || Util.isEmpty(msg.getShopName())) {
				name = mActivity.getString(R.string.shop_vip_name);
			} else {
				name = msg.getShopName();
			}
			message = msg.getContent();
		} else if (msg_flag == ShopConst.Massage.MSG_VIP) {
			avatarUrl = msg.getUserAvatar();
			if (null == msg.getUserName() || Util.isEmpty(msg.getUserName())) {
				name = mActivity.getString(R.string.cardvip_name);
			} else {
				name = msg.getUserName();
			}
			message = msg.getMessage();
		} else if (msg_flag == ShopConst.Massage.MSG_CARD) {
			avatarUrl = msg.getAvatarUrl();
			message = msg.getContent();
			if (null == msg.getNickName() || Util.isEmpty(msg.getNickName())) {
				name = mActivity.getString(R.string.cardvip_name);
			} else {
				name = msg.getNickName();
			}
		}
		ImageView ivCardImage = ((ImageView) holder.getView(R.id.iv_msghead));
		Util.showImage(mActivity, avatarUrl, ivCardImage);// 商家图片显示图片

		((TextView) holder.getView(R.id.tv_msgname)).setText(name);
		((TextView) holder.getView(R.id.tv_msgcontent)).setText(message);
		((TextView) holder.getView(R.id.tv_msgdate)).setText(msg.getCreateTime());
		TextView tvMsgPrompt = holder.getView(R.id.tv_msgprompt);
		if (msg.getUnreadCount() > 0) {
			tvMsgPrompt.setVisibility(View.VISIBLE);
			tvMsgPrompt.setText(msg.getUnreadCount() + "");
		} else {
			tvMsgPrompt.setVisibility(View.GONE);
		}

		return holder.getConvertView();
	}

}
