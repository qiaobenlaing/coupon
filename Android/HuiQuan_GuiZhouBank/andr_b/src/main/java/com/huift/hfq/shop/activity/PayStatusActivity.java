package com.huift.hfq.shop.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huift.hfq.shop.R;

public class PayStatusActivity extends Activity {
    private ImageView iv_status;
    private TextView tv_status, tv_msg;
    private int status;//1:成功 其他均为失败
    private String msg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_status);
        initView();
    }

    private void initView() {
        status = getIntent().getIntExtra("payStatus", 0);
        msg = getIntent().getStringExtra("payMsg");

        iv_status = findViewById(R.id.iv_status);
        tv_status = findViewById(R.id.tv_status);
        tv_msg = findViewById(R.id.tv_msg);

        findViewById(R.id.iv_title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_msg.setText(msg);
        if (status==1){
            tv_status.setText("支付成功");
            iv_status.setImageResource(R.drawable.paystatus_success);
        }else {
            tv_status.setText("支付失败");
            iv_status.setImageResource(R.drawable.paystatus_fail);
        }
    }
}
