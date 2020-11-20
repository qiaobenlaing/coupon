package com.huift.hfq.cust.fragment;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huift.hfq.base.Const;
import com.huift.hfq.base.TimeCountUtil;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.BankList;
import com.huift.hfq.base.pojo.User;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.base.utils.QrCodeUtils;
import com.huift.hfq.cust.R;

import com.google.zxing.WriterException;
import com.huift.hfq.cust.activity.H5ShopDetailActivity;
import com.huift.hfq.cust.activity.ICBCBankcardSelectActivity;
import com.huift.hfq.cust.activity.MyHomeAddBankActivity;
import com.huift.hfq.cust.activity.NewShopInfoActivity;
import com.huift.hfq.cust.activity.PayResultActivity;
import com.huift.hfq.cust.application.CustConst;
import com.huift.hfq.cust.model.BankcardPayConfirmTask;
import com.huift.hfq.cust.model.CancelBankcardPayTask;
import com.huift.hfq.cust.model.GetIcbcPayValCodeTask;
import com.huift.hfq.cust.model.MyHomeBankListTask;
import com.huift.hfq.cust.model.POCancelBankcardPayTask;
import com.huift.hfq.cust.model.ValidatePayPwdTask;
import com.huift.hfq.cust.util.SkipActivityUtil;
import com.jungly.gridpasswordview.GridPasswordView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 工行在线支付
 * @author yingchen
 *
 */
public class ICBCOnlinePayFragment extends Fragment {

	public static final String TAG = ICBCOnlinePayFragment.class.getSimpleName();
	public static final String SHOP_CODE = "shop_code";
	public static final String SHOP_NAME = "shop_name";
	public static final String REAL_PAY = "real_pay";
	/** 用户支付记录编码标记*/
	public static final String REAL_CONSUMECODE = "REAL_CONSUMECODE";
	public static final String ORDERNBR = "orderNbr";
	/** 标记是银行卡 还是外卖等产品的支付  以便取消订单调用不同的API;*/
	public static final String IS_BANK_PAY = "is_bank_pay";
	

	@ViewInject(R.id.shop_name)
	private TextView mShopNameTextView;
	@ViewInject(R.id.consume_code)
	private TextView mConsumeCodeTextView;
	@ViewInject(R.id.real_pay)
	private TextView mRealPayTextView;
	@ViewInject(R.id.edt_mobileNbr)
	private EditText mEdtMobileNbr;
	@ViewInject(R.id.right_one)
	private Button mButtonGetIden;
	@ViewInject(R.id.tv_card)
	private TextView mCardTextView;
	@ViewInject(R.id.idenCode)
	private EditText mIdenCodeEditText;
	@ViewInject(R.id.submit)
	private Button mButtonSubmit;
	@ViewInject(R.id.bank_card)
	private RelativeLayout mRelativeLayoutSelectCard;
	@ViewInject(R.id.edt_mobileNbr)
	private EditText mEditTextInputMbNbr;
	@ViewInject(R.id.ll_pay_online)
	private LinearLayout mPayOnlineLinearLayout;
	@ViewInject(R.id.arrow)
	private ImageView mArrow;
	@ViewInject(R.id.iv_barcode)
	private ImageView mIvBarCode;
	private String mShopCode, mShopName, mRealPay, mCardNo, mBankAccountCode;
	

	/**是否需要输入验证码  默认为true*/
	private boolean mNeedInputIdenCode  = true ;
	
	private String mReal_consumeCode ;//用户支付记录编码
	
	private String mOrderNbr = ""; // 订单号
	
	private String mIdenCode = "";
	
	private String mMobileNbr = "";
	
	//private String returnIden;
	
	private  List<BankList> mLvBankListData;//银行卡列表
	
	private boolean mIsBankCard = true;
	
	/**弹出支付密码的PopWindow*/
	private PopupWindow mPayPwdPopWindow;
	/**支付密码的PopWindow加载的视图*/
	private View mPayPwdView;
	/**支付PopWindow的银行卡*/
	private TextView mBankCardTextView;
	
	
	/**
	 * 需要传递参数时有利于解耦
	 */
	public static ICBCOnlinePayFragment newInstance() {
		Bundle args = new Bundle();
		ICBCOnlinePayFragment fragment = new ICBCOnlinePayFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_shop_icbc_online_pay, container, false);
		ViewUtils.inject(this, v);
		
		//****记录买单流程*********
		ActivityUtils.add(getMyActivity());
		Util.addLoginActivity(getMyActivity());
		init(v);
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
	
