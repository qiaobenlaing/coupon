// ---------------------------------------------------------
// @author    
// @version   1.0.0
// @createTime 2015.5.4
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.huift.hfq.base.view.StatusBarView;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.adapter.GuideViewAadapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 引导界面的activity
 *
 * @author
 */
public class GuideActivity extends Activity implements OnPageChangeListener {

    @ViewInject(R.id.guideactivity_viewPager)
    private ViewPager mViewPager;
    @ViewInject(R.id.b_turn_to)
    private Button button;
    private LinearLayout ll_tag;
    /**
     * 图片数据源
     */
    private int[] pics = {R.drawable.guide1, R.drawable.guide2, R.drawable.guide3};
    /**
     * ViewPager适配器
     */
    private GuideViewAadapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        StatusBarView.statusBar(this);
        ViewUtils.inject(this);//注册控件
        mAdapter = new GuideViewAadapter(this, pics);//创建适配器
        mViewPager.setAdapter(mAdapter);//关联适配器
        mViewPager.addOnPageChangeListener(this);//设置页卡改变监听事件
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(GuideActivity.this, LoginActivity.class);
                startActivity(it);
                finish();//结束GuideActivity
            }
        });

        ll_tag = findViewById(R.id.ll_tag);
        for (int i = 0; i < pics.length; i++) {
            View dotView = LayoutInflater.from(this).inflate(R.layout.layout_dot, null);
            dotView.setEnabled(false);
            ll_tag.addView(dotView);
        }
        ll_tag.getChildAt(0).setEnabled(true);

    }

    //OnPageChangeListenner中的方法
    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int pos) {
        for (int i = 0; i < pics.length; i++) {
            ll_tag.getChildAt(i).setEnabled(false);
        }
        ll_tag.getChildAt(pos).setEnabled(true);

        if (pos == pics.length - 1) {
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.GONE);
        }
    }
}
