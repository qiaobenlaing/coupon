// ---------------------------------------------------------
// @author    Weiping Liu
// @version   1.0.0
// @copyright 版权所有 (c) 2014 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------

package com.huift.hfq.base.data;

import java.io.File;
import java.io.IOException;

import com.huift.hfq.base.Const;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * 存储相关操作
 * 
 * @author Weiping Liu
 * @version 1.0.0
 */
public class Storage {

	/**
	 * 检查系统是否有SD卡
	 * 
	 * @return
	 */
	public static boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 检查我们需要的文件夹是否存在，如果不存在则创建。
	 * 
	 * @return true：创建成功或文件夹已经存在；false：创建失败。
	 */
	public static File getExtDir() {
		return getExtDir("");
	}

	/**
	 * 检查我们需要的文件夹是否存在，如果不存在则创建。
	 * 
	 * @param subDirName
	 *            默认文件中子文件夹。一般为项目名称。
	 * @return true：创建成功或文件夹已经存在；false：创建失败。
	 */
	public static File getExtDir(String subDirName) {
		subDirName = subDirName == null ? "" : subDirName;
		String subDir = Const.DEFAULT_DIR + "/" + subDirName;
		File dir = null;
		File dirExt = new File(Environment.getExternalStorageDirectory()
				.getPath() + "/" + subDir);
		if (dirExt.exists() || dirExt.mkdirs()) {
			dir = dirExt;
		} else {
			File dirData = new File("/data/data/" + subDir);
			if ((dirData.exists() || dirData.mkdirs()) && dirData.canWrite()) {
				dir = dirData;
			} else {
				File dirCache = new File("/data/cache/" + subDir);
				if ((dirCache.exists() || dirCache.mkdirs())
						&& dirCache.canWrite()) {
					dir = dirCache;
				}
			}
		}
		// 在里面再建一个.nodedia文件夹，防止xiaoniu文件夹被扫描媒体
		if (dir != null) {
			File noMediaFile = new File(dir, ".nomedia");
			if (!noMediaFile.exists()) {
				try {
					noMediaFile.createNewFile();
				} catch (IOException e) {
					Log.w("Storage", "在目录(" + dir + ")中新建.nomedia文件失败。", e);
				}
			}
		}
		return dir;
	}
	
	/**
	 * 获取可写目录。优先获取本程序固定的SD卡目录；如果没有SD卡，则返回activity的cache目录/data/cache。
	 * @param ctx Activity实例。
	 * @return
	 */
	public static File getDir(Context ctx) {
		File dir = getExtDir();
//		Log.d("文件获取", "SD卡文件获取：" + dir.getAbsolutePath());
		return dir != null ? dir : ctx.getCacheDir();
	}

}