	private void initData() {
		mShopNameTextView.setText(mShopName);
		mConsumeCodeTextView.setText("订单号:" + mOrderNbr);
		
		String newPay = "";
		if (Util.isEmpty(mRealPay)) {
			newPay = "0";
		} else {
			newPay = String.format("%.2f", Double.parseDouble(mRealPay));//保留两位小数
		}
		mRealPayTextView.setText(newPay + " 元");
		
	}

	private void init(View v) {
		mIsBankCard = getActivity().getIntent().getBooleanExtra(IS_BANK_PAY, true);
		mShopCode = getMyActivity().getIntent().getStringExtra(SHOP_CODE);
		mShopName = getMyActivity().getIntent().getStringExtra(SHOP_NAME);
		mRealPay = getMyActivity().getIntent().getStringExtra(REAL_PAY);
		//consumeCode = getMyActivity().getIntent().getStringExtra(CONSUME_CODE);
		mOrderNbr = getMyActivity().getIntent().getStringExtra(ORDERNBR);
		Log.d(TAG, " mOrderNbr =  >>>> hou " + mOrderNbr);
		//生成条形码
		try {
			mIvBarCode.setImageBitmap(QrCodeUtils.CreateOneDCode(mOrderNbr));
		} catch (WriterException e) {
			Log.e(TAG, "生成条形码 >>> " + e.getMessage());
		}
		mReal_consumeCode = getMyActivity().getIntent().getStringExtra(REAL_CONSUMECODE);
		
		//金额 AND是否开启免验证码的判断
		//isOpenAndPay();
	
		//金额AND是否开启支付密码
		isSetPwdForPay();
		
		//获取银行卡列表
		getBankAccountList();
	}

	/**
	 * 金额小于300且开启过支付密码   则不需要验证码  
	 * 					         否则需要验证码
	 */
	public void isSetPwdForPay(){
		//实付金额
		Double relpay = Double.parseDouble(mRealPay);
		
		User user = DB.getObj(DB.Key.CUST_USER, User.class);
		String isUserSetPayPwd = user.getIsUserSetPayPwd();
		Log.d(TAG, "isUserSetPayPwd===="+isUserSetPayPwd);
		
		//判断是否开启过支付密码 isUserSetPayPwd----1(开启过)   0(未开启)
		if(relpay<=300&&String.valueOf(Util.NUM_ONE).equals(isUserSetPayPwd)){
			mNeedInputIdenCode = false;
		}else{
			mNeedInputIdenCode = true;
		}
		
		//视图显示
		if(mNeedInputIdenCode){  //需要输入验证码
			mPayOnlineLinearLayout.setVisibility(View.VISIBLE);
			mRelativeLayoutSelectCard.setVisibility(View.VISIBLE);
		}else{  //不需要输入验证码
			mPayOnlineLinearLayout.setVisibility(View.GONE);
			mRelativeLayoutSelectCard.setVisibility(View.GONE);
		}
										
	}	
	
	
	/**
	 * 验证金额是否大于300元 和 是否开启过免验证码支付的功能
	 * 1.金额>300  需要输入验证码
	 * 2.金额<=300 开启免验证码   不需要输入验证码
	 * 3.金额<=300 未开启免验证码  需要输入验证码
	 */
	/*private void isOpenAndPay() {
		//实付金额
		Double relpay = Double.parseDouble(mRealPay);
		//是否开启过免验证码功能
		User user = DB.getObj(DB.Key.CUST_USER, User.class);
		String freeValCodePay  = "0";
		if (null == user) {
			freeValCodePay = "0";
		} else {
			freeValCodePay = user.getFreeValCodePay();
		}
		
		if(relpay<=300&&String.valueOf(Util.NUM_ONE).equals(freeValCodePay)){
			mNeedInputIdenCode =false;
		}else{
			mNeedInputIdenCode = true;
		}
		
		if(mNeedInputIdenCode){  //需要输入验证码
			mPayOnlineLinearLayout.setVisibility(View.VISIBLE);
		}else{  //不需要输入验证码
			mPayOnlineLinearLayout.setVisibility(View.GONE);
		}
	}*/

