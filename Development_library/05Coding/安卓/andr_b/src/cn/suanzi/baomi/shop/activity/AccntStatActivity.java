package cn.suanzi.baomi.shop.activity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.DateInfo;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.adapter.CalendarAdapter;
import cn.suanzi.baomi.shop.utils.DataUtils;
import cn.suanzi.baomi.shop.utils.TimeUtils;
import cn.suanzi.baomi.shop.view.MyViewPager;
import cn.suanzi.baomi.shop.view.Panel;

import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @author wensi.yu 
 * 银行卡账单列表 (日历模式) 
 */
public class AccntStatActivity extends Activity{
	
	private final static String TAG = "AccntStatActivity";
	
	private LinearLayout mIvBackup;
	private TextView mTvdesc;
	
	/**
	 * 和viewpager相关变量
	 * */
	public MyViewPager mViewPager = null;
	public MyPagerAdapter mPagerAdapter = null;
	private int mCurrPager = 500;
	private TextView mShader;
	
	/**
	 * 和日历gridview相关变量
	 * */
	private GridView mGridView = null;
	public CalendarAdapter mAdapter = null;
	private GridView mCurrentView = null;
	public List<DateInfo> mCurrList = null;
	public List<DateInfo> mList = null;
	public int mLastSelected = 0;
	
	/**
	 * 显示年月
	 * */
	public TextView mYearMonth = null;      
	
	/**
	 * 第一个页面的年月
	 * */
	private int mShowCurrentYear;
	private int mShowCurrentMonth;
	
	/**
	 * 收缩展开的面板
	 * */
	private Panel mPanel;
	
