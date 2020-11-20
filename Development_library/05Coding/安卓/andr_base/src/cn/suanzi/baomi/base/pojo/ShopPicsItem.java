package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;

import android.graphics.Bitmap;

/**
 * 装修图片的集合
 * @author qian.zhou
 */
public class ShopPicsItem implements Serializable {
	
	private Bitmap picsImage;

	public Bitmap getPicsImage() {
		return picsImage;
	}

	public void setPicsImage(Bitmap picsImage) {
		this.picsImage = picsImage;
	}
	
	
}
