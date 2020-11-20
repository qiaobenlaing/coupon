package cn.suanzi.baomi.cust.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.adapter.CommenViewHolder;
import cn.suanzi.baomi.base.adapter.CommonListViewAdapter;
import cn.suanzi.baomi.base.pojo.Shop;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.util.SkipActivityUtil;

public class LikeShopAdapter extends CommonListViewAdapter<Shop> {
	
	private final static String TAG = "LikeShopAdapter";
	
	private List<Shop> mDatas;
	public LikeShopAdapter(Activity activity, List<Shop> datas) {
		super(activity, datas);
		mDatas = datas;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView, parent,R.layout.item_grab, position);
		final Shop shop = mDatas.get(position);
		ImageView shopHead = holder.getView(R.id.iv_shophead);
		Util.showImage(mActivity, shop.getLogoUrl() , shopHead);
		((TextView) holder.getView(R.id.tv_shopname)).setText(shop.getShopName());
		TextView tvGoTo = holder.getView(R.id.tv_gotolike);
		tvGoTo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SkipActivityUtil.skipNewShopDetailActivity(mActivity, shop.getShopCode());
			}
		});
		return holder.getConvertView();
	}

}
