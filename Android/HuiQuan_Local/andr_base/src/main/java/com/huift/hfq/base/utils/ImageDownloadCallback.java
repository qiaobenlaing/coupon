package com.huift.hfq.base.utils;

import android.graphics.Bitmap;

public interface ImageDownloadCallback {

	public void success(Bitmap bitmap);
	public void fail();
}
