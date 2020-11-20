package cn.suanzi.baomi.base.utils;

import android.graphics.Bitmap;

public interface ImageDownloadCallback {

	public void success(Bitmap bitmap);
	public void fail();
}
