package cn.suanzi.baomi.shop.fragment;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.TimeCountUtil;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.activity.TextMessageActivity;
import cn.suanzi.baomi.shop.model.AddMRecipientTask;
import cn.suanzi.baomi.shop.model.GetValidateCodeTask;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 设置接受短信息
 * 
 * @author liyanfang
 */
public class AddTextMessageFragment extends Fragment {

	private final static String TAG = AddTextMessageFragment.class.getSimpleName();
	/** 手机号码长度 */
	private static final int PHONE_NUMBER = 11;
	/** 姓名 */
	@ViewInject(R.id.tv_name)
	private TextView mTvName;
	/** 电话号码 */
	@ViewInject(R.id.tv_tel_no)
	private TextView mTvTelNo;
	/** 发送验证码 */
	@ViewInject(R.id.tv_register_send)
	private TextView mTvSendCode;
	/** 验证码 */
	@ViewInject(R.id.tv_code_no)
	private TextView mTvCodeNo;
	/** 验证码*/
	private String mValCoe;

	public static AddTextMessageFragment newInstance() {
		Bundle args = new Bundle();
		AddTextMessageFragment fragment = new AddTextMessageFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_add_message_no, container, false);// 说明v，注释
																							// e.g:Fragment的view
		ViewUtils.inject(this, view);
		findView(view);
		return view;
	}

	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	/**
	 * 获取控件
	 * 
	 * @param view
	 */
	private void findView(View view) {
		// 标题
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_mid_content);
		// 添加短信的人
		TextView tvEdit = (TextView) view.findViewById(R.id.tv_msg);
		tvTitle.setText(Util.getString(R.string.set_msg));
		tvEdit.setText(Util.getString(R.string.dialog_ok));
	}

	/**
	 * 点击时间
	 * 
	 * @param view
	 */
	@OnClick({ R.id.layout_turn_in, R.id.tv_msg, R.id.tv_register_send })
	public void btnAllClick(View view) {
		switch (view.getId()) {
		case R.id.layout_turn_in:
			getMyActivity().finish();
			break;
		case R.id.tv_msg: // 添加
			getAddTextMessage();
			break;
		case R.id.tv_register_send: // 获取验证码
			getVerificationCode();
			break;

		default:
			break;
		}
	}

	/**
	 * 添加联系人
	 */
	private void getAddTextMessage() {
		if (TextUtils.isEmpty(mTvName.getText())) {
			Util.getContentValidate(R.string.input_name);
		} else if (TextUtils.isEmpty(mTvTelNo.getText())) {
			Util.getContentValidate(R.string.input_tel);
		} else if (Util.isPhone(getMyActivity(), mTvTelNo.getText().toString())) { // 手机号码格式不正确
			
		} else if (mTvTelNo.getText().toString().length() != PHONE_NUMBER) {
			Util.getContentValidate(R.string.tel_format_error);
		} else if (TextUtils.isEmpty(mTvCodeNo.getText())) {
			Util.getContentValidate(R.string.input_code);
	    } else if (mTvCodeNo.getText().equals(mValCoe)) {
	    	Util.getContentValidate(R.string.code_error);
	    } else {
			String[] params = {mTvName.getText().toString(),mTvTelNo.getText().toString(),mTvCodeNo.getText().toString()};
			new AddMRecipientTask(getMyActivity(), new AddMRecipientTask.Callback() {

				@Override
				public void getResult(boolean retFlag) {
					if (retFlag) {
						Log.d(TAG, "retFlag : " + retFlag);
						Intent intent = new Intent(getMyActivity(),TextMessageActivity.class);
						getMyActivity().setResult(TextMessageFragment.RESPONSE_CODE, intent);
						getMyActivity().finish();
					}
				}
			}).execute(params);
		}
	}

	/**
	 * 获取验证码
	 */
	private void getVerificationCode() {
		if (TextUtils.isEmpty(mTvTelNo.getText())) {
			Util.getContentValidate(R.string.input_tel);
		} else if (Util.isPhone(getMyActivity(), mTvTelNo.getText().toString())) { // 手机号码格式不正确

		} else if (mTvTelNo.getText().toString().length() != PHONE_NUMBER) {
			Util.getContentValidate(R.string.tel_format_error);
		} else {
			String[] params = { mTvTelNo.getText().toString(), "mr", "s" }; // mr 是消息添加联系人s：商家端
			// 获取验证码
			mTvSendCode.setEnabled(false);
			new GetValidateCodeTask(getMyActivity(), new GetValidateCodeTask.Callback() {
				@Override
				public void getResult(JSONObject mResult) {
					mTvSendCode.setEnabled(true);
					if (null != mResult) {  
						if (mResult.get("code").toString().equals(String.valueOf(ErrorCode.SUCC))) {
							TimeCountUtil timeCountUtil = new TimeCountUtil(getActivity(), 60000, 1000, mTvSendCode);
							timeCountUtil.start();
							mValCoe = mResult.get("valCode").toString();
							Log.d(TAG, "mValCoe :接受发送的验证码==============" + mValCoe);
						}
					}
				}
			}).execute(params);
		}
	}

}
