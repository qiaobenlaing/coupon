package cn.suanzi.baomi.cust.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.FinishActivityUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.utils.Bimp;
import cn.suanzi.baomi.cust.utils.PublicWay;
import cn.suanzi.baomi.cust.utils.Res;
import cn.suanzi.baomi.cust.zoom.PhotoView;
import cn.suanzi.baomi.cust.zoom.ViewPagerFixed;

/**
 * 图片浏览时一张的界面
 * @author wensi.yu
 * 
 */
public class GalleryActivity extends Activity {

	private Intent mIntent;
	/** 返回按钮 */
	private ImageView mBack_bt;
	/** 发送按钮 */
	private TextView mSend_bt;
	/** 删除按钮 */
	private Button mDel_bt;
	/** 顶部显示预览图片位置的textview */
	private TextView mPositionTextView;
	/** 获取前一个activity传过来的position */
	private int mPosition;
	/** 当前的位置 */
	private int mLocation = 0;

	private ArrayList<View> mListViews = null;
	private ViewPagerFixed mPager;
	private MyPageAdapter mAdapter;

	public List<Bitmap> mBitmap = new ArrayList<Bitmap>();
	public List<String> mDrr = new ArrayList<String>();
	public List<String> mDetel = new ArrayList<String>();

	private Context mContext;

	RelativeLayout mPhoto_relativeLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plugin_camera_gallery);// 切屏到主界面
		PublicWay.activityList.add(this);
		mContext = this;
		mBack_bt = (ImageView) findViewById(Res.getWidgetID("gallery_back"));
		mSend_bt = (TextView) findViewById(Res.getWidgetID("send_button"));
		mDel_bt = (Button) findViewById(Res.getWidgetID("gallery_del"));
		mBack_bt.setOnClickListener(new BackListener());
		mSend_bt.setOnClickListener(new GallerySendListener());
		mDel_bt.setOnClickListener(new DelListener());
		mIntent = getIntent();
		Bundle bundle = mIntent.getExtras();
		mPosition = Integer.parseInt(mIntent.getStringExtra("position"));
		isShowOkBt();
		// 为发送按钮设置文字
		mPager = (ViewPagerFixed) findViewById(Res.getWidgetID("gallery01"));
		mPager.setOnPageChangeListener(pageChangeListener);
		for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
			initListViews(Bimp.tempSelectBitmap.get(i).getBitmap());
		}

		mAdapter = new MyPageAdapter(mListViews);
		mPager.setAdapter(mAdapter);
		// pager.setPageMargin(getResources().getDimensionPixelOffset(Res.getDimenID("ui_10_dip")));
		int id = mIntent.getIntExtra("ID", 0);
		mPager.setCurrentItem(id);
		FinishActivityUtils.addActivity(GalleryActivity.this);
		Util.addLoginActivity(GalleryActivity.this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {
			mLocation = arg0;
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageScrollStateChanged(int arg0) {

		}
	};

	private void initListViews(Bitmap bm) {
		if (mListViews == null)
			mListViews = new ArrayList<View>();
		PhotoView img = new PhotoView(this);
		img.setBackgroundColor(0xff000000);
		img.setImageBitmap(bm);
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mListViews.add(img);
	}

	/**
	 * 返回按钮添加的监听器
	 * 
	 * @author ad
	 * 
	 */
	private class BackListener implements OnClickListener {

		public void onClick(View v) {
			mIntent.setClass(GalleryActivity.this, ImageFile.class);
			startActivity(mIntent);
		}
	}

	/**
	 * 删除按钮添加的监听器
	 * 
	 * @author ad
	 * 
	 */
	private class DelListener implements OnClickListener {

		public void onClick(View v) {
			if (mListViews.size() == 1) {
				Bimp.tempSelectBitmap.clear();
				Bimp.max = 0;
				mSend_bt.setText(Res.getString("finish") + "(" + Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
				Intent intent = new Intent("data.broadcast.action");
				sendBroadcast(intent);
				finish();
			} else {
				Bimp.tempSelectBitmap.remove(mLocation);
				Bimp.max--;
				mPager.removeAllViews();
				mListViews.remove(mLocation);
				mAdapter.setListViews(mListViews);
				mSend_bt.setText(Res.getString("finish") + "(" + Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	/**
	 * 完成按钮的监听
	 * 
	 * @author ad
	 * 
	 */
	private class GallerySendListener implements OnClickListener {
		public void onClick(View v) {
			finish();
			mIntent.setClass(mContext, ImageUploadActivity.class);
			startActivity(mIntent);
			FinishActivityUtils.exit();
		}

	}

	public void isShowOkBt() {
		if (Bimp.tempSelectBitmap.size() > 0) {
			mSend_bt.setText(Res.getString("finish") + "(" + Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
			mSend_bt.setPressed(true);
			mSend_bt.setClickable(true);
			mSend_bt.setTextColor(Color.RED);
		} else {
			mSend_bt.setPressed(false);
			mSend_bt.setClickable(false);
			mSend_bt.setTextColor(Color.parseColor("#5D5D5D"));
		}
	}

	/**
	 * 监听返回按钮
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mPosition == 1) {
				this.finish();
				mIntent.setClass(GalleryActivity.this, ImageUploadActivity.class);
				startActivity(mIntent);
				FinishActivityUtils.exit();
			} else if (mPosition == 2) {
				this.finish();
				mIntent.setClass(GalleryActivity.this, ShowAllPhoto.class);
				startActivity(mIntent);
			}
		}
		return true;
	}

	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;

		private int size;

		public MyPageAdapter(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {
			try {
				((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

	public void onResume() {
		super.onResume();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}
}
