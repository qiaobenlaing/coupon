package cn.suanzi.baomi.cust.adapter;

import java.util.List;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.Decoration;
import cn.suanzi.baomi.cust.R;

/**
 * 商店产品图片
 * @author yanfang.li
 */
public class ShopProductAdapter extends Adapter<ShopProductAdapter.ViewHolder> {
	private final static String TAG = ShopProductAdapter.class.getSimpleName();
	private List<Decoration> mDecorations;
	private OnItemClickListener mOnItemClickLitener;
	private Activity mActivity;

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private ImageView ivProductImg;
		public ViewHolder(View arg0) {
			super(arg0);
			ivProductImg = (ImageView) arg0.findViewById(R.id.iv_product_img);
		}
	}

	public ShopProductAdapter(List<Decoration> mDecorations, Activity activity) {
		this.mDecorations = mDecorations;
		this.mActivity = activity;
	}

	@Override
	public int getItemCount() {
		return mDecorations.size();
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		final Decoration decoration = mDecorations.get(position);
		holder.itemView.setTag(decoration);
		Util.showImage(mActivity, decoration.getUrl(), holder.ivProductImg);
		// 如果设置了回调，则设置点击事件
		if (mOnItemClickLitener != null) {
			holder.itemView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mOnItemClickLitener.onItemClick(null, holder.itemView, position, holder.getItemId());
				}
			});

		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View v = LayoutInflater.from(arg0.getContext()).inflate(R.layout.item_shop_product, arg0, false);
		ViewHolder viewHolder = new ViewHolder(v);
//		viewHolder.setIsRecyclable(false);
		return viewHolder;
	}

	public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
		this.mOnItemClickLitener = mOnItemClickLitener;
	}
}
