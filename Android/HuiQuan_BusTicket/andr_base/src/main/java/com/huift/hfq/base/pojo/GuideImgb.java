package com.huift.hfq.base.pojo;

import android.graphics.Bitmap;

/**
 * 首页欢迎图片
 * @author liyanfang
 *
 */
public class GuideImgb {
	
	private String id;
	
	/**
	 * 图片名称
	 */
	private String guideImg;
	
	/**
	 * 图片格式
	 * @return
	 */
	private Bitmap bitmap;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGuideImg() {
		return guideImg;
	}

	public void setGuideImg(String guideImg) {
		this.guideImg = guideImg;
	}

	public GuideImgb() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public GuideImgb(String id, String guideImg, Bitmap bitmap) {
		super();
		this.id = id;
		this.guideImg = guideImg;
		this.bitmap = bitmap;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
}
