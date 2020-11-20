// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.shop.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.fragment.SelectPicFragment;

/**
 * 图片上传
 * @author yanfang.li
 */
public class SelectPicActivity extends Activity{
	
	private static final String TAG = "SelectPicActivity";
	/** 使用照相机拍照获取图片 **/
	public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
	/** 使用相册中的图片 **/
	public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
	/**  从Intent获取图片路径的KEY **/
	public static final String KEY_PHOTO_PATH = "photo_path";
	
	/** 获取到的图片路径 **/
	private String picPath;
	
	private Intent lastIntent ;
	
	private Uri photoUri;
	
	public static SelectPicFragment newInstance() {
		Bundle args = new Bundle();
		SelectPicFragment fragment = new SelectPicFragment();
		fragment.setArguments(args);
		return fragment;  
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_pic);
		initView();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}
	/**
	 * 初始化
	 * @param view
	 */
	private void initView() {
		// 显示图片和相机
		LinearLayout lySelPic = (LinearLayout) findViewById(R.id.ly_sel_pic);
		// 选择相机
		Button btnTakePhoto = (Button) findViewById(R.id.btn_take_photo);
		// 选择图库
		Button btnpickPhoto = (Button) findViewById(R.id.btn_pick_photo);
		// 取消
		Button btnCancelPhoto = (Button) findViewById(R.id.btn_cancel);
		
		// 绑定点击事件
		lySelPic.setOnClickListener(btnDialogClick);
		btnTakePhoto.setOnClickListener(btnDialogClick);
		btnpickPhoto.setOnClickListener(btnDialogClick);
		btnCancelPhoto.setOnClickListener(btnDialogClick);
		
	}
	
	/**
	 * 各个控件对应的点击事件，列如点击相机进去拍照
	 */
	OnClickListener btnDialogClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ly_sel_pic:
				finish();
				break;
			case R.id.btn_take_photo:
				takePhoto();
				break;
			case R.id.btn_pick_photo:
				pickPhoto();
				break;
			default:
				finish();
				break;
			}
		}
	};
	
	/**
	 * 直接进入图库选择图片
	 */
	private void pickPhoto() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
	}
	
	/**
	 * 点击相机进行拍照获取图片
	 */
	private void takePhoto() {
		//执行拍照前，应该先判断SD卡是否存在
		String SDState = Environment.getExternalStorageState();
		if(SDState.equals(Environment.MEDIA_MOUNTED))
		{
			
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
			 // 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
			 // 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
			 // 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
			ContentValues values = new ContentValues();  
			photoUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);  
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
			// 启动一个相机
			startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
		}else{
			Toast.makeText(this,"内存卡不存在", Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return super.onTouchEvent(event);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK)
		{
			doPhoto(requestCode,data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 选择图片后，获取图片的路径
	 * @param requestCode
	 * @param data
	 */
	private void doPhoto(int requestCode, Intent data) {
		if(requestCode == SELECT_PIC_BY_PICK_PHOTO )  //从相册取图片，有些手机有异常情况，请注意
		{
			if(data == null)
			{
				Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
			photoUri = data.getData();
			if(photoUri == null )
			{
				Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
		}
		String[] pojo = {MediaStore.Images.Media.DATA};
		Cursor cursor = managedQuery(photoUri, pojo, null, null,null);   
		if(cursor != null )
		{
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToFirst();
			picPath = cursor.getString(columnIndex);
			cursor.close();
		}
		Log.i(TAG, "imagePath = "+picPath);
		if(picPath != null && ( picPath.endsWith(".png") || picPath.endsWith(".PNG") ||picPath.endsWith(".jpg") ||picPath.endsWith(".JPG")  ))
		{
			lastIntent.putExtra(KEY_PHOTO_PATH, picPath);
			setResult(Activity.RESULT_OK, lastIntent);
			finish();
		}else{
			Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
		}
	}
	
	public void onResume(){
    	super.onResume();
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
    }
}
