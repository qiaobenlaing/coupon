package com.huift.hfq.cust.decoding;

import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.huift.hfq.cust.R;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.huift.hfq.cust.activity.ScanActivity;
import com.huift.hfq.cust.camera.CameraManager;
import com.huift.hfq.cust.view.ViewfinderResultPointCallback;

/**
 * 扫一扫
 * @author wensi.yu
 *
 */
public final class ScanningHandler extends Handler {

	private static final String TAG = ScanningHandler.class.getSimpleName();

	private final ScanActivity activity;
	private final DecodeThread decodeThread;
	private State state;

	public enum State {
		PREVIEW, SUCCESS, DONE
	}

	public ScanningHandler(ScanActivity activity,
			Vector<BarcodeFormat> decodeFormats, String characterSet) {
		this.activity = activity;
		decodeThread = new DecodeThread(activity, decodeFormats, characterSet,
				new ViewfinderResultPointCallback(activity.getViewfinderView()));
		decodeThread.start();
		state = State.SUCCESS;
		// Start ourselves capturing previews and decoding.
		CameraManager.get().startPreview();
		restartPreviewAndDecode();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void handleMessage(Message message) {
		switch (message.what) {
		case R.id.auto_focus:
			// Log.d(TAG, "Got auto-focus message");
			// When one auto focus pass finishes, start another. This is the
			// closest thing to
			// continuous AF. It does seem to hunt a bit, but I'm not sure what
			// else to do.
			if (state == State.PREVIEW) {
				CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
			}
			break;
		case R.id.restart_preview:
			Log.d(TAG, "Got restart preview message");
			restartPreviewAndDecode();
			break;
		case R.id.decode_succeeded:
			Log.d(TAG, "Got decode succeeded message");
			state = State.SUCCESS;
			Bundle bundle = message.getData();

			/***********************************************************************/
			Bitmap barcode = bundle == null ? null : (Bitmap) bundle.getParcelable(DecodeThread.BARCODE_BITMAP);

			activity.handleDecode((Result) message.obj, barcode);
			
			break;
		case R.id.decode_failed:
			// We're decoding as fast as possible, so when one decode fails,
			// start another.
			state = State.PREVIEW;
			CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),R.id.decode);
			break;
		case R.id.return_scan_result:
			Log.i(TAG, "Got return scan result message");
			Log.d(TAG,"扫描到了 .........."+ message.obj);
			activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
			activity.finish();
			break;
		case R.id.launch_product_query:
			Log.i(TAG, "Got product query message");
			System.out.println("扫描到了 ........准备跳转"+ message.obj);
			String url = (String) message.obj;
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			activity.startActivity(intent);
			break;
		}
	}

	public void quitSynchronously() {
		state = State.DONE;
		CameraManager.get().stopPreview();
		Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
		quit.sendToTarget();
		try {
			decodeThread.join();
		} catch (InterruptedException e) {
			// continue
		}

		// Be absolutely sure we don't send any queued up messages
		removeMessages(R.id.decode_succeeded);
		removeMessages(R.id.decode_failed);
	}

	private void restartPreviewAndDecode() {
		if (state == State.SUCCESS) {
			state = State.PREVIEW;
			CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
					R.id.decode);
			CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
			activity.drawViewfinder();
		}
	}
}
