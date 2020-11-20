// ---------------------------------------------------------
// @author    
// @version   1.0.0
// @createTime 2015.5.4
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.huift.hfq.base.Const;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.model.LoginTask;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.HomeActivity;
import com.huift.hfq.shop.activity.RegisterActivity;
import com.huift.hfq.shop.activity.ResetPwdActivity;
import com.huift.hfq.shop.activity.SettledActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 登录
 *
 * @author wensi.yu
 */
public class LoginFragment extends Fragment {

    /**
     * 用户名编辑框
     */
    @ViewInject(R.id.edt_login_username)
    private EditText mEdtUsername;
    /**
     * 密码编辑框
     */
    @ViewInject(R.id.edt_login_password)
    private EditText mEdtPassword;
    /**
     * 忘记密码文本框（用作按钮）
     */
    @ViewInject(R.id.tv_forget_pwd)
    private TextView mTvForgetPwd;
    /**
     * 登录按钮
     */
    @ViewInject(R.id.btn_login)
    private Button mBtnLogin;
    /**
     * 账号激活
     */
    @ViewInject(R.id.tv_register)
    private TextView mBtnRegest;
    /**
     * 显示密码的标示
     **/
    private Boolean mShowPassword = false;
    /**
     * 显示密码的状态
     */
    private TextView mTvShowPassword;
    private String mMobileNbr = null;
    private String mPassword = null;

    /**
     * 进度条
     */
    private ProgressBar mPrLogin;

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ViewUtils.inject(this, v);
        //登录的对象
        DB.saveStr(ShopConst.LoginKey.LOGITN_MAIN, getClass().getSimpleName());
        DB.saveStr(ShopConst.LoginKey.LOGIN, getClass().getSimpleName());
        init(v);
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
     * 设置密码显示和隐藏
     */
    private void init(View view) {
        mPrLogin = (ProgressBar) view.findViewById(R.id.pr_login_data);
        Util.addActivity(getMyActivity());
        Util.addLoginActivity(getMyActivity());
        // 去空格
        Util.inputFilterSpace(mEdtUsername);
        mTvShowPassword = (TextView) view.findViewById(R.id.tv_login_showpassword);

        mTvShowPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mShowPassword) {
                    // 设置EditText文本为可见的
                    mEdtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // 设置EditText文本为隐藏的
                    mEdtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                mShowPassword = !mShowPassword;
                mEdtPassword.postInvalidate();
                // 切换后将EditText光标置于末尾
                CharSequence charSequence = mEdtPassword.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }
        });

        /** 点击密码框后面的图标隐藏软键盘 **/
        mTvShowPassword.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    // 隐藏软键盘
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 登陆相关操作
     */
    @OnClick({R.id.btn_login, R.id.tv_register, R.id.tv_forget_pwd, R.id.tv_settled})
    public void loginBtnClick(View view) {
        mMobileNbr = mEdtUsername.getText().toString();
        if (TextUtils.isEmpty(mEdtPassword.getText())) {
            mPassword = "";
        } else {
            mPassword = Util.md5(mEdtPassword.getText().toString());
        }
        //保存登录的用户名和密码
        SharedPreferences mSharedPreferences = getMyActivity().getSharedPreferences(ShopConst.LoginSave.LOGIN_KEEP, Context.MODE_PRIVATE);
        final Editor editor = mSharedPreferences.edit();
        editor.putString("mobileNbr", mMobileNbr);
        editor.putString("password", mPassword);
        editor.commit();

        //传过来的registerid
        String regId = !Util.isEmpty(DB.getStr(Const.JPUSH_REGID)) ? DB.getStr(Const.JPUSH_REGID) : "";
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_login:
                mPrLogin.setVisibility(View.VISIBLE);
                if (Util.getLoginValidate(getMyActivity(), mMobileNbr)) {
                    mPrLogin.setVisibility(View.GONE);
                    break;
                }
                mBtnLogin.setEnabled(false);
                new LoginTask(getMyActivity(), new LoginTask.Callback() {
                    @Override
                    public void getResult(int result) {
                        mPrLogin.setVisibility(View.GONE);
                        mBtnLogin.setEnabled(true);
                        //当返回错误信息时
                        if (result == ErrorCode.FAIL) {
                            editor.clear();
                            editor.commit();
                        }
                    }
                }, HomeActivity.class, Const.Login.ANDR_B_HOMEFRAGMENT).execute(mMobileNbr, mPassword, regId);
                break;
            case R.id.tv_register://账户激活
                intent = new Intent(getMyActivity(), RegisterActivity.class);
                getMyActivity().startActivity(intent);
                break;
            case R.id.tv_forget_pwd://忘记密码
                intent = new Intent(getMyActivity(), ResetPwdActivity.class);
                getMyActivity().startActivity(intent);
                break;
            case R.id.tv_settled://我要入驻
                intent = new Intent(getMyActivity(), SettledActivity.class);
                getMyActivity().startActivity(intent);
                DB.saveBoolean(DB.Key.CUST_LOGIN, false);
                break;
            default:
                break;
        }
    }
}
