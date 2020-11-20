// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.huift.hfq.base.Const;
import com.huift.hfq.base.ErrorCode;
import com.huift.hfq.base.Util;
import com.huift.hfq.base.data.DB;
import com.huift.hfq.base.pojo.Messages;
import com.huift.hfq.base.pojo.PageData;
import com.huift.hfq.base.pojo.Shop;
import com.huift.hfq.base.pojo.UserCardVip;
import com.huift.hfq.base.pojo.UserToken;
import com.huift.hfq.base.view.XXListView;
import com.huift.hfq.base.view.XXListView.IXXListViewListener;
import com.huift.hfq.shop.R;
import com.huift.hfq.shop.ShopConst;
import com.huift.hfq.shop.adapter.MessageListAdapter;
import com.huift.hfq.shop.model.GetMsgTask;
import com.huift.hfq.shop.model.ReadMsgTask;
import com.huift.hfq.shop.model.SendMsgTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 会员和管理员交流的界面
 *
 * @author yanfang.li
 */
public class VipChatFragment extends Fragment implements IXXListViewListener {

    /**
     * 用于savedInstanceState的常量，当前fragment的index
     */
    public final static String FRAG_INFO = "frag_info";
    public final static String EDIT_INFO = "edit_info";
    /**
     * 保存的对象
     */
    public final static String USER_OBJ = "userobj";
    public final static String MSG_FLAG = "msgflag";
    /**
     * 表示是自己发送的
     */
    public final static String MY_FLAG = "myflag";
    /**
     * 接收的消息
     */
    public final static String VIP_FLAG = "vipflag";
    public static final String MSUGGEST = "suggest";
    /**
     * 消息内容
     */
    private EditText mEdtContent;
    /**
     * 用户对象
     */
    private UserCardVip mCardVip;
    /**
     * 适配器
     */
    private MessageListAdapter mMsgAdapter = null;
    /**
     * 显示消息的列表
     */
    private XXListView mLvMsg;
    private List<Messages> mMsgDatas = new ArrayList<Messages>();
    private Type jsonType = new TypeToken<List<Messages>>() {
    }.getType();// 所属类别
    private Shop mShop;
    private Handler mHandler;
    private int mPage = 1;
    private boolean mFlagDate = false;

