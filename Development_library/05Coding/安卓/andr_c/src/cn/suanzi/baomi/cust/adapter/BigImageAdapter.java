package cn.suanzi.baomi.cust.adapter;

import java.util.List;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 查看大图的适配器
 * 
 * @author yanfang.li
 */
public class BigImageAdapter extends PagerAdapter {
	private static final String TAG = BigImageAdapter.class.getSimpleName();
	private List<ImageView> mImageViewList;
	private Activity mActivity;
	private double mRawX = 0;

	public BigImageAdapter(Activity activity, List<ImageView> images) {
		this.mActivity = activity;
		this.mImageViewList = images;
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
	public void destroyItem(View container, int position, Object object) {
		// Log.d(TAG, position % mImageList.size()
		// +">>>position++++"+position+"===view:"+object);
		// container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(View container, final int position) {

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

		// 点击退出
		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// 按下
					mRawX = event.getRawX();
					Log.d(TAG, "getRaw_dowm=" + mRawX);
					break;
				case MotionEvent.ACTION_UP:// 离开
					double distanceUpX = event.getRawX() - mRawX;
					if (distanceUpX == 0) {
						mActivity.finish(); // 退出本页面
					}
					Log.d(TAG, "getRaw_up=" + event.getRawX() + ">>distanceUpX=" + distanceUpX);
					break;
				case MotionEvent.ACTION_CANCEL:// 消息丢失和取消的
					Log.d(TAG, "getRaw_cancel=" + event.getRawX());
					break;
				}
				return true;

			}
		});

		return view;
	}

}
