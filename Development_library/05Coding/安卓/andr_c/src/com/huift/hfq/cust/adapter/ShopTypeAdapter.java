package com.huift.hfq.cust.adapter;

import java.util.List;

import com.huift.hfq.cust.util.SkipActivityUtil;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.HomeTemplate;
import com.huift.hfq.base.utils.SizeUtil;
import com.huift.hfq.cust.R;

public class ShopTypeAdapter extends RecyclerView.Adapter<ShopTypeAdapter.MyViewHolder> {

	private static final String TAG = ShopTypeAdapter.class.getSimpleName();

	/** 对应的activity */
	private Activity mActivity;
	/** 数组集合 */
	private List<HomeTemplate> mDatas;
	/** 模板好 */
	private String mModuleValue;
	/** 显示多少行数 */
	private int mLine;
	/** 装类别的空间 */
	private RecyclerView mRvShopType;

	public ShopTypeAdapter() {
		super();
	}

	public ShopTypeAdapter(Activity activity, List<HomeTemplate> datas, String moduleValue, RecyclerView rvShopType,
			int line) {
		super();
		this.mActivity = activity;
		this.mDatas = datas;
		this.mModuleValue = moduleValue;
		this.mRvShopType = rvShopType;
		this.mLine = line;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_shoptype, parent,false));
		return holder;
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, int position) {
		final HomeTemplate template = mDatas.get(position);
		holder.itemView.setTag(template);
		//显示图片
		Util.showFirstImages(mActivity, template.getImgUrl(), holder.ivShopType);
		
		// 标题
		if (!Util.isEmpty(template.getTitle())) {
			holder.tvShopType.setText(template.getTitle());
			if (!Util.isEmpty(template.getTitleColor())) {
				holder.tvShopType.setTextColor(Color.parseColor(template.getTitleColor()));
			}
		}

		holder.itemView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SkipActivityUtil.skipHomeActivity(mActivity, template, mModuleValue);
			}
		});

		// 设置图片的宽
//		RelativeLayout.LayoutParams ryparams = (RelativeLayout.LayoutParams) mRvShopType.getLayoutParams();
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.ryShopType.getLayoutParams();
//		ryparams.width = Util.getWindowWidthAndHeight(mActivity)[0];
		if (mDatas.size() >= 5 && mDatas.size() < 8) {
			params.width = Util.getWindowWidthAndHeight(mActivity)[0] * 1 / 5;
//			ryparams.height = SizeUtil.dip2px(80);
		} else {
			params.width = Util.getWindowWidthAndHeight(mActivity)[0] * 1 / 4;
//			ryparams.height = SizeUtil.dip2px(160);
		}
		
		holder.ryShopType.setLayoutParams(params);
//		mRvShopType.setLayoutParams(ryparams);
		
	}

	@Override
	public int getItemCount() {
		return mDatas.size();
	}

	class MyViewHolder extends ViewHolder {
		// 图片
		ImageView ivShopType;
		// 文字
		TextView tvShopType;
		// 文类
		RelativeLayout ryShopType;

		public MyViewHolder(final View view) {
			super(view);
			ivShopType = (ImageView) view.findViewById(R.id.iv_shoptype);
			tvShopType = (TextView) view.findViewById(R.id.tv_shoptype);
			ryShopType = (RelativeLayout) view.findViewById(R.id.ly_home_shoptype);
		}
	}

}
