package cn.suanzi.baomi.base.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.util.Log;

/**
 * 判断app是在前台运行还是在后台运行
 * @author ad
 *
 */
public class AppStatus {
	public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                Log.i(context.getPackageName(), "此appimportace ="+ appProcess.importance+ ",context.getClass().getName()="+ context.getClass().getName());
                if (appProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台"+ appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "处于前台"+ appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }
}
