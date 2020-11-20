package com.huift.hfq.cust.dialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huift.hfq.base.pickadapter.AbstractWheelTextAdapter;
import com.huift.hfq.base.pickview.OnWheelChangedListener;
import com.huift.hfq.base.pickview.OnWheelScrollListener;
import com.huift.hfq.base.pickview.WheelView;
import com.huift.hfq.cust.R;

/**
 * 
 * @author ywl
 * 
 */
public class ChangeClassDialog extends Dialog implements View.OnClickListener {

	private static final String TAG = ChangeClassDialog.class.getSimpleName();
	/** 天 */
	private WheelView mWvDay;
	/** 开始时间 */
	private WheelView mWvStartTime;
	/** 时长 */
	private WheelView mWvDuration;
	/** 最外层布局 */
	private View mLyNameParent;
	/** 数据外的布局 */
	private View mLyNameChild;
	/** 确定按钮 */
	private TextView mBtnSure;
	/** 消息按钮 */
	private TextView mBtnCancel;
	/** 选中时间 */
	private TextView mTvSelTitle;
	/** 对应的父类窗体 */
	private Context context;
	/** 工作日 */
	private String[] mDayDatas;
	/** 开始时间 */
	private String[] mStartTimeDatas;
	/** 时长 */
	private String[] mDurationDatas;
	/** 数据结构 */
	private ArrayList<String> mDayList = new ArrayList<String>();
	private ArrayList<String> mStartTimeList = new ArrayList<String>();
	private ArrayList<String> mDurationList = new ArrayList<String>();
	/** 第一个适配器 */
	private TimeAdapter mDayAdapter;
	/** 第二个适配器 */
	private TimeAdapter mStartTimeAdapter;
	/** 第三个适配 */
	private TimeAdapter mDurationAdapter;
	/** 工作日 */
	private String mChangeTime = "07:00";
	private String mDayTime = "工作日";
	/** 开始时间 */
	private String mStartTime = "07:00";
	/** 时间间隔 */
	private String mDurationTime = "30";
	/** 点击时间 */
	private OnCallbackListener onCallbackCListener;
	/** 选中的字体大小 */
	private int mMaxsize = 24;
	/** 没选中的字体大小 */
	private int mMinsize = 14;

	/** 构造函数 */
	public ChangeClassDialog(Context context) {
		super(context, R.style.ShareDialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_class_name);
		// 获取控件
		findView();
		// 初始化数据
		initAllDate();
		// 初始化适配器
		initAdapter();

	}

