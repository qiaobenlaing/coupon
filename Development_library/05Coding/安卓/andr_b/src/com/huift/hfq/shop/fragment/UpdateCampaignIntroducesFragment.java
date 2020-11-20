package com.huift.hfq.shop.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.Tools;
import com.huift.hfq.base.pojo.ActivityUpLaod;
import com.huift.hfq.base.utils.AppUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 活动介绍的修改
 * @author qian.zhou
 */
public class UpdateCampaignIntroducesFragment extends Fragment{
	
	private final static String TAG = "CampaignIntroducesFragment";
	
	private final static String CAMPAIGN_TITLE = "活动介绍";
	private final static String CAMPAIGN_SAVE = "完成";
	/** 输入的内容*/
	private LinearLayout mLayout;
	private List<View> list;
	/** 得到的文字和图片*/
	private List<ActivityUpLaod> mUpLoadList;
	
	
	
	public static UpdateCampaignIntroducesFragment newInstance() {
		Bundle args = new Bundle();
		UpdateCampaignIntroducesFragment fragment = new UpdateCampaignIntroducesFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	private Activity getMyActivity(){
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;       
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_update_campaign_introduces, container, false);
		ViewUtils.inject(this, view);
		list = new ArrayList<View>();
		init(view);
		
		//获取传递过来的图文页对象集合
		ArrayList<ActivityUpLaod> activityUpLaods = (ArrayList<ActivityUpLaod>) getActivity().getIntent().getSerializableExtra("UpLoadList");
		if(null != activityUpLaods && activityUpLaods.size()>0){
			Log.d(TAG, "size=="+activityUpLaods.size());
			showaAlreadyActivity(activityUpLaods);
		}
		return view;
	}
	
	/**
	 * 展示已经完成的图文页  可以进行修改
	 */
	private void showaAlreadyActivity(ArrayList<ActivityUpLaod> activityUpLaods) {
		removeNoConetntText();
		//
		for(ActivityUpLaod upLaod :activityUpLaods){
			if(upLaod.getType()==0){ //文字
 				insertEditText(upLaod);
			}else if(upLaod.getType()==1){ //图片
				insertImagView(upLaod);
			}
		}
	}
	
	/**
	 * 插入图片
	 */
	private void insertImagView(ActivityUpLaod upLaod) {
		ImageView imageView = new ImageView(getActivity());
		
		imageView.setScaleType(ScaleType.FIT_XY);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 500);
		layoutParams.setMargins(10, 10, 10, 10);
		
		if (!Util.isEmpty(upLaod.getNetPath())) {
			imageView.setTag("netPath"+upLaod.getNetPath());
		} else if (!Util.isEmpty(upLaod.getLoaclPath())) { 
			imageView.setTag("loaclPath"+upLaod.getLoaclPath());
		}
		
		if (!Util.isEmpty(upLaod.getNetPath())) {
			Util.showOwnImagView(getActivity(),  upLaod.getNetPath(), imageView);
		} else if (!Util.isEmpty(upLaod.getLoaclPath())) {
			Bitmap bitmap = BitmapFactory.decodeFile(upLaod.getLoaclPath());
			if (null != bitmap) {
				imageView.setImageBitmap(bitmap);
			}
		}
		mLayout.addView(imageView,mLayout.getChildCount(),layoutParams);
		
		
		final EditText editText = new EditText(getMyActivity());
    	LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	layoutParams1.setMargins(10, 10, 10, 10);
    	editText.setBackgroundDrawable(null);
    	mLayout.addView(editText,mLayout.getChildCount(),layoutParams1);
    	editText.setTag(mLayout.getChildCount()-1);
    	
