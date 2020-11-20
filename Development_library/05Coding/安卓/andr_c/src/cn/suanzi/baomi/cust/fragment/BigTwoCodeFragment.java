package cn.suanzi.baomi.cust.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import cn.suanzi.baomi.base.Const;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.pojo.BankList;
import cn.suanzi.baomi.base.pojo.User;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.ImageDownloadCallback;
import cn.suanzi.baomi.base.utils.QrCodeUtils;
import cn.suanzi.baomi.base.utils.TimestampUtil;
import cn.suanzi.baomi.cust.R;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的支付二维码
 * @author yanfang.li
 */
public class BigTwoCodeFragment extends Fragment {
	private static final String TAG = BigTwoCodeFragment.class.getSimpleName();
	/** 定时发送的时间*/
	private static final int TIME = 60*1000;
	/** 用户头像*/
	public static final String USER= "user";
	/** 银行卡对象*/
	public static final String BANK= "bank";
	/** 用户对象*/
	private User mUser;
	/** 银行卡对象*/
	private BankList mBank; 
	/** 生成二维码的空间*/
	private ImageView mIvPayTwoCode;
	/** 生成二维码的字符串*/
	private String mCreateTwoCodeStr; 
	/** 银行卡*/
	private String mBankCodeHex = "";
	
	/** 定时执行任务*/
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			createTwoCode();
		};
	};
	
	/**
	 * 需要传递参数时有利于解耦
	 */
	public static BigTwoCodeFragment newInstance() {
		Bundle args = new Bundle();
		BigTwoCodeFragment fragment = new BigTwoCodeFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_big_twocode, container, false);
		ViewUtils.inject(this, v);
		findView(v);
		initData();
		return v;
	}
	
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}
	
	/**
	 * 查找控件
	 * @param v 视图
	 */
	private void findView(View v) {
		mIvPayTwoCode = (ImageView) v.findViewById(R.id.iv_big_paytwocode);
		// 获取屏幕宽高
		WindowManager wm = (WindowManager) getMyActivity().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		int mScreenWidth = outMetrics.widthPixels;
		// 将px装换为dp
		LayoutParams params = (LayoutParams) mIvPayTwoCode.getLayoutParams();
		params.height = mScreenWidth;
		params.width = mScreenWidth;
		mIvPayTwoCode.setLayoutParams(params);
	}
	
	
	/**
	 * 生成二维码
	 */
	private void createTwoCode () {
		TimestampUtil.setRomdonValue(getMyActivity(),new TimestampUtil.CallBack() {
			
			@Override
			public void getNetTime(String netTime) {
				if (netTime == null) {
					return;
				}
				getNetForTwoCode(netTime);
			}

		});
		
	
	}
	
	/**
	 * 从网络获取时间生成二维码
	 */
	private void getNetForTwoCode(String netTime) {
		Log.d(TAG, "createTwoCode3");
		// 十六进制的银行卡信息
		try {
			mBankCodeHex = Long.toHexString(Long.parseLong(mBank.getAccountNbrPre6())) + Long.toHexString(Long.parseLong(mBank.getAccountNbrLast4()));
			Log.d(TAG, "mCreateTwoCodeStr:"+ Long.toHexString(Long.parseLong(mBank.getAccountNbrPre6())) +", 前6："+Long.toHexString(Long.parseLong(mBank.getAccountNbrPre6())).length());
			Log.d(TAG, "mCreateTwoCodeStr:"+ mBank.getAccountNbrLast4() +"，16："+Long.toHexString(Long.parseLong(mBank.getAccountNbrLast4()))+", 后4："+Long.toHexString(Long.parseLong(mBank.getAccountNbrLast4())).length());
		} catch (Exception e) {
			Log.e(TAG, "银行卡前6后4转换有误："+e.getMessage());
		}
		mCreateTwoCodeStr = "payType:" + mUser.getUserCode() + mBankCodeHex + netTime;
		if(!Util.isEmpty(mUser.getAvatarUrl()) && !Util.isEmpty(mCreateTwoCodeStr)){
			String url = Const.IMG_URL + mUser.getAvatarUrl();
				Util.getLocalOrNetBitmap(url, new ImageDownloadCallback(){
					@Override
					public void success(final Bitmap bitmap) {
						//生成二维码
						getMyActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								mIvPayTwoCode.setImageBitmap(QrCodeUtils.createQrCode(mCreateTwoCodeStr, bitmap));
								if(bitmap.isRecycled() == false){
									bitmap.recycle();
								}
							}
						});
					}
					@Override
					public void fail() {
						Log.d(TAG, "出问题啦。。。。。。。。。。。");
						//生成二维码
						getMyActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								mIvPayTwoCode.setImageBitmap(QrCodeUtils.createQrCode(mCreateTwoCodeStr));
							}
						});
					}
				});
		} else {
			mIvPayTwoCode.setImageBitmap(QrCodeUtils.createQrCode(mCreateTwoCodeStr));
		}
		mHandler.sendEmptyMessageDelayed(1, TIME);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		mUser = (User) getMyActivity().getIntent().getSerializableExtra(USER);
		mBank = (BankList) getMyActivity().getIntent().getSerializableExtra(BANK);
		if (null != mUser && null != mBank) {
			createTwoCode();
		}
	}
	
	@OnClick({R.id.rl_big_paytwocode,R.id.iv_big_paytwocode})
	private void allClickEvent (View view) {
		switch (view.getId()) {
		case R.id.rl_big_paytwocode:
		case R.id.iv_big_paytwocode:
			getMyActivity().finish();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 友盟统计
	 */
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(BigTwoCodeFragment.class.getSimpleName());
	}
	
	@Override
	public void onStop() {
		super.onStop();
		mHandler.removeCallbacksAndMessages(null);
		
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(BigTwoCodeFragment.class.getSimpleName()); // 统计页面
	}
}
