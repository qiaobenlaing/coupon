package com.huift.hfq.shop.activity;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Decoration;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.FinishActivityUtils;
import com.huift.hfq.shop.adapter.BigImageAdapter;
import com.huift.hfq.shop.model.DelShopDecImgTask;
import com.lidroid.xutils.ViewUtils;

/**
 * 查看大图
 * @author qian.zhou
 */
public class EnvironMentBigPhotoActivity extends Activity {
	public static final String TAG = EnvironMentBigPhotoActivity.class.getSimpleName();
	/** 产品信息和环境信息的对象*/
	public static final String DECORATION = "decoration";
	/** 编辑图片*/
	private TextView mTvEditPhoto;
	/** 删除图片*/
	private TextView mTvDelPhtoto;
	public static final String IMAGE_LIST = "imageList";
	public static final String IMAG_INDEX = "index";
	/** 滚屏图片的集合 */
	private List<Decoration> mImagesList;
	/** 图片控件的集合*/
	private List<ImageView> mImageViewList;
	/** 当前图片索引*/
	private int mPageNum = 0;
	/** 产品名称*/
	private TextView mTvProductName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showenvironment_photobig);
		FinishActivityUtils.addActivity(EnvironMentBigPhotoActivity.this);
		ViewUtils.inject(this);
		//初始化数据
		init();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}

	private void init() {
		//初始化视图
		mTvProductName = (TextView) findViewById(R.id.tv_name);//产品名称
		mTvEditPhoto = (TextView) findViewById(R.id.tv_edit);//编辑
		mTvDelPhtoto = (TextView) findViewById(R.id.tv_del);//删除
		//取值
		Intent intent = this.getIntent();
		final Decoration decoration = (Decoration) intent.getSerializableExtra(DECORATION);
		mImagesList = (List<Decoration>) intent.getSerializableExtra(IMAGE_LIST);
		Log.d(TAG, "商家环境图片的集合的大小为：：" + mImagesList.size());
		mPageNum = intent.getIntExtra(IMAG_INDEX, 0);
		if (null != decoration ) {
			
			//查看商家的环境大图
			setDecoration(mPageNum, decoration);
		}
		//循环滑动查看商家环境的大图
		initViewPare();
	}
	
	/**
	 * 查看环境大图
	 * @param pageNum 页码
	 * @param decoration 保存图片路径的对象
	 */
	private void setDecoration (int pageNum, final Decoration decoration) {
		TextView tvPicNum = (TextView) findViewById(R.id.tv_num);
		if (decoration != null) {
			mTvProductName.setText(decoration.getTitle());
			if (mImagesList.size() >0) {
				
				tvPicNum.setText((pageNum + 1) + "/" + mImagesList.size());
			}
			//点击编辑
			mTvEditPhoto.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(EnvironMentBigPhotoActivity.this, UploadEnvironmentPhotoActivity.class);
					//one表示是从大图页面跳转到上传页面的
					intent.putExtra(UploadEnvironmentPhotoActivity.FLAG, "two");
					intent.putExtra(UploadEnvironmentPhotoActivity.DECORATION, decoration);
					startActivity(intent);
				}
			});
			//点击删除
			mTvDelPhtoto.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mTvDelPhtoto.setEnabled(false);
					new DelShopDecImgTask(EnvironMentBigPhotoActivity.this, new DelShopDecImgTask.Callback() {
						@Override
						public void getResult(JSONObject result) {
							if(result == null){
								return;
							} else{
								//TODO
							}
						}
					}).execute(decoration.getDecorationCode());
				}
			});
		}
	}
	
	/**
	 * viewPage(滑动显示图片)
	 */
	private void initViewPare() {
		mImageViewList = new ArrayList<ImageView>();
		for (int i = 0; i < mImagesList.size(); i++) {
			final ImageView imageView = new ImageView(this);
			imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			Decoration decoration = mImagesList.get(i);
			Util.showImage(this, decoration.getImgUrl(), imageView);
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
