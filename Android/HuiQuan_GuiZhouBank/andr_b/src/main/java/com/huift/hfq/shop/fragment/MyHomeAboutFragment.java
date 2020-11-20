package com.huift.hfq.shop.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.AppUpdate;
import com.huift.hfq.base.utils.DialogUtils;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopApplication;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.activity.AboutActivity;
import com.huift.hfq.shop.activity.SysWechatActivity;
import com.huift.hfq.shop.model.GetNewestShopAppVersionTask;
import com.huift.hfq.shop.service.UpdateService;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.minidev.json.JSONObject;

/**
 * 关于
 *
 * @author wensi.yu
 */
public class MyHomeAboutFragment extends Fragment {

    private final static String TAG = "MyHomeAboutFragment";
    private static final String VERSION_TITLE = "关于";
    /**
     * 返回图片
     **/
    @ViewInject(R.id.layout_turn_in)
    private LinearLayout mIvBackup;
    /**
     * 功能描述文本
     **/
    @ViewInject(R.id.tv_mid_content)
    private TextView mTvdesc;
    /**
     * 版本号
     */
    private TextView myHomeVersion;
    /**
     * 版本号
     **/
    private TextView mTvAppUpdate;
    /**
     * 更新的对象
     */
    private AppUpdate mAppUpdate;
    /**
     * 当前版本
     */
    private String mCurrentVesion;

    public static MyHomeAboutFragment newInstance() {
        Bundle args = new Bundle();
        MyHomeAboutFragment fragment = new MyHomeAboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myhome_about, container, false);
        ViewUtils.inject(this, view);
        Util.addLoginActivity(getActivity());
        init(view);
        return view;
    }

    private void init(View view) {
        //设置标题
        TextView msg = (TextView) view.findViewById(R.id.tv_msg);
        msg.setVisibility(View.GONE);
        mTvdesc.setText(VERSION_TITLE);
        mIvBackup.setVisibility(View.VISIBLE);
        mTvAppUpdate = (TextView) view.findViewById(R.id.tv_appUpdate);
        myHomeVersion = (TextView) view.findViewById(R.id.myhome_version);
        mCurrentVesion = Util.getAppVersionCode(getActivity());
        myHomeVersion.setText(mCurrentVesion);
        //拨打电话
        mAppUpdate = DB.getObj(ShopConst.Key.APP_UPP, AppUpdate.class); // 保存更新的对象
        if (mAppUpdate != null) {
            if (ShopApplication.canUpdate) {
                mTvAppUpdate.setVisibility(View.VISIBLE);
            } else {
                mTvAppUpdate.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 跳转
     */
    @OnClick({R.id.ly_myhome_phone, R.id.ly_weixin, R.id.about_huiquan, R.id.release_check})
    private void lineBankClick(View v) {
        switch (v.getId()) {
            case R.id.ly_myhome_phone:
                DialogUtils.showDialog(getActivity(), getString(R.string.cue), getString(R.string.dialog_tel), getString(R.string.ok), getString(R.string.no), new DialogUtils().new OnResultListener() {
                    @Override
                    public void onOK() {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(getResources().getString(R.string.myhome_tel)));
                        getActivity().startActivity(intent);
                    }
                });
                break;
            case R.id.ly_weixin:
                startActivity(new Intent(getActivity(), SysWechatActivity.class));
                break;
            case R.id.about_huiquan:
                Intent intentAbout = new Intent(getActivity(), AboutActivity.class);
                startActivity(intentAbout);
                break;
            case R.id.release_check:
                if (ShopApplication.isApkDownloading) {
                    Toast.makeText(getActivity(), "正在下载中", Toast.LENGTH_SHORT).show();
                } else {
                    isUpdate();
                }
                break;
        }
    }

    /**
     * 版本升级
     */
    public void isUpdate() {
        new GetNewestShopAppVersionTask(getActivity(), new GetNewestShopAppVersionTask.Callback() {
            @Override
            public void getResult(JSONObject result) {
                if (result != null) {
                    Log.d(TAG, "result = >>>>>" + result.toString());
                    mAppUpdate = Util.json2Obj(result.toString(), AppUpdate.class);
                    if (mAppUpdate != null) {
                        DB.saveObj(ShopConst.Key.APP_UPP, mAppUpdate); // 保存跟新的对象
                        String newVersion = mAppUpdate.getVersionCode();
                        long currentVersionNum = Long.parseLong(mCurrentVesion.toLowerCase()
                                .replaceAll("v", "")
                                .replaceAll("．", "")
                                .replace(".", ""));
                        long newVersionNum = Long.parseLong(newVersion.toLowerCase()
                                .replaceAll("v", "")
                                .replaceAll("．", "")
                                .replace(".", ""));
                        // 服务器上新版本比现在app的版本高的话就提示升级
                        if (newVersionNum - currentVersionNum > 0) {
                            ShopApplication.canUpdate = true;
                            UpdateService.show(getActivity());
                        } else {
                            Util.getContentValidate(R.string.latest_version);
                        }
                    } else {
                        Util.getContentValidate(R.string.latest_version);
                    }
                }
            }
        }).execute();
    }

    /**
     * 返回
     *
     * @param view
     */
    @OnClick(R.id.layout_turn_in)
    public void trunIdenCode(View view) {
        getActivity().finish();
    }

}
