// ---------------------------------------------------------
// @author   yanfang.li
// @version   1.0.0
// @createTime 2015.5.4
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package com.huift.hfq.shop.fragment;

import net.minidev.json.JSONObject;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.huift.hfq.shop.R;

import com.huift.hfq.base.Util;
import com.huift.hfq.base.pojo.UserCardVip;
import com.huift.hfq.shop.activity.VipChatActivity;
import com.huift.hfq.shop.model.GetVipInfoTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 每张会员卡的详细信息
 * @author yanfang.li
 */
public class CardDetailFragment extends Fragment{
	
	private final static String TAG = "CardDetailFragment";
	
	public static CardDetailFragment newInstance() {
		Bundle args = new Bundle();
		CardDetailFragment fragment = new CardDetailFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_card_detail, container, false);
	    ViewUtils.inject(this, v);
	    //出事化方法
	    init(v);
		return v;
	}
	
	/**
	 * 初始化方法
	 * @param v 视图
	 */
	private void init(View v) {
		Util.addLoginActivity(getActivity());
		//获取上一个Activity传来的方法
		Intent intent = getActivity().getIntent();
	    //String userCardCode = intent.getStringExtra(USER_CARD_CODE);
		//调用查询某个会员的异步任务类
	    //getVipInfo(v,userCardCode,tokenCode);
	}

	/**
	 * 调用查询某个会员的异步任务类
	 * @param view 视图 
	 * @param tokenCode 需要令牌认证
	 * @param userCardCode  会员卡用户编码
	 */
	private void getVipInfo(final View view, String userCardCode, String tokenCode) {
		new GetVipInfoTask(getActivity(), new GetVipInfoTask.Callback() {
			
			@Override
			public void getResult(JSONObject result) {
				// 获取控件
				ImageView ivCardHead = (ImageView) view.findViewById(R.id.iv_cardvip_head);// 头像
				TextView mTvNickName = (TextView) view.findViewById(R.id.tv_cardvip_dtl_nkn);// 昵称 
				TextView mTvCardNbr = (TextView) view.findViewById(R.id.tv_cardvip_dtl_id);// 卡号 
				TextView mTvPoint = (TextView) view.findViewById(R.id.tv_cardvip_dtl_point);// 积分
				TextView mTvPointOverdue = (TextView) view.findViewById(R.id.tv_cardvip_dtl_overdue);// 积分过期
				TextView mTvApplyTime = (TextView) view.findViewById(R.id.tv_cardvip_dtl_date);// 办卡时间
					
				UserCardVip card = Util.json2Obj(result.toString(), UserCardVip.class);
				// 给控件赋值
				Util.showImage(getActivity(), card.getAvatarUrl(), ivCardHead);
				mTvNickName.setText(card.getNickName());
			    mTvCardNbr.setText(card.getCardNbr());
			    mTvPoint.setText(card.getPoint());
			    mTvPointOverdue.setText(card.getToExpiredPoints());
			    mTvApplyTime.setText(card.getApplyTime());
			}

		}).execute(userCardCode,tokenCode);
		
	}
	
	/**
	 * 返回按钮的跳转事件 管理者和会员沟通界面
	 * 
	 * @param v 视图
	 */
	@OnClick({ R.id.iv_cardvip_dtl_rtn, R.id.iv_cardvip_dtl_msg })
	private void ivSkipClick(View v) {
		switch (v.getId()) {
		case R.id.iv_cardvip_dtl_rtn://返回
				getActivity().finish();
			break;
		case R.id.iv_cardvip_dtl_msg://会员与管理者交流点击事件
			startActivity(new Intent(getActivity(),VipChatActivity.class));
			break;

		default:
			break;
		}
	}
}
