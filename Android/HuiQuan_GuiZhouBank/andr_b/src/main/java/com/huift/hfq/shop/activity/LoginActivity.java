// ---------------------------------------------------------
// @author    
// @version   1.0.0
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.activity;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.huift.hfq.base.SingleFragmentActivity;
import com.huift.hfq.shop.fragment.LoginFragment;

import java.util.ArrayList;

/**
 * @author
 * 登录Activity 单Fragment
 */
public class LoginActivity extends SingleFragmentActivity {
	private final int PERMISSION_REPUEST_CODE=1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkPermission();
	}

	@Override
	protected Fragment createFragment() {
		return LoginFragment.newInstance();
	}

	/**
	 * android 6.0 以上需要动态申请权限
	 */
	private void checkPermission() {
		String[] permissions = {
				Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.READ_PHONE_STATE,
				Manifest.permission.CAMERA
		};

		ArrayList<String> toApplyList = new ArrayList<String>();
		for (String perm : permissions) {
			if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
				// 进入到这里代表没有权限.
				toApplyList.add(perm);
			}
		}
		//android8.0 安装应用权限
		if (android.os.Build.VERSION.SDK_INT >= 26) {
			boolean hasInstallPermission = getPackageManager().canRequestPackageInstalls();
			if (!hasInstallPermission) {
				//请求安装未知应用来源的权限
				toApplyList.add(Manifest.permission.REQUEST_INSTALL_PACKAGES);
			}
		}

		String[] tmpList = new String[toApplyList.size()];
		if (!toApplyList.isEmpty()) {
			ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), PERMISSION_REPUEST_CODE);
		}

	}


	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		int refuseCount = 0;
		// 此处为android 6.0以上动态授权的回调，自行实现。
		if (requestCode == PERMISSION_REPUEST_CODE) {
			for (int i = 0; i < permissions.length; i++) {
				switch (permissions[i]) {
					case Manifest.permission.WRITE_EXTERNAL_STORAGE:
						if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
							refuseCount++;
//                            finish();
						}
						break;
					case Manifest.permission.READ_PHONE_STATE:
						if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
							refuseCount++;
						}
						break;
					case Manifest.permission.CAMERA:
						if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
							refuseCount++;
						}
						break;
				}
			}
		}
		if (refuseCount > 0) {
			Toast.makeText(this,"您拒绝了部分权限，应用可能无法正常运行",Toast.LENGTH_SHORT).show();
		}
	}
}