    public static VipChatFragment newInstance() {

        Bundle args = new Bundle();
        VipChatFragment fragment = new VipChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        ViewUtils.inject(this, v);
        Util.addLoginActivity(getActivity());
        init(v);
        if (savedInstanceState != null) {
            mMsgDatas = (List<Messages>) savedInstanceState.getSerializable(FRAG_INFO);
            String edtContent = savedInstanceState.getString(EDIT_INFO);
            if (!Util.isEmpty(edtContent)) {
                mEdtContent.setText(edtContent);
            }
            if (mMsgDatas.size() > 0) {
                if (mMsgAdapter == null) {
                    mMsgAdapter = new MessageListAdapter(getActivity(), mMsgDatas);
                    mLvMsg.setAdapter(mMsgAdapter);
                } else {
                    mMsgAdapter.notifyDataSetChanged();
                }
            }
        }
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMsgDatas.size() > 0) {
            outState.putSerializable(FRAG_INFO, (Serializable) mMsgDatas);
        }
        if (!Util.isEmpty(mEdtContent.getText().toString())) {
            outState.putString(EDIT_INFO, mEdtContent.getText().toString());
        }
    }

    /**
     * 初始化方法
     */
    private void init(View v) {
        mShop = DB.getObj(DB.Key.SHOP_INFO, Shop.class);
        //Log.d(TAG, "传过来的图片路径为a：：：" + Const.IMG_URLmShop.getLogoUrl());
        mHandler = new Handler();
        mFlagDate = true;
        mPage = 1;
        Intent intent = getActivity().getIntent();
        mCardVip = (UserCardVip) intent.getSerializableExtra(USER_OBJ);
        mEdtContent = (EditText) v.findViewById(R.id.edt_chatmessage);
        String isSuggest = intent.getStringExtra(MSUGGEST);
        mLvMsg = (XXListView) v.findViewById(R.id.lv_chat);
        mLvMsg.setPullRefreshEnable(true);
        mLvMsg.setPullLoadEnable(false);
        mLvMsg.setXXListViewListener(this, Const.PullRefresh.SHOP_VIP_CHAT);
        LinearLayout ivreturn = (LinearLayout) v.findViewById(R.id.layout_turn_in);
        ivreturn.setVisibility(View.VISIBLE);
        TextView tvHeandName = (TextView) v.findViewById(R.id.tv_mid_content);
        if (String.valueOf(Util.NUM_ONE).equals(isSuggest)) {//如果是建议反馈
            tvHeandName.setText(getResources().getString(R.string.huiquan_message));
            DB.saveInt(ShopConst.Massage.FEED_BACK, 0); // 消息数目清零
        } else {//是会员交流
            if (mCardVip != null) {
                if (!Util.isEmpty(mCardVip.getNickName())) {
                    String nickName = mCardVip.getNickName();
                    String nickTitle = "";
                    if (nickName.indexOf("（") > 0) {
                        nickTitle = nickName.substring(0, nickName.indexOf("（"));
                    } else if (nickName.indexOf("(") > 0) {
                        nickTitle = nickName.substring(0, nickName.indexOf("("));
                    } else {
                        nickTitle = mCardVip.getNickName();
                    }
                    String nickSimple = "";
                    if (nickTitle.length() > 10) {
                        nickSimple = nickTitle.substring(0, 10) + "...";
                    } else {
                        nickSimple = nickTitle;
                    }
                    tvHeandName.setText("给" + nickSimple + "留言");
                } else {
                    tvHeandName.setText("给会员留言");
                }
            } else {
                tvHeandName.setText("给会员留言");
            }
        }
        getMsg(); // 获取商家和某一会员的消息记录
    }

    /**
     * 获取商家和某一会员的消息记录
     */
    private void getMsg() {
        UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
        String shopCode = userToken.getShopCode();
        String tokenCode = userToken.getTokenCode();
        String staffCode = "";
        String[] params = {mCardVip.getUserCode(), shopCode, mPage + "", staffCode, tokenCode};
        new GetMsgTask(getActivity(), new GetMsgTask.Callback() {
            @Override
            public void getResult(JSONObject result) {
                mFlagDate = true;
                if (result == null || result.size() == 0) {
                    return;
                } else {
                    PageData page = new PageData(result, "msgList", jsonType);
                    mPage = page.getPage();
                    if (page.hasNextPage() == false) {
                        mFlagDate = false;
                        mLvMsg.stopRefresh();
                    } else {
                        mFlagDate = true;
                    }

                    JSONArray msgAr = (JSONArray) result.get("msgList");
                    for (int i = 0; i < msgAr.size(); i++) {
                        Messages messages = Util.json2Obj(msgAr.get(i).toString(), Messages.class);
                        if (messages.getFrom_where() == 1) { // 顾客
                            messages.setMsgflag(true);
                        } else { // 店家
                            messages.setUserCode(mCardVip.getUserCode());
                            messages.setMsgflag(false);
                        }
                        mMsgDatas.add(messages);
                    }
                    if (mMsgAdapter == null) {
                        mMsgAdapter = new MessageListAdapter(getActivity(), mMsgDatas);
                        mLvMsg.setAdapter(mMsgAdapter);
                    } else {
                        mMsgAdapter.notifyDataSetChanged();
                    }
                    if (page.getPage() == 1) {
                        mLvMsg.setSelection(mLvMsg.getCount() - 1);
                    } else {
                        mLvMsg.setSelection(0);
                    }
                }

            }
        }).execute(params);

        // 阅读消息
        new ReadMsgTask(getActivity(), new ReadMsgTask.Callback() {

            @Override
            public void getResult(JSONObject result) {
                String succ_code = ErrorCode.SUCC + "";
                String fail_code = ErrorCode.FAIL + "";
                String readMsg = null;
                if (result == null) {
                    readMsg = fail_code;
                } else {
                    String code = result.get("code").toString();
                    if (succ_code.equals(code)) {
                        DB.saveBoolean(ShopConst.Key.MSG_SEND, true);
                        readMsg = succ_code;
                    } else {
                        readMsg = fail_code;
                    }
                }
                // 保存添加成功
                DB.saveStr(MessageListAdapter.READ_MSG, readMsg);
            }
        }).execute(mCardVip.getUserCode());
    }

    /**
     * 返回事件
     *
     * @param view
     */
    @OnClick(R.id.layout_turn_in)
    private void ivReturnClick(View view) {
        getActivity().finish();
    }

    /**
     * 发送
     *
     * @param view 视图
     */
    @OnClick(R.id.btn_chatsend)
    private void btnSendClick(View view) {
        String sendMsg = mEdtContent.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        String time = sdf.format(new Date());
        Shop shop = DB.getObj(DB.Key.SHOP_INFO, Shop.class);
        if (Util.isEmpty(sendMsg)) {
            return;
        } else {
            //友盟统计
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("shopCode", shop.getShopCode());
            map.put("shopName", shop.getShopName());

            String[] params = {mCardVip.getUserCode(), sendMsg};
            mEdtContent.setText("");
            Messages msg = new Messages();
            msg.setMessage(sendMsg);
            msg.setLogoUrl(shop.getLogoUrl());
            msg.setUserCode(mCardVip.getUserCode());
            msg.setCreateTime(time);
            msg.setMsgflag(false);
            mMsgDatas.add(msg);
            if (mMsgAdapter == null) {
                mMsgAdapter = new MessageListAdapter(getActivity(), mMsgDatas);
                mLvMsg.setAdapter(mMsgAdapter);
            } else {
                mMsgAdapter.notifyDataSetChanged();
            }
            mLvMsg.setSelection(mLvMsg.getCount() - 1);

            new SendMsgTask(getActivity(), new SendMsgTask.Callback() {

                @Override
                public void getResult(JSONObject result) {
                    String succ_code = ErrorCode.SUCC + "";
                    String fail_code = ErrorCode.FAIL + "";
                    String sendMsg = null;
                    if (result == null) { // 发送失败
                        sendMsg = fail_code;
                    } else {
                        DB.saveBoolean(ShopConst.Key.MSG_SEND, true);
                        String code = result.get("code").toString();
                        if (succ_code.equals(code)) {
                            sendMsg = succ_code;
                        } else {
                            sendMsg = fail_code;
                        }
                    }
                    // 保存添加成功
                    DB.saveStr(MessageListAdapter.SEND_MSG, sendMsg);
                }
            }).execute(params);
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        if (mFlagDate) {
            mFlagDate = false;
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    // 平台活动
                    mPage++;
                    getMsg();
                    mLvMsg.stopRefresh();
                }
            }, 2000);
        } else {
            mLvMsg.stopRefresh();
        }

    }

    @Override
    public void onLoadMore() {

    }
}
