package cn.suanzi.baomi.cust.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.suanzi.baomi.base.Const;
import cn.suanzi.baomi.base.TimeCountUtil;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.base.utils.ActivityUtils;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.PopupWindowUtils;
import cn.suanzi.baomi.base.utils.PopupWindowUtils.OnResultListener;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.activity.MyHomeBankListActivity;
import cn.suanzi.baomi.cust.activity.OnlinePayProcotolActivity;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.model.GetSignCardValCode;
import cn.suanzi.baomi.cust.model.SignBankAccountTask;
import cn.suanzi.baomi.cust.util.BankActivityUtils;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 短信验证
 * 
 * @author wensi.yu
 * 
 */
public class MyHomeAddValidateFragment extends Fragment {

	private final static String TAG = MyHomeAddValidateFragment.class.getSimpleName();
	
	private final static String BANK_TYPE_ZERO = "身份证";
	private final static String BANK_TYPE_ONE = "护照";
	private final static String BANK_TYPE_TWO = "军官证";
	private final static String BANK_TYPE_THREE = "士兵证";
	private final static String BANK_TYPE_FOUR = "港澳通行证";
	private final static String BANK_TYPE_FIVE = "临时身份证";
	private final static String BANK_TYPE_SEX = "户口簿";
	private final static String BANK_TYPE_OTHER = "其他";
	private final static String BANK_TYPE_NINE = "警官证";
	private final static String BANK_TYPE_THRE = "外国人居留证";
	public  final static String ORDERNBR = "orderNbr";
	protected static final String MOBILENBR = "MOBILENBR";
	
	/**添加银行卡产生的订单号*/
	private String mOrderNbr;
	
	/**预留的手机号码*/
	private String mMobileNbr;
	
	/** 标题 **/
	final static String BANK_TITLE = "短信验证";

	private boolean mIsAddBankCardNormal = true;
	
	private String mRetrunIdencode;
	
	private String mInputIdencode;
	
	/** 功能描述 */
	@ViewInject(R.id.tv_bank_function_desc)
	TextView mTvBankTitle;
	/** 添加 */
	@ViewInject(R.id.iv_bank_function_add)
	ImageView mTvBankAdd;
	/** 返回 **/
	@ViewInject(R.id.iv_bank_function_backup)
	ImageView mIvBankBack;
	/** 下一步 **/
	@ViewInject(R.id.btn_addvalitade_next)
	Button mBtnAddValitadeNext;
	/** 验证码 **/
	@ViewInject(R.id.tv_addvalitade_send)
	TextView mTvAddValitad;
	/** 输入验证码 **/
	@ViewInject(R.id.tv_addvalitade_input)
	TextView mTvAddValitadInput;
	/**预留手机提示*/
	@ViewInject(R.id.tv_addvalidate_content)
	TextView mTVAddAddValitadContent;
	@ViewInject(R.id.ch_addvalitade_ok)
	CheckBox mAllowPayProcotol;
	/** PopupWindow容器 **/
	private PopupWindow mPopupWindow;
	
	/*private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == Util.NUM_ZERO){ 
				isOpen();
			}
		};
	};*/
	
	/**
	 * 判断用户是否开通了免验证码支付  如果开通了  直接跳转到我的惠圈   否则跳转到开通免验证支付界面
	 */
/*	public void isOpen(){
		new GetUserInfoTask(getActivity(), new GetUserInfoTask.Callback() {
			@Override
			public void getResult(JSONObject object) {
				if(null!=object){
					User user = Util.json2Obj(object.toString(), User.class);
					String freeValCodePay = user.getFreeValCodePay();
					Log.d(TAG, "freeValCodePay---"+freeValCodePay);
					if(String.valueOf(Util.NUM_ZERO).equals(freeValCodePay)){  //0---没有开启
						startActivity(new Intent(getActivity(),SettingNoIndenPayActivity.class));
					}else if(String.valueOf(Util.NUM_ONE).equals(freeValCodePay)){ //1---代表开启
						//TODO
					}
				}
			}
		}).execute();
	}*/
	
	public static MyHomeAddValidateFragment newInstance() {
		Bundle args = new Bundle();
		MyHomeAddValidateFragment fragment = new MyHomeAddValidateFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_myhomeaddvalidate, container, false);// 说明v，注释//
		ViewUtils.inject(this, v);
		
		//--------记录绑定银行卡流程-------
		/*ActivityUtils.add(getActivity());*/
		BankActivityUtils.add(getMyActivity());
				
