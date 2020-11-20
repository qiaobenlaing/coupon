package com.huift.hfq.cust.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Decoration;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.adapter.BigImageAdapter;
import com.lidroid.xutils.ViewUtils;

/**
 * 查看大图
 * @author qian.zhou
 */
public class ShopBigPhotoActivity extends Activity {
	public static final String TAG = ShopBigPhotoActivity.class.getSimpleName();
	/** 装饰对象*/
	public static final String DECORATION = "decoration";
	/** 装饰集合*/
	public static final String DECORATION_LIST = "decorationList";
	/** 当前图片的位置*/
	public static final String POSITION = "position";
	/** 环境类型*/
	public static final String TYPE = "type";
	public static final  int CROP_PIC = 1;
	/** 环境里面的图片*/
	public static final  String IMAGETYPE = "0";
	/** 传过来的图片集合*/
	private List<Decoration> mPicList;
	/** 显示图片集合*/
	private List<ImageView> mImageViewList;
	/** 页数*/
	private int mPage = 0;
	/** 图片对象*/
	private Decoration mDecoration ;
	/** 页数*/
	private TextView mTvPageCount;
	/** 那种类型*/
	private String mType; 
	/** 图片名称*/
	private TextView mTvName; 
	/** 图片价格*/
	private TextView mTvPrice; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showphotobig);
		Util.addActivity(ShopBigPhotoActivity.this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		ViewUtils.inject(this);
		Log.d(TAG, "activity_showphotobig UUUUUUUUUUUUUUUU");
		init();
	}
	
	private void init() {
		mTvPageCount = (TextView) findViewById(R.id.tv_count);
		mPicList = (List<Decoration>) this.getIntent().getSerializableExtra(DECORATION_LIST);
		mPage = this.getIntent().getIntExtra(POSITION, 0);
		mDecoration = (Decoration) this.getIntent().getSerializableExtra(DECORATION);
		mType = this.getIntent().getStringExtra(TYPE);
		mTvName = (TextView) findViewById(R.id.tv_name);
		mTvPrice = (TextView) findViewById(R.id.tv_price);
		setInitIamge(mPage,mDecoration); // 初始化图片的信息
		initViewPare(); // viewPage初始化
	}
	
	/**
	 * 初始化图片
	 */
	private void setInitIamge (int page,Decoration decoration) {
		if (mType.equals(IMAGETYPE)) { // 环境里面的图片
			mTvPrice.setVisibility(View.GONE);
		} else {
			mTvPrice.setVisibility(View.VISIBLE);
			mTvPrice.setText(getString(R.string.money_symbol) + decoration.getPrice());
		}
		// 初始化标题
		if (Util.isEmpty(decoration.getTitle())) {
			mTvName.setVisibility(View.GONE);
		} else {
			mTvName.setVisibility(View.VISIBLE);
			mTvName.setText(decoration.getTitle());
		}
		// 初始化页数
		mTvPageCount.setText((page + 1) + "/" + mPicList.size());
	}
	
	/**
	 * 初始化viewpage
	 */
	private void initViewPare () {
		mImageViewList = new ArrayList<ImageView>();
		Decoration decoration = null;
		if (mPicList.size() != 2) {
			for (int i = 0; i < mPicList.size(); i++) {
				final ImageView imageView = new ImageView(this);
				imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
				decoration = mPicList.get(i);
				String imageUrl = "";
				if (mType.equals(IMAGETYPE)) { // 环境里面的图片
					imageUrl = decoration.getImgUrl();
				} else {
					imageUrl = decoration.getUrl();
				}
				Util.showImage(this, imageUrl, imageView);
				mImageViewList.add(imageView);
			}
		} else {
			Log.d(TAG, "activity_showphotobig  mPicList");
			for (int i = 0; i < mPicList.size() + 2; i++) {
				final ImageView imageView = new ImageView(this);
				imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
				if (i % 2 == 0) {
					decoration = mPicList.get(0);
				} else {
					decoration = mPicList.get(1);
				} 
				
				String imageUrl = "";
				if (mType.equals(IMAGETYPE)) { // 环境里面的图片
					imageUrl = decoration.getImgUrl();
				} else {
					imageUrl = decoration.getUrl();
				}
				Util.showImage(this, imageUrl, imageView);
				mImageViewList.add(imageView);
			}
		}
		
		ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
		BigImageAdapter adapter = new BigImageAdapter(this, mImageViewList);
		viewPager.setAdapter(adapter);
		if (mImageViewList.size() > 1) {
			viewPager.setCurrentItem(mImageViewList.size()*100 + mPage);
		} 
		Log.d(TAG, "mImageViewList.size()*100 + mPage>>"+ (mImageViewList.size()*100 + mPage));
		//viewPage的滚动事件
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if (mPicList.size() == 2) {
					position = position % 2;
				} else if (position > 2) {
					position = position % mImageViewList.size();
				}
				if (mPicList.size() > 1) {
					Decoration decoration = mPicList.get(position);
					setInitIamge(position,decoration);
				}
			}
			@Override
			public void onPageScrolled(int i, float f, int j) {}
			@Override
			public void onPageScrollStateChanged(int i) {}
		});
	}
	
	 public void onResume(){
	    	super.onResume();
	        AppUtils.setActivity(this);
	        AppUtils.setContext(getApplicationContext());
	    }
}
