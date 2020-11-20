package com.huift.hfq.shop.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.api.Tools;
import com.huift.hfq.base.pojo.ActivityUpLaod;
import com.huift.hfq.base.pojo.Campaign;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 编辑活动
 * @author qian.zhou
 */
public class EditAcitityContextFragment extends Fragment {
	/** 活动编码*/
	public static final String CAMPAINGN_OBJ = "Campaign";
	/** 保存活动信息的对象*/
	private Campaign mCampaign;
	/** 输入的内容*/
	private LinearLayout mLayout;
	private List<View> list;
	/** 得到的文字和图片*/
	private List<ActivityUpLaod> mUpLoadList;

	/**
	 * 需要传递参数时有利于解耦
	 */
	public static EditAcitityContextFragment newInstance() {
		Bundle args = new Bundle();
		EditAcitityContextFragment fragment = new EditAcitityContextFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_edit_activity, container, false);
		ViewUtils.inject(this, view);
		Util.addLoginActivity(getMyActivity());
		list = new ArrayList<View>();
		init(view);
		return view;
	}

	// 初始化方法
	private void init(View v) {
		Intent intent = getMyActivity().getIntent();
		mCampaign = (Campaign) intent.getSerializableExtra(CAMPAINGN_OBJ);
		//标题
		TextView tvTitle = (TextView) v.findViewById(R.id.tv_mid_content);
		tvTitle.setText(R.string.campaignadd_introduces);
		TextView tvMsg = (TextView) v.findViewById(R.id.tv_msg);
		tvMsg.setText(R.string.submit_message);
		mLayout = (LinearLayout) v.findViewById(R.id.ly_campaign_intro);
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
				it.putExtra(CampaignAddFragment.CAMPAIGN_INTRO_LIST, (ArrayList<ActivityUpLaod>) mUpLoadList);
				getMyActivity().setResult(CampaignAddFragment.CAMPAIGN_INTRO_SUCC, it);
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
				if (resultCode == getActivity().RESULT_OK) {
					Uri uri = data.getData();
					try {
						Bitmap bitmap = MediaStore.Images.Media.getBitmap(getMyActivity().getContentResolver(), uri);
						String picPath = Tools.getFilePath(getMyActivity()) + System.currentTimeMillis() + ".jpg";
						Tools.savBitmapToJpg(bitmap, picPath);

						if (null != bitmap) {
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

	private void insetImg(Bitmap bitmap,String localPath){
		ImageView imageView  = new ImageView(getMyActivity());
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 300);
		imageView.setImageBitmap(bitmap);
		imageView.setTag(localPath);
		mLayout.addView(imageView,mLayout.getChildCount(),layoutParams);
		removeNoConetntText();

		EditText editText = new EditText(getMyActivity());
		LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 120);
		mLayout.addView(editText,mLayout.getChildCount(),layoutParams1);
	}

	/**
	 * 去除空白的EditText
	 */
	private void removeNoConetntText() {
		for (int i = 0; i < mLayout.getChildCount(); i++) {
			View childAt = mLayout.getChildAt(i);
			if (childAt instanceof TextView) {
				String content = ((TextView)childAt).getText().toString();
				if ("".equals(content)) {
					list.add(childAt);
				}
			}
		}
		for (View view :list) {
			mLayout.removeView(view);
		}
	}

	/**
	 * 保存里面的内容
	 */
	private void getSaveDetail(){
		mUpLoadList = new ArrayList<ActivityUpLaod>();
		for (int i = 0;i < mLayout.getChildCount();i++) {
			View view = mLayout.getChildAt(i);
			if (view instanceof EditText) {
				if (!Util.isEmpty(((EditText)view).getText().toString())) {
					ActivityUpLaod upLaod = new ActivityUpLaod(0, i+1, ((EditText)view).getText().toString(), "", "");
					mUpLoadList.add(upLaod);
				}
			} else if (view instanceof ImageView) {
				String localPath = (String) view.getTag();
				ActivityUpLaod upLaod = new ActivityUpLaod(1, i+1, "", localPath, "");
				mUpLoadList.add(upLaod);
			}
		}
	}

	/**
	 * 当前的activity不为空
	 * @return
	 */
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}
}
