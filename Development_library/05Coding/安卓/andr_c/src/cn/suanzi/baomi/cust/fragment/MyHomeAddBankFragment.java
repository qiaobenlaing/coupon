package cn.suanzi.baomi.cust.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.suanzi.baomi.base.Const;
import cn.suanzi.baomi.base.TimeCountUtil;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.utils.ActivityUtils;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.activity.MyHomeAddValidateActivity;
import cn.suanzi.baomi.cust.activity.PayResultActivity;
import cn.suanzi.baomi.cust.model.AddBankAccountTask;
import cn.suanzi.baomi.cust.model.GetPayValCodeQuicklyTask;
import cn.suanzi.baomi.cust.model.SignAndPay;
import cn.suanzi.baomi.cust.util.BankActivityUtils;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 添加银行卡
 * @author wensi.yu ， ying.cheng
 *
 */
public class MyHomeAddBankFragment extends Fragment {

	private final static String TAG = MyHomeAddBankFragment.class.getSimpleName();
	//标记是否是正常的添加银行卡流程（正常流程：从我的界面进行银行卡的添加）
	public final static String NORMALADD = "normaladd";
	//绑定银行卡并支付的标识
	public final static String BLINDANDPAY = "blindandpay";
	//绑定银行卡并支付的用户消费记录编码的标识
	public final static String CONSUME_CODE = "consume_code";
	
	public final static String SHOPE_CODE = "shope_code";
	
	public final static String IS_BANK_PAY = "is_bank_pay";
	

	
	private boolean mBlindAndPayFlag = false;//标识是否直接绑定银行卡并支付  默认为false
	private String mConsumeCode ;
	
	private String mShopCode ;
	
	private boolean mIsBankCard ;
	
	/**否是正常的添加银行卡流程*/
	private boolean mBankCardAddNormal = true;

	/**标题**/
	final static String BANK_TITLE = "银行卡详情";
	/** 功能描述文本 */
	@ViewInject(R.id.tv_mid_content)
	TextView mTvdesc;
	/**添加*/
	@ViewInject(R.id.iv_add)
	ImageView mTvBankAdd;
	/**返回**/
	@ViewInject(R.id.iv_turn_in)
	ImageView mIvBankBack;
	/**下一步**/
	@ViewInject(R.id.btn_actadd_nextone)
	Button mBtnAddBankNext;
	/**姓名**/
	@ViewInject(R.id.tv_addbank_name)
	EditText mTvAddBankName;
	/**证件号码**/
	@ViewInject(R.id.tv_addbank_card)
	EditText mTvAddBankNumber;
	/**卡号前六号**/
	@ViewInject(R.id.et_addbank_sex)
	EditText mTvAddBankCardSex;
	/**卡号后前四位**/
	@ViewInject(R.id.et_addbank_four)
	EditText mTvAddBankCardFour;
	/**手机号**/
	@ViewInject(R.id.tv_addbank_phone)
	EditText mTvAddBankPhone;
	/**证件类型**/
	/*@ViewInject(R.id.sp_addbank_cardtype)
	private Spinner mSpAddBankType;*/
	/**下拉列表证件类型**/
	private String mCertificateType;
	
	@ViewInject(R.id.rl_sign_pay)
	private RelativeLayout mSignAndPay;
	
	@ViewInject(R.id.sign_pay_valcode)
	private TextView mSendIdenCode;
	@ViewInject(R.id.input_idencode)
	private EditText mIdenCodeEditText;
	
	/*private UserToken mUserToken;
	private String mUserCode = null;
	private String mTokenCode = null;*/
	private String mIdenCode;
	private String mBankAccountCode;
	
	//private String returnIden;
	//private String inputIden;
	
	public static MyHomeAddBankFragment newInstance() {
		Bundle args = new Bundle();
		MyHomeAddBankFragment fragment = new MyHomeAddBankFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_myhomeaddbank, container, false);// 说明v，注释// e.g:Fragment的view
		ViewUtils.inject(this, v);
		
