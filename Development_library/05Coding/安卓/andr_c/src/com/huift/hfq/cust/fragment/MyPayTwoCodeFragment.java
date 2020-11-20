package com.huift.hfq.cust.fragment;

import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huift.hfq.base.Const;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.BankList;
import com.huift.hfq.base.pojo.User;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.ImageDownloadCallback;
import com.huift.hfq.base.utils.QrCodeUtils;
import com.huift.hfq.base.utils.TimestampUtil;
import com.huift.hfq.cust.R;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huift.hfq.cust.activity.ICBCBankcardSelectActivity;
import com.huift.hfq.cust.activity.MyHomeAddBankActivity;
import com.huift.hfq.cust.model.GetUserInfoTask;
import com.huift.hfq.cust.model.ListAllBankCardTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的支付二维码
 * 
 * @author yanfang.li
 */
public class MyPayTwoCodeFragment extends Fragment {
	private static final String TAG = MyPayTwoCodeFragment.class.getSimpleName();
	/** 定时发送的时间 */
	private static final int TIME = 60 * 1000;
	/** 用户头像 */
	public static final String USER = "user";
	/** 用户头像*/
	public static final String USER_HEAD = "userHead";
	/** bundle的key*/
	public static final String BUNDLE = "bundle";
	/** 用户对象 */
	private User mUser;
	/** 银行卡列表 */
	private List<BankList> mBankList;
	/** 银行卡对象 */
	private BankList mBank;
	/** 生成二维码的空间 */
	private ImageView mIvPayTwoCode;
	/** 查看大图 */
	private ImageView mIvBigTwoCode;
	/** 银行卡名称 */
	private TextView mTvBankName;
	/** 银行卡卡号 */
	private TextView mTvBankCardNo;
	/** 刷新 */
	private ImageView mIvRefresh;
	/** 大图外层 */
	private LinearLayout mLyBigTwoCode;
	/** 选择银行卡的相对布局 */
	private RelativeLayout mRlPayWay;
	/** 开通惠支付的布局 */
	private LinearLayout mLyOpenHuiPay;
	/** 生成二维码的字符串 */
	private String mCreateTwoCodeStr;
	/** 用户头像 */
	private Bitmap mHeadBitmap;
	/** 二维码*/
	private Bitmap mTwoCodeBitmap;
	/** 是否进行开通绑卡的标识*/
	private boolean mIsBlindCard = false;

	/** 定时执行任务 */
	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				createTwoCode();
				break;
			case 3:
				String netTime = (String) msg.obj;
				getNetForTwoCode(netTime);
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 需要传递参数时有利于解耦
	 */
	public static MyPayTwoCodeFragment newInstance() {
		Bundle args = new Bundle();
		MyPayTwoCodeFragment fragment = new MyPayTwoCodeFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_pay_twocode, container, false);
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
	 * 
	 * @param v
	 *            视图
	 */
	private void findView(View v) {
		mIvPayTwoCode = (ImageView) v.findViewById(R.id.iv_paytwocode);
		mIvBigTwoCode = (ImageView) v.findViewById(R.id.iv_big_paytwocode);
		mLyBigTwoCode = (LinearLayout) v.findViewById(R.id.ly_big_paytwocode);
		mTvBankName = (TextView) v.findViewById(R.id.tv_bank_name);
		mTvBankCardNo = (TextView) v.findViewById(R.id.tv_icbc_no);
		mLyOpenHuiPay = (LinearLayout) v.findViewById(R.id.ly_no_bank_card);
		mRlPayWay = (RelativeLayout) v.findViewById(R.id.rl_pay_way);
		mIvRefresh = (ImageView) v.findViewById(R.id.tv_refresh);
		// 获取屏幕宽高
		WindowManager wm = (WindowManager) getMyActivity().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		int mScreenWidth = outMetrics.widthPixels;
		// 小二维码的尺寸
		setImageWh((int) mScreenWidth * 2 / 3, mIvPayTwoCode);
		// 大图二维码的尺寸
		setImageWh((int) mScreenWidth, mIvBigTwoCode);
	}

	/**
	 * 设置图片尺寸大小
	 * 
	 * @param size
	 * @param imageView
	 */
	private void setImageWh(int size, ImageView imageView) {
		LayoutParams params = (LayoutParams) imageView.getLayoutParams();
		params.height = size;
		params.width = size;
		imageView.setLayoutParams(params);
	}

