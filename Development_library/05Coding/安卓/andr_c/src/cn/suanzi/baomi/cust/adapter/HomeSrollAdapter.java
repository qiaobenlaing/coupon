package cn.suanzi.baomi.cust.adapter;

import java.util.List;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import cn.suanzi.baomi.base.pojo.HomeTemplate;
import cn.suanzi.baomi.cust.util.SkipActivityUtil;

import com.umeng.analytics.MobclickAgent;

/**
 * 首页滚屏图片
 * @author yanfang.li
 */
public class HomeSrollAdapter extends PagerAdapter {
	private static final String TAG = HomeSrollAdapter.class.getSimpleName();
	/** 图片的集合*/
	private List<ImageView> mImageViewList;
	/** activity*/
	private Activity mActivity;
	/** 滚屏的活动结合*/
	private List<HomeTemplate> homeTemplates;
	/** 处理滚屏的线程*/
	private Handler mHandler; 
	/** 滚屏的线程*/
	private Runnable mRunnable;
	/** 滑动的位置*/
	private double mRawX = 0;
	/** 首页模板号*/
	private String mModuleValue;
	
	public HomeSrollAdapter(Activity activity, List<ImageView> images,List<HomeTemplate> homeTemplates, Handler handler, Runnable runnable,String moduleValue) {
		this.mActivity = activity;
		this.mImageViewList = images;
		this.homeTemplates = homeTemplates;
		this.mHandler = handler;
		this.mRunnable = runnable;
		this.mModuleValue = moduleValue;
	}
	
	@Override
	public int getCount() {
		return mImageViewList.size() > 1 ? Integer.MAX_VALUE : mImageViewList.size();
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
//		Log.d(TAG, position % mImageList.size() +">>>position++++"+position+"===view:"+object);
//		container.removeView((View) object);
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
			Log.e(TAG, "滚屏图片>>"+e.getMessage());
		}
		
		// 触摸事件
		view.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN://按下
					mRawX = event.getRawX();
					Log.d(TAG, "getRaw_dowm="+mRawX);
					Log.e("TAG", "移除消息");
					mHandler.removeCallbacksAndMessages(null);
					break;
				case MotionEvent.ACTION_UP://离开
					//double rawUpX = Calculate.suBtraction(rawX, event.getRawX());
					double distanceUpX = event.getRawX() - mRawX;
					if (distanceUpX == 0) {
						getSkipActivity(position);
					} else {
					}
					Log.d(TAG, "getRaw_up="+event.getRawX() + ">>distanceUpX="+distanceUpX);
					Log.e("TAG", "发消息");
					mHandler.postDelayed(mRunnable, 3000);
					break;
				case MotionEvent.ACTION_CANCEL://消息丢失和取消的
					Log.d(TAG, "getRaw_cancel="+event.getRawX());
					Log.e("TAG", "ACTION_CANCEL发消息");
					mHandler.postDelayed(mRunnable, 3000);
					break;
				}
				return true;
			
			}
		});
		
		return view;
	}
	
	/**
	 * 跳转到活动详情界面
	 * @param position
	 */
	private void getSkipActivity (int position) {
		if (null != homeTemplates && homeTemplates.size() > 0) {
			HomeTemplate homeTemplate = homeTemplates.get(position % mImageViewList.size());
			SkipActivityUtil.skipHomeActivity(mActivity, homeTemplate,mModuleValue); // 跳转界面
			//友盟统计
			MobclickAgent.onEvent(mActivity, "home_activity_image");
		
		}
	}

}
