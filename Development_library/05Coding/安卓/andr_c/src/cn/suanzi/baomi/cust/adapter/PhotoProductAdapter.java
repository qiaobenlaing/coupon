package cn.suanzi.baomi.cust.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import cn.suanzi.baomi.base.adapter.CommenViewHolder;
import cn.suanzi.baomi.base.adapter.CommonListViewAdapter;
import cn.suanzi.baomi.base.pojo.Decoration;
import cn.suanzi.baomi.base.pojo.ShopDecoration;
import cn.suanzi.baomi.base.view.MyGridView;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.activity.ShopBigPhotoActivity;

/**
 * 商家产品相册
 * @author ad
 */
public class PhotoProductAdapter extends CommonListViewAdapter<ShopDecoration> {
	
	private final static String TAG = PhotoProductAdapter.class.getSimpleName();
	
	private List<ShopDecoration> mDatas;
	public PhotoProductAdapter(Activity activity, List<ShopDecoration> datas) {
		super(activity, datas);
		mDatas = datas;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity, convertView, parent,R.layout.item_photo_production, position);
		final ShopDecoration decoration = mDatas.get(position);
		final MyGridView gvProduct = holder.getView(R.id.gv_product);
		TextView tvProductName = holder.getView(R.id.tv_productname);
		TextView tvVline = holder.getView(R.id.tv_vline);
		tvProductName.setText(decoration.getName());
		if (null != decoration.getPhotoList() && decoration.getPhotoList().size() > 0 ) {
			PhotoProductImageAdapter imageAdapter = new PhotoProductImageAdapter(mActivity, decoration.getPhotoList());
			gvProduct.setAdapter(imageAdapter);
			gvProduct.setVisibility(View.VISIBLE);
			tvVline.setVisibility(View.VISIBLE);
			gvProduct.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
					Decoration decoration = (Decoration) gvProduct.getItemAtPosition(position);
					Intent intent = new Intent(mActivity, ShopBigPhotoActivity.class);
					intent.putExtra(ShopBigPhotoActivity.DECORATION, decoration);
					intent.putExtra(ShopBigPhotoActivity.TYPE, "1"); // 1 代表是产品图片
					mActivity.startActivity(intent);
				}
			});
		} else {
			gvProduct.setVisibility(View.GONE);
			tvVline.setVisibility(View.GONE);
		}
		
		return holder.getConvertView();
	}

}
