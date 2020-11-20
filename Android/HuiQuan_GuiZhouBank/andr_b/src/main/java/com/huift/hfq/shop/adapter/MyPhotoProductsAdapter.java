package com.huift.hfq.shop.adapter;

import java.util.List;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.ShopDecoration;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.ProductPhotoActivity;
import com.huift.hfq.shop.model.DelSubAlbumTask;

import net.minidev.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.huift.hfq.shop.R;

/**
 * 商家相册(产品)
 * @author qian.zhou
 */
public class MyPhotoProductsAdapter extends CommonListViewAdapter<ShopDecoration>{
	
	public static final String TAG = MyPhotoProductsAdapter.class.getSimpleName();
	public static final String ADD_PHOTO = "home";
	/**获得一个用户信息对象**/
	private UserToken mUserToken;
	/**用户登录后获得的令牌 **/
	private String mTokenCode;
	
	public MyPhotoProductsAdapter(Activity activity, List<ShopDecoration> datas) {
		super(activity, datas);
	}
	
	@Override
	public void setItems(List<ShopDecoration> shopDecorations) {
		super.setItems(shopDecorations);
	}
	@Override
	public void addItems(List<ShopDecoration> shopDecorations) {
		super.addItems(shopDecorations);
	}
	
	@SuppressLint("ViewHolder")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CommenViewHolder holder = CommenViewHolder.get(mActivity , convertView , parent, R.layout.item_photo_products, position);
		ShopDecoration shopDecoration = mDatas.get(position);
		// 获得一个用户信息对象
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mTokenCode = mUserToken.getTokenCode();//令牌
		
		final FrameLayout fyProductPhoto = holder.getView(R.id.fy_add_type);
		ImageView ivPhotoTitle = holder.getView(R.id.iv_photo_title);
		TextView tvPhotoTypeName = holder.getView(R.id.tv_productype_name);
		TextView tvPhotoCount = holder.getView(R.id.tv_photo_count);//一共有多少张
		
		//赋值
		tvPhotoTypeName.setText(shopDecoration.getName());
		tvPhotoCount.setText(shopDecoration.getPhotoCount());
		Util.showImage(mActivity, shopDecoration.getUrl(), ivPhotoTitle);
		
		//点击listview的每一行进入相册
		fyProductPhoto.setTag(position);
		fyProductPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final int index = (Integer) v.getTag(); 
				final String code = mDatas.get(index).getCode();
				final String name = mDatas.get(index).getName();
				Intent intent = new Intent(mActivity, ProductPhotoActivity.class);
				intent.putExtra(ProductPhotoActivity.CODE, code);
				intent.putExtra(ProductPhotoActivity.NAME, name);
				mActivity.startActivity(intent);
			}
		});
		
		
		//item的长按事件
		fyProductPhoto.setTag(position);
		fyProductPhoto.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				final int index = (Integer) v.getTag(); 
				final String code = mDatas.get(index).getCode();
				DialogUtils.showDialog(mActivity, mActivity.getString(R.string.cue),mActivity.getString(R.string.quit_delete), mActivity.getString(R.string.ok), mActivity.getString(R.string.no), new DialogUtils().new OnResultListener() {
					@Override
					public void onOK() {
						new DelSubAlbumTask(mActivity, new DelSubAlbumTask.Callback() {
		        			@Override
		        			public void getResult(JSONObject object) {
		        				if(object == null){
		        					return;
		        				} else{
		        					if(String.valueOf(ErrorCode.SUCC).equals(object.get("code").toString())){
		        						Util.showToastZH("删除成功");
		        						DB.saveBoolean(ShopConst.Key.UPP_ALBUM_PHOTO, true);
		        						mDatas.remove(position);
		        						notifyDataSetChanged();
		        					} else{
		        						Util.showToastZH("删除失败");
		        					}
		        				}
		        			}
		        		}).execute(code, mTokenCode);
					}
				});
				return false;
			}
		});
		return holder.getConvertView();
	}
}