    	editText.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				Log.d(TAG, "调用onKey");
				if(keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN){
					Log.d(TAG, "setOnKeyListener 11===="+((Integer)(editText.getTag())));
					Log.d(TAG, "setOnKeyListener 22===="+(editText.getSelectionStart()==0));
					Log.d(TAG, "setOnKeyListener 33===="+(mLayout.getChildAt((Integer)(editText.getTag())-1) instanceof ImageView));
					Log.d(TAG, "setOnKeyListener 44===="+(mLayout.getChildAt((Integer)(editText.getTag())-1)));
					
					if(editText.getSelectionStart() == 0 && ((Integer)(editText.getTag()))>0 &&(mLayout.getChildAt((Integer)(editText.getTag())-1) instanceof ImageView) ){
						Log.d(TAG, "进来了----");
						mLayout.removeViewAt((Integer)(editText.getTag())-1);
						changeAllEditTextTag((Integer)(editText.getTag())-1);
						View childAt = mLayout.getChildAt((Integer)editText.getTag());
						Log.d(TAG, "childAt == " +childAt);
						if(childAt  instanceof EditText){
							childAt.requestFocus();
						}
					}
				}
				return false;
			}
		});
		
	}

	/**
	 * 插入文本内容
	 * @param upLaod
	 */
	private void insertEditText(ActivityUpLaod upLaod) {
		final EditText editText = new EditText(getMyActivity());
    	LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	editText.setText(upLaod.getContent());
    	layoutParams1.setMargins(10, 10, 10, 10);
    	editText.setBackgroundDrawable(null);
    	mLayout.addView(editText,mLayout.getChildCount(),layoutParams1);
    	editText.setTag(mLayout.getChildCount()-1);
    	
    	editText.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				Log.d(TAG, "调用onKey");
				if(keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN){
					Log.d(TAG, "setOnKeyListener 11===="+((Integer)(editText.getTag())));
					Log.d(TAG, "setOnKeyListener 22===="+(editText.getSelectionStart()==0));
					Log.d(TAG, "setOnKeyListener 33===="+(mLayout.getChildAt((Integer)(editText.getTag())-1) instanceof ImageView));
					Log.d(TAG, "setOnKeyListener 44===="+(mLayout.getChildAt((Integer)(editText.getTag())-1)));
					
					if(editText.getSelectionStart() == 0 && ((Integer)(editText.getTag()))>0 &&(mLayout.getChildAt((Integer)(editText.getTag())-1) instanceof ImageView) ){
						Log.d(TAG, "进来了----");
						mLayout.removeViewAt((Integer)(editText.getTag())-1);
						changeAllEditTextTag((Integer)(editText.getTag())-1);
						View childAt = mLayout.getChildAt((Integer)editText.getTag());
						Log.d(TAG, "childAt == " +childAt);
						if(childAt  instanceof EditText){
							childAt.requestFocus();
						}
					}
				}
				return false;
			}
		});
	}
	
	/**
	 * 改变所有EditText的Tag -1 以便进行删除
	 */
	private void changeAllEditTextTag(int start){
		for (int i = start; i < mLayout.getChildCount(); i++) {
			View childAt = mLayout.getChildAt(i);
			if(childAt instanceof EditText){
				if((Integer)(childAt.getTag())>1){
					childAt.setTag((Integer)(childAt.getTag())-1);
				}
			}
		}
	}
	
	private void init(View view) {
		TextView tvCampaignName = (TextView) view.findViewById(R.id.tv_mid_content);// 标题
		tvCampaignName.setText(CAMPAIGN_TITLE);
		TextView tvSet = (TextView) view.findViewById(R.id.tv_msg);// 保存
		tvSet.setVisibility(View.VISIBLE);
		tvSet.setText(CAMPAIGN_SAVE);
		mLayout = (LinearLayout) view.findViewById(R.id.ly_campaign_intro);
	}

	/**
	 * 点击的回调
	 * @param v
	 */
	@OnClick({R.id.layout_turn_in,R.id.layout_campaign_selimg,R.id.tv_msg})
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_turn_in://返回
			getMyActivity().finish();
			break;
			
		case R.id.layout_campaign_selimg: //插入图片
			//打开图库
			Intent intent= new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, 100);
			break;
			
		case R.id.tv_msg: //完成
			getSaveDetail();
			
			Intent it= new Intent();
			it.putExtra(EditAcitityMsgFragment.CAMPAIGN_INTRO_LIST, (ArrayList<ActivityUpLaod>) mUpLoadList);
			getMyActivity().setResult(EditAcitityMsgFragment.CAMPAIGN_INTRO_SUCC, it);
			getMyActivity().finish();
			break;

		default:
			break;
		}
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 100: //选择图片回调
			if(resultCode == getActivity().RESULT_OK){
				Uri uri = data.getData();
				try {
					Bitmap bitmap = MediaStore.Images.Media.getBitmap(getMyActivity().getContentResolver(), uri);
					String picPath = getFilePath(getMyActivity()) + "/" + System.currentTimeMillis() + ".jpg";
					Tools.savBitmapToJpg(bitmap, picPath);
					Log.d(TAG, "path==="+picPath);
					
					if(null != bitmap){
						insetImg(Util.getImage(picPath,getMyActivity()),picPath);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			break;

		default:
			break;
		}
	}
	
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

	private void insetImg(Bitmap bitmap,String localPath){
		ImageView imageView  = new ImageView(getActivity());
		imageView.setScaleType(ScaleType.FIT_XY);
    	LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 500);
    	layoutParams.setMargins(10, 10, 10, 10);
    	imageView.setImageBitmap(bitmap);
    	imageView.setTag("loaclPath"+localPath);
    	mLayout.addView(imageView,mLayout.getChildCount(),layoutParams);
    	removeNoConetntText();
    	
    	final EditText editText = new EditText(getMyActivity());
    	LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	editText.setBackgroundDrawable(null);
    	layoutParams1.setMargins(10, 10, 10, 10);
    	mLayout.addView(editText,mLayout.getChildCount(),layoutParams1);
    	Log.d(TAG, "position count==="+mLayout.getChildCount());
    	editText.setTag(mLayout.getChildCount()-1);
    	
    	editText.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				Log.d(TAG, "调用onKey");
				if(keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN){
					Log.d(TAG, "setOnKeyListener 11===="+((Integer)(editText.getTag())));
					Log.d(TAG, "setOnKeyListener 22===="+(editText.getSelectionStart()==0));
					Log.d(TAG, "setOnKeyListener 33===="+(mLayout.getChildAt((Integer)(editText.getTag())-1) instanceof ImageView));
					Log.d(TAG, "setOnKeyListener 44===="+(mLayout.getChildAt((Integer)(editText.getTag())-1)));
					
					if(editText.getSelectionStart() == 0 && (mLayout.getChildAt((Integer)(editText.getTag())-1) instanceof ImageView) ){
						Log.d(TAG, "进来了----");
						mLayout.removeViewAt((Integer)(editText.getTag())-1);
						editText.setTag((Integer)editText.getTag()-1);
						
						View childAt = mLayout.getChildAt((Integer)editText.getTag()-1);
						Log.d(TAG, "childAt == " +childAt);
						if (childAt  instanceof EditText) {
							childAt.requestFocus();
						} else{
							//TODO
						}
					}
				}
				return false;
			}
		});

    	
    
    
	}

    /**
     * 去除空白的EditText
     */
	private void removeNoConetntText() {
		for (int i = 0; i < mLayout.getChildCount(); i++) {
			View childAt = mLayout.getChildAt(i);
			 if(childAt instanceof TextView){
				 String content = ((TextView)childAt).getText().toString();
				 if("".equals(content)){
					 list.add(childAt);
				 }
			 }
		}	
		for(View view :list){
			mLayout.removeView(view);
		}
	}
	
	/**
	 * 保存里面的内容
	 */
	private void getSaveDetail(){
		mUpLoadList = new ArrayList<ActivityUpLaod>();
		for(int i = 0;i < mLayout.getChildCount();i++){
			View view = mLayout.getChildAt(i);
			if(view instanceof EditText){
				if(!Util.isEmpty(((EditText)view).getText().toString())){
					ActivityUpLaod upLaod = new ActivityUpLaod(0, i+1, ((EditText)view).getText().toString(), "", "");
					mUpLoadList.add(upLaod);
				}
			}else if(view instanceof ImageView){
				String localPath = (String) view.getTag();
				ActivityUpLaod upLaod =null;
			
				Log.d("TAG", "路径===="+localPath);
				if(localPath.startsWith("netPath")){ //服务器拉下来的图片
					upLaod = new ActivityUpLaod(1, i+1, "", "", localPath.substring(7));
				}else if(localPath.startsWith("loaclPath")){ //本地图片
					upLaod = new ActivityUpLaod(1, i+1, "",  localPath.substring(9),"");
				}
				mUpLoadList.add(upLaod);
			}
		}
		Log.d("TAG", "数据===="+mUpLoadList.toString());
	}
}