		Util.addLoginActivity(getMyActivity());
		init();
		return v;
	}
	
	private Activity getMyActivity(){
		Activity act = getActivity();
		if(act==null){
			act=AppUtils.getActivity();
		}
		return act;
	}

	/**
	 * 初始化
	 */
	private void init() {
		//获取传递过来的添加银行卡产生的订单号
		mOrderNbr = getMyActivity().getIntent().getStringExtra(ORDERNBR);
		//获取传递过来的手机号
		mMobileNbr = getMyActivity().getIntent().getStringExtra(MOBILENBR);
		
		mIsAddBankCardNormal = getActivity().getIntent().getBooleanExtra(MyHomeAddBankFragment.NORMALADD, true);
		
		if(mMobileNbr!=null&&!"".equals(mMobileNbr)){  //手机号码中间用*号代替
			mMobileNbr = new StringBuffer(mMobileNbr).replace(3, 7, "****").toString();
		}
		ColorStateList redColor = ColorStateList.valueOf(0xFFFF0000);
		//设置手机号
		/*mTVAddAddValitadContent.setText("您正在开通工银跨界支付并做绑定验证，验证码已发送至您的预留手机："+
		mMobileNbr+"，请按提示进行操作。");*/
		StringBuffer buffer = new StringBuffer();
		buffer.append(Util.getString(R.string.go_icbc_bank));
		buffer.append(mMobileNbr);
		buffer.append(Util.getString(R.string.go_icbc_operation));
		buffer.append(Util.getString(R.string.go_icbc_validate));
		SpannableString ss = new SpannableString(buffer.toString());
		int start1 = buffer.toString().length()-Util.getString(R.string.go_icbc_validate).length();
		ss.setSpan(new TextAppearanceSpan(null, 0, 0, redColor, null), start1, buffer.toString().length(),
				Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		
		mTVAddAddValitadContent.setText(ss);
		// 设置标题
		mTvBankTitle.setText(BANK_TITLE);
		mTvBankAdd.setVisibility(View.GONE);
	}

	/**
	 * 添加成功
	 */
	private void showPopupWindowOk(View v) {
		View view = LayoutInflater.from(getMyActivity()).inflate(R.layout.popuw_addbank_ok, null);
		view.setBackgroundColor(Color.TRANSPARENT);
		// 设置mPopupWindow的宽高
		mPopupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// 获得焦点，点击mPopupWindow以外的地方，窗体消失
		mPopupWindow.setFocusable(true);
		// mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_btncardset));
		mPopupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
		mPopupWindow.setOutsideTouchable(true);
		// 设置mPopupWindow的显示位置
		mPopupWindow.showAsDropDown(v, 200, 10);
		// mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

		Button addBankOk = (Button) view.findViewById(R.id.btn_addbank_ok);
		addBankOk.setOnClickListener(BankListener);// 确定
	}

	OnClickListener BankListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ActivityUtils.finishAll();
			Intent intent = new Intent(getMyActivity(), MyHomeBankListActivity.class);
			getMyActivity().startActivity(intent);
			getMyActivity().finish();
		}
	};

	/**
	 * 添加失败
	 */
	private void showPopupWindowCancel() {
		PopupWindowUtils.showDialog(getActivity(), getStrings(R.string.dialog_add_fail), 
				getStrings(R.string.dialog_fail_reason), getStrings(R.string.dialog_add_again),
				getStrings(R.string.dialog_add_cancle), new  OnResultListener() {
					
					@Override
					public void onOK() { //重试
						signBankAccount();
					}
					
					@Override
					public void onCancel() { //取消
					}
				});
	}

	
	
	/**
	 * 取消
	 */
	OnClickListener BankCancelListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 获得一个用户信息对象
			UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
			String tokenCode = userToken.getTokenCode();

			// new CancelBankAccountTask(getMyActivity()).execute(params);

		}
	};

	/**
	 * 重试
	 */
	OnClickListener BankAgainListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mPopupWindow.dismiss();

		}
	};

	/**
	 * 返回到添加银行卡
	 * 
	 * 下一步到绑定银行卡完成
	 */
	@OnClick({ R.id.iv_bank_function_backup, R.id.btn_addvalitade_next, R.id.tv_addvalitade_send,R.id.ll_pay_procotol })
	public void btnBankReturnClick(final View view) {
		switch (view.getId()) {
		case R.id.iv_bank_function_backup:
			getMyActivity().finish();
			break;
		case R.id.btn_addvalitade_next:

			// 获得值
			SharedPreferences mSharedPreferences = getMyActivity().getSharedPreferences("actaddbank",
					Context.MODE_PRIVATE);
			// 账户姓名
			String accountName = mSharedPreferences.getString("accountName", "");
			// 证件类型
			String idType = mSharedPreferences.getString("idType", "");
			String idPaper = "";
			if (idType.equals(BANK_TYPE_ZERO)) {
				idPaper = CustConst.AddBank.BANK_ZERO;
			}
			if (idType.equals(BANK_TYPE_ONE)) {
				idPaper = CustConst.AddBank.BANK_ONE;
			}
			if (idType.equals(BANK_TYPE_TWO)) {
				idPaper = CustConst.AddBank.BANK_TWO;
			}
			if (idType.equals(BANK_TYPE_THREE)) {
				idPaper = CustConst.AddBank.BANK_THREE;
			}
			if (idType.equals(BANK_TYPE_FOUR)) {
				idPaper = CustConst.AddBank.BANK_FOUR;
			}
			if (idType.equals(BANK_TYPE_FIVE)) {
				idPaper = CustConst.AddBank.BANK_FIVE;
			}
			if (idType.equals(BANK_TYPE_SEX)) {
				idPaper = CustConst.AddBank.BANK_SEX;
			}
			if (idType.equals(BANK_TYPE_OTHER)) {
				idPaper = CustConst.AddBank.BANK_SEVEN;
			}
			if (idType.equals(BANK_TYPE_NINE)) {
				idPaper = CustConst.AddBank.BANK_NINE;
			}
			if (idType.equals(BANK_TYPE_THRE)) {
				idPaper = CustConst.AddBank.BANK_TWEELFTH;
			}
			// 证件号
			String idNbr = mSharedPreferences.getString("idNbr", "");
			// 卡号前六位
			String accountNbrPreSex = mSharedPreferences.getString("accountNbrPreSex", "");
			// 卡号后四位
			String accountNbrLastFour = mSharedPreferences.getString("accountNbrLastFour", "");
			// 预留手机号
			String mobileNbr = mSharedPreferences.getString("mobileNbr", "");

			Log.i(TAG, "accountName+++++++++" + accountName);
			Log.i(TAG, "idType+++++++" + idType);
			Log.i(TAG, "idNbr+++++++" + idNbr);
			Log.i(TAG, "accountNbrPreSex+++++++" + accountNbrPreSex);
			Log.i(TAG, "accountNbrLastFour+++++++" + accountNbrLastFour);
			Log.i(TAG, "mobileNbr++++++++" + mobileNbr);

			
			
			
			//获取验证码**************等短信可以发送后再进行修改
			mInputIdencode = mTvAddValitadInput.getText().toString().trim();
			if(TextUtils.isEmpty(mInputIdencode)){
				/*Toast.makeText(getMyActivity(), "请输入验证码", 0).show();*/
				Util.getContentValidate(R.string.toast_register_indencode);
				return;
			}
			
			//是否勾选协议
			if(!mAllowPayProcotol.isChecked()){
				//Toast.makeText(getMyActivity(), "还未同意在线支付协议", 0).show();
				Util.getContentValidate(R.string.not_agree_pay_protocol);
				return;
			}
			
			
			//绑定银行卡
			signBankAccount();
			
			break;
		case R.id.tv_addvalitade_send:
			// 获得值
			SharedPreferences mSharedPreferences1 = getMyActivity().getSharedPreferences("actaddbank",
					Context.MODE_PRIVATE);
			String mobileNbr1 = mSharedPreferences1.getString("mobileNbr", "");

			// 发送验证码
			TimeCountUtil timeCountUtil = new TimeCountUtil(getMyActivity(), 60000, 1000, mTvAddValitad);
			timeCountUtil.start();

			// 获取验证码
			mTvAddValitad.setEnabled(false);
			new GetSignCardValCode(getMyActivity(), new GetSignCardValCode.Callback() {

				@Override
				public void getResult(String result) {
					if(result!=null){
						mRetrunIdencode = result;
						if(Const.IS_DEBUG){
							//显示验证码
							mTvAddValitadInput.setText(mRetrunIdencode);
						}
						mTvAddValitad.setEnabled(true);
					}else{
						mTvAddValitad.setEnabled(true);
					}
				}
			
			}).execute(mOrderNbr);
			break;
			
		case R.id.ll_pay_procotol:
			getMyActivity().startActivity(new  Intent(getMyActivity(), OnlinePayProcotolActivity.class));
			break;
		}
	}
	
	
	/**
	 * 绑定银行卡
	 */
	public void signBankAccount(){
		new SignBankAccountTask(getMyActivity(), new SignBankAccountTask.Callback() {
			@Override
			public void getResult(boolean result) {
				if(result){ //绑定成功
					//showPopupWindowOk(view);
					if(mIsAddBankCardNormal){
						getMyActivity().startActivity(new Intent(getMyActivity(), MyHomeBankListActivity.class));
					}
					Util.getContentValidate(R.string.success_blind_card);
					BankActivityUtils.finishAll();
				/*	//判断用户是否开通了免验证码支付  如果开通了  直接跳转到我的惠圈   否则跳转到开通免验证支付界面
					mHandler.sendEmptyMessage(Util.NUM_ZERO);*/
				}else{
					//TODO
					//showPopupWindowCancel();
				}
			}
		}).execute(mOrderNbr,mInputIdencode);
	}
	
	
	/**
	 * 获取字符串
	 * @param id
	 * @return
	 */
	private String getStrings (int id) {
		return getActivity().getResources().getString(id);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(MyHomeAddValidateFragment.class.getSimpleName()); 
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(MyHomeAddValidateFragment.class.getSimpleName()); //统计页面
	}
}
