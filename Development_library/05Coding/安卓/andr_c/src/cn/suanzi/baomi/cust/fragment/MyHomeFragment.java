//---------------------------------------------------------
//@author    Zhonghui.Dong
//@version   1.0.0
//@createTime 2015.6.2
//@copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
//---------------------------------------------------------
package cn.suanzi.baomi.cust.fragment;

import java.io.Serializable;

import net.minidev.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.suanzi.baomi.base.Const;
import cn.suanzi.baomi.base.SzApplication;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.Messages;
import cn.suanzi.baomi.base.pojo.User;
import cn.suanzi.baomi.base.utils.AppUtils;
import cn.suanzi.baomi.base.utils.Calculate;
import cn.suanzi.baomi.base.utils.GlideCircleTransform;
import cn.suanzi.baomi.base.utils.ImageDownloadCallback;
import cn.suanzi.baomi.cust.R;
import cn.suanzi.baomi.cust.activity.ActThemeDetailActivity;
import cn.suanzi.baomi.cust.activity.MessageActivity;
import cn.suanzi.baomi.cust.activity.MyHomeAboutActivity;
import cn.suanzi.baomi.cust.activity.MyHomeBankListActivity;
import cn.suanzi.baomi.cust.activity.MyHomeSettingActivity;
import cn.suanzi.baomi.cust.activity.MyInfoMationActivity;
import cn.suanzi.baomi.cust.activity.MyOrderManagerActivity;
import cn.suanzi.baomi.cust.activity.MyPayTwoCodeActivity;
import cn.suanzi.baomi.cust.activity.MyPromotionActivity;
import cn.suanzi.baomi.cust.activity.MyRecommonedActivity;
import cn.suanzi.baomi.cust.activity.MyRedBagActivity;
import cn.suanzi.baomi.cust.activity.OriginalPwdActivity;
import cn.suanzi.baomi.cust.activity.SetPayPwdActivity;
import cn.suanzi.baomi.cust.activity.VipChatActivity;
import cn.suanzi.baomi.cust.application.CustConst;
import cn.suanzi.baomi.cust.model.CountAllTypeMsgTask;
import cn.suanzi.baomi.cust.model.GetUserInfoTask;
import cn.suanzi.baomi.cust.model.IfShowRecommendTask;
import cn.suanzi.baomi.cust.model.MyInfoMationUppTask;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的惠圈
 * @author qian.zhou
 */
public class MyHomeFragment extends BaseFragment {
	private static final String TAG = MyHomeFragment.class.getSimpleName();
	/** startActivityForResult()的requestCode: 修改个人信息 */
	public static final int INTENT_REQ_UPP_INFO = Util.NUM_ONE;
	public static final String MSUGGEST = String.valueOf(Util.NUM_ONE);
	/** intent跳转的时的请求参数 */
	private static final int REQUEST_CODE = 100;
	/** 返回成功 */
	public static final int REQUEST_SUCC = 101;
	/** 消息返回的对象 */
	public static final String MESSAGE_OBJ = "messageHomeObj";
	private TextView mTvPhone, mTvNickNmae;
	private ImageView mIvLogo;
	private String mImg;
	private User mUser;
	private boolean mUppFlag = false;
	/** 消息 */
	private Messages mMessages;
	/** 消息数目 */
	private TextView mTvAllPrompt;
	/** 对商家建议的*/
	private TextView mTvSuggest;
	/** 正在加载的进度条*/
	private ProgressBar mProgView;
	private View mView;
	/**没有设置支付密码的时候的提示*/
	private TextView mSetPayPwdMentioned;
	/** 推荐得好礼*/
	private LinearLayout mLayoutMyRecommend;
	/**标志是否是第一次显示该Fragment 默认为true*/
	private boolean isShowFragmentFirst = true;
	/** 用户头像*/
	private Bitmap mHeadBitmap;
	
