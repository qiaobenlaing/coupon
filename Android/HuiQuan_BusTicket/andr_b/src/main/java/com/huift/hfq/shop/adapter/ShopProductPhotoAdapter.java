package com.huift.hfq.shop.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Image;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.activity.BigPhotoActivity;

import java.io.Serializable;
import java.util.List;

public class ShopProductPhotoAdapter  extends Adapter<ShopProductPhotoAdapter.ViewHolder>{
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
			e.printStackTrace();
		}
		viewholder.ivShopLogo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, BigPhotoActivity.class);
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