	/**年月*/
	/*private String mShowYearMonth;
	private String sShowYearMonth;*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accnt_calendar);
		//保存
		Util.addActivity(this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		mIvBackup = (LinearLayout) findViewById(R.id.layout_turn_in);
		mIvBackup.setVisibility(View.VISIBLE);
		mTvdesc = (TextView) findViewById(R.id.tv_mid_content);
		mTvdesc.setText(R.string.tv_accntlist_title);
		//返回
		mIvBackup.setOnClickListener(Listener);
		
		
        initData();
        initView();
	}
	
	/**
	 * 返回
	 */
	OnClickListener Listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			finish();
			
		}
	};

	/**
	 * 初始化view
	 * */
	private void initView() {
    	mViewPager = (MyViewPager) findViewById(R.id.viewpager); 
    	mShader = (TextView) findViewById(R.id.main_frame_shader);
    	/*panel = (Panel) findViewById(R.id.panel);
    	panel.setOpen(true, false);*/
    	mShader.setVisibility(View.GONE);
    	mPagerAdapter = new MyPagerAdapter();
    	mViewPager.setAdapter(mPagerAdapter);
    	mViewPager.setCurrentItem(500); 
    	mViewPager.setPageMargin(0);
    	mYearMonth = (TextView) findViewById(R.id.main_year_month);
    	mYearMonth.setText(String.format("%04d-%02d", mShowCurrentYear, mShowCurrentMonth));
    	Log.i(TAG, "*********************************aa");
    	
    	initCalendarView(mCurrPager);
    	
    	mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageScrollStateChanged(int arg0) {
				if (arg0 == 1) {
					mShader.setText("");
					mShader.setVisibility(View.VISIBLE);
				}
				if (arg0 == 0) {
					mCurrentView = (GridView) mViewPager.findViewById(mCurrPager);
					if (mCurrentView != null) {
						mAdapter = (CalendarAdapter) mCurrentView.getAdapter();
						mCurrList = mAdapter.getList();
						int pos = DataUtils.getDayFlag(mCurrList, mLastSelected);
						mAdapter.setSelectedPosition(pos);
						mAdapter.notifyDataSetInvalidated();
					}
					mShader.setVisibility(View.GONE);
				}
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			public void onPageSelected(int position) {
		    	int year = TimeUtils.getTimeByPosition(position, mShowCurrentYear, mShowCurrentMonth, "year");
		    	int month = TimeUtils.getTimeByPosition(position, mShowCurrentYear, mShowCurrentMonth, "month");
		    	mYearMonth.setText(String.format("%04d-%02d", year, month));
				mCurrPager = position;
				mShader.setText(mYearMonth.getText());
				Log.i(TAG, "*********************************bb");
				Log.i(TAG, "showYearMonth=================="+mYearMonth.getText());
				
		       	//保存值
				SharedPreferences mSharedPreferences = getSharedPreferences("mDate", Context.MODE_PRIVATE);
			    Editor editor = mSharedPreferences.edit(); 
			    String mShowYearMonth = (String) mYearMonth.getText();
			    editor.putString("mShowYearMonth", mShowYearMonth);
			    Log.i(TAG, "mShowYearMonth☆☆☆☆☆☆☆☆☆☆☆☆"+mShowYearMonth);
			    editor.commit();
			    
			}
			
    	});
    	
    }
	
  /**
	 * 初始化日历的gridview
	 * */
    private GridView initCalendarView(int position) {
    	int year = TimeUtils.getTimeByPosition(position, mShowCurrentYear, mShowCurrentMonth, "year");
    	int month = TimeUtils.getTimeByPosition(position, mShowCurrentYear, mShowCurrentMonth, "month");
    	Log.i(TAG, "year11==================="+year);
    	Log.i(TAG, "month11=================="+month);
    	
    	String formatDate = TimeUtils.getFormatDate(year, month);
    	try {
    		mList = TimeUtils.initCalendar(formatDate, month);
    	} catch (Exception e) {
    		finish();
    	}
		mGridView = new GridView(this);
    	mAdapter = new CalendarAdapter(this, mList);
    	if (position == 500) {
    		mCurrList = mList;
	    	int pos = DataUtils.getDayFlag(mList, mLastSelected);
	    	mAdapter.setSelectedPosition(pos);
    	}
    	mGridView.setAdapter(mAdapter);
    	mGridView.setNumColumns(7);
    	mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
    	mGridView.setGravity(Gravity.CENTER);
    	//gridView.setOnItemClickListener(new OnItemClickListenerImpl(adapter, this));
    	
    	mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				SharedPreferences sharedPreferences = getSharedPreferences("mDate", Context.MODE_PRIVATE);
				String sShowYearMonth = sharedPreferences.getString("mShowYearMonth", ""); 
				Log.i("TAG", "sShowYearMonth AAAAAAAAAAAAAAAAAAAAAAAAA="+sShowYearMonth);
				
				DateInfo dateInfo = (DateInfo) mCurrentView.getItemAtPosition(position);
				//Intent intent = new Intent(AccntStatActivity.this,AccntStatListActivity.class);
				//intent.putExtra(AccntStatListFragment.newInstance().ACCNT_YEARORMONTH, sShowYearMonth);
				Log.i(TAG, "mShowYearMonth======================*********"+sShowYearMonth);
				//intent.putExtra(AccntStatListFragment.newInstance().ACCNT_DATE, dateInfo.getDate());
				//intent.putExtra("mDay", dateInfo.getDate());
				Log.i(TAG, "dateInfo====================***********"+dateInfo.getDate());
				//startActivity(intent);
			}
		});
    	return mGridView;
    }
	
	
	/**
	 * 初始化数据
	 * */
    private void initData() {
    	mShowCurrentYear = TimeUtils.getCurrentYear();
    	mShowCurrentMonth = TimeUtils.getCurrentMonth();
    	
    	String mCurrentYear = String.valueOf(mShowCurrentYear);
    	//String mCurrentMonth = String.valueOf(currentMonth);
    	
    	Log.i(TAG, "mCurrentYear22==================="+mCurrentYear);
    	//Log.i(TAG, "mCurrentMonth22==================="+mCurrentMonth);
    	
    	String mShowYearMonth = mCurrentYear+"-"+(mShowCurrentMonth > 9 ? mShowCurrentMonth : "0"+mShowCurrentMonth);
    	Log.i(TAG, "initData.mShowYearMonth==============="+mShowYearMonth);
    	//保存值
		SharedPreferences mSharedPreferences = this.getSharedPreferences("mDate", Context.MODE_PRIVATE);
	    Editor editor = mSharedPreferences.edit();     
	    editor.putString("mShowYearMonth", mShowYearMonth);
	    editor.commit();
    	
    	mLastSelected = TimeUtils.getCurrentDay();
    	String formatDate = TimeUtils.getFormatDate(mShowCurrentYear, mShowCurrentMonth);
    	try {
    		mList = TimeUtils.initCalendar(formatDate, mShowCurrentMonth);
    	} catch (Exception e) {
    		finish();
    	}
    }
	
	/**
	 * viewpager的适配器，从第500页开始，最多支持0-1000页
	 * */
    private class MyPagerAdapter extends PagerAdapter {

		@Override
		public void setPrimaryItem(ViewGroup container, int position,
				Object object) {
			mCurrentView = (GridView) object;
			mAdapter = (CalendarAdapter) mCurrentView.getAdapter();
		}

		@Override
		public int getCount() {
			return 1000;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			GridView gv = initCalendarView(position);
			gv.setId(position);
			container.addView(gv);
			return gv;
		}
    }
    
    /**
	 * 点击返回到主页面
	 * @param view
	 */
	@OnClick(R.id.iv_backup)
	public void btnAccntStatClick(View view) {		
		Intent intent = new Intent(AccntStatActivity.this,HomeActivity.class);
		intent.putExtra("type", 1);  
		startActivity(intent);
	}
	
	public void onResume(){
    	super.onResume();
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
    }
}
