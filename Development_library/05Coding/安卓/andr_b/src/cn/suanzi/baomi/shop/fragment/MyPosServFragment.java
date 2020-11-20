package cn.suanzi.baomi.shop.fragment;

import net.minidev.json.JSONObject;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.suanzi.baomi.base.ErrorCode;
import cn.suanzi.baomi.base.Util;
import cn.suanzi.baomi.base.data.DB;
import cn.suanzi.baomi.base.pojo.UserToken;
import cn.suanzi.baomi.shop.R;
import cn.suanzi.baomi.shop.ShopConst;
import cn.suanzi.baomi.shop.activity.HomeActivity;
import cn.suanzi.baomi.shop.activity.MyPosFaqActivity;
import cn.suanzi.baomi.shop.model.MyPosServTask;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * POS服务显示界面
 * 
 * @author qian.zhou
 */
public class MyPosServFragment extends Fragment {
	private static final String TAG = "MyPosServFragment";
	/** 申请POS机 **/
	private LinearLayout mLyApplyPos;
	/** 获得一个用户信息对象 **/
	private UserToken mUserToken;
	/** 商家编码 **/
	private String mShopCode;
	/** 用户登录后获得的令牌 **/
	private String mTokenCode;
	/** 服务类型 */
	private String mType;
	/** 服务说明 */
	private String mPosDescr;
	/***/
	private CheckBox ckMaterial, ckRepair, ckOther;
	/***/
	private EditText etPosDescr;
	/** 提交按钮 */
	private TextView mTvMsg;

	/**
	 * 需要传递参数时有利于解耦
	 */
	public static MyPosServFragment newInstance() {
		Bundle args = new Bundle();
		MyPosServFragment fragment = new MyPosServFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_mypos_serv, container, false);
		Util.hintKbTwo(getActivity());// 关闭软键盘
		init(v);
		Util.addLoginActivity(getActivity());
		ViewUtils.inject(this, v);
		return v;
	}

	// 初始化方法
	private void init(View v) {
		LinearLayout ivBack = (LinearLayout) v.findViewById(R.id.layout_turn_in);// 返回
		ivBack.setVisibility(View.VISIBLE);// 显示
		ivBack.setBackgroundResource(R.drawable.backup);
		TextView tvTitle = (TextView) v.findViewById(R.id.tv_mid_content);
		tvTitle.setText(getResources().getString(R.string.ser_type));
		mTvMsg = (TextView) v.findViewById(R.id.tv_msg);
		mTvMsg.setText(getResources().getString(R.string.submit_message));

		// 获得一个用户信息对象
		mUserToken = DB.getObj(DB.Key.CUST_USER_TOKEN, UserToken.class);
		mShopCode = mUserToken.getShopCode();// 商家编码
		mTokenCode = mUserToken.getTokenCode();// 用户登录后获得的令牌
		ckMaterial = (CheckBox) v.findViewById(R.id.ck_pos_material);// 耗材配送
		ckRepair = (CheckBox) v.findViewById(R.id.ck_pos_repair);// 故障报修
		ckOther = (CheckBox) v.findViewById(R.id.ck_pos_other);// 其他
		etPosDescr = (EditText) v.findViewById(R.id.et_service_descr);// 服务说明
		mLyApplyPos = (LinearLayout) v.findViewById(R.id.ly_apply_pos);// 申请pos机
		mTvMsg.setOnClickListener(submitListener);// 提交
		mLyApplyPos.setOnClickListener(applyPosListener);// 提交
	}

	// 点击申请pos服务
	OnClickListener applyPosListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mType = String.valueOf(Util.NUM_ONE);// 类型为1
			Util.addToast(R.string.service_pos);
		}
	};

	// 点击提交，提交所有数据
	OnClickListener submitListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mPosDescr = etPosDescr.getText().toString();
			if (ckMaterial.isChecked() == true) {// 如果耗材配送被选中
				mType = String.valueOf(Util.NUM_TWO);// 类型为2
			} else if (ckRepair.isChecked() == true) {// 故障报修
				mType = String.valueOf(Util.NUM_THIRD);// 类型为3
			} else if (ckOther.isChecked() == true) {// 其他
				mType = String.valueOf(Util.NUM_FOUR);// 类型为4
			}
			switch (v.getId()) {
			case R.id.tv_msg:
				if ("".equals(etPosDescr.getText().toString())) {// 服务说明
					Util.getContentValidate(R.string.service_description_null);
					break;
				}
				/*
				 * if((!ckMaterial.isChecked()) && (!ckRepair.isChecked()) &&
				 * (!ckOther.isChecked())){ Util.addToast(getActivity(),
				 * getResources().getString(R.string.check_sertype)); break; }
				 */
			default:
				mTvMsg.setEnabled(false);
				new MyPosServTask(getActivity(), new MyPosServTask.Callback() {
					@Override
					public void getResult(JSONObject result) {
						if (result == null) {
							mTvMsg.setEnabled(true);
						} else {
							mTvMsg.setEnabled(true);
							if (String.valueOf(ErrorCode.SUCC).equals(result.get("code").toString())) {
								Util.addToast(R.string.apply_success);
							}
							Intent intent = new Intent(getActivity(), HomeActivity.class);
							intent.putExtra(ShopConst.FRAG_ID, Util.NUM_THIRD);
							startActivity(intent);
							getActivity().finish();
						}
					}
				}).execute(mShopCode, mType, mPosDescr, mTokenCode);
				break;
			}
		}
	};

	/**
	 * 点击事件
	 */
	@OnClick({ R.id.lv_card_problem, R.id.layout_turn_in, R.id.ly_mypos_serv_phone })
	private void ivTurnTo(View v) {
		switch (v.getId()) {
		case R.id.lv_card_problem:
			startActivity(new Intent(getActivity(), MyPosFaqActivity.class));
			break;
		case R.id.layout_turn_in:
			getActivity().finish();
			break;
		case R.id.ly_mypos_serv_phone:
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(getResources().getString(R.string.tel)));
			getActivity().startActivity(intent);
			break;
		default:
			break;
		}
	}
}
