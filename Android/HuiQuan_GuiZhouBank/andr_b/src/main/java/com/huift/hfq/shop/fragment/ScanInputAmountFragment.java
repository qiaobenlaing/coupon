package com.huift.hfq.shop.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.PayOrderBean;
import com.huift.hfq.base.utils.ActivityUtils;
import com.huift.hfq.base.utils.AppUtils;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.activity.ConfirmOrderActivity;
import com.huift.hfq.shop.activity.ScanActivity;
import com.huift.hfq.shop.model.CreateOrderTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 输入金额收款
 *
 * @author wensi.yu
 */
public class ScanInputAmountFragment extends Fragment {
    //扫一扫标签
    public static final String FLAG = ScanInputAmountFragment.class.getSimpleName();
    private final int REQUEST_CODE = 1;

    private EditText et_totalMoney;
    private EditText et_noDiscountMoney;
    private EditText et_couponNo;
    private ImageView iv_scan;
    private Button b_submit;
    private CheckBox cb_noDiscount;
    private LinearLayout ll_noDiscount;

    public static ScanInputAmountFragment newInstance() {
        Bundle args = new Bundle();
        ScanInputAmountFragment fragment = new ScanInputAmountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scaninput_amount, container, false);
        ViewUtils.inject(this, view);
        Window window = getMyActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        init(view);
        ActivityUtils.add(getMyActivity());
        return view;
    }

    private Activity getMyActivity() {
        Activity act = getActivity();
        if (act == null) {
            act = AppUtils.getActivity();
        }
        return act;
    }

    private void init(View view) {
        TextView tvCampaignName = (TextView) view.findViewById(R.id.tv_mid_content);//标题
        tvCampaignName.setText("输入金额");
        TextView tvSet = (TextView) view.findViewById(R.id.tv_msg);//添加
        tvSet.setVisibility(View.GONE);
        et_totalMoney = (EditText) view.findViewById(R.id.et_totalMoney);//消费金额
        et_noDiscountMoney = (EditText) view.findViewById(R.id.et_scan_nodiscount);//不参与优惠金额
        et_couponNo = view.findViewById(R.id.et_couponNo);
        iv_scan = view.findViewById(R.id.iv_scan);
        b_submit = (Button) view.findViewById(R.id.b_submit);
        cb_noDiscount = (CheckBox) view.findViewById(R.id.ck_scan_nodiscount);//选择不参与优惠的金额
        ll_noDiscount = (LinearLayout) view.findViewById(R.id.ly_scan_nodiscount);//不参与优惠额的一行

        iv_scan.setOnClickListener(btnClick);
        cb_noDiscount.setOnClickListener(btnClick);
        b_submit.setOnClickListener(btnClick);

        Util.initDate(et_totalMoney);//自动弹出软键盘
    }

    private OnClickListener btnClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_scan:
                    Intent intent = new Intent(getMyActivity(), ScanActivity.class);
                    intent.putExtra(ScanActivity.FLAG, FLAG);
                    startActivityForResult(intent, REQUEST_CODE);
                    break;
                case R.id.ck_scan_nodiscount://不参与优惠金额
                    if (cb_noDiscount.isChecked()) {
                        ll_noDiscount.setVisibility(View.VISIBLE);
                        et_noDiscountMoney.requestFocus();
                        cb_noDiscount.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                    } else {
                        ll_noDiscount.setVisibility(View.GONE);
                        cb_noDiscount.setTextColor(ContextCompat.getColor(getActivity(), R.color.msg_font));
                        et_noDiscountMoney.setText("");
                    }
                    break;
                case R.id.b_submit://确定
                    String totalMoney = et_totalMoney.getText().toString();
                    String noDiscountMoney = et_noDiscountMoney.getText().toString();
                    if (TextUtils.isEmpty(totalMoney)||Double.parseDouble(totalMoney)==0) {
                        Util.getContentValidate(R.string.toast_consumptionamount_nothing);
                        return;
                    }
                    if (TextUtils.isEmpty(noDiscountMoney)) {
                        noDiscountMoney="0";
                    }else {
                        if (Double.parseDouble(totalMoney) < Double.parseDouble(noDiscountMoney)) {
                            Util.getContentValidate(R.string.toast_consumptionamount_diff);
                            return;
                        }
                    }
                    createOrder(totalMoney,noDiscountMoney);
                    break;
            }
        }
    };

    private void createOrder(String totalPrice,String noDiscountPrice){
        new CreateOrderTask(getMyActivity(), new CreateOrderTask.Callback() {
            @Override
            public void getResult(PayOrderBean payOrder) {
                if (payOrder!=null){
                    if (payOrder.getCode()== ErrorCode.SUCC){
                        Intent intent=new Intent(getMyActivity(), ConfirmOrderActivity.class);
                        intent.putExtra("payOrder",payOrder);
                        startActivity(intent);
                    }else {
                        Util.showToastZH(payOrder.getMsg());
                    }
                }else {
                    Util.showToastZH("请求失败，请稍后再试！");
                }
            }
        }).execute(totalPrice,et_couponNo.getText().toString(),noDiscountPrice);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == REQUEST_CODE){
            String qrCode=data.getStringExtra(ScanActivity.INTENT_DATA);
            et_couponNo.setText(qrCode);
        }
    }

    /**
     * 返回
     */
    @OnClick(R.id.layout_turn_in)
    public void trunIdenCode(View view) {
        getMyActivity().finish();
    }
}
