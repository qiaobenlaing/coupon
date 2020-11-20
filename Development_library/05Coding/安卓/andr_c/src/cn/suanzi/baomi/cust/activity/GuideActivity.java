package cn.suanzi.baomi.cust.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.LocationUtil;
import cn.suanzi.baomi.base.view.StatusBarView;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.adapter.GuideViewAadapter;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 引导界面的activity
 * 
 * @author Wei.Fang
 */
public class GuideActivity extends Activity implements OnPageChangeListener {
	/** 界面滑动ViewPager */
	@ViewInject(R.id.guideactivity_viewPager)
	ViewPager mViewPager;
	/** 小圆点的Layout父容器 */
	@ViewInject(R.id.layout_point)
	LinearLayout mLinearLayout;
	/** 跳转用的可点击图片，当作按钮使用 */
	@ViewInject(R.id.iv_turn_to)
	ImageView mImgBtn;
	/** 当前页卡角标 */
	int currentIndex;
	/** 图片数据源 */
	private int[] pics = { R.drawable.guide1,R.drawable.guide2, R.drawable.guide3 };
	/** ViewPager适配器 */
	private GuideViewAadapter mAdapter;
	/** 页面下方的ImageView组 */
	private ImageView[] mPoints;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		StatusBarView.statusBar(this);
		ViewUtils.inject(this);// 注册控件
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		initPoint();
		LocationUtil.getLocationClient();
		mAdapter = new GuideViewAadapter(this, pics);// 创建适配器
		mViewPager.setAdapter(mAdapter);// 关联适配器
		mViewPager.setOnPageChangeListener(this);// 设置页卡改变监听事件
	}
	
	
	
	/**
	 * 初始化小圆点并设置
	 */
	private void initPoint() {
		mPoints = new ImageView[pics.length];
		for (int i = 0; i < pics.length; i++) {
			mPoints[i] = (ImageView) mLinearLayout.getChildAt(i);// 获取layout_point中的子元素ImageView赋给mPoints组
			mPoints[i].setEnabled(false);
		}
		mPoints[currentIndex].setEnabled(true);
	}

	// OnPageChangeListenner中的方法
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int pos) {
		mPoints[pos].setEnabled(true);
		mPoints[currentIndex].setEnabled(false);
		currentIndex = pos;
		/**
		 * 当最后一页时为跳转至LoginActivity
		 */
		if (pos == pics.length - 1) {
			
			//final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
			
			mImgBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					Intent it = new Intent(GuideActivity.this,HomeActivity.class);
					GuideActivity.this.startActivity(it);
					GuideActivity.this.finish();// 结束GuideActivity
				}
			});
		}
	}
	
	 public void onResume(){
    	super.onResume();
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
        AppUtils.setContext(getApplicationContext());
	  }
}
