// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.cust.fragment;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.suanzi.baomi.base.Const;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.Messages;
import cn.suanzi.baomi.base.pojo.PageData;
import cn.suanzi.baomi.base.pojo.User;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.view.XXListView;
import cn.suanzi.baomi.base.view.XXListView.IXXListViewListener;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.adapter.MessageListAdapter;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.model.GetMsgTask;
import cn.suanzi.baomi.cust.model.GetUserInfo;
import cn.suanzi.baomi.cust.model.ReadMsgTask;
import cn.suanzi.baomi.cust.model.SendMsgTask;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 会员和管理员交流的界面
 * 
 * @author yanfang.li
 */
public class VipChatFragment extends Fragment implements IXXListViewListener {

	private final static String TAG = VipChatFragment.class.getSimpleName();
	/** 用于savedInstanceState的常量，当前fragment的index */
	public final static String FRAG_INFO = "frag_info";
	public final static String EDIT_INFO = "edit_info";
	/** 保存的对象 */
	public final static String MSG_OBJ = "messages";
	public static final String MSUGGEST = "suggest";
	public final static String MSG_FLAG = "msgFlag";
	/** 表示是自己发送的 */
	public final static String MY_FLAG = "myFlag";
	/** 接收的消息 */
	public final static String VIP_FLAG = "vipFlag";
	/** 消息内容 */
	private EditText mEdtContent;
	/** 用户对象 */
	private Messages mMessages;
	/** 适配器 */
	private MessageListAdapter mMsgAdapter = null;
	/** 显示消息的列表 */
	private XXListView mLvMsg;
	private List<Messages> mMsgDatas = new ArrayList<Messages>();
	private Type jsonType = new TypeToken<List<Messages>>() {
	}.getType();// 所属类别
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_chat, container, false);
		ViewUtils.inject(this, v);
		Util.addLoginActivity(getMyActivity());
		inti(v);
		if (savedInstanceState != null) {
			mMsgDatas = (List<Messages>) savedInstanceState.getSerializable(FRAG_INFO);
			String edtContent = savedInstanceState.getString(EDIT_INFO);
			if (!Util.isEmpty(edtContent)) {
				mEdtContent.setText(edtContent);
			}
			if (mMsgDatas.size() > 0) {
				if (mMsgAdapter == null) {
					mMsgAdapter = new MessageListAdapter(getMyActivity(), mMsgDatas);
					mLvMsg.setAdapter(mMsgAdapter);
				} else {
					mMsgAdapter.notifyDataSetChanged();
				}
			}
		}
		return v;
	}

	/**
	 * 保证activity不为空
	 * 
	 * @return activity
	 */
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}

	/**
	 * 初始化方法
	 */
	private void inti(View v) {
		Intent intent = getMyActivity().getIntent();
		mHandler = new Handler();
		mPage = 1;
		mFlagDate = true;
		mMessages = (Messages) intent.getSerializableExtra(MSG_OBJ);
		mEdtContent = (EditText) v.findViewById(R.id.edt_chatmessage);
		String isSuggest = intent.getStringExtra(MSUGGEST);
		ImageView ivAdd = (ImageView) v.findViewById(R.id.iv_add);
		ivAdd.setVisibility(View.GONE);
		mLvMsg = (XXListView) v.findViewById(R.id.lv_chat);
		mLvMsg.setPullLoadEnable(false);
		mLvMsg.setPullRefreshEnable(true);
		mLvMsg.setXXListViewListener(this, Const.PullRefresh.CUST_VIP_CHAT);
		TextView tvHeandName = (TextView) v.findViewById(R.id.tv_mid_content);
		if (String.valueOf(Util.NUM_ONE).equals(isSuggest)) {// 如果是建议反馈
			tvHeandName.setText(getResources().getString(R.string.huiquan_message));
		} else {// 是会员交流
			if (!Util.isEmpty(mMessages.getShopName())) {
				String shopName = mMessages.getShopName();
				String shopTitle = "";
				if (shopName.indexOf("（") > 0) {
					shopTitle = shopName.substring(0, shopName.indexOf("（"));
				} else if (shopName.indexOf("(") > 0) {
					shopTitle = shopName.substring(0, shopName.indexOf("("));
				} else {
					shopTitle = mMessages.getShopName();
				}
				String shopSimple = "";
				if (shopTitle.length() > 10) {
					shopSimple = shopTitle.substring(0, 10) + "...";
				} else {
					shopSimple = shopTitle;
				}
				tvHeandName.setText("给" + shopSimple + "留言");
			} else {
				tvHeandName.setText("给商家留言");
			}
		}
		getMsg(); // 获取商家和某一会员的消息记录
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
	 * 获取商家和某一会员的消息记录
	 */
	private void getMsg() {
		UserToken userToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		if (null == userToken) { return; }
		String tokenCode = userToken.getTokenCode();
		String userCode = userToken.getUserCode();
		String[] params = { userCode, mMessages.getShopCode(), mPage + "", tokenCode };
		new GetMsgTask(getMyActivity(), new GetMsgTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				mFlagDate = true;
				mLvMsg.stopRefresh();
				if (result == null || result.size() == 0) {
					return;
				} else {
					getReadMsg(); // 阅读消息
					PageData page = new PageData(result, "msgList", jsonType);
					mPage = page.getPage();
					if (page.hasNextPage() == false) {
						mFlagDate = false;
					} else {
						mFlagDate = true;
					}
					JSONArray msgAr = (JSONArray) result.get("msgList");
					for (int i = 0; i < msgAr.size(); i++) {
						Messages messages = Util.json2Obj(msgAr.get(i).toString(), Messages.class);
						if (messages.getFrom_where() == 0) {
							messages.setMsgflag(true);
						} else {
							messages.setMsgflag(false);
						}
						mMsgDatas.add(messages);
					}
					if (mMsgAdapter == null) {
						mMsgAdapter = new MessageListAdapter(getMyActivity(), mMsgDatas);
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

	}

	private void getReadMsg() {
		// 阅读消息
		new ReadMsgTask(getMyActivity(), new ReadMsgTask.Callback() {
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
						DB.saveBoolean(CustConst.Key.MSG_SEND, true);
						readMsg = succ_code;
					} else {
						readMsg = fail_code;
					}
				}
				// 保存添加成功
				DB.saveStr(MessageListAdapter.READ_MSG, readMsg);
			}
		}).execute(mMessages.getShopCode());
	}

	/**
	 * 返回事件
	 * 
	 * @param view
	 */
	@OnClick(R.id.iv_turn_in)
	private void ivReturnClick(View view) {
		Log.d(TAG, "回退");

		// 强制关闭软键盘
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mEdtContent.getWindowToken(), 0);

		getMyActivity().finish();
	}

	/**
	 * 发送
	 * 
	 * @param view
	 *            视图
	 */
	@OnClick(R.id.btn_chatsend)
	private void btnSendClick(View view) {
		final String sendMsgstr = mEdtContent.getText().toString();
		if (Util.isEmpty(sendMsgstr)) {
			return;
		} else {

			final User user = DB.getObj(DB.Key.CUST_USER, User.class);
			if (null == user) {
				GetUserInfo.getUserInfo(getMyActivity());
			}
			// 友盟统计
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("username", user.getRealName());
			map.put("mobileNbr", user.getMobileNbr());
			MobclickAgent.onEvent(getMyActivity(), "vipchat_message", map);

			String[] params = { mMessages.getShopCode(), sendMsgstr};
			mEdtContent.setText("");
			new SendMsgTask(getMyActivity(), new SendMsgTask.Callback() {
				@Override
				public void getResult(JSONObject result) {
					String succ_code = ErrorCode.SUCC + "";
					String fail_code = ErrorCode.FAIL + "";
					String sendMsg = null;
					if (result == null) {
						Util.showToastZH("留言失败");
						sendMsg = fail_code;
					} else {
						DB.saveBoolean(CustConst.Key.MSG_SEND, true);
						String code = result.get("code").toString();
						String dataTime = result.get("datetime").toString();
						if (succ_code.equals(code)) {
							Messages msg = new Messages();
							Log.d(TAG, "sendMsg:" + sendMsg);
							msg.setMessage(sendMsgstr);
							msg.setUserAvatar(user.getAvatarUrl());
							msg.setUserCode(user.getUserCode());
							msg.setCreateTime(dataTime);
							msg.setMsgflag(false);
							mMsgDatas.add(msg);
							if (mMsgAdapter == null) {
								mMsgAdapter = new MessageListAdapter(getMyActivity(), mMsgDatas);
								mLvMsg.setAdapter(mMsgAdapter);
							} else {
								mMsgAdapter.notifyDataSetChanged();
							}
							mLvMsg.setSelection(mLvMsg.getCount() - 1);
							sendMsg = succ_code;
						} else {
							sendMsg = fail_code;
							Util.showToastZH("留言失败");
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
		Log.d(TAG, "mFlagDate:" + mFlagDate);
		if (mFlagDate) {
			mFlagDate = false;
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// 平台活动
					mPage++;
					getMsg();
				}
			}, 2000);
		} else {
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					Util.showToastZH("没有更多消息了");
					// 平台活动
					mLvMsg.stopRefresh();
				}
			}, 500);
		}

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}

	/**
	 * 友盟统计
	 */
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(VipChatFragment.class.getSimpleName());
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(VipChatFragment.class.getSimpleName()); // 统计页面
	}
}
