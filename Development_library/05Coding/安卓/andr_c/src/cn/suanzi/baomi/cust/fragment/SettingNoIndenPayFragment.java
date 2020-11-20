package cn.suanzi.baomi.cust.fragment;

import com.umeng.analytics.MobclickAgent;

import net.minidev.json.JSONObject;
import android.app.ActionBar.LayoutParams;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.User;
import cn.suanzi.baomi.base.utils.DialogUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.model.EditFreeValTask;
import cn.suanzi.baomi.cust.model.GetUserInfoTask;

/**
 * @author yingchen
 */
public class SettingNoIndenPayFragment extends Fragment implements View.OnClickListener{
	private static final String TAG = SettingNoIndenPayFragment.class.getSimpleName();
	/**fragment加载的视图*/
	private View view;
	/**设置开关*/
	private ImageView mSwitchImageView;
	/**是否开启的标识*/
	private boolean mSwitchFlag = false;
	/**进度条*/
	private ProgressDialog mProcessDialog;
	/**输入密码的PopWindow*/
	private PopupWindow mPopWindow;
	/**PopWindow的视图*/
	private View mPopWindowView;
	/**密码输入框*/
	private EditText mPasswordEditText;
	/**确定按钮*/
	private Button mEnsureButton;
	/**取消按钮*/
	private Button mCancleButton;
	/**开启免验证码支付时输入的密码*/
	private String mPassWord = "";
	/**是否开启免验证码*/
	private String mFreeValCodePay = "";
	
	public static SettingNoIndenPayFragment newInstance(){
		Bundle args = new Bundle();
		SettingNoIndenPayFragment fragment = new SettingNoIndenPayFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_setting_no_inden, container,false);
		initView();
		//判断用户是否开启过免验证码支付
		isOpen();
		return view;
	}

	/**
	 * 初始化控件  AND 一些简单操作
	 * @param view
	 */
	private void initView() {
		//标题
		TextView title = (TextView) view.findViewById(R.id.tv_mid_content);
		title.setText("免验证码支付");
		
		//返回
		ImageView back = (ImageView) view.findViewById(R.id.iv_turn_in);
		back.setOnClickListener(this);
		
		mSwitchImageView = (ImageView) view.findViewById(R.id.iv_setting_no_inden_swtich);
		mSwitchImageView.setOnClickListener(this);
	}
	
	/***
	 * 请求API 判断用户是否开启过免验证码支付  用于页面显示
	 */
	private void isOpen() {
		mProcessDialog = new ProgressDialog(getActivity());
		mProcessDialog.setMessage(getResources().getString(R.string.msg_task_processing));
		mProcessDialog.show();
		new GetUserInfoTask(getActivity(), new GetUserInfoTask.Callback() {
			@Override
			public void getResult(JSONObject object) {
				if(null!=object){
					mProcessDialog.dismiss();
					User user = Util.json2Obj(object.toString(), User.class);
					String code = user.getFreeValCodePay();
					if(String.valueOf(Util.NUM_ZERO).equals(code)){  //0---没有开启
						mSwitchFlag = false;
						mSwitchImageView.setBackgroundResource(R.drawable.checkbox_empty);
					}else if(String.valueOf(Util.NUM_ONE).equals(code)){ //1---代表开启
						mSwitchFlag = true;
						mSwitchImageView.setBackgroundResource(R.drawable.checkbox_check);
					}
				}
			}
		}).execute();
	}

	/**
	 * 点击监听
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_turn_in:
			onBack();
			break;
		case R.id.iv_setting_no_inden_swtich:
			Log.d(TAG, "mSwitchFlag=="+mSwitchFlag);
			if(mSwitchFlag){ //开启--->关闭
				mFreeValCodePay = String.valueOf(Util.NUM_ZERO);
				OpenOrCloseIden();
			}else{  //关闭---->开启
				mFreeValCodePay = String.valueOf(Util.NUM_ONE);
				//弹出输入密码框
				showPasswordWindow();
			}
			
			break;

		case R.id.btn_ok: //popwindow的确定按钮
			mPassWord = mPasswordEditText.getText().toString().trim();
			if(Util.isEmpty(mPassWord)){
				Util.getContentValidate(R.string.hint_input_pwd);
			}else{
				mPassWord = Util.md5(mPassWord); //加密
				OpenOrCloseIden();
			}
			break;
			
		case R.id.btn_cancel://popwindow的取消按钮
			mPopWindow.dismiss();
			break;
		default:
			break;
			
		}
	}

	/**
	 * 开启或者关闭 免验证码支付功能呢
	 * @param freeValCodePay  1---开启     0---关闭  
	 * 开启时需要密码
	 * 关闭时不需要密码
	 */
	private void OpenOrCloseIden() {
		Log.d(TAG, "mFreeValCodePay=="+mFreeValCodePay+",mPassWord=="+mPassWord);
		
		new EditFreeValTask(getActivity(), new EditFreeValTask.CallBack() {
			@Override
			public void getResult(boolean success) {
				if(success){
					User user = DB.getObj(DB.Key.CUST_USER, User.class);
					mSwitchFlag = !mSwitchFlag;
					if(mSwitchFlag){
						Util.getContentValidate(R.string.setting_iden_success);
						mSwitchImageView.setBackgroundResource(R.drawable.checkbox_check);
						user.setFreeValCodePay(String.valueOf(Util.NUM_ONE));
						mPopWindow.dismiss();
					}else{
						Util.getContentValidate(R.string.setting_no_iden_success);
						mSwitchImageView.setBackgroundResource(R.drawable.checkbox_empty);
						user.setFreeValCodePay(String.valueOf(Util.NUM_ZERO));
					}
					DB.saveObj(DB.Key.CUST_USER, user);
				}else{
					/*if(mSwitchFlag){
						Util.getContentValidate(R.string.setting_iden_fail);
					}else{
						Util.getContentValidate(R.string.setting_no_iden_fail);
					}*/
				}
				
			}
		}).execute(mFreeValCodePay,mPassWord);
	}
	
	/**
	 *弹出输入密码框
	 */
	public void showPasswordWindow(){
		if(null == mPopWindowView){
			mPopWindowView = View.inflate(getActivity(), R.layout.popupw_dialog_editor, null);
			mPasswordEditText = (EditText) mPopWindowView.findViewById(R.id.et_dialog_pwd);
			mEnsureButton = (Button) mPopWindowView.findViewById(R.id.btn_ok);
			mCancleButton = (Button) mPopWindowView.findViewById(R.id.btn_cancel);
			mEnsureButton.setOnClickListener(this);
			mCancleButton.setOnClickListener(this);
		}
		mPasswordEditText.setText("");
		if(null == mPopWindow){
			mPopWindow = new PopupWindow(mPopWindowView,LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			mPopWindow.setFocusable(true);
		}
		
		mPopWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
	}
	
	/**
	 * 回退
	 */
	public void onBack(){
		if(!mSwitchFlag){
			DialogUtils.showDialog(getActivity(),Util.getString(R.string.cue),Util.getString(R.string.setting_no_iden_mentioned), Util.getString(R.string.setting_no_iden_continue),
					Util.getString(R.string.setting_no_iden_stop), new DialogUtils().new OnResultListener() {
				@Override
				public void onCancel() {
					SettingNoIndenPayFragment.this.getActivity().finish();
				}
			});
		}else{
			SettingNoIndenPayFragment.this.getActivity().finish();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(SettingNoIndenPayFragment.class.getSimpleName());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(SettingNoIndenPayFragment.class.getSimpleName()); // 统计页面
	}
}
