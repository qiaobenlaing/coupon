package cn.suanzi.baomi.shop.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.suanzi.baomi.base.Bimp;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopConst;
import cn.suanzi.baomi.shop.activity.ActAddSelectDetailActivity;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @author wensi.yu
 * 添加营销活动中的图片
 */
public class ActAddImageFragment extends Fragment {
	
	private final static String TAG = "AlbumActivity";
	
	/** 返回图片**/
	@ViewInject(R.id.layout_turn_in)
	private LinearLayout mIvBackup;
	/** 功能描述文本**/
	@ViewInject(R.id.tv_mid_content)
	private TextView mTvdesc;
	/** 上一步**/
	@ViewInject(R.id.btn_actaddexplain_laststep)
	private Button mBtnActAddLastStep;
	/** 预览**/
	@ViewInject(R.id.btn_actaddexplain_nextstep)
	private Button mBtnActAddNextStep;
	/** 图片**/
	@ViewInject(R.id.iv_actaddimage_img)
	private ImageView mIvActaddImageImg;
	/** PopupWindow容器**/
	private PopupWindow mPopupWindow;
	/** 照相**/
	private Button mBtnActaddimageCamera;
	/** 从相册中选择**/
	private Button mBtnActaddimagePhoto;
	/** 取消**/
	private Button mBtnActaddimageCancel;
	/** 选择图片路径**/
	private String mImagePath;
	/**保存*/
	private SharedPreferences mSharedPreferences;
	
	public static ActAddImageFragment newInstance() {
		Bundle args = new Bundle();
		ActAddImageFragment fragment = new ActAddImageFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_actlist_addimage, container, false);//说明v，注释 e.g:Fragment的view
		ViewUtils.inject(this, view);
		init();
		return view;
	}
	
	/**
	 * 初始化
	 */
	private void init() {
		//添加
		Util.addActivity(getActivity());
		Util.addLoginActivity(getActivity());
		//设置标题
		mIvBackup.setVisibility(View.VISIBLE);
		mTvdesc.setText(R.string.tv_actlist_title);
		mBtnActAddNextStep.setText(R.string.tv_actlist_select);
		mIvActaddImageImg.setOnTouchListener(imageTouchListener);
		
		mSharedPreferences = getActivity().getSharedPreferences(ShopConst.ActAdd.ACT_ADD, Context.MODE_PRIVATE);
		mImagePath = mSharedPreferences.getString("imagePath", "");
		try{
			//显示图片
			Bitmap bitmap= Bimp.revitionImageSize(mImagePath);
			mIvActaddImageImg.setImageBitmap(bitmap);
			Log.d(TAG, "初始化的url=============="+bitmap);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 点击图片
	 * @param view
	 */
	OnTouchListener imageTouchListener = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				View view = LayoutInflater.from(getActivity()).inflate(R.layout.select_pic, null);
				view.setBackgroundColor(Color.TRANSPARENT);
				mPopupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				mPopupWindow.setFocusable(true);
				mPopupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
				mPopupWindow.setOutsideTouchable(true);
				mPopupWindow.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
				
				mBtnActaddimageCamera = (Button) view.findViewById(R.id.btn_take_photo);
				mBtnActaddimagePhoto = (Button) view.findViewById(R.id.btn_pick_photo);
				mBtnActaddimageCancel = (Button) view.findViewById(R.id.btn_cancel);
				
				mBtnActaddimageCamera.setOnClickListener(cameraClick);//照相
				mBtnActaddimagePhoto.setOnClickListener(photoClick); //从相册中选择
				mBtnActaddimageCancel.setOnClickListener(canClick);//取消
				
			}
			return true;
		}
	};
	
	/**
	 * 照相
	 */
	OnClickListener cameraClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
	        startActivityForResult(intent, 1); 
		}
	};
	
	/**
	 * 从相册中选择
	 */
	OnClickListener photoClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, Util.RESULT_LOAD_IMAGE);
		}     
	};
	
	/**
	 * 取消按钮
	 */
	OnClickListener canClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mPopupWindow.dismiss();
		}
	};
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);  
        //mPopupWindow.dismiss();
        if (requestCode == Util.TAKE_PHOTO) {
			 mImagePath = Util.takePhoto(getActivity(),resultCode, data, mIvActaddImageImg);
			 Log.i(TAG, "拍照图片路径============"+mImagePath);
		 } else if (requestCode == Util.RESULT_LOAD_IMAGE) {
			 mImagePath = Util.mapdepot(getActivity(), resultCode, data, mIvActaddImageImg);
			 Log.i(TAG, "从相册中图片路径============"+mImagePath);
		 }
        
        mSharedPreferences = getActivity().getSharedPreferences(ShopConst.ActAdd.ACT_ADD, Context.MODE_PRIVATE);
	    Editor editor = mSharedPreferences.edit();   
	    editor.putString("imagePath", mImagePath);
	    Log.i("TAG", "添加图片的URL====================="+mImagePath);
	    editor.commit();
	}
	
	/**
	 * 取消
	 */
	OnClickListener cancelClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mPopupWindow.dismiss();
		}
	};
	
	/**
	 * 点击上一步按钮
	 * @param view
	 */
	@OnClick(R.id.btn_actaddexplain_laststep)
	public void btnActAddLastStepClick(View view) {
		getActivity().finish();
	}


	/**
	 * 点击返回按钮
	 * @param view
	 */
	@OnClick(R.id.layout_turn_in)
	public void btnActAddBackClick(View view) {
		getActivity().finish();
	}
	
	/**
	 * 点击预览按钮
	 * @param view
	 */
	@OnClick(R.id.btn_actaddexplain_nextstep)
	public void btnActAddNextStepClick(View view) {
		
		switch (view.getId()) {
		case R.id.btn_actaddexplain_nextstep:
			Util.addActivity(getActivity());
			// 判断上传路径不为空
			if (Util.isEmpty(mImagePath)) {
				Util.getContentValidate(R.string.toast_actaddimage_upload);
				break;
			}
			// 图片上传
			Util.getImageUpload(getActivity(), mImagePath);
			
			SharedPreferences mSharedPreferences = getActivity().getSharedPreferences(ShopConst.ActAdd.ACT_ADD, Context.MODE_PRIVATE);
		    Editor editor = mSharedPreferences.edit();   
		    editor.putString("imagePath", mImagePath);
		    Log.i("TAG", "添加图片的URL====================="+mImagePath);
		    editor.commit();
			
			Intent intent = new Intent(getActivity(), ActAddSelectDetailActivity.class);
			getActivity().startActivity(intent);
			
			break;
		default:
			break;
		}
	}
}
