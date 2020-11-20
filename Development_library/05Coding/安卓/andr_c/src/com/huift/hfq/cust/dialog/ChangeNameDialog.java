package com.huift.hfq.cust.dialog;

import java.util.ArrayList;

import com.huift.hfq.base.pickadapter.AbstractWheelTextAdapter;
import com.huift.hfq.base.pickview.OnWheelChangedListener;
import com.huift.hfq.base.pickview.OnWheelScrollListener;
import com.huift.hfq.base.pickview.WheelView;
import com.huift.hfq.cust.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 
 * @author ywl
 *
 */
public class ChangeNameDialog extends Dialog implements android.view.View.OnClickListener {

	private static final String TAG = ChangeNameDialog.class.getSimpleName();
	/** 选中的字体大小*/
	private int mMaxsize = 24;
	/** 没选中的字体大小*/
	private int mMinsize = 14;
	/** 选中控件*/
	private WheelView mWvName;
	/** 最外层布局*/
	private View mLyNameParent;
	/** 数据外的布局*/
	private View mLyNameChild;
	/** 确定按钮*/
	private TextView mBtnSure;
	/** 消息按钮*/
	private TextView mBtnCancel;
	/** 对应的父类窗体*/
	private Context context;
	/** 自制数据*/
	private String[] mMyNameDatas;
	/** 数据结构*/
	private ArrayList<String> mNameList = new ArrayList<String>();
	/** 适配器*/
	private NameAdapter mNameAdapter;
	/** 初始化数据*/
	private String mInitName = "小三";
	/** 点击时间*/
	private OnNameCListener onNameCListener;

	/** 构造函数*/
	public ChangeNameDialog(Context context) {
		super(context, R.style.ShareDialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_my_name);
		// 获取控件
		findView();
		// 初始化数据
		initDataTest();
		// 初始化适配器
		initAdapter();
		
	}

	/**
	 * 准备
	 */
	private void initDataTest() {
		mMyNameDatas = new String[10];
		mMyNameDatas[0] = "周倩";
		mMyNameDatas[1] = "彭颖";
		mMyNameDatas[2] = "鬼哥";
		mMyNameDatas[3] = "胖子";
		mMyNameDatas[4] = "班长";
		mMyNameDatas[5] = "思思";
		mMyNameDatas[6] = "小三";
		mMyNameDatas[7] = "猪哥";
		mMyNameDatas[8] = "莉子";
		mMyNameDatas[9] = "皮皮狗";
		myInitNameTest(); // 初始化数据
	}
	
	/**
	 * 初始化省会
	 */
	public void myInitNameTest() {
		for (int i = 0; i < mMyNameDatas.length; i++) {
			mNameList.add(mMyNameDatas[i]);
		}
	}
	
	/**
	 * 初始化适配器
	 */
	private void initAdapter() {
		mNameAdapter = new NameAdapter(context, mNameList, getNameItem(mInitName), mMaxsize, mMinsize);
		mWvName.setVisibleItems(5);
		mWvName.setViewAdapter(mNameAdapter);
		mWvName.setCyclic(true);
		mWvName.setCurrentItem(getNameItem(mInitName));
		
		mWvName.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String currentText = (String) mNameAdapter.getItemText(wheel.getCurrentItem());
				mInitName = currentText;
				setTextviewSize(currentText, mNameAdapter);
			}
		});

		mWvName.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String currentText = (String) mNameAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mNameAdapter);
			}
		});
	}

	/**
	 * 获取控件
	 */
	private void findView() {
		mWvName = (WheelView) findViewById(R.id.wv_my_name);
		mLyNameParent = findViewById(R.id.ly_name_parent);
		mLyNameChild = findViewById(R.id.ly_name_child);
		mBtnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
		mBtnCancel = (TextView) findViewById(R.id.btn_myinfo_cancel);
		
//		mWvName.setBackgroundResource(context.getResources().getColor(R.color.red));
		
		mLyNameParent.setOnClickListener(this);
		mLyNameChild.setOnClickListener(this);
		mBtnSure.setOnClickListener(this);
		mBtnCancel.setOnClickListener(this);
	}

	private class NameAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected NameAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
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

	
	/**
	 * 设置字体大小
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSize(String curriteItemText, NameAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(24);
			} else {
				textvew.setTextSize(14);
			}
		}
	}

	/**
	 * 回掉借口
	 * @param onNameCListener
	 */
	public void setNamekListener(OnNameCListener onNameCListener) {
		this.onNameCListener = onNameCListener;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mBtnSure) {
			if (onNameCListener != null) {
				onNameCListener.onClick(mInitName);
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
	 * 回调接口
	 * @author Administrator
	 *
	 */
	public interface OnNameCListener {
		public void onClick(String name);
	}

	/**
	 * 初始化地点
	 * 
	 * @param province
	 * @param city
	 */
	public void setName(String name) {
		if (name != null && name.length() > 0) {
			this.mInitName = name;
		}
	}

	/**
	 * 返回列表索引，没有就返回默认“姓名”
	 * @param name
	 * @return
	 */
	public int getNameItem(String name) {
		int size = mNameList.size();
		int nameIndex = 0;
		// 输入的名字在列表里没
		boolean flagName = true;
		for (int i = 0; i < size; i++) {
			if (name.equals(mNameList.get(i))) {
				flagName = false;
				return nameIndex;
			} else {
				nameIndex++;
			}
		}
		if (flagName) {
			mInitName = "小三";
			return 22;
		}
		return nameIndex;
	}

}