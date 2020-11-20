package cn.suanzi.baomi.shop.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.Storage;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.Calculate;
import cn.suanzi.baomi.base.utils.FinishActivityUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.util.Bimp;
import cn.suanzi.baomi.shop.util.FileUtils;
import cn.suanzi.baomi.shop.util.ImageItem;
import cn.suanzi.baomi.shop.util.PublicWay;
import cn.suanzi.baomi.shop.util.Res;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 照片的首页(活动)
 * @author wensi.yu
 */
public class ImageUploadActivity extends Activity {

	private final static String TAG = "ImageUploadActivity";
 
	public final static String IMAGE_URl = "iamgeUrl";
	public final static int IMAGE_SUCC = 101;
	private final static int REQUEST_CODE = 100;

	/** 返回图片 **/
	@ViewInject(R.id.layout_turn_in)
	private LinearLayout mIvBackup;
	/** 功能描述文本 **/
	@ViewInject(R.id.tv_mid_content)
	private TextView mTvdesc;
	/** 上一步 **/
	@ViewInject(R.id.btn_actaddexplain_laststep)
	private Button mBtnActAddLastStep;
	/** 预览 **/
	@ViewInject(R.id.btn_actaddexplain_nextstep)
	private Button mBtnActAddNextStep;

	private GridView mNoScrollgridview;
	private GridAdapter mAdapter;
	private View mParentView;
	private PopupWindow mPop = null;
	private LinearLayout mLyPopup;
	public static Bitmap mBimap;
	private String mImagePath;
	private ArrayList<String> mListFiles;
	/** 得到图片的路径 */
	private static ArrayList<String> mImageFile;
	/** 处理过的路径 */
	private static ArrayList<String> mUpLoadIamge;
	private String mImageLoadUrl;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Res.init(this);
		mBimap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused);
		PublicWay.activityList.add(this);
		mParentView = getLayoutInflater().inflate(R.layout.activity_selectimg, null);
		ViewUtils.inject(this, mParentView);
		setContentView(mParentView);
		FinishActivityUtils.addActivity(ImageUploadActivity.this);
		Util.addActivity(ImageUploadActivity.this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
		init();
	}

	public void init() {
		Util.addLoginActivity(ImageUploadActivity.this);
		mListFiles = new ArrayList<String>();
		mImageFile = new ArrayList<String>();
		mUpLoadIamge = new ArrayList<String>();
		if (mUpLoadIamge.size() > 0) {
			mUpLoadIamge.clear();
		}

		if (mImageFile.size() > 0) {
			mImageFile.clear();
		}

		// 设置标题
		mIvBackup.setVisibility(View.VISIBLE);
		mTvdesc.setText(R.string.tv_actlist_title);
		mBtnActAddNextStep.setText(R.string.tv_actlist_select);

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
		// Button bt1 = (Button) view.findViewById(R.id.btn_take_photo);
		Button bt2 = (Button) view.findViewById(R.id.btn_pick_photo);
		Button bt3 = (Button) view.findViewById(R.id.btn_cancel);
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPop.dismiss();
				mLyPopup.clearAnimation();
			}
		});
		// 拍照
		/*
		 * bt1.setOnClickListener(new OnClickListener() { public void
		 * onClick(View v) { photo(); pop.dismiss(); ll_popup.clearAnimation();
		 * } });
		 */
		// 从相册选择
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ImageUploadActivity.this, AlbumActivity.class);
				startActivityForResult(intent, REQUEST_CODE);
				overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
				mPop.dismiss();
				mLyPopup.clearAnimation();
			}
		});
		// 取消
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mPop.dismiss();
				mLyPopup.clearAnimation();
			}
		});

		mNoScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		mNoScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mAdapter = new GridAdapter(this);
		mAdapter.update();
		mNoScrollgridview.setAdapter(mAdapter);
		mNoScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == Bimp.tempSelectBitmap.size()) {
					mLyPopup.startAnimation(AnimationUtils.loadAnimation(ImageUploadActivity.this, R.anim.activity_translate_in));
					mPop.showAtLocation(mParentView, Gravity.BOTTOM, 0, 0);
				} else {
					Intent intent = new Intent(ImageUploadActivity.this, GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
	}

	/**
	 * 清楚图片路径
	 */
	public static void clearImageUrl() {
		if (null != mImageFile || mImageFile.size() > 0) {
			mImageFile.clear();
			Log.d(TAG, "imageurlhhh1212mImageFile=" + mImageFile.size());
		}
		if (null != mUpLoadIamge || mUpLoadIamge.size() > 0) {
			mUpLoadIamge.clear();
			Log.d(TAG, "aaaaImage=" + mUpLoadIamge.size());
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
			if (Bimp.tempSelectBitmap.size() == 6) {
				return 6;
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
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (position == Bimp.tempSelectBitmap.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 6) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
				mImageLoadUrl = Bimp.tempSelectBitmap.get(position).getImagePath();
				mImageFile.add(Bimp.tempSelectBitmap.get(position).getImagePath());
				for (int i = (mImageFile.size() - 1); i >= 0; i--) {
					double len = 0;
					if (mImageFile.size() >= 6) {
						len = Calculate.suBtraction((mImageFile.size() - 1), 6);
					} else {
						len = Calculate.suBtraction((mImageFile.size() - 1), (getCount() - 1));
					}
					if (i > len) {
						Log.d(TAG, "mUpLoadIamge=" + mUpLoadIamge.size());
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

	protected void onRestart() {
		mAdapter.update();
		super.onRestart();
	}

	private static final int TAKE_PICTURE = 0x000001;

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	/**
	 * 拍照和调用图库时要执行的方法
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.tempSelectBitmap.size() < 6 && resultCode == RESULT_OK) {
				/*
				 * //拍照的路径 mImagePath = takePhoto(MainActivity.this,resultCode,
				 * data); Log.d(TAG, "mImagePath"+mImagePath);
				 * listFiles.add(mImagePath); for (int i = 0; i <
				 * listFiles.size(); i++) { Log.d(TAG,
				 * "循环的图片========="+listFiles.get(i)); //上传图片
				 * Util.getImageUpload(MainActivity.this, mImagePath); }
				 */

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
			}
			break;
		/*
		 * case REQUEST_CODE: // 返回结果 if (resultCode == IMAGE_SUCC) { try {
		 * mImageFile = (List<String>) data.getSerializableExtra(IMAGE_URl); for
		 * (int i = 0; i < mImageFile.size(); i++) { String iamgeUrl =
		 * mImageFile.get(i); Log.d(TAG, "iamgeUrlhhh="+iamgeUrl); } } catch
		 * (Exception e) { Log.e(TAG, "没有图片="+e.getMessage());//TODO }
		 * 
		 * } break;
		 */
		}
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

	/**
	 * 给拍照或者从图库选择的图片命名
	 * @param context
	 * @return
	 */
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

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			for (int i = 0; i < PublicWay.activityList.size(); i++) {
				if (null != PublicWay.activityList.get(i)) {
					FinishActivityUtils.exit();
				}
			}
		}
		return true;
	}

	/**
	 * 点击预览按钮
	 * 
	 * @param view
	 */
	@OnClick(R.id.btn_actaddexplain_nextstep)
	public void btnActAddNextStepClick(View view) {
		switch (view.getId()) {
		case R.id.btn_actaddexplain_nextstep:
			Log.d(TAG, "mUpLoadIamge===" + mUpLoadIamge); // 修改之后的照片
			if (mUpLoadIamge.size() <= 0) {
				Util.getContentValidate(R.string.url_nothing);
				break;
			}

			for (int i = 0; i < mUpLoadIamge.size(); i++) {
				Log.d(TAG, "mUpLoadIamgess===" + mUpLoadIamge.size());
				String imageLoad = mUpLoadIamge.get(i);
				Log.d(TAG, "上传的路径========" + imageLoad);
				Util.getImageUpload(ImageUploadActivity.this, imageLoad);
			}

			Intent intent = new Intent(this, ActAddSelectDetailActivity.class);
			Bundle bundle = new Bundle();
			// bundle.putStringArrayList("Files", listFiles);--拍照
			bundle.putStringArrayList("mUpLoadIamge", mUpLoadIamge);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 点击返回按钮
	 * 
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	public void btnActAddBackClick(View view) {
		this.finish();
		FinishActivityUtils.exit();
	}

	/**
	 * 点击上一步按钮
	 * 
	 * @param view
	 */
	@OnClick(R.id.btn_actaddexplain_laststep)
	public void btnActAddLastStepClick(View view) {
		finish();
		FinishActivityUtils.exit();
	}

	@Override
	protected void onResume() {
		JPushInterface.onResume(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}
}
