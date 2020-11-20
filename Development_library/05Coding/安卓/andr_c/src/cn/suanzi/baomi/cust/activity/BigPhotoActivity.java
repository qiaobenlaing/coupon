package cn.suanzi.baomi.cust.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.suanzi.baomi.base.Bimp;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.Image;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.UtilsImageLoader;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.adapter.BigImageAdapter;

import com.lidroid.xutils.ViewUtils;

/**
 * 查看大图
 * @author qian.zhou
 */
public class BigPhotoActivity extends Activity {
	public static final String TAG = "BigPhotoActivity";
	/** 图片路径*/
	public static final String IMAGEURL = "imageUrl";
	/** 图片当前的索引*/
	public static final String IMAG_INDEX = "imageIndex";
	/** 图片路径的集合*/
	public static final String IMAGE_LIST = "imageList";
	public static final int CROP_PIC = 1;
	/** 滚屏图片的集合 */
	private List<Image> mPicList;      
	/** 展示图片的集合*/
	private List<ImageView> mImageViewList;
	/** 当前页数*/
	private int mPageNum = 0;
	private String mIamgeString = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showphoto);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		Util.addActivity(BigPhotoActivity.this);
		ViewUtils.inject(this);
		mPicList = (List<Image>) this.getIntent().getSerializableExtra(IMAGE_LIST);
		mPageNum = this.getIntent().getIntExtra(IMAG_INDEX, 0);
		Log.d(TAG, "mPageNum   >>>>><<<<<  mPageNum");
		initViewPare();
	}

	/**
	 * viewPage
	 */
	private void initViewPare() {
		mImageViewList = new ArrayList<ImageView>();
		// 图片存放的对象
		Image image = null; 
		if (mPicList.size() != 2) {
			for (int i = 0; i < mPicList.size(); i++) {
				final ImageView imageView = new ImageView(this);
				imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			    image = mPicList.get(i);
				Util.showImage(this, image.getImageUrl(), imageView);
				mImageViewList.add(imageView);
			}
		} else {
			for (int i = 0; i < mPicList.size() + 2 ; i++) {
				ImageView imageView = new ImageView(this);
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				if (i % 2 == 0) {
					image = mPicList.get(0);
					mIamgeString = image.getImageUrl();
				} else {
					image = mPicList.get(1);
					mIamgeString = image.getImageUrl();
				} 
				Util.showImage(this, mIamgeString, imageView);
				mImageViewList.add(imageView);
			}
		}
		
		final TextView tvPicNum = (TextView) findViewById(R.id.tv_pic_num);
		final Button tvDownload = (Button) findViewById(R.id.tv_download);
		ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
		BigImageAdapter adapter = new BigImageAdapter(this, mImageViewList);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(mPicList.size()*100 + mPageNum);
		tvPicNum.setText((mPageNum + 1) + "/" + mImageViewList.size());
		tvDownload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//显示图片
				Bitmap bitmap;
				try {
					Log.d(TAG, "图片装换异常 >>> " + mIamgeString);
					bitmap = Bimp.revitionImageSize(mIamgeString);
					UtilsImageLoader.saveFile(bitmap, "12312312");
				} catch (IOException e) {
					Log.d(TAG, "图片装换异常 >>> " + e.getMessage());
				}
				
			}
		});
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if (mPicList.size() == 2) {
					position = position % 2;
				} else if (position > 2) {
					position = position % mImageViewList.size();
				}
				if (mPicList.size() > 1) {
					tvPicNum.setText( (position + 1) + "/" + mPicList.size());
				}
		}
			@Override
			public void onPageScrolled(int i, float f, int t) {}
			@Override
			public void onPageScrollStateChanged(int i) {}
		});
	}

	public void onResume() {
		super.onResume();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}
}
