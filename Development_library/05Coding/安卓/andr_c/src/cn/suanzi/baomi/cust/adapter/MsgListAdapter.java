// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.cust.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.adapter.CommenViewHolder;
import cn.suanzi.baomi.base.adapter.CommonListViewAdapter;
import cn.suanzi.baomi.base.pojo.Messages;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.application.CustConst;

/**
 * 消息列表
 * @author yanfang.li
 */
public class MsgListAdapter extends CommonListViewAdapter<Messages>{
	
	private static final String TAG = MsgListAdapter.class.getSimpleName();
	private int msg_flag; // 1
	/**
	 * 确定泛型后必须写的构造方法
	 * @param context
	 * @param datas
	 */
	public MsgListAdapter(Activity activity, List<Messages> datas,int flag) {
		super(activity, datas);
		this.msg_flag = flag; // 0 代表是会员沟通  1代表的是会员卡 2优惠券 3异业广播 
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, 
				R.layout.item_massage_list, position);
		Messages msg = (Messages)getItem(position);//在数据源中获取实体类对象
		
		String message = null;
		String logoUrl = msg.getLogoUrl();
		if (msg_flag == CustConst.Massage.MSG_SHOP) {
			message = msg.getContent();
		} else if (msg_flag == CustConst.Massage.MSG_VIP) {
			message = msg.getMessage();
		} else if (msg_flag == CustConst.Massage.MSG_CARD) {
			message = msg.getContent();
		}
		ImageView ivCouponImage = ((ImageView) holder.getView(R.id.iv_msghead));
		Util.showImage(mActivity, logoUrl, ivCouponImage);// 商家图片显示图片
		String shopName = "";
		if (null == msg.getShopName() || Util.isEmpty(msg.getShopName())) {
			shopName = mActivity.getString(R.string.shop_vip_name);
		} else {
			shopName = msg.getShopName();
		}
		((TextView) holder.getView(R.id.tv_msgname)).setText(shopName);
		((TextView) holder.getView(R.id.tv_msgcontent)).setText(message);
		((TextView) holder.getView(R.id.tv_msgdate)).setText(msg.getCreateTime());
		TextView tvMsgPrompt = holder.getView(R.id.tv_msgprompt);
		if (msg.getUnreadCount() > 0) {
			tvMsgPrompt.setVisibility(View.VISIBLE);
			tvMsgPrompt.setText(msg.getUnreadCount()+"");
		} else {
			tvMsgPrompt.setVisibility(View.GONE);
		}
		return holder.getConvertView();
	}

}