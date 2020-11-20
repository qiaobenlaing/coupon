package cn.suanzi.baomi.cust.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.User;
import cn.suanzi.baomi.cust.R;

public class NewShopVisitAdapter extends BaseAdapter {
	private Context context;
	private List<User> datas;
	
	
	public NewShopVisitAdapter(Context context, List<User> datas) {
		super();
		this.context = context;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder  = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_shop_visit, null);
			holder.head = (ImageView) convertView.findViewById(R.id.iv_user_head);
			holder.name = (TextView) convertView.findViewById(R.id.tv_user_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		User user = datas.get(position);
		Util.showFirstImages((Activity)context, user.getAvatarUrl(), holder.head);
		if(!Util.isEmpty(user.getNickName())){
			holder.name.setText(user.getNickName());
		}
		
		return convertView;
	}
	class ViewHolder{
		ImageView head;
		TextView name;
	}
}
