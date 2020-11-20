package cn.suanzi.baomi.base.pojo;

import android.graphics.Bitmap;

/**
 * 首页欢迎图片
 * @author liyanfang
 *
 */
public class GuideImg {
	
	private String id;
	
	/**
	 * 图片名称
	 */
	private String guideImg;
	
	/**
	 * 图片的bitmap
	 */
	private Bitmap bitmap;
	
	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

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

	public GuideImg() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GuideImg(String id, String guideImg) {
		super();
		this.id = id;
		this.guideImg = guideImg;
	}
	
}
