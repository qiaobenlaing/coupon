package com.huift.hfq.base.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

public class QrCodeUtils {

	/**
	 * 生成二维码
	 * 
	 * @param string
	 *            二维码中包含的文本信息
	 * @return Bitmap 二维码位图
	 */
	public static Bitmap createQrCode(String string) {
		return createQrCode(string, 500, 500);
	}

	/**
	 * 
	 * @param string
	 *            二维码中包含的文本信息
	 * @param width
	 *            二维码图片的宽度
	 * @param height
	 *            二维码图片的高度
	 * @return Bitmap 二维码位图
	 */
	public static Bitmap createQrCode(String string, int width, int height) {
		Bitmap bitmap = null;
		try {
			// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
			BitMatrix matrix = new MultiFormatWriter().encode(string, BarcodeFormat.QR_CODE, width, height);
			int mWidth = matrix.getWidth();
			int mHeight = matrix.getHeight();
			// 二维矩阵转为一维像素数组,也就是一直横着排了
			int[] pixels = new int[mWidth * mHeight];
			for (int y = 0; y < mHeight; y++) {
				for (int x = 0; x < mWidth; x++) {
					if (matrix.get(x, y)) {
						pixels[y * mWidth + x] = 0xff000000;
					}
				}
			}
			bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_4444);
			// 通过像素数组生成bitmap
			bitmap.setPixels(pixels, 0, mWidth, 0, 0, mWidth, mHeight);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
			if (bitmap != null) {
				bitmap.recycle();
			}
			Runtime.getRuntime().runFinalization();
			System.gc();
		}
		return bitmap;
	}

	/**
	 * 生成带图片二维码
	 * 
	 * @param string
	 *            二维码中包含的文本信息
	 * @param mBitmap
	 *            logo图片
	 * @return Bitmap 二维码位图
	 */
	public static Bitmap createQrCode(String string, Bitmap mBitmap) {
		return createQrCode(string, mBitmap, 500, 500);
	}

	/**
	 * 生成带图片二维码
	 * 
	 * @param string
	 *            二维码中包含的文本信息
	 * @param mBitmap
	 *            logo图片
	 * @param width
	 *            二维码图片的宽度
	 * @param height
	 *            二维码图片的高度
	 * @return Bitmap 二维码位图v
	 */
	public static Bitmap createQrCode(String string, Bitmap mBitmap, int width, int height) {
		Bitmap bitmap = null;
		try {
			int IMAGE_HALFWIDTH = 60;// 宽度值，影响中间图片大小
			Matrix m = new Matrix();
			float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
			float sy = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getHeight();
			m.setScale(sx, sy);// 设置缩放信息
			// 将logo图片按martix设置的信息缩放
			mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), m, false);
			MultiFormatWriter writer = new MultiFormatWriter();
			Hashtable<EncodeHintType, Object> hst = new Hashtable<EncodeHintType, Object>();
			hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");// 设置字符编码
			hst.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			BitMatrix matrix = writer.encode(string, BarcodeFormat.QR_CODE, width, height, hst);// 生成二维码矩阵信息
			int mWidth = matrix.getWidth();// 矩阵高度
			int mHeight = matrix.getHeight();// 矩阵宽度
			int halfW = mWidth / 2;
			int halfH = mHeight / 2;
			int[] pixels = new int[mWidth * mHeight];// 定义数组长度为矩阵高度*矩阵宽度，用于记录矩阵中像素信息
			for (int y = 0; y < mHeight; y++) {// 从行开始迭代矩阵
				for (int x = 0; x < mWidth; x++) {// 迭代列
					if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH && y > halfH - IMAGE_HALFWIDTH
							&& y < halfH + IMAGE_HALFWIDTH) {// 该位置用于存放图片信息
						// 记录图片每个像素信息
						pixels[y * mWidth + x] = mBitmap.getPixel(x - halfW + IMAGE_HALFWIDTH, y - halfH
								+ IMAGE_HALFWIDTH);
					} else {
						if (matrix.get(x, y)) {// 如果有黑块点，记录信息
							pixels[y * mWidth + x] = 0xff000000;// 记录黑块信息
						}
					}
				}
			}
			bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_4444);
			// 通过像素数组生成bitmap
			bitmap.setPixels(pixels, 0, mWidth, 0, 0, mWidth, mHeight);
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
			if (bitmap != null) {
				bitmap.recycle();
			}
			Runtime.getRuntime().runFinalization();
			System.gc();
		}
		return bitmap;
	}
	
	/**
	 * 生成条形码
	 * @param content
	 * @return
	 * @throws WriterException
	 */
	 public static Bitmap CreateOneDCode(String content) throws WriterException {
	        // 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
	        BitMatrix matrix = new MultiFormatWriter().encode(content,
	                BarcodeFormat.CODE_128, 500, 100);
	        int width = matrix.getWidth();
	        int height = matrix.getHeight();
	        int[] pixels = new int[width * height];
	        for (int y = 0; y < height; y++) {
	            for (int x = 0; x < width; x++) {
	                if (matrix.get(x, y)) {
	                    pixels[y * width + x] = 0xff000000;
	                }
	            }
	        }
	        Bitmap bitmap = Bitmap.createBitmap(width, height,
	                Bitmap.Config.ARGB_8888);
	        // 通过像素数组生成bitmap,具体参考api
	        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
	        return bitmap;
	    }
}
