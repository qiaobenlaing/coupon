package com.huift.hfq.cust.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.data.Storage;
import com.huift.hfq.base.pojo.ErrorInfo;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.Calculate;
import com.huift.hfq.base.utils.FinishActivityUtils;
import com.huift.hfq.cust.R;

import com.huift.hfq.cust.adapter.ErrorInfoAdapter;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.model.AddErrorInformationTask;
import com.huift.hfq.cust.model.ErrorInforTask;
import com.huift.hfq.cust.utils.Bimp;
import com.huift.hfq.cust.utils.FileUtils;
import com.huift.hfq.cust.utils.ImageItem;
import com.huift.hfq.cust.utils.PublicWay;
import com.huift.hfq.cust.utils.Res;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 纠错的信息
 * @author wensi.yu
 */
public class ImageUploadActivity extends Activity {

	private final static String TAG = "ImageUploadActivity";
 
	private final static String ERROR_TITLE = "纠错信息";
	public final static String IMAGE_URl = "imageUrl";
	public final static String IMAGE_CODE = "imageCode";
	public final static String FLAG = "1";
	public final static int IMAGE_SUCC = 101;
	public final static int REQUEST_CODE = 100;
	/** 返回图片 **/
	private ImageView mIvBackup;
	/** 功能描述文本 **/
	private TextView mTvdesc;
	/** 一张图片的显示*/
	private GridView mNoScrollgridview;
	/** Adapter*/
	private GridAdapter mAdapter;
	private View mParentView;
	/** 弹出层*/
	private PopupWindow mPop = null;
	/** 整个弹出层*/
	private LinearLayout mLyPopup;
	/** bitmap*/
	public static Bitmap mBitmap;
	private String mImagePath;
	private ArrayList<String> mListFiles;
	/** 得到图片的路径 */
	private static ArrayList<String> mImageFile;
	/** 处理过的路径 */
	private static ArrayList<String> mUpLoadIamge;
	private String mImageLoadUrl;
	/** 提交*/
	private Button mBtnRrrorCommit;
	/** 下拉信息*/
	private TextView mTvErrorInfo;
	/** 错误信息的一行*/
	private RelativeLayout mLyErrorLine;
	/**显示的popWindow*/
	private PopupWindow mPopupWindow;
	/**popwidnow的内容视图*/
	private View mPopWindowView;
	/** 适配器*/
	private ErrorInfoAdapter mErrorAdapter;
	/** ListView*/
	private ListView mListView;
	/** 点击错误信息的编码*/
	private String mErrorInfoCode;
	/** 得到被反馈的商家编码*/
	private String mToShopCode;
	/** 错误信息的对象*/
	private ErrorInfo errorInfo;
	/** 进度条*/
	private ProgressDialog mProcessDialog;
	/** 点击选择错误信息的标志*/
	private boolean mClickFlag = false;
	/** 输入的错误信息*/
	private EditText mEtInputErrorInfo;
	/** 错误信息*/
	private String mInputErrorInfo;
	/** 保存输入的纠正信息*/
	private String mInputErroeInfo;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				String mutliImgPath = (String) msg.obj;
				errorInfo(mutliImgPath);
			}
		};
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Res.init(this);
		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused);
		PublicWay.activityList.add(this);
		mParentView = getLayoutInflater().inflate(R.layout.activity_selectimg, null);
		ViewUtils.inject(this, mParentView);
		setContentView(mParentView);
		FinishActivityUtils.addActivity(ImageUploadActivity.this);
		Util.addActivity(ImageUploadActivity.this);
		AppUtils.setActivity(this);
		Util.addLoginActivity(ImageUploadActivity.this);
		AppUtils.setContext(getApplicationContext());
		Util.addLoginActivity(this);//结束当前页面
		init();
	}

	public void init() {
		mListFiles = new ArrayList<String>();
		mImageFile = new ArrayList<String>();
		mUpLoadIamge = new ArrayList<String>();
		Log.d(TAG, "图片的长度====" + mUpLoadIamge.size());
		mIvBackup = (ImageView) findViewById(R.id.iv_turn_in);//返回
		mTvdesc = (TextView) findViewById(R.id.tv_mid_content);//标题
		mIvBackup.setVisibility(View.VISIBLE);
		mTvdesc.setText(ERROR_TITLE);
		mToShopCode = DB.getStr(CustConst.TO_SHOP_CODE);
		Log.d(TAG, "mToShopCode=="+mToShopCode);
		mBtnRrrorCommit = (Button) findViewById(R.id.btn_commit);//提交
		mEtInputErrorInfo = (EditText) findViewById(R.id.et_errorinfo_input);//输入的错误信息
		mEtInputErrorInfo.addTextChangedListener(changeTextListener);
		mTvErrorInfo = (TextView) findViewById(R.id.tv_select_error);//错误信息
		mLyErrorLine = (RelativeLayout) findViewById(R.id.ly_error_line);//错误的一行
		mLyErrorLine.setOnClickListener(errorListener);
		mTvErrorInfo.setText(R.string.err_info_list);
		//getBtnStatus(Util.NUM_ZERO); // 默认进来是不可编辑的
		
		mPop = new PopupWindow(ImageUploadActivity.this);
		View view = getLayoutInflater().inflate(R.layout.activity_phone_popu, null);
		mLyPopup = (LinearLayout) view.findViewById(R.id.ll_popup);
		mPop.setWidth(LayoutParams.MATCH_PARENT);
		mPop.setHeight(LayoutParams.WRAP_CONTENT);
		mPop.setBackgroundDrawable(new BitmapDrawable());
		mPop.setFocusable(true);
		mPop.setOutsideTouchable(true);
		mPop.setContentView(view);
		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		// Button takePhoto = (Button) view.findViewById(R.id.btn_take_photo);
		Button pickPhoto = (Button) view.findViewById(R.id.btn_pick_photo);
		Button cancel = (Button) view.findViewById(R.id.btn_cancel);
		parent.setOnClickListener(popuListener);
		//takePhoto.setOnClickListener(popuListener);
		pickPhoto.setOnClickListener(popuListener);
		cancel.setOnClickListener(popuListener);

		mNoScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		mNoScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mAdapter = new GridAdapter(this);
		//mAdapter.update();
		mNoScrollgridview.setAdapter(mAdapter);
		mNoScrollgridview.setOnItemClickListener(itemListener);
	}
	
	/**
	 * 输入纠错信息的监听
	 */
	TextWatcher changeTextListener = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable editable) {
			mInputErrorInfo = mEtInputErrorInfo.getText().toString();
			
			Log.d(TAG, "changeTextListener  errInfo=="+mErrorInfoCode);
			Log.d(TAG, "changeTextListener  mInputErrorInfo=="+mInputErrorInfo);
			Log.d(TAG, "changeTextListener  tempSelectBitmap=="+Bimp.tempSelectBitmap.size());
			
			if ((null != DB.getStr(CustConst.Err_SHOP_CODE) && !"".equals(DB.getStr(CustConst.Err_SHOP_CODE)))
					&& (Bimp.tempSelectBitmap.size() > 0 && null != Bimp.tempSelectBitmap)) {
				getBtnStatus(Util.NUM_ONE);  
				Log.d(TAG, "changeTextListener ok。。。。");
			} else if ((null != DB.getStr(CustConst.Err_SHOP_CODE) && !"".equals(DB.getStr(CustConst.Err_SHOP_CODE))) && (editable.length() > 0 && null != editable && !"".equals(editable))) {
				Log.d(TAG, "changeTextListener ok。。。。11");
				getBtnStatus(Util.NUM_ONE);
			} else {
				Log.d(TAG, "changeTextListener no");
				getBtnStatus(Util.NUM_ZERO);
			}
			
			DB.saveStr(CustConst.INPUT_ERROR_INFO, mInputErrorInfo);
		}
	};
	
	/**
	 * 点击默认的图片
	 */
	OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			Log.d(TAG, "position===="+position);
			if (position == Bimp.tempSelectBitmap.size()) {
				mLyPopup.startAnimation(AnimationUtils.loadAnimation(ImageUploadActivity.this, R.anim.activity_translate_in));
				mPop.showAtLocation(mParentView, Gravity.BOTTOM, 0, 0);
			} else {
				Intent intent = new Intent(ImageUploadActivity.this, GalleryActivity.class);
				intent.putExtra("position", "1");
				intent.putExtra("ID", position);
				startActivity(intent);
			}
		}
	};
	
	/**
	 * 弹出层的点击事件
	 */
	private OnClickListener popuListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.parent:
				mPop.dismiss();
				mLyPopup.clearAnimation();
				break;
			/*case R.id.btn_take_photo://拍照
				 photo();
				 mPop.dismiss();
				 mLyPopup.clearAnimation();
				break;*/
			case R.id.btn_pick_photo://从图库选择
				Intent intent = new Intent(ImageUploadActivity.this, AlbumActivity.class);
				startActivityForResult(intent, REQUEST_CODE);
				overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
				mPop.dismiss();
				mLyPopup.clearAnimation();
				break;
			case R.id.btn_cancel://取消
				mPop.dismiss();
				mLyPopup.clearAnimation();
				break;
			default:
				break;
			}
		}
	};
	
	protected void onRestart() {
		mAdapter.update();
		super.onRestart();
	}
	
	@Override
	protected void onResume() {
		JPushInterface.onResume(this);
		super.onResume();
		Log.d(TAG, "onResume()。。。。。。");
		mErrorInfoCode= DB.getStr(CustConst.Err_SHOP_CODE);
		Log.d(TAG, "onResume()  mErrorInfoCode=="+mErrorInfoCode);
		
		if (!Util.isEmpty(mErrorInfoCode)) {
			if (Integer.parseInt(mErrorInfoCode) == Util.NUM_ONE) {
				mTvErrorInfo.setText(R.string.err_info_one);
			} else if (Integer.parseInt(mErrorInfoCode) == Util.NUM_TWO) {
				mTvErrorInfo.setText(R.string.err_info_two);
			} else if (Integer.parseInt(mErrorInfoCode) == Util.NUM_THIRD) {
				mTvErrorInfo.setText(R.string.err_info_three);
			} else if (Integer.parseInt(mErrorInfoCode) == Util.NUM_FOUR) {
				mTvErrorInfo.setText(R.string.err_info_four);
			} else if (Integer.parseInt(mErrorInfoCode) == Util.NUM_FIVE) {
				mTvErrorInfo.setText(R.string.err_info_five);
			} else if (Integer.parseInt(mErrorInfoCode) == Util.NUM_SIX) {
				mTvErrorInfo.setText(R.string.err_info_sex);
			} 
 		}
		
		//保存输入的纠正信息
		mInputErroeInfo = DB.getStr(CustConst.INPUT_ERROR_INFO);
		mEtInputErrorInfo.setText(mInputErroeInfo);
		//输入错误信息的改变事件
		mEtInputErrorInfo.addTextChangedListener(changeTextListener);
		
	}
	
	/**
	 * 查询所有错误信息
	 */
	private void selectErrorInfo () {
	
		new ErrorInforTask(this, new ErrorInforTask.Callback() {
			
			@Override
			public void getResult(JSONArray result) {
				Log.d(TAG, "result=="+result);
				if (null == result) {
					return;
				}
				
				List<ErrorInfo> errorData = new ArrayList<ErrorInfo>();
				ErrorInfo item = null;
				for (int i = 0; i < result.size(); i++) {
					JSONObject errJsonObject = (JSONObject) result.get(i);
					item = Util.json2Obj(errJsonObject.toString(), ErrorInfo.class);
					errorData.add(item); 
				}
				
				mErrorAdapter = new ErrorInfoAdapter(ImageUploadActivity.this, errorData);
				mListView.setAdapter(mErrorAdapter);
			}
		}).execute();
	}
	
	/**
	 * 下拉的点击事件
	 */
	OnClickListener errorListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Log.d(TAG, "你点击了下拉。。。。。。");
			typePopWindow();
			selectErrorInfo ();
		}
	};
	
	/**
	 * 显示错误信息的popwindow
	 */
	private void typePopWindow() {
		mPopWindowView = View.inflate(this, R.layout.popwindow_errorinfo, null);
		mPopupWindow = new PopupWindow(mPopWindowView, LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		mListView = (ListView) mPopWindowView.findViewById(R.id.lv_1);
		mListView.setOnItemClickListener(selectErrorListener);
		mPopupWindow.setFocusable(true);  //能够焦点获得
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());  //设置背景
		mPopupWindow.setOutsideTouchable(true);  //外部点击
		mPopupWindow.showAsDropDown(mLyErrorLine);
	}
	   
	/**
	 * 点击选择了哪个错误信息
	 */
	OnItemClickListener selectErrorListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			errorInfo = (ErrorInfo) mListView.getItemAtPosition(position);
			mErrorInfoCode = errorInfo.getValue();
			Log.d(TAG, "mErrorInfoCode=="+mErrorInfoCode);
			mTvErrorInfo.setText(errorInfo.getInfo());
			DB.saveStr(CustConst.Err_SHOP_CODE, errorInfo.getValue());
			if((!"".equals(mInputErrorInfo) && null != mInputErrorInfo) || (Bimp.tempSelectBitmap.size() > 0 && null != Bimp.tempSelectBitmap)){
				Log.d(TAG, "aa。。。。。11");
				getBtnStatus(Util.NUM_ONE);
			} else {
				Log.d(TAG, "aa。。。。。22");
				getBtnStatus(Util.NUM_ZERO);
			}
			mPopupWindow.dismiss();
		}
	};
	
	/**
	 * 清除图片路径
	 */
	public static void clearImageUrl() {
		if (null != mImageFile || mImageFile.size() > 0) {
			mImageFile.clear();
			Log.d(TAG, "清除图片的路径 11==" + mImageFile.size());
		}
		if (null != mUpLoadIamge || mUpLoadIamge.size() > 0) {
			mUpLoadIamge.clear();
			Log.d(TAG, "清除图片的路径 22==" + mUpLoadIamge.size());
		}
	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			if (Bimp.tempSelectBitmap.size() == 3) {
				return 3;
			}
			return (Bimp.tempSelectBitmap.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.item_errorinfo_image);//图片
				holder.imageCancel = (TextView) convertView.findViewById(R.id.ly_image_cancel);//取消按钮
				
				if (Bimp.tempSelectBitmap.size() > 0) {
					holder.imageCancel.setVisibility(View.VISIBLE);
					//图片上的取消按钮
					holder.imageCancel.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Log.d(TAG, "你点击了删除按钮");
							if (mUpLoadIamge.size() == 1) {
								Bimp.tempSelectBitmap.clear();
								Bimp.max = 0;
								holder.image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused));
								holder.imageCancel.setVisibility(View.GONE);
							} else {
								Bimp.tempSelectBitmap.remove(selectedPosition + 1);
								Bimp.max--;
								mUpLoadIamge.remove(selectedPosition + 1);
								mAdapter.notifyDataSetChanged();
							}
							
							if (Bimp.tempSelectBitmap.size() > 0 && null != Bimp.tempSelectBitmap) {
								getBtnStatus(Util.NUM_ONE);
							} else {
								getBtnStatus(Util.NUM_ZERO);
							}
						}
					});
				} else {
					holder.imageCancel.setVisibility(View.GONE);
				}
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			if (position == Bimp.tempSelectBitmap.size()) {
				holder.imageCancel.setVisibility(View.GONE);
				holder.image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 3) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
				mImageLoadUrl = Bimp.tempSelectBitmap.get(position).getImagePath();
				mImageFile.add(Bimp.tempSelectBitmap.get(position).getImagePath());
				for (int i = (mImageFile.size() - 1); i >= 0; i--) {
					double len = 0;
					if (mImageFile.size() >= 3) {
						len = Calculate.suBtraction((mImageFile.size() - 1),3);
					} else {
						len = Calculate.suBtraction((mImageFile.size() - 1), (getCount() - 1));
					}
					if (i > len) {
						String filepath = mImageFile.get(i);
						// 判断是否有重复的
						if (mUpLoadIamge.contains(filepath) == false) {
							mUpLoadIamge.add(filepath);
							Log.d(TAG, "imageurlhhh1212=" + filepath);
						}
						
					}
				}
			}
			
			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
			public TextView imageCancel;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					mAdapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.tempSelectBitmap.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	private static final int TAKE_PICTURE = 1;

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	/**
	 * 得到的结果
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult====");
		Log.d(TAG, "tempSelectBitmap===="+Bimp.tempSelectBitmap.size());
		Log.d(TAG, "resultCode == RESULT_OK===="+(resultCode == RESULT_OK));
		Log.d(TAG, "requestCode"+requestCode);
		
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.tempSelectBitmap.size() < 3 && resultCode == RESULT_OK) {

				String fileName = String.valueOf(System.currentTimeMillis());
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				FileUtils.saveBitmap(bm, fileName);

				ImageItem takePhoto = new ImageItem();
				takePhoto.setBitmap(bm);
				Bimp.tempSelectBitmap.add(takePhoto);
				
			}
			break;
		}
		
		/*if (requestCode==REQUEST_CODE && Bimp.tempSelectBitmap.size() <= 3 && resultCode == RESULT_OK) {
			Log.d(TAG, "onActivityResult======11");
			
			 * //拍照的路径 mImagePath = takePhoto(MainActivity.this,resultCode,
			 * data); Log.d(TAG, "mImagePath"+mImagePath);
			 * listFiles.add(mImagePath); for (int i = 0; i <
			 * listFiles.size(); i++) { Log.d(TAG,
			 * "循环的图片========="+listFiles.get(i)); //上传图片
			 * Util.getImageUpload(MainActivity.this, mImagePath); }
			 
			
			String fileName = String.valueOf(System.currentTimeMillis());
			Bitmap bm = (Bitmap) data.getExtras().get("data");
			String mPicPath = getFilePath(ImageUploadActivity.this) + "/" + System.currentTimeMillis() + ".jpg";
			Log.d(TAG, "mPicPath" + mPicPath);
			FileUtils.saveBitmap(bm, fileName);
			Log.d(TAG, "图片fileName" + fileName);
			ImageItem takePhoto = new ImageItem();
			takePhoto.setBitmap(bm);
			Bimp.tempSelectBitmap.add(takePhoto);
			Log.d(TAG, "图片" + Bimp.tempSelectBitmap);
			
			if(!Util.isEmpty(DB.getStr(CustConst.Err_SHOP_CODE))){
				Log.d(TAG, "onActivityResult======22");
				mClickFlag = true;
			}
			
		}*/
		
	}

	/**
	 * 拍照
	 * @param activity
	 * @param resultCode
	 * @param data
	 * @return
	 */
	public static String takePhoto(Activity activity, int resultCode, Intent data) {
		String fileName = "";
		if (resultCode == Activity.RESULT_OK) {
			if (Storage.hasSDCard())// 检查系统是否有SD卡
			{
				new DateFormat();
				String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
				Bundle bundle = data.getExtras();
				Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
				// 文件输出流
				FileOutputStream fileout = null;
				// 判断文件是否为空或者名称重复()
				fileName = Storage.getExtDir() + "/" + name;
				try {
					fileout = new FileOutputStream(fileName);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileout);// 把数据写入文件

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					try {
						fileout.flush();
						fileout.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return fileName;
	}

	private String getFilePath(Context context) {
		File f = context.getExternalFilesDir(null);
		if (f == null) {
			f = Environment.getExternalStorageDirectory();
			if (f == null) {
				f = context.getFilesDir();
			} else {
				f = new File(f.getAbsolutePath() + "/suanzi/");
				f.mkdirs();
			}
		}
		return f == null ? null : f.getAbsolutePath();
	}

	/**
	 * 用户给平台提供商家错误信息
	 */
	private void errorInfo (String moreImagePath) {
		
		new AddErrorInformationTask(this, new AddErrorInformationTask.Callback() {
			
			@Override
			public void getResult(JSONObject result) {
				Log.d(TAG, "AddErrorInformationTask 进来了");
				
				if (null == result) {
					return;
				}
				
				if (Integer.parseInt(result.get("code").toString()) == ErrorCode.SUCC) {
					Util.getContentValidate(R.string.err_info_succ);
					Util.exitLogin();
					Log.d(TAG, "chengongla----");
					
					if (mProcessDialog != null) {
						mProcessDialog.dismiss();
					}
					
					DB.deleteKey(CustConst.Err_SHOP_CODE);
					DB.deleteKey(CustConst.INPUT_ERROR_INFO);
					Bimp.tempSelectBitmap.clear();//默认进来清除图片
					
				} else {
					Util.getContentValidate(R.string.err_info_fail);
				}
				
			}
		}).execute(mErrorInfoCode,moreImagePath,mToShopCode,mInputErrorInfo);
	}
		
	/**
	 * 判断提交的按钮状态
	 * @param status
	 */
	private void getBtnStatus(int status) {
		int isNull = Util.NUM_ZERO;
		Log.d(TAG,"status=="+status);
		if (isNull == status) {
			mBtnRrrorCommit.setEnabled(false);
			mBtnRrrorCommit.setTextColor(getResources().getColor(R.color.coupon_fontgrey));
			mBtnRrrorCommit.setBackgroundColor(Color.GRAY);
		} else {
			mBtnRrrorCommit.setEnabled(true);
			mBtnRrrorCommit.setTextColor(getResources().getColor(R.color.white));
			mBtnRrrorCommit.setBackgroundResource(R.drawable.login_btn);
		}
	}
	
	/**
	 * 点击提交
	 * @param view
	 */
	@OnClick(R.id.btn_commit)
	public void btnCommit(View view) {
		switch (view.getId()) {
		case R.id.btn_commit:
			if(Util.isEmpty(DB.getStr(CustConst.Err_SHOP_CODE))){
				Util.getContentValidate(R.string.url_data);
				return;
			}
			
			mProcessDialog = new ProgressDialog(this);
			mProcessDialog.setCancelable(false);
			mProcessDialog.setMessage(Util.getString(R.string.toast_uploading));
			mProcessDialog.show();
			
			Util.getMultiImageUpLoad(this,mUpLoadIamge, new Util.onUploadFinish() {
				
				@Override
				public void getImgUrl(String multiImgPath) {
					
					Log.d(TAG, "上传多张图片以后的路径集合===="+multiImgPath); 
					
					Message msg = Message.obtain();
					msg.what = 0;
					msg.obj = multiImgPath;
					mHandler.sendMessage(msg);
					
				}
			});
			break;
		default:
			break;
		}
	}
	
	/**
	 * 重写返回按钮
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			for (int i = 0; i < PublicWay.activityList.size(); i++) {
				if (null != PublicWay.activityList.get(i)) {
					FinishActivityUtils.exit();
					DB.deleteKey(CustConst.Err_SHOP_CODE);//清除错误的编码
					Bimp.tempSelectBitmap.clear();//进来清除图片
					DB.deleteKey(CustConst.INPUT_ERROR_INFO);//清除错误的信息
				}
			}
		}
		return true;
	}
	
	/**
	 * 点击返回按钮
	 */
	@OnClick(R.id.iv_turn_in)
	public void btnActAddBackClick(View view) {
		this.finish();
		FinishActivityUtils.exit();
		DB.deleteKey(CustConst.Err_SHOP_CODE);//清除错误的编码
		Bimp.tempSelectBitmap.clear();//进来清除图片
		DB.deleteKey(CustConst.INPUT_ERROR_INFO);//清除错误信息
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "onStop()。。。。。。。。   ");
		super.onStop();
		mHandler.removeCallbacksAndMessages(null);
	}
	
	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}
}
