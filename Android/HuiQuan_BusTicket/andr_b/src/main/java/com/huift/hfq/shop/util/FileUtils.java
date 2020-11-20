package com.huift.hfq.shop.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.huift.hfq.base.api.Tools;

public class FileUtils {

	public static void saveBitmap(Context context,Bitmap bm, String picName) {
		try {
			File f = new File(Tools.getFilePath(context), picName + ".JPEG");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File createSDDir(Context context,String dirName) throws IOException {
		File dir = new File(Tools.getFilePath(context) + dirName);
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

			System.out.println("createSDDir:" + dir.getAbsolutePath());
			System.out.println("createSDDir:" + dir.mkdir());
		}
		return dir;
	}

	public static boolean isFileExist(Context context,String fileName) {
		File file = new File(Tools.getFilePath(context) + fileName);
		file.isFile();
		return file.exists();
	}
	
	public static void delFile(Context context,String fileName){
		File file = new File(Tools.getFilePath(context) + fileName);
		if(file.isFile()){
			file.delete();
        }
		file.exists();
	}

	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
		return true;
	}

}
