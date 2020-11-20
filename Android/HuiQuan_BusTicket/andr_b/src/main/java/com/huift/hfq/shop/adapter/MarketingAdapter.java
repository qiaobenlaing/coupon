package com.huift.hfq.shop.adapter;

import java.io.Serializable;
import java.util.List;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Image;
import com.huift.hfq.shop.activity.BigPhotoActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 店铺形象
 * @author qian.zhou
 */
public class MarketingAdapter extends PagerAdapter {
	private static final String TAG = MarketingAdapter.class.getSimpleName();
	/** startActivityForResult()的requestCode: 修改装修的图片 */
	public static final int INTENT_REQ_UPP_INFO = Util.NUM_ONE;
	private List<ImageView> mImageViewList;
	private List<Image> mImageList;
	private String[] mShoppics;
	private Activity mActivity;
	private Handler mHandler;
	private Runnable mRunnable;
	private double mRawX = 0;
	private static final String NUM_ONE = "1";

	public MarketingAdapter(Activity activity, List<ImageView> imagesViewList,List<Image> imagesList,String[] shopPics, Handler handler,
			Runnable runnable) {
		this.mActivity = activity;
		this.mImageViewList = imagesViewList;
		this.mImageList = imagesList;
		this.mShoppics = shopPics;
		this.mHandler = handler;
		this.mRunnable = runnable;
	}

	@Override
	public int getCount() {
		if (mImageViewList.size() > 1) {
			return Integer.MAX_VALUE;
		}
		return mImageViewList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		View view = null;
		try {
			view = mImageViewList.get(position % mImageViewList.size());
			ViewGroup group = (ViewGroup) view.getParent();
			if (group != null) {
				group.removeView(view);
			}
			((ViewPager) container).addView(mImageViewList.get(position % mImageViewList.size()));
		} catch (Exception e) {
			Log.e(TAG, "滚屏图片>>" + e.getMessage());
		}
		// 触摸事件
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// 按下
					mRawX = event.getRawX();
					Log.d(TAG, "getRaw_dowm=" + mRawX);
					Log.d("TAG", "移除消息");
					mHandler.removeCallbacksAndMessages(null);
					break;
				case MotionEvent.ACTION_UP:// 离开     
					double distanceUpX = event.getRawX() - mRawX;
					if (distanceUpX == 0) {
						getBigPhoto(position);
					} else {
					}
					Log.d(TAG, "getRaw_up=" + event.getRawX() + ">>distanceUpX=" + distanceUpX);
					Log.e("TAG", "发消息");
					mHandler.postDelayed(mRunnable, 3000);
					break;
				case MotionEvent.ACTION_CANCEL:// 消息丢失和取消的
					Log.d(TAG, "getRaw_cancel=" + event.getRawX());
					Log.d("TAG", "ACTION_CANCEL发消息");
					mHandler.postDelayed(mRunnable, 3000);
					break;
				}
				return true;
			}
		});
		return view;
	}
	
	/**
	 * 查看大图
	 * @param position 位置
	 */
	private void getBigPhoto(int position) {
		if (mShoppics != null) {
			if (null == mImageViewList || mImageViewList.size() == 0) {
				// TODO 没有活动
			} else {
				Intent intent = new Intent(mActivity, BigPhotoActivity.class);
				intent.putExtra(BigPhotoActivity.IMAGE_LIST, (Serializable)mImageList);
				intent.putExtra(BigPhotoActivity.IMAGEURL, mShoppics[position % mImageViewList.size()]);
				intent.putExtra(BigPhotoActivity.IMAGE_GLAG, NUM_ONE);
				mActivity.startActivity(intent);
			}
		}
	}
}
