package com.huift.hfq.cust.fragment;

import java.io.File;
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
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.huift.hfq.base.Const;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.Util.onUploadFinish;
import com.huift.hfq.base.adapter.CommenViewHolder;
import com.huift.hfq.base.adapter.CommonListViewAdapter;
import com.huift.hfq.base.api.Tools;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.Citys;
import com.huift.hfq.base.pojo.User;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.GlideCircleTransform;
import com.huift.hfq.base.utils.UploadUtils;
import com.huift.hfq.cust.R;

import com.bumptech.glide.Glide;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.model.GetUserInfoTask;
import com.huift.hfq.cust.model.GetZhejiangCityTask;
import com.huift.hfq.cust.model.MyInfoMationUppTask;
import com.huift.hfq.cust.model.UpdateUserHeadTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 个人信息
 * @author qian.zhou
 */
public class MyInfoMationFragment extends Fragment {
	private final static String TAG = "MyInfoMationFragment";
	public final String USER_OBJ = "user";
	/** 显示的图片 */
	private ImageView mIvaddimage;
	/** PopupWindow是一个容器 **/
	private PopupWindow mPopupWindow;
	/** 选择视图的索引*/
	private int mClickPicId;
	public static Activity mActivity;
	/** 线程*/
	private Handler mHandler;
	/** 全局视图*/
	private View mView;
	/** 头像的图片路径*/
	private String mTmpPic;
	/** 提交 */
	private TextView mTvSubmit;
	/** 性别*/
	private TextView mTvSex;
	/** 地区*/
	private TextView mTvArea;
	/** 城市的数据源 */
	private List<Citys> mCityDates;
	/** 性别*/
	private String mSex;
	/** 裁剪图片*/
	private final static int CROP_PIC = 1;
	/** 从图库选择图片*/
	private final static int SELECT_PIC = 0;
	/** 拍照*/
	private final static int TAKE_PIC = 2;
	private int mCrop = 300;// 裁剪大小
	private File mTemp;
	/** 保存用户信息的对象*/
	private User mUser;
	/** 用户昵称*/
	private  EditText mEtNickName;
	/** 用户真实名称*/
	private EditText mEtName;
	/** 个性签名*/
	private EditText mEtSignature;
	/** 用户手机号码*/
	private TextView mTvPhoneNum;
	/** 昵称*/
	private String mNickName;
	/** 性别*/
	private String mUserSex = "U";
	 
