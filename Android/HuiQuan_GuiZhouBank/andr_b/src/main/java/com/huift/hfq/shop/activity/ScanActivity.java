package com.huift.hfq.shop.activity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.Activitys;
import com.huift.hfq.base.pojo.Coupon;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.camera.CameraManager;
import com.huift.hfq.shop.decoding.InactivityTimer;
import com.huift.hfq.shop.decoding.ScanningHandler;
import com.huift.hfq.shop.fragment.ActivityVerificationFragment;
import com.huift.hfq.shop.fragment.BillClassFragment;
import com.huift.hfq.shop.fragment.CouponVerificationFragment;
import com.huift.hfq.shop.fragment.HomeFragment;
import com.huift.hfq.shop.fragment.MyOrderManagerFragment;
import com.huift.hfq.shop.fragment.ScanInputAmountFragment;
import com.huift.hfq.shop.model.GetCouponInfoByCodeTask;
import com.huift.hfq.shop.model.GetInfoByActCodeTask;
import com.huift.hfq.shop.view.ViewfinderView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.IOException;
import java.util.Vector;

/***
 * 扫一扫主页
 *
 * @author wensi.yu
 */
public class ScanActivity extends FragmentActivity implements Callback {

    /**
     * 从哪个页面进入的标志
     */
    public final static String FLAG = "flag";
    public static final String INTENT_DATA = "intent_data";

    private ScanningHandler mHandler;
    private ViewfinderView mViewfinderView;
    private boolean mHasSurface;
    private Vector<BarcodeFormat> mDecodeFormats;
    private String mCharacterSet;
    private InactivityTimer mInactivityTimer;
    private MediaPlayer mMediaPlayer;
    private boolean mPlayBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean mVibrate;

    private String resultString;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_scan);
        ViewUtils.inject(this);
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
        Util.addActivity(this);// 手机取消
        init();
    }

    private void init() {
        ActivityUtils.add(this);// 订单完成
        LinearLayout ivBanck = (LinearLayout) findViewById(R.id.layout_turn_in);
        ivBanck.setVisibility(View.VISIBLE);
        LinearLayout lyFlash = (LinearLayout) findViewById(R.id.layout_msg);
        TextView tvFlash = (TextView) findViewById(R.id.tv_msg);
        tvFlash.setVisibility(View.VISIBLE);
        lyFlash.setOnClickListener(flashClick);

        CameraManager.init(getApplication());
        mViewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        mHasSurface = false;
        mInactivityTimer = new InactivityTimer(this);
    }

    /**
     * 闪光灯
     */
    OnClickListener flashClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            CameraManager.get().flashHandler();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.setActivity(this);
        AppUtils.setContext(getApplicationContext());
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (mHasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            // surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        mDecodeFormats = null;
        mCharacterSet = null;
        mPlayBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            mPlayBeep = false;
        } // 没有执行
        initBeepSound();
        mVibrate = true;
        mViewfinderView.setVisibility(View.VISIBLE);
    }

    /**
     * 暂停
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mHandler != null) {
            mHandler.quitSynchronously();
            mHandler = null;
        }
        CameraManager.get().closeDriver();
    }

    /**
     * 销毁
     */
    @Override
    protected void onDestroy() {
        mInactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * 扫描结果进行解码
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        mInactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        resultString = result.getText().replaceAll(" ", "");
        Log.d("QrCode", "扫描的结果=" + resultString);

        if (resultString.equals("")) {
            Util.getContentValidate(R.string.toast_scan_nothing);
            finish();
        } else {
            //事件分发
            String flag = getIntent().getStringExtra(FLAG);

            if (flag.equals(ScanInputAmountFragment.FLAG)) {// 来自ScanInputAmountFragment
                Intent intent = new Intent();
                intent.putExtra(INTENT_DATA, resultString);
                setResult(RESULT_OK, intent);
                finish();
            } else if (flag.equals(BillClassFragment.FLAG)){// 来自BillClassFragment
                Intent intent = new Intent();
                intent.putExtra(INTENT_DATA, resultString);
                setResult(RESULT_OK, intent);
                finish();
            } else if (flag.equals(MyOrderManagerFragment.FLAG)){// 来自MyOrderManagerFragment
                Intent intent = new Intent();
                intent.putExtra(INTENT_DATA, resultString);
                setResult(RESULT_OK, intent);
//                getInfoByActCode(resultString);
                finish();
            } else if (flag.equals(HomeFragment.FLAG)){// 来自HomeFragment
                if (resultString.length() == 10){
                    getInfoByActCode(resultString);
                    finish();
                }else if (resultString.length() == 11) {
                    getCouponInfoByCode(resultString);
                    finish();
                }
            } else {
                Util.showToastZH("扫描结果：" + resultString);
                finish();
            }
        }
    }

    /**
     * 根据输入优惠券验证码获得优惠券的信息
     */
    public void getCouponInfoByCode(String search) {
        new GetCouponInfoByCodeTask(ScanActivity.this, new GetCouponInfoByCodeTask.Callback() {
            @Override
            public void getResult(Coupon coupon) {
                    if (coupon != null) {
                        if (coupon.getCode() == ErrorCode.SUCC) {
                            Intent intent = new Intent(ScanActivity.this, CouponVerificationActivity.class);
                            intent.putExtra(CouponVerificationFragment.COUPON_OBG, coupon);
                            startActivity(intent);
                        } else {
                            Util.showToastZH(coupon.getMsg());
                        }
                    }else {
                        Util.showToastZH("请求失败，请稍后再试！");
                    }
            }
        }).execute(search);
    }

    /**
     * 根据用户活动验证码获得相关信息
     */
    public void getInfoByActCode(String search) {
        new GetInfoByActCodeTask(ScanActivity.this, new GetInfoByActCodeTask.Callback() {
            @Override
            public void getResult(Object result) {
                if (result != null) {
                    Activitys activitys = Util.json2Obj(result.toString(), Activitys.class);
                    if (activitys != null) {
                        Intent intent = new Intent(ScanActivity.this, ActivityVerificationActivity.class);
                        intent.putExtra(ActivityVerificationFragment.ACTIVITY_OBJ, activitys);
                        startActivity(intent);
                    }
                }
            }
        }).execute(search);
    }

    /**
     * 自动聚焦
     *
     * @param surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (mHandler == null) {
            mHandler = new ScanningHandler(this, mDecodeFormats, mCharacterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        if (!mHasSurface) {
            mHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mHasSurface = false;
    }

    public ViewfinderView getViewfinderView() {
        return mViewfinderView;
    }

    public void drawViewfinder() {
        mViewfinderView.drawViewfinder();
    }

    public Handler getHandler() {
        return mHandler;
    }

    private void initBeepSound() {
        if (mPlayBeep && mMediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(beepListener);
            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mMediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mMediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mMediaPlayer.prepare();
            } catch (IOException e) {
                mMediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (mPlayBeep == true && mMediaPlayer != null) {
            mMediaPlayer.start();
        }

        if (mVibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    /**
     * 点击返回按钮
     *
     * @param view
     */
    @OnClick(R.id.layout_turn_in)
    public void btnAclist(View view) {
        finish();
    }
}