	public static MyHomeFragment newInstance() {
		Bundle args = new Bundle();
		MyHomeFragment fragment = new MyHomeFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_myhome, container, false);
		ViewUtils.inject(this, mView);
		init(mView);
		SzApplication.setCurrActivity(getMyActivity());
		Util.addRecommonedActivity(getMyActivity());
		return mView;
	}
	
	@Override
	public void viewVisible() {
		super.viewVisible();
	}
	
	// 初始化方法
	private void init(View v) {
		mTvAllPrompt = (TextView) v.findViewById(R.id.tv_allprompt);
		mTvSuggest = (TextView) v.findViewById(R.id.tv_suggest);
		ImageView ivBack = (ImageView) v.findViewById(R.id.iv_turn_in);
		ivBack.setVisibility(View.GONE);
		TextView tvContent = (TextView) v.findViewById(R.id.tv_mid_content);
		tvContent.setText(getResources().getString(R.string.my_huiquan));
		ImageView ivEdit = (ImageView) v.findViewById(R.id.iv_share_all);
		ivEdit.setBackgroundResource(R.drawable.edit_add);
		mTvPhone = (TextView) v.findViewById(R.id.tv_phonenum);// 手机号
		mTvNickNmae = (TextView) v.findViewById(R.id.tv_nickname);// 昵称
		mIvLogo = (ImageView) v.findViewById(R.id.iv_mylogo);// 头像
		mProgView = (ProgressBar) v.findViewById(R.id.prog_nodata);
		mSetPayPwdMentioned = (TextView) v.findViewById(R.id.tv_set_paypwd);
		mLayoutMyRecommend = (LinearLayout) v.findViewById(R.id.layout_myRecommend);
		//新人注册
		ImageView ivNewRegister = (ImageView) getActivity().findViewById(R.id.tv_new_register);
		ivNewRegister.setVisibility(View.GONE);
		//设置数据
		initData();     
		/** 推荐得好礼*/
		ifShowRecommend();
	}
	
	private void ifShowRecommend () {
		new IfShowRecommendTask(getMyActivity(), new IfShowRecommendTask.Callback() {
			
			@Override
			public void getResult(int retCode) {
				switch (retCode) {
				case 1: // 显示模块
					mLayoutMyRecommend.setVisibility(View.VISIBLE);
					break;
				default: // 其他的不显示模块
					mLayoutMyRecommend.setVisibility(View.GONE);
					break;
				}
			}
		}).execute();
	}
	
	/**
	 * 设置数据
	 */
	public void initData(){
		// 检查网络
		if (Util.isNetworkOpen(getMyActivity())) {
			getUserInfo(mView);
		} else {
			Util.getToastBottom(getMyActivity(), "请连接网络");
			User user = DB.getObj(DB.Key.CUST_USER, User.class);
			if (user != null) {
				mTvPhone.setText(user.getMobileNbr());
				mTvNickNmae.setText(user.getNickName());
				mImg = Const.IMG_URL + user.getAvatarUrl();
				Log.d(TAG, "未联网获取的图片路径为：：" + mImg);
				Glide.with(getMyActivity()).load(mImg).centerCrop().transform(new GlideCircleTransform(getMyActivity())).into(mIvLogo);
				getUserHeadBitMap(mImg); // 生成用户头像的bitMap
			}
		}
		// 获得消息数目
		countAllTypeMsg();
	}
	
	/**
	 * 得到用户头像的bitMap
	 */
	private void getUserHeadBitMap(String url) {
		Util.getLocalOrNetBitmap(url, new ImageDownloadCallback() {
			@Override
			public void success(final Bitmap bitmap) {
				mHeadBitmap = bitmap;
			}
			@Override
			public void fail() {
				mHeadBitmap = null;
			}
		});
	}
	
	@Override
	public void onActivityResult(int reqCode, int respCode, Intent intent) {
		if (reqCode == INTENT_REQ_UPP_INFO) {// 如果是修改
			if (respCode == MyInfoMationActivity.INTENT_RESP_SAVED) {
				// TODO　刷新用户列表
				User uppUser = (User) intent.getSerializableExtra(MyInfoMationUppTask.USER);
				DB.saveObj(DB.Key.CUST_USER, uppUser);
				String avatarUrl = uppUser.getAvatarUrl();
				mUser.setMobileNbr(uppUser.getMobileNbr());
				mUser.setNickName(uppUser.getNickName());
				mUser.setRealName(uppUser.getRealName());
				mUser.setSex(uppUser.getSex());
				mUser.setCity(uppUser.getCity());
				mUser.setSignature(uppUser.getSignature());
				mUser.setSignature(uppUser.getSignature());
				mUser.setAvatarUrl(avatarUrl);
				Log.d(TAG, "修改过后的值为：：：：：：：" + mUser.getNickName());
				mTvPhone.setText(mUser.getMobileNbr());
				mTvNickNmae.setText(mUser.getNickName());
				if ("".equals(avatarUrl) || "null".equals(avatarUrl)) {
					return;             
				}
				mImg = Const.IMG_URL + avatarUrl;
				Glide.with(getMyActivity()).load(mImg).centerCrop().transform(new GlideCircleTransform(getMyActivity())).into(mIvLogo);
				getUserHeadBitMap(mImg); // 生成用户头像的bitMap
			} else {
				// do nothing
			}
		} else if (reqCode == REQUEST_CODE) {
			if (respCode == REQUEST_SUCC) {
				mMessages = (Messages) intent.getSerializableExtra(MESSAGE_OBJ);
				Log.d(TAG, "mMessages=" + mMessages.getCard() + ",,," + mMessages.getCommunication());
			}
		}
	}

	/**
	 * 判断activity不为空
	 * @return
	 */
	private Activity getMyActivity() {
		Activity act = getActivity();
		if (act == null) {
			act = AppUtils.getActivity();
		}
		return act;
	}
	
	/**
	 * 检测网络
	 * 
	 * @param flag
	 *            true 是有网络 false 是没有网络
	 * @param netType
	 *            1 是有网络 2 是没有网络
	 */
	public void netRecerve(boolean flag) {
		if (flag && null != mUser) {
			// TODO  有网 
			mProgView.setVisibility(View.VISIBLE);
			
		} else {
			mProgView.setVisibility(View.GONE);
					// TODO  没有网
		}
	}


	/**
	 * 查询个人信息
	 */
	public void getUserInfo(final View view) {
		Activity act = getMyActivity();
		if (act == null) {
			return;
		}
		new GetUserInfoTask(act, new GetUserInfoTask.Callback() {
			@Override
			public void getResult(JSONObject object) {
				if (object == null) {
					return;
				} else {
					try {
						mUser = Util.json2Obj(object.toString(), User.class);  
						ImageView ivShoplogo = (ImageView) view.findViewById(R.id.iv_mylogo);
						if (mUser != null) {
							
							ivShoplogo.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									Intent intent = new Intent(getMyActivity(), MyInfoMationActivity.class);
									intent.putExtra(MyInfoMationFragment.newInstance().USER_OBJ, mUser);
									startActivityForResult(intent, INTENT_REQ_UPP_INFO);
								}
							});
						} else {
							Log.d(TAG, "user对象是空的");
						}
						DB.saveObj(DB.Key.CUST_USER, mUser);
						//是否设置过支付密码
						if(String.valueOf(Util.NUM_ZERO).equals(mUser.getIsUserSetPayPwd())){ //0----没有设置
							mSetPayPwdMentioned.setVisibility(View.VISIBLE);
						}else if(String.valueOf(Util.NUM_ONE).equals(mUser.getIsUserSetPayPwd())){ //1----设置了
							mSetPayPwdMentioned.setVisibility(View.GONE);
						}
						mProgView.setVisibility(View.GONE);
						// 赋值
						mTvPhone.setText(mUser.getMobileNbr());
						mTvNickNmae.setText(mUser.getNickName());
						String avatarUrl = mUser.getAvatarUrl();
						if ("".equals(avatarUrl) || "null".equals(avatarUrl)) {
							return;
						}
						mImg = Const.IMG_URL + avatarUrl;
						Glide.with(getMyActivity()).load(mImg).centerCrop().transform(new GlideCircleTransform(getMyActivity())).into(mIvLogo);
						getUserHeadBitMap(mImg);// 获取用户头像的bitMap
					} catch (Exception e) {
						e.printStackTrace();
					}  
				}
			}
		}).execute();
	}

	/**
	 * 点击我的工行卡到工行列表
	 */
	@OnClick({ R.id.layout_message, R.id.layout_suggest, R.id.layout_about, R.id.layout_regbag,
			R.id.layout_set, R.id.iv_share_all, R.id.layout_update_pwd, R.id.ly_huibi, R.id.layout_myorder,
			R.id.layout_regact, R.id.layout_card,R.id.layout_myRecommend,R.id.layout_noIndenCode_pay,R.id.ly_payTwoCode})
	private void lineBankClick(View v) {
		Intent intent = null;
		isShowFragmentFirst = false;
		switch (v.getId()) {
		case R.id.layout_card:
			intent = new Intent(getMyActivity(), MyHomeBankListActivity.class);
			intent.putExtra("title", Util.getString(R.string.myhome_bank));
			startActivity(intent);
			break;
		case R.id.ly_payTwoCode: // 付款二维码
			intent = new Intent(getMyActivity(), MyPayTwoCodeActivity.class);
			Bundle bundle = new Bundle();
			bundle.putParcelable(MyPayTwoCodeFragment.USER_HEAD, mHeadBitmap);
			bundle.putSerializable(MyPayTwoCodeFragment.USER, (Serializable)mUser);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.layout_message: {
			intent = new Intent(getMyActivity(), MessageActivity.class);
			if (null != mMessages) {
				intent.putExtra(MessageActivity.MESSAGE_OBJ, mMessages);
			}
			startActivityForResult(intent, REQUEST_CODE);
			mTvAllPrompt.setVisibility(View.GONE);
			Activity act = getMyActivity();
			if (act == null) {
				return;
			}
			MobclickAgent.onEvent(act, "myhome_message"); // TODO
			break;
		}
		case R.id.layout_suggest: {
			// 检查网络
			if (Util.isNetworkOpen(getMyActivity())) {
				Intent adviceIntent = new Intent(getMyActivity(), VipChatActivity.class);
				Messages messages = new Messages();
				messages.setShopCode(Const.HQ_CODE);
				adviceIntent.putExtra(VipChatFragment.MSG_OBJ, messages);
				adviceIntent.putExtra(VipChatFragment.MSUGGEST, MSUGGEST);
				mTvSuggest.setVisibility(View.GONE);
				startActivity(adviceIntent);
				Activity act = getMyActivity();
				if (act == null) {
					return;
				}
				// 友盟统计
				MobclickAgent.onEvent(act, "suggest_count");
			} else {
				Util.getToastBottom(getMyActivity(), "请连接网络");
			}
			break;
		}

		case R.id.layout_set:
			intent = new Intent(getMyActivity(), MyHomeSettingActivity.class);
			intent.putExtra("title", getMyActivity().getResources().getString(R.string.myhome_set));
			startActivity(intent);
			break;
		case R.id.layout_about:
			intent = new Intent(getMyActivity(), MyHomeAboutActivity.class);
			intent.putExtra("title", getMyActivity().getResources().getString(R.string.myhome_about));
			startActivity(intent);
			break;
		case R.id.iv_share_all:
			intent = new Intent(getMyActivity(), MyInfoMationActivity.class);
			intent.putExtra(MyInfoMationFragment.newInstance().USER_OBJ, mUser);
			startActivityForResult(intent, INTENT_REQ_UPP_INFO);
			break;
		case R.id.layout_regbag: {
			Activity act = getMyActivity();
			if (act == null) {
				return;
			}
			// 红包
			intent = new Intent(getMyActivity(), MyRedBagActivity.class);
			startActivity(intent);
			MobclickAgent.onEvent(act, "myhome_regbag");
			break;
		}
		case R.id.layout_update_pwd:// 修改密码
			startActivity(new Intent(getMyActivity(), OriginalPwdActivity.class));
			break;
		case R.id.ly_huibi:// 惠币
			String type = CustConst.HactTheme.HUIBI_INTRO;
			intent = new Intent(getMyActivity(), ActThemeDetailActivity.class);
			intent.putExtra(ActThemeDetailActivity.TYPE, type);
			startActivity(intent);
			break;
		case R.id.layout_myorder:// 我的订单
			startActivity(new Intent(getMyActivity(), MyOrderManagerActivity.class));
			break;
		case R.id.layout_regact:// 活动

			//Intent actIntent = new Intent(getMyActivity(), ActMyContentActivity.class);
			Intent actIntent = new Intent(getActivity(), MyPromotionActivity.class);
			startActivity(actIntent);

//			Util.showToastZH("尽请期待");
//			startActivity(new Intent(getActivity(), BuyPromotionActivity.class));
			
			break;
		case R.id.layout_myRecommend:// 推荐
			intent = new Intent(getMyActivity(), MyRecommonedActivity.class);
			startActivity(intent);
			break;
		case R.id.layout_noIndenCode_pay://设置支付密码
			startActivity(new Intent(getMyActivity(), SetPayPwdActivity.class));
			break;
		}
	}

	/**
	 * 得到所有消息
	 */
	private void countAllTypeMsg() {
		new CountAllTypeMsgTask(getMyActivity(), new CountAllTypeMsgTask.Callback() {
			@Override
			public void getResult(JSONObject result) {
				if (null == result) {
					return;
				}           
				mMessages = Util.json2Obj(result.toString(), Messages.class);
				int countFirst = (int) Calculate.add(mMessages.getCommunication(), mMessages.getShop());
				int sumCount = (int) Calculate.add(countFirst,  mMessages.getCoupon());
				int feekCount = mMessages.getFeedback();
				Log.d(TAG, " feekCount >>> " +feekCount);
			    setMessageCount(feekCount, mTvSuggest);
			    setMessageCount(sumCount, mTvAllPrompt);
			}
		}).execute();
	}
	
	/**
	 * 设置消息数目
	 * @param msgCount 消息数目
	 * @param textView 装消息的空间
	 */
	private void setMessageCount (int msgCount , TextView textView) {
		if (msgCount > 0) {
			textView.setVisibility(View.VISIBLE);
			textView.setText(msgCount > 99 ? "99+" : msgCount + "");
		} else {
			textView.setVisibility(View.GONE);
		}
	}

	/**
	 * 友盟统计
	 */
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(MyHomeFragment.class.getSimpleName());
	}

	@Override
	public void onResume() {
		//当Fragment重新显示  刷新数据
		if(!isShowFragmentFirst){
			Log.d(TAG, "重新回来");
			initData();
		}
		super.onResume();
		Activity act = getMyActivity();
		if (act == null) {
			return;
		}
		mUppFlag = DB.getBoolean(CustConst.Key.UPP_USERINFO);
		Log.d(TAG, "flag11的状态为：：："  + mUppFlag);
		if (mUppFlag) {
			DB.saveBoolean(CustConst.Key.UPP_USERINFO,false);
			getUserInfo(mView);
			Log.d(TAG, "flag22的状态为：：："  + mUppFlag);
		}
		MobclickAgent.onPageStart(MyHomeFragment.class.getSimpleName()); // 统计页面
	}
	
}