	/**
	 * 传递参数有利于解耦
	 * @return
	 */
	public static MyInfoMationFragment newInstance() {
		Bundle args = new Bundle();
		MyInfoMationFragment fragment = new MyInfoMationFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_myinformation, container, false);
		ViewUtils.inject(this, mView);
		Util.addActivity(getMyActivity());
		mActivity = getMyActivity();
		init(mView);
		Util.addLoginActivity(getMyActivity());
		return mView;
	}

	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	private void init(View view) {
		mCityDates = new ArrayList<Citys>();
		TextView tvcontent = (TextView) view.findViewById(R.id.tv_mid_content);
		tvcontent.setText(getResources().getString(R.string.person_information));
		TextView tvSubmit = (TextView) view.findViewById(R.id.tv_add);
		tvSubmit.setText(getResources().getString(R.string.person_submit));
		mTvSubmit = (TextView) mView.findViewById(R.id.tv_add);//完成
		mTvSubmit.setText(getResources().getString(R.string.person_submit));
		// 修改信息
		if (mClickPicId != 0) { 
			return; 
		}
		mHandler = new Handler();
		mIvaddimage = (ImageView) mView.findViewById(R.id.iv_myshopinfo_logo);// 头像
		mIvaddimage.setOnClickListener(ivAddImageTouch);
		mEtNickName = (EditText) mView.findViewById(R.id.et_myinfomation_nickname);// 昵称
		mEtName = (EditText) mView.findViewById(R.id.et_myinfomation_name);// 姓名
		mTvSex = (TextView) mView.findViewById(R.id.tv_myinfomation_sex);// 性别
		mTvArea = (TextView) mView.findViewById(R.id.tv_myinfomation_area);// 地区
		mEtSignature = (EditText) mView.findViewById(R.id.et_myinfomation_signature);// 个性签名
		mTvPhoneNum = (TextView) mView.findViewById(R.id.tv_myinfomation_phone_number);// 手机号码
		// 选择性别
		mTvSex.setOnClickListener(sexListener);
		// 地区
		mTvArea.setOnClickListener(areaListener);
		//编辑
		mTvSubmit.setOnClickListener(submitListener);
		//给控件赋值
		initData();
	}
	
	/**
	 * 编辑的点击事件
	 */
	OnClickListener submitListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			MobclickAgent.onEvent(getMyActivity(), "myinfomation_edit");
			TextView tvBtn = (TextView) v;
			String editText = Util.getString(R.string.person_submit);
			if (editText.equals(tvBtn.getText())) {//编辑状态
				mEtNickName.setEnabled(true);
				mEtName.setEnabled(true);
				mTvSex.setEnabled(true);
				mTvArea.setEnabled(true);
				mEtSignature.setEnabled(true);
				mTvPhoneNum.setEnabled(false);
				mTvSubmit.setText(Util.getString(R.string.submit_message));
			} else { // "提交"状态
				//检查网络
				if (Util.isNetworkOpen(getMyActivity()) && null != mUser) {
					switch (v.getId()) {
					case R.id.tv_add:
						if (mEtNickName.getText().toString().length() >= 10) {
							Util.getContentValidate(R.string.nickname_tolong);
						} else {
							mNickName = Util.isEmpty(mEtNickName.getText().toString()) ? "" : mEtNickName.getText().toString();
							// 手机号码
							String mobileNbr = mUser.getMobileNbr();
							//真实姓名
							String realName = Util.isEmpty(mEtName.getText().toString()) ? "" : mEtName.getText().toString(); 
							//城市
							String city = Util.isEmpty(mTvArea.getText().toString()) ? "" : mTvArea.getText().toString();
							//个性签名
							String signature = Util.isEmpty(mEtSignature.getText().toString()) ? "" : mEtSignature.getText().toString();
							//性别
							String sex = Util.isEmpty(mUserSex) ? "" : mUserSex;
							//图片路径
							if (Util.isEmpty(mTmpPic)) {
								mTmpPic = mUser == null ? "" : mUser.getAvatarUrl();
							}
							mTvSubmit.setEnabled(false);
							String[] params = { mobileNbr, realName, mNickName, city, signature, sex, mTmpPic};
							//修改个人信息
							myInfoMationUpp(params);
						}
						break;
					default:
						break;
					}
				} else {
					Util.getToastBottom(getMyActivity(), "没有网络了");
				}
			}
		}
	};
	
	/**
	 * 修改个人信息
	 */
	public void myInfoMationUpp(String[] params){
		new MyInfoMationUppTask(getMyActivity(), new MyInfoMationUppTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (String.valueOf(ErrorCode.SUCC).equals(result.get("code").toString())) {
					DB.saveBoolean(CustConst.Key.UPP_USERINFO,true);
					mEtNickName.setEnabled(false);
					mEtName.setEnabled(false);
					mTvSex.setEnabled(false);
					mTvArea.setEnabled(false);
					mEtSignature.setEnabled(false);
					mTvPhoneNum.setEnabled(false);
					mTvSubmit.setEnabled(true);
				} else {
					mTvSubmit.setEnabled(true);
					Util.getContentValidate(R.string.upp_fail);
				}
			}
		}).execute(params);
	}
	
	/**
	 * 查询个人信息
	 */
	public void getUserInfo() {
		Activity act = getMyActivity();
		if (act == null) {
			return;
		}
		new GetUserInfoTask(act, new GetUserInfoTask.Callback() {
			@Override
			public void getResult(JSONObject object) {
				if (object == null) {
					return;
				} else {
					try {
						mUser = Util.json2Obj(object.toString(), User.class);          
					} catch (Exception e) {
						e.printStackTrace();
					}  
				}
			}
		}).execute();
	}
	
	/**
	 *设置数据
	 */
	public void initData(){
		Intent intent = getMyActivity().getIntent();
		//判断是否联网
		if (Util.isNetworkOpen(getMyActivity())) {
			getZhejiangCity();
			mUser = (User) intent.getSerializableExtra(USER_OBJ);
			Log.d(TAG, "nicakname>>>" + mUser.getNickName());
			if (mUser != null) {
				Log.d(TAG, "图片路径为：：" + mUser.getAvatarUrl());
				if (Util.isEmpty(mUser.getAvatarUrl())) {
					mTmpPic = "";
				} else {
					mTmpPic = !Util.isEmpty(mUser.getAvatarUrl()) ? mUser.getAvatarUrl() : "";
				}
				//头像
				Glide.with(getMyActivity()).load(Const.IMG_URL + mTmpPic).centerCrop().transform(new GlideCircleTransform(getMyActivity())).into(mIvaddimage);
				//昵称
				String nickname = !Util.isEmpty(mUser.getNickName()) ? mUser.getNickName() : "";
				mEtNickName.setText(nickname);
				mEtNickName.setSelection(nickname.length());
				//姓名
				String realname = !Util.isEmpty(mUser.getRealName()) ? mUser.getRealName() : "";
				mEtName.setText(realname);
				mEtName.setSelection(realname.length());
				//性别
				mSex = !Util.isEmpty(mUser.getSex()) ? mUser.getSex() : "";
				if ("M".equals(mSex)) {
					mTvSex.setText("男");
					
				} else if ("F".equals(mSex)) {
					
					mTvSex.setText("女");
				} else {
					
					mTvSex.setText("未填");
				}
				//个性签名
				String signature = !Util.isEmpty(mUser.getSignature()) ? mUser.getSignature() : "";
				mEtSignature.setText(signature);
				mEtSignature.setSelection(signature.length());
				//手机号码
				String mobileNbr = !Util.isEmpty(mUser.getMobileNbr()) ? mUser.getMobileNbr() : "";
				String strBegin = mobileNbr.substring(0, 3);
				String strEnd = mobileNbr.substring(7, 11);
				mTvPhoneNum.setText(strBegin + "****" + strEnd);
				//地区
				mTvArea.setText(!Util.isEmpty(mUser.getCity()) ? mUser.getCity() : "");
			} else {
				getUserInfo();
			}
		} else {
			Util.getToastBottom(getMyActivity(), "请连接网络");
			mUser = DB.getObj(DB.Key.CUST_USER, User.class);
			if (mUser != null) {
				if (Util.isEmpty(mUser.getAvatarUrl())) {
					mTmpPic = "";
				} else {
					if (!Util.isEmpty(mUser.getAvatarUrl())) {
						
						mTmpPic = mUser.getAvatarUrl();
					} else {
						//
						mTmpPic = "";
					}
				}
				//头像
				Glide.with(getMyActivity()).load(Const.IMG_URL + mTmpPic).centerCrop().transform(new GlideCircleTransform(getMyActivity())).into(mIvaddimage);
				//昵称
				String nickname = !Util.isEmpty(mUser.getNickName()) ? mUser.getNickName() : "";
				mEtNickName.setText(nickname);
				mEtNickName.setSelection(nickname.length());
				//姓名
				String realname = !Util.isEmpty(mUser.getRealName()) ? mUser.getRealName() : "";
				mEtName.setText(realname);
				mEtName.setSelection(realname.length());
				//性别
				mSex = !Util.isEmpty(mUser.getSex()) ? mUser.getSex() : "";
				mTvSex.setText(mSex);
				//个性签名
				String signature = !Util.isEmpty(mUser.getSignature()) ? mUser.getSignature() : "";
				mEtSignature.setText(signature);
				mEtSignature.setSelection(signature.length());
				//手机号码
				String mobileNbr = !Util.isEmpty(mUser.getMobileNbr()) ? mUser.getMobileNbr() : "";
				String strBegin = mobileNbr.substring(0, 3);
				String strEnd = mobileNbr.substring(7, 11);
				mTvPhoneNum.setText(strBegin + "****" + strEnd);
				//地区
				mTvArea.setText(!Util.isEmpty(mUser.getCity()) ? mUser.getCity() : "");
			}
		}
	}
	
	/**
	 * 拍照后裁剪图片
	 * @param uri 图片资源
	 */
	private void cropPic(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("output", Uri.fromFile(mTemp));
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", mCrop);
        intent.putExtra("outputY", mCrop);
        startActivityForResult(intent, CROP_PIC);
    }
	
	/**
	 * 从图库选择图片并裁剪
	 * @param uri 图片资源
	 */
	private void cropPic1(Uri uri) {
		if (uri == null) {
			Log.e(TAG, "uri is null , return ");
			return;
		}
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", true);
		intent.putExtra("aspectX", 1);  
        intent.putExtra("aspectY", 1);  
		intent.putExtra("outputX", 340);
		intent.putExtra("outputY", 340);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CROP_PIC);
	}
	
	// 手机号码
	OnClickListener phoneListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Util.getContentValidate(R.string.nonsupport);
		}
	};

	/**
	 * 弹出拍照图库
	 */
	OnClickListener ivAddImageTouch = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mClickPicId = v.getId();
			View view = LayoutInflater.from(getMyActivity()).inflate(R.layout.select_pic, null);
			//view.setBackgroundColor(808080);
			// 设置mPopupWindow的宽高
			mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			// 获得焦点，点击mPopupWindow以外的地方，窗体消失
			mPopupWindow.setFocusable(true);
			mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btncardset));
			mPopupWindow.setOutsideTouchable(true);
			// 设置mPopupWindow的显示位置
			mPopupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
			//Button btnPhoto = (Button) view.findViewById(R.id.btn_take_photo);// 拍照
			Button btnImage = (Button) view.findViewById(R.id.btn_pick_photo);// 图库
			Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);// 取消
			// 点击拍照按钮
