package com.huift.hfq.shop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.Decoration;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.FinishActivityUtils;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.adapter.BigImageAdapter;
import com.huift.hfq.shop.model.DelSubAlbumPhotoTask;
import com.lidroid.xutils.ViewUtils;

import net.minidev.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 查看大图
 * @author qian.zhou
 */
public class ProductBigPhotoActivity extends Activity {
	/** 产品信息和环境信息的对象*/
	public static final String DECORATION = "decoration";
	/** setResult()用的，如果确实删除了信息 */
	public final static int DEL_RESP_PHOTO = 1;
	/** setResult()用的，如果确实修改了信息 */
	public final static int UPP_PRODUCT_PHOTO = 2;
	/** 编辑图片*/
	private TextView mTvEditPhoto;
	/** 删除图片*/
	private TextView mTvDelPhtoto;
	/** 图片路径的集合*/
	public static final String IMAGE_LIST = "imageList";
	/** 当前展示图片的索引*/
	public static final String IMAG_INDEX = "index";
	/** 滚屏图片的集合 */
	private List<Decoration> mImagesList;
	private List<ImageView> mImageViewList;
	/** 图片页数*/
	private int mPageNum = 0;
	/** 产品名称*/
	private TextView mTvProductName;
	/** 产品价格*/
	private TextView mTvPrice;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showphotobig);
		FinishActivityUtils.addActivity(ProductBigPhotoActivity.this);
		ViewUtils.inject(this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		init();
	}

	private void init() {
		//初始化视图
		mTvProductName = (TextView) findViewById(R.id.tv_name);//产品名称
		mTvPrice = (TextView) findViewById(R.id.tv_product_price);//产品价格
		mTvEditPhoto = (TextView) findViewById(R.id.tv_edit);//编辑
		mTvDelPhtoto = (TextView) findViewById(R.id.tv_del);//删除
		//取值
		Intent intent = this.getIntent();
		Decoration decoration = (Decoration) intent.getSerializableExtra(DECORATION);
		mImagesList = (List<Decoration>) intent.getSerializableExtra(IMAGE_LIST);
		mPageNum = intent.getIntExtra(IMAG_INDEX, 0);
		if (null != decoration) {
			
			//查看产品大图
			setDecoration(mPageNum, decoration);
		}
		//循环查看产品大图
		initViewPare();
	}
	
	/**
	 * 查看产品大图
	 * @param pageNum 页码
	 * @param decoration 保存图片路径的对象
	 */
	private void setDecoration (int pageNum,final Decoration decoration) {
        TextView tvPicNum = (TextView) findViewById(R.id.tv_num);
		//赋值
		if (decoration != null) {
			mTvProductName.setText(decoration.getTitle());
			mTvPrice.setText("￥" + decoration.getPrice() + ".00");
			if (mImagesList.size() > 0) {
				
				tvPicNum.setText((pageNum + 1) + "/" + mImagesList.size());
			}
			//点击编辑
			mTvEditPhoto.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(ProductBigPhotoActivity.this, UploadPhotoActivity.class);
					//one表示是从大图页面跳转到上传页面的
					intent.putExtra(UploadPhotoActivity.FLAG, "one");
					intent.putExtra(UploadPhotoActivity.DECORATION, decoration);
					startActivity(intent);
				}
			});
			
			//删除
			mTvDelPhtoto.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mTvDelPhtoto.setEnabled(false);
					new DelSubAlbumPhotoTask(ProductBigPhotoActivity.this, new DelSubAlbumPhotoTask.Callback() {
						@Override
						public void getResult(JSONObject object) {
							mTvDelPhtoto.setEnabled(true);
							if (object == null) {
								return;
							} else {
								if(String.valueOf(ErrorCode.SUCC).equals(object.get("code").toString())){
									DB.saveBoolean(ShopConst.Key.UPP_DECORATION, true);
									DB.saveBoolean(ShopConst.Key.UPP_ALBUM_PHOTO, true);
								}
							}
						}
					}).execute(decoration.getCode());
				}
			});
		}	
	}
	
	/**
	 * viewPage
	 */
	private void initViewPare() {
		mImageViewList = new ArrayList<ImageView>();
		for (int i = 0; i < mImagesList.size(); i++) {
			final ImageView imageView = new ImageView(this);
			imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			Decoration decoration = mImagesList.get(i);
			Util.showImage(this, decoration.getUrl(), imageView);
			mImageViewList.add(imageView);
		}
		ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
		BigImageAdapter adapter = new BigImageAdapter(this, mImageViewList);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(mImageViewList.size()*100 + mPageNum);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if (position > 2) {
					position = position % mImageViewList.size();
			}
				if(mImageViewList.size() > 1){
					Decoration decoration = mImagesList.get(position);
					setDecoration(position, decoration);
				}
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			@Override
			public void onPageScrollStateChanged(int arg0) {}
		});
	}
	
	public void onResume(){
    	super.onResume();
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
    }
}
