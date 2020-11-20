package cn.suanzi.baomi.cust.fragment;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.TimeCountUtil;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.User;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.model.GetPayPwdIdenCodeTask;
import cn.suanzi.baomi.cust.model.SetPayPwdTask;
import cn.suanzi.baomi.cust.model.ValSSPValCodeTask;
import cn.suanzi.baomi.cust.model.ValidatePayPwdTask;

import com.jungly.gridpasswordview.GridPasswordView;
import com.jungly.gridpasswordview.GridPasswordView.OnPasswordChangedListener;
import com.umeng.analytics.MobclickAgent;
/**
 * 设置支付密码
 * @author yingchen
 */
public class SetPayPwdFragment extends Fragment implements OnClickListener{
	private static final String TAG = SetPayPwdFragment.class.getSimpleName();
	/**确认支付密码*/
	private static final int ENSURE_PAY_PASSWORD = 1;
	/**检查支付密码是否正确*/
	private static final int CHECK_PAY_PASSWORD = 2;
	/**加载的视图*/
	private View mView;
	/**保存的用户信息*/
	private User mUser;
	/**短信验证码输入框*/
	private EditText mIdenInput;
	/**未设置过支付密码   获取短信验证码*/
	private LinearLayout mNoSetPwdLinearLayout;
	/**设置过支付密码  支付密码管理（修改 AND忘记）*/
	private LinearLayout mYesSetPwdLinearLayout;
	/**输入密码*/
	private LinearLayout mInputPwdLinearLayout;
	private GridPasswordView mInputPwdGPV;
	/**确认密码*/
	private LinearLayout mConfrimPwdLinearLayout;
	private GridPasswordView mConfirmPwdGPV;
	/**检查密码(验证输入的密码是否正确)*/
	private LinearLayout mCheckPwdLinearLayout;
	private GridPasswordView mCheckPwdGPV;
	/**第一次输入的密码*/
	private String mFirstInputPwd = "";
	/**当前显示的view*/
	private View mCurrentView;
	/**左平移出去的view的集合*/
	private List<View> mLeftOutViews;
	/**修改支付密码*/
	private LinearLayout mChangePwdLinearLayout;
	/**忘记支付密码*/
	private LinearLayout mForgetPwdLinearLayout;
	/**获取短信验证码*/
	private Button mIdenGet;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ENSURE_PAY_PASSWORD: //确认支付密码
				String ensurePassword = (String) msg.obj;
				new SetPayPwdTask(getActivity(), new SetPayPwdTask.CallBack() {
					@Override
					public void getResult(boolean success) {
						if(success){
							Util.getContentValidate(R.string.set_pay_password_success);
							getActivity().finish();
						}
					}
				}).execute(Util.md5(mFirstInputPwd),Util.md5(ensurePassword));
				break;
			case CHECK_PAY_PASSWORD: //检查支付密码是否正确
				String checkPassword = (String) msg.obj;
				new ValidatePayPwdTask(getActivity(), new ValidatePayPwdTask.CallBack() {
					
					@Override
					public void getResult(JSONObject result) {
						if(null != result){
							String isSuccess = result.get("code").toString();
							if(String.valueOf(Util.NUM_ONE).equals(isSuccess)){  //1---检查支付密码正确
								leftOutAndRightIn(mCheckPwdLinearLayout, mInputPwdLinearLayout);
							}else if(String.valueOf(Util.NUM_ZERO).equals(isSuccess)){ //0---检查支付密码错误
								Util.getContentValidate(R.string.check_pay_pwd_fail);
							}
						}
					}
				}).execute(Util.md5(checkPassword));
				break;
			default:
				break;
			}
		};
	};
	
	public static SetPayPwdFragment newInstance(){
		Bundle args = new Bundle();
		SetPayPwdFragment fragment = new SetPayPwdFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mUser = DB.getObj(DB.Key.CUST_USER, User.class);
		mLeftOutViews = new ArrayList<View>();
		Log.d(TAG, "设置支付密码==="+mUser.getIsUserSetPayPwd());
		mView = inflater.inflate(R.layout.fragment_set_pay_pwd, container,false);
		//初始化视图
		initView();
		return mView;
	}
	
	/**
	 * 初始化视图
	 */
	private void initView(){
		//标题
		TextView titleTextView = ((TextView) mView.findViewById(R.id.tv_mid_content));
		
		//回退
		ImageView backImageView = (ImageView) mView.findViewById(R.id.iv_turn_in);
		backImageView.setOnClickListener(this);
		
		//未设置过支付密码
		mNoSetPwdLinearLayout = (LinearLayout) mView.findViewById(R.id.ll_set_no_paypwd);
		//提示（包含手机号码）
		TextView mobile = (TextView) mView.findViewById(R.id.tv_set_paypwd_mobile);
		String mobileNbr = mUser.getMobileNbr();
		String pre3MobileNbr = mobileNbr.substring(0, 3);
		String lase4MobileNbr = mobileNbr.substring(7,mobileNbr.length());
		mobile.setText("请输入手机号"+pre3MobileNbr+"****"+lase4MobileNbr+"收到的短信验证码");
		//短信输入框
		mIdenInput = (EditText) mView.findViewById(R.id.et_iden_input);
		mIdenGet = (Button) mView.findViewById(R.id.tv_iden_get);
		mIdenGet.setOnClickListener(this);
		//验证
		Button idenCheck = (Button) mView.findViewById(R.id.btn_iden_check);
		idenCheck.setOnClickListener(this);
		
		//设置过支付密码   支付密码管理（修改 AND忘记）
		mYesSetPwdLinearLayout = (LinearLayout) mView.findViewById(R.id.ll_set_yes_paypwd);
		mChangePwdLinearLayout = (LinearLayout) mView.findViewById(R.id.ll_change_pwd);
		mChangePwdLinearLayout.setOnClickListener(this);
		mForgetPwdLinearLayout = (LinearLayout) mView.findViewById(R.id.ll_forget_pwd);
		mForgetPwdLinearLayout.setOnClickListener(this);
		
		
		//输入支付密码
		mInputPwdLinearLayout = (LinearLayout) mView.findViewById(R.id.ll_input_pwd);
		/*TextView inputPwdTextView = (TextView) mView.findViewById(R.id.tv_input_pwd);*/
		mInputPwdGPV = (GridPasswordView) mView.findViewById(R.id.gvpwd_input_pwd);
		
		//确认支付密
		mConfrimPwdLinearLayout = (LinearLayout) mView.findViewById(R.id.ll_confirm_pwd);
		/*TextView confirmPwdTextView = (TextView) mView.findViewById(R.id.tv_confirm_pwd);*/
		mConfirmPwdGPV = (GridPasswordView) mView.findViewById(R.id.gvpwd_confirm_pwd);
		
		//检查支付密码
		mCheckPwdLinearLayout = (LinearLayout) mView.findViewById(R.id.ll_check_pwd);
		/*TextView checkPwdTextView = (TextView) mView.findViewById(R.id.tv_check_pwd);*/
		mCheckPwdGPV = (GridPasswordView) mView.findViewById(R.id.gvpwd_check_pwd);
		
		String isUserSetPayPwd = mUser.getIsUserSetPayPwd();
		//判断用户是否设置过支付密码
		if(String.valueOf(Util.NUM_ZERO).equals(isUserSetPayPwd)){ //0---未设置过
			mNoSetPwdLinearLayout.setVisibility(View.VISIBLE);
			titleTextView.setText(Util.getString(R.string.set_pay_password));
			mCurrentView = mNoSetPwdLinearLayout;
		}else if(String.valueOf(Util.NUM_ONE).equals(isUserSetPayPwd)){ //1---已设置过
			mYesSetPwdLinearLayout.setVisibility(View.VISIBLE);
			titleTextView.setText(Util.getString(R.string.pay_password_manager));
			mCurrentView = mYesSetPwdLinearLayout;
		}
		
		listenOnPasswordView(mInputPwdGPV,mConfirmPwdGPV,mCheckPwdGPV);
			
	}

	/**
	 * 密码输入框的监听
	 * @param inputPwdGPV  输入密码 -- 第一次输入密码
 	 * @param confirmPwdGPV  确认密码  --设置密码
	 * @param checkPwdGPV  检查密码 --检查之前的密码是否正确
	 */
	private void listenOnPasswordView(GridPasswordView inputPwdGPV, GridPasswordView confirmPwdGPV, GridPasswordView checkPwdGPV) {
		//输入密码  --- 设置密码   （密码设置）
		inputPwdGPV.setOnPasswordChangedListener(new OnPasswordChangedListener() {
			@Override
			public void onMaxLength(String psw) {
				mFirstInputPwd = psw;
				leftOutAndRightIn(mInputPwdLinearLayout, mConfrimPwdLinearLayout);
			}
			@Override
			public void onChanged(String psw) {
				
			}
		});
		
		//设置密码 --- 设置结果  （成功finish 失败 提示重新设置）
		confirmPwdGPV.setOnPasswordChangedListener(new OnPasswordChangedListener() {
			@Override
			public void onMaxLength(String psw) {
				if(mFirstInputPwd.equals(psw)){
					//调用API  设置支付密码
					Message message = Message.obtain();
					message.obj = psw;
					message.what = ENSURE_PAY_PASSWORD;
					mHandler.sendMessage(message);
				}else{
					Util.getContentValidate(R.string.set_pay_password_not_same);
				}
			}
			
			@Override
			public void onChanged(String psw) {
				Log.d(TAG, "confirmPwdGPV =="+psw);
			}
		});
		
		
		/*检查密码  --- ①输入密码 ---确认密码（密码重置）        
					② finish ---取消支付密码的功能   MAYBE TODO
		*/
		mCheckPwdGPV.setOnPasswordChangedListener(new OnPasswordChangedListener() {
			@Override
			public void onMaxLength(String psw) {
				/*
				if("123456".equals(psw)){
					leftOutAndRightIn(mCheckPwdLinearLayout, mInputPwdLinearLayout);
				}*/
				//检查密码是否正确
				Message message = Message.obtain();
				message.what = CHECK_PAY_PASSWORD;
				message.obj = psw;
				mHandler.sendMessage(message);
			}
			
			@Override
			public void onChanged(String psw) {
				
			}
		});
	}

	
	
	/**
	 * 从左侧平移出去  右侧平移进来
	 * @param leftOutView 左侧平移出去的视图
	 * @param rightInView 右侧平移进来的视图
	 */
	private void leftOutAndRightIn(final View leftOutView,final View rightInView){
	
		TranslateAnimation leftOut = (TranslateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.left_out);
		leftOutView.startAnimation(leftOut);
		leftOut.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				setView(leftOutView,rightInView);
				leftOutView.setVisibility(View.GONE);
				rightInView.setVisibility(View.VISIBLE);
				
				TranslateAnimation rightIn = (TranslateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.right_in);
				rightInView.startAnimation(rightIn);
			}
		});
		mCurrentView = rightInView;
		mLeftOutViews.add(leftOutView);
		
		
	}
	
	
	/**
	 * 动画之间一些视图的处理（focus enabled）
	 * @param outView
	 * @param inView
	 */
	private void setView(View outView, View inView) {
		
		if(inView == mYesSetPwdLinearLayout){
			mYesSetPwdLinearLayout.setEnabled(true);
			mChangePwdLinearLayout.setEnabled(true);
			mForgetPwdLinearLayout.setEnabled(true);
		}else if(inView == mNoSetPwdLinearLayout ){
			mNoSetPwdLinearLayout.setEnabled(true);
			mIdenInput.requestFocus();
			mIdenGet.setEnabled(true);
		}else if(inView == mInputPwdLinearLayout){
			mInputPwdLinearLayout.setEnabled(true);
			mInputPwdGPV.requestFocus();
			mInputPwdGPV.setEnabled(true);
		}else if(inView == mConfrimPwdLinearLayout){
			mConfrimPwdLinearLayout.setEnabled(true);
			mConfirmPwdGPV.requestFocus();
			mConfirmPwdGPV.setEnabled(true);
		}else if(inView == mCheckPwdLinearLayout){
			mCheckPwdLinearLayout.setEnabled(true);
			mCheckPwdGPV.requestFocus();
			mCheckPwdGPV.setEnabled(true);
		}
		
		if(outView == mYesSetPwdLinearLayout){
			mYesSetPwdLinearLayout.setEnabled(false);
			mChangePwdLinearLayout.setEnabled(false);
			mForgetPwdLinearLayout.setEnabled(false);
		}else if(outView == mNoSetPwdLinearLayout){
			mNoSetPwdLinearLayout.setEnabled(false);
			mIdenInput.clearFocus();
			mIdenGet.setEnabled(false);
		}else if(outView == mInputPwdLinearLayout){
			mInputPwdLinearLayout.setEnabled(false);
			mInputPwdGPV.clearPassword();
			mInputPwdGPV.clearFocus();
			mInputPwdGPV.setEnabled(false);
		}else if(outView == mConfrimPwdLinearLayout){
			mConfrimPwdLinearLayout.setEnabled(false);
			mConfirmPwdGPV.clearPassword();
			mConfirmPwdGPV.clearFocus();
			mConfirmPwdGPV.setEnabled(false);
		}else if(outView == mCheckPwdLinearLayout){
			mCheckPwdLinearLayout.setEnabled(false);
			mCheckPwdGPV.clearPassword();
			mCheckPwdGPV.clearFocus();
			mCheckPwdGPV.setEnabled(false);
		}
		
	
	}

	/**
	 * 从右侧平移出去  左侧平移进来
	 * @param rightOutView 右侧平移出去的视图
	 * @param leftInView 左侧平移进来的视图
	 */
	private void rightOutLeftIn(final View rightOutView,final View leftInView){	
		TranslateAnimation rightOut = (TranslateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.right_out);
		rightOutView.startAnimation(rightOut);
		rightOut.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				setView(rightOutView, leftInView);
				rightOutView.setVisibility(View.GONE);
				leftInView.setVisibility(View.VISIBLE);
				TranslateAnimation rightIn = (TranslateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.left_in);
				leftInView.startAnimation(rightIn);
			}
		});
		mCurrentView = leftInView;
		//移除
		mLeftOutViews.remove(mLeftOutViews.size()-1);
	}


	/**
	 * 点击的回掉
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_turn_in:
			/*hideKeyBord();*/
			if(null == mLeftOutViews || mLeftOutViews.size() == 0){
				getActivity().finish();
			}else{
				View view = mLeftOutViews.get(mLeftOutViews.size()-1);
				Log.d(TAG, "mLeftOutViews size == "+mLeftOutViews.size());
				rightOutLeftIn(mCurrentView, view);
			}
			break;
			
		case R.id.tv_iden_get: //获取短信验证码
			Log.d(TAG, "获取短信验证码");
			TimeCountUtil timeCountUtil = new TimeCountUtil(getActivity(), 60000, 1000, mIdenGet);
			timeCountUtil.start();

			mIdenGet.setEnabled(false);
			// 获取短信验证码
			new GetPayPwdIdenCodeTask(getActivity(),new GetPayPwdIdenCodeTask.Callback() {
				
				@Override
				public void getResult(String idenCode) {
					if(null != idenCode){
						Log.d(TAG, "GetPayPwdIdenCodeTask==="+idenCode);
					}
					mIdenGet.setEnabled(true);
				}
			}).execute(mUser.getMobileNbr());
			break;
			
		case R.id.btn_iden_check: //短线验证码验证
			String inputIdenCode = mIdenInput.getText().toString().trim();
			if(Util.isEmpty(inputIdenCode)){
				Util.getContentValidate(R.string.toast_register_indencode);
			}else{
				new ValSSPValCodeTask(getActivity(), new ValSSPValCodeTask.CallBack() {
					
					@Override
					public void getResult(JSONObject result) {
						String isSuccess = result.get("code").toString();
						if(String.valueOf(Util.NUM_ONE).equals(isSuccess)){ //1---验证正确
							leftOutAndRightIn(mNoSetPwdLinearLayout, mInputPwdLinearLayout);
						}else if(String.valueOf(Util.NUM_ZERO).equals(isSuccess)){ //0---验证失败
							Util.getContentValidate(R.string.check_iden_code_fail);
						}
					}
				}).execute(mUser.getMobileNbr(),inputIdenCode);
			}
			break;
			
		case R.id.ll_change_pwd://修改密码
			leftOutAndRightIn(mYesSetPwdLinearLayout, mCheckPwdLinearLayout);
			break;
			
		case R.id.ll_forget_pwd: //忘记密码
			leftOutAndRightIn(mYesSetPwdLinearLayout, mNoSetPwdLinearLayout);
			break;
		default:
			break;
		}
		
	}
	
	/**
	 * 关闭软键盘
	 */
	/*private void hideKeyBord() {
		View view = getActivity().getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}*/
	
	@Override
	public void onStop() {
		super.onStop();
		mHandler.removeCallbacksAndMessages(null);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(SetPayPwdFragment.class.getSimpleName());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(SetPayPwdFragment.class.getSimpleName()); // 统计页面
	}
}