	/**
	 * 生成二维码
	 */
	private void createTwoCode() {
		TimestampUtil.setRomdonValue(getMyActivity(), new TimestampUtil.CallBack() {

			@Override
			public void getNetTime(String netTime) {
				if (netTime == null) { return; }
				Message message = Message.obtain();
				message.what = 3;
				message.obj = netTime;
				mHandler.sendMessage(message);
			}

		});

	}

	/**
	 * 从网络获取时间生成二维码
	 */
	private void getNetForTwoCode(String netTime) {
		// 十六进制的银行卡信息
		String accountNbrPre6 = "";
		String accountNbrLast4 = "";
		try {
			// 前6
			accountNbrPre6 = Long.toHexString(Long.parseLong(mBank.getAccountNbrPre6()));
			// 后4
			accountNbrLast4 = Long.toHexString(Long.parseLong(mBank.getAccountNbrLast4()));
			int digit = 4 - accountNbrLast4.length(); // 位数
			if (digit > 0) {
				for (int i = 0; i < digit; i++) {
					accountNbrLast4 += TimestampUtil.mLetter[(int) (Math.random() * TimestampUtil.mLetter.length)]
							.toString();
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "银行卡前6后4转换有误：" + e.getMessage());
		}
		/*if (Util.isEmpty(accountNbrPre6) || Util.isEmpty(accountNbrLast4)) {
			mIvPayTwoCode.setEnabled(false);
			mIvRefresh.setEnabled(false);
			mRlPayWay.setEnabled(false);
			mLyOpenHuiPay.setVisibility(View.VISIBLE);
//			mHandler.sendEmptyMessage(2);
			return;
		}*/
		mCreateTwoCodeStr = "payType:" + mUser.getUserCode() + accountNbrPre6 + accountNbrLast4 + netTime;
		if (null != mHeadBitmap && !Util.isEmpty(mCreateTwoCodeStr)) {
			mTwoCodeBitmap = QrCodeUtils.createQrCode(mCreateTwoCodeStr, mHeadBitmap);
			mIvPayTwoCode.setImageBitmap(mTwoCodeBitmap);
		} else if (!Util.isEmpty(mCreateTwoCodeStr)) {
			mTwoCodeBitmap = QrCodeUtils.createQrCode(mCreateTwoCodeStr);
			mIvPayTwoCode.setImageBitmap(mTwoCodeBitmap);
		}
		// 关闭动画
		mIvRefresh.clearAnimation();
		
		mHandler.sendEmptyMessageDelayed(1, TIME);
	}

	/**
	 * 获取银行卡列表
	 */
	
	private void listAllBankCard() {
		new ListAllBankCardTask(getMyActivity(), new ListAllBankCardTask.Callback() {

			@Override
			public void getResult(JSONArray result) {
				if (null != result) {
					try {
						mBankList = new Gson().fromJson(result.toString(), new TypeToken<List<BankList>>() {
						}.getType());
					} catch (Exception e) {
						Log.d(TAG, "Exception:" + e.getMessage());
					}
					mBank = mBankList.get(0);
					setBankInfo(mBank);
					if (null != mBank) {
						try {
							createTwoCode();
						} catch (Exception e2) {
							Log.d(TAG, "Exception  e2:" + e2.getMessage());
						}
					}
					mIvPayTwoCode.setEnabled(true);
					mIvRefresh.setEnabled(true);
					mRlPayWay.setEnabled(true);
					mLyOpenHuiPay.setVisibility(View.GONE);
				} else {
					mIvPayTwoCode.setEnabled(false);
					mIvRefresh.setEnabled(false);
					mRlPayWay.setEnabled(false);
					mLyOpenHuiPay.setVisibility(View.VISIBLE);
				}
			}
		}).execute();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ICBCBankcardSelectActivity.REQUEST_CODE:
			if (resultCode == ICBCBankcardSelectActivity.RESULT_OK) {
				String bankAccountCode = data.getExtras().getString("bankAccountCode");
				String accountNbrLast4 = data.getExtras().getString("accountNbrLast4");
				String accountNbrPre6 = data.getExtras().getString("accountNbrPre6");
				if (null == mBank) {
					mBank = new BankList();
				}
				mBank.setBankAccountCode(bankAccountCode);
				mBank.setAccountNbrLast4(accountNbrLast4);
				mBank.setAccountNbrPre6(accountNbrPre6);
				setBankInfo(mBank);
				createTwoCode();
			}
			break;
		}

	}

