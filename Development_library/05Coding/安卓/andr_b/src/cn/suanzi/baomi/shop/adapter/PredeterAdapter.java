// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.adapter;

import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.User;
import cn.suanzi.baomi.base.view.MyListView;
import cn.suanzi.baomi.shop.R;

/**
 * 预定名单的适配器
 * @author qian.zhou
 */
public class PredeterAdapter extends BaseAdapter{
	private final static String TAG = PredeterAdapter.class.getSimpleName();
	
	private Activity mActivity;
	private List<User> list;
	
	public PredeterAdapter(Activity activity, List<User> list) {
		super();
		this.mActivity = activity;
		this.list = list;
	}
	
	public void addItems(List<User> datas){
		if (datas == null) {
			return ;
		}
		if (list == null) {
			list = datas;
		}else{
			list.addAll(datas);
		}
		notifyDataSetChanged();
	}
	
	public void setItems(List<User> datas){
		list = datas;
		notifyDataSetChanged();
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null ) {
			
			holder = new ViewHolder();
			convertView = View.inflate(mActivity, R.layout.item_predeter, null);
			holder.ivUserLogo = (ImageView) convertView.findViewById(R.id.iv_act_logo);
			holder.tvUserName =  (TextView) convertView.findViewById(R.id.tv_username);//用户名称
			holder.tvUser =  (TextView) convertView.findViewById(R.id.tv_userphone);//用户信息
			holder.tvOrderNum = (TextView) convertView.findViewById(R.id.tv_num);//购票数量
			holder.lvUser = (MyListView) convertView.findViewById(R.id.lv_act_user);
			convertView.setTag(holder);
		} else {
			
			holder = (ViewHolder) convertView.getTag();
		}
		User user = list.get(position);
		//用户头像
		if (!Util.isEmpty(user.getAvatarUrl())) {
			Util.showImage(mActivity, user.getAvatarUrl(), holder.ivUserLogo);
		} else {
			Log.d(TAG, "图片的路径为空");
		}
		//用户名称
		holder.tvUserName.setText(!Util.isEmpty(user.getNickName()) ? user.getNickName() : "");
		//用户信息
		if (!Util.isEmpty(user.getRealName()) || !Util.isEmpty(user.getMobileNbr())) {
			
			holder.tvUser.setText( user.getRealName() + user.getMobileNbr());
		}
		//购票数量
		holder.tvOrderNum.setText( "X" + user.getTotalNbr());
		//赋值
		PredeterUserAdapter adapter = new PredeterUserAdapter(mActivity, user.getFeeScale());
		holder.lvUser.setAdapter(adapter);
		return convertView;
	}
	
	public class ViewHolder{
		public ImageView ivUserLogo;
		public TextView tvUserName;
		public TextView tvUser;
		public TextView tvOrderNum;
		public MyListView lvUser;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	/*public void setVisibility (boolean num){
		if (num) {
			holder.tvOrderNum.setVisibility(View.VISIBLE);
		} else {
			holder.tvOrderNum.setVisibility(View.GONE);
		}
	}*/
}
