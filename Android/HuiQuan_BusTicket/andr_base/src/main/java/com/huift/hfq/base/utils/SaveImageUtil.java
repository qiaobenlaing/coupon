package com.huift.hfq.base.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

/**
 * 缓存图片
 * @author liyanfang
 *
 */
public class SaveImageUtil {
	private static final String TAG = SaveImageUtil.class.getSimpleName();
	static SharedPreferences mySharedPreferences = AppUtils.getActivity().getSharedPreferences("base64", Activity.MODE_PRIVATE);
	/**
	 * 保存图片
	 * @param imageView
	 */
	public static void saveImg(ImageView imageView,String key) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		baos = new ByteArrayOutputStream();
		((BitmapDrawable) imageView.getDrawable()).getBitmap().compress(CompressFormat.JPEG, 50, baos);
		String imageBase64 = new String(Base64.encodeBase64(baos.toByteArray()));
		editor.putString(key, imageBase64);
		editor.commit();
		try {
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "关闭输入流错误：" + e.getMessage());
		}
	}
	
	/**
	 * 获取图片
	 * @param key 保存的可以
	 * @param name 获取的名字
	 * @return
	 */
	public static Drawable getImg (String key, String name) {
		String imageBase64 = mySharedPreferences.getString(key, "");
		byte[] base64Bytes = Base64.decodeBase64(imageBase64.getBytes());
		ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
		Drawable createFromStream = Drawable.createFromStream(bais, name);
		try {
			bais.close();
		} catch (IOException e) {
			Log.e(TAG, "关闭输出流错误：" + e.getMessage());
		}
		return createFromStream;
	}
	
}
