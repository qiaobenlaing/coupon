package cn.suanzi.baomi.shop.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.Image;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.adapter.BigImageAdapter;

import com.lidroid.xutils.ViewUtils;

/**
 * 查看大图
 * @author qian.zhou
 */
public class BigPhotoActivity extends Activity{
	public static final String TAG = "BigPhotoActivity";
	/** 图片路径*/
	public static final String IMAGEURL = "imageUrl";
	/** 当前图片对应的索引*/
	public static final String IMAG_INDEX = "index";
	public static final  int CROP_PIC = 1;
	/** 图片路径集合*/
	public static final String IMAGE_LIST = "imageList";
    /** 滚屏图片的集合 */
	private List<Image> mImagesList;
	/** 图片的集合*/
	private List<ImageView> mImageViewList;
	/** 当前页*/
	private int mPageNum = 0;
	/** 表示是网络图片还是本地图片   1、本地图片    2、网络图片*/
    public static final String IMAGE_GLAG = "imageFlag";
    private String mType;
    
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showphoto);
		Util.addActivity(BigPhotoActivity.this);
		ViewUtils.inject(this);
		//初始化数据
		init();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		init();
	}
	
	/**
	 * 初始化数据
	 */
	private void init() {
		mImagesList = (List<Image>) this.getIntent().getSerializableExtra(IMAGE_LIST);
		mPageNum = this.getIntent().getIntExtra(IMAG_INDEX, 0);
		mType = this.getIntent().getStringExtra(IMAGE_GLAG);
		initViewPare();
	}
	
	/**
	 * viewPage（大图滑动）
	 */
	private void initViewPare() {
		mImageViewList = new ArrayList<ImageView>();
		if (mImagesList != null) {
			for (int i = 0; i < mImagesList.size(); i++) {
				final ImageView imageView = new ImageView(this);
				imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
				Image image = mImagesList.get(i);
				if (Util.isEmpty(image.getImageUrl())) {
					imageView.setBackgroundResource(R.drawable.no_shopdetail_url);
				} else {
					if ("1".equals(mType)) {
						Util.showShopDetailImages(this, image.getImageUrl(), imageView);
					} else {
						Util.showImage(this, image.getImageUrl(), imageView);
					}
				}
				mImageViewList.add(imageView);
			}
			
			final TextView tvPicNum = (TextView) findViewById(R.id.tv_pic_num);
			ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
			viewPager.setBackgroundResource(0);
			BigImageAdapter adapter = new BigImageAdapter(this, mImageViewList);
			viewPager.setAdapter(adapter);
			
			viewPager.setCurrentItem(mImageViewList.size()*100 + mPageNum);
			tvPicNum.setText((mPageNum + 1) + "/" + mImageViewList.size());
			viewPager.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					if (position > 2) {
						position = position % mImageViewList.size();
					}
					if (mImageViewList.size() > 1) {
						tvPicNum.setText( (position + 1) + "/" + mImageViewList.size());
					}
				}
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {}
				@Override
				public void onPageScrollStateChanged(int arg0) {}
			});
		} else {
			//TODO
		}
	}
	
   public void onResume(){
    	super.onResume();
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
   }
}