	/**
	 * @param v
	 */
	@OnClick({ R.id.backup, R.id.submit, R.id.bank_card,R.id.right_one})
	private void click(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.backup: //回退跳到订单详情界面
			
			showPayOrCancleDialog();//弹出支付或者取消的对话框
			break;
		case R.id.bank_card:
			
			
			intent = new Intent(getMyActivity().getApplicationContext(), ICBCBankcardSelectActivity.class);
			startActivityForResult(intent, ICBCBankcardSelectActivity.REQUEST_CODE);
			break;
		case R.id.submit:
			
			MobclickAgent.onEvent(getActivity(), "online_pay_ok");
			/**暂时屏蔽*/
			/*intent = new Intent(getMyActivity().getApplicationContext(), ICBCPasswordActivity.class);
			
			//携带金额
			intent.putExtra(ICBCPasswordActivity.NEED_PAY, realPay);
			getActivity().startActivity(intent);*/
			
			
			if(mLvBankListData==null||mLvBankListData.size()==0){
				//跳转到银行卡列表
				intent = new  Intent(getMyActivity(),MyHomeAddBankActivity.class);
				intent.putExtra(MyHomeAddBankFragment.BLINDANDPAY, true);
				intent.putExtra(MyHomeAddBankFragment.CONSUME_CODE, mReal_consumeCode);
				intent.putExtra(MyHomeAddBankFragment.SHOPE_CODE, mShopCode);
				intent.putExtra(MyHomeAddBankFragment.IS_BANK_PAY, mIsBankCard);

				getMyActivity().startActivity(intent);
				return;
			}
			
			mMobileNbr = mEdtMobileNbr.getText().toString();
			
			//判断验证码是否为空
			if(TextUtils.isEmpty(mIdenCodeEditText.getText().toString().trim())&&mNeedInputIdenCode){
				Util.getContentValidate(R.string.toast_register_indencode);
				return;
			}
			
			//验证码
			mIdenCode = mIdenCodeEditText.getText().toString().trim();
		
			if(mNeedInputIdenCode){  //需要验证码支付
				confrimPayOnlien(); 
			}else{
			
				showPayPwdWindow();
			}
			
			break;
		case R.id.right_one:
			mMobileNbr = mEdtMobileNbr.getText().toString();
		
			if (Util.isEmpty(mMobileNbr)) {
				Util.getContentValidate(R.string.toast_register_phone);
				break;
			}
			
			//手机号码格式不正确
			if(Util.isPhone(getMyActivity(), mMobileNbr)){
				Util.getContentValidate(R.string.toast_register_format);
				break;
			}
			
			
			// 发送验证码
			TimeCountUtil timeCountUtil = new TimeCountUtil(getMyActivity(), 60000, 1000, mButtonGetIden);
			timeCountUtil.start();
			String params[] = {mReal_consumeCode, mBankAccountCode, mMobileNbr };
			
			
			mEdtMobileNbr.setEnabled(false);
			new GetIcbcPayValCodeTask(getMyActivity(), new GetIcbcPayValCodeTask.Callback() {

				@Override
				public void getResult(String result) {
					if(result!=null){ //获取验证码成功   result==返回的验证码   待修改（以短信的形式返回）
						
						//显示验证码
						if(Const.IS_DEBUG){
							//idenCodeEditText.setText(returnIden);
						}
						mEdtMobileNbr.setEnabled(true);
						
					}else{
					
						mEdtMobileNbr.setEnabled(true);
					}
				}
				
			}).execute(params);
			break;
		}
	} 
	
	/**
	 * 弹出支付密码输入框
	 */
	private void showPayPwdWindow(){
		if(null == mPayPwdView){
			mPayPwdView = View.inflate(getActivity(), R.layout.pop_pay_pwd, null);
			
			//X 按钮 关闭popWindow
			TextView popDismis = (TextView) mPayPwdView.findViewById(R.id.iv_pop_close);
			popDismis.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mPayPwdPopWindow.dismiss();
					mPayPwdPopWindow = null;
					mPayPwdView = null ;
				}
			});
		
			//支付金额 
			TextView totalPrice = (TextView) mPayPwdView.findViewById(R.id.tv_total_price_pop);
			totalPrice.setText(String.format("%.2f", Double.parseDouble(mRealPay)));
			
			mBankCardTextView = (TextView) mPayPwdView.findViewById(R.id.tv_bank_card_pop);
			if(mLvBankListData!=null&&mLvBankListData.size()>0){
				BankList bankList = mLvBankListData.get(0);
				mBankAccountCode = bankList.getBankAccountCode();
				String accountNbrLast4 = bankList.getAccountNbrLast4();
				mBankCardTextView.setText("建行卡" + "******" + accountNbrLast4);
			}
			
			//选择银行卡 
			RelativeLayout bankCardSelect = (RelativeLayout) mPayPwdView.findViewById(R.id.rl_select_card_pop);
			bankCardSelect.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getMyActivity().getApplicationContext(), ICBCBankcardSelectActivity.class);
					startActivityForResult(intent, ICBCBankcardSelectActivity.PAY_PWD_REQUEST_CODE);
				}
			});

			
			//支付密码框
			GridPasswordView inputPassWordView = (GridPasswordView) mPayPwdView.findViewById(R.id.gpv_input_pop);
			inputPassWordView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
				
				@Override
				public void onMaxLength(String psw) {
					new ValidatePayPwdTask(getActivity(), new ValidatePayPwdTask.CallBack() {
						
						@Override
						public void getResult(JSONObject result) {
							if(null != result){
								String isSuccess = result.get("code").toString();
								if(String.valueOf(Util.NUM_ONE).equals(isSuccess)){  //1---检查支付密码正确
									Log.d(TAG, "paypwd === "+mReal_consumeCode);
									Log.d(TAG, "paypwd === "+mBankAccountCode);
									Log.d(TAG, "paypwd === "+mIdenCode);
									confrimPayOnlien();
								}else if(String.valueOf(Util.NUM_ZERO).equals(isSuccess)){ //0---检查支付密码错误
									Util.getContentValidate(R.string.check_pay_pwd_fail);
								}
							}
						}
					}).execute(Util.md5(psw));
				}
				
				@Override
				public void onChanged(String psw) {
					
				}
			});
		}
		
		if(null == mPayPwdPopWindow){
			mPayPwdPopWindow = new PopupWindow(mPayPwdView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			mPayPwdPopWindow.setFocusable(true);
			mPayPwdPopWindow.setInputMethodMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
	                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		}
		mPayPwdPopWindow.showAtLocation(mPayPwdView, Gravity.CENTER, 0, 0);
	}
	
	/**
	 * 弹出支付或者取消的对话框
	 */
	public void showPayOrCancleDialog(){
		DialogUtils.showDialog(getMyActivity(), Util.getString(R.string.dialog_pay),Util.getString(R.string.dialog_pay_content), Util.getString(R.string.dialog_continue_pay),
				Util.getString(R.string.dialog_cancel_pay), new DialogUtils().new OnResultListener() {
					@Override
					public void onCancel() {
						//确认取消支付
						canclePay();
					}
		});
		
	}

	
	
	/**
	 * 取消支付
	 */
	
	public  void  canclePay(){
		if(mIsBankCard){//银行卡支付取消
			new CancelBankcardPayTask(getActivity(), new CancelBankcardPayTask.Callback() {
				
				@Override
				public void getResult(boolean result) {
					if(result){//取消支付成功  
						ActivityUtils.finishAll();
						DB.saveBoolean(CustConst.Key.CANCEL_ORDER_ISSUCCESS, true);
						//SkipActivityUtil.skipH5ShopDetailActivity(getMyActivity(), mShopCode,CustConst.HactTheme.H5SHOP_DETAIL);
						/*Intent intent = new Intent(getActivity(), NewShopInfoActivity.class);
						intent.putExtra("shopCode", mShopCode);
						getActivity().startActivity(intent);*/
					}else{
						//TODO
					}
				}
			}).execute(mReal_consumeCode);
		}else{ //外卖等产品支付取消
			new POCancelBankcardPayTask(getActivity(), new POCancelBankcardPayTask.Callback() {
				
				@Override
				public void getResult(boolean result) {
					if(result){//取消支付成功  
						ActivityUtils.finishAll();
						//SkipActivityUtil.skipH5ShopDetailActivity(getMyActivity(), mShopCode,CustConst.HactTheme.H5SHOP_DETAIL);
						/*Intent intent = new Intent(getActivity(), NewShopInfoActivity.class);
						intent.putExtra("shopCode", mShopCode);
						getActivity().startActivity(intent);*/
					}else{
						//TODO
					}
				}
			}).execute(mReal_consumeCode);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ICBCBankcardSelectActivity.REQUEST_CODE:
			if (resultCode == ICBCBankcardSelectActivity.RESULT_OK) {
				mBankAccountCode = data.getExtras().getString("bankAccountCode");
				String accountNbrLast4 = data.getExtras().getString("accountNbrLast4");
				String mobileNbr = data.getExtras().getString("mobileNbr");
				mCardTextView.setText("建行卡" + "******" + accountNbrLast4);
				mEditTextInputMbNbr.setText(mobileNbr);
			}
			break;
			
		case ICBCBankcardSelectActivity.PAY_PWD_REQUEST_CODE:
			if(resultCode == ICBCBankcardSelectActivity.RESULT_OK){
				mBankAccountCode = data.getExtras().getString("bankAccountCode");
				String accountNbrLast4 = data.getExtras().getString("accountNbrLast4");
				mBankCardTextView.setText("建行卡" + "******" + accountNbrLast4);
			}
		}
		
	}
	
	
	/**
	 * 确认在线支付
	 */
	public void confrimPayOnlien(){
		new BankcardPayConfirmTask(getMyActivity(), new BankcardPayConfirmTask.Callback() {
			@Override
			public void getResult(Boolean result,String retMesg) { //若成功的话  retMesg = orderCode  失败的话 retMesg = 错误代码
				Intent intent = new Intent(getActivity(), PayResultActivity.class); 
				intent.putExtra(PayResultActivity.SHOPE_CODE, mShopCode);
				intent.putExtra(PayResultActivity.IS_BANK_PAY, mIsBankCard);
				if(result){ //支付成功
					intent.putExtra(PayResultActivity.PAY_RESULT, true);
					intent.putExtra(PayResultActivity.CONSUMECODE, mReal_consumeCode);
					intent.putExtra(PayResultActivity.ORDER_CODE, retMesg);
					startActivity(intent);
				}else{
					Log.d(TAG, "支付失败错误代码"+retMesg);
					intent.putExtra(PayResultActivity.FAIL_RET_CODE, retMesg);
					intent.putExtra(PayResultActivity.PAY_RESULT, false);
					
					if("B2621".equals(retMesg)||"B0280".equals(retMesg)||"4102".equals(retMesg)){
						//如果是  验证码输入错误(B2621)  或者 余额不足(B0280或4102) 不跳转到支付结果界面PayResultActivity
						//TODO
					}else{
						
						//其他错误
						startActivity(intent);
					}
				}
			}
		}).execute(mReal_consumeCode,mBankAccountCode,mIdenCode);
	}
	
	/**
	 * 获取用户的银行卡列表
	 */
	
	private void getBankAccountList() {
		
		new MyHomeBankListTask(getMyActivity(), new MyHomeBankListTask.Callback() {
			
			@Override
			public void getResult(JSONObject result) {
				if (null != result) {
					JSONArray mActBanktArray = (JSONArray) result.get("bankAccountList");
					mLvBankListData = new ArrayList<BankList>();
					for (int i = 0; i < mActBanktArray.size(); i++) {
						JSONObject myHomeBankObject = (JSONObject) mActBanktArray.get(i);
						// 把JsonObject对象转换为实体
						BankList item = Util.json2Obj(myHomeBankObject.toString(), BankList.class);
						mLvBankListData.add(item);
					}
					//默认显示第一张银行卡
					showFirstCard();
				}
			}
		}).execute(String.valueOf(Util.NUM_ONE));
	}
	
	/**
	 * 默认显示第一张银行卡
	 */
	public  void  showFirstCard(){
		
		//如果没有绑定银行卡
		if(mLvBankListData==null||mLvBankListData.size()==0){
			mButtonSubmit.setText("下一步");
			/*mButtonSubmit.setBackgroundColor(Color.GRAY);*/
			mButtonSubmit.setEnabled(true);
			mButtonGetIden.setEnabled(false);
			mCardTextView.setText("建行签约快捷支付");
			mRelativeLayoutSelectCard.setEnabled(false);
			mIdenCodeEditText.setEnabled(false);
			mEditTextInputMbNbr.setEnabled(false);
			
			
			mPayOnlineLinearLayout.setVisibility(View.GONE);
			mArrow.setVisibility(View.GONE);
		}
		
		//如果已经绑定银行卡,默认显示用户的第一张银行卡
		if(mLvBankListData!=null&&mLvBankListData.size()>0){
			
			BankList bankList = mLvBankListData.get(0);
			mBankAccountCode = bankList.getBankAccountCode();
			String accountNbrLast4 = bankList.getAccountNbrLast4();
			mCardTextView.setText("建行卡" + "******" + accountNbrLast4);
			
			String mobileNbr = bankList.getMobileNbr();
			mEditTextInputMbNbr.setText(mobileNbr);
		}
	}
	
	/**
	 * 点击返回键 fragment中所做的操作   弹出对话框
	 */
	public void onBackPressedFragment(){
		//changeToDetailOrder();
		showPayOrCancleDialog();
	}
	
	/**
	 * 友盟统计
	 */
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(ICBCOnlinePayFragment.class.getSimpleName()); 
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(ICBCOnlinePayFragment.class.getSimpleName()); //统计页面
	}
	
	/**
	 * 取消订单跳转到H5店铺详情页面
	 */
	public void skipToH5ShopDetail(){
		Intent intent = new Intent(getActivity(), H5ShopDetailActivity.class);
		intent.putExtra(H5ShopDetailActivity.SHOP_CODE, mShopCode);
		this.startActivity(intent);
	}
}