//			btnPhoto.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
//                    //下面这句指定调用相机拍照后的照片存储的路径 
//                    intent.putExtra("output", Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "xiaoma.jpg"))); 
//					startActivityForResult(intent, TAKE_PIC);
//					mPopupWindow.dismiss();
//				}
//			});
			// 点击图库按钮
			btnImage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					if (i.resolveActivity(getMyActivity().getPackageManager()) != null) {
						startActivityForResult(i, SELECT_PIC);
					}
					mPopupWindow.dismiss();
				}
			});
			// 点击取消按钮
			btnCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mPopupWindow.dismiss();
				}
			});
		}
	};
	
	/** 给拍照或者从图库选择的图片命名*/
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
	 * 上传照片
	 * @param data  获取的图片数据
	 */
	private void updateHead(Intent data) {
		if (data == null) { 
			return;      
		} else{
			if (data.hasExtra("data")) {
				Bitmap bmap = data.getParcelableExtra("data");
				Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getMyActivity().getContentResolver(), bmap, null,null));
				Glide.with(getMyActivity()).load(uri).centerCrop().transform(new GlideCircleTransform(getMyActivity())).into(mIvaddimage);
				if (bmap == null) { return; }
				String picPath = getFilePath(getMyActivity()) + "/" + System.currentTimeMillis() + ".jpg";
				Tools.savBitmapToJpg(bmap, picPath);
				UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
				UploadUtils.uploadImage(getMyActivity(), picPath, userToken.getTokenCode(), new onUploadFinish() {
					
					@Override
					public void getImgUrl(String img) {
						mTmpPic = img;
						mHandler.post(upDateHead);
					}
				});
				/*Util.getImageUpload(getMyActivity(), picPath, new onUploadFinish() {
					@Override
					public void getImgUrl(String img) {
						mTmpPic = img;
						mHandler.post(upDateHead);
					}
				});*/                 
			}
		}
	}
	
	/**
	 * 判断用户的手机是否有sdCard
	 * @return
	 */
	public static boolean hasSdcard(){     
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 拍照和调用图库时要执行的方法
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CROP_PIC:
			if (resultCode == Activity.RESULT_OK) {
				if (data != null) {
					updateHead(data); 
				}
			}
			break;
		case TAKE_PIC:
			mTemp = new File(Environment.getExternalStorageDirectory()  + "/xiaoma.jpg"); 
			cropPic(Uri.fromFile(mTemp)); 
			break;
		case SELECT_PIC:
			 if (resultCode == Activity.RESULT_OK) {
                 if (data == null) {
                     return;
                 } else {
                	 Uri uri = data.getData();
                     cropPic1(uri);
                 }
             }
             break;
		default:
			break;
		}
	}
	
	private Runnable upDateHead = new Runnable() {
		@Override
		public void run() {
			if (Util.isNetworkOpen(getMyActivity())) {//联网
				upDateHead(mTmpPic);
			} else {//没有连接网络
				Util.getToastBottom(getMyActivity(), "请连接网络");
			}
		}
	};
	
	/**
	 * 执行上传图像的异步任务类
	 * @param pic 图片路径
	 */
	private void upDateHead(String picPath) {
		new UpdateUserHeadTask(getMyActivity(), new UpdateUserHeadTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (String.valueOf(ErrorCode.SUCC).equals(result.get("code").toString())) {
					DB.saveBoolean(CustConst.Key.UPP_USERINFO,true);
				}
			}
		}).execute(picPath);
	}

	/**
	 * 选择性别
	 */       
	OnClickListener sexListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			View view = LayoutInflater.from(getMyActivity()).inflate(R.layout.popuw_sex, null);
			view.setBackgroundColor(808080);
			mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			mPopupWindow.setFocusable(true);
			mPopupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
			mPopupWindow.setOutsideTouchable(true);
			mPopupWindow.showAsDropDown(mTvSex, 0, 0);
			TextView tvMail = (TextView) view.findViewById(R.id.tv_mail);
			TextView tvFeMail = (TextView) view.findViewById(R.id.tv_femail);
			tvMail.setOnClickListener(new OnClickListener() {// 男
				@Override
				public void onClick(View v) {
					mTvSex.setText(getResources().getString(R.string.info_mail));
					mUserSex = "M";
					mPopupWindow.dismiss();
				}
			});
			tvFeMail.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mTvSex.setText(getResources().getString(R.string.info_femail));
					mUserSex = "F";
					mPopupWindow.dismiss();
				}
			});
		}
	};
	
	// 选择城市
	OnClickListener areaListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			View view = LayoutInflater.from(getMyActivity()).inflate(R.layout.popuw_area, null);
			final ListView lvArea = (ListView) view.findViewById(R.id.lv_area);
			CitysAdapter adapter = new CitysAdapter(getMyActivity(), mCityDates);
			lvArea.setAdapter(adapter);
			final PopupWindow popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			popupWindow.setFocusable(true);
			popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
			popupWindow.setOutsideTouchable(true);
			popupWindow.showAsDropDown(mTvArea, 0, 0);
			lvArea.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					Citys citys = (Citys) lvArea.getItemAtPosition(position);
					mTvArea.setText(citys.getName());
					popupWindow.dismiss();
				}
		    });
		}
	};

	/**	
	 * 城市的列表
	 */
	private class CitysAdapter extends CommonListViewAdapter<Citys> {
		public CitysAdapter(Activity activity, List<Citys> datas) {
			super(activity, datas);
		}

		@Override
		public android.view.View getView(int position, android.view.View convertView, ViewGroup parent) {
			CommenViewHolder holder = CommenViewHolder.get(getMyActivity(), convertView, parent, R.layout.item_zhejiangcity, position);
			Citys citys = (Citys) getItem(position);
			((TextView) holder.getView(R.id.tv_homearea)).setText(citys.getName());
			return holder.getConvertView();
		}
	}

	/**
	 * 获得城市 
	 */
	public void getZhejiangCity() {
		new GetZhejiangCityTask(getMyActivity(), new GetZhejiangCityTask.Callback() {
			@Override
			public void getResult(JSONArray result) {
				Citys citys = null;
				if (result == null) {
					Util.getContentValidate(R.string.city_isnull);
				} else {
					for (int i = 0; i < result.size(); i++) {
						citys = new Citys();
						citys = Util.json2Obj(result.get(i).toString(), Citys.class);
						mCityDates.add(citys);
					}
				}
			}
		}).execute();
	}

	/**
	 * 点击返回查看到活动列表
	 * @param view
	 */
	@OnClick(R.id.iv_turn_in)
	public void btnBackClick(View view) {
		Activity act = getMyActivity();
		if(act == null){
			return ;
		}    
		act.finish();
	}
	
	/**
	 * 友盟统计
	 */
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(MyInfoMationFragment.class.getSimpleName()); 
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(MyInfoMationFragment.class.getSimpleName()); //统计页面
	}
}