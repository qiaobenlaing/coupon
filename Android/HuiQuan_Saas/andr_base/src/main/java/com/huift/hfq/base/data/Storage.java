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
import android.text.TextUtils;
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
		return status.equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * 检查我们需要的文件夹是否存在，如果不存在则创建。
	 *
	 *            默认文件中子文件夹。一般为项目名称。
	 * @return true：创建成功或文件夹已经存在；false：创建失败。
	 */
	public static String getExtDir(Context context) {
		String dirPath;
		if (hasSDCard()){
			dirPath=Environment.getExternalStorageDirectory().getPath() + Const.DEFAULT_DIR;
		}else {
			dirPath=context.getFilesDir().getPath();
		}
		// 在里面再建一个.nomedia文件，防止文件夹被扫描媒体
		if (!TextUtils.isEmpty(dirPath)) {
			File dir=new File(dirPath);
			if (!dir.exists()){
				dir.mkdirs();
			}
			File noMediaFile = new File(dirPath, ".nomedia");
			if (!noMediaFile.exists()) {
				try {
					noMediaFile.createNewFile();
				} catch (IOException e) {
					Log.w("Storage", "新建.nomedia文件失败。", e);
				}
			}
		}
		return dirPath;
	}
}
