package cn.suanzi.baomi.cust.dialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

import cn.suanzi.baomi.base.pickadapter.AbstractWheelTextAdapter;
import cn.suanzi.baomi.base.pickview.OnWheelChangedListener;
import cn.suanzi.baomi.base.pickview.OnWheelScrollListener;
import cn.suanzi.baomi.base.pickview.WheelView;
import cn.suanzi.baomi.base.utils.PickUtil;
import cn.suanzi.baomi.cust.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 日期选择对话框
 * @author ywl
 *
 */
public class ChangeBirthDialog extends Dialog implements android.view.View.OnClickListener {
	private static final String TAG = ChangeBirthDialog.class.getSimpleName();
	/** 最大年数是2100*/
	private static final int MAXYEAR = 2100;
	/** 最小年数是1900*/
	private static final int MINYEAR = 1900;
	
	private Context context;
	/** 控件*/
	private WheelView mWvYear;
	private WheelView mWvMonth;
	private WheelView mWvDay;
	private View mVChangeBirth;
	private View mVChangeBirthChild;
	private TextView mBtnSure;
	private TextView mBtnCancel;
	private TextView mTvSelTime;
	
	/** 数据*/
	private ArrayList<String> mYearList = new ArrayList<String>();
	private ArrayList<String> mMonthList = new ArrayList<String>();
	private ArrayList<String> mDayList = new ArrayList<String>();
	
	/** 适配器*/
	private CalendarTextAdapter mYearAdapter;
	private CalendarTextAdapter mMonthAdapter;
	private CalendarTextAdapter mDaydapter;

	/** 年月*/
	private int month;
	private int day;

	private int currentYear = PickUtil.getYear();
	private int currentMonth = 1;
	private int currentDay = 1;

	/** 判断是否有设置过时间*/
	private boolean mIsSetDataFlag = false;
	
	/** 选中的时间*/
	private String selectYear;
	private String selectMonth;
	private String selectDay;
	
	/** 监听事件*/
	private OnBirthListener onBirthListener;

	public ChangeBirthDialog(Context context) {
		super(context, R.style.ShareDialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_myinfo_changebirth);
		findView();

		if (!mIsSetDataFlag) {
			initData();
		} 
		initAllData();
		initAdapter();
		mTvSelTime.setText("选择日期：" + selectYear +"年"+ selectMonth+"月"+selectDay+"日");
	}

	/**
	 * 初始化所有数据
	 */
	private void initAllData() {
		initYears();
		initMonths();
		initDays(day);
	}

