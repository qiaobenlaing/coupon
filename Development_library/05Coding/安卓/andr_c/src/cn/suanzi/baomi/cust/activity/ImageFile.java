package cn.suanzi.baomi.cust.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.FinishActivityUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.adapter.FolderAdapter;
import cn.suanzi.baomi.cust.utils.PublicWay;
import cn.suanzi.baomi.cust.utils.Res;

/**
 * 这个类主要是用来进行显示包含图片的文件夹
 * @author ad
 *
 */
public class ImageFile extends Activity {

	private FolderAdapter mFolderAdapter;
	private Button mBtCancel;
	private Context mContext;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plugin_camera_image_file);
		PublicWay.activityList.add(this);
		mContext = this;
		// bt_cancel = (Button) findViewById(Res.getWidgetID("cancel"));
		// bt_cancel.setOnClickListener(new CancelListener());
		GridView gridView = (GridView) findViewById(Res.getWidgetID("fileGridView"));
		TextView textView = (TextView) findViewById(Res.getWidgetID("headerTitle"));
		textView.setText(Res.getString("photo"));
		mFolderAdapter = new FolderAdapter(this);
		gridView.setAdapter(mFolderAdapter);
		FinishActivityUtils.addActivity(ImageFile.this);
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}

	/*
	 * private class CancelListener implements OnClickListener {// 取消按钮的监听
	 * public void onClick(View v) { //清空选择的图片 Bimp.tempSelectBitmap.clear();
	 * Intent intent = new Intent(); intent.setClass(mContext,
	 * ImageUploadActivity.class); startActivity(intent); } }
	 */

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(mContext, ImageUploadActivity.class);
			startActivity(intent);
			FinishActivityUtils.exit();
		}

		return true;
	}

	public void onResume() {
		super.onResume();
		AppUtils.setActivity(this);
		AppUtils.setContext(getApplicationContext());
	}
}