	/**
	 * 准备数据
	 */
	private void initAllDate() {
		mDayDatas = new String[] { "每天", "工作日", "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
		mDurationDatas = new String[] { "30", "35", "40", "45", "50", "55", "60" };
		mStartTimeDatas = new String[181];
		mStartTimeDatas[0] = mStartTime;
		SimpleDateFormat forma = new SimpleDateFormat("HH:mm");
		Calendar cal = Calendar.getInstance();
		Date date = null;

		for (int i = 0; i < mStartTimeDatas.length; i++) {
			try {
				date = forma.parse(mChangeTime);
				cal.setTime(date);
				cal.add(java.util.Calendar.MINUTE, 5);
				mChangeTime = forma.format(cal.getTime());
				mStartTimeDatas[i + 1] = mChangeTime;
				if (mChangeTime.equals("22:00")) {
					break;
				}
			}
			catch (ParseException e) {
				e.printStackTrace();
			}
		}

		initDate(); // 初始化数据
		mTvSelTitle.setText("选中时间 ： " + mDayTime + "-" + mStartTime + "-" + mDurationTime);
	}

	/**
	 * 初始化数据
	 */
	public void initDate() {
		for (int i = 0; i < mDayDatas.length; i++) {
			mDayList.add(mDayDatas[i]);
		}
		for (int i = 0; i < mDurationDatas.length; i++) {
			mDurationList.add(mDurationDatas[i]);
		}
		for (int i = 0; i < mStartTimeDatas.length; i++) {
			mStartTimeList.add(mStartTimeDatas[i]);
		}
	}

	/**
	 * 初始化适配器
	 */
	private void initAdapter() {
		// 第一个选择
		mDayAdapter = new TimeAdapter(context, mDayList, getNameItem(mDayTime, mDayList), mMaxsize, mMinsize);
		mWvDay.setVisibleItems(5);
		mWvDay.setViewAdapter(mDayAdapter);
		mWvDay.setCyclic(true);
		mWvDay.setCurrentItem(getNameItem(mDayTime, mDayList));
		// 第二个选择
		mStartTimeAdapter = new TimeAdapter(context, mStartTimeList, getNameItem(mStartTime, mStartTimeList), mMaxsize, mMinsize);
		mWvStartTime.setVisibleItems(5);
		mWvStartTime.setViewAdapter(mStartTimeAdapter);
		mWvStartTime.setCyclic(true);
		mWvStartTime.setCurrentItem(getNameItem(mStartTime, mStartTimeList));
		// 第三个选择
		mDurationAdapter = new TimeAdapter(context, mDurationList, getNameItem(mDurationTime, mDurationList), mMaxsize, mMinsize);
		mWvDuration.setVisibleItems(5);
		mWvDuration.setViewAdapter(mDurationAdapter);
		mWvDuration.setCyclic(true);
		mWvDuration.setCurrentItem(getNameItem(mDurationTime, mDurationList));

		// 第一个
		mWvDay.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String currentText = (String) mDayAdapter.getItemText(wheel.getCurrentItem());
				mDayTime = currentText;
				mTvSelTitle.setText("选中时间 ： " + mDayTime + "-" + mStartTime + "-" + mDurationTime);
				setTextviewSize(currentText, mDayAdapter);
			}
		});
		mWvDay.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String currentText = (String) mDayAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mDayAdapter);
			}
		});
		// 第二个
		mWvStartTime.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String currentText = (String) mStartTimeAdapter.getItemText(wheel.getCurrentItem());
				mStartTime = currentText;
				mTvSelTitle.setText("选中时间 ： " + mDayTime + "-" + mStartTime + "-" + mDurationTime);
				setTextviewSize(currentText, mStartTimeAdapter);
			}
		});

		mWvStartTime.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String currentText = (String) mStartTimeAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mStartTimeAdapter);
			}
		});
		// 第三个
		mWvDuration.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String currentText = (String) mDurationAdapter.getItemText(wheel.getCurrentItem());
				mDurationTime = currentText;
				mTvSelTitle.setText("选中时间 ： " + mDayTime + "-" + mStartTime + "-" + mDurationTime);
				setTextviewSize(currentText, mDurationAdapter);
			}
		});

		mWvDuration.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String currentText = (String) mDurationAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mDurationAdapter);
			}
		});
	}

	/**
	 * 获取控件
	 */
	private void findView() {
		mWvDay = (WheelView) findViewById(R.id.wv_day);
		mWvStartTime = (WheelView) findViewById(R.id.wv_start_time);
		mWvDuration = (WheelView) findViewById(R.id.wv_duration);
		mLyNameParent = findViewById(R.id.ly_name_parent);
		mLyNameChild = findViewById(R.id.ly_name_child);
		mBtnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
		mBtnCancel = (TextView) findViewById(R.id.btn_myinfo_cancel);
		mTvSelTitle = (TextView) findViewById(R.id.tv_share_title);
		mLyNameParent.setOnClickListener(this);
		mLyNameChild.setOnClickListener(this);
		mBtnSure.setOnClickListener(this);
		mBtnCancel.setOnClickListener(this);
	}

	/**
	 * 适配器
	 * @author liyanfang
	 */
	private class TimeAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected TimeAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
			this.list = list;
			setItemTextResource(R.id.tempValue);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index) + "";
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mBtnSure) {
			if (onCallbackCListener != null) {
				onCallbackCListener.onClick(mDayTime , mStartTime , mDurationTime);
			}
		} else if (v == mBtnCancel) {

		} else if (v == mLyNameChild) {
			return;
		} else {
			dismiss();
		}
		dismiss();
	}

	/**
	 * 回掉接口
	 * @param onNameCListener
	 */
	public void setBackListener(OnCallbackListener callbackListener) {
		this.onCallbackCListener = callbackListener;
	}
	
	/**
	 * 回调接口的实现类
	 * @author Administrator
	 */
	public interface OnCallbackListener {
		public void onClick(String dayTime ,String startTime ,String duration);
	}

	/**
	 * 初始化数据
	 * @param day 第一个选项
	 * @param startTime 第二个选项
	 * @param duration 第三个选项
	 */
	public void setInitData(String day, String startTime, String duration) {
		if (day != null && day.length() > 0) {
			this.mDayTime = day;
		}
		if (startTime != null && startTime.length() > 0) {
			System.out.println("b");
			this.mStartTime = startTime;
		}
		if (duration != null && duration.length() > 0) {
			System.out.println("c");
			this.mDurationTime = duration;
		}
	}

	/**
	 * 返回列表索引，没有就返回默认“0”
	 * @param name
	 * @return 
	 */
	public int getNameItem(String name, List<String> dataList) {
		int size = dataList.size();
		int nameIndex = 0;
		// 输入的名字在列表里没
		boolean flagName = true;
		for (int i = 0; i < size; i++) {
			if (name.equals(dataList.get(i))) {
				flagName = false;
				return nameIndex;
			}
			else {
				nameIndex++;
			}
		}
		if (flagName) {
			return 0;
		}
		return nameIndex;
	}
	
	/**
	 * 设置字体大小
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSize(String curriteItemText, AbstractWheelTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(24);
			}
			else {
				textvew.setTextSize(14);
			}
		}
	}

}