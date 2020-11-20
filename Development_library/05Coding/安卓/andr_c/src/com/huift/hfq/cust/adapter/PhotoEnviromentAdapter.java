package com.huift.hfq.cust.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.adapter.CommenViewHolder;
import com.huift.hfq.base.adapter.CommonListViewAdapter;
import com.huift.hfq.base.pojo.Decoration;
import com.huift.hfq.cust.R;

/**
 * 商家环境图片
 * @author ad
 */
public class PhotoEnviromentAdapter extends CommonListViewAdapter<Decoration> {
	
	private final static String TAG = PhotoEnviromentAdapter.class.getSimpleName();
	
	private List<Decoration> mDatas;
	public PhotoEnviromentAdapter(Activity activity, List<Decoration> datas) {
		super(activity, datas);
		mDatas = datas;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView, parent,R.layout.item_photo_environment, position);
		final Decoration decoration = mDatas.get(position);
		ImageView productImage = holder.getView(R.id.tv_envirte_img);
		Util.showImage(mActivity, decoration.getImgUrl(), productImage);
		TextView tvProductName = holder.getView(R.id.tv_envirte_title);
		if (null != decoration.getTitle() || Util.isEmpty(decoration.getTitle())) {
			tvProductName.setText(decoration.getTitle());
			tvProductName.setVisibility(View.VISIBLE);
		} else {
			tvProductName.setVisibility(View.GONE);
		}
		
		return holder.getConvertView();
	}

}