	/**
	 * 初始化适配器
	 */
	private void initAdapter() {
		mYearAdapter = new CalendarTextAdapter(context, mYearList, setYear(currentYear), PickUtil.MAXTEXTSIZE, PickUtil.MINTEXTSIZE);
		mWvYear.setVisibleItems(5);
		mWvYear.setViewAdapter(mYearAdapter);
		mWvYear.setCyclic(true);
		mWvYear.setCurrentItem(setYear(currentYear));

		mMonthAdapter = new CalendarTextAdapter(context, mMonthList, setMonth(currentMonth), PickUtil.MAXTEXTSIZE, PickUtil.MINTEXTSIZE);
		mWvMonth.setVisibleItems(5);
		mWvMonth.setViewAdapter(mMonthAdapter);
		mWvMonth.setCyclic(true);
		mWvMonth.setCurrentItem(setMonth(currentMonth));

		mDaydapter = new CalendarTextAdapter(context, mDayList, currentDay - 1, PickUtil.MAXTEXTSIZE, PickUtil.MINTEXTSIZE);
		mWvDay.setVisibleItems(5);
		mWvDay.setViewAdapter(mDaydapter);
		mWvDay.setCyclic(true);
		mWvDay.setCurrentItem(currentDay - 1);

		// 年
		mWvYear.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
				selectYear = Pattern.compile("[^0-9]").matcher(currentText).replaceAll("");
				PickUtil.setTextviewSize(currentText, mYearAdapter);
				currentYear = Integer.parseInt(selectYear);
				setYear(currentYear);
				initMonths();
				mMonthAdapter = new CalendarTextAdapter(context, mMonthList, 0,PickUtil.MAXTEXTSIZE, PickUtil.MINTEXTSIZE);
				mWvMonth.setVisibleItems(5);
				mWvMonth.setViewAdapter(mMonthAdapter);
				mWvMonth.setCurrentItem(0);
				mTvSelTime.setText("选择日期：" + selectYear +"年"+ selectMonth+"月"+selectDay+"日");
			}
		});

		mWvYear.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
				PickUtil.setTextviewSize(currentText, mYearAdapter);
			}
		});

		// 月
		mWvMonth.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
				selectMonth = Pattern.compile("[^0-9]").matcher(currentText).replaceAll("");;
				PickUtil.setTextviewSize(currentText, mMonthAdapter);
				setMonth(Integer.parseInt(selectMonth));
				initDays(day);
				mDaydapter = new CalendarTextAdapter(context, mDayList, 0, PickUtil.MAXTEXTSIZE, PickUtil.MINTEXTSIZE);
				mWvDay.setVisibleItems(5);
				mWvDay.setViewAdapter(mDaydapter);
				mWvDay.setCurrentItem(0);
				mTvSelTime.setText("选择日期：" + selectYear +"年"+ selectMonth+"月"+selectDay+"日");
			}
		});

		mWvMonth.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
				PickUtil.setTextviewSize(currentText, mMonthAdapter);
			}
		});
		
		// 日
		mWvDay.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mDaydapter.getItemText(wheel.getCurrentItem());
				PickUtil.setTextviewSize(currentText, mDaydapter);
				selectDay = Pattern.compile("[^0-9]").matcher(currentText).replaceAll("");
				mTvSelTime.setText("选择日期：" + selectYear +"年"+ selectMonth+"月"+selectDay+"日");
			}
		});

		mWvDay.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String currentText = (String) mDaydapter.getItemText(wheel.getCurrentItem());
				PickUtil.setTextviewSize(currentText, mDaydapter);
			}
		});
	}

	/**
	 * 获取控件
	 */
	private void findView() {
		mWvYear = (WheelView) findViewById(R.id.wv_birth_year);
		mWvMonth = (WheelView) findViewById(R.id.wv_birth_month);
		mWvDay = (WheelView) findViewById(R.id.wv_birth_day);

		mVChangeBirth = findViewById(R.id.ly_myinfo_changebirth);
		mVChangeBirthChild = findViewById(R.id.ly_myinfo_changebirth_child);
		mBtnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
		mBtnCancel = (TextView) findViewById(R.id.btn_myinfo_cancel);
		mTvSelTime = (TextView) findViewById(R.id.tv_share_title);

		mVChangeBirth.setOnClickListener(this);
		mVChangeBirthChild.setOnClickListener(this);
		mBtnSure.setOnClickListener(this);
		mBtnCancel.setOnClickListener(this);
	}
	
	/**
	 * 给年赋值
	 */
	public void initYears() {
		for (int i = MAXYEAR; i >= MINYEAR; i--) {
			mYearList.add(i + "年");
		}
	}

	/**
	 * 给月赋值
	 * @param months
	 */
	public void initMonths() {
		mMonthList.clear();
		for (int i = 1; i <= 12; i++) {
			mMonthList.add((i <= 9 ? "0" + i : i+"") + "月");
		}
	}

	/**
	 *  给日赋值
	 * @param days
	 */
	public void initDays(int days) {
		mDayList.clear();
		Log.d(TAG, "initDays=" + days);
		for (int i = 1; i <= days; i++) {
			mDayList.add((i <= 9 ? "0" + i : i+"") + "日");
		}
	}
	
	/**
	 * 适配器
	 */
	private class CalendarTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected CalendarTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
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

	public void setBirthdayListener(OnBirthListener onBirthListener) {
		this.onBirthListener = onBirthListener;
	}

	@Override
	public void onClick(View v) {

		if (v == mBtnSure) {
			if (onBirthListener != null) {
				onBirthListener.onClick(selectYear, selectMonth, selectDay);
			}
		} else if (v == mBtnSure) {

		} else if (v == mVChangeBirthChild) {
			return;
		} else {
			dismiss();
		}
		dismiss();

	}
	
	public void initData() {
		// 赋值当前的年月日
		setDate(PickUtil.getYear(), PickUtil.getMonth(), PickUtil.getDay());
		this.currentDay = 1;
		this.currentMonth = 1;
	}

	/**
	 * 设置年月日
	 * @param year
	 * @param month
	 * @param day
	 */
	public void setDate(int year, int month, int day) {
		selectYear = year + "";
		selectMonth = month + "";
		selectDay = day + "";
		mIsSetDataFlag = true;
		this.currentYear = year;
		this.currentMonth = month;
		this.currentDay = day;
		if (year == PickUtil.getYear()) {
			this.month = PickUtil.getMonth();
		} else {
			this.month = 12;
		}
		calDays(year, month);
	}

	/**
	 * 设置年份
	 * @param year
	 */
	public int setYear(int year) {
		int yearIndex = 0;
		if (year != PickUtil.getYear()) {
			this.month = 12;
		} else {
			this.month = PickUtil.getMonth();
		}
		for (int i = MAXYEAR; i >= MINYEAR; i--) {
			if (i == year) {
				return yearIndex;
			}
			yearIndex++;
		}
		return yearIndex;
	}

	
	/**
	 * 设置月份
	 * @param year
	 * @param month
	 * @return
	 */
	public int setMonth(int month) {
		int monthIndex = 0;
		calDays(currentYear, month);
		for (int i = 1; i < this.month; i++) {
			if (month == i) {
				return monthIndex;
			} else {
				monthIndex++;
			}
		}
		return monthIndex;
	}

	/**
	 * 回调
	 */
	public interface OnBirthListener {
		public void onClick(String year, String month, String day);
	}

	/**
	 * 计算每月多少天
	 * @param month 月
	 * @param leayyear年
	 */
	public void calDays(int year, int month) {
		boolean leayyear = false;
		if (year % 4 == 0 && year % 100 != 0) {
			leayyear = true;
		} else {
			leayyear = false;
		}
		for (int i = 1; i <= 12; i++) {
			if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
				this.day = 31;
			} else if (month == 2) {
				this.day = leayyear ? 29 : 28;
			} else {
				this.day = 30;
			}
		}
		if (year == PickUtil.getYear() && month == PickUtil.getMonth()) {
			this.day = PickUtil.getDay();
		}
	}
}