package cn.suanzi.baomi.cust.adapter;

import java.util.List;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.HomeTemplate;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.util.SkipActivityUtil;

public class ShopCircleAdapter extends RecyclerView.Adapter<ShopCircleAdapter.MyViewHolder> {

	private static final String TAG = ShopCircleAdapter.class.getSimpleName();

	private Activity mActivity;
	private List<HomeTemplate> mDatas;
	private String mModuleValue;
	private RecyclerView mRvShopCircle;

	public ShopCircleAdapter() {
		super();
	}

	public ShopCircleAdapter(Activity activity, List<HomeTemplate> datas ,String moduleValue, RecyclerView rvShopCircle) {
		super();
		this.mActivity = activity;
		this.mDatas = datas;
		this.mModuleValue = moduleValue;
		this.mRvShopCircle = rvShopCircle;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_shopcircle, parent,false));
		return holder;
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position) {
		final HomeTemplate template = mDatas.get(position);
		holder.itemView.setTag(template);
		Util.showBannnerImage(mActivity, template.getImgUrl(), holder.ivShopCircle);
		// 如果设置了回调，则设置点击事件
		holder.itemView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SkipActivityUtil.skipHomeActivity(mActivity, template, mModuleValue);
			}
		});
		// 设置图片的宽高
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.ivShopCircle.getLayoutParams();
		params.width = (int) (Util.getWindowWidthAndHeight(mActivity)[0] * template.getScreenRate());
		params.height = (int) (params.width / template.getImgRate());
		holder.ivShopCircle.setLayoutParams(params);
	}

	@Override
	public int getItemCount() {
		return mDatas.size();
	}

	class MyViewHolder extends ViewHolder {

		ImageView ivShopCircle;

		public MyViewHolder(View view) {
			super(view);
			ivShopCircle = (ImageView) view.findViewById(R.id.iv_shopcircle);
		}
	}

}
