package cn.suanzi.baomi.shop.adapter;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.Image;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.activity.BigPhotoActivity;

public class ShopProductPhotoAdapter  extends Adapter<ShopProductPhotoAdapter.ViewHolder>{
	private final static String TAG = ShopProductPhotoAdapter.class.getSimpleName();
	private List<Image> decorations;
	private Activity activity;

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private ImageView ivShopLogo;
		public ViewHolder(View view) {
			super(view);
			ivShopLogo = (ImageView) view.findViewById(R.id.iv_show_photo);
		}
	}

	public ShopProductPhotoAdapter(Activity act, List<Image> decorations) {
		this.decorations = decorations;
		this.activity = act;
		Log.d(TAG, "coupons1=" + decorations.size());
	}

	@Override
	public int getItemCount() {
		return decorations.size();
	}

	@Override
	public void onBindViewHolder(final ViewHolder viewholder, final int position) {
		final Image image = decorations.get(position);
		try {
			Util.showImage(activity, image.getImageUrl() , viewholder.ivShopLogo);
		} catch (Exception e) {
			Log.d(TAG, "kkkk>>>" + e.getMessage());
		}
		viewholder.ivShopLogo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, BigPhotoActivity.class);
				Log.d(TAG, "图片的集合的大小为：：" + decorations.size());
				intent.putExtra(BigPhotoActivity.IMAGE_LIST, (Serializable)decorations);
				intent.putExtra(BigPhotoActivity.IMAG_INDEX, (position % decorations.size()));
				intent.putExtra(BigPhotoActivity.IMAGEURL, image.getImageUrl());
				activity.startActivity(intent);
			}
		});
	}

	@Override  
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View v = LayoutInflater.from(arg0.getContext()).inflate(R.layout.item_shopproduct_photo, arg0, false);
		ViewHolder viewHolder = new ViewHolder(v);
		return viewHolder;
	}
}
