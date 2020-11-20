// ---------------------------------------------------------
// @author    
// @version   1.0.0
// @createTime 2015.5.4
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.view.StatusBarView;
import com.huift.hfq.shop.adapter.GuideViewAadapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 引导界面的activity
 * @author 
 */
public class GuideActivity extends Activity implements OnPageChangeListener{
	
	private final static String TAG = "GuideActivity";
	/** 界面滑动ViewPager */
	@ViewInject(R.id.guideactivity_viewPager)
	private ViewPager mViewPager;
	/** 小圆点的Layout父容器*/
	@ViewInject(R.id.layout_point)
	private LinearLayout mLinearLayout;
	/** 跳转用的可点击图片，当作按钮使用*/
	@ViewInject(R.id.iv_turn_to)
	private ImageView mImgBtn;
	/** 当前页卡角标 */
	private int currentIndex;
	/** 图片数据源 */
	private int[] pics = {R.drawable.guide1,R.drawable.guide2,R.drawable.guide3};
	/** ViewPager适配器*/
	private GuideViewAadapter mAdapter;
	/** 页面下方的ImageView组*/
	private ImageView[] mPoints;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		StatusBarView.statusBar(this);
		ViewUtils.inject(this);//注册控件
		initPoint();
		mAdapter = new GuideViewAadapter(this,pics);//创建适配器
	    mViewPager.setAdapter(mAdapter);//关联适配器
	  	mViewPager.setOnPageChangeListener(this);//设置页卡改变监听事件
	}

	/**
	 * 初始化小圆点并设置
	 */
	private void initPoint() {
		mPoints = new ImageView[pics.length];
		for (int i=0;i<pics.length;i++) {
			mPoints[i] = (ImageView)mLinearLayout.getChildAt(i);//获取layout_point中的子元素ImageView赋给mPoints组
			mPoints[i].setScaleType(ScaleType.CENTER_INSIDE);
			mPoints[i].setEnabled(false);
	        }
		mPoints[currentIndex].setEnabled(true);
		//mPoints[currentIndex].setVisibility(View.GONE);
	}

	//OnPageChangeListenner中的方法
	@Override
	public void onPageScrollStateChanged(int arg0) {}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}
	@Override
	public void onPageSelected(int pos) {
		mPoints[pos].setEnabled(true);
		mPoints[currentIndex].setEnabled(false)       ;
		Log.i(TAG, "mPoints[pos]==========="+mPoints[pos]);
		currentIndex = pos;
		//mPoints[currentIndex].setVisibility(View.GONE);
		/**
		 * 当最后一页时为跳转至LoginActivity
		 */
		if (pos == pics.length-1) {
			mImgBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent it =new Intent(GuideActivity.this,LoginActivity.class);
					GuideActivity.this.startActivity(it);
					GuideActivity.this.finish();//结束GuideActivity
				}
			});
		}
	}
}
