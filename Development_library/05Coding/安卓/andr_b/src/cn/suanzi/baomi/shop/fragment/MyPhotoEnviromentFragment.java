package cn.suanzi.baomi.shop.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.api.Tools;
import cn.suanzi.baomi.base.pojo.Decoration;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.activity.EnvironMentBigPhotoActivity;
import cn.suanzi.baomi.shop.activity.UploadEnvironmentPhotoActivity;
import cn.suanzi.baomi.shop.adapter.EnvironmentPhotoAdapter;
import cn.suanzi.baomi.shop.model.GetShopDecorationListTask;

import com.umeng.analytics.MobclickAgent;

/**
 * 商家相册 (环境)
 * @author qian.zhou
 */
public class MyPhotoEnviromentFragment extends Fragment{
	private static final String TAG = "MyPhotoEnviromentFragment";
	/** startActivityForResult()的requestCode: 上传环境图片 */
	public static final int INTENT_REQ_ADD_INFO = 1001;
	/** startActivityForResult()的requestCode: 删除产品图片的某一张图片信息 */
	public static final int DEL_PHOTO_INFO = 1002;
	/** setResult()用的，如果确实删除了信息 */
	public final static int DEL_RESP_PHOTO = 2;
	/** 从图库选择照片*/
	private final static int SELECT_PIC = 0;
	/** 用listview显示所有产品的具体信息 **/
	private GridView mGvProduct;
	/**判断Fragment的状态**/
	static MyPhotoEnviromentFragment mInstance = null;
	/** 相册列表适配器*/
	private EnvironmentPhotoAdapter mEPhotoAdapter;
	private List<Decoration> mDList;
	/** 没有相片数据*/
	private TextView mTvNoData;
	/**上传*/
	private TextView mTvUploadPhoto;
	/**添加子相册*/
	private TextView mTvAddPhoto;
	/** 正在加载数据*/
	private LinearLayout mLyNodate;
	/** 正在加载的内容*/
	private RelativeLayout mLyContent;
	
	/**
	 * 需要传递参数时有利于解耦
	 * @return MyStaffFragment
	 */
	public static MyPhotoEnviromentFragment newInstance() {
		if (mInstance == null) {
			Bundle args = new Bundle();
			mInstance = new MyPhotoEnviromentFragment();
			mInstance.setArguments(args);
		}
		return mInstance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_photo_enviroment, container, false);
		init(view);
		Util.addLoginActivity(getActivity());
		return view;
	}

	// 初始化方法
	private void init(View v) {
		mGvProduct = (GridView) v.findViewById(R.id.gv_photo_environment);
		mTvNoData = (TextView) v.findViewById(R.id.tv_nodata);
		mTvUploadPhoto = (TextView) v.findViewById(R.id.tv_upload_photo);
		mLyNodate = (LinearLayout) v.findViewById(R.id.ly_nodate);
		mLyContent = (RelativeLayout) v.findViewById(R.id.ry_content);
		mTvUploadPhoto.setOnClickListener(upLoadListener);
		//添加相册
		mTvAddPhoto = (TextView) getActivity().findViewById(R.id.tv_msg);
		mTvAddPhoto.setVisibility(View.GONE);
		//GridView的点击事件
		mGvProduct.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> v, View view, int position, long arg3) {
				Decoration decoration = (Decoration) mGvProduct.getItemAtPosition(position);
				Intent intent = new Intent(getActivity(), EnvironMentBigPhotoActivity.class);
				intent.putExtra(EnvironMentBigPhotoActivity.DECORATION, decoration);
				intent.putExtra(EnvironMentBigPhotoActivity.IMAGE_LIST, (Serializable)mDList);
				intent.putExtra(EnvironMentBigPhotoActivity.IMAG_INDEX, (position % mDList.size()));
				startActivityForResult(intent, DEL_PHOTO_INFO);
			}
		});
		setData(0); // 正在加载数据
		//调用产品子相册的列表
		getPhotoEnviroList();
	}
	
	/**
	 * 设置数据
	 * @param type 有没有数据 1 是有数据 0 是没有数据
	 */
	private void setData (int type) {
		if (type == 1) {
			mLyNodate.setVisibility(View.GONE);
			mLyContent.setVisibility(View.VISIBLE);
		} else {
			mLyNodate.setVisibility(View.VISIBLE);
			mLyContent.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 调用系统图库
	 */
	OnClickListener upLoadListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			//友盟统计
			MobclickAgent.onEvent(getActivity(), "shop_env_uploadphoto");
			try {
				Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				if (i.resolveActivity(getActivity().getPackageManager()) != null) {
					startActivityForResult(i, SELECT_PIC);
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.d(TAG, "调用图库失败................");
			}
		}
	};
	
	/**
	 * 给图片命名
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
	
	/**
	 * 上传图片
	 * @param bitmap
	 */
	private void updateHead(Bitmap bitmap) {
		try {
			if (bitmap == null) { return; }
			String mPicPath = getFilePath(getActivity()) + "/" + System.currentTimeMillis() + ".jpg";
			Tools.savBitmapToJpg(bitmap, mPicPath);
			Intent intent1 = new Intent(getActivity(), UploadEnvironmentPhotoActivity.class);
			intent1.putExtra(UploadEnvironmentPhotoActivity.IMAGE_URL, mPicPath);
			intent1.putExtra(UploadEnvironmentPhotoActivity.FLAG, "one");
			startActivityForResult(intent1, INTENT_REQ_ADD_INFO);
		} catch (Exception e) {
			// TODO: handle exception
			Log.d(TAG, "上传图片失败............." + e.getMessage());
		}
	}
	
	/**
	 * 拍照和调用图库时要执行的方法
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	   if (requestCode == SELECT_PIC) {
		   if (resultCode == Activity.RESULT_OK) {
			   if (intent == null) {
                   return;
               }
			   try {
			    Uri uri = intent.getData();
				Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
				updateHead(bitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		   }
	   } else if (requestCode == DEL_PHOTO_INFO) {
		  if (resultCode == MyPhotoEnviromentFragment.DEL_RESP_PHOTO) {
			  getPhotoEnviroList();
		  }
	   }
	};
	       
	/**
	 * 查询环境照片列表
	 */
	public void getPhotoEnviroList(){
		new GetShopDecorationListTask(getActivity(), new GetShopDecorationListTask.Callback() {
			@Override
			public void getResult(JSONArray jsonArray) {
				if (jsonArray == null ) {
					Log.d(TAG, "");
					return;
				} else {
					setData(1); // 有数据
					if (jsonArray.size() == 0) {
						mTvNoData.setVisibility(View.VISIBLE);
						mGvProduct.setVisibility(View.GONE);
					} else {
						mTvNoData.setVisibility(View.GONE);
						mGvProduct.setVisibility(View.VISIBLE);
						mDList = new ArrayList<Decoration>();
						for (int i = 0; i < jsonArray.size(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArray.get(i);
							Decoration decoration = Util.json2Obj(jsonObject.toString(), Decoration.class);
							mDList.add(decoration);
						}
						mEPhotoAdapter = new EnvironmentPhotoAdapter(getActivity(), mDList);
						mGvProduct.setAdapter(mEPhotoAdapter);
					}
				}
			}
		}).execute();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MainScreen"); //统计页面
		getPhotoEnviroList();
	}
	
	/**
	 * 友盟统计
	 */
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainScreen"); 
	}
}