		//--------记录绑定银行卡流程-------
		if(!mBlindAndPayFlag){
			/*ActivityUtils.add(getActivity());*/
			BankActivityUtils.add(getMyActivity());
		}
		
		Util.addLoginActivity(getMyActivity());
		init(v);
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
	private void init(View view) {
		//获取是否是添加银行卡的正常流程
		mBankCardAddNormal = getActivity().getIntent().getBooleanExtra(NORMALADD, true);
		//获取是否绑定并支付的标识
		mBlindAndPayFlag = getActivity().getIntent().getBooleanExtra(BLINDANDPAY, false);
		//获取用户消费记录编码
		if(mBlindAndPayFlag){
			mConsumeCode = getMyActivity().getIntent().getStringExtra(CONSUME_CODE);
			mShopCode = getActivity().getIntent().getStringExtra(SHOPE_CODE);
			mIsBankCard = getActivity().getIntent().getBooleanExtra(IS_BANK_PAY, true);	
		}
		if(mBlindAndPayFlag){
			mBtnAddBankNext.setText("签约并支付");
		}else{
			mBtnAddBankNext.setText("下一步");
		}
		
		//获取令牌
	/*	mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mUserCode = mUserToken.getUserCode();
		mTokenCode = mUserToken.getTokenCode();*/
		
		//设置标题
		mTvdesc.setText(BANK_TITLE);
		mTvBankAdd.setVisibility(View.GONE);
		
		//建立数据源
		String[] spItem = getResources().getStringArray(R.array.cardType);
		//绑定数据源
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(getMyActivity(),android.R.layout.simple_spinner_item, spItem);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//绑定到控件
		/*mSpAddBankType.setAdapter(adapter);
		
		//暂时屏蔽 只能使用身份证
		mSpAddBankType.setClickable(false);*/
		//spinner的点击事件
		//mSpAddBankType.setOnItemSelectedListener(spinnerListener);
		
		mTvAddBankCardSex.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(s.toString().length()==6){  //焦点跳转到后四位
					mTvAddBankCardFour.requestFocus();
				}
				
			}
		});
		
		 //判断是否是绑定和签约一起
		 if(mBlindAndPayFlag){
		    mSignAndPay.setVisibility(View.VISIBLE);
		 }
	}
	
	/**
	 * 下拉列表的点击事件
	 */
	OnItemSelectedListener spinnerListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			
			mCertificateType = parent.getItemAtPosition(position).toString();
			Toast.makeText(getMyActivity(), "点击："+mCertificateType, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
			
		}
	};
	
	/**
	 * 返回到银行卡列表
	 * 
	 * 下一步到短信验证
	 */
	@OnClick({R.id.iv_turn_in,R.id.btn_actadd_nextone,R.id.sign_pay_valcode})
	public void btnBankReturnClick(View view) {
		//姓名
		String accountName = mTvAddBankName.getText().toString().trim();
		//证件号
		String idNbr = mTvAddBankNumber.getText().toString().trim().toUpperCase().trim(); //小写x---》大写X
		//卡号前六位
		String accountNbrPreSex = mTvAddBankCardSex.getText().toString().trim();
		//卡号后四位
//		String accountNbrLastFour = mTvAddBankCardFour.getText().toString();
		//预留手机号
		String mobileNbr = mTvAddBankPhone.getText().toString().trim();
		
		switch (view.getId()) {
		case R.id.iv_turn_in:
			ActivityUtils.remove(getMyActivity());
			getMyActivity().finish();
			break;
		case R.id.btn_actadd_nextone:
			//姓名不能为空
			if(Util.isEmpty(accountName)){
				Util.getContentValidate(R.string.toast_actaddbank_nothing);
				mTvAddBankName.findFocus();
				break;
			}
			//证件号不能为空
			if(Util.isEmpty(idNbr)){
				Util.getContentValidate(R.string.toast_actaddbank_nothing);
				mTvAddBankNumber.findFocus();
				break;
			}
			//银行卡号不能为空
			if(Util.isEmpty(accountNbrPreSex)){
				Util.getContentValidate(R.string.toast_actaddbank_nothing);
				mTvAddBankCardSex.findFocus();
				break;
			}
			//银行卡号不能为空
//			if(Util.isEmpty(accountNbrLastFour)){
//				Util.getContentValidate(R.string.toast_actaddbank_nothing);
//				mTvAddBankCardFour.findFocus();
//				break;
//			}
			//预留手机号不能为空
			if(Util.isEmpty(mobileNbr)){
				Util.getContentValidate(R.string.toast_actaddbank_nothing);
				mTvAddBankName.findFocus();
				break;
			}
			//手机的长度
			if(mobileNbr.length() != 11){
				Util.getContentValidate(R.string.toast_register_format);
				mTvAddBankPhone.findFocus();
				break;
			}
			//手机号码格式不正确
			if(Util.isPhone(getMyActivity(), mobileNbr)){
				break;
			}
			
			//保存值
			SharedPreferences mSharedPreferences = getMyActivity().getSharedPreferences("actaddbank", Context.MODE_PRIVATE);
		    Editor editor = mSharedPreferences.edit();   
		    editor.putString("accountName", accountName);
		    editor.putString("idType", mCertificateType);
		    editor.putString("idNbr", idNbr);
		    editor.putString("accountNbrPreSex", accountNbrPreSex);
//		    editor.putString("accountNbrLastFour", accountNbrLastFour);
		    editor.putString("mobileNbr", mobileNbr);
		    
		    Log.i(TAG, "accountName**********"+accountName);
		    Log.i(TAG, "idType**********"+mCertificateType);
		    Log.i(TAG, "idNbr**********"+idNbr);
		    Log.i(TAG, "accountNbrPreSex**********"+accountNbrPreSex);
//		    Log.i(TAG, "accountNbrLastFour**********"+accountNbrLastFour);
		    Log.i(TAG, "mobileNbr**********"+mobileNbr);
		    editor.commit();
		    ActivityUtils.add(getMyActivity());
		    
		    if(mBlindAndPayFlag){
		    	mIdenCode = mIdenCodeEditText.getText().toString().trim();
		    	if(TextUtils.isEmpty(mIdenCode)){
		    		Toast.makeText(getMyActivity(), "请输入验证码", Toast.LENGTH_SHORT).show();
		    		return;
		    	}
		    	
		    	
		    	//确认在线支付
		    	confirmPay();
		    }else{
		    	 //添加银行卡的异步
			    addTask(accountName,idNbr,accountNbrPreSex,mobileNbr);
			
		    }
		    
			break;
			
		case R.id.sign_pay_valcode:	
			//姓名不能为空
			if(Util.isEmpty(accountName)){
				Util.getContentValidate(R.string.toast_actaddbank_nothing);
				mTvAddBankName.findFocus();
				break;
			}
			//证件号不能为空
			if(Util.isEmpty(idNbr)){
				Util.getContentValidate(R.string.toast_actaddbank_nothing);
				mTvAddBankNumber.findFocus();
				break;
			}
			//银行卡号不能为空
			if(Util.isEmpty(accountNbrPreSex)){
				Util.getContentValidate(R.string.toast_actaddbank_nothing);
				mTvAddBankCardSex.findFocus();
				break;
			}
			//银行卡号不能为空
//			if(Util.isEmpty(accountNbrLastFour)){
//				Util.getContentValidate(R.string.toast_actaddbank_nothing));
//				mTvAddBankCardFour.findFocus();
//				break;
//			}
			//预留手机号不能为空
			if(Util.isEmpty(mobileNbr)){
				Util.getContentValidate(R.string.toast_actaddbank_nothing);
				mTvAddBankName.findFocus();
				break;
			}
			//手机的长度
			if(mobileNbr.length() != 11){
				Util.getContentValidate(R.string.toast_register_format);
				mTvAddBankPhone.findFocus();
				break;
			}
			//手机号码格式不正确
			if(Util.isPhone(getActivity(), mobileNbr)){
				break;
			}
		
			Log.d("yingchen", "accountName**********"+accountName);
		    Log.d("yingchen", "idType**********"+mCertificateType);
		    Log.d("yingchen", "idNbr**********"+idNbr);
		    Log.d("yingchen", "accountNbrPreSex**********"+accountNbrPreSex);
		    Log.d("yingchen", "mobileNbr**********"+mobileNbr);
		    Log.d("yingchen", "mConsumeCode**********"+mConsumeCode);
		
			// 发送验证码
			TimeCountUtil timeCountUtil = new TimeCountUtil(getActivity(), 60000, 1000, mSendIdenCode);
			timeCountUtil.start();
			
			// 获取验证码(绑定and支付获取验证码)
			mSendIdenCode.setEnabled(false);
			
			new GetPayValCodeQuicklyTask(getActivity(), new GetPayValCodeQuicklyTask.Callback() {
				
				@Override
				public void getResult(String result) {
					if(result!=null){//成功获取验证码
						String[] split = result.split("##");
						/*returnIden = split[0];*/
						if(Const.IS_DEBUG){
							//mIdenCode.setText(returnIden);
						}
						mBankAccountCode = split[1];
						
						mSendIdenCode.setEnabled(true);
					}else{
						//TODO
						mSendIdenCode.setEnabled(true);
					}	
				}
			}).execute(accountName,0+"",idNbr,accountNbrPreSex,mobileNbr,mConsumeCode); //0+""----代表证件类型  暂时只有0---身份证，待增加
			
			break;
		default:
			break;
		}
	}

	
	
	/**
	 * 添加银行卡的异步任务类 
	 */
	private void addTask(String accountName, String idNbr,String accountNbrPreSex,final String mobileNbr) {
		
		//暂时只支持身份证 0
		new AddBankAccountTask(getMyActivity(), new AddBankAccountTask.Callback() {

			@Override
			public void getResult(String result) {
				if(result!=null){ //添加银行卡成功  result---添加银行卡成功产生的订单号
					Intent intent = new Intent(getMyActivity(), MyHomeAddValidateActivity.class);
					intent.putExtra(MyHomeAddValidateFragment.ORDERNBR, result);
					intent.putExtra(MyHomeAddValidateFragment.MOBILENBR, mobileNbr);
					intent.putExtra(NORMALADD, mBankCardAddNormal);
					startActivity(intent);	
				}else{
					
				}
			}
			
		}).execute(accountName,0+"",idNbr,accountNbrPreSex,mobileNbr);
		
	}
	
	public void confirmPay(){
		
		new SignAndPay(getActivity(), new SignAndPay.Callback() {
			@Override
			public void getResult(boolean result,String orderCode) {
				/*Intent intent = new Intent(getActivity(), ActThemeDetailActivity.class);
				if(result){  //支付成功
					intent.putExtra(ActThemeDetailActivity.TYPE, "pay_success");
					intent.putExtra("consumeCode", mConsumeCode);
					startActivity(intent);
				}else{  //支付失败
					intent.putExtra(ActThemeDetailActivity.TYPE, "pay_fail");
					startActivity(intent);
				}*/
				
				Intent intent = new Intent(getActivity(), PayResultActivity.class); 
				intent.putExtra(PayResultActivity.SHOPE_CODE, mShopCode);
				intent.putExtra(PayResultActivity.IS_BANK_PAY, mIsBankCard);
				if(result){ //支付成功
					intent.putExtra(PayResultActivity.PAY_RESULT, true);
					intent.putExtra(PayResultActivity.CONSUMECODE, mConsumeCode);
					intent.putExtra(PayResultActivity.ORDER_CODE, orderCode);
					startActivity(intent);
				}else{
					intent.putExtra(PayResultActivity.PAY_RESULT, false);
					startActivity(intent);
				}
			}
		}).execute(mConsumeCode,mBankAccountCode,mIdenCode);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(MyHomeAddBankFragment.class.getSimpleName()); 
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(MyHomeAddBankFragment.class.getSimpleName()); //统计页面
	}
}