	/**
	 * 设置银行卡信息
	 * 
	 * @param bank
	 *            银行卡对象
	 */
	private void setBankInfo(BankList bank) {
		if (null != bank) {
			if (Util.isEmpty(mBank.getBankName())) {
				mTvBankName.setText("银行卡");
			} else {
				mTvBankName.setText("中国建设银行");
				//mTvBankName.setText(mBank.getBankName());
			}
			mTvBankCardNo.setText("**********" + mBank.getAccountNbrLast4());
		}
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		Bundle bundle = getMyActivity().getIntent().getExtras();
		mUser = (User) bundle.getSerializable(USER);
		mHeadBitmap =  bundle.getParcelable(USER_HEAD);
		if (null == mUser) {
			getUserInfo();
		} else {
			listAllBankCard(); // 获取银行卡列表
		}
	}
	
	/**
	 * 得到用户头像的bitMap
	 */
	private void getUserHeadBitMap(String url) {
		Util.getLocalOrNetBitmap(url, new ImageDownloadCallback() {
			@Override
			public void success(final Bitmap bitmap) {
				mHeadBitmap = bitmap;
			}

			@Override
			public void fail() {
				mHeadBitmap = null;
			}
		});

	}

	/**
	 * 重新获取用户信息
	 */
	private void getUserInfo() {
		new GetUserInfoTask(getMyActivity(), new GetUserInfoTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (null != result) {
					mUser = Util.json2Obj(result.toString(), User.class);
					getUserHeadBitMap(Const.IMG_URL + mUser.getAvatarUrl());
					listAllBankCard(); // 获取银行卡列表
				}
			}
		}).execute();

	}

	@OnClick({ R.id.backup, R.id.tv_refresh, R.id.iv_paytwocode, R.id.rl_pay_way, R.id.ly_big_paytwocode,
			R.id.open_hui_pay ,R.id.ly_refresh})
	private void allClickEvent(View view) {
		switch (view.getId()) {
		case R.id.backup: // 回退事件
			getMyActivity().finish();
			break;
		case R.id.tv_refresh: // 手动刷新二维码
		case R.id.ly_refresh: // 手动刷新二维码
			mHandler.removeCallbacksAndMessages(null);
			RotateAnimation animation = new RotateAnimation(0, -360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			animation.setDuration(100);
			animation.setInterpolator(new LinearInterpolator());
			animation.setFillAfter(true);
			animation.setRepeatCount(Integer.MAX_VALUE);
			mIvRefresh.startAnimation(animation);
			if (null != mBank && null != mUser) {
				createTwoCode();
			}
			break;
		case R.id.iv_paytwocode: // 查看大图的二维码
			mIvBigTwoCode.setImageBitmap(mTwoCodeBitmap);
			mLyBigTwoCode.setVisibility(View.VISIBLE);
			break;
		case R.id.ly_big_paytwocode: // 查看大图后关闭
			mLyBigTwoCode.setVisibility(View.GONE);
			break;
		case R.id.rl_pay_way: // 选中银行卡
			startActivityForResult(new Intent(getMyActivity(), ICBCBankcardSelectActivity.class),
					ICBCBankcardSelectActivity.REQUEST_CODE);
			break;
		case R.id.open_hui_pay: // 开动惠支付
			mIsBlindCard = true;
			Intent intentAdd = new Intent(getActivity(), MyHomeAddBankActivity.class);
			intentAdd.putExtra("title", getActivity().getResources().getString(R.string.myhome_add));
			intentAdd.putExtra(MyHomeAddBankFragment.NORMALADD, false);
			startActivity(intentAdd);
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
		MobclickAgent.onPageEnd(MyPayTwoCodeFragment.class.getSimpleName());
	}

	@Override
	public void onStop() {
		super.onStop();
		mHandler.removeCallbacksAndMessages(null);

	}

	@Override
	public void onResume() {
		super.onResume();
		if(mIsBlindCard){
			listAllBankCard();//添加银行卡后回到该界面  重新获取银行卡列表
		}
		MobclickAgent.onPageStart(MyPayTwoCodeFragment.class.getSimpleName()); // 统计页面
	}
}
