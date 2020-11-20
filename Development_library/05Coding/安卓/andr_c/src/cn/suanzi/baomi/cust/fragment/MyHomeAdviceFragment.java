package cn.suanzi.baomi.cust.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.Message;
import cn.suanzi.baomi.base.pojo.User;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.adapter.MessageListAdapter;
import cn.suanzi.baomi.cust.adapter.SuggestListAdapter;
import cn.suanzi.baomi.cust.model.SysSuggestTask;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 建议
 * @author qian.zhou
 *
 */
public class MyHomeAdviceFragment extends Fragment {
	private static final String TAG = MyHomeAdviceFragment.class.getSimpleName();
	/**获得一个用户信息对象**/
	private UserToken mUserToken;
	/**用户登录后获得的令牌 **/
	private String mTokenCode;
	/**反馈者的编码**/
	private String mUserCode;
	private EditText mEditSuggest;
	/**提交建议**/
	private Button mBtnSave;
	/** 显示消息的列表*/
	private ListView mLvMsg;
	private List<Message> mMsgDatas = new ArrayList<Message>();
	private SuggestListAdapter mSugAdapter = null;
	private User mUser;

	public static MyHomeAdviceFragment newInstance() {
		Bundle args = new Bundle();
		MyHomeAdviceFragment fragment = new MyHomeAdviceFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_sys_suggest, container, false);
		ViewUtils.inject(this, v);
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
	
	//初始化方法
	private void init(View v) {
		ImageView ivBack =  (ImageView) v.findViewById(R.id.iv_turn_in);//返回
		TextView tvTitle = (TextView) v.findViewById(R.id.tv_mid_content);
		tvTitle.setText(getResources().getString(R.string.complaint_advice));
		ImageView ivAdd = (ImageView) v.findViewById(R.id.iv_add);
		ivAdd.setVisibility(View.GONE);
		mLvMsg = (ListView) v.findViewById(R.id.lv_chat);
		mUser = DB.getObj(DB.Key.CUST_USER, User.class);
		// 获得一个用户信息对象
	    mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
	    mUserCode = mUserToken.getUserCode();// 用户编码
	    mTokenCode = mUserToken.getTokenCode();// 用户登录后获得的令牌
	    mEditSuggest = (EditText) v.findViewById(R.id.edt_chatmessage);
	}
	
	/**
	 * 获取用户和平台的反馈详情
	 *//*
	public void getUserFeedBack(){
		new GetUserFeedbackTask(getActivity(), new GetUserFeedbackTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
			final  String hqCode = (String) result.get("hqCode");//惠圈编码
			 JSONArray jsonArray = (JSONArray) result.get("feedback");
			 for (int i = 0; i < jsonArray.size(); i++) {
				 Message message = Util.json2Obj(jsonArray.get(i).toString(), Message.class);
				if (hqCode.equals(message.getCreatorCode())) {
					 message.setMsgflag(true);//平台回复消息
				} else {
					message.setMsgflag(false);//用户发消息
					message.setAvatarUrl(mUser.getAvatarUrl());
				}
				mMsgDatas.add(message);
			 }
			 if (mSugAdapter == null) {
				 mSugAdapter = new SuggestListAdapter(getActivity(), mMsgDatas);
					mLvMsg.setAdapter(mSugAdapter);
				} else {
					mSugAdapter.notifyDataSetChanged();
				}
				mLvMsg.setSelection(mLvMsg.getCount() - 1);
			}
		}).execute(mUserCode, mTokenCode);
		
		//设置反馈为已读状态
		new ReadFeedbackTask(getActivity(), new ReadFeedbackTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				
			}
		}).execute();
	}*/
	
	/**
	 * 发送
	 * @param view 视图
	 */
	@OnClick(R.id.btn_chatsend)
	private void btnSendClick(View view) {
		String sendMsg = mEditSuggest.getText().toString();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		String time = sdf.format(new Date());
		if (Util.isEmpty(sendMsg)) {
			return ;
		} else {
			String []params = {mUserCode, sendMsg, mTokenCode};
			mEditSuggest.setText("");
			Message msg = new Message();
			msg.setContent(sendMsg);
			msg.setAvatarUrl(mUser.getAvatarUrl());
			msg.setCreateTime(time);
			msg.setMsgflag(false);
			mMsgDatas.add(msg);
			if (mSugAdapter == null) {
				mSugAdapter = new SuggestListAdapter(getMyActivity(), mMsgDatas);
				mLvMsg.setAdapter(mSugAdapter);
			} else {
				mSugAdapter.notifyDataSetChanged();
			}
			mLvMsg.setSelection(mLvMsg.getCount() - 1);
			new SysSuggestTask(getMyActivity(), new SysSuggestTask.Callback() {
				@Override
				public void getResult(JSONObject result) {
					String succ_code = ErrorCode.SUCC+"";
					String fail_code = ErrorCode.FAIL+"";
					String sendMsg = null;
					if (result == null) { 
						sendMsg = fail_code;
					} else {
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
	
	/**返回上一级**/
	@OnClick(R.id.iv_turn_in)
	private void ivbackUpClick(View v) {
		getMyActivity().finish() ;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(MyHomeAdviceFragment.class.getSimpleName()); 
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(MyHomeAdviceFragment.class.getSimpleName()); //统计页面
	}
}
