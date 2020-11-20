package cn.suanzi.baomi.cust.util;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.HomeTemplate;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.SizeUtil;
import cn.suanzi.baomi.cust.R;

/**
 * 用来显示活动模块的下的各个图片
 * 
 * @author yingchen
 * 
 */
public class ActImgUtil {
	private static final String TAG = ActImgUtil.class.getSimpleName();
	/** 默认图片的大小*/
	private static final int IMG_SIZE = 62;
	/** 默认文字大小*/
	private static final int FONT_SIZE = 14;
	/** 图片*/
	private static final int IMG = 1;
	/** 文字*/
	private static final int FONT = 2;
	/** 图片*/
	private static ImageView mImageView;
	/** 标题*/
	private static TextView mTitleTV;
	/** 副标题*/
	private static TextView mSubTitleTV;

	public static void showActImg(RelativeLayout rl, Activity act, HomeTemplate template) {
		// 图片显示位置
		String imgPosition = template.getImgPosition();
		if (null == imgPosition || "".equals(imgPosition)) {
			Log.d(TAG, "未知图片位置>>>>>>>>>");
			return;
		}

		if ("0".equals(imgPosition)) { // 图片显示左边
			View leftView = View.inflate(act, R.layout.act_img_left, null);
			mImageView = (ImageView) leftView.findViewById(R.id.iv_act);
			mTitleTV = (TextView) leftView.findViewById(R.id.tv_act_title);
			mSubTitleTV = (TextView) leftView.findViewById(R.id.tv_act_subtitle);

			showView(rl, act, template, leftView);
		} else if ("1".equals(imgPosition)) { // 图片显示在上边
			View topView = View.inflate(act, R.layout.act_img_top, null);
			mImageView = (ImageView) topView.findViewById(R.id.iv_act);
			mTitleTV = (TextView) topView.findViewById(R.id.tv_act_title);
			mSubTitleTV = (TextView) topView.findViewById(R.id.tv_act_subtitle);

			showView(rl, act, template, topView);
		} else if ("2".equals(imgPosition)) { // 图片显示在右边
			View rightView = View.inflate(act, R.layout.act_img_right, null);
			mImageView = (ImageView) rightView.findViewById(R.id.iv_act);
			mTitleTV = (TextView) rightView.findViewById(R.id.tv_act_title);
			mSubTitleTV = (TextView) rightView.findViewById(R.id.tv_act_subtitle);

			showView(rl, act, template, rightView);
		} else if ("3".equals(imgPosition)) { // 图片显示在下边
			View bottomView = View.inflate(act, R.layout.act_img_bottom, null);
			mImageView = (ImageView) bottomView.findViewById(R.id.iv_act);
			mTitleTV = (TextView) bottomView.findViewById(R.id.tv_act_title);
			mSubTitleTV = (TextView) bottomView.findViewById(R.id.tv_act_subtitle);

			showView(rl, act, template, bottomView);
		} else if ("4".equals(imgPosition)) { // 图片显示在中间
			View centerView = View.inflate(act, R.layout.act_img_center, null);
			mImageView = (ImageView) centerView.findViewById(R.id.iv_act);
			Log.d(TAG, "mImageView : ");
			// 设置图片
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
			layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
			rl.addView(centerView, layoutParams);
			
			LayoutParams params = (LayoutParams) mImageView.getLayoutParams();
			params.height = getSize(template.getImgHeight(), IMG);
			mImageView.setLayoutParams(params);
			
			Util.showFirstImages(act, template.getImgUrl(), mImageView);
			
		}
	}

	private static void showView(RelativeLayout rl, Activity act, HomeTemplate template, View bottomView) {
		Log.d(TAG, "showViewImgWidth >>>> W" + getSize(template.getImgWidth(), IMG));
		Log.d(TAG, "showViewImgWidth >>>> H" + getSize(template.getImgHeight(), IMG));
		
		LayoutParams params = (LayoutParams) mImageView.getLayoutParams();
		params.height = getSize(template.getImgHeight(), IMG);
		params.width = getSize(template.getImgWidth(), IMG);
		mImageView.setLayoutParams(params);
		
		Util.showFirstImages(act, template.getImgUrl(), mImageView);
//		mImageView.setLayoutParams(new LinearLayout.LayoutParams(getSize(template.getImgWidth(), IMG), getSize(template.getImgHeight(), IMG)));
		// 设置图片
		// 设置标题
		mTitleTV.setText(template.getTitle());
		if (Util.isEmpty(template.getTitleColor())) {
			mTitleTV.setTextColor(AppUtils.getContext().getResources().getColor(R.color.deep_textcolor));
		} else {
			mTitleTV.setTextColor(Color.parseColor(template.getTitleColor()));
		}
		
		mTitleTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16); // 22SP 设置字体

		// 设置子标题
		mSubTitleTV.setText(template.getSubTitle());
		if (Util.isEmpty(template.getTitleColor())) {
			mTitleTV.setTextColor(AppUtils.getContext().getResources().getColor(R.color.deep_textcolor));
		} else {
			mTitleTV.setTextColor(Color.parseColor(template.getTitleColor()));
			mSubTitleTV.setTextColor(Color.parseColor(template.getSubTitleColor()));
		}
		mSubTitleTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13); // 22SP

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		rl.addView(bottomView, layoutParams);
	}

	/**
	 * 返回一个尺寸
	 * @param size 输入的尺寸
	 * @param type 1 代表图片的尺寸 2 代表是文字的大小
	 * @return 返回一个float类型的数字
	 */
	private static int getSize(String size,int type) {
		int numSize = 0;
		if (type == IMG) { // 图片的尺寸
			if (Util.isEmpty(size)) {
				numSize = SizeUtil.dip2px(IMG_SIZE); // 默认图片的大小
			} else {
				numSize = SizeUtil.dip2px(Float.parseFloat(size));
//				numSize = (int) Float.parseFloat(size);
			}
		} else {
			if (Util.isEmpty(size)) {
				numSize = FONT_SIZE; // 默认字体的大小
			} else {
				numSize = SizeUtil.px2sp(Float.parseFloat(size));
			}
		}
		
		return numSize;
	}

}